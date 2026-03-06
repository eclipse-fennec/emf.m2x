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
package org.eclipse.fennec.m2x.model.qvttemplate.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.fennec.m2x.model.qvttemplate.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class QvttemplateFactoryImpl extends EFactoryImpl implements QvttemplateFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static QvttemplateFactory init() {
		try {
			QvttemplateFactory theQvttemplateFactory = (QvttemplateFactory)EPackage.Registry.INSTANCE.getEFactory(QvttemplatePackage.eNS_URI);
			if (theQvttemplateFactory != null) {
				return theQvttemplateFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new QvttemplateFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QvttemplateFactoryImpl() {
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
			case QvttemplatePackage.OBJECT_TEMPLATE_EXP: return createObjectTemplateExp();
			case QvttemplatePackage.COLLECTION_TEMPLATE_EXP: return createCollectionTemplateExp();
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM: return createPropertyTemplateItem();
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
	public ObjectTemplateExp createObjectTemplateExp() {
		ObjectTemplateExpImpl objectTemplateExp = new ObjectTemplateExpImpl();
		return objectTemplateExp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CollectionTemplateExp createCollectionTemplateExp() {
		CollectionTemplateExpImpl collectionTemplateExp = new CollectionTemplateExpImpl();
		return collectionTemplateExp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public PropertyTemplateItem createPropertyTemplateItem() {
		PropertyTemplateItemImpl propertyTemplateItem = new PropertyTemplateItemImpl();
		return propertyTemplateItem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public QvttemplatePackage getQvttemplatePackage() {
		return (QvttemplatePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static QvttemplatePackage getPackage() {
		return QvttemplatePackage.eINSTANCE;
	}

} //QvttemplateFactoryImpl
