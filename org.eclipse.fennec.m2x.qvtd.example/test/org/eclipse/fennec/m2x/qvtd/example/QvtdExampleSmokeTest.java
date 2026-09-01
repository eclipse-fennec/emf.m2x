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
package org.eclipse.fennec.m2x.qvtd.example;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.junit.jupiter.api.Test;

/**
 * Keeps the example honest: both mains run against the real files and produce the same
 * inventory — two lendable books as records, the zero-copies manuscript dropped by the
 * when-guard's query.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdExampleSmokeTest {

	@Test
	void theAdHocExample_runs() throws Exception {
		assertInventory(AdHocExample.run());
	}

	@Test
	void theCompiledUnitExample_runs() throws Exception {
		assertInventory(CompiledUnitExample.run());
	}

	private static void assertInventory(EObject inventory) {
		assertEquals("City Library", inventory.eGet(inventory.eClass().getEStructuralFeature("name")));
		@SuppressWarnings("unchecked")
		List<EObject> records = (List<EObject>) inventory.eGet(inventory.eClass().getEStructuralFeature("records"));
		assertEquals(2, records.size(), "the zero-copies manuscript is dropped by the when-guard");
	}
}
