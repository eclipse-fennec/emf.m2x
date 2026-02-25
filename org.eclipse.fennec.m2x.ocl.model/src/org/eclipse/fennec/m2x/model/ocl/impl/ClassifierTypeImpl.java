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
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Classifier Type</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.ClassifierTypeImpl#getReferredClassifier <em>Referred Classifier</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ClassifierTypeImpl extends OclTypeImpl implements ClassifierType {
	/**
	 * The cached value of the '{@link #getReferredClassifier() <em>Referred Classifier</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferredClassifier()
	 * @generated
	 * @ordered
	 */
	protected EClassifier referredClassifier;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ClassifierTypeImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OclPackage.Literals.CLASSIFIER_TYPE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClassifier getReferredClassifier() {
		if (referredClassifier != null && referredClassifier.eIsProxy()) {
			InternalEObject oldReferredClassifier = (InternalEObject)referredClassifier;
			referredClassifier = (EClassifier)eResolveProxy(oldReferredClassifier);
			if (referredClassifier != oldReferredClassifier) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OclPackage.CLASSIFIER_TYPE__REFERRED_CLASSIFIER, oldReferredClassifier, referredClassifier));
			}
		}
		return referredClassifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClassifier basicGetReferredClassifier() {
		return referredClassifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReferredClassifier(EClassifier newReferredClassifier) {
		EClassifier oldReferredClassifier = referredClassifier;
		referredClassifier = newReferredClassifier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.CLASSIFIER_TYPE__REFERRED_CLASSIFIER, oldReferredClassifier, referredClassifier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case OclPackage.CLASSIFIER_TYPE__REFERRED_CLASSIFIER:
				if (resolve) return getReferredClassifier();
				return basicGetReferredClassifier();
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
			case OclPackage.CLASSIFIER_TYPE__REFERRED_CLASSIFIER:
				setReferredClassifier((EClassifier)newValue);
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
			case OclPackage.CLASSIFIER_TYPE__REFERRED_CLASSIFIER:
				setReferredClassifier((EClassifier)null);
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
			case OclPackage.CLASSIFIER_TYPE__REFERRED_CLASSIFIER:
				return referredClassifier != null;
		}
		return super.eIsSet(featureID);
	}

} //ClassifierTypeImpl
