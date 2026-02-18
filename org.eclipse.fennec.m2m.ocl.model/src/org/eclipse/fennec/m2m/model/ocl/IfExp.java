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
 * A representation of the model object '<em><b>If Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A conditional expression: if condition then thenExp else elseExp endif (OCL v2.4 Section 8.3.13).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedCondition <em>Owned Condition</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedThen <em>Owned Then</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedElse <em>Owned Else</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getIfExp()
 * @model
 * @generated
 */
@ProviderType
public interface IfExp extends OclExpression {
	/**
	 * Returns the value of the '<em><b>Owned Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Condition</em>' containment reference.
	 * @see #setOwnedCondition(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getIfExp_OwnedCondition()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedCondition();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedCondition <em>Owned Condition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Condition</em>' containment reference.
	 * @see #getOwnedCondition()
	 * @generated
	 */
	void setOwnedCondition(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Owned Then</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Then</em>' containment reference.
	 * @see #setOwnedThen(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getIfExp_OwnedThen()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedThen();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedThen <em>Owned Then</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Then</em>' containment reference.
	 * @see #getOwnedThen()
	 * @generated
	 */
	void setOwnedThen(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Owned Else</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Else</em>' containment reference.
	 * @see #setOwnedElse(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getIfExp_OwnedElse()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedElse();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedElse <em>Owned Else</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Else</em>' containment reference.
	 * @see #getOwnedElse()
	 * @generated
	 */
	void setOwnedElse(OclExpression value);

} // IfExp
