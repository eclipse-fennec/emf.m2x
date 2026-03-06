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
package org.eclipse.fennec.m2x.model.qvtbase.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENamedElementImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.fennec.m2x.model.qvtbase.Domain;
import org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Domain</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.impl.DomainImpl#isIsCheckable <em>Is Checkable</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.impl.DomainImpl#isIsEnforceable <em>Is Enforceable</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.impl.DomainImpl#getRule <em>Rule</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtbase.impl.DomainImpl#getTypedModel <em>Typed Model</em>}</li>
 * </ul>
 *
 * @generated
 */
public abstract class DomainImpl extends ENamedElementImpl implements Domain {
	/**
	 * The default value of the '{@link #isIsCheckable() <em>Is Checkable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsCheckable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_CHECKABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsCheckable() <em>Is Checkable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsCheckable()
	 * @generated
	 * @ordered
	 */
	protected boolean isCheckable = IS_CHECKABLE_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsEnforceable() <em>Is Enforceable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEnforceable()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_ENFORCEABLE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsEnforceable() <em>Is Enforceable</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsEnforceable()
	 * @generated
	 * @ordered
	 */
	protected boolean isEnforceable = IS_ENFORCEABLE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getTypedModel() <em>Typed Model</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTypedModel()
	 * @generated
	 * @ordered
	 */
	protected TypedModel typedModel;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DomainImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QvtbasePackage.Literals.DOMAIN;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIsCheckable() {
		return isCheckable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsCheckable(boolean newIsCheckable) {
		boolean oldIsCheckable = isCheckable;
		isCheckable = newIsCheckable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtbasePackage.DOMAIN__IS_CHECKABLE, oldIsCheckable, isCheckable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIsEnforceable() {
		return isEnforceable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsEnforceable(boolean newIsEnforceable) {
		boolean oldIsEnforceable = isEnforceable;
		isEnforceable = newIsEnforceable;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtbasePackage.DOMAIN__IS_ENFORCEABLE, oldIsEnforceable, isEnforceable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Rule getRule() {
		if (eContainerFeatureID() != QvtbasePackage.DOMAIN__RULE) return null;
		return (Rule)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetRule(Rule newRule, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newRule, QvtbasePackage.DOMAIN__RULE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setRule(Rule newRule) {
		if (newRule != eInternalContainer() || (eContainerFeatureID() != QvtbasePackage.DOMAIN__RULE && newRule != null)) {
			if (EcoreUtil.isAncestor(this, newRule))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newRule != null)
				msgs = ((InternalEObject)newRule).eInverseAdd(this, QvtbasePackage.RULE__DOMAIN, Rule.class, msgs);
			msgs = basicSetRule(newRule, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtbasePackage.DOMAIN__RULE, newRule, newRule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TypedModel getTypedModel() {
		if (typedModel != null && typedModel.eIsProxy()) {
			InternalEObject oldTypedModel = (InternalEObject)typedModel;
			typedModel = (TypedModel)eResolveProxy(oldTypedModel);
			if (typedModel != oldTypedModel) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, QvtbasePackage.DOMAIN__TYPED_MODEL, oldTypedModel, typedModel));
			}
		}
		return typedModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TypedModel basicGetTypedModel() {
		return typedModel;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTypedModel(TypedModel newTypedModel) {
		TypedModel oldTypedModel = typedModel;
		typedModel = newTypedModel;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtbasePackage.DOMAIN__TYPED_MODEL, oldTypedModel, typedModel));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QvtbasePackage.DOMAIN__RULE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetRule((Rule)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QvtbasePackage.DOMAIN__RULE:
				return basicSetRule(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case QvtbasePackage.DOMAIN__RULE:
				return eInternalContainer().eInverseRemove(this, QvtbasePackage.RULE__DOMAIN, Rule.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case QvtbasePackage.DOMAIN__IS_CHECKABLE:
				return isIsCheckable();
			case QvtbasePackage.DOMAIN__IS_ENFORCEABLE:
				return isIsEnforceable();
			case QvtbasePackage.DOMAIN__RULE:
				return getRule();
			case QvtbasePackage.DOMAIN__TYPED_MODEL:
				if (resolve) return getTypedModel();
				return basicGetTypedModel();
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
			case QvtbasePackage.DOMAIN__IS_CHECKABLE:
				setIsCheckable((Boolean)newValue);
				return;
			case QvtbasePackage.DOMAIN__IS_ENFORCEABLE:
				setIsEnforceable((Boolean)newValue);
				return;
			case QvtbasePackage.DOMAIN__RULE:
				setRule((Rule)newValue);
				return;
			case QvtbasePackage.DOMAIN__TYPED_MODEL:
				setTypedModel((TypedModel)newValue);
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
			case QvtbasePackage.DOMAIN__IS_CHECKABLE:
				setIsCheckable(IS_CHECKABLE_EDEFAULT);
				return;
			case QvtbasePackage.DOMAIN__IS_ENFORCEABLE:
				setIsEnforceable(IS_ENFORCEABLE_EDEFAULT);
				return;
			case QvtbasePackage.DOMAIN__RULE:
				setRule((Rule)null);
				return;
			case QvtbasePackage.DOMAIN__TYPED_MODEL:
				setTypedModel((TypedModel)null);
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
			case QvtbasePackage.DOMAIN__IS_CHECKABLE:
				return isCheckable != IS_CHECKABLE_EDEFAULT;
			case QvtbasePackage.DOMAIN__IS_ENFORCEABLE:
				return isEnforceable != IS_ENFORCEABLE_EDEFAULT;
			case QvtbasePackage.DOMAIN__RULE:
				return getRule() != null;
			case QvtbasePackage.DOMAIN__TYPED_MODEL:
				return typedModel != null;
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
		result.append(" (isCheckable: ");
		result.append(isCheckable);
		result.append(", isEnforceable: ");
		result.append(isEnforceable);
		result.append(')');
		return result.toString();
	}

} //DomainImpl
