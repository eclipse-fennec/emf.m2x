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

/**
 * Sentinel for the OCL UnlimitedNatural '*' (infinity) value.
 *
 * <p>Distinct from {@code Long(-1)} so that normal integer arithmetic
 * is not affected. Per OCL v2.5 §11.5.5, arithmetic on '*' is undefined;
 * only comparisons, max, min, toString are supported.
 *
 * @since 1.0
 */
public final class OclUnlimitedNatural {

	/** The singleton '*' value. */
	public static final OclUnlimitedNatural INSTANCE = new OclUnlimitedNatural();

	private OclUnlimitedNatural() {
	}

	@Override
	public String toString() {
		return "*";
	}
}
