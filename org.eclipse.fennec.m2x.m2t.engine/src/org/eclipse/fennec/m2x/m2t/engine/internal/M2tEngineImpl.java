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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.io.IOException;
import java.io.InputStream;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.stream.Stream;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
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
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.m2t.api.UnresolvedReferenceMode;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.m2t.parser.M2tParserSupport;
import org.eclipse.fennec.m2x.m2t.parser.M2tWhitespaceNormalizer;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;

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

	/** Language tag of this engine's units, as used in store keys and manifests. */
	private static final String LANGUAGE = "m2t";

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
	private final UnitPackager packager;

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

	/**
	 * What each linked module's link could not resolve. Kept beside {@link #linkedModules}
	 * because {@code execute} has to act on it even when the caller linked explicitly some
	 * time earlier (#144).
	 */
	private final Map<Module, List<String>> unresolvedReferences =
			Collections.synchronizedMap(new WeakHashMap<>());

	/** Tracks which modules have been whitespace-normalized. */
	private final Set<Module> normalizedModules =
			Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

	/** Indentation map for standalone template invocations (§8.4). */
	/**
	 * Where each expression node stood in its template, for runtime diagnostics (#116). Filled at
	 * parse time and dropped with the module, like the indentation map beside it.
	 */
	private final Map<EObject, SourcePosition> globalPositions =
			java.util.Collections.synchronizedMap(new java.util.IdentityHashMap<>());

	private final Map<TemplateInvocation, String> globalIndentationMap =
			Collections.synchronizedMap(new WeakHashMap<>());

	/**
	 * Creates a new engine from the given configuration.
	 *
	 * @param config the engine configuration, must not be {@code null}
	 */
	public M2tEngineImpl(M2tConfiguration config) {
		this(config, UnitPackager.withDefaults());
	}

	/**
	 * Creates a new engine from the given configuration, compiling with the given packager.
	 *
	 * <p>The packager is where the package fingerprints of a compiled unit come from. Under OSGi
	 * the component hands in one built on the {@code FingerprintService} it was given; a plain JVM
	 * uses {@link UnitPackager#withDefaults()}.
	 *
	 * @param config the engine configuration
	 * @param packager the packager compiled units are built with
	 */
	public M2tEngineImpl(M2tConfiguration config, UnitPackager packager) {
		this.config = Objects.requireNonNull(config, "config must not be null");
		this.packager = Objects.requireNonNull(packager, "packager must not be null");
		this.resourceSet = config.resourceSet() != null ? config.resourceSet() : new ResourceSetImpl();
		// The engine evaluates with the OCL engine it was given — its cache, its
		// providers. Only when none was supplied is one built from the configuration;
		// the MOFM2T standard library rides along per evaluation (M2tEvaluator), so a
		// supplied engine needs no preparation.
		this.oclEngine = resolveOclEngine(config);
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
		return parseCached(source, unitName).module();
	}

	/**
	 * Parses and remembers the result, so that the module can be linked and executed by this
	 * engine later — the parse result carries what the AST does not: the names still to bind.
	 */
	private M2tParseResult parseCached(String source, String unitName) throws M2tParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		M2tParseResult result = parserSupport.buildModuleWithPending(source, unitName,
				EcorePackage.eINSTANCE.getEObject(), config.packageRegistry());
		parseResultCache.put(result.module(), result);
		globalPositions.putAll(result.positions());
		return result;
	}

	@Override
	public CompiledUnit compile(String source, String unitName) throws M2tParseException {
		return compile(source, unitName, UnitCompileOptions.defaults());
	}

	@Override
	public CompiledUnit compile(URI moduleUri) throws M2tParseException {
		return compile(moduleUri, UnitCompileOptions.defaults());
	}

	@Override
	public CompiledUnit compile(String source, String unitName, UnitCompileOptions options)
			throws M2tParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		Objects.requireNonNull(options, "options must not be null");
		// Compile asks the same resolvers under the same D29 limits as a link
		return new M2tUnitCompiler(this::parseCached, parseResultCache, effectiveUnitResolvers(),
				config.unitResolverEnabled() ? config.allowedUnitModules() : Set.of(), packager, options)
				.compile(source, unitName);
	}

	@Override
	public CompiledUnit compile(URI moduleUri, UnitCompileOptions options) throws M2tParseException {
		Objects.requireNonNull(moduleUri, "moduleUri must not be null");
		try (InputStream in = resourceSet.getURIConverter().createInputStream(moduleUri)) {
			String source = new String(in.readAllBytes(), config.defaultCharset());
			String unitName = moduleUri.lastSegment() != null
					? moduleUri.lastSegment() : moduleUri.toString();
			return compile(source, unitName, options);
		} catch (IOException e) {
			throw new M2tParseException("Failed to read module: " + moduleUri, e, List.of());
		}
	}

	@Override
	public List<String> link(Module... modules) {
		Objects.requireNonNull(modules, "modules must not be null");
		List<M2tParseResult> results = new ArrayList<>();
		List<String> warnings = new ArrayList<>();
		for (Module m : modules) {
			Objects.requireNonNull(m, "module element must not be null");
			M2tParseResult pr = parseResultCache.get(m);
			if (pr == null) {
				// A module compiled under pin or rebind and loaded here carries what it still
				// has to bind on itself (#139); an engine that never parsed it binds it from that
				pr = M2tLinkInfo.recover(m).orElse(null);
			}
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
		results.addAll(resolveMissingUnits(results, warnings));
		M2tModuleLinker linker = new M2tModuleLinker();
		warnings.addAll(linker.link(results));
		Map<Module, List<String>> unresolved = linker.unresolvedReferences();
		for (Module m : modules) {
			linkedModules.add(m);
			unresolvedReferences.put(m, unresolved.getOrDefault(m, List.of()));
		}
		return warnings;
	}

	/**
	 * Fetches the modules a template extends or imports but that nobody handed in.
	 *
	 * <p>Without this, {@code extends} and {@code import} could only ever name a module the
	 * same call already carried, so a template could not use a library that lives elsewhere
	 * no matter how the engine was configured. Resolution runs before the linker so that
	 * the linker keeps seeing one flat set of modules.
	 *
	 * <p>Repeated until nothing new turns up, because a resolved module may extend or import
	 * in turn. Names that stay unresolved are left to the linker, which reports them as the
	 * warnings it always did — a linker asked to link a partial set is not a failure.
	 */
	private List<M2tParseResult> resolveMissingUnits(List<M2tParseResult> known,
			List<String> warnings) {
		List<M2tUnitResolver> resolvers = effectiveUnitResolvers();
		if (resolvers.isEmpty()) {
			return List.of();
		}
		Set<String> haveOrTried = new HashSet<>();
		for (M2tParseResult result : known) {
			haveOrTried.add(result.module().getName());
		}
		List<M2tParseResult> resolved = new ArrayList<>();
		Deque<M2tParseResult> pending = new ArrayDeque<>(known);
		while (!pending.isEmpty()) {
			M2tParseResult result = pending.removeFirst();
			for (String name : referencedNames(result)) {
				if (!haveOrTried.add(name)) {
					continue;
				}
				// The allow-list narrows which names may be asked for, before anything is
				// asked. An empty one puts no restriction on them.
				if (!config.allowedUnitModules().isEmpty()
						&& !config.allowedUnitModules().contains(name)) {
					continue;
				}
				M2tParseResult fetched = resolveUnit(resolvers, name, warnings);
				if (fetched != null) {
					resolved.add(fetched);
					pending.addLast(fetched);
				}
			}
		}
		return resolved;
	}

	private List<String> referencedNames(M2tParseResult result) {
		List<String> names = new ArrayList<>(result.extendsNames());
		names.addAll(result.importNames());
		return names;
	}

	private M2tParseResult resolveUnit(List<M2tUnitResolver> resolvers, String name,
			List<String> warnings) {
		for (M2tUnitResolver resolver : resolvers) {
			Optional<M2tUnit> unit = resolver.resolveUnit(name);
			if (unit.isEmpty()) {
				continue;
			}
			try {
				return switch (unit.get()) {
					case M2tUnit.CompiledUnit compiled -> resultFor(compiled.module());
					case M2tUnit.SourceUnit source -> {
						parse(source.source(), name);
						yield parseResultCache.get(moduleByName(name));
					}
				};
			} catch (M2tParseException failure) {
				// One unusable module must not decide the fate of the others; the linker
				// will report the name as unresolved.
				warnings.add("Resolved module '" + name + "' could not be parsed: "
						+ failure.getMessage());
				return null;
			}
		}
		return null;
	}

	/**
	 * The parse result of a module this engine may never have parsed: what it parsed, else
	 * what the module carries as link information (compiled under pin or rebind, #139), else a
	 * leaf — a module that arrives fully bound, from an embedded unit or as a library without
	 * an {@code extends}, has nothing left to resolve.
	 */
	private M2tParseResult resultFor(Module module) {
		M2tParseResult known = parseResultCache.get(module);
		if (known != null) {
			return known;
		}
		return M2tLinkInfo.recover(module)
				.orElseGet(() -> new M2tParseResult(module, List.of(), List.of(), Map.of(), Map.of()));
	}

	private Module moduleByName(String name) {
		for (Module module : parseResultCache.keySet()) {
			if (name.equals(module.getName())) {
				return module;
			}
		}
		return null;
	}

	/**
	 * The resolvers this generation may consult: the configured ones, then the discovered
	 * one, capped — and none at all unless resolution was switched on.
	 */
	private List<M2tUnitResolver> effectiveUnitResolvers() {
		if (!config.unitResolverEnabled()) {
			return List.of();
		}
		Stream<M2tUnitResolver> discovered = config.discoverUnitResolvers()
				? Stream.of(new ServiceLoaderM2tUnitResolver())
				: Stream.of();
		return Stream.concat(config.unitResolvers().stream(), discovered)
				.limit(config.maxUnitResolvers())
				.toList();
	}

	@Override
	public M2tResult execute(PreparedContext prepared, String qualifiedName, M2tContext context) {
		Objects.requireNonNull(prepared, "prepared must not be null");
		Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
		Objects.requireNonNull(context, "context must not be null");
		Unit unit = prepared.unit(qualifiedName).orElseThrow(() -> new IllegalArgumentException(
				"the prepared context holds no unit '" + qualifiedName + "'"));
		if (!(unit instanceof Unit.Packaged packaged) || !(packaged.document().getUnit() instanceof Module module)) {
			throw new IllegalArgumentException("'" + qualifiedName + "' is not a compiled MOFM2T unit");
		}
		// Execute proper: no link. Link information still on the module means it was not bound.
		if (M2tLinkInfo.recover(module).isPresent()) {
			throw new IllegalArgumentException("'" + qualifiedName
					+ "' still carries link information; the context was not prepared with this engine's binder");
		}
		linkedModules.add(module);
		unresolvedReferences.put(module, List.of());
		return execute(module, context);
	}

	@Override
	public UnitBinder unitBinder() {
		return new M2tUnitBinder();
	}

	@Override
	public M2tResult execute(Module module, M2tContext context) {
		Objects.requireNonNull(module, "module must not be null");
		Objects.requireNonNull(context, "context must not be null");

		// Auto-link if not yet linked
		if (!linkedModules.contains(module)) {
			link(module);
		}

		// A reference naming something that is not there ends the generation, as it does in
		// QVT-O and QVT-R. Generating anyway is the quieter and worse outcome: a missing
		// extends silently changes which templates are visible, so the document is not absent
		// but wrong (#144). WARN restores the older, lenient behaviour.
		List<String> unresolved = unresolvedReferences.getOrDefault(module, List.of());
		if (!unresolved.isEmpty()
				&& config.unresolvedReferenceMode() == UnresolvedReferenceMode.FAIL) {
			return unresolvedReferenceFailure(unresolved);
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
				getAllLinkedModules(module), globalIndentationMap, globalPositions,
				config.maxDiagnostics(),
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

	/** Diagnostic source of the linking errors this engine raises itself. */
	private static final String UNRESOLVED_SOURCE_ID = "m2t.engine";

	/**
	 * The result of a generation that never ran because the module set was incomplete: one
	 * error diagnostic per unresolved reference, and no files.
	 */
	private M2tResult unresolvedReferenceFailure(List<String> unresolved) {
		List<Diagnostic> diagnostics = new ArrayList<>();
		for (String message : unresolved) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, UNRESOLVED_SOURCE_ID, 0,
					message, null));
		}
		return new M2tResult(List.copyOf(diagnostics), Map.of(), Map.of());
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
	@Override
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
	@Override
	public void clearCaches() {
		parseResultCache.clear();
		linkedModules.clear();
		normalizedModules.clear();
		globalIndentationMap.clear();
		globalPositions.clear();
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

	/**
	 * Returns the OCL engine this engine evaluates with — the one it was given, one built
	 * from the OCL configuration it was given, or a default one.
	 *
	 * <p>The third case is what lets a caller configure M2t without knowing anything about
	 * OCL ({@link M2tConfiguration#builder()}). It is the single point where this engine falls back,
	 * so there is one place to look when the question is which OCL engine ran (D42).
	 */
	@Override
	public OclEngine getOclEngine() {
		return oclEngine;
	}

	private static OclEngine resolveOclEngine(M2tConfiguration config) {
		if (config.oclEngine() != null) {
			return config.oclEngine();
		}
		if (config.oclConfiguration() != null) {
			return OclEngines.create(config.oclConfiguration());
		}
		return OclEngines.create(new OclParserSupport());
	}
}
