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
package org.eclipse.fennec.m2x.model.qvttemplate;

import org.eclipse.fennec.m2x.model.ocl.LiteralExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Template Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A template expression specifies a pattern that matches model elements in a candidate model. The matched model element may be bound to a variable (QVT v1.3 §7.11.2.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp#getBindsTo <em>Binds To</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp#getWhere <em>Where</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getTemplateExp()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface TemplateExp extends LiteralExp {
	/**
	 * Returns the value of the '<em><b>Binds To</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The variable that refers to the model element matched by this template expression.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Binds To</em>' reference.
	 * @see #setBindsTo(Variable)
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getTemplateExp_BindsTo()
	 * @model
	 * @generated
	 */
	Variable getBindsTo();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp#getBindsTo <em>Binds To</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Binds To</em>' reference.
	 * @see #getBindsTo()
	 * @generated
	 */
	void setBindsTo(Variable value);

	/**
	 * Returns the value of the '<em><b>Where</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * A boolean expression that must evaluate to true for the template expression to match.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Where</em>' containment reference.
	 * @see #setWhere(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getTemplateExp_Where()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getWhere();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp#getWhere <em>Where</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Where</em>' containment reference.
	 * @see #getWhere()
	 * @generated
	 */
	void setWhere(OclExpression value);

} // TemplateExp
