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
package org.eclipse.fennec.m2x.qvto.api;

import java.util.List;
import java.util.Objects;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.model.trace.Trace;

/**
 * Result of a QVT-O transformation execution.
 *
 * <p>Contains the collected diagnostics (errors, warnings) and an optional
 * {@link Trace} model when tracing was enabled via
 * {@link QvtoEvaluationOptions#tracingEnabled()}.
 *
 * <p>The actual transformation output is available in the {@code out} and
 * {@code inout} model extents of the {@link QvtoExecutionContext}.
 *
 * @param diagnostics collected diagnostics during execution, never {@code null}
 * @param trace the trace model, or {@code null} if tracing was not enabled
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtoExecutionResult(
		List<Diagnostic> diagnostics,
		Trace trace) {

	public QvtoExecutionResult {
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
	 * Returns {@code true} if a fatal error was reported.
	 */
	public boolean hasFatalError() {
		return diagnostics.stream()
				.anyMatch(d -> d.getSeverity() >= Diagnostic.CANCEL);
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
