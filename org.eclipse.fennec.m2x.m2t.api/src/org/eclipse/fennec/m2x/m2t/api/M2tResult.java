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
package org.eclipse.fennec.m2x.m2t.api;

import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.Diagnostic;

/**
 * Result of a MOFM2T template execution.
 *
 * <p>Contains the collected diagnostics and the generated file contents
 * (when using in-memory generation).
 *
 * @param diagnostics collected diagnostics during execution, never {@code null}
 * @param generatedFiles map of file path to generated content (for in-memory mode)
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record M2tResult(
		List<Diagnostic> diagnostics,
		Map<String, String> generatedFiles) {

	public M2tResult {
		Objects.requireNonNull(diagnostics, "diagnostics must not be null");
		diagnostics = List.copyOf(diagnostics);
		generatedFiles = generatedFiles == null
				? Map.of()
				: Map.copyOf(generatedFiles);
	}

	/**
	 * Returns {@code true} if no error or fatal diagnostics were reported.
	 */
	public boolean isSuccess() {
		return diagnostics.stream()
				.noneMatch(d -> d.getSeverity() >= Diagnostic.ERROR);
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
