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


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.fennec.emf.osgi.annotation.provide.EPackage;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * The <b>Package</b> for the model.
 * It contains accessors for the meta objects to represent
 * <ul>
 *   <li>each class,</li>
 *   <li>each feature of each class,</li>
 *   <li>each operation of each class,</li>
 *   <li>each enum,</li>
 *   <li>and each data type</li>
 * </ul>
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * Manifest of a compiled unit: what it was built from, what it was built against, and what it still needs. The nsURI is deliberately neutral — a stored unit survives moving this Ecore, but not a change of its nsURI.
 * <!-- end-model-doc -->
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledFactory
 * @model kind="package"
 *        annotation="Version value='1.0'"
 * @generated
 */
@ProviderType
@EPackage(uri = CompiledPackage.eNS_URI, fingerprint = "fp1:fc1a27fd9fa5e8154363f6f6be948e2038ef3b59c911b319079812195e3f703a", genModel = "/model/compiledunit.genmodel", genModelSourceLocations = {"model/compiledunit.genmodel","org.eclipse.fennec.m2x.unit/model/compiledunit.genmodel"}, ecore = "/model/compiledunit.ecore", ecoreSourceLocations = "/model/compiledunit.ecore")
public interface CompiledPackage extends org.eclipse.emf.ecore.EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "compiled";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/fennec/m2x/compiled/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "compiled";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CompiledPackage eINSTANCE = org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl <em>Unit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getCompiledUnit()
	 * @generated
	 */
	int COMPILED_UNIT = 0;

	/**
	 * The feature id for the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT__ID = 0;

	/**
	 * The feature id for the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT__NAMESPACE = 1;

	/**
	 * The feature id for the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT__VERSION = 2;

	/**
	 * The feature id for the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT__DESCRIPTION = 3;

	/**
	 * The feature id for the '<em><b>Manifest</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT__MANIFEST = 4;

	/**
	 * The feature id for the '<em><b>Unit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT__UNIT = 5;

	/**
	 * The feature id for the '<em><b>Satellite</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT__SATELLITE = 6;

	/**
	 * The feature id for the '<em><b>Embedded</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT__EMBEDDED = 7;

	/**
	 * The feature id for the '<em><b>Packages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT__PACKAGES = 8;

	/**
	 * The number of structural features of the '<em>Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_FEATURE_COUNT = 9;

	/**
	 * The number of operations of the '<em>Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl <em>Unit Manifest</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getCompiledUnitManifest()
	 * @generated
	 */
	int COMPILED_UNIT_MANIFEST = 1;

	/**
	 * The feature id for the '<em><b>Format Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__FORMAT_VERSION = 0;

	/**
	 * The feature id for the '<em><b>Produced By</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__PRODUCED_BY = 1;

	/**
	 * The feature id for the '<em><b>Language</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__LANGUAGE = 2;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__QUALIFIED_NAME = 3;

	/**
	 * The feature id for the '<em><b>Unit Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__UNIT_FINGERPRINT = 4;

	/**
	 * The feature id for the '<em><b>Source Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__SOURCE_FINGERPRINT = 5;

	/**
	 * The feature id for the '<em><b>Dependency Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__DEPENDENCY_MODE = 6;

	/**
	 * The feature id for the '<em><b>Package Entry</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__PACKAGE_ENTRY = 7;

	/**
	 * The feature id for the '<em><b>Dependency Entry</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__DEPENDENCY_ENTRY = 8;

	/**
	 * The feature id for the '<em><b>Blackbox Requirement</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__BLACKBOX_REQUIREMENT = 9;

	/**
	 * The feature id for the '<em><b>Resolved Closure</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST__RESOLVED_CLOSURE = 10;

	/**
	 * The number of structural features of the '<em>Unit Manifest</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST_FEATURE_COUNT = 11;

	/**
	 * The number of operations of the '<em>Unit Manifest</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPILED_UNIT_MANIFEST_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.SourceUnitImpl <em>Source Unit</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.SourceUnitImpl
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getSourceUnit()
	 * @generated
	 */
	int SOURCE_UNIT = 2;

	/**
	 * The feature id for the '<em><b>Language</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_UNIT__LANGUAGE = 0;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_UNIT__QUALIFIED_NAME = 1;

	/**
	 * The feature id for the '<em><b>Uri</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_UNIT__URI = 2;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_UNIT__SOURCE = 3;

	/**
	 * The feature id for the '<em><b>Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_UNIT__FINGERPRINT = 4;

	/**
	 * The number of structural features of the '<em>Source Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_UNIT_FEATURE_COUNT = 5;

	/**
	 * The number of operations of the '<em>Source Unit</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SOURCE_UNIT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.PackageEntryImpl <em>Package Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.PackageEntryImpl
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getPackageEntry()
	 * @generated
	 */
	int PACKAGE_ENTRY = 3;

	/**
	 * The feature id for the '<em><b>Ns URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_ENTRY__NS_URI = 0;

	/**
	 * The feature id for the '<em><b>Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_ENTRY__FINGERPRINT = 1;

	/**
	 * The feature id for the '<em><b>Scheme</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_ENTRY__SCHEME = 2;

	/**
	 * The feature id for the '<em><b>Role</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_ENTRY__ROLE = 3;

	/**
	 * The number of structural features of the '<em>Package Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_ENTRY_FEATURE_COUNT = 4;

	/**
	 * The number of operations of the '<em>Package Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PACKAGE_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.DependencyEntryImpl <em>Dependency Entry</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.DependencyEntryImpl
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getDependencyEntry()
	 * @generated
	 */
	int DEPENDENCY_ENTRY = 4;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_ENTRY__QUALIFIED_NAME = 0;

	/**
	 * The feature id for the '<em><b>Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_ENTRY__MODE = 1;

	/**
	 * The feature id for the '<em><b>Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_ENTRY__FINGERPRINT = 2;

	/**
	 * The number of structural features of the '<em>Dependency Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_ENTRY_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Dependency Entry</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int DEPENDENCY_ENTRY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.BlackboxRequirementImpl <em>Blackbox Requirement</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.BlackboxRequirementImpl
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getBlackboxRequirement()
	 * @generated
	 */
	int BLACKBOX_REQUIREMENT = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLACKBOX_REQUIREMENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Signature Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLACKBOX_REQUIREMENT__SIGNATURE_FINGERPRINT = 1;

	/**
	 * The feature id for the '<em><b>Provider</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLACKBOX_REQUIREMENT__PROVIDER = 2;

	/**
	 * The number of structural features of the '<em>Blackbox Requirement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLACKBOX_REQUIREMENT_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Blackbox Requirement</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLACKBOX_REQUIREMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.ResolvedDependencyImpl <em>Resolved Dependency</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.ResolvedDependencyImpl
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getResolvedDependency()
	 * @generated
	 */
	int RESOLVED_DEPENDENCY = 6;

	/**
	 * The feature id for the '<em><b>Qualified Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOLVED_DEPENDENCY__QUALIFIED_NAME = 0;

	/**
	 * The feature id for the '<em><b>Fingerprint</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOLVED_DEPENDENCY__FINGERPRINT = 1;

	/**
	 * The feature id for the '<em><b>Source</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOLVED_DEPENDENCY__SOURCE = 2;

	/**
	 * The number of structural features of the '<em>Resolved Dependency</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOLVED_DEPENDENCY_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Resolved Dependency</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RESOLVED_DEPENDENCY_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.compiled.DependencyMode <em>Dependency Mode</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyMode
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getDependencyMode()
	 * @generated
	 */
	int DEPENDENCY_MODE = 7;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.compiled.PackageRole <em>Package Role</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.compiled.PackageRole
	 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getPackageRole()
	 * @generated
	 */
	int PACKAGE_ROLE = 8;


	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unit</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit
	 * @generated
	 */
	EClass getCompiledUnit();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getId <em>Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Id</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getId()
	 * @see #getCompiledUnit()
	 * @generated
	 */
	EAttribute getCompiledUnit_Id();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getNamespace <em>Namespace</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Namespace</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getNamespace()
	 * @see #getCompiledUnit()
	 * @generated
	 */
	EAttribute getCompiledUnit_Namespace();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getVersion <em>Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Version</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getVersion()
	 * @see #getCompiledUnit()
	 * @generated
	 */
	EAttribute getCompiledUnit_Version();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getDescription <em>Description</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Description</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getDescription()
	 * @see #getCompiledUnit()
	 * @generated
	 */
	EAttribute getCompiledUnit_Description();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getManifest <em>Manifest</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Manifest</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getManifest()
	 * @see #getCompiledUnit()
	 * @generated
	 */
	EReference getCompiledUnit_Manifest();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getUnit <em>Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Unit</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getUnit()
	 * @see #getCompiledUnit()
	 * @generated
	 */
	EReference getCompiledUnit_Unit();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getSatellite <em>Satellite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Satellite</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getSatellite()
	 * @see #getCompiledUnit()
	 * @generated
	 */
	EReference getCompiledUnit_Satellite();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getEmbedded <em>Embedded</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Embedded</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getEmbedded()
	 * @see #getCompiledUnit()
	 * @generated
	 */
	EReference getCompiledUnit_Embedded();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getPackages <em>Packages</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Packages</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getPackages()
	 * @see #getCompiledUnit()
	 * @generated
	 */
	EReference getCompiledUnit_Packages();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest <em>Unit Manifest</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unit Manifest</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest
	 * @generated
	 */
	EClass getCompiledUnitManifest();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getFormatVersion <em>Format Version</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Format Version</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getFormatVersion()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EAttribute getCompiledUnitManifest_FormatVersion();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getProducedBy <em>Produced By</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Produced By</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getProducedBy()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EAttribute getCompiledUnitManifest_ProducedBy();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getLanguage <em>Language</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Language</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getLanguage()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EAttribute getCompiledUnitManifest_Language();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getQualifiedName <em>Qualified Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Qualified Name</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getQualifiedName()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EAttribute getCompiledUnitManifest_QualifiedName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getUnitFingerprint <em>Unit Fingerprint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unit Fingerprint</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getUnitFingerprint()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EAttribute getCompiledUnitManifest_UnitFingerprint();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getSourceFingerprint <em>Source Fingerprint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source Fingerprint</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getSourceFingerprint()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EAttribute getCompiledUnitManifest_SourceFingerprint();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getDependencyMode <em>Dependency Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Dependency Mode</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getDependencyMode()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EAttribute getCompiledUnitManifest_DependencyMode();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getPackageEntry <em>Package Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Package Entry</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getPackageEntry()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EReference getCompiledUnitManifest_PackageEntry();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getDependencyEntry <em>Dependency Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Dependency Entry</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getDependencyEntry()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EReference getCompiledUnitManifest_DependencyEntry();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getBlackboxRequirement <em>Blackbox Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Blackbox Requirement</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getBlackboxRequirement()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EReference getCompiledUnitManifest_BlackboxRequirement();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getResolvedClosure <em>Resolved Closure</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Resolved Closure</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest#getResolvedClosure()
	 * @see #getCompiledUnitManifest()
	 * @generated
	 */
	EReference getCompiledUnitManifest_ResolvedClosure();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit <em>Source Unit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Source Unit</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.SourceUnit
	 * @generated
	 */
	EClass getSourceUnit();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getLanguage <em>Language</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Language</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.SourceUnit#getLanguage()
	 * @see #getSourceUnit()
	 * @generated
	 */
	EAttribute getSourceUnit_Language();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getQualifiedName <em>Qualified Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Qualified Name</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.SourceUnit#getQualifiedName()
	 * @see #getSourceUnit()
	 * @generated
	 */
	EAttribute getSourceUnit_QualifiedName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getUri <em>Uri</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Uri</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.SourceUnit#getUri()
	 * @see #getSourceUnit()
	 * @generated
	 */
	EAttribute getSourceUnit_Uri();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.SourceUnit#getSource()
	 * @see #getSourceUnit()
	 * @generated
	 */
	EAttribute getSourceUnit_Source();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.SourceUnit#getFingerprint <em>Fingerprint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fingerprint</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.SourceUnit#getFingerprint()
	 * @see #getSourceUnit()
	 * @generated
	 */
	EAttribute getSourceUnit_Fingerprint();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry <em>Package Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Package Entry</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.PackageEntry
	 * @generated
	 */
	EClass getPackageEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getNsURI <em>Ns URI</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Ns URI</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.PackageEntry#getNsURI()
	 * @see #getPackageEntry()
	 * @generated
	 */
	EAttribute getPackageEntry_NsURI();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getFingerprint <em>Fingerprint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fingerprint</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.PackageEntry#getFingerprint()
	 * @see #getPackageEntry()
	 * @generated
	 */
	EAttribute getPackageEntry_Fingerprint();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getScheme <em>Scheme</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Scheme</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.PackageEntry#getScheme()
	 * @see #getPackageEntry()
	 * @generated
	 */
	EAttribute getPackageEntry_Scheme();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.PackageEntry#getRole <em>Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Role</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.PackageEntry#getRole()
	 * @see #getPackageEntry()
	 * @generated
	 */
	EAttribute getPackageEntry_Role();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry <em>Dependency Entry</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Dependency Entry</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyEntry
	 * @generated
	 */
	EClass getDependencyEntry();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getQualifiedName <em>Qualified Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Qualified Name</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getQualifiedName()
	 * @see #getDependencyEntry()
	 * @generated
	 */
	EAttribute getDependencyEntry_QualifiedName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getMode <em>Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Mode</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getMode()
	 * @see #getDependencyEntry()
	 * @generated
	 */
	EAttribute getDependencyEntry_Mode();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getFingerprint <em>Fingerprint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fingerprint</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyEntry#getFingerprint()
	 * @see #getDependencyEntry()
	 * @generated
	 */
	EAttribute getDependencyEntry_Fingerprint();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement <em>Blackbox Requirement</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Blackbox Requirement</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement
	 * @generated
	 */
	EClass getBlackboxRequirement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getName()
	 * @see #getBlackboxRequirement()
	 * @generated
	 */
	EAttribute getBlackboxRequirement_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getSignatureFingerprint <em>Signature Fingerprint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Signature Fingerprint</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getSignatureFingerprint()
	 * @see #getBlackboxRequirement()
	 * @generated
	 */
	EAttribute getBlackboxRequirement_SignatureFingerprint();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getProvider <em>Provider</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Provider</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement#getProvider()
	 * @see #getBlackboxRequirement()
	 * @generated
	 */
	EAttribute getBlackboxRequirement_Provider();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency <em>Resolved Dependency</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Resolved Dependency</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.ResolvedDependency
	 * @generated
	 */
	EClass getResolvedDependency();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getQualifiedName <em>Qualified Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Qualified Name</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getQualifiedName()
	 * @see #getResolvedDependency()
	 * @generated
	 */
	EAttribute getResolvedDependency_QualifiedName();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getFingerprint <em>Fingerprint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Fingerprint</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getFingerprint()
	 * @see #getResolvedDependency()
	 * @generated
	 */
	EAttribute getResolvedDependency_Fingerprint();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getSource <em>Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Source</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.ResolvedDependency#getSource()
	 * @see #getResolvedDependency()
	 * @generated
	 */
	EAttribute getResolvedDependency_Source();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.m2x.model.compiled.DependencyMode <em>Dependency Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Dependency Mode</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.DependencyMode
	 * @generated
	 */
	EEnum getDependencyMode();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.m2x.model.compiled.PackageRole <em>Package Role</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Package Role</em>'.
	 * @see org.eclipse.fennec.m2x.model.compiled.PackageRole
	 * @generated
	 */
	EEnum getPackageRole();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	CompiledFactory getCompiledFactory();

	/**
	 * <!-- begin-user-doc -->
	 * Defines literals for the meta objects that represent
	 * <ul>
	 *   <li>each class,</li>
	 *   <li>each feature of each class,</li>
	 *   <li>each operation of each class,</li>
	 *   <li>each enum,</li>
	 *   <li>and each data type</li>
	 * </ul>
	 * <!-- end-user-doc -->
	 * @generated
	 */
	interface Literals {
		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl <em>Unit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getCompiledUnit()
		 * @generated
		 */
		EClass COMPILED_UNIT = eINSTANCE.getCompiledUnit();

		/**
		 * The meta object literal for the '<em><b>Id</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT__ID = eINSTANCE.getCompiledUnit_Id();

		/**
		 * The meta object literal for the '<em><b>Namespace</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT__NAMESPACE = eINSTANCE.getCompiledUnit_Namespace();

		/**
		 * The meta object literal for the '<em><b>Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT__VERSION = eINSTANCE.getCompiledUnit_Version();

		/**
		 * The meta object literal for the '<em><b>Description</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT__DESCRIPTION = eINSTANCE.getCompiledUnit_Description();

		/**
		 * The meta object literal for the '<em><b>Manifest</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPILED_UNIT__MANIFEST = eINSTANCE.getCompiledUnit_Manifest();

		/**
		 * The meta object literal for the '<em><b>Unit</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPILED_UNIT__UNIT = eINSTANCE.getCompiledUnit_Unit();

		/**
		 * The meta object literal for the '<em><b>Satellite</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPILED_UNIT__SATELLITE = eINSTANCE.getCompiledUnit_Satellite();

		/**
		 * The meta object literal for the '<em><b>Embedded</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPILED_UNIT__EMBEDDED = eINSTANCE.getCompiledUnit_Embedded();

		/**
		 * The meta object literal for the '<em><b>Packages</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPILED_UNIT__PACKAGES = eINSTANCE.getCompiledUnit_Packages();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl <em>Unit Manifest</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getCompiledUnitManifest()
		 * @generated
		 */
		EClass COMPILED_UNIT_MANIFEST = eINSTANCE.getCompiledUnitManifest();

		/**
		 * The meta object literal for the '<em><b>Format Version</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT_MANIFEST__FORMAT_VERSION = eINSTANCE.getCompiledUnitManifest_FormatVersion();

		/**
		 * The meta object literal for the '<em><b>Produced By</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT_MANIFEST__PRODUCED_BY = eINSTANCE.getCompiledUnitManifest_ProducedBy();

		/**
		 * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT_MANIFEST__LANGUAGE = eINSTANCE.getCompiledUnitManifest_Language();

		/**
		 * The meta object literal for the '<em><b>Qualified Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT_MANIFEST__QUALIFIED_NAME = eINSTANCE.getCompiledUnitManifest_QualifiedName();

		/**
		 * The meta object literal for the '<em><b>Unit Fingerprint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT_MANIFEST__UNIT_FINGERPRINT = eINSTANCE.getCompiledUnitManifest_UnitFingerprint();

		/**
		 * The meta object literal for the '<em><b>Source Fingerprint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT_MANIFEST__SOURCE_FINGERPRINT = eINSTANCE.getCompiledUnitManifest_SourceFingerprint();

		/**
		 * The meta object literal for the '<em><b>Dependency Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COMPILED_UNIT_MANIFEST__DEPENDENCY_MODE = eINSTANCE.getCompiledUnitManifest_DependencyMode();

		/**
		 * The meta object literal for the '<em><b>Package Entry</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPILED_UNIT_MANIFEST__PACKAGE_ENTRY = eINSTANCE.getCompiledUnitManifest_PackageEntry();

		/**
		 * The meta object literal for the '<em><b>Dependency Entry</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPILED_UNIT_MANIFEST__DEPENDENCY_ENTRY = eINSTANCE.getCompiledUnitManifest_DependencyEntry();

		/**
		 * The meta object literal for the '<em><b>Blackbox Requirement</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPILED_UNIT_MANIFEST__BLACKBOX_REQUIREMENT = eINSTANCE.getCompiledUnitManifest_BlackboxRequirement();

		/**
		 * The meta object literal for the '<em><b>Resolved Closure</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPILED_UNIT_MANIFEST__RESOLVED_CLOSURE = eINSTANCE.getCompiledUnitManifest_ResolvedClosure();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.SourceUnitImpl <em>Source Unit</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.SourceUnitImpl
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getSourceUnit()
		 * @generated
		 */
		EClass SOURCE_UNIT = eINSTANCE.getSourceUnit();

		/**
		 * The meta object literal for the '<em><b>Language</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOURCE_UNIT__LANGUAGE = eINSTANCE.getSourceUnit_Language();

		/**
		 * The meta object literal for the '<em><b>Qualified Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOURCE_UNIT__QUALIFIED_NAME = eINSTANCE.getSourceUnit_QualifiedName();

		/**
		 * The meta object literal for the '<em><b>Uri</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOURCE_UNIT__URI = eINSTANCE.getSourceUnit_Uri();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOURCE_UNIT__SOURCE = eINSTANCE.getSourceUnit_Source();

		/**
		 * The meta object literal for the '<em><b>Fingerprint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute SOURCE_UNIT__FINGERPRINT = eINSTANCE.getSourceUnit_Fingerprint();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.PackageEntryImpl <em>Package Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.PackageEntryImpl
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getPackageEntry()
		 * @generated
		 */
		EClass PACKAGE_ENTRY = eINSTANCE.getPackageEntry();

		/**
		 * The meta object literal for the '<em><b>Ns URI</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PACKAGE_ENTRY__NS_URI = eINSTANCE.getPackageEntry_NsURI();

		/**
		 * The meta object literal for the '<em><b>Fingerprint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PACKAGE_ENTRY__FINGERPRINT = eINSTANCE.getPackageEntry_Fingerprint();

		/**
		 * The meta object literal for the '<em><b>Scheme</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PACKAGE_ENTRY__SCHEME = eINSTANCE.getPackageEntry_Scheme();

		/**
		 * The meta object literal for the '<em><b>Role</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PACKAGE_ENTRY__ROLE = eINSTANCE.getPackageEntry_Role();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.DependencyEntryImpl <em>Dependency Entry</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.DependencyEntryImpl
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getDependencyEntry()
		 * @generated
		 */
		EClass DEPENDENCY_ENTRY = eINSTANCE.getDependencyEntry();

		/**
		 * The meta object literal for the '<em><b>Qualified Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEPENDENCY_ENTRY__QUALIFIED_NAME = eINSTANCE.getDependencyEntry_QualifiedName();

		/**
		 * The meta object literal for the '<em><b>Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEPENDENCY_ENTRY__MODE = eINSTANCE.getDependencyEntry_Mode();

		/**
		 * The meta object literal for the '<em><b>Fingerprint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute DEPENDENCY_ENTRY__FINGERPRINT = eINSTANCE.getDependencyEntry_Fingerprint();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.BlackboxRequirementImpl <em>Blackbox Requirement</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.BlackboxRequirementImpl
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getBlackboxRequirement()
		 * @generated
		 */
		EClass BLACKBOX_REQUIREMENT = eINSTANCE.getBlackboxRequirement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BLACKBOX_REQUIREMENT__NAME = eINSTANCE.getBlackboxRequirement_Name();

		/**
		 * The meta object literal for the '<em><b>Signature Fingerprint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BLACKBOX_REQUIREMENT__SIGNATURE_FINGERPRINT = eINSTANCE.getBlackboxRequirement_SignatureFingerprint();

		/**
		 * The meta object literal for the '<em><b>Provider</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BLACKBOX_REQUIREMENT__PROVIDER = eINSTANCE.getBlackboxRequirement_Provider();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.compiled.impl.ResolvedDependencyImpl <em>Resolved Dependency</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.ResolvedDependencyImpl
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getResolvedDependency()
		 * @generated
		 */
		EClass RESOLVED_DEPENDENCY = eINSTANCE.getResolvedDependency();

		/**
		 * The meta object literal for the '<em><b>Qualified Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOLVED_DEPENDENCY__QUALIFIED_NAME = eINSTANCE.getResolvedDependency_QualifiedName();

		/**
		 * The meta object literal for the '<em><b>Fingerprint</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOLVED_DEPENDENCY__FINGERPRINT = eINSTANCE.getResolvedDependency_Fingerprint();

		/**
		 * The meta object literal for the '<em><b>Source</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute RESOLVED_DEPENDENCY__SOURCE = eINSTANCE.getResolvedDependency_Source();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.compiled.DependencyMode <em>Dependency Mode</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.compiled.DependencyMode
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getDependencyMode()
		 * @generated
		 */
		EEnum DEPENDENCY_MODE = eINSTANCE.getDependencyMode();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.compiled.PackageRole <em>Package Role</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.compiled.PackageRole
		 * @see org.eclipse.fennec.m2x.model.compiled.impl.CompiledPackageImpl#getPackageRole()
		 * @generated
		 */
		EEnum PACKAGE_ROLE = eINSTANCE.getPackageRole();

	}

} //CompiledPackage
