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
package org.eclipse.fennec.m2x.m2t.api;

import java.util.Objects;

/**
 * The comment syntax a protected area's markers are written in.
 *
 * <p>MOFM2T §7.2: "It produces delimiters in the output text to clearly identify the protected
 * part. Since such delimiters are specific to a target language, they are not defined in this
 * standard. An implementation tool is responsible for producing the correct delimiters for the
 * desired target language." A marker is {@code prefix [protected 'id' hash] suffix} and
 * {@code prefix [/protected] suffix}; which delimiter a file gets is configured per file
 * extension in {@link M2tConfiguration} (#367).
 *
 * @param prefix what opens the comment, such as {@code //} or {@code <!--}; never empty
 * @param suffix what closes it, such as {@code -->}; empty for a line comment
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record ProtectedAreaDelimiter(String prefix, String suffix) {

	/** {@code //}, the default for a file without a configured extension. */
	public static final ProtectedAreaDelimiter LINE_SLASHES = new ProtectedAreaDelimiter("//", "");

	/** {@code <!-- … -->}, for XML and HTML. */
	public static final ProtectedAreaDelimiter XML = new ProtectedAreaDelimiter("<!--", "-->");

	/** {@code #}, for Python, shell scripts and properties. */
	public static final ProtectedAreaDelimiter HASH = new ProtectedAreaDelimiter("#", "");

	/** {@code --}, for SQL. */
	public static final ProtectedAreaDelimiter DOUBLE_DASH = new ProtectedAreaDelimiter("--", "");

	public ProtectedAreaDelimiter {
		Objects.requireNonNull(prefix, "prefix must not be null");
		suffix = suffix == null ? "" : suffix;
		if (prefix.isBlank()) {
			throw new IllegalArgumentException("prefix must not be blank");
		}
		if (prefix.contains("\n") || suffix.contains("\n")) {
			throw new IllegalArgumentException("a delimiter is on the marker's line");
		}
	}

	/**
	 * @param id the area's id, already escaped
	 * @param hash the hash of the area's default content, or {@code null}
	 * @return the start marker line, without line break
	 */
	public String startMarker(String id, String hash) {
		return wrap("[protected '" + id + "'" + (hash == null ? "" : " " + hash) + "]");
	}

	/** @return the end marker line, without line break */
	public String endMarker() {
		return wrap("[/protected]");
	}

	private String wrap(String marker) {
		return prefix + " " + marker + (suffix.isEmpty() ? "" : " " + suffix);
	}
}
