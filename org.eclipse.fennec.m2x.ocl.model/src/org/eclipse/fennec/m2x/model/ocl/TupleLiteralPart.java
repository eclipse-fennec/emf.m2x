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

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Tuple Literal Part</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A name-value pair in a tuple literal, e.g. 'name = 'John'' in Tuple{name = 'John', age = 30}.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart#getOwnedInit <em>Owned Init</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getTupleLiteralPart()
 * @model
 * @generated
 */
@ProviderType
public interface TupleLiteralPart extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getTupleLiteralPart_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' reference.
	 * @see #setType(OclType)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getTupleLiteralPart_Type()
	 * @model
	 * @generated
	 */
	OclType getType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart#getType <em>Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' reference.
	 * @see #getType()
	 * @generated
	 */
	void setType(OclType value);

	/**
	 * Returns the value of the '<em><b>Owned Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The value expression for this tuple part.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Init</em>' containment reference.
	 * @see #setOwnedInit(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getTupleLiteralPart_OwnedInit()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedInit();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart#getOwnedInit <em>Owned Init</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Init</em>' containment reference.
	 * @see #getOwnedInit()
	 * @generated
	 */
	void setOwnedInit(OclExpression value);

} // TupleLiteralPart
