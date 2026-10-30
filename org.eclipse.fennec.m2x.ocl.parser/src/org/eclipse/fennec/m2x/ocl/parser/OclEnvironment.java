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
package org.eclipse.fennec.m2x.ocl.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.eclipse.fennec.m2x.model.ocl.Variable;

/**
 * Name resolution environment for OCL parsing and type checking.
 *
 * <p>Implements the nested scope model from OCL v2.4 Section 9.4: each environment
 * has an optional parent, and name lookup proceeds from the current scope outward.
 *
 * <p>Environments are immutable after creation — {@link #nested(Variable)} returns
 * a new child environment.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclEnvironment {

	private final OclEnvironment parent;
	private final Map<String, Variable> variables;
	/**
	 * The variables of this scope that may serve as the implicit source of an unqualified
	 * property or operation name (OCL v2.4 §9.4.1 {@code mayBeImplicit}): the iterator variables
	 * of an iterator — named ones too (§9.3.25 [A]) — and the iterator and result of iterate
	 * (§9.3.26), but not let variables (§9.3.41).
	 */
	private final List<Variable> implicitSources;

	private OclEnvironment(OclEnvironment parent, Map<String, Variable> variables,
			List<Variable> implicitSources) {
		this.parent = parent;
		this.variables = Map.copyOf(variables);
		this.implicitSources = List.copyOf(implicitSources);
	}

	/**
	 * Creates a root environment with a {@code self} variable.
	 *
	 * @param selfVariable the variable representing {@code self}
	 * @return a new root environment
	 */
	public static OclEnvironment root(Variable selfVariable) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		vars.put("self", selfVariable);
		return new OclEnvironment(null, vars, List.of());
	}

	/**
	 * Creates a child environment with one additional variable.
	 *
	 * @param variable the variable to add
	 * @return a new child environment
	 */
	public OclEnvironment nested(Variable variable) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		vars.put(variable.getName(), variable);
		return new OclEnvironment(this, vars, List.of());
	}

	/**
	 * Creates a child environment in which {@code name} denotes an existing variable — the
	 * variable of the explicit form of a classifier context, {@code context p : Person}, which
	 * is {@code self} under another name (OCL v2.4 §12.12.5 [B], #324).
	 *
	 * @param name the second name
	 * @param variable the variable it denotes
	 * @return a new child environment
	 */
	public OclEnvironment alias(String name, Variable variable) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		vars.put(name, variable);
		return new OclEnvironment(this, vars, List.of());
	}

	/**
	 * Creates a child environment with multiple additional variables.
	 *
	 * @param additionalVariables the variables to add
	 * @return a new child environment
	 */
	public OclEnvironment nested(Iterable<Variable> additionalVariables) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		for (Variable v : additionalVariables) {
			vars.put(v.getName(), v);
		}
		return new OclEnvironment(this, vars, List.of());
	}

	/**
	 * Creates a child environment with an implicit iterator variable.
	 * Property names in the body will be resolved against this variable's type first.
	 *
	 * @param iterVar the implicit iterator variable
	 * @return a new child environment with implicit source
	 */
	public OclEnvironment nestedImplicit(Variable iterVar) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		vars.put(iterVar.getName(), iterVar);
		return new OclEnvironment(this, vars, List.of(iterVar));
	}

	/**
	 * Creates a child environment with iterator variables that may serve as implicit source
	 * (OCL v2.4 §9.3.25 [A]: {@code addElement(varName, ast, true)}) — for the iterator
	 * variables of an iterator and for the iterator and result variable of iterate.
	 *
	 * @param iteratorVariables the variables to add
	 * @return a new child environment
	 */
	public OclEnvironment nestedIterators(Iterable<Variable> iteratorVariables) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		List<Variable> sources = new ArrayList<>();
		for (Variable v : iteratorVariables) {
			vars.put(v.getName(), v);
			sources.add(v);
		}
		return new OclEnvironment(this, vars, sources);
	}

	/**
	 * Looks up a variable by name, searching from the current scope outward.
	 *
	 * @param name the variable name
	 * @return the variable, or empty if not found
	 */
	public Optional<Variable> lookup(String name) {
		Variable local = variables.get(name);
		if (local != null) {
			return Optional.of(local);
		}
		if (parent != null) {
			return parent.lookup(name);
		}
		return Optional.empty();
	}

	/**
	 * Returns the innermost implicit iterator variable, if any.
	 * Used by {@code resolveImplicitProperty} to resolve unqualified names
	 * against the iterator element type before falling back to {@code self}.
	 */
	public Optional<Variable> lookupImplicitIterator() {
		List<Variable> sources = lookupImplicitSources();
		return sources.isEmpty() ? Optional.empty() : Optional.of(sources.get(0));
	}

	/**
	 * Returns every variable that may serve as implicit source, innermost scope first — the
	 * order in which OCL v2.4 §9.4.1 [10]–[12] look a name up before falling back to
	 * {@code self} (§7.8: "the most inner scope").
	 *
	 * @return the candidates, innermost first; empty outside any iterator
	 */
	public List<Variable> lookupImplicitSources() {
		List<Variable> result = new ArrayList<>();
		for (OclEnvironment scope = this; scope != null; scope = scope.parent) {
			result.addAll(scope.implicitSources);
		}
		return result;
	}

	/**
	 * Counts the implicit iterators the parser created in this scope chain; used to give each
	 * one a distinct name, so a body can still reach an outer one at evaluation, where variables
	 * are looked up by name.
	 *
	 * @return the number of parser-created implicit iterators in scope
	 */
	public int implicitIteratorDepth() {
		int depth = 0;
		for (OclEnvironment scope = this; scope != null; scope = scope.parent) {
			for (Variable v : scope.implicitSources) {
				if (v.getName() != null && v.getName().startsWith("_implicit")) {
					depth++;
				}
			}
		}
		return depth;
	}
}
