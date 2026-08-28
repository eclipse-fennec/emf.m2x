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
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Unit</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl#getManifest <em>Manifest</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl#getUnit <em>Unit</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl#getSatellite <em>Satellite</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl#getEmbedded <em>Embedded</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.impl.CompiledUnitImpl#getPackages <em>Packages</em>}</li>
 * </ul>
 *
 * @generated
 */
public class CompiledUnitImpl extends MinimalEObjectImpl.Container implements CompiledUnit {
	/**
	 * The default value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected static final String ID_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getId() <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getId()
	 * @generated
	 * @ordered
	 */
	protected String id = ID_EDEFAULT;

	/**
	 * The default value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected static final String NAMESPACE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getNamespace() <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getNamespace()
	 * @generated
	 * @ordered
	 */
	protected String namespace = NAMESPACE_EDEFAULT;

	/**
	 * The default value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected static final String VERSION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getVersion() <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVersion()
	 * @generated
	 * @ordered
	 */
	protected String version = VERSION_EDEFAULT;

	/**
	 * The default value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected static final String DESCRIPTION_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getDescription() <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDescription()
	 * @generated
	 * @ordered
	 */
	protected String description = DESCRIPTION_EDEFAULT;

	/**
	 * The cached value of the '{@link #getManifest() <em>Manifest</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getManifest()
	 * @generated
	 * @ordered
	 */
	protected CompiledUnitManifest manifest;

	/**
	 * The cached value of the '{@link #getUnit() <em>Unit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getUnit()
	 * @generated
	 * @ordered
	 */
	protected EPackage unit;

	/**
	 * The cached value of the '{@link #getSatellite() <em>Satellite</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSatellite()
	 * @generated
	 * @ordered
	 */
	protected EList<EObject> satellite;

	/**
	 * The cached value of the '{@link #getEmbedded() <em>Embedded</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEmbedded()
	 * @generated
	 * @ordered
	 */
	protected EList<CompiledUnit> embedded;

	/**
	 * The cached value of the '{@link #getPackages() <em>Packages</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPackages()
	 * @generated
	 * @ordered
	 */
	protected EList<EPackage> packages;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected CompiledUnitImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CompiledPackage.Literals.COMPILED_UNIT;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getId() {
		return id;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setId(String newId) {
		String oldId = id;
		id = newId;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT__ID, oldId, id));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getNamespace() {
		return namespace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setNamespace(String newNamespace) {
		String oldNamespace = namespace;
		namespace = newNamespace;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT__NAMESPACE, oldNamespace, namespace));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getVersion() {
		return version;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVersion(String newVersion) {
		String oldVersion = version;
		version = newVersion;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT__VERSION, oldVersion, version));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getDescription() {
		return description;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDescription(String newDescription) {
		String oldDescription = description;
		description = newDescription;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT__DESCRIPTION, oldDescription, description));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public CompiledUnitManifest getManifest() {
		return manifest;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetManifest(CompiledUnitManifest newManifest, NotificationChain msgs) {
		CompiledUnitManifest oldManifest = manifest;
		manifest = newManifest;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT__MANIFEST, oldManifest, newManifest);
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
	public void setManifest(CompiledUnitManifest newManifest) {
		if (newManifest != manifest) {
			NotificationChain msgs = null;
			if (manifest != null)
				msgs = ((InternalEObject)manifest).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CompiledPackage.COMPILED_UNIT__MANIFEST, null, msgs);
			if (newManifest != null)
				msgs = ((InternalEObject)newManifest).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CompiledPackage.COMPILED_UNIT__MANIFEST, null, msgs);
			msgs = basicSetManifest(newManifest, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT__MANIFEST, newManifest, newManifest));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EPackage getUnit() {
		return unit;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetUnit(EPackage newUnit, NotificationChain msgs) {
		EPackage oldUnit = unit;
		unit = newUnit;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT__UNIT, oldUnit, newUnit);
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
	public void setUnit(EPackage newUnit) {
		if (newUnit != unit) {
			NotificationChain msgs = null;
			if (unit != null)
				msgs = ((InternalEObject)unit).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - CompiledPackage.COMPILED_UNIT__UNIT, null, msgs);
			if (newUnit != null)
				msgs = ((InternalEObject)newUnit).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - CompiledPackage.COMPILED_UNIT__UNIT, null, msgs);
			msgs = basicSetUnit(newUnit, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompiledPackage.COMPILED_UNIT__UNIT, newUnit, newUnit));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EObject> getSatellite() {
		if (satellite == null) {
			satellite = new EObjectContainmentEList<EObject>(EObject.class, this, CompiledPackage.COMPILED_UNIT__SATELLITE);
		}
		return satellite;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<CompiledUnit> getEmbedded() {
		if (embedded == null) {
			embedded = new EObjectContainmentEList<CompiledUnit>(CompiledUnit.class, this, CompiledPackage.COMPILED_UNIT__EMBEDDED);
		}
		return embedded;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EPackage> getPackages() {
		if (packages == null) {
			packages = new EObjectContainmentEList<EPackage>(EPackage.class, this, CompiledPackage.COMPILED_UNIT__PACKAGES);
		}
		return packages;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CompiledPackage.COMPILED_UNIT__MANIFEST:
				return basicSetManifest(null, msgs);
			case CompiledPackage.COMPILED_UNIT__UNIT:
				return basicSetUnit(null, msgs);
			case CompiledPackage.COMPILED_UNIT__SATELLITE:
				return ((InternalEList<?>)getSatellite()).basicRemove(otherEnd, msgs);
			case CompiledPackage.COMPILED_UNIT__EMBEDDED:
				return ((InternalEList<?>)getEmbedded()).basicRemove(otherEnd, msgs);
			case CompiledPackage.COMPILED_UNIT__PACKAGES:
				return ((InternalEList<?>)getPackages()).basicRemove(otherEnd, msgs);
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
			case CompiledPackage.COMPILED_UNIT__ID:
				return getId();
			case CompiledPackage.COMPILED_UNIT__NAMESPACE:
				return getNamespace();
			case CompiledPackage.COMPILED_UNIT__VERSION:
				return getVersion();
			case CompiledPackage.COMPILED_UNIT__DESCRIPTION:
				return getDescription();
			case CompiledPackage.COMPILED_UNIT__MANIFEST:
				return getManifest();
			case CompiledPackage.COMPILED_UNIT__UNIT:
				return getUnit();
			case CompiledPackage.COMPILED_UNIT__SATELLITE:
				return getSatellite();
			case CompiledPackage.COMPILED_UNIT__EMBEDDED:
				return getEmbedded();
			case CompiledPackage.COMPILED_UNIT__PACKAGES:
				return getPackages();
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
			case CompiledPackage.COMPILED_UNIT__ID:
				setId((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT__NAMESPACE:
				setNamespace((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT__VERSION:
				setVersion((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT__DESCRIPTION:
				setDescription((String)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT__MANIFEST:
				setManifest((CompiledUnitManifest)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT__UNIT:
				setUnit((EPackage)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT__SATELLITE:
				getSatellite().clear();
				getSatellite().addAll((Collection<? extends EObject>)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT__EMBEDDED:
				getEmbedded().clear();
				getEmbedded().addAll((Collection<? extends CompiledUnit>)newValue);
				return;
			case CompiledPackage.COMPILED_UNIT__PACKAGES:
				getPackages().clear();
				getPackages().addAll((Collection<? extends EPackage>)newValue);
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
			case CompiledPackage.COMPILED_UNIT__ID:
				setId(ID_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT__NAMESPACE:
				setNamespace(NAMESPACE_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT__VERSION:
				setVersion(VERSION_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT__DESCRIPTION:
				setDescription(DESCRIPTION_EDEFAULT);
				return;
			case CompiledPackage.COMPILED_UNIT__MANIFEST:
				setManifest((CompiledUnitManifest)null);
				return;
			case CompiledPackage.COMPILED_UNIT__UNIT:
				setUnit((EPackage)null);
				return;
			case CompiledPackage.COMPILED_UNIT__SATELLITE:
				getSatellite().clear();
				return;
			case CompiledPackage.COMPILED_UNIT__EMBEDDED:
				getEmbedded().clear();
				return;
			case CompiledPackage.COMPILED_UNIT__PACKAGES:
				getPackages().clear();
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
			case CompiledPackage.COMPILED_UNIT__ID:
				return ID_EDEFAULT == null ? id != null : !ID_EDEFAULT.equals(id);
			case CompiledPackage.COMPILED_UNIT__NAMESPACE:
				return NAMESPACE_EDEFAULT == null ? namespace != null : !NAMESPACE_EDEFAULT.equals(namespace);
			case CompiledPackage.COMPILED_UNIT__VERSION:
				return VERSION_EDEFAULT == null ? version != null : !VERSION_EDEFAULT.equals(version);
			case CompiledPackage.COMPILED_UNIT__DESCRIPTION:
				return DESCRIPTION_EDEFAULT == null ? description != null : !DESCRIPTION_EDEFAULT.equals(description);
			case CompiledPackage.COMPILED_UNIT__MANIFEST:
				return manifest != null;
			case CompiledPackage.COMPILED_UNIT__UNIT:
				return unit != null;
			case CompiledPackage.COMPILED_UNIT__SATELLITE:
				return satellite != null && !satellite.isEmpty();
			case CompiledPackage.COMPILED_UNIT__EMBEDDED:
				return embedded != null && !embedded.isEmpty();
			case CompiledPackage.COMPILED_UNIT__PACKAGES:
				return packages != null && !packages.isEmpty();
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
		result.append(" (id: ");
		result.append(id);
		result.append(", namespace: ");
		result.append(namespace);
		result.append(", version: ");
		result.append(version);
		result.append(", description: ");
		result.append(description);
		result.append(')');
		return result.toString();
	}

} //CompiledUnitImpl
