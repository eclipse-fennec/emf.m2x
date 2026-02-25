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
package org.eclipse.fennec.m2x.model.trace;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>EValue</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A trace value: can be a primitive, model element reference, or collection of values.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.EValue#getPrimitiveValue <em>Primitive Value</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.EValue#getOclObject <em>Ocl Object</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.EValue#getCollectionType <em>Collection Type</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.EValue#getIntermediateElement <em>Intermediate Element</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.EValue#getCollection <em>Collection</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.EValue#getModelElement <em>Model Element</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getEValue()
 * @model
 * @generated
 */
@ProviderType
public interface EValue extends EObject {
	/**
	 * Returns the value of the '<em><b>Primitive Value</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Primitive Value</em>' attribute.
	 * @see #setPrimitiveValue(String)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getEValue_PrimitiveValue()
	 * @model
	 * @generated
	 */
	String getPrimitiveValue();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.EValue#getPrimitiveValue <em>Primitive Value</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Primitive Value</em>' attribute.
	 * @see #getPrimitiveValue()
	 * @generated
	 */
	void setPrimitiveValue(String value);

	/**
	 * Returns the value of the '<em><b>Ocl Object</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Ocl Object</em>' attribute.
	 * @see #setOclObject(Object)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getEValue_OclObject()
	 * @model transient="true"
	 * @generated
	 */
	Object getOclObject();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.EValue#getOclObject <em>Ocl Object</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Ocl Object</em>' attribute.
	 * @see #getOclObject()
	 * @generated
	 */
	void setOclObject(Object value);

	/**
	 * Returns the value of the '<em><b>Collection Type</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Collection Type</em>' attribute.
	 * @see #setCollectionType(String)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getEValue_CollectionType()
	 * @model
	 * @generated
	 */
	String getCollectionType();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.EValue#getCollectionType <em>Collection Type</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Collection Type</em>' attribute.
	 * @see #getCollectionType()
	 * @generated
	 */
	void setCollectionType(String value);

	/**
	 * Returns the value of the '<em><b>Intermediate Element</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Intermediate Element</em>' containment reference.
	 * @see #setIntermediateElement(EObject)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getEValue_IntermediateElement()
	 * @model containment="true"
	 * @generated
	 */
	EObject getIntermediateElement();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.EValue#getIntermediateElement <em>Intermediate Element</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Intermediate Element</em>' containment reference.
	 * @see #getIntermediateElement()
	 * @generated
	 */
	void setIntermediateElement(EObject value);

	/**
	 * Returns the value of the '<em><b>Collection</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.trace.EValue}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Collection</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getEValue_Collection()
	 * @model containment="true"
	 * @generated
	 */
	EList<EValue> getCollection();

	/**
	 * Returns the value of the '<em><b>Model Element</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Model Element</em>' reference.
	 * @see #setModelElement(EObject)
	 * @see org.eclipse.fennec.m2x.model.trace.TracePackage#getEValue_ModelElement()
	 * @model
	 * @generated
	 */
	EObject getModelElement();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.trace.EValue#getModelElement <em>Model Element</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Model Element</em>' reference.
	 * @see #getModelElement()
	 * @generated
	 */
	void setModelElement(EObject value);

} // EValue
