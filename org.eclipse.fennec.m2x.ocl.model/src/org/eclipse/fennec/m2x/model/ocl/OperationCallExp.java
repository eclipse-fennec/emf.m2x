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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EOperation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operation Call Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Invocation of an operation, including infix operators (+, =, etc.) and built-in OCL operations (OCL v2.4 Section 8.3.4).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.OperationCallExp#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.OperationCallExp#getOwnedArguments <em>Owned Arguments</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.OperationCallExp#getReferredOperation <em>Referred Operation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getOperationCallExp()
 * @model
 * @generated
 */
@ProviderType
public interface OperationCallExp extends FeatureCallExp {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The name of the called operation (e.g. 'size', '+', 'oclIsKindOf'). Always set by the parser, regardless of whether referredOperation is null. Used for stdlib dispatch in the evaluator.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getOperationCallExp_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.OperationCallExp#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Owned Arguments</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.OclExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Arguments</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getOperationCallExp_OwnedArguments()
	 * @model containment="true"
	 * @generated
	 */
	EList<OclExpression> getOwnedArguments();

	/**
	 * Returns the value of the '<em><b>Referred Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The Ecore operation being called. May be null for built-in OCL operations that have no EOperation counterpart.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred Operation</em>' reference.
	 * @see #setReferredOperation(EOperation)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getOperationCallExp_ReferredOperation()
	 * @model
	 * @generated
	 */
	EOperation getReferredOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.OperationCallExp#getReferredOperation <em>Referred Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred Operation</em>' reference.
	 * @see #getReferredOperation()
	 * @generated
	 */
	void setReferredOperation(EOperation value);

} // OperationCallExp
