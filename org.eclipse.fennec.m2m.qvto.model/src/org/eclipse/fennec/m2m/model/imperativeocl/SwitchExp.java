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

import org.eclipse.emf.common.util.EList;

import org.eclipse.fennec.m2m.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Switch Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Switch expression with alternative parts and optional else (QVT v1.3 Section 8.2.1.6).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp#getAlternativePart <em>Alternative Part</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp#getElsePart <em>Else Part</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getSwitchExp()
 * @model
 * @generated
 */
@ProviderType
public interface SwitchExp extends ImperativeExpression {
	/**
	 * Returns the value of the '<em><b>Alternative Part</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2m.model.imperativeocl.AltExp}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Alternative Part</em>' containment reference list.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getSwitchExp_AlternativePart()
	 * @model containment="true"
	 * @generated
	 */
	EList<AltExp> getAlternativePart();

	/**
	 * Returns the value of the '<em><b>Else Part</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Else Part</em>' containment reference.
	 * @see #setElsePart(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getSwitchExp_ElsePart()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getElsePart();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp#getElsePart <em>Else Part</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Else Part</em>' containment reference.
	 * @see #getElsePart()
	 * @generated
	 */
	void setElsePart(OclExpression value);

} // SwitchExp
