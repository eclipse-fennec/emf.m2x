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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.Diagnostic;

/**
 * Result of an OCL expression evaluation, carrying both the computed value
 * and any diagnostics (errors, warnings, infos) collected during evaluation.
 *
 * <p>OCL has three distinct value states:
 * <ul>
 *   <li>A valid value — any {@code Object} (e.g., {@code Long}, {@code String}, {@code EObject})</li>
 *   <li>OCL {@code null}/void — represented as Java {@code null}</li>
 *   <li>OCL {@code invalid} — represented as {@link OclInvalid#INSTANCE}</li>
 * </ul>
 *
 * <p>Usage:
 * <pre>
 * OclResult result = engine.evaluateWithDiagnostics(expr, ctx, options);
 * if (result.isSuccess()) {
 *     String name = result.getValueAs(String.class);
 * } else {
 *     result.diagnostics().forEach(d -&gt; log.warn(d.getMessage()));
 * }
 * </pre>
 *
 * @param value the evaluation result ({@code null} for OCL void,
 *     {@link OclInvalid#INSTANCE} for OCL invalid, or any valid value)
 * @param diagnostics collected diagnostics during evaluation, never {@code null}
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record OclResult(Object value, List<Diagnostic> diagnostics) {

	/**
	 * Canonical constructor with validation.
	 */
	public OclResult {
		Objects.requireNonNull(diagnostics, "diagnostics must not be null");
		diagnostics = List.copyOf(diagnostics);
	}

	/**
	 * Returns {@code true} if the evaluation completed without any
	 * {@link Diagnostic#ERROR}-level diagnostics.
	 *
	 * @return {@code true} if no errors were reported
	 */
	public boolean isSuccess() {
		return diagnostics.stream()
				.noneMatch(d -> d.getSeverity() >= Diagnostic.ERROR);
	}

	/**
	 * Returns {@code true} if the result value is OCL {@code invalid}.
	 *
	 * @return {@code true} if value is {@link OclInvalid#INSTANCE}
	 */
	public boolean isInvalid() {
		return value == OclInvalid.INSTANCE;
	}

	/**
	 * Returns {@code true} if the result value is OCL {@code null}/void.
	 *
	 * @return {@code true} if value is Java {@code null}
	 */
	public boolean isNull() {
		return value == null;
	}

	/**
	 * Returns {@code true} if the result carries a valid, non-null value
	 * (i.e., neither OCL {@code null} nor OCL {@code invalid}).
	 *
	 * @return {@code true} if value is present and valid
	 */
	public boolean hasValue() {
		return value != null && !isInvalid();
	}

	/**
	 * Casts the result value to the given type.
	 *
	 * @param <T> the expected type
	 * @param type the expected class
	 * @return the value cast to the given type
	 * @throws ClassCastException if the value is not assignable to the given type
	 * @throws NullPointerException if type is {@code null}
	 */
	public <T> T getValueAs(Class<T> type) {
		Objects.requireNonNull(type, "type must not be null");
		return type.cast(value);
	}
}
