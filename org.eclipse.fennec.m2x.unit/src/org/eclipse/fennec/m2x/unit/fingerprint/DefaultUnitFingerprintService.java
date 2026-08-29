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
package org.eclipse.fennec.m2x.unit.fingerprint;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.Objects;
import java.util.Set;
import java.util.TreeMap;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;

/**
 * The m2x fingerprint mechanism for units (#138).
 *
 * <p>Same contract as {@code FingerprintService} in {@code emf.osgi}: a value is
 * {@code <scheme>:<digest>}, the scheme tag versions the canonicalization, values of different
 * schemes are not comparable. Packages keep their {@code fp1} values from that service; units get
 * {@code m2x1} from here, and a manifest carries both. {@code fp1} could not serve for units: it
 * canonicalizes the Ecore metastructure only, and all three unit roots inherit from
 * {@code EPackage}, so two transformations with the same intermediate classes and entirely
 * different logic would share a value.
 *
 * <p>Two kinds of value, because a source has no AST before it is parsed:
 * <ul>
 *   <li>a {@link Unit.Source} is fingerprinted over its text, with line endings normalized to
 *       LF so that a Windows checkout agrees with a Linux one;</li>
 *   <li>a {@link Unit.Compiled} over the canonical form of its AST ({@link AstCanonicalizer}) —
 *       a reformatted source yields the same value, a reordered statement does not.</li>
 * </ul>
 *
 * <p>A {@link CompiledUnit} folds the fingerprints of its dependencies in, Merkle-style: the
 * value stands for the unit <em>and</em> what it was compiled against under embed and pin. Under
 * rebind the dependency entries carry no fingerprint and the value describes the unit alone —
 * which is why prepare records what it actually bound (#139, #140).
 *
 * <p>One scheme tag for all four languages: they nest — every unit embeds OCL — so a change to
 * the OCL rules cascades anyway, and one tag is easier to administer than four.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class DefaultUnitFingerprintService implements UnitFingerprintService {

	/** The one scheme so far. Changing what m2x1 hashes means a new tag, not an edit. */
	public static final String SCHEME = "m2x1";

	/** A shared instance; the service holds no state. */
	public static final DefaultUnitFingerprintService INSTANCE = new DefaultUnitFingerprintService();

	@Override
	public String currentScheme() {
		return SCHEME;
	}

	@Override
	public Set<String> supportedSchemes() {
		return Set.of(SCHEME);
	}

	@Override
	public String fingerprint(Unit unit) {
		return fingerprintInScheme(unit, SCHEME);
	}

	@Override
	public String fingerprintInScheme(Unit unit, String scheme) {
		Objects.requireNonNull(unit, "unit must not be null");
		requireScheme(scheme);
		if (unit instanceof Unit.Source source) {
			return value(sourceCanonicalForm(source.source()));
		}
		if (unit instanceof Unit.Compiled compiled) {
			return value(astCanonicalForm(compiled.root()));
		}
		throw new IllegalArgumentException("a unit is either Source or Compiled: "
				+ unit.getClass().getName());
	}

	@Override
	public String fingerprint(CompiledUnit compiled) {
		Objects.requireNonNull(compiled, "compiled must not be null");
		Objects.requireNonNull(compiled.getUnit(), "the compiled unit carries no script");
		StringBuilder form = new StringBuilder(astCanonicalForm(compiled.getUnit()));
		// Dependencies in a fixed order, so the value does not depend on how the entries were
		// listed; a dependency without a fingerprint (rebind) contributes its name alone.
		TreeMap<String, String> dependencies = new TreeMap<>();
		if (compiled.getManifest() != null) {
			for (DependencyEntry entry : compiled.getManifest().getDependencyEntry()) {
				dependencies.put(String.valueOf(entry.getQualifiedName()),
						entry.getFingerprint() == null ? "" : entry.getFingerprint());
			}
		}
		if (!dependencies.isEmpty()) {
			form.append("\ndependencies\n");
			dependencies.forEach((name, fingerprint) ->
					form.append(name).append('=').append(fingerprint).append('\n'));
		}
		// What the document carries is deliberately not folded in here: a metamodel copy and an
		// embedded unit each already have a recorded identity — the PackageEntry's fp1 value and
		// the DependencyEntry's m2x1 value — and UnitValidator holds them to it (#183). Folding
		// them into this value as well would make a compiled document differ from the very AST it
		// was built from, which is a property the round-trip tests rely on.
		return value(form.toString());
	}

	/**
	 * The canonical form a fingerprint is computed over — what to compare when two values that
	 * should agree do not. The fingerprint alone says "different"; this says where.
	 *
	 * @param unit the unit
	 * @return the canonical text of the current scheme, never {@code null}
	 */
	public String canonicalForm(Unit unit) {
		Objects.requireNonNull(unit, "unit must not be null");
		if (unit instanceof Unit.Source source) {
			return sourceCanonicalForm(source.source());
		}
		if (unit instanceof Unit.Compiled compiled) {
			return astCanonicalForm(compiled.root());
		}
		throw new IllegalArgumentException("a unit is either Source or Compiled");
	}

	/**
	 * The canonical form of a source: its text with every line ending as LF. Nothing else is
	 * normalized — a source fingerprint says "this text", the AST fingerprint says "this
	 * program".
	 */
	static String sourceCanonicalForm(String source) {
		return "source\n" + source.replace("\r\n", "\n").replace('\r', '\n');
	}

	static String astCanonicalForm(EObject root) {
		return "ast\n" + AstCanonicalizer.canonicalize(root);
	}

	private static String value(String canonicalForm) {
		return SCHEME + ":" + sha256(canonicalForm);
	}

	private static String sha256(String text) {
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return HexFormat.of().formatHex(digest.digest(text.getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			// SHA-256 is mandated on every JVM
			throw new IllegalStateException("SHA-256 is not available", e);
		}
	}

	private static void requireScheme(String scheme) {
		if (!SCHEME.equals(scheme)) {
			throw new IllegalArgumentException("unsupported fingerprint scheme '" + scheme
					+ "'; supported: " + SCHEME);
		}
	}
}
