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

import org.eclipse.emf.ecore.EEnumLiteral;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Enum Literal Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A reference to an enumeration literal, e.g. Color::red (OCL v2.4 Section 8.3.10).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.EnumLiteralExp#getReferredLiteral <em>Referred Literal</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getEnumLiteralExp()
 * @model
 * @generated
 */
@ProviderType
public interface EnumLiteralExp extends LiteralExp {
	/**
	 * Returns the value of the '<em><b>Referred Literal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The Ecore enumeration literal being referenced.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred Literal</em>' reference.
	 * @see #setReferredLiteral(EEnumLiteral)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getEnumLiteralExp_ReferredLiteral()
	 * @model
	 * @generated
	 */
	EEnumLiteral getReferredLiteral();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.EnumLiteralExp#getReferredLiteral <em>Referred Literal</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred Literal</em>' reference.
	 * @see #getReferredLiteral()
	 * @generated
	 */
	void setReferredLiteral(EEnumLiteral value);

} // EnumLiteralExp
