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
package org.eclipse.fennec.m2x.model.qvtoperational;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EParameter;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Var Parameter</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A typed parameter with direction kind, used for operation context, parameters, and results (QVT v1.3 Section 8.2.2.3). Does not extend ocl::Variable directly to avoid duplicate 'name' feature (ENamedElement.name vs Variable.name); relevant Variable features (representedParameter, ownedInit) are declared locally.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getRepresentedParameter <em>Represented Parameter</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getOwnedInit <em>Owned Init</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getCtxOwner <em>Ctx Owner</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getResOwner <em>Res Owner</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getVarParameter()
 * @model
 * @generated
 */
@ProviderType
public interface VarParameter extends EObject, EParameter {
	/**
	 * Returns the value of the '<em><b>Kind</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2x.model.qvtoperational.DirectionKind}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.DirectionKind
	 * @see #setKind(DirectionKind)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getVarParameter_Kind()
	 * @model
	 * @generated
	 */
	DirectionKind getKind();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getKind <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Kind</em>' attribute.
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.DirectionKind
	 * @see #getKind()
	 * @generated
	 */
	void setKind(DirectionKind value);

	/**
	 * Returns the value of the '<em><b>Represented Parameter</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional reference to the OCL Variable this parameter represents at runtime.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Represented Parameter</em>' reference.
	 * @see #setRepresentedParameter(Variable)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getVarParameter_RepresentedParameter()
	 * @model
	 * @generated
	 */
	Variable getRepresentedParameter();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getRepresentedParameter <em>Represented Parameter</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Represented Parameter</em>' reference.
	 * @see #getRepresentedParameter()
	 * @generated
	 */
	void setRepresentedParameter(Variable value);

	/**
	 * Returns the value of the '<em><b>Owned Init</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Optional initialization expression for this parameter (default value).
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Owned Init</em>' containment reference.
	 * @see #setOwnedInit(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getVarParameter_OwnedInit()
	 * @model containment="true"
	 * @generated
	 */
	OclExpression getOwnedInit();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getOwnedInit <em>Owned Init</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Owned Init</em>' containment reference.
	 * @see #getOwnedInit()
	 * @generated
	 */
	void setOwnedInit(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Ctx Owner</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation#getContext <em>Context</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ctx Owner</em>' container reference.
	 * @see #setCtxOwner(ImperativeOperation)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getVarParameter_CtxOwner()
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation#getContext
	 * @model opposite="context" transient="false"
	 * @generated
	 */
	ImperativeOperation getCtxOwner();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getCtxOwner <em>Ctx Owner</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ctx Owner</em>' container reference.
	 * @see #getCtxOwner()
	 * @generated
	 */
	void setCtxOwner(ImperativeOperation value);

	/**
	 * Returns the value of the '<em><b>Res Owner</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation#getResult <em>Result</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Res Owner</em>' container reference.
	 * @see #setResOwner(ImperativeOperation)
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage#getVarParameter_ResOwner()
	 * @see org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation#getResult
	 * @model opposite="result" transient="false"
	 * @generated
	 */
	ImperativeOperation getResOwner();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvtoperational.VarParameter#getResOwner <em>Res Owner</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Res Owner</em>' container reference.
	 * @see #getResOwner()
	 * @generated
	 */
	void setResOwner(ImperativeOperation value);

} // VarParameter
