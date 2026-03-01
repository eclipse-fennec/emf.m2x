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
package org.eclipse.fennec.m2x.model.m2t.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.m2x.model.m2t.M2tPackage;
import org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Protected Area Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ProtectedAreaBlockImpl#getMarker <em>Marker</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ProtectedAreaBlockImpl extends BlockImpl implements ProtectedAreaBlock {
	/**
	 * The cached value of the '{@link #getMarker() <em>Marker</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMarker()
	 * @generated
	 * @ordered
	 */
	protected OclExpression marker;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ProtectedAreaBlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return M2tPackage.Literals.PROTECTED_AREA_BLOCK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getMarker() {
		return marker;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetMarker(OclExpression newMarker, NotificationChain msgs) {
		OclExpression oldMarker = marker;
		marker = newMarker;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.PROTECTED_AREA_BLOCK__MARKER, oldMarker, newMarker);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMarker(OclExpression newMarker) {
		if (newMarker != marker) {
			NotificationChain msgs = null;
			if (marker != null)
				msgs = ((InternalEObject)marker).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.PROTECTED_AREA_BLOCK__MARKER, null, msgs);
			if (newMarker != null)
				msgs = ((InternalEObject)newMarker).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.PROTECTED_AREA_BLOCK__MARKER, null, msgs);
			msgs = basicSetMarker(newMarker, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.PROTECTED_AREA_BLOCK__MARKER, newMarker, newMarker));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case M2tPackage.PROTECTED_AREA_BLOCK__MARKER:
				return basicSetMarker(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case M2tPackage.PROTECTED_AREA_BLOCK__MARKER:
				return getMarker();
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
			case M2tPackage.PROTECTED_AREA_BLOCK__MARKER:
				setMarker((OclExpression)newValue);
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
			case M2tPackage.PROTECTED_AREA_BLOCK__MARKER:
				setMarker((OclExpression)null);
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
			case M2tPackage.PROTECTED_AREA_BLOCK__MARKER:
				return marker != null;
		}
		return super.eIsSet(featureID);
	}

} //ProtectedAreaBlockImpl
