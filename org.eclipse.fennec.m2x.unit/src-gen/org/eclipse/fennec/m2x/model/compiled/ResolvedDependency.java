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
 * A representation of the model object '<em><b>Resolved Dependency</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * One entry of the closure record prepare writes under rebind: which unit was bound, and in which version.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getFingerprint <em>Fingerprint</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getSource <em>Source</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getResolvedDependency()
 * @model
 * @generated
 */
@ProviderType
public interface ResolvedDependency extends EObject {
	/**
	 * Returns the value of the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Qualified name of the bound unit.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Qualified Name</em>' attribute.
	 * @see #setQualifiedName(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getResolvedDependency_QualifiedName()
	 * @model
	 * @generated
	 */
	String getQualifiedName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getQualifiedName <em>Qualified Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Qualified Name</em>' attribute.
	 * @see #getQualifiedName()
	 * @generated
	 */
	void setQualifiedName(String value);

	/**
	 * Returns the value of the '<em><b>Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The unit fingerprint prepare actually bound.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fingerprint</em>' attribute.
	 * @see #setFingerprint(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getResolvedDependency_Fingerprint()
	 * @model
	 * @generated
	 */
	String getFingerprint();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getFingerprint <em>Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fingerprint</em>' attribute.
	 * @see #getFingerprint()
	 * @generated
	 */
	void setFingerprint(String value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Which resolver or store the unit came from, so that two sources disagreeing about the same name can be told apart afterwards.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Source</em>' attribute.
	 * @see #setSource(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getResolvedDependency_Source()
	 * @model
	 * @generated
	 */
	String getSource();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getSource <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' attribute.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(String value);

} // ResolvedDependency
