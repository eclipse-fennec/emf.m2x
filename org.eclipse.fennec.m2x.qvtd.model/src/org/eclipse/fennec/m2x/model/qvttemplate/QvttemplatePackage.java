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
package org.eclipse.fennec.m2x.model.qvttemplate;


import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
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
 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplateFactory
 * @model kind="package"
 *        annotation="Version value='1.0'"
 * @generated
 */
@ProviderType
@EPackage(uri = QvttemplatePackage.eNS_URI, genModel = "/model/qvtd.genmodel", genModelSourceLocations = {"model/qvtd.genmodel","org.eclipse.fennec.m2x.qvtd.model/model/qvtd.genmodel"}, ecore = "/model/qvttemplate.ecore", ecoreSourceLocations = "/model/qvttemplate.ecore")
public interface QvttemplatePackage extends org.eclipse.emf.ecore.EPackage {
	/**
	 * The package name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNAME = "qvttemplate";

	/**
	 * The package namespace URI.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_URI = "http://www.eclipse.org/fennec/m2x/qvtd/qvttemplate/1.0";

	/**
	 * The package namespace name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	String eNS_PREFIX = "qvttemplate";

	/**
	 * The singleton instance of the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	QvttemplatePackage eINSTANCE = org.eclipse.fennec.m2x.model.qvttemplate.impl.QvttemplatePackageImpl.init();

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.TemplateExpImpl <em>Template Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.TemplateExpImpl
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.QvttemplatePackageImpl#getTemplateExp()
	 * @generated
	 */
	int TEMPLATE_EXP = 0;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_EXP__TYPE = OclPackage.LITERAL_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Binds To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_EXP__BINDS_TO = OclPackage.LITERAL_EXP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Where</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_EXP__WHERE = OclPackage.LITERAL_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Template Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_EXP_FEATURE_COUNT = OclPackage.LITERAL_EXP_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Template Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int TEMPLATE_EXP_OPERATION_COUNT = OclPackage.LITERAL_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.ObjectTemplateExpImpl <em>Object Template Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.ObjectTemplateExpImpl
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.QvttemplatePackageImpl#getObjectTemplateExp()
	 * @generated
	 */
	int OBJECT_TEMPLATE_EXP = 1;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_TEMPLATE_EXP__TYPE = TEMPLATE_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Binds To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_TEMPLATE_EXP__BINDS_TO = TEMPLATE_EXP__BINDS_TO;

	/**
	 * The feature id for the '<em><b>Where</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_TEMPLATE_EXP__WHERE = TEMPLATE_EXP__WHERE;

	/**
	 * The feature id for the '<em><b>Referred Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_TEMPLATE_EXP__REFERRED_CLASS = TEMPLATE_EXP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Part</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_TEMPLATE_EXP__PART = TEMPLATE_EXP_FEATURE_COUNT + 1;

	/**
	 * The number of structural features of the '<em>Object Template Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_TEMPLATE_EXP_FEATURE_COUNT = TEMPLATE_EXP_FEATURE_COUNT + 2;

	/**
	 * The number of operations of the '<em>Object Template Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int OBJECT_TEMPLATE_EXP_OPERATION_COUNT = TEMPLATE_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.CollectionTemplateExpImpl <em>Collection Template Exp</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.CollectionTemplateExpImpl
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.QvttemplatePackageImpl#getCollectionTemplateExp()
	 * @generated
	 */
	int COLLECTION_TEMPLATE_EXP = 2;

	/**
	 * The feature id for the '<em><b>Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TEMPLATE_EXP__TYPE = TEMPLATE_EXP__TYPE;

	/**
	 * The feature id for the '<em><b>Binds To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TEMPLATE_EXP__BINDS_TO = TEMPLATE_EXP__BINDS_TO;

	/**
	 * The feature id for the '<em><b>Where</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TEMPLATE_EXP__WHERE = TEMPLATE_EXP__WHERE;

	/**
	 * The feature id for the '<em><b>Referred Collection Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TEMPLATE_EXP__REFERRED_COLLECTION_TYPE = TEMPLATE_EXP_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Member</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TEMPLATE_EXP__MEMBER = TEMPLATE_EXP_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Rest</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TEMPLATE_EXP__REST = TEMPLATE_EXP_FEATURE_COUNT + 2;

	/**
	 * The number of structural features of the '<em>Collection Template Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TEMPLATE_EXP_FEATURE_COUNT = TEMPLATE_EXP_FEATURE_COUNT + 3;

	/**
	 * The number of operations of the '<em>Collection Template Exp</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int COLLECTION_TEMPLATE_EXP_OPERATION_COUNT = TEMPLATE_EXP_OPERATION_COUNT + 0;

	/**
	 * The meta object id for the '{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.PropertyTemplateItemImpl <em>Property Template Item</em>}' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.PropertyTemplateItemImpl
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.QvttemplatePackageImpl#getPropertyTemplateItem()
	 * @generated
	 */
	int PROPERTY_TEMPLATE_ITEM = 3;

	/**
	 * The feature id for the '<em><b>EAnnotations</b></em>' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TEMPLATE_ITEM__EANNOTATIONS = EcorePackage.EMODEL_ELEMENT__EANNOTATIONS;

	/**
	 * The feature id for the '<em><b>Is Opposite</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TEMPLATE_ITEM__IS_OPPOSITE = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 0;

	/**
	 * The feature id for the '<em><b>Referred Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TEMPLATE_ITEM__REFERRED_PROPERTY = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 1;

	/**
	 * The feature id for the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TEMPLATE_ITEM__VALUE = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 2;

	/**
	 * The feature id for the '<em><b>Obj Container</b></em>' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 3;

	/**
	 * The number of structural features of the '<em>Property Template Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TEMPLATE_ITEM_FEATURE_COUNT = EcorePackage.EMODEL_ELEMENT_FEATURE_COUNT + 4;

	/**
	 * The operation id for the '<em>Get EAnnotation</em>' operation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TEMPLATE_ITEM___GET_EANNOTATION__STRING = EcorePackage.EMODEL_ELEMENT___GET_EANNOTATION__STRING;

	/**
	 * The number of operations of the '<em>Property Template Item</em>' class.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	int PROPERTY_TEMPLATE_ITEM_OPERATION_COUNT = EcorePackage.EMODEL_ELEMENT_OPERATION_COUNT + 0;


	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp <em>Template Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Template Exp</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp
	 * @generated
	 */
	EClass getTemplateExp();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp#getBindsTo <em>Binds To</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Binds To</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp#getBindsTo()
	 * @see #getTemplateExp()
	 * @generated
	 */
	EReference getTemplateExp_BindsTo();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp#getWhere <em>Where</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Where</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp#getWhere()
	 * @see #getTemplateExp()
	 * @generated
	 */
	EReference getTemplateExp_Where();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp <em>Object Template Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Object Template Exp</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp
	 * @generated
	 */
	EClass getObjectTemplateExp();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp#getReferredClass <em>Referred Class</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Class</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp#getReferredClass()
	 * @see #getObjectTemplateExp()
	 * @generated
	 */
	EReference getObjectTemplateExp_ReferredClass();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp#getPart <em>Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Part</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp#getPart()
	 * @see #getObjectTemplateExp()
	 * @generated
	 */
	EReference getObjectTemplateExp_Part();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp <em>Collection Template Exp</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Collection Template Exp</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp
	 * @generated
	 */
	EClass getCollectionTemplateExp();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getReferredCollectionType <em>Referred Collection Type</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Collection Type</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getReferredCollectionType()
	 * @see #getCollectionTemplateExp()
	 * @generated
	 */
	EReference getCollectionTemplateExp_ReferredCollectionType();

	/**
	 * Returns the meta object for the containment reference list '{@link org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getMember <em>Member</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference list '<em>Member</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getMember()
	 * @see #getCollectionTemplateExp()
	 * @generated
	 */
	EReference getCollectionTemplateExp_Member();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getRest <em>Rest</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Rest</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getRest()
	 * @see #getCollectionTemplateExp()
	 * @generated
	 */
	EReference getCollectionTemplateExp_Rest();

	/**
	 * Returns the meta object for class '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem <em>Property Template Item</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for class '<em>Property Template Item</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem
	 * @generated
	 */
	EClass getPropertyTemplateItem();

	/**
	 * Returns the meta object for the attribute '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#isIsOpposite <em>Is Opposite</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the attribute '<em>Is Opposite</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#isIsOpposite()
	 * @see #getPropertyTemplateItem()
	 * @generated
	 */
	EAttribute getPropertyTemplateItem_IsOpposite();

	/**
	 * Returns the meta object for the reference '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getReferredProperty <em>Referred Property</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the reference '<em>Referred Property</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getReferredProperty()
	 * @see #getPropertyTemplateItem()
	 * @generated
	 */
	EReference getPropertyTemplateItem_ReferredProperty();

	/**
	 * Returns the meta object for the containment reference '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getValue <em>Value</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the containment reference '<em>Value</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getValue()
	 * @see #getPropertyTemplateItem()
	 * @generated
	 */
	EReference getPropertyTemplateItem_Value();

	/**
	 * Returns the meta object for the container reference '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getObjContainer <em>Obj Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the meta object for the container reference '<em>Obj Container</em>'.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getObjContainer()
	 * @see #getPropertyTemplateItem()
	 * @generated
	 */
	EReference getPropertyTemplateItem_ObjContainer();

	/**
	 * Returns the factory that creates the instances of the model.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the factory that creates the instances of the model.
	 * @generated
	 */
	QvttemplateFactory getQvttemplateFactory();

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
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.TemplateExpImpl <em>Template Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.TemplateExpImpl
		 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.QvttemplatePackageImpl#getTemplateExp()
		 * @generated
		 */
		EClass TEMPLATE_EXP = eINSTANCE.getTemplateExp();

		/**
		 * The meta object literal for the '<em><b>Binds To</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE_EXP__BINDS_TO = eINSTANCE.getTemplateExp_BindsTo();

		/**
		 * The meta object literal for the '<em><b>Where</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference TEMPLATE_EXP__WHERE = eINSTANCE.getTemplateExp_Where();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.ObjectTemplateExpImpl <em>Object Template Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.ObjectTemplateExpImpl
		 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.QvttemplatePackageImpl#getObjectTemplateExp()
		 * @generated
		 */
		EClass OBJECT_TEMPLATE_EXP = eINSTANCE.getObjectTemplateExp();

		/**
		 * The meta object literal for the '<em><b>Referred Class</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OBJECT_TEMPLATE_EXP__REFERRED_CLASS = eINSTANCE.getObjectTemplateExp_ReferredClass();

		/**
		 * The meta object literal for the '<em><b>Part</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference OBJECT_TEMPLATE_EXP__PART = eINSTANCE.getObjectTemplateExp_Part();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.CollectionTemplateExpImpl <em>Collection Template Exp</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.CollectionTemplateExpImpl
		 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.QvttemplatePackageImpl#getCollectionTemplateExp()
		 * @generated
		 */
		EClass COLLECTION_TEMPLATE_EXP = eINSTANCE.getCollectionTemplateExp();

		/**
		 * The meta object literal for the '<em><b>Referred Collection Type</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLLECTION_TEMPLATE_EXP__REFERRED_COLLECTION_TYPE = eINSTANCE.getCollectionTemplateExp_ReferredCollectionType();

		/**
		 * The meta object literal for the '<em><b>Member</b></em>' containment reference list feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLLECTION_TEMPLATE_EXP__MEMBER = eINSTANCE.getCollectionTemplateExp_Member();

		/**
		 * The meta object literal for the '<em><b>Rest</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference COLLECTION_TEMPLATE_EXP__REST = eINSTANCE.getCollectionTemplateExp_Rest();

		/**
		 * The meta object literal for the '{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.PropertyTemplateItemImpl <em>Property Template Item</em>}' class.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.PropertyTemplateItemImpl
		 * @see org.eclipse.fennec.m2x.model.qvttemplate.impl.QvttemplatePackageImpl#getPropertyTemplateItem()
		 * @generated
		 */
		EClass PROPERTY_TEMPLATE_ITEM = eINSTANCE.getPropertyTemplateItem();

		/**
		 * The meta object literal for the '<em><b>Is Opposite</b></em>' attribute feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EAttribute PROPERTY_TEMPLATE_ITEM__IS_OPPOSITE = eINSTANCE.getPropertyTemplateItem_IsOpposite();

		/**
		 * The meta object literal for the '<em><b>Referred Property</b></em>' reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY_TEMPLATE_ITEM__REFERRED_PROPERTY = eINSTANCE.getPropertyTemplateItem_ReferredProperty();

		/**
		 * The meta object literal for the '<em><b>Value</b></em>' containment reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY_TEMPLATE_ITEM__VALUE = eINSTANCE.getPropertyTemplateItem_Value();

		/**
		 * The meta object literal for the '<em><b>Obj Container</b></em>' container reference feature.
		 * <!-- begin-user-doc -->
		 * <!-- end-user-doc -->
		 * @generated
		 */
		EReference PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER = eINSTANCE.getPropertyTemplateItem_ObjContainer();

	}

} //QvttemplatePackage
