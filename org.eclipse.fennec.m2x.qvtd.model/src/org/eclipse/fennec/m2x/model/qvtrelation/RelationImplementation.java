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
package org.eclipse.fennec.m2x.model.qvtrelation;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;

import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relation Implementation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Specifies an optional black-box operational implementation to enforce a domain of a relation. Invoked when the relation evaluates to false during enforcement (QVT v1.3 §7.11.3.6).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation#getImpl <em>Impl</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation#getInDirectionOf <em>In Direction Of</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation#getRelation <em>Relation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationImplementation()
 * @model
 * @generated
 */
@ProviderType
public interface RelationImplementation extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Impl</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The operation that implements the relation in the given direction.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Impl</em>' reference.
	 * @see #setImpl(EOperation)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationImplementation_Impl()
	 * @model required="true"
	 * @generated
	 */
	EOperation getImpl();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation#getImpl <em>Impl</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Impl</em>' reference.
	 * @see #getImpl()
	 * @generated
	 */
	void setImpl(EOperation value);

	/**
	 * Returns the value of the '<em><b>In Direction Of</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The direction of the relation being implemented.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>In Direction Of</em>' reference.
	 * @see #setInDirectionOf(TypedModel)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationImplementation_InDirectionOf()
	 * @model required="true"
	 * @generated
	 */
	TypedModel getInDirectionOf();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation#getInDirectionOf <em>In Direction Of</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>In Direction Of</em>' reference.
	 * @see #getInDirectionOf()
	 * @generated
	 */
	void setInDirectionOf(TypedModel value);

	/**
	 * Returns the value of the '<em><b>Relation</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtrelation.Relation#getOperationalImpl <em>Operational Impl</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The relation being implemented.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Relation</em>' container reference.
	 * @see #setRelation(Relation)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationImplementation_Relation()
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.Relation#getOperationalImpl
	 * @model opposite="operationalImpl" required="true" transient="false"
	 * @generated
	 */
	Relation getRelation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation#getRelation <em>Relation</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Relation</em>' container reference.
	 * @see #getRelation()
	 * @generated
	 */
	void setRelation(Relation value);

} // RelationImplementation
