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
package org.eclipse.fennec.m2m.qvto.engine.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

import org.eclipse.fennec.m2m.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.model.qvtoperational.Status;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;

/**
 * Implements §8.3.6 transformation instance operations:
 * {@code transform()}, {@code parallelTransform()}, and {@code wait()}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class QvtoTransformationOperations {

	private QvtoTransformationOperations() {
		// utility class
	}

	/**
	 * §8.3.6.1: Synchronous execution of a transformation instance.
	 * Uses the parent's extents and returns a Status.
	 */
	static Status handleTransform(QvtoTransformationInstance instance) {
		try {
			QvtoExecutionContext ctx = buildContext(instance);
			QvtoExecutionResult result = instance.getEngine().execute(
					instance.getTransformation(), ctx);
			return result.isSuccess()
					? QvtoStatusHelper.success()
					: QvtoStatusHelper.failed(new RuntimeException(
							"Transformation failed: " + result.diagnostics()));
		} catch (Exception e) {
			return QvtoStatusHelper.failed(e);
		}
	}

	/**
	 * §8.3.6.2: Asynchronous execution of a transformation instance
	 * using virtual threads.
	 */
	static Status handleParallelTransform(QvtoTransformationInstance instance) {
		Executor executor = instance.getEngine().getParallelExecutor();
		CompletableFuture<Status> future = CompletableFuture.supplyAsync(() -> {
			try {
				QvtoExecutionContext ctx = buildContext(instance);
				QvtoExecutionResult result = instance.getEngine().execute(
						instance.getTransformation(), ctx);
				return result.isSuccess()
						? QvtoStatusHelper.success()
						: QvtoStatusHelper.failed(new RuntimeException(
								"Transformation failed: " + result.diagnostics()));
			} catch (Exception e) {
				return QvtoStatusHelper.failed(e);
			}
		}, executor);
		return QvtoStatusHelper.pending(future);
	}

	/**
	 * §8.3.6.3: Wait for all statuses to complete.
	 */
	static void handleWait(Object arg) {
		if (arg instanceof Collection<?> statuses) {
			for (Object s : statuses) {
				if (s instanceof Status status) {
					QvtoStatusHelper.await(status);
				}
			}
		} else if (arg instanceof Status status) {
			QvtoStatusHelper.await(status);
		}
	}

	/**
	 * §8.1.21: Bind model arguments to a transformation instance at call time.
	 * Used when {@code asTransformation()} creates an unbound instance and
	 * {@code transform(model1, model2, ...)} provides the model extents.
	 */
	static QvtoTransformationInstance bindModels(QvtoTransformationInstance instance, Object[] args) {
		OperationalTransformation transformation = instance.getTransformation();
		List<ModelParameter> params = transformation.getModelParameter();
		Map<ModelParameter, QvtoModelExtent> bindings = new LinkedHashMap<>();
		for (int i = 0; i < params.size() && i < args.length; i++) {
			if (args[i] instanceof QvtoModelExtent extent) {
				bindings.put(params.get(i), extent);
			}
		}
		return new QvtoTransformationInstance(transformation,
				QvtoOperationResolver.findModuleClassIn(transformation),
				bindings, instance.getEngine());
	}

	/**
	 * Builds an execution context from the transformation instance's model bindings.
	 */
	private static QvtoExecutionContext buildContext(QvtoTransformationInstance instance) {
		OperationalTransformation transformation = instance.getTransformation();
		Map<ModelParameter, QvtoModelExtent> bindings = instance.getModelBindings();
		List<QvtoModelExtent> extents = new ArrayList<>();
		for (ModelParameter mp : transformation.getModelParameter()) {
			QvtoModelExtent extent = bindings.get(mp);
			if (extent != null) {
				extents.add(extent);
			}
		}
		return QvtoExecutionContext.of(extents, Map.of());
	}
}
