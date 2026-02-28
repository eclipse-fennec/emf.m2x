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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.VarParameter;

/**
 * Resolves operations, mappings, and constructors within a transformation
 * and its imported modules (§8.1.14, §8.2.1.4, §8.2.1.13).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoOperationResolver {

	private final OperationalTransformation transformation;
	private EClass cachedModuleClass;
	private boolean moduleClassResolved;
	private Map<String, List<ImperativeOperation>> operationIndex;

	QvtoOperationResolver(OperationalTransformation transformation) {
		this.transformation = Objects.requireNonNull(transformation);
	}

	/**
	 * Finds the main() entry operation in the transformation's module class.
	 */
	ImperativeOperation findMainOperation() {
		EClass moduleClass = findModuleClass();
		if (moduleClass == null) {
			return null;
		}
		for (EOperation op : moduleClass.getEOperations()) {
			if (op instanceof ImperativeOperation impOp && "main".equals(op.getName())) {
				return impOp;
			}
		}
		return null;
	}

	/**
	 * Finds the module EClass (named like the transformation) within the transformation.
	 */
	EClass findModuleClass() {
		if (!moduleClassResolved) {
			String name = transformation.getName();
			cachedModuleClass = transformation.getEClassifiers().stream()
					.filter(EClass.class::isInstance)
					.map(EClass.class::cast)
					.filter(c -> c.getName().equals(name))
					.findFirst()
					.orElse(null);
			moduleClassResolved = true;
		}
		return cachedModuleClass;
	}

	/**
	 * Finds the internal module EClass within any Module (transformation or library).
	 */
	static EClass findModuleClassIn(Module module) {
		String name = module.getName();
		if (name == null) {
			return null;
		}
		return module.getEClassifiers().stream()
				.filter(EClass.class::isInstance)
				.map(EClass.class::cast)
				.filter(c -> c.getName().equals(name))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Finds all imperative operations with the given name across the transformation
	 * and imported modules. Used for implicit disjunction (§8.1.14.2).
	 * Respects selective import visibility via {@code ModuleImport.importedNames}.
	 */
	List<ImperativeOperation> findAllOperations(String name) {
		if (operationIndex == null) {
			operationIndex = buildOperationIndex();
		}
		return operationIndex.getOrDefault(name, List.of());
	}

	private Map<String, List<ImperativeOperation>> buildOperationIndex() {
		Map<String, List<ImperativeOperation>> index = new HashMap<>();
		// 1. Index main transformation module class
		EClass moduleClass = findModuleClass();
		if (moduleClass != null) {
			for (EOperation op : moduleClass.getEOperations()) {
				if (op instanceof ImperativeOperation impOp) {
					index.computeIfAbsent(op.getName(), k -> new ArrayList<>()).add(impOp);
				}
			}
		}
		// 2. Index imported modules (§8.1.4: library operations accessible via import)
		for (ModuleImport mi : transformation.getModuleImport()) {
			Module importedModule = mi.getImportedModule();
			if (importedModule != null) {
				List<String> importedNames = mi.getImportedNames();
				Set<String> importedNameSet = importedNames.isEmpty()
						? null : new HashSet<>(importedNames);
				EClass importedModuleClass = findModuleClassIn(importedModule);
				if (importedModuleClass != null) {
					for (EOperation op : importedModuleClass.getEOperations()) {
						if (op instanceof ImperativeOperation impOp) {
							// §8.4: selective visibility — only allow named operations
							if (importedNameSet != null && !importedNameSet.contains(op.getName())) {
								continue;
							}
							index.computeIfAbsent(op.getName(), k -> new ArrayList<>()).add(impOp);
						}
					}
				}
			}
		}
		return index;
	}

	/**
	 * Finds an imperative operation by name in the transformation's module class
	 * and imported modules (§8.2.1.4 ModuleImport).
	 */
	ImperativeOperation findOperation(String name, Object contextType) {
		List<ImperativeOperation> all = findAllOperations(name);
		return all.isEmpty() ? null : all.get(0);
	}

	/**
	 * Filters candidates by context type compatibility, then sorts by specificity
	 * (§8.1.14.2/§8.1.14.3). Most-derived context type first.
	 */
	static List<ImperativeOperation> filterAndSortByType(
			List<ImperativeOperation> candidates, Object sourceObj) {
		if (!(sourceObj instanceof EObject eObj)) {
			return candidates;
		}
		EClass sourceClass = eObj.eClass();
		// Filter: only candidates whose context type is compatible with sourceObj
		List<ImperativeOperation> compatible = new ArrayList<>();
		for (ImperativeOperation op : candidates) {
			EClassifier ct = contextType(op);
			if (ct == null || (ct instanceof EClass ctClass && ctClass.isSuperTypeOf(sourceClass))) {
				compatible.add(op);
			}
		}
		if (compatible.isEmpty()) {
			return candidates; // fallback to all if nothing matches
		}
		// Sort: most-derived context type first
		compatible.sort((a, b) -> {
			EClassifier typeA = contextType(a);
			EClassifier typeB = contextType(b);
			if (typeA == typeB) return 0;
			if (typeA == null) return 1;
			if (typeB == null) return -1;
			if (typeA instanceof EClass ca && typeB instanceof EClass cb) {
				if (ca.isSuperTypeOf(cb)) return 1;
				if (cb.isSuperTypeOf(ca)) return -1;
			}
			return 0;
		});
		return compatible;
	}

	/**
	 * Returns the context type of an imperative operation, or null if non-contextual.
	 */
	static EClassifier contextType(ImperativeOperation op) {
		VarParameter ctx = op.getContext();
		return ctx != null ? ctx.getEType() : null;
	}

	/**
	 * Finds a constructor for the given EClass by matching the class name and argument count.
	 * §8.2.1.13: Constructor name is usually the class name. Constructors are looked up
	 * in the module's operations list.
	 */
	Constructor findConstructor(EClass eClass, int argCount) {
		EClass moduleClass = findModuleClass();
		if (moduleClass == null) {
			return null;
		}
		String className = eClass.getName();
		for (EOperation op : moduleClass.getEOperations()) {
			if (op instanceof Constructor ctor && className.equals(op.getName())) {
				// Match by argument count (excluding result params)
				int paramCount = ctor.getEParameters().size();
				if (ctor.getResult() != null) {
					paramCount -= ctor.getResult().size();
				}
				if (paramCount == argCount) {
					return ctor;
				}
			}
		}
		return null;
	}
}
