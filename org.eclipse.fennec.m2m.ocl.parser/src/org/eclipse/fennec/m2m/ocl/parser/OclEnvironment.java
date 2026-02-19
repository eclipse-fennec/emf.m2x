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
package org.eclipse.fennec.m2m.ocl.parser;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.fennec.m2m.model.ocl.Variable;

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
class OclEnvironment {

	private final OclEnvironment parent;
	private final Map<String, Variable> variables;

	private OclEnvironment(OclEnvironment parent, Map<String, Variable> variables) {
		this.parent = parent;
		this.variables = Map.copyOf(variables);
	}

	/**
	 * Creates a root environment with a {@code self} variable.
	 *
	 * @param selfVariable the variable representing {@code self}
	 * @return a new root environment
	 */
	static OclEnvironment root(Variable selfVariable) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		vars.put("self", selfVariable);
		return new OclEnvironment(null, vars);
	}

	/**
	 * Creates a child environment with one additional variable.
	 *
	 * @param variable the variable to add
	 * @return a new child environment
	 */
	OclEnvironment nested(Variable variable) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		vars.put(variable.getName(), variable);
		return new OclEnvironment(this, vars);
	}

	/**
	 * Creates a child environment with multiple additional variables.
	 *
	 * @param additionalVariables the variables to add
	 * @return a new child environment
	 */
	OclEnvironment nested(Iterable<Variable> additionalVariables) {
		Map<String, Variable> vars = new LinkedHashMap<>();
		for (Variable v : additionalVariables) {
			vars.put(v.getName(), v);
		}
		return new OclEnvironment(this, vars);
	}

	/**
	 * Looks up a variable by name, searching from the current scope outward.
	 *
	 * @param name the variable name
	 * @return the variable, or empty if not found
	 */
	Optional<Variable> lookup(String name) {
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
