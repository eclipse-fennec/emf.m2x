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
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEngine;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;

/**
 * EMF {@link EOperation.Internal.InvocationDelegate.Factory} that evaluates
 * OCL body expressions annotated on EOperations.
 *
 * <p>Reads the OCL expression from the EAnnotation with source
 * {@value OclDelegateUri#URI}, detail key {@code "body"}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclInvocationDelegateFactory implements EOperation.Internal.InvocationDelegate.Factory {

	private final OclEngine engine;

	public OclInvocationDelegateFactory(OclEngine engine) {
		this.engine = engine;
	}

	@Override
	public EOperation.Internal.InvocationDelegate createInvocationDelegate(EOperation operation) {
		String body = OclDelegateUtil.getAnnotationDetail(operation, "body");
		if (body == null) {
			throw new IllegalStateException(
					"No OCL body annotation on operation: " + operation.getName());
		}
		return new OclInvocationDelegate(operation, body);
	}

	private class OclInvocationDelegate implements EOperation.Internal.InvocationDelegate {

		private final EOperation operation;
		private final String body;
		private volatile OclExpression parsed;

		OclInvocationDelegate(EOperation operation, String body) {
			this.operation = operation;
			this.body = body;
		}

		@Override
		public Object dynamicInvoke(InternalEObject target, EList<?> arguments)
				throws InvocationTargetException {
			try {
				OclExpression expression = getParsedExpression();
				Map<String, Object> variables = bindArguments(arguments);
				OclContext context = OclContext.of(target, variables);
				return engine.evaluate(expression, context);
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

		private OclExpression getParsedExpression() throws OclParseException {
			OclExpression result = parsed;
			if (result == null) {
				synchronized (this) {
					result = parsed;
					if (result == null) {
						result = engine.parse(body, operation.getEContainingClass());
						parsed = result;
					}
				}
			}
			return result;
		}
	}
}
