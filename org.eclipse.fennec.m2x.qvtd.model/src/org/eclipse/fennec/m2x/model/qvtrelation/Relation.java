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

import org.eclipse.fennec.m2x.model.qvtbase.Pattern;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A relation is the basic unit of transformation behavior specification. It specifies a relationship that must hold between the model elements of a set of candidate models (QVT v1.3 §7.11.3.2).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.Relation#isIsTopLevel <em>Is Top Level</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.Relation#getVariable <em>Variable</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.Relation#getWhen <em>When</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.Relation#getWhere <em>Where</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.Relation#getOperationalImpl <em>Operational Impl</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelation()
 * @model
 * @generated
 */
@ProviderType
public interface Relation extends Rule {
	/**
	 * Returns the value of the '<em><b>Is Top Level</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Indicates whether the relation is a top-level relation. Top-level relations must hold for all matching elements; non-top-level relations are invoked from where clauses.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Is Top Level</em>' attribute.
	 * @see #setIsTopLevel(boolean)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelation_IsTopLevel()
	 * @model default="false"
	 * @generated
	 */
	boolean isIsTopLevel();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.Relation#isIsTopLevel <em>Is Top Level</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Top Level</em>' attribute.
	 * @see #isIsTopLevel()
	 * @generated
	 */
	void setIsTopLevel(boolean value);

	/**
	 * Returns the value of the '<em><b>Variable</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.Variable}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The set of variables occurring in the relation. Includes all variables from domains, when and where clauses.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Variable</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelation_Variable()
	 * @model containment="true"
	 * @generated
	 */
	EList<Variable> getVariable();

	/**
	 * Returns the value of the '<em><b>When</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The pattern that specifies the when clause — conditions under which the relationship needs to hold.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>When</em>' containment reference.
	 * @see #setWhen(Pattern)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelation_When()
	 * @model containment="true"
	 * @generated
	 */
	Pattern getWhen();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.Relation#getWhen <em>When</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>When</em>' containment reference.
	 * @see #getWhen()
	 * @generated
	 */
	void setWhen(Pattern value);

	/**
	 * Returns the value of the '<em><b>Where</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The pattern that specifies the where clause — conditions that must be satisfied by all participating model elements.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Where</em>' containment reference.
	 * @see #setWhere(Pattern)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelation_Where()
	 * @model containment="true"
	 * @generated
	 */
	Pattern getWhere();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.Relation#getWhere <em>Where</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Where</em>' containment reference.
	 * @see #getWhere()
	 * @generated
	 */
	void setWhere(Pattern value);

	/**
	 * Returns the value of the '<em><b>Operational Impl</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation#getRelation <em>Relation</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional black-box operational implementations to enforce domains of this relation (§7.8).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Operational Impl</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelation_OperationalImpl()
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation#getRelation
	 * @model opposite="relation" containment="true"
	 * @generated
	 */
	EList<RelationImplementation> getOperationalImpl();

} // Relation
