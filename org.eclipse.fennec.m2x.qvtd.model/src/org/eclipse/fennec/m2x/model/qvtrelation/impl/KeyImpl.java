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
package org.eclipse.fennec.m2x.model.qvtrelation.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EModelElementImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;

import org.eclipse.fennec.m2x.model.qvtrelation.Key;
import org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Key</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.impl.KeyImpl#getIdentifies <em>Identifies</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.impl.KeyImpl#getPart <em>Part</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.impl.KeyImpl#getOppositePart <em>Opposite Part</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtrelation.impl.KeyImpl#getTransformation <em>Transformation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class KeyImpl extends EModelElementImpl implements Key {
	/**
	 * The cached value of the '{@link #getIdentifies() <em>Identifies</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIdentifies()
	 * @generated
	 * @ordered
	 */
	protected EClass identifies;

	/**
	 * The cached value of the '{@link #getPart() <em>Part</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPart()
	 * @generated
	 * @ordered
	 */
	protected EList<EStructuralFeature> part;

	/**
	 * The cached value of the '{@link #getOppositePart() <em>Opposite Part</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOppositePart()
	 * @generated
	 * @ordered
	 */
	protected EList<EStructuralFeature> oppositePart;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected KeyImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QvtrelationPackage.Literals.KEY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getIdentifies() {
		if (identifies != null && identifies.eIsProxy()) {
			InternalEObject oldIdentifies = (InternalEObject)identifies;
			identifies = (EClass)eResolveProxy(oldIdentifies);
			if (identifies != oldIdentifies) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, QvtrelationPackage.KEY__IDENTIFIES, oldIdentifies, identifies));
			}
		}
		return identifies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClass basicGetIdentifies() {
		return identifies;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIdentifies(EClass newIdentifies) {
		EClass oldIdentifies = identifies;
		identifies = newIdentifies;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtrelationPackage.KEY__IDENTIFIES, oldIdentifies, identifies));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EStructuralFeature> getPart() {
		if (part == null) {
			part = new EObjectResolvingEList<EStructuralFeature>(EStructuralFeature.class, this, QvtrelationPackage.KEY__PART);
		}
		return part;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EStructuralFeature> getOppositePart() {
		if (oppositePart == null) {
			oppositePart = new EObjectResolvingEList<EStructuralFeature>(EStructuralFeature.class, this, QvtrelationPackage.KEY__OPPOSITE_PART);
		}
		return oppositePart;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public RelationalTransformation getTransformation() {
		if (eContainerFeatureID() != QvtrelationPackage.KEY__TRANSFORMATION) return null;
		return (RelationalTransformation)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetTransformation(RelationalTransformation newTransformation, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newTransformation, QvtrelationPackage.KEY__TRANSFORMATION, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setTransformation(RelationalTransformation newTransformation) {
		if (newTransformation != eInternalContainer() || (eContainerFeatureID() != QvtrelationPackage.KEY__TRANSFORMATION && newTransformation != null)) {
			if (EcoreUtil.isAncestor(this, newTransformation))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newTransformation != null)
				msgs = ((InternalEObject)newTransformation).eInverseAdd(this, QvtrelationPackage.RELATIONAL_TRANSFORMATION__OWNED_KEY, RelationalTransformation.class, msgs);
			msgs = basicSetTransformation(newTransformation, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtrelationPackage.KEY__TRANSFORMATION, newTransformation, newTransformation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QvtrelationPackage.KEY__TRANSFORMATION:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetTransformation((RelationalTransformation)otherEnd, msgs);
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
			case QvtrelationPackage.KEY__TRANSFORMATION:
				return basicSetTransformation(null, msgs);
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
			case QvtrelationPackage.KEY__TRANSFORMATION:
				return eInternalContainer().eInverseRemove(this, QvtrelationPackage.RELATIONAL_TRANSFORMATION__OWNED_KEY, RelationalTransformation.class, msgs);
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
			case QvtrelationPackage.KEY__IDENTIFIES:
				if (resolve) return getIdentifies();
				return basicGetIdentifies();
			case QvtrelationPackage.KEY__PART:
				return getPart();
			case QvtrelationPackage.KEY__OPPOSITE_PART:
				return getOppositePart();
			case QvtrelationPackage.KEY__TRANSFORMATION:
				return getTransformation();
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
			case QvtrelationPackage.KEY__IDENTIFIES:
				setIdentifies((EClass)newValue);
				return;
			case QvtrelationPackage.KEY__PART:
				getPart().clear();
				getPart().addAll((Collection<? extends EStructuralFeature>)newValue);
				return;
			case QvtrelationPackage.KEY__OPPOSITE_PART:
				getOppositePart().clear();
				getOppositePart().addAll((Collection<? extends EStructuralFeature>)newValue);
				return;
			case QvtrelationPackage.KEY__TRANSFORMATION:
				setTransformation((RelationalTransformation)newValue);
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
			case QvtrelationPackage.KEY__IDENTIFIES:
				setIdentifies((EClass)null);
				return;
			case QvtrelationPackage.KEY__PART:
				getPart().clear();
				return;
			case QvtrelationPackage.KEY__OPPOSITE_PART:
				getOppositePart().clear();
				return;
			case QvtrelationPackage.KEY__TRANSFORMATION:
				setTransformation((RelationalTransformation)null);
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
			case QvtrelationPackage.KEY__IDENTIFIES:
				return identifies != null;
			case QvtrelationPackage.KEY__PART:
				return part != null && !part.isEmpty();
			case QvtrelationPackage.KEY__OPPOSITE_PART:
				return oppositePart != null && !oppositePart.isEmpty();
			case QvtrelationPackage.KEY__TRANSFORMATION:
				return getTransformation() != null;
		}
		return super.eIsSet(featureID);
	}

} //KeyImpl
