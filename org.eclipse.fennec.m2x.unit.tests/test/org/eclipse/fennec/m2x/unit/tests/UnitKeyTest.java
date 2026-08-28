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
package org.eclipse.fennec.m2x.unit.tests;

import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

/**
 * {@link UnitKey} tells sources and compiled units apart, and a pinned version
 * from the current one.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitKeyTest {

	@Test
	void typeId_foldsLanguageAndKind() {
		assertEquals("qvto-source", UnitKey.of("qvto", "lib.Strings", UnitKind.SOURCE).typeId());
		assertEquals("qvto-compiled", UnitKey.of("qvto", "lib.Strings", UnitKind.COMPILED).typeId());
		assertEquals("qvtr-compiled", UnitKey.of("qvtr", "uml.ToRdbms", UnitKind.COMPILED).typeId());
		assertEquals("m2t-source", UnitKey.of("m2t", "gen.Java", UnitKind.SOURCE).typeId());
	}

	@Test
	void of_asksForWhateverTheStoreHolds() {
		UnitKey key = UnitKey.of("qvto", "lib.Strings", UnitKind.COMPILED);
		assertEquals(Optional.empty(), key.fingerprint());
	}

	@Test
	void pinned_asksForOneVersion() {
		UnitKey key = UnitKey.pinned("qvto", "lib.Strings", UnitKind.COMPILED, "m2x1:abc");
		assertEquals(Optional.of("m2x1:abc"), key.fingerprint());
	}

	@Test
	void sameNameDifferentKind_areDifferentKeys() {
		assertNotEquals(UnitKey.of("qvto", "lib.Strings", UnitKind.SOURCE),
				UnitKey.of("qvto", "lib.Strings", UnitKind.COMPILED));
	}

	@Test
	void sameNameDifferentLanguage_areDifferentKeys() {
		assertNotEquals(UnitKey.of("qvto", "shared.Lib", UnitKind.COMPILED),
				UnitKey.of("qvtr", "shared.Lib", UnitKind.COMPILED));
	}

	@Test
	void pinnedAndCurrent_areDifferentKeys() {
		assertNotEquals(UnitKey.of("qvto", "lib.Strings", UnitKind.COMPILED),
				UnitKey.pinned("qvto", "lib.Strings", UnitKind.COMPILED, "m2x1:abc"));
	}

	@Test
	void equalKeys_areEqualAndHashAlike() {
		UnitKey one = UnitKey.pinned("qvto", "lib.Strings", UnitKind.COMPILED, "m2x1:abc");
		UnitKey other = UnitKey.pinned("qvto", "lib.Strings", UnitKind.COMPILED, "m2x1:abc");
		assertEquals(one, other);
		assertEquals(one.hashCode(), other.hashCode());
	}

	@Test
	void nullArguments_areRejected() {
		assertThrows(NullPointerException.class,
				() -> UnitKey.of(null, "lib.Strings", UnitKind.COMPILED));
		assertThrows(NullPointerException.class,
				() -> UnitKey.of("qvto", null, UnitKind.COMPILED));
		assertThrows(NullPointerException.class,
				() -> UnitKey.of("qvto", "lib.Strings", null));
		assertThrows(NullPointerException.class,
				() -> UnitKey.pinned("qvto", "lib.Strings", UnitKind.COMPILED, null));
		assertThrows(NullPointerException.class,
				() -> new UnitKey("qvto", "lib.Strings", UnitKind.COMPILED, null));
	}

	@Test
	void kindTag_isTheLowerCaseName() {
		assertEquals("source", UnitKind.SOURCE.tag());
		assertEquals("compiled", UnitKind.COMPILED.tag());
		assertTrue(UnitKey.of("qvto", "x", UnitKind.SOURCE).typeId().endsWith(UnitKind.SOURCE.tag()));
	}
}
