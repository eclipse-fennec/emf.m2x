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
package org.eclipse.fennec.m2m.qvto.engine.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2m.model.trace.EDirectionKind;
import org.eclipse.fennec.m2m.model.trace.EMappingContext;
import org.eclipse.fennec.m2m.model.trace.EMappingOperation;
import org.eclipse.fennec.m2m.model.trace.EMappingParameters;
import org.eclipse.fennec.m2m.model.trace.EMappingResults;
import org.eclipse.fennec.m2m.model.trace.EValue;
import org.eclipse.fennec.m2m.model.trace.Trace;
import org.eclipse.fennec.m2m.model.trace.TraceFactory;
import org.eclipse.fennec.m2m.model.trace.TraceRecord;
import org.eclipse.fennec.m2m.model.trace.VarParameterValue;

/**
 * In-memory trace store for resolve support and EMF trace export.
 *
 * <p>Maintains two parallel data structures:
 * <ul>
 *   <li>A flat list of {@link QvtoTraceRecord} for fast resolve lookups (linear scan)</li>
 *   <li>An EMF {@link Trace} model for export via
 *       {@link org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult}</li>
 * </ul>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoTraceManager {

	private final List<QvtoTraceRecord> records = new ArrayList<>();
	private final Trace trace = TraceFactory.eINSTANCE.createTrace();

	/**
	 * Records a mapping invocation for both resolve lookups and EMF trace export.
	 *
	 * @param mappingOp the mapping operation AST node
	 * @param source the source object
	 * @param result the result object
	 */
	void addRecord(MappingOperation mappingOp, Object source, Object result) {
		String mappingName = mappingOp.getName();
		records.add(new QvtoTraceRecord(mappingName, mappingOp, source, result));

		// Build parallel EMF TraceRecord for export
		TraceRecord emfRecord = TraceFactory.eINSTANCE.createTraceRecord();

		// EMappingOperation
		EMappingOperation emfMappingOp = TraceFactory.eINSTANCE.createEMappingOperation();
		emfMappingOp.setName(mappingName);
		emfMappingOp.setRuntimeMappingOperation(mappingOp);
		EObject container = mappingOp.eContainer();
		if (container instanceof EClass ec) {
			emfMappingOp.setModule(ec.getName());
			EPackage pkg = ec.getEPackage();
			emfMappingOp.setPackage(pkg != null ? pkg.getName() : "");
		} else if (container instanceof EPackage ep) {
			emfMappingOp.setModule(ep.getName());
			emfMappingOp.setPackage(ep.getName());
		} else {
			emfMappingOp.setModule("");
			emfMappingOp.setPackage("");
		}
		emfRecord.setMappingOperation(emfMappingOp);

		// EMappingContext (self)
		EMappingContext emfContext = TraceFactory.eINSTANCE.createEMappingContext();
		if (source != null) {
			VarParameterValue selfParam = createVarParameterValue(
					EDirectionKind.IN, "self", source);
			emfContext.setContext(selfParam);
		}
		emfRecord.setContext(emfContext);

		// EMappingParameters (empty — parameter trace not yet captured)
		EMappingParameters emfParams = TraceFactory.eINSTANCE.createEMappingParameters();
		emfRecord.setParameters(emfParams);

		// EMappingResults
		EMappingResults emfResults = TraceFactory.eINSTANCE.createEMappingResults();
		if (result != null) {
			VarParameterValue resultParam = createVarParameterValue(
					EDirectionKind.OUT, "result", result);
			emfResults.getResult().add(resultParam);
		}
		emfRecord.setResult(emfResults);

		trace.getTraceRecords().add(emfRecord);
	}

	/**
	 * Returns the EMF trace model for export.
	 */
	Trace getTrace() {
		return trace;
	}

	/**
	 * Forward resolve: finds all result EObjects created from the given source.
	 */
	List<EObject> resolve(Object source, EClass targetType) {
		List<EObject> results = new ArrayList<>();
		for (QvtoTraceRecord record : records) {
			if (record.source() == source && record.result() instanceof EObject eo) {
				if (targetType == null || targetType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}

	/**
	 * Inverse resolve: finds all source EObjects that produced the given result.
	 */
	List<EObject> invResolve(Object result, EClass sourceType) {
		List<EObject> results = new ArrayList<>();
		for (QvtoTraceRecord record : records) {
			if (record.result() == result && record.source() instanceof EObject eo) {
				if (sourceType == null || sourceType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}

	/**
	 * Forward resolve constrained to a specific mapping.
	 */
	List<EObject> resolveIn(String mappingName, Object source, EClass targetType) {
		List<EObject> results = new ArrayList<>();
		for (QvtoTraceRecord record : records) {
			if (record.mappingName().equals(mappingName)
					&& record.source() == source
					&& record.result() instanceof EObject eo) {
				if (targetType == null || targetType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}

	private static VarParameterValue createVarParameterValue(
			EDirectionKind kind, String name, Object value) {
		VarParameterValue param = TraceFactory.eINSTANCE.createVarParameterValue();
		param.setKind(kind);
		param.setName(name);
		param.setType(value instanceof EObject eo
				? eo.eClass().getName() : value.getClass().getSimpleName());
		EValue eValue = TraceFactory.eINSTANCE.createEValue();
		if (value instanceof EObject eo) {
			eValue.setModelElement(eo);
		} else {
			eValue.setOclObject(value);
		}
		param.setValue(eValue);
		return param;
	}
}
