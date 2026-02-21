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

import java.util.Objects;

/**
 * OCL equality for use in collection data structures ({@link OclSet},
 * {@link OclOrderedSet}).
 *
 * <p>Provides a simplified equality check that handles the critical
 * case of numeric cross-type equality (§11.5.1: Integer is subclass
 * of Real, so {@code 4 = 4.0} is {@code true}).
 *
 * <p>For full OCL equality (including invalid propagation, collection
 * equality, tuple equality), see {@code OclStdlib#oclEquals}.
 *
 * @since 1.0
 */
public final class OclEqualityUtil {

	private OclEqualityUtil() {
		// utility class
	}

	/**
	 * Checks equality using OCL numeric cross-type semantics.
	 * {@code 4 = 4.0} returns {@code true} (§11.5.1).
	 *
	 * <p>Handles null safely: {@code null} equals {@code null},
	 * {@code null} does not equal any non-null value.
	 */
	public static boolean oclEquals(Object left, Object right) {
		if (left == right) {
			return true;
		}
		if (left == null || right == null) {
			return false;
		}
		// Numeric cross-type equality: Integer is subclass of Real (§11.4.2, §11.5.1)
		if (left instanceof Number ln && right instanceof Number rn) {
			return Double.compare(ln.doubleValue(), rn.doubleValue()) == 0;
		}
		return Objects.equals(left, right);
	}
}
