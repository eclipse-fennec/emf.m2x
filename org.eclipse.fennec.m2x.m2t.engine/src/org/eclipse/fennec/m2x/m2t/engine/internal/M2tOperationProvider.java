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

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.function.BiFunction;

import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Query;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * Hands the OCL engine the queries a module can call (MOFM2T v1.0 §8.1.15).
 *
 * <p>A query invocation inside a template body never reaches OCL as an operation call:
 * {@link M2tModuleLinker} rewrites the parsed invocation node into a {@code QueryInvocation}
 * that {@code M2tEvaluator} executes itself. A <b>guard</b> is a plain {@code OclExpression}
 * ({@code Template::guard}, {@code <guard> ::= '?' '(' <OclExpressionCS> ')'}), so that rewrite
 * never reaches it and the call is handed to OCL, which knew no such operation and reported
 * {@code Unknown operation} for something the module defines perfectly well (#146). The same
 * held for every other place an OCL expression is evaluated: {@code for} guards, {@code let},
 * {@code if}.
 *
 * <p>QVT-R has the same shape for the same reason ({@code QvtrOperationProvider}, #118).
 *
 * <p>Both call forms are answered. MOFM2T is written for the receiver form
 * {@code c.hasOps()}, where the receiver is the query's first parameter, but the same query
 * may be written {@code hasOps(c)}; which one arrives is visible from the argument count, so
 * the implementation decides per call rather than registering two operations.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tOperationProvider implements OclOperationProvider {

	private final Module module;
	private final BiFunction<Query, Object[], Object> invoker;

	/**
	 * @param module the module whose visible queries become OCL operations
	 * @param invoker evaluates a query with its argument values — the evaluator's own path, so
	 *            that a query calling another query recurses naturally
	 */
	M2tOperationProvider(Module module, BiFunction<Query, Object[], Object> invoker) {
		this.module = Objects.requireNonNull(module, "module must not be null");
		this.invoker = Objects.requireNonNull(invoker, "invoker must not be null");
	}

	@Override
	public List<OclOperation> getOperations() {
		AnyType any = OclStandardLibrary.INSTANCE.oclAny();
		List<OclOperation> operations = new ArrayList<>();
		for (Query query : M2tModuleLinker.visibleQueries(module).values()) {
			operations.add(toOperation(query, any));
		}
		return operations;
	}

	/**
	 * A query is registered under the arity of the receiver form, one less than its parameter
	 * count, because that is the form MOFM2T is written in and the one exact-arity dispatch
	 * should find first. The function form still arrives here: the OCL evaluator falls back to
	 * matching by name alone when no arity matches.
	 */
	private OclOperation toOperation(Query query, AnyType any) {
		int receiverFormArity = Math.max(0, query.getParameter().size() - 1);
		List<EClassifier> parameterTypes = new ArrayList<>(
				Collections.nCopies(receiverFormArity, (EClassifier) any));
		return new OclOperation(query.getName(), any, parameterTypes, any,
				(self, args) -> invoke(query, self, args));
	}

	private Object invoke(Query query, Object self, Object[] args) {
		int parameterCount = query.getParameter().size();
		Object[] arguments;
		if (args.length == parameterCount) {
			// hasOps(c) — every parameter is an argument, the receiver plays no part
			arguments = args;
		} else {
			// c.hasOps() — the receiver is the first parameter
			arguments = new Object[args.length + 1];
			arguments[0] = self;
			System.arraycopy(args, 0, arguments, 1, args.length);
		}
		return invoker.apply(query, arguments);
	}
}
