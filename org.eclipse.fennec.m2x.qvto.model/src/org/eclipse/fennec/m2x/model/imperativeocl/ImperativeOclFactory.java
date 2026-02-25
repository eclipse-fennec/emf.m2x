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
package org.eclipse.fennec.m2x.model.imperativeocl;

import org.eclipse.emf.ecore.EFactory;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage
 * @generated
 */
@ProviderType
public interface ImperativeOclFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ImperativeOclFactory eINSTANCE = org.eclipse.fennec.m2x.model.imperativeocl.impl.ImperativeOclFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Assign Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Assign Exp</em>'.
	 * @generated
	 */
	AssignExp createAssignExp();

	/**
	 * Returns a new object of class '<em>Block Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Block Exp</em>'.
	 * @generated
	 */
	BlockExp createBlockExp();

	/**
	 * Returns a new object of class '<em>Break Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Break Exp</em>'.
	 * @generated
	 */
	BreakExp createBreakExp();

	/**
	 * Returns a new object of class '<em>Continue Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Continue Exp</em>'.
	 * @generated
	 */
	ContinueExp createContinueExp();

	/**
	 * Returns a new object of class '<em>Return Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Return Exp</em>'.
	 * @generated
	 */
	ReturnExp createReturnExp();

	/**
	 * Returns a new object of class '<em>While Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>While Exp</em>'.
	 * @generated
	 */
	WhileExp createWhileExp();

	/**
	 * Returns a new object of class '<em>For Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>For Exp</em>'.
	 * @generated
	 */
	ForExp createForExp();

	/**
	 * Returns a new object of class '<em>Imperative Iterate Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Imperative Iterate Exp</em>'.
	 * @generated
	 */
	ImperativeIterateExp createImperativeIterateExp();

	/**
	 * Returns a new object of class '<em>Switch Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Switch Exp</em>'.
	 * @generated
	 */
	SwitchExp createSwitchExp();

	/**
	 * Returns a new object of class '<em>Alt Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Alt Exp</em>'.
	 * @generated
	 */
	AltExp createAltExp();

	/**
	 * Returns a new object of class '<em>Compute Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Compute Exp</em>'.
	 * @generated
	 */
	ComputeExp createComputeExp();

	/**
	 * Returns a new object of class '<em>Assert Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Assert Exp</em>'.
	 * @generated
	 */
	AssertExp createAssertExp();

	/**
	 * Returns a new object of class '<em>Log Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Log Exp</em>'.
	 * @generated
	 */
	LogExp createLogExp();

	/**
	 * Returns a new object of class '<em>Raise Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Raise Exp</em>'.
	 * @generated
	 */
	RaiseExp createRaiseExp();

	/**
	 * Returns a new object of class '<em>Try Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Try Exp</em>'.
	 * @generated
	 */
	TryExp createTryExp();

	/**
	 * Returns a new object of class '<em>Catch Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Catch Exp</em>'.
	 * @generated
	 */
	CatchExp createCatchExp();

	/**
	 * Returns a new object of class '<em>Instantiation Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Instantiation Exp</em>'.
	 * @generated
	 */
	InstantiationExp createInstantiationExp();

	/**
	 * Returns a new object of class '<em>Transformation Instantiation Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Transformation Instantiation Exp</em>'.
	 * @generated
	 */
	TransformationInstantiationExp createTransformationInstantiationExp();

	/**
	 * Returns a new object of class '<em>Variable Init Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Variable Init Exp</em>'.
	 * @generated
	 */
	VariableInitExp createVariableInitExp();

	/**
	 * Returns a new object of class '<em>Unlink Exp</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Unlink Exp</em>'.
	 * @generated
	 */
	UnlinkExp createUnlinkExp();

	/**
	 * Returns a new object of class '<em>Typedef</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Typedef</em>'.
	 * @generated
	 */
	Typedef createTypedef();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	ImperativeOclPackage getImperativeOclPackage();

} //ImperativeOclFactory
