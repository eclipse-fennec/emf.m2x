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

import java.util.ArrayList;
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
 * <p>For transformations with collection-of-models parameters (§8.1.1),
 * use the {@link Builder} to bind multiple extents to a single parameter name.
 *
 * @param modelExtents the model extents (in/inout/out) in declaration order
 * @param configProperties configuration properties, accessible in QVT-O as {@code configProperty}
 * @param parameterBindings named parameter bindings (empty for legacy positional mode)
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtoExecutionContext(
		List<QvtoModelExtent> modelExtents,
		Map<String, Object> configProperties,
		List<ParameterBinding> parameterBindings) {

	/**
	 * Binds one or more model extents to a named transformation parameter.
	 *
	 * @param name the parameter name
	 * @param extents the model extents bound to this parameter
	 */
	public record ParameterBinding(String name, List<QvtoModelExtent> extents) {
		public ParameterBinding {
			Objects.requireNonNull(name, "name must not be null");
			Objects.requireNonNull(extents, "extents must not be null");
			extents = List.copyOf(extents);
		}

		/** Creates a single-extent binding. */
		public static ParameterBinding of(String name, QvtoModelExtent extent) {
			return new ParameterBinding(name, List.of(extent));
		}

		/** Creates a multi-extent binding for collection-of-models parameters. */
		public static ParameterBinding ofGroup(String name, QvtoModelExtent... extents) {
			return new ParameterBinding(name, Arrays.asList(extents));
		}
	}

	public QvtoExecutionContext {
		Objects.requireNonNull(modelExtents, "modelExtents must not be null");
		modelExtents = List.copyOf(modelExtents);
		configProperties = configProperties == null
				? Map.of()
				: Collections.unmodifiableMap(new LinkedHashMap<>(configProperties));
		parameterBindings = parameterBindings == null
				? List.of()
				: List.copyOf(parameterBindings);
	}

	/** Legacy constructor for backward compatibility. */
	public QvtoExecutionContext(List<QvtoModelExtent> modelExtents, Map<String, Object> configProperties) {
		this(modelExtents, configProperties, List.of());
	}

	/** Returns {@code true} if named parameter bindings are present. */
	public boolean hasParameterBindings() {
		return !parameterBindings.isEmpty();
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

	/** Creates a new builder for constructing execution contexts with named bindings. */
	public static Builder builder() {
		return new Builder();
	}

	/**
	 * Builder for {@link QvtoExecutionContext} supporting collection-of-models parameters.
	 */
	public static final class Builder {
		private final List<ParameterBinding> bindings = new ArrayList<>();
		private final Map<String, Object> configProperties = new LinkedHashMap<>();

		Builder() {}

		/** Binds a single extent to a named parameter. */
		public Builder addModelExtent(String paramName, QvtoModelExtent extent) {
			bindings.add(ParameterBinding.of(paramName, extent));
			return this;
		}

		/** Binds multiple extents to a named collection-of-models parameter. */
		public Builder addModelExtents(String paramName, QvtoModelExtent... extents) {
			bindings.add(new ParameterBinding(paramName, Arrays.asList(extents)));
			return this;
		}

		/** Binds multiple extents to a named collection-of-models parameter. */
		public Builder addModelExtents(String paramName, List<QvtoModelExtent> extents) {
			bindings.add(new ParameterBinding(paramName, extents));
			return this;
		}

		/** Sets a configuration property. */
		public Builder configProperty(String key, Object value) {
			configProperties.put(key, value);
			return this;
		}

		/** Builds the execution context. The flat modelExtents list is derived from bindings. */
		public QvtoExecutionContext build() {
			List<QvtoModelExtent> flatExtents = new ArrayList<>();
			for (ParameterBinding binding : bindings) {
				flatExtents.addAll(binding.extents());
			}
			return new QvtoExecutionContext(flatExtents,
					configProperties.isEmpty() ? Map.of() : configProperties,
					bindings);
		}
	}
}
