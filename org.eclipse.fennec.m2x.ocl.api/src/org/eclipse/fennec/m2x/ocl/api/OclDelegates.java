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

/**
 * The annotation vocabulary an {@code .ecore} uses to carry OCL.
 *
 * <p>These strings are a public contract: they are written by hand into models, and read by
 * anything that wants to find the OCL a model carries. They lived as private constants of the
 * engine while being documented for users to type, which is the wrong way round.
 *
 * @since 1.0
 */
public final class OclDelegates {

	/** Annotation source of the Fennec OCL delegates. */
	public static final String DELEGATE_URI = "http://www.eclipse.org/fennec/m2x/ocl/1.0";

	/** The Eclipse OCL Pivot delegate source, which the Fennec engine also serves. */
	public static final String LEGACY_PIVOT_URI = "http://www.eclipse.org/emf/2002/Ecore/OCL/Pivot";

	/** Ecore's own annotation source, which names a class's invariants. */
	public static final String ECORE_URI = "http://www.eclipse.org/emf/2002/Ecore";

	/** Detail key under {@link #ECORE_URI} listing the invariants of a class, space separated. */
	public static final String CONSTRAINTS_KEY = "constraints";

	/** Detail key of an operation body. */
	public static final String BODY_KEY = "body";

	/** Detail key of a derived feature's derivation. */
	public static final String DERIVATION_KEY = "derivation";

	private OclDelegates() {
		// constants
	}
}
