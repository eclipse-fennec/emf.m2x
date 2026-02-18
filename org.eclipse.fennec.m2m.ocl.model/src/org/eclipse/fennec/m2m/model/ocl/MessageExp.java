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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Message Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An OclMessage expression representing an asynchronous operation call or signal send in a postcondition (OCL v2.4 Section 8.3.17).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedTarget <em>Owned Target</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedArguments <em>Owned Arguments</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedCalledOperation <em>Owned Called Operation</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedSentSignal <em>Owned Sent Signal</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getMessageExp()
 * @model
 * @generated
 */
@ProviderType
public interface MessageExp extends OclExpression {
	/**
	 * Returns the value of the '<em><b>Owned Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The target object expression of the message send.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Target</em>' containment reference.
	 * @see #setOwnedTarget(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getMessageExp_OwnedTarget()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedTarget();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedTarget <em>Owned Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Target</em>' containment reference.
	 * @see #getOwnedTarget()
	 * @generated
	 */
	void setOwnedTarget(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Owned Arguments</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2m.model.ocl.OclExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Arguments</em>' containment reference list.
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getMessageExp_OwnedArguments()
	 * @model containment="true"
	 * @generated
	 */
	EList<OclExpression> getOwnedArguments();

	/**
	 * Returns the value of the '<em><b>Owned Called Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The operation being called via message send, or null if this is a signal send.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Called Operation</em>' reference.
	 * @see #setOwnedCalledOperation(EOperation)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getMessageExp_OwnedCalledOperation()
	 * @model
	 * @generated
	 */
	EOperation getOwnedCalledOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedCalledOperation <em>Owned Called Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Called Operation</em>' reference.
	 * @see #getOwnedCalledOperation()
	 * @generated
	 */
	void setOwnedCalledOperation(EOperation value);

	/**
	 * Returns the value of the '<em><b>Owned Sent Signal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The signal being sent, or null if this is an operation call. Typed as EClassifier since Ecore has no signal concept.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Sent Signal</em>' reference.
	 * @see #setOwnedSentSignal(EClassifier)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getMessageExp_OwnedSentSignal()
	 * @model
	 * @generated
	 */
	EClassifier getOwnedSentSignal();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedSentSignal <em>Owned Sent Signal</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Sent Signal</em>' reference.
	 * @see #getOwnedSentSignal()
	 * @generated
	 */
	void setOwnedSentSignal(EClassifier value);

} // MessageExp
