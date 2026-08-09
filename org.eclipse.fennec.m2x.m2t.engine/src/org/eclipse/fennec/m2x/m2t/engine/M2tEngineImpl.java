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
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;

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

	private final OclEngine oclEngine;
	private final M2tConfiguration config;
	/**
	 * Resolves {@code parse(URI)} through its {@link ResourceSet#getURIConverter()
	 * URIConverter}: the configured resource set, or a default one when none was given,
	 * so that every URI takes the same road (D42).
	 *
	 * <p>Its package registry is deliberately not consulted for type resolution — that
	 * follows {@link M2tConfiguration#packageRegistry()}.
	 */
	private final ResourceSet resourceSet;
	private final M2tParserSupport parserSupport;

	// The four caches below are keyed weakly. They used to hold every module ever
	// parsed for the lifetime of the engine, which for a long-lived instance means a
	// leak. EMF model objects do not override equals/hashCode, so a WeakHashMap keys by
	// identity exactly as the IdentityHashMap did — it just lets go once the caller
	// drops the module. Callers who want to be certain use release(Module).

	/** Cache of parse results keyed by module identity, for auto-linking. */
	private final Map<Module, M2tParseResult> parseResultCache =
			Collections.synchronizedMap(new WeakHashMap<>());

	/** Tracks which modules have been linked to avoid re-linking. */
	private final Set<Module> linkedModules =
			Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

	/** Tracks which modules have been whitespace-normalized. */
	private final Set<Module> normalizedModules =
			Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

	/** Indentation map for standalone template invocations (§8.4). */
	private final Map<TemplateInvocation, String> globalIndentationMap =
			Collections.synchronizedMap(new WeakHashMap<>());

	/**
	 * Creates a new engine from the given configuration.
	 *
	 * @param config the engine configuration, must not be {@code null}
	 */
	public M2tEngineImpl(M2tConfiguration config) {
		this.config = Objects.requireNonNull(config, "config must not be null");
		this.resourceSet = config.resourceSet() != null ? config.resourceSet() : new ResourceSetImpl();
		// The engine evaluates with the OCL engine it was given — its cache, its
		// providers. Only when none was supplied is one built from the configuration;
		// the MOFM2T standard library rides along per evaluation (M2tEvaluator), so a
		// supplied engine needs no preparation.
		this.oclEngine = config.oclEngine() != null
				? config.oclEngine()
				: OclEngines.create(config.oclConfiguration());
		this.parserSupport = new M2tParserSupport();
	}

	// --- Parsing ---

	@Override
	public Module parse(URI moduleUri) throws M2tParseException {
		Objects.requireNonNull(moduleUri, "moduleUri must not be null");
		try (InputStream in = resourceSet.getURIConverter().createInputStream(moduleUri)) {
			String source = new String(in.readAllBytes(), config.defaultCharset());
			String unitName = moduleUri.lastSegment() != null
					? moduleUri.lastSegment() : moduleUri.toString();
			return parse(source, unitName);
		} catch (IOException e) {
			throw new M2tParseException("Failed to read module: " + moduleUri, e, List.of());
		}
	}

	@Override
	public Module parse(String source, String unitName) throws M2tParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		M2tParseResult result = parserSupport.buildModuleWithPending(source, unitName,
				EcorePackage.eINSTANCE.getEObject(), config.packageRegistry());
		parseResultCache.put(result.module(), result);
		return result.module();
	}

	// --- Execution ---

	@Override
	public List<String> link(Module... modules) {
		Objects.requireNonNull(modules, "modules must not be null");
		List<M2tParseResult> results = new ArrayList<>();
		List<String> warnings = new ArrayList<>();
		for (Module m : modules) {
			Objects.requireNonNull(m, "module element must not be null");
			M2tParseResult pr = parseResultCache.get(m);
			if (pr != null) {
				results.add(pr);
			} else {
				// Parsed by someone else, or released — either way this engine has
				// nothing to link it against, and saying nothing would look like success
				warnings.add("Module '" + m.getName()
						+ "' was not parsed by this engine and cannot be linked");
			}
		}
		if (results.isEmpty()) {
			return List.copyOf(warnings);
		}
		M2tModuleLinker linker = new M2tModuleLinker();
		warnings.addAll(linker.link(results));
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

		Map<String, OpenModeKind> openModes = writers.getFileOpenModes();
		Map<String, Charset> charsets = writers.getFileCharsets();

		// Apply protected area merging if strategy is configured AND protected areas enabled (D31, T-6)
		M2tGenerationStrategy strategy = config.generationStrategy();
		if (strategy != null && config.protectedAreaEnabled()) {
			M2tProtectedAreaMerger merger = new M2tProtectedAreaMerger();
			Map<String, String> mergedFiles = new LinkedHashMap<>(generatedFiles);
			for (Map.Entry<String, String> entry : mergedFiles.entrySet()) {
				// An APPEND file is extended, not regenerated: merging its existing
				// content in and then appending the result would duplicate the file.
				if (openModes.get(entry.getKey()) == OpenModeKind.APPEND) {
					continue;
				}
				String existing = strategy.readExistingContent(entry.getKey(),
						charsetFor(entry.getKey(), charsets));
				if (existing != null) {
					entry.setValue(merger.merge(entry.getValue(), existing));
				}
			}
			generatedFiles = Map.copyOf(mergedFiles);
		}

		// Hand the final content to the strategy. This happens after the merge on
		// purpose: the merger needs the complete generated and the complete existing
		// content before it can decide anything, so output cannot be streamed through
		// the strategy while the template is being evaluated.
		if (strategy != null) {
			writeGeneratedFiles(strategy, generatedFiles, openModes, charsets, evaluator);
		}

		return new M2tResult(evaluator.getDiagnostics(), generatedFiles, fileUniqueIds);
	}

	/**
	 * Passes every generated file to the configured {@link M2tGenerationStrategy}.
	 * A failure on one file is reported as a diagnostic and does not stop the rest —
	 * a generator run should not lose nine files because the tenth is unwritable.
	 */
	private void writeGeneratedFiles(M2tGenerationStrategy strategy,
			Map<String, String> generatedFiles, Map<String, OpenModeKind> openModes,
			Map<String, Charset> charsets, M2tEvaluator evaluator) {
		for (Map.Entry<String, String> entry : generatedFiles.entrySet()) {
			String filePath = entry.getKey();
			OpenModeKind mode = openModes.getOrDefault(filePath, OpenModeKind.OVERWRITE);
			try {
				Writer writer = strategy.createWriter(filePath, mode,
						charsetFor(filePath, charsets));
				if (writer == null) {
					continue;
				}
				try {
					writer.write(entry.getValue());
				} finally {
					strategy.closeWriter(filePath, writer);
				}
			} catch (IOException | RuntimeException e) {
				evaluator.addGenerationError(filePath, e);
			}
		}
	}

	/**
	 * Returns the charset the file block declared, or the configured default.
	 */
	private Charset charsetFor(String filePath, Map<String, Charset> charsets) {
		return charsets.getOrDefault(filePath, config.defaultCharset());
	}

	/**
	 * Drops everything this engine remembers about the given module.
	 *
	 * <p>The engine caches a module's parse result, its link and normalization state and
	 * the indentation of its template invocations, so that repeated executions do not
	 * re-parse and re-link. Those caches are keyed weakly and let go once the caller
	 * drops the module, but garbage collection is not a schedule: an engine that lives
	 * as long as the application and parses modules in a loop should say when it is done
	 * with one.
	 *
	 * @param module the module to forget, must not be {@code null}
	 */
	public void release(Module module) {
		Objects.requireNonNull(module, "module must not be null");
		parseResultCache.remove(module);
		linkedModules.remove(module);
		normalizedModules.remove(module);
		synchronized (globalIndentationMap) {
			globalIndentationMap.keySet().removeIf(
					invocation -> EcoreUtil.isAncestor(module, invocation));
		}
	}

	/**
	 * Drops everything this engine remembers about every module.
	 *
	 * <p>After this call, previously parsed modules have to be parsed and linked again.
	 */
	public void clearCaches() {
		parseResultCache.clear();
		linkedModules.clear();
		normalizedModules.clear();
		globalIndentationMap.clear();
	}

	/**
	 * Returns all modules in the link set that includes the given module.
	 * Collects from linkedModules set, or just the module itself if not linked.
	 */
	private List<Module> getAllLinkedModules(Module module) {
		List<Module> all = new ArrayList<>();
		synchronized (linkedModules) {
			all.addAll(linkedModules);
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
