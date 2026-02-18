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

import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Checked exception thrown when an OCL expression or document cannot be parsed.
 *
 * <p>Contains the list of {@link Resource.Diagnostic} errors with line/column information
 * for precise error reporting (e.g., in editors).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclParseException extends Exception {

	private static final long serialVersionUID = 1L;

	private final List<Resource.Diagnostic> errors;

	/**
	 * Creates a parse exception with a message and list of diagnostics.
	 *
	 * @param message summary message
	 * @param errors the parse errors with position information
	 */
	public OclParseException(String message, List<Resource.Diagnostic> errors) {
		super(message);
		this.errors = List.copyOf(Objects.requireNonNull(errors, "errors must not be null"));
	}

	/**
	 * Creates a parse exception with a message only.
	 *
	 * @param message summary message
	 */
	public OclParseException(String message) {
		this(message, Collections.emptyList());
	}

	/**
	 * Creates a parse exception with a message and cause.
	 *
	 * @param message summary message
	 * @param cause the underlying cause
	 */
	public OclParseException(String message, Throwable cause) {
		super(message, cause);
		this.errors = Collections.emptyList();
	}

	/**
	 * Returns the parse errors with line and column information.
	 *
	 * @return unmodifiable list of parse errors, never {@code null}
	 */
	public List<Resource.Diagnostic> getErrors() {
		return errors;
	}
}
