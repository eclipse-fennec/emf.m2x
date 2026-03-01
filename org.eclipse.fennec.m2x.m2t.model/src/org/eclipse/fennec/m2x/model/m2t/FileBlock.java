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
 * A representation of the model object '<em><b>File Block</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Directs generated text output to a file (MOFM2T 1.0 Section 8.1).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getFileUrl <em>File Url</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getOpenMode <em>Open Mode</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getUniqId <em>Uniq Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getCharset <em>Charset</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getFileBlock()
 * @model
 * @generated
 */
@ProviderType
public interface FileBlock extends Block {
	/**
	 * Returns the value of the '<em><b>File Url</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>File Url</em>' containment reference.
	 * @see #setFileUrl(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getFileBlock_FileUrl()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getFileUrl();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getFileUrl <em>File Url</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>File Url</em>' containment reference.
	 * @see #getFileUrl()
	 * @generated
	 */
	void setFileUrl(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Open Mode</b></em>' attribute.
	 * The default value is <code>"overwrite"</code>.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.m2t.OpenModeKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Open Mode</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.m2t.OpenModeKind
	 * @see #setOpenMode(OpenModeKind)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getFileBlock_OpenMode()
	 * @model default="overwrite"
	 * @generated
	 */
	OpenModeKind getOpenMode();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getOpenMode <em>Open Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Open Mode</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.m2t.OpenModeKind
	 * @see #getOpenMode()
	 * @generated
	 */
	void setOpenMode(OpenModeKind value);

	/**
	 * Returns the value of the '<em><b>Uniq Id</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional unique identifier expression for protected area matching across file renames.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Uniq Id</em>' containment reference.
	 * @see #setUniqId(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getFileBlock_UniqId()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getUniqId();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getUniqId <em>Uniq Id</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Uniq Id</em>' containment reference.
	 * @see #getUniqId()
	 * @generated
	 */
	void setUniqId(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Charset</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional charset expression (Acceleo extension, not in base MOFM2T spec).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Charset</em>' containment reference.
	 * @see #setCharset(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage#getFileBlock_Charset()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getCharset();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.m2t.FileBlock#getCharset <em>Charset</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Charset</em>' containment reference.
	 * @see #getCharset()
	 * @generated
	 */
	void setCharset(OclExpression value);

} // FileBlock
