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
package org.eclipse.fennec.m2x.model.ocl;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Type</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * An OCL type is an EClassifier, as in OCL v2.4 Section 8.2 (types are Classifiers) and in the Eclipse OCL Ecore binding. That is what lets the standard library be an ordinary EPackage in the EPackage.Registry, resolvable from any resource set, and lets a compiled unit reference Integer the way it references EString. Abstract base class for all OCL types. Each type carries a name used in diagnostics and pretty-printing.
 * <!-- end-model-doc -->
 *
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getOclType()
 * @model abstract="true"
 * @generated
 */
@ProviderType
public interface OclType extends EObject, EClassifier {
} // OclType
