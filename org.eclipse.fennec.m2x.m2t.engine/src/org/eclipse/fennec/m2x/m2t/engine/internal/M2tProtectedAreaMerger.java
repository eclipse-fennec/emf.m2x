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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HexFormat;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Merges protected areas between newly generated content and existing file content.
 *
 * <p>Protected areas are delimited by markers:
 * <pre>
 * // [protected 'uniqueId']
 * ... user code preserved during re-generation ...
 * // [/protected]
 * </pre>
 *
 * <p>During re-generation, the merger:
 * <ol>
 *   <li>Extracts protected areas from the existing file (user-modified content)</li>
 *   <li>Replaces the corresponding areas in the new content with the preserved user content</li>
 *   <li>New protected areas not present in the existing file get their default content</li>
 * </ol>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tProtectedAreaMerger {

	private static final String START_PREFIX = "// [protected '";
	private static final String END_MARKER = "// [/protected]";
	private static final int HASH_LENGTH = 8; // 4 bytes = 8 hex chars

	/** Matches markers with optional hash: // [protected 'id'] or // [protected 'id' hexhash] */
	private static final Pattern PROTECTED_PATTERN = Pattern.compile(
			"// \\[protected '([^']+)'(?: ([0-9a-f]+))?\\]\\r?\\n(.*?)// \\[/protected\\]",
			Pattern.DOTALL);

	/**
	 * Merges protected areas from existing content into newly generated content.
	 *
	 * <p>Uses hash-based smart merge (D31): if the existing content's body still
	 * matches the hash recorded in the existing marker, the content was never
	 * user-modified and the new template default replaces it. If the hash does
	 * not match (user edited the content), the existing content is preserved.
	 * Legacy markers without hash always preserve existing content.
	 *
	 * @param newContent the newly generated content
	 * @param existingContent the existing file content (may be {@code null})
	 * @return the merged content
	 */
	public String merge(String newContent, String existingContent) {
		if (existingContent == null || existingContent.isEmpty()) {
			return newContent;
		}

		// Extract protected areas from existing content (markerId → AreaInfo)
		Map<String, AreaInfo> existingAreas = extractProtectedAreas(existingContent);
		if (existingAreas.isEmpty()) {
			return newContent;
		}

		// Replace protected areas in new content based on hash comparison
		StringBuilder result = new StringBuilder();
		Matcher matcher = PROTECTED_PATTERN.matcher(newContent);
		int lastEnd = 0;

		while (matcher.find()) {
			String markerId = matcher.group(1);
			result.append(newContent, lastEnd, matcher.start());

			AreaInfo existing = existingAreas.get(markerId);
			if (existing != null && existing.isUserModified()) {
				// User modified the content — preserve it, but use the new hash
				String newHash = matcher.group(2);
				result.append(startMarkerLine(markerId, newHash)).append('\n');
				result.append(existing.content);
				result.append(END_MARKER);
			} else {
				// No existing content, or content unchanged (hash matches) — use new default
				result.append(matcher.group());
			}
			lastEnd = matcher.end();
		}
		result.append(newContent, lastEnd, newContent.length());

		return result.toString();
	}

	/**
	 * Extracts all protected areas from content into a map (markerId → AreaInfo).
	 */
	private Map<String, AreaInfo> extractProtectedAreas(String content) {
		Map<String, AreaInfo> areas = new LinkedHashMap<>();
		Matcher matcher = PROTECTED_PATTERN.matcher(content);
		while (matcher.find()) {
			String markerId = matcher.group(1);
			String hash = matcher.group(2); // may be null for legacy markers
			String body = matcher.group(3);
			areas.put(markerId, new AreaInfo(body, hash));
		}
		return areas;
	}

	/** Info about an extracted protected area. */
	private record AreaInfo(String content, String hash) {

		/**
		 * Returns {@code true} if the content was user-modified.
		 * Legacy markers (no hash) are always treated as user-modified to be safe.
		 */
		boolean isUserModified() {
			if (hash == null) {
				// Legacy marker without hash — always preserve (safe default)
				return true;
			}
			// Compare actual content hash against the recorded hash
			return !hash.equals(contentHash(content));
		}
	}

	/**
	 * Generates a protected area start marker without hash (legacy format).
	 *
	 * @param markerId the unique marker identifier
	 * @return the start marker line
	 */
	public static String startMarker(String markerId) {
		return START_PREFIX + markerId + "']";
	}

	/**
	 * Generates a protected area start marker with hash of default content (D31).
	 *
	 * @param markerId the unique marker identifier
	 * @param defaultContent the default body content to hash
	 * @return the start marker line including hash
	 */
	public static String startMarker(String markerId, String defaultContent) {
		String hash = contentHash(defaultContent);
		return startMarkerLine(markerId, hash);
	}

	/**
	 * Builds the start marker line from id and pre-computed hash.
	 */
	private static String startMarkerLine(String markerId, String hash) {
		if (hash == null) {
			return START_PREFIX + markerId + "']";
		}
		return START_PREFIX + markerId + "' " + hash + "]";
	}

	/**
	 * Returns the protected area end marker.
	 */
	public static String endMarker() {
		return END_MARKER;
	}

	/**
	 * Computes a short hex hash (CRC32-like) of the given content.
	 * Uses the first 4 bytes of SHA-256 for collision resistance.
	 *
	 * @param content the content to hash
	 * @return 8-character hex hash string
	 */
	public static String contentHash(String content) {
		try {
			MessageDigest md = MessageDigest.getInstance("SHA-256");
			byte[] digest = md.digest(content.getBytes(StandardCharsets.UTF_8));
			// Use first 4 bytes → 8 hex chars
			byte[] prefix = new byte[HASH_LENGTH / 2];
			System.arraycopy(digest, 0, prefix, 0, prefix.length);
			return HexFormat.of().formatHex(prefix);
		} catch (NoSuchAlgorithmException e) {
			throw new AssertionError("SHA-256 not available", e);
		}
	}
}
