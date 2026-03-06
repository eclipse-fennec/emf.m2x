/*
 * ******************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 * ******************************************************************
 */
package org.eclipse.fennec.m2x.m2t.engine;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tGenerationStrategy;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tEvalEnvironment;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tEvaluator;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tModuleLinker;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tProtectedAreaMerger;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tStandardLibrary;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tWriterStack;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.m2t.parser.M2tParserSupport;
import org.eclipse.fennec.m2x.m2t.parser.M2tWhitespaceNormalizer;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;

/**
 * Plain Java implementation of the {@link M2tEngine} facade.
 *
 * <p>This class has no OSGi dependencies and can be instantiated directly:
 * <pre>
 * OclConfiguration oclConfig = OclConfiguration.builder(parser).build();
 * M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
 * M2tEngine engine = new M2tEngineImpl(config);
 *
 * Module module = engine.parse(source, "myTemplate");
 * M2tResult result = engine.execute(module, M2tContext.of(inputElement));
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tEngineImpl implements M2tEngine {

	private final OclEngineImpl oclEngine;
	private final M2tConfiguration config;
	private final M2tParserSupport parserSupport;

	/** Cache of parse results keyed by module identity, for auto-linking. */
	private final Map<Module, M2tParseResult> parseResultCache =
			Collections.synchronizedMap(new IdentityHashMap<>());

	/** Tracks which modules have been linked to avoid re-linking. */
	private final Set<Module> linkedModules =
			Collections.synchronizedSet(Collections.newSetFromMap(new IdentityHashMap<>()));

	/** Tracks which modules have been whitespace-normalized. */
	private final Set<Module> normalizedModules =
			Collections.synchronizedSet(Collections.newSetFromMap(new IdentityHashMap<>()));

	/** Indentation map for standalone template invocations (§8.4). */
	private final Map<TemplateInvocation, String> globalIndentationMap =
			Collections.synchronizedMap(new IdentityHashMap<>());

	/**
	 * Creates a new engine from the given configuration.
	 *
	 * @param config the engine configuration, must not be {@code null}
	 */
	public M2tEngineImpl(M2tConfiguration config) {
		this.config = Objects.requireNonNull(config, "config must not be null");
		// Augment OCL config with MOFM2T standard library (§8.3)
		OclConfiguration base = config.oclConfiguration();
		OclConfiguration.Builder oclBuilder = OclConfiguration.builder(base.parser())
				.customOperationsEnabled(true)
				.nullHandling(base.nullHandling())
				.errorRecovery(base.errorRecovery())
				.maxDepth(base.maxDepth())
				.maxCollectionSize(base.maxCollectionSize())
				.maxClosureIterations(base.maxClosureIterations())
				.maxRegexLength(base.maxRegexLength())
				.operationProviders(base.operationProviders())
				.addOperationProvider(new M2tStandardLibrary());
		if (base.timeoutMs() > 0) {
			oclBuilder.timeoutMs(base.timeoutMs());
		}
		if (base.expressionCache() != null) {
			oclBuilder.expressionCache(base.expressionCache());
		}
		this.oclEngine = new OclEngineImpl(oclBuilder.build());
		this.parserSupport = new M2tParserSupport();
	}

	// --- Parsing ---

	@Override
	public Module parse(URI moduleUri) throws M2tParseException {
		Objects.requireNonNull(moduleUri, "moduleUri must not be null");
		try {
			String source = Files.readString(Path.of(moduleUri));
			String unitName = Path.of(moduleUri).getFileName().toString();
			return parse(source, unitName);
		} catch (IOException e) {
			throw new M2tParseException("Failed to read module: " + moduleUri, e, List.of());
		}
	}

	@Override
	public Module parse(String source, String unitName) throws M2tParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		M2tParseResult result = parserSupport.buildModuleWithPending(source, unitName);
		parseResultCache.put(result.module(), result);
		return result.module();
	}

	// --- Execution ---

	@Override
	public List<String> link(Module... modules) {
		Objects.requireNonNull(modules, "modules must not be null");
		List<M2tParseResult> results = new ArrayList<>();
		for (Module m : modules) {
			Objects.requireNonNull(m, "module element must not be null");
			M2tParseResult pr = parseResultCache.get(m);
			if (pr != null) {
				results.add(pr);
			}
		}
		if (results.isEmpty()) {
			return List.of();
		}
		M2tModuleLinker linker = new M2tModuleLinker();
		List<String> warnings = linker.link(results);
		for (Module m : modules) {
			linkedModules.add(m);
		}
		return warnings;
	}

	@Override
	public M2tResult execute(Module module, M2tContext context) {
		Objects.requireNonNull(module, "module must not be null");
		Objects.requireNonNull(context, "context must not be null");

		// Auto-link if not yet linked
		if (!linkedModules.contains(module)) {
			link(module);
		}

		// Apply MOFM2T §8.4 whitespace normalization after linking (needs resolved invocations)
		WhitespaceMode wsMode = config.whitespaceMode();
		if (wsMode != WhitespaceMode.NONE && normalizedModules.add(module)) {
			M2tWhitespaceNormalizer normalizer = new M2tWhitespaceNormalizer(wsMode);
			globalIndentationMap.putAll(normalizer.normalize(module));
		}

		M2tEvalEnvironment env = M2tEvalEnvironment.root(context);
		M2tWriterStack writers = new M2tWriterStack(config.maxOutputSize());
		M2tEvaluator evaluator = new M2tEvaluator(oclEngine, env, writers, module,
				getAllLinkedModules(module), globalIndentationMap, config.maxDiagnostics(),
				config.maxTemplateDepth(), config.maxForIterations(), config.maxCrossProductSize(),
				config.protectedAreaEnabled());

		Template main = findMainTemplate(module);
		if (main == null) {
			throw new IllegalStateException("Module '" + module.getName()
					+ "' has no [template public main(...)] entry point");
		}

		List<EObject> inputElements = context.inputElements();
		evaluator.execute(main, inputElements);

		// Check output size limit (T-7)
		if (writers.isOutputLimitExceeded()) {
			evaluator.addOutputLimitError(config.maxOutputSize());
		}

		// Collect generated files and unique IDs
		Map<String, String> generatedFiles = writers.getGeneratedFiles();
		Map<String, String> fileUniqueIds = writers.getFileUniqueIds();

		// Apply protected area merging if strategy is configured AND protected areas enabled (D31, T-6)
		M2tGenerationStrategy strategy = config.generationStrategy();
		if (strategy != null && config.protectedAreaEnabled()) {
			M2tProtectedAreaMerger merger = new M2tProtectedAreaMerger();
			Map<String, String> mergedFiles = new LinkedHashMap<>(generatedFiles);
			for (Map.Entry<String, String> entry : mergedFiles.entrySet()) {
				String existing = strategy.readExistingContent(entry.getKey(), config.defaultCharset());
				if (existing != null) {
					entry.setValue(merger.merge(entry.getValue(), existing));
				}
			}
			generatedFiles = Map.copyOf(mergedFiles);
		}

		return new M2tResult(evaluator.getDiagnostics(), generatedFiles, fileUniqueIds);
	}

	/**
	 * Returns all modules in the link set that includes the given module.
	 * Collects from linkedModules set, or just the module itself if not linked.
	 */
	private List<Module> getAllLinkedModules(Module module) {
		List<Module> all = new ArrayList<>();
		for (Module m : linkedModules) {
			all.add(m);
		}
		if (all.isEmpty()) {
			all.add(module);
		}
		return all;
	}

	/**
	 * Finds the main template in the module (marked with {@code isMain = true}).
	 * Falls back to searching for a template named "main" if no explicit main exists.
	 *
	 * @param module the module to search
	 * @return the main template, or {@code null} if none found
	 */
	private Template findMainTemplate(Module module) {
		// First: look for explicitly marked main template
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (element instanceof Template t && t.isMain()) {
				return t;
			}
		}
		// Fallback: template named "main"
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (element instanceof Template t && "main".equals(t.getName())) {
				return t;
			}
		}
		return null;
	}
}
