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

import org.eclipse.fennec.m2x.model.qvtbase.Transformation;

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
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation#getRefined <em>Refined</em>}</li>
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
	 * Returns the value of the '<em><b>Intermediate Property</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EStructuralFeature}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Intermediate (contextual) properties of the transformation (QVT v1.3 §8.2.1.3). Contained: the parser never installs them into an EClass, the engine's property store resolves them by context (#127).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Intermediate Property</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getOperationalTransformation_IntermediateProperty()
	 * @model containment="true"
	 * @generated
	 */
	EList<EStructuralFeature> getIntermediateProperty();

	/**
	 * Returns the value of the '<em><b>Refined</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The relational transformation being refined by this operational transformation (QVT v1.3 §8.2.1.1). When set, each mapping operation may refine a specific relation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Refined</em>' reference.
	 * @see #setRefined(Transformation)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getOperationalTransformation_Refined()
	 * @model
	 * @generated
	 */
	Transformation getRefined();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation#getRefined <em>Refined</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Refined</em>' reference.
	 * @see #getRefined()
	 * @generated
	 */
	void setRefined(Transformation value);

} // OperationalTransformation
