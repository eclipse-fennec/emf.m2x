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
 * A representation of the model object '<em><b>Resolve In Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * ResolveIn expression: resolve within a specific mapping operation (QVT v1.3 Section 8.2.2.19).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.ResolveInExp#getInMapping <em>In Mapping</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getResolveInExp()
 * @model
 * @generated
 */
@ProviderType
public interface ResolveInExp extends ResolveExp {
	/**
	 * Returns the value of the '<em><b>In Mapping</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>In Mapping</em>' reference.
	 * @see #setInMapping(MappingOperation)
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getResolveInExp_InMapping()
	 * @model
	 * @generated
	 */
	MappingOperation getInMapping();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.qvtoperational.ResolveInExp#getInMapping <em>In Mapping</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Mapping</em>' reference.
	 * @see #getInMapping()
	 * @generated
	 */
	void setInMapping(MappingOperation value);

} // ResolveInExp
