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
package org.eclipse.fennec.m2x.unit.api;

/**
 * Materializing a unit document into a consumer's context failed: a reference resolved to
 * nothing there, or the document did not pass validation.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class UnitMaterializeException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the exception.
	 *
	 * @param message what failed
	 */
	public UnitMaterializeException(String message) {
		super(message);
	}

	/**
	 * Creates the exception with a cause.
	 *
	 * @param message what failed
	 * @param cause why
	 */
	public UnitMaterializeException(String message, Throwable cause) {
		super(message, cause);
	}
}
