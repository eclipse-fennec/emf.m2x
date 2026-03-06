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
package org.eclipse.fennec.m2x.model.qvtbase.impl;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.EParameterImpl;

import org.eclipse.fennec.m2x.model.qvtbase.FunctionParameter;
import org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Function Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * @generated
 */
public class FunctionParameterImpl extends EParameterImpl implements FunctionParameter {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected FunctionParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QvtbasePackage.Literals.FUNCTION_PARAMETER;
	}

} //FunctionParameterImpl
