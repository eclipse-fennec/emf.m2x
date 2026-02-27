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
package org.eclipse.fennec.m2x.model.qvtoperational;

import org.eclipse.fennec.m2x.model.ocl.CollectionKind;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Model Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A model parameter representing a model extent of the transformation (QVT v1.3 Section 8.2.2.4). When collectionKind is set, this parameter accepts a collection of model extents (§8.1.1, §8.2.1.5).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter#getCollectionKind <em>Collection Kind</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getModelParameter()
 * @model
 * @generated
 */
@ProviderType
public interface ModelParameter extends VarParameter {
	/**
	 * Returns the value of the '<em><b>Collection Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.ocl.CollectionKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Collection Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.ocl.CollectionKind
	 * @see #isSetCollectionKind()
	 * @see #unsetCollectionKind()
	 * @see #setCollectionKind(CollectionKind)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getModelParameter_CollectionKind()
	 * @model unsettable="true"
	 * @generated
	 */
	CollectionKind getCollectionKind();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter#getCollectionKind <em>Collection Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Collection Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.ocl.CollectionKind
	 * @see #isSetCollectionKind()
	 * @see #unsetCollectionKind()
	 * @see #getCollectionKind()
	 * @generated
	 */
	void setCollectionKind(CollectionKind value);

	/**
	 * Unsets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter#getCollectionKind <em>Collection Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSetCollectionKind()
	 * @see #getCollectionKind()
	 * @see #setCollectionKind(CollectionKind)
	 * @generated
	 */
	void unsetCollectionKind();

	/**
	 * Returns whether the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter#getCollectionKind <em>Collection Kind</em>}' attribute is set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return whether the value of the '<em>Collection Kind</em>' attribute is set.
	 * @see #unsetCollectionKind()
	 * @see #getCollectionKind()
	 * @see #setCollectionKind(CollectionKind)
	 * @generated
	 */
	boolean isSetCollectionKind();

} // ModelParameter
