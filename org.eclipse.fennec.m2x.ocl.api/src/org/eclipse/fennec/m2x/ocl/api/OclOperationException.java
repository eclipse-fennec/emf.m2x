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
 * Thrown by the implementation of an {@link OclOperation} to say that a call is invalid — a
 * {@code null} argument, a value out of range — in words of its own.
 *
 * <p>The evaluator answers the call with {@code invalid} and reports the message as an ERROR,
 * as it does for the predefined operations ("Null argument for operation 'x'"). Any other
 * exception from a provider is reported too, but as the failure of the provider, with the
 * exception's class in the message (#182) — right for a bug, wrong for a call the operation
 * rejects by design (#373).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclOperationException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * @param message why the call is invalid, as the ERROR will say it
	 */
	public OclOperationException(String message) {
		super(message);
	}
}
