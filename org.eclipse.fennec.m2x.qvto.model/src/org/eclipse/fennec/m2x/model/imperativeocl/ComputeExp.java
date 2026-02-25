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
package org.eclipse.fennec.m2x.model.imperativeocl;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Compute Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Compute expression: compute(var) { body } — evaluates body, returns var (QVT v1.3 Section 8.2.1.7).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.ComputeExp#getReturnedElement <em>Returned Element</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.ComputeExp#getBody <em>Body</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getComputeExp()
 * @model
 * @generated
 */
@ProviderType
public interface ComputeExp extends ImperativeExpression {
	/**
	 * Returns the value of the '<em><b>Returned Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Returned Element</em>' containment reference.
	 * @see #setReturnedElement(Variable)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getComputeExp_ReturnedElement()
	 * @model containment="true" required="true"
	 * @generated
	 */
	Variable getReturnedElement();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.ComputeExp#getReturnedElement <em>Returned Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Returned Element</em>' containment reference.
	 * @see #getReturnedElement()
	 * @generated
	 */
	void setReturnedElement(Variable value);

	/**
	 * Returns the value of the '<em><b>Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Body</em>' containment reference.
	 * @see #setBody(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage#getComputeExp_Body()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getBody();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.imperativeocl.ComputeExp#getBody <em>Body</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Body</em>' containment reference.
	 * @see #getBody()
	 * @generated
	 */
	void setBody(OclExpression value);

} // ComputeExp
