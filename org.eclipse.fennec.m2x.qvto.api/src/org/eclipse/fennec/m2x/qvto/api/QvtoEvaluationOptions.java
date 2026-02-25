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
 * timeout, and trace generation control.
 *
 * @param maxStackDepth maximum depth for recursive mapping calls
 * @param timeout maximum execution time, or {@code null} for no limit
 * @param tracingEnabled whether to generate a {@code Trace} model during execution
 * @param oclOptions options for the underlying OCL evaluator
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtoEvaluationOptions(
		int maxStackDepth,
		Duration timeout,
		boolean tracingEnabled,
		OclEvaluationOptions oclOptions) {

	private static final int DEFAULT_MAX_STACK_DEPTH = 1000;

	public QvtoEvaluationOptions {
		if (maxStackDepth <= 0) {
			throw new IllegalArgumentException("maxStackDepth must be positive: " + maxStackDepth);
		}
		Objects.requireNonNull(oclOptions, "oclOptions must not be null");
	}

	/**
	 * Returns default options: maxStackDepth=1000, no timeout, tracing disabled,
	 * strict OCL options.
	 */
	public static QvtoEvaluationOptions defaults() {
		return new QvtoEvaluationOptions(DEFAULT_MAX_STACK_DEPTH, null, false,
				OclEvaluationOptions.strict());
	}

	public QvtoEvaluationOptions withMaxStackDepth(int depth) {
		return new QvtoEvaluationOptions(depth, timeout, tracingEnabled, oclOptions);
	}

	public QvtoEvaluationOptions withTimeout(Duration timeout) {
		return new QvtoEvaluationOptions(maxStackDepth, timeout, tracingEnabled, oclOptions);
	}

	public QvtoEvaluationOptions withTracing(boolean enabled) {
		return new QvtoEvaluationOptions(maxStackDepth, timeout, enabled, oclOptions);
	}

	public QvtoEvaluationOptions withOclOptions(OclEvaluationOptions options) {
		return new QvtoEvaluationOptions(maxStackDepth, timeout, tracingEnabled, options);
	}
}
