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
package org.eclipse.fennec.m2x.ocl.api;

import java.util.Collections;
import java.util.Map;
import java.util.Objects;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.stream.Collectors;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;

/**
 * What a parse result is cached under: the expression text, the context type and the
 * variables declared in scope.
 *
 * <p>The variables belong to the key because they are part of what the text means: the
 * same text parses differently, or not at all, depending on which names are declared and
 * with which type. They are kept sorted by name, so the order in which a caller declared
 * them does not matter.
 *
 * @param expression the OCL expression text
 * @param contextType the context classifier
 * @param variables name and type of each declared variable, sorted by name; a {@code null}
 *        type is unknown
 * @since 1.0
 */
public record OclCacheKey(String expression, EClassifier contextType, SortedMap<String, EClassifier> variables) {

	/**
	 * Validates the components and takes an unmodifiable sorted copy of the variables.
	 */
	public OclCacheKey {
		Objects.requireNonNull(expression, "expression must not be null");
		Objects.requireNonNull(contextType, "contextType must not be null");
		Objects.requireNonNull(variables, "variables must not be null");
		variables = Collections.unmodifiableSortedMap(new TreeMap<>(variables));
	}

	/**
	 * Creates a key for an expression without declared variables.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the context classifier
	 * @return the key
	 */
	public static OclCacheKey of(String expression, EClassifier contextType) {
		return new OclCacheKey(expression, contextType, new TreeMap<>());
	}

	/**
	 * Creates a key for an expression with declared variables.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the context classifier
	 * @param variables name and type of each declared variable; a {@code null} type is unknown
	 * @return the key
	 */
	public static OclCacheKey of(String expression, EClassifier contextType, Map<String, EClassifier> variables) {
		Objects.requireNonNull(variables, "variables must not be null");
		return new OclCacheKey(expression, contextType, new TreeMap<>(variables));
	}

	/**
	 * Returns {@code true} when no variables are declared.
	 *
	 * @return whether the variables are empty
	 */
	public boolean hasVariables() {
		return !variables.isEmpty();
	}

	/**
	 * Returns the same key with another expression text. Decorating caches use it to put a
	 * prefix — a model fingerprint, for instance — in front of the text.
	 *
	 * @param newExpression the expression text of the new key
	 * @return the new key
	 */
	public OclCacheKey withExpression(String newExpression) {
		return new OclCacheKey(newExpression, contextType, variables);
	}

	/**
	 * Returns the variables as text, {@code name:nsURI#Type} separated by commas, or the
	 * empty string when there are none. A type is identified by package nsURI and name, as
	 * the context type is in a cache key; an unknown type is written as {@code ?}.
	 *
	 * @return the variables as text
	 */
	public String variablesText() {
		return variables.entrySet().stream()
				.map(entry -> entry.getKey() + ":" + typeText(entry.getValue()))
				.collect(Collectors.joining(","));
	}

	private static String typeText(EClassifier type) {
		if (type == null) {
			return "?";
		}
		EPackage ePackage = type.getEPackage();
		return (ePackage != null ? ePackage.getNsURI() : "") + "#" + type.getName();
	}
}
