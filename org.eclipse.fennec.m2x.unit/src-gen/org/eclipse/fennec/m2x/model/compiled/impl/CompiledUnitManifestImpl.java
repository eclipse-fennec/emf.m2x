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

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.compiled.PackageEntry;
import org.eclipse.fennec.m2x.model.compiled.ResolvedDependency;
import org.eclipse.fennec.m2x.model.compiled.UnitNature;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unit Manifest</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getFormatVersion <em>Format Version</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getProducedBy <em>Produced By</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getLanguage <em>Language</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getQualifiedName <em>Qualified Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getUnitFingerprint <em>Unit Fingerprint</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getSourceFingerprint <em>Source Fingerprint</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getDependencyMode <em>Dependency Mode</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getNature <em>Nature</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getPackageEntry <em>Package Entry</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getDependencyEntry <em>Dependency Entry</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getBlackboxRequirement <em>Blackbox Requirement</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitManifestImpl#getResolvedClosure <em>Resolved Closure</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompiledUnitManifestImpl extends MinimalEObjectImpl.Container implements CompiledUnitManifest {
	/**
	 * The default value of the '{@link #getFormatVersion() <em>Format Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormatVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String FORMAT_VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getFormatVersion() <em>Format Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getFormatVersion()
	 * @generated
	 * @ordered
	 */
	protected String formatVersion = FORMAT_VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getProducedBy() <em>Produced By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProducedBy()
	 * @generated
	 * @ordered
	 */
	protected static final String PRODUCED_BY_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getProducedBy() <em>Produced By</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getProducedBy()
	 * @generated
	 * @ordered
	 */
	protected String producedBy = PRODUCED_BY_EDEFAULT;

	/**
	 * The default value of the '{@link #getLanguage() <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguage()
	 * @generated
	 * @ordered
	 */
	protected static final String LANGUAGE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getLanguage() <em>Language</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLanguage()
	 * @generated
	 * @ordered
	 */
	protected String language = LANGUAGE_EDEFAULT;

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
	 * The default value of the '{@link #getUnitFingerprint() <em>Unit Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitFingerprint()
	 * @generated
	 * @ordered
	 */
	protected static final String UNIT_FINGERPRINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getUnitFingerprint() <em>Unit Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnitFingerprint()
	 * @generated
	 * @ordered
	 */
	protected String unitFingerprint = UNIT_FINGERPRINT_EDEFAULT;

	/**
	 * The default value of the '{@link #getSourceFingerprint() <em>Source Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceFingerprint()
	 * @generated
	 * @ordered
	 */
	protected static final String SOURCE_FINGERPRINT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getSourceFingerprint() <em>Source Fingerprint</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceFingerprint()
	 * @generated
	 * @ordered
	 */
	protected String sourceFingerprint = SOURCE_FINGERPRINT_EDEFAULT;

	/**
	 * The default value of the '{@link #getDependencyMode() <em>Dependency Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencyMode()
	 * @generated
	 * @ordered
	 */
	protected static final DependencyMode DEPENDENCY_MODE_EDEFAULT = DependencyMode.EMBED;

	/**
	 * The cached value of the '{@link #getDependencyMode() <em>Dependency Mode</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencyMode()
	 * @generated
	 * @ordered
	 */
	protected DependencyMode dependencyMode = DEPENDENCY_MODE_EDEFAULT;

	/**
	 * The default value of the '{@link #getNature() <em>Nature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNature()
	 * @generated
	 * @ordered
	 */
	protected static final UnitNature NATURE_EDEFAULT = UnitNature.TRANSFORMATION;

	/**
	 * The cached value of the '{@link #getNature() <em>Nature</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNature()
	 * @generated
	 * @ordered
	 */
	protected UnitNature nature = NATURE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPackageEntry() <em>Package Entry</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackageEntry()
	 * @generated
	 * @ordered
	 */
	protected EList<PackageEntry> packageEntry;

	/**
	 * The cached value of the '{@link #getDependencyEntry() <em>Dependency Entry</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDependencyEntry()
	 * @generated
	 * @ordered
	 */
	protected EList<DependencyEntry> dependencyEntry;

	/**
	 * The cached value of the '{@link #getBlackboxRequirement() <em>Blackbox Requirement</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBlackboxRequirement()
	 * @generated
	 * @ordered
	 */
	protected EList<BlackboxRequirement> blackboxRequirement;

	/**
	 * The cached value of the '{@link #getResolvedClosure() <em>Resolved Closure</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getResolvedClosure()
	 * @generated
	 * @ordered
	 */
	protected EList<ResolvedDependency> resolvedClosure;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CompiledUnitManifestImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CompiledPackage.Literals.COMPILED_UNIT_MANIFEST;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getFormatVersion() {
		return formatVersion;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setFormatVersion(String newFormatVersion) {
		String oldFormatVersion = formatVersion;
		formatVersion = newFormatVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT_MANIFEST__FORMAT_VERSION, oldFormatVersion, formatVersion));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getProducedBy() {
		return producedBy;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setProducedBy(String newProducedBy) {
		String oldProducedBy = producedBy;
		producedBy = newProducedBy;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT_MANIFEST__PRODUCED_BY, oldProducedBy, producedBy));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLanguage() {
		return language;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setLanguage(String newLanguage) {
		String oldLanguage = language;
		language = newLanguage;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT_MANIFEST__LANGUAGE, oldLanguage, language));
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
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT_MANIFEST__QUALIFIED_NAME, oldQualifiedName, qualifiedName));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getUnitFingerprint() {
		return unitFingerprint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setUnitFingerprint(String newUnitFingerprint) {
		String oldUnitFingerprint = unitFingerprint;
		unitFingerprint = newUnitFingerprint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT_MANIFEST__UNIT_FINGERPRINT, oldUnitFingerprint, unitFingerprint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getSourceFingerprint() {
		return sourceFingerprint;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSourceFingerprint(String newSourceFingerprint) {
		String oldSourceFingerprint = sourceFingerprint;
		sourceFingerprint = newSourceFingerprint;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT_MANIFEST__SOURCE_FINGERPRINT, oldSourceFingerprint, sourceFingerprint));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public DependencyMode getDependencyMode() {
		return dependencyMode;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDependencyMode(DependencyMode newDependencyMode) {
		DependencyMode oldDependencyMode = dependencyMode;
		dependencyMode = newDependencyMode == null ? DEPENDENCY_MODE_EDEFAULT : newDependencyMode;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_MODE, oldDependencyMode, dependencyMode));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public UnitNature getNature() {
		return nature;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNature(UnitNature newNature) {
		UnitNature oldNature = nature;
		nature = newNature == null ? NATURE_EDEFAULT : newNature;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT_MANIFEST__NATURE, oldNature, nature));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<PackageEntry> getPackageEntry() {
		if (packageEntry == null) {
			packageEntry = new EObjectContainmentEList<PackageEntry>(PackageEntry.class, this, CompiledPackage.COMPILED_UNIT_MANIFEST__PACKAGE_ENTRY);
		}
		return packageEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<DependencyEntry> getDependencyEntry() {
		if (dependencyEntry == null) {
			dependencyEntry = new EObjectContainmentEList<DependencyEntry>(DependencyEntry.class, this, CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_ENTRY);
		}
		return dependencyEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<BlackboxRequirement> getBlackboxRequirement() {
		if (blackboxRequirement == null) {
			blackboxRequirement = new EObjectContainmentEList<BlackboxRequirement>(BlackboxRequirement.class, this, CompiledPackage.COMPILED_UNIT_MANIFEST__BLACKBOX_REQUIREMENT);
		}
		return blackboxRequirement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ResolvedDependency> getResolvedClosure() {
		if (resolvedClosure == null) {
			resolvedClosure = new EObjectContainmentEList<ResolvedDependency>(ResolvedDependency.class, this, CompiledPackage.COMPILED_UNIT_MANIFEST__RESOLVED_CLOSURE);
		}
		return resolvedClosure;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CompiledPackage.COMPILED_UNIT_MANIFEST__PACKAGE_ENTRY:
				return ((InternalEList<?>)getPackageEntry()).basicRemove(otherEnd, msgs);
			case CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_ENTRY:
				return ((InternalEList<?>)getDependencyEntry()).basicRemove(otherEnd, msgs);
			case CompiledPackage.COMPILED_UNIT_MANIFEST__BLACKBOX_REQUIREMENT:
				return ((InternalEList<?>)getBlackboxRequirement()).basicRemove(otherEnd, msgs);
			case CompiledPackage.COMPILED_UNIT_MANIFEST__RESOLVED_CLOSURE:
				return ((InternalEList<?>)getResolvedClosure()).basicRemove(otherEnd, msgs);
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
			case CompiledPackage.COMPILED_UNIT_MANIFEST__FORMAT_VERSION:
				return getFormatVersion();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__PRODUCED_BY:
				return getProducedBy();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__LANGUAGE:
				return getLanguage();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__QUALIFIED_NAME:
				return getQualifiedName();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__UNIT_FINGERPRINT:
				return getUnitFingerprint();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__SOURCE_FINGERPRINT:
				return getSourceFingerprint();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_MODE:
				return getDependencyMode();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__NATURE:
				return getNature();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__PACKAGE_ENTRY:
				return getPackageEntry();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_ENTRY:
				return getDependencyEntry();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__BLACKBOX_REQUIREMENT:
				return getBlackboxRequirement();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__RESOLVED_CLOSURE:
				return getResolvedClosure();
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
			case CompiledPackage.COMPILED_UNIT_MANIFEST__FORMAT_VERSION:
				setFormatVersion((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__PRODUCED_BY:
				setProducedBy((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__LANGUAGE:
				setLanguage((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__QUALIFIED_NAME:
				setQualifiedName((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__UNIT_FINGERPRINT:
				setUnitFingerprint((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__SOURCE_FINGERPRINT:
				setSourceFingerprint((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_MODE:
				setDependencyMode((DependencyMode)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__NATURE:
				setNature((UnitNature)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__PACKAGE_ENTRY:
				getPackageEntry().clear();
				getPackageEntry().addAll((Collection<? extends PackageEntry>)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_ENTRY:
				getDependencyEntry().clear();
				getDependencyEntry().addAll((Collection<? extends DependencyEntry>)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__BLACKBOX_REQUIREMENT:
				getBlackboxRequirement().clear();
				getBlackboxRequirement().addAll((Collection<? extends BlackboxRequirement>)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__RESOLVED_CLOSURE:
				getResolvedClosure().clear();
				getResolvedClosure().addAll((Collection<? extends ResolvedDependency>)newValue);
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
			case CompiledPackage.COMPILED_UNIT_MANIFEST__FORMAT_VERSION:
				setFormatVersion(FORMAT_VERSION_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__PRODUCED_BY:
				setProducedBy(PRODUCED_BY_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__LANGUAGE:
				setLanguage(LANGUAGE_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__QUALIFIED_NAME:
				setQualifiedName(QUALIFIED_NAME_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__UNIT_FINGERPRINT:
				setUnitFingerprint(UNIT_FINGERPRINT_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__SOURCE_FINGERPRINT:
				setSourceFingerprint(SOURCE_FINGERPRINT_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_MODE:
				setDependencyMode(DEPENDENCY_MODE_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__NATURE:
				setNature(NATURE_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__PACKAGE_ENTRY:
				getPackageEntry().clear();
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_ENTRY:
				getDependencyEntry().clear();
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__BLACKBOX_REQUIREMENT:
				getBlackboxRequirement().clear();
				return;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__RESOLVED_CLOSURE:
				getResolvedClosure().clear();
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
			case CompiledPackage.COMPILED_UNIT_MANIFEST__FORMAT_VERSION:
				return FORMAT_VERSION_EDEFAULT == null ? formatVersion != null : !FORMAT_VERSION_EDEFAULT.equals(formatVersion);
			case CompiledPackage.COMPILED_UNIT_MANIFEST__PRODUCED_BY:
				return PRODUCED_BY_EDEFAULT == null ? producedBy != null : !PRODUCED_BY_EDEFAULT.equals(producedBy);
			case CompiledPackage.COMPILED_UNIT_MANIFEST__LANGUAGE:
				return LANGUAGE_EDEFAULT == null ? language != null : !LANGUAGE_EDEFAULT.equals(language);
			case CompiledPackage.COMPILED_UNIT_MANIFEST__QUALIFIED_NAME:
				return QUALIFIED_NAME_EDEFAULT == null ? qualifiedName != null : !QUALIFIED_NAME_EDEFAULT.equals(qualifiedName);
			case CompiledPackage.COMPILED_UNIT_MANIFEST__UNIT_FINGERPRINT:
				return UNIT_FINGERPRINT_EDEFAULT == null ? unitFingerprint != null : !UNIT_FINGERPRINT_EDEFAULT.equals(unitFingerprint);
			case CompiledPackage.COMPILED_UNIT_MANIFEST__SOURCE_FINGERPRINT:
				return SOURCE_FINGERPRINT_EDEFAULT == null ? sourceFingerprint != null : !SOURCE_FINGERPRINT_EDEFAULT.equals(sourceFingerprint);
			case CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_MODE:
				return dependencyMode != DEPENDENCY_MODE_EDEFAULT;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__NATURE:
				return nature != NATURE_EDEFAULT;
			case CompiledPackage.COMPILED_UNIT_MANIFEST__PACKAGE_ENTRY:
				return packageEntry != null && !packageEntry.isEmpty();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__DEPENDENCY_ENTRY:
				return dependencyEntry != null && !dependencyEntry.isEmpty();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__BLACKBOX_REQUIREMENT:
				return blackboxRequirement != null && !blackboxRequirement.isEmpty();
			case CompiledPackage.COMPILED_UNIT_MANIFEST__RESOLVED_CLOSURE:
				return resolvedClosure != null && !resolvedClosure.isEmpty();
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
		result.append(" (formatVersion: ");
		result.append(formatVersion);
		result.append(", producedBy: ");
		result.append(producedBy);
		result.append(", language: ");
		result.append(language);
		result.append(", qualifiedName: ");
		result.append(qualifiedName);
		result.append(", unitFingerprint: ");
		result.append(unitFingerprint);
		result.append(", sourceFingerprint: ");
		result.append(sourceFingerprint);
		result.append(", dependencyMode: ");
		result.append(dependencyMode);
		result.append(", nature: ");
		result.append(nature);
		result.append(')');
		return result.toString();
	}

} //CompiledUnitManifestImpl
