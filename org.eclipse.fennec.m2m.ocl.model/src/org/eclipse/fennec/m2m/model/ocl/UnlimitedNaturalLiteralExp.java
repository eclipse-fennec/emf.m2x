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
package org.eclipse.fennec.m2m.model.ocl;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unlimited Natural Literal Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An unlimited natural literal: a non-negative integer or the special value * (unlimited). The value -1 represents *.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp#getUnlimitedNaturalSymbol <em>Unlimited Natural Symbol</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getUnlimitedNaturalLiteralExp()
 * @model
 * @generated
 */
@ProviderType
public interface UnlimitedNaturalLiteralExp extends PrimitiveLiteralExp {
	/**
	 * Returns the value of the '<em><b>Unlimited Natural Symbol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Unlimited Natural Symbol</em>' attribute.
	 * @see #setUnlimitedNaturalSymbol(long)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getUnlimitedNaturalLiteralExp_UnlimitedNaturalSymbol()
	 * @model
	 * @generated
	 */
	long getUnlimitedNaturalSymbol();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp#getUnlimitedNaturalSymbol <em>Unlimited Natural Symbol</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unlimited Natural Symbol</em>' attribute.
	 * @see #getUnlimitedNaturalSymbol()
	 * @generated
	 */
	void setUnlimitedNaturalSymbol(long value);

} // UnlimitedNaturalLiteralExp
