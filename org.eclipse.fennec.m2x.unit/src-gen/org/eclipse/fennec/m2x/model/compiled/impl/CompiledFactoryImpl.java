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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.fennec.m2x.model.compiled.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class CompiledFactoryImpl extends EFactoryImpl implements CompiledFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static CompiledFactory init() {
		try {
			CompiledFactory theCompiledFactory = (CompiledFactory)EPackage.Registry.INSTANCE.getEFactory(CompiledPackage.eNS_URI);
			if (theCompiledFactory != null) {
				return theCompiledFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new CompiledFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public CompiledFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case CompiledPackage.COMPILED_UNIT: return createCompiledUnit();
			case CompiledPackage.COMPILED_UNIT_MANIFEST: return createCompiledUnitManifest();
			case CompiledPackage.SOURCE_UNIT: return createSourceUnit();
			case CompiledPackage.PACKAGE_ENTRY: return createPackageEntry();
			case CompiledPackage.DEPENDENCY_ENTRY: return createDependencyEntry();
			case CompiledPackage.BLACKBOX_REQUIREMENT: return createBlackboxRequirement();
			case CompiledPackage.RESOLVED_DEPENDENCY: return createResolvedDependency();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case CompiledPackage.DEPENDENCY_MODE:
				return createDependencyModeFromString(eDataType, initialValue);
			case CompiledPackage.PACKAGE_ROLE:
				return createPackageRoleFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case CompiledPackage.DEPENDENCY_MODE:
				return convertDependencyModeToString(eDataType, instanceValue);
			case CompiledPackage.PACKAGE_ROLE:
				return convertPackageRoleToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CompiledUnit createCompiledUnit() {
		CompiledUnitImpl compiledUnit = new CompiledUnitImpl();
		return compiledUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CompiledUnitManifest createCompiledUnitManifest() {
		CompiledUnitManifestImpl compiledUnitManifest = new CompiledUnitManifestImpl();
		return compiledUnitManifest;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public SourceUnit createSourceUnit() {
		SourceUnitImpl sourceUnit = new SourceUnitImpl();
		return sourceUnit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PackageEntry createPackageEntry() {
		PackageEntryImpl packageEntry = new PackageEntryImpl();
		return packageEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DependencyEntry createDependencyEntry() {
		DependencyEntryImpl dependencyEntry = new DependencyEntryImpl();
		return dependencyEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public BlackboxRequirement createBlackboxRequirement() {
		BlackboxRequirementImpl blackboxRequirement = new BlackboxRequirementImpl();
		return blackboxRequirement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ResolvedDependency createResolvedDependency() {
		ResolvedDependencyImpl resolvedDependency = new ResolvedDependencyImpl();
		return resolvedDependency;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public DependencyMode createDependencyModeFromString(EDataType eDataType, String initialValue) {
		DependencyMode result = DependencyMode.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertDependencyModeToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public PackageRole createPackageRoleFromString(EDataType eDataType, String initialValue) {
		PackageRole result = PackageRole.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertPackageRoleToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CompiledPackage getCompiledPackage() {
		return (CompiledPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static CompiledPackage getPackage() {
		return CompiledPackage.eINSTANCE;
	}

} //CompiledFactoryImpl
