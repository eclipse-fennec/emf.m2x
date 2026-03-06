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

import java.util.Map;
import java.util.Objects;

/**
 * Execution context for a QVT-R transformation.
 *
 * <p>Unlike QVT-O (positional extents), QVT-R uses named model extents keyed
 * by the {@code TypedModel} name from the transformation declaration. The
 * execution direction is specified by {@link #targetModelName()} (§7.1.2).
 *
 * <p>Two execution modes are supported:
 * <ul>
 *   <li><b>enforce</b> — the target model is populated/updated by the
 *       transformation (default). Created via {@link #enforce(String, Map)}.
 *   <li><b>checkOnly</b> — all models are checked for consistency without
 *       modification (§7.9). Created via {@link #checkOnly(Map)}.
 * </ul>
 *
 * @param modelExtents named model extents keyed by TypedModel name
 * @param targetModelName the target model name for enforce direction, or
 *        {@code null} for check-only mode
 * @param checkOnly {@code true} for check-only mode (§7.9)
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtdExecutionContext(
		Map<String, QvtdModelExtent> modelExtents,
		String targetModelName,
		boolean checkOnly) {

	/**
	 * Compact constructor: validates and makes extents unmodifiable.
	 */
	public QvtdExecutionContext {
		Objects.requireNonNull(modelExtents, "modelExtents must not be null");
		if (modelExtents.isEmpty()) {
			throw new IllegalArgumentException("modelExtents must not be empty");
		}
		modelExtents = Map.copyOf(modelExtents);
		if (!checkOnly) {
			Objects.requireNonNull(targetModelName, "targetModelName must not be null in enforce mode");
			if (!modelExtents.containsKey(targetModelName)) {
				throw new IllegalArgumentException(
						"targetModelName '%s' not found in modelExtents: %s"
								.formatted(targetModelName, modelExtents.keySet()));
			}
		}
	}

	/**
	 * Creates an enforce-mode execution context.
	 *
	 * <p>The target model will be populated/updated by the transformation.
	 * All other models serve as source (checkonly) domains.
	 *
	 * @param targetModelName the name of the TypedModel to enforce
	 * @param extents the named model extents
	 * @return the execution context
	 */
	public static QvtdExecutionContext enforce(String targetModelName,
			Map<String, QvtdModelExtent> extents) {
		return new QvtdExecutionContext(extents, targetModelName, false);
	}

	/**
	 * Creates a check-only execution context (§7.9).
	 *
	 * <p>No model is modified; the transformation only checks whether the
	 * relations hold across all models.
	 *
	 * @param extents the named model extents
	 * @return the execution context
	 */
	public static QvtdExecutionContext checkOnly(Map<String, QvtdModelExtent> extents) {
		return new QvtdExecutionContext(extents, null, true);
	}
}
