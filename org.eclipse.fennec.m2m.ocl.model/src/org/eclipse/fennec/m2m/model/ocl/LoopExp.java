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

import org.eclipse.emf.common.util.EList;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Loop Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract base for iterator and iterate expressions that loop over a collection source (OCL v2.4 Section 8.3.5).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.LoopExp#getOwnedBody <em>Owned Body</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.LoopExp#getOwnedIterators <em>Owned Iterators</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getLoopExp()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface LoopExp extends CallExp {
	/**
	 * Returns the value of the '<em><b>Owned Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The body expression evaluated for each element of the source collection.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Body</em>' containment reference.
	 * @see #setOwnedBody(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getLoopExp_OwnedBody()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedBody();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.LoopExp#getOwnedBody <em>Owned Body</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Body</em>' containment reference.
	 * @see #getOwnedBody()
	 * @generated
	 */
	void setOwnedBody(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Owned Iterators</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2m.model.ocl.Variable}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The iterator variable(s) bound to successive elements of the source collection.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Iterators</em>' containment reference list.
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getLoopExp_OwnedIterators()
	 * @model containment="true"
	 * @generated
	 */
	EList<Variable> getOwnedIterators();

} // LoopExp
