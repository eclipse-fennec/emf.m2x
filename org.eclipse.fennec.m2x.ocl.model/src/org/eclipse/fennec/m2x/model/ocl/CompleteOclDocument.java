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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Complete Ocl Document</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A Complete OCL document as a unit root (#209): the constraints of one .ocl document under one containment tree, so the document can travel as a compiled unit — sealed, fingerprinted, stored and prepared like a transformation, only that its lifecycle ends at prepare: a document is installed into an engine, never executed. Inherits EPackage because that is the one shape the neutral compiled-unit model holds a unit root by.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.CompleteOclDocument#getConstraints <em>Constraints</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getCompleteOclDocument()
 * @model
 * @generated
 */
@ProviderType
public interface CompleteOclDocument extends EObject, EPackage {
	/**
	 * Returns the value of the '<em><b>Constraints</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.ocl.Constraint}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The document's constraints in source order — what OclEngine.parseDocument answers, rooted. Context classifiers stay non-containment references into the metamodels; synthetic context operations of def: constraints travel as satellites.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Constraints</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.ocl.OclPackage#getCompleteOclDocument_Constraints()
	 * @model containment="true"
	 * @generated
	 */
	EList<Constraint> getConstraints();

} // CompleteOclDocument
