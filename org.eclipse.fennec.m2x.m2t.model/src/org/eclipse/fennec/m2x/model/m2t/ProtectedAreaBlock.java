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
package org.eclipse.fennec.m2x.model.m2t;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Protected Area Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A protected area whose content is preserved across re-generation (MOFM2T 1.0 Section 8.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock#getMarker <em>Marker</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getProtectedAreaBlock()
 * @model
 * @generated
 */
@ProviderType
public interface ProtectedAreaBlock extends Block {
	/**
	 * Returns the value of the '<em><b>Marker</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Marker</em>' containment reference.
	 * @see #setMarker(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getProtectedAreaBlock_Marker()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getMarker();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock#getMarker <em>Marker</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Marker</em>' containment reference.
	 * @see #getMarker()
	 * @generated
	 */
	void setMarker(OclExpression value);

} // ProtectedAreaBlock
