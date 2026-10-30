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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.VarParameter;
import org.eclipse.fennec.m2x.model.trace.EDirectionKind;
import org.eclipse.fennec.m2x.model.trace.EMappingContext;
import org.eclipse.fennec.m2x.model.trace.EMappingOperation;
import org.eclipse.fennec.m2x.model.trace.EMappingParameters;
import org.eclipse.fennec.m2x.model.trace.EMappingResults;
import org.eclipse.fennec.m2x.model.trace.EValue;
import org.eclipse.fennec.m2x.model.trace.Trace;
import org.eclipse.fennec.m2x.model.trace.TraceFactory;
import org.eclipse.fennec.m2x.model.trace.TraceRecord;
import org.eclipse.fennec.m2x.model.trace.VarParameterValue;

/**
 * In-memory trace store for resolve support and EMF trace export.
 *
 * <p>Maintains indexed data structures for O(1) trace lookups:
 * <ul>
 *   <li>A flat list of {@link QvtoTraceRecord} for ordered iteration</li>
 *   <li>Index maps for cached-result, source→records, and result→records lookups</li>
 *   <li>An EMF {@link Trace} model for export via
 *       {@link org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult}</li>
 * </ul>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoTraceManager {

	private final List<QvtoTraceRecord> records = new ArrayList<>();
	private final Trace trace = TraceFactory.eINSTANCE.createTrace();
	private final int maxTraceRecords;
	private boolean limitReached;

	// Index 1: Cached-result lookup — (MappingOperation, compositeKey) → QvtoTraceRecord
	private final Map<MappingOperation, Map<Object, QvtoTraceRecord>> mappingIndex = new HashMap<>();

	// Index 2: Source → Records (for resolve/resolveIn)
	private final Map<Object, List<QvtoTraceRecord>> sourceIndex = new IdentityHashMap<>();

	// Index 3: Result → Records (for invResolve/invResolveIn)
	private final Map<Object, List<QvtoTraceRecord>> resultIndex = new IdentityHashMap<>();
	// Index 4: Invoked (disjuncting) mapping → the records of the candidates that answered it
	private final Map<MappingOperation, List<QvtoTraceRecord>> invokedIndex = new HashMap<>();

	/**
	 * Creates a new trace manager with the given record limit.
	 *
	 * @param maxTraceRecords maximum number of trace records, or 0 for unlimited
	 */
	QvtoTraceManager(int maxTraceRecords) {
		this.maxTraceRecords = maxTraceRecords;
	}

	/**
	 * Returns {@code true} if the trace record limit has been reached.
	 */
	boolean isLimitReached() {
		return limitReached;
	}

	/**
	 * Records a mapping invocation for both resolve lookups and EMF trace export.
	 * If {@code maxTraceRecords} is set and reached, further records are dropped (Q-6 / M-4c)
	 * and {@link #isLimitReached()} turns true, which the evaluator reports as an error (#284).
	 *
	 * @param mappingOp the mapping operation AST node
	 * @param source the source object
	 * @param args the in/inout parameter values (may be empty)
	 * @param result the result object
	 */
	void addRecord(MappingOperation mappingOp, Object source, Object[] args, Object result) {
		if (maxTraceRecords > 0 && records.size() >= maxTraceRecords) {
			limitReached = true;
			return;
		}
		String mappingName = mappingOp.getName();
		Object[] clonedArgs = args.clone();
		QvtoTraceRecord record = new QvtoTraceRecord(mappingName, mappingOp, source, clonedArgs, result);
		records.add(record);

		// Update mapping index (for lookupCachedResult)
		Object compositeKey = buildCompositeKey(source, clonedArgs);
		mappingIndex.computeIfAbsent(mappingOp, k -> new HashMap<>()).put(compositeKey, record);

		// Update source index (for resolve/resolveIn). A mapping without context is resolved from
		// its first object argument, as Eclipse does (#334)
		Object resolveSource = source != null ? source : firstObject(clonedArgs);
		if (resolveSource != null) {
			sourceIndex.computeIfAbsent(resolveSource, k -> new ArrayList<>()).add(record);
		}

		// Update result index (for invResolve/invResolveIn) — every result object (#334)
		for (EObject resultObject : resultObjects(result)) {
			resultIndex.computeIfAbsent(resultObject, k -> new ArrayList<>()).add(record);
		}

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

		// EMappingParameters — capture in/inout parameters (§8.1.11.1: inout traced as in)
		EMappingParameters emfParams = TraceFactory.eINSTANCE.createEMappingParameters();
		List<?> formalParams = mappingOp.getEParameters();
		var resultParams = mappingOp.getResult();
		int argIdx = 0;
		for (int i = 0; i < formalParams.size(); i++) {
			Object fp = formalParams.get(i);
			if (fp instanceof VarParameter vp && !resultParams.contains(vp)) {
				Object argValue = argIdx < args.length ? args[argIdx++] : null;
				if (argValue != null) {
					VarParameterValue paramVal = createVarParameterValue(
							EDirectionKind.IN, vp.getName(), argValue);
					emfParams.getParameters().add(paramVal);
				}
			}
		}
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
	 * Checks if a mapping has already been executed for the given source and args.
	 * §8.1.11.2: Match on executed-mapping + context-parameter + in-parameters.
	 * Class instances by identity, DataType values by deep equality.
	 *
	 * @param mappingOp the mapping operation
	 * @param source the source object (identity-based check)
	 * @param args the in/inout parameter values
	 * @return the cached result, or {@code null} if no cache hit
	 */
	Object lookupCachedResult(MappingOperation mappingOp, Object source, Object[] args) {
		QvtoTraceRecord record = lookupRecord(mappingOp, source, args);
		return record != null ? record.result() : null;
	}

	/**
	 * The trace record of an earlier execution of the mapping with the same context and in
	 * parameters, or {@code null}. A hit means the relation holds — also for a mapping without
	 * a result, whose execution is then suppressed all the same (§8.1.11.2, #334).
	 */
	QvtoTraceRecord lookupRecord(MappingOperation mappingOp, Object source, Object[] args) {
		Map<Object, QvtoTraceRecord> byKey = mappingIndex.get(mappingOp);
		return byKey == null ? null : byKey.get(buildCompositeKey(source, args));
	}

	/**
	 * Notes that a disjuncting mapping was answered by one of its candidates: the record
	 * becomes visible to {@code resolveIn} of the invoked mapping too (§8.1.11.4, #334).
	 */
	void markInvoked(MappingOperation invokedMapping, QvtoTraceRecord record) {
		if (record != null) {
			invokedIndex.computeIfAbsent(invokedMapping, k -> new ArrayList<>()).add(record);
		}
	}

	/** Whether the record was produced by, or answered a call of, the given mapping (§8.1.11.4). */
	private boolean inMapping(QvtoTraceRecord record, MappingOperation mapping) {
		if (sameMapping(record.mappingOp(), mapping)) {
			return true;
		}
		for (Map.Entry<MappingOperation, List<QvtoTraceRecord>> invoked : invokedIndex.entrySet()) {
			if (sameMapping(invoked.getKey(), mapping) && invoked.getValue().contains(record)) {
				return true;
			}
		}
		return false;
	}

	/**
	 * Whether two references denote the same mapping operation: the same object, or — for a
	 * reference the parser resolved to a node of its own — the same name and context type, so
	 * that SourceElement::toT is not SourceContainer::toT.
	 */
	private static boolean sameMapping(MappingOperation a, MappingOperation b) {
		if (a == b) {
			return true;
		}
		if (a == null || b == null || !a.getName().equals(b.getName())) {
			return false;
		}
		Object contextA = a.getContext() != null ? a.getContext().getEType() : null;
		Object contextB = b.getContext() != null ? b.getContext().getEType() : null;
		if (contextA instanceof EClass classA && contextB instanceof EClass classB) {
			return classA == classB || classA.getName().equals(classB.getName());
		}
		return contextA == null || contextB == null || contextA == contextB;
	}

	/**
	 * The objects a mapping produced: its single result, the values of a tuple of several
	 * results, or the elements of a collection result (§8.1.11.3: out-parameters union
	 * result-parameters, #334).
	 */
	static List<EObject> resultObjects(Object result) {
		List<EObject> objects = new ArrayList<>();
		if (result instanceof EObject eo) {
			objects.add(eo);
		} else if (result instanceof Map<?, ?> tuple) {
			for (Object value : tuple.values()) {
				objects.addAll(resultObjects(value));
			}
		} else if (result instanceof Collection<?> values) {
			for (Object value : values) {
				if (value instanceof EObject eo) {
					objects.add(eo);
				}
			}
		}
		return objects;
	}

	private static Object firstObject(Object[] args) {
		for (Object arg : args) {
			if (arg instanceof EObject) {
				return arg;
			}
		}
		return null;
	}

	/**
	 * Forward resolve: finds all result EObjects created from the given source.
	 * §8.1.11.3: When source is null, ALL trace records are searched.
	 */
	List<EObject> resolve(Object source, EClass targetType) {
		List<EObject> results = new ArrayList<>();
		List<QvtoTraceRecord> candidates = source != null
				? sourceIndex.getOrDefault(source, List.of())
				: records;
		for (QvtoTraceRecord record : candidates) {
			for (EObject eo : resultObjects(record.result())) {
				if (targetType == null || targetType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}

	/**
	 * Inverse resolve: finds all source EObjects that produced the given result.
	 * When result is null, ALL trace records are searched.
	 */
	List<EObject> invResolve(Object result, EClass sourceType) {
		List<EObject> results = new ArrayList<>();
		List<QvtoTraceRecord> candidates = result != null
				? resultIndex.getOrDefault(result, List.of())
				: records;
		for (QvtoTraceRecord record : candidates) {
			if (record.source() instanceof EObject eo) {
				if (sourceType == null || sourceType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}

	/**
	 * Forward resolve constrained to a specific mapping.
	 * When source is null, ALL trace records for the mapping are searched.
	 */
	List<EObject> resolveIn(MappingOperation mapping, Object source, EClass targetType) {
		List<EObject> results = new ArrayList<>();
		List<QvtoTraceRecord> candidates = source != null
				? sourceIndex.getOrDefault(source, List.of())
				: records;
		for (QvtoTraceRecord record : candidates) {
			// §8.2.1.23: "by a unique mapping operation" — the operation, not its name (#334)
			if (!inMapping(record, mapping)) {
				continue;
			}
			for (EObject eo : resultObjects(record.result())) {
				if (targetType == null || targetType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}

	/**
	 * Inverse resolve constrained to a specific mapping.
	 * §8.1.11.5: invresolveIn searches source objects from trace records
	 * where invoked-mapping or executed-mapping matches the given mapping.
	 * When result is null, ALL trace records for the mapping are searched.
	 */
	List<EObject> invResolveIn(MappingOperation mapping, Object result, EClass sourceType) {
		List<EObject> results = new ArrayList<>();
		List<QvtoTraceRecord> candidates = result != null
				? resultIndex.getOrDefault(result, List.of())
				: records;
		for (QvtoTraceRecord record : candidates) {
			if (!inMapping(record, mapping)) {
				continue;
			}
			if (record.source() instanceof EObject eo) {
				if (sourceType == null || sourceType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}

	/**
	 * Builds a composite key for the mapping index.
	 * Uses identity for EObjects and equality for DataType values (§8.1.11.2).
	 */
	private static Object buildCompositeKey(Object source, Object[] args) {
		if (args.length == 0) {
			return source;
		}
		// Use List for composite key — List.hashCode/equals uses element equality
		// For EObject identity we wrap in IdentityKey
		List<Object> key = new ArrayList<>(1 + args.length);
		key.add(wrapKey(source));
		for (Object arg : args) {
			key.add(wrapKey(arg));
		}
		return key;
	}

	private static Object wrapKey(Object value) {
		if (value instanceof EObject) {
			return new IdentityKey(value);
		}
		return value;
	}

	/**
	 * Wrapper that uses identity (==) for hashCode/equals, used for EObject keys.
	 */
	private record IdentityKey(Object value) {
		@Override
		public int hashCode() {
			return System.identityHashCode(value);
		}

		@Override
		public boolean equals(Object obj) {
			return obj instanceof IdentityKey other && this.value == other.value;
		}
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
