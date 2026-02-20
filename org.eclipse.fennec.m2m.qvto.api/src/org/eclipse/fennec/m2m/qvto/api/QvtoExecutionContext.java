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
package org.eclipse.fennec.m2m.qvto.api;

import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

/**
 * Execution context for a QVT-O transformation, bundling model extents
 * and configuration properties.
 *
 * <p>The ordering of {@code modelExtents} must match the order of
 * {@code modelParameter} declarations in the transformation. Configuration
 * properties are accessible via {@code configProperty} in QVT-O source.
 *
 * @param modelExtents the model extents (in/inout/out) in declaration order
 * @param configProperties configuration properties, accessible in QVT-O as {@code configProperty}
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtoExecutionContext(
		List<QvtoModelExtent> modelExtents,
		Map<String, Object> configProperties) {

	public QvtoExecutionContext {
		Objects.requireNonNull(modelExtents, "modelExtents must not be null");
		modelExtents = List.copyOf(modelExtents);
		configProperties = configProperties == null
				? Map.of()
				: Collections.unmodifiableMap(new LinkedHashMap<>(configProperties));
	}

	/**
	 * Creates a context with the given extents and no configuration properties.
	 *
	 * @param extents the model extents
	 * @return the execution context
	 */
	public static QvtoExecutionContext of(QvtoModelExtent... extents) {
		Objects.requireNonNull(extents, "extents must not be null");
		return new QvtoExecutionContext(Arrays.asList(extents), Map.of());
	}

	/**
	 * Creates a context with the given extents and configuration properties.
	 *
	 * @param extents the model extents
	 * @param configProperties the configuration properties
	 * @return the execution context
	 */
	public static QvtoExecutionContext of(List<QvtoModelExtent> extents, Map<String, Object> configProperties) {
		return new QvtoExecutionContext(extents, configProperties);
	}
}
