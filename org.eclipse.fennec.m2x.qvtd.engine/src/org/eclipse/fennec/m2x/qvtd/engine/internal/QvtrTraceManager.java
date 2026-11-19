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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtbase.Domain;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;

/**
 * Manages implicit trace records for QVT-R transformations.
 *
 * <p>Per the QVT v1.3 spec, when a top-level or non-top relation executes
 * successfully, a trace record is created linking the source bindings to the
 * target bindings. These trace records are consulted when a relation is
 * invoked via {@code RelationCallExp} in a when-clause to look up pre-existing
 * bindings, or in a where-clause to verify that a relation has been established.
 *
 * <p>Trace records are keyed by the relation and indexed by the root variable
 * values (arguments) of each domain, matching the order of domains in the relation.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrTraceManager {

	private static final Logger LOG = Logger.getLogger(QvtrTraceManager.class.getName());

	/**
	 * Warn threshold: logs a warning when this many records accumulate per relation.
	 */
	private static final int TRACE_WARN_THRESHOLD = 10_000;

	private final int maxTraceRecords;

	/**
	 * A single trace record: the full variable bindings for a successful relation execution.
	 */
	public record TraceRecord(Relation relation, Map<String, Object> bindings) {
		public TraceRecord {
			Objects.requireNonNull(relation);
			Objects.requireNonNull(bindings);
			// Not Map.copyOf: a [?] root without a match is bound to null, and the relation holds
			// for that binding too (#349)
			bindings = Collections.unmodifiableMap(new HashMap<>(bindings));
		}
	}

	private final Map<String, List<TraceRecord>> tracesByRelation = new HashMap<>();
	/**
	 * Every record once: a relation that runs again for a binding it already holds for adds
	 * nothing, which is how the evaluator sees that a cycle of relations has reached its
	 * fixpoint (#349).
	 */
	private final Set<TraceRecord> known = new HashSet<>();

	/** Records held across all relations — what {@code maxTraceRecords} bounds. */
	private int recorded;

	/**
	 * Creates a trace manager with the given record limit.
	 *
	 * <p>The limit bounds the whole trace, as QVT-O's does (#284). It guards memory against a
	 * transformation that grows its trace with the model, and a bound per relation does not do
	 * that: a transformation with many relations could hold that many times the limit.
	 *
	 * @param maxTraceRecords maximum records across all relations (M-R8)
	 */
	public QvtrTraceManager(int maxTraceRecords) {
		this.maxTraceRecords = maxTraceRecords;
	}

	/**
	 * Records a trace entry for a successful relation execution.
	 *
	 * <p>Past {@code maxTraceRecords} the entry is dropped and {@code false} returned; the caller
	 * reports that (#284). The trace stops growing there, so a when-clause may miss what was
	 * executed afterwards.
	 *
	 * @param relation the relation that was executed
	 * @param bindings the complete variable bindings (source + target)
	 * @return {@code true} if the entry was recorded, {@code false} if the limit dropped it
	 */
	public boolean record(Relation relation, Map<String, Object> bindings) {
		String key = relation.getName();
		List<TraceRecord> traces = tracesByRelation.computeIfAbsent(key, k -> new ArrayList<>());
		TraceRecord record = new TraceRecord(relation, bindings);
		if (known.contains(record)) {
			return true;
		}
		if (recorded >= maxTraceRecords) {
			return false;
		}
		int size = traces.size();
		if (size == TRACE_WARN_THRESHOLD) {
			LOG.log(Level.WARNING,
					"Relation ''{0}'' has accumulated {1} trace records — possible memory issue",
					new Object[]{key, size});
		}
		traces.add(record);
		known.add(record);
		recorded++;
		return true;
	}

	/**
	 * The number of distinct records, which grows only when a relation holds for a binding it
	 * did not hold for before (#349).
	 *
	 * @return the number of records
	 */
	public int size() {
		return recorded;
	}

	/**
	 * Looks up trace records matching a RelationCallExp invocation.
	 *
	 * <p>Per §7.11.3.8, the arguments to a RelationCallExp correspond to the
	 * root variables of the relation's domains, ordered by domain order then
	 * by pattern order within each domain.
	 *
	 * <p>When called from a when-clause, this finds existing trace records
	 * where the provided argument values match the corresponding root variable
	 * bindings. Unbound arguments (null) are treated as wildcards.
	 *
	 * @param relation the relation being called
	 * @param arguments the argument values (may contain nulls for unbound)
	 * @return matching trace records, or empty list if none found
	 */
	public List<TraceRecord> lookup(Relation relation, List<Object> arguments) {
		List<TraceRecord> traces = tracesByRelation.get(relation.getName());
		if (traces == null || traces.isEmpty()) {
			return List.of();
		}

		// Collect root variable names in domain order
		List<String> rootVarNames = collectRootVarNames(relation);

		List<TraceRecord> matches = new ArrayList<>();
		for (TraceRecord record : traces) {
			if (matchesArguments(record, rootVarNames, arguments)) {
				matches.add(record);
			}
		}
		return matches;
	}

	/**
	 * Checks if any trace record exists for the given relation and arguments.
	 */
	public boolean hasTrace(Relation relation, List<Object> arguments) {
		return !lookup(relation, arguments).isEmpty();
	}

	/**
	 * Collects root variable names from all domains of a relation,
	 * ordered by domain order then pattern order.
	 */
	static List<String> collectRootVarNames(Relation relation) {
		List<String> names = new ArrayList<>();
		for (Domain domain : relation.getDomain()) {
			if (domain instanceof RelationDomain rd) {
				for (Variable rootVar : rd.getRootVariable()) {
					names.add(rootVar.getName());
				}
			}
		}
		return names;
	}

	private boolean matchesArguments(TraceRecord record, List<String> rootVarNames,
			List<Object> arguments) {
		int argCount = Math.min(rootVarNames.size(), arguments.size());
		for (int i = 0; i < argCount; i++) {
			Object arg = arguments.get(i);
			if (arg == null) {
				continue; // Wildcard — matches anything
			}
			String varName = rootVarNames.get(i);
			Object traceValue = record.bindings().get(varName);
			if (!Objects.equals(arg, traceValue)) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Clears all trace records.
	 */
	/**
	 * Every record of this run, in the order they were recorded.
	 *
	 * @return the records
	 */
	public List<TraceRecord> records() {
		List<TraceRecord> all = new ArrayList<>();
		tracesByRelation.values().forEach(all::addAll);
		return all;
	}

	public void clear() {
		tracesByRelation.clear();
		known.clear();
		recorded = 0;
	}
}
