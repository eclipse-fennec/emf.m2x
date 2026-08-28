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

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Objects;
import java.util.UUID;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.compiled.PackageEntry;
import org.eclipse.fennec.m2x.model.compiled.PackageRole;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;

/**
 * Packages a parsed unit into a {@link CompiledUnit} document: the script, its satellites, its
 * manifest — the language-neutral part of Compile (§4 of the compiled-unit concept).
 *
 * <p>Packaging has two halves with the language's own work in between. {@link #begin} opens the
 * document: identity, manifest header, the script. The language compiler then resolves the
 * imports — embedding, pinning or recording them — and adds its blackbox requirements.
 * {@link #seal} closes the document: the satellites get their home, the referenced packages are
 * recorded with their fingerprints and the dynamic ones copied in, and the unit fingerprint is
 * stamped last, because it folds in what the compiler recorded. A unit without imports goes
 * through {@link #compile(String, String, EPackage)}, which is the two halves back to back.
 *
 * <p>Package fingerprints come from the {@link FingerprintService} of emf.osgi ({@code fp1}), the
 * value the ecosystem — {@code @EPackage(fingerprint)}, the metadata service — keys models by.
 * Unit fingerprints come from the m2x mechanism. Two schemes, two tags; the manifest carries both.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class UnitPackager {

	/** The version of the manifest format this packager writes. */
	public static final String FORMAT_VERSION = "1.0";

	private static final String PRODUCER = producer();

	private final FingerprintService packageFingerprints;
	private final UnitFingerprintService unitFingerprints;

	/**
	 * Creates a packager with the given package fingerprint service and the default unit
	 * fingerprint mechanism.
	 *
	 * @param packageFingerprints the service package fingerprints come from
	 */
	public UnitPackager(FingerprintService packageFingerprints) {
		this(packageFingerprints, DefaultUnitFingerprintService.INSTANCE);
	}

	/**
	 * Creates a packager with both services given.
	 *
	 * @param packageFingerprints the service package fingerprints come from
	 * @param unitFingerprints the service unit fingerprints come from
	 */
	public UnitPackager(FingerprintService packageFingerprints, UnitFingerprintService unitFingerprints) {
		this.packageFingerprints = Objects.requireNonNull(packageFingerprints,
				"packageFingerprints must not be null");
		this.unitFingerprints = Objects.requireNonNull(unitFingerprints,
				"unitFingerprints must not be null");
	}

	/**
	 * A packager for a plain JVM: the default emf.osgi fingerprint implementation, reached
	 * without a framework. Under OSGi the {@link FingerprintService} arrives as a service and
	 * goes through {@link #UnitPackager(FingerprintService)} instead.
	 *
	 * @return the packager, never {@code null}
	 */
	public static UnitPackager withDefaults() {
		return new UnitPackager(FingerprintHelper.getDefaultFingerprintService());
	}

	/**
	 * Packages a parsed unit that has nothing to resolve: {@link #begin} and {@link #seal} back
	 * to back, dependencies pinned, no source recorded.
	 *
	 * @param language the language tag, e.g. {@code qvto}, {@code qvtr}, {@code m2t}
	 * @param qualifiedName the name the unit is imported by
	 * @param unit the parsed script; must not already be contained elsewhere
	 * @return the sealed compiled unit
	 * @throws IllegalArgumentException if the unit is already contained in another object
	 * @throws IllegalStateException if the document does not become self-contained
	 */
	public static CompiledUnit compile(String language, String qualifiedName, EPackage unit) {
		UnitPackager packager = withDefaults();
		return packager.seal(packager.begin(language, qualifiedName, unit, DependencyMode.PIN, null));
	}

	/**
	 * Opens a compiled-unit document around a parsed script.
	 *
	 * @param language the language tag
	 * @param qualifiedName the name the unit is imported by
	 * @param unit the parsed script; must not already be contained elsewhere
	 * @param mode how the compiler is going to bind the dependencies
	 * @param source the source text the script was parsed from, for the source fingerprint;
	 *            {@code null} when the script did not come from text
	 * @return the open document, with identity and manifest header but no satellites yet
	 * @throws IllegalArgumentException if the unit is already contained in another object
	 */
	public CompiledUnit begin(String language, String qualifiedName, EPackage unit, DependencyMode mode,
			String source) {
		Objects.requireNonNull(language, "language must not be null");
		Objects.requireNonNull(qualifiedName, "qualifiedName must not be null");
		Objects.requireNonNull(unit, "unit must not be null");
		Objects.requireNonNull(mode, "mode must not be null");
		if (unit.eContainer() != null) {
			throw new IllegalArgumentException("the unit '" + qualifiedName
					+ "' is already contained in a " + unit.eContainer().eClass().getName()
					+ "; a script belongs to one compiled unit");
		}
		CompiledFactory factory = CompiledFactory.eINSTANCE;
		CompiledUnit compiled = factory.createCompiledUnit();
		compiled.setId(UUID.randomUUID().toString());
		CompiledUnitManifest manifest = factory.createCompiledUnitManifest();
		manifest.setFormatVersion(FORMAT_VERSION);
		manifest.setProducedBy(PRODUCER);
		manifest.setLanguage(language);
		manifest.setQualifiedName(qualifiedName);
		manifest.setDependencyMode(mode);
		if (source != null) {
			manifest.setSourceFingerprint(
					unitFingerprints.fingerprint(new SourceText(qualifiedName, source)));
		}
		compiled.setManifest(manifest);
		compiled.setUnit(unit);
		return compiled;
	}

	/**
	 * Closes a document: contains the satellites, records the referenced packages, stamps the
	 * unit fingerprint.
	 *
	 * <p>Afterwards {@link SatelliteCollector#find(EObject)} on the result is empty — that is
	 * asserted, not assumed. A satellite the collector could not place would leave a dangling
	 * reference in a document that claims to be self-contained, so it is an error here rather
	 * than a failure on the first {@code save()} somewhere else (#137: loud, never a silent skip).
	 *
	 * <p>Every package the document refers to gets a {@link PackageEntry} with its fingerprint. A
	 * dynamic package — one without generated code — is copied into the document as well and
	 * marked {@link PackageRole#EMBEDDED}: a runtime may have nothing to offer for it, and the
	 * copy is what prepare then serves. A package with generated code is only
	 * {@link PackageRole#REFERENCED}: prepare finds it in the registry, checks the fingerprint,
	 * and hands the unit the generated instance (§6 of the concept).
	 *
	 * @param compiled the open document
	 * @return the same document, sealed
	 * @throws IllegalStateException if the document does not become self-contained
	 */
	public CompiledUnit seal(CompiledUnit compiled) {
		Objects.requireNonNull(compiled, "compiled must not be null");
		String qualifiedName = compiled.getManifest().getQualifiedName();
		SatelliteCollector.contain(compiled, compiled.getSatellite());
		List<EObject> left = SatelliteCollector.find(compiled);
		if (!left.isEmpty()) {
			throw new IllegalStateException("the compiled unit '" + qualifiedName
					+ "' still references " + left.size() + " uncontained object(s), first: "
					+ left.get(0).eClass().getName());
		}
		recordPackages(compiled);
		compiled.getManifest().setUnitFingerprint(unitFingerprints.fingerprint(compiled));
		return compiled;
	}

	/**
	 * An independent copy of a script that is not ours to package: the AST together with the
	 * satellites it references, so that the copy points at nothing of the original.
	 *
	 * <p>A resolver lends its AST; packaging it directly would move it into our document and
	 * take its satellites with it. A plain {@link EcoreUtil#copy} would not do either — the
	 * copy's references to uncontained satellites would still point at the originals (§5.5 of
	 * the concept). Copying the script and its satellites in one go is what yields a unit of our
	 * own. Type references into metamodels stay where they are; those are shared by design.
	 *
	 * @param unit the borrowed script
	 * @return the copy, uncontained
	 */
	public static EPackage detach(EPackage unit) {
		Objects.requireNonNull(unit, "unit must not be null");
		List<EObject> originals = new ArrayList<>();
		originals.add(unit);
		originals.addAll(SatelliteCollector.find(unit));
		EcoreUtil.Copier copier = new EcoreUtil.Copier();
		Collection<EObject> copies = copier.copyAll(originals);
		copier.copyReferences();
		return (EPackage) copies.iterator().next();
	}

	private void recordPackages(CompiledUnit compiled) {
		CompiledUnitManifest manifest = compiled.getManifest();
		Collection<EPackage> referenced = ReferencedPackages.of(compiled).values();
		List<EPackage> dynamic = referenced.stream().filter(ReferencedPackages::isDynamic).toList();
		for (EPackage ePackage : referenced) {
			PackageEntry entry = CompiledFactory.eINSTANCE.createPackageEntry();
			entry.setNsURI(ePackage.getNsURI());
			entry.setFingerprint(packageFingerprints.fingerprint(ePackage));
			entry.setScheme(packageFingerprints.currentScheme());
			entry.setRole(ReferencedPackages.isDynamic(ePackage) ? PackageRole.EMBEDDED : PackageRole.REFERENCED);
			manifest.getPackageEntry().add(entry);
		}
		if (!dynamic.isEmpty()) {
			// Copied together, so that references between two dynamic packages stay between the
			// copies; whole roots, so that a subpackage keeps its super-package.
			compiled.getPackages().addAll(EcoreUtil.copyAll(ReferencedPackages.roots(dynamic)));
		}
	}

	private static String producer() {
		String version = UnitPackager.class.getPackage().getImplementationVersion();
		return "org.eclipse.fennec.m2x.unit" + (version == null ? "" : " " + version);
	}

	/** The source text of a unit, for the source fingerprint of the manifest. */
	private record SourceText(String qualifiedName, String source) implements Unit.Source {
		@Override
		public URI uri() {
			return URI.createURI("unit:/" + qualifiedName);
		}
	}
}
