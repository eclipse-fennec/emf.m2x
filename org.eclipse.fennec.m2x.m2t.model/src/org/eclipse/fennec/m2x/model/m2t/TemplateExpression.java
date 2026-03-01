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
package org.eclipse.fennec.m2x.model.m2t;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Template Expression</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract base class for all template expressions that produce text output (MOFM2T 1.0 Section 8.1). Extends OclExpression to allow integration with OCL evaluation.
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getTemplateExpression()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface TemplateExpression extends OclExpression {
} // TemplateExpression
