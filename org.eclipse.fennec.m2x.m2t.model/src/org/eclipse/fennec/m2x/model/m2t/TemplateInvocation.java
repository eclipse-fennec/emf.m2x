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
package org.eclipse.fennec.m2x.model.m2t;

import org.eclipse.emf.common.util.EList;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Template Invocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Invocation of a template with arguments and optional separators (MOFM2T 1.0 Section 8.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getDefinition <em>Definition</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getArgument <em>Argument</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getBefore <em>Before</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getEach <em>Each</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getAfter <em>After</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#isSuper <em>Super</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplateInvocation()
 * @model
 * @generated
 */
@ProviderType
public interface TemplateInvocation extends TemplateExpression {
	/**
	 * Returns the value of the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' reference.
	 * @see #setDefinition(Template)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplateInvocation_Definition()
	 * @model required="true"
	 * @generated
	 */
	Template getDefinition();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getDefinition <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' reference.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(Template value);

	/**
	 * Returns the value of the '<em><b>Argument</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.OclExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Argument</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplateInvocation_Argument()
	 * @model containment="true"
	 * @generated
	 */
	EList<OclExpression> getArgument();

	/**
	 * Returns the value of the '<em><b>Before</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Before</em>' containment reference.
	 * @see #setBefore(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplateInvocation_Before()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getBefore();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getBefore <em>Before</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Before</em>' containment reference.
	 * @see #getBefore()
	 * @generated
	 */
	void setBefore(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Each</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Each</em>' containment reference.
	 * @see #setEach(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplateInvocation_Each()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getEach();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getEach <em>Each</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Each</em>' containment reference.
	 * @see #getEach()
	 * @generated
	 */
	void setEach(OclExpression value);

	/**
	 * Returns the value of the '<em><b>After</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>After</em>' containment reference.
	 * @see #setAfter(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplateInvocation_After()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getAfter();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#getAfter <em>After</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>After</em>' containment reference.
	 * @see #getAfter()
	 * @generated
	 */
	void setAfter(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Super</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether this invocation delegates to the overridden template (super call).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Super</em>' attribute.
	 * @see #setSuper(boolean)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplateInvocation_Super()
	 * @model
	 * @generated
	 */
	boolean isSuper();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.TemplateInvocation#isSuper <em>Super</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Super</em>' attribute.
	 * @see #isSuper()
	 * @generated
	 */
	void setSuper(boolean value);

} // TemplateInvocation
