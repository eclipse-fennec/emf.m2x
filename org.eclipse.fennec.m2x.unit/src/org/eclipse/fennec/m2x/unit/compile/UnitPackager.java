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
package org.eclipse.fennec.m2x.unit.compile;

import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;

/**
 * Turns a parsed unit into a {@link CompiledUnit}: one document that holds the script, the
 * parser's satellites and the manifest, and references nothing uncontained.
 *
 * <p>This is the Package step of the Compile phase (concept §4), the same for QVT-O, QVT-R and
 * MOFM2T: each engine's {@code compile()} parses in its own language and then hands the
 * result here. The unit root is typed {@link EPackage} because all three language roots
 * inherit from it — the one thing this neutral code can hold without knowing a language.
 *
 * <p>The script is <em>moved</em> into the compiled unit, not copied. A caller that still holds
 * the parsed object holds the very object now contained in the compiled unit, and can go on
 * executing it — {@code compile()} adds a document around the graph, it does not replace the
 * graph.
 *
 * <p>What this step does not do, because a later step owns it: bind dependencies under
 * embed/pin/rebind (#139), record package entries and blackbox requirements (#139). Those
 * manifest slots stay empty here; the unit fingerprint is computed (#138).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class UnitPackager {

	private UnitPackager() {
	}

	/**
	 * Packages a parsed unit.
	 *
	 * <p>Afterwards {@link SatelliteCollector#find(EObject)} on the result is empty — that is
	 * asserted, not assumed. A satellite the collector could not place would leave a dangling
	 * reference in a document that claims to be self-contained, so it is an error here rather
	 * than a failure on the first {@code save()} somewhere else (#137: loud, never a silent skip).
	 *
	 * @param language the language tag, e.g. {@code qvto}, {@code qvtr}, {@code m2t}
	 * @param qualifiedName the name the unit is imported by
	 * @param unit the parsed script; must not already be contained elsewhere
	 * @return the compiled unit, with a fresh {@code id}
	 * @throws IllegalArgumentException if the unit is already contained in another object
	 * @throws IllegalStateException if the document does not become self-contained
	 */
	public static CompiledUnit compile(String language, String qualifiedName, EPackage unit) {
		Objects.requireNonNull(language, "language must not be null");
		Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
		Objects.requireNonNull(unit, "unit must not be null");
		if (unit.eContainer() != null) {
			throw new IllegalArgumentException("the unit '" + qualifiedName
					+ "' is already contained in a " + unit.eContainer().eClass().getName()
					+ "; a script belongs to one compiled unit");
		}

		CompiledFactory factory = CompiledFactory.eINSTANCE;
		CompiledUnit compiled = factory.createCompiledUnit();
		compiled.setId(UUID.randomUUID().toString());

		CompiledUnitManifest manifest = factory.createCompiledUnitManifest();
		manifest.setLanguage(language);
		manifest.setQualifiedName(qualifiedName);
		compiled.setManifest(manifest);

		compiled.setUnit(unit);
		SatelliteCollector.contain(compiled, compiled.getSatellite());

		List<EObject> left = SatelliteCollector.find(compiled);
		if (!left.isEmpty()) {
			throw new IllegalStateException("the compiled unit '" + qualifiedName
					+ "' still references " + left.size() + " uncontained object(s), first: "
					+ left.get(0).eClass().getName());
		}
		// Fingerprinting is part of Package (concept §4): the manifest says what this is
		manifest.setUnitFingerprint(DefaultUnitFingerprintService.INSTANCE.fingerprint(compiled));
		return compiled;
	}
}
