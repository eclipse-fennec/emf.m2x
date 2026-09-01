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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.UnitNature;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.eclipse.fennec.m2x.unit.resolve.ResolutionPolicy;
import org.eclipse.fennec.m2x.unit.compile.SignatureFingerprint;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;

/**
 * Compile for QVT-O: parse → resolve → package (§4 of the compiled-unit concept, #139).
 *
 * <p>The linker of the execute path binds every import in place. This compiler asks the same
 * sources — inline modules, unit resolvers, blackbox registry, under the same D29 limits — but
 * what it does with an answer is the dependency mode:
 *
 * <ul>
 * <li><b>embed</b> — the dependency is compiled in turn (recursively, in the same mode) and
 * attached as an embedded unit; the import is bound to the module inside it. The document then
 * carries everything it needs and runs without a resolver.</li>
 * <li><b>pin</b> — the dependency is compiled to learn its unit fingerprint, which the manifest
 * records with the name; the compiled dependency itself is dropped and the import stays a stub.</li>
 * <li><b>rebind</b> — the manifest records the name; the import stays a stub. Resolving still
 * happens, so an import nobody can serve fails here and not at the first run.</li>
 * </ul>
 *
 * <p>Same-file resolution — a stub for a library defined in the same source — is not a dependency
 * and is bound in every mode, exactly as the linker binds it. A blackbox import is never a
 * dependency either: its implementation is the runtime's, so the manifest gets a requirement
 * naming the library and the fingerprint of the signatures the unit was compiled against, and
 * the stub stays for the runtime to bind.
 *
 * <p>Nothing a resolver lends is mutated: a source is parsed afresh, a bare AST is copied together
 * with its satellites ({@link UnitPackager#detach}), a unit that already sits in a compiled
 * document is taken as a copy of that document.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class QvtoUnitCompiler {

	static final String LANGUAGE = "qvto";

	private final QvtoLinkPolicy policy;
	private QvtoReferencedUnits referencedUnits;
	private final UnitPackager packager;
	private final DependencyMode mode;
	private final QvtoLinker linker;
	private final Set<String> requiredBlackboxModules = new HashSet<>();

	QvtoUnitCompiler(QvtoLinkPolicy policy, UnitPackager packager, UnitCompileOptions options) {
		this.policy = Objects.requireNonNull(policy, "policy must not be null");
		this.packager = Objects.requireNonNull(packager, "packager must not be null");
		this.mode = Objects.requireNonNull(options, "options must not be null").dependencyMode();
		this.linker = new QvtoLinker(policy);
	}

	/**
	 * Compiles a source text.
	 *
	 * @param source the source
	 * @param qualifiedName the name the unit is imported by
	 * @return the sealed compiled unit
	 * @throws QvtoParseException if parsing fails, an import cannot be resolved, imports form a
	 *             cycle, or the result cannot be made self-contained
	 */
	CompiledUnit compile(String source, String qualifiedName) throws QvtoParseException {
		OperationalTransformation transformation = policy.parserSupport().parse(source, qualifiedName, policy.packageRegistry());
		return compile(transformation, qualifiedName, source, new ArrayDeque<>());
	}

	private CompiledUnit compile(OperationalTransformation transformation, String qualifiedName,
			String source, Deque<String> path) throws QvtoParseException {
		if (path.contains(qualifiedName)) {
			throw new QvtoParseException("Circular import detected: " + qualifiedName);
		}
		path.push(qualifiedName);
		CompiledUnit document;
		try {
			document = packager.begin(LANGUAGE, qualifiedName, transformation, mode, source);
		} catch (IllegalArgumentException e) {
			throw new QvtoParseException("Cannot compile '" + qualifiedName + "': " + e.getMessage(), e);
		}
		// What kind of unit this is, answerable without loading the AST (#224): a standalone
		// library source parses into a synthetic transformation wrapper, and the unwrap telling
		// them apart happens here, once, where the compiler knows — never again by a consumer
		// mirroring the heuristic.
		if (QvtoLinker.unwrapLibrary(transformation) != transformation) {
			document.getManifest().setNature(UnitNature.LIBRARY);
		}
		Map<Module, Module> stubToResolved = new HashMap<>();
		resolveImports(transformation, document, stubToResolved, new HashSet<>(), path);
		if (!stubToResolved.isEmpty()) {
			linker.updateTransformationInstantiationRefs(transformation, stubToResolved);
		}
		path.pop();
		try {
			return packager.seal(document);
		} catch (IllegalStateException e) {
			// A unit that is not self-contained is not storable; better said here, with the unit
			// name, than on the first save() somewhere else with a dangling reference (#137).
			throw new QvtoParseException("Cannot compile '" + qualifiedName + "': " + e.getMessage(), e);
		}
	}

	private void resolveImports(Module module, CompiledUnit document, Map<Module, Module> stubToResolved,
			Set<Module> visited, Deque<String> path) throws QvtoParseException {
		if (!visited.add(module)) {
			return;
		}
		// Inline modules first — a library defined in this very source satisfies a stub of the
		// same name before any resolver is asked, exactly as the linker does.
		Map<String, Module> inline = new HashMap<>();
		for (ModuleImport imp : module.getModuleImport()) {
			Module imported = imp.getImportedModule();
			if (imported != null && !QvtoLinker.isLinkerStub(imported) && imported.getName() != null) {
				inline.put(imported.getName(), imported);
			}
		}
		for (ModuleImport imp : new ArrayList<>(module.getModuleImport())) {
			Module imported = imp.getImportedModule();
			if (imported == null) {
				continue;
			}
			if (!QvtoLinker.isLinkerStub(imported)) {
				resolveImports(imported, document, stubToResolved, visited, path);
				continue;
			}
			String name = imported.getName();
			if (name == null) {
				continue;
			}
			Module local = inline.get(name);
			if (local != null) {
				imp.setImportedModule(local);
				stubToResolved.put(imported, local);
				continue;
			}
			Optional<QvtoUnit> unit = resolveUnit(name);
			if (unit.isPresent()) {
				bind(imp, imported, unit.get(), document, stubToResolved, path);
				continue;
			}
			Optional<QvtoBlackboxLibrary> blackbox = resolveBlackbox(name);
			if (blackbox.isPresent()) {
				document.getManifest().getBlackboxRequirement().add(requirement(name, blackbox.get()));
				continue;
			}
			throw new QvtoParseException("Cannot resolve import: " + name);
		}
	}

	private void bind(ModuleImport imp, Module stub, QvtoUnit unit, CompiledUnit document,
			Map<Module, Module> stubToResolved, Deque<String> path) throws QvtoParseException {
		DependencyEntry entry = CompiledFactory.eINSTANCE.createDependencyEntry();
		entry.setQualifiedName(stub.getName());
		entry.setMode(mode);
		switch (mode) {
			case EMBED -> {
				CompiledUnit dependency = compileDependency(unit, path);
				document.getEmbedded().add(dependency);
				Module target = QvtoLinker.unwrapLibrary((Module) dependency.getUnit());
				imp.setImportedModule(target);
				stubToResolved.put(stub, target);
				entry.setFingerprint(dependency.getManifest().getUnitFingerprint());
			}
			case PIN -> entry.setFingerprint(fingerprintOf(unit, path));
			case REBIND -> {
				// resolved, so the name is known to be servable; bound at prepare time
			}
		}
		document.getManifest().getDependencyEntry().add(entry);
	}

	/**
	 * The dependency as a compiled document of our own — never the resolver's object.
	 */
	private CompiledUnit compileDependency(QvtoUnit unit, Deque<String> path) throws QvtoParseException {
		return switch (unit) {
			case QvtoUnit.SourceUnit source -> compile(
					policy.parserSupport().parse(source.source(), source.qualifiedName(), policy.packageRegistry()),
					source.qualifiedName(), source.source(), path);
			case QvtoUnit.CompiledUnit compiled -> {
				if (compiled.transformation().eContainer() instanceof CompiledUnit document) {
					yield EcoreUtil.copy(document);
				}
				yield compile((OperationalTransformation) UnitPackager.detach(compiled.transformation()),
						compiled.qualifiedName(), null, path);
			}
			// dereferenced at resolution; kept for exhaustiveness, and for a caller that hands one in
			case QvtoUnit.ResourceUnit reference -> compileDependency(referencedUnits().dereference(reference), path);
		};
	}

	/**
	 * The unit fingerprint a pin records. A dependency that already is a compiled document has
	 * one in its manifest; anything else is compiled to learn it and dropped.
	 */
	private String fingerprintOf(QvtoUnit unit, Deque<String> path) throws QvtoParseException {
		if (unit instanceof QvtoUnit.CompiledUnit compiled
				&& compiled.transformation().eContainer() instanceof CompiledUnit document
				&& document.getManifest() != null && document.getManifest().getUnitFingerprint() != null) {
			return document.getManifest().getUnitFingerprint();
		}
		return compileDependency(unit, path).getManifest().getUnitFingerprint();
	}

	private Optional<QvtoUnit> resolveUnit(String qualifiedName) throws QvtoParseException {
		// D29: allow-list for unit modules, as in the linker
		if (!policy.allowedUnitModules().isEmpty() && !policy.allowedUnitModules().contains(qualifiedName)) {
			return Optional.empty();
		}
		Optional<QvtoUnit> unit;
		try {
			unit = ResolutionPolicy.resolve(qualifiedName, QvtoLinker.sources(policy.unitResolvers()));
		} catch (UnitResolutionException failure) {
			throw new QvtoParseException("Cannot resolve import '" + qualifiedName + "': " + failure.getMessage(),
					failure);
		}
		if (unit.isPresent()) {
			// a reference is loaded here, in this compile's context, before any switch sees it
			unit = Optional.of(referencedUnits().dereference(unit.get()));
		}
		return unit;
	}

	private QvtoReferencedUnits referencedUnits() {
		if (referencedUnits == null) {
			referencedUnits = new QvtoReferencedUnits(policy.packageRegistry());
		}
		return referencedUnits;
	}

	private Optional<QvtoBlackboxLibrary> resolveBlackbox(String qualifiedName) {
		if (policy.blackboxRegistry() == null) {
			return Optional.empty();
		}
		if (!policy.allowedBlackboxModules().isEmpty() && !policy.allowedBlackboxModules().contains(qualifiedName)) {
			return Optional.empty();
		}
		// D29: a ceiling on distinct libraries per compile, counted over the whole compile
		if (!requiredBlackboxModules.contains(qualifiedName)
				&& requiredBlackboxModules.size() >= policy.maxBlackboxLibraries()) {
			return Optional.empty();
		}
		Optional<QvtoBlackboxLibrary> library = policy.blackboxRegistry().getLibrary(qualifiedName);
		library.ifPresent(l -> requiredBlackboxModules.add(qualifiedName));
		return library;
	}

	private static BlackboxRequirement requirement(String qualifiedName, QvtoBlackboxLibrary library) {
		BlackboxRequirement requirement = CompiledFactory.eINSTANCE.createBlackboxRequirement();
		requirement.setName(qualifiedName);
		requirement.setProvider(library.getClass().getName());
		requirement.setSignatureFingerprint(SignatureFingerprint.of(signatures(library)));
		return requirement;
	}

	/**
	 * One line per operation: {@code name(context;param,param):return}, types as
	 * {@code nsURI#Name} — the same shape at compile and at load time.
	 */
	static List<String> signatures(QvtoBlackboxLibrary library) {
		List<String> lines = new ArrayList<>();
		for (BlackboxOperationDescriptor descriptor : library.getOperationDescriptors()) {
			StringBuilder line = new StringBuilder(descriptor.getName()).append('(')
					.append(typeKey(descriptor.getContextType())).append(';');
			List<String> parameters = new ArrayList<>();
			for (EClassifier type : descriptor.getParameterTypes()) {
				parameters.add(typeKey(type));
			}
			line.append(String.join(",", parameters)).append("):")
					.append(typeKey(descriptor.getReturnType()));
			lines.add(line.toString());
		}
		return lines;
	}

	private static String typeKey(EClassifier type) {
		if (type == null) {
			return "";
		}
		String nsURI = type.getEPackage() == null ? "" : type.getEPackage().getNsURI();
		return nsURI + "#" + type.getName();
	}
}
