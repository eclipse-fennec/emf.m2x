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

import org.eclipse.fennec.m2x.model.qvtbase.Transformation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relational Transformation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A RelationalTransformation is a specialization of a Transformation representing a transformation definition that uses the QVT-Relation formalism (QVT v1.3 §7.11.3.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation#getOwnedKey <em>Owned Key</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationalTransformation()
 * @model
 * @generated
 */
@ProviderType
public interface RelationalTransformation extends Transformation {
	/**
	 * Returns the value of the '<em><b>Owned Key</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtrelation.Key}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtrelation.Key#getTransformation <em>Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The keys defined within the transformation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Key</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationalTransformation_OwnedKey()
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.Key#getTransformation
	 * @model opposite="transformation" containment="true"
	 * @generated
	 */
	EList<Key> getOwnedKey();

} // RelationalTransformation
