/**
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

import java.util.Map;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;

import org.eclipse.fennec.m2m.model.trace.*;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TraceFactoryImpl extends EFactoryImpl implements TraceFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static TraceFactory init() {
		try {
			TraceFactory theTraceFactory = (TraceFactory)EPackage.Registry.INSTANCE.getEFactory(TracePackage.eNS_URI);
			if (theTraceFactory != null) {
				return theTraceFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new TraceFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public TraceFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case TracePackage.TRACE: return createTrace();
			case TracePackage.TRACE_RECORD: return createTraceRecord();
			case TracePackage.EMAPPING_OPERATION: return createEMappingOperation();
			case TracePackage.EMAPPING_CONTEXT: return createEMappingContext();
			case TracePackage.EMAPPING_PARAMETERS: return createEMappingParameters();
			case TracePackage.EMAPPING_RESULTS: return createEMappingResults();
			case TracePackage.VAR_PARAMETER_VALUE: return createVarParameterValue();
			case TracePackage.EVALUE: return createEValue();
			case TracePackage.ETUPLE_PART_VALUE: return createETuplePartValue();
			case TracePackage.MAPPING_OPERATION_TO_TRACE_RECORD_MAP_ENTRY: return (EObject)createMappingOperationToTraceRecordMapEntry();
			case TracePackage.OBJECT_TO_TRACE_RECORD_MAP_ENTRY: return (EObject)createObjectToTraceRecordMapEntry();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case TracePackage.EDIRECTION_KIND:
				return createEDirectionKindFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case TracePackage.EDIRECTION_KIND:
				return convertEDirectionKindToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Trace createTrace() {
		TraceImpl trace = new TraceImpl();
		return trace;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TraceRecord createTraceRecord() {
		TraceRecordImpl traceRecord = new TraceRecordImpl();
		return traceRecord;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMappingOperation createEMappingOperation() {
		EMappingOperationImpl eMappingOperation = new EMappingOperationImpl();
		return eMappingOperation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMappingContext createEMappingContext() {
		EMappingContextImpl eMappingContext = new EMappingContextImpl();
		return eMappingContext;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMappingParameters createEMappingParameters() {
		EMappingParametersImpl eMappingParameters = new EMappingParametersImpl();
		return eMappingParameters;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EMappingResults createEMappingResults() {
		EMappingResultsImpl eMappingResults = new EMappingResultsImpl();
		return eMappingResults;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VarParameterValue createVarParameterValue() {
		VarParameterValueImpl varParameterValue = new VarParameterValueImpl();
		return varParameterValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EValue createEValue() {
		EValueImpl eValue = new EValueImpl();
		return eValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ETuplePartValue createETuplePartValue() {
		ETuplePartValueImpl eTuplePartValue = new ETuplePartValueImpl();
		return eTuplePartValue;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<MappingOperation, EList<TraceRecord>> createMappingOperationToTraceRecordMapEntry() {
		MappingOperationToTraceRecordMapEntryImpl mappingOperationToTraceRecordMapEntry = new MappingOperationToTraceRecordMapEntryImpl();
		return mappingOperationToTraceRecordMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public Map.Entry<Object, EList<TraceRecord>> createObjectToTraceRecordMapEntry() {
		ObjectToTraceRecordMapEntryImpl objectToTraceRecordMapEntry = new ObjectToTraceRecordMapEntryImpl();
		return objectToTraceRecordMapEntry;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public EDirectionKind createEDirectionKindFromString(EDataType eDataType, String initialValue) {
		EDirectionKind result = EDirectionKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertEDirectionKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TracePackage getTracePackage() {
		return (TracePackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static TracePackage getPackage() {
		return TracePackage.eINSTANCE;
	}

} //TraceFactoryImpl
