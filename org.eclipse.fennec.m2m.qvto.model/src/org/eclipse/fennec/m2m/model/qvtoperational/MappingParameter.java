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
package org.eclipse.fennec.m2m.model.qvtoperational;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mapping Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A mapping operation parameter with optional extent binding (QVT v1.3 Section 8.2.2.12).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.MappingParameter#getExtent <em>Extent</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getMappingParameter()
 * @model
 * @generated
 */
@ProviderType
public interface MappingParameter extends VarParameter {
	/**
	 * Returns the value of the '<em><b>Extent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Extent</em>' reference.
	 * @see #setExtent(ModelParameter)
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getMappingParameter_Extent()
	 * @model
	 * @generated
	 */
	ModelParameter getExtent();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.qvtoperational.MappingParameter#getExtent <em>Extent</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Extent</em>' reference.
	 * @see #getExtent()
	 * @generated
	 */
	void setExtent(ModelParameter value);

} // MappingParameter
