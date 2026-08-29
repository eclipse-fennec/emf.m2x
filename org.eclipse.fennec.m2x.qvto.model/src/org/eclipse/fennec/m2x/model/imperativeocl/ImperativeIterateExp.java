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

import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Imperative Iterate Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Imperative iterate expression with explicit target variable (QVT v1.3 Section 8.2.1.5).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.ImperativeIterateExp#getTarget <em>Target</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.ImperativeIterateExp#getName <em>Name</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getImperativeIterateExp()
 * @model
 * @generated
 */
@ProviderType
public interface ImperativeIterateExp extends ImperativeLoopExp {
	/**
	 * Returns the value of the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target</em>' containment reference.
	 * @see #setTarget(Variable)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getImperativeIterateExp_Target()
	 * @model containment="true"
	 * @generated
	 */
	Variable getTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.ImperativeIterateExp#getTarget <em>Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Target</em>' containment reference.
	 * @see #getTarget()
	 * @generated
	 */
	void setTarget(Variable value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The loop variant name: 'xcollect', 'xselect', 'xselectOne', 'xcollectselect' or 'xcollectselectOne' (QVT v1.3 Section 8.2.2.7). The spec derives it from NamedElement; here it is an attribute, as it already is on ForExp.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getImperativeIterateExp_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.ImperativeIterateExp#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

} // ImperativeIterateExp
