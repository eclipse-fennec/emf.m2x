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
package org.eclipse.fennec.m2x.m2t.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.m2t.engine.internal.M2tProtectedAreaMerger;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link M2tProtectedAreaMerger} — D31 hash-based smart merge.
 *
 * <p>Verifies that:
 * <ul>
 *   <li>Start markers include a hash of the default content</li>
 *   <li>Unchanged default content (hash matches) is overwritten on re-generation</li>
 *   <li>User-modified content (hash mismatch) is preserved on re-generation</li>
 * </ul>
 */
class M2tProtectedAreaMergerTest {

	private M2tProtectedAreaMerger merger;

	@BeforeEach
	void setUp() {
		merger = new M2tProtectedAreaMerger();
	}

	@Nested
	@DisplayName("Marker format")
	class MarkerFormatTests {

		@Test
		@DisplayName("startMarker includes hash of default content (D31)")
		void startMarkerIncludesHash() {
			String defaultContent = "default content";
			String marker = M2tProtectedAreaMerger.startMarker("my-id", defaultContent);
			// Marker format: // [protected 'my-id' HEXHASH]
			assertTrue(marker.matches("// \\[protected 'my-id' [0-9a-f]+\\]"),
					"Start marker must include hex hash: " + marker);
		}

		@Test
		@DisplayName("same default content produces same hash")
		void sameContentSameHash() {
			String marker1 = M2tProtectedAreaMerger.startMarker("id1", "content");
			String marker2 = M2tProtectedAreaMerger.startMarker("id2", "content");
			// Extract hash from both markers
			String hash1 = marker1.replaceAll(".*'\\s+([0-9a-f]+)\\]", "$1");
			String hash2 = marker2.replaceAll(".*'\\s+([0-9a-f]+)\\]", "$1");
			assertEquals(hash1, hash2, "Same content must produce same hash");
		}

		@Test
		@DisplayName("different default content produces different hash")
		void differentContentDifferentHash() {
			String marker1 = M2tProtectedAreaMerger.startMarker("id", "content A");
			String marker2 = M2tProtectedAreaMerger.startMarker("id", "content B");
			String hash1 = marker1.replaceAll(".*'\\s+([0-9a-f]+)\\]", "$1");
			String hash2 = marker2.replaceAll(".*'\\s+([0-9a-f]+)\\]", "$1");
			assertTrue(!hash1.equals(hash2), "Different content must produce different hash");
		}

		@Test
		@DisplayName("legacy startMarker without hash still works")
		void legacyStartMarkerWithoutHash() {
			String marker = M2tProtectedAreaMerger.startMarker("my-id");
			assertEquals("// [protected 'my-id']", marker);
		}
	}

	@Nested
	@DisplayName("Smart merge (D31)")
	class SmartMergeTests {

		@Test
		@DisplayName("user-modified content is preserved on re-generation")
		void userModifiedContentPreserved() {
			String defaultContent = "default content\n";
			String hash = M2tProtectedAreaMerger.contentHash(defaultContent);

			// Existing file: user changed the protected area content
			String existing =
					"before\n" +
					"// [protected 'foo' " + hash + "]\n" +
					"user modified this\n" +
					"// [/protected]\n" +
					"after\n";

			// New generation: template produces new default
			String newDefault = "new default content\n";
			String newHash = M2tProtectedAreaMerger.contentHash(newDefault);
			String newContent =
					"before\n" +
					"// [protected 'foo' " + newHash + "]\n" +
					newDefault +
					"// [/protected]\n" +
					"after\n";

			String merged = merger.merge(newContent, existing);
			assertTrue(merged.contains("user modified this"),
					"User-modified content must be preserved");
			assertTrue(!merged.contains("new default content"),
					"New default must NOT overwrite user content");
		}

		@Test
		@DisplayName("unchanged default content is overwritten with new template default")
		void unchangedDefaultOverwritten() {
			String oldDefault = "old default\n";
			String oldHash = M2tProtectedAreaMerger.contentHash(oldDefault);

			// Existing file: protected area still has the original default (matches hash)
			String existing =
					"before\n" +
					"// [protected 'foo' " + oldHash + "]\n" +
					oldDefault +
					"// [/protected]\n" +
					"after\n";

			// New generation: template default changed
			String newDefault = "improved default\n";
			String newHash = M2tProtectedAreaMerger.contentHash(newDefault);
			String newContent =
					"before\n" +
					"// [protected 'foo' " + newHash + "]\n" +
					newDefault +
					"// [/protected]\n" +
					"after\n";

			String merged = merger.merge(newContent, existing);
			assertTrue(merged.contains("improved default"),
					"New template default must replace unchanged old default");
			assertTrue(!merged.contains("old default"),
					"Old default must be overwritten");
		}

		@Test
		@DisplayName("first generation (no existing file) keeps default content")
		void firstGenerationKeepsDefault() {
			String newContent =
					"// [protected 'foo' abc123]\n" +
					"default content\n" +
					"// [/protected]\n";

			String merged = merger.merge(newContent, null);
			assertEquals(newContent, merged, "First generation: no merge, keep as-is");
		}

		@Test
		@DisplayName("legacy markers without hash always preserve existing content")
		void legacyMarkersPreserveContent() {
			// Existing file with legacy markers (no hash)
			String existing =
					"// [protected 'foo']\n" +
					"user content\n" +
					"// [/protected]\n";

			// New generation also has no hash (legacy)
			String newContent =
					"// [protected 'foo']\n" +
					"new default\n" +
					"// [/protected]\n";

			String merged = merger.merge(newContent, existing);
			assertTrue(merged.contains("user content"),
					"Legacy markers (no hash): always preserve existing content");
		}

		@Test
		@DisplayName("multiple areas: modified preserved, unchanged overwritten")
		void multipleAreasMixedModification() {
			String defaultA = "default A\n";
			String hashA = M2tProtectedAreaMerger.contentHash(defaultA);
			String defaultB = "default B\n";
			String hashB = M2tProtectedAreaMerger.contentHash(defaultB);
			String defaultC = "default C\n";
			String hashC = M2tProtectedAreaMerger.contentHash(defaultC);

			// Existing: area 'a' user-modified, area 'b' unchanged, area 'c' user-modified
			String existing =
					"header\n" +
					"// [protected 'a' " + hashA + "]\n" +
					"user code A\n" +
					"// [/protected]\n" +
					"middle\n" +
					"// [protected 'b' " + hashB + "]\n" +
					defaultB +
					"// [/protected]\n" +
					"gap\n" +
					"// [protected 'c' " + hashC + "]\n" +
					"user code C\n" +
					"// [/protected]\n" +
					"footer\n";

			// New generation: all defaults changed
			String newA = "new default A\n";
			String newHashA = M2tProtectedAreaMerger.contentHash(newA);
			String newB = "new default B\n";
			String newHashB = M2tProtectedAreaMerger.contentHash(newB);
			String newC = "new default C\n";
			String newHashC = M2tProtectedAreaMerger.contentHash(newC);
			String newContent =
					"header\n" +
					"// [protected 'a' " + newHashA + "]\n" +
					newA +
					"// [/protected]\n" +
					"middle\n" +
					"// [protected 'b' " + newHashB + "]\n" +
					newB +
					"// [/protected]\n" +
					"gap\n" +
					"// [protected 'c' " + newHashC + "]\n" +
					newC +
					"// [/protected]\n" +
					"footer\n";

			String merged = merger.merge(newContent, existing);
			// Area 'a': user-modified → preserve
			assertTrue(merged.contains("user code A"), "Area 'a' user content must be preserved");
			assertFalse(merged.contains("new default A"), "Area 'a' new default must not appear");
			// Area 'b': unchanged → overwrite with new default
			assertTrue(merged.contains("new default B"), "Area 'b' must get new template default");
			// Area 'c': user-modified → preserve
			assertTrue(merged.contains("user code C"), "Area 'c' user content must be preserved");
			assertFalse(merged.contains("new default C"), "Area 'c' new default must not appear");
		}

		@Test
		@DisplayName("protected area removed from template — old area dropped")
		void areaRemovedFromTemplate() {
			String defaultContent = "default\n";
			String hash = M2tProtectedAreaMerger.contentHash(defaultContent);

			// Existing file has area 'old-area'
			String existing =
					"before\n" +
					"// [protected 'old-area' " + hash + "]\n" +
					"user content\n" +
					"// [/protected]\n" +
					"after\n";

			// New generation: no protected areas at all
			String newContent = "before\nafter\n";

			String merged = merger.merge(newContent, existing);
			assertEquals("before\nafter\n", merged,
					"Removed area must not appear in merged output");
		}

		@Test
		@DisplayName("new protected area added to template — gets default content")
		void newAreaAddedToTemplate() {
			String oldDefault = "old default\n";
			String oldHash = M2tProtectedAreaMerger.contentHash(oldDefault);

			// Existing file has only area 'existing'
			String existing =
					"header\n" +
					"// [protected 'existing' " + oldHash + "]\n" +
					"user content\n" +
					"// [/protected]\n" +
					"footer\n";

			// New generation: has 'existing' + new area 'brand-new'
			String newDefault = "new default\n";
			String newHash = M2tProtectedAreaMerger.contentHash(newDefault);
			String brandNewDefault = "brand new default\n";
			String brandNewHash = M2tProtectedAreaMerger.contentHash(brandNewDefault);
			String newContent =
					"header\n" +
					"// [protected 'existing' " + newHash + "]\n" +
					newDefault +
					"// [/protected]\n" +
					"// [protected 'brand-new' " + brandNewHash + "]\n" +
					brandNewDefault +
					"// [/protected]\n" +
					"footer\n";

			String merged = merger.merge(newContent, existing);
			// 'existing' was user-modified → preserve
			assertTrue(merged.contains("user content"),
					"Existing user-modified area must be preserved");
			// 'brand-new' not in existing → gets default
			assertTrue(merged.contains("brand new default"),
					"New area must get its default content");
		}

		@Test
		@DisplayName("existing content without protected areas — no merge needed")
		void existingContentWithoutAreas() {
			String newDefault = "default\n";
			String hash = M2tProtectedAreaMerger.contentHash(newDefault);
			String newContent =
					"header\n" +
					"// [protected 'foo' " + hash + "]\n" +
					newDefault +
					"// [/protected]\n" +
					"footer\n";

			// Existing file has no protected areas at all
			String existing = "some old content without markers\n";

			String merged = merger.merge(newContent, existing);
			assertEquals(newContent, merged,
					"No existing areas → use new content as-is");
		}
	}
}
