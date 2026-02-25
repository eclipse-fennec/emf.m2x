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

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Call Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract base for all call expressions. A call has a source expression, and may be implicit or use safe navigation (OCL v2.4 Section 8.3.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.CallExp#getOwnedSource <em>Owned Source</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.CallExp#isIsImplicit <em>Is Implicit</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.CallExp#isIsSafe <em>Is Safe</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getCallExp()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface CallExp extends OclExpression {
	/**
	 * Returns the value of the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The source expression on which this call is evaluated. E.g. in 'self.name', 'self' is the source.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Source</em>' containment reference.
	 * @see #setOwnedSource(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getCallExp_OwnedSource()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedSource();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.CallExp#getOwnedSource <em>Owned Source</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Source</em>' containment reference.
	 * @see #getOwnedSource()
	 * @generated
	 */
	void setOwnedSource(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * True if the source was not explicitly written in the OCL text but inserted by the compiler (e.g. implicit 'self').
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Is Implicit</em>' attribute.
	 * @see #setIsImplicit(boolean)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getCallExp_IsImplicit()
	 * @model
	 * @generated
	 */
	boolean isIsImplicit();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.CallExp#isIsImplicit <em>Is Implicit</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Implicit</em>' attribute.
	 * @see #isIsImplicit()
	 * @generated
	 */
	void setIsImplicit(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * OCL v2.5: true for safe navigation operators (?. and ?->) which return null instead of OclInvalid when the source is null.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Is Safe</em>' attribute.
	 * @see #setIsSafe(boolean)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getCallExp_IsSafe()
	 * @model
	 * @generated
	 */
	boolean isIsSafe();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.CallExp#isIsSafe <em>Is Safe</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Safe</em>' attribute.
	 * @see #isIsSafe()
	 * @generated
	 */
	void setIsSafe(boolean value);

} // CallExp
