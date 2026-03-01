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
package org.eclipse.fennec.m2x.model.m2t.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentWithInverseEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2x.model.m2t.M2tPackage;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Module</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ModuleImpl#getOwnedModuleElement <em>Owned Module Element</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ModuleImpl#getInput <em>Input</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ModuleImpl#getExtends <em>Extends</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ModuleImpl#getImports <em>Imports</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ModuleImpl extends EPackageImpl implements org.eclipse.fennec.m2x.model.m2t.Module {
	/**
	 * The cached value of the '{@link #getOwnedModuleElement() <em>Owned Module Element</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOwnedModuleElement()
	 * @generated
	 * @ordered
	 */
	protected EList<ModuleElement> ownedModuleElement;

	/**
	 * The cached value of the '{@link #getInput() <em>Input</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInput()
	 * @generated
	 * @ordered
	 */
	protected EList<EPackage> input;

	/**
	 * The cached value of the '{@link #getExtends() <em>Extends</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getExtends()
	 * @generated
	 * @ordered
	 */
	protected EList<org.eclipse.fennec.m2x.model.m2t.Module> extends_;

	/**
	 * The cached value of the '{@link #getImports() <em>Imports</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImports()
	 * @generated
	 * @ordered
	 */
	protected EList<org.eclipse.fennec.m2x.model.m2t.Module> imports;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ModuleImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return M2tPackage.Literals.MODULE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<ModuleElement> getOwnedModuleElement() {
		if (ownedModuleElement == null) {
			ownedModuleElement = new EObjectContainmentWithInverseEList<ModuleElement>(ModuleElement.class, this, M2tPackage.MODULE__OWNED_MODULE_ELEMENT, M2tPackage.MODULE_ELEMENT__MODULE);
		}
		return ownedModuleElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EPackage> getInput() {
		if (input == null) {
			input = new EObjectResolvingEList<EPackage>(EPackage.class, this, M2tPackage.MODULE__INPUT);
		}
		return input;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<org.eclipse.fennec.m2x.model.m2t.Module> getExtends() {
		if (extends_ == null) {
			extends_ = new EObjectResolvingEList<org.eclipse.fennec.m2x.model.m2t.Module>(org.eclipse.fennec.m2x.model.m2t.Module.class, this, M2tPackage.MODULE__EXTENDS);
		}
		return extends_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<org.eclipse.fennec.m2x.model.m2t.Module> getImports() {
		if (imports == null) {
			imports = new EObjectResolvingEList<org.eclipse.fennec.m2x.model.m2t.Module>(org.eclipse.fennec.m2x.model.m2t.Module.class, this, M2tPackage.MODULE__IMPORTS);
		}
		return imports;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case M2tPackage.MODULE__OWNED_MODULE_ELEMENT:
				return ((InternalEList<InternalEObject>)(InternalEList<?>)getOwnedModuleElement()).basicAdd(otherEnd, msgs);
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
			case M2tPackage.MODULE__OWNED_MODULE_ELEMENT:
				return ((InternalEList<?>)getOwnedModuleElement()).basicRemove(otherEnd, msgs);
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
			case M2tPackage.MODULE__OWNED_MODULE_ELEMENT:
				return getOwnedModuleElement();
			case M2tPackage.MODULE__INPUT:
				return getInput();
			case M2tPackage.MODULE__EXTENDS:
				return getExtends();
			case M2tPackage.MODULE__IMPORTS:
				return getImports();
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
			case M2tPackage.MODULE__OWNED_MODULE_ELEMENT:
				getOwnedModuleElement().clear();
				getOwnedModuleElement().addAll((Collection<? extends ModuleElement>)newValue);
				return;
			case M2tPackage.MODULE__INPUT:
				getInput().clear();
				getInput().addAll((Collection<? extends EPackage>)newValue);
				return;
			case M2tPackage.MODULE__EXTENDS:
				getExtends().clear();
				getExtends().addAll((Collection<? extends org.eclipse.fennec.m2x.model.m2t.Module>)newValue);
				return;
			case M2tPackage.MODULE__IMPORTS:
				getImports().clear();
				getImports().addAll((Collection<? extends org.eclipse.fennec.m2x.model.m2t.Module>)newValue);
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
			case M2tPackage.MODULE__OWNED_MODULE_ELEMENT:
				getOwnedModuleElement().clear();
				return;
			case M2tPackage.MODULE__INPUT:
				getInput().clear();
				return;
			case M2tPackage.MODULE__EXTENDS:
				getExtends().clear();
				return;
			case M2tPackage.MODULE__IMPORTS:
				getImports().clear();
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
			case M2tPackage.MODULE__OWNED_MODULE_ELEMENT:
				return ownedModuleElement != null && !ownedModuleElement.isEmpty();
			case M2tPackage.MODULE__INPUT:
				return input != null && !input.isEmpty();
			case M2tPackage.MODULE__EXTENDS:
				return extends_ != null && !extends_.isEmpty();
			case M2tPackage.MODULE__IMPORTS:
				return imports != null && !imports.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //ModuleImpl
