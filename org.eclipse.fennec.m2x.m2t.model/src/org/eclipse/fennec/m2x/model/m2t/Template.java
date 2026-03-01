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
import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Template</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A named template that generates text from model elements (MOFM2T 1.0 Section 8.1). Templates can have parameters, a guard condition, and override other templates.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Template#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Template#getGuard <em>Guard</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Template#getOverrides <em>Overrides</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Template#isMain <em>Main</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.Template#getPost <em>Post</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplate()
 * @model
 * @generated
 */
@ProviderType
public interface Template extends Block, ModuleElement {
	/**
	 * Returns the value of the '<em><b>Parameter</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.Variable}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameter</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplate_Parameter()
	 * @model containment="true"
	 * @generated
	 */
	EList<Variable> getParameter();

	/**
	 * Returns the value of the '<em><b>Guard</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Guard</em>' containment reference.
	 * @see #setGuard(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplate_Guard()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getGuard();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.Template#getGuard <em>Guard</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Guard</em>' containment reference.
	 * @see #getGuard()
	 * @generated
	 */
	void setGuard(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Overrides</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.m2t.Template}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Overrides</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplate_Overrides()
	 * @model
	 * @generated
	 */
	EList<Template> getOverrides();

	/**
	 * Returns the value of the '<em><b>Main</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Whether this template is the entry point of the module.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Main</em>' attribute.
	 * @see #setMain(boolean)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplate_Main()
	 * @model
	 * @generated
	 */
	boolean isMain();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.Template#isMain <em>Main</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Main</em>' attribute.
	 * @see #isMain()
	 * @generated
	 */
	void setMain(boolean value);

	/**
	 * Returns the value of the '<em><b>Post</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional post-processing expression applied to the generated text (e.g. trim()).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Post</em>' containment reference.
	 * @see #setPost(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplate_Post()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getPost();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.Template#getPost <em>Post</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Post</em>' containment reference.
	 * @see #getPost()
	 * @generated
	 */
	void setPost(OclExpression value);

} // Template
