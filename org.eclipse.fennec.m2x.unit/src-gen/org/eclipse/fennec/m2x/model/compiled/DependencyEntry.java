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
package org.eclipse.fennec.m2x.model.compiled;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Dependency Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * One unit this unit depends on. The mode is per dependency, because a single compile may embed one library and pin another.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getMode <em>Mode</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getFingerprint <em>Fingerprint</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getDependencyEntry()
 * @model
 * @generated
 */
@ProviderType
public interface DependencyEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Qualified name of the required unit.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Qualified Name</em>' attribute.
	 * @see #setQualifiedName(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getDependencyEntry_QualifiedName()
	 * @model
	 * @generated
	 */
	String getQualifiedName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getQualifiedName <em>Qualified Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Qualified Name</em>' attribute.
	 * @see #getQualifiedName()
	 * @generated
	 */
	void setQualifiedName(String value);

	/**
	 * Returns the value of the '<em><b>Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.compiled.DependencyMode}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * How this dependency was bound.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Mode</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyMode
	 * @see #setMode(DependencyMode)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getDependencyEntry_Mode()
	 * @model
	 * @generated
	 */
	DependencyMode getMode();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getMode <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mode</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyMode
	 * @see #getMode()
	 * @generated
	 */
	void setMode(DependencyMode value);

	/**
	 * Returns the value of the '<em><b>Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The dependency's unit fingerprint under embed and pin. Empty under rebind, where the binding is decided at prepare time and recorded in resolvedClosure.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fingerprint</em>' attribute.
	 * @see #setFingerprint(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getDependencyEntry_Fingerprint()
	 * @model
	 * @generated
	 */
	String getFingerprint();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getFingerprint <em>Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fingerprint</em>' attribute.
	 * @see #getFingerprint()
	 * @generated
	 */
	void setFingerprint(String value);

} // DependencyEntry
