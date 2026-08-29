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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.eclipse.fennec.m2x.unit.resolve.ResolutionPolicy;

/**
 * Compile for MOFM2T: parse → resolve → package (§4 of the compiled-unit concept, #139).
 *
 * <p>A module depends on what it {@code extends} and {@code imports}. The compiler resolves
 * every such name through the unit resolvers, under the same enable flag and allow-list as
 * generation (D29), and binds according to the dependency mode:
 *
 * <ul>
 * <li><b>embed</b> — the dependency is compiled in turn and attached as an embedded unit; the
 * module is linked against it, so every {@code extends}, {@code import}, {@code overrides} and
 * invocation points into the document. The unit then generates without a resolver.</li>
 * <li><b>pin</b> — the dependency is compiled to learn its unit fingerprint, which the manifest
 * records; the module stays unbound, with its link information kept on it
 * ({@link M2tLinkInfo}) for the engine that binds it later.</li>
 * <li><b>rebind</b> — the manifest records the name; the module stays unbound the same way.</li>
 * </ul>
 *
 * <p>MOFM2T has no blackbox concept: its Java services arrive through the OCL operation
 * providers of the engine, not through the module. Nothing a resolver lends is mutated: a source
 * is parsed afresh, a bare AST is copied with its satellites, a unit that already sits in a
 * compiled document is taken as a copy of that document.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class M2tUnitCompiler {

	static final String LANGUAGE = "m2t";

	/** How the engine parses — through its own cache, so the module stays linkable by it. */
	interface Parser {
		M2tParseResult parse(String source, String unitName) throws M2tParseException;
	}

	private final Parser parser;
	private final Function<Module, M2tParseResult> knownResults;
	private final List<M2tUnitResolver> unitResolvers;
	private final Set<String> allowedUnitModules;
	private final UnitPackager packager;
	private final DependencyMode mode;

	M2tUnitCompiler(Parser parser, Function<Module, M2tParseResult> knownResults,
			List<M2tUnitResolver> unitResolvers, Set<String> allowedUnitModules,
			UnitPackager packager, UnitCompileOptions options) {
		this.parser = Objects.requireNonNull(parser, "parser must not be null");
		this.knownResults = Objects.requireNonNull(knownResults, "knownResults must not be null");
		this.unitResolvers = Objects.requireNonNull(unitResolvers, "unitResolvers must not be null");
		this.allowedUnitModules = Objects.requireNonNull(allowedUnitModules, "allowedUnitModules must not be null");
		this.packager = Objects.requireNonNull(packager, "packager must not be null");
		this.mode = Objects.requireNonNull(options, "options must not be null").dependencyMode();
	}

	CompiledUnit compile(String source, String unitName) throws M2tParseException {
		return compile(parser.parse(source, unitName), unitName, source, new ArrayDeque<>());
	}

	private CompiledUnit compile(M2tParseResult result, String unitName, String source, Deque<String> path)
			throws M2tParseException {
		if (path.contains(unitName)) {
			throw new M2tParseException("Circular import detected: " + unitName, List.of());
		}
		path.push(unitName);
		Module module = result.module();
		CompiledUnit document;
		try {
			document = packager.begin(LANGUAGE, unitName, module, mode, source);
		} catch (IllegalArgumentException e) {
			throw new M2tParseException("Cannot compile '" + unitName + "': " + e.getMessage(), e, List.of());
		}
		List<M2tParseResult> linkSet = new ArrayList<>();
		linkSet.add(result);
		Set<String> referenced = new LinkedHashSet<>(result.extendsNames());
		referenced.addAll(result.importNames());
		for (String name : referenced) {
			M2tUnit unit = resolveUnit(name)
					.orElseThrow(() -> new M2tParseException("Cannot resolve import: " + name, List.of()));
			bind(name, unit, document, linkSet, path);
		}
		if (mode == DependencyMode.EMBED) {
			M2tModuleLinker linker = new M2tModuleLinker();
			linker.link(linkSet);
			List<String> unresolved = linker.unresolvedReferences().getOrDefault(module, List.of());
			if (!unresolved.isEmpty()) {
				throw new M2tParseException("Cannot compile '" + unitName + "': " + String.join("; ", unresolved),
						List.of());
			}
			M2tLinkInfo.strip(module);
		} else {
			M2tLinkInfo.record(result);
		}
		path.pop();
		try {
			return packager.seal(document);
		} catch (IllegalStateException e) {
			throw new M2tParseException("Cannot compile '" + unitName + "': " + e.getMessage(), e, List.of());
		}
	}

	private void bind(String name, M2tUnit unit, CompiledUnit document, List<M2tParseResult> linkSet,
			Deque<String> path) throws M2tParseException {
		DependencyEntry entry = CompiledFactory.eINSTANCE.createDependencyEntry();
		entry.setQualifiedName(name);
		entry.setMode(mode);
		switch (mode) {
			case EMBED -> {
				CompiledUnit dependency = compileDependency(unit, name, path);
				document.getEmbedded().add(dependency);
				// Bound already, inside its own document: the linker only needs it in the index
				linkSet.add(leaf((Module) dependency.getUnit()));
				entry.setFingerprint(dependency.getManifest().getUnitFingerprint());
			}
			case PIN -> entry.setFingerprint(fingerprintOf(unit, name, path));
			case REBIND -> {
				// resolved, so the name is known to be servable; bound at prepare time
			}
		}
		document.getManifest().getDependencyEntry().add(entry);
	}

	private CompiledUnit compileDependency(M2tUnit unit, String name, Deque<String> path)
			throws M2tParseException {
		return switch (unit) {
			case M2tUnit.SourceUnit source -> compile(parser.parse(source.source(), name), name, source.source(), path);
			case M2tUnit.CompiledUnit compiled -> {
				if (compiled.module().eContainer() instanceof CompiledUnit document) {
					yield EcoreUtil.copy(document);
				}
				Module copy = (Module) UnitPackager.detach(compiled.module());
				M2tParseResult known = knownResults.apply(compiled.module());
				// A module this engine parsed keeps its link information; any other bare module
				// is taken as it is — a leaf with nothing left to resolve
				M2tParseResult result = known == null ? leaf(copy)
						: new M2tParseResult(copy, known.extendsNames(), known.importNames(), Map.of(), Map.of());
				yield compile(result, name, null, path);
			}
		};
	}

	private String fingerprintOf(M2tUnit unit, String name, Deque<String> path) throws M2tParseException {
		if (unit instanceof M2tUnit.CompiledUnit compiled
				&& compiled.module().eContainer() instanceof CompiledUnit document
				&& document.getManifest() != null && document.getManifest().getUnitFingerprint() != null) {
			return document.getManifest().getUnitFingerprint();
		}
		return compileDependency(unit, name, path).getManifest().getUnitFingerprint();
	}

	private Optional<M2tUnit> resolveUnit(String name) throws M2tParseException {
		if (!allowedUnitModules.isEmpty() && !allowedUnitModules.contains(name)) {
			return Optional.empty();
		}
		try {
			return ResolutionPolicy.resolve(name, M2tEngineImpl.sources(unitResolvers));
		} catch (UnitResolutionException failure) {
			throw new M2tParseException("Cannot resolve import '" + name + "': " + failure.getMessage(), failure,
					List.of());
		}
	}

	private static M2tParseResult leaf(Module module) {
		return new M2tParseResult(module, List.of(), List.of(), Map.of(), Map.of());
	}
}
