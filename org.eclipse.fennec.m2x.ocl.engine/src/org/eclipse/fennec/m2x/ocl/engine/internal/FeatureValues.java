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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.ocl.api.OclFeatureValue;

/**
 * Creates the {@link OclFeatureValue} of a navigated many-valued feature (#319): one collection
 * class per OCL kind, each a subclass of the class the evaluator already classifies by.
 */
final class FeatureValues {

	private FeatureValues() {
	}

	/**
	 * The OCL collection for the elements of a many-valued feature.
	 *
	 * @param owner the navigated object
	 * @param feature the many-valued feature
	 * @param elements the elements of the feature's list
	 * @return a Set, OrderedSet, Bag or Sequence after {@code feature.isUnique()/isOrdered()}
	 */
	static Collection<Object> of(EObject owner, EStructuralFeature feature, Collection<?> elements) {
		if (feature.isUnique()) {
			return feature.isOrdered()
					? new FeatureOrderedSet(owner, feature, elements)
					: new FeatureSet(owner, feature, elements);
		}
		return feature.isOrdered()
				? new FeatureSequence(owner, feature, elements)
				: new FeatureBag(owner, feature, elements);
	}

	static final class FeatureSet extends OclSet<Object> implements OclFeatureValue {

		private final transient EObject owner;
		private final transient EStructuralFeature feature;

		FeatureSet(EObject owner, EStructuralFeature feature, Collection<?> elements) {
			super(elements);
			this.owner = owner;
			this.feature = feature;
		}

		@Override
		public EObject owner() {
			return owner;
		}

		@Override
		public EStructuralFeature feature() {
			return feature;
		}
	}

	static final class FeatureOrderedSet extends OclOrderedSet<Object> implements OclFeatureValue {

		private static final long serialVersionUID = 1L;

		private final transient EObject owner;
		private final transient EStructuralFeature feature;

		FeatureOrderedSet(EObject owner, EStructuralFeature feature, Collection<?> elements) {
			super(elements);
			this.owner = owner;
			this.feature = feature;
		}

		@Override
		public EObject owner() {
			return owner;
		}

		@Override
		public EStructuralFeature feature() {
			return feature;
		}
	}

	static final class FeatureBag extends OclBag<Object> implements OclFeatureValue {

		private static final long serialVersionUID = 1L;

		private final transient EObject owner;
		private final transient EStructuralFeature feature;

		FeatureBag(EObject owner, EStructuralFeature feature, Collection<?> elements) {
			super(elements);
			this.owner = owner;
			this.feature = feature;
		}

		@Override
		public EObject owner() {
			return owner;
		}

		@Override
		public EStructuralFeature feature() {
			return feature;
		}
	}

	static final class FeatureSequence extends ArrayList<Object> implements OclFeatureValue {

		private static final long serialVersionUID = 1L;

		private final transient EObject owner;
		private final transient EStructuralFeature feature;

		FeatureSequence(EObject owner, EStructuralFeature feature, Collection<?> elements) {
			super(elements);
			this.owner = owner;
			this.feature = feature;
		}

		@Override
		public EObject owner() {
			return owner;
		}

		@Override
		public EStructuralFeature feature() {
			return feature;
		}
	}
}
