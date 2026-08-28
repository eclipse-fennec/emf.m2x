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
package org.eclipse.fennec.m2x.m2t.engine;

import java.util.Objects;
import java.util.Optional;

import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.m2t.api.M2tUnit;
import org.eclipse.fennec.m2x.m2t.api.M2tUnitResolver;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;

/**
 * Resolves units from a {@link UnitStore} (§5.5 of the compiled-unit concept).
 *
 * <p>Asked for a name, the resolver looks for a compiled unit first and for a source second: a
 * compiled unit carries its manifest and its fingerprint, which is what a {@code pin} needs
 * without compiling again; a source is parsed by the caller. The store hands out independent
 * copies, so the AST this resolver lends is nobody else's.
 *
 * <p>A store that cannot answer is not "not found": a {@link UnitStoreException} surfaces as a
 * {@link UnitResolutionException} instead of an empty result, so a broken store does not let a
 * stale copy elsewhere step in unnoticed (§7 of the concept, #141).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class M2tStoreUnitResolver implements M2tUnitResolver {

	private static final String LANGUAGE = "m2t";

	private final UnitStore store;

	/**
	 * Creates a resolver over a store.
	 *
	 * @param store the store to resolve from
	 */
	public M2tStoreUnitResolver(UnitStore store) {
		this.store = Objects.requireNonNull(store, "store must not be null");
	}

	@Override
	public Optional<M2tUnit> resolveUnit(String qualifiedName) {
		if (qualifiedName == null || qualifiedName.isBlank()) {
			return Optional.empty();
		}
		try {
			Optional<Unit> compiled = store.load(UnitKey.of(LANGUAGE, qualifiedName, UnitKind.COMPILED));
			if (compiled.isPresent() && compiled.get() instanceof PackagedUnit packaged
					&& packaged.document().getUnit() instanceof Module root) {
				return Optional.of(new M2tUnit.CompiledUnit(qualifiedName, root));
			}
			Optional<Unit> source = store.load(UnitKey.of(LANGUAGE, qualifiedName, UnitKind.SOURCE));
			if (source.isPresent() && source.get() instanceof Unit.Source text) {
				return Optional.of(new M2tUnit.SourceUnit(qualifiedName, text.uri(), text.source()));
			}
			return Optional.empty();
		} catch (UnitStoreException e) {
			throw new UnitResolutionException("the unit store could not answer for '" + qualifiedName + "': "
					+ e.getMessage(), e);
		}
	}
}
