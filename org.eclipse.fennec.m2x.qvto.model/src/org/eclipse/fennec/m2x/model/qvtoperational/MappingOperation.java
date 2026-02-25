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
package org.eclipse.fennec.m2x.model.qvtoperational;

import org.eclipse.emf.common.util.EList;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Mapping Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A mapping operation with when/where guards and disjunct/inherited/merged clauses (QVT v1.3 Section 8.2.2.12).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation#getWhen <em>When</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation#getWhere <em>Where</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation#getDisjunct <em>Disjunct</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation#getInherited <em>Inherited</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation#getMerged <em>Merged</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getMappingOperation()
 * @model
 * @generated
 */
@ProviderType
public interface MappingOperation extends ImperativeOperation {
	/**
	 * Returns the value of the '<em><b>When</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.OclExpression}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>When</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getMappingOperation_When()
	 * @model containment="true"
	 * @generated
	 */
	EList<OclExpression> getWhen();

	/**
	 * Returns the value of the '<em><b>Where</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Where</em>' containment reference.
	 * @see #setWhere(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getMappingOperation_Where()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getWhere();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation#getWhere <em>Where</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Where</em>' containment reference.
	 * @see #getWhere()
	 * @generated
	 */
	void setWhere(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Disjunct</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Disjunct</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getMappingOperation_Disjunct()
	 * @model
	 * @generated
	 */
	EList<MappingOperation> getDisjunct();

	/**
	 * Returns the value of the '<em><b>Inherited</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Inherited</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getMappingOperation_Inherited()
	 * @model
	 * @generated
	 */
	EList<MappingOperation> getInherited();

	/**
	 * Returns the value of the '<em><b>Merged</b></em>' reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Merged</em>' reference list.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getMappingOperation_Merged()
	 * @model
	 * @generated
	 */
	EList<MappingOperation> getMerged();

} // MappingOperation
