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

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Collection Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A single element in a collection literal, e.g. the '1' in Set{1, 2}.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.CollectionItem#getOwnedItem <em>Owned Item</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getCollectionItem()
 * @model
 * @generated
 */
@ProviderType
public interface CollectionItem extends CollectionLiteralPart {
	/**
	 * Returns the value of the '<em><b>Owned Item</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Item</em>' containment reference.
	 * @see #setOwnedItem(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getCollectionItem_OwnedItem()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedItem();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.CollectionItem#getOwnedItem <em>Owned Item</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Item</em>' containment reference.
	 * @see #getOwnedItem()
	 * @generated
	 */
	void setOwnedItem(OclExpression value);

} // CollectionItem
