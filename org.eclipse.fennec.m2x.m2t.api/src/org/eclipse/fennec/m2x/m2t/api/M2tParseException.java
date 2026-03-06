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
package org.eclipse.fennec.m2x.m2t.api;

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Checked exception thrown when a MOFM2T template source cannot be parsed.
 *
 * <p>Contains the list of {@link Resource.Diagnostic} errors with line/column
 * information for precise error reporting.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tParseException extends Exception {

	private static final long serialVersionUID = 1L;

	private final List<Resource.Diagnostic> errors;

	public M2tParseException(String message, List<Resource.Diagnostic> errors) {
		super(Objects.requireNonNull(message, "message must not be null"));
		this.errors = List.copyOf(Objects.requireNonNull(errors, "errors must not be null"));
	}

	public M2tParseException(String message) {
		this(message, Collections.emptyList());
	}

	public M2tParseException(String message, Throwable cause) {
		super(Objects.requireNonNull(message, "message must not be null"), cause);
		this.errors = Collections.emptyList();
	}

	public M2tParseException(String message, Throwable cause, List<Resource.Diagnostic> errors) {
		super(Objects.requireNonNull(message, "message must not be null"), cause);
		this.errors = List.copyOf(Objects.requireNonNull(errors, "errors must not be null"));
	}

	/**
	 * Returns the parse errors with location information.
	 *
	 * @return unmodifiable list of errors, never {@code null}
	 */
	public List<Resource.Diagnostic> getErrors() {
		return errors;
	}
}
