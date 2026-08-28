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
 * A representation of the model object '<em><b>Blackbox Requirement</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A blackbox operation the unit calls. Carried as a declaration so that load time can state which implementation is missing.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getSignatureFingerprint <em>Signature Fingerprint</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getProvider <em>Provider</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getBlackboxRequirement()
 * @model
 * @generated
 */
@ProviderType
public interface BlackboxRequirement extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Qualified name of the required blackbox library or operation.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getBlackboxRequirement_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Signature Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Fingerprint of the expected signature, so that a provider with the right name but the wrong shape is caught at prepare time rather than at the call.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Signature Fingerprint</em>' attribute.
	 * @see #setSignatureFingerprint(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getBlackboxRequirement_SignatureFingerprint()
	 * @model
	 * @generated
	 */
	String getSignatureFingerprint();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getSignatureFingerprint <em>Signature Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Signature Fingerprint</em>' attribute.
	 * @see #getSignatureFingerprint()
	 * @generated
	 */
	void setSignatureFingerprint(String value);

	/**
	 * Returns the value of the '<em><b>Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The provider expected to supply the implementation, where the compile knew one.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Provider</em>' attribute.
	 * @see #setProvider(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getBlackboxRequirement_Provider()
	 * @model
	 * @generated
	 */
	String getProvider();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getProvider <em>Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Provider</em>' attribute.
	 * @see #getProvider()
	 * @generated
	 */
	void setProvider(String value);

} // BlackboxRequirement
