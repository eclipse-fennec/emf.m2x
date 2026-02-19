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
package org.eclipse.fennec.m2m.model.ocl;


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
 * @see org.eclipse.fennec.m2m.model.ocl.OclFactory
 * @model kind="package"
 *        annotation="Version value='1.0'"
 * @generated
 */
@ProviderType
@EPackage(uri = OclPackage.eNS_URI, genModel = "/model/ocl.genmodel", genModelSourceLocations = {"model/ocl.genmodel","org.eclipse.fennec.m2m.ocl.model/model/ocl.genmodel"}, ecore = "/model/ocl.ecore", ecoreSourceLocations = "/model/ocl.ecore")
public interface OclPackage extends org.eclipse.emf.ecore.EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "ocl";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/fennec/m2m/ocl/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "ocl";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	OclPackage eINSTANCE = org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.OclTypeImpl <em>Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getOclType()
	 * @generated
	 */
	int OCL_TYPE = 0;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_TYPE__NAME = 0;

	/**
	 * The number of structural features of the '<em>Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_TYPE_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_TYPE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.PrimitiveTypeImpl <em>Primitive Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.PrimitiveTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getPrimitiveType()
	 * @generated
	 */
	int PRIMITIVE_TYPE = 1;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE__NAME = OCL_TYPE__NAME;

	/**
	 * The number of structural features of the '<em>Primitive Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE_FEATURE_COUNT = OCL_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Primitive Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_TYPE_OPERATION_COUNT = OCL_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.AnyTypeImpl <em>Any Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.AnyTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getAnyType()
	 * @generated
	 */
	int ANY_TYPE = 2;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANY_TYPE__NAME = OCL_TYPE__NAME;

	/**
	 * The number of structural features of the '<em>Any Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANY_TYPE_FEATURE_COUNT = OCL_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Any Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ANY_TYPE_OPERATION_COUNT = OCL_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.VoidTypeImpl <em>Void Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.VoidTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getVoidType()
	 * @generated
	 */
	int VOID_TYPE = 3;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOID_TYPE__NAME = OCL_TYPE__NAME;

	/**
	 * The number of structural features of the '<em>Void Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOID_TYPE_FEATURE_COUNT = OCL_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Void Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VOID_TYPE_OPERATION_COUNT = OCL_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.InvalidTypeImpl <em>Invalid Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.InvalidTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getInvalidType()
	 * @generated
	 */
	int INVALID_TYPE = 4;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVALID_TYPE__NAME = OCL_TYPE__NAME;

	/**
	 * The number of structural features of the '<em>Invalid Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVALID_TYPE_FEATURE_COUNT = OCL_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Invalid Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVALID_TYPE_OPERATION_COUNT = OCL_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionTypeImpl <em>Collection Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionType()
	 * @generated
	 */
	int COLLECTION_TYPE = 5;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TYPE__NAME = OCL_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Element Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TYPE__ELEMENT_TYPE = OCL_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TYPE__KIND = OCL_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Collection Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TYPE_FEATURE_COUNT = OCL_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Collection Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TYPE_OPERATION_COUNT = OCL_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.SetTypeImpl <em>Set Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.SetTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getSetType()
	 * @generated
	 */
	int SET_TYPE = 6;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET_TYPE__NAME = COLLECTION_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Element Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET_TYPE__ELEMENT_TYPE = COLLECTION_TYPE__ELEMENT_TYPE;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET_TYPE__KIND = COLLECTION_TYPE__KIND;

	/**
	 * The number of structural features of the '<em>Set Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET_TYPE_FEATURE_COUNT = COLLECTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Set Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SET_TYPE_OPERATION_COUNT = COLLECTION_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.OrderedSetTypeImpl <em>Ordered Set Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OrderedSetTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getOrderedSetType()
	 * @generated
	 */
	int ORDERED_SET_TYPE = 7;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORDERED_SET_TYPE__NAME = COLLECTION_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Element Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORDERED_SET_TYPE__ELEMENT_TYPE = COLLECTION_TYPE__ELEMENT_TYPE;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORDERED_SET_TYPE__KIND = COLLECTION_TYPE__KIND;

	/**
	 * The number of structural features of the '<em>Ordered Set Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORDERED_SET_TYPE_FEATURE_COUNT = COLLECTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Ordered Set Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ORDERED_SET_TYPE_OPERATION_COUNT = COLLECTION_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.BagTypeImpl <em>Bag Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.BagTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getBagType()
	 * @generated
	 */
	int BAG_TYPE = 8;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BAG_TYPE__NAME = COLLECTION_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Element Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BAG_TYPE__ELEMENT_TYPE = COLLECTION_TYPE__ELEMENT_TYPE;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BAG_TYPE__KIND = COLLECTION_TYPE__KIND;

	/**
	 * The number of structural features of the '<em>Bag Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BAG_TYPE_FEATURE_COUNT = COLLECTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Bag Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BAG_TYPE_OPERATION_COUNT = COLLECTION_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.SequenceTypeImpl <em>Sequence Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.SequenceTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getSequenceType()
	 * @generated
	 */
	int SEQUENCE_TYPE = 9;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_TYPE__NAME = COLLECTION_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Element Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_TYPE__ELEMENT_TYPE = COLLECTION_TYPE__ELEMENT_TYPE;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_TYPE__KIND = COLLECTION_TYPE__KIND;

	/**
	 * The number of structural features of the '<em>Sequence Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_TYPE_FEATURE_COUNT = COLLECTION_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Sequence Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int SEQUENCE_TYPE_OPERATION_COUNT = COLLECTION_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MapTypeImpl <em>Map Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.MapTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMapType()
	 * @generated
	 */
	int MAP_TYPE = 10;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_TYPE__NAME = OCL_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Key Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_TYPE__KEY_TYPE = OCL_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Value Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_TYPE__VALUE_TYPE = OCL_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Map Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_TYPE_FEATURE_COUNT = OCL_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Map Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_TYPE_OPERATION_COUNT = OCL_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TupleTypeImpl <em>Tuple Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.TupleTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTupleType()
	 * @generated
	 */
	int TUPLE_TYPE = 11;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_TYPE__NAME = OCL_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Owned Parts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_TYPE__OWNED_PARTS = OCL_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Tuple Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_TYPE_FEATURE_COUNT = OCL_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Tuple Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_TYPE_OPERATION_COUNT = OCL_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MessageTypeImpl <em>Message Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.MessageTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMessageType()
	 * @generated
	 */
	int MESSAGE_TYPE = 12;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_TYPE__NAME = OCL_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Referred Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_TYPE__REFERRED_OPERATION = OCL_TYPE_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Referred Signal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_TYPE__REFERRED_SIGNAL = OCL_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Message Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_TYPE_FEATURE_COUNT = OCL_TYPE_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Message Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_TYPE_OPERATION_COUNT = OCL_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.ClassifierTypeImpl <em>Classifier Type</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.ClassifierTypeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getClassifierType()
	 * @generated
	 */
	int CLASSIFIER_TYPE = 13;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFIER_TYPE__NAME = OCL_TYPE__NAME;

	/**
	 * The feature id for the '<em><b>Referred Classifier</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFIER_TYPE__REFERRED_CLASSIFIER = OCL_TYPE_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Classifier Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFIER_TYPE_FEATURE_COUNT = OCL_TYPE_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Classifier Type</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CLASSIFIER_TYPE_OPERATION_COUNT = OCL_TYPE_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.OclExpressionImpl <em>Expression</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclExpressionImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getOclExpression()
	 * @generated
	 */
	int OCL_EXPRESSION = 14;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_EXPRESSION__TYPE = 0;

	/**
	 * The number of structural features of the '<em>Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_EXPRESSION_FEATURE_COUNT = 1;

	/**
	 * The number of operations of the '<em>Expression</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OCL_EXPRESSION_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CallExpImpl <em>Call Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.CallExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCallExp()
	 * @generated
	 */
	int CALL_EXP = 15;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALL_EXP__TYPE = OCL_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALL_EXP__OWNED_SOURCE = OCL_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALL_EXP__IS_IMPLICIT = OCL_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALL_EXP__IS_SAFE = OCL_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Call Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALL_EXP_FEATURE_COUNT = OCL_EXPRESSION_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Call Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CALL_EXP_OPERATION_COUNT = OCL_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.FeatureCallExpImpl <em>Feature Call Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.FeatureCallExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getFeatureCallExp()
	 * @generated
	 */
	int FEATURE_CALL_EXP = 16;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CALL_EXP__TYPE = CALL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CALL_EXP__OWNED_SOURCE = CALL_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CALL_EXP__IS_IMPLICIT = CALL_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CALL_EXP__IS_SAFE = CALL_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Is Pre</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CALL_EXP__IS_PRE = CALL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Feature Call Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CALL_EXP_FEATURE_COUNT = CALL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Feature Call Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int FEATURE_CALL_EXP_OPERATION_COUNT = CALL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.PropertyCallExpImpl <em>Property Call Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.PropertyCallExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getPropertyCallExp()
	 * @generated
	 */
	int PROPERTY_CALL_EXP = 17;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CALL_EXP__TYPE = FEATURE_CALL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CALL_EXP__OWNED_SOURCE = FEATURE_CALL_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CALL_EXP__IS_IMPLICIT = FEATURE_CALL_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CALL_EXP__IS_SAFE = FEATURE_CALL_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Is Pre</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CALL_EXP__IS_PRE = FEATURE_CALL_EXP__IS_PRE;

	/**
	 * The feature id for the '<em><b>Referred Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CALL_EXP__REFERRED_PROPERTY = FEATURE_CALL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Property Call Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CALL_EXP_FEATURE_COUNT = FEATURE_CALL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Property Call Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_CALL_EXP_OPERATION_COUNT = FEATURE_CALL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.OperationCallExpImpl <em>Operation Call Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OperationCallExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getOperationCallExp()
	 * @generated
	 */
	int OPERATION_CALL_EXP = 18;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP__TYPE = FEATURE_CALL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP__OWNED_SOURCE = FEATURE_CALL_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP__IS_IMPLICIT = FEATURE_CALL_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP__IS_SAFE = FEATURE_CALL_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Is Pre</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP__IS_PRE = FEATURE_CALL_EXP__IS_PRE;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP__NAME = FEATURE_CALL_EXP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Owned Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP__OWNED_ARGUMENTS = FEATURE_CALL_EXP_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Referred Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP__REFERRED_OPERATION = FEATURE_CALL_EXP_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Operation Call Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP_FEATURE_COUNT = FEATURE_CALL_EXP_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Operation Call Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OPERATION_CALL_EXP_OPERATION_COUNT = FEATURE_CALL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.LoopExpImpl <em>Loop Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.LoopExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getLoopExp()
	 * @generated
	 */
	int LOOP_EXP = 19;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_EXP__TYPE = CALL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_EXP__OWNED_SOURCE = CALL_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_EXP__IS_IMPLICIT = CALL_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_EXP__IS_SAFE = CALL_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Owned Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_EXP__OWNED_BODY = CALL_EXP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Owned Iterators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_EXP__OWNED_ITERATORS = CALL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Loop Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_EXP_FEATURE_COUNT = CALL_EXP_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Loop Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LOOP_EXP_OPERATION_COUNT = CALL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.IteratorExpImpl <em>Iterator Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.IteratorExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getIteratorExp()
	 * @generated
	 */
	int ITERATOR_EXP = 20;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATOR_EXP__TYPE = LOOP_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATOR_EXP__OWNED_SOURCE = LOOP_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATOR_EXP__IS_IMPLICIT = LOOP_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATOR_EXP__IS_SAFE = LOOP_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Owned Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATOR_EXP__OWNED_BODY = LOOP_EXP__OWNED_BODY;

	/**
	 * The feature id for the '<em><b>Owned Iterators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATOR_EXP__OWNED_ITERATORS = LOOP_EXP__OWNED_ITERATORS;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATOR_EXP__NAME = LOOP_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Iterator Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATOR_EXP_FEATURE_COUNT = LOOP_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Iterator Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATOR_EXP_OPERATION_COUNT = LOOP_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.IterateExpImpl <em>Iterate Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.IterateExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getIterateExp()
	 * @generated
	 */
	int ITERATE_EXP = 21;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATE_EXP__TYPE = LOOP_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Source</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATE_EXP__OWNED_SOURCE = LOOP_EXP__OWNED_SOURCE;

	/**
	 * The feature id for the '<em><b>Is Implicit</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATE_EXP__IS_IMPLICIT = LOOP_EXP__IS_IMPLICIT;

	/**
	 * The feature id for the '<em><b>Is Safe</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATE_EXP__IS_SAFE = LOOP_EXP__IS_SAFE;

	/**
	 * The feature id for the '<em><b>Owned Body</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATE_EXP__OWNED_BODY = LOOP_EXP__OWNED_BODY;

	/**
	 * The feature id for the '<em><b>Owned Iterators</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATE_EXP__OWNED_ITERATORS = LOOP_EXP__OWNED_ITERATORS;

	/**
	 * The feature id for the '<em><b>Owned Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATE_EXP__OWNED_RESULT = LOOP_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Iterate Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATE_EXP_FEATURE_COUNT = LOOP_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Iterate Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ITERATE_EXP_OPERATION_COUNT = LOOP_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.LiteralExpImpl <em>Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.LiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getLiteralExp()
	 * @generated
	 */
	int LITERAL_EXP = 22;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_EXP__TYPE = OCL_EXPRESSION__TYPE;

	/**
	 * The number of structural features of the '<em>Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_EXP_FEATURE_COUNT = OCL_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LITERAL_EXP_OPERATION_COUNT = OCL_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.PrimitiveLiteralExpImpl <em>Primitive Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.PrimitiveLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getPrimitiveLiteralExp()
	 * @generated
	 */
	int PRIMITIVE_LITERAL_EXP = 23;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_LITERAL_EXP__TYPE = LITERAL_EXP__TYPE;

	/**
	 * The number of structural features of the '<em>Primitive Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_LITERAL_EXP_FEATURE_COUNT = LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Primitive Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PRIMITIVE_LITERAL_EXP_OPERATION_COUNT = LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.IntegerLiteralExpImpl <em>Integer Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.IntegerLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getIntegerLiteralExp()
	 * @generated
	 */
	int INTEGER_LITERAL_EXP = 24;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTEGER_LITERAL_EXP__TYPE = PRIMITIVE_LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Integer Symbol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTEGER_LITERAL_EXP__INTEGER_SYMBOL = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Integer Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTEGER_LITERAL_EXP_FEATURE_COUNT = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Integer Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INTEGER_LITERAL_EXP_OPERATION_COUNT = PRIMITIVE_LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.RealLiteralExpImpl <em>Real Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.RealLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getRealLiteralExp()
	 * @generated
	 */
	int REAL_LITERAL_EXP = 25;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_LITERAL_EXP__TYPE = PRIMITIVE_LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Real Symbol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_LITERAL_EXP__REAL_SYMBOL = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Real Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_LITERAL_EXP_FEATURE_COUNT = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Real Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int REAL_LITERAL_EXP_OPERATION_COUNT = PRIMITIVE_LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.UnlimitedNaturalLiteralExpImpl <em>Unlimited Natural Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.UnlimitedNaturalLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getUnlimitedNaturalLiteralExp()
	 * @generated
	 */
	int UNLIMITED_NATURAL_LITERAL_EXP = 26;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNLIMITED_NATURAL_LITERAL_EXP__TYPE = PRIMITIVE_LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Unlimited Natural Symbol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNLIMITED_NATURAL_LITERAL_EXP__UNLIMITED_NATURAL_SYMBOL = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Unlimited Natural Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNLIMITED_NATURAL_LITERAL_EXP_FEATURE_COUNT = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Unlimited Natural Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int UNLIMITED_NATURAL_LITERAL_EXP_OPERATION_COUNT = PRIMITIVE_LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.StringLiteralExpImpl <em>String Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.StringLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getStringLiteralExp()
	 * @generated
	 */
	int STRING_LITERAL_EXP = 27;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_LITERAL_EXP__TYPE = PRIMITIVE_LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>String Symbol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_LITERAL_EXP__STRING_SYMBOL = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>String Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_LITERAL_EXP_FEATURE_COUNT = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>String Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int STRING_LITERAL_EXP_OPERATION_COUNT = PRIMITIVE_LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.BooleanLiteralExpImpl <em>Boolean Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.BooleanLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getBooleanLiteralExp()
	 * @generated
	 */
	int BOOLEAN_LITERAL_EXP = 28;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOLEAN_LITERAL_EXP__TYPE = PRIMITIVE_LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Boolean Symbol</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOLEAN_LITERAL_EXP__BOOLEAN_SYMBOL = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Boolean Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOLEAN_LITERAL_EXP_FEATURE_COUNT = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Boolean Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int BOOLEAN_LITERAL_EXP_OPERATION_COUNT = PRIMITIVE_LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.NullLiteralExpImpl <em>Null Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.NullLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getNullLiteralExp()
	 * @generated
	 */
	int NULL_LITERAL_EXP = 29;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NULL_LITERAL_EXP__TYPE = PRIMITIVE_LITERAL_EXP__TYPE;

	/**
	 * The number of structural features of the '<em>Null Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NULL_LITERAL_EXP_FEATURE_COUNT = PRIMITIVE_LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Null Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int NULL_LITERAL_EXP_OPERATION_COUNT = PRIMITIVE_LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.InvalidLiteralExpImpl <em>Invalid Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.InvalidLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getInvalidLiteralExp()
	 * @generated
	 */
	int INVALID_LITERAL_EXP = 30;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVALID_LITERAL_EXP__TYPE = LITERAL_EXP__TYPE;

	/**
	 * The number of structural features of the '<em>Invalid Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVALID_LITERAL_EXP_FEATURE_COUNT = LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of operations of the '<em>Invalid Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int INVALID_LITERAL_EXP_OPERATION_COUNT = LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.EnumLiteralExpImpl <em>Enum Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.EnumLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getEnumLiteralExp()
	 * @generated
	 */
	int ENUM_LITERAL_EXP = 31;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_EXP__TYPE = LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Referred Literal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_EXP__REFERRED_LITERAL = LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Enum Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_EXP_FEATURE_COUNT = LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Enum Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int ENUM_LITERAL_EXP_OPERATION_COUNT = LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionLiteralExpImpl <em>Collection Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionLiteralExp()
	 * @generated
	 */
	int COLLECTION_LITERAL_EXP = 32;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_LITERAL_EXP__TYPE = LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_LITERAL_EXP__KIND = LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Owned Parts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_LITERAL_EXP__OWNED_PARTS = LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Collection Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_LITERAL_EXP_FEATURE_COUNT = LITERAL_EXP_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Collection Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_LITERAL_EXP_OPERATION_COUNT = LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TupleLiteralExpImpl <em>Tuple Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.TupleLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTupleLiteralExp()
	 * @generated
	 */
	int TUPLE_LITERAL_EXP = 33;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_LITERAL_EXP__TYPE = LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Parts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_LITERAL_EXP__OWNED_PARTS = LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Tuple Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_LITERAL_EXP_FEATURE_COUNT = LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Tuple Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_LITERAL_EXP_OPERATION_COUNT = LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MapLiteralExpImpl <em>Map Literal Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.MapLiteralExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMapLiteralExp()
	 * @generated
	 */
	int MAP_LITERAL_EXP = 34;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_LITERAL_EXP__TYPE = LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Parts</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_LITERAL_EXP__OWNED_PARTS = LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Map Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_LITERAL_EXP_FEATURE_COUNT = LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Map Literal Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_LITERAL_EXP_OPERATION_COUNT = LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.IfExpImpl <em>If Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.IfExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getIfExp()
	 * @generated
	 */
	int IF_EXP = 35;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_EXP__TYPE = OCL_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Condition</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_EXP__OWNED_CONDITION = OCL_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Owned Then</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_EXP__OWNED_THEN = OCL_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Owned Else</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_EXP__OWNED_ELSE = OCL_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>If Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_EXP_FEATURE_COUNT = OCL_EXPRESSION_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>If Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int IF_EXP_OPERATION_COUNT = OCL_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.LetExpImpl <em>Let Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.LetExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getLetExp()
	 * @generated
	 */
	int LET_EXP = 36;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_EXP__TYPE = OCL_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Variable</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_EXP__OWNED_VARIABLE = OCL_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Owned In</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_EXP__OWNED_IN = OCL_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Let Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_EXP_FEATURE_COUNT = OCL_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Let Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int LET_EXP_OPERATION_COUNT = OCL_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.VariableExpImpl <em>Variable Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.VariableExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getVariableExp()
	 * @generated
	 */
	int VARIABLE_EXP = 37;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_EXP__TYPE = OCL_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Referred Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_EXP__REFERRED_VARIABLE = OCL_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Variable Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_EXP_FEATURE_COUNT = OCL_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Variable Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_EXP_OPERATION_COUNT = OCL_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TypeExpImpl <em>Type Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.TypeExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTypeExp()
	 * @generated
	 */
	int TYPE_EXP = 38;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_EXP__TYPE = OCL_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Referred Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_EXP__REFERRED_TYPE = OCL_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Type Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_EXP_FEATURE_COUNT = OCL_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Type Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TYPE_EXP_OPERATION_COUNT = OCL_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MessageExpImpl <em>Message Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.MessageExpImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMessageExp()
	 * @generated
	 */
	int MESSAGE_EXP = 39;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_EXP__TYPE = OCL_EXPRESSION__TYPE;

	/**
	 * The feature id for the '<em><b>Owned Target</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_EXP__OWNED_TARGET = OCL_EXPRESSION_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Owned Arguments</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_EXP__OWNED_ARGUMENTS = OCL_EXPRESSION_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Owned Called Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_EXP__OWNED_CALLED_OPERATION = OCL_EXPRESSION_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Owned Sent Signal</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_EXP__OWNED_SENT_SIGNAL = OCL_EXPRESSION_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Message Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_EXP_FEATURE_COUNT = OCL_EXPRESSION_FEATURE_COUNT + 4;

	/**
	 * The number of operations of the '<em>Message Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MESSAGE_EXP_OPERATION_COUNT = OCL_EXPRESSION_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.VariableImpl <em>Variable</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.VariableImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getVariable()
	 * @generated
	 */
	int VARIABLE = 40;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Owned Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE__OWNED_INIT = 2;

	/**
	 * The number of structural features of the '<em>Variable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Variable</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int VARIABLE_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.ConstraintImpl <em>Constraint</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.ConstraintImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getConstraint()
	 * @generated
	 */
	int CONSTRAINT = 41;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__NAME = 0;

	/**
	 * The feature id for the '<em><b>Kind</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__KIND = 1;

	/**
	 * The feature id for the '<em><b>Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__SPECIFICATION = 2;

	/**
	 * The feature id for the '<em><b>Context Classifier</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__CONTEXT_CLASSIFIER = 3;

	/**
	 * The feature id for the '<em><b>Context Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__CONTEXT_OPERATION = 4;

	/**
	 * The feature id for the '<em><b>Context Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT__CONTEXT_PROPERTY = 5;

	/**
	 * The number of structural features of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_FEATURE_COUNT = 6;

	/**
	 * The number of operations of the '<em>Constraint</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int CONSTRAINT_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionLiteralPartImpl <em>Collection Literal Part</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionLiteralPartImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionLiteralPart()
	 * @generated
	 */
	int COLLECTION_LITERAL_PART = 42;

	/**
	 * The number of structural features of the '<em>Collection Literal Part</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_LITERAL_PART_FEATURE_COUNT = 0;

	/**
	 * The number of operations of the '<em>Collection Literal Part</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_LITERAL_PART_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionItemImpl <em>Collection Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionItemImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionItem()
	 * @generated
	 */
	int COLLECTION_ITEM = 43;

	/**
	 * The feature id for the '<em><b>Owned Item</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_ITEM__OWNED_ITEM = COLLECTION_LITERAL_PART_FEATURE_COUNT + 0;

	/**
	 * The number of structural features of the '<em>Collection Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_ITEM_FEATURE_COUNT = COLLECTION_LITERAL_PART_FEATURE_COUNT + 1;

	/**
	 * The number of operations of the '<em>Collection Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_ITEM_OPERATION_COUNT = COLLECTION_LITERAL_PART_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionRangeImpl <em>Collection Range</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionRangeImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionRange()
	 * @generated
	 */
	int COLLECTION_RANGE = 44;

	/**
	 * The feature id for the '<em><b>Owned First</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_RANGE__OWNED_FIRST = COLLECTION_LITERAL_PART_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Owned Last</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_RANGE__OWNED_LAST = COLLECTION_LITERAL_PART_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Collection Range</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_RANGE_FEATURE_COUNT = COLLECTION_LITERAL_PART_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Collection Range</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_RANGE_OPERATION_COUNT = COLLECTION_LITERAL_PART_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TupleLiteralPartImpl <em>Tuple Literal Part</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.TupleLiteralPartImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTupleLiteralPart()
	 * @generated
	 */
	int TUPLE_LITERAL_PART = 45;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_LITERAL_PART__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_LITERAL_PART__TYPE = 1;

	/**
	 * The feature id for the '<em><b>Owned Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_LITERAL_PART__OWNED_INIT = 2;

	/**
	 * The number of structural features of the '<em>Tuple Literal Part</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_LITERAL_PART_FEATURE_COUNT = 3;

	/**
	 * The number of operations of the '<em>Tuple Literal Part</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_LITERAL_PART_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MapLiteralPartImpl <em>Map Literal Part</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.MapLiteralPartImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMapLiteralPart()
	 * @generated
	 */
	int MAP_LITERAL_PART = 46;

	/**
	 * The feature id for the '<em><b>Owned Key</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_LITERAL_PART__OWNED_KEY = 0;

	/**
	 * The feature id for the '<em><b>Owned Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_LITERAL_PART__OWNED_VALUE = 1;

	/**
	 * The number of structural features of the '<em>Map Literal Part</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_LITERAL_PART_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Map Literal Part</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int MAP_LITERAL_PART_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TuplePartImpl <em>Tuple Part</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.TuplePartImpl
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTuplePart()
	 * @generated
	 */
	int TUPLE_PART = 47;

	/**
	 * The feature id for the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_PART__NAME = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_PART__TYPE = 1;

	/**
	 * The number of structural features of the '<em>Tuple Part</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_PART_FEATURE_COUNT = 2;

	/**
	 * The number of operations of the '<em>Tuple Part</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TUPLE_PART_OPERATION_COUNT = 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.CollectionKind <em>Collection Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionKind
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionKind()
	 * @generated
	 */
	int COLLECTION_KIND = 48;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2m.model.ocl.ConstraintKind <em>Constraint Kind</em>}' enum.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2m.model.ocl.ConstraintKind
	 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getConstraintKind()
	 * @generated
	 */
	int CONSTRAINT_KIND = 49;


	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.OclType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.OclType
	 * @generated
	 */
	EClass getOclType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.OclType#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.OclType#getName()
	 * @see #getOclType()
	 * @generated
	 */
	EAttribute getOclType_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.PrimitiveType <em>Primitive Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Primitive Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.PrimitiveType
	 * @generated
	 */
	EClass getPrimitiveType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.AnyType <em>Any Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Any Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.AnyType
	 * @generated
	 */
	EClass getAnyType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.VoidType <em>Void Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Void Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.VoidType
	 * @generated
	 */
	EClass getVoidType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.InvalidType <em>Invalid Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Invalid Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.InvalidType
	 * @generated
	 */
	EClass getInvalidType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.CollectionType <em>Collection Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Collection Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionType
	 * @generated
	 */
	EClass getCollectionType();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.CollectionType#getElementType <em>Element Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Element Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionType#getElementType()
	 * @see #getCollectionType()
	 * @generated
	 */
	EReference getCollectionType_ElementType();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.CollectionType#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionType#getKind()
	 * @see #getCollectionType()
	 * @generated
	 */
	EAttribute getCollectionType_Kind();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.SetType <em>Set Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Set Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.SetType
	 * @generated
	 */
	EClass getSetType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.OrderedSetType <em>Ordered Set Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Ordered Set Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.OrderedSetType
	 * @generated
	 */
	EClass getOrderedSetType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.BagType <em>Bag Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Bag Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.BagType
	 * @generated
	 */
	EClass getBagType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.SequenceType <em>Sequence Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Sequence Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.SequenceType
	 * @generated
	 */
	EClass getSequenceType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.MapType <em>Map Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Map Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MapType
	 * @generated
	 */
	EClass getMapType();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.MapType#getKeyType <em>Key Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Key Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MapType#getKeyType()
	 * @see #getMapType()
	 * @generated
	 */
	EReference getMapType_KeyType();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.MapType#getValueType <em>Value Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Value Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MapType#getValueType()
	 * @see #getMapType()
	 * @generated
	 */
	EReference getMapType_ValueType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.TupleType <em>Tuple Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tuple Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TupleType
	 * @generated
	 */
	EClass getTupleType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.ocl.TupleType#getOwnedParts <em>Owned Parts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Parts</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TupleType#getOwnedParts()
	 * @see #getTupleType()
	 * @generated
	 */
	EReference getTupleType_OwnedParts();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.MessageType <em>Message Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Message Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MessageType
	 * @generated
	 */
	EClass getMessageType();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.MessageType#getReferredOperation <em>Referred Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Operation</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MessageType#getReferredOperation()
	 * @see #getMessageType()
	 * @generated
	 */
	EReference getMessageType_ReferredOperation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.MessageType#getReferredSignal <em>Referred Signal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Signal</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MessageType#getReferredSignal()
	 * @see #getMessageType()
	 * @generated
	 */
	EReference getMessageType_ReferredSignal();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.ClassifierType <em>Classifier Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Classifier Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.ClassifierType
	 * @generated
	 */
	EClass getClassifierType();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.ClassifierType#getReferredClassifier <em>Referred Classifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Classifier</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.ClassifierType#getReferredClassifier()
	 * @see #getClassifierType()
	 * @generated
	 */
	EReference getClassifierType_ReferredClassifier();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.OclExpression <em>Expression</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Expression</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.OclExpression
	 * @generated
	 */
	EClass getOclExpression();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.OclExpression#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.OclExpression#getType()
	 * @see #getOclExpression()
	 * @generated
	 */
	EReference getOclExpression_Type();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.CallExp <em>Call Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Call Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CallExp
	 * @generated
	 */
	EClass getCallExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.CallExp#getOwnedSource <em>Owned Source</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Source</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CallExp#getOwnedSource()
	 * @see #getCallExp()
	 * @generated
	 */
	EReference getCallExp_OwnedSource();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.CallExp#isIsImplicit <em>Is Implicit</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Implicit</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CallExp#isIsImplicit()
	 * @see #getCallExp()
	 * @generated
	 */
	EAttribute getCallExp_IsImplicit();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.CallExp#isIsSafe <em>Is Safe</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Safe</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CallExp#isIsSafe()
	 * @see #getCallExp()
	 * @generated
	 */
	EAttribute getCallExp_IsSafe();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.FeatureCallExp <em>Feature Call Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Feature Call Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.FeatureCallExp
	 * @generated
	 */
	EClass getFeatureCallExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.FeatureCallExp#isIsPre <em>Is Pre</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Pre</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.FeatureCallExp#isIsPre()
	 * @see #getFeatureCallExp()
	 * @generated
	 */
	EAttribute getFeatureCallExp_IsPre();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.PropertyCallExp <em>Property Call Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Call Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.PropertyCallExp
	 * @generated
	 */
	EClass getPropertyCallExp();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.PropertyCallExp#getReferredProperty <em>Referred Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Property</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.PropertyCallExp#getReferredProperty()
	 * @see #getPropertyCallExp()
	 * @generated
	 */
	EReference getPropertyCallExp_ReferredProperty();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.OperationCallExp <em>Operation Call Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Operation Call Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.OperationCallExp
	 * @generated
	 */
	EClass getOperationCallExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.OperationCallExp#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.OperationCallExp#getName()
	 * @see #getOperationCallExp()
	 * @generated
	 */
	EAttribute getOperationCallExp_Name();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.ocl.OperationCallExp#getOwnedArguments <em>Owned Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Arguments</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.OperationCallExp#getOwnedArguments()
	 * @see #getOperationCallExp()
	 * @generated
	 */
	EReference getOperationCallExp_OwnedArguments();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.OperationCallExp#getReferredOperation <em>Referred Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Operation</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.OperationCallExp#getReferredOperation()
	 * @see #getOperationCallExp()
	 * @generated
	 */
	EReference getOperationCallExp_ReferredOperation();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.LoopExp <em>Loop Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Loop Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.LoopExp
	 * @generated
	 */
	EClass getLoopExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.LoopExp#getOwnedBody <em>Owned Body</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Body</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.LoopExp#getOwnedBody()
	 * @see #getLoopExp()
	 * @generated
	 */
	EReference getLoopExp_OwnedBody();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.ocl.LoopExp#getOwnedIterators <em>Owned Iterators</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Iterators</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.LoopExp#getOwnedIterators()
	 * @see #getLoopExp()
	 * @generated
	 */
	EReference getLoopExp_OwnedIterators();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.IteratorExp <em>Iterator Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Iterator Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IteratorExp
	 * @generated
	 */
	EClass getIteratorExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.IteratorExp#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IteratorExp#getName()
	 * @see #getIteratorExp()
	 * @generated
	 */
	EAttribute getIteratorExp_Name();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.IterateExp <em>Iterate Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Iterate Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IterateExp
	 * @generated
	 */
	EClass getIterateExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.IterateExp#getOwnedResult <em>Owned Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Result</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IterateExp#getOwnedResult()
	 * @see #getIterateExp()
	 * @generated
	 */
	EReference getIterateExp_OwnedResult();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.LiteralExp <em>Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.LiteralExp
	 * @generated
	 */
	EClass getLiteralExp();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.PrimitiveLiteralExp <em>Primitive Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Primitive Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.PrimitiveLiteralExp
	 * @generated
	 */
	EClass getPrimitiveLiteralExp();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.IntegerLiteralExp <em>Integer Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Integer Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IntegerLiteralExp
	 * @generated
	 */
	EClass getIntegerLiteralExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.IntegerLiteralExp#getIntegerSymbol <em>Integer Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Integer Symbol</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IntegerLiteralExp#getIntegerSymbol()
	 * @see #getIntegerLiteralExp()
	 * @generated
	 */
	EAttribute getIntegerLiteralExp_IntegerSymbol();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.RealLiteralExp <em>Real Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Real Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.RealLiteralExp
	 * @generated
	 */
	EClass getRealLiteralExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.RealLiteralExp#getRealSymbol <em>Real Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Real Symbol</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.RealLiteralExp#getRealSymbol()
	 * @see #getRealLiteralExp()
	 * @generated
	 */
	EAttribute getRealLiteralExp_RealSymbol();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp <em>Unlimited Natural Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Unlimited Natural Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp
	 * @generated
	 */
	EClass getUnlimitedNaturalLiteralExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp#getUnlimitedNaturalSymbol <em>Unlimited Natural Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Unlimited Natural Symbol</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.UnlimitedNaturalLiteralExp#getUnlimitedNaturalSymbol()
	 * @see #getUnlimitedNaturalLiteralExp()
	 * @generated
	 */
	EAttribute getUnlimitedNaturalLiteralExp_UnlimitedNaturalSymbol();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.StringLiteralExp <em>String Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>String Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.StringLiteralExp
	 * @generated
	 */
	EClass getStringLiteralExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.StringLiteralExp#getStringSymbol <em>String Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>String Symbol</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.StringLiteralExp#getStringSymbol()
	 * @see #getStringLiteralExp()
	 * @generated
	 */
	EAttribute getStringLiteralExp_StringSymbol();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.BooleanLiteralExp <em>Boolean Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Boolean Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.BooleanLiteralExp
	 * @generated
	 */
	EClass getBooleanLiteralExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.BooleanLiteralExp#isBooleanSymbol <em>Boolean Symbol</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Boolean Symbol</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.BooleanLiteralExp#isBooleanSymbol()
	 * @see #getBooleanLiteralExp()
	 * @generated
	 */
	EAttribute getBooleanLiteralExp_BooleanSymbol();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.NullLiteralExp <em>Null Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Null Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.NullLiteralExp
	 * @generated
	 */
	EClass getNullLiteralExp();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.InvalidLiteralExp <em>Invalid Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Invalid Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.InvalidLiteralExp
	 * @generated
	 */
	EClass getInvalidLiteralExp();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.EnumLiteralExp <em>Enum Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Enum Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.EnumLiteralExp
	 * @generated
	 */
	EClass getEnumLiteralExp();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.EnumLiteralExp#getReferredLiteral <em>Referred Literal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Literal</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.EnumLiteralExp#getReferredLiteral()
	 * @see #getEnumLiteralExp()
	 * @generated
	 */
	EReference getEnumLiteralExp_ReferredLiteral();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.CollectionLiteralExp <em>Collection Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Collection Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionLiteralExp
	 * @generated
	 */
	EClass getCollectionLiteralExp();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.CollectionLiteralExp#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionLiteralExp#getKind()
	 * @see #getCollectionLiteralExp()
	 * @generated
	 */
	EAttribute getCollectionLiteralExp_Kind();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.ocl.CollectionLiteralExp#getOwnedParts <em>Owned Parts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Parts</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionLiteralExp#getOwnedParts()
	 * @see #getCollectionLiteralExp()
	 * @generated
	 */
	EReference getCollectionLiteralExp_OwnedParts();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.TupleLiteralExp <em>Tuple Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tuple Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TupleLiteralExp
	 * @generated
	 */
	EClass getTupleLiteralExp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.ocl.TupleLiteralExp#getOwnedParts <em>Owned Parts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Parts</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TupleLiteralExp#getOwnedParts()
	 * @see #getTupleLiteralExp()
	 * @generated
	 */
	EReference getTupleLiteralExp_OwnedParts();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.MapLiteralExp <em>Map Literal Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Map Literal Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MapLiteralExp
	 * @generated
	 */
	EClass getMapLiteralExp();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.ocl.MapLiteralExp#getOwnedParts <em>Owned Parts</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Parts</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MapLiteralExp#getOwnedParts()
	 * @see #getMapLiteralExp()
	 * @generated
	 */
	EReference getMapLiteralExp_OwnedParts();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.IfExp <em>If Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>If Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IfExp
	 * @generated
	 */
	EClass getIfExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedCondition <em>Owned Condition</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Condition</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedCondition()
	 * @see #getIfExp()
	 * @generated
	 */
	EReference getIfExp_OwnedCondition();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedThen <em>Owned Then</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Then</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedThen()
	 * @see #getIfExp()
	 * @generated
	 */
	EReference getIfExp_OwnedThen();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedElse <em>Owned Else</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Else</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.IfExp#getOwnedElse()
	 * @see #getIfExp()
	 * @generated
	 */
	EReference getIfExp_OwnedElse();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.LetExp <em>Let Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Let Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.LetExp
	 * @generated
	 */
	EClass getLetExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.LetExp#getOwnedVariable <em>Owned Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Variable</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.LetExp#getOwnedVariable()
	 * @see #getLetExp()
	 * @generated
	 */
	EReference getLetExp_OwnedVariable();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.LetExp#getOwnedIn <em>Owned In</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned In</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.LetExp#getOwnedIn()
	 * @see #getLetExp()
	 * @generated
	 */
	EReference getLetExp_OwnedIn();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.VariableExp <em>Variable Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.VariableExp
	 * @generated
	 */
	EClass getVariableExp();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.VariableExp#getReferredVariable <em>Referred Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Variable</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.VariableExp#getReferredVariable()
	 * @see #getVariableExp()
	 * @generated
	 */
	EReference getVariableExp_ReferredVariable();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.TypeExp <em>Type Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Type Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TypeExp
	 * @generated
	 */
	EClass getTypeExp();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.TypeExp#getReferredType <em>Referred Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TypeExp#getReferredType()
	 * @see #getTypeExp()
	 * @generated
	 */
	EReference getTypeExp_ReferredType();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.MessageExp <em>Message Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Message Exp</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MessageExp
	 * @generated
	 */
	EClass getMessageExp();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedTarget <em>Owned Target</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Target</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedTarget()
	 * @see #getMessageExp()
	 * @generated
	 */
	EReference getMessageExp_OwnedTarget();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedArguments <em>Owned Arguments</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Owned Arguments</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedArguments()
	 * @see #getMessageExp()
	 * @generated
	 */
	EReference getMessageExp_OwnedArguments();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedCalledOperation <em>Owned Called Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Owned Called Operation</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedCalledOperation()
	 * @see #getMessageExp()
	 * @generated
	 */
	EReference getMessageExp_OwnedCalledOperation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedSentSignal <em>Owned Sent Signal</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Owned Sent Signal</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MessageExp#getOwnedSentSignal()
	 * @see #getMessageExp()
	 * @generated
	 */
	EReference getMessageExp_OwnedSentSignal();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.Variable <em>Variable</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Variable</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Variable
	 * @generated
	 */
	EClass getVariable();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.Variable#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Variable#getName()
	 * @see #getVariable()
	 * @generated
	 */
	EAttribute getVariable_Name();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.Variable#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Variable#getType()
	 * @see #getVariable()
	 * @generated
	 */
	EReference getVariable_Type();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.Variable#getOwnedInit <em>Owned Init</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Init</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Variable#getOwnedInit()
	 * @see #getVariable()
	 * @generated
	 */
	EReference getVariable_OwnedInit();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.Constraint <em>Constraint</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Constraint</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Constraint
	 * @generated
	 */
	EClass getConstraint();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Constraint#getName()
	 * @see #getConstraint()
	 * @generated
	 */
	EAttribute getConstraint_Name();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getKind <em>Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Kind</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Constraint#getKind()
	 * @see #getConstraint()
	 * @generated
	 */
	EAttribute getConstraint_Kind();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getSpecification <em>Specification</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Specification</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Constraint#getSpecification()
	 * @see #getConstraint()
	 * @generated
	 */
	EReference getConstraint_Specification();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getContextClassifier <em>Context Classifier</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Context Classifier</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Constraint#getContextClassifier()
	 * @see #getConstraint()
	 * @generated
	 */
	EReference getConstraint_ContextClassifier();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getContextOperation <em>Context Operation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Context Operation</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Constraint#getContextOperation()
	 * @see #getConstraint()
	 * @generated
	 */
	EReference getConstraint_ContextOperation();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getContextProperty <em>Context Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Context Property</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.Constraint#getContextProperty()
	 * @see #getConstraint()
	 * @generated
	 */
	EReference getConstraint_ContextProperty();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.CollectionLiteralPart <em>Collection Literal Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Collection Literal Part</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionLiteralPart
	 * @generated
	 */
	EClass getCollectionLiteralPart();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.CollectionItem <em>Collection Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Collection Item</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionItem
	 * @generated
	 */
	EClass getCollectionItem();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.CollectionItem#getOwnedItem <em>Owned Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Item</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionItem#getOwnedItem()
	 * @see #getCollectionItem()
	 * @generated
	 */
	EReference getCollectionItem_OwnedItem();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.CollectionRange <em>Collection Range</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Collection Range</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionRange
	 * @generated
	 */
	EClass getCollectionRange();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.CollectionRange#getOwnedFirst <em>Owned First</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned First</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionRange#getOwnedFirst()
	 * @see #getCollectionRange()
	 * @generated
	 */
	EReference getCollectionRange_OwnedFirst();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.CollectionRange#getOwnedLast <em>Owned Last</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Last</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionRange#getOwnedLast()
	 * @see #getCollectionRange()
	 * @generated
	 */
	EReference getCollectionRange_OwnedLast();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart <em>Tuple Literal Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tuple Literal Part</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart
	 * @generated
	 */
	EClass getTupleLiteralPart();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart#getName()
	 * @see #getTupleLiteralPart()
	 * @generated
	 */
	EAttribute getTupleLiteralPart_Name();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart#getType()
	 * @see #getTupleLiteralPart()
	 * @generated
	 */
	EReference getTupleLiteralPart_Type();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart#getOwnedInit <em>Owned Init</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Init</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TupleLiteralPart#getOwnedInit()
	 * @see #getTupleLiteralPart()
	 * @generated
	 */
	EReference getTupleLiteralPart_OwnedInit();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.MapLiteralPart <em>Map Literal Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Map Literal Part</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MapLiteralPart
	 * @generated
	 */
	EClass getMapLiteralPart();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.MapLiteralPart#getOwnedKey <em>Owned Key</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Key</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MapLiteralPart#getOwnedKey()
	 * @see #getMapLiteralPart()
	 * @generated
	 */
	EReference getMapLiteralPart_OwnedKey();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2m.model.ocl.MapLiteralPart#getOwnedValue <em>Owned Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Owned Value</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.MapLiteralPart#getOwnedValue()
	 * @see #getMapLiteralPart()
	 * @generated
	 */
	EReference getMapLiteralPart_OwnedValue();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2m.model.ocl.TuplePart <em>Tuple Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Tuple Part</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TuplePart
	 * @generated
	 */
	EClass getTuplePart();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2m.model.ocl.TuplePart#getName <em>Name</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Name</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TuplePart#getName()
	 * @see #getTuplePart()
	 * @generated
	 */
	EAttribute getTuplePart_Name();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2m.model.ocl.TuplePart#getType <em>Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Type</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.TuplePart#getType()
	 * @see #getTuplePart()
	 * @generated
	 */
	EReference getTuplePart_Type();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.m2m.model.ocl.CollectionKind <em>Collection Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Collection Kind</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.CollectionKind
	 * @generated
	 */
	EEnum getCollectionKind();

	/**
	 * Returns the meta object for enum '{@link org.eclipse.fennec.m2m.model.ocl.ConstraintKind <em>Constraint Kind</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for enum '<em>Constraint Kind</em>'.
	 * @see org.eclipse.fennec.m2m.model.ocl.ConstraintKind
	 * @generated
	 */
	EEnum getConstraintKind();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	OclFactory getOclFactory();

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
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.OclTypeImpl <em>Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getOclType()
		 * @generated
		 */
		EClass OCL_TYPE = eINSTANCE.getOclType();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OCL_TYPE__NAME = eINSTANCE.getOclType_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.PrimitiveTypeImpl <em>Primitive Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.PrimitiveTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getPrimitiveType()
		 * @generated
		 */
		EClass PRIMITIVE_TYPE = eINSTANCE.getPrimitiveType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.AnyTypeImpl <em>Any Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.AnyTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getAnyType()
		 * @generated
		 */
		EClass ANY_TYPE = eINSTANCE.getAnyType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.VoidTypeImpl <em>Void Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.VoidTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getVoidType()
		 * @generated
		 */
		EClass VOID_TYPE = eINSTANCE.getVoidType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.InvalidTypeImpl <em>Invalid Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.InvalidTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getInvalidType()
		 * @generated
		 */
		EClass INVALID_TYPE = eINSTANCE.getInvalidType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionTypeImpl <em>Collection Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionType()
		 * @generated
		 */
		EClass COLLECTION_TYPE = eINSTANCE.getCollectionType();

		/**
		 * The meta object literal for the '<em><b>Element Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLLECTION_TYPE__ELEMENT_TYPE = eINSTANCE.getCollectionType_ElementType();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLLECTION_TYPE__KIND = eINSTANCE.getCollectionType_Kind();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.SetTypeImpl <em>Set Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.SetTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getSetType()
		 * @generated
		 */
		EClass SET_TYPE = eINSTANCE.getSetType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.OrderedSetTypeImpl <em>Ordered Set Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OrderedSetTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getOrderedSetType()
		 * @generated
		 */
		EClass ORDERED_SET_TYPE = eINSTANCE.getOrderedSetType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.BagTypeImpl <em>Bag Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.BagTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getBagType()
		 * @generated
		 */
		EClass BAG_TYPE = eINSTANCE.getBagType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.SequenceTypeImpl <em>Sequence Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.SequenceTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getSequenceType()
		 * @generated
		 */
		EClass SEQUENCE_TYPE = eINSTANCE.getSequenceType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MapTypeImpl <em>Map Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.MapTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMapType()
		 * @generated
		 */
		EClass MAP_TYPE = eINSTANCE.getMapType();

		/**
		 * The meta object literal for the '<em><b>Key Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAP_TYPE__KEY_TYPE = eINSTANCE.getMapType_KeyType();

		/**
		 * The meta object literal for the '<em><b>Value Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAP_TYPE__VALUE_TYPE = eINSTANCE.getMapType_ValueType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TupleTypeImpl <em>Tuple Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.TupleTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTupleType()
		 * @generated
		 */
		EClass TUPLE_TYPE = eINSTANCE.getTupleType();

		/**
		 * The meta object literal for the '<em><b>Owned Parts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TUPLE_TYPE__OWNED_PARTS = eINSTANCE.getTupleType_OwnedParts();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MessageTypeImpl <em>Message Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.MessageTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMessageType()
		 * @generated
		 */
		EClass MESSAGE_TYPE = eINSTANCE.getMessageType();

		/**
		 * The meta object literal for the '<em><b>Referred Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSAGE_TYPE__REFERRED_OPERATION = eINSTANCE.getMessageType_ReferredOperation();

		/**
		 * The meta object literal for the '<em><b>Referred Signal</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSAGE_TYPE__REFERRED_SIGNAL = eINSTANCE.getMessageType_ReferredSignal();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.ClassifierTypeImpl <em>Classifier Type</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.ClassifierTypeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getClassifierType()
		 * @generated
		 */
		EClass CLASSIFIER_TYPE = eINSTANCE.getClassifierType();

		/**
		 * The meta object literal for the '<em><b>Referred Classifier</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CLASSIFIER_TYPE__REFERRED_CLASSIFIER = eINSTANCE.getClassifierType_ReferredClassifier();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.OclExpressionImpl <em>Expression</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclExpressionImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getOclExpression()
		 * @generated
		 */
		EClass OCL_EXPRESSION = eINSTANCE.getOclExpression();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OCL_EXPRESSION__TYPE = eINSTANCE.getOclExpression_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CallExpImpl <em>Call Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.CallExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCallExp()
		 * @generated
		 */
		EClass CALL_EXP = eINSTANCE.getCallExp();

		/**
		 * The meta object literal for the '<em><b>Owned Source</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CALL_EXP__OWNED_SOURCE = eINSTANCE.getCallExp_OwnedSource();

		/**
		 * The meta object literal for the '<em><b>Is Implicit</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CALL_EXP__IS_IMPLICIT = eINSTANCE.getCallExp_IsImplicit();

		/**
		 * The meta object literal for the '<em><b>Is Safe</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CALL_EXP__IS_SAFE = eINSTANCE.getCallExp_IsSafe();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.FeatureCallExpImpl <em>Feature Call Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.FeatureCallExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getFeatureCallExp()
		 * @generated
		 */
		EClass FEATURE_CALL_EXP = eINSTANCE.getFeatureCallExp();

		/**
		 * The meta object literal for the '<em><b>Is Pre</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute FEATURE_CALL_EXP__IS_PRE = eINSTANCE.getFeatureCallExp_IsPre();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.PropertyCallExpImpl <em>Property Call Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.PropertyCallExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getPropertyCallExp()
		 * @generated
		 */
		EClass PROPERTY_CALL_EXP = eINSTANCE.getPropertyCallExp();

		/**
		 * The meta object literal for the '<em><b>Referred Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY_CALL_EXP__REFERRED_PROPERTY = eINSTANCE.getPropertyCallExp_ReferredProperty();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.OperationCallExpImpl <em>Operation Call Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OperationCallExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getOperationCallExp()
		 * @generated
		 */
		EClass OPERATION_CALL_EXP = eINSTANCE.getOperationCallExp();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute OPERATION_CALL_EXP__NAME = eINSTANCE.getOperationCallExp_Name();

		/**
		 * The meta object literal for the '<em><b>Owned Arguments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_CALL_EXP__OWNED_ARGUMENTS = eINSTANCE.getOperationCallExp_OwnedArguments();

		/**
		 * The meta object literal for the '<em><b>Referred Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OPERATION_CALL_EXP__REFERRED_OPERATION = eINSTANCE.getOperationCallExp_ReferredOperation();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.LoopExpImpl <em>Loop Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.LoopExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getLoopExp()
		 * @generated
		 */
		EClass LOOP_EXP = eINSTANCE.getLoopExp();

		/**
		 * The meta object literal for the '<em><b>Owned Body</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LOOP_EXP__OWNED_BODY = eINSTANCE.getLoopExp_OwnedBody();

		/**
		 * The meta object literal for the '<em><b>Owned Iterators</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LOOP_EXP__OWNED_ITERATORS = eINSTANCE.getLoopExp_OwnedIterators();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.IteratorExpImpl <em>Iterator Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.IteratorExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getIteratorExp()
		 * @generated
		 */
		EClass ITERATOR_EXP = eINSTANCE.getIteratorExp();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute ITERATOR_EXP__NAME = eINSTANCE.getIteratorExp_Name();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.IterateExpImpl <em>Iterate Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.IterateExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getIterateExp()
		 * @generated
		 */
		EClass ITERATE_EXP = eINSTANCE.getIterateExp();

		/**
		 * The meta object literal for the '<em><b>Owned Result</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ITERATE_EXP__OWNED_RESULT = eINSTANCE.getIterateExp_OwnedResult();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.LiteralExpImpl <em>Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.LiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getLiteralExp()
		 * @generated
		 */
		EClass LITERAL_EXP = eINSTANCE.getLiteralExp();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.PrimitiveLiteralExpImpl <em>Primitive Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.PrimitiveLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getPrimitiveLiteralExp()
		 * @generated
		 */
		EClass PRIMITIVE_LITERAL_EXP = eINSTANCE.getPrimitiveLiteralExp();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.IntegerLiteralExpImpl <em>Integer Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.IntegerLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getIntegerLiteralExp()
		 * @generated
		 */
		EClass INTEGER_LITERAL_EXP = eINSTANCE.getIntegerLiteralExp();

		/**
		 * The meta object literal for the '<em><b>Integer Symbol</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute INTEGER_LITERAL_EXP__INTEGER_SYMBOL = eINSTANCE.getIntegerLiteralExp_IntegerSymbol();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.RealLiteralExpImpl <em>Real Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.RealLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getRealLiteralExp()
		 * @generated
		 */
		EClass REAL_LITERAL_EXP = eINSTANCE.getRealLiteralExp();

		/**
		 * The meta object literal for the '<em><b>Real Symbol</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute REAL_LITERAL_EXP__REAL_SYMBOL = eINSTANCE.getRealLiteralExp_RealSymbol();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.UnlimitedNaturalLiteralExpImpl <em>Unlimited Natural Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.UnlimitedNaturalLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getUnlimitedNaturalLiteralExp()
		 * @generated
		 */
		EClass UNLIMITED_NATURAL_LITERAL_EXP = eINSTANCE.getUnlimitedNaturalLiteralExp();

		/**
		 * The meta object literal for the '<em><b>Unlimited Natural Symbol</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute UNLIMITED_NATURAL_LITERAL_EXP__UNLIMITED_NATURAL_SYMBOL = eINSTANCE.getUnlimitedNaturalLiteralExp_UnlimitedNaturalSymbol();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.StringLiteralExpImpl <em>String Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.StringLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getStringLiteralExp()
		 * @generated
		 */
		EClass STRING_LITERAL_EXP = eINSTANCE.getStringLiteralExp();

		/**
		 * The meta object literal for the '<em><b>String Symbol</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute STRING_LITERAL_EXP__STRING_SYMBOL = eINSTANCE.getStringLiteralExp_StringSymbol();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.BooleanLiteralExpImpl <em>Boolean Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.BooleanLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getBooleanLiteralExp()
		 * @generated
		 */
		EClass BOOLEAN_LITERAL_EXP = eINSTANCE.getBooleanLiteralExp();

		/**
		 * The meta object literal for the '<em><b>Boolean Symbol</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute BOOLEAN_LITERAL_EXP__BOOLEAN_SYMBOL = eINSTANCE.getBooleanLiteralExp_BooleanSymbol();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.NullLiteralExpImpl <em>Null Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.NullLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getNullLiteralExp()
		 * @generated
		 */
		EClass NULL_LITERAL_EXP = eINSTANCE.getNullLiteralExp();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.InvalidLiteralExpImpl <em>Invalid Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.InvalidLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getInvalidLiteralExp()
		 * @generated
		 */
		EClass INVALID_LITERAL_EXP = eINSTANCE.getInvalidLiteralExp();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.EnumLiteralExpImpl <em>Enum Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.EnumLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getEnumLiteralExp()
		 * @generated
		 */
		EClass ENUM_LITERAL_EXP = eINSTANCE.getEnumLiteralExp();

		/**
		 * The meta object literal for the '<em><b>Referred Literal</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference ENUM_LITERAL_EXP__REFERRED_LITERAL = eINSTANCE.getEnumLiteralExp_ReferredLiteral();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionLiteralExpImpl <em>Collection Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionLiteralExp()
		 * @generated
		 */
		EClass COLLECTION_LITERAL_EXP = eINSTANCE.getCollectionLiteralExp();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute COLLECTION_LITERAL_EXP__KIND = eINSTANCE.getCollectionLiteralExp_Kind();

		/**
		 * The meta object literal for the '<em><b>Owned Parts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLLECTION_LITERAL_EXP__OWNED_PARTS = eINSTANCE.getCollectionLiteralExp_OwnedParts();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TupleLiteralExpImpl <em>Tuple Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.TupleLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTupleLiteralExp()
		 * @generated
		 */
		EClass TUPLE_LITERAL_EXP = eINSTANCE.getTupleLiteralExp();

		/**
		 * The meta object literal for the '<em><b>Owned Parts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TUPLE_LITERAL_EXP__OWNED_PARTS = eINSTANCE.getTupleLiteralExp_OwnedParts();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MapLiteralExpImpl <em>Map Literal Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.MapLiteralExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMapLiteralExp()
		 * @generated
		 */
		EClass MAP_LITERAL_EXP = eINSTANCE.getMapLiteralExp();

		/**
		 * The meta object literal for the '<em><b>Owned Parts</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAP_LITERAL_EXP__OWNED_PARTS = eINSTANCE.getMapLiteralExp_OwnedParts();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.IfExpImpl <em>If Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.IfExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getIfExp()
		 * @generated
		 */
		EClass IF_EXP = eINSTANCE.getIfExp();

		/**
		 * The meta object literal for the '<em><b>Owned Condition</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IF_EXP__OWNED_CONDITION = eINSTANCE.getIfExp_OwnedCondition();

		/**
		 * The meta object literal for the '<em><b>Owned Then</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IF_EXP__OWNED_THEN = eINSTANCE.getIfExp_OwnedThen();

		/**
		 * The meta object literal for the '<em><b>Owned Else</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference IF_EXP__OWNED_ELSE = eINSTANCE.getIfExp_OwnedElse();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.LetExpImpl <em>Let Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.LetExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getLetExp()
		 * @generated
		 */
		EClass LET_EXP = eINSTANCE.getLetExp();

		/**
		 * The meta object literal for the '<em><b>Owned Variable</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LET_EXP__OWNED_VARIABLE = eINSTANCE.getLetExp_OwnedVariable();

		/**
		 * The meta object literal for the '<em><b>Owned In</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference LET_EXP__OWNED_IN = eINSTANCE.getLetExp_OwnedIn();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.VariableExpImpl <em>Variable Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.VariableExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getVariableExp()
		 * @generated
		 */
		EClass VARIABLE_EXP = eINSTANCE.getVariableExp();

		/**
		 * The meta object literal for the '<em><b>Referred Variable</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VARIABLE_EXP__REFERRED_VARIABLE = eINSTANCE.getVariableExp_ReferredVariable();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TypeExpImpl <em>Type Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.TypeExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTypeExp()
		 * @generated
		 */
		EClass TYPE_EXP = eINSTANCE.getTypeExp();

		/**
		 * The meta object literal for the '<em><b>Referred Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TYPE_EXP__REFERRED_TYPE = eINSTANCE.getTypeExp_ReferredType();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MessageExpImpl <em>Message Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.MessageExpImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMessageExp()
		 * @generated
		 */
		EClass MESSAGE_EXP = eINSTANCE.getMessageExp();

		/**
		 * The meta object literal for the '<em><b>Owned Target</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSAGE_EXP__OWNED_TARGET = eINSTANCE.getMessageExp_OwnedTarget();

		/**
		 * The meta object literal for the '<em><b>Owned Arguments</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSAGE_EXP__OWNED_ARGUMENTS = eINSTANCE.getMessageExp_OwnedArguments();

		/**
		 * The meta object literal for the '<em><b>Owned Called Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSAGE_EXP__OWNED_CALLED_OPERATION = eINSTANCE.getMessageExp_OwnedCalledOperation();

		/**
		 * The meta object literal for the '<em><b>Owned Sent Signal</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MESSAGE_EXP__OWNED_SENT_SIGNAL = eINSTANCE.getMessageExp_OwnedSentSignal();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.VariableImpl <em>Variable</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.VariableImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getVariable()
		 * @generated
		 */
		EClass VARIABLE = eINSTANCE.getVariable();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute VARIABLE__NAME = eINSTANCE.getVariable_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VARIABLE__TYPE = eINSTANCE.getVariable_Type();

		/**
		 * The meta object literal for the '<em><b>Owned Init</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference VARIABLE__OWNED_INIT = eINSTANCE.getVariable_OwnedInit();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.ConstraintImpl <em>Constraint</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.ConstraintImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getConstraint()
		 * @generated
		 */
		EClass CONSTRAINT = eINSTANCE.getConstraint();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT__NAME = eINSTANCE.getConstraint_Name();

		/**
		 * The meta object literal for the '<em><b>Kind</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute CONSTRAINT__KIND = eINSTANCE.getConstraint_Kind();

		/**
		 * The meta object literal for the '<em><b>Specification</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT__SPECIFICATION = eINSTANCE.getConstraint_Specification();

		/**
		 * The meta object literal for the '<em><b>Context Classifier</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT__CONTEXT_CLASSIFIER = eINSTANCE.getConstraint_ContextClassifier();

		/**
		 * The meta object literal for the '<em><b>Context Operation</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT__CONTEXT_OPERATION = eINSTANCE.getConstraint_ContextOperation();

		/**
		 * The meta object literal for the '<em><b>Context Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference CONSTRAINT__CONTEXT_PROPERTY = eINSTANCE.getConstraint_ContextProperty();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionLiteralPartImpl <em>Collection Literal Part</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionLiteralPartImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionLiteralPart()
		 * @generated
		 */
		EClass COLLECTION_LITERAL_PART = eINSTANCE.getCollectionLiteralPart();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionItemImpl <em>Collection Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionItemImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionItem()
		 * @generated
		 */
		EClass COLLECTION_ITEM = eINSTANCE.getCollectionItem();

		/**
		 * The meta object literal for the '<em><b>Owned Item</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLLECTION_ITEM__OWNED_ITEM = eINSTANCE.getCollectionItem_OwnedItem();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.CollectionRangeImpl <em>Collection Range</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.CollectionRangeImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionRange()
		 * @generated
		 */
		EClass COLLECTION_RANGE = eINSTANCE.getCollectionRange();

		/**
		 * The meta object literal for the '<em><b>Owned First</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLLECTION_RANGE__OWNED_FIRST = eINSTANCE.getCollectionRange_OwnedFirst();

		/**
		 * The meta object literal for the '<em><b>Owned Last</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLLECTION_RANGE__OWNED_LAST = eINSTANCE.getCollectionRange_OwnedLast();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TupleLiteralPartImpl <em>Tuple Literal Part</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.TupleLiteralPartImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTupleLiteralPart()
		 * @generated
		 */
		EClass TUPLE_LITERAL_PART = eINSTANCE.getTupleLiteralPart();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TUPLE_LITERAL_PART__NAME = eINSTANCE.getTupleLiteralPart_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TUPLE_LITERAL_PART__TYPE = eINSTANCE.getTupleLiteralPart_Type();

		/**
		 * The meta object literal for the '<em><b>Owned Init</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TUPLE_LITERAL_PART__OWNED_INIT = eINSTANCE.getTupleLiteralPart_OwnedInit();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.MapLiteralPartImpl <em>Map Literal Part</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.MapLiteralPartImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getMapLiteralPart()
		 * @generated
		 */
		EClass MAP_LITERAL_PART = eINSTANCE.getMapLiteralPart();

		/**
		 * The meta object literal for the '<em><b>Owned Key</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAP_LITERAL_PART__OWNED_KEY = eINSTANCE.getMapLiteralPart_OwnedKey();

		/**
		 * The meta object literal for the '<em><b>Owned Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference MAP_LITERAL_PART__OWNED_VALUE = eINSTANCE.getMapLiteralPart_OwnedValue();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.impl.TuplePartImpl <em>Tuple Part</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.TuplePartImpl
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getTuplePart()
		 * @generated
		 */
		EClass TUPLE_PART = eINSTANCE.getTuplePart();

		/**
		 * The meta object literal for the '<em><b>Name</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute TUPLE_PART__NAME = eINSTANCE.getTuplePart_Name();

		/**
		 * The meta object literal for the '<em><b>Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TUPLE_PART__TYPE = eINSTANCE.getTuplePart_Type();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.CollectionKind <em>Collection Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.CollectionKind
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getCollectionKind()
		 * @generated
		 */
		EEnum COLLECTION_KIND = eINSTANCE.getCollectionKind();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2m.model.ocl.ConstraintKind <em>Constraint Kind</em>}' enum.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2m.model.ocl.ConstraintKind
		 * @see org.eclipse.fennec.m2m.model.ocl.impl.OclPackageImpl#getConstraintKind()
		 * @generated
		 */
		EEnum CONSTRAINT_KIND = eINSTANCE.getConstraintKind();

	}

} //OclPackage
