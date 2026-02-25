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
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.function.Function;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.ocl.api.OclContext;

/**
 * Storage and resolution of intermediate properties (§8.1.10, §8.2.1.14).
 *
 * <p>Intermediate properties are per-instance values for {@link ContextualProperty}
 * declarations. This class manages the storage map and provides lookup, read, write,
 * and default-initialization logic.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoIntermediatePropertyStore {

	private final OperationalTransformation transformation;
	private final Function<OclExpression, Object> evalFn;

	/** Per-instance intermediate property values. */
	private final IdentityHashMap<EObject, Map<String, Object>> intermediatePropertyValues = new IdentityHashMap<>();

	/** §8.4: Static intermediate property values — shared across all instances, keyed by context class name. */
	private final Map<String, Map<String, Object>> staticPropertyValues = new HashMap<>();

	/** Cached flag: true if any intermediate class has a static feature. */
	private final boolean hasStaticIntermediateClassFeatures;

	QvtoIntermediatePropertyStore(OperationalTransformation transformation,
			Function<OclExpression, Object> evalFn) {
		this.transformation = Objects.requireNonNull(transformation);
		this.evalFn = Objects.requireNonNull(evalFn);
		this.hasStaticIntermediateClassFeatures = checkStaticIntermediateClassFeatures();
	}

	private boolean checkStaticIntermediateClassFeatures() {
		for (EClass ic : transformation.getIntermediateClass()) {
			for (EStructuralFeature sf : ic.getEStructuralFeatures()) {
				if (isStatic(sf)) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * §8.1.10/§8.2.1.14: Finds a ContextualProperty (intermediate property) matching
	 * the given name for the given EObject's type.
	 */
	ContextualProperty findIntermediateProperty(EObject target, String propName) {
		EClass targetClass = target.eClass();
		for (EStructuralFeature sf : transformation.getIntermediateProperty()) {
			if (sf instanceof ContextualProperty cp
					&& propName.equals(cp.getName())
					&& cp.getContext() != null
					&& isTypeMatch(cp.getContext(), targetClass)) {
				return cp;
			}
		}
		return null;
	}

	/**
	 * Reads an intermediate property value for a given EObject instance.
	 * §8.2.1.14: If no value has been set, evaluates the initExpression if present.
	 */
	Object getIntermediatePropertyValue(EObject target, String propName) {
		Map<String, Object> props = intermediatePropertyValues.get(target);
		if (props != null && props.containsKey(propName)) {
			return props.get(propName);
		}
		// §8.2.1.14: Evaluate initExpression for uninitialized properties
		ContextualProperty cp = findIntermediateProperty(target, propName);
		if (cp != null && cp.getInitExpression() != null) {
			Object initValue = evalFn.apply(cp.getInitExpression());
			setIntermediatePropertyValue(target, propName, initValue);
			return initValue;
		}
		return null;
	}

	/**
	 * Writes an intermediate property value for a given EObject instance.
	 */
	void setIntermediatePropertyValue(EObject target, String propName, Object value) {
		intermediatePropertyValues.computeIfAbsent(target, k -> new HashMap<>())
				.put(propName, value);
	}

	/**
	 * §8.1.10: Initializes default values for intermediate class features.
	 * Default expressions are stored in EAnnotations with source "fennec:intermediate:default"
	 * by the parser's QvtoUnitBuilder.
	 */
	void initIntermediateClassDefaults(EObject target, EClass eClass) {
		for (EStructuralFeature feature : eClass.getEAllStructuralFeatures()) {
			EAnnotation ann = feature.getEAnnotation("fennec:intermediate:default");
			if (ann != null && !ann.getReferences().isEmpty()
					&& ann.getReferences().get(0) instanceof OclExpression defaultExpr) {
				Object defaultValue = evalFn.apply(defaultExpr);
				if (defaultValue != null) {
					target.eSet(feature, defaultValue);
				}
			}
		}
	}

	/**
	 * §8.1.10: Property interceptor for OCL delegated evaluation.
	 * Returns the intermediate property value if the property is an intermediate property,
	 * otherwise returns {@link OclContext#PROPERTY_NOT_HANDLED}.
	 * §8.4: Also intercepts static intermediate class features to read from shared store.
	 */
	Object interceptIntermediateProperty(EObject target, String propName) {
		// §8.4: Check for static intermediate class feature
		EStructuralFeature sf = target.eClass().getEStructuralFeature(propName);
		if (sf != null && isStatic(sf)) {
			return getStaticValue(sf);
		}
		ContextualProperty cp = findIntermediateProperty(target, propName);
		if (cp != null) {
			return getIntermediatePropertyValue(target, propName);
		}
		return OclContext.PROPERTY_NOT_HANDLED;
	}

	/**
	 * @return {@code true} if the transformation has any intermediate properties or
	 *         intermediate classes with static features that require interception
	 */
	boolean hasIntermediateProperties() {
		return !transformation.getIntermediateProperty().isEmpty()
				|| hasStaticIntermediateClassFeatures;
	}

	/**
	 * §8.4: Checks if a structural feature has the 'static' modifier.
	 * The parser stores this as EAnnotation("fennec:intermediate:modifier", {"static": "true"})
	 * on the EAttribute of an intermediate class.
	 */
	static boolean isStatic(EStructuralFeature feature) {
		EAnnotation ann = feature.getEAnnotation("fennec:intermediate:modifier");
		return ann != null && "true".equals(ann.getDetails().get("static"));
	}

	/**
	 * §8.4: Reads a static property value from the class-level shared store.
	 * Falls back to the feature's init expression on first access.
	 */
	Object getStaticValue(EStructuralFeature feature) {
		String classKey = feature.getEContainingClass().getName();
		String propName = feature.getName();
		Map<String, Object> props = staticPropertyValues.get(classKey);
		if (props != null && props.containsKey(propName)) {
			return props.get(propName);
		}
		// Lazy init from default expression
		EAnnotation ann = feature.getEAnnotation("fennec:intermediate:default");
		if (ann != null && !ann.getReferences().isEmpty()
				&& ann.getReferences().get(0) instanceof OclExpression defaultExpr) {
			Object initValue = evalFn.apply(defaultExpr);
			setStaticValue(feature, initValue);
			return initValue;
		}
		return null;
	}

	/**
	 * §8.4: Writes a static property value to the class-level shared store.
	 */
	void setStaticValue(EStructuralFeature feature, Object value) {
		String classKey = feature.getEContainingClass().getName();
		staticPropertyValues.computeIfAbsent(classKey, k -> new HashMap<>())
				.put(feature.getName(), value);
	}

	/**
	 * Checks if contextClass matches or is a supertype of targetClass.
	 * Uses name-based comparison as fallback for cross-package EClass identity.
	 */
	private static boolean isTypeMatch(EClass contextClass, EClass targetClass) {
		if (contextClass.isSuperTypeOf(targetClass)) {
			return true;
		}
		// Fallback: name-based comparison for cross-package identity
		return contextClass.getName().equals(targetClass.getName());
	}
}
