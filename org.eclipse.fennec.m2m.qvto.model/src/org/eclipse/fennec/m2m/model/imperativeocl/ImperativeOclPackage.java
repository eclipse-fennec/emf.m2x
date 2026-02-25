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
package org.eclipse.fennec.m2m.model.imperativeocl;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.fennec.emf.osgi.annotation.provide.EPackage;

import org.eclipse.fennec.m2m.model.ocl.OclPackage;

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
 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclFactory
 * @model kind="package"
 *        annotation="Version value='1.0'"
 * @generated
 */
@ProviderType
@EPackage(uri = ImperativeOclPackage.eNS_URI, genModel = "/model/qvto.genmodel", genModelSourceLocations = {"model/qvto.genmodel","org.eclipse.fennec.m2m.qvto.model/model/qvto.genmodel"}, ecore = "/model/imperativeocl.ecore", ecoreSourceLocations = "/model/imperativeocl.ecore")
public interface ImperativeOclPackage extends org.eclipse.emf.ecore.EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "imperativeocl";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/fennec/m2m/qvto/imperativeocl/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "imperativeocl";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	ImperativeOclPackage eINSTANCE = org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeExpressionImpl <em>Imperative Expression</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeExpressionImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getImperativeExpression()
	 * @generated
	 */
	int IMPERATIVE_EXPRESSION = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_EXPRESSION__TYPE = OclPackage.OCL_EXPRESSION__TYPE;

	/**
	 * The number of structural features of the '<em>Imperative Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_EXPRESSION_FEATURE_COUNT = OclPackage.OCL_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Imperative Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_EXPRESSION_OPERATION_COUNT = OclPackage.OCL_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.AssignExpImpl <em>Assign Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.AssignExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getAssignExp()
	 * @generated
	 */
	int ASSIGN_EXP = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGN_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Is Reset</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGN_EXP__IS_RESET = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Left</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGN_EXP__LEFT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGN_EXP__VALUE = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGN_EXP__DEFAULT_VALUE = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Assign Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGN_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 4;

	/**
	 * The number of operations of the '<em>Assign Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSIGN_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.BlockExpImpl <em>Block Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.BlockExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getBlockExp()
	 * @generated
	 */
	int BLOCK_EXP = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_EXP__BODY = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Block Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Block Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.BreakExpImpl <em>Break Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.BreakExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getBreakExp()
	 * @generated
	 */
	int BREAK_EXP = 3;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The number of structural features of the '<em>Break Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Break Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BREAK_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ContinueExpImpl <em>Continue Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ContinueExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getContinueExp()
	 * @generated
	 */
	int CONTINUE_EXP = 4;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINUE_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The number of structural features of the '<em>Continue Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINUE_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Continue Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONTINUE_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ReturnExpImpl <em>Return Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ReturnExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getReturnExp()
	 * @generated
	 */
	int RETURN_EXP = 5;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_EXP__VALUE = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Return Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Return Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RETURN_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.WhileExpImpl <em>While Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.WhileExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getWhileExp()
	 * @generated
	 */
	int WHILE_EXP = 6;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WHILE_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WHILE_EXP__CONDITION = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WHILE_EXP__BODY = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>While Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WHILE_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>While Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int WHILE_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeLoopExpImpl <em>Imperative Loop Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeLoopExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getImperativeLoopExp()
	 * @generated
	 */
	int IMPERATIVE_LOOP_EXP = 7;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_LOOP_EXP__TYPE = OclPackage.LOOP_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_LOOP_EXP__OWNED_SOURCE = OclPackage.LOOP_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_LOOP_EXP__IS_IMPLICIT = OclPackage.LOOP_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_LOOP_EXP__IS_SAFE = OclPackage.LOOP_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Owned Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_LOOP_EXP__OWNED_BODY = OclPackage.LOOP_EXP__OWNED_BODY;

	/**
	 * The feature id for the '<em><b>Owned Iterators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_LOOP_EXP__OWNED_ITERATORS = OclPackage.LOOP_EXP__OWNED_ITERATORS;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_LOOP_EXP__CONDITION = OclPackage.LOOP_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Imperative Loop Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_LOOP_EXP_FEATURE_COUNT = OclPackage.LOOP_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Imperative Loop Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_LOOP_EXP_OPERATION_COUNT = OclPackage.LOOP_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ForExpImpl <em>For Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ForExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getForExp()
	 * @generated
	 */
	int FOR_EXP = 8;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP__TYPE = IMPERATIVE_LOOP_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP__OWNED_SOURCE = IMPERATIVE_LOOP_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP__IS_IMPLICIT = IMPERATIVE_LOOP_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP__IS_SAFE = IMPERATIVE_LOOP_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Owned Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP__OWNED_BODY = IMPERATIVE_LOOP_EXP__OWNED_BODY;

	/**
	 * The feature id for the '<em><b>Owned Iterators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP__OWNED_ITERATORS = IMPERATIVE_LOOP_EXP__OWNED_ITERATORS;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP__CONDITION = IMPERATIVE_LOOP_EXP__CONDITION;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP__NAME = IMPERATIVE_LOOP_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>For Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP_FEATURE_COUNT = IMPERATIVE_LOOP_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>For Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_EXP_OPERATION_COUNT = IMPERATIVE_LOOP_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeIterateExpImpl <em>Imperative Iterate Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeIterateExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getImperativeIterateExp()
	 * @generated
	 */
	int IMPERATIVE_ITERATE_EXP = 9;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP__TYPE = IMPERATIVE_LOOP_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP__OWNED_SOURCE = IMPERATIVE_LOOP_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP__IS_IMPLICIT = IMPERATIVE_LOOP_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP__IS_SAFE = IMPERATIVE_LOOP_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Owned Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP__OWNED_BODY = IMPERATIVE_LOOP_EXP__OWNED_BODY;

	/**
	 * The feature id for the '<em><b>Owned Iterators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP__OWNED_ITERATORS = IMPERATIVE_LOOP_EXP__OWNED_ITERATORS;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP__CONDITION = IMPERATIVE_LOOP_EXP__CONDITION;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP__TARGET = IMPERATIVE_LOOP_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Imperative Iterate Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP_FEATURE_COUNT = IMPERATIVE_LOOP_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Imperative Iterate Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IMPERATIVE_ITERATE_EXP_OPERATION_COUNT = IMPERATIVE_LOOP_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.SwitchExpImpl <em>Switch Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.SwitchExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getSwitchExp()
	 * @generated
	 */
	int SWITCH_EXP = 10;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Alternative Part</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH_EXP__ALTERNATIVE_PART = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Else Part</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH_EXP__ELSE_PART = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Switch Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Switch Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SWITCH_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.AltExpImpl <em>Alt Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.AltExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getAltExp()
	 * @generated
	 */
	int ALT_EXP = 11;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALT_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALT_EXP__CONDITION = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALT_EXP__BODY = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Alt Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALT_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Alt Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ALT_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ComputeExpImpl <em>Compute Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ComputeExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getComputeExp()
	 * @generated
	 */
	int COMPUTE_EXP = 12;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPUTE_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Returned Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPUTE_EXP__RETURNED_ELEMENT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPUTE_EXP__BODY = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Compute Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPUTE_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Compute Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COMPUTE_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.AssertExpImpl <em>Assert Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.AssertExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getAssertExp()
	 * @generated
	 */
	int ASSERT_EXP = 13;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSERT_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Severity</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSERT_EXP__SEVERITY = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Assertion</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSERT_EXP__ASSERTION = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Log</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSERT_EXP__LOG = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Assert Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSERT_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Assert Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ASSERT_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.LogExpImpl <em>Log Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.LogExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getLogExp()
	 * @generated
	 */
	int LOG_EXP = 14;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP__TYPE = OclPackage.OPERATION_CALL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP__OWNED_SOURCE = OclPackage.OPERATION_CALL_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP__IS_IMPLICIT = OclPackage.OPERATION_CALL_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP__IS_SAFE = OclPackage.OPERATION_CALL_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Is Pre</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP__IS_PRE = OclPackage.OPERATION_CALL_EXP__IS_PRE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP__NAME = OclPackage.OPERATION_CALL_EXP__NAME;

	/**
	 * The feature id for the '<em><b>Owned Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP__OWNED_ARGUMENTS = OclPackage.OPERATION_CALL_EXP__OWNED_ARGUMENTS;

	/**
	 * The feature id for the '<em><b>Referred Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP__REFERRED_OPERATION = OclPackage.OPERATION_CALL_EXP__REFERRED_OPERATION;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP__CONDITION = OclPackage.OPERATION_CALL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Log Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP_FEATURE_COUNT = OclPackage.OPERATION_CALL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Log Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOG_EXP_OPERATION_COUNT = OclPackage.OPERATION_CALL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.RaiseExpImpl <em>Raise Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.RaiseExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getRaiseExp()
	 * @generated
	 */
	int RAISE_EXP = 15;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RAISE_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Argument</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RAISE_EXP__ARGUMENT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Exception</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RAISE_EXP__EXCEPTION = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Raise Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RAISE_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Raise Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int RAISE_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.TryExpImpl <em>Try Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.TryExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getTryExp()
	 * @generated
	 */
	int TRY_EXP = 16;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRY_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Try Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRY_EXP__TRY_BODY = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Except Clause</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRY_EXP__EXCEPT_CLAUSE = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Try Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRY_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Try Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRY_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.CatchExpImpl <em>Catch Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.CatchExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getCatchExp()
	 * @generated
	 */
	int CATCH_EXP = 17;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATCH_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATCH_EXP__BODY = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Exception</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATCH_EXP__EXCEPTION = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Catch Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATCH_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Catch Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CATCH_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.InstantiationExpImpl <em>Instantiation Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.InstantiationExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getInstantiationExp()
	 * @generated
	 */
	int INSTANTIATION_EXP = 18;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTANTIATION_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Argument</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTANTIATION_EXP__ARGUMENT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Instantiated Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTANTIATION_EXP__INSTANTIATED_CLASS = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Extent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTANTIATION_EXP__EXTENT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Instantiation Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTANTIATION_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Instantiation Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INSTANTIATION_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.TransformationInstantiationExpImpl <em>Transformation Instantiation Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.TransformationInstantiationExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getTransformationInstantiationExp()
	 * @generated
	 */
	int TRANSFORMATION_INSTANTIATION_EXP = 19;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION_INSTANTIATION_EXP__TYPE = INSTANTIATION_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Argument</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION_INSTANTIATION_EXP__ARGUMENT = INSTANTIATION_EXP__ARGUMENT;

	/**
	 * The feature id for the '<em><b>Instantiated Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION_INSTANTIATION_EXP__INSTANTIATED_CLASS = INSTANTIATION_EXP__INSTANTIATED_CLASS;

	/**
	 * The feature id for the '<em><b>Extent</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION_INSTANTIATION_EXP__EXTENT = INSTANTIATION_EXP__EXTENT;

	/**
	 * The feature id for the '<em><b>Imported Transformation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION_INSTANTIATION_EXP__IMPORTED_TRANSFORMATION = INSTANTIATION_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Transformation Instantiation Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION_INSTANTIATION_EXP_FEATURE_COUNT = INSTANTIATION_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Transformation Instantiation Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRANSFORMATION_INSTANTIATION_EXP_OPERATION_COUNT = INSTANTIATION_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.VariableInitExpImpl <em>Variable Init Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.VariableInitExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getVariableInitExp()
	 * @generated
	 */
	int VARIABLE_INIT_EXP = 20;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INIT_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>With Result</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INIT_EXP__WITH_RESULT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Referred Variable</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INIT_EXP__REFERRED_VARIABLE = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Variable Init Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INIT_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Variable Init Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_INIT_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.UnlinkExpImpl <em>Unlink Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.UnlinkExpImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getUnlinkExp()
	 * @generated
	 */
	int UNLINK_EXP = 21;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNLINK_EXP__TYPE = IMPERATIVE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Item</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNLINK_EXP__ITEM = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNLINK_EXP__TARGET = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Unlink Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNLINK_EXP_FEATURE_COUNT = IMPERATIVE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Unlink Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNLINK_EXP_OPERATION_COUNT = IMPERATIVE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.TypedefImpl <em>Typedef</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.TypedefImpl
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getTypedef()
	 * @generated
	 */
	int TYPEDEF = 22;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EANNOTATIONS = EcorePackage.ECLASS__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__NAME = EcorePackage.ECLASS__NAME;

	/**
	 * The feature id for the '<em><b>Instance Class Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__INSTANCE_CLASS_NAME = EcorePackage.ECLASS__INSTANCE_CLASS_NAME;

	/**
	 * The feature id for the '<em><b>Instance Class</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__INSTANCE_CLASS = EcorePackage.ECLASS__INSTANCE_CLASS;

	/**
	 * The feature id for the '<em><b>Default Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__DEFAULT_VALUE = EcorePackage.ECLASS__DEFAULT_VALUE;

	/**
	 * The feature id for the '<em><b>Instance Type Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__INSTANCE_TYPE_NAME = EcorePackage.ECLASS__INSTANCE_TYPE_NAME;

	/**
	 * The feature id for the '<em><b>EPackage</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EPACKAGE = EcorePackage.ECLASS__EPACKAGE;

	/**
	 * The feature id for the '<em><b>EType Parameters</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__ETYPE_PARAMETERS = EcorePackage.ECLASS__ETYPE_PARAMETERS;

	/**
	 * The feature id for the '<em><b>Abstract</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__ABSTRACT = EcorePackage.ECLASS__ABSTRACT;

	/**
	 * The feature id for the '<em><b>Interface</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__INTERFACE = EcorePackage.ECLASS__INTERFACE;

	/**
	 * The feature id for the '<em><b>ESuper Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__ESUPER_TYPES = EcorePackage.ECLASS__ESUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EOperations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EOPERATIONS = EcorePackage.ECLASS__EOPERATIONS;

	/**
	 * The feature id for the '<em><b>EAll Attributes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EALL_ATTRIBUTES = EcorePackage.ECLASS__EALL_ATTRIBUTES;

	/**
	 * The feature id for the '<em><b>EAll References</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EALL_REFERENCES = EcorePackage.ECLASS__EALL_REFERENCES;

	/**
	 * The feature id for the '<em><b>EReferences</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EREFERENCES = EcorePackage.ECLASS__EREFERENCES;

	/**
	 * The feature id for the '<em><b>EAttributes</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EATTRIBUTES = EcorePackage.ECLASS__EATTRIBUTES;

	/**
	 * The feature id for the '<em><b>EAll Containments</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EALL_CONTAINMENTS = EcorePackage.ECLASS__EALL_CONTAINMENTS;

	/**
	 * The feature id for the '<em><b>EAll Operations</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EALL_OPERATIONS = EcorePackage.ECLASS__EALL_OPERATIONS;

	/**
	 * The feature id for the '<em><b>EAll Structural Features</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EALL_STRUCTURAL_FEATURES = EcorePackage.ECLASS__EALL_STRUCTURAL_FEATURES;

	/**
	 * The feature id for the '<em><b>EAll Super Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EALL_SUPER_TYPES = EcorePackage.ECLASS__EALL_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EID Attribute</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EID_ATTRIBUTE = EcorePackage.ECLASS__EID_ATTRIBUTE;

	/**
	 * The feature id for the '<em><b>EStructural Features</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__ESTRUCTURAL_FEATURES = EcorePackage.ECLASS__ESTRUCTURAL_FEATURES;

	/**
	 * The feature id for the '<em><b>EGeneric Super Types</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EGENERIC_SUPER_TYPES = EcorePackage.ECLASS__EGENERIC_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>EAll Generic Super Types</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__EALL_GENERIC_SUPER_TYPES = EcorePackage.ECLASS__EALL_GENERIC_SUPER_TYPES;

	/**
	 * The feature id for the '<em><b>Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__CONDITION = EcorePackage.ECLASS_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Base</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF__BASE = EcorePackage.ECLASS_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Typedef</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF_FEATURE_COUNT = EcorePackage.ECLASS_FEATURE_COUNT + 2;

	/**
	 * The operation id for the '<em>Get EAnnotation</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_EANNOTATION__STRING = EcorePackage.ECLASS___GET_EANNOTATION__STRING;

	/**
	 * The operation id for the '<em>Is Instance</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___IS_INSTANCE__OBJECT = EcorePackage.ECLASS___IS_INSTANCE__OBJECT;

	/**
	 * The operation id for the '<em>Get Classifier ID</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_CLASSIFIER_ID = EcorePackage.ECLASS___GET_CLASSIFIER_ID;

	/**
	 * The operation id for the '<em>Is Super Type Of</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___IS_SUPER_TYPE_OF__ECLASS = EcorePackage.ECLASS___IS_SUPER_TYPE_OF__ECLASS;

	/**
	 * The operation id for the '<em>Get Feature Count</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_FEATURE_COUNT = EcorePackage.ECLASS___GET_FEATURE_COUNT;

	/**
	 * The operation id for the '<em>Get EStructural Feature</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_ESTRUCTURAL_FEATURE__INT = EcorePackage.ECLASS___GET_ESTRUCTURAL_FEATURE__INT;

	/**
	 * The operation id for the '<em>Get Feature ID</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_FEATURE_ID__ESTRUCTURALFEATURE = EcorePackage.ECLASS___GET_FEATURE_ID__ESTRUCTURALFEATURE;

	/**
	 * The operation id for the '<em>Get EStructural Feature</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_ESTRUCTURAL_FEATURE__STRING = EcorePackage.ECLASS___GET_ESTRUCTURAL_FEATURE__STRING;

	/**
	 * The operation id for the '<em>Get Operation Count</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_OPERATION_COUNT = EcorePackage.ECLASS___GET_OPERATION_COUNT;

	/**
	 * The operation id for the '<em>Get EOperation</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_EOPERATION__INT = EcorePackage.ECLASS___GET_EOPERATION__INT;

	/**
	 * The operation id for the '<em>Get Operation ID</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_OPERATION_ID__EOPERATION = EcorePackage.ECLASS___GET_OPERATION_ID__EOPERATION;

	/**
	 * The operation id for the '<em>Get Override</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_OVERRIDE__EOPERATION = EcorePackage.ECLASS___GET_OVERRIDE__EOPERATION;

	/**
	 * The operation id for the '<em>Get Feature Type</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF___GET_FEATURE_TYPE__ESTRUCTURALFEATURE = EcorePackage.ECLASS___GET_FEATURE_TYPE__ESTRUCTURALFEATURE;

	/**
	 * The number of operations of the '<em>Typedef</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPEDEF_OPERATION_COUNT = EcorePackage.ECLASS_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind <em>Severity Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getSeverityKind()
	 * @generated
	 */
	int SEVERITY_KIND = 23;


	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.ImperativeExpression <em>Imperative Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Imperative Expression</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeExpression
	 * @generated
	 */
	EClass getImperativeExpression();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssignExp <em>Assign Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Assign Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AssignExp
	 * @generated
	 */
	EClass getAssignExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssignExp#isIsReset <em>Is Reset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Reset</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AssignExp#isIsReset()
	 * @see #getAssignExp()
	 * @generated
	 */
	EAttribute getAssignExp_IsReset();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssignExp#getLeft <em>Left</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Left</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AssignExp#getLeft()
	 * @see #getAssignExp()
	 * @generated
	 */
	EReference getAssignExp_Left();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssignExp#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Value</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AssignExp#getValue()
	 * @see #getAssignExp()
	 * @generated
	 */
	EReference getAssignExp_Value();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssignExp#getDefaultValue <em>Default Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Default Value</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AssignExp#getDefaultValue()
	 * @see #getAssignExp()
	 * @generated
	 */
	EReference getAssignExp_DefaultValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.BlockExp <em>Block Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Block Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.BlockExp
	 * @generated
	 */
	EClass getBlockExp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.imperativeocl.BlockExp#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Body</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.BlockExp#getBody()
	 * @see #getBlockExp()
	 * @generated
	 */
	EReference getBlockExp_Body();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.BreakExp <em>Break Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Break Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.BreakExp
	 * @generated
	 */
	EClass getBreakExp();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.ContinueExp <em>Continue Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Continue Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ContinueExp
	 * @generated
	 */
	EClass getContinueExp();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.ReturnExp <em>Return Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Return Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ReturnExp
	 * @generated
	 */
	EClass getReturnExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.ReturnExp#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ReturnExp#getValue()
	 * @see #getReturnExp()
	 * @generated
	 */
	EReference getReturnExp_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.WhileExp <em>While Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>While Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.WhileExp
	 * @generated
	 */
	EClass getWhileExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.WhileExp#getCondition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Condition</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.WhileExp#getCondition()
	 * @see #getWhileExp()
	 * @generated
	 */
	EReference getWhileExp_Condition();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.WhileExp#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Body</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.WhileExp#getBody()
	 * @see #getWhileExp()
	 * @generated
	 */
	EReference getWhileExp_Body();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.ImperativeLoopExp <em>Imperative Loop Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Imperative Loop Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeLoopExp
	 * @generated
	 */
	EClass getImperativeLoopExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.ImperativeLoopExp#getCondition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Condition</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeLoopExp#getCondition()
	 * @see #getImperativeLoopExp()
	 * @generated
	 */
	EReference getImperativeLoopExp_Condition();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.ForExp <em>For Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>For Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ForExp
	 * @generated
	 */
	EClass getForExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.imperativeocl.ForExp#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ForExp#getName()
	 * @see #getForExp()
	 * @generated
	 */
	EAttribute getForExp_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.ImperativeIterateExp <em>Imperative Iterate Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Imperative Iterate Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeIterateExp
	 * @generated
	 */
	EClass getImperativeIterateExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.ImperativeIterateExp#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ImperativeIterateExp#getTarget()
	 * @see #getImperativeIterateExp()
	 * @generated
	 */
	EReference getImperativeIterateExp_Target();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp <em>Switch Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Switch Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp
	 * @generated
	 */
	EClass getSwitchExp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp#getAlternativePart <em>Alternative Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Alternative Part</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp#getAlternativePart()
	 * @see #getSwitchExp()
	 * @generated
	 */
	EReference getSwitchExp_AlternativePart();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp#getElsePart <em>Else Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Else Part</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp#getElsePart()
	 * @see #getSwitchExp()
	 * @generated
	 */
	EReference getSwitchExp_ElsePart();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.AltExp <em>Alt Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Alt Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AltExp
	 * @generated
	 */
	EClass getAltExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.AltExp#getCondition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Condition</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AltExp#getCondition()
	 * @see #getAltExp()
	 * @generated
	 */
	EReference getAltExp_Condition();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.AltExp#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Body</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AltExp#getBody()
	 * @see #getAltExp()
	 * @generated
	 */
	EReference getAltExp_Body();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.ComputeExp <em>Compute Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Compute Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ComputeExp
	 * @generated
	 */
	EClass getComputeExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.ComputeExp#getReturnedElement <em>Returned Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Returned Element</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ComputeExp#getReturnedElement()
	 * @see #getComputeExp()
	 * @generated
	 */
	EReference getComputeExp_ReturnedElement();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.ComputeExp#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Body</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.ComputeExp#getBody()
	 * @see #getComputeExp()
	 * @generated
	 */
	EReference getComputeExp_Body();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp <em>Assert Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Assert Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AssertExp
	 * @generated
	 */
	EClass getAssertExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getSeverity <em>Severity</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Severity</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getSeverity()
	 * @see #getAssertExp()
	 * @generated
	 */
	EAttribute getAssertExp_Severity();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getAssertion <em>Assertion</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Assertion</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getAssertion()
	 * @see #getAssertExp()
	 * @generated
	 */
	EReference getAssertExp_Assertion();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getLog <em>Log</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Log</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.AssertExp#getLog()
	 * @see #getAssertExp()
	 * @generated
	 */
	EReference getAssertExp_Log();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.LogExp <em>Log Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Log Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.LogExp
	 * @generated
	 */
	EClass getLogExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.LogExp#getCondition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Condition</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.LogExp#getCondition()
	 * @see #getLogExp()
	 * @generated
	 */
	EReference getLogExp_Condition();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp <em>Raise Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Raise Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp
	 * @generated
	 */
	EClass getRaiseExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp#getArgument <em>Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Argument</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp#getArgument()
	 * @see #getRaiseExp()
	 * @generated
	 */
	EReference getRaiseExp_Argument();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp#getException <em>Exception</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Exception</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp#getException()
	 * @see #getRaiseExp()
	 * @generated
	 */
	EReference getRaiseExp_Exception();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.TryExp <em>Try Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Try Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.TryExp
	 * @generated
	 */
	EClass getTryExp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.imperativeocl.TryExp#getTryBody <em>Try Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Try Body</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.TryExp#getTryBody()
	 * @see #getTryExp()
	 * @generated
	 */
	EReference getTryExp_TryBody();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.imperativeocl.TryExp#getExceptClause <em>Except Clause</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Except Clause</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.TryExp#getExceptClause()
	 * @see #getTryExp()
	 * @generated
	 */
	EReference getTryExp_ExceptClause();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.CatchExp <em>Catch Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Catch Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.CatchExp
	 * @generated
	 */
	EClass getCatchExp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.imperativeocl.CatchExp#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Body</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.CatchExp#getBody()
	 * @see #getCatchExp()
	 * @generated
	 */
	EReference getCatchExp_Body();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.m2m.model.imperativeocl.CatchExp#getException <em>Exception</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Exception</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.CatchExp#getException()
	 * @see #getCatchExp()
	 * @generated
	 */
	EReference getCatchExp_Exception();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp <em>Instantiation Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Instantiation Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp
	 * @generated
	 */
	EClass getInstantiationExp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp#getArgument <em>Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Argument</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp#getArgument()
	 * @see #getInstantiationExp()
	 * @generated
	 */
	EReference getInstantiationExp_Argument();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp#getInstantiatedClass <em>Instantiated Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Instantiated Class</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp#getInstantiatedClass()
	 * @see #getInstantiationExp()
	 * @generated
	 */
	EReference getInstantiationExp_InstantiatedClass();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp#getExtent <em>Extent</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Extent</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp#getExtent()
	 * @see #getInstantiationExp()
	 * @generated
	 */
	EReference getInstantiationExp_Extent();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.TransformationInstantiationExp <em>Transformation Instantiation Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Transformation Instantiation Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.TransformationInstantiationExp
	 * @generated
	 */
	EClass getTransformationInstantiationExp();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.TransformationInstantiationExp#getImportedTransformation <em>Imported Transformation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Imported Transformation</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.TransformationInstantiationExp#getImportedTransformation()
	 * @see #getTransformationInstantiationExp()
	 * @generated
	 */
	EReference getTransformationInstantiationExp_ImportedTransformation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp <em>Variable Init Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable Init Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp
	 * @generated
	 */
	EClass getVariableInitExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp#isWithResult <em>With Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>With Result</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp#isWithResult()
	 * @see #getVariableInitExp()
	 * @generated
	 */
	EAttribute getVariableInitExp_WithResult();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp#getReferredVariable <em>Referred Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Referred Variable</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp#getReferredVariable()
	 * @see #getVariableInitExp()
	 * @generated
	 */
	EReference getVariableInitExp_ReferredVariable();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.UnlinkExp <em>Unlink Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unlink Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.UnlinkExp
	 * @generated
	 */
	EClass getUnlinkExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.UnlinkExp#getItem <em>Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Item</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.UnlinkExp#getItem()
	 * @see #getUnlinkExp()
	 * @generated
	 */
	EReference getUnlinkExp_Item();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.UnlinkExp#getTarget <em>Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Target</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.UnlinkExp#getTarget()
	 * @see #getUnlinkExp()
	 * @generated
	 */
	EReference getUnlinkExp_Target();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.imperativeocl.Typedef <em>Typedef</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Typedef</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.Typedef
	 * @generated
	 */
	EClass getTypedef();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.Typedef#getCondition <em>Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Condition</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.Typedef#getCondition()
	 * @see #getTypedef()
	 * @generated
	 */
	EReference getTypedef_Condition();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.imperativeocl.Typedef#getBase <em>Base</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Base</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.Typedef#getBase()
	 * @see #getTypedef()
	 * @generated
	 */
	EReference getTypedef_Base();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind <em>Severity Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Severity Kind</em>'.
	 * @see org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind
	 * @generated
	 */
	EEnum getSeverityKind();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	ImperativeOclFactory getImperativeOclFactory();

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
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeExpressionImpl <em>Imperative Expression</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeExpressionImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getImperativeExpression()
		 * @generated
		 */
		EClass IMPERATIVE_EXPRESSION = eINSTANCE.getImperativeExpression();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.AssignExpImpl <em>Assign Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.AssignExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getAssignExp()
		 * @generated
		 */
		EClass ASSIGN_EXP = eINSTANCE.getAssignExp();

		/**
		 * The meta object literal for the '<em><b>Is Reset</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSIGN_EXP__IS_RESET = eINSTANCE.getAssignExp_IsReset();

		/**
		 * The meta object literal for the '<em><b>Left</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSIGN_EXP__LEFT = eINSTANCE.getAssignExp_Left();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSIGN_EXP__VALUE = eINSTANCE.getAssignExp_Value();

		/**
		 * The meta object literal for the '<em><b>Default Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSIGN_EXP__DEFAULT_VALUE = eINSTANCE.getAssignExp_DefaultValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.BlockExpImpl <em>Block Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.BlockExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getBlockExp()
		 * @generated
		 */
		EClass BLOCK_EXP = eINSTANCE.getBlockExp();

		/**
		 * The meta object literal for the '<em><b>Body</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BLOCK_EXP__BODY = eINSTANCE.getBlockExp_Body();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.BreakExpImpl <em>Break Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.BreakExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getBreakExp()
		 * @generated
		 */
		EClass BREAK_EXP = eINSTANCE.getBreakExp();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ContinueExpImpl <em>Continue Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ContinueExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getContinueExp()
		 * @generated
		 */
		EClass CONTINUE_EXP = eINSTANCE.getContinueExp();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ReturnExpImpl <em>Return Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ReturnExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getReturnExp()
		 * @generated
		 */
		EClass RETURN_EXP = eINSTANCE.getReturnExp();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RETURN_EXP__VALUE = eINSTANCE.getReturnExp_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.WhileExpImpl <em>While Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.WhileExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getWhileExp()
		 * @generated
		 */
		EClass WHILE_EXP = eINSTANCE.getWhileExp();

		/**
		 * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WHILE_EXP__CONDITION = eINSTANCE.getWhileExp_Condition();

		/**
		 * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference WHILE_EXP__BODY = eINSTANCE.getWhileExp_Body();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeLoopExpImpl <em>Imperative Loop Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeLoopExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getImperativeLoopExp()
		 * @generated
		 */
		EClass IMPERATIVE_LOOP_EXP = eINSTANCE.getImperativeLoopExp();

		/**
		 * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMPERATIVE_LOOP_EXP__CONDITION = eINSTANCE.getImperativeLoopExp_Condition();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ForExpImpl <em>For Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ForExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getForExp()
		 * @generated
		 */
		EClass FOR_EXP = eINSTANCE.getForExp();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FOR_EXP__NAME = eINSTANCE.getForExp_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeIterateExpImpl <em>Imperative Iterate Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeIterateExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getImperativeIterateExp()
		 * @generated
		 */
		EClass IMPERATIVE_ITERATE_EXP = eINSTANCE.getImperativeIterateExp();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IMPERATIVE_ITERATE_EXP__TARGET = eINSTANCE.getImperativeIterateExp_Target();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.SwitchExpImpl <em>Switch Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.SwitchExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getSwitchExp()
		 * @generated
		 */
		EClass SWITCH_EXP = eINSTANCE.getSwitchExp();

		/**
		 * The meta object literal for the '<em><b>Alternative Part</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWITCH_EXP__ALTERNATIVE_PART = eINSTANCE.getSwitchExp_AlternativePart();

		/**
		 * The meta object literal for the '<em><b>Else Part</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference SWITCH_EXP__ELSE_PART = eINSTANCE.getSwitchExp_ElsePart();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.AltExpImpl <em>Alt Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.AltExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getAltExp()
		 * @generated
		 */
		EClass ALT_EXP = eINSTANCE.getAltExp();

		/**
		 * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ALT_EXP__CONDITION = eINSTANCE.getAltExp_Condition();

		/**
		 * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ALT_EXP__BODY = eINSTANCE.getAltExp_Body();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.ComputeExpImpl <em>Compute Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ComputeExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getComputeExp()
		 * @generated
		 */
		EClass COMPUTE_EXP = eINSTANCE.getComputeExp();

		/**
		 * The meta object literal for the '<em><b>Returned Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPUTE_EXP__RETURNED_ELEMENT = eINSTANCE.getComputeExp_ReturnedElement();

		/**
		 * The meta object literal for the '<em><b>Body</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COMPUTE_EXP__BODY = eINSTANCE.getComputeExp_Body();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.AssertExpImpl <em>Assert Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.AssertExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getAssertExp()
		 * @generated
		 */
		EClass ASSERT_EXP = eINSTANCE.getAssertExp();

		/**
		 * The meta object literal for the '<em><b>Severity</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ASSERT_EXP__SEVERITY = eINSTANCE.getAssertExp_Severity();

		/**
		 * The meta object literal for the '<em><b>Assertion</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSERT_EXP__ASSERTION = eINSTANCE.getAssertExp_Assertion();

		/**
		 * The meta object literal for the '<em><b>Log</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ASSERT_EXP__LOG = eINSTANCE.getAssertExp_Log();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.LogExpImpl <em>Log Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.LogExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getLogExp()
		 * @generated
		 */
		EClass LOG_EXP = eINSTANCE.getLogExp();

		/**
		 * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LOG_EXP__CONDITION = eINSTANCE.getLogExp_Condition();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.RaiseExpImpl <em>Raise Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.RaiseExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getRaiseExp()
		 * @generated
		 */
		EClass RAISE_EXP = eINSTANCE.getRaiseExp();

		/**
		 * The meta object literal for the '<em><b>Argument</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RAISE_EXP__ARGUMENT = eINSTANCE.getRaiseExp_Argument();

		/**
		 * The meta object literal for the '<em><b>Exception</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference RAISE_EXP__EXCEPTION = eINSTANCE.getRaiseExp_Exception();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.TryExpImpl <em>Try Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.TryExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getTryExp()
		 * @generated
		 */
		EClass TRY_EXP = eINSTANCE.getTryExp();

		/**
		 * The meta object literal for the '<em><b>Try Body</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRY_EXP__TRY_BODY = eINSTANCE.getTryExp_TryBody();

		/**
		 * The meta object literal for the '<em><b>Except Clause</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRY_EXP__EXCEPT_CLAUSE = eINSTANCE.getTryExp_ExceptClause();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.CatchExpImpl <em>Catch Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.CatchExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getCatchExp()
		 * @generated
		 */
		EClass CATCH_EXP = eINSTANCE.getCatchExp();

		/**
		 * The meta object literal for the '<em><b>Body</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CATCH_EXP__BODY = eINSTANCE.getCatchExp_Body();

		/**
		 * The meta object literal for the '<em><b>Exception</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CATCH_EXP__EXCEPTION = eINSTANCE.getCatchExp_Exception();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.InstantiationExpImpl <em>Instantiation Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.InstantiationExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getInstantiationExp()
		 * @generated
		 */
		EClass INSTANTIATION_EXP = eINSTANCE.getInstantiationExp();

		/**
		 * The meta object literal for the '<em><b>Argument</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INSTANTIATION_EXP__ARGUMENT = eINSTANCE.getInstantiationExp_Argument();

		/**
		 * The meta object literal for the '<em><b>Instantiated Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INSTANTIATION_EXP__INSTANTIATED_CLASS = eINSTANCE.getInstantiationExp_InstantiatedClass();

		/**
		 * The meta object literal for the '<em><b>Extent</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INSTANTIATION_EXP__EXTENT = eINSTANCE.getInstantiationExp_Extent();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.TransformationInstantiationExpImpl <em>Transformation Instantiation Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.TransformationInstantiationExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getTransformationInstantiationExp()
		 * @generated
		 */
		EClass TRANSFORMATION_INSTANTIATION_EXP = eINSTANCE.getTransformationInstantiationExp();

		/**
		 * The meta object literal for the '<em><b>Imported Transformation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRANSFORMATION_INSTANTIATION_EXP__IMPORTED_TRANSFORMATION = eINSTANCE.getTransformationInstantiationExp_ImportedTransformation();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.VariableInitExpImpl <em>Variable Init Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.VariableInitExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getVariableInitExp()
		 * @generated
		 */
		EClass VARIABLE_INIT_EXP = eINSTANCE.getVariableInitExp();

		/**
		 * The meta object literal for the '<em><b>With Result</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIABLE_INIT_EXP__WITH_RESULT = eINSTANCE.getVariableInitExp_WithResult();

		/**
		 * The meta object literal for the '<em><b>Referred Variable</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VARIABLE_INIT_EXP__REFERRED_VARIABLE = eINSTANCE.getVariableInitExp_ReferredVariable();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.UnlinkExpImpl <em>Unlink Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.UnlinkExpImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getUnlinkExp()
		 * @generated
		 */
		EClass UNLINK_EXP = eINSTANCE.getUnlinkExp();

		/**
		 * The meta object literal for the '<em><b>Item</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNLINK_EXP__ITEM = eINSTANCE.getUnlinkExp_Item();

		/**
		 * The meta object literal for the '<em><b>Target</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference UNLINK_EXP__TARGET = eINSTANCE.getUnlinkExp_Target();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.impl.TypedefImpl <em>Typedef</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.TypedefImpl
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getTypedef()
		 * @generated
		 */
		EClass TYPEDEF = eINSTANCE.getTypedef();

		/**
		 * The meta object literal for the '<em><b>Condition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPEDEF__CONDITION = eINSTANCE.getTypedef_Condition();

		/**
		 * The meta object literal for the '<em><b>Base</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPEDEF__BASE = eINSTANCE.getTypedef_Base();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind <em>Severity Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind
		 * @see org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl#getSeverityKind()
		 * @generated
		 */
		EEnum SEVERITY_KIND = eINSTANCE.getSeverityKind();

	}

} //ImperativeOclPackage
