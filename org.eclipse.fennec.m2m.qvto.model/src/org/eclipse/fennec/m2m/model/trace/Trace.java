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
package org.eclipse.fennec.m2m.model.trace;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;

import org.eclipse.emf.ecore.EObject;

import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Trace</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * Root container for all trace records of a transformation execution.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.Trace#getTraceRecords <em>Trace Records</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.Trace#getTraceRecordMap <em>Trace Record Map</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.Trace#getSourceToTraceRecordMap <em>Source To Trace Record Map</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.model.trace.Trace#getTargetToTraceRecordMap <em>Target To Trace Record Map</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTrace()
 * @model
 * @generated
 */
@ProviderType
public interface Trace extends EObject {
	/**
	 * Returns the value of the '<em><b>Trace Records</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2m.model.trace.TraceRecord}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trace Records</em>' containment reference list.
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTrace_TraceRecords()
	 * @model containment="true"
	 * @generated
	 */
	EList<TraceRecord> getTraceRecords();

	/**
	 * Returns the value of the '<em><b>Trace Record Map</b></em>' map.
	 * The key is of type {@link org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation},
	 * and the value is of type list of {@link org.eclipse.fennec.m2m.model.trace.TraceRecord},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Trace Record Map</em>' map.
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTrace_TraceRecordMap()
	 * @model mapType="org.eclipse.fennec.m2m.model.trace.MappingOperationToTraceRecordMapEntry&lt;org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation, org.eclipse.fennec.m2m.model.trace.TraceRecord&gt;" transient="true"
	 * @generated
	 */
	EMap<MappingOperation, EList<TraceRecord>> getTraceRecordMap();

	/**
	 * Returns the value of the '<em><b>Source To Trace Record Map</b></em>' map.
	 * The key is of type {@link java.lang.Object},
	 * and the value is of type list of {@link org.eclipse.fennec.m2m.model.trace.TraceRecord},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Source To Trace Record Map</em>' map.
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTrace_SourceToTraceRecordMap()
	 * @model mapType="org.eclipse.fennec.m2m.model.trace.ObjectToTraceRecordMapEntry&lt;org.eclipse.emf.ecore.EJavaObject, org.eclipse.fennec.m2m.model.trace.TraceRecord&gt;" transient="true"
	 * @generated
	 */
	EMap<Object, EList<TraceRecord>> getSourceToTraceRecordMap();

	/**
	 * Returns the value of the '<em><b>Target To Trace Record Map</b></em>' map.
	 * The key is of type {@link java.lang.Object},
	 * and the value is of type list of {@link org.eclipse.fennec.m2m.model.trace.TraceRecord},
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Target To Trace Record Map</em>' map.
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#getTrace_TargetToTraceRecordMap()
	 * @model mapType="org.eclipse.fennec.m2m.model.trace.ObjectToTraceRecordMapEntry&lt;org.eclipse.emf.ecore.EJavaObject, org.eclipse.fennec.m2m.model.trace.TraceRecord&gt;" transient="true"
	 * @generated
	 */
	EMap<Object, EList<TraceRecord>> getTargetToTraceRecordMap();

} // Trace
