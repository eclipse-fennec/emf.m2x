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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.CompleteOclContribution;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Registering a Complete OCL document puts it into effect (#185).
 *
 * <p>A Complete OCL document adds constraints and {@code def:} operations to a metamodel
 * without touching the {@code .ecore} (OCL v2.4 §12) — which is what lets a bundle ship rules
 * for a model it does not own. {@code registerCompleteOclDocument} is the programmatic half of
 * that extension point, and it used to add the contribution to a list nobody read: the
 * document had no effect and the caller was told nothing.
 */
class OclCompleteOclContributionTest extends AbstractOclTest {

	private CompleteOclContribution registered;

	@AfterEach
	void unregister() {
		if (registered != null) {
			engine.unregisterCompleteOclDocument(registered);
			registered = null;
		}
	}

	@Test
	@DisplayName("a registered document's def: operation can be called")
	void aRegisteredDefinitionIsCallable() throws OclParseException {
		EObject person = createPerson("Alice", 30, 50000.0, false);
		registered = contribution("""
				package company
				context Person
				  def: isAdult() : Boolean = self.age >= 18
				endpackage
				""");

		engine.registerCompleteOclDocument(registered);

		assertEquals(true, eval("self.isAdult()", person));
	}

	@Test
	@DisplayName("unregistering takes it out again")
	void unregisteringRemovesTheDefinition() throws OclParseException {
		EObject person = createPerson("Alice", 30, 50000.0, false);
		CompleteOclContribution contribution = contribution("""
				package company
				context Person
				  def: isAdult() : Boolean = self.age >= 18
				endpackage
				""");
		engine.registerCompleteOclDocument(contribution);
		assertEquals(true, eval("self.isAdult()", person), "registered, so it is there");

		engine.unregisterCompleteOclDocument(contribution);

		assertSame(OclInvalid.INSTANCE, eval("self.isAdult()", person),
				"and gone once it is unregistered — an operation nothing defines is invalid");
	}

	@Test
	@DisplayName("a document that cannot be parsed is refused, loudly")
	void anUnparseableDocumentIsRefused() {
		CompleteOclContribution broken = contribution("context Person inv : self.age >=");

		IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
				() -> engine.registerCompleteOclDocument(broken));

		assertTrue(failure.getMessage().contains("cannot be parsed"), failure::getMessage);
		assertTrue(failure.getCause() instanceof OclParseException, String.valueOf(failure.getCause()));
	}

	@Test
	@DisplayName("registering the same contribution twice leaves one registration")
	void registeringTwiceIsIdempotent() throws OclParseException {
		EObject person = createPerson("Alice", 30, 50000.0, false);
		registered = contribution("""
				package company
				context Person
				  def: isAdult() : Boolean = self.age >= 18
				endpackage
				""");

		engine.registerCompleteOclDocument(registered);
		engine.registerCompleteOclDocument(registered);
		engine.unregisterCompleteOclDocument(registered);
		registered = null;

		assertSame(OclInvalid.INSTANCE, eval("self.isAdult()", person),
				"one unregister has to be enough, or a contribution could never be removed");
	}

	private static CompleteOclContribution contribution(String text) {
		return new CompleteOclContribution() {

			@Override
			public URI getDocumentUri() {
				return URI.createURI("mem:/test.ocl");
			}

			@Override
			public String getDocumentText() {
				return text;
			}
		};
	}
}
