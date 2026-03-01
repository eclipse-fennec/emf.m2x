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

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.fennec.m2x.model.m2t.ForBlock;
import org.eclipse.fennec.m2x.model.m2t.M2tPackage;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>For Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl#getLoopVariable <em>Loop Variable</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl#getIterSet <em>Iter Set</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl#getGuard <em>Guard</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl#getBefore <em>Before</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl#getEach <em>Each</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.ForBlockImpl#getAfter <em>After</em>}</li>
 * </ul>
 *
 * @generated
 */
public class ForBlockImpl extends BlockImpl implements ForBlock {
	/**
	 * The cached value of the '{@link #getLoopVariable() <em>Loop Variable</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getLoopVariable()
	 * @generated
	 * @ordered
	 */
	protected Variable loopVariable;

	/**
	 * The cached value of the '{@link #getIterSet() <em>Iter Set</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIterSet()
	 * @generated
	 * @ordered
	 */
	protected OclExpression iterSet;

	/**
	 * The cached value of the '{@link #getGuard() <em>Guard</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGuard()
	 * @generated
	 * @ordered
	 */
	protected OclExpression guard;

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
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected ForBlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return M2tPackage.Literals.FOR_BLOCK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Variable getLoopVariable() {
		return loopVariable;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetLoopVariable(Variable newLoopVariable, NotificationChain msgs) {
		Variable oldLoopVariable = loopVariable;
		loopVariable = newLoopVariable;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__LOOP_VARIABLE, oldLoopVariable, newLoopVariable);
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
	public void setLoopVariable(Variable newLoopVariable) {
		if (newLoopVariable != loopVariable) {
			NotificationChain msgs = null;
			if (loopVariable != null)
				msgs = ((InternalEObject)loopVariable).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__LOOP_VARIABLE, null, msgs);
			if (newLoopVariable != null)
				msgs = ((InternalEObject)newLoopVariable).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__LOOP_VARIABLE, null, msgs);
			msgs = basicSetLoopVariable(newLoopVariable, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__LOOP_VARIABLE, newLoopVariable, newLoopVariable));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getIterSet() {
		return iterSet;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetIterSet(OclExpression newIterSet, NotificationChain msgs) {
		OclExpression oldIterSet = iterSet;
		iterSet = newIterSet;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__ITER_SET, oldIterSet, newIterSet);
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
	public void setIterSet(OclExpression newIterSet) {
		if (newIterSet != iterSet) {
			NotificationChain msgs = null;
			if (iterSet != null)
				msgs = ((InternalEObject)iterSet).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__ITER_SET, null, msgs);
			if (newIterSet != null)
				msgs = ((InternalEObject)newIterSet).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__ITER_SET, null, msgs);
			msgs = basicSetIterSet(newIterSet, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__ITER_SET, newIterSet, newIterSet));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getGuard() {
		return guard;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGuard(OclExpression newGuard, NotificationChain msgs) {
		OclExpression oldGuard = guard;
		guard = newGuard;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__GUARD, oldGuard, newGuard);
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
	public void setGuard(OclExpression newGuard) {
		if (newGuard != guard) {
			NotificationChain msgs = null;
			if (guard != null)
				msgs = ((InternalEObject)guard).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__GUARD, null, msgs);
			if (newGuard != null)
				msgs = ((InternalEObject)newGuard).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__GUARD, null, msgs);
			msgs = basicSetGuard(newGuard, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__GUARD, newGuard, newGuard));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__BEFORE, oldBefore, newBefore);
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
				msgs = ((InternalEObject)before).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__BEFORE, null, msgs);
			if (newBefore != null)
				msgs = ((InternalEObject)newBefore).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__BEFORE, null, msgs);
			msgs = basicSetBefore(newBefore, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__BEFORE, newBefore, newBefore));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__EACH, oldEach, newEach);
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
				msgs = ((InternalEObject)each).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__EACH, null, msgs);
			if (newEach != null)
				msgs = ((InternalEObject)newEach).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__EACH, null, msgs);
			msgs = basicSetEach(newEach, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__EACH, newEach, newEach));
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
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__AFTER, oldAfter, newAfter);
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
				msgs = ((InternalEObject)after).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__AFTER, null, msgs);
			if (newAfter != null)
				msgs = ((InternalEObject)newAfter).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.FOR_BLOCK__AFTER, null, msgs);
			msgs = basicSetAfter(newAfter, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.FOR_BLOCK__AFTER, newAfter, newAfter));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case M2tPackage.FOR_BLOCK__LOOP_VARIABLE:
				return basicSetLoopVariable(null, msgs);
			case M2tPackage.FOR_BLOCK__ITER_SET:
				return basicSetIterSet(null, msgs);
			case M2tPackage.FOR_BLOCK__GUARD:
				return basicSetGuard(null, msgs);
			case M2tPackage.FOR_BLOCK__BEFORE:
				return basicSetBefore(null, msgs);
			case M2tPackage.FOR_BLOCK__EACH:
				return basicSetEach(null, msgs);
			case M2tPackage.FOR_BLOCK__AFTER:
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
			case M2tPackage.FOR_BLOCK__LOOP_VARIABLE:
				return getLoopVariable();
			case M2tPackage.FOR_BLOCK__ITER_SET:
				return getIterSet();
			case M2tPackage.FOR_BLOCK__GUARD:
				return getGuard();
			case M2tPackage.FOR_BLOCK__BEFORE:
				return getBefore();
			case M2tPackage.FOR_BLOCK__EACH:
				return getEach();
			case M2tPackage.FOR_BLOCK__AFTER:
				return getAfter();
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
			case M2tPackage.FOR_BLOCK__LOOP_VARIABLE:
				setLoopVariable((Variable)newValue);
				return;
			case M2tPackage.FOR_BLOCK__ITER_SET:
				setIterSet((OclExpression)newValue);
				return;
			case M2tPackage.FOR_BLOCK__GUARD:
				setGuard((OclExpression)newValue);
				return;
			case M2tPackage.FOR_BLOCK__BEFORE:
				setBefore((OclExpression)newValue);
				return;
			case M2tPackage.FOR_BLOCK__EACH:
				setEach((OclExpression)newValue);
				return;
			case M2tPackage.FOR_BLOCK__AFTER:
				setAfter((OclExpression)newValue);
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
			case M2tPackage.FOR_BLOCK__LOOP_VARIABLE:
				setLoopVariable((Variable)null);
				return;
			case M2tPackage.FOR_BLOCK__ITER_SET:
				setIterSet((OclExpression)null);
				return;
			case M2tPackage.FOR_BLOCK__GUARD:
				setGuard((OclExpression)null);
				return;
			case M2tPackage.FOR_BLOCK__BEFORE:
				setBefore((OclExpression)null);
				return;
			case M2tPackage.FOR_BLOCK__EACH:
				setEach((OclExpression)null);
				return;
			case M2tPackage.FOR_BLOCK__AFTER:
				setAfter((OclExpression)null);
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
			case M2tPackage.FOR_BLOCK__LOOP_VARIABLE:
				return loopVariable != null;
			case M2tPackage.FOR_BLOCK__ITER_SET:
				return iterSet != null;
			case M2tPackage.FOR_BLOCK__GUARD:
				return guard != null;
			case M2tPackage.FOR_BLOCK__BEFORE:
				return before != null;
			case M2tPackage.FOR_BLOCK__EACH:
				return each != null;
			case M2tPackage.FOR_BLOCK__AFTER:
				return after != null;
		}
		return super.eIsSet(featureID);
	}

} //ForBlockImpl
