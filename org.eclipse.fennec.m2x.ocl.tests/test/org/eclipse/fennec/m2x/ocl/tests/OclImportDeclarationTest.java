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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * What a Complete OCL document's {@code import} declarations do (§12.3).
 *
 * <p>They used to do nothing at all — {@code visitImportDeclaration} logged at
 * {@code FINE} and returned, which nobody sees. That is fine for one of the three forms
 * and wrong for the other two.
 *
 * <p>Importing a package really is a no-op: type names resolve against the whole package
 * registry the engine was given, whether or not a document asks for them. An alias is
 * remembered and usable. A {@code library} import has nothing it could resolve to — in
 * Fennec, additional operations reach the engine as operation providers — so it is reported
 * rather than dropped, because otherwise the failure surfaces later as an unknown operation,
 * at a place that gives no hint about the cause.
 */
class OclImportDeclarationTest extends AbstractOclTest {

	@Test
	@DisplayName("importing a package is accepted — the registry already carries it")
	void plainImportIsAccepted() throws OclParseException {
		List<Constraint> constraints = engine.parseDocument("""
				import company::Company
				context Person
				  inv NameNotEmpty: self.name.size() > 0
				""");

		assertFalse(constraints.isEmpty(), "the document parses and its constraint survives");
	}

	@Test
	@DisplayName("an alias makes a qualified name resolve for the rest of the document")
	void aliasImportIsUsable() throws OclParseException {
		List<Constraint> constraints = engine.parseDocument("""
				import c : company
				context Person
				  inv KnowsItsOwnType: self.oclIsKindOf(c::Person)
				""");

		assertFalse(constraints.isEmpty(),
				"c::Person has to resolve, otherwise the alias was dropped");
	}

	@Test
	@DisplayName("the alias resolves the package, not whatever follows it")
	void aliasIsNotAWildcard() {
		OclParseException failure = assertDocumentFails("""
				import c : company
				context Person
				  inv Nonsense: self.oclIsKindOf(c::NoSuchType)
				""");

		assertTrue(messages(failure).stream().anyMatch(m -> m.contains("Unknown type (c::NoSuchType)")),
				() -> "diagnostics: " + messages(failure));
	}

	@Test
	@DisplayName("a library import is reported instead of quietly dropped")
	void libraryImportIsReported() {
		OclParseException failure = assertDocumentFails("""
				library company::Utilities
				context Person
				  inv NameNotEmpty: self.name.size() > 0
				""");

		assertTrue(messages(failure).stream().anyMatch(m -> m.contains("Library imports")),
				() -> "diagnostics: " + messages(failure));
	}

	// --- helpers ---

	private static List<String> messages(OclParseException failure) {
		return failure.getErrors().stream().map(Resource.Diagnostic::getMessage).toList();
	}

	private OclParseException assertDocumentFails(String document) {
		try {
			engine.parseDocument(document);
		} catch (OclParseException expected) {
			return expected;
		}
		throw new AssertionError("expected the declaration to be reported: " + document);
	}
}
