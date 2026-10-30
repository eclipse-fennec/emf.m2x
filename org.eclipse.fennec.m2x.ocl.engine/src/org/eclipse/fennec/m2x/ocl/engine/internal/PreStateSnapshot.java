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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.model.ocl.FeatureCallExp;

/**
 * Immutable snapshot of pre-state values captured before an operation body
 * executes, used for {@code @pre} evaluation in postconditions.
 *
 * <p>Pre-values are keyed by AST node identity (not equals), since the same
 * expression text may appear multiple times in the postcondition AST with
 * different node instances.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class PreStateSnapshot {

	public static final PreStateSnapshot EMPTY = new PreStateSnapshot(
			Collections.emptyMap(), Collections.emptySet());

	private final Map<FeatureCallExp, Object> preValues;
	private final Set<EObject> preExistingObjects;
	/**
	 * The pre-state value of a feature per object (#323): {@code a.b.c@pre} reads {@code c} in the
	 * pre-state of whatever {@code a.b} is after the operation (OCL v2.4 A.3.2.2 (vii)), and a
	 * {@code @pre} inside an iterator body is read per element — neither has one value per AST
	 * node.
	 */
	private final Map<EObject, Map<EStructuralFeature, Object>> featureValues;

	PreStateSnapshot(Map<FeatureCallExp, Object> preValues, Set<EObject> preExistingObjects) {
		this(preValues, preExistingObjects, Collections.emptyMap());
	}

	PreStateSnapshot(Map<FeatureCallExp, Object> preValues, Set<EObject> preExistingObjects,
			Map<EObject, Map<EStructuralFeature, Object>> featureValues) {
		this.preValues = preValues;
		this.preExistingObjects = preExistingObjects;
		this.featureValues = featureValues;
	}

	/**
	 * Whether the pre-state of the given feature of the object was captured.
	 *
	 * @param object the object
	 * @param feature the feature
	 * @return {@code true} if a pre-state value is known
	 */
	public boolean hasFeatureValue(EObject object, EStructuralFeature feature) {
		Map<EStructuralFeature, Object> values = featureValues.get(object);
		return values != null && values.containsKey(feature);
	}

	/**
	 * The pre-state value of the given feature of the object.
	 *
	 * @param object the object
	 * @param feature the feature
	 * @return the value, {@code null} if unset or not captured
	 */
	public Object getFeatureValue(EObject object, EStructuralFeature feature) {
		Map<EStructuralFeature, Object> values = featureValues.get(object);
		return values == null ? null : values.get(feature);
	}

	/**
	 * Whether this snapshot captured feature values — that is, whether the postcondition it was
	 * made for reads a property {@code @pre}.
	 *
	 * @return {@code true} if feature values were captured
	 */
	public boolean hasFeatureValues() {
		return !featureValues.isEmpty();
	}

	public boolean hasPreValue(FeatureCallExp exp) {
		return preValues.containsKey(exp);
	}

	public Object getPreValue(FeatureCallExp exp) {
		return preValues.get(exp);
	}

	public boolean existedBefore(EObject obj) {
		return preExistingObjects.contains(obj);
	}

	public static Builder builder() {
		return new Builder();
	}

	public static final class Builder {
		private final Map<FeatureCallExp, Object> preValues = new IdentityHashMap<>();
		private final Set<EObject> preExistingObjects = Collections.newSetFromMap(new IdentityHashMap<>());
		private final Map<EObject, Map<EStructuralFeature, Object>> featureValues = new IdentityHashMap<>();

		/**
		 * Records the pre-state value of a feature of an object.
		 *
		 * @param object the object
		 * @param feature the feature
		 * @param value its value before the operation; a many-valued feature's list is copied
		 * @return this builder
		 */
		public Builder putFeatureValue(EObject object, EStructuralFeature feature, Object value) {
			Object captured = value instanceof Collection<?> list ? new ArrayList<>(list) : value;
			featureValues.computeIfAbsent(object, k -> new IdentityHashMap<>()).put(feature, captured);
			return this;
		}

		public Builder putPreValue(FeatureCallExp exp, Object value) {
			preValues.put(exp, value);
			return this;
		}

		public Builder addPreExistingObject(EObject obj) {
			preExistingObjects.add(obj);
			return this;
		}

		Set<EObject> preExistingObjects() {
			return preExistingObjects;
		}

		public PreStateSnapshot build() {
			return new PreStateSnapshot(preValues, preExistingObjects, featureValues);
		}
	}
}
