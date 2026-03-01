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
 * A representation of the model object '<em><b>Macro Invocation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Invocation of a macro with arguments (MOFM2T 1.0 Section 8.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.MacroInvocation#getDefinition <em>Definition</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.MacroInvocation#getArgument <em>Argument</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getMacroInvocation()
 * @model
 * @generated
 */
@ProviderType
public interface MacroInvocation extends TemplateExpression {
	/**
	 * Returns the value of the '<em><b>Definition</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Definition</em>' reference.
	 * @see #setDefinition(Macro)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getMacroInvocation_Definition()
	 * @model required="true"
	 * @generated
	 */
	Macro getDefinition();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.MacroInvocation#getDefinition <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Definition</em>' reference.
	 * @see #getDefinition()
	 * @generated
	 */
	void setDefinition(Macro value);

	/**
	 * Returns the value of the '<em><b>Argument</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.OclExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Argument</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getMacroInvocation_Argument()
	 * @model containment="true"
	 * @generated
	 */
	EList<OclExpression> getArgument();

} // MacroInvocation
