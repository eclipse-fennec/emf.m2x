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
package org.eclipse.fennec.m2x.model.m2t;

import org.eclipse.emf.ecore.EFactory;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * The <b>Factory</b> for the model.
 * It provides a create method for each non-abstract class of the model.
 * <!-- end-user-doc -->
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage
 * @generated
 */
@ProviderType
public interface M2tFactory extends EFactory {
	/**
	 * The singleton instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	M2tFactory eINSTANCE = org.eclipse.fennec.m2x.model.m2t.impl.M2tFactoryImpl.init();

	/**
	 * Returns a new object of class '<em>Module</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Module</em>'.
	 * @generated
	 */
	Module createModule();

	/**
	 * Returns a new object of class '<em>Init Section</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Init Section</em>'.
	 * @generated
	 */
	InitSection createInitSection();

	/**
	 * Returns a new object of class '<em>Template</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Template</em>'.
	 * @generated
	 */
	Template createTemplate();

	/**
	 * Returns a new object of class '<em>Query</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Query</em>'.
	 * @generated
	 */
	Query createQuery();

	/**
	 * Returns a new object of class '<em>Macro</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Macro</em>'.
	 * @generated
	 */
	Macro createMacro();

	/**
	 * Returns a new object of class '<em>For Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>For Block</em>'.
	 * @generated
	 */
	ForBlock createForBlock();

	/**
	 * Returns a new object of class '<em>If Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>If Block</em>'.
	 * @generated
	 */
	IfBlock createIfBlock();

	/**
	 * Returns a new object of class '<em>Let Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Let Block</em>'.
	 * @generated
	 */
	LetBlock createLetBlock();

	/**
	 * Returns a new object of class '<em>File Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>File Block</em>'.
	 * @generated
	 */
	FileBlock createFileBlock();

	/**
	 * Returns a new object of class '<em>Protected Area Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Protected Area Block</em>'.
	 * @generated
	 */
	ProtectedAreaBlock createProtectedAreaBlock();

	/**
	 * Returns a new object of class '<em>Trace Block</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Trace Block</em>'.
	 * @generated
	 */
	TraceBlock createTraceBlock();

	/**
	 * Returns a new object of class '<em>Text Expression</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Text Expression</em>'.
	 * @generated
	 */
	TextExpression createTextExpression();

	/**
	 * Returns a new object of class '<em>Template Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Template Invocation</em>'.
	 * @generated
	 */
	TemplateInvocation createTemplateInvocation();

	/**
	 * Returns a new object of class '<em>Query Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Query Invocation</em>'.
	 * @generated
	 */
	QueryInvocation createQueryInvocation();

	/**
	 * Returns a new object of class '<em>Macro Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return a new object of class '<em>Macro Invocation</em>'.
	 * @generated
	 */
	MacroInvocation createMacroInvocation();

	/**
	 * Returns the package supported by this factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the package supported by this factory.
	 * @generated
	 */
	M2tPackage getM2tPackage();

} //M2tFactory
