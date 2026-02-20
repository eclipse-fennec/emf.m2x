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
package org.eclipse.fennec.m2m.model.qvtoperational.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2m.model.ocl.OclExpression;

import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Mapping Operation</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.impl.MappingOperationImpl#getWhen <em>When</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.impl.MappingOperationImpl#getWhere <em>Where</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.impl.MappingOperationImpl#getDisjunct <em>Disjunct</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.impl.MappingOperationImpl#getInherited <em>Inherited</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.qvtoperational.impl.MappingOperationImpl#getMerged <em>Merged</em>}</li>
 * </ul>
 *
 * @generated
 */
public class MappingOperationImpl extends ImperativeOperationImpl implements MappingOperation {
	/**
	 * The cached value of the '{@link #getWhen() <em>When</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWhen()
	 * @generated
	 * @ordered
	 */
	protected EList<OclExpression> when;

	/**
	 * The cached value of the '{@link #getWhere() <em>Where</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getWhere()
	 * @generated
	 * @ordered
	 */
	protected OclExpression where;

	/**
	 * The cached value of the '{@link #getDisjunct() <em>Disjunct</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getDisjunct()
	 * @generated
	 * @ordered
	 */
	protected EList<MappingOperation> disjunct;

	/**
	 * The cached value of the '{@link #getInherited() <em>Inherited</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getInherited()
	 * @generated
	 * @ordered
	 */
	protected EList<MappingOperation> inherited;

	/**
	 * The cached value of the '{@link #getMerged() <em>Merged</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getMerged()
	 * @generated
	 * @ordered
	 */
	protected EList<MappingOperation> merged;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected MappingOperationImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return QvtOperationalPackage.Literals.MAPPING_OPERATION;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<OclExpression> getWhen() {
		if (when == null) {
			when = new EObjectContainmentEList<OclExpression>(OclExpression.class, this, QvtOperationalPackage.MAPPING_OPERATION__WHEN);
		}
		return when;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getWhere() {
		return where;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetWhere(OclExpression newWhere, NotificationChain msgs) {
		OclExpression oldWhere = where;
		where = newWhere;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, QvtOperationalPackage.MAPPING_OPERATION__WHERE, oldWhere, newWhere);
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
	public void setWhere(OclExpression newWhere) {
		if (newWhere != where) {
			NotificationChain msgs = null;
			if (where != null)
				msgs = ((InternalEObject)where).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - QvtOperationalPackage.MAPPING_OPERATION__WHERE, null, msgs);
			if (newWhere != null)
				msgs = ((InternalEObject)newWhere).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - QvtOperationalPackage.MAPPING_OPERATION__WHERE, null, msgs);
			msgs = basicSetWhere(newWhere, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, QvtOperationalPackage.MAPPING_OPERATION__WHERE, newWhere, newWhere));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MappingOperation> getDisjunct() {
		if (disjunct == null) {
			disjunct = new EObjectResolvingEList<MappingOperation>(MappingOperation.class, this, QvtOperationalPackage.MAPPING_OPERATION__DISJUNCT);
		}
		return disjunct;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MappingOperation> getInherited() {
		if (inherited == null) {
			inherited = new EObjectResolvingEList<MappingOperation>(MappingOperation.class, this, QvtOperationalPackage.MAPPING_OPERATION__INHERITED);
		}
		return inherited;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<MappingOperation> getMerged() {
		if (merged == null) {
			merged = new EObjectResolvingEList<MappingOperation>(MappingOperation.class, this, QvtOperationalPackage.MAPPING_OPERATION__MERGED);
		}
		return merged;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case QvtOperationalPackage.MAPPING_OPERATION__WHEN:
				return ((InternalEList<?>)getWhen()).basicRemove(otherEnd, msgs);
			case QvtOperationalPackage.MAPPING_OPERATION__WHERE:
				return basicSetWhere(null, msgs);
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
			case QvtOperationalPackage.MAPPING_OPERATION__WHEN:
				return getWhen();
			case QvtOperationalPackage.MAPPING_OPERATION__WHERE:
				return getWhere();
			case QvtOperationalPackage.MAPPING_OPERATION__DISJUNCT:
				return getDisjunct();
			case QvtOperationalPackage.MAPPING_OPERATION__INHERITED:
				return getInherited();
			case QvtOperationalPackage.MAPPING_OPERATION__MERGED:
				return getMerged();
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
			case QvtOperationalPackage.MAPPING_OPERATION__WHEN:
				getWhen().clear();
				getWhen().addAll((Collection<? extends OclExpression>)newValue);
				return;
			case QvtOperationalPackage.MAPPING_OPERATION__WHERE:
				setWhere((OclExpression)newValue);
				return;
			case QvtOperationalPackage.MAPPING_OPERATION__DISJUNCT:
				getDisjunct().clear();
				getDisjunct().addAll((Collection<? extends MappingOperation>)newValue);
				return;
			case QvtOperationalPackage.MAPPING_OPERATION__INHERITED:
				getInherited().clear();
				getInherited().addAll((Collection<? extends MappingOperation>)newValue);
				return;
			case QvtOperationalPackage.MAPPING_OPERATION__MERGED:
				getMerged().clear();
				getMerged().addAll((Collection<? extends MappingOperation>)newValue);
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
			case QvtOperationalPackage.MAPPING_OPERATION__WHEN:
				getWhen().clear();
				return;
			case QvtOperationalPackage.MAPPING_OPERATION__WHERE:
				setWhere((OclExpression)null);
				return;
			case QvtOperationalPackage.MAPPING_OPERATION__DISJUNCT:
				getDisjunct().clear();
				return;
			case QvtOperationalPackage.MAPPING_OPERATION__INHERITED:
				getInherited().clear();
				return;
			case QvtOperationalPackage.MAPPING_OPERATION__MERGED:
				getMerged().clear();
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
			case QvtOperationalPackage.MAPPING_OPERATION__WHEN:
				return when != null && !when.isEmpty();
			case QvtOperationalPackage.MAPPING_OPERATION__WHERE:
				return where != null;
			case QvtOperationalPackage.MAPPING_OPERATION__DISJUNCT:
				return disjunct != null && !disjunct.isEmpty();
			case QvtOperationalPackage.MAPPING_OPERATION__INHERITED:
				return inherited != null && !inherited.isEmpty();
			case QvtOperationalPackage.MAPPING_OPERATION__MERGED:
				return merged != null && !merged.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //MappingOperationImpl
