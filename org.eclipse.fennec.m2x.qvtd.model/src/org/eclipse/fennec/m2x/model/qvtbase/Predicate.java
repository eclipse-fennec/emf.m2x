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
package org.eclipse.fennec.m2x.model.qvtbase;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Predicate</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A predicate is a boolean-valued expression owned by a pattern. Specified by an OCL expression that may reference variables of the owning pattern (QVT v1.3 §7.11.1.7).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Predicate#getConditionExpression <em>Condition Expression</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Predicate#getPattern <em>Pattern</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getPredicate()
 * @model
 * @generated
 */
@ProviderType
public interface Predicate extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Condition Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The OCL expression that specifies the predicate.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Condition Expression</em>' containment reference.
	 * @see #setConditionExpression(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getPredicate_ConditionExpression()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getConditionExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtbase.Predicate#getConditionExpression <em>Condition Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Condition Expression</em>' containment reference.
	 * @see #getConditionExpression()
	 * @generated
	 */
	void setConditionExpression(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Pattern</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtbase.Pattern#getPredicate <em>Predicate</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The pattern that owns the predicate.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Pattern</em>' container reference.
	 * @see #setPattern(Pattern)
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getPredicate_Pattern()
	 * @see org.eclipse.fennec.m2x.model.qvtbase.Pattern#getPredicate
	 * @model opposite="predicate" required="true" transient="false"
	 * @generated
	 */
	Pattern getPattern();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtbase.Predicate#getPattern <em>Pattern</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Pattern</em>' container reference.
	 * @see #getPattern()
	 * @generated
	 */
	void setPattern(Pattern value);

} // Predicate
