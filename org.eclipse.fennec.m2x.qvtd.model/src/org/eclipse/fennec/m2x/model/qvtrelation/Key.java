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
package org.eclipse.fennec.m2x.model.qvtrelation;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Key</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A key defines a set of properties of a class that uniquely identify an instance. Used during enforcement to determine whether to create a new object or update an existing one (QVT v1.3 §7.11.3.5).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.Key#getIdentifies <em>Identifies</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.Key#getPart <em>Part</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.Key#getOppositePart <em>Opposite Part</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.Key#getTransformation <em>Transformation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getKey()
 * @model
 * @generated
 */
@ProviderType
public interface Key extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Identifies</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The class that is identified by the key.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Identifies</em>' reference.
	 * @see #setIdentifies(EClass)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getKey_Identifies()
	 * @model required="true"
	 * @generated
	 */
	EClass getIdentifies();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.Key#getIdentifies <em>Identifies</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Identifies</em>' reference.
	 * @see #getIdentifies()
	 * @generated
	 */
	void setIdentifies(EClass value);

	/**
	 * Returns the value of the '<em><b>Part</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EStructuralFeature}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Properties of the class that make up the key.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Part</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getKey_Part()
	 * @model
	 * @generated
	 */
	EList<EStructuralFeature> getPart();

	/**
	 * Returns the value of the '<em><b>Opposite Part</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EStructuralFeature}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Opposite properties of the class that make up the key.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Opposite Part</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getKey_OppositePart()
	 * @model
	 * @generated
	 */
	EList<EStructuralFeature> getOppositePart();

	/**
	 * Returns the value of the '<em><b>Transformation</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation#getOwnedKey <em>Owned Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The relational transformation that owns this key.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transformation</em>' container reference.
	 * @see #setTransformation(RelationalTransformation)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getKey_Transformation()
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation#getOwnedKey
	 * @model opposite="ownedKey" required="true" transient="false"
	 * @generated
	 */
	RelationalTransformation getTransformation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.Key#getTransformation <em>Transformation</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transformation</em>' container reference.
	 * @see #getTransformation()
	 * @generated
	 */
	void setTransformation(RelationalTransformation value);

} // Key
