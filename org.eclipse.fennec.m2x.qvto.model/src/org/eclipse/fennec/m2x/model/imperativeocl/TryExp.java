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
 * A representation of the model object '<em><b>Try Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Try-catch expression for exception handling (QVT v1.3 Section 8.2.1.8).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.TryExp#getTryBody <em>Try Body</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.TryExp#getExceptClause <em>Except Clause</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getTryExp()
 * @model
 * @generated
 */
@ProviderType
public interface TryExp extends ImperativeExpression {
	/**
	 * Returns the value of the '<em><b>Try Body</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.OclExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Try Body</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getTryExp_TryBody()
	 * @model containment="true"
	 * @generated
	 */
	EList<OclExpression> getTryBody();

	/**
	 * Returns the value of the '<em><b>Except Clause</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.imperativeocl.CatchExp}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Except Clause</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getTryExp_ExceptClause()
	 * @model containment="true"
	 * @generated
	 */
	EList<CatchExp> getExceptClause();

} // TryExp
