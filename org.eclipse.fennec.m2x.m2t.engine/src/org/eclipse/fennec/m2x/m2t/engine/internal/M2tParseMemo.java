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

import java.util.Objects;

import org.eclipse.emf.common.notify.Adapter;
import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.fennec.m2x.m2t.parser.M2tParseResult;
import org.eclipse.fennec.m2x.model.m2t.Module;

/**
 * The parse result of a module, kept on the module itself.
 *
 * <p>The engine has to find its way back from a module to what the parser knew about it —
 * the names of an {@code extends}, an {@code import}, an {@code overrides} — because the AST
 * does not carry them. That used to be a map from module to parse result in the engine. It
 * could not work: a parse result names its own module, and everything it indexes (templates,
 * invocations) is contained in that module, so the value of an entry keeps its own key
 * strongly reachable. A {@code WeakHashMap} never drops such an entry, which made every module
 * an engine ever parsed live as long as the engine (#184).
 *
 * <p>An adapter has the lifetime this needs by construction: the module holds it, it holds
 * the module, and the two are collected together as soon as the caller lets go of the module.
 * The engine keeps no reference of its own, and {@link #detach} makes
 * {@link org.eclipse.fennec.m2x.m2t.api.M2tEngine#release(Module)} exact rather than a hint
 * to the garbage collector.
 *
 * <p>Adapters are not persisted and are not copied by {@code EcoreUtil.copy}, so a compiled
 * document carries none of this — what a stored unit still has to bind travels as annotations
 * instead, see {@link M2tLinkInfo}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class M2tParseMemo extends AdapterImpl {

	private final M2tParseResult result;

	private M2tParseMemo(M2tParseResult result) {
		this.result = result;
	}

	/**
	 * Remembers the parse result on the module it belongs to, replacing an earlier one.
	 *
	 * @param result the parse result, must not be {@code null}
	 */
	static void attach(M2tParseResult result) {
		Objects.requireNonNull(result, "result must not be null");
		Module module = result.module();
		detach(module);
		module.eAdapters().add(new M2tParseMemo(result));
	}

	/**
	 * Returns what the parser knew about the module.
	 *
	 * @param module the module, may be {@code null}
	 * @return the parse result, or {@code null} if this engine never parsed the module
	 */
	static M2tParseResult of(Module module) {
		if (module == null) {
			return null;
		}
		for (Adapter adapter : module.eAdapters()) {
			if (adapter instanceof M2tParseMemo memo) {
				return memo.result;
			}
		}
		return null;
	}

	/**
	 * Forgets the parse result of the module.
	 *
	 * @param module the module, must not be {@code null}
	 */
	static void detach(Module module) {
		Objects.requireNonNull(module, "module must not be null");
		module.eAdapters().removeIf(M2tParseMemo.class::isInstance);
	}

	@Override
	public boolean isAdapterForType(Object type) {
		return type == M2tParseMemo.class;
	}
}
