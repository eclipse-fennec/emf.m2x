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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.m2m.model.ocl.IfExp;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.OclPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>If Exp</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.impl.IfExpImpl#getOwnedCondition <em>Owned Condition</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.impl.IfExpImpl#getOwnedThen <em>Owned Then</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.ocl.impl.IfExpImpl#getOwnedElse <em>Owned Else</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IfExpImpl extends OclExpressionImpl implements IfExp {
	/**
	 * The cached value of the '{@link #getOwnedCondition() <em>Owned Condition</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedCondition()
	 * @generated
	 * @ordered
	 */
	protected OclExpression ownedCondition;

	/**
	 * The cached value of the '{@link #getOwnedThen() <em>Owned Then</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedThen()
	 * @generated
	 * @ordered
	 */
	protected OclExpression ownedThen;

	/**
	 * The cached value of the '{@link #getOwnedElse() <em>Owned Else</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedElse()
	 * @generated
	 * @ordered
	 */
	protected OclExpression ownedElse;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IfExpImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OclPackage.Literals.IF_EXP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getOwnedCondition() {
		return ownedCondition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwnedCondition(OclExpression newOwnedCondition, NotificationChain msgs) {
		OclExpression oldOwnedCondition = ownedCondition;
		ownedCondition = newOwnedCondition;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OclPackage.IF_EXP__OWNED_CONDITION, oldOwnedCondition, newOwnedCondition);
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
	public void setOwnedCondition(OclExpression newOwnedCondition) {
		if (newOwnedCondition != ownedCondition) {
			NotificationChain msgs = null;
			if (ownedCondition != null)
				msgs = ((InternalEObject)ownedCondition).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OclPackage.IF_EXP__OWNED_CONDITION, null, msgs);
			if (newOwnedCondition != null)
				msgs = ((InternalEObject)newOwnedCondition).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OclPackage.IF_EXP__OWNED_CONDITION, null, msgs);
			msgs = basicSetOwnedCondition(newOwnedCondition, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.IF_EXP__OWNED_CONDITION, newOwnedCondition, newOwnedCondition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getOwnedThen() {
		return ownedThen;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwnedThen(OclExpression newOwnedThen, NotificationChain msgs) {
		OclExpression oldOwnedThen = ownedThen;
		ownedThen = newOwnedThen;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OclPackage.IF_EXP__OWNED_THEN, oldOwnedThen, newOwnedThen);
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
	public void setOwnedThen(OclExpression newOwnedThen) {
		if (newOwnedThen != ownedThen) {
			NotificationChain msgs = null;
			if (ownedThen != null)
				msgs = ((InternalEObject)ownedThen).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OclPackage.IF_EXP__OWNED_THEN, null, msgs);
			if (newOwnedThen != null)
				msgs = ((InternalEObject)newOwnedThen).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OclPackage.IF_EXP__OWNED_THEN, null, msgs);
			msgs = basicSetOwnedThen(newOwnedThen, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.IF_EXP__OWNED_THEN, newOwnedThen, newOwnedThen));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getOwnedElse() {
		return ownedElse;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetOwnedElse(OclExpression newOwnedElse, NotificationChain msgs) {
		OclExpression oldOwnedElse = ownedElse;
		ownedElse = newOwnedElse;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OclPackage.IF_EXP__OWNED_ELSE, oldOwnedElse, newOwnedElse);
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
	public void setOwnedElse(OclExpression newOwnedElse) {
		if (newOwnedElse != ownedElse) {
			NotificationChain msgs = null;
			if (ownedElse != null)
				msgs = ((InternalEObject)ownedElse).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OclPackage.IF_EXP__OWNED_ELSE, null, msgs);
			if (newOwnedElse != null)
				msgs = ((InternalEObject)newOwnedElse).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OclPackage.IF_EXP__OWNED_ELSE, null, msgs);
			msgs = basicSetOwnedElse(newOwnedElse, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.IF_EXP__OWNED_ELSE, newOwnedElse, newOwnedElse));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OclPackage.IF_EXP__OWNED_CONDITION:
				return basicSetOwnedCondition(null, msgs);
			case OclPackage.IF_EXP__OWNED_THEN:
				return basicSetOwnedThen(null, msgs);
			case OclPackage.IF_EXP__OWNED_ELSE:
				return basicSetOwnedElse(null, msgs);
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
			case OclPackage.IF_EXP__OWNED_CONDITION:
				return getOwnedCondition();
			case OclPackage.IF_EXP__OWNED_THEN:
				return getOwnedThen();
			case OclPackage.IF_EXP__OWNED_ELSE:
				return getOwnedElse();
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
			case OclPackage.IF_EXP__OWNED_CONDITION:
				setOwnedCondition((OclExpression)newValue);
				return;
			case OclPackage.IF_EXP__OWNED_THEN:
				setOwnedThen((OclExpression)newValue);
				return;
			case OclPackage.IF_EXP__OWNED_ELSE:
				setOwnedElse((OclExpression)newValue);
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
			case OclPackage.IF_EXP__OWNED_CONDITION:
				setOwnedCondition((OclExpression)null);
				return;
			case OclPackage.IF_EXP__OWNED_THEN:
				setOwnedThen((OclExpression)null);
				return;
			case OclPackage.IF_EXP__OWNED_ELSE:
				setOwnedElse((OclExpression)null);
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
			case OclPackage.IF_EXP__OWNED_CONDITION:
				return ownedCondition != null;
			case OclPackage.IF_EXP__OWNED_THEN:
				return ownedThen != null;
			case OclPackage.IF_EXP__OWNED_ELSE:
				return ownedElse != null;
		}
		return super.eIsSet(featureID);
	}

} //IfExpImpl
