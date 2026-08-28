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

/**
 * Marks an exception a custom operation throws to steer its own language's control flow, not to
 * report a failure.
 *
 * <p>OCL treats a provider that throws as a failed operation and turns it into {@code invalid}
 * with a diagnostic (#182) — third-party code must not be able to tear down an evaluation. A
 * language layered on OCL needs the opposite for a few of its constructs: QVT-O's {@code raise}
 * travels from a helper to the {@code except} that catches it, and its {@code return},
 * {@code break} and {@code continue} travel the same way. Those exceptions carry this marker and
 * pass through the OCL evaluator untouched.
 *
 * <p>Implement it only for an exception that some frame of the same evaluation will catch. An
 * exception nobody catches escapes to the caller, which is exactly what the diagnostics channel
 * exists to prevent.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public interface OclControlFlowSignal {
}
