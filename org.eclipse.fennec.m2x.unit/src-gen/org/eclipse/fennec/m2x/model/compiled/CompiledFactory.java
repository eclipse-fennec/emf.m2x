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

import org.eclipse.emf.ecore.EFactory;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage
 * @generated
 */
@ProviderType
public interface CompiledFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	CompiledFactory eINSTANCE = org.eclipse.fennec.m2x.model.compiled.impl.CompiledFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unit</em>'.
	 * @generated
	 */
	CompiledUnit createCompiledUnit();

	/**
	 * Returns a new object of class '<em>Unit Manifest</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unit Manifest</em>'.
	 * @generated
	 */
	CompiledUnitManifest createCompiledUnitManifest();

	/**
	 * Returns a new object of class '<em>Source Unit</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Source Unit</em>'.
	 * @generated
	 */
	SourceUnit createSourceUnit();

	/**
	 * Returns a new object of class '<em>Package Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Package Entry</em>'.
	 * @generated
	 */
	PackageEntry createPackageEntry();

	/**
	 * Returns a new object of class '<em>Dependency Entry</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Dependency Entry</em>'.
	 * @generated
	 */
	DependencyEntry createDependencyEntry();

	/**
	 * Returns a new object of class '<em>Blackbox Requirement</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Blackbox Requirement</em>'.
	 * @generated
	 */
	BlackboxRequirement createBlackboxRequirement();

	/**
	 * Returns a new object of class '<em>Resolved Dependency</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Resolved Dependency</em>'.
	 * @generated
	 */
	ResolvedDependency createResolvedDependency();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	CompiledPackage getCompiledPackage();

} //CompiledFactory
