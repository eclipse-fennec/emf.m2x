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

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unlink Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Unlink expression to remove a reference between objects (QVT v1.3 Section 8.2.1.11).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.UnlinkExp#getItem <em>Item</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.UnlinkExp#getTarget <em>Target</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getUnlinkExp()
 * @model
 * @generated
 */
@ProviderType
public interface UnlinkExp extends ImperativeExpression {
	/**
	 * Returns the value of the '<em><b>Item</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Item</em>' containment reference.
	 * @see #setItem(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getUnlinkExp_Item()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getItem();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.UnlinkExp#getItem <em>Item</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Item</em>' containment reference.
	 * @see #getItem()
	 * @generated
	 */
	void setItem(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' containment reference.
	 * @see #setTarget(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getUnlinkExp_Target()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.UnlinkExp#getTarget <em>Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' containment reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(OclExpression value);

} // UnlinkExp
