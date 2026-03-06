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

import org.eclipse.emf.ecore.EClassifier;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Collection Template Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A collection template expression specifies a pattern that matches a collection of elements. Has member expressions and an optional rest variable (QVT v1.3 §7.11.2.3).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getReferredCollectionType <em>Referred Collection Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getMember <em>Member</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getRest <em>Rest</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getCollectionTemplateExp()
 * @model
 * @generated
 */
@ProviderType
public interface CollectionTemplateExp extends TemplateExp {
	/**
	 * Returns the value of the '<em><b>Referred Collection Type</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The type of the collection that is being specified (Set, Sequence, OrderedSet, etc.).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred Collection Type</em>' reference.
	 * @see #setReferredCollectionType(EClassifier)
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getCollectionTemplateExp_ReferredCollectionType()
	 * @model required="true"
	 * @generated
	 */
	EClassifier getReferredCollectionType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getReferredCollectionType <em>Referred Collection Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred Collection Type</em>' reference.
	 * @see #getReferredCollectionType()
	 * @generated
	 */
	void setReferredCollectionType(EClassifier value);

	/**
	 * Returns the value of the '<em><b>Member</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.OclExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The expressions that the elements of the collection must have matches for.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Member</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getCollectionTemplateExp_Member()
	 * @model containment="true"
	 * @generated
	 */
	EList<OclExpression> getMember();

	/**
	 * Returns the value of the '<em><b>Rest</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The variable that the rest of the collection (excluding elements matched by member expressions) must match.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Rest</em>' reference.
	 * @see #setRest(Variable)
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getCollectionTemplateExp_Rest()
	 * @model
	 * @generated
	 */
	Variable getRest();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp#getRest <em>Rest</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Rest</em>' reference.
	 * @see #getRest()
	 * @generated
	 */
	void setRest(Variable value);

} // CollectionTemplateExp
