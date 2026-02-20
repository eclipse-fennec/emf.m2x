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
package org.eclipse.fennec.m2m.qvto.engine.internal;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Mutable variable environment for QVT-O evaluation.
 *
 * <p>Unlike the OCL {@code OclEvalEnvironment} which is immutable (chain-of-scopes
 * via nested copies), this environment uses a mutable scope stack with
 * {@code pushScope()}/{@code popScope()} to support imperative variable assignment.
 *
 * <p>Variable lookup walks the stack from top (innermost) to bottom (outermost).
 * Assignment ({@code :=}) updates the first scope that contains the variable.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoEvalEnvironment {

	private final Deque<Map<String, Object>> scopeStack = new ArrayDeque<>();

	/**
	 * Creates a new environment with an empty root scope.
	 */
	public QvtoEvalEnvironment() {
		scopeStack.push(new HashMap<>());
	}

	/**
	 * Creates an environment pre-populated with configuration properties.
	 *
	 * @param configProperties the transformation configuration properties
	 * @return a new environment with the properties defined in the root scope
	 */
	public static QvtoEvalEnvironment forTransformation(Map<String, Object> configProperties) {
		Objects.requireNonNull(configProperties, "configProperties must not be null");
		QvtoEvalEnvironment env = new QvtoEvalEnvironment();
		for (Map.Entry<String, Object> entry : configProperties.entrySet()) {
			env.define(entry.getKey(), entry.getValue());
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
		scopeStack.peek().put(name, value);
	}

	/**
	 * Assigns a value to an existing variable, walking from innermost to
	 * outermost scope and updating the first match.
	 *
	 * <p>If the variable is not found in any scope, it is defined in the
	 * current scope (implicit declaration, as QVT-O allows).
	 *
	 * @param name the variable name
	 * @param value the new value
	 */
	public void assign(String name, Object value) {
		for (Map<String, Object> scope : scopeStack) {
			if (scope.containsKey(name)) {
				scope.put(name, value);
				return;
			}
		}
		// Not found — define in current scope
		scopeStack.peek().put(name, value);
	}

	/**
	 * Looks up a variable by name, searching from innermost to outermost scope.
	 *
	 * @param name the variable name
	 * @return the variable value, or {@code null} if not found or bound to null
	 */
	public Object lookup(String name) {
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
		for (Map<String, Object> scope : scopeStack) {
			if (scope.containsKey(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a snapshot of all visible variables (for creating OclContext snapshots).
	 *
	 * <p>When multiple scopes define the same variable, the innermost wins.
	 *
	 * @return unmodifiable map of all visible variable bindings
	 */
	public Map<String, Object> allVisibleVariables() {
		Map<String, Object> result = new LinkedHashMap<>();
		// Walk from outermost to innermost so inner scopes override
		Object[] scopes = scopeStack.toArray();
		for (int i = scopes.length - 1; i >= 0; i--) {
			@SuppressWarnings("unchecked")
			Map<String, Object> scope = (Map<String, Object>) scopes[i];
			result.putAll(scope);
		}
		// Cannot use Map.copyOf() — values may be null
		return Collections.unmodifiableMap(result);
	}
}
