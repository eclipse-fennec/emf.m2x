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
package org.eclipse.fennec.m2x.model.compiled.impl;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Blackbox Requirement</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.BlackboxRequirementImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.BlackboxRequirementImpl#getSignatureFingerprint <em>Signature Fingerprint</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.BlackboxRequirementImpl#getProvider <em>Provider</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BlackboxRequirementImpl extends MinimalEObjectImpl.Container implements BlackboxRequirement {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getSignatureFingerprint() <em>Signature Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSignatureFingerprint()
	 * @generated
	 * @ordered
	 */
	protected static final String SIGNATURE_FINGERPRINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSignatureFingerprint() <em>Signature Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSignatureFingerprint()
	 * @generated
	 * @ordered
	 */
	protected String signatureFingerprint = SIGNATURE_FINGERPRINT_EDEFAULT;

	/**
	 * The default value of the '{@link #getProvider() <em>Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvider()
	 * @generated
	 * @ordered
	 */
	protected static final String PROVIDER_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProvider() <em>Provider</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProvider()
	 * @generated
	 * @ordered
	 */
	protected String provider = PROVIDER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BlackboxRequirementImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CompiledPackage.Literals.BLACKBOX_REQUIREMENT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
		return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.BLACKBOX_REQUIREMENT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSignatureFingerprint() {
		return signatureFingerprint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSignatureFingerprint(String newSignatureFingerprint) {
		String oldSignatureFingerprint = signatureFingerprint;
		signatureFingerprint = newSignatureFingerprint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.BLACKBOX_REQUIREMENT__SIGNATURE_FINGERPRINT, oldSignatureFingerprint, signatureFingerprint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getProvider() {
		return provider;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProvider(String newProvider) {
		String oldProvider = provider;
		provider = newProvider;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.BLACKBOX_REQUIREMENT__PROVIDER, oldProvider, provider));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CompiledPackage.BLACKBOX_REQUIREMENT__NAME:
				return getName();
			case CompiledPackage.BLACKBOX_REQUIREMENT__SIGNATURE_FINGERPRINT:
				return getSignatureFingerprint();
			case CompiledPackage.BLACKBOX_REQUIREMENT__PROVIDER:
				return getProvider();
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
			case CompiledPackage.BLACKBOX_REQUIREMENT__NAME:
				setName((String)newValue);
				return;
			case CompiledPackage.BLACKBOX_REQUIREMENT__SIGNATURE_FINGERPRINT:
				setSignatureFingerprint((String)newValue);
				return;
			case CompiledPackage.BLACKBOX_REQUIREMENT__PROVIDER:
				setProvider((String)newValue);
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
			case CompiledPackage.BLACKBOX_REQUIREMENT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CompiledPackage.BLACKBOX_REQUIREMENT__SIGNATURE_FINGERPRINT:
				setSignatureFingerprint(SIGNATURE_FINGERPRINT_EDEFAULT);
				return;
			case CompiledPackage.BLACKBOX_REQUIREMENT__PROVIDER:
				setProvider(PROVIDER_EDEFAULT);
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
			case CompiledPackage.BLACKBOX_REQUIREMENT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CompiledPackage.BLACKBOX_REQUIREMENT__SIGNATURE_FINGERPRINT:
				return SIGNATURE_FINGERPRINT_EDEFAULT == null ? signatureFingerprint != null : !SIGNATURE_FINGERPRINT_EDEFAULT.equals(signatureFingerprint);
			case CompiledPackage.BLACKBOX_REQUIREMENT__PROVIDER:
				return PROVIDER_EDEFAULT == null ? provider != null : !PROVIDER_EDEFAULT.equals(provider);
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
		result.append(" (name: ");
		result.append(name);
		result.append(", signatureFingerprint: ");
		result.append(signatureFingerprint);
		result.append(", provider: ");
		result.append(provider);
		result.append(')');
		return result.toString();
	}

} //BlackboxRequirementImpl
