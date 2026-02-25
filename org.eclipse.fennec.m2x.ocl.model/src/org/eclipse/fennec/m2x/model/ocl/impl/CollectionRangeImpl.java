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
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.m2x.model.ocl.CollectionRange;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Collection Range</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.CollectionRangeImpl#getOwnedFirst <em>Owned First</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.CollectionRangeImpl#getOwnedLast <em>Owned Last</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CollectionRangeImpl extends CollectionLiteralPartImpl implements CollectionRange {
	/**
	 * The cached value of the '{@link #getOwnedFirst() <em>Owned First</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedFirst()
	 * @generated
	 * @ordered
	 */
	protected OclExpression ownedFirst;

	/**
	 * The cached value of the '{@link #getOwnedLast() <em>Owned Last</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedLast()
	 * @generated
	 * @ordered
	 */
	protected OclExpression ownedLast;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CollectionRangeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OclPackage.Literals.COLLECTION_RANGE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getOwnedFirst() {
		return ownedFirst;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwnedFirst(OclExpression newOwnedFirst, NotificationChain msgs) {
		OclExpression oldOwnedFirst = ownedFirst;
		ownedFirst = newOwnedFirst;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OclPackage.COLLECTION_RANGE__OWNED_FIRST, oldOwnedFirst, newOwnedFirst);
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
	public void setOwnedFirst(OclExpression newOwnedFirst) {
		if (newOwnedFirst != ownedFirst) {
			NotificationChain msgs = null;
			if (ownedFirst != null)
				msgs = ((InternalEObject)ownedFirst).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OclPackage.COLLECTION_RANGE__OWNED_FIRST, null, msgs);
			if (newOwnedFirst != null)
				msgs = ((InternalEObject)newOwnedFirst).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OclPackage.COLLECTION_RANGE__OWNED_FIRST, null, msgs);
			msgs = basicSetOwnedFirst(newOwnedFirst, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.COLLECTION_RANGE__OWNED_FIRST, newOwnedFirst, newOwnedFirst));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getOwnedLast() {
		return ownedLast;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwnedLast(OclExpression newOwnedLast, NotificationChain msgs) {
		OclExpression oldOwnedLast = ownedLast;
		ownedLast = newOwnedLast;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OclPackage.COLLECTION_RANGE__OWNED_LAST, oldOwnedLast, newOwnedLast);
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
	public void setOwnedLast(OclExpression newOwnedLast) {
		if (newOwnedLast != ownedLast) {
			NotificationChain msgs = null;
			if (ownedLast != null)
				msgs = ((InternalEObject)ownedLast).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OclPackage.COLLECTION_RANGE__OWNED_LAST, null, msgs);
			if (newOwnedLast != null)
				msgs = ((InternalEObject)newOwnedLast).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OclPackage.COLLECTION_RANGE__OWNED_LAST, null, msgs);
			msgs = basicSetOwnedLast(newOwnedLast, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.COLLECTION_RANGE__OWNED_LAST, newOwnedLast, newOwnedLast));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OclPackage.COLLECTION_RANGE__OWNED_FIRST:
				return basicSetOwnedFirst(null, msgs);
			case OclPackage.COLLECTION_RANGE__OWNED_LAST:
				return basicSetOwnedLast(null, msgs);
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
			case OclPackage.COLLECTION_RANGE__OWNED_FIRST:
				return getOwnedFirst();
			case OclPackage.COLLECTION_RANGE__OWNED_LAST:
				return getOwnedLast();
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
			case OclPackage.COLLECTION_RANGE__OWNED_FIRST:
				setOwnedFirst((OclExpression)newValue);
				return;
			case OclPackage.COLLECTION_RANGE__OWNED_LAST:
				setOwnedLast((OclExpression)newValue);
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
			case OclPackage.COLLECTION_RANGE__OWNED_FIRST:
				setOwnedFirst((OclExpression)null);
				return;
			case OclPackage.COLLECTION_RANGE__OWNED_LAST:
				setOwnedLast((OclExpression)null);
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
			case OclPackage.COLLECTION_RANGE__OWNED_FIRST:
				return ownedFirst != null;
			case OclPackage.COLLECTION_RANGE__OWNED_LAST:
				return ownedLast != null;
		}
		return super.eIsSet(featureID);
	}

} //CollectionRangeImpl
