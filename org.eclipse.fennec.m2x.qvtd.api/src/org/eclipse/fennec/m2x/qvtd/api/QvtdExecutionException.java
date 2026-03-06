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
package org.eclipse.fennec.m2x.qvtd.api;

/**
 * Unchecked exception thrown when a QVT-R transformation execution fails
 * with a fatal error (stack overflow, timeout, etc.).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtdExecutionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	public QvtdExecutionException(String message) {
		super(message);
	}

	public QvtdExecutionException(String message, Throwable cause) {
		super(message, cause);
	}
}
