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
 * A representation of the model object '<em><b>Package Entry</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * One EPackage the unit was compiled against. Prepare looks the nsURI up in the runtime registry and compares fingerprints: on equality the runtime instance is adopted, so generated code stays in play; a matching nsURI with a differing fingerprint is a hard failure, naming both values.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getNsURI <em>Ns URI</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getFingerprint <em>Fingerprint</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getScheme <em>Scheme</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getRole <em>Role</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getPackageEntry()
 * @model
 * @generated
 */
@ProviderType
public interface PackageEntry extends EObject {
	/**
	 * Returns the value of the '<em><b>Ns URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The package's namespace URI — the identity prepare looks up.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Ns URI</em>' attribute.
	 * @see #setNsURI(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getPackageEntry_NsURI()
	 * @model
	 * @generated
	 */
	String getNsURI();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getNsURI <em>Ns URI</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ns URI</em>' attribute.
	 * @see #getNsURI()
	 * @generated
	 */
	void setNsURI(String value);

	/**
	 * Returns the value of the '<em><b>Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The package fingerprint in the form <scheme>:<digest>, computed by the FingerprintService of emf.osgi rather than by the m2x unit mechanism.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Fingerprint</em>' attribute.
	 * @see #setFingerprint(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getPackageEntry_Fingerprint()
	 * @model
	 * @generated
	 */
	String getFingerprint();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getFingerprint <em>Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Fingerprint</em>' attribute.
	 * @see #getFingerprint()
	 * @generated
	 */
	void setFingerprint(String value);

	/**
	 * Returns the value of the '<em><b>Scheme</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The canonicalization scheme the fingerprint was computed in, e.g. fp1. Values of different schemes are not comparable, so it has to travel with the value.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Scheme</em>' attribute.
	 * @see #setScheme(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getPackageEntry_Scheme()
	 * @model
	 * @generated
	 */
	String getScheme();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getScheme <em>Scheme</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Scheme</em>' attribute.
	 * @see #getScheme()
	 * @generated
	 */
	void setScheme(String value);

	/**
	 * Returns the value of the '<em><b>Role</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.compiled.PackageRole}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether the unit only refers to this package or carries a copy of it.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Role</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.compiled.PackageRole
	 * @see #setRole(PackageRole)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getPackageEntry_Role()
	 * @model
	 * @generated
	 */
	PackageRole getRole();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getRole <em>Role</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Role</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.compiled.PackageRole
	 * @see #getRole()
	 * @generated
	 */
	void setRole(PackageRole value);

} // PackageEntry
