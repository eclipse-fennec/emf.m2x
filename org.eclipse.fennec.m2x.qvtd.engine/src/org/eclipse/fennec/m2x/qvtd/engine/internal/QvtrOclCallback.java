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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.Map;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

/**
 * Callback interface for evaluating OCL expressions within QVT-R helpers.
 *
 * <p>Breaks circular dependencies between extracted helper classes and the
 * main {@link QvtrEvaluator}. The evaluator implements this interface via
 * method reference ({@code this::evaluateOcl}).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@FunctionalInterface
public interface QvtrOclCallback {

	/**
	 * Evaluates an OCL expression with the given variable bindings.
	 *
	 * @param expression the OCL expression to evaluate
	 * @param bindings the current variable bindings
	 * @return the evaluation result, or {@code null} on error
	 */
	Object evaluate(OclExpression expression, Map<String, Object> bindings);
}
