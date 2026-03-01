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
 * A representation of the model object '<em><b>Trace Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Associates generated text with a model element for traceability (MOFM2T 1.0 Section 8.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.TraceBlock#getModelElement <em>Model Element</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTraceBlock()
 * @model
 * @generated
 */
@ProviderType
public interface TraceBlock extends Block {
	/**
	 * Returns the value of the '<em><b>Model Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Element</em>' containment reference.
	 * @see #setModelElement(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTraceBlock_ModelElement()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getModelElement();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.TraceBlock#getModelElement <em>Model Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Element</em>' containment reference.
	 * @see #getModelElement()
	 * @generated
	 */
	void setModelElement(OclExpression value);

} // TraceBlock
