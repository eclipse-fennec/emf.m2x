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

import org.eclipse.emf.ecore.impl.EModelElementImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.Status;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Status</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.impl.StatusImpl#isSucceeded <em>Succeeded</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.impl.StatusImpl#isFailed <em>Failed</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.impl.StatusImpl#getRaisedException <em>Raised Exception</em>}</li>
 * </ul>
 *
 * @generated
 */
public class StatusImpl extends EModelElementImpl implements Status {
	/**
	 * The default value of the '{@link #isSucceeded() <em>Succeeded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSucceeded()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SUCCEEDED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSucceeded() <em>Succeeded</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSucceeded()
	 * @generated
	 * @ordered
	 */
	protected boolean succeeded = SUCCEEDED_EDEFAULT;

	/**
	 * The default value of the '{@link #isFailed() <em>Failed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFailed()
	 * @generated
	 * @ordered
	 */
	protected static final boolean FAILED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isFailed() <em>Failed</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isFailed()
	 * @generated
	 * @ordered
	 */
	protected boolean failed = FAILED_EDEFAULT;

	/**
	 * The default value of the '{@link #getRaisedException() <em>Raised Exception</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRaisedException()
	 * @generated
	 * @ordered
	 */
	protected static final Exception RAISED_EXCEPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getRaisedException() <em>Raised Exception</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getRaisedException()
	 * @generated
	 * @ordered
	 */
	protected Exception raisedException = RAISED_EXCEPTION_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected StatusImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QvtOperationalPackage.Literals.STATUS;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSucceeded() {
		return succeeded;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSucceeded(boolean newSucceeded) {
		boolean oldSucceeded = succeeded;
		succeeded = newSucceeded;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtOperationalPackage.STATUS__SUCCEEDED, oldSucceeded, succeeded));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isFailed() {
		return failed;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFailed(boolean newFailed) {
		boolean oldFailed = failed;
		failed = newFailed;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtOperationalPackage.STATUS__FAILED, oldFailed, failed));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Exception getRaisedException() {
		return raisedException;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRaisedException(Exception newRaisedException) {
		Exception oldRaisedException = raisedException;
		raisedException = newRaisedException;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtOperationalPackage.STATUS__RAISED_EXCEPTION, oldRaisedException, raisedException));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case QvtOperationalPackage.STATUS__SUCCEEDED:
				return isSucceeded();
			case QvtOperationalPackage.STATUS__FAILED:
				return isFailed();
			case QvtOperationalPackage.STATUS__RAISED_EXCEPTION:
				return getRaisedException();
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
			case QvtOperationalPackage.STATUS__SUCCEEDED:
				setSucceeded((Boolean)newValue);
				return;
			case QvtOperationalPackage.STATUS__FAILED:
				setFailed((Boolean)newValue);
				return;
			case QvtOperationalPackage.STATUS__RAISED_EXCEPTION:
				setRaisedException((Exception)newValue);
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
			case QvtOperationalPackage.STATUS__SUCCEEDED:
				setSucceeded(SUCCEEDED_EDEFAULT);
				return;
			case QvtOperationalPackage.STATUS__FAILED:
				setFailed(FAILED_EDEFAULT);
				return;
			case QvtOperationalPackage.STATUS__RAISED_EXCEPTION:
				setRaisedException(RAISED_EXCEPTION_EDEFAULT);
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
			case QvtOperationalPackage.STATUS__SUCCEEDED:
				return succeeded != SUCCEEDED_EDEFAULT;
			case QvtOperationalPackage.STATUS__FAILED:
				return failed != FAILED_EDEFAULT;
			case QvtOperationalPackage.STATUS__RAISED_EXCEPTION:
				return RAISED_EXCEPTION_EDEFAULT == null ? raisedException != null : !RAISED_EXCEPTION_EDEFAULT.equals(raisedException);
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
		result.append(" (succeeded: ");
		result.append(succeeded);
		result.append(", failed: ");
		result.append(failed);
		result.append(", raisedException: ");
		result.append(raisedException);
		result.append(')');
		return result.toString();
	}

} //StatusImpl
