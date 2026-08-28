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
package org.eclipse.fennec.m2x.unit.resolve;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.function.Function;

import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;

/**
 * How a unit is resolved when several sources may have it (#141, concept §7).
 *
 * <p>Three rules, applied in this order:
 *
 * <ol>
 * <li><b>Every source is asked.</b> Not until the first answer — all of them, so that a second
 * source with different content is seen at all. Which sources there are and in which order is the
 * caller's: configured resolvers in configuration order, whiteboard services by ranking, class-path
 * providers in declaration order.</li>
 * <li><b>A failing source is an error.</b> A source that throws ends the resolution with a
 * {@link UnitResolutionException} naming it. Skipping it and asking the next would let a stale copy
 * step in for a broken store, and the caller would see "not found" where it should see "broken".</li>
 * <li><b>Answers have to agree.</b> Two answers for one name are compared by what can be compared: a
 * source with a source by source fingerprint, a compiled document with a compiled document by unit
 * fingerprint, a source with a compiled document by the document's {@code sourceFingerprint} — the
 * reason the manifest records it — and a bare AST with a bare AST by AST fingerprint. Different
 * content is a conflict, named with both sources. A pair nothing can compare (a bare AST against a
 * source) is accepted with the first answer, and this is the one gap of the rule.</li>
 * </ol>
 *
 * <p>The first answer in source order is the one returned.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class ResolutionPolicy {

	/**
	 * One place a unit may come from, with a name for the diagnostic.
	 *
	 * @param <U> the language's unit type
	 */
	public interface Source<U extends Unit> {

		/**
		 * Returns a name for messages — the resolver class, the service's bundle, the store.
		 *
		 * @return the name, never {@code null}
		 */
		String name();

		/**
		 * Asks the source.
		 *
		 * @param qualifiedName the unit name
		 * @return the unit, or empty if the source does not have it
		 */
		Optional<U> resolve(String qualifiedName);

		/**
		 * Wraps a resolving function as a source.
		 *
		 * @param name the source's name
		 * @param resolver the function
		 * @param <U> the unit type
		 * @return the source
		 */
		static <U extends Unit> Source<U> of(String name, Function<String, Optional<U>> resolver) {
			Objects.requireNonNull(name, "name must not be null");
			Objects.requireNonNull(resolver, "resolver must not be null");
			return new Source<>() {
				@Override
				public String name() {
					return name;
				}

				@Override
				public Optional<U> resolve(String qualifiedName) {
					return resolver.apply(qualifiedName);
				}
			};
		}
	}

	private ResolutionPolicy() {
	}

	/**
	 * Resolves a unit from the given sources under the three rules, with the default fingerprints.
	 *
	 * @param qualifiedName the unit name
	 * @param sources the sources, in the order that decides which answer is returned
	 * @param <U> the unit type
	 * @return the first answer, or empty if no source has the unit
	 * @throws UnitResolutionException if a source fails or two sources disagree
	 */
	public static <U extends Unit> Optional<U> resolve(String qualifiedName, List<? extends Source<U>> sources) {
		return resolve(qualifiedName, sources, DefaultUnitFingerprintService.INSTANCE);
	}

	/**
	 * Resolves a unit from the given sources under the three rules.
	 *
	 * @param qualifiedName the unit name
	 * @param sources the sources, in the order that decides which answer is returned
	 * @param fingerprints where the fingerprints for the comparison come from
	 * @param <U> the unit type
	 * @return the first answer, or empty if no source has the unit
	 * @throws UnitResolutionException if a source fails or two sources disagree
	 */
	public static <U extends Unit> Optional<U> resolve(String qualifiedName, List<? extends Source<U>> sources,
			UnitFingerprintService fingerprints) {
		Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
		Objects.requireNonNull(sources, "sources must not be null");
		Objects.requireNonNull(fingerprints, "fingerprints must not be null");
		List<Answer<U>> answers = new ArrayList<>();
		for (Source<U> source : sources) {
			Optional<U> unit;
			try {
				unit = source.resolve(qualifiedName);
			} catch (RuntimeException failure) {
				// A source's own UnitResolutionException included: the message names the source
				// either way, and a caller reads one shape
				throw new UnitResolutionException("source " + source.name() + " failed for '" + qualifiedName + "': "
						+ failure.getMessage(), failure);
			}
			if (unit != null && unit.isPresent()) {
				answers.add(new Answer<>(source.name(), unit.get(), Identity.of(unit.get(), fingerprints)));
			}
		}
		if (answers.isEmpty()) {
			return Optional.empty();
		}
		Answer<U> first = answers.get(0);
		for (Answer<U> other : answers.subList(1, answers.size())) {
			first.identity.requireAgreement(other.identity, qualifiedName, first.source, other.source);
		}
		return Optional.of(first.unit);
	}

	private record Answer<U extends Unit>(String source, U unit, Identity identity) {
	}

	/**
	 * What a unit can be compared by: a source fingerprint, a unit fingerprint, an AST fingerprint —
	 * whichever it has.
	 */
	private record Identity(String sourceFingerprint, String unitFingerprint, String astFingerprint) {

		static Identity of(Unit unit, UnitFingerprintService fingerprints) {
			if (unit instanceof Unit.Source source) {
				return new Identity(fingerprints.fingerprint(source), null, null);
			}
			if (unit instanceof Unit.Packaged packaged) {
				return of(packaged.document());
			}
			if (unit instanceof Unit.Compiled compiled) {
				if (compiled.root() != null && compiled.root().eContainer() instanceof CompiledUnit document) {
					return of(document);
				}
				return new Identity(null, null, fingerprints.fingerprint(compiled));
			}
			return new Identity(null, null, null);
		}

		private static Identity of(CompiledUnit document) {
			return document.getManifest() == null ? new Identity(null, null, null)
					: new Identity(document.getManifest().getSourceFingerprint(),
							document.getManifest().getUnitFingerprint(), null);
		}

		void requireAgreement(Identity other, String qualifiedName, String mine, String theirs) {
			compare(sourceFingerprint, other.sourceFingerprint, "source", qualifiedName, mine, theirs);
			compare(unitFingerprint, other.unitFingerprint, "unit", qualifiedName, mine, theirs);
			compare(astFingerprint, other.astFingerprint, "AST", qualifiedName, mine, theirs);
		}

		private static void compare(String a, String b, String what, String qualifiedName, String mine, String theirs) {
			if (a != null && b != null && !a.equals(b)) {
				throw new UnitResolutionException("'" + qualifiedName + "' comes from " + mine + " with " + what
						+ " fingerprint " + a + " and from " + theirs + " with " + b
						+ "; two sources disagree about one unit");
			}
		}
	}
}
