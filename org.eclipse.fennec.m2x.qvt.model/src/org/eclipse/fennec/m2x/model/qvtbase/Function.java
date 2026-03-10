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
package org.eclipse.fennec.m2x.model.qvtbase;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A function is a side-effect-free operation owned by a transformation. Often called a query. May be specified by an OCL expression or have a black-box implementation (QVT v1.3 §7.11.1.5).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Function#getQueryExpression <em>Query Expression</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getFunction()
 * @model
 * @generated
 */
@ProviderType
public interface Function extends EObject, EOperation {
	/**
	 * Returns the value of the '<em><b>Query Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The OCL expression that specifies the function. If absent, a black-box implementation is assumed.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Query Expression</em>' containment reference.
	 * @see #setQueryExpression(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getFunction_QueryExpression()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getQueryExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtbase.Function#getQueryExpression <em>Query Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Query Expression</em>' containment reference.
	 * @see #getQueryExpression()
	 * @generated
	 */
	void setQueryExpression(OclExpression value);

} // Function
