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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transformation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A transformation defines how one set of models can be transformed into another. It contains a set of rules that specify its execution behavior. Syntactically extends EPackage for namespace semantics (QVT v1.3 §7.11.1.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Transformation#getModelParameter <em>Model Parameter</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Transformation#getRule <em>Rule</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Transformation#getOwnedTag <em>Owned Tag</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Transformation#getExtends <em>Extends</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getTransformation()
 * @model
 * @generated
 */
@ProviderType
public interface Transformation extends EObject, EPackage {
	/**
	 * Returns the value of the '<em><b>Model Parameter</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtbase.TypedModel}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtbase.TypedModel#getTransformation <em>Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The set of typed models, which specify the types of models that may participate in the transformation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Model Parameter</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getTransformation_ModelParameter()
	 * @see org.eclipse.fennec.m2x.model.qvtbase.TypedModel#getTransformation
	 * @model opposite="transformation" containment="true"
	 * @generated
	 */
	EList<TypedModel> getModelParameter();

	/**
	 * Returns the value of the '<em><b>Rule</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtbase.Rule}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtbase.Rule#getTransformation <em>Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The rules owned by the transformation, which together specify the execution behavior of the transformation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Rule</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getTransformation_Rule()
	 * @see org.eclipse.fennec.m2x.model.qvtbase.Rule#getTransformation
	 * @model opposite="transformation" containment="true"
	 * @generated
	 */
	EList<Rule> getRule();

	/**
	 * Returns the value of the '<em><b>Owned Tag</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtbase.Tag}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtbase.Tag#getOwner <em>Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The set of tags associated with the transformation whose values may be used to configure its runtime environment.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Tag</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getTransformation_OwnedTag()
	 * @see org.eclipse.fennec.m2x.model.qvtbase.Tag#getOwner
	 * @model opposite="owner" containment="true"
	 * @generated
	 */
	EList<Tag> getOwnedTag();

	/**
	 * Returns the value of the '<em><b>Extends</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A transformation can extend another transformation. The rules of the extended transformation are included in the extending transformation. Extension is transitive.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Extends</em>' reference.
	 * @see #setExtends(Transformation)
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getTransformation_Extends()
	 * @model
	 * @generated
	 */
	Transformation getExtends();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtbase.Transformation#getExtends <em>Extends</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extends</em>' reference.
	 * @see #getExtends()
	 * @generated
	 */
	void setExtends(Transformation value);

} // Transformation
