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
package org.eclipse.fennec.m2x.model.qvtrelation.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.fennec.m2x.model.qvtrelation.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class QvtrelationFactoryImpl extends EFactoryImpl implements QvtrelationFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static QvtrelationFactory init() {
		try {
			QvtrelationFactory theQvtrelationFactory = (QvtrelationFactory)EPackage.Registry.INSTANCE.getEFactory(QvtrelationPackage.eNS_URI);
			if (theQvtrelationFactory != null) {
				return theQvtrelationFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new QvtrelationFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QvtrelationFactoryImpl() {
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
			case QvtrelationPackage.RELATIONAL_TRANSFORMATION: return createRelationalTransformation();
			case QvtrelationPackage.RELATION: return createRelation();
			case QvtrelationPackage.RELATION_DOMAIN: return createRelationDomain();
			case QvtrelationPackage.DOMAIN_PATTERN: return createDomainPattern();
			case QvtrelationPackage.KEY: return createKey();
			case QvtrelationPackage.RELATION_IMPLEMENTATION: return createRelationImplementation();
			case QvtrelationPackage.RELATION_DOMAIN_ASSIGNMENT: return createRelationDomainAssignment();
			case QvtrelationPackage.RELATION_CALL_EXP: return createRelationCallExp();
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
	public RelationalTransformation createRelationalTransformation() {
		RelationalTransformationImpl relationalTransformation = new RelationalTransformationImpl();
		return relationalTransformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Relation createRelation() {
		RelationImpl relation = new RelationImpl();
		return relation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RelationDomain createRelationDomain() {
		RelationDomainImpl relationDomain = new RelationDomainImpl();
		return relationDomain;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DomainPattern createDomainPattern() {
		DomainPatternImpl domainPattern = new DomainPatternImpl();
		return domainPattern;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Key createKey() {
		KeyImpl key = new KeyImpl();
		return key;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RelationImplementation createRelationImplementation() {
		RelationImplementationImpl relationImplementation = new RelationImplementationImpl();
		return relationImplementation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RelationDomainAssignment createRelationDomainAssignment() {
		RelationDomainAssignmentImpl relationDomainAssignment = new RelationDomainAssignmentImpl();
		return relationDomainAssignment;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RelationCallExp createRelationCallExp() {
		RelationCallExpImpl relationCallExp = new RelationCallExpImpl();
		return relationCallExp;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public QvtrelationPackage getQvtrelationPackage() {
		return (QvtrelationPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static QvtrelationPackage getPackage() {
		return QvtrelationPackage.eINSTANCE;
	}

} //QvtrelationFactoryImpl
