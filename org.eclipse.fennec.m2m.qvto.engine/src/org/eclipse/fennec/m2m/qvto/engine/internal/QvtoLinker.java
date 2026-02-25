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
package org.eclipse.fennec.m2m.qvto.engine.internal;

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
import org.eclipse.fennec.m2m.model.imperativeocl.TransformationInstantiationExp;
import org.eclipse.fennec.m2m.model.qvtoperational.Module;
import org.eclipse.fennec.m2m.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2m.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2m.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2m.qvto.parser.QvtoParserSupport;

/**
 * Resolves stub modules created by the parser into fully parsed modules
 * by consulting registered {@link QvtoUnitResolver}s.
 *
 * <p>The parser creates stub {@code Library} objects with only the qualified name set
 * for each {@code access} or {@code extends} import. This linker replaces those stubs
 * with real parsed modules before execution.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoLinker {

	private final QvtoParserSupport parserSupport;
	private final List<QvtoUnitResolver> unitResolvers;
	private final EPackage.Registry packageRegistry;

	public QvtoLinker(QvtoParserSupport parserSupport,
			List<QvtoUnitResolver> unitResolvers,
			EPackage.Registry packageRegistry) {
		this.parserSupport = Objects.requireNonNull(parserSupport);
		this.unitResolvers = Objects.requireNonNull(unitResolvers);
		this.packageRegistry = Objects.requireNonNull(packageRegistry);
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

			Module resolved = resolveModule(qualifiedName);
			if (resolved == null) {
				throw new QvtoParseException(
						"Cannot resolve import: " + qualifiedName);
			}

			// Track stub→resolved mapping for TransformationInstantiationExp updates
			stubToResolved.put(imported, resolved);
			imp.setImportedModule(resolved);

			// Recursively link the imported module's own imports
			linkModule(resolved, visited, stubToResolved);
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

	private Module toModule(QvtoUnit unit) throws QvtoParseException {
		return switch (unit) {
			case QvtoUnit.CompiledUnit compiled -> compiled.transformation();
			case QvtoUnit.SourceUnit source -> parserSupport.parse(
					source.source(), source.qualifiedName(), packageRegistry);
		};
	}
}
