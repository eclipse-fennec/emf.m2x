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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Object Template Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An object template expression specifies a pattern that may match only single model elements. Has a type specified by the referred class and a set of property template items (QVT v1.3 §7.11.2.2).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp#getReferredClass <em>Referred Class</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp#getPart <em>Part</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getObjectTemplateExp()
 * @model
 * @generated
 */
@ProviderType
public interface ObjectTemplateExp extends TemplateExp {
	/**
	 * Returns the value of the '<em><b>Referred Class</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The EClass that specifies the type of objects to be matched by this expression.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred Class</em>' reference.
	 * @see #setReferredClass(EClass)
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getObjectTemplateExp_ReferredClass()
	 * @model required="true"
	 * @generated
	 */
	EClass getReferredClass();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp#getReferredClass <em>Referred Class</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred Class</em>' reference.
	 * @see #getReferredClass()
	 * @generated
	 */
	void setReferredClass(EClass value);

	/**
	 * Returns the value of the '<em><b>Part</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getObjContainer <em>Obj Container</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specification of value expressions that must be satisfied by the corresponding slots of the matched object.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Part</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getObjectTemplateExp_Part()
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getObjContainer
	 * @model opposite="objContainer" containment="true"
	 * @generated
	 */
	EList<PropertyTemplateItem> getPart();

} // ObjectTemplateExp
