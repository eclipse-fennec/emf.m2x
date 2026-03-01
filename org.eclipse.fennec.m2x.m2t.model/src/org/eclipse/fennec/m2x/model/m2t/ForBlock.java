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

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>For Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Iterates over a collection, generating body text for each element (MOFM2T 1.0 Section 8.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getLoopVariable <em>Loop Variable</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getIterSet <em>Iter Set</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getGuard <em>Guard</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getBefore <em>Before</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getEach <em>Each</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getAfter <em>After</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getForBlock()
 * @model
 * @generated
 */
@ProviderType
public interface ForBlock extends Block {
	/**
	 * Returns the value of the '<em><b>Loop Variable</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Loop Variable</em>' containment reference.
	 * @see #setLoopVariable(Variable)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getForBlock_LoopVariable()
	 * @model containment="true"
	 * @generated
	 */
	Variable getLoopVariable();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getLoopVariable <em>Loop Variable</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Loop Variable</em>' containment reference.
	 * @see #getLoopVariable()
	 * @generated
	 */
	void setLoopVariable(Variable value);

	/**
	 * Returns the value of the '<em><b>Iter Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Iter Set</em>' containment reference.
	 * @see #setIterSet(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getForBlock_IterSet()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getIterSet();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getIterSet <em>Iter Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Iter Set</em>' containment reference.
	 * @see #getIterSet()
	 * @generated
	 */
	void setIterSet(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Guard</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Guard</em>' containment reference.
	 * @see #setGuard(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getForBlock_Guard()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getGuard();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getGuard <em>Guard</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Guard</em>' containment reference.
	 * @see #getGuard()
	 * @generated
	 */
	void setGuard(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Before</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Before</em>' containment reference.
	 * @see #setBefore(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getForBlock_Before()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getBefore();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getBefore <em>Before</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Before</em>' containment reference.
	 * @see #getBefore()
	 * @generated
	 */
	void setBefore(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Each</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Each</em>' containment reference.
	 * @see #setEach(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getForBlock_Each()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getEach();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getEach <em>Each</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Each</em>' containment reference.
	 * @see #getEach()
	 * @generated
	 */
	void setEach(OclExpression value);

	/**
	 * Returns the value of the '<em><b>After</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>After</em>' containment reference.
	 * @see #setAfter(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getForBlock_After()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getAfter();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getAfter <em>After</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>After</em>' containment reference.
	 * @see #getAfter()
	 * @generated
	 */
	void setAfter(OclExpression value);

} // ForBlock
