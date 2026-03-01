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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A MOFM2T module: container for templates, queries, and macros (MOFM2T 1.0 Section 8.1). Extends EPackage to serve as namespace. The module's input models are declared as TypedModels (metamodel references).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Module#getOwnedModuleElement <em>Owned Module Element</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Module#getInput <em>Input</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Module#getExtends <em>Extends</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Module#getImports <em>Imports</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getModule()
 * @model
 * @generated
 */
@ProviderType
public interface Module extends EObject, EPackage {
	/**
	 * Returns the value of the '<em><b>Owned Module Element</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.m2t.ModuleElement}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getModule <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Owned Module Element</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getModule_OwnedModuleElement()
	 * @see org.eclipse.fennec.m2x.model.m2t.ModuleElement#getModule
	 * @model opposite="module" containment="true"
	 * @generated
	 */
	EList<ModuleElement> getOwnedModuleElement();

	/**
	 * Returns the value of the '<em><b>Input</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EPackage}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Input metamodel packages (typed models) for this module.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Input</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getModule_Input()
	 * @model
	 * @generated
	 */
	EList<EPackage> getInput();

	/**
	 * Returns the value of the '<em><b>Extends</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.m2t.Module}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Modules extended by this module (template inheritance).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Extends</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getModule_Extends()
	 * @model
	 * @generated
	 */
	EList<Module> getExtends();

	/**
	 * Returns the value of the '<em><b>Imports</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.m2t.Module}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Modules imported by this module (access to public elements).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Imports</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getModule_Imports()
	 * @model
	 * @generated
	 */
	EList<Module> getImports();

} // Module
