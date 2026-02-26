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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Blackbox Operation Descriptor</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Describes a single operation provided by a blackbox library (§8.1.4).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor#getContextType <em>Context Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor#getParameterTypes <em>Parameter Types</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor#getReturnType <em>Return Type</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getBlackboxOperationDescriptor()
 * @model
 * @generated
 */
@ProviderType
public interface BlackboxOperationDescriptor extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getBlackboxOperationDescriptor_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Context Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Context type for contextual operations, or null for module-level operations.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Context Type</em>' reference.
	 * @see #setContextType(EClassifier)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getBlackboxOperationDescriptor_ContextType()
	 * @model
	 * @generated
	 */
	EClassifier getContextType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor#getContextType <em>Context Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context Type</em>' reference.
	 * @see #getContextType()
	 * @generated
	 */
	void setContextType(EClassifier value);

	/**
	 * Returns the value of the '<em><b>Parameter Types</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EClassifier}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter Types</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getBlackboxOperationDescriptor_ParameterTypes()
	 * @model
	 * @generated
	 */
	EList<EClassifier> getParameterTypes();

	/**
	 * Returns the value of the '<em><b>Return Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Return Type</em>' reference.
	 * @see #setReturnType(EClassifier)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getBlackboxOperationDescriptor_ReturnType()
	 * @model required="true"
	 * @generated
	 */
	EClassifier getReturnType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor#getReturnType <em>Return Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Return Type</em>' reference.
	 * @see #getReturnType()
	 * @generated
	 */
	void setReturnType(EClassifier value);

} // BlackboxOperationDescriptor
