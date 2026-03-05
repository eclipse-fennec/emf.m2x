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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;

/**
 * Mutable variable environment for MOFM2T template evaluation.
 *
 * <p>Follows the same scope-stack pattern as {@code QvtoEvalEnvironment}:
 * <ul>
 *   <li>{@link #pushScope()} / {@link #popScope()} — block-level scoping for
 *       for-blocks, let-blocks, template invocations</li>
 *   <li>{@link #define(String, Object)} — define in current scope</li>
 *   <li>{@link #assign(String, Object)} — update existing variable (walk up)</li>
 *   <li>{@link #lookup(String)} — resolve variable (walk up)</li>
 * </ul>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tEvalEnvironment {

	private final Deque<Map<String, Object>> scopeStack = new ArrayDeque<>();

	/**
	 * Creates a new environment with an empty root scope.
	 */
	public M2tEvalEnvironment() {
		scopeStack.push(new HashMap<>());
	}

	/**
	 * Creates a root environment initialized from the given execution context.
	 * Defines {@code self} as the first input element.
	 *
	 * @param context the execution context with input model elements
	 * @return a new environment with input elements available
	 */
	public static M2tEvalEnvironment root(M2tContext context) {
		Objects.requireNonNull(context, "context must not be null");
		M2tEvalEnvironment env = new M2tEvalEnvironment();
		List<EObject> inputs = context.inputElements();
		if (!inputs.isEmpty()) {
			env.define("self", inputs.get(0));
		}
		return env;
	}

	/**
	 * Pushes a new empty scope onto the stack.
	 */
	public void pushScope() {
		scopeStack.push(new HashMap<>());
	}

	/**
	 * Pops the current scope from the stack.
	 *
	 * @throws IllegalStateException if only the root scope remains
	 */
	public void popScope() {
		if (scopeStack.size() <= 1) {
			throw new IllegalStateException("Cannot pop root scope");
		}
		scopeStack.pop();
	}

	/**
	 * Defines a variable in the current (innermost) scope.
	 *
	 * @param name the variable name
	 * @param value the variable value (may be {@code null})
	 */
	public void define(String name, Object value) {
		Objects.requireNonNull(name, "name must not be null");
		scopeStack.peek().put(name, value);
	}

	/**
	 * Assigns a value to an existing variable, walking from innermost to
	 * outermost scope and updating the first match.
	 *
	 * <p>If the variable is not found, it is defined in the current scope.
	 *
	 * @param name the variable name
	 * @param value the new value
	 */
	public void assign(String name, Object value) {
		Objects.requireNonNull(name, "name must not be null");
		for (Map<String, Object> scope : scopeStack) {
			if (scope.containsKey(name)) {
				scope.put(name, value);
				return;
			}
		}
		scopeStack.peek().put(name, value);
	}

	/**
	 * Looks up a variable by name, searching from innermost to outermost scope.
	 *
	 * @param name the variable name
	 * @return the variable value, or {@code null} if not found or bound to null
	 */
	public Object lookup(String name) {
		Objects.requireNonNull(name, "name must not be null");
		for (Map<String, Object> scope : scopeStack) {
			if (scope.containsKey(name)) {
				return scope.get(name);
			}
		}
		return null;
	}

	/**
	 * Checks whether a variable is defined in any scope.
	 *
	 * @param name the variable name
	 * @return {@code true} if the variable is bound
	 */
	public boolean contains(String name) {
		Objects.requireNonNull(name, "name must not be null");
		for (Map<String, Object> scope : scopeStack) {
			if (scope.containsKey(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a snapshot of all visible variables.
	 *
	 * @return unmodifiable map of all visible variable bindings
	 */
	public Map<String, Object> allVisibleVariables() {
		Map<String, Object> result = new LinkedHashMap<>();
		Object[] scopes = scopeStack.toArray();
		for (int i = scopes.length - 1; i >= 0; i--) {
			@SuppressWarnings("unchecked")
			Map<String, Object> scope = (Map<String, Object>) scopes[i];
			result.putAll(scope);
		}
		return Collections.unmodifiableMap(result);
	}
}
