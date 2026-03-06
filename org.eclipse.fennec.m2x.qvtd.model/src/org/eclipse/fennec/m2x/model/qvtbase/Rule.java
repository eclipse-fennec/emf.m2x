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

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Rule</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A rule specifies how the model elements specified by its domains are related with each other. Abstract — concrete subclass is Relation (QVT v1.3 §7.11.1.4).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Rule#isIsAbstract <em>Is Abstract</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Rule#getDomain <em>Domain</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Rule#getTransformation <em>Transformation</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.Rule#getOverrides <em>Overrides</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getRule()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface Rule extends EObject, ENamedElement {
	/**
	 * Returns the value of the '<em><b>Is Abstract</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Indicates that the rule is abstract. An abstract rule is never matched directly. Default is false.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Is Abstract</em>' attribute.
	 * @see #setIsAbstract(boolean)
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getRule_IsAbstract()
	 * @model default="false"
	 * @generated
	 */
	boolean isIsAbstract();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtbase.Rule#isIsAbstract <em>Is Abstract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Abstract</em>' attribute.
	 * @see #isIsAbstract()
	 * @generated
	 */
	void setIsAbstract(boolean value);

	/**
	 * Returns the value of the '<em><b>Domain</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtbase.Domain}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtbase.Domain#getRule <em>Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The domains owned by this rule.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Domain</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getRule_Domain()
	 * @see org.eclipse.fennec.m2x.model.qvtbase.Domain#getRule
	 * @model opposite="rule" containment="true"
	 * @generated
	 */
	EList<Domain> getDomain();

	/**
	 * Returns the value of the '<em><b>Transformation</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtbase.Transformation#getRule <em>Rule</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The transformation that owns this rule.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Transformation</em>' container reference.
	 * @see #setTransformation(Transformation)
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getRule_Transformation()
	 * @see org.eclipse.fennec.m2x.model.qvtbase.Transformation#getRule
	 * @model opposite="rule" transient="false"
	 * @generated
	 */
	Transformation getTransformation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtbase.Rule#getTransformation <em>Transformation</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Transformation</em>' container reference.
	 * @see #getTransformation()
	 * @generated
	 */
	void setTransformation(Transformation value);

	/**
	 * Returns the value of the '<em><b>Overrides</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The rule that this rule overrides. The overriding rule is executed in place of the overridden rule when the overriding conditions are satisfied.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Overrides</em>' reference.
	 * @see #setOverrides(Rule)
	 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getRule_Overrides()
	 * @model
	 * @generated
	 */
	Rule getOverrides();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtbase.Rule#getOverrides <em>Overrides</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Overrides</em>' reference.
	 * @see #getOverrides()
	 * @generated
	 */
	void setOverrides(Rule value);

} // Rule
