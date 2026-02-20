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

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage;

import org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl;

import org.eclipse.fennec.m2m.model.ocl.OclPackage;

import org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage;

import org.eclipse.fennec.m2m.model.qvtoperational.impl.QvtOperationalPackageImpl;

import org.eclipse.fennec.m2m.model.trace.EDirectionKind;
import org.eclipse.fennec.m2m.model.trace.EMappingContext;
import org.eclipse.fennec.m2m.model.trace.EMappingOperation;
import org.eclipse.fennec.m2m.model.trace.EMappingParameters;
import org.eclipse.fennec.m2m.model.trace.EMappingResults;
import org.eclipse.fennec.m2m.model.trace.ETuplePartValue;
import org.eclipse.fennec.m2m.model.trace.EValue;
import org.eclipse.fennec.m2m.model.trace.Trace;
import org.eclipse.fennec.m2m.model.trace.TraceFactory;
import org.eclipse.fennec.m2m.model.trace.TracePackage;
import org.eclipse.fennec.m2m.model.trace.TraceRecord;
import org.eclipse.fennec.m2m.model.trace.VarParameterValue;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class TracePackageImpl extends EPackageImpl implements TracePackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass traceEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass traceRecordEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eMappingOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eMappingContextEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eMappingParametersEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eMappingResultsEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass varParameterValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass eTuplePartValueEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mappingOperationToTraceRecordMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass objectToTraceRecordMapEntryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum eDirectionKindEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.fennec.m2m.model.trace.TracePackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private TracePackageImpl() {
		super(eNS_URI, TraceFactory.eINSTANCE);
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link TracePackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static TracePackage init() {
		if (isInited) return (TracePackage)EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);

		// Obtain or create and register package
		Object registeredTracePackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		TracePackageImpl theTracePackage = registeredTracePackage instanceof TracePackageImpl ? (TracePackageImpl)registeredTracePackage : new TracePackageImpl();

		isInited = true;

		// Initialize simple dependencies
		OclPackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ImperativeOclPackage.eNS_URI);
		ImperativeOclPackageImpl theImperativeOclPackage = (ImperativeOclPackageImpl)(registeredPackage instanceof ImperativeOclPackageImpl ? registeredPackage : ImperativeOclPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(QvtOperationalPackage.eNS_URI);
		QvtOperationalPackageImpl theQvtOperationalPackage = (QvtOperationalPackageImpl)(registeredPackage instanceof QvtOperationalPackageImpl ? registeredPackage : QvtOperationalPackage.eINSTANCE);

		// Create package meta-data objects
		theTracePackage.createPackageContents();
		theImperativeOclPackage.createPackageContents();
		theQvtOperationalPackage.createPackageContents();

		// Initialize created meta-data
		theTracePackage.initializePackageContents();
		theImperativeOclPackage.initializePackageContents();
		theQvtOperationalPackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theTracePackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(TracePackage.eNS_URI, theTracePackage);
		return theTracePackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTrace() {
		return traceEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTrace_TraceRecords() {
		return (EReference)traceEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTrace_TraceRecordMap() {
		return (EReference)traceEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTrace_SourceToTraceRecordMap() {
		return (EReference)traceEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTrace_TargetToTraceRecordMap() {
		return (EReference)traceEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getTraceRecord() {
		return traceRecordEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTraceRecord_MappingOperation() {
		return (EReference)traceRecordEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTraceRecord_Context() {
		return (EReference)traceRecordEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTraceRecord_Parameters() {
		return (EReference)traceRecordEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getTraceRecord_Result() {
		return (EReference)traceRecordEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEMappingOperation() {
		return eMappingOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEMappingOperation_Name() {
		return (EAttribute)eMappingOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEMappingOperation_Package() {
		return (EAttribute)eMappingOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEMappingOperation_Module() {
		return (EAttribute)eMappingOperationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEMappingOperation_RuntimeMappingOperation() {
		return (EReference)eMappingOperationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEMappingContext() {
		return eMappingContextEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEMappingContext_Context() {
		return (EReference)eMappingContextEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEMappingParameters() {
		return eMappingParametersEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEMappingParameters_Parameters() {
		return (EReference)eMappingParametersEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEMappingResults() {
		return eMappingResultsEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEMappingResults_Result() {
		return (EReference)eMappingResultsEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVarParameterValue() {
		return varParameterValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVarParameterValue_Kind() {
		return (EAttribute)varParameterValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVarParameterValue_Name() {
		return (EAttribute)varParameterValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVarParameterValue_Type() {
		return (EAttribute)varParameterValueEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVarParameterValue_Value() {
		return (EReference)varParameterValueEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEValue() {
		return eValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEValue_PrimitiveValue() {
		return (EAttribute)eValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEValue_OclObject() {
		return (EAttribute)eValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getEValue_CollectionType() {
		return (EAttribute)eValueEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEValue_IntermediateElement() {
		return (EReference)eValueEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEValue_Collection() {
		return (EReference)eValueEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getEValue_ModelElement() {
		return (EReference)eValueEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getETuplePartValue() {
		return eTuplePartValueEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getETuplePartValue_Name() {
		return (EAttribute)eTuplePartValueEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getETuplePartValue_Value() {
		return (EReference)eTuplePartValueEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMappingOperationToTraceRecordMapEntry() {
		return mappingOperationToTraceRecordMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingOperationToTraceRecordMapEntry_Key() {
		return (EReference)mappingOperationToTraceRecordMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingOperationToTraceRecordMapEntry_Value() {
		return (EReference)mappingOperationToTraceRecordMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getObjectToTraceRecordMapEntry() {
		return objectToTraceRecordMapEntryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getObjectToTraceRecordMapEntry_Key() {
		return (EAttribute)objectToTraceRecordMapEntryEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getObjectToTraceRecordMapEntry_Value() {
		return (EReference)objectToTraceRecordMapEntryEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getEDirectionKind() {
		return eDirectionKindEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TraceFactory getTraceFactory() {
		return (TraceFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		traceEClass = createEClass(TRACE);
		createEReference(traceEClass, TRACE__TRACE_RECORDS);
		createEReference(traceEClass, TRACE__TRACE_RECORD_MAP);
		createEReference(traceEClass, TRACE__SOURCE_TO_TRACE_RECORD_MAP);
		createEReference(traceEClass, TRACE__TARGET_TO_TRACE_RECORD_MAP);

		traceRecordEClass = createEClass(TRACE_RECORD);
		createEReference(traceRecordEClass, TRACE_RECORD__MAPPING_OPERATION);
		createEReference(traceRecordEClass, TRACE_RECORD__CONTEXT);
		createEReference(traceRecordEClass, TRACE_RECORD__PARAMETERS);
		createEReference(traceRecordEClass, TRACE_RECORD__RESULT);

		eMappingOperationEClass = createEClass(EMAPPING_OPERATION);
		createEAttribute(eMappingOperationEClass, EMAPPING_OPERATION__NAME);
		createEAttribute(eMappingOperationEClass, EMAPPING_OPERATION__PACKAGE);
		createEAttribute(eMappingOperationEClass, EMAPPING_OPERATION__MODULE);
		createEReference(eMappingOperationEClass, EMAPPING_OPERATION__RUNTIME_MAPPING_OPERATION);

		eMappingContextEClass = createEClass(EMAPPING_CONTEXT);
		createEReference(eMappingContextEClass, EMAPPING_CONTEXT__CONTEXT);

		eMappingParametersEClass = createEClass(EMAPPING_PARAMETERS);
		createEReference(eMappingParametersEClass, EMAPPING_PARAMETERS__PARAMETERS);

		eMappingResultsEClass = createEClass(EMAPPING_RESULTS);
		createEReference(eMappingResultsEClass, EMAPPING_RESULTS__RESULT);

		varParameterValueEClass = createEClass(VAR_PARAMETER_VALUE);
		createEAttribute(varParameterValueEClass, VAR_PARAMETER_VALUE__KIND);
		createEAttribute(varParameterValueEClass, VAR_PARAMETER_VALUE__NAME);
		createEAttribute(varParameterValueEClass, VAR_PARAMETER_VALUE__TYPE);
		createEReference(varParameterValueEClass, VAR_PARAMETER_VALUE__VALUE);

		eValueEClass = createEClass(EVALUE);
		createEAttribute(eValueEClass, EVALUE__PRIMITIVE_VALUE);
		createEAttribute(eValueEClass, EVALUE__OCL_OBJECT);
		createEAttribute(eValueEClass, EVALUE__COLLECTION_TYPE);
		createEReference(eValueEClass, EVALUE__INTERMEDIATE_ELEMENT);
		createEReference(eValueEClass, EVALUE__COLLECTION);
		createEReference(eValueEClass, EVALUE__MODEL_ELEMENT);

		eTuplePartValueEClass = createEClass(ETUPLE_PART_VALUE);
		createEAttribute(eTuplePartValueEClass, ETUPLE_PART_VALUE__NAME);
		createEReference(eTuplePartValueEClass, ETUPLE_PART_VALUE__VALUE);

		mappingOperationToTraceRecordMapEntryEClass = createEClass(MAPPING_OPERATION_TO_TRACE_RECORD_MAP_ENTRY);
		createEReference(mappingOperationToTraceRecordMapEntryEClass, MAPPING_OPERATION_TO_TRACE_RECORD_MAP_ENTRY__KEY);
		createEReference(mappingOperationToTraceRecordMapEntryEClass, MAPPING_OPERATION_TO_TRACE_RECORD_MAP_ENTRY__VALUE);

		objectToTraceRecordMapEntryEClass = createEClass(OBJECT_TO_TRACE_RECORD_MAP_ENTRY);
		createEAttribute(objectToTraceRecordMapEntryEClass, OBJECT_TO_TRACE_RECORD_MAP_ENTRY__KEY);
		createEReference(objectToTraceRecordMapEntryEClass, OBJECT_TO_TRACE_RECORD_MAP_ENTRY__VALUE);

		// Create enums
		eDirectionKindEEnum = createEEnum(EDIRECTION_KIND);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		QvtOperationalPackage theQvtOperationalPackage = (QvtOperationalPackage)EPackage.Registry.INSTANCE.getEPackage(QvtOperationalPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		eTuplePartValueEClass.getESuperTypes().add(this.getEValue());

		// Initialize classes, features, and operations; add parameters
		initEClass(traceEClass, Trace.class, "Trace", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTrace_TraceRecords(), this.getTraceRecord(), null, "traceRecords", null, 0, -1, Trace.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTrace_TraceRecordMap(), this.getMappingOperationToTraceRecordMapEntry(), null, "traceRecordMap", null, 0, -1, Trace.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTrace_SourceToTraceRecordMap(), this.getObjectToTraceRecordMapEntry(), null, "sourceToTraceRecordMap", null, 0, -1, Trace.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTrace_TargetToTraceRecordMap(), this.getObjectToTraceRecordMapEntry(), null, "targetToTraceRecordMap", null, 0, -1, Trace.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(traceRecordEClass, TraceRecord.class, "TraceRecord", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getTraceRecord_MappingOperation(), this.getEMappingOperation(), null, "mappingOperation", null, 1, 1, TraceRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTraceRecord_Context(), this.getEMappingContext(), null, "context", null, 1, 1, TraceRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTraceRecord_Parameters(), this.getEMappingParameters(), null, "parameters", null, 1, 1, TraceRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getTraceRecord_Result(), this.getEMappingResults(), null, "result", null, 1, 1, TraceRecord.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eMappingOperationEClass, EMappingOperation.class, "EMappingOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEMappingOperation_Name(), ecorePackage.getEString(), "name", null, 1, 1, EMappingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMappingOperation_Package(), ecorePackage.getEString(), "package", null, 1, 1, EMappingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEMappingOperation_Module(), ecorePackage.getEString(), "module", null, 1, 1, EMappingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEMappingOperation_RuntimeMappingOperation(), theQvtOperationalPackage.getMappingOperation(), null, "runtimeMappingOperation", null, 1, 1, EMappingOperation.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eMappingContextEClass, EMappingContext.class, "EMappingContext", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEMappingContext_Context(), this.getVarParameterValue(), null, "context", null, 0, 1, EMappingContext.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eMappingParametersEClass, EMappingParameters.class, "EMappingParameters", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEMappingParameters_Parameters(), this.getVarParameterValue(), null, "parameters", null, 0, -1, EMappingParameters.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eMappingResultsEClass, EMappingResults.class, "EMappingResults", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getEMappingResults_Result(), this.getVarParameterValue(), null, "result", null, 0, -1, EMappingResults.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(varParameterValueEClass, VarParameterValue.class, "VarParameterValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVarParameterValue_Kind(), this.getEDirectionKind(), "kind", "IN", 1, 1, VarParameterValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVarParameterValue_Name(), ecorePackage.getEString(), "name", null, 1, 1, VarParameterValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getVarParameterValue_Type(), ecorePackage.getEString(), "type", null, 1, 1, VarParameterValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVarParameterValue_Value(), this.getEValue(), null, "value", null, 1, 1, VarParameterValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eValueEClass, EValue.class, "EValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getEValue_PrimitiveValue(), ecorePackage.getEString(), "primitiveValue", null, 0, 1, EValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEValue_OclObject(), ecorePackage.getEJavaObject(), "oclObject", null, 0, 1, EValue.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getEValue_CollectionType(), ecorePackage.getEString(), "collectionType", null, 0, 1, EValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEValue_IntermediateElement(), ecorePackage.getEObject(), null, "intermediateElement", null, 0, 1, EValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEValue_Collection(), this.getEValue(), null, "collection", null, 0, -1, EValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getEValue_ModelElement(), ecorePackage.getEObject(), null, "modelElement", null, 0, 1, EValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(eTuplePartValueEClass, ETuplePartValue.class, "ETuplePartValue", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getETuplePartValue_Name(), ecorePackage.getEString(), "name", null, 0, 1, ETuplePartValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getETuplePartValue_Value(), this.getEValue(), null, "value", null, 0, 1, ETuplePartValue.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mappingOperationToTraceRecordMapEntryEClass, Map.Entry.class, "MappingOperationToTraceRecordMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMappingOperationToTraceRecordMapEntry_Key(), theQvtOperationalPackage.getMappingOperation(), null, "key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappingOperationToTraceRecordMapEntry_Value(), this.getTraceRecord(), null, "value", null, 0, -1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectToTraceRecordMapEntryEClass, Map.Entry.class, "ObjectToTraceRecordMapEntry", !IS_ABSTRACT, !IS_INTERFACE, !IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getObjectToTraceRecordMapEntry_Key(), ecorePackage.getEJavaObject(), "key", null, 1, 1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getObjectToTraceRecordMapEntry_Value(), this.getTraceRecord(), null, "value", null, 0, -1, Map.Entry.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(eDirectionKindEEnum, EDirectionKind.class, "EDirectionKind");
		addEEnumLiteral(eDirectionKindEEnum, EDirectionKind.IN);
		addEEnumLiteral(eDirectionKindEEnum, EDirectionKind.INOUT);
		addEEnumLiteral(eDirectionKindEEnum, EDirectionKind.OUT);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// Version
		createVersionAnnotations();
	}

	/**
	 * Initializes the annotations for <b>Version</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createVersionAnnotations() {
		String source = "Version";
		addAnnotation
		  (this,
		   source,
		   new String[] {
			   "value", "1.0"
		   });
	}

} //TracePackageImpl
