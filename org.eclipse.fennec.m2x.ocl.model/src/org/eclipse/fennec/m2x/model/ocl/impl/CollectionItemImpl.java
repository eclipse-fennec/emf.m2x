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

import org.eclipse.fennec.m2x.model.ocl.CollectionItem;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Collection Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.CollectionItemImpl#getOwnedItem <em>Owned Item</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CollectionItemImpl extends CollectionLiteralPartImpl implements CollectionItem {
	/**
	 * The cached value of the '{@link #getOwnedItem() <em>Owned Item</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedItem()
	 * @generated
	 * @ordered
	 */
	protected OclExpression ownedItem;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CollectionItemImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OclPackage.Literals.COLLECTION_ITEM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getOwnedItem() {
		return ownedItem;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwnedItem(OclExpression newOwnedItem, NotificationChain msgs) {
		OclExpression oldOwnedItem = ownedItem;
		ownedItem = newOwnedItem;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OclPackage.COLLECTION_ITEM__OWNED_ITEM, oldOwnedItem, newOwnedItem);
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
	public void setOwnedItem(OclExpression newOwnedItem) {
		if (newOwnedItem != ownedItem) {
			NotificationChain msgs = null;
			if (ownedItem != null)
				msgs = ((InternalEObject)ownedItem).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OclPackage.COLLECTION_ITEM__OWNED_ITEM, null, msgs);
			if (newOwnedItem != null)
				msgs = ((InternalEObject)newOwnedItem).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OclPackage.COLLECTION_ITEM__OWNED_ITEM, null, msgs);
			msgs = basicSetOwnedItem(newOwnedItem, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.COLLECTION_ITEM__OWNED_ITEM, newOwnedItem, newOwnedItem));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OclPackage.COLLECTION_ITEM__OWNED_ITEM:
				return basicSetOwnedItem(null, msgs);
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
			case OclPackage.COLLECTION_ITEM__OWNED_ITEM:
				return getOwnedItem();
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
			case OclPackage.COLLECTION_ITEM__OWNED_ITEM:
				setOwnedItem((OclExpression)newValue);
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
			case OclPackage.COLLECTION_ITEM__OWNED_ITEM:
				setOwnedItem((OclExpression)null);
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
			case OclPackage.COLLECTION_ITEM__OWNED_ITEM:
				return ownedItem != null;
		}
		return super.eIsSet(featureID);
	}

} //CollectionItemImpl
