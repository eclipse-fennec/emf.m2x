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

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.Diagnostic;

/**
 * Result of a QVT-R transformation execution.
 *
 * <p>Contains the collected diagnostics and flags indicating whether enforce
 * was performed and whether the models are consistent.
 *
 * <p>The actual transformation output is available in the model extents of
 * the {@link QvtdExecutionContext}.
 *
 * @param diagnostics collected diagnostics during execution, never {@code null}
 * @param enforced {@code true} if enforce was executed, {@code false} for check-only
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtdExecutionResult(
		List<Diagnostic> diagnostics,
		boolean enforced) {

	/**
	 * Compact constructor: validates and makes diagnostics unmodifiable.
	 */
	public QvtdExecutionResult {
		Objects.requireNonNull(diagnostics, "diagnostics must not be null");
		diagnostics = List.copyOf(diagnostics);
	}

	/**
	 * Returns {@code true} if no error or fatal diagnostics were reported.
	 */
	public boolean isSuccess() {
		return diagnostics.stream()
				.noneMatch(d -> d.getSeverity() >= Diagnostic.ERROR);
	}

	/**
	 * Returns {@code true} if all relations hold across the models.
	 *
	 * <p>In check-only mode this indicates full consistency. In enforce mode
	 * this indicates the transformation completed without consistency violations.
	 */
	public boolean isConsistent() {
		return isSuccess();
	}

	/**
	 * Returns the highest severity among all diagnostics, or {@link Diagnostic#OK}
	 * if there are no diagnostics.
	 */
	public int getSeverity() {
		return diagnostics.stream()
				.mapToInt(Diagnostic::getSeverity)
				.max()
				.orElse(Diagnostic.OK);
	}
}
