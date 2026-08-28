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

import java.io.Serial;

/**
 * Signals that a {@link UnitStore} could not carry out an operation.
 *
 * <p>A store that is unreachable, that holds something it cannot read back, or
 * that is asked for a pinned fingerprint it no longer has must say so. It must
 * not report the same thing as "not found": with several sources in play, a
 * broken store that answers empty lets a stale copy from another source step in
 * unnoticed (§7 of the compiled-unit concept).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class UnitStoreException extends Exception {

	@Serial
	private static final long serialVersionUID = 1L;

	/**
	 * Creates an exception with a message.
	 *
	 * @param message what went wrong
	 */
	public UnitStoreException(String message) {
		super(message);
	}

	/**
	 * Creates an exception with a message and a cause.
	 *
	 * @param message what went wrong
	 * @param cause the underlying failure
	 */
	public UnitStoreException(String message, Throwable cause) {
		super(message, cause);
	}
}
