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
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.eclipse.fennec.m2x.unit.resolve.ResolutionPolicy;

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
	private final Set<String> allowedBlackboxModules;
	private final Set<String> allowedUnitModules;
	private final int maxBlackboxLibraries;

	/** Blackbox modules resolved during this link, to hold {@code maxBlackboxLibraries}. */
	private final Set<String> resolvedBlackboxModules = new HashSet<>();

	public QvtoLinker(QvtoParserSupport parserSupport,
			List<QvtoUnitResolver> unitResolvers,
			EPackage.Registry packageRegistry,
			QvtoBlackboxRegistry blackboxRegistry,
			Set<String> allowedBlackboxModules,
			Set<String> allowedUnitModules,
			int maxBlackboxLibraries) {
		this.parserSupport = Objects.requireNonNull(parserSupport);
		this.unitResolvers = Objects.requireNonNull(unitResolvers);
		this.packageRegistry = Objects.requireNonNull(packageRegistry);
		this.blackboxRegistry = blackboxRegistry; // nullable
		this.allowedBlackboxModules = Objects.requireNonNull(allowedBlackboxModules);
		this.allowedUnitModules = Objects.requireNonNull(allowedUnitModules);
		this.maxBlackboxLibraries = maxBlackboxLibraries;
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
		// Build index of inline-defined (non-stub) modules for local resolution.
		// When the parser encounters "library Foo() { ... }" and "access Foo" in
		// the same compilation unit, it creates both an inline Library AND a stub.
		// The stub must be resolved to the inline Library before trying external resolvers.
		Map<String, Module> inlineModules = new HashMap<>();
		for (ModuleImport imp : module.getModuleImport()) {
			Module imported = imp.getImportedModule();
			if (imported != null && !isLinkerStub(imported) && imported.getName() != null) {
				inlineModules.put(imported.getName(), imported);
			}
		}

		for (ModuleImport imp : module.getModuleImport()) {
			Module imported = imp.getImportedModule();
			if (imported == null) {
				continue;
			}
			// Only stub modules (created by the parser for external imports) carry
			// the linker-stub annotation. Inline-defined modules (forward declarations,
			// libraries with body) do NOT have this annotation and are already resolved.
			if (!isLinkerStub(imported)) {
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

			// Try inline-defined modules first (same compilation unit)
			Module resolved = inlineModules.get(qualifiedName);

			// Try unit resolvers
			if (resolved == null) {
				resolved = resolveModule(qualifiedName);
			}

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
	void updateTransformationInstantiationRefs(
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
		// D29: Allow-list enforcement for unit modules
		if (!allowedUnitModules.isEmpty() && !allowedUnitModules.contains(qualifiedName)) {
			return null;
		}
		Optional<QvtoUnit> unit;
		try {
			unit = ResolutionPolicy.resolve(qualifiedName, sources(unitResolvers));
		} catch (UnitResolutionException failure) {
			throw new QvtoParseException("Cannot resolve import '" + qualifiedName + "': " + failure.getMessage(),
					failure);
		}
		return unit.isPresent() ? toModule(unit.get()) : null;
	}

	/** The configured resolvers as sources, in configuration order — the order that decides. */
	static List<ResolutionPolicy.Source<QvtoUnit>> sources(List<QvtoUnitResolver> resolvers) {
		List<ResolutionPolicy.Source<QvtoUnit>> sources = new ArrayList<>();
		for (QvtoUnitResolver resolver : resolvers) {
			sources.add(ResolutionPolicy.Source.of(resolver.getClass().getName(), resolver::resolveUnit));
		}
		return sources;
	}

	/**
	 * Resolves a blackbox library by qualified name and creates a synthetic
	 * {@link Library} module with blackbox operations as EOperations.
	 */
	Module resolveBlackboxModule(String qualifiedName) {
		if (blackboxRegistry == null) {
			return null;
		}
		// D29: Allow-list enforcement for blackbox modules
		if (!allowedBlackboxModules.isEmpty() && !allowedBlackboxModules.contains(qualifiedName)) {
			return null;
		}
		// D29: and a ceiling on how many distinct libraries one link may pull in, counted
		// over the whole link rather than per import, so a chain of imports cannot walk
		// past it one module at a time.
		if (!resolvedBlackboxModules.contains(qualifiedName)
				&& resolvedBlackboxModules.size() >= maxBlackboxLibraries) {
			return null;
		}
		Optional<QvtoBlackboxLibrary> optLib = blackboxRegistry.getLibrary(qualifiedName);
		if (optLib.isEmpty()) {
			return null;
		}
		QvtoBlackboxLibrary bbLib = optLib.get();
		resolvedBlackboxModules.add(qualifiedName);

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
		return unwrapLibrary(module);
	}

	/**
	 * The module an import actually means. For a standalone library source the parser wraps the
	 * library in a synthetic transformation; if that transformation has no module class of its own
	 * but an inline library that has, the library is what the importer wants.
	 */
	static Module unwrapLibrary(Module module) {
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

	static boolean isLinkerStub(Module module) {
		return module.getEAnnotation(QvtoParserSupport.LINKER_STUB_ANNOTATION) != null;
	}
}
