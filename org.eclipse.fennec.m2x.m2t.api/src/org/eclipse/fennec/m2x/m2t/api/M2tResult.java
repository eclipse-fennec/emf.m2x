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
 * <p>Contains the collected diagnostics, the generated file contents
 * (when using in-memory generation), and optional file unique IDs
 * for tracking renamed files across regenerations (§8.1.17).
 *
 * @param diagnostics collected diagnostics during execution, never {@code null}
 * @param generatedFiles map of file path to generated content (for in-memory mode)
 * @param fileUniqueIds map of file path to unique ID (for file-rename tracking, §8.1.17)
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record M2tResult(
		List<Diagnostic> diagnostics,
		Map<String, String> generatedFiles,
		Map<String, String> fileUniqueIds) {

	/**
	 * Convenience constructor without unique IDs.
	 */
	public M2tResult(List<Diagnostic> diagnostics, Map<String, String> generatedFiles) {
		this(diagnostics, generatedFiles, Map.of());
	}

	public M2tResult {
		Objects.requireNonNull(diagnostics, "diagnostics must not be null");
		diagnostics = List.copyOf(diagnostics);
		generatedFiles = generatedFiles == null
				? Map.of()
				: Map.copyOf(generatedFiles);
		fileUniqueIds = fileUniqueIds == null
				? Map.of()
				: Map.copyOf(fileUniqueIds);
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
