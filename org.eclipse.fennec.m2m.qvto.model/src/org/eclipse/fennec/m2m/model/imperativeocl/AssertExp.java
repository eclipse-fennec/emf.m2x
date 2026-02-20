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

import org.eclipse.fennec.m2m.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Assert Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Assert expression: assert severity assertion with log (QVT v1.3 Section 8.2.1.3).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getSeverity <em>Severity</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getAssertion <em>Assertion</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getLog <em>Log</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getAssertExp()
 * @model
 * @generated
 */
@ProviderType
public interface AssertExp extends ImperativeExpression {
	/**
	 * Returns the value of the '<em><b>Severity</b></em>' attribute.
	 * The default value is <code>"error"</code>.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Severity</em>' attribute.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind
	 * @see #setSeverity(SeverityKind)
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getAssertExp_Severity()
	 * @model default="error"
	 * @generated
	 */
	SeverityKind getSeverity();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getSeverity <em>Severity</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Severity</em>' attribute.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind
	 * @see #getSeverity()
	 * @generated
	 */
	void setSeverity(SeverityKind value);

	/**
	 * Returns the value of the '<em><b>Assertion</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Assertion</em>' containment reference.
	 * @see #setAssertion(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getAssertExp_Assertion()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getAssertion();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getAssertion <em>Assertion</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Assertion</em>' containment reference.
	 * @see #getAssertion()
	 * @generated
	 */
	void setAssertion(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Log</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Log</em>' containment reference.
	 * @see #setLog(LogExp)
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage#getAssertExp_Log()
	 * @model containment="true"
	 * @generated
	 */
	LogExp getLog();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getLog <em>Log</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Log</em>' containment reference.
	 * @see #getLog()
	 * @generated
	 */
	void setLog(LogExp value);

} // AssertExp
