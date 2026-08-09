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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

/**
 * EMF {@link EValidator.ValidationDelegate} that evaluates OCL constraint
 * expressions annotated on EClasses and EOperations.
 *
 * <p>Constraint expressions are read from EAnnotations with source
 * {@value OclDelegateUtil#DELEGATE_URI}. For named constraints, the detail key
 * is the constraint name. For EOperation-based invariants, the detail key
 * is {@code "body"}.
 *
 * <p>In an OSGi environment, this factory is registered as a service with
 * properties matching the emf.osgi delegate registry whiteboard pattern.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(
		service = EValidator.ValidationDelegate.class,
		property = {
				"emf.configuratorName=" + OclDelegateUtil.DELEGATE_URI,
				"emf.name=fennec-ocl",
				"emf.configuratorType=VALIDATION_DELEGATE"
		})
public class OclValidationDelegateFactory implements EValidator.ValidationDelegate {

	private final OclDelegateSupport engine;
	private final ConcurrentHashMap<String, OclExpression> expressionCache = new ConcurrentHashMap<>();

	/**
	 * DS constructor — engine is injected as a service.
	 */
	@Activate
	public OclValidationDelegateFactory(@Reference OclDelegateSupport engine) {
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

	/**
	 * The key a compiled constraint is cached under.
	 *
	 * <p>The nsURI belongs in it. Without it, two unrelated models that both define a class
	 * of the same name — and both constrain it with the same text — share one entry, and
	 * whichever was validated first decides which model the compiled expression refers to.
	 * The failure is quiet: property access falls back to resolving a feature by name on the
	 * runtime class, so a constraint that only navigates keeps working by accident, while one
	 * that names a type reports a perfectly valid object as invalid.
	 *
	 * <p>An nsURI is not a model <em>version</em>, so this is the smaller half of the
	 * identity question — see #50 for keying on a fingerprint instead.
	 */
	private static String cacheKey(EClass contextType, String expression) {
		EPackage ePackage = contextType.getEPackage();
		String nsURI = ePackage != null ? ePackage.getNsURI() : "";
		return nsURI + "#" + contextType.getName() + "#" + expression;
	}

	private boolean evaluateConstraint(String expression, EObject eObject, EClass contextType) {
		try {
			String cacheKey = cacheKey(contextType, expression);
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
