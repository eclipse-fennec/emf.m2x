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
package org.eclipse.fennec.m2x.model.qvttemplate.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EModelElementImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Property Template Item</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.PropertyTemplateItemImpl#isIsOpposite <em>Is Opposite</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.PropertyTemplateItemImpl#getReferredProperty <em>Referred Property</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.PropertyTemplateItemImpl#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.impl.PropertyTemplateItemImpl#getObjContainer <em>Obj Container</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PropertyTemplateItemImpl extends EModelElementImpl implements PropertyTemplateItem {
	/**
	 * The default value of the '{@link #isIsOpposite() <em>Is Opposite</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsOpposite()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_OPPOSITE_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsOpposite() <em>Is Opposite</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsOpposite()
	 * @generated
	 * @ordered
	 */
	protected boolean isOpposite = IS_OPPOSITE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getReferredProperty() <em>Referred Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReferredProperty()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature referredProperty;

	/**
	 * The cached value of the '{@link #getValue() <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getValue()
	 * @generated
	 * @ordered
	 */
	protected OclExpression value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PropertyTemplateItemImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QvttemplatePackage.Literals.PROPERTY_TEMPLATE_ITEM;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIsOpposite() {
		return isOpposite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsOpposite(boolean newIsOpposite) {
		boolean oldIsOpposite = isOpposite;
		isOpposite = newIsOpposite;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__IS_OPPOSITE, oldIsOpposite, isOpposite));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EStructuralFeature getReferredProperty() {
		if (referredProperty != null && referredProperty.eIsProxy()) {
			InternalEObject oldReferredProperty = (InternalEObject)referredProperty;
			referredProperty = (EStructuralFeature)eResolveProxy(oldReferredProperty);
			if (referredProperty != oldReferredProperty) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__REFERRED_PROPERTY, oldReferredProperty, referredProperty));
			}
		}
		return referredProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature basicGetReferredProperty() {
		return referredProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReferredProperty(EStructuralFeature newReferredProperty) {
		EStructuralFeature oldReferredProperty = referredProperty;
		referredProperty = newReferredProperty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__REFERRED_PROPERTY, oldReferredProperty, referredProperty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getValue() {
		return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetValue(OclExpression newValue, NotificationChain msgs) {
		OclExpression oldValue = value;
		value = newValue;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__VALUE, oldValue, newValue);
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
	public void setValue(OclExpression newValue) {
		if (newValue != value) {
			NotificationChain msgs = null;
			if (value != null)
				msgs = ((InternalEObject)value).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__VALUE, null, msgs);
			if (newValue != null)
				msgs = ((InternalEObject)newValue).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__VALUE, null, msgs);
			msgs = basicSetValue(newValue, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__VALUE, newValue, newValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ObjectTemplateExp getObjContainer() {
		if (eContainerFeatureID() != QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER) return null;
		return (ObjectTemplateExp)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetObjContainer(ObjectTemplateExp newObjContainer, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newObjContainer, QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setObjContainer(ObjectTemplateExp newObjContainer) {
		if (newObjContainer != eInternalContainer() || (eContainerFeatureID() != QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER && newObjContainer != null)) {
			if (EcoreUtil.isAncestor(this, newObjContainer))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newObjContainer != null)
				msgs = ((InternalEObject)newObjContainer).eInverseAdd(this, QvttemplatePackage.OBJECT_TEMPLATE_EXP__PART, ObjectTemplateExp.class, msgs);
			msgs = basicSetObjContainer(newObjContainer, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER, newObjContainer, newObjContainer));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetObjContainer((ObjectTemplateExp)otherEnd, msgs);
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
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__VALUE:
				return basicSetValue(null, msgs);
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER:
				return basicSetObjContainer(null, msgs);
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
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER:
				return eInternalContainer().eInverseRemove(this, QvttemplatePackage.OBJECT_TEMPLATE_EXP__PART, ObjectTemplateExp.class, msgs);
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
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__IS_OPPOSITE:
				return isIsOpposite();
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__REFERRED_PROPERTY:
				if (resolve) return getReferredProperty();
				return basicGetReferredProperty();
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__VALUE:
				return getValue();
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER:
				return getObjContainer();
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
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__IS_OPPOSITE:
				setIsOpposite((Boolean)newValue);
				return;
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__REFERRED_PROPERTY:
				setReferredProperty((EStructuralFeature)newValue);
				return;
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__VALUE:
				setValue((OclExpression)newValue);
				return;
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER:
				setObjContainer((ObjectTemplateExp)newValue);
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
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__IS_OPPOSITE:
				setIsOpposite(IS_OPPOSITE_EDEFAULT);
				return;
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__REFERRED_PROPERTY:
				setReferredProperty((EStructuralFeature)null);
				return;
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__VALUE:
				setValue((OclExpression)null);
				return;
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER:
				setObjContainer((ObjectTemplateExp)null);
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
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__IS_OPPOSITE:
				return isOpposite != IS_OPPOSITE_EDEFAULT;
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__REFERRED_PROPERTY:
				return referredProperty != null;
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__VALUE:
				return value != null;
			case QvttemplatePackage.PROPERTY_TEMPLATE_ITEM__OBJ_CONTAINER:
				return getObjContainer() != null;
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
		result.append(" (isOpposite: ");
		result.append(isOpposite);
		result.append(')');
		return result.toString();
	}

} //PropertyTemplateItemImpl
