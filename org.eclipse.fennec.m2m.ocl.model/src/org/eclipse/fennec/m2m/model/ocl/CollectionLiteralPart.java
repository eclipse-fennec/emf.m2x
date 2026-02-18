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

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Collection Literal Part</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Abstract base for parts of a collection literal: either a single item or a range (OCL v2.4 Section 8.3.11).
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.fennec.m2m.model.ocl.OclPackage#getCollectionLiteralPart()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface CollectionLiteralPart extends EObject {
} // CollectionLiteralPart
