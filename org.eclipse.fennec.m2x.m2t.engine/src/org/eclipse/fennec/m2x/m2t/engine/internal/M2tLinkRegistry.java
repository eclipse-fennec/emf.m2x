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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Deque;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Supplier;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.m2t.parser.M2tWhitespaceNormalizer;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;

/**
 * What an engine remembers about the modules it has seen, and the lock that guards rewriting
 * them.
 *
 * <p>Split out of the engine (#194), which was doing five jobs at once and where every fix of
 * #184 had to be threaded through the execute path because there was nowhere else to put it.
 * The three properties this class exists to keep are properties of the state, not of the
 * generation, and they are stated once here rather than at each use:
 *
 * <ul>
 * <li><b>Weakly.</b> An engine is long-lived — under OSGi one service serves everybody — so
 * remembering a module must not keep it alive. The parse result is the exception and lives on
 * the module itself as a {@link M2tParseMemo}: it names its own module and indexes nodes
 * inside it, so as the value of a weak map it would keep its own key reachable.</li>
 * <li><b>Per link set.</b> What a generation may see is decided by the module it starts from,
 * together with whatever it was linked with and what those extend or import — never by
 * everything the engine ever linked, which would let a module linked later take over a
 * template of a generation that never asked for it.</li>
 * <li><b>Under one lock.</b> Linking and whitespace normalization both rewrite the caller's
 * AST in place and are each done once and then remembered; without the lock a second thread
 * can evaluate a module while the first is still rewriting it.</li>
 * </ul>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class M2tLinkRegistry {

	/** Guards everything that rewrites an AST: linking and whitespace normalization. */
	private final Object astMutationLock = new Object();

	/** The modules this engine parsed, for lookup by name. Weak — no value points back. */
	private final Set<Module> parsedModules =
			Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

	/** Tracks which modules have been linked, to avoid re-linking. */
	private final Set<Module> linkedModules =
			Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

	/** What each linked module's link could not resolve (#144). */
	private final Map<Module, List<String>> unresolvedReferences =
			Collections.synchronizedMap(new WeakHashMap<>());

	/** Which modules were linked together with each module (#184). */
	private final Map<Module, List<Module>> linkGroups =
			Collections.synchronizedMap(new WeakHashMap<>());

	/** Tracks which modules have been whitespace-normalized. */
	private final Set<Module> normalizedModules =
			Collections.synchronizedSet(Collections.newSetFromMap(new WeakHashMap<>()));

	/** Where each expression node stood in its template, for runtime diagnostics (#116). */
	private final Map<EObject, SourcePosition> positions =
			Collections.synchronizedMap(new WeakHashMap<>());

	/** Indentation of standalone template invocations (MOFM2T §8.4). */
	private final Map<TemplateInvocation, String> indentation =
			Collections.synchronizedMap(new WeakHashMap<>());

	/**
	 * Remembers a parse result: on the module itself, and the module for lookup by name.
	 *
	 * @param result what the parser produced, must not be {@code null}
	 */
	void remember(M2tParseResult result) {
		Objects.requireNonNull(result, "result must not be null");
		M2tParseMemo.attach(result);
		parsedModules.add(result.module());
		positions.putAll(result.positions());
	}

	/**
	 * What the parser knew about a module, or {@code null} if this engine never parsed it.
	 *
	 * @param module the module, may be {@code null}
	 * @return the parse result, or {@code null}
	 */
	M2tParseResult parseResultOf(Module module) {
		return M2tParseMemo.of(module);
	}

	/**
	 * A module this engine parsed under that name, or {@code null}.
	 *
	 * @param name the module name
	 * @return the module, or {@code null}
	 */
	Module moduleByName(String name) {
		for (Module module : List.copyOf(parsedModules)) {
			if (name.equals(module.getName())) {
				return module;
			}
		}
		return null;
	}

	/**
	 * Runs something that rewrites an AST, with no other such work in flight.
	 *
	 * @param <T> what the work answers
	 * @param work the rewrite
	 * @return what the work answered
	 */
	<T> T underAstLock(Supplier<T> work) {
		synchronized (astMutationLock) {
			return work.get();
		}
	}

	/**
	 * Records the outcome of one link call.
	 *
	 * @param modules the modules the caller handed in
	 * @param group every module of the link, the resolved ones included
	 * @param unresolved what each of them could not resolve
	 */
	void recordLink(Module[] modules, List<Module> group, Map<Module, List<String>> unresolved) {
		for (Module module : modules) {
			linkedModules.add(module);
			unresolvedReferences.put(module, unresolved.getOrDefault(module, List.of()));
			linkGroups.put(module, group);
		}
	}

	/**
	 * @param module the module
	 * @return whether this engine has linked it
	 */
	boolean isLinked(Module module) {
		return linkedModules.contains(module);
	}

	/**
	 * @param module the module
	 * @return what its link could not resolve, never {@code null}
	 */
	List<String> unresolvedReferences(Module module) {
		return unresolvedReferences.getOrDefault(module, List.of());
	}

	/**
	 * Normalizes a module's whitespace, once.
	 *
	 * @param module the module
	 * @param normalizer the normalizer to run if it has not been run for this module
	 */
	void normalizeOnce(Module module, M2tWhitespaceNormalizer normalizer) {
		if (normalizedModules.add(module)) {
			indentation.putAll(normalizer.normalize(module));
		}
	}

	/**
	 * The modules one generation started from the given module runs against, and what is known
	 * about their nodes.
	 *
	 * @param module the module the generation starts from
	 * @return the link set
	 */
	M2tLinkSet linkSetFor(Module module) {
		List<Module> all = new ArrayList<>();
		Set<Module> seen = Collections.newSetFromMap(new IdentityHashMap<>());
		Deque<Module> queue = new ArrayDeque<>();
		queue.add(module);
		List<Module> group = linkGroups.get(module);
		if (group != null) {
			queue.addAll(group);
		}
		while (!queue.isEmpty()) {
			Module current = queue.removeFirst();
			if (current == null || !seen.add(current)) {
				continue;
			}
			all.add(current);
			queue.addAll(current.getExtends());
			queue.addAll(current.getImports());
		}
		return new M2tLinkSet(all, indentation, positions);
	}

	/**
	 * Forgets everything about one module.
	 *
	 * @param module the module, must not be {@code null}
	 */
	void release(Module module) {
		Objects.requireNonNull(module, "module must not be null");
		M2tParseMemo.detach(module);
		parsedModules.remove(module);
		linkedModules.remove(module);
		linkGroups.remove(module);
		unresolvedReferences.remove(module);
		normalizedModules.remove(module);
		synchronized (indentation) {
			indentation.keySet().removeIf(invocation -> EcoreUtil.isAncestor(module, invocation));
		}
		synchronized (positions) {
			positions.keySet().removeIf(node -> EcoreUtil.isAncestor(module, node));
		}
	}

	/** Forgets everything about every module. */
	void clear() {
		for (Module module : List.copyOf(parsedModules)) {
			M2tParseMemo.detach(module);
		}
		parsedModules.clear();
		linkedModules.clear();
		linkGroups.clear();
		unresolvedReferences.clear();
		normalizedModules.clear();
		indentation.clear();
		positions.clear();
	}
}
