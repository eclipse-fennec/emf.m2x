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
package org.eclipse.fennec.m2x.unit.prepare;

import java.util.ArrayDeque;
import java.util.Collection;
import java.util.Deque;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.PackageEntry;
import org.eclipse.fennec.m2x.model.compiled.ResolvedDependency;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitBinder;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitResourceSet;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;

/**
 * Prepare: from stored units to a {@link PreparedContext} an engine can run without asking
 * anybody anything (#140, concept §4.1, §6).
 *
 * <p>One prepare run loads the requested units and, following their manifests, the whole
 * dependency closure into <em>one</em> resource set — a pipeline shares it, so the output of one
 * unit is not foreign to the next. Per unit:
 *
 * <ul>
 * <li><b>Load.</b> Under {@code pin} a dependency is loaded at exactly the pinned fingerprint;
 * a version the store no longer has is a hard failure naming what it has instead (decision E4).
 * Under {@code rebind} the newest version is taken and what was bound is written into the
 * unit's {@code resolvedClosure} — under rebind the unit fingerprint says nothing about the
 * dependencies, so this record is the only way to reconstruct what a run computed. Under
 * {@code embed} there is nothing to load. Two units pinning different versions of one name are
 * a conflict, not a silent choice.</li>
 * <li><b>Verify.</b> Every {@link PackageEntry} is checked against the runtime registry: a
 * runtime instance with the same fingerprint is what the unit's references resolve to —
 * generated code wins, blackboxes keep working with generated types; a missing instance is served
 * from the copy the unit carries; an instance with a <em>differing</em> fingerprint is a hard
 * failure naming the nsURI and both values. The same nsURI recorded with two fingerprints by two
 * units of one run is a failure too.</li>
 * <li><b>Validate and bind.</b> The language's {@link UnitBinder} checks the unit's well-formedness
 * ({@code validate}, #142) and binds the unit to its loaded dependencies and
 * checks the blackbox requirements against the runtime. Afterwards nothing is left to resolve.</li>
 * </ul>
 *
 * <p>The resolution itself happens as the resource set resolves the loaded document — tier by
 * tier, runtime registry, then global, then copy ({@link UnitResourceSet}). Verification comes
 * after, and that order is sound: where the runtime has an instance the document resolved to it,
 * and it is either equal — fine — or different — failure, context discarded; where it has none
 * the copy served. No decision changes.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class UnitPreparer {

	private static final String SOURCE_STORE = "store";

	private final UnitStore store;
	private final EPackage.Registry runtime;
	private final FingerprintService fingerprints;
	private final Map<String, UnitBinder> binders = new HashMap<>();

	/**
	 * Creates a preparer.
	 *
	 * @param store where units come from
	 * @param runtime the runtime's package registry — what generated code and the caller registered
	 * @param fingerprints the service package fingerprints are compared with
	 * @param binders one binder per language the units may be written in
	 */
	public UnitPreparer(UnitStore store, EPackage.Registry runtime, FingerprintService fingerprints,
			Collection<UnitBinder> binders) {
		this.store = Objects.requireNonNull(store, "store must not be null");
		this.runtime = Objects.requireNonNull(runtime, "runtime must not be null");
		this.fingerprints = Objects.requireNonNull(fingerprints, "fingerprints must not be null");
		for (UnitBinder binder : Objects.requireNonNull(binders, "binders must not be null")) {
			this.binders.put(binder.language(), binder);
		}
	}

	/**
	 * A preparer for a plain JVM: the global registry as runtime, the default fingerprint service.
	 *
	 * @param store where units come from
	 * @param binders one binder per language
	 * @return the preparer, never {@code null}
	 */
	public static UnitPreparer withDefaults(UnitStore store, UnitBinder... binders) {
		return new UnitPreparer(store, EPackage.Registry.INSTANCE, FingerprintHelper.getDefaultFingerprintService(),
				List.of(binders));
	}

	/**
	 * Prepares the given units and their dependency closure.
	 *
	 * @param roots the units to prepare, compiled ones; a key without fingerprint takes the newest
	 * @return the context, never {@code null}
	 * @throws UnitPrepareException if a unit or a pinned version is missing, a metamodel differs, a
	 *             blackbox is missing, or no binder serves a unit's language
	 */
	public PreparedContext prepare(UnitKey... roots) throws UnitPrepareException {
		return prepare(List.of(roots));
	}

	/**
	 * Prepares the given units and their dependency closure — see {@link #prepare(UnitKey...)}.
	 *
	 * @param roots the units to prepare
	 * @return the context, never {@code null}
	 * @throws UnitPrepareException see {@link #prepare(UnitKey...)}
	 */
	public PreparedContext prepare(Collection<UnitKey> roots) throws UnitPrepareException {
		Objects.requireNonNull(roots, "roots must not be null");
		UnitResourceSet resourceSet = new UnitResourceSet(runtime);
		Map<String, PackagedUnit> loaded = new LinkedHashMap<>();
		Map<String, String> packageFingerprints = new HashMap<>();
		Deque<UnitKey> pending = new ArrayDeque<>(roots);
		while (!pending.isEmpty()) {
			UnitKey key = pending.removeFirst();
			if (key.kind() != UnitKind.COMPILED) {
				throw new UnitPrepareException("'" + key.qualifiedName() + "' is a " + key.kind().tag()
						+ "; prepare takes compiled units — compile() first");
			}
			PackagedUnit already = loaded.get(key.qualifiedName());
			if (already != null) {
				requireSameVersion(key, already);
				continue;
			}
			PackagedUnit unit = load(key, resourceSet);
			loaded.put(key.qualifiedName(), unit);
			verifyPackages(unit.document(), resourceSet, packageFingerprints);
			for (DependencyEntry dependency : unit.document().getManifest().getDependencyEntry()) {
				switch (dependency.getMode()) {
					case PIN -> pending.addLast(UnitKey.pinned(unit.language(), dependency.getQualifiedName(),
							UnitKind.COMPILED, Objects.requireNonNull(dependency.getFingerprint(),
									"a pinned dependency carries a fingerprint")));
					case REBIND -> pending.addLast(UnitKey.of(unit.language(), dependency.getQualifiedName(),
							UnitKind.COMPILED));
					case EMBED -> {
						// carried inside the unit, bound at compile time
					}
				}
			}
		}
		// The language's well-formedness first, for every unit, before anything is bound (#142)
		for (PackagedUnit unit : loaded.values()) {
			binder(unit).validate(unit.document());
		}
		for (PackagedUnit unit : loaded.values()) {
			bind(unit, loaded);
		}
		return new DefaultPreparedContext(resourceSet, loaded);
	}

	private PackagedUnit load(UnitKey key, UnitResourceSet resourceSet) throws UnitPrepareException {
		Optional<Unit> unit;
		try {
			unit = store.load(key, resourceSet);
		} catch (UnitStoreException e) {
			throw new UnitPrepareException("cannot load '" + key.qualifiedName() + "': " + e.getMessage(), e);
		}
		if (unit.isEmpty()) {
			throw new UnitPrepareException("the store has no compiled unit '" + key.qualifiedName() + "'");
		}
		if (!(unit.get() instanceof PackagedUnit packaged)) {
			throw new UnitPrepareException("'" + key.qualifiedName() + "' did not come back as a compiled document");
		}
		return packaged;
	}

	private static void requireSameVersion(UnitKey requested, PackagedUnit loaded) throws UnitPrepareException {
		String have = loaded.document().getManifest().getUnitFingerprint();
		if (requested.fingerprint().isPresent() && !requested.fingerprint().get().equals(have)) {
			throw new UnitPrepareException("'" + requested.qualifiedName() + "' is required at fingerprint "
					+ requested.fingerprint().get() + " but this run already holds it at " + have
					+ "; two units of one pipeline pin different versions of the same name");
		}
	}

	private void verifyPackages(CompiledUnit document, UnitResourceSet resourceSet,
			Map<String, String> seen) throws UnitPrepareException {
		String unitName = document.getManifest().getQualifiedName();
		for (PackageEntry entry : document.getManifest().getPackageEntry()) {
			String nsURI = entry.getNsURI();
			String recorded = entry.getFingerprint();
			String other = seen.putIfAbsent(nsURI, recorded);
			if (other != null && !other.equals(recorded)) {
				throw new UnitPrepareException("'" + unitName + "' was compiled against " + nsURI + " with fingerprint "
						+ recorded + ", another unit of this run against " + other);
			}
			EPackage runtimeInstance = runtime.getEPackage(nsURI);
			if (runtimeInstance == null) {
				EPackage carried = resourceSet.packageFor(nsURI);
				if (carried == null) {
					throw new UnitPrepareException("'" + unitName + "' needs the metamodel " + nsURI
							+ ", which the runtime does not have and the unit carries no copy of");
				}
				// The copy is what the unit's types will resolve to, so it is held to the same
				// entry as a runtime instance would be (#183) — before, a carried copy was used
				// without being compared to the fingerprint it was recorded with
				requireFingerprint(carried, entry, unitName, "the copy the unit carries");
				continue;
			}
			requireFingerprint(runtimeInstance, entry, unitName, "the runtime");
		}
	}

	private UnitBinder binder(PackagedUnit unit) throws UnitPrepareException {
		UnitBinder binder = binders.get(unit.language());
		if (binder == null) {
			throw new UnitPrepareException("no binder for language '" + unit.language() + "' (unit '"
					+ unit.qualifiedName() + "')");
		}
		return binder;
	}

	/**
	 * Holds a package to the fingerprint the unit was compiled against.
	 *
	 * @param ePackage the package that would serve the nsURI
	 * @param entry what the manifest recorded
	 * @param unitName the unit, for the message
	 * @param source where the package came from, for the message
	 * @throws UnitPrepareException if the fingerprints differ, naming both
	 */
	private void requireFingerprint(EPackage ePackage, PackageEntry entry, String unitName, String source)
			throws UnitPrepareException {
		String actual;
		try {
			actual = fingerprints.fingerprintInScheme(entry.getScheme(), ePackage);
		} catch (RuntimeException e) {
			throw new UnitPrepareException("cannot fingerprint " + entry.getNsURI() + " in scheme '"
					+ entry.getScheme() + "': " + e.getMessage(), e);
		}
		if (!actual.equals(entry.getFingerprint())) {
			throw new UnitPrepareException("'" + unitName + "' was compiled against " + entry.getNsURI()
					+ " with fingerprint " + entry.getFingerprint() + ", " + source + " provides " + actual);
		}
	}

	private void bind(PackagedUnit unit, Map<String, PackagedUnit> loaded) throws UnitPrepareException {
		UnitBinder binder = binder(unit);
		Map<String, CompiledUnit> dependencies = new LinkedHashMap<>();
		for (DependencyEntry dependency : unit.document().getManifest().getDependencyEntry()) {
			PackagedUnit bound = loaded.get(dependency.getQualifiedName());
			switch (dependency.getMode()) {
				case PIN -> dependencies.put(dependency.getQualifiedName(), bound.document());
				case REBIND -> {
					dependencies.put(dependency.getQualifiedName(), bound.document());
					ResolvedDependency record = CompiledFactory.eINSTANCE.createResolvedDependency();
					record.setQualifiedName(dependency.getQualifiedName());
					record.setFingerprint(bound.document().getManifest().getUnitFingerprint());
					record.setSource(SOURCE_STORE);
					unit.document().getManifest().getResolvedClosure().add(record);
				}
				case EMBED -> {
					// bound at compile time
				}
			}
		}
		binder.bind(unit.document(), dependencies);
		binder.verifyBlackboxes(unit.document());
	}
}
