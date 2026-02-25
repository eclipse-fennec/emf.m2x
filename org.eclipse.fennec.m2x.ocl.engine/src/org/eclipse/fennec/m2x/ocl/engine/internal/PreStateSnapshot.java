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

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
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

	PreStateSnapshot(Map<FeatureCallExp, Object> preValues, Set<EObject> preExistingObjects) {
		this.preValues = preValues;
		this.preExistingObjects = preExistingObjects;
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

		public Builder putPreValue(FeatureCallExp exp, Object value) {
			preValues.put(exp, value);
			return this;
		}

		public Builder addPreExistingObject(EObject obj) {
			preExistingObjects.add(obj);
			return this;
		}

		public PreStateSnapshot build() {
			return new PreStateSnapshot(preValues, preExistingObjects);
		}
	}
}
