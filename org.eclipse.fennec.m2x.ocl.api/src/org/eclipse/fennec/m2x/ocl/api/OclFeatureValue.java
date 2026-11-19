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
package org.eclipse.fennec.m2x.ocl.api;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * The value of a many-valued feature as OCL sees it: a Set, OrderedSet, Bag or Sequence
 * according to the feature's {@code unique} and {@code ordered} flags (OCL v2.4 §7.5.3,
 * §7.5.11), holding the elements of the model's list at the time of the navigation.
 *
 * <p>The collection is a copy, so an OCL expression can neither write into the model through
 * it nor see the model change underneath it. It remembers where it came from, for the two
 * parties that need the live list: the guard that refuses mutating operations on model values
 * (#182), and an imperative language on top of OCL whose statements change a feature — QVT-O's
 * {@code feature.unlink(item)} (QVT v1.3 §8.2.2.12).
 *
 * @since 1.0
 */
public interface OclFeatureValue {

	/**
	 * @return the object whose feature was navigated
	 */
	EObject owner();

	/**
	 * @return the many-valued feature that was navigated
	 */
	EStructuralFeature feature();
}
