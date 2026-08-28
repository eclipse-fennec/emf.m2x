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
 * A unit could not be resolved for a reason other than "nobody has it": a source failed, or two
 * sources answered with different content for one name.
 *
 * <p>This is the error channel of resolution (§7 of the compiled-unit concept, #141). A resolver
 * that cannot answer throws — this or any runtime exception, which the policy wraps — and the
 * failure travels to the caller naming the source; it is never turned into an empty answer, so a
 * broken store cannot let a stale copy elsewhere step in unnoticed. Unchecked, because the resolver
 * signature {@code Optional<U> resolveUnit(String)} stays as it is.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class UnitResolutionException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	/**
	 * Creates the exception.
	 *
	 * @param message what went wrong, naming the unit and the source
	 */
	public UnitResolutionException(String message) {
		super(message);
	}

	/**
	 * Creates the exception with a cause.
	 *
	 * @param message what went wrong
	 * @param cause the source's failure
	 */
	public UnitResolutionException(String message, Throwable cause) {
		super(message, cause);
	}
}
