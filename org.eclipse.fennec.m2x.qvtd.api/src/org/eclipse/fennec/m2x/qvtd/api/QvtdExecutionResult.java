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
	 * The diagnostic code of a relation that does not hold (§7.10.1): a WARNING naming the
	 * relation, the direction and the binding it found no match for. It does not fail the
	 * execution — {@link #isSuccess()} stays {@code true} — but the models are not consistent
	 * (#348).
	 */
	public static final int INCONSISTENCY = 1;

	/** The diagnostic source of the QVT-R engine. */
	public static final String SOURCE = "org.eclipse.fennec.m2x.qvtd.engine";

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
	 * Returns {@code true} if the execution succeeded and found no relation that does not hold.
	 *
	 * <p>In check-only mode every relation is checked in every direction (§7.9, §7.10.1). In
	 * enforce mode the domains of the target model that are only checked are (§7.2.3) — and, with
	 * {@link QvtdExecutionContext#withPostCheck()}, every direction once enforcing is done.
	 * An inconsistency is a WARNING with the code {@link #INCONSISTENCY} (#348).
	 */
	public boolean isConsistent() {
		return isSuccess() && diagnostics.stream()
				.noneMatch(d -> d.getCode() == INCONSISTENCY && SOURCE.equals(d.getSource()));
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
