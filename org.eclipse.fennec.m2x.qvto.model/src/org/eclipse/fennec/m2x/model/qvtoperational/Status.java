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
package org.eclipse.fennec.m2x.model.qvtoperational;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Status</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Status of a transformation execution: succeeded, failed, or pending (QVT v1.3 Section 8.3.6).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.Status#isSucceeded <em>Succeeded</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.Status#isFailed <em>Failed</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.Status#getRaisedException <em>Raised Exception</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getStatus()
 * @model
 * @generated
 */
@ProviderType
public interface Status extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Succeeded</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Succeeded</em>' attribute.
	 * @see #setSucceeded(boolean)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getStatus_Succeeded()
	 * @model
	 * @generated
	 */
	boolean isSucceeded();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.Status#isSucceeded <em>Succeeded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Succeeded</em>' attribute.
	 * @see #isSucceeded()
	 * @generated
	 */
	void setSucceeded(boolean value);

	/**
	 * Returns the value of the '<em><b>Failed</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Failed</em>' attribute.
	 * @see #setFailed(boolean)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getStatus_Failed()
	 * @model
	 * @generated
	 */
	boolean isFailed();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.Status#isFailed <em>Failed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Failed</em>' attribute.
	 * @see #isFailed()
	 * @generated
	 */
	void setFailed(boolean value);

	/**
	 * Returns the value of the '<em><b>Raised Exception</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Raised Exception</em>' attribute.
	 * @see #setRaisedException(Exception)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getStatus_RaisedException()
	 * @model dataType="org.eclipse.fennec.m2x.model.qvtoperational.JavaException" transient="true"
	 * @generated
	 */
	Exception getRaisedException();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.Status#getRaisedException <em>Raised Exception</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Raised Exception</em>' attribute.
	 * @see #getRaisedException()
	 * @generated
	 */
	void setRaisedException(Exception value);

} // Status
