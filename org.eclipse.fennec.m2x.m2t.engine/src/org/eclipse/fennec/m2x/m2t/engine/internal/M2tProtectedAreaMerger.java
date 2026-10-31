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
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.fennec.m2x.m2t.api.ProtectedAreaDelimiter;

/**
 * Merges protected areas between newly generated content and existing file content.
 *
 * <p>Protected areas are delimited by markers in the comment syntax of the target language
 * (MOFM2T §7.2, #367) — with the default {@code //}:
 * <pre>
 * // [protected 'uniqueId' hash]
 * ... user code preserved during re-generation ...
 * // [/protected]
 * </pre>
 *
 * <p>During re-generation, the merger:
 * <ol>
 *   <li>Extracts protected areas from the existing file (user-modified content)</li>
 *   <li>Replaces the corresponding areas in the new content with the preserved user content</li>
 *   <li>New protected areas not present in the existing file get their default content</li>
 *   <li>Reports the user-modified areas the new content no longer has, so that their code
 *       is not lost silently, and the ids the existing file holds twice, which it does not
 *       merge</li>
 * </ol>
 *
 * <p>An id is written with {@code \} and {@code '} escaped by a backslash, so an id with a
 * quote survives the round trip.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tProtectedAreaMerger {

	private static final int HASH_LENGTH = 8; // 4 bytes = 8 hex chars

	private final ProtectedAreaDelimiter delimiter;
	private final Pattern pattern;

	/**
	 * What a merge produced.
	 *
	 * @param content the merged content
	 * @param lost the user-modified areas of the existing file the new content no longer has,
	 *            by id
	 * @param duplicateIds the ids the existing file holds more than once; they were not merged
	 */
	public record Result(String content, Map<String, String> lost, Set<String> duplicateIds) {
	}

	/** A merger for markers in the default {@code //} syntax. */
	public M2tProtectedAreaMerger() {
		this(ProtectedAreaDelimiter.LINE_SLASHES);
	}

	/**
	 * @param delimiter the comment syntax the markers are written in
	 */
	public M2tProtectedAreaMerger(ProtectedAreaDelimiter delimiter) {
		this.delimiter = Objects.requireNonNull(delimiter, "delimiter must not be null");
		String suffix = delimiter.suffix().isEmpty() ? "" : " " + Pattern.quote(delimiter.suffix());
		this.pattern = Pattern.compile(Pattern.quote(delimiter.prefix())
				+ " \\[protected '((?:[^'\\\\\\r\\n]|\\\\.)+)'(?: ([0-9a-f]+))?\\]" + suffix + "\\r?\\n(.*?)"
				+ Pattern.quote(delimiter.prefix()) + " \\[/protected\\]" + suffix,
				Pattern.DOTALL);
	}

	/**
	 * Merges protected areas from existing content into newly generated content.
	 *
	 * @param newContent the newly generated content
	 * @param existingContent the existing file content (may be {@code null})
	 * @return the merged content
	 * @see #mergeReporting(String, String)
	 */
	public String merge(String newContent, String existingContent) {
		return mergeReporting(newContent, existingContent).content();
	}

	/**
	 * Merges protected areas from existing content into newly generated content, and reports
	 * what could not be merged.
	 *
	 * <p>Uses hash-based smart merge (D31): if the existing content's body still
	 * matches the hash recorded in the existing marker, the content was never
	 * user-modified and the new template default replaces it. If the hash does
	 * not match (user edited the content), the existing content is preserved.
	 * Legacy markers without hash always preserve existing content.
	 *
	 * @param newContent the newly generated content
	 * @param existingContent the existing file content (may be {@code null})
	 * @return the merged content, the lost areas and the duplicate ids
	 */
	public Result mergeReporting(String newContent, String existingContent) {
		Objects.requireNonNull(newContent, "newContent must not be null");
		if (existingContent == null || existingContent.isEmpty()) {
			return new Result(newContent, Map.of(), Set.of());
		}

		Set<String> duplicateIds = new LinkedHashSet<>();
		Map<String, AreaInfo> existingAreas = extractProtectedAreas(existingContent, duplicateIds);
		if (existingAreas.isEmpty()) {
			return new Result(newContent, Map.of(), Set.copyOf(duplicateIds));
		}

		// Replace protected areas in new content based on hash comparison
		StringBuilder result = new StringBuilder();
		Matcher matcher = pattern.matcher(newContent);
		int lastEnd = 0;
		Set<String> generatedIds = new LinkedHashSet<>();

		while (matcher.find()) {
			String markerId = unescape(matcher.group(1));
			generatedIds.add(markerId);
			result.append(newContent, lastEnd, matcher.start());

			AreaInfo existing = duplicateIds.contains(markerId) ? null : existingAreas.get(markerId);
			if (existing != null && existing.isUserModified()) {
				// User modified the content — preserve it, but use the new hash
				result.append(delimiter.startMarker(escape(markerId), matcher.group(2))).append('\n');
				result.append(existing.content);
				result.append(delimiter.endMarker());
			} else {
				// No existing content, or content unchanged (hash matches) — use new default
				result.append(matcher.group());
			}
			lastEnd = matcher.end();
		}
		result.append(newContent, lastEnd, newContent.length());

		// §7.2: protected text parts "are preserved and not overwritten" — one whose area is no
		// longer generated goes back to the caller rather than vanish; an untouched default
		// may go (#367)
		Map<String, String> lost = new LinkedHashMap<>();
		existingAreas.forEach((id, area) -> {
			if (!generatedIds.contains(id) && area.isUserModified()) {
				lost.put(id, area.content);
			}
		});
		return new Result(result.toString(), lost, Set.copyOf(duplicateIds));
	}

	/**
	 * Extracts all protected areas from content into a map (markerId → AreaInfo), and the ids
	 * found more than once.
	 */
	private Map<String, AreaInfo> extractProtectedAreas(String content, Set<String> duplicateIds) {
		Map<String, AreaInfo> areas = new LinkedHashMap<>();
		Matcher matcher = pattern.matcher(content);
		while (matcher.find()) {
			String markerId = unescape(matcher.group(1));
			String hash = matcher.group(2); // may be null for legacy markers
			String body = matcher.group(3);
			if (areas.putIfAbsent(markerId, new AreaInfo(body, hash)) != null) {
				duplicateIds.add(markerId);
			}
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
	 * The start marker of an area, in this merger's comment syntax.
	 *
	 * @param markerId the area's id, unescaped
	 * @param body the area's default body, exactly as it is written between the markers
	 * @return the start marker line, without line break
	 */
	public String startMarkerFor(String markerId, String body) {
		Objects.requireNonNull(markerId, "markerId must not be null");
		Objects.requireNonNull(body, "body must not be null");
		return delimiter.startMarker(escape(markerId), contentHash(body));
	}

	/** @return the end marker line in this merger's comment syntax, without line break */
	public String endMarkerLine() {
		return delimiter.endMarker();
	}

	/**
	 * Generates a protected area start marker without hash (legacy format).
	 *
	 * @param markerId the unique marker identifier
	 * @return the start marker line
	 */
	public static String startMarker(String markerId) {
		return ProtectedAreaDelimiter.LINE_SLASHES.startMarker(escape(markerId), null);
	}

	/**
	 * Generates a protected area start marker with hash of default content (D31).
	 *
	 * @param markerId the unique marker identifier
	 * @param defaultContent the default body content to hash
	 * @return the start marker line including hash
	 */
	public static String startMarker(String markerId, String defaultContent) {
		return new M2tProtectedAreaMerger().startMarkerFor(markerId, defaultContent);
	}

	/**
	 * Returns the protected area end marker.
	 */
	public static String endMarker() {
		return ProtectedAreaDelimiter.LINE_SLASHES.endMarker();
	}

	/** An id as written into a marker: {@code \} and {@code '} escaped by a backslash. */
	static String escape(String id) {
		return id.replace("\\", "\\\\").replace("'", "\\'");
	}

	private static String unescape(String written) {
		StringBuilder id = new StringBuilder(written.length());
		for (int i = 0; i < written.length(); i++) {
			char c = written.charAt(i);
			if (c == '\\' && i + 1 < written.length()) {
				c = written.charAt(++i);
			}
			id.append(c);
		}
		return id.toString();
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
