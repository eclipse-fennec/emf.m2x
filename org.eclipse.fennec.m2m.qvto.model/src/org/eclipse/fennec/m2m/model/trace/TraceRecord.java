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

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Record</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A single trace record capturing one mapping execution: which mapping, its context, parameters, and results.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.TraceRecord#getMappingOperation <em>Mapping Operation</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.TraceRecord#getContext <em>Context</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.TraceRecord#getParameters <em>Parameters</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.TraceRecord#getResult <em>Result</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTraceRecord()
 * @model
 * @generated
 */
@ProviderType
public interface TraceRecord extends EObject {
	/**
	 * Returns the value of the '<em><b>Mapping Operation</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Mapping Operation</em>' containment reference.
	 * @see #setMappingOperation(EMappingOperation)
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTraceRecord_MappingOperation()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EMappingOperation getMappingOperation();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.trace.TraceRecord#getMappingOperation <em>Mapping Operation</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Mapping Operation</em>' containment reference.
	 * @see #getMappingOperation()
	 * @generated
	 */
	void setMappingOperation(EMappingOperation value);

	/**
	 * Returns the value of the '<em><b>Context</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' containment reference.
	 * @see #setContext(EMappingContext)
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTraceRecord_Context()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EMappingContext getContext();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.trace.TraceRecord#getContext <em>Context</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' containment reference.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(EMappingContext value);

	/**
	 * Returns the value of the '<em><b>Parameters</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Parameters</em>' containment reference.
	 * @see #setParameters(EMappingParameters)
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTraceRecord_Parameters()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EMappingParameters getParameters();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.trace.TraceRecord#getParameters <em>Parameters</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Parameters</em>' containment reference.
	 * @see #getParameters()
	 * @generated
	 */
	void setParameters(EMappingParameters value);

	/**
	 * Returns the value of the '<em><b>Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Result</em>' containment reference.
	 * @see #setResult(EMappingResults)
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTraceRecord_Result()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EMappingResults getResult();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.trace.TraceRecord#getResult <em>Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Result</em>' containment reference.
	 * @see #getResult()
	 * @generated
	 */
	void setResult(EMappingResults value);

} // TraceRecord
