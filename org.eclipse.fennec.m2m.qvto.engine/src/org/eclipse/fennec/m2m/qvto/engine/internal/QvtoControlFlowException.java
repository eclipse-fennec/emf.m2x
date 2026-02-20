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
package org.eclipse.fennec.m2m.qvto.engine.internal;

/**
 * Sealed hierarchy for QVT-O control flow exceptions.
 *
 * <p>These exceptions are used internally by the evaluator to implement
 * {@code return}, {@code break}, and {@code continue} statements. They
 * are never exposed to callers — the evaluator catches them at the
 * appropriate scope boundary.
 *
 * <p>Stack trace generation is suppressed for performance, since these
 * exceptions are used for normal control flow, not error reporting.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
sealed abstract class QvtoControlFlowException extends RuntimeException
		permits QvtoControlFlowException.ReturnException,
				QvtoControlFlowException.BreakException,
				QvtoControlFlowException.ContinueException {

	private static final long serialVersionUID = 1L;

	QvtoControlFlowException() {
		super(null, null, true, false); // no stack trace
	}

	/**
	 * Thrown by {@code ReturnExp} to exit the current operation.
	 */
	static final class ReturnException extends QvtoControlFlowException {
		private static final long serialVersionUID = 1L;
		final Object value;

		ReturnException(Object value) {
			this.value = value;
		}
	}

	/**
	 * Thrown by {@code BreakExp} to exit the enclosing loop.
	 */
	static final class BreakException extends QvtoControlFlowException {
		private static final long serialVersionUID = 1L;
	}

	/**
	 * Thrown by {@code ContinueExp} to skip to the next loop iteration.
	 */
	static final class ContinueException extends QvtoControlFlowException {
		private static final long serialVersionUID = 1L;
	}
}
