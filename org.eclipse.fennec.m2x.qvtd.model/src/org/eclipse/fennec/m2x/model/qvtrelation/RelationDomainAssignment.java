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

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Relation Domain Assignment</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Sets the value of a domain variable by evaluating an associated expression. Used for default value assignments during enforcement (QVT v1.3 §7.11.3.7).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment#getVariable <em>Variable</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment#getValueExp <em>Value Exp</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationDomainAssignment()
 * @model
 * @generated
 */
@ProviderType
public interface RelationDomainAssignment extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Variable</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The variable being assigned.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Variable</em>' reference.
	 * @see #setVariable(Variable)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationDomainAssignment_Variable()
	 * @model required="true"
	 * @generated
	 */
	Variable getVariable();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment#getVariable <em>Variable</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Variable</em>' reference.
	 * @see #getVariable()
	 * @generated
	 */
	void setVariable(Variable value);

	/**
	 * Returns the value of the '<em><b>Value Exp</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The expression that provides the value of the variable.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Value Exp</em>' containment reference.
	 * @see #setValueExp(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getRelationDomainAssignment_ValueExp()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getValueExp();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment#getValueExp <em>Value Exp</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value Exp</em>' containment reference.
	 * @see #getValueExp()
	 * @generated
	 */
	void setValueExp(OclExpression value);

} // RelationDomainAssignment
