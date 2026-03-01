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

import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Let Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Variable binding block with optional elseLet chain and else block (MOFM2T 1.0 Section 8.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.LetBlock#getLetVariable <em>Let Variable</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.LetBlock#getElseLet <em>Else Let</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.LetBlock#getElse <em>Else</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getLetBlock()
 * @model
 * @generated
 */
@ProviderType
public interface LetBlock extends Block {
	/**
	 * Returns the value of the '<em><b>Let Variable</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Let Variable</em>' containment reference.
	 * @see #setLetVariable(Variable)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getLetBlock_LetVariable()
	 * @model containment="true"
	 * @generated
	 */
	Variable getLetVariable();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.LetBlock#getLetVariable <em>Let Variable</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Let Variable</em>' containment reference.
	 * @see #getLetVariable()
	 * @generated
	 */
	void setLetVariable(Variable value);

	/**
	 * Returns the value of the '<em><b>Else Let</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.m2t.LetBlock}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Else Let</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getLetBlock_ElseLet()
	 * @model containment="true"
	 * @generated
	 */
	EList<LetBlock> getElseLet();

	/**
	 * Returns the value of the '<em><b>Else</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Else</em>' containment reference.
	 * @see #setElse(Block)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getLetBlock_Else()
	 * @model containment="true"
	 * @generated
	 */
	Block getElse();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.LetBlock#getElse <em>Else</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Else</em>' containment reference.
	 * @see #getElse()
	 * @generated
	 */
	void setElse(Block value);

} // LetBlock
