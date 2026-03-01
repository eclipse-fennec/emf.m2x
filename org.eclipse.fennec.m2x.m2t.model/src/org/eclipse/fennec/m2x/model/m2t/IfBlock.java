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

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>If Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Conditional text generation with optional elseIf chain and else block (MOFM2T 1.0 Section 8.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.IfBlock#getIfExpr <em>If Expr</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.IfBlock#getElseIf <em>Else If</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.IfBlock#getElse <em>Else</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getIfBlock()
 * @model
 * @generated
 */
@ProviderType
public interface IfBlock extends Block {
	/**
	 * Returns the value of the '<em><b>If Expr</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>If Expr</em>' containment reference.
	 * @see #setIfExpr(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getIfBlock_IfExpr()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getIfExpr();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.IfBlock#getIfExpr <em>If Expr</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>If Expr</em>' containment reference.
	 * @see #getIfExpr()
	 * @generated
	 */
	void setIfExpr(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Else If</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.m2t.IfBlock}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Else If</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getIfBlock_ElseIf()
	 * @model containment="true"
	 * @generated
	 */
	EList<IfBlock> getElseIf();

	/**
	 * Returns the value of the '<em><b>Else</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Else</em>' containment reference.
	 * @see #setElse(Block)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getIfBlock_Else()
	 * @model containment="true"
	 * @generated
	 */
	Block getElse();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.IfBlock#getElse <em>Else</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Else</em>' containment reference.
	 * @see #getElse()
	 * @generated
	 */
	void setElse(Block value);

} // IfBlock
