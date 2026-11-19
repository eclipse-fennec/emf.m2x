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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;

/**
 * The outcome of parsing an expression, as an {@link OclExpressionCache} keeps it: either
 * the AST or the reason the text does not parse.
 *
 * <p>A failure is kept as message and diagnostics, not as the exception itself: an
 * exception carries the stack of the call that first failed and can be changed through
 * {@link Throwable#addSuppressed(Throwable)}, so every later call gets a fresh one from
 * {@link Failed#toException()}.
 *
 * @since 1.0
 */
public sealed interface OclParseResult {

	/**
	 * A successful parse.
	 *
	 * @param expression the parsed AST
	 */
	record Parsed(OclExpression expression) implements OclParseResult {

		/**
		 * Validates the AST.
		 */
		public Parsed {
			Objects.requireNonNull(expression, "expression must not be null");
		}
	}

	/**
	 * A failed parse: syntax or type errors that are a property of the text and the model,
	 * so parsing the same text against the same model again fails the same way.
	 *
	 * @param message the message of the parse exception
	 * @param errors the diagnostics of the parse exception
	 */
	record Failed(String message, List<Resource.Diagnostic> errors) implements OclParseResult {

		/**
		 * Takes an unmodifiable copy of the diagnostics.
		 */
		public Failed {
			errors = List.copyOf(Objects.requireNonNull(errors, "errors must not be null"));
		}

		/**
		 * Captures what a parse exception reports.
		 *
		 * @param exception the parse exception
		 * @return the failure
		 */
		public static Failed of(OclParseException exception) {
			Objects.requireNonNull(exception, "exception must not be null");
			return new Failed(exception.getMessage(), exception.getErrors());
		}

		/**
		 * Creates a new exception that reports this failure.
		 *
		 * @return a new parse exception with the message and diagnostics of this failure
		 */
		public OclParseException toException() {
			return new OclParseException(message, errors);
		}
	}
}
