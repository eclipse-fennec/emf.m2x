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

import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;

/**
 * Handles blackbox library invocation and {@code implementedby} delegation
 * (§7.8, §7.11.3.6).
 *
 * <p>Extracted from {@link QvtrEvaluator} for testability and separation
 * of concerns. Security enforcement (M-R5) is centralized here.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrBlackboxBridge {

	private final QvtdBlackboxRegistry blackboxRegistry;
	private final QvtdConfiguration config;
	private final List<RelationImplementationProvider> implementationProviders;
	private final QvtrExtentManager extentManager;
	private final QvtdExecutionContext context;
	private final List<Diagnostic> diagnostics;
	private final QvtrOclCallback oclCallback;

	public QvtrBlackboxBridge(QvtdBlackboxRegistry blackboxRegistry, QvtdConfiguration config,
			List<RelationImplementationProvider> implementationProviders,
			QvtrExtentManager extentManager, QvtdExecutionContext context,
			List<Diagnostic> diagnostics, QvtrOclCallback oclCallback) {
		this.blackboxRegistry = blackboxRegistry;
		this.config = config;
		this.implementationProviders = implementationProviders;
		this.extentManager = extentManager;
		this.context = context;
		this.diagnostics = diagnostics;
		this.oclCallback = oclCallback;
	}

	/**
	 * Invokes an implementedby operational implementation (§7.11.3.6).
	 * Searches the relation's {@code operationalImpl} list for an implementation
	 * matching the target direction and delegates first to registered
	 * {@link RelationImplementationProvider}s (D39, Phase 4b), then falls back
	 * to the blackbox registry.
	 *
	 * @return {@code true} if an implementation was found and invoked
	 */
	public boolean invokeImplementedBy(Relation relation, TypedModel targetModel,
			Map<String, Object> bindings) {
		for (RelationImplementation impl : relation.getOperationalImpl()) {
			TypedModel direction = impl.getInDirectionOf();
			if (direction != null && targetModel.getName().equals(direction.getName())) {
				EOperation op = impl.getImpl();
				if (op == null) {
					continue;
				}
				String opName = op.getName();

				// §7.8 / D39: Try RelationImplementationProviders first (QVT-O hybrid)
				String qualifiedName = relation.getName();
				for (RelationImplementationProvider provider : implementationProviders) {
					if (provider.canProvide(qualifiedName)) {
						QvtdExecutionResult result = provider.executeRelation(qualifiedName, context);
						if (result.isSuccess()) {
							return true;
						}
						diagnostics.addAll(result.diagnostics());
						return true; // invoked, even if errors
					}
				}

				// Fallback: blackbox registry (M-R5: enforce config gate)
				if (blackboxRegistry == null || !config.blackboxEnabled()) {
					continue;
				}
				if (!isBlackboxAllowed(opName)) {
					continue;
				}

				// Evaluate argument expressions from annotation
				Object[] argValues = new Object[0];
				EAnnotation ann = impl.getEAnnotation("qvtr.implementedby.args");
				if (ann != null) {
					List<EObject> argExprs = ann.getReferences();
					argValues = new Object[argExprs.size()];
					for (int i = 0; i < argExprs.size(); i++) {
						if (argExprs.get(i) instanceof OclExpression argExpr) {
							argValues[i] = oclCallback.evaluate(argExpr, bindings);
						}
					}
				}

				// Invoke through blackbox registry
				for (var library : blackboxRegistry.getLibraries()) {
					try {
						Object result = library.invoke(opName, null, argValues);
						if (result instanceof EObject created) {
							if (created.eContainer() == null) {
								extentManager.getExtent(targetModel).add(created);
							}
						}
						return true;
					} catch (Exception e) {
						// Library doesn't support this operation — try next
					}
				}
			}
		}
		return false;
	}

	/**
	 * Evaluates a blackbox query by delegating to the registered blackbox
	 * library (§7.8). Searches all registered libraries for an operation
	 * matching the query name.
	 */
	public Object evaluateBlackboxQuery(Function query, OperationCallExp callExpr,
			Map<String, Object> callerBindings) {
		if (blackboxRegistry == null || !config.blackboxEnabled()) {
			return null;
		}

		// Evaluate arguments
		List<OclExpression> args = callExpr.getOwnedArguments();
		Object[] argValues = new Object[args.size()];
		for (int i = 0; i < args.size(); i++) {
			argValues[i] = oclCallback.evaluate(args.get(i), callerBindings);
		}

		// Search all registered libraries for the operation
		String queryName = query.getName();
		for (var library : blackboxRegistry.getLibraries()) {
			try {
				Object result = library.invoke(queryName, null, argValues);
				if (result != null) {
					return result;
				}
			} catch (Exception e) {
				// Library doesn't support this operation — try next
			}
		}
		return null;
	}

	/**
	 * Checks if a blackbox operation is allowed by the configuration's
	 * allow-list (M-R5).
	 */
	public boolean isBlackboxAllowed(String operationName) {
		Set<String> allowed = config.allowedBlackboxModules();
		return allowed.isEmpty() || allowed.contains(operationName);
	}
}
