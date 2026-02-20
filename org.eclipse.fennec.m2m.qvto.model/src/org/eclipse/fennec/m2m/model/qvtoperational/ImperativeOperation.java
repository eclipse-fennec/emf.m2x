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
package org.eclipse.fennec.m2m.model.qvtoperational;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Imperative Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An imperative operation with body, context, and result parameters (QVT v1.3 Section 8.2.2.5).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation#isIsBlackbox <em>Is Blackbox</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation#getBody <em>Body</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation#getContext <em>Context</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation#getResult <em>Result</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation#getOverridden <em>Overridden</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getImperativeOperation()
 * @model
 * @generated
 */
@ProviderType
public interface ImperativeOperation extends EObject, EOperation {
	/**
	 * Returns the value of the '<em><b>Is Blackbox</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Blackbox</em>' attribute.
	 * @see #setIsBlackbox(boolean)
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getImperativeOperation_IsBlackbox()
	 * @model
	 * @generated
	 */
	boolean isIsBlackbox();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation#isIsBlackbox <em>Is Blackbox</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Blackbox</em>' attribute.
	 * @see #isIsBlackbox()
	 * @generated
	 */
	void setIsBlackbox(boolean value);

	/**
	 * Returns the value of the '<em><b>Body</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2m.model.qvtoperational.OperationBody#getOperation <em>Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Body</em>' containment reference.
	 * @see #setBody(OperationBody)
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getImperativeOperation_Body()
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.OperationBody#getOperation
	 * @model opposite="operation" containment="true"
	 * @generated
	 */
	OperationBody getBody();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation#getBody <em>Body</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Body</em>' containment reference.
	 * @see #getBody()
	 * @generated
	 */
	void setBody(OperationBody value);

	/**
	 * Returns the value of the '<em><b>Context</b></em>' containment reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2m.model.qvtoperational.VarParameter#getCtxOwner <em>Ctx Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' containment reference.
	 * @see #setContext(VarParameter)
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getImperativeOperation_Context()
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.VarParameter#getCtxOwner
	 * @model opposite="ctxOwner" containment="true"
	 * @generated
	 */
	VarParameter getContext();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation#getContext <em>Context</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' containment reference.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(VarParameter value);

	/**
	 * Returns the value of the '<em><b>Result</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2m.model.qvtoperational.VarParameter}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2m.model.qvtoperational.VarParameter#getResOwner <em>Res Owner</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result</em>' containment reference list.
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getImperativeOperation_Result()
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.VarParameter#getResOwner
	 * @model opposite="resOwner" containment="true"
	 * @generated
	 */
	EList<VarParameter> getResult();

	/**
	 * Returns the value of the '<em><b>Overridden</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Overridden</em>' reference.
	 * @see #setOverridden(ImperativeOperation)
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getImperativeOperation_Overridden()
	 * @model
	 * @generated
	 */
	ImperativeOperation getOverridden();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation#getOverridden <em>Overridden</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Overridden</em>' reference.
	 * @see #getOverridden()
	 * @generated
	 */
	void setOverridden(ImperativeOperation value);

} // ImperativeOperation
