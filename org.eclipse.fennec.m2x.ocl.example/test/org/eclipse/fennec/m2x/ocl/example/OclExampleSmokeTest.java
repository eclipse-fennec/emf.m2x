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
package org.eclipse.fennec.m2x.ocl.example;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclDocumentValidator;
import org.eclipse.fennec.m2x.ocl.example.AdHocExample.Session;
import org.eclipse.fennec.m2x.ocl.example.DelegatesExample.Report;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Keeps the three example mains honest: the delegate annotations answer through EMF's own
 * machinery, and the Complete OCL document puts the same things into effect whether it is
 * loaded ad hoc or arrives as a compiled unit.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclExampleSmokeTest {

	@Test
	@DisplayName("the delegate annotations answer through Diagnostician, eGet and eInvoke")
	void theDelegatesExample_answersThroughEmf() throws Exception {
		Report report = DelegatesExample.run();

		assertEquals(Diagnostic.ERROR, report.validation().getSeverity(),
				"Codex Gigas (1229) violates validYear");
		assertTrue(flatten(report.validation()).contains("validYear"), () -> flatten(report.validation()));
		assertEquals(6, report.bookCount(), "eGet computes the derived bookCount");
		assertEquals(1, report.melvilleBooks(), "eInvoke runs the booksBy body");
	}

	@Test
	@DisplayName("the document loaded ad hoc puts def, derive, body and inv into effect")
	void theAdHocExample_putsTheDocumentIntoEffect() throws Exception {
		assertDocumentApplies(AdHocExample.run());
	}

	@Test
	@DisplayName("the document as a compiled unit puts the same things into effect")
	void theCompiledUnitExample_putsTheDocumentIntoEffect() throws Exception {
		assertDocumentApplies(CompiledUnitExample.run());
	}

	private void assertDocumentApplies(Session session) throws Exception {
		OclEngine engine = session.engine();
		OclContext city = OclContext.of(session.city());

		// def: features and operations answer in ad-hoc expressions
		assertEquals("Herman Melville: Moby Dick", engine.evaluate("self.books->first().label", city));
		assertEquals(5, engine.evaluate("self.books->select(b | b.isVintage)->size()", city),
				"every book but SICP (1985) is vintage");
		assertEquals(5, engine.evaluate("self.authorCount", city),
				"Anonymous wrote two of the six books");

		// the language being used: closure over the shelf tree, tuples over the catalog
		assertEquals(4, engine.evaluate("self.shelves->closure(s | s.shelves)->size()", city),
				"Fiction, Rare, Science and the nested Classics");
		assertEquals(2, engine.evaluate(
				"self.books->collect(b | Tuple{author = b.author, year = b.year})->select(t | t.year < 1500)->size()",
				city), "two manuscripts predate 1500");

		// derive: wins inside OCL evaluation, EMF's eGet stays raw — the documented boundary
		EObject mobyDick = (EObject) engine.evaluate("self.books->first()", city);
		assertEquals("Herman Melville - Moby Dick",
				engine.evaluate("self.displayName", OclContext.of(mobyDick)));
		assertEquals("MD-0001", mobyDick.eGet(mobyDick.eClass().getEStructuralFeature("displayName")),
				"eGet stays raw — that boundary belongs to the delegate annotations");

		// body: makes the annotation-free operation callable in evaluation
		EObject ada = member(session, 0);
		EObject grace = member(session, 1);
		assertEquals(Boolean.TRUE, engine.evaluate("self.canBorrow()", OclContext.of(ada)));
		assertEquals(Boolean.FALSE, engine.evaluate("self.canBorrow()", OclContext.of(grace)));

		// inv: feeds the OclDocumentValidator — and only that validator
		OclDocumentValidator validator = new OclDocumentValidator(engine);
		assertTrue(validator.validate(ada, new BasicDiagnostic(), null));
		BasicDiagnostic diagnostics = new BasicDiagnostic();
		assertFalse(validator.validate(grace, diagnostics, null), "Grace borrowed four books");
		assertTrue(diagnostics.getChildren().get(0).getMessage().contains("borrowLimit"),
				diagnostics.getChildren().get(0).getMessage());
		assertEquals(Diagnostic.OK, Diagnostician.INSTANCE.validate(grace).getSeverity(),
				"a document inv: does not reach EMF's Diagnostician — that path reads annotations only");
	}

	private static EObject member(Session session, int index) {
		EObject city = session.city();
		return (EObject) ((List<?>) city.eGet(city.eClass().getEStructuralFeature("members"))).get(index);
	}

	private static String flatten(Diagnostic diagnostic) {
		StringBuilder text = new StringBuilder(diagnostic.getMessage() == null ? "" : diagnostic.getMessage());
		for (Diagnostic child : diagnostic.getChildren()) {
			text.append(" | ").append(flatten(child));
		}
		return text.toString();
	}
}
