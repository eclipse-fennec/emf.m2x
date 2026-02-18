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
package org.eclipse.fennec.m2m.ocl.api;

/**
 * Singleton sentinel representing the OCL {@code invalid} value.
 *
 * <p>In OCL, {@code invalid} is a special value that propagates through expressions,
 * similar to {@code NaN} in floating-point arithmetic. It is distinct from {@code null}
 * (OCL {@code void}), which is represented by Java {@code null}.
 *
 * <p>The {@link OclEngine#evaluate evaluate} methods return {@link #INSTANCE} when
 * evaluation produces the OCL {@code invalid} value.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclInvalid {

	/** The singleton {@code OclInvalid} instance. */
	public static final OclInvalid INSTANCE = new OclInvalid();

	private OclInvalid() {
		// singleton
	}

	@Override
	public String toString() {
		return "OclInvalid";
	}
}
