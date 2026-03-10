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
package org.eclipse.fennec.m2x.model.qvtbase;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Typed Model</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A typed model specifies a typed parameter of a transformation. At runtime, a model that is passed to the transformation by this name is constrained to contain only those model elements whose types are specified in the set of model packages (QVT v1.3 §7.11.1.2).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.TypedModel#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.TypedModel#getUsedPackage <em>Used Package</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.TypedModel#getDependsOn <em>Depends On</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getTypedModel()
 * @model
 * @generated
 */
@ProviderType
public interface TypedModel extends EObject, ENamedElement {
	/**
	 * Returns the value of the '<em><b>Transformation</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtbase.Transformation#getModelParameter <em>Model Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The transformation that owns the typed model.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transformation</em>' container reference.
	 * @see #setTransformation(Transformation)
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getTypedModel_Transformation()
	 * @see org.eclipse.fennec.m2x.model.qvtbase.Transformation#getModelParameter
	 * @model opposite="modelParameter" required="true" transient="false"
	 * @generated
	 */
	Transformation getTransformation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtbase.TypedModel#getTransformation <em>Transformation</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transformation</em>' container reference.
	 * @see #getTransformation()
	 * @generated
	 */
	void setTransformation(Transformation value);

	/**
	 * Returns the value of the '<em><b>Used Package</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EPackage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The metamodel packages that specify the types for the model elements of conforming models.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Used Package</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getTypedModel_UsedPackage()
	 * @model required="true"
	 * @generated
	 */
	EList<EPackage> getUsedPackage();

	/**
	 * Returns the value of the '<em><b>Depends On</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtbase.TypedModel}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The set of typed models that this typed model declares a dependency on.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Depends On</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getTypedModel_DependsOn()
	 * @model
	 * @generated
	 */
	EList<TypedModel> getDependsOn();

} // TypedModel
