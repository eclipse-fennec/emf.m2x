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


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;

import org.eclipse.fennec.emf.osgi.annotation.provide.EPackage;

import org.eclipse.fennec.m2x.model.ocl.OclPackage;

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
 * @see org.eclipse.fennec.m2x.model.m2t.M2tFactory
 * @model kind="package"
 *        annotation="Version value='1.0'"
 * @generated
 */
@ProviderType
@EPackage(uri = M2tPackage.eNS_URI, genModel = "/model/m2t.genmodel", genModelSourceLocations = {"model/m2t.genmodel","org.eclipse.fennec.m2x.m2t.model/model/m2t.genmodel"}, ecore = "/model/m2t.ecore", ecoreSourceLocations = "/model/m2t.ecore")
public interface M2tPackage extends org.eclipse.emf.ecore.EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "m2t";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/fennec/m2x/m2t/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "m2t";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	M2tPackage eINSTANCE = org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.ModuleImpl <em>Module</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.ModuleImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getModule()
	 * @generated
	 */
	int MODULE = 0;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__EANNOTATIONS = EcorePackage.EPACKAGE__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__NAME = EcorePackage.EPACKAGE__NAME;

	/**
	 * The feature id for the '<em><b>Ns URI</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__NS_URI = EcorePackage.EPACKAGE__NS_URI;

	/**
	 * The feature id for the '<em><b>Ns Prefix</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__NS_PREFIX = EcorePackage.EPACKAGE__NS_PREFIX;

	/**
	 * The feature id for the '<em><b>EFactory Instance</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__EFACTORY_INSTANCE = EcorePackage.EPACKAGE__EFACTORY_INSTANCE;

	/**
	 * The feature id for the '<em><b>EClassifiers</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__ECLASSIFIERS = EcorePackage.EPACKAGE__ECLASSIFIERS;

	/**
	 * The feature id for the '<em><b>ESubpackages</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__ESUBPACKAGES = EcorePackage.EPACKAGE__ESUBPACKAGES;

	/**
	 * The feature id for the '<em><b>ESuper Package</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__ESUPER_PACKAGE = EcorePackage.EPACKAGE__ESUPER_PACKAGE;

	/**
	 * The feature id for the '<em><b>Owned Module Element</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__OWNED_MODULE_ELEMENT = EcorePackage.EPACKAGE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Input</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__INPUT = EcorePackage.EPACKAGE_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Extends</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__EXTENDS = EcorePackage.EPACKAGE_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Imports</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE__IMPORTS = EcorePackage.EPACKAGE_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Module</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_FEATURE_COUNT = EcorePackage.EPACKAGE_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get EAnnotation</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE___GET_EANNOTATION__STRING = EcorePackage.EPACKAGE___GET_EANNOTATION__STRING;

	/**
	 * The operation id for the '<em>Get EClassifier</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE___GET_ECLASSIFIER__STRING = EcorePackage.EPACKAGE___GET_ECLASSIFIER__STRING;

	/**
	 * The number of operations of the '<em>Module</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_OPERATION_COUNT = EcorePackage.EPACKAGE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.ModuleElementImpl <em>Module Element</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.ModuleElementImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getModuleElement()
	 * @generated
	 */
	int MODULE_ELEMENT = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_ELEMENT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_ELEMENT__VISIBILITY = 1;

	/**
	 * The feature id for the '<em><b>Module</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_ELEMENT__MODULE = 2;

	/**
	 * The number of structural features of the '<em>Module Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_ELEMENT_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Module Element</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MODULE_ELEMENT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateExpressionImpl <em>Template Expression</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.TemplateExpressionImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTemplateExpression()
	 * @generated
	 */
	int TEMPLATE_EXPRESSION = 4;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_EXPRESSION__TYPE = OclPackage.OCL_EXPRESSION__TYPE;

	/**
	 * The number of structural features of the '<em>Template Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_EXPRESSION_FEATURE_COUNT = OclPackage.OCL_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Template Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_EXPRESSION_OPERATION_COUNT = OclPackage.OCL_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.BlockImpl <em>Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.BlockImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getBlock()
	 * @generated
	 */
	int BLOCK = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK__TYPE = TEMPLATE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK__BODY = TEMPLATE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK__INIT = TEMPLATE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_FEATURE_COUNT = TEMPLATE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BLOCK_OPERATION_COUNT = TEMPLATE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.InitSectionImpl <em>Init Section</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.InitSectionImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getInitSection()
	 * @generated
	 */
	int INIT_SECTION = 3;

	/**
	 * The feature id for the '<em><b>Variable</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INIT_SECTION__VARIABLE = 0;

	/**
	 * The number of structural features of the '<em>Init Section</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INIT_SECTION_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Init Section</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INIT_SECTION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl <em>Template</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTemplate()
	 * @generated
	 */
	int TEMPLATE = 5;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__TYPE = BLOCK__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__BODY = BLOCK__BODY;

	/**
	 * The feature id for the '<em><b>Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__INIT = BLOCK__INIT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__NAME = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__VISIBILITY = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Module</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__MODULE = BLOCK_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__PARAMETER = BLOCK_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Guard</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__GUARD = BLOCK_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Overrides</b></em>' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__OVERRIDES = BLOCK_FEATURE_COUNT + 5;

	/**
	 * The feature id for the '<em><b>Main</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__MAIN = BLOCK_FEATURE_COUNT + 6;

	/**
	 * The feature id for the '<em><b>Post</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE__POST = BLOCK_FEATURE_COUNT + 7;

	/**
	 * The number of structural features of the '<em>Template</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 8;

	/**
	 * The number of operations of the '<em>Template</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_OPERATION_COUNT = BLOCK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.QueryImpl <em>Query</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.QueryImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getQuery()
	 * @generated
	 */
	int QUERY = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY__NAME = MODULE_ELEMENT__NAME;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY__VISIBILITY = MODULE_ELEMENT__VISIBILITY;

	/**
	 * The feature id for the '<em><b>Module</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY__MODULE = MODULE_ELEMENT__MODULE;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY__PARAMETER = MODULE_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY__EXPRESSION = MODULE_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Return Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY__RETURN_TYPE = MODULE_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_FEATURE_COUNT = MODULE_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Query</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_OPERATION_COUNT = MODULE_ELEMENT_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.MacroImpl <em>Macro</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.MacroImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getMacro()
	 * @generated
	 */
	int MACRO = 7;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO__TYPE = BLOCK__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO__BODY = BLOCK__BODY;

	/**
	 * The feature id for the '<em><b>Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO__INIT = BLOCK__INIT;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO__NAME = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Visibility</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO__VISIBILITY = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Module</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO__MODULE = BLOCK_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Parameter</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO__PARAMETER = BLOCK_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Return Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO__RETURN_TYPE = BLOCK_FEATURE_COUNT + 4;

	/**
	 * The number of structural features of the '<em>Macro</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 5;

	/**
	 * The number of operations of the '<em>Macro</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_OPERATION_COUNT = BLOCK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl <em>For Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getForBlock()
	 * @generated
	 */
	int FOR_BLOCK = 8;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK__TYPE = BLOCK__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK__BODY = BLOCK__BODY;

	/**
	 * The feature id for the '<em><b>Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK__INIT = BLOCK__INIT;

	/**
	 * The feature id for the '<em><b>Loop Variable</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK__LOOP_VARIABLE = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Iter Set</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK__ITER_SET = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Guard</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK__GUARD = BLOCK_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Before</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK__BEFORE = BLOCK_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>Each</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK__EACH = BLOCK_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>After</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK__AFTER = BLOCK_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>For Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 6;

	/**
	 * The number of operations of the '<em>For Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FOR_BLOCK_OPERATION_COUNT = BLOCK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.IfBlockImpl <em>If Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.IfBlockImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getIfBlock()
	 * @generated
	 */
	int IF_BLOCK = 9;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_BLOCK__TYPE = BLOCK__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_BLOCK__BODY = BLOCK__BODY;

	/**
	 * The feature id for the '<em><b>Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_BLOCK__INIT = BLOCK__INIT;

	/**
	 * The feature id for the '<em><b>If Expr</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_BLOCK__IF_EXPR = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Else If</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_BLOCK__ELSE_IF = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Else</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_BLOCK__ELSE = BLOCK_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>If Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_BLOCK_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>If Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_BLOCK_OPERATION_COUNT = BLOCK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.LetBlockImpl <em>Let Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.LetBlockImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getLetBlock()
	 * @generated
	 */
	int LET_BLOCK = 10;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_BLOCK__TYPE = BLOCK__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_BLOCK__BODY = BLOCK__BODY;

	/**
	 * The feature id for the '<em><b>Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_BLOCK__INIT = BLOCK__INIT;

	/**
	 * The feature id for the '<em><b>Let Variable</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_BLOCK__LET_VARIABLE = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Else Let</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_BLOCK__ELSE_LET = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Else</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_BLOCK__ELSE = BLOCK_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Let Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_BLOCK_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Let Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_BLOCK_OPERATION_COUNT = BLOCK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.FileBlockImpl <em>File Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.FileBlockImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getFileBlock()
	 * @generated
	 */
	int FILE_BLOCK = 11;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_BLOCK__TYPE = BLOCK__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_BLOCK__BODY = BLOCK__BODY;

	/**
	 * The feature id for the '<em><b>Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_BLOCK__INIT = BLOCK__INIT;

	/**
	 * The feature id for the '<em><b>File Url</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_BLOCK__FILE_URL = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Open Mode</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_BLOCK__OPEN_MODE = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Uniq Id</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_BLOCK__UNIQ_ID = BLOCK_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Charset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_BLOCK__CHARSET = BLOCK_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>File Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_BLOCK_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 4;

	/**
	 * The number of operations of the '<em>File Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FILE_BLOCK_OPERATION_COUNT = BLOCK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.ProtectedAreaBlockImpl <em>Protected Area Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.ProtectedAreaBlockImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getProtectedAreaBlock()
	 * @generated
	 */
	int PROTECTED_AREA_BLOCK = 12;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROTECTED_AREA_BLOCK__TYPE = BLOCK__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROTECTED_AREA_BLOCK__BODY = BLOCK__BODY;

	/**
	 * The feature id for the '<em><b>Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROTECTED_AREA_BLOCK__INIT = BLOCK__INIT;

	/**
	 * The feature id for the '<em><b>Marker</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROTECTED_AREA_BLOCK__MARKER = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Protected Area Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROTECTED_AREA_BLOCK_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Protected Area Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROTECTED_AREA_BLOCK_OPERATION_COUNT = BLOCK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TraceBlockImpl <em>Trace Block</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.TraceBlockImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTraceBlock()
	 * @generated
	 */
	int TRACE_BLOCK = 13;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_BLOCK__TYPE = BLOCK__TYPE;

	/**
	 * The feature id for the '<em><b>Body</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_BLOCK__BODY = BLOCK__BODY;

	/**
	 * The feature id for the '<em><b>Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_BLOCK__INIT = BLOCK__INIT;

	/**
	 * The feature id for the '<em><b>Model Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_BLOCK__MODEL_ELEMENT = BLOCK_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Trace Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_BLOCK_FEATURE_COUNT = BLOCK_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Trace Block</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TRACE_BLOCK_OPERATION_COUNT = BLOCK_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TextExpressionImpl <em>Text Expression</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.TextExpressionImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTextExpression()
	 * @generated
	 */
	int TEXT_EXPRESSION = 14;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_EXPRESSION__TYPE = TEMPLATE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_EXPRESSION__VALUE = TEMPLATE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Text Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_EXPRESSION_FEATURE_COUNT = TEMPLATE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Text Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEXT_EXPRESSION_OPERATION_COUNT = TEMPLATE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl <em>Template Invocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTemplateInvocation()
	 * @generated
	 */
	int TEMPLATE_INVOCATION = 15;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_INVOCATION__TYPE = TEMPLATE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_INVOCATION__DEFINITION = TEMPLATE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Argument</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_INVOCATION__ARGUMENT = TEMPLATE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Before</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_INVOCATION__BEFORE = TEMPLATE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Each</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_INVOCATION__EACH = TEMPLATE_EXPRESSION_FEATURE_COUNT + 3;

	/**
	 * The feature id for the '<em><b>After</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_INVOCATION__AFTER = TEMPLATE_EXPRESSION_FEATURE_COUNT + 4;

	/**
	 * The feature id for the '<em><b>Super</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_INVOCATION__SUPER = TEMPLATE_EXPRESSION_FEATURE_COUNT + 5;

	/**
	 * The number of structural features of the '<em>Template Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_INVOCATION_FEATURE_COUNT = TEMPLATE_EXPRESSION_FEATURE_COUNT + 6;

	/**
	 * The number of operations of the '<em>Template Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_INVOCATION_OPERATION_COUNT = TEMPLATE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.QueryInvocationImpl <em>Query Invocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.QueryInvocationImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getQueryInvocation()
	 * @generated
	 */
	int QUERY_INVOCATION = 16;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_INVOCATION__TYPE = TEMPLATE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_INVOCATION__DEFINITION = TEMPLATE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Argument</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_INVOCATION__ARGUMENT = TEMPLATE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Query Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_INVOCATION_FEATURE_COUNT = TEMPLATE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Query Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int QUERY_INVOCATION_OPERATION_COUNT = TEMPLATE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.MacroInvocationImpl <em>Macro Invocation</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.MacroInvocationImpl
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getMacroInvocation()
	 * @generated
	 */
	int MACRO_INVOCATION = 17;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_INVOCATION__TYPE = TEMPLATE_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_INVOCATION__DEFINITION = TEMPLATE_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Argument</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_INVOCATION__ARGUMENT = TEMPLATE_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Macro Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_INVOCATION_FEATURE_COUNT = TEMPLATE_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Macro Invocation</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MACRO_INVOCATION_OPERATION_COUNT = TEMPLATE_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.OpenModeKind <em>Open Mode Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.OpenModeKind
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getOpenModeKind()
	 * @generated
	 */
	int OPEN_MODE_KIND = 18;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.m2t.VisibilityKind <em>Visibility Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.m2t.VisibilityKind
	 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getVisibilityKind()
	 * @generated
	 */
	int VISIBILITY_KIND = 19;


	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.Module <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Module</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Module
	 * @generated
	 */
	EClass getModule();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.Module#getOwnedModuleElement <em>Owned Module Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Module Element</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Module#getOwnedModuleElement()
	 * @see #getModule()
	 * @generated
	 */
	EReference getModule_OwnedModuleElement();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.m2x.model.m2t.Module#getInput <em>Input</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Input</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Module#getInput()
	 * @see #getModule()
	 * @generated
	 */
	EReference getModule_Input();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.m2x.model.m2t.Module#getExtends <em>Extends</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Extends</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Module#getExtends()
	 * @see #getModule()
	 * @generated
	 */
	EReference getModule_Extends();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.m2x.model.m2t.Module#getImports <em>Imports</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Imports</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Module#getImports()
	 * @see #getModule()
	 * @generated
	 */
	EReference getModule_Imports();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement <em>Module Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Module Element</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ModuleElement
	 * @generated
	 */
	EClass getModuleElement();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ModuleElement#getName()
	 * @see #getModuleElement()
	 * @generated
	 */
	EAttribute getModuleElement_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getVisibility <em>Visibility</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Visibility</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ModuleElement#getVisibility()
	 * @see #getModuleElement()
	 * @generated
	 */
	EAttribute getModuleElement_Visibility();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.fennec.m2x.model.m2t.ModuleElement#getModule <em>Module</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Module</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ModuleElement#getModule()
	 * @see #getModuleElement()
	 * @generated
	 */
	EReference getModuleElement_Module();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.Block <em>Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Block</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Block
	 * @generated
	 */
	EClass getBlock();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.Block#getBody <em>Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Body</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Block#getBody()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_Body();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.Block#getInit <em>Init</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Init</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Block#getInit()
	 * @see #getBlock()
	 * @generated
	 */
	EReference getBlock_Init();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.InitSection <em>Init Section</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Init Section</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.InitSection
	 * @generated
	 */
	EClass getInitSection();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.InitSection#getVariable <em>Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Variable</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.InitSection#getVariable()
	 * @see #getInitSection()
	 * @generated
	 */
	EReference getInitSection_Variable();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.TemplateExpression <em>Template Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Template Expression</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TemplateExpression
	 * @generated
	 */
	EClass getTemplateExpression();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.Template <em>Template</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Template</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Template
	 * @generated
	 */
	EClass getTemplate();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.Template#getParameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameter</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Template#getParameter()
	 * @see #getTemplate()
	 * @generated
	 */
	EReference getTemplate_Parameter();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.Template#getGuard <em>Guard</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Guard</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Template#getGuard()
	 * @see #getTemplate()
	 * @generated
	 */
	EReference getTemplate_Guard();

	/**
	 * Returns the meta object for the reference list '{@link org.eclipse.fennec.m2x.model.m2t.Template#getOverrides <em>Overrides</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference list '<em>Overrides</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Template#getOverrides()
	 * @see #getTemplate()
	 * @generated
	 */
	EReference getTemplate_Overrides();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.m2t.Template#isMain <em>Main</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Main</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Template#isMain()
	 * @see #getTemplate()
	 * @generated
	 */
	EAttribute getTemplate_Main();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.Template#getPost <em>Post</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Post</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Template#getPost()
	 * @see #getTemplate()
	 * @generated
	 */
	EReference getTemplate_Post();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.Query <em>Query</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Query</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Query
	 * @generated
	 */
	EClass getQuery();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.Query#getParameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameter</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Query#getParameter()
	 * @see #getQuery()
	 * @generated
	 */
	EReference getQuery_Parameter();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.Query#getExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Expression</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Query#getExpression()
	 * @see #getQuery()
	 * @generated
	 */
	EReference getQuery_Expression();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.m2t.Query#getReturnType <em>Return Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Return Type</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Query#getReturnType()
	 * @see #getQuery()
	 * @generated
	 */
	EReference getQuery_ReturnType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.Macro <em>Macro</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Macro</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Macro
	 * @generated
	 */
	EClass getMacro();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.Macro#getParameter <em>Parameter</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Parameter</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Macro#getParameter()
	 * @see #getMacro()
	 * @generated
	 */
	EReference getMacro_Parameter();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.m2t.Macro#getReturnType <em>Return Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Return Type</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.Macro#getReturnType()
	 * @see #getMacro()
	 * @generated
	 */
	EReference getMacro_ReturnType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock <em>For Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>For Block</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ForBlock
	 * @generated
	 */
	EClass getForBlock();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getLoopVariable <em>Loop Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Loop Variable</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ForBlock#getLoopVariable()
	 * @see #getForBlock()
	 * @generated
	 */
	EReference getForBlock_LoopVariable();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getIterSet <em>Iter Set</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Iter Set</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ForBlock#getIterSet()
	 * @see #getForBlock()
	 * @generated
	 */
	EReference getForBlock_IterSet();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getGuard <em>Guard</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Guard</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ForBlock#getGuard()
	 * @see #getForBlock()
	 * @generated
	 */
	EReference getForBlock_Guard();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getBefore <em>Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Before</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ForBlock#getBefore()
	 * @see #getForBlock()
	 * @generated
	 */
	EReference getForBlock_Before();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getEach <em>Each</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Each</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ForBlock#getEach()
	 * @see #getForBlock()
	 * @generated
	 */
	EReference getForBlock_Each();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.ForBlock#getAfter <em>After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>After</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ForBlock#getAfter()
	 * @see #getForBlock()
	 * @generated
	 */
	EReference getForBlock_After();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.IfBlock <em>If Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>If Block</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.IfBlock
	 * @generated
	 */
	EClass getIfBlock();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.IfBlock#getIfExpr <em>If Expr</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>If Expr</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.IfBlock#getIfExpr()
	 * @see #getIfBlock()
	 * @generated
	 */
	EReference getIfBlock_IfExpr();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.IfBlock#getElseIf <em>Else If</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Else If</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.IfBlock#getElseIf()
	 * @see #getIfBlock()
	 * @generated
	 */
	EReference getIfBlock_ElseIf();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.IfBlock#getElse <em>Else</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Else</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.IfBlock#getElse()
	 * @see #getIfBlock()
	 * @generated
	 */
	EReference getIfBlock_Else();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.LetBlock <em>Let Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Let Block</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.LetBlock
	 * @generated
	 */
	EClass getLetBlock();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.LetBlock#getLetVariable <em>Let Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Let Variable</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.LetBlock#getLetVariable()
	 * @see #getLetBlock()
	 * @generated
	 */
	EReference getLetBlock_LetVariable();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.LetBlock#getElseLet <em>Else Let</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Else Let</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.LetBlock#getElseLet()
	 * @see #getLetBlock()
	 * @generated
	 */
	EReference getLetBlock_ElseLet();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.LetBlock#getElse <em>Else</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Else</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.LetBlock#getElse()
	 * @see #getLetBlock()
	 * @generated
	 */
	EReference getLetBlock_Else();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.FileBlock <em>File Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>File Block</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.FileBlock
	 * @generated
	 */
	EClass getFileBlock();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getFileUrl <em>File Url</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>File Url</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.FileBlock#getFileUrl()
	 * @see #getFileBlock()
	 * @generated
	 */
	EReference getFileBlock_FileUrl();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getOpenMode <em>Open Mode</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Open Mode</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.FileBlock#getOpenMode()
	 * @see #getFileBlock()
	 * @generated
	 */
	EAttribute getFileBlock_OpenMode();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getUniqId <em>Uniq Id</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Uniq Id</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.FileBlock#getUniqId()
	 * @see #getFileBlock()
	 * @generated
	 */
	EReference getFileBlock_UniqId();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getCharset <em>Charset</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Charset</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.FileBlock#getCharset()
	 * @see #getFileBlock()
	 * @generated
	 */
	EReference getFileBlock_Charset();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock <em>Protected Area Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Protected Area Block</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock
	 * @generated
	 */
	EClass getProtectedAreaBlock();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock#getMarker <em>Marker</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Marker</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock#getMarker()
	 * @see #getProtectedAreaBlock()
	 * @generated
	 */
	EReference getProtectedAreaBlock_Marker();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.TraceBlock <em>Trace Block</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Trace Block</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TraceBlock
	 * @generated
	 */
	EClass getTraceBlock();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.TraceBlock#getModelElement <em>Model Element</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Model Element</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TraceBlock#getModelElement()
	 * @see #getTraceBlock()
	 * @generated
	 */
	EReference getTraceBlock_ModelElement();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.TextExpression <em>Text Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Text Expression</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TextExpression
	 * @generated
	 */
	EClass getTextExpression();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.m2t.TextExpression#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Value</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TextExpression#getValue()
	 * @see #getTextExpression()
	 * @generated
	 */
	EAttribute getTextExpression_Value();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation <em>Template Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Template Invocation</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TemplateInvocation
	 * @generated
	 */
	EClass getTemplateInvocation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getDefinition()
	 * @see #getTemplateInvocation()
	 * @generated
	 */
	EReference getTemplateInvocation_Definition();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getArgument <em>Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Argument</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getArgument()
	 * @see #getTemplateInvocation()
	 * @generated
	 */
	EReference getTemplateInvocation_Argument();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getBefore <em>Before</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Before</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getBefore()
	 * @see #getTemplateInvocation()
	 * @generated
	 */
	EReference getTemplateInvocation_Before();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getEach <em>Each</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Each</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getEach()
	 * @see #getTemplateInvocation()
	 * @generated
	 */
	EReference getTemplateInvocation_Each();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getAfter <em>After</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>After</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getAfter()
	 * @see #getTemplateInvocation()
	 * @generated
	 */
	EReference getTemplateInvocation_After();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#isSuper <em>Super</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Super</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#isSuper()
	 * @see #getTemplateInvocation()
	 * @generated
	 */
	EAttribute getTemplateInvocation_Super();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.QueryInvocation <em>Query Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Query Invocation</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.QueryInvocation
	 * @generated
	 */
	EClass getQueryInvocation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.m2t.QueryInvocation#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.QueryInvocation#getDefinition()
	 * @see #getQueryInvocation()
	 * @generated
	 */
	EReference getQueryInvocation_Definition();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.QueryInvocation#getArgument <em>Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Argument</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.QueryInvocation#getArgument()
	 * @see #getQueryInvocation()
	 * @generated
	 */
	EReference getQueryInvocation_Argument();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.m2t.MacroInvocation <em>Macro Invocation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Macro Invocation</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.MacroInvocation
	 * @generated
	 */
	EClass getMacroInvocation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.m2t.MacroInvocation#getDefinition <em>Definition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Definition</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.MacroInvocation#getDefinition()
	 * @see #getMacroInvocation()
	 * @generated
	 */
	EReference getMacroInvocation_Definition();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.m2t.MacroInvocation#getArgument <em>Argument</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Argument</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.MacroInvocation#getArgument()
	 * @see #getMacroInvocation()
	 * @generated
	 */
	EReference getMacroInvocation_Argument();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.m2x.model.m2t.OpenModeKind <em>Open Mode Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Open Mode Kind</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.OpenModeKind
	 * @generated
	 */
	EEnum getOpenModeKind();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.m2x.model.m2t.VisibilityKind <em>Visibility Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Visibility Kind</em>'.
	 * @see org.eclipse.fennec.m2x.model.m2t.VisibilityKind
	 * @generated
	 */
	EEnum getVisibilityKind();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	M2tFactory getM2tFactory();

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
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.ModuleImpl <em>Module</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.ModuleImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getModule()
		 * @generated
		 */
		EClass MODULE = eINSTANCE.getModule();

		/**
		 * The meta object literal for the '<em><b>Owned Module Element</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE__OWNED_MODULE_ELEMENT = eINSTANCE.getModule_OwnedModuleElement();

		/**
		 * The meta object literal for the '<em><b>Input</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE__INPUT = eINSTANCE.getModule_Input();

		/**
		 * The meta object literal for the '<em><b>Extends</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE__EXTENDS = eINSTANCE.getModule_Extends();

		/**
		 * The meta object literal for the '<em><b>Imports</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE__IMPORTS = eINSTANCE.getModule_Imports();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.ModuleElementImpl <em>Module Element</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.ModuleElementImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getModuleElement()
		 * @generated
		 */
		EClass MODULE_ELEMENT = eINSTANCE.getModuleElement();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODULE_ELEMENT__NAME = eINSTANCE.getModuleElement_Name();

		/**
		 * The meta object literal for the '<em><b>Visibility</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute MODULE_ELEMENT__VISIBILITY = eINSTANCE.getModuleElement_Visibility();

		/**
		 * The meta object literal for the '<em><b>Module</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MODULE_ELEMENT__MODULE = eINSTANCE.getModuleElement_Module();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.BlockImpl <em>Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.BlockImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getBlock()
		 * @generated
		 */
		EClass BLOCK = eINSTANCE.getBlock();

		/**
		 * The meta object literal for the '<em><b>Body</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BLOCK__BODY = eINSTANCE.getBlock_Body();

		/**
		 * The meta object literal for the '<em><b>Init</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference BLOCK__INIT = eINSTANCE.getBlock_Init();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.InitSectionImpl <em>Init Section</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.InitSectionImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getInitSection()
		 * @generated
		 */
		EClass INIT_SECTION = eINSTANCE.getInitSection();

		/**
		 * The meta object literal for the '<em><b>Variable</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference INIT_SECTION__VARIABLE = eINSTANCE.getInitSection_Variable();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateExpressionImpl <em>Template Expression</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.TemplateExpressionImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTemplateExpression()
		 * @generated
		 */
		EClass TEMPLATE_EXPRESSION = eINSTANCE.getTemplateExpression();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl <em>Template</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTemplate()
		 * @generated
		 */
		EClass TEMPLATE = eINSTANCE.getTemplate();

		/**
		 * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE__PARAMETER = eINSTANCE.getTemplate_Parameter();

		/**
		 * The meta object literal for the '<em><b>Guard</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE__GUARD = eINSTANCE.getTemplate_Guard();

		/**
		 * The meta object literal for the '<em><b>Overrides</b></em>' reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE__OVERRIDES = eINSTANCE.getTemplate_Overrides();

		/**
		 * The meta object literal for the '<em><b>Main</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPLATE__MAIN = eINSTANCE.getTemplate_Main();

		/**
		 * The meta object literal for the '<em><b>Post</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE__POST = eINSTANCE.getTemplate_Post();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.QueryImpl <em>Query</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.QueryImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getQuery()
		 * @generated
		 */
		EClass QUERY = eINSTANCE.getQuery();

		/**
		 * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUERY__PARAMETER = eINSTANCE.getQuery_Parameter();

		/**
		 * The meta object literal for the '<em><b>Expression</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUERY__EXPRESSION = eINSTANCE.getQuery_Expression();

		/**
		 * The meta object literal for the '<em><b>Return Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUERY__RETURN_TYPE = eINSTANCE.getQuery_ReturnType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.MacroImpl <em>Macro</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.MacroImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getMacro()
		 * @generated
		 */
		EClass MACRO = eINSTANCE.getMacro();

		/**
		 * The meta object literal for the '<em><b>Parameter</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MACRO__PARAMETER = eINSTANCE.getMacro_Parameter();

		/**
		 * The meta object literal for the '<em><b>Return Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MACRO__RETURN_TYPE = eINSTANCE.getMacro_ReturnType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl <em>For Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getForBlock()
		 * @generated
		 */
		EClass FOR_BLOCK = eINSTANCE.getForBlock();

		/**
		 * The meta object literal for the '<em><b>Loop Variable</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOR_BLOCK__LOOP_VARIABLE = eINSTANCE.getForBlock_LoopVariable();

		/**
		 * The meta object literal for the '<em><b>Iter Set</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOR_BLOCK__ITER_SET = eINSTANCE.getForBlock_IterSet();

		/**
		 * The meta object literal for the '<em><b>Guard</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOR_BLOCK__GUARD = eINSTANCE.getForBlock_Guard();

		/**
		 * The meta object literal for the '<em><b>Before</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOR_BLOCK__BEFORE = eINSTANCE.getForBlock_Before();

		/**
		 * The meta object literal for the '<em><b>Each</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOR_BLOCK__EACH = eINSTANCE.getForBlock_Each();

		/**
		 * The meta object literal for the '<em><b>After</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FOR_BLOCK__AFTER = eINSTANCE.getForBlock_After();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.IfBlockImpl <em>If Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.IfBlockImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getIfBlock()
		 * @generated
		 */
		EClass IF_BLOCK = eINSTANCE.getIfBlock();

		/**
		 * The meta object literal for the '<em><b>If Expr</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IF_BLOCK__IF_EXPR = eINSTANCE.getIfBlock_IfExpr();

		/**
		 * The meta object literal for the '<em><b>Else If</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IF_BLOCK__ELSE_IF = eINSTANCE.getIfBlock_ElseIf();

		/**
		 * The meta object literal for the '<em><b>Else</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IF_BLOCK__ELSE = eINSTANCE.getIfBlock_Else();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.LetBlockImpl <em>Let Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.LetBlockImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getLetBlock()
		 * @generated
		 */
		EClass LET_BLOCK = eINSTANCE.getLetBlock();

		/**
		 * The meta object literal for the '<em><b>Let Variable</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LET_BLOCK__LET_VARIABLE = eINSTANCE.getLetBlock_LetVariable();

		/**
		 * The meta object literal for the '<em><b>Else Let</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LET_BLOCK__ELSE_LET = eINSTANCE.getLetBlock_ElseLet();

		/**
		 * The meta object literal for the '<em><b>Else</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LET_BLOCK__ELSE = eINSTANCE.getLetBlock_Else();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.FileBlockImpl <em>File Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.FileBlockImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getFileBlock()
		 * @generated
		 */
		EClass FILE_BLOCK = eINSTANCE.getFileBlock();

		/**
		 * The meta object literal for the '<em><b>File Url</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FILE_BLOCK__FILE_URL = eINSTANCE.getFileBlock_FileUrl();

		/**
		 * The meta object literal for the '<em><b>Open Mode</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FILE_BLOCK__OPEN_MODE = eINSTANCE.getFileBlock_OpenMode();

		/**
		 * The meta object literal for the '<em><b>Uniq Id</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FILE_BLOCK__UNIQ_ID = eINSTANCE.getFileBlock_UniqId();

		/**
		 * The meta object literal for the '<em><b>Charset</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference FILE_BLOCK__CHARSET = eINSTANCE.getFileBlock_Charset();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.ProtectedAreaBlockImpl <em>Protected Area Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.ProtectedAreaBlockImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getProtectedAreaBlock()
		 * @generated
		 */
		EClass PROTECTED_AREA_BLOCK = eINSTANCE.getProtectedAreaBlock();

		/**
		 * The meta object literal for the '<em><b>Marker</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROTECTED_AREA_BLOCK__MARKER = eINSTANCE.getProtectedAreaBlock_Marker();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TraceBlockImpl <em>Trace Block</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.TraceBlockImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTraceBlock()
		 * @generated
		 */
		EClass TRACE_BLOCK = eINSTANCE.getTraceBlock();

		/**
		 * The meta object literal for the '<em><b>Model Element</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TRACE_BLOCK__MODEL_ELEMENT = eINSTANCE.getTraceBlock_ModelElement();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TextExpressionImpl <em>Text Expression</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.TextExpressionImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTextExpression()
		 * @generated
		 */
		EClass TEXT_EXPRESSION = eINSTANCE.getTextExpression();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEXT_EXPRESSION__VALUE = eINSTANCE.getTextExpression_Value();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl <em>Template Invocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getTemplateInvocation()
		 * @generated
		 */
		EClass TEMPLATE_INVOCATION = eINSTANCE.getTemplateInvocation();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE_INVOCATION__DEFINITION = eINSTANCE.getTemplateInvocation_Definition();

		/**
		 * The meta object literal for the '<em><b>Argument</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE_INVOCATION__ARGUMENT = eINSTANCE.getTemplateInvocation_Argument();

		/**
		 * The meta object literal for the '<em><b>Before</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE_INVOCATION__BEFORE = eINSTANCE.getTemplateInvocation_Before();

		/**
		 * The meta object literal for the '<em><b>Each</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE_INVOCATION__EACH = eINSTANCE.getTemplateInvocation_Each();

		/**
		 * The meta object literal for the '<em><b>After</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE_INVOCATION__AFTER = eINSTANCE.getTemplateInvocation_After();

		/**
		 * The meta object literal for the '<em><b>Super</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TEMPLATE_INVOCATION__SUPER = eINSTANCE.getTemplateInvocation_Super();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.QueryInvocationImpl <em>Query Invocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.QueryInvocationImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getQueryInvocation()
		 * @generated
		 */
		EClass QUERY_INVOCATION = eINSTANCE.getQueryInvocation();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUERY_INVOCATION__DEFINITION = eINSTANCE.getQueryInvocation_Definition();

		/**
		 * The meta object literal for the '<em><b>Argument</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference QUERY_INVOCATION__ARGUMENT = eINSTANCE.getQueryInvocation_Argument();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.impl.MacroInvocationImpl <em>Macro Invocation</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.MacroInvocationImpl
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getMacroInvocation()
		 * @generated
		 */
		EClass MACRO_INVOCATION = eINSTANCE.getMacroInvocation();

		/**
		 * The meta object literal for the '<em><b>Definition</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MACRO_INVOCATION__DEFINITION = eINSTANCE.getMacroInvocation_Definition();

		/**
		 * The meta object literal for the '<em><b>Argument</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MACRO_INVOCATION__ARGUMENT = eINSTANCE.getMacroInvocation_Argument();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.OpenModeKind <em>Open Mode Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.OpenModeKind
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getOpenModeKind()
		 * @generated
		 */
		EEnum OPEN_MODE_KIND = eINSTANCE.getOpenModeKind();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.m2t.VisibilityKind <em>Visibility Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.m2t.VisibilityKind
		 * @see org.eclipse.fennec.m2x.model.m2t.impl.M2tPackageImpl#getVisibilityKind()
		 * @generated
		 */
		EEnum VISIBILITY_KIND = eINSTANCE.getVisibilityKind();

	}

} //M2tPackage
