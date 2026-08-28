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
package org.eclipse.fennec.m2x.unit.api;

import java.util.Set;

import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;

import org.osgi.annotation.versioning.ProviderType;

/**
 * Computes reproducible fingerprints of units.
 *
 * <p>The contract follows the one of {@code FingerprintService} in
 * {@code emf.osgi}: a value is {@code <scheme>:<digest>}, the scheme tag versions
 * the canonicalization algorithm, and values with different tags are not
 * comparable. Package fingerprints stay with that service and its {@code fp1}
 * scheme; unit fingerprints come from here, and a manifest carries both (§5.4 of
 * the compiled-unit concept).
 *
 * <p>m2x needs its own mechanism because {@code fp1} canonicalizes the Ecore
 * metastructure only. All three unit types inherit from {@code EPackage}, so the
 * service runs on them and returns a value that reflects their classifiers but
 * neither mappings, nor bodies, nor templates — two transformations with the same
 * intermediate classes and entirely different logic would share a value. That is
 * the silent inconsistency this mechanism exists to prevent.
 *
 * <p><b>What a value must not depend on:</b> source positions, comments,
 * {@code documentation} annotations and the parser's satellite container. A
 * reformatted source yields the same value. What it does depend on: names,
 * structure, operand and statement order, type references as {@code nsURI#name},
 * and the fingerprints of the unit's dependencies, Merkle-style.
 *
 * <p>A {@link Unit.Source} is fingerprinted over its text, a {@link Unit.Compiled}
 * over its canonicalized AST — a source has no AST before it is parsed, which is
 * why the two kinds carry different values for the same unit.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface UnitFingerprintService {

	/**
	 * Returns the scheme new fingerprints are computed in.
	 *
	 * @return the scheme tag, e.g. {@code m2x1}, never {@code null}
	 */
	String currentScheme();

	/**
	 * Returns every scheme this service can compute, so that a value written by an
	 * older version stays checkable after a bump.
	 *
	 * @return the supported scheme tags, never {@code null}
	 */
	Set<String> supportedSchemes();

	/**
	 * Computes the fingerprint of a unit in the current scheme.
	 *
	 * @param unit the unit to fingerprint
	 * @return the value in the form {@code <scheme>:<digest>}, never {@code null}
	 */
	String fingerprint(Unit unit);

	/**
	 * Computes the fingerprint of a unit in a specific scheme.
	 *
	 * @param unit the unit to fingerprint
	 * @param scheme one of {@link #supportedSchemes()}
	 * @return the value in the form {@code <scheme>:<digest>}, never {@code null}
	 * @throws IllegalArgumentException if the scheme is not supported
	 */
	String fingerprintInScheme(Unit unit, String scheme);

	/**
	 * Computes the fingerprint of a compiled unit in the current scheme: its script, with the
	 * fingerprints of its dependencies folded in Merkle-style. This is the value the manifest
	 * carries as {@code unitFingerprint}.
	 *
	 * @param compiled the compiled unit
	 * @return the value in the form {@code <scheme>:<digest>}, never {@code null}
	 */
	String fingerprint(CompiledUnit compiled);
}
