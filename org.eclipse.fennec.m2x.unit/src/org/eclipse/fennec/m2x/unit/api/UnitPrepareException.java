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
 * Prepare could not produce a context: a unit is missing, a pinned version is gone, a
 * metamodel differs from what the unit was compiled against, a blackbox is not there, or a
 * binder is missing for a language.
 *
 * <p>Every one of these is a failure at prepare time by design — with a message that names the
 * culprit — rather than a failure in the middle of a run (§4.1 of the compiled-unit concept).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class UnitPrepareException extends Exception {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the exception.
	 *
	 * @param message what went wrong, naming the unit, package or blackbox
	 */
	public UnitPrepareException(String message) {
		super(message);
	}

	/**
	 * Creates the exception with a cause.
	 *
	 * @param message what went wrong
	 * @param cause the underlying failure
	 */
	public UnitPrepareException(String message, Throwable cause) {
		super(message, cause);
	}
}
