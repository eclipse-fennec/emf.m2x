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
package org.eclipse.fennec.m2m.model.ocl;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Constraint</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A named OCL constraint (invariant, pre/postcondition, body, init, derive, or def) attached to a model element.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getSpecification <em>Specification</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getContextClassifier <em>Context Classifier</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getContextOperation <em>Context Operation</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getContextProperty <em>Context Property</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getConstraint()
 * @model
 * @generated
 */
@ProviderType
public interface Constraint extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getConstraint_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2m.model.ocl.ConstraintKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2m.model.ocl.ConstraintKind
	 * @see #setKind(ConstraintKind)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getConstraint_Kind()
	 * @model
	 * @generated
	 */
	ConstraintKind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2m.model.ocl.ConstraintKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(ConstraintKind value);

	/**
	 * Returns the value of the '<em><b>Specification</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The OCL expression that forms the body of this constraint.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Specification</em>' containment reference.
	 * @see #setSpecification(OclExpression)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getConstraint_Specification()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getSpecification();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getSpecification <em>Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Specification</em>' containment reference.
	 * @see #getSpecification()
	 * @generated
	 */
	void setSpecification(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Context Classifier</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The classifier context for inv and def constraints.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Context Classifier</em>' reference.
	 * @see #setContextClassifier(EClassifier)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getConstraint_ContextClassifier()
	 * @model
	 * @generated
	 */
	EClassifier getContextClassifier();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getContextClassifier <em>Context Classifier</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context Classifier</em>' reference.
	 * @see #getContextClassifier()
	 * @generated
	 */
	void setContextClassifier(EClassifier value);

	/**
	 * Returns the value of the '<em><b>Context Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The operation context for pre, post, and body constraints.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Context Operation</em>' reference.
	 * @see #setContextOperation(EOperation)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getConstraint_ContextOperation()
	 * @model
	 * @generated
	 */
	EOperation getContextOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getContextOperation <em>Context Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context Operation</em>' reference.
	 * @see #getContextOperation()
	 * @generated
	 */
	void setContextOperation(EOperation value);

	/**
	 * Returns the value of the '<em><b>Context Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The property context for init and derive constraints.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Context Property</em>' reference.
	 * @see #setContextProperty(EStructuralFeature)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getConstraint_ContextProperty()
	 * @model
	 * @generated
	 */
	EStructuralFeature getContextProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.Constraint#getContextProperty <em>Context Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context Property</em>' reference.
	 * @see #getContextProperty()
	 * @generated
	 */
	void setContextProperty(EStructuralFeature value);

} // Constraint
