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

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
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
	/**
	 * The named iterator variables of this scope, which are implicit sources of an unqualified
	 * name too (OCL v2.4 §9.3.25 [A], as the OCL parser has done since #320).
	 */
	private final List<Variable> namedSources;
	/**
	 * Whether this scope is the body of an object expression, whose own object is the implicit
	 * target of an unqualified name (§8.2.1.17, §8.2.2.24): no iterator around it is (#383).
	 */
	private final boolean implicitBarrier;

	private QvtoEnvironment(QvtoEnvironment parent, Map<String, Variable> variables,
			Variable implicitIterator) {
		this(parent, variables, implicitIterator, List.of(), false);
	}

	private QvtoEnvironment(QvtoEnvironment parent, Map<String, Variable> variables,
			Variable implicitIterator, List<Variable> namedSources, boolean implicitBarrier) {
		this.parent = parent;
		this.variables = Map.copyOf(variables);
		this.implicitIterator = implicitIterator;
		this.namedSources = List.copyOf(namedSources);
		this.implicitBarrier = implicitBarrier;
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
	 * Creates a child environment with the variables of an iterator, which may serve as the
	 * implicit source of an unqualified name — {@code ->collect(p | name)} is p's name.
	 *
	 * @param iteratorVariables the iterator (and accumulator) variables
	 * @return a new child environment
	 */
	QvtoEnvironment nestedIterators(Iterable<Variable> iteratorVariables) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		List<Variable> sources = new ArrayList<>();
		for (Variable v : iteratorVariables) {
			vars.put(v.getName(), v);
			sources.add(v);
		}
		return new QvtoEnvironment(this, vars, null, sources, false);
	}

	/**
	 * Creates the scope of an object expression's body: its object variable is visible, and the
	 * object being created — not an iterator around the expression — is the implicit target of an
	 * unqualified name. Before #383 a named iterator of an enclosing collect took
	 * {@code name := …} in {@code ids->collect(i | object EClass { name := … })}.
	 *
	 * @param objectVariable the variable the object expression binds
	 * @return a new child environment
	 */
	QvtoEnvironment nestedObjectBody(Variable objectVariable) {
		Objects.requireNonNull(objectVariable, "objectVariable must not be null");
		Map<String, Variable> vars = new LinkedHashMap<>();
		vars.put(objectVariable.getName(), objectVariable);
		return new QvtoEnvironment(this, vars, null, List.of(), true);
	}

	/**
	 * Every implicit source, innermost scope first: an iterator without a name, and the named
	 * iterator variables (OCL v2.4 §9.4.1 [10], §7.8 "the most inner scope").
	 *
	 * @return the sources, innermost first
	 */
	List<Variable> lookupImplicitSources() {
		List<Variable> sources = new ArrayList<>();
		for (QvtoEnvironment scope = this; scope != null && !scope.implicitBarrier; scope = scope.parent) {
			if (scope.implicitIterator != null) {
				sources.add(scope.implicitIterator);
			}
			sources.addAll(scope.namedSources);
		}
		return sources;
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
