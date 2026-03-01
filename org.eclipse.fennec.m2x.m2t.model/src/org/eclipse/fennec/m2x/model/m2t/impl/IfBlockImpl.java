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

import org.eclipse.fennec.m2x.model.m2t.Block;
import org.eclipse.fennec.m2x.model.m2t.IfBlock;
import org.eclipse.fennec.m2x.model.m2t.M2tPackage;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>If Block</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.IfBlockImpl#getIfExpr <em>If Expr</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.IfBlockImpl#getElseIf <em>Else If</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.IfBlockImpl#getElse <em>Else</em>}</li>
 * </ul>
 *
 * @generated
 */
public class IfBlockImpl extends BlockImpl implements IfBlock {
	/**
	 * The cached value of the '{@link #getIfExpr() <em>If Expr</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getIfExpr()
	 * @generated
	 * @ordered
	 */
	protected OclExpression ifExpr;

	/**
	 * The cached value of the '{@link #getElseIf() <em>Else If</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElseIf()
	 * @generated
	 * @ordered
	 */
	protected EList<IfBlock> elseIf;

	/**
	 * The cached value of the '{@link #getElse() <em>Else</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getElse()
	 * @generated
	 * @ordered
	 */
	protected Block else_;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected IfBlockImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return M2tPackage.Literals.IF_BLOCK;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getIfExpr() {
		return ifExpr;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetIfExpr(OclExpression newIfExpr, NotificationChain msgs) {
		OclExpression oldIfExpr = ifExpr;
		ifExpr = newIfExpr;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.IF_BLOCK__IF_EXPR, oldIfExpr, newIfExpr);
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
	public void setIfExpr(OclExpression newIfExpr) {
		if (newIfExpr != ifExpr) {
			NotificationChain msgs = null;
			if (ifExpr != null)
				msgs = ((InternalEObject)ifExpr).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.IF_BLOCK__IF_EXPR, null, msgs);
			if (newIfExpr != null)
				msgs = ((InternalEObject)newIfExpr).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.IF_BLOCK__IF_EXPR, null, msgs);
			msgs = basicSetIfExpr(newIfExpr, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.IF_BLOCK__IF_EXPR, newIfExpr, newIfExpr));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<IfBlock> getElseIf() {
		if (elseIf == null) {
			elseIf = new EObjectContainmentEList<IfBlock>(IfBlock.class, this, M2tPackage.IF_BLOCK__ELSE_IF);
		}
		return elseIf;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Block getElse() {
		return else_;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetElse(Block newElse, NotificationChain msgs) {
		Block oldElse = else_;
		else_ = newElse;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.IF_BLOCK__ELSE, oldElse, newElse);
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
	public void setElse(Block newElse) {
		if (newElse != else_) {
			NotificationChain msgs = null;
			if (else_ != null)
				msgs = ((InternalEObject)else_).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.IF_BLOCK__ELSE, null, msgs);
			if (newElse != null)
				msgs = ((InternalEObject)newElse).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.IF_BLOCK__ELSE, null, msgs);
			msgs = basicSetElse(newElse, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.IF_BLOCK__ELSE, newElse, newElse));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case M2tPackage.IF_BLOCK__IF_EXPR:
				return basicSetIfExpr(null, msgs);
			case M2tPackage.IF_BLOCK__ELSE_IF:
				return ((InternalEList<?>)getElseIf()).basicRemove(otherEnd, msgs);
			case M2tPackage.IF_BLOCK__ELSE:
				return basicSetElse(null, msgs);
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
			case M2tPackage.IF_BLOCK__IF_EXPR:
				return getIfExpr();
			case M2tPackage.IF_BLOCK__ELSE_IF:
				return getElseIf();
			case M2tPackage.IF_BLOCK__ELSE:
				return getElse();
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
			case M2tPackage.IF_BLOCK__IF_EXPR:
				setIfExpr((OclExpression)newValue);
				return;
			case M2tPackage.IF_BLOCK__ELSE_IF:
				getElseIf().clear();
				getElseIf().addAll((Collection<? extends IfBlock>)newValue);
				return;
			case M2tPackage.IF_BLOCK__ELSE:
				setElse((Block)newValue);
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
			case M2tPackage.IF_BLOCK__IF_EXPR:
				setIfExpr((OclExpression)null);
				return;
			case M2tPackage.IF_BLOCK__ELSE_IF:
				getElseIf().clear();
				return;
			case M2tPackage.IF_BLOCK__ELSE:
				setElse((Block)null);
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
			case M2tPackage.IF_BLOCK__IF_EXPR:
				return ifExpr != null;
			case M2tPackage.IF_BLOCK__ELSE_IF:
				return elseIf != null && !elseIf.isEmpty();
			case M2tPackage.IF_BLOCK__ELSE:
				return else_ != null;
		}
		return super.eIsSet(featureID);
	}

} //IfBlockImpl
