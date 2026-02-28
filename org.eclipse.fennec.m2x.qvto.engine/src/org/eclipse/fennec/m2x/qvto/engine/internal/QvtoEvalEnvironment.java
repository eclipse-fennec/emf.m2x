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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.ArrayDeque;
import java.util.Collections;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

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
	private Map<String, Object> cachedSnapshot;
	private boolean snapshotDirty = true;

	/** Reuse pool for scope maps to avoid allocation in tight iterator loops (#7). */
	private static final int SCOPE_POOL_MAX = 8;
	private final Deque<Map<String, Object>> scopePool = new ArrayDeque<>(SCOPE_POOL_MAX);

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
		// Return to pool for reuse (avoids allocation in iterator loops)
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
		Objects.requireNonNull(name, "name must not be null");
		// Fast path: check current scope first (covers >90% of cases)
		Map<String, Object> current = scopeStack.peek();
		if (current.containsKey(name)) {
			current.put(name, value);
			snapshotDirty = true;
			return;
		}
		for (Map<String, Object> scope : scopeStack) {
			if (scope == current) continue; // already checked
			if (scope.containsKey(name)) {
				scope.put(name, value);
				snapshotDirty = true;
				return;
			}
		}
		// Not found — define in current scope
		current.put(name, value);
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
		// Fast path: check current scope first (covers >90% of cases)
		Map<String, Object> current = scopeStack.peek();
		if (current.containsKey(name)) {
			return current.get(name);
		}
		// Slow path: walk remaining scopes
		for (Map<String, Object> scope : scopeStack) {
			if (scope == current) continue; // already checked
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
		// Fast path: check current scope first (covers >90% of cases)
		Map<String, Object> current = scopeStack.peek();
		if (current.containsKey(name)) {
			return true;
		}
		// Slow path: walk remaining scopes
		for (Map<String, Object> scope : scopeStack) {
			if (scope == current) continue; // already checked
			if (scope.containsKey(name)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Resolves the implicit property target for a given feature name by checking
	 * {@code _objectExp}, {@code result}, and {@code self} in a single scope walk (#6).
	 *
	 * <p>This avoids 3 separate {@link #lookup(String)} calls per implicit property
	 * assignment in mapping bodies.
	 *
	 * @param featureName the structural feature to match
	 * @param aliasResolver optional function to resolve alias names (may be {@code null})
	 * @return the EObject that owns the feature, or {@code null}
	 */
	public EObject lookupImplicitTarget(String featureName,
			java.util.function.BiFunction<String, EObject, String> aliasResolver) {
		// Collect the 3 candidate variables in a single scope walk
		Object objectExpVal = null;
		Object resultVal = null;
		Object selfVal = null;
		boolean foundObjectExp = false;
		boolean foundResult = false;
		boolean foundSelf = false;

		for (Map<String, Object> scope : scopeStack) {
			if (!foundObjectExp && scope.containsKey("_objectExp")) {
				objectExpVal = scope.get("_objectExp");
				foundObjectExp = true;
			}
			if (!foundResult && scope.containsKey("result")) {
				resultVal = scope.get("result");
				foundResult = true;
			}
			if (!foundSelf && scope.containsKey("self")) {
				selfVal = scope.get("self");
				foundSelf = true;
			}
			if (foundObjectExp && foundResult && foundSelf) {
				break;
			}
		}

		// Check in priority order: _objectExp > result > self (§8.2.1.17)
		if (objectExpVal instanceof EObject eo && hasFeature(eo, featureName, aliasResolver)) {
			return eo;
		}
		if (resultVal instanceof EObject eo && hasFeature(eo, featureName, aliasResolver)) {
			return eo;
		}
		if (selfVal instanceof EObject eo && hasFeature(eo, featureName, aliasResolver)) {
			return eo;
		}
		return null;
	}

	private static boolean hasFeature(EObject eo, String featureName,
			java.util.function.BiFunction<String, EObject, String> aliasResolver) {
		EStructuralFeature sf = eo.eClass().getEStructuralFeature(featureName);
		if (sf != null) {
			return true;
		}
		return aliasResolver != null && aliasResolver.apply(featureName, eo) != null;
	}

	/**
	 * Returns a snapshot of all visible variables (for creating OclContext snapshots).
	 *
	 * <p>When multiple scopes define the same variable, the innermost wins.
	 *
	 * @return unmodifiable map of all visible variable bindings
	 */
	public Map<String, Object> allVisibleVariables() {
		if (!snapshotDirty && cachedSnapshot != null) {
			return cachedSnapshot;
		}
		Map<String, Object> result = new LinkedHashMap<>();
		// Walk from outermost to innermost so inner scopes override
		Object[] scopes = scopeStack.toArray();
		for (int i = scopes.length - 1; i >= 0; i--) {
			@SuppressWarnings("unchecked")
			Map<String, Object> scope = (Map<String, Object>) scopes[i];
			result.putAll(scope);
		}
		// Cannot use Map.copyOf() — values may be null
		cachedSnapshot = Collections.unmodifiableMap(result);
		snapshotDirty = false;
		return cachedSnapshot;
	}
}
