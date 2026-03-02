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

import java.time.Duration;
import java.util.Objects;

import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;

/**
 * Immutable evaluation options for QVT-O transformation execution.
 *
 * <p>Wraps {@link OclEvaluationOptions} for the underlying OCL evaluator
 * and adds QVT-O-specific options such as stack depth limits, execution
 * timeout, loop iteration limits, and trace generation control.
 *
 * <h3>Security Hardening</h3>
 * <p>These options protect against resource exhaustion attacks from untrusted
 * QVT-O transformations. All limits are enforced at runtime by the evaluator.
 * See {@code qvto-security-analysis.md} for the full threat model.
 *
 * @param maxStackDepth maximum depth for recursive mapping/helper calls (Q-5/S-9)
 * @param timeout maximum execution time, or {@code null} for no limit (Q-5)
 * @param maxLoopIterations maximum iterations for while/for loops (Q-5)
 * @param maxDiagnostics maximum number of diagnostic entries before truncation (Q-7)
 * @param maxTraceRecords maximum number of trace records, or 0 for unlimited (Q-6)
 * @param tracingEnabled whether to generate a {@code Trace} model during execution
 * @param oclOptions options for the underlying OCL evaluator
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtoEvaluationOptions(
		int maxStackDepth,
		Duration timeout,
		int maxLoopIterations,
		int maxDiagnostics,
		int maxTraceRecords,
		boolean tracingEnabled,
		OclEvaluationOptions oclOptions) {

	private static final int DEFAULT_MAX_STACK_DEPTH = 1000;
	private static final int DEFAULT_MAX_LOOP_ITERATIONS = 1_000_000;
	private static final int DEFAULT_MAX_DIAGNOSTICS = 10_000;
	private static final int DEFAULT_MAX_TRACE_RECORDS = 1_000_000;

	public QvtoEvaluationOptions {
		if (maxStackDepth <= 0) {
			throw new IllegalArgumentException("maxStackDepth must be positive: " + maxStackDepth);
		}
		if (maxLoopIterations <= 0) {
			throw new IllegalArgumentException("maxLoopIterations must be positive: " + maxLoopIterations);
		}
		if (maxDiagnostics <= 0) {
			throw new IllegalArgumentException("maxDiagnostics must be positive: " + maxDiagnostics);
		}
		if (maxTraceRecords < 0) {
			throw new IllegalArgumentException("maxTraceRecords must be non-negative: " + maxTraceRecords);
		}
		Objects.requireNonNull(oclOptions, "oclOptions must not be null");
	}

	/**
	 * Returns default options: maxStackDepth=1000, no timeout,
	 * maxLoopIterations=1,000,000, maxDiagnostics=10,000,
	 * maxTraceRecords=1,000,000, tracing disabled, strict OCL options.
	 */
	public static QvtoEvaluationOptions defaults() {
		return new QvtoEvaluationOptions(DEFAULT_MAX_STACK_DEPTH, null,
				DEFAULT_MAX_LOOP_ITERATIONS, DEFAULT_MAX_DIAGNOSTICS,
				DEFAULT_MAX_TRACE_RECORDS, false, OclEvaluationOptions.strict());
	}

	public QvtoEvaluationOptions withMaxStackDepth(int depth) {
		return new QvtoEvaluationOptions(depth, timeout, maxLoopIterations,
				maxDiagnostics, maxTraceRecords, tracingEnabled, oclOptions);
	}

	public QvtoEvaluationOptions withTimeout(Duration timeout) {
		return new QvtoEvaluationOptions(maxStackDepth, timeout, maxLoopIterations,
				maxDiagnostics, maxTraceRecords, tracingEnabled, oclOptions);
	}

	public QvtoEvaluationOptions withMaxLoopIterations(int maxIterations) {
		return new QvtoEvaluationOptions(maxStackDepth, timeout, maxIterations,
				maxDiagnostics, maxTraceRecords, tracingEnabled, oclOptions);
	}

	public QvtoEvaluationOptions withMaxDiagnostics(int maxDiag) {
		return new QvtoEvaluationOptions(maxStackDepth, timeout, maxLoopIterations,
				maxDiag, maxTraceRecords, tracingEnabled, oclOptions);
	}

	public QvtoEvaluationOptions withMaxTraceRecords(int maxRecords) {
		return new QvtoEvaluationOptions(maxStackDepth, timeout, maxLoopIterations,
				maxDiagnostics, maxRecords, tracingEnabled, oclOptions);
	}

	public QvtoEvaluationOptions withTracing(boolean enabled) {
		return new QvtoEvaluationOptions(maxStackDepth, timeout, maxLoopIterations,
				maxDiagnostics, maxTraceRecords, enabled, oclOptions);
	}

	public QvtoEvaluationOptions withOclOptions(OclEvaluationOptions options) {
		return new QvtoEvaluationOptions(maxStackDepth, timeout, maxLoopIterations,
				maxDiagnostics, maxTraceRecords, tracingEnabled, options);
	}
}
