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
package org.eclipse.fennec.m2x.model.qvttemplate;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Property Template Item</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Specifies constraints on the values of slots of the model element matching the container object template expression (QVT v1.3 §7.11.2.4).
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#isIsOpposite <em>Is Opposite</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getReferredProperty <em>Referred Property</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getValue <em>Value</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getObjContainer <em>Obj Container</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getPropertyTemplateItem()
 * @model
 * @generated
 */
@ProviderType
public interface PropertyTemplateItem extends EObject, EModelElement {
	/**
	 * Returns the value of the '<em><b>Is Opposite</b></em>' attribute.
	 * The default value is <code>"false"</code>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Specifies whether the referred property is owned by the opposite end class. Default is false.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Is Opposite</em>' attribute.
	 * @see #setIsOpposite(boolean)
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getPropertyTemplateItem_IsOpposite()
	 * @model default="false"
	 * @generated
	 */
	boolean isIsOpposite();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#isIsOpposite <em>Is Opposite</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Opposite</em>' attribute.
	 * @see #isIsOpposite()
	 * @generated
	 */
	void setIsOpposite(boolean value);

	/**
	 * Returns the value of the '<em><b>Referred Property</b></em>' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The EStructuralFeature that identifies the slot that is being constrained.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Referred Property</em>' reference.
	 * @see #setReferredProperty(EStructuralFeature)
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getPropertyTemplateItem_ReferredProperty()
	 * @model required="true"
	 * @generated
	 */
	EStructuralFeature getReferredProperty();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getReferredProperty <em>Referred Property</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Referred Property</em>' reference.
	 * @see #getReferredProperty()
	 * @generated
	 */
	void setReferredProperty(EStructuralFeature value);

	/**
	 * Returns the value of the '<em><b>Value</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The value expression that the slot may take.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Value</em>' containment reference.
	 * @see #setValue(OclExpression)
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getPropertyTemplateItem_Value()
	 * @model containment="true" required="true"
	 * @generated
	 */
	OclExpression getValue();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getValue <em>Value</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Value</em>' containment reference.
	 * @see #getValue()
	 * @generated
	 */
	void setValue(OclExpression value);

	/**
	 * Returns the value of the '<em><b>Obj Container</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp#getPart <em>Part</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The object template expression that owns this property template item.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Obj Container</em>' container reference.
	 * @see #setObjContainer(ObjectTemplateExp)
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage#getPropertyTemplateItem_ObjContainer()
	 * @see org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp#getPart
	 * @model opposite="part" required="true" transient="false"
	 * @generated
	 */
	ObjectTemplateExp getObjContainer();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem#getObjContainer <em>Obj Container</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Obj Container</em>' container reference.
	 * @see #getObjContainer()
	 * @generated
	 */
	void setObjContainer(ObjectTemplateExp value);

} // PropertyTemplateItem
