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
package org.eclipse.fennec.m2x.model.imperativeocl.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2x.model.imperativeocl.AssignExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Assign Exp</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.impl.AssignExpImpl#isIsReset <em>Is Reset</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.impl.AssignExpImpl#isIsSubtract <em>Is Subtract</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.impl.AssignExpImpl#getLeft <em>Left</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.impl.AssignExpImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.impl.AssignExpImpl#getDefaultValue <em>Default Value</em>}</li>
 * </ul>
 *
 * @generated
 */
public class AssignExpImpl extends ImperativeExpressionImpl implements AssignExp {
	/**
	 * The default value of the '{@link #isIsReset() <em>Is Reset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsReset()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_RESET_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsReset() <em>Is Reset</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsReset()
	 * @generated
	 * @ordered
	 */
	protected boolean isReset = IS_RESET_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsSubtract() <em>Is Subtract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsSubtract()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_SUBTRACT_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsSubtract() <em>Is Subtract</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsSubtract()
	 * @generated
	 * @ordered
	 */
	protected boolean isSubtract = IS_SUBTRACT_EDEFAULT;

	/**
	 * The cached value of the '{@link #getLeft() <em>Left</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLeft()
	 * @generated
	 * @ordered
	 */
	protected OclExpression left;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected EList<OclExpression> value;

	/**
	 * The cached value of the '{@link #getDefaultValue() <em>Default Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefaultValue()
	 * @generated
	 * @ordered
	 */
	protected OclExpression defaultValue;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected AssignExpImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ImperativeOclPackage.Literals.ASSIGN_EXP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIsReset() {
		return isReset;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsReset(boolean newIsReset) {
		boolean oldIsReset = isReset;
		isReset = newIsReset;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ImperativeOclPackage.ASSIGN_EXP__IS_RESET, oldIsReset, isReset));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIsSubtract() {
		return isSubtract;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsSubtract(boolean newIsSubtract) {
		boolean oldIsSubtract = isSubtract;
		isSubtract = newIsSubtract;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ImperativeOclPackage.ASSIGN_EXP__IS_SUBTRACT, oldIsSubtract, isSubtract));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getLeft() {
		return left;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLeft(OclExpression newLeft, NotificationChain msgs) {
		OclExpression oldLeft = left;
		left = newLeft;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ImperativeOclPackage.ASSIGN_EXP__LEFT, oldLeft, newLeft);
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
	public void setLeft(OclExpression newLeft) {
		if (newLeft != left) {
			NotificationChain msgs = null;
			if (left != null)
				msgs = ((InternalEObject)left).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ImperativeOclPackage.ASSIGN_EXP__LEFT, null, msgs);
			if (newLeft != null)
				msgs = ((InternalEObject)newLeft).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ImperativeOclPackage.ASSIGN_EXP__LEFT, null, msgs);
			msgs = basicSetLeft(newLeft, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ImperativeOclPackage.ASSIGN_EXP__LEFT, newLeft, newLeft));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OclExpression> getValue() {
		if (value == null) {
			value = new EObjectContainmentEList<OclExpression>(OclExpression.class, this, ImperativeOclPackage.ASSIGN_EXP__VALUE);
		}
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getDefaultValue() {
		return defaultValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetDefaultValue(OclExpression newDefaultValue, NotificationChain msgs) {
		OclExpression oldDefaultValue = defaultValue;
		defaultValue = newDefaultValue;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, ImperativeOclPackage.ASSIGN_EXP__DEFAULT_VALUE, oldDefaultValue, newDefaultValue);
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
	public void setDefaultValue(OclExpression newDefaultValue) {
		if (newDefaultValue != defaultValue) {
			NotificationChain msgs = null;
			if (defaultValue != null)
				msgs = ((InternalEObject)defaultValue).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - ImperativeOclPackage.ASSIGN_EXP__DEFAULT_VALUE, null, msgs);
			if (newDefaultValue != null)
				msgs = ((InternalEObject)newDefaultValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - ImperativeOclPackage.ASSIGN_EXP__DEFAULT_VALUE, null, msgs);
			msgs = basicSetDefaultValue(newDefaultValue, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ImperativeOclPackage.ASSIGN_EXP__DEFAULT_VALUE, newDefaultValue, newDefaultValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case ImperativeOclPackage.ASSIGN_EXP__LEFT:
				return basicSetLeft(null, msgs);
			case ImperativeOclPackage.ASSIGN_EXP__VALUE:
				return ((InternalEList<?>)getValue()).basicRemove(otherEnd, msgs);
			case ImperativeOclPackage.ASSIGN_EXP__DEFAULT_VALUE:
				return basicSetDefaultValue(null, msgs);
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
			case ImperativeOclPackage.ASSIGN_EXP__IS_RESET:
				return isIsReset();
			case ImperativeOclPackage.ASSIGN_EXP__IS_SUBTRACT:
				return isIsSubtract();
			case ImperativeOclPackage.ASSIGN_EXP__LEFT:
				return getLeft();
			case ImperativeOclPackage.ASSIGN_EXP__VALUE:
				return getValue();
			case ImperativeOclPackage.ASSIGN_EXP__DEFAULT_VALUE:
				return getDefaultValue();
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
			case ImperativeOclPackage.ASSIGN_EXP__IS_RESET:
				setIsReset((Boolean)newValue);
				return;
			case ImperativeOclPackage.ASSIGN_EXP__IS_SUBTRACT:
				setIsSubtract((Boolean)newValue);
				return;
			case ImperativeOclPackage.ASSIGN_EXP__LEFT:
				setLeft((OclExpression)newValue);
				return;
			case ImperativeOclPackage.ASSIGN_EXP__VALUE:
				getValue().clear();
				getValue().addAll((Collection<? extends OclExpression>)newValue);
				return;
			case ImperativeOclPackage.ASSIGN_EXP__DEFAULT_VALUE:
				setDefaultValue((OclExpression)newValue);
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
			case ImperativeOclPackage.ASSIGN_EXP__IS_RESET:
				setIsReset(IS_RESET_EDEFAULT);
				return;
			case ImperativeOclPackage.ASSIGN_EXP__IS_SUBTRACT:
				setIsSubtract(IS_SUBTRACT_EDEFAULT);
				return;
			case ImperativeOclPackage.ASSIGN_EXP__LEFT:
				setLeft((OclExpression)null);
				return;
			case ImperativeOclPackage.ASSIGN_EXP__VALUE:
				getValue().clear();
				return;
			case ImperativeOclPackage.ASSIGN_EXP__DEFAULT_VALUE:
				setDefaultValue((OclExpression)null);
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
			case ImperativeOclPackage.ASSIGN_EXP__IS_RESET:
				return isReset != IS_RESET_EDEFAULT;
			case ImperativeOclPackage.ASSIGN_EXP__IS_SUBTRACT:
				return isSubtract != IS_SUBTRACT_EDEFAULT;
			case ImperativeOclPackage.ASSIGN_EXP__LEFT:
				return left != null;
			case ImperativeOclPackage.ASSIGN_EXP__VALUE:
				return value != null && !value.isEmpty();
			case ImperativeOclPackage.ASSIGN_EXP__DEFAULT_VALUE:
				return defaultValue != null;
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
		result.append(" (isReset: ");
		result.append(isReset);
		result.append(", isSubtract: ");
		result.append(isSubtract);
		result.append(')');
		return result.toString();
	}

} //AssignExpImpl
