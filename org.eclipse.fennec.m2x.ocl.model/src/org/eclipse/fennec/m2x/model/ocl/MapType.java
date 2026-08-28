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

import org.eclipse.emf.ecore.EClassifier;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Map Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The Map(K, V) type. OCL v2.5 extension for key-value mappings.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.MapType#getKeyType <em>Key Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.MapType#getValueType <em>Value Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getMapType()
 * @model
 * @generated
 */
@ProviderType
public interface MapType extends OclType {
	/**
	 * Returns the value of the '<em><b>Key Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Key Type</em>' reference.
	 * @see #setKeyType(EClassifier)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getMapType_KeyType()
	 * @model
	 * @generated
	 */
	EClassifier getKeyType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.MapType#getKeyType <em>Key Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Key Type</em>' reference.
	 * @see #getKeyType()
	 * @generated
	 */
	void setKeyType(EClassifier value);

	/**
	 * Returns the value of the '<em><b>Value Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value Type</em>' reference.
	 * @see #setValueType(EClassifier)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getMapType_ValueType()
	 * @model
	 * @generated
	 */
	EClassifier getValueType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.MapType#getValueType <em>Value Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Type</em>' reference.
	 * @see #getValueType()
	 * @generated
	 */
	void setValueType(EClassifier value);

} // MapType
