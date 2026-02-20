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
package org.eclipse.fennec.m2m.ocl.engine.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.fennec.m2m.model.ocl.FeatureCallExp;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.engine.OclEngineImpl;

/**
 * EMF {@link EOperation.Internal.InvocationDelegate.Factory} that evaluates
 * OCL body expressions annotated on EOperations.
 *
 * <p>Reads the OCL expression from the EAnnotation with source
 * {@value OclDelegateUtil#DELEGATE_URI}, detail keys {@code "body"},
 * {@code "pre"}, and {@code "post"}.
 *
 * <p>When a postcondition is present, the delegate performs a lazy pre-scan
 * to capture {@code @pre} values before executing the body, then evaluates
 * the postcondition afterwards.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclInvocationDelegateFactory implements EOperation.Internal.InvocationDelegate.Factory {

	private final OclEngineImpl engine;

	public OclInvocationDelegateFactory(OclEngineImpl engine) {
		this.engine = Objects.requireNonNull(engine, "engine must not be null");
	}

	@Override
	public EOperation.Internal.InvocationDelegate createInvocationDelegate(EOperation operation) {
		String body = OclDelegateUtil.getAnnotationDetail(operation, "body");
		String pre = OclDelegateUtil.getAnnotationDetail(operation, "pre");
		String post = OclDelegateUtil.getAnnotationDetail(operation, "post");
		if (body == null && post == null) {
			throw new IllegalStateException(
					"No OCL body or post annotation on operation: " + operation.getName());
		}
		return new OclInvocationDelegate(operation, body, pre, post);
	}

	private class OclInvocationDelegate implements EOperation.Internal.InvocationDelegate {

		private final EOperation operation;
		private final String body;
		private final String pre;
		private final String post;
		private volatile OclExpression parsedBody;
		private volatile OclExpression parsedPre;
		private volatile OclExpression parsedPost;

		OclInvocationDelegate(EOperation operation, String body, String pre, String post) {
			this.operation = operation;
			this.body = body;
			this.pre = pre;
			this.post = post;
		}

		@Override
		public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
				throws InvocationTargetException {
			try {
				Map<String, Object> variables = bindArguments(arguments);
				OclContext context = OclContext.of(target, variables);

				// 1. Evaluate precondition if present
				if (pre != null) {
					OclExpression preExpr = getParsedPre();
					Object preResult = engine.evaluate(preExpr, context, engine.getDelegateOptions());
					if (!Boolean.TRUE.equals(preResult)) {
						throw new IllegalStateException(
								"Precondition violated for operation: " + operation.getName());
					}
				}

				// 2. Pre-scan postcondition for @pre nodes and capture pre-state
				PreStateSnapshot snapshot = PreStateSnapshot.EMPTY;
				if (post != null) {
					OclExpression postExpr = getParsedPost();
					List<FeatureCallExp> preNodes = PreStateScanCollector.findPreNodes(postExpr);
					snapshot = PreStateScanCollector.capturePreState(
							preNodes, context, engine.getDelegateOptions(),
							engine.getOperationProviders());
				}

				// 3. Execute body (if present)
				Object result = null;
				if (body != null) {
					OclExpression bodyExpr = getParsedBody();
					result = engine.evaluate(bodyExpr, context, engine.getDelegateOptions());
				}

				// 4. Evaluate postcondition if present
				if (post != null) {
					OclExpression postExpr = getParsedPost();
					// Bind 'result' variable for postcondition
					Map<String, Object> postVars = new LinkedHashMap<>(variables);
					postVars.put("result", result);
					OclContext postContext = OclContext.of(target, postVars);
					Object postResult = engine.evaluatePostcondition(postExpr, postContext, snapshot);
					if (!Boolean.TRUE.equals(postResult)) {
						throw new IllegalStateException(
								"Postcondition violated for operation: " + operation.getName());
					}
				}

				return result;
			} catch (Exception e) {
				throw new InvocationTargetException(e,
						"OCL invocation failed for " + operation.getName() + ": " + e.getMessage());
			}
		}

		private Map<String, Object> bindArguments(EList<?> arguments) {
			Map<String, Object> variables = new LinkedHashMap<>();
			EList<EParameter> params = operation.getEParameters();
			if (arguments != null) {
				for (int i = 0; i < Math.min(params.size(), arguments.size()); i++) {
					variables.put(params.get(i).getName(), arguments.get(i));
				}
			}
			return variables;
		}

		private OclExpression getParsedBody() throws OclParseException {
			return parseLazily(body, () -> parsedBody, expr -> parsedBody = expr);
		}

		private OclExpression getParsedPre() throws OclParseException {
			return parseLazily(pre, () -> parsedPre, expr -> parsedPre = expr);
		}

		private OclExpression getParsedPost() throws OclParseException {
			return parseLazily(post, () -> parsedPost, expr -> parsedPost = expr);
		}

		/**
		 * Thread-safe lazy parsing with double-checked locking.
		 * Parses the OCL source string on first access and caches the result.
		 * The {@code fieldReader} re-reads the volatile field inside the synchronized block.
		 */
		private OclExpression parseLazily(String source, Supplier<OclExpression> fieldReader,
				Consumer<OclExpression> setter) throws OclParseException {
			OclExpression result = fieldReader.get();
			if (result == null) {
				synchronized (this) {
					result = fieldReader.get();
					if (result == null) {
						result = engine.parse(source, operation.getEContainingClass());
						setter.accept(result);
					}
				}
			}
			return result;
		}
	}
}
