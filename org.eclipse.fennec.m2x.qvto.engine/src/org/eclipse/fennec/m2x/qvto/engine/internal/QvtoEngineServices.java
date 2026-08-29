/*
 * ******************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *   Data In Motion Consulting - initial implementation
 * ******************************************************************
 */
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.concurrent.Executor;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;

/**
 * What an evaluation needs from the engine that started it.
 *
 * <p>Three things, and no more: where a node stood in its source, how a nested
 * transformation is executed ({@code new T(m).transform()}, §8.2.2.1), and the executor
 * a parallel {@code map} runs on. The evaluator used to hold the whole
 * {@link QvtoEngineImpl}, nullable "for backward compatibility in tests" — which meant
 * every call had to ask whether the engine was there, and a test could not supply the
 * three without building an engine (#185).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public interface QvtoEngineServices {

	/**
	 * Where a node stood in its source, for a runtime diagnostic (#116).
	 *
	 * @param node the AST node
	 * @return the position, or {@code null} if none is known
	 */
	SourcePosition positionOf(EObject node);

	/**
	 * Executes a nested transformation.
	 *
	 * @param transformation the transformation to run
	 * @param context its extents and configuration properties
	 * @return the result of the nested run
	 */
	QvtoExecutionResult execute(OperationalTransformation transformation,
			QvtoExecutionContext context);

	/**
	 * The executor a parallel {@code map} runs on.
	 *
	 * @return the executor
	 */
	Executor parallelExecutor();

	/**
	 * The services of an evaluation that has no engine behind it: no positions, no nested
	 * execution, and everything parallel run on the calling thread.
	 *
	 * @return services that do nothing an engine would do
	 */
	static QvtoEngineServices none() {
		return new QvtoEngineServices() {

			@Override
			public SourcePosition positionOf(EObject node) {
				return null;
			}

			@Override
			public QvtoExecutionResult execute(OperationalTransformation transformation,
					QvtoExecutionContext context) {
				throw new IllegalStateException(
						"nested transformation execution needs an engine, and this evaluation has none");
			}

			@Override
			public Executor parallelExecutor() {
				return Runnable::run;
			}
		};
	}
}
