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
package org.eclipse.fennec.m2m.model.ocl.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2m.model.ocl.MessageExp;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.OclPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Message Exp</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.impl.MessageExpImpl#getOwnedTarget <em>Owned Target</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.impl.MessageExpImpl#getOwnedArguments <em>Owned Arguments</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.impl.MessageExpImpl#getOwnedCalledOperation <em>Owned Called Operation</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.impl.MessageExpImpl#getOwnedSentSignal <em>Owned Sent Signal</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MessageExpImpl extends OclExpressionImpl implements MessageExp {
	/**
	 * The cached value of the '{@link #getOwnedTarget() <em>Owned Target</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedTarget()
	 * @generated
	 * @ordered
	 */
	protected OclExpression ownedTarget;

	/**
	 * The cached value of the '{@link #getOwnedArguments() <em>Owned Arguments</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedArguments()
	 * @generated
	 * @ordered
	 */
	protected EList<OclExpression> ownedArguments;

	/**
	 * The cached value of the '{@link #getOwnedCalledOperation() <em>Owned Called Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedCalledOperation()
	 * @generated
	 * @ordered
	 */
	protected EOperation ownedCalledOperation;

	/**
	 * The cached value of the '{@link #getOwnedSentSignal() <em>Owned Sent Signal</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedSentSignal()
	 * @generated
	 * @ordered
	 */
	protected EClassifier ownedSentSignal;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MessageExpImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OclPackage.Literals.MESSAGE_EXP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getOwnedTarget() {
		return ownedTarget;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwnedTarget(OclExpression newOwnedTarget, NotificationChain msgs) {
		OclExpression oldOwnedTarget = ownedTarget;
		ownedTarget = newOwnedTarget;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OclPackage.MESSAGE_EXP__OWNED_TARGET, oldOwnedTarget, newOwnedTarget);
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
	public void setOwnedTarget(OclExpression newOwnedTarget) {
		if (newOwnedTarget != ownedTarget) {
			NotificationChain msgs = null;
			if (ownedTarget != null)
				msgs = ((InternalEObject)ownedTarget).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OclPackage.MESSAGE_EXP__OWNED_TARGET, null, msgs);
			if (newOwnedTarget != null)
				msgs = ((InternalEObject)newOwnedTarget).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OclPackage.MESSAGE_EXP__OWNED_TARGET, null, msgs);
			msgs = basicSetOwnedTarget(newOwnedTarget, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.MESSAGE_EXP__OWNED_TARGET, newOwnedTarget, newOwnedTarget));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OclExpression> getOwnedArguments() {
		if (ownedArguments == null) {
			ownedArguments = new EObjectContainmentEList<OclExpression>(OclExpression.class, this, OclPackage.MESSAGE_EXP__OWNED_ARGUMENTS);
		}
		return ownedArguments;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getOwnedCalledOperation() {
		if (ownedCalledOperation != null && ownedCalledOperation.eIsProxy()) {
			InternalEObject oldOwnedCalledOperation = (InternalEObject)ownedCalledOperation;
			ownedCalledOperation = (EOperation)eResolveProxy(oldOwnedCalledOperation);
			if (ownedCalledOperation != oldOwnedCalledOperation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OclPackage.MESSAGE_EXP__OWNED_CALLED_OPERATION, oldOwnedCalledOperation, ownedCalledOperation));
			}
		}
		return ownedCalledOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation basicGetOwnedCalledOperation() {
		return ownedCalledOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOwnedCalledOperation(EOperation newOwnedCalledOperation) {
		EOperation oldOwnedCalledOperation = ownedCalledOperation;
		ownedCalledOperation = newOwnedCalledOperation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.MESSAGE_EXP__OWNED_CALLED_OPERATION, oldOwnedCalledOperation, ownedCalledOperation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClassifier getOwnedSentSignal() {
		if (ownedSentSignal != null && ownedSentSignal.eIsProxy()) {
			InternalEObject oldOwnedSentSignal = (InternalEObject)ownedSentSignal;
			ownedSentSignal = (EClassifier)eResolveProxy(oldOwnedSentSignal);
			if (ownedSentSignal != oldOwnedSentSignal) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OclPackage.MESSAGE_EXP__OWNED_SENT_SIGNAL, oldOwnedSentSignal, ownedSentSignal));
			}
		}
		return ownedSentSignal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClassifier basicGetOwnedSentSignal() {
		return ownedSentSignal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOwnedSentSignal(EClassifier newOwnedSentSignal) {
		EClassifier oldOwnedSentSignal = ownedSentSignal;
		ownedSentSignal = newOwnedSentSignal;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.MESSAGE_EXP__OWNED_SENT_SIGNAL, oldOwnedSentSignal, ownedSentSignal));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OclPackage.MESSAGE_EXP__OWNED_TARGET:
				return basicSetOwnedTarget(null, msgs);
			case OclPackage.MESSAGE_EXP__OWNED_ARGUMENTS:
				return ((InternalEList<?>)getOwnedArguments()).basicRemove(otherEnd, msgs);
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
			case OclPackage.MESSAGE_EXP__OWNED_TARGET:
				return getOwnedTarget();
			case OclPackage.MESSAGE_EXP__OWNED_ARGUMENTS:
				return getOwnedArguments();
			case OclPackage.MESSAGE_EXP__OWNED_CALLED_OPERATION:
				if (resolve) return getOwnedCalledOperation();
				return basicGetOwnedCalledOperation();
			case OclPackage.MESSAGE_EXP__OWNED_SENT_SIGNAL:
				if (resolve) return getOwnedSentSignal();
				return basicGetOwnedSentSignal();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case OclPackage.MESSAGE_EXP__OWNED_TARGET:
				setOwnedTarget((OclExpression)newValue);
				return;
			case OclPackage.MESSAGE_EXP__OWNED_ARGUMENTS:
				getOwnedArguments().clear();
				getOwnedArguments().addAll((Collection<? extends OclExpression>)newValue);
				return;
			case OclPackage.MESSAGE_EXP__OWNED_CALLED_OPERATION:
				setOwnedCalledOperation((EOperation)newValue);
				return;
			case OclPackage.MESSAGE_EXP__OWNED_SENT_SIGNAL:
				setOwnedSentSignal((EClassifier)newValue);
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
			case OclPackage.MESSAGE_EXP__OWNED_TARGET:
				setOwnedTarget((OclExpression)null);
				return;
			case OclPackage.MESSAGE_EXP__OWNED_ARGUMENTS:
				getOwnedArguments().clear();
				return;
			case OclPackage.MESSAGE_EXP__OWNED_CALLED_OPERATION:
				setOwnedCalledOperation((EOperation)null);
				return;
			case OclPackage.MESSAGE_EXP__OWNED_SENT_SIGNAL:
				setOwnedSentSignal((EClassifier)null);
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
			case OclPackage.MESSAGE_EXP__OWNED_TARGET:
				return ownedTarget != null;
			case OclPackage.MESSAGE_EXP__OWNED_ARGUMENTS:
				return ownedArguments != null && !ownedArguments.isEmpty();
			case OclPackage.MESSAGE_EXP__OWNED_CALLED_OPERATION:
				return ownedCalledOperation != null;
			case OclPackage.MESSAGE_EXP__OWNED_SENT_SIGNAL:
				return ownedSentSignal != null;
		}
		return super.eIsSet(featureID);
	}

} //MessageExpImpl
