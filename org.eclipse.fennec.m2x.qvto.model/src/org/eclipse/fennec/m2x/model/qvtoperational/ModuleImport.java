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

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Module Import</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An import declaration referencing another module with extension or access semantics (QVT v1.3 Section 8.2.2.15).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport#getImportedModule <em>Imported Module</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport#getModule <em>Module</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport#getBinding <em>Binding</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getModuleImport()
 * @model
 * @generated
 */
@ProviderType
public interface ModuleImport extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.qvtoperational.ImportKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.ImportKind
	 * @see #setKind(ImportKind)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getModuleImport_Kind()
	 * @model
	 * @generated
	 */
	ImportKind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.ImportKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(ImportKind value);

	/**
	 * Returns the value of the '<em><b>Imported Module</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Imported Module</em>' reference.
	 * @see #setImportedModule(org.eclipse.fennec.m2x.model.qvtoperational.Module)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getModuleImport_ImportedModule()
	 * @model required="true"
	 * @generated
	 */
	org.eclipse.fennec.m2x.model.qvtoperational.Module getImportedModule();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport#getImportedModule <em>Imported Module</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Imported Module</em>' reference.
	 * @see #getImportedModule()
	 * @generated
	 */
	void setImportedModule(org.eclipse.fennec.m2x.model.qvtoperational.Module value);

	/**
	 * Returns the value of the '<em><b>Module</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtoperational.Module#getModuleImport <em>Module Import</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Module</em>' container reference.
	 * @see #setModule(org.eclipse.fennec.m2x.model.qvtoperational.Module)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getModuleImport_Module()
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.Module#getModuleImport
	 * @model opposite="moduleImport"
	 * @generated
	 */
	org.eclipse.fennec.m2x.model.qvtoperational.Module getModule();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport#getModule <em>Module</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Module</em>' container reference.
	 * @see #getModule()
	 * @generated
	 */
	void setModule(org.eclipse.fennec.m2x.model.qvtoperational.Module value);

	/**
	 * Returns the value of the '<em><b>Binding</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtoperational.ModelType}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Binding</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getModuleImport_Binding()
	 * @model
	 * @generated
	 */
	EList<ModelType> getBinding();

} // ModuleImport
