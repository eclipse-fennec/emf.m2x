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

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Message Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The type of an OclMessage, parameterized by the operation or signal that was sent (OCL v2.4 Section 8.2.7).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.MessageType#getReferredOperation <em>Referred Operation</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.MessageType#getReferredSignal <em>Referred Signal</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getMessageType()
 * @model
 * @generated
 */
@ProviderType
public interface MessageType extends OclType {
	/**
	 * Returns the value of the '<em><b>Referred Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The operation that this message type refers to, or null if it refers to a signal.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred Operation</em>' reference.
	 * @see #setReferredOperation(EOperation)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getMessageType_ReferredOperation()
	 * @model
	 * @generated
	 */
	EOperation getReferredOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.MessageType#getReferredOperation <em>Referred Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred Operation</em>' reference.
	 * @see #getReferredOperation()
	 * @generated
	 */
	void setReferredOperation(EOperation value);

	/**
	 * Returns the value of the '<em><b>Referred Signal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The signal that this message type refers to, or null if it refers to an operation. Typed as EClassifier since Ecore has no signal concept.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred Signal</em>' reference.
	 * @see #setReferredSignal(EClassifier)
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getMessageType_ReferredSignal()
	 * @model
	 * @generated
	 */
	EClassifier getReferredSignal();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.ocl.MessageType#getReferredSignal <em>Referred Signal</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred Signal</em>' reference.
	 * @see #getReferredSignal()
	 * @generated
	 */
	void setReferredSignal(EClassifier value);

} // MessageType
