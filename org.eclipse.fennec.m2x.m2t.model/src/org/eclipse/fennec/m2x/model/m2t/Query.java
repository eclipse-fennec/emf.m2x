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
package org.eclipse.fennec.m2x.model.m2t;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClassifier;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Query</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A named query that evaluates an OCL expression and returns a value (MOFM2T 1.0 Section 8.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Query#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Query#getExpression <em>Expression</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Query#getReturnType <em>Return Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getQuery()
 * @model
 * @generated
 */
@ProviderType
public interface Query extends ModuleElement {
	/**
	 * Returns the value of the '<em><b>Parameter</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.Variable}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getQuery_Parameter()
	 * @model containment="true"
	 * @generated
	 */
	EList<Variable> getParameter();

	/**
	 * Returns the value of the '<em><b>Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Expression</em>' containment reference.
	 * @see #setExpression(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getQuery_Expression()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.Query#getExpression <em>Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Expression</em>' containment reference.
	 * @see #getExpression()
	 * @generated
	 */
	void setExpression(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Return Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The declared return type of this query. Named 'returnType' to avoid conflict with OclExpression.type.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Return Type</em>' reference.
	 * @see #setReturnType(EClassifier)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getQuery_ReturnType()
	 * @model
	 * @generated
	 */
	EClassifier getReturnType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.Query#getReturnType <em>Return Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Type</em>' reference.
	 * @see #getReturnType()
	 * @generated
	 */
	void setReturnType(EClassifier value);

} // Query
