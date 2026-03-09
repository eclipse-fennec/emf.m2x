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
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtbase.Domain;

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

	/**
	 * A single trace record: the full variable bindings for a successful relation execution.
	 */
	public record TraceRecord(Relation relation, Map<String, Object> bindings) {
		public TraceRecord {
			Objects.requireNonNull(relation);
			Objects.requireNonNull(bindings);
			bindings = Map.copyOf(bindings);
		}
	}

	private final Map<String, List<TraceRecord>> tracesByRelation = new HashMap<>();

	/**
	 * Records a trace entry for a successful relation execution.
	 *
	 * @param relation the relation that was executed
	 * @param bindings the complete variable bindings (source + target)
	 */
	public void record(Relation relation, Map<String, Object> bindings) {
		String key = relation.getName();
		tracesByRelation.computeIfAbsent(key, k -> new ArrayList<>())
				.add(new TraceRecord(relation, bindings));
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
	public void clear() {
		tracesByRelation.clear();
	}
}
