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

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Map Literal Part</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A key-value pair in a map literal. OCL v2.5 extension.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.MapLiteralPart#getOwnedKey <em>Owned Key</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.MapLiteralPart#getOwnedValue <em>Owned Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getMapLiteralPart()
 * @model
 * @generated
 */
@ProviderType
public interface MapLiteralPart extends EObject {
	/**
	 * Returns the value of the '<em><b>Owned Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Key</em>' containment reference.
	 * @see #setOwnedKey(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getMapLiteralPart_OwnedKey()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedKey();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.MapLiteralPart#getOwnedKey <em>Owned Key</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Key</em>' containment reference.
	 * @see #getOwnedKey()
	 * @generated
	 */
	void setOwnedKey(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Owned Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Value</em>' containment reference.
	 * @see #setOwnedValue(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getMapLiteralPart_OwnedValue()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedValue();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.MapLiteralPart#getOwnedValue <em>Owned Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Value</em>' containment reference.
	 * @see #getOwnedValue()
	 * @generated
	 */
	void setOwnedValue(OclExpression value);

} // MapLiteralPart
