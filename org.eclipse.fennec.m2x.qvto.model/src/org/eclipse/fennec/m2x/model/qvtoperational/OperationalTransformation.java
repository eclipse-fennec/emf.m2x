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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Operational Transformation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An operational (imperative) transformation with model parameters (QVT v1.3 Section 8.2.2.11).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation#getModelParameter <em>Model Parameter</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation#getIntermediateClass <em>Intermediate Class</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation#getIntermediateProperty <em>Intermediate Property</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getOperationalTransformation()
 * @model
 * @generated
 */
@ProviderType
public interface OperationalTransformation extends org.eclipse.fennec.m2x.model.qvtoperational.Module {
	/**
	 * Returns the value of the '<em><b>Model Parameter</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Parameter</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getOperationalTransformation_ModelParameter()
	 * @model containment="true"
	 * @generated
	 */
	EList<ModelParameter> getModelParameter();

	/**
	 * Returns the value of the '<em><b>Intermediate Class</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EClass}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Intermediate Class</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getOperationalTransformation_IntermediateClass()
	 * @model
	 * @generated
	 */
	EList<EClass> getIntermediateClass();

	/**
	 * Returns the value of the '<em><b>Intermediate Property</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EStructuralFeature}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Intermediate Property</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getOperationalTransformation_IntermediateProperty()
	 * @model
	 * @generated
	 */
	EList<EStructuralFeature> getIntermediateProperty();

} // OperationalTransformation
