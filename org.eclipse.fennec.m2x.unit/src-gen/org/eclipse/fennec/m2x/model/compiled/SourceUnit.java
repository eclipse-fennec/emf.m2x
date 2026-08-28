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
 * A representation of the model object '<em><b>Source Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A unit's source text as a store entry. A store holds sources and compiled units side by side and tells them apart by kind; a content-addressed backend can only hold EObjects, so the text travels in this wrapper. Its fingerprint is the source fingerprint of the m2x unit mechanism, computed over the normalized text — the same value a CompiledUnitManifest records as sourceFingerprint.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getUri <em>Uri</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getSource <em>Source</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getFingerprint <em>Fingerprint</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getSourceUnit()
 * @model
 * @generated
 */
@ProviderType
public interface SourceUnit extends EObject {
	/**
	 * Returns the value of the '<em><b>Language</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Language tag of the unit: qvto, qvtr or m2t.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Language</em>' attribute.
	 * @see #setLanguage(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getSourceUnit_Language()
	 * @model
	 * @generated
	 */
	String getLanguage();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getLanguage <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Language</em>' attribute.
	 * @see #getLanguage()
	 * @generated
	 */
	void setLanguage(String value);

	/**
	 * Returns the value of the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Qualified name the unit is imported by.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Qualified Name</em>' attribute.
	 * @see #setQualifiedName(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getSourceUnit_QualifiedName()
	 * @model required="true"
	 * @generated
	 */
	String getQualifiedName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getQualifiedName <em>Qualified Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Qualified Name</em>' attribute.
	 * @see #getQualifiedName()
	 * @generated
	 */
	void setQualifiedName(String value);

	/**
	 * Returns the value of the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Where the source was read from, informational.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Uri</em>' attribute.
	 * @see #setUri(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getSourceUnit_Uri()
	 * @model
	 * @generated
	 */
	String getUri();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getUri <em>Uri</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uri</em>' attribute.
	 * @see #getUri()
	 * @generated
	 */
	void setUri(String value);

	/**
	 * Returns the value of the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The source text.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Source</em>' attribute.
	 * @see #setSource(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getSourceUnit_Source()
	 * @model required="true"
	 * @generated
	 */
	String getSource();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getSource <em>Source</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source</em>' attribute.
	 * @see #getSource()
	 * @generated
	 */
	void setSource(String value);

	/**
	 * Returns the value of the '<em><b>Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Fingerprint of the source text in the form <scheme>:<digest>.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fingerprint</em>' attribute.
	 * @see #setFingerprint(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getSourceUnit_Fingerprint()
	 * @model
	 * @generated
	 */
	String getFingerprint();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getFingerprint <em>Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fingerprint</em>' attribute.
	 * @see #getFingerprint()
	 * @generated
	 */
	void setFingerprint(String value);

} // SourceUnit
