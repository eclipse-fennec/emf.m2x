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

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Iterate Exp</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The iterate expression: the most general collection loop with an explicit accumulator variable (OCL v2.4 Section 8.3.7).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.IterateExp#getOwnedResult <em>Owned Result</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getIterateExp()
 * @model
 * @generated
 */
@ProviderType
public interface IterateExp extends LoopExp {
	/**
	 * Returns the value of the '<em><b>Owned Result</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The accumulator variable that holds the intermediate and final result of the iteration.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Result</em>' containment reference.
	 * @see #setOwnedResult(Variable)
	 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getIterateExp_OwnedResult()
	 * @model containment="true"
	 * @generated
	 */
	Variable getOwnedResult();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.model.ocl.IterateExp#getOwnedResult <em>Owned Result</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Result</em>' containment reference.
	 * @see #getOwnedResult()
	 * @generated
	 */
	void setOwnedResult(Variable value);

} // IterateExp
