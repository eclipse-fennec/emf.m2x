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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unit Manifest</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * How the unit was built and what it was built against: everything needed to decide whether a stored unit is still valid, and to say precisely what moved when it is not. Identity, namespace and version live on the CompiledUnit that owns this manifest. The overall unitFingerprint answers the question with one comparison; the entries below are what turns a mismatch into a diagnostic that names the culprit instead of declaring the unit outdated.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getFormatVersion <em>Format Version</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getProducedBy <em>Produced By</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getUnitFingerprint <em>Unit Fingerprint</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getSourceFingerprint <em>Source Fingerprint</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getDependencyMode <em>Dependency Mode</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getNature <em>Nature</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getPackageEntry <em>Package Entry</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getDependencyEntry <em>Dependency Entry</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getBlackboxRequirement <em>Blackbox Requirement</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getResolvedClosure <em>Resolved Closure</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest()
 * @model
 * @generated
 */
@ProviderType
public interface CompiledUnitManifest extends EObject {
	/**
	 * Returns the value of the '<em><b>Format Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Version of the manifest format itself, so a reader can tell what it is looking at before it interprets anything.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Format Version</em>' attribute.
	 * @see #setFormatVersion(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_FormatVersion()
	 * @model
	 * @generated
	 */
	String getFormatVersion();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getFormatVersion <em>Format Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Format Version</em>' attribute.
	 * @see #getFormatVersion()
	 * @generated
	 */
	void setFormatVersion(String value);

	/**
	 * Returns the value of the '<em><b>Produced By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Which engine version produced the unit.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Produced By</em>' attribute.
	 * @see #setProducedBy(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_ProducedBy()
	 * @model
	 * @generated
	 */
	String getProducedBy();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getProducedBy <em>Produced By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Produced By</em>' attribute.
	 * @see #getProducedBy()
	 * @generated
	 */
	void setProducedBy(String value);

	/**
	 * Returns the value of the '<em><b>Language</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Language tag of the unit: qvto, qvtr or m2t. Together with qualifiedName and the kind it forms the store key.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Language</em>' attribute.
	 * @see #setLanguage(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_Language()
	 * @model
	 * @generated
	 */
	String getLanguage();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getLanguage <em>Language</em>}' attribute.
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
	 * Qualified name the unit is imported by — the name a resolver is asked for. Distinct from CompiledUnit.id, which stays unique across versions of the same name.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Qualified Name</em>' attribute.
	 * @see #setQualifiedName(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_QualifiedName()
	 * @model
	 * @generated
	 */
	String getQualifiedName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getQualifiedName <em>Qualified Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Qualified Name</em>' attribute.
	 * @see #getQualifiedName()
	 * @generated
	 */
	void setQualifiedName(String value);

	/**
	 * Returns the value of the '<em><b>Unit Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Fingerprint of the unit in the form <scheme>:<digest>, computed Merkle-style over the unit and the fingerprints of its dependencies. Under rebind it describes the unit alone, which is why resolvedClosure exists.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Unit Fingerprint</em>' attribute.
	 * @see #setUnitFingerprint(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_UnitFingerprint()
	 * @model
	 * @generated
	 */
	String getUnitFingerprint();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getUnitFingerprint <em>Unit Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit Fingerprint</em>' attribute.
	 * @see #getUnitFingerprint()
	 * @generated
	 */
	void setUnitFingerprint(String value);

	/**
	 * Returns the value of the '<em><b>Source Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Fingerprint of the source text this unit was compiled from, in the form <scheme>:<digest>. A store that holds the source as well can tell from this alone whether the compiled unit is behind it.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Source Fingerprint</em>' attribute.
	 * @see #setSourceFingerprint(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_SourceFingerprint()
	 * @model
	 * @generated
	 */
	String getSourceFingerprint();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getSourceFingerprint <em>Source Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Source Fingerprint</em>' attribute.
	 * @see #getSourceFingerprint()
	 * @generated
	 */
	void setSourceFingerprint(String value);

	/**
	 * Returns the value of the '<em><b>Dependency Mode</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.compiled.DependencyMode}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * How dependencies were bound when this unit was compiled. It has to be readable on load, otherwise prepare cannot behave correctly.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Dependency Mode</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyMode
	 * @see #setDependencyMode(DependencyMode)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_DependencyMode()
	 * @model
	 * @generated
	 */
	DependencyMode getDependencyMode();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getDependencyMode <em>Dependency Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Dependency Mode</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyMode
	 * @see #getDependencyMode()
	 * @generated
	 */
	void setDependencyMode(DependencyMode value);

	/**
	 * Returns the value of the '<em><b>Nature</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.compiled.UnitNature}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether this unit is a startable transformation or a library others import (#224). Derived by the compiling language at package time — for QVT-O a standalone library source compiles into a library, everything else is a transformation — so a holder of the document decides without loading the AST.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Nature</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.compiled.UnitNature
	 * @see #setNature(UnitNature)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_Nature()
	 * @model
	 * @generated
	 */
	UnitNature getNature();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getNature <em>Nature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Nature</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.compiled.UnitNature
	 * @see #getNature()
	 * @generated
	 */
	void setNature(UnitNature value);

	/**
	 * Returns the value of the '<em><b>Package Entry</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.compiled.PackageEntry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The EPackages the unit was compiled against, with the fingerprints prepare verifies them by.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Package Entry</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_PackageEntry()
	 * @model containment="true"
	 * @generated
	 */
	EList<PackageEntry> getPackageEntry();

	/**
	 * Returns the value of the '<em><b>Dependency Entry</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The units this one depends on, each with the mode it was bound in.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Dependency Entry</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_DependencyEntry()
	 * @model containment="true"
	 * @generated
	 */
	EList<DependencyEntry> getDependencyEntry();

	/**
	 * Returns the value of the '<em><b>Blackbox Requirement</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Blackbox declarations the unit needs. The implementations are not part of a compiled unit — only their declarations are, so a missing one surfaces at load time with a clear statement instead of mid-run.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Blackbox Requirement</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_BlackboxRequirement()
	 * @model containment="true"
	 * @generated
	 */
	EList<BlackboxRequirement> getBlackboxRequirement();

	/**
	 * Returns the value of the '<em><b>Resolved Closure</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * What prepare actually bound. Recorded under rebind, where the unit fingerprint says nothing about the dependencies: without this record nobody can reconstruct what a given run computed.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Resolved Closure</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnitManifest_ResolvedClosure()
	 * @model containment="true"
	 * @generated
	 */
	EList<ResolvedDependency> getResolvedClosure();

} // CompiledUnitManifest
