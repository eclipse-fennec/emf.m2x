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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.fennec.m2x.ocl.api.OclContext;

/**
 * Runtime variable environment for OCL evaluation.
 *
 * <p>Implements a chain of scopes (linked list of frames). Each frame holds
 * variable bindings for one lexical scope (e.g., {@code let}, iterator, {@code iterate}).
 * Lookup proceeds from the current frame outward to the root.
 *
 * <p>Each {@code evaluate()} call creates a fresh root environment from the
 * {@link OclContext}, ensuring thread safety (no shared mutable state).
 *
 * <p>This class is distinct from the parser's {@code OclEnvironment}, which maps
 * names to {@code Variable} EMF objects at parse time. This class maps names to
 * runtime {@code Object} values at evaluation time.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclEvalEnvironment {

	private final OclEvalEnvironment parent;
	private final Map<String, Object> bindings;
	private final OclContext context;

	private OclEvalEnvironment(OclEvalEnvironment parent, Map<String, Object> bindings, OclContext context) {
		this.parent = parent;
		this.bindings = bindings;
		this.context = context;
	}

	/**
	 * Creates a root environment from an evaluation context.
	 *
	 * <p>Binds {@code self} to {@code context.self()} and all
	 * {@code context.variables()} entries.
	 *
	 * @param context the evaluation context
	 * @return a new root environment
	 */
	/**
	 * Default value for the {@code oclLocale} property (OCL v2.4 §11.2.1).
	 * Can be overridden via {@code let oclLocale : String = 'fr_CA' in ...}.
	 */
	static final String DEFAULT_OCL_LOCALE = "en_us";

	public static OclEvalEnvironment root(OclContext context) {
		Objects.requireNonNull(context, "context must not be null");
		Map<String, Object> bindings = new HashMap<>();
		bindings.put("self", context.self());
		bindings.put("oclLocale", DEFAULT_OCL_LOCALE);
		bindings.putAll(context.variables());
		return new OclEvalEnvironment(null, bindings, context);
	}

	/**
	 * Creates a child environment with one additional variable binding.
	 *
	 * @param name the variable name
	 * @param value the variable value
	 * @return a new child environment
	 */
	OclEvalEnvironment nested(String name, Object value) {
		Map<String, Object> childBindings = new HashMap<>(2);
		childBindings.put(name, value);
		return new OclEvalEnvironment(this, childBindings, context);
	}

	/**
	 * Returns the original evaluation context, providing access to the model extent.
	 */
	OclContext getContext() {
		return context;
	}

	/**
	 * Looks up a variable by name, searching from the current scope outward.
	 *
	 * @param name the variable name
	 * @return the variable value, or {@code null} if not found
	 *     (note: a variable explicitly bound to {@code null} is indistinguishable
	 *     from an unbound variable — use {@link #contains(String)} if needed)
	 */
	Object lookup(String name) {
		Object value = bindings.get(name);
		if (value != null || bindings.containsKey(name)) {
			return value;
		}
		if (parent != null) {
			return parent.lookup(name);
		}
		return null;
	}

	/**
	 * Checks whether a variable is bound in this environment (any scope).
	 *
	 * @param name the variable name
	 * @return {@code true} if the variable is bound
	 */
	boolean contains(String name) {
		if (bindings.containsKey(name)) {
			return true;
		}
		if (parent != null) {
			return parent.contains(name);
		}
		return false;
	}
}
