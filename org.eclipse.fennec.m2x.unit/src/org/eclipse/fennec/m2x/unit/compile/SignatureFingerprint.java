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

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Collection;
import java.util.HexFormat;
import java.util.Objects;
import java.util.TreeSet;

/**
 * The fingerprint of a blackbox signature set, recorded in a
 * {@link org.eclipse.fennec.m2x.model.compiled.BlackboxRequirement}.
 *
 * <p>A blackbox implementation stays out of a compiled unit; what goes in is the shape the unit
 * was compiled against, so that a provider with the right name but the wrong operations is
 * caught when the unit is loaded, not at the call. The shape is a set of signature lines — one
 * per operation, in whatever textual form the language chooses, as long as it is the same at
 * compile and at load time. The lines are sorted, so declaration order does not matter, and
 * hashed with SHA-256 under the {@code m2x1} tag like every other unit fingerprint.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class SignatureFingerprint {

	private static final String SCHEME = "m2x1";

	private SignatureFingerprint() {
	}

	/**
	 * Computes the fingerprint of a set of signature lines.
	 *
	 * @param signatures the signature lines, one per operation
	 * @return the fingerprint in the form {@code m2x1:<digest>}, never {@code null}
	 */
	public static String of(Collection<String> signatures) {
		Objects.requireNonNull(signatures, "signatures must not be null");
		StringBuilder form = new StringBuilder("signatures\n");
		for (String line : new TreeSet<>(signatures)) {
			form.append(line).append('\n');
		}
		try {
			MessageDigest digest = MessageDigest.getInstance("SHA-256");
			return SCHEME + ":" + HexFormat.of()
					.formatHex(digest.digest(form.toString().getBytes(StandardCharsets.UTF_8)));
		} catch (NoSuchAlgorithmException e) {
			throw new IllegalStateException("SHA-256 is not available", e);
		}
	}
}
