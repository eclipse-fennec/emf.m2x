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
package org.eclipse.fennec.m2x.model.ocl.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.ConstraintKind;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Constraint</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.ConstraintImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.ConstraintImpl#getKind <em>Kind</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.ConstraintImpl#getSpecification <em>Specification</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.ConstraintImpl#getContextClassifier <em>Context Classifier</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.ConstraintImpl#getContextOperation <em>Context Operation</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.ConstraintImpl#getContextProperty <em>Context Property</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.ocl.impl.ConstraintImpl#isIsStatic <em>Is Static</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ConstraintImpl extends MinimalEObjectImpl.Container implements Constraint {
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
	 * The default value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected static final ConstraintKind KIND_EDEFAULT = ConstraintKind.INV;

	/**
	 * The cached value of the '{@link #getKind() <em>Kind</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getKind()
	 * @generated
	 * @ordered
	 */
	protected ConstraintKind kind = KIND_EDEFAULT;

	/**
	 * The cached value of the '{@link #getSpecification() <em>Specification</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSpecification()
	 * @generated
	 * @ordered
	 */
	protected OclExpression specification;

	/**
	 * The cached value of the '{@link #getContextClassifier() <em>Context Classifier</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextClassifier()
	 * @generated
	 * @ordered
	 */
	protected EClassifier contextClassifier;

	/**
	 * The cached value of the '{@link #getContextOperation() <em>Context Operation</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextOperation()
	 * @generated
	 * @ordered
	 */
	protected EOperation contextOperation;

	/**
	 * The cached value of the '{@link #getContextProperty() <em>Context Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getContextProperty()
	 * @generated
	 * @ordered
	 */
	protected EStructuralFeature contextProperty;

	/**
	 * The default value of the '{@link #isIsStatic() <em>Is Static</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsStatic()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_STATIC_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsStatic() <em>Is Static</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsStatic()
	 * @generated
	 * @ordered
	 */
	protected boolean isStatic = IS_STATIC_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ConstraintImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return OclPackage.Literals.CONSTRAINT;
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
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.CONSTRAINT__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ConstraintKind getKind() {
		return kind;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setKind(ConstraintKind newKind) {
		ConstraintKind oldKind = kind;
		kind = newKind == null ? KIND_EDEFAULT : newKind;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.CONSTRAINT__KIND, oldKind, kind));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getSpecification() {
		return specification;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetSpecification(OclExpression newSpecification, NotificationChain msgs) {
		OclExpression oldSpecification = specification;
		specification = newSpecification;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, OclPackage.CONSTRAINT__SPECIFICATION, oldSpecification, newSpecification);
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
	public void setSpecification(OclExpression newSpecification) {
		if (newSpecification != specification) {
			NotificationChain msgs = null;
			if (specification != null)
				msgs = ((InternalEObject)specification).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - OclPackage.CONSTRAINT__SPECIFICATION, null, msgs);
			if (newSpecification != null)
				msgs = ((InternalEObject)newSpecification).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - OclPackage.CONSTRAINT__SPECIFICATION, null, msgs);
			msgs = basicSetSpecification(newSpecification, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.CONSTRAINT__SPECIFICATION, newSpecification, newSpecification));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClassifier getContextClassifier() {
		if (contextClassifier != null && contextClassifier.eIsProxy()) {
			InternalEObject oldContextClassifier = (InternalEObject)contextClassifier;
			contextClassifier = (EClassifier)eResolveProxy(oldContextClassifier);
			if (contextClassifier != oldContextClassifier) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OclPackage.CONSTRAINT__CONTEXT_CLASSIFIER, oldContextClassifier, contextClassifier));
			}
		}
		return contextClassifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EClassifier basicGetContextClassifier() {
		return contextClassifier;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContextClassifier(EClassifier newContextClassifier) {
		EClassifier oldContextClassifier = contextClassifier;
		contextClassifier = newContextClassifier;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.CONSTRAINT__CONTEXT_CLASSIFIER, oldContextClassifier, contextClassifier));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EOperation getContextOperation() {
		if (contextOperation != null && contextOperation.eIsProxy()) {
			InternalEObject oldContextOperation = (InternalEObject)contextOperation;
			contextOperation = (EOperation)eResolveProxy(oldContextOperation);
			if (contextOperation != oldContextOperation) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OclPackage.CONSTRAINT__CONTEXT_OPERATION, oldContextOperation, contextOperation));
			}
		}
		return contextOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EOperation basicGetContextOperation() {
		return contextOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContextOperation(EOperation newContextOperation) {
		EOperation oldContextOperation = contextOperation;
		contextOperation = newContextOperation;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.CONSTRAINT__CONTEXT_OPERATION, oldContextOperation, contextOperation));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EStructuralFeature getContextProperty() {
		if (contextProperty != null && contextProperty.eIsProxy()) {
			InternalEObject oldContextProperty = (InternalEObject)contextProperty;
			contextProperty = (EStructuralFeature)eResolveProxy(oldContextProperty);
			if (contextProperty != oldContextProperty) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, OclPackage.CONSTRAINT__CONTEXT_PROPERTY, oldContextProperty, contextProperty));
			}
		}
		return contextProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EStructuralFeature basicGetContextProperty() {
		return contextProperty;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setContextProperty(EStructuralFeature newContextProperty) {
		EStructuralFeature oldContextProperty = contextProperty;
		contextProperty = newContextProperty;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.CONSTRAINT__CONTEXT_PROPERTY, oldContextProperty, contextProperty));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIsStatic() {
		return isStatic;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsStatic(boolean newIsStatic) {
		boolean oldIsStatic = isStatic;
		isStatic = newIsStatic;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, OclPackage.CONSTRAINT__IS_STATIC, oldIsStatic, isStatic));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case OclPackage.CONSTRAINT__SPECIFICATION:
				return basicSetSpecification(null, msgs);
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
			case OclPackage.CONSTRAINT__NAME:
				return getName();
			case OclPackage.CONSTRAINT__KIND:
				return getKind();
			case OclPackage.CONSTRAINT__SPECIFICATION:
				return getSpecification();
			case OclPackage.CONSTRAINT__CONTEXT_CLASSIFIER:
				if (resolve) return getContextClassifier();
				return basicGetContextClassifier();
			case OclPackage.CONSTRAINT__CONTEXT_OPERATION:
				if (resolve) return getContextOperation();
				return basicGetContextOperation();
			case OclPackage.CONSTRAINT__CONTEXT_PROPERTY:
				if (resolve) return getContextProperty();
				return basicGetContextProperty();
			case OclPackage.CONSTRAINT__IS_STATIC:
				return isIsStatic();
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
			case OclPackage.CONSTRAINT__NAME:
				setName((String)newValue);
				return;
			case OclPackage.CONSTRAINT__KIND:
				setKind((ConstraintKind)newValue);
				return;
			case OclPackage.CONSTRAINT__SPECIFICATION:
				setSpecification((OclExpression)newValue);
				return;
			case OclPackage.CONSTRAINT__CONTEXT_CLASSIFIER:
				setContextClassifier((EClassifier)newValue);
				return;
			case OclPackage.CONSTRAINT__CONTEXT_OPERATION:
				setContextOperation((EOperation)newValue);
				return;
			case OclPackage.CONSTRAINT__CONTEXT_PROPERTY:
				setContextProperty((EStructuralFeature)newValue);
				return;
			case OclPackage.CONSTRAINT__IS_STATIC:
				setIsStatic((Boolean)newValue);
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
			case OclPackage.CONSTRAINT__NAME:
				setName(NAME_EDEFAULT);
				return;
			case OclPackage.CONSTRAINT__KIND:
				setKind(KIND_EDEFAULT);
				return;
			case OclPackage.CONSTRAINT__SPECIFICATION:
				setSpecification((OclExpression)null);
				return;
			case OclPackage.CONSTRAINT__CONTEXT_CLASSIFIER:
				setContextClassifier((EClassifier)null);
				return;
			case OclPackage.CONSTRAINT__CONTEXT_OPERATION:
				setContextOperation((EOperation)null);
				return;
			case OclPackage.CONSTRAINT__CONTEXT_PROPERTY:
				setContextProperty((EStructuralFeature)null);
				return;
			case OclPackage.CONSTRAINT__IS_STATIC:
				setIsStatic(IS_STATIC_EDEFAULT);
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
			case OclPackage.CONSTRAINT__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case OclPackage.CONSTRAINT__KIND:
				return kind != KIND_EDEFAULT;
			case OclPackage.CONSTRAINT__SPECIFICATION:
				return specification != null;
			case OclPackage.CONSTRAINT__CONTEXT_CLASSIFIER:
				return contextClassifier != null;
			case OclPackage.CONSTRAINT__CONTEXT_OPERATION:
				return contextOperation != null;
			case OclPackage.CONSTRAINT__CONTEXT_PROPERTY:
				return contextProperty != null;
			case OclPackage.CONSTRAINT__IS_STATIC:
				return isStatic != IS_STATIC_EDEFAULT;
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
		result.append(", kind: ");
		result.append(kind);
		result.append(", isStatic: ");
		result.append(isStatic);
		result.append(')');
		return result.toString();
	}

} //ConstraintImpl
