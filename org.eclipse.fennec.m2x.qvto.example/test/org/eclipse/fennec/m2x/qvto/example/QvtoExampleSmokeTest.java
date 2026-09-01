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
package org.eclipse.fennec.m2x.qvto.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.Test;

/**
 * Keeps the example honest: both mains run against the real files and produce the same
 * catalog — two lendable books, ordered by year, the zero-copies manuscript dropped by the
 * mapping's when-guard.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoExampleSmokeTest {

	@Test
	void theAdHocExample_runs() throws Exception {
		assertCatalog(AdHocExample.run());
	}

	@Test
	void theCompiledUnitExample_runs() throws Exception {
		assertCatalog(CompiledUnitExample.run());
	}

	private static void assertCatalog(EObject catalog) {
		assertEquals("City Library", catalog.eGet(catalog.eClass().getEStructuralFeature("title")));
		@SuppressWarnings("unchecked")
		List<EObject> entries = (List<EObject>) catalog.eGet(catalog.eClass().getEStructuralFeature("entries"));
		assertEquals(2, entries.size(), "the zero-copies manuscript is dropped by the when-guard");
		assertEquals("Melville: Moby Dick", label(entries.get(0)), "ordered by year: 1851 first");
		assertEquals("Abelson: Structure and Interpretation of Computer Programs", label(entries.get(1)));
	}

	private static String label(EObject entry) {
		return (String) entry.eGet(entry.eClass().getEStructuralFeature("label"));
	}
}
