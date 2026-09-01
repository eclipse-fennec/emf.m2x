/**
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
package org.eclipse.fennec.m2x.model.compiled.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.compiled.PackageEntry;
import org.eclipse.fennec.m2x.model.compiled.PackageRole;
import org.eclipse.fennec.m2x.model.compiled.ResolvedDependency;
import org.eclipse.fennec.m2x.model.compiled.SourceUnit;
import org.eclipse.fennec.m2x.model.compiled.UnitNature;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CompiledPackageImpl extends EPackageImpl implements CompiledPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass compiledUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass compiledUnitManifestEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass sourceUnitEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass packageEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass dependencyEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass blackboxRequirementEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resolvedDependencyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum dependencyModeEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum packageRoleEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum unitNatureEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private CompiledPackageImpl() {
		super(eNS_URI, CompiledFactory.eINSTANCE);
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link CompiledPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static CompiledPackage init() {
		if (isInited) return (CompiledPackage)EPackage.Registry.INSTANCE.getEPackage(CompiledPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredCompiledPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		CompiledPackageImpl theCompiledPackage = registeredCompiledPackage instanceof CompiledPackageImpl ? (CompiledPackageImpl)registeredCompiledPackage : new CompiledPackageImpl();

		isInited = true;

		// Create package meta-data objects
		theCompiledPackage.createPackageContents();

		// Initialize created meta-data
		theCompiledPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theCompiledPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(CompiledPackage.eNS_URI, theCompiledPackage);
		return theCompiledPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCompiledUnit() {
		return compiledUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnit_Id() {
		return (EAttribute)compiledUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnit_Namespace() {
		return (EAttribute)compiledUnitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnit_Version() {
		return (EAttribute)compiledUnitEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnit_Description() {
		return (EAttribute)compiledUnitEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCompiledUnit_Manifest() {
		return (EReference)compiledUnitEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCompiledUnit_Unit() {
		return (EReference)compiledUnitEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCompiledUnit_Satellite() {
		return (EReference)compiledUnitEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCompiledUnit_Embedded() {
		return (EReference)compiledUnitEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCompiledUnit_Packages() {
		return (EReference)compiledUnitEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getCompiledUnitManifest() {
		return compiledUnitManifestEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnitManifest_FormatVersion() {
		return (EAttribute)compiledUnitManifestEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnitManifest_ProducedBy() {
		return (EAttribute)compiledUnitManifestEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnitManifest_Language() {
		return (EAttribute)compiledUnitManifestEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnitManifest_QualifiedName() {
		return (EAttribute)compiledUnitManifestEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnitManifest_UnitFingerprint() {
		return (EAttribute)compiledUnitManifestEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnitManifest_SourceFingerprint() {
		return (EAttribute)compiledUnitManifestEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnitManifest_DependencyMode() {
		return (EAttribute)compiledUnitManifestEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getCompiledUnitManifest_Nature() {
		return (EAttribute)compiledUnitManifestEClass.getEStructuralFeatures().get(7);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCompiledUnitManifest_PackageEntry() {
		return (EReference)compiledUnitManifestEClass.getEStructuralFeatures().get(8);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCompiledUnitManifest_DependencyEntry() {
		return (EReference)compiledUnitManifestEClass.getEStructuralFeatures().get(9);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCompiledUnitManifest_BlackboxRequirement() {
		return (EReference)compiledUnitManifestEClass.getEStructuralFeatures().get(10);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getCompiledUnitManifest_ResolvedClosure() {
		return (EReference)compiledUnitManifestEClass.getEStructuralFeatures().get(11);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getSourceUnit() {
		return sourceUnitEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSourceUnit_Language() {
		return (EAttribute)sourceUnitEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSourceUnit_QualifiedName() {
		return (EAttribute)sourceUnitEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSourceUnit_Uri() {
		return (EAttribute)sourceUnitEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSourceUnit_Source() {
		return (EAttribute)sourceUnitEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getSourceUnit_Fingerprint() {
		return (EAttribute)sourceUnitEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getPackageEntry() {
		return packageEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPackageEntry_NsURI() {
		return (EAttribute)packageEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPackageEntry_Fingerprint() {
		return (EAttribute)packageEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPackageEntry_Scheme() {
		return (EAttribute)packageEntryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getPackageEntry_Role() {
		return (EAttribute)packageEntryEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getDependencyEntry() {
		return dependencyEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDependencyEntry_QualifiedName() {
		return (EAttribute)dependencyEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDependencyEntry_Mode() {
		return (EAttribute)dependencyEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getDependencyEntry_Fingerprint() {
		return (EAttribute)dependencyEntryEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getBlackboxRequirement() {
		return blackboxRequirementEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBlackboxRequirement_Name() {
		return (EAttribute)blackboxRequirementEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBlackboxRequirement_SignatureFingerprint() {
		return (EAttribute)blackboxRequirementEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getBlackboxRequirement_Provider() {
		return (EAttribute)blackboxRequirementEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getResolvedDependency() {
		return resolvedDependencyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getResolvedDependency_QualifiedName() {
		return (EAttribute)resolvedDependencyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getResolvedDependency_Fingerprint() {
		return (EAttribute)resolvedDependencyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getResolvedDependency_Source() {
		return (EAttribute)resolvedDependencyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getDependencyMode() {
		return dependencyModeEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getPackageRole() {
		return packageRoleEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getUnitNature() {
		return unitNatureEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CompiledFactory getCompiledFactory() {
		return (CompiledFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		compiledUnitEClass = createEClass(COMPILED_UNIT);
		createEAttribute(compiledUnitEClass, COMPILED_UNIT__ID);
		createEAttribute(compiledUnitEClass, COMPILED_UNIT__NAMESPACE);
		createEAttribute(compiledUnitEClass, COMPILED_UNIT__VERSION);
		createEAttribute(compiledUnitEClass, COMPILED_UNIT__DESCRIPTION);
		createEReference(compiledUnitEClass, COMPILED_UNIT__MANIFEST);
		createEReference(compiledUnitEClass, COMPILED_UNIT__UNIT);
		createEReference(compiledUnitEClass, COMPILED_UNIT__SATELLITE);
		createEReference(compiledUnitEClass, COMPILED_UNIT__EMBEDDED);
		createEReference(compiledUnitEClass, COMPILED_UNIT__PACKAGES);

		compiledUnitManifestEClass = createEClass(COMPILED_UNIT_MANIFEST);
		createEAttribute(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__FORMAT_VERSION);
		createEAttribute(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__PRODUCED_BY);
		createEAttribute(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__LANGUAGE);
		createEAttribute(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__QUALIFIED_NAME);
		createEAttribute(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__UNIT_FINGERPRINT);
		createEAttribute(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__SOURCE_FINGERPRINT);
		createEAttribute(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__DEPENDENCY_MODE);
		createEAttribute(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__NATURE);
		createEReference(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__PACKAGE_ENTRY);
		createEReference(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__DEPENDENCY_ENTRY);
		createEReference(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__BLACKBOX_REQUIREMENT);
		createEReference(compiledUnitManifestEClass, COMPILED_UNIT_MANIFEST__RESOLVED_CLOSURE);

		sourceUnitEClass = createEClass(SOURCE_UNIT);
		createEAttribute(sourceUnitEClass, SOURCE_UNIT__LANGUAGE);
		createEAttribute(sourceUnitEClass, SOURCE_UNIT__QUALIFIED_NAME);
		createEAttribute(sourceUnitEClass, SOURCE_UNIT__URI);
		createEAttribute(sourceUnitEClass, SOURCE_UNIT__SOURCE);
		createEAttribute(sourceUnitEClass, SOURCE_UNIT__FINGERPRINT);

		packageEntryEClass = createEClass(PACKAGE_ENTRY);
		createEAttribute(packageEntryEClass, PACKAGE_ENTRY__NS_URI);
		createEAttribute(packageEntryEClass, PACKAGE_ENTRY__FINGERPRINT);
		createEAttribute(packageEntryEClass, PACKAGE_ENTRY__SCHEME);
		createEAttribute(packageEntryEClass, PACKAGE_ENTRY__ROLE);

		dependencyEntryEClass = createEClass(DEPENDENCY_ENTRY);
		createEAttribute(dependencyEntryEClass, DEPENDENCY_ENTRY__QUALIFIED_NAME);
		createEAttribute(dependencyEntryEClass, DEPENDENCY_ENTRY__MODE);
		createEAttribute(dependencyEntryEClass, DEPENDENCY_ENTRY__FINGERPRINT);

		blackboxRequirementEClass = createEClass(BLACKBOX_REQUIREMENT);
		createEAttribute(blackboxRequirementEClass, BLACKBOX_REQUIREMENT__NAME);
		createEAttribute(blackboxRequirementEClass, BLACKBOX_REQUIREMENT__SIGNATURE_FINGERPRINT);
		createEAttribute(blackboxRequirementEClass, BLACKBOX_REQUIREMENT__PROVIDER);

		resolvedDependencyEClass = createEClass(RESOLVED_DEPENDENCY);
		createEAttribute(resolvedDependencyEClass, RESOLVED_DEPENDENCY__QUALIFIED_NAME);
		createEAttribute(resolvedDependencyEClass, RESOLVED_DEPENDENCY__FINGERPRINT);
		createEAttribute(resolvedDependencyEClass, RESOLVED_DEPENDENCY__SOURCE);

		// Create enums
		dependencyModeEEnum = createEEnum(DEPENDENCY_MODE);
		packageRoleEEnum = createEEnum(PACKAGE_ROLE);
		unitNatureEEnum = createEEnum(UNIT_NATURE);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes

		// Initialize classes, features, and operations; add parameters
		initEClass(compiledUnitEClass, CompiledUnit.class, "CompiledUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCompiledUnit_Id(), ecorePackage.getEString(), "id", null, 1, 1, CompiledUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnit_Namespace(), ecorePackage.getEString(), "namespace", null, 0, 1, CompiledUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnit_Version(), ecorePackage.getEString(), "version", null, 0, 1, CompiledUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnit_Description(), ecorePackage.getEString(), "description", null, 0, 1, CompiledUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCompiledUnit_Manifest(), this.getCompiledUnitManifest(), null, "manifest", null, 1, 1, CompiledUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCompiledUnit_Unit(), ecorePackage.getEPackage(), null, "unit", null, 1, 1, CompiledUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCompiledUnit_Satellite(), ecorePackage.getEObject(), null, "satellite", null, 0, -1, CompiledUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCompiledUnit_Embedded(), this.getCompiledUnit(), null, "embedded", null, 0, -1, CompiledUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCompiledUnit_Packages(), ecorePackage.getEPackage(), null, "packages", null, 0, -1, CompiledUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(compiledUnitManifestEClass, CompiledUnitManifest.class, "CompiledUnitManifest", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getCompiledUnitManifest_FormatVersion(), ecorePackage.getEString(), "formatVersion", null, 0, 1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnitManifest_ProducedBy(), ecorePackage.getEString(), "producedBy", null, 0, 1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnitManifest_Language(), ecorePackage.getEString(), "language", null, 0, 1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnitManifest_QualifiedName(), ecorePackage.getEString(), "qualifiedName", null, 0, 1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnitManifest_UnitFingerprint(), ecorePackage.getEString(), "unitFingerprint", null, 0, 1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnitManifest_SourceFingerprint(), ecorePackage.getEString(), "sourceFingerprint", null, 0, 1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnitManifest_DependencyMode(), this.getDependencyMode(), "dependencyMode", null, 0, 1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getCompiledUnitManifest_Nature(), this.getUnitNature(), "nature", null, 0, 1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCompiledUnitManifest_PackageEntry(), this.getPackageEntry(), null, "packageEntry", null, 0, -1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCompiledUnitManifest_DependencyEntry(), this.getDependencyEntry(), null, "dependencyEntry", null, 0, -1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCompiledUnitManifest_BlackboxRequirement(), this.getBlackboxRequirement(), null, "blackboxRequirement", null, 0, -1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getCompiledUnitManifest_ResolvedClosure(), this.getResolvedDependency(), null, "resolvedClosure", null, 0, -1, CompiledUnitManifest.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(sourceUnitEClass, SourceUnit.class, "SourceUnit", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getSourceUnit_Language(), ecorePackage.getEString(), "language", null, 0, 1, SourceUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSourceUnit_QualifiedName(), ecorePackage.getEString(), "qualifiedName", null, 1, 1, SourceUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSourceUnit_Uri(), ecorePackage.getEString(), "uri", null, 0, 1, SourceUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSourceUnit_Source(), ecorePackage.getEString(), "source", null, 1, 1, SourceUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getSourceUnit_Fingerprint(), ecorePackage.getEString(), "fingerprint", null, 0, 1, SourceUnit.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(packageEntryEClass, PackageEntry.class, "PackageEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getPackageEntry_NsURI(), ecorePackage.getEString(), "nsURI", null, 0, 1, PackageEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPackageEntry_Fingerprint(), ecorePackage.getEString(), "fingerprint", null, 0, 1, PackageEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPackageEntry_Scheme(), ecorePackage.getEString(), "scheme", null, 0, 1, PackageEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getPackageEntry_Role(), this.getPackageRole(), "role", null, 0, 1, PackageEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(dependencyEntryEClass, DependencyEntry.class, "DependencyEntry", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getDependencyEntry_QualifiedName(), ecorePackage.getEString(), "qualifiedName", null, 0, 1, DependencyEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDependencyEntry_Mode(), this.getDependencyMode(), "mode", null, 0, 1, DependencyEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getDependencyEntry_Fingerprint(), ecorePackage.getEString(), "fingerprint", null, 0, 1, DependencyEntry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(blackboxRequirementEClass, BlackboxRequirement.class, "BlackboxRequirement", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getBlackboxRequirement_Name(), ecorePackage.getEString(), "name", null, 0, 1, BlackboxRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlackboxRequirement_SignatureFingerprint(), ecorePackage.getEString(), "signatureFingerprint", null, 0, 1, BlackboxRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getBlackboxRequirement_Provider(), ecorePackage.getEString(), "provider", null, 0, 1, BlackboxRequirement.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resolvedDependencyEClass, ResolvedDependency.class, "ResolvedDependency", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getResolvedDependency_QualifiedName(), ecorePackage.getEString(), "qualifiedName", null, 0, 1, ResolvedDependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResolvedDependency_Fingerprint(), ecorePackage.getEString(), "fingerprint", null, 0, 1, ResolvedDependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResolvedDependency_Source(), ecorePackage.getEString(), "source", null, 0, 1, ResolvedDependency.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(dependencyModeEEnum, DependencyMode.class, "DependencyMode");
		addEEnumLiteral(dependencyModeEEnum, DependencyMode.EMBED);
		addEEnumLiteral(dependencyModeEEnum, DependencyMode.PIN);
		addEEnumLiteral(dependencyModeEEnum, DependencyMode.REBIND);

		initEEnum(packageRoleEEnum, PackageRole.class, "PackageRole");
		addEEnumLiteral(packageRoleEEnum, PackageRole.REFERENCED);
		addEEnumLiteral(packageRoleEEnum, PackageRole.EMBEDDED);

		initEEnum(unitNatureEEnum, UnitNature.class, "UnitNature");
		addEEnumLiteral(unitNatureEEnum, UnitNature.TRANSFORMATION);
		addEEnumLiteral(unitNatureEEnum, UnitNature.LIBRARY);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// Version
		createVersionAnnotations();
	}

	/**
	 * Initializes the annotations for <b>Version</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createVersionAnnotations() {
		String source = "Version";
		addAnnotation
		  (this,
		   source,
		   new String[] {
			   "value", "1.0"
		   });
	}

} //CompiledPackageImpl
