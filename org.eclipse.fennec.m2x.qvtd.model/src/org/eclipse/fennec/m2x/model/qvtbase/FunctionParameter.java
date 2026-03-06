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
package org.eclipse.fennec.m2x.model.qvtbase;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EParameter;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Function Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A function parameter specifies the parameters of a function. Extends EParameter only — Variable semantics handled by engine (QVT v1.3 §7.11.1.6, see D22 Lesson 1).
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage#getFunctionParameter()
 * @model
 * @generated
 */
@ProviderType
public interface FunctionParameter extends EObject, EParameter {
} // FunctionParameter
