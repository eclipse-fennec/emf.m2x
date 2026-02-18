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

import org.eclipse.emf.ecore.EClassifier;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Classifier Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Wraps an Ecore EClassifier as an OCL type, used when the OCL type system refers to a user-model classifier.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.ClassifierType#getReferredClassifier <em>Referred Classifier</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getClassifierType()
 * @model
 * @generated
 */
@ProviderType
public interface ClassifierType extends OclType {
	/**
	 * Returns the value of the '<em><b>Referred Classifier</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The Ecore classifier (EClass, EDataType, EEnum) that this OCL type wraps.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred Classifier</em>' reference.
	 * @see #setReferredClassifier(EClassifier)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getClassifierType_ReferredClassifier()
	 * @model
	 * @generated
	 */
	EClassifier getReferredClassifier();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.ClassifierType#getReferredClassifier <em>Referred Classifier</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred Classifier</em>' reference.
	 * @see #getReferredClassifier()
	 * @generated
	 */
	void setReferredClassifier(EClassifier value);

} // ClassifierType
