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

import org.eclipse.emf.common.util.EList;

import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.eclipse.fennec.m2x.model.qvtbase.Domain;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relation Domain</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Specifies the domains of a relation. Has root variables that can be matched in a model and domain patterns specifying the model elements of interest (QVT v1.3 §7.11.3.3).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain#getRootVariable <em>Root Variable</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain#getPattern <em>Pattern</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain#getDefaultAssignment <em>Default Assignment</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationDomain()
 * @model
 * @generated
 */
@ProviderType
public interface RelationDomain extends Domain {
	/**
	 * Returns the value of the '<em><b>Root Variable</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.Variable}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The distinguished typed variables of the relation domain that can be matched in a model.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Root Variable</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationDomain_RootVariable()
	 * @model required="true"
	 * @generated
	 */
	EList<Variable> getRootVariable();

	/**
	 * Returns the value of the '<em><b>Pattern</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern#getRelationDomain <em>Relation Domain</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The domain patterns that specify the model elements of the relation domain.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Pattern</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationDomain_Pattern()
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern#getRelationDomain
	 * @model opposite="relationDomain" containment="true" required="true"
	 * @generated
	 */
	EList<DomainPattern> getPattern();

	/**
	 * Returns the value of the '<em><b>Default Assignment</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Assignments that set default values for domain variables during enforcement.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Default Assignment</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationDomain_DefaultAssignment()
	 * @model containment="true"
	 * @generated
	 */
	EList<RelationDomainAssignment> getDefaultAssignment();

} // RelationDomain
