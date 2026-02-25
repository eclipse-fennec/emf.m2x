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
package org.eclipse.fennec.m2x.model.imperativeocl.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.fennec.m2x.model.imperativeocl.ImperativeExpression;
import org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage;

import org.eclipse.fennec.m2x.model.ocl.impl.OclExpressionImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Imperative Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public abstract class ImperativeExpressionImpl extends OclExpressionImpl implements ImperativeExpression {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ImperativeExpressionImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ImperativeOclPackage.Literals.IMPERATIVE_EXPRESSION;
	}

} //ImperativeExpressionImpl
