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
package org.eclipse.fennec.m2x.model.qvtoperational.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.m2x.model.ocl.CollectionKind;

import org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Model Parameter</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.impl.ModelParameterImpl#getCollectionKind <em>Collection Kind</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModelParameterImpl extends VarParameterImpl implements ModelParameter {
	/**
	 * The default value of the '{@link #getCollectionKind() <em>Collection Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionKind()
	 * @generated
	 * @ordered
	 */
	protected static final CollectionKind COLLECTION_KIND_EDEFAULT = CollectionKind.COLLECTION;

	/**
	 * The cached value of the '{@link #getCollectionKind() <em>Collection Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionKind()
	 * @generated
	 * @ordered
	 */
	protected CollectionKind collectionKind = COLLECTION_KIND_EDEFAULT;

	/**
	 * This is true if the Collection Kind attribute has been set.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 * @ordered
	 */
	protected boolean collectionKindESet;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModelParameterImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QvtOperationalPackage.Literals.MODEL_PARAMETER;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CollectionKind getCollectionKind() {
		return collectionKind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCollectionKind(CollectionKind newCollectionKind) {
		CollectionKind oldCollectionKind = collectionKind;
		collectionKind = newCollectionKind == null ? COLLECTION_KIND_EDEFAULT : newCollectionKind;
		boolean oldCollectionKindESet = collectionKindESet;
		collectionKindESet = true;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtOperationalPackage.MODEL_PARAMETER__COLLECTION_KIND, oldCollectionKind, collectionKind, !oldCollectionKindESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void unsetCollectionKind() {
		CollectionKind oldCollectionKind = collectionKind;
		boolean oldCollectionKindESet = collectionKindESet;
		collectionKind = COLLECTION_KIND_EDEFAULT;
		collectionKindESet = false;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.UNSET, QvtOperationalPackage.MODEL_PARAMETER__COLLECTION_KIND, oldCollectionKind, COLLECTION_KIND_EDEFAULT, oldCollectionKindESet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSetCollectionKind() {
		return collectionKindESet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case QvtOperationalPackage.MODEL_PARAMETER__COLLECTION_KIND:
				return getCollectionKind();
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
			case QvtOperationalPackage.MODEL_PARAMETER__COLLECTION_KIND:
				setCollectionKind((CollectionKind)newValue);
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
			case QvtOperationalPackage.MODEL_PARAMETER__COLLECTION_KIND:
				unsetCollectionKind();
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
			case QvtOperationalPackage.MODEL_PARAMETER__COLLECTION_KIND:
				return isSetCollectionKind();
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (collectionKind: ");
		if (collectionKindESet) result.append(collectionKind); else result.append("<unset>");
		result.append(')');
		return result.toString();
	}

} //ModelParameterImpl
