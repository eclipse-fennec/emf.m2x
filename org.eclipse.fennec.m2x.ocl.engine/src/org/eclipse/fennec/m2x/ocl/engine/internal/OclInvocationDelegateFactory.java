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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.fennec.m2x.model.ocl.ConstraintKind;
import org.eclipse.fennec.m2x.model.ocl.FeatureCallExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclValues;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

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
 * <p>In an OSGi environment, this factory is registered as a service with
 * properties matching the emf.osgi delegate registry whiteboard pattern.
 * In standalone mode, create an instance directly via constructor.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(
		service = EOperation.Internal.InvocationDelegate.Factory.class,
		property = {
				"emf.configuratorName=" + OclDelegateUtil.DELEGATE_URI,
				"emf.name=fennec-ocl",
				"emf.configuratorType=OPERATION_INVOCATION_FACTORY"
		})
public class OclInvocationDelegateFactory implements EOperation.Internal.InvocationDelegate.Factory {

	private final OclDelegateSupport engine;

	/**
	 * DS constructor — engine is injected as a service.
	 */
	@Activate
	public OclInvocationDelegateFactory(@Reference OclDelegateSupport engine) {
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

				// 1. Evaluate the preconditions: the annotation's and those of registered documents
				for (OclExpression preExpr : conditions(ConstraintKind.PRE)) {
					Object preResult = engine.evaluate(preExpr, context, engine.getDelegateOptions());
					if (!Boolean.TRUE.equals(preResult)) {
						throw new IllegalStateException(
								"Precondition violated for operation: " + operation.getName());
					}
				}

				// 2. Pre-scan the postconditions for @pre nodes and capture pre-state
				List<OclExpression> postconditions = conditions(ConstraintKind.POST);
				PreStateSnapshot snapshot = PreStateSnapshot.EMPTY;
				if (!postconditions.isEmpty()) {
					List<FeatureCallExp> preNodes = new ArrayList<>();
					for (OclExpression postExpr : postconditions) {
						preNodes.addAll(PreStateScanCollector.findPreNodes(postExpr));
					}
					snapshot = PreStateScanCollector.capturePreState(
							preNodes, context, engine.getDelegateOptions(),
							engine.getOperationProviders(engine.getDelegateOptions()));
				}

				// 3. Execute body (if present)
				Object result = null;
				if (body != null) {
					OclExpression bodyExpr = getParsedBody();
					result = engine.evaluate(bodyExpr, context, engine.getDelegateOptions());
				}

				// 3a. EMF casts delegate results for multi-valued EOperations to EList.
				// OCL stdlib collection ops (->asSequence, ->asOrderedSet, ->asBag,
				// ->asSet, ->select, ...) return ArrayList / OclOrderedSet / OclBag /
				// OclSet — none of which implement EList. Wrap so callers of the
				// generated EMF accessor receive a valid (unmodifiable) EList.
				if (operation.isMany() && result != null && !(result instanceof EList<?>)) {
					List<Object> elements = new ArrayList<>();
					for (Object element : result instanceof Collection<?> col ? col : List.of(result)) {
						elements.add(OclValues.toEcore(element, operation.getEType()));
					}
					result = ECollections.unmodifiableEList(new BasicEList<>(elements));
				} else if (!operation.isMany()) {
					// A number has the operation type's instance class (#322)
					result = OclValues.toEcore(result, operation.getEType());
				}

				// 4. Evaluate the postconditions, with 'result' bound
				if (!postconditions.isEmpty()) {
					Map<String, Object> postVars = new LinkedHashMap<>(variables);
					postVars.put("result", result);
					OclContext postContext = OclContext.of(target, postVars);
					for (OclExpression postExpr : postconditions) {
						Object postResult = engine.evaluatePostcondition(postExpr, postContext, snapshot);
						if (!Boolean.TRUE.equals(postResult)) {
							throw new IllegalStateException(
									"Postcondition violated for operation: " + operation.getName());
						}
					}
				}

				return result;
			} catch (Exception e) {
				throw new InvocationTargetException(e,
						"OCL invocation failed for " + operation.getName() + ": " + e.getMessage());
			}
		}

		/**
		 * The preconditions or postconditions to check: the annotation's, then those registered
		 * Complete OCL documents state for this operation (OCL v2.4 §12.7, #323).
		 */
		private List<OclExpression> conditions(ConstraintKind kind) throws OclParseException {
			List<OclExpression> result = new ArrayList<>();
			if (kind == ConstraintKind.PRE && pre != null) {
				result.add(getParsedPre());
			}
			if (kind == ConstraintKind.POST && post != null) {
				result.add(getParsedPost());
			}
			result.addAll(engine.documentConditions(operation, kind));
			return result;
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
			return parseLazily(body, false, () -> parsedBody, expr -> parsedBody = expr);
		}

		private OclExpression getParsedPre() throws OclParseException {
			return parseLazily(pre, false, () -> parsedPre, expr -> parsedPre = expr);
		}

		private OclExpression getParsedPost() throws OclParseException {
			return parseLazily(post, true, () -> parsedPost, expr -> parsedPost = expr);
		}

		/**
		 * The variables evaluation binds for this operation: its parameters, and {@code result}
		 * in the postcondition (OCL v2.4 §12.2.1, #324). Declared to the parser, a parameter
		 * name is that parameter — undeclared it could read as a property of an iterator.
		 */
		private Map<String, EClassifier> declaredVariables(boolean withResult) {
			Map<String, EClassifier> variables = new LinkedHashMap<>();
			for (EParameter parameter : operation.getEParameters()) {
				variables.put(parameter.getName(), parameter.getEType());
			}
			if (withResult) {
				variables.put("result", operation.getEType());
			}
			return variables;
		}

		/**
		 * Thread-safe lazy parsing with double-checked locking.
		 * Parses the OCL source string on first access and caches the result.
		 * The {@code fieldReader} re-reads the volatile field inside the synchronized block.
		 */
		private OclExpression parseLazily(String source, boolean withResult, Supplier<OclExpression> fieldReader,
				Consumer<OclExpression> setter) throws OclParseException {
			OclExpression result = fieldReader.get();
			if (result == null) {
				synchronized (this) {
					result = fieldReader.get();
					if (result == null) {
						result = engine.parse(source, operation.getEContainingClass(), declaredVariables(withResult));
						setter.accept(result);
					}
				}
			}
			return result;
		}
	}
}
