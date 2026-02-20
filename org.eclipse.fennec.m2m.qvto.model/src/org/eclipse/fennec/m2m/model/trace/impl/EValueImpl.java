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
package org.eclipse.fennec.m2m.model.trace.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2m.model.trace.EValue;
import org.eclipse.fennec.m2m.model.trace.TracePackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>EValue</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.impl.EValueImpl#getPrimitiveValue <em>Primitive Value</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.impl.EValueImpl#getOclObject <em>Ocl Object</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.impl.EValueImpl#getCollectionType <em>Collection Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.impl.EValueImpl#getIntermediateElement <em>Intermediate Element</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.impl.EValueImpl#getCollection <em>Collection</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.impl.EValueImpl#getModelElement <em>Model Element</em>}</li>
 * </ul>
 *
 * @generated
 */
public class EValueImpl extends MinimalEObjectImpl.Container implements EValue {
	/**
	 * The default value of the '{@link #getPrimitiveValue() <em>Primitive Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimitiveValue()
	 * @generated
	 * @ordered
	 */
	protected static final String PRIMITIVE_VALUE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getPrimitiveValue() <em>Primitive Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPrimitiveValue()
	 * @generated
	 * @ordered
	 */
	protected String primitiveValue = PRIMITIVE_VALUE_EDEFAULT;

	/**
	 * The default value of the '{@link #getOclObject() <em>Ocl Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOclObject()
	 * @generated
	 * @ordered
	 */
	protected static final Object OCL_OBJECT_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getOclObject() <em>Ocl Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOclObject()
	 * @generated
	 * @ordered
	 */
	protected Object oclObject = OCL_OBJECT_EDEFAULT;

	/**
	 * The default value of the '{@link #getCollectionType() <em>Collection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionType()
	 * @generated
	 * @ordered
	 */
	protected static final String COLLECTION_TYPE_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getCollectionType() <em>Collection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollectionType()
	 * @generated
	 * @ordered
	 */
	protected String collectionType = COLLECTION_TYPE_EDEFAULT;

	/**
	 * The cached value of the '{@link #getIntermediateElement() <em>Intermediate Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIntermediateElement()
	 * @generated
	 * @ordered
	 */
	protected EObject intermediateElement;

	/**
	 * The cached value of the '{@link #getCollection() <em>Collection</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getCollection()
	 * @generated
	 * @ordered
	 */
	protected EList<EValue> collection;

	/**
	 * The cached value of the '{@link #getModelElement() <em>Model Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getModelElement()
	 * @generated
	 * @ordered
	 */
	protected EObject modelElement;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected EValueImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TracePackage.Literals.EVALUE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getPrimitiveValue() {
		return primitiveValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPrimitiveValue(String newPrimitiveValue) {
		String oldPrimitiveValue = primitiveValue;
		primitiveValue = newPrimitiveValue;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.EVALUE__PRIMITIVE_VALUE, oldPrimitiveValue, primitiveValue));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object getOclObject() {
		return oclObject;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setOclObject(Object newOclObject) {
		Object oldOclObject = oclObject;
		oclObject = newOclObject;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.EVALUE__OCL_OBJECT, oldOclObject, oclObject));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getCollectionType() {
		return collectionType;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setCollectionType(String newCollectionType) {
		String oldCollectionType = collectionType;
		collectionType = newCollectionType;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.EVALUE__COLLECTION_TYPE, oldCollectionType, collectionType));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getIntermediateElement() {
		return intermediateElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetIntermediateElement(EObject newIntermediateElement, NotificationChain msgs) {
		EObject oldIntermediateElement = intermediateElement;
		intermediateElement = newIntermediateElement;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, TracePackage.EVALUE__INTERMEDIATE_ELEMENT, oldIntermediateElement, newIntermediateElement);
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
	public void setIntermediateElement(EObject newIntermediateElement) {
		if (newIntermediateElement != intermediateElement) {
			NotificationChain msgs = null;
			if (intermediateElement != null)
				msgs = ((InternalEObject)intermediateElement).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - TracePackage.EVALUE__INTERMEDIATE_ELEMENT, null, msgs);
			if (newIntermediateElement != null)
				msgs = ((InternalEObject)newIntermediateElement).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - TracePackage.EVALUE__INTERMEDIATE_ELEMENT, null, msgs);
			msgs = basicSetIntermediateElement(newIntermediateElement, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.EVALUE__INTERMEDIATE_ELEMENT, newIntermediateElement, newIntermediateElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<EValue> getCollection() {
		if (collection == null) {
			collection = new EObjectContainmentEList<EValue>(EValue.class, this, TracePackage.EVALUE__COLLECTION);
		}
		return collection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject getModelElement() {
		if (modelElement != null && modelElement.eIsProxy()) {
			InternalEObject oldModelElement = (InternalEObject)modelElement;
			modelElement = eResolveProxy(oldModelElement);
			if (modelElement != oldModelElement) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, TracePackage.EVALUE__MODEL_ELEMENT, oldModelElement, modelElement));
			}
		}
		return modelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EObject basicGetModelElement() {
		return modelElement;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setModelElement(EObject newModelElement) {
		EObject oldModelElement = modelElement;
		modelElement = newModelElement;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, TracePackage.EVALUE__MODEL_ELEMENT, oldModelElement, modelElement));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TracePackage.EVALUE__INTERMEDIATE_ELEMENT:
				return basicSetIntermediateElement(null, msgs);
			case TracePackage.EVALUE__COLLECTION:
				return ((InternalEList<?>)getCollection()).basicRemove(otherEnd, msgs);
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
			case TracePackage.EVALUE__PRIMITIVE_VALUE:
				return getPrimitiveValue();
			case TracePackage.EVALUE__OCL_OBJECT:
				return getOclObject();
			case TracePackage.EVALUE__COLLECTION_TYPE:
				return getCollectionType();
			case TracePackage.EVALUE__INTERMEDIATE_ELEMENT:
				return getIntermediateElement();
			case TracePackage.EVALUE__COLLECTION:
				return getCollection();
			case TracePackage.EVALUE__MODEL_ELEMENT:
				if (resolve) return getModelElement();
				return basicGetModelElement();
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
			case TracePackage.EVALUE__PRIMITIVE_VALUE:
				setPrimitiveValue((String)newValue);
				return;
			case TracePackage.EVALUE__OCL_OBJECT:
				setOclObject(newValue);
				return;
			case TracePackage.EVALUE__COLLECTION_TYPE:
				setCollectionType((String)newValue);
				return;
			case TracePackage.EVALUE__INTERMEDIATE_ELEMENT:
				setIntermediateElement((EObject)newValue);
				return;
			case TracePackage.EVALUE__COLLECTION:
				getCollection().clear();
				getCollection().addAll((Collection<? extends EValue>)newValue);
				return;
			case TracePackage.EVALUE__MODEL_ELEMENT:
				setModelElement((EObject)newValue);
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
			case TracePackage.EVALUE__PRIMITIVE_VALUE:
				setPrimitiveValue(PRIMITIVE_VALUE_EDEFAULT);
				return;
			case TracePackage.EVALUE__OCL_OBJECT:
				setOclObject(OCL_OBJECT_EDEFAULT);
				return;
			case TracePackage.EVALUE__COLLECTION_TYPE:
				setCollectionType(COLLECTION_TYPE_EDEFAULT);
				return;
			case TracePackage.EVALUE__INTERMEDIATE_ELEMENT:
				setIntermediateElement((EObject)null);
				return;
			case TracePackage.EVALUE__COLLECTION:
				getCollection().clear();
				return;
			case TracePackage.EVALUE__MODEL_ELEMENT:
				setModelElement((EObject)null);
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
			case TracePackage.EVALUE__PRIMITIVE_VALUE:
				return PRIMITIVE_VALUE_EDEFAULT == null ? primitiveValue != null : !PRIMITIVE_VALUE_EDEFAULT.equals(primitiveValue);
			case TracePackage.EVALUE__OCL_OBJECT:
				return OCL_OBJECT_EDEFAULT == null ? oclObject != null : !OCL_OBJECT_EDEFAULT.equals(oclObject);
			case TracePackage.EVALUE__COLLECTION_TYPE:
				return COLLECTION_TYPE_EDEFAULT == null ? collectionType != null : !COLLECTION_TYPE_EDEFAULT.equals(collectionType);
			case TracePackage.EVALUE__INTERMEDIATE_ELEMENT:
				return intermediateElement != null;
			case TracePackage.EVALUE__COLLECTION:
				return collection != null && !collection.isEmpty();
			case TracePackage.EVALUE__MODEL_ELEMENT:
				return modelElement != null;
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
		result.append(" (primitiveValue: ");
		result.append(primitiveValue);
		result.append(", oclObject: ");
		result.append(oclObject);
		result.append(", collectionType: ");
		result.append(collectionType);
		result.append(')');
		return result.toString();
	}

} //EValueImpl
