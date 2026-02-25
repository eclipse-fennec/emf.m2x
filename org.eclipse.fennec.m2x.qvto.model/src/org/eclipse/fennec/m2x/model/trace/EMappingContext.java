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
 * A representation of the model object '<em><b>EMapping Context</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Context (self) parameter value of a trace record.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.EMappingContext#getContext <em>Context</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getEMappingContext()
 * @model
 * @generated
 */
@ProviderType
public interface EMappingContext extends EObject {
	/**
	 * Returns the value of the '<em><b>Context</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Context</em>' containment reference.
	 * @see #setContext(VarParameterValue)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getEMappingContext_Context()
	 * @model containment="true"
	 * @generated
	 */
	VarParameterValue getContext();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.EMappingContext#getContext <em>Context</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Context</em>' containment reference.
	 * @see #getContext()
	 * @generated
	 */
	void setContext(VarParameterValue value);

} // EMappingContext
