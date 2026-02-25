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
package org.eclipse.fennec.m2x.model.trace;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Var Parameter Value</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A named, typed parameter value in a trace record.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.VarParameterValue#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.VarParameterValue#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.VarParameterValue#getType <em>Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.VarParameterValue#getValue <em>Value</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getVarParameterValue()
 * @model
 * @generated
 */
@ProviderType
public interface VarParameterValue extends EObject {
	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The default value is <code>"IN"</code>.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.trace.EDirectionKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.trace.EDirectionKind
	 * @see #setKind(EDirectionKind)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getVarParameterValue_Kind()
	 * @model default="IN" required="true"
	 * @generated
	 */
	EDirectionKind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.VarParameterValue#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.trace.EDirectionKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(EDirectionKind value);

	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getVarParameterValue_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.VarParameterValue#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Type</em>' attribute.
	 * @see #setType(String)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getVarParameterValue_Type()
	 * @model required="true"
	 * @generated
	 */
	String getType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.VarParameterValue#getType <em>Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Type</em>' attribute.
	 * @see #getType()
	 * @generated
	 */
	void setType(String value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(EValue)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getVarParameterValue_Value()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EValue getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.VarParameterValue#getValue <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(EValue value);

} // VarParameterValue
