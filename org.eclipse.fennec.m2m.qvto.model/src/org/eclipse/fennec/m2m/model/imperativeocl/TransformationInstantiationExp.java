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
package org.eclipse.fennec.m2m.model.imperativeocl;

import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Transformation Instantiation Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Instantiation of an imported transformation via new T(args) (QVT v1.3 Section 8.1.13).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.imperativeocl.TransformationInstantiationExp#getImportedTransformation <em>Imported Transformation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getTransformationInstantiationExp()
 * @model
 * @generated
 */
@ProviderType
public interface TransformationInstantiationExp extends InstantiationExp {
	/**
	 * Returns the value of the '<em><b>Imported Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Imported Transformation</em>' reference.
	 * @see #setImportedTransformation(OperationalTransformation)
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getTransformationInstantiationExp_ImportedTransformation()
	 * @model
	 * @generated
	 */
	OperationalTransformation getImportedTransformation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.imperativeocl.TransformationInstantiationExp#getImportedTransformation <em>Imported Transformation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Imported Transformation</em>' reference.
	 * @see #getImportedTransformation()
	 * @generated
	 */
	void setImportedTransformation(OperationalTransformation value);

} // TransformationInstantiationExp
