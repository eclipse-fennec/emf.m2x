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
package org.eclipse.fennec.m2m.model.imperativeocl;

import org.eclipse.emf.ecore.EClassifier;

import org.eclipse.fennec.m2m.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Raise Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Raise expression: raise exception with optional argument (QVT v1.3 Section 8.2.1.8).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp#getArgument <em>Argument</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp#getException <em>Exception</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getRaiseExp()
 * @model
 * @generated
 */
@ProviderType
public interface RaiseExp extends ImperativeExpression {
	/**
	 * Returns the value of the '<em><b>Argument</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Argument</em>' containment reference.
	 * @see #setArgument(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getRaiseExp_Argument()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getArgument();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp#getArgument <em>Argument</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Argument</em>' containment reference.
	 * @see #getArgument()
	 * @generated
	 */
	void setArgument(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Exception</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Exception</em>' reference.
	 * @see #setException(EClassifier)
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getRaiseExp_Exception()
	 * @model required="true"
	 * @generated
	 */
	EClassifier getException();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp#getException <em>Exception</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Exception</em>' reference.
	 * @see #getException()
	 * @generated
	 */
	void setException(EClassifier value);

} // RaiseExp
