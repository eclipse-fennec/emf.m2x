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
package org.eclipse.fennec.m2x.model.ocl.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.m2x.model.ocl.EnumLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Enum Literal Exp</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.EnumLiteralExpImpl#getReferredLiteral <em>Referred Literal</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EnumLiteralExpImpl extends LiteralExpImpl implements EnumLiteralExp {
	/**
	 * The cached value of the '{@link #getReferredLiteral() <em>Referred Literal</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferredLiteral()
	 * @generated
	 * @ordered
	 */
	protected EEnumLiteral referredLiteral;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EnumLiteralExpImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OclPackage.Literals.ENUM_LITERAL_EXP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnumLiteral getReferredLiteral() {
		if (referredLiteral != null && referredLiteral.eIsProxy()) {
			InternalEObject oldReferredLiteral = (InternalEObject)referredLiteral;
			referredLiteral = (EEnumLiteral)eResolveProxy(oldReferredLiteral);
			if (referredLiteral != oldReferredLiteral) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OclPackage.ENUM_LITERAL_EXP__REFERRED_LITERAL, oldReferredLiteral, referredLiteral));
			}
		}
		return referredLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EEnumLiteral basicGetReferredLiteral() {
		return referredLiteral;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReferredLiteral(EEnumLiteral newReferredLiteral) {
		EEnumLiteral oldReferredLiteral = referredLiteral;
		referredLiteral = newReferredLiteral;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.ENUM_LITERAL_EXP__REFERRED_LITERAL, oldReferredLiteral, referredLiteral));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OclPackage.ENUM_LITERAL_EXP__REFERRED_LITERAL:
				if (resolve) return getReferredLiteral();
				return basicGetReferredLiteral();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case OclPackage.ENUM_LITERAL_EXP__REFERRED_LITERAL:
				setReferredLiteral((EEnumLiteral)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case OclPackage.ENUM_LITERAL_EXP__REFERRED_LITERAL:
				setReferredLiteral((EEnumLiteral)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case OclPackage.ENUM_LITERAL_EXP__REFERRED_LITERAL:
				return referredLiteral != null;
		}
		return super.eIsSet(featureID);
	}

} //EnumLiteralExpImpl
