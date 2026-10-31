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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Deque;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtbase.Pattern;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;

/**
 * The order in which the top relations of a transformation run.
 *
 * <p>"The execution of a transformation requires that all its top-level relations hold"
 * (§7.2.2) — no order of declaration enters into it. A relation whose when clause calls another
 * can only find what that other one has recorded, so it runs after it: each top relation runs
 * after the relations its when clause depends on, and relations that depend on each other form a
 * component that runs until it adds no trace (#349).
 *
 * <p>A when call to a relation that is not top level depends on the top relations whose where
 * clauses — directly or through other relations they call — invoke it, because only those
 * record its traces.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class QvtrRelationOrder {

	private final List<Relation> relations;
	private final Map<Relation, Set<Relation>> dependencies = new HashMap<>();
	private final Map<Relation, Integer> index = new HashMap<>();
	private final Map<Relation, Integer> lowLink = new HashMap<>();
	private final Deque<Relation> stack = new ArrayDeque<>();
	private final Set<Relation> onStack = new HashSet<>();
	private final List<List<Relation>> result = new ArrayList<>();

	private QvtrRelationOrder(List<Relation> relations) {
		this.relations = relations;
		for (Relation relation : relations) {
			Set<Relation> needed = new LinkedHashSet<>();
			for (Relation called : calls(relation.getWhen())) {
				if (relations.contains(called)) {
					needed.add(called);
				} else {
					needed.addAll(invokersOf(called));
				}
			}
			dependencies.put(relation, needed);
		}
	}

	/**
	 * The top relations in groups, each group after the groups it depends on. A group of one
	 * relation that does not depend on itself runs once; any other group is a cycle.
	 *
	 * @param relations the top relations that run, in declaration order
	 * @return the groups in the order they run
	 */
	static List<List<Relation>> components(List<Relation> relations) {
		Objects.requireNonNull(relations, "relations must not be null");
		return new QvtrRelationOrder(relations).tarjan();
	}

	/**
	 * Whether a group's relations depend on each other, so that it has to run until it adds no
	 * trace.
	 *
	 * @param component a group {@link #components} returned
	 * @return {@code true} for a cycle
	 */
	static boolean isCyclic(List<Relation> component) {
		if (component.size() > 1) {
			return true;
		}
		Relation only = component.get(0);
		return calls(only.getWhen()).contains(only);
	}

	/** The top relations whose where clauses invoke the relation, directly or transitively. */
	private Set<Relation> invokersOf(Relation target) {
		Set<Relation> invokers = new LinkedHashSet<>();
		for (Relation relation : relations) {
			if (reachesThroughWhere(relation, target, new HashSet<>())) {
				invokers.add(relation);
			}
		}
		return invokers;
	}

	private static boolean reachesThroughWhere(Relation from, Relation target, Set<Relation> seen) {
		if (!seen.add(from)) {
			return false;
		}
		for (Relation called : calls(from.getWhere())) {
			if (called == target || reachesThroughWhere(called, target, seen)) {
				return true;
			}
		}
		return false;
	}

	/** The relations called anywhere in a when or where pattern. */
	private static Set<Relation> calls(Pattern pattern) {
		Set<Relation> called = new LinkedHashSet<>();
		if (pattern != null) {
			for (var it = pattern.eAllContents(); it.hasNext();) {
				EObject o = it.next();
				if (o instanceof RelationCallExp call && call.getReferredRelation() != null) {
					called.add(call.getReferredRelation());
				}
			}
		}
		return called;
	}

	// Tarjan's algorithm emits a component only after every component it reaches — with an edge
	// from a relation to what it depends on, dependencies come first

	private List<List<Relation>> tarjan() {
		for (Relation relation : relations) {
			if (!index.containsKey(relation)) {
				connect(relation);
			}
		}
		return result;
	}

	private void connect(Relation relation) {
		index.put(relation, index.size());
		lowLink.put(relation, index.get(relation));
		stack.push(relation);
		onStack.add(relation);
		for (Relation needed : dependencies.get(relation)) {
			if (!index.containsKey(needed)) {
				connect(needed);
				lowLink.put(relation, Math.min(lowLink.get(relation), lowLink.get(needed)));
			} else if (onStack.contains(needed)) {
				lowLink.put(relation, Math.min(lowLink.get(relation), index.get(needed)));
			}
		}
		if (lowLink.get(relation).equals(index.get(relation))) {
			List<Relation> component = new ArrayList<>();
			Relation member;
			do {
				member = stack.pop();
				onStack.remove(member);
				component.add(member);
			} while (member != relation);
			// Declaration order inside a cycle
			component.sort((a, b) -> Integer.compare(relations.indexOf(a), relations.indexOf(b)));
			result.add(List.copyOf(component));
		}
	}
}
