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

import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;

/**
 * The variables an expression reads from outside: those its variable expressions refer to,
 * without the ones it declares itself (iterators, let variables). Whether a where predicate can
 * be evaluated for a binding depends on them (#351).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class QvtrFreeVariables {

	private QvtrFreeVariables() {
	}

	/**
	 * The names of the variables the expression reads but does not declare.
	 *
	 * @param expression the expression
	 * @return the names, in order of first use
	 */
	static Set<String> of(OclExpression expression) {
		Set<Variable> declared = new HashSet<>();
		Set<String> used = new LinkedHashSet<>();
		visit(expression, declared, used);
		for (var it = expression.eAllContents(); it.hasNext();) {
			visit(it.next(), declared, used);
		}
		for (Variable variable : declared) {
			used.remove(variable.getName());
		}
		return used;
	}

	private static void visit(EObject object, Set<Variable> declared, Set<String> used) {
		if (object instanceof Variable variable) {
			declared.add(variable);
		} else if (object instanceof VariableExp exp && exp.getReferredVariable() != null) {
			used.add(exp.getReferredVariable().getName());
		}
	}
}
