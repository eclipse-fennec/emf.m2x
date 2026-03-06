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

import org.eclipse.fennec.m2x.model.qvtbase.Pattern;

import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Domain Pattern</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A domain pattern specifies an arbitrarily complex pattern graph in terms of template expressions. Has a distinguished root template expression bound to the root variable (QVT v1.3 §7.11.3.4).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern#getTemplateExpression <em>Template Expression</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern#getRelationDomain <em>Relation Domain</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getDomainPattern()
 * @model
 * @generated
 */
@ProviderType
public interface DomainPattern extends Pattern {
	/**
	 * Returns the value of the '<em><b>Template Expression</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The root template expression of the domain pattern. Must be bound to the root variable of the owning relation domain.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Template Expression</em>' containment reference.
	 * @see #setTemplateExpression(TemplateExp)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getDomainPattern_TemplateExpression()
	 * @model containment="true"
	 * @generated
	 */
	TemplateExp getTemplateExpression();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern#getTemplateExpression <em>Template Expression</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Template Expression</em>' containment reference.
	 * @see #getTemplateExpression()
	 * @generated
	 */
	void setTemplateExpression(TemplateExp value);

	/**
	 * Returns the value of the '<em><b>Relation Domain</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain#getPattern <em>Pattern</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The relation domain that owns this domain pattern.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Relation Domain</em>' container reference.
	 * @see #setRelationDomain(RelationDomain)
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage#getDomainPattern_RelationDomain()
	 * @see org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain#getPattern
	 * @model opposite="pattern" required="true" transient="false"
	 * @generated
	 */
	RelationDomain getRelationDomain();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern#getRelationDomain <em>Relation Domain</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Relation Domain</em>' container reference.
	 * @see #getRelationDomain()
	 * @generated
	 */
	void setRelationDomain(RelationDomain value);

} // DomainPattern
