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
package org.eclipse.fennec.m2x.model.imperativeocl;

import org.eclipse.emf.common.util.EList;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assign Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Assignment expression: left := value (QVT v1.3 Section 8.2.1.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.AssignExp#isIsReset <em>Is Reset</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.AssignExp#isIsSubtract <em>Is Subtract</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.AssignExp#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.AssignExp#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.AssignExp#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getAssignExp()
 * @model
 * @generated
 */
@ProviderType
public interface AssignExp extends ImperativeExpression {
	/**
	 * Returns the value of the '<em><b>Is Reset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * If true, the assignment resets the target before assigning (= vs +=).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Is Reset</em>' attribute.
	 * @see #setIsReset(boolean)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getAssignExp_IsReset()
	 * @model
	 * @generated
	 */
	boolean isIsReset();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.AssignExp#isIsReset <em>Is Reset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Reset</em>' attribute.
	 * @see #isIsReset()
	 * @generated
	 */
	void setIsReset(boolean value);

	/**
	 * Returns the value of the '<em><b>Is Subtract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * If true, removes the value from the target collection (-= operator).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Is Subtract</em>' attribute.
	 * @see #setIsSubtract(boolean)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getAssignExp_IsSubtract()
	 * @model
	 * @generated
	 */
	boolean isIsSubtract();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.AssignExp#isIsSubtract <em>Is Subtract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Subtract</em>' attribute.
	 * @see #isIsSubtract()
	 * @generated
	 */
	void setIsSubtract(boolean value);

	/**
	 * Returns the value of the '<em><b>Left</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Left</em>' containment reference.
	 * @see #setLeft(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getAssignExp_Left()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getLeft();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.AssignExp#getLeft <em>Left</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Left</em>' containment reference.
	 * @see #getLeft()
	 * @generated
	 */
	void setLeft(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.OclExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getAssignExp_Value()
	 * @model containment="true"
	 * @generated
	 */
	EList<OclExpression> getValue();

	/**
	 * Returns the value of the '<em><b>Default Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Default Value</em>' containment reference.
	 * @see #setDefaultValue(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getAssignExp_DefaultValue()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getDefaultValue();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.AssignExp#getDefaultValue <em>Default Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Default Value</em>' containment reference.
	 * @see #getDefaultValue()
	 * @generated
	 */
	void setDefaultValue(OclExpression value);

} // AssignExp
