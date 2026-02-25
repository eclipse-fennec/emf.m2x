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
package org.eclipse.fennec.m2x.model.ocl;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>State Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A reference to a UML State, used as argument to oclIsInState(). The referredState is typed generically as EObject to avoid a UML metamodel dependency (OCL v2.4 Section 7.5.10).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.StateExp#getReferredState <em>Referred State</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getStateExp()
 * @model
 * @generated
 */
@ProviderType
public interface StateExp extends OclExpression {
	/**
	 * Returns the value of the '<em><b>Referred State</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The UML State being referenced. Typed as EObject; the ocl.uml bundle casts to the concrete UML State type at runtime.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred State</em>' reference.
	 * @see #setReferredState(EObject)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getStateExp_ReferredState()
	 * @model
	 * @generated
	 */
	EObject getReferredState();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.StateExp#getReferredState <em>Referred State</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred State</em>' reference.
	 * @see #getReferredState()
	 * @generated
	 */
	void setReferredState(EObject value);

} // StateExp
