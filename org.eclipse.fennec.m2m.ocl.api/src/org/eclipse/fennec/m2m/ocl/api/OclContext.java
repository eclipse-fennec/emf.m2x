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
package org.eclipse.fennec.m2m.ocl.api;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.ResourceSet;

/**
 * Evaluation context for OCL expressions, bundling the context object ({@code self}),
 * an optional model extent for {@code allInstances()}, and external variables.
 *
 * <p>External variables are made available to OCL expressions by name. Typical uses:
 * <ul>
 *   <li>{@code result} — bound to the operation return value in {@code post:} conditions</li>
 *   <li>Operation parameters — bound by name in {@code pre:/post:/body:} contexts</li>
 *   <li>User-defined variables — e.g., {@code minSalary} passed from Java code</li>
 * </ul>
 *
 * <p>Usage examples:
 * <pre>
 * // Simple: just self
 * OclContext ctx = OclContext.of(employee);
 *
 * // With external variables
 * OclContext ctx = OclContext.of(employee, Map.of("minSalary", 50000));
 *
 * // With model extent for allInstances()
 * OclContext ctx = new OclContext(employee, extent, Map.of());
 * </pre>
 *
 * @param self the {@code EObject} that serves as {@code self} in the expression
 * @param extent the model extent for {@code allInstances()}, or {@code null} if not needed
 * @param variables external variables accessible by name, never {@code null}
 * @param resourceSet optional {@code ResourceSet} for package resolution, or {@code null}
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record OclContext(
		EObject self,
		OclModelExtent extent,
		Map<String, Object> variables,
		ResourceSet resourceSet) {

	/**
	 * Canonical constructor with validation.
	 */
	public OclContext {
		Objects.requireNonNull(self, "self must not be null");
		Objects.requireNonNull(variables, "variables must not be null");
		variables = Collections.unmodifiableMap(new LinkedHashMap<>(variables));
		// resourceSet is nullable — not every evaluation needs it
	}

	/**
	 * Backward-compatible constructor without resourceSet.
	 *
	 * @param self the context object
	 * @param extent the model extent, or {@code null}
	 * @param variables external variables
	 */
	public OclContext(EObject self, OclModelExtent extent, Map<String, Object> variables) {
		this(self, extent, variables, null);
	}

	/**
	 * Creates a context with only a {@code self} object, no extent or variables.
	 *
	 * @param self the context object
	 * @return the evaluation context
	 */
	public static OclContext of(EObject self) {
		return new OclContext(self, null, Map.of());
	}

	/**
	 * Creates a context with a {@code self} object and external variables.
	 *
	 * @param self the context object
	 * @param variables external variables accessible by name
	 * @return the evaluation context
	 */
	public static OclContext of(EObject self, Map<String, Object> variables) {
		return new OclContext(self, null, variables);
	}

	/**
	 * Creates a context with a {@code self} object and a model extent.
	 *
	 * @param self the context object
	 * @param extent the model extent for {@code allInstances()}
	 * @return the evaluation context
	 */
	public static OclContext of(EObject self, OclModelExtent extent) {
		return new OclContext(self, extent, Map.of());
	}

	/**
	 * Creates a context with a {@code self} object and a resource set.
	 *
	 * @param self the context object
	 * @param resourceSet the resource set for package resolution
	 * @return the evaluation context
	 */
	public static OclContext of(EObject self, ResourceSet resourceSet) {
		return new OclContext(self, null, Map.of(), resourceSet);
	}

	/**
	 * Creates a context with a {@code self} object, model extent, and resource set.
	 *
	 * @param self the context object
	 * @param extent the model extent for {@code allInstances()}
	 * @param resourceSet the resource set for package resolution
	 * @return the evaluation context
	 */
	public static OclContext of(EObject self, OclModelExtent extent, ResourceSet resourceSet) {
		return new OclContext(self, extent, Map.of(), resourceSet);
	}
}
