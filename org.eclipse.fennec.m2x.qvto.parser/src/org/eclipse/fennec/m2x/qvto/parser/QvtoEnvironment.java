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
package org.eclipse.fennec.m2x.qvto.parser;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.fennec.m2x.model.ocl.Variable;

/**
 * Name resolution environment for QVT-O parsing.
 *
 * <p>Implements a nested scope model: each environment has an optional parent,
 * and name lookup proceeds from the current scope outward.
 *
 * <p>Environments are immutable after creation — {@link #nested(Variable)} returns
 * a new child environment.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoEnvironment {

	private final QvtoEnvironment parent;
	private final Map<String, Variable> variables;
	private final Variable implicitIterator;

	private QvtoEnvironment(QvtoEnvironment parent, Map<String, Variable> variables,
			Variable implicitIterator) {
		this.parent = parent;
		this.variables = Map.copyOf(variables);
		this.implicitIterator = implicitIterator;
	}

	/**
	 * Creates a root environment with no variables.
	 *
	 * @return a new root environment
	 */
	static QvtoEnvironment root() {
		return new QvtoEnvironment(null, Map.of(), null);
	}

	/**
	 * Creates a child environment with one additional variable.
	 *
	 * @param variable the variable to add
	 * @return a new child environment
	 */
	QvtoEnvironment nested(Variable variable) {
		Objects.requireNonNull(variable, "variable must not be null");
		Map<String, Variable> vars = new LinkedHashMap<>();
		vars.put(variable.getName(), variable);
		return new QvtoEnvironment(this, vars, null);
	}

	/**
	 * Creates a child environment with multiple additional variables.
	 *
	 * @param additionalVariables the variables to add
	 * @return a new child environment
	 */
	QvtoEnvironment nested(Iterable<Variable> additionalVariables) {
		Objects.requireNonNull(additionalVariables, "additionalVariables must not be null");
		Map<String, Variable> vars = new LinkedHashMap<>();
		for (Variable v : additionalVariables) {
			vars.put(v.getName(), v);
		}
		return new QvtoEnvironment(this, vars, null);
	}

	/**
	 * Creates a child environment whose implicit source is an iterator without a name — the
	 * {@code ->select(name = 'x')} form of OCL v2.4 §7.6.3.
	 *
	 * @param iterator the implicit iterator variable
	 * @return a new child environment
	 */
	QvtoEnvironment nestedImplicit(Variable iterator) {
		Objects.requireNonNull(iterator, "iterator must not be null");
		return new QvtoEnvironment(this, Map.of(), iterator);
	}

	/**
	 * Returns the innermost implicit iterator, if any.
	 *
	 * @return the implicit iterator variable, or empty outside an iterator without a variable
	 */
	Optional<Variable> lookupImplicitIterator() {
		if (implicitIterator != null) {
			return Optional.of(implicitIterator);
		}
		return parent != null ? parent.lookupImplicitIterator() : Optional.empty();
	}

	/**
	 * Looks up a variable by name, searching from the current scope outward.
	 *
	 * @param name the variable name
	 * @return the variable, or empty if not found
	 */
	Optional<Variable> lookup(String name) {
		Objects.requireNonNull(name, "name must not be null");
		Variable local = variables.get(name);
		if (local != null) {
			return Optional.of(local);
		}
		if (parent != null) {
			return parent.lookup(name);
		}
		return Optional.empty();
	}
}
