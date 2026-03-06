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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;

import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Pattern</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A pattern is a set of variable declarations and predicates, which when evaluated in the context of a model, results in a set of bindings for the variables (QVT v1.3 §7.11.1.8).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Pattern#getBindsTo <em>Binds To</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Pattern#getPredicate <em>Predicate</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getPattern()
 * @model
 * @generated
 */
@ProviderType
public interface Pattern extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Binds To</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.Variable}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The set of variables that are to be bound when the pattern is evaluated.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Binds To</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getPattern_BindsTo()
	 * @model
	 * @generated
	 */
	EList<Variable> getBindsTo();

	/**
	 * Returns the value of the '<em><b>Predicate</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtbase.Predicate}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtbase.Predicate#getPattern <em>Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The set of predicates that must evaluate to true for a valid binding.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Predicate</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getPattern_Predicate()
	 * @see org.eclipse.fennec.m2x.model.qvtbase.Predicate#getPattern
	 * @model opposite="pattern" containment="true"
	 * @generated
	 */
	EList<Predicate> getPredicate();

} // Pattern
