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

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.imperativeocl.TransformationInstantiationExp;
import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.Helper;
import org.eclipse.fennec.m2x.model.qvtoperational.Library;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.model.qvtoperational.VarParameter;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.parser.QvtoParserSupport;

/**
 * Resolves stub modules created by the parser into fully parsed modules
 * by consulting registered {@link QvtoUnitResolver}s and optionally a
 * {@link QvtoBlackboxRegistry}.
 *
 * <p>The parser creates stub {@code Library} objects with only the qualified name set
 * for each {@code access} or {@code extends} import. This linker replaces those stubs
 * with real parsed modules before execution.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoLinker {

	private static final QvtOperationalFactory QVTO = QvtOperationalFactory.eINSTANCE;

	private final QvtoParserSupport parserSupport;
	private final List<QvtoUnitResolver> unitResolvers;
	private final EPackage.Registry packageRegistry;
	private final QvtoBlackboxRegistry blackboxRegistry;

	public QvtoLinker(QvtoParserSupport parserSupport,
			List<QvtoUnitResolver> unitResolvers,
			EPackage.Registry packageRegistry,
			QvtoBlackboxRegistry blackboxRegistry) {
		this.parserSupport = Objects.requireNonNull(parserSupport);
		this.unitResolvers = Objects.requireNonNull(unitResolvers);
		this.packageRegistry = Objects.requireNonNull(packageRegistry);
		this.blackboxRegistry = blackboxRegistry; // nullable
	}

	/**
	 * Links all unresolved module imports in the given transformation.
	 * Recursively resolves imports of imported modules.
	 *
	 * @param transformation the transformation whose imports to resolve
	 * @throws QvtoParseException if a required import cannot be resolved or parsed
	 */
	public void link(OperationalTransformation transformation) throws QvtoParseException {
		Map<Module, Module> stubToResolved = new HashMap<>();
		Set<String> visited = new HashSet<>();
		visited.add(transformation.getName());
		linkModule(transformation, visited, stubToResolved);

		// Update TransformationInstantiationExp references from stubs to resolved modules
		if (!stubToResolved.isEmpty()) {
			updateTransformationInstantiationRefs(transformation, stubToResolved);
		}
	}

	private void linkModule(Module module, Set<String> visited,
			Map<Module, Module> stubToResolved) throws QvtoParseException {
		for (ModuleImport imp : module.getModuleImport()) {
			Module imported = imp.getImportedModule();
			if (imported == null) {
				continue;
			}
			// Stub modules have a name but no classifiers (empty module)
			if (!imported.getEClassifiers().isEmpty()) {
				// Already resolved (e.g. inline library from same parse unit)
				String name = imported.getName();
				if (name != null && visited.add(name)) {
					linkModule(imported, visited, stubToResolved);
				}
				continue;
			}

			String qualifiedName = imported.getName();
			if (qualifiedName == null) {
				continue;
			}

			// Cycle detection
			if (!visited.add(qualifiedName)) {
				throw new QvtoParseException(
						"Circular import detected: " + qualifiedName);
			}

			// Try unit resolvers first
			Module resolved = resolveModule(qualifiedName);

			// Fallback: try blackbox registry
			if (resolved == null) {
				resolved = resolveBlackboxModule(qualifiedName);
			}

			if (resolved == null) {
				throw new QvtoParseException(
						"Cannot resolve import: " + qualifiedName);
			}

			// Track stub→resolved mapping for TransformationInstantiationExp updates
			stubToResolved.put(imported, resolved);
			imp.setImportedModule(resolved);

			// Recursively link the imported module's own imports
			// (blackbox modules have no own imports, but regular modules may)
			if (!resolved.isIsBlackbox()) {
				linkModule(resolved, visited, stubToResolved);
			}
		}
	}

	/**
	 * Walks the AST of the transformation and updates all
	 * {@link TransformationInstantiationExp} references from stubs to resolved modules.
	 */
	private void updateTransformationInstantiationRefs(
			OperationalTransformation transformation,
			Map<Module, Module> stubToResolved) {
		Iterator<EObject> it = transformation.eAllContents();
		while (it.hasNext()) {
			EObject obj = it.next();
			if (obj instanceof TransformationInstantiationExp tie) {
				OperationalTransformation oldRef = tie.getImportedTransformation();
				if (oldRef != null) {
					Module resolved = stubToResolved.get(oldRef);
					if (resolved instanceof OperationalTransformation resolvedOt) {
						tie.setImportedTransformation(resolvedOt);
					}
				}
			}
		}
	}

	private Module resolveModule(String qualifiedName) throws QvtoParseException {
		for (QvtoUnitResolver resolver : unitResolvers) {
			Optional<QvtoUnit> unit = resolver.resolveUnit(qualifiedName);
			if (unit.isPresent()) {
				return toModule(unit.get());
			}
		}
		return null;
	}

	/**
	 * Resolves a blackbox library by qualified name and creates a synthetic
	 * {@link Library} module with blackbox operations as EOperations.
	 */
	private Module resolveBlackboxModule(String qualifiedName) {
		if (blackboxRegistry == null) {
			return null;
		}
		Optional<QvtoBlackboxLibrary> optLib = blackboxRegistry.getLibrary(qualifiedName);
		if (optLib.isEmpty()) {
			return null;
		}
		QvtoBlackboxLibrary bbLib = optLib.get();

		// Create synthetic Library module
		Library library = QVTO.createLibrary();
		library.setName(bbLib.getModuleName());
		library.setIsBlackbox(true);

		// Create module class (like parser does for regular modules)
		EClass moduleClass = EcoreFactory.eINSTANCE.createEClass();
		moduleClass.setName(bbLib.getModuleName());
		library.getEClassifiers().add(moduleClass);

		// Create synthetic Helper for each operation descriptor
		for (BlackboxOperationDescriptor desc : bbLib.getOperationDescriptors()) {
			Helper helper = QVTO.createHelper();
			helper.setName(desc.getName());
			helper.setIsBlackbox(true);
			helper.setIsQuery(true);

			// Set return type
			helper.setEType(desc.getReturnType());

			// Set context parameter if contextual
			EClassifier ctxType = desc.getContextType();
			if (ctxType != null) {
				VarParameter ctxParam = QVTO.createVarParameter();
				ctxParam.setName("self");
				ctxParam.setEType(ctxType);
				helper.setContext(ctxParam);
			}

			// Set parameters
			for (EClassifier paramType : desc.getParameterTypes()) {
				VarParameter param = QVTO.createVarParameter();
				param.setName("arg" + helper.getEParameters().size());
				param.setEType(paramType);
				helper.getEParameters().add(param);
			}

			moduleClass.getEOperations().add(helper);
		}

		return library;
	}

	private Module toModule(QvtoUnit unit) throws QvtoParseException {
		Module module = switch (unit) {
			case QvtoUnit.CompiledUnit compiled -> compiled.transformation();
			case QvtoUnit.SourceUnit source -> parserSupport.parse(
					source.source(), source.qualifiedName(), packageRegistry);
		};
		// For standalone library sources, the parser wraps the library in a synthetic OT.
		// If the OT has no module class but has an inline library import, use the library instead.
		if (module instanceof OperationalTransformation ot
				&& QvtoOperationResolver.findModuleClassIn(ot) == null) {
			for (ModuleImport mi : ot.getModuleImport()) {
				Module imported = mi.getImportedModule();
				if (imported instanceof Library lib
						&& QvtoOperationResolver.findModuleClassIn(lib) != null) {
					return lib;
				}
			}
		}
		return module;
	}
}
