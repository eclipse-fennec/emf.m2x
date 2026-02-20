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

import org.eclipse.emf.common.util.EList;

import org.eclipse.fennec.m2m.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Block Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A block of sequentially executed expressions (QVT v1.3 Section 8.2.1.2).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.imperativeocl.BlockExp#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getBlockExp()
 * @model
 * @generated
 */
@ProviderType
public interface BlockExp extends ImperativeExpression {
	/**
	 * Returns the value of the '<em><b>Body</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2m.model.ocl.OclExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Body</em>' containment reference list.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getBlockExp_Body()
	 * @model containment="true"
	 * @generated
	 */
	EList<OclExpression> getBody();

} // BlockExp
