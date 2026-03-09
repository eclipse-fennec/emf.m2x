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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

/**
 * Mutable variable environment for QVT-R evaluation.
 *
 * <p>Uses a mutable scope stack with {@code pushScope()}/{@code popScope()}
 * to manage variable bindings across relation execution, pattern matching,
 * and when/where clause evaluation.
 *
 * <p>Variable lookup walks the stack from top (innermost) to bottom (outermost).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrEvalEnvironment {

	private final Deque<Map<String, Object>> scopeStack = new ArrayDeque<>();
	private Map<String, Object> cachedSnapshot;
	private boolean snapshotDirty = true;

	private static final int SCOPE_POOL_MAX = 8;
	private final Deque<Map<String, Object>> scopePool = new ArrayDeque<>(SCOPE_POOL_MAX);

	/**
	 * Creates a new environment with an empty root scope.
	 */
	public QvtrEvalEnvironment() {
		scopeStack.push(new HashMap<>());
	}

	/**
	 * Pushes a new empty scope onto the stack.
	 */
	public void pushScope() {
		Map<String, Object> scope = scopePool.pollFirst();
		if (scope == null) {
			scope = new HashMap<>();
		}
		scopeStack.push(scope);
		snapshotDirty = true;
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
		Map<String, Object> popped = scopeStack.pop();
		if (scopePool.size() < SCOPE_POOL_MAX) {
			popped.clear();
			scopePool.addFirst(popped);
		}
		snapshotDirty = true;
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
		snapshotDirty = true;
	}

	/**
	 * Looks up a variable by name, searching from innermost to outermost scope.
	 *
	 * @param name the variable name
	 * @return the variable value, or {@code null} if not found or bound to null
	 */
	public Object lookup(String name) {
		Objects.requireNonNull(name, "name must not be null");
		Map<String, Object> current = scopeStack.peek();
		if (current.containsKey(name)) {
			return current.get(name);
		}
		for (Map<String, Object> scope : scopeStack) {
			if (scope == current) continue;
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
		Map<String, Object> current = scopeStack.peek();
		if (current.containsKey(name)) {
			return true;
		}
		for (Map<String, Object> scope : scopeStack) {
			if (scope == current) continue;
			if (scope.containsKey(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Returns a snapshot of all visible variables (for creating OclContext snapshots).
	 *
	 * @return unmodifiable map of all visible variable bindings
	 */
	public Map<String, Object> allVisibleVariables() {
		if (!snapshotDirty && cachedSnapshot != null) {
			return cachedSnapshot;
		}
		Map<String, Object> result = new LinkedHashMap<>();
		Object[] scopes = scopeStack.toArray();
		for (int i = scopes.length - 1; i >= 0; i--) {
			@SuppressWarnings("unchecked")
			Map<String, Object> scope = (Map<String, Object>) scopes[i];
			result.putAll(scope);
		}
		cachedSnapshot = Collections.unmodifiableMap(result);
		snapshotDirty = false;
		return cachedSnapshot;
	}
}
