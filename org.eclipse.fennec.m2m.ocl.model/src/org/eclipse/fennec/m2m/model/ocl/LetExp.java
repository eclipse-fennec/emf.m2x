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
package org.eclipse.fennec.m2m.model.ocl;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Let Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A let expression that introduces a local variable: let x : T = initExp in bodyExp (OCL v2.4 Section 8.3.14).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.LetExp#getOwnedVariable <em>Owned Variable</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.LetExp#getOwnedIn <em>Owned In</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getLetExp()
 * @model
 * @generated
 */
@ProviderType
public interface LetExp extends OclExpression {
	/**
	 * Returns the value of the '<em><b>Owned Variable</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The variable introduced by this let expression.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Variable</em>' containment reference.
	 * @see #setOwnedVariable(Variable)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getLetExp_OwnedVariable()
	 * @model containment="true"
	 * @generated
	 */
	Variable getOwnedVariable();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.LetExp#getOwnedVariable <em>Owned Variable</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Variable</em>' containment reference.
	 * @see #getOwnedVariable()
	 * @generated
	 */
	void setOwnedVariable(Variable value);

	/**
	 * Returns the value of the '<em><b>Owned In</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The body expression in which the variable is in scope.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned In</em>' containment reference.
	 * @see #setOwnedIn(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getLetExp_OwnedIn()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedIn();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.LetExp#getOwnedIn <em>Owned In</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned In</em>' containment reference.
	 * @see #getOwnedIn()
	 * @generated
	 */
	void setOwnedIn(OclExpression value);

} // LetExp
