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

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.engine.OclEngineImpl;

/**
 * EMF {@link EValidator.ValidationDelegate} that evaluates OCL constraint
 * expressions annotated on EClasses and EOperations.
 *
 * <p>Constraint expressions are read from EAnnotations with source
 * {@value OclDelegateUtil#DELEGATE_URI}. For named constraints, the detail key
 * is the constraint name. For EOperation-based invariants, the detail key
 * is {@code "body"}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclValidationDelegateFactory implements EValidator.ValidationDelegate {

	private final OclEngineImpl engine;
	private final ConcurrentHashMap<String, OclExpression> expressionCache = new ConcurrentHashMap<>();

	public OclValidationDelegateFactory(OclEngineImpl engine) {
		this.engine = Objects.requireNonNull(engine, "engine must not be null");
	}

	@Override
	public boolean validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, EOperation invariant, String expression) {
		return evaluateConstraint(expression, eObject, eClass);
	}

	@Override
	public boolean validate(EClass eClass, EObject eObject,
			Map<Object, Object> context, String constraint, String expression) {
		// If expression is null, look up by constraint name from annotation
		String expr = expression;
		if (expr == null) {
			expr = OclDelegateUtil.getAnnotationDetail(eClass, constraint);
		}
		if (expr == null) {
			throw new IllegalStateException(
					"No OCL expression for constraint '" + constraint + "' on " + eClass.getName());
		}
		return evaluateConstraint(expr, eObject, eClass);
	}

	@Override
	public boolean validate(EDataType eDataType, Object value,
			Map<Object, Object> context, String constraint, String expression) {
		// EDataType validation — not typically used with OCL, but supported
		if (expression == null) {
			throw new IllegalStateException(
					"No OCL expression for constraint '" + constraint
							+ "' on " + eDataType.getName());
		}
		try {
			// Validate that the expression parses, but for EDataType there's no
			// EObject to evaluate against — return true if parsing succeeds
			engine.parse(expression, eDataType);
			return true;
		} catch (OclParseException e) {
			return false;
		}
	}

	private boolean evaluateConstraint(String expression, EObject eObject, EClass contextType) {
		try {
			String cacheKey = contextType.getName() + "#" + expression;
			OclExpression parsed = expressionCache.computeIfAbsent(cacheKey, k -> {
				try {
					return engine.parse(expression, contextType);
				} catch (OclParseException e) {
					throw new IllegalStateException(
							"Failed to parse OCL constraint on " + contextType.getName()
									+ ": " + e.getMessage(), e);
				}
			});
			OclContext oclContext = OclContext.of(eObject);
			Object result = engine.evaluate(parsed, oclContext, engine.getDelegateOptions());
			if (result instanceof Boolean b) {
				return b;
			}
			// Non-boolean result or OclInvalid → constraint violated
			return result != null && result != OclInvalid.INSTANCE;
		} catch (IllegalStateException e) {
			throw e;
		}
	}
}
