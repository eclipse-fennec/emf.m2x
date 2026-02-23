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

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.m2m.model.ocl.AnyType;
import org.eclipse.fennec.m2m.model.ocl.ClassifierType;
import org.eclipse.fennec.m2m.model.ocl.OclFactory;
import org.eclipse.fennec.m2m.model.ocl.OclType;
import org.eclipse.fennec.m2m.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.Module;
import org.eclipse.fennec.m2m.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.ocl.api.OclOperation;
import org.eclipse.fennec.m2m.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;

/**
 * Bridge that exposes QVT-O helpers and queries as {@link OclOperationProvider}
 * so the OCL evaluator can find and invoke them during expression evaluation.
 *
 * <p>This implements the mutual recursion aspect of D25: when the OCL evaluator
 * encounters an operation call it doesn't know, it consults custom providers
 * including this one. This provider delegates back to the {@link QvtoEvaluator}
 * to execute the imperative operation body.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoOperationProvider implements OclOperationProvider {

	private static final AnyType ANY_TYPE = OclFactory.eINSTANCE.createAnyType();

	private final OperationalTransformation transformation;
	private final QvtoEvaluator evaluator;
	private List<OclOperation> operations;

	public QvtoOperationProvider(OperationalTransformation transformation, QvtoEvaluator evaluator) {
		this.transformation = Objects.requireNonNull(transformation, "transformation must not be null");
		this.evaluator = Objects.requireNonNull(evaluator, "evaluator must not be null");
	}

	@Override
	public List<OclOperation> getOperations() {
		if (operations == null) {
			operations = buildOperations();
		}
		return operations;
	}

	private List<OclOperation> buildOperations() {
		List<OclOperation> ops = new ArrayList<>();

		// Built-in model extent operations
		ops.add(new OclOperation("objectsOfType", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent && args.length > 0) {
						EClass filterType = resolveEClassArg(args[0]);
						if (filterType != null) {
							List<EObject> result = new ArrayList<>();
							for (EObject eo : extent.getContents()) {
								if (filterType.isInstance(eo)) {
									result.add(eo);
								}
							}
							return result;
						}
					}
					return List.of();
				}));
		ops.add(new OclOperation("objects", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent) {
						return new ArrayList<>(extent.getContents());
					}
					return List.of();
				}));
		// §8.1.3: addObject — add an element to a model extent
		ops.add(new OclOperation("addObject", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent && args.length > 0
							&& args[0] instanceof EObject eo) {
						extent.add(eo);
					}
					return null;
				}));

		// Module-level helpers/queries/mappings
		EClass moduleClass = findModuleClass();
		if (moduleClass == null) {
			return ops;
		}

		addModuleOperations(ops, moduleClass);

		// Also export operations from imported modules (§8.1.4 Library access)
		for (ModuleImport mi : transformation.getModuleImport()) {
			Module importedModule = mi.getImportedModule();
			if (importedModule != null) {
				EClass importedModuleClass = findModuleClassIn(importedModule);
				if (importedModuleClass != null) {
					addModuleOperations(ops, importedModuleClass);
				}
			}
		}

		return ops;
	}

	private void addModuleOperations(List<OclOperation> ops, EClass moduleClass) {
		for (EOperation eOp : moduleClass.getEOperations()) {
			if (eOp instanceof ImperativeOperation impOp) {
				String name = impOp.getName();
				if (name == null || "main".equals(name)) {
					continue;
				}
				// §8.2.1.10: Use context type for dispatch so same-name operations
				// on different context types are dispatched correctly
				OclType ownerType = resolveOwnerType(impOp);
				ops.add(new OclOperation(
						name,
						ownerType,
						List.of(),
						ANY_TYPE,
						(self, args) -> evaluator.callOperation(impOp, self, args)
				));
			}
		}
	}

	/**
	 * §8.2.1.10: Resolves the owner type for an imperative operation from its context parameter.
	 * Returns ANY_TYPE for non-contextual operations.
	 */
	private OclType resolveOwnerType(ImperativeOperation impOp) {
		var ctxParam = impOp.getContext();
		if (ctxParam == null || ctxParam.getEType() == null) {
			return ANY_TYPE;
		}
		EClassifier classifier = ctxParam.getEType();
		if (classifier instanceof EClass ec) {
			ClassifierType ct = OclFactory.eINSTANCE.createClassifierType();
			ct.setReferredClassifier(ec);
			return ct;
		}
		if (classifier instanceof EDataType dt) {
			PrimitiveType pt = OclFactory.eINSTANCE.createPrimitiveType();
			pt.setName(dt.getName());
			return pt;
		}
		return ANY_TYPE;
	}

	private static EClass findModuleClassIn(Module module) {
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

	private static EClass resolveEClassArg(Object arg) {
		if (arg instanceof EClass ec) {
			return ec;
		}
		if (arg instanceof ClassifierType ct && ct.getReferredClassifier() instanceof EClass ec) {
			return ec;
		}
		return null;
	}

	private EClass findModuleClass() {
		String name = transformation.getName();
		return transformation.getEClassifiers().stream()
				.filter(EClass.class::isInstance)
				.map(EClass.class::cast)
				.filter(c -> c.getName().equals(name))
				.findFirst()
				.orElse(null);
	}
}
