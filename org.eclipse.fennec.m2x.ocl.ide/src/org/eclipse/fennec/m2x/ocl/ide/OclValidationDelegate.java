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
package org.eclipse.fennec.m2x.ocl.ide;

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.fennec.m2x.ocl.engine.OclDelegateFactories;

/**
 * Extension-registry entry point for the OCL validation delegate.
 *
 * <p>Registered by {@code plugin.xml} on the
 * {@code org.eclipse.emf.ecore.validation_delegate} extension point. EMF
 * instantiates this class via its public no-argument constructor and invokes it
 * to evaluate OCL invariant constraints on EClasses and EOperations when a model
 * is validated in the generic EMF editor.
 *
 * <p>The actual evaluation is delegated to the Fennec OCL engine via
 * {@link OclDelegateFactories}; this class only adapts the engine delegate to
 * the registry's no-argument instantiation contract.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class OclValidationDelegate implements EValidator.ValidationDelegate {

	private final EValidator.ValidationDelegate delegate =
			OclDelegateFactories.newValidationDelegate(SharedOclEngine.get());

	/**
	 * Public no-argument constructor required by the Eclipse extension registry.
	 */
	public OclValidationDelegate() {
		// instantiated reflectively by the extension registry
	}

	@Override
	public boolean validate(EClass eClass, EObject eObject, Map<Object, Object> context,
			EOperation invariant, String expression) {
		return delegate.validate(eClass, eObject, context, invariant, expression);
	}

	@Override
	public boolean validate(EClass eClass, EObject eObject, Map<Object, Object> context,
			String constraint, String expression) {
		return delegate.validate(eClass, eObject, context, constraint, expression);
	}

	@Override
	public boolean validate(EDataType eDataType, Object value, Map<Object, Object> context,
			String constraint, String expression) {
		return delegate.validate(eDataType, value, context, constraint, expression);
	}
}
