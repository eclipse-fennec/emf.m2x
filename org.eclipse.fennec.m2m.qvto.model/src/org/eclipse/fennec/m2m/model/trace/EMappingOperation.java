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
package org.eclipse.fennec.m2m.model.trace;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EMapping Operation</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Serializable description of a mapping operation in a trace record.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.EMappingOperation#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.EMappingOperation#getPackage <em>Package</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.EMappingOperation#getModule <em>Module</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.EMappingOperation#getRuntimeMappingOperation <em>Runtime Mapping Operation</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getEMappingOperation()
 * @model
 * @generated
 */
@ProviderType
public interface EMappingOperation extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getEMappingOperation_Name()
	 * @model required="true"
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.trace.EMappingOperation#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Package</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Package</em>' attribute.
	 * @see #setPackage(String)
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getEMappingOperation_Package()
	 * @model required="true"
	 * @generated
	 */
	String getPackage();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.trace.EMappingOperation#getPackage <em>Package</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Package</em>' attribute.
	 * @see #getPackage()
	 * @generated
	 */
	void setPackage(String value);

	/**
	 * Returns the value of the '<em><b>Module</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Module</em>' attribute.
	 * @see #setModule(String)
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getEMappingOperation_Module()
	 * @model required="true"
	 * @generated
	 */
	String getModule();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.trace.EMappingOperation#getModule <em>Module</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Module</em>' attribute.
	 * @see #getModule()
	 * @generated
	 */
	void setModule(String value);

	/**
	 * Returns the value of the '<em><b>Runtime Mapping Operation</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Runtime Mapping Operation</em>' reference.
	 * @see #setRuntimeMappingOperation(MappingOperation)
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getEMappingOperation_RuntimeMappingOperation()
	 * @model required="true" transient="true"
	 * @generated
	 */
	MappingOperation getRuntimeMappingOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.trace.EMappingOperation#getRuntimeMappingOperation <em>Runtime Mapping Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Runtime Mapping Operation</em>' reference.
	 * @see #getRuntimeMappingOperation()
	 * @generated
	 */
	void setRuntimeMappingOperation(MappingOperation value);

} // EMappingOperation
