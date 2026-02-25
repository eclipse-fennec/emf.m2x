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

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.m2x.model.imperativeocl.ImperativeOclPackage;
import org.eclipse.fennec.m2x.model.imperativeocl.TransformationInstantiationExp;

import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Transformation Instantiation Exp</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.imperativeocl.impl.TransformationInstantiationExpImpl#getImportedTransformation <em>Imported Transformation</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TransformationInstantiationExpImpl extends InstantiationExpImpl implements TransformationInstantiationExp {
	/**
	 * The cached value of the '{@link #getImportedTransformation() <em>Imported Transformation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getImportedTransformation()
	 * @generated
	 * @ordered
	 */
	protected OperationalTransformation importedTransformation;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TransformationInstantiationExpImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return ImperativeOclPackage.Literals.TRANSFORMATION_INSTANTIATION_EXP;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OperationalTransformation getImportedTransformation() {
		if (importedTransformation != null && importedTransformation.eIsProxy()) {
			InternalEObject oldImportedTransformation = (InternalEObject)importedTransformation;
			importedTransformation = (OperationalTransformation)eResolveProxy(oldImportedTransformation);
			if (importedTransformation != oldImportedTransformation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, ImperativeOclPackage.TRANSFORMATION_INSTANTIATION_EXP__IMPORTED_TRANSFORMATION, oldImportedTransformation, importedTransformation));
			}
		}
		return importedTransformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OperationalTransformation basicGetImportedTransformation() {
		return importedTransformation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setImportedTransformation(OperationalTransformation newImportedTransformation) {
		OperationalTransformation oldImportedTransformation = importedTransformation;
		importedTransformation = newImportedTransformation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, ImperativeOclPackage.TRANSFORMATION_INSTANTIATION_EXP__IMPORTED_TRANSFORMATION, oldImportedTransformation, importedTransformation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case ImperativeOclPackage.TRANSFORMATION_INSTANTIATION_EXP__IMPORTED_TRANSFORMATION:
				if (resolve) return getImportedTransformation();
				return basicGetImportedTransformation();
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
			case ImperativeOclPackage.TRANSFORMATION_INSTANTIATION_EXP__IMPORTED_TRANSFORMATION:
				setImportedTransformation((OperationalTransformation)newValue);
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
			case ImperativeOclPackage.TRANSFORMATION_INSTANTIATION_EXP__IMPORTED_TRANSFORMATION:
				setImportedTransformation((OperationalTransformation)null);
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
			case ImperativeOclPackage.TRANSFORMATION_INSTANTIATION_EXP__IMPORTED_TRANSFORMATION:
				return importedTransformation != null;
		}
		return super.eIsSet(featureID);
	}

} //TransformationInstantiationExpImpl
