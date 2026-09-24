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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Collects what a link phase could not resolve, each at the declaration that asked for it.
 *
 * <p>A failed import is a compile error like a syntax error, and reaches the caller the same way:
 * as a positioned {@link Resource.Diagnostic} in the parse exception of its language (#264).
 * Collected rather than thrown at the first one, so that a unit missing three libraries says so
 * once, and not in three rounds.
 *
 * <p>The diagnostics are those of one unit — the one whose declarations are being linked. What
 * goes wrong inside a dependency is reported at the import of that dependency, with the
 * dependency's own message: a line number of another file would point at the wrong place, and the
 * import is the line the importing unit can change.
 *
 * @since 1.0
 */
public final class LinkDiagnostics {

	private final List<Resource.Diagnostic> diagnostics = new ArrayList<>();
	private Throwable cause;

	/**
	 * Records a declaration that could not be resolved.
	 *
	 * @param message  what is wrong
	 * @param position where the declaration stands, or {@code null} when nobody recorded it
	 * @param cause    the failure behind it, or {@code null}; the first one recorded is kept
	 */
	public void add(String message, SourcePosition position, Throwable cause) {
		diagnostics.add(ParseDiagnostic.of(message, position));
		if (this.cause == null) {
			this.cause = cause;
		}
	}

	/**
	 * @return whether anything was recorded
	 */
	public boolean hasErrors() {
		return !diagnostics.isEmpty();
	}

	/**
	 * @return the messages of all recorded failures, in the order they were found, joined by
	 *         {@code "; "} — for one failure, exactly its message
	 */
	public String message() {
		return diagnostics.stream().map(Resource.Diagnostic::getMessage).collect(Collectors.joining("; "));
	}

	/**
	 * @return the recorded failures in the order they were found, unmodifiable
	 */
	public List<Resource.Diagnostic> getDiagnostics() {
		return Collections.unmodifiableList(diagnostics);
	}

	/**
	 * @return the failure behind the first recorded diagnostic that had one, or {@code null}
	 */
	public Throwable cause() {
		return cause;
	}
}
