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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.parser.QvtoParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.compile.SignatureFingerprint;

/**
 * Binds a loaded QVT-O unit: every import stub the manifest lists as a dependency is pointed at
 * the module inside the loaded dependency, every blackbox stub at the synthetic library the
 * registry yields — the same shape the execute-time linker gives a freshly parsed unit, done
 * once at prepare time (#140).
 *
 * <p>Verification of a blackbox requirement is the check the compiler recorded it for: the
 * library has to be there, and the fingerprint of its signatures has to be the one the unit was
 * compiled against.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class QvtoUnitBinder implements UnitBinder {

	private final QvtoLinker linker;
	private final QvtoBlackboxRegistry blackboxRegistry;

	QvtoUnitBinder(QvtoParserSupport parserSupport, EPackage.Registry packageRegistry,
			QvtoBlackboxRegistry blackboxRegistry, Set<String> allowedBlackboxModules, int maxBlackboxLibraries) {
		this.blackboxRegistry = blackboxRegistry; // nullable: none configured or disabled
		this.linker = new QvtoLinker(parserSupport, List.of(), packageRegistry, blackboxRegistry,
				allowedBlackboxModules, Set.of(), maxBlackboxLibraries);
	}

	@Override
	public String language() {
		return QvtoUnitCompiler.LANGUAGE;
	}

	@Override
	public void validate(CompiledUnit unit) throws UnitPrepareException {
		Objects.requireNonNull(unit, "unit must not be null");
		String name = unit.getManifest().getQualifiedName();
		if (!(unit.getUnit() instanceof OperationalTransformation transformation)) {
			throw new UnitPrepareException("'" + name + "' is declared a QVT-O unit but its script is a "
					+ (unit.getUnit() == null ? "null" : unit.getUnit().eClass().getName()));
		}
		List<String> findings = new ArrayList<>();
		for (Iterator<EObject> it = unit.eAllContents(); it.hasNext();) {
			EObject node = it.next();
			if (node instanceof VariableExp use) {
				// The parser resolves every variable use to a declaration it created — in the script
				// or among the satellites. A use pointing elsewhere is no parser's doing.
				Variable declaration = use.getReferredVariable();
				if (declaration == null) {
					findings.add("a variable use without declaration at " + EcoreUtil.getRelativeURIFragmentPath(unit, use));
				} else if (!EcoreUtil.isAncestor(unit, declaration)) {
					findings.add("the variable '" + declaration.getName() + "' used at "
							+ EcoreUtil.getRelativeURIFragmentPath(unit, use) + " is declared outside the document");
				}
			} else if (node instanceof ModuleImport imp && imp.getImportedModule() != null
					&& QvtoLinker.isLinkerStub(imp.getImportedModule()) && imp.getImportedModule().getName() == null) {
				findings.add("an import stub without a name at " + EcoreUtil.getRelativeURIFragmentPath(unit, imp));
			}
		}
		if (transformation.getName() == null || transformation.getName().isBlank()) {
			findings.add("the transformation has no name");
		}
		if (!findings.isEmpty()) {
			throw new UnitPrepareException("'" + name + "' is not a well-formed QVT-O unit: " + String.join("; ", findings));
		}
	}

	@Override
	public void bind(CompiledUnit unit, Map<String, CompiledUnit> dependencies) throws UnitPrepareException {
		Objects.requireNonNull(unit, "unit must not be null");
		if (!(unit.getUnit() instanceof OperationalTransformation transformation)) {
			throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName() + "' is not a QVT-O unit");
		}
		Map<Module, Module> stubToResolved = new HashMap<>();
		bind(transformation, unit, dependencies, stubToResolved, new HashSet<>());
		if (!stubToResolved.isEmpty()) {
			linker.updateTransformationInstantiationRefs(transformation, stubToResolved);
		}
	}

	private void bind(Module module, CompiledUnit unit, Map<String, CompiledUnit> dependencies,
			Map<Module, Module> stubToResolved, Set<Module> visited) throws UnitPrepareException {
		if (!visited.add(module)) {
			return;
		}
		for (ModuleImport imp : new ArrayList<>(module.getModuleImport())) {
			Module imported = imp.getImportedModule();
			if (imported == null) {
				continue;
			}
			if (!QvtoLinker.isLinkerStub(imported)) {
				bind(imported, unit, dependencies, stubToResolved, visited);
				continue;
			}
			String name = imported.getName();
			CompiledUnit dependency = dependencies.get(name);
			Module target;
			if (dependency != null) {
				target = QvtoLinker.unwrapLibrary((Module) dependency.getUnit());
			} else {
				target = linker.resolveBlackboxModule(name);
				if (target == null) {
					throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName() + "' imports '" + name
							+ "', which is neither a loaded dependency nor a blackbox the runtime has");
				}
				// The synthetic library belongs to this unit's document, or the document dangles
				unit.getSatellite().add(target);
			}
			imp.setImportedModule(target);
			stubToResolved.put(imported, target);
		}
	}

	@Override
	public void verifyBlackboxes(CompiledUnit unit) throws UnitPrepareException {
		for (BlackboxRequirement requirement : unit.getManifest().getBlackboxRequirement()) {
			Optional<QvtoBlackboxLibrary> library = blackboxRegistry == null ? Optional.empty()
					: blackboxRegistry.getLibrary(requirement.getName());
			if (library.isEmpty()) {
				throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName()
						+ "' requires the blackbox library '" + requirement.getName() + "', which the runtime does not have");
			}
			String actual = SignatureFingerprint.of(QvtoUnitCompiler.signatures(library.get()));
			if (requirement.getSignatureFingerprint() != null && !requirement.getSignatureFingerprint().equals(actual)) {
				throw new UnitPrepareException("'" + unit.getManifest().getQualifiedName()
						+ "' was compiled against blackbox library '" + requirement.getName() + "' with signatures "
						+ requirement.getSignatureFingerprint() + ", the runtime provides " + actual);
			}
		}
	}

	/** Whether an import stub is left anywhere in the transformation — what a prepared unit must not have. */
	static Optional<String> unboundImport(OperationalTransformation transformation) {
		Set<Module> visited = new HashSet<>();
		return unboundImport(transformation, visited);
	}

	private static Optional<String> unboundImport(Module module, Set<Module> visited) {
		if (!visited.add(module)) {
			return Optional.empty();
		}
		for (ModuleImport imp : module.getModuleImport()) {
			Module imported = imp.getImportedModule();
			if (imported == null) {
				continue;
			}
			if (QvtoLinker.isLinkerStub(imported)) {
				return Optional.ofNullable(imported.getName());
			}
			Optional<String> below = unboundImport(imported, visited);
			if (below.isPresent()) {
				return below;
			}
		}
		return Optional.empty();
	}
}
