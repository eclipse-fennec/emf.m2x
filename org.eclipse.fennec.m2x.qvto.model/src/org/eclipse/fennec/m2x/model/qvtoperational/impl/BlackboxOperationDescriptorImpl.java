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
package org.eclipse.fennec.m2x.model.qvtoperational.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.EModelElementImpl;
import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectResolvingEList;

import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Blackbox Operation Descriptor</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.impl.BlackboxOperationDescriptorImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.impl.BlackboxOperationDescriptorImpl#getContextType <em>Context Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.impl.BlackboxOperationDescriptorImpl#getParameterTypes <em>Parameter Types</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvtoperational.impl.BlackboxOperationDescriptorImpl#getReturnType <em>Return Type</em>}</li>
 * </ul>
 *
 * @generated
 */
public class BlackboxOperationDescriptorImpl extends EModelElementImpl implements BlackboxOperationDescriptor {
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
	 * The cached value of the '{@link #getContextType() <em>Context Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextType()
	 * @generated
	 * @ordered
	 */
	protected EClassifier contextType;

	/**
	 * The cached value of the '{@link #getParameterTypes() <em>Parameter Types</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameterTypes()
	 * @generated
	 * @ordered
	 */
	protected EList<EClassifier> parameterTypes;

	/**
	 * The cached value of the '{@link #getReturnType() <em>Return Type</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getReturnType()
	 * @generated
	 * @ordered
	 */
	protected EClassifier returnType;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected BlackboxOperationDescriptorImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QvtOperationalPackage.Literals.BLACKBOX_OPERATION_DESCRIPTOR;
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
			eNotify(new ENotificationImpl(this, Notification.SET, QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClassifier getContextType() {
		if (contextType != null && contextType.eIsProxy()) {
			InternalEObject oldContextType = (InternalEObject)contextType;
			contextType = (EClassifier)eResolveProxy(oldContextType);
			if (contextType != oldContextType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__CONTEXT_TYPE, oldContextType, contextType));
			}
		}
		return contextType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClassifier basicGetContextType() {
		return contextType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContextType(EClassifier newContextType) {
		EClassifier oldContextType = contextType;
		contextType = newContextType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__CONTEXT_TYPE, oldContextType, contextType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EClassifier> getParameterTypes() {
		if (parameterTypes == null) {
			parameterTypes = new EObjectResolvingEList<EClassifier>(EClassifier.class, this, QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__PARAMETER_TYPES);
		}
		return parameterTypes;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClassifier getReturnType() {
		if (returnType != null && returnType.eIsProxy()) {
			InternalEObject oldReturnType = (InternalEObject)returnType;
			returnType = (EClassifier)eResolveProxy(oldReturnType);
			if (returnType != oldReturnType) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__RETURN_TYPE, oldReturnType, returnType));
			}
		}
		return returnType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClassifier basicGetReturnType() {
		return returnType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setReturnType(EClassifier newReturnType) {
		EClassifier oldReturnType = returnType;
		returnType = newReturnType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__RETURN_TYPE, oldReturnType, returnType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__NAME:
				return getName();
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__CONTEXT_TYPE:
				if (resolve) return getContextType();
				return basicGetContextType();
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__PARAMETER_TYPES:
				return getParameterTypes();
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__RETURN_TYPE:
				if (resolve) return getReturnType();
				return basicGetReturnType();
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
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__NAME:
				setName((String)newValue);
				return;
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__CONTEXT_TYPE:
				setContextType((EClassifier)newValue);
				return;
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__PARAMETER_TYPES:
				getParameterTypes().clear();
				getParameterTypes().addAll((Collection<? extends EClassifier>)newValue);
				return;
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__RETURN_TYPE:
				setReturnType((EClassifier)newValue);
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
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__NAME:
				setName(NAME_EDEFAULT);
				return;
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__CONTEXT_TYPE:
				setContextType((EClassifier)null);
				return;
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__PARAMETER_TYPES:
				getParameterTypes().clear();
				return;
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__RETURN_TYPE:
				setReturnType((EClassifier)null);
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
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__CONTEXT_TYPE:
				return contextType != null;
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__PARAMETER_TYPES:
				return parameterTypes != null && !parameterTypes.isEmpty();
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR__RETURN_TYPE:
				return returnType != null;
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
		result.append(')');
		return result.toString();
	}

} //BlackboxOperationDescriptorImpl
