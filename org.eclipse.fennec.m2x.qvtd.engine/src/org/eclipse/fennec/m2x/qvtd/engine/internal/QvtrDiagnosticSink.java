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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.Collection;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;

/**
 * Where a collaborator of the evaluator reports what went wrong.
 *
 * <p>The evaluator used to hand its own {@code List<Diagnostic>} to the bridge and the
 * enforcer. That list is the run's result: whoever holds it can read it, clear it, or reorder
 * it, and nothing in the type says which of the three a collaborator is allowed to do (#185).
 * Reporting is all any of them needs, and it is all this offers.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@FunctionalInterface
public interface QvtrDiagnosticSink {

	/** Diagnostic source id of the QVT-R engine. */
	String SOURCE = "org.eclipse.fennec.m2x.qvtd.engine";

	/**
	 * Reports one diagnostic.
	 *
	 * @param diagnostic what happened, must not be {@code null}
	 */
	void add(Diagnostic diagnostic);

	/**
	 * Reports all of them, in order.
	 *
	 * @param diagnostics what happened, must not be {@code null}
	 */
	default void addAll(Collection<? extends Diagnostic> diagnostics) {
		diagnostics.forEach(this::add);
	}

	/**
	 * Reports an error of the QVT-R engine.
	 *
	 * @param message what is wrong, phrased for the author of the transformation
	 */
	default void error(String message) {
		add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE, 0, message, null));
	}

	/**
	 * A sink that collects into the given list.
	 *
	 * @param collected the list to add to, must not be {@code null}
	 * @return a sink writing into it
	 */
	static QvtrDiagnosticSink into(Collection<? super Diagnostic> collected) {
		return collected::add;
	}
}
