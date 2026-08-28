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

import org.eclipse.emf.ecore.EClassifier;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A type literal used in oclIsKindOf(), oclAsType(), etc. (OCL v2.4 Section 8.3.16).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.TypeExp#getReferredType <em>Referred Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getTypeExp()
 * @model
 * @generated
 */
@ProviderType
public interface TypeExp extends OclExpression {
	/**
	 * Returns the value of the '<em><b>Referred Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The OCL type this expression refers to.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred Type</em>' reference.
	 * @see #setReferredType(EClassifier)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getTypeExp_ReferredType()
	 * @model
	 * @generated
	 */
	EClassifier getReferredType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.TypeExp#getReferredType <em>Referred Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred Type</em>' reference.
	 * @see #getReferredType()
	 * @generated
	 */
	void setReferredType(EClassifier value);

} // TypeExp
