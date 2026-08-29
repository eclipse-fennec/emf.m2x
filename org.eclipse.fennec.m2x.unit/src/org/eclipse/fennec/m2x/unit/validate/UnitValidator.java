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
package org.eclipse.fennec.m2x.unit.validate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EValidatorRegistryImpl;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.emf.ecore.util.EObjectValidator;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.compiled.PackageEntry;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;

/**
 * Checks a loaded compiled unit for what the parser would have guaranteed by construction
 * (#142, concept §9).
 *
 * <p>A unit loaded from a store bypasses the parser and every check it enforces. The evaluator's
 * runtime limits still hold; what does not is the shape of the document — and this is what is
 * checked here, language-neutrally:
 *
 * <ul>
 * <li><b>Integrity</b> — the {@code unitFingerprint} is recomputed and compared with the manifest.
 * That catches drift: a backend that corrupted or swapped the content. It does <em>not</em> catch
 * manipulation — whoever changes the unit can recompute the fingerprint — and is not sold as
 * doing so; authenticity is the backend's question (a backend that signs on {@code put} and
 * verifies on {@code get}).</li>
 * <li><b>Manifest consistency</b> — known format version, language and name present, every
 * {@code pin} entry with a fingerprint, embedded units of the same language, every package copy
 * with an nsURI and a matching entry.</li>
 * <li><b>Closure</b> — every reference that leaves the document points into a metamodel and at
 * nothing else; no proxies.</li>
 * <li><b>Ecore structure</b> — EMF's {@link Diagnostician} with the multiplicity and data-value
 * rules for the languages' own features; Ecore's own rules are not applied, because the unit
 * roots inherit Ecore's structure without its well-formedness (a module has no nsURI).</li>
 * <li><b>Size</b> — tree depth and node count bounded, closing for the load path the two gaps the
 * security analyses name for the parser (no parse-depth limit, no input-size limit).</li>
 * </ul>
 *
 * <p>What a language guarantees beyond the shape — variables declared, arities matching — is the
 * language's to check, in {@code UnitBinder.validate}, before prepare binds the unit.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class UnitValidator {

	/** Default bound on the containment depth of a document. */
	public static final int DEFAULT_MAX_DEPTH = 1_000;
	/** Default bound on the number of objects in a document. */
	public static final int DEFAULT_MAX_NODES = 1_000_000;

	private final int maxDepth;
	private final int maxNodes;
	private final UnitFingerprintService fingerprints;
	private final FingerprintService packageFingerprints;

	/**
	 * Creates a validator with explicit bounds.
	 *
	 * @param maxDepth the containment depth a document may have
	 * @param maxNodes the number of objects a document may have
	 * @param fingerprints where the unit fingerprint is recomputed with
	 */
	public UnitValidator(int maxDepth, int maxNodes, UnitFingerprintService fingerprints) {
		this(maxDepth, maxNodes, fingerprints, FingerprintHelper.getDefaultFingerprintService());
	}

	/**
	 * Creates a validator with explicit bounds and both fingerprint services.
	 *
	 * @param maxDepth the containment depth a document may have
	 * @param maxNodes the number of objects a document may have
	 * @param fingerprints where the unit fingerprint is recomputed with
	 * @param packageFingerprints where a carried package copy is measured with — the same service
	 *            the packager recorded the {@code PackageEntry} values with
	 */
	public UnitValidator(int maxDepth, int maxNodes, UnitFingerprintService fingerprints,
			FingerprintService packageFingerprints) {
		if (maxDepth <= 0 || maxNodes <= 0) {
			throw new IllegalArgumentException("bounds must be positive");
		}
		this.maxDepth = maxDepth;
		this.maxNodes = maxNodes;
		this.fingerprints = Objects.requireNonNull(fingerprints, "fingerprints must not be null");
		this.packageFingerprints = Objects.requireNonNull(packageFingerprints,
				"packageFingerprints must not be null");
	}

	/**
	 * The validator with the default bounds and the default fingerprint mechanism.
	 *
	 * @return the validator, never {@code null}
	 */
	public static UnitValidator defaults() {
		return new UnitValidator(DEFAULT_MAX_DEPTH, DEFAULT_MAX_NODES, DefaultUnitFingerprintService.INSTANCE);
	}

	/**
	 * Validates a document. An empty result means the document passed.
	 *
	 * @param document the loaded compiled unit
	 * @return the violations, each naming what and where; empty if none
	 */
	public List<String> validate(CompiledUnit document) {
		Objects.requireNonNull(document, "document must not be null");
		List<String> findings = new ArrayList<>();
		checkSize(document, findings);
		if (!findings.isEmpty()) {
			return findings; // a document past the bounds is not walked any further
		}
		checkManifest(document, findings);
		checkClosure(document, findings);
		checkStructure(document, findings);
		checkIntegrity(document, findings);
		return findings;
	}

	private void checkSize(CompiledUnit document, List<String> findings) {
		int nodes = 1;
		int deepest = 0;
		for (Iterator<EObject> it = document.eAllContents(); it.hasNext();) {
			EObject node = it.next();
			nodes++;
			if (nodes > maxNodes) {
				findings.add("the document has more than " + maxNodes + " objects");
				return;
			}
			int depth = 0;
			for (EObject up = node; up != null && up != document; up = up.eContainer()) {
				depth++;
			}
			if (depth > deepest) {
				deepest = depth;
				if (deepest > maxDepth) {
					findings.add("the document is nested deeper than " + maxDepth + " at " + path(document, node));
					return;
				}
			}
		}
	}

	private static void checkManifest(CompiledUnit document, List<String> findings) {
		CompiledUnitManifest manifest = document.getManifest();
		if (manifest == null) {
			findings.add("the document has no manifest");
			return;
		}
		if (!UnitPackager.FORMAT_VERSION.equals(manifest.getFormatVersion())) {
			findings.add("unknown format version '" + manifest.getFormatVersion() + "', this reader knows '"
					+ UnitPackager.FORMAT_VERSION + "'");
		}
		if (manifest.getLanguage() == null || manifest.getLanguage().isBlank()) {
			findings.add("the manifest names no language");
		}
		if (manifest.getQualifiedName() == null || manifest.getQualifiedName().isBlank()) {
			findings.add("the manifest names no qualified name");
		}
		if (document.getUnit() == null) {
			findings.add("the document carries no script");
		}
		for (DependencyEntry entry : manifest.getDependencyEntry()) {
			if (entry.getQualifiedName() == null || entry.getQualifiedName().isBlank()) {
				findings.add("a dependency entry names no unit");
			}
			if (entry.getMode() == DependencyMode.PIN && (entry.getFingerprint() == null || entry.getFingerprint().isBlank())) {
				findings.add("the pinned dependency '" + entry.getQualifiedName() + "' carries no fingerprint");
			}
		}
		for (CompiledUnit embedded : document.getEmbedded()) {
			if (embedded.getManifest() == null) {
				findings.add("an embedded unit has no manifest");
			} else if (manifest.getLanguage() != null && !manifest.getLanguage().equals(embedded.getManifest().getLanguage())) {
				findings.add("the embedded unit '" + embedded.getManifest().getQualifiedName() + "' is a '"
						+ embedded.getManifest().getLanguage() + "' unit inside a '" + manifest.getLanguage() + "' unit");
			}
		}
		Set<String> entries = new HashSet<>();
		for (PackageEntry entry : manifest.getPackageEntry()) {
			if (entry.getNsURI() == null || entry.getNsURI().isBlank()) {
				findings.add("a package entry names no nsURI");
				continue;
			}
			entries.add(entry.getNsURI());
			if (entry.getFingerprint() == null || entry.getFingerprint().isBlank()) {
				findings.add("the package entry for " + entry.getNsURI() + " carries no fingerprint");
			}
		}
		for (EPackage copy : document.getPackages()) {
			if (copy.getNsURI() == null) {
				findings.add("a package copy has no nsURI");
			} else if (!entries.contains(copy.getNsURI())) {
				findings.add("the package copy " + copy.getNsURI() + " has no package entry");
			}
		}
	}

	private static void checkClosure(CompiledUnit document, List<String> findings) {
		for (Map.Entry<EObject, ?> reference : EcoreUtil.ExternalCrossReferencer.find(document).entrySet()) {
			EObject target = reference.getKey();
			if (target instanceof EFactory factory && factory.getEPackage() != null
					&& EcoreUtil.isAncestor(document, factory.getEPackage())) {
				continue; // EMF's lazily created factory of a package in the document — a back-link, not a reference out
			}
			if (target.eIsProxy()) {
				findings.add("an unresolved reference to " + EcoreUtil.getURI(target));
			} else if (!SatelliteCollector.isMetamodelElement(target)) {
				findings.add("a reference leaves the document to a " + target.eClass().getName()
						+ " that belongs to no metamodel: " + EcoreUtil.getURI(target));
			}
		}
	}

	/**
	 * Ecore's multiplicity and data-value rules for the languages' own features. The unit roots
	 * inherit Ecore's structure — a QVT-O {@code Module} is an {@code EPackage} — without
	 * satisfying Ecore's well-formedness rules for it (a module has no nsURI to be well formed, a
	 * synthetic attribute of a QVT-R domain no type), and the parser produces them so by design.
	 * Ecore's own validator and Ecore-owned required features are therefore left out; what remains
	 * is what the language metamodels require.
	 */
	private static void checkStructure(CompiledUnit document, List<String> findings) {
		// A registry with the default entry only: the Diagnostician looks a package up and falls
		// back to the entry under null, so every object is checked by the one validator below
		EValidator.Registry registry = new EValidatorRegistryImpl();
		registry.put(null, LANGUAGE_FEATURES_ONLY);
		collect(new Diagnostician(registry).validate(document), document, findings);
	}

	private static final EValidator LANGUAGE_FEATURES_ONLY = new EObjectValidator() {
		@Override
		public boolean validate_EveryMultiplicityConforms(EObject eObject, DiagnosticChain diagnostics,
				Map<Object, Object> context) {
			boolean result = true;
			for (EStructuralFeature feature : eObject.eClass().getEAllStructuralFeatures()) {
				if (feature.getEContainingClass().getEPackage() == EcorePackage.eINSTANCE) {
					continue;
				}
				result &= validate_MultiplicityConforms(eObject, feature, diagnostics, context);
			}
			return result;
		}

		@Override
		public boolean validate_EveryProxyResolves(EObject eObject, DiagnosticChain diagnostics,
				Map<Object, Object> context) {
			return true; // the closure check reports proxies with the URI
		}

		@Override
		public boolean validate_EveryReferenceIsContained(EObject eObject, DiagnosticChain diagnostics,
				Map<Object, Object> context) {
			return true; // a metamodel built in memory lives in no resource; the closure check decides
		}
	};

	private static void collect(Diagnostic diagnostic, CompiledUnit document, List<String> findings) {
		if (diagnostic.getSeverity() >= Diagnostic.ERROR && diagnostic.getChildren().isEmpty()) {
			Object subject = diagnostic.getData().isEmpty() ? null : diagnostic.getData().get(0);
			findings.add(diagnostic.getMessage()
					+ (subject instanceof EObject object ? " at " + path(document, object) : ""));
		}
		for (Diagnostic child : diagnostic.getChildren()) {
			collect(child, document, findings);
		}
	}

	private void checkIntegrity(CompiledUnit document, List<String> findings) {
		if (document.getManifest() == null || document.getUnit() == null) {
			return;
		}
		String recorded = document.getManifest().getUnitFingerprint();
		if (recorded == null || recorded.isBlank()) {
			findings.add("the manifest carries no unit fingerprint");
			return;
		}
		String actual;
		try {
			actual = fingerprints.fingerprint(document);
		} catch (RuntimeException e) {
			findings.add("the unit fingerprint cannot be recomputed: " + e.getMessage());
			return;
		}
		if (!recorded.equals(actual)) {
			findings.add("the manifest records unit fingerprint " + recorded + " but the content has " + actual
					+ " — the document changed after it was sealed");
		}
		checkCarried(document, findings);
	}

	/**
	 * Holds what a document carries to the identity the manifest recorded for it (#183).
	 *
	 * <p>The unit fingerprint covers the script. A package copy and an embedded unit are carried
	 * <em>beside</em> the script, and each already has a recorded value: the {@code PackageEntry}
	 * of the copy's nsURI carries its {@code fp1} fingerprint, the {@code DependencyEntry} of the
	 * embedded unit carries its {@code m2x1} one. Before this, neither was ever compared — a
	 * tampered copy served the unit's types and a swapped embedded library was executed, both
	 * through a validator that reported nothing.
	 *
	 * <p>An embedded unit is validated as a document in its own right as well: it is what will run.
	 */
	private void checkCarried(CompiledUnit document, List<String> findings) {
		Map<String, PackageEntry> entries = new HashMap<>();
		for (PackageEntry entry : document.getManifest().getPackageEntry()) {
			entries.put(entry.getNsURI(), entry);
		}
		for (EPackage copy : document.getPackages()) {
			PackageEntry entry = entries.get(copy.getNsURI());
			if (entry == null || entry.getFingerprint() == null || entry.getScheme() == null) {
				continue; // reported by checkManifest as a copy without an entry
			}
			String actual;
			try {
				actual = packageFingerprints.fingerprintInScheme(entry.getScheme(), copy);
			} catch (RuntimeException e) {
				findings.add("the package copy " + copy.getNsURI() + " cannot be fingerprinted in scheme '"
						+ entry.getScheme() + "': " + e.getMessage());
				continue;
			}
			if (!actual.equals(entry.getFingerprint())) {
				findings.add("the package copy " + copy.getNsURI() + " has fingerprint " + actual
						+ " but its entry records " + entry.getFingerprint()
						+ " — the carried metamodel changed after it was sealed");
			}
		}
		Map<String, String> pinned = new HashMap<>();
		for (DependencyEntry entry : document.getManifest().getDependencyEntry()) {
			if (entry.getFingerprint() != null) {
				pinned.put(entry.getQualifiedName(), entry.getFingerprint());
			}
		}
		for (CompiledUnit embedded : document.getEmbedded()) {
			String name = embedded.getManifest() == null ? embedded.getId()
					: embedded.getManifest().getQualifiedName();
			String recorded = pinned.get(name);
			if (recorded != null && embedded.getManifest() != null
					&& !recorded.equals(embedded.getManifest().getUnitFingerprint())) {
				findings.add("the embedded unit '" + name + "' carries fingerprint "
						+ embedded.getManifest().getUnitFingerprint() + " but the manifest records "
						+ recorded + " — a different unit was embedded than the one recorded");
			}
			for (String finding : validate(embedded)) {
				findings.add("in the embedded unit '" + name + "': " + finding);
			}
		}
	}

	private static String path(CompiledUnit document, EObject object) {
		String fragment = EcoreUtil.getRelativeURIFragmentPath(document, object);
		return fragment.isEmpty() ? "the document root" : fragment;
	}
}
