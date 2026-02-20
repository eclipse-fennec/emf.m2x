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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.fennec.m2m.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Contextual Property</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An intermediate or contextual property attached to a class at transformation time (QVT v1.3 Section 8.2.2.16).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty#getInitExpression <em>Init Expression</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty#getContext <em>Context</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty#getOverridden <em>Overridden</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getContextualProperty()
 * @model
 * @generated
 */
@ProviderType
public interface ContextualProperty extends EObject, EStructuralFeature {
	/**
	 * Returns the value of the '<em><b>Init Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Init Expression</em>' containment reference.
	 * @see #setInitExpression(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getContextualProperty_InitExpression()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getInitExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty#getInitExpression <em>Init Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Init Expression</em>' containment reference.
	 * @see #getInitExpression()
	 * @generated
	 */
	void setInitExpression(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Context</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' reference.
	 * @see #setContext(EClass)
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getContextualProperty_Context()
	 * @model required="true"
	 * @generated
	 */
	EClass getContext();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty#getContext <em>Context</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' reference.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(EClass value);

	/**
	 * Returns the value of the '<em><b>Overridden</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Overridden</em>' reference.
	 * @see #setOverridden(EStructuralFeature)
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#getContextualProperty_Overridden()
	 * @model
	 * @generated
	 */
	EStructuralFeature getOverridden();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty#getOverridden <em>Overridden</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Overridden</em>' reference.
	 * @see #getOverridden()
	 * @generated
	 */
	void setOverridden(EStructuralFeature value);

} // ContextualProperty
