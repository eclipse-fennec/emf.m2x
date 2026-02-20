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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.m2m.model.ocl.AnyType;
import org.eclipse.fennec.m2m.model.ocl.ClassifierType;
import org.eclipse.fennec.m2m.model.ocl.OclFactory;
import org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation;
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

		// Module-level helpers/queries/mappings
		EClass moduleClass = findModuleClass();
		if (moduleClass == null) {
			return ops;
		}

		for (EOperation eOp : moduleClass.getEOperations()) {
			if (eOp instanceof ImperativeOperation impOp) {
				String name = impOp.getName();
				if (name == null || "main".equals(name)) {
					continue; // skip main entry
				}
				ops.add(new OclOperation(
						name,
						ANY_TYPE,
						List.of(), // parameter types (not checked at this level)
						ANY_TYPE,  // return type
						(self, args) -> evaluator.callOperation(impOp, self, args)
				));
			}
		}
		return ops;
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
