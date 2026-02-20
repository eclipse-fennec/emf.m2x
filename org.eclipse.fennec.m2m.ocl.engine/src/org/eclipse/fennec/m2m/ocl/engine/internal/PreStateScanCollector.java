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
package org.eclipse.fennec.m2m.ocl.engine.internal;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2m.model.ocl.FeatureCallExp;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2m.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2m.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2m.ocl.api.OclResult;

/**
 * Scans a postcondition AST for {@code @pre} nodes and captures their
 * pre-state values before the operation body executes.
 *
 * <p>This implements the "lazy pre-scan" approach: walk the postcondition
 * expression tree, find all {@link FeatureCallExp} nodes with
 * {@code isPre = true}, then evaluate them in the current (pre-body) state.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class PreStateScanCollector {

	private PreStateScanCollector() {
		// static utility
	}

	/**
	 * Finds all {@link FeatureCallExp} nodes with {@code isPre = true}
	 * in the given expression tree.
	 */
	static List<FeatureCallExp> findPreNodes(OclExpression expression) {
		List<FeatureCallExp> result = new ArrayList<>();
		Iterator<EObject> it = expression.eAllContents();
		while (it.hasNext()) {
			EObject child = it.next();
			if (child instanceof FeatureCallExp fce && fce.isIsPre()) {
				result.add(fce);
			}
		}
		// Also check the root expression itself
		if (expression instanceof FeatureCallExp fce && fce.isIsPre()) {
			result.add(fce);
		}
		return result;
	}

	/**
	 * Captures pre-state values for the given {@code @pre} nodes by evaluating
	 * them in the current environment (before the operation body runs).
	 *
	 * <p>For a {@link PropertyCallExp}, evaluates the source and reads the
	 * structural feature value. For an {@link OperationCallExp}, evaluates
	 * the entire expression.
	 */
	static PreStateSnapshot capturePreState(
			List<FeatureCallExp> preNodes,
			OclContext context,
			OclEvaluationOptions options,
			List<OclOperationProvider> providers) {

		if (preNodes.isEmpty()) {
			return collectPreExistingObjects(context);
		}

		PreStateSnapshot.Builder builder = PreStateSnapshot.builder();

		// Capture pre-values using a temporary evaluator in the current state
		OclEvalEnvironment env = OclEvalEnvironment.root(context);
		OclEvaluator evaluator = new OclEvaluator(env, options, providers);

		for (FeatureCallExp node : preNodes) {
			Object value;
			if (node instanceof PropertyCallExp pce) {
				// Evaluate the source, then read the property
				OclExpression sourceExpr = pce.getOwnedSource();
				OclResult sourceResult = evaluator.evaluate(sourceExpr);
				Object source = sourceResult.value();
				if (source instanceof EObject eo) {
					EStructuralFeature sf = pce.getReferredProperty();
					if (sf != null) {
						value = eo.eGet(sf);
					} else {
						value = null;
					}
				} else {
					value = null;
				}
			} else {
				// OperationCallExp@pre: evaluate the whole expression
				OclResult result = evaluator.evaluate((OclExpression) node);
				value = result.value();
			}
			builder.putPreValue(node, value);
		}

		// Collect pre-existing objects
		collectPreExistingObjects(context, builder);

		return builder.build();
	}

	/**
	 * Creates a snapshot with only pre-existing objects (no @pre values).
	 * Used when there are no @pre nodes but oclIsNew() may still be used.
	 */
	private static PreStateSnapshot collectPreExistingObjects(OclContext context) {
		PreStateSnapshot.Builder builder = PreStateSnapshot.builder();
		collectPreExistingObjects(context, builder);
		return builder.build();
	}

	private static void collectPreExistingObjects(OclContext context, PreStateSnapshot.Builder builder) {
		EObject self = context.self();
		if (self == null) {
			return;
		}

		// Collect from the resource if available, otherwise from root container
		Resource resource = self.eResource();
		if (resource != null) {
			for (EObject root : resource.getContents()) {
				collectAllContents(root, builder);
			}
		} else {
			// Walk up to root container and collect entire tree
			EObject root = self;
			while (root.eContainer() != null) {
				root = root.eContainer();
			}
			collectAllContents(root, builder);
		}
	}

	private static void collectAllContents(EObject root, PreStateSnapshot.Builder builder) {
		builder.addPreExistingObject(root);
		Iterator<EObject> it = root.eAllContents();
		while (it.hasNext()) {
			builder.addPreExistingObject(it.next());
		}
	}
}
