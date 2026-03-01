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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2x.model.m2t.M2tPackage;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Template Invocation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl#getDefinition <em>Definition</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl#getArgument <em>Argument</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl#getBefore <em>Before</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl#getEach <em>Each</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl#getAfter <em>After</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateInvocationImpl#isSuper <em>Super</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TemplateInvocationImpl extends TemplateExpressionImpl implements TemplateInvocation {
	/**
	 * The cached value of the '{@link #getDefinition() <em>Definition</em>}' reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDefinition()
	 * @generated
	 * @ordered
	 */
	protected Template definition;

	/**
	 * The cached value of the '{@link #getArgument() <em>Argument</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getArgument()
	 * @generated
	 * @ordered
	 */
	protected EList<OclExpression> argument;

	/**
	 * The cached value of the '{@link #getBefore() <em>Before</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getBefore()
	 * @generated
	 * @ordered
	 */
	protected OclExpression before;

	/**
	 * The cached value of the '{@link #getEach() <em>Each</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getEach()
	 * @generated
	 * @ordered
	 */
	protected OclExpression each;

	/**
	 * The cached value of the '{@link #getAfter() <em>After</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAfter()
	 * @generated
	 * @ordered
	 */
	protected OclExpression after;

	/**
	 * The default value of the '{@link #isSuper() <em>Super</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSuper()
	 * @generated
	 * @ordered
	 */
	protected static final boolean SUPER_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isSuper() <em>Super</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isSuper()
	 * @generated
	 * @ordered
	 */
	protected boolean super_ = SUPER_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TemplateInvocationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return M2tPackage.Literals.TEMPLATE_INVOCATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Template getDefinition() {
		if (definition != null && definition.eIsProxy()) {
			InternalEObject oldDefinition = (InternalEObject)definition;
			definition = (Template)eResolveProxy(oldDefinition);
			if (definition != oldDefinition) {
				if (eNotificationRequired())
					eNotify(new ENotificationImpl(this, Notification.RESOLVE, M2tPackage.TEMPLATE_INVOCATION__DEFINITION, oldDefinition, definition));
			}
		}
		return definition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Template basicGetDefinition() {
		return definition;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setDefinition(Template newDefinition) {
		Template oldDefinition = definition;
		definition = newDefinition;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE_INVOCATION__DEFINITION, oldDefinition, definition));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OclExpression> getArgument() {
		if (argument == null) {
			argument = new EObjectContainmentEList<OclExpression>(OclExpression.class, this, M2tPackage.TEMPLATE_INVOCATION__ARGUMENT);
		}
		return argument;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getBefore() {
		return before;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetBefore(OclExpression newBefore, NotificationChain msgs) {
		OclExpression oldBefore = before;
		before = newBefore;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE_INVOCATION__BEFORE, oldBefore, newBefore);
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
	public void setBefore(OclExpression newBefore) {
		if (newBefore != before) {
			NotificationChain msgs = null;
			if (before != null)
				msgs = ((InternalEObject)before).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE_INVOCATION__BEFORE, null, msgs);
			if (newBefore != null)
				msgs = ((InternalEObject)newBefore).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE_INVOCATION__BEFORE, null, msgs);
			msgs = basicSetBefore(newBefore, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE_INVOCATION__BEFORE, newBefore, newBefore));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getEach() {
		return each;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEach(OclExpression newEach, NotificationChain msgs) {
		OclExpression oldEach = each;
		each = newEach;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE_INVOCATION__EACH, oldEach, newEach);
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
	public void setEach(OclExpression newEach) {
		if (newEach != each) {
			NotificationChain msgs = null;
			if (each != null)
				msgs = ((InternalEObject)each).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE_INVOCATION__EACH, null, msgs);
			if (newEach != null)
				msgs = ((InternalEObject)newEach).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE_INVOCATION__EACH, null, msgs);
			msgs = basicSetEach(newEach, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE_INVOCATION__EACH, newEach, newEach));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getAfter() {
		return after;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetAfter(OclExpression newAfter, NotificationChain msgs) {
		OclExpression oldAfter = after;
		after = newAfter;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE_INVOCATION__AFTER, oldAfter, newAfter);
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
	public void setAfter(OclExpression newAfter) {
		if (newAfter != after) {
			NotificationChain msgs = null;
			if (after != null)
				msgs = ((InternalEObject)after).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE_INVOCATION__AFTER, null, msgs);
			if (newAfter != null)
				msgs = ((InternalEObject)newAfter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE_INVOCATION__AFTER, null, msgs);
			msgs = basicSetAfter(newAfter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE_INVOCATION__AFTER, newAfter, newAfter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isSuper() {
		return super_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSuper(boolean newSuper) {
		boolean oldSuper = super_;
		super_ = newSuper;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE_INVOCATION__SUPER, oldSuper, super_));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case M2tPackage.TEMPLATE_INVOCATION__ARGUMENT:
				return ((InternalEList<?>)getArgument()).basicRemove(otherEnd, msgs);
			case M2tPackage.TEMPLATE_INVOCATION__BEFORE:
				return basicSetBefore(null, msgs);
			case M2tPackage.TEMPLATE_INVOCATION__EACH:
				return basicSetEach(null, msgs);
			case M2tPackage.TEMPLATE_INVOCATION__AFTER:
				return basicSetAfter(null, msgs);
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
			case M2tPackage.TEMPLATE_INVOCATION__DEFINITION:
				if (resolve) return getDefinition();
				return basicGetDefinition();
			case M2tPackage.TEMPLATE_INVOCATION__ARGUMENT:
				return getArgument();
			case M2tPackage.TEMPLATE_INVOCATION__BEFORE:
				return getBefore();
			case M2tPackage.TEMPLATE_INVOCATION__EACH:
				return getEach();
			case M2tPackage.TEMPLATE_INVOCATION__AFTER:
				return getAfter();
			case M2tPackage.TEMPLATE_INVOCATION__SUPER:
				return isSuper();
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
			case M2tPackage.TEMPLATE_INVOCATION__DEFINITION:
				setDefinition((Template)newValue);
				return;
			case M2tPackage.TEMPLATE_INVOCATION__ARGUMENT:
				getArgument().clear();
				getArgument().addAll((Collection<? extends OclExpression>)newValue);
				return;
			case M2tPackage.TEMPLATE_INVOCATION__BEFORE:
				setBefore((OclExpression)newValue);
				return;
			case M2tPackage.TEMPLATE_INVOCATION__EACH:
				setEach((OclExpression)newValue);
				return;
			case M2tPackage.TEMPLATE_INVOCATION__AFTER:
				setAfter((OclExpression)newValue);
				return;
			case M2tPackage.TEMPLATE_INVOCATION__SUPER:
				setSuper((Boolean)newValue);
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
			case M2tPackage.TEMPLATE_INVOCATION__DEFINITION:
				setDefinition((Template)null);
				return;
			case M2tPackage.TEMPLATE_INVOCATION__ARGUMENT:
				getArgument().clear();
				return;
			case M2tPackage.TEMPLATE_INVOCATION__BEFORE:
				setBefore((OclExpression)null);
				return;
			case M2tPackage.TEMPLATE_INVOCATION__EACH:
				setEach((OclExpression)null);
				return;
			case M2tPackage.TEMPLATE_INVOCATION__AFTER:
				setAfter((OclExpression)null);
				return;
			case M2tPackage.TEMPLATE_INVOCATION__SUPER:
				setSuper(SUPER_EDEFAULT);
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
			case M2tPackage.TEMPLATE_INVOCATION__DEFINITION:
				return definition != null;
			case M2tPackage.TEMPLATE_INVOCATION__ARGUMENT:
				return argument != null && !argument.isEmpty();
			case M2tPackage.TEMPLATE_INVOCATION__BEFORE:
				return before != null;
			case M2tPackage.TEMPLATE_INVOCATION__EACH:
				return each != null;
			case M2tPackage.TEMPLATE_INVOCATION__AFTER:
				return after != null;
			case M2tPackage.TEMPLATE_INVOCATION__SUPER:
				return super_ != SUPER_EDEFAULT;
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
		result.append(" (super: ");
		result.append(super_);
		result.append(')');
		return result.toString();
	}

} //TemplateInvocationImpl
