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
package org.eclipse.fennec.m2x.model.ocl;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Collection Range</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A range in a collection literal, e.g. the '1..10' in Sequence{1..10}.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.CollectionRange#getOwnedFirst <em>Owned First</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.CollectionRange#getOwnedLast <em>Owned Last</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getCollectionRange()
 * @model
 * @generated
 */
@ProviderType
public interface CollectionRange extends CollectionLiteralPart {
	/**
	 * Returns the value of the '<em><b>Owned First</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned First</em>' containment reference.
	 * @see #setOwnedFirst(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getCollectionRange_OwnedFirst()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedFirst();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.CollectionRange#getOwnedFirst <em>Owned First</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned First</em>' containment reference.
	 * @see #getOwnedFirst()
	 * @generated
	 */
	void setOwnedFirst(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Owned Last</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Last</em>' containment reference.
	 * @see #setOwnedLast(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getCollectionRange_OwnedLast()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedLast();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.CollectionRange#getOwnedLast <em>Owned Last</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Last</em>' containment reference.
	 * @see #getOwnedLast()
	 * @generated
	 */
	void setOwnedLast(OclExpression value);

} // CollectionRange
