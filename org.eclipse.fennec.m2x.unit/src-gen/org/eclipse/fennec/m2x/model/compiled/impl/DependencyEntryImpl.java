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

import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Dependency Entry</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.DependencyEntryImpl#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.DependencyEntryImpl#getMode <em>Mode</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.DependencyEntryImpl#getFingerprint <em>Fingerprint</em>}</li>
 * </ul>
 *
 * @generated
 */
public class DependencyEntryImpl extends MinimalEObjectImpl.Container implements DependencyEntry {
	/**
	 * The default value of the '{@link #getQualifiedName() <em>Qualified Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQualifiedName()
	 * @generated
	 * @ordered
	 */
	protected static final String QUALIFIED_NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getQualifiedName() <em>Qualified Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getQualifiedName()
	 * @generated
	 * @ordered
	 */
	protected String qualifiedName = QUALIFIED_NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected static final DependencyMode MODE_EDEFAULT = DependencyMode.EMBED;

	/**
	 * The cached value of the '{@link #getMode() <em>Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMode()
	 * @generated
	 * @ordered
	 */
	protected DependencyMode mode = MODE_EDEFAULT;

	/**
	 * The default value of the '{@link #getFingerprint() <em>Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFingerprint()
	 * @generated
	 * @ordered
	 */
	protected static final String FINGERPRINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFingerprint() <em>Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFingerprint()
	 * @generated
	 * @ordered
	 */
	protected String fingerprint = FINGERPRINT_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected DependencyEntryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CompiledPackage.Literals.DEPENDENCY_ENTRY;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getQualifiedName() {
		return qualifiedName;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setQualifiedName(String newQualifiedName) {
		String oldQualifiedName = qualifiedName;
		qualifiedName = newQualifiedName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.DEPENDENCY_ENTRY__QUALIFIED_NAME, oldQualifiedName, qualifiedName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DependencyMode getMode() {
		return mode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMode(DependencyMode newMode) {
		DependencyMode oldMode = mode;
		mode = newMode == null ? MODE_EDEFAULT : newMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.DEPENDENCY_ENTRY__MODE, oldMode, mode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getFingerprint() {
		return fingerprint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFingerprint(String newFingerprint) {
		String oldFingerprint = fingerprint;
		fingerprint = newFingerprint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.DEPENDENCY_ENTRY__FINGERPRINT, oldFingerprint, fingerprint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case CompiledPackage.DEPENDENCY_ENTRY__QUALIFIED_NAME:
				return getQualifiedName();
			case CompiledPackage.DEPENDENCY_ENTRY__MODE:
				return getMode();
			case CompiledPackage.DEPENDENCY_ENTRY__FINGERPRINT:
				return getFingerprint();
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
			case CompiledPackage.DEPENDENCY_ENTRY__QUALIFIED_NAME:
				setQualifiedName((String)newValue);
				return;
			case CompiledPackage.DEPENDENCY_ENTRY__MODE:
				setMode((DependencyMode)newValue);
				return;
			case CompiledPackage.DEPENDENCY_ENTRY__FINGERPRINT:
				setFingerprint((String)newValue);
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
			case CompiledPackage.DEPENDENCY_ENTRY__QUALIFIED_NAME:
				setQualifiedName(QUALIFIED_NAME_EDEFAULT);
				return;
			case CompiledPackage.DEPENDENCY_ENTRY__MODE:
				setMode(MODE_EDEFAULT);
				return;
			case CompiledPackage.DEPENDENCY_ENTRY__FINGERPRINT:
				setFingerprint(FINGERPRINT_EDEFAULT);
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
			case CompiledPackage.DEPENDENCY_ENTRY__QUALIFIED_NAME:
				return QUALIFIED_NAME_EDEFAULT == null ? qualifiedName != null : !QUALIFIED_NAME_EDEFAULT.equals(qualifiedName);
			case CompiledPackage.DEPENDENCY_ENTRY__MODE:
				return mode != MODE_EDEFAULT;
			case CompiledPackage.DEPENDENCY_ENTRY__FINGERPRINT:
				return FINGERPRINT_EDEFAULT == null ? fingerprint != null : !FINGERPRINT_EDEFAULT.equals(fingerprint);
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
		result.append(" (qualifiedName: ");
		result.append(qualifiedName);
		result.append(", mode: ");
		result.append(mode);
		result.append(", fingerprint: ");
		result.append(fingerprint);
		result.append(')');
		return result.toString();
	}

} //DependencyEntryImpl
