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
package org.eclipse.fennec.m2x.model.trace.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EcoreEMap;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;

import org.eclipse.fennec.m2x.model.trace.Trace;
import org.eclipse.fennec.m2x.model.trace.TracePackage;
import org.eclipse.fennec.m2x.model.trace.TraceRecord;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Trace</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.impl.TraceImpl#getTraceRecords <em>Trace Records</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.impl.TraceImpl#getTraceRecordMap <em>Trace Record Map</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.impl.TraceImpl#getSourceToTraceRecordMap <em>Source To Trace Record Map</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.trace.impl.TraceImpl#getTargetToTraceRecordMap <em>Target To Trace Record Map</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TraceImpl extends MinimalEObjectImpl.Container implements Trace {
	/**
	 * The cached value of the '{@link #getTraceRecords() <em>Trace Records</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceRecords()
	 * @generated
	 * @ordered
	 */
	protected EList<TraceRecord> traceRecords;

	/**
	 * The cached value of the '{@link #getTraceRecordMap() <em>Trace Record Map</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTraceRecordMap()
	 * @generated
	 * @ordered
	 */
	protected EMap<MappingOperation, EList<TraceRecord>> traceRecordMap;

	/**
	 * The cached value of the '{@link #getSourceToTraceRecordMap() <em>Source To Trace Record Map</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSourceToTraceRecordMap()
	 * @generated
	 * @ordered
	 */
	protected EMap<Object, EList<TraceRecord>> sourceToTraceRecordMap;

	/**
	 * The cached value of the '{@link #getTargetToTraceRecordMap() <em>Target To Trace Record Map</em>}' map.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getTargetToTraceRecordMap()
	 * @generated
	 * @ordered
	 */
	protected EMap<Object, EList<TraceRecord>> targetToTraceRecordMap;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TraceImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return TracePackage.Literals.TRACE;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<TraceRecord> getTraceRecords() {
		if (traceRecords == null) {
			traceRecords = new EObjectContainmentEList<TraceRecord>(TraceRecord.class, this, TracePackage.TRACE__TRACE_RECORDS);
		}
		return traceRecords;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<MappingOperation, EList<TraceRecord>> getTraceRecordMap() {
		if (traceRecordMap == null) {
			traceRecordMap = new EcoreEMap<MappingOperation,EList<TraceRecord>>(TracePackage.Literals.MAPPING_OPERATION_TO_TRACE_RECORD_MAP_ENTRY, MappingOperationToTraceRecordMapEntryImpl.class, this, TracePackage.TRACE__TRACE_RECORD_MAP);
		}
		return traceRecordMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<Object, EList<TraceRecord>> getSourceToTraceRecordMap() {
		if (sourceToTraceRecordMap == null) {
			sourceToTraceRecordMap = new EcoreEMap<Object,EList<TraceRecord>>(TracePackage.Literals.OBJECT_TO_TRACE_RECORD_MAP_ENTRY, ObjectToTraceRecordMapEntryImpl.class, this, TracePackage.TRACE__SOURCE_TO_TRACE_RECORD_MAP);
		}
		return sourceToTraceRecordMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMap<Object, EList<TraceRecord>> getTargetToTraceRecordMap() {
		if (targetToTraceRecordMap == null) {
			targetToTraceRecordMap = new EcoreEMap<Object,EList<TraceRecord>>(TracePackage.Literals.OBJECT_TO_TRACE_RECORD_MAP_ENTRY, ObjectToTraceRecordMapEntryImpl.class, this, TracePackage.TRACE__TARGET_TO_TRACE_RECORD_MAP);
		}
		return targetToTraceRecordMap;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case TracePackage.TRACE__TRACE_RECORDS:
				return ((InternalEList<?>)getTraceRecords()).basicRemove(otherEnd, msgs);
			case TracePackage.TRACE__TRACE_RECORD_MAP:
				return ((InternalEList<?>)getTraceRecordMap()).basicRemove(otherEnd, msgs);
			case TracePackage.TRACE__SOURCE_TO_TRACE_RECORD_MAP:
				return ((InternalEList<?>)getSourceToTraceRecordMap()).basicRemove(otherEnd, msgs);
			case TracePackage.TRACE__TARGET_TO_TRACE_RECORD_MAP:
				return ((InternalEList<?>)getTargetToTraceRecordMap()).basicRemove(otherEnd, msgs);
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
			case TracePackage.TRACE__TRACE_RECORDS:
				return getTraceRecords();
			case TracePackage.TRACE__TRACE_RECORD_MAP:
				if (coreType) return getTraceRecordMap();
				else return getTraceRecordMap().map();
			case TracePackage.TRACE__SOURCE_TO_TRACE_RECORD_MAP:
				if (coreType) return getSourceToTraceRecordMap();
				else return getSourceToTraceRecordMap().map();
			case TracePackage.TRACE__TARGET_TO_TRACE_RECORD_MAP:
				if (coreType) return getTargetToTraceRecordMap();
				else return getTargetToTraceRecordMap().map();
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
			case TracePackage.TRACE__TRACE_RECORDS:
				getTraceRecords().clear();
				getTraceRecords().addAll((Collection<? extends TraceRecord>)newValue);
				return;
			case TracePackage.TRACE__TRACE_RECORD_MAP:
				((EStructuralFeature.Setting)getTraceRecordMap()).set(newValue);
				return;
			case TracePackage.TRACE__SOURCE_TO_TRACE_RECORD_MAP:
				((EStructuralFeature.Setting)getSourceToTraceRecordMap()).set(newValue);
				return;
			case TracePackage.TRACE__TARGET_TO_TRACE_RECORD_MAP:
				((EStructuralFeature.Setting)getTargetToTraceRecordMap()).set(newValue);
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
			case TracePackage.TRACE__TRACE_RECORDS:
				getTraceRecords().clear();
				return;
			case TracePackage.TRACE__TRACE_RECORD_MAP:
				getTraceRecordMap().clear();
				return;
			case TracePackage.TRACE__SOURCE_TO_TRACE_RECORD_MAP:
				getSourceToTraceRecordMap().clear();
				return;
			case TracePackage.TRACE__TARGET_TO_TRACE_RECORD_MAP:
				getTargetToTraceRecordMap().clear();
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
			case TracePackage.TRACE__TRACE_RECORDS:
				return traceRecords != null && !traceRecords.isEmpty();
			case TracePackage.TRACE__TRACE_RECORD_MAP:
				return traceRecordMap != null && !traceRecordMap.isEmpty();
			case TracePackage.TRACE__SOURCE_TO_TRACE_RECORD_MAP:
				return sourceToTraceRecordMap != null && !sourceToTraceRecordMap.isEmpty();
			case TracePackage.TRACE__TARGET_TO_TRACE_RECORD_MAP:
				return targetToTraceRecordMap != null && !targetToTraceRecordMap.isEmpty();
		}
		return super.eIsSet(featureID);
	}

} //TraceImpl
