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

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module Element</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract base class for all elements owned by a Module: templates, queries, and macros (MOFM2T 1.0 Section 8.1). Does not extend ENamedElement to avoid raw-type EList issues when combined with Block (OclExpression hierarchy).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getVisibility <em>Visibility</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getModule <em>Module</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getModuleElement()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface ModuleElement extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getModuleElement_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Visibility</b></em>' attribute.
	 * The default value is <code>"public"</code>.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.m2t.VisibilityKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Visibility</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.m2t.VisibilityKind
	 * @see #setVisibility(VisibilityKind)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getModuleElement_Visibility()
	 * @model default="public"
	 * @generated
	 */
	VisibilityKind getVisibility();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getVisibility <em>Visibility</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Visibility</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.m2t.VisibilityKind
	 * @see #getVisibility()
	 * @generated
	 */
	void setVisibility(VisibilityKind value);

	/**
	 * Returns the value of the '<em><b>Module</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.m2t.Module#getOwnedModuleElement <em>Owned Module Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Module</em>' container reference.
	 * @see #setModule(org.eclipse.fennec.m2x.model.m2t.Module)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getModuleElement_Module()
	 * @see org.eclipse.fennec.m2x.model.m2t.Module#getOwnedModuleElement
	 * @model opposite="ownedModuleElement" transient="false"
	 * @generated
	 */
	org.eclipse.fennec.m2x.model.m2t.Module getModule();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getModule <em>Module</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Module</em>' container reference.
	 * @see #getModule()
	 * @generated
	 */
	void setModule(org.eclipse.fennec.m2x.model.m2t.Module value);

} // ModuleElement
