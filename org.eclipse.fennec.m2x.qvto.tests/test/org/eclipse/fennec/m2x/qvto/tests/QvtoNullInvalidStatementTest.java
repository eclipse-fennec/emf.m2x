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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * null passed to an operation the transformation defines (#306), and null or invalid where a
 * statement needs a value (#308).
 */
class QvtoNullInvalidStatementTest extends AbstractQvtoEngineTest {

	// --- #306: null is an argument like any other for a user-defined operation ---

	@Test
	@DisplayName("#306: a helper takes null, and its body decides")
	void helperTakesNull() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				helper describe(text : String) : String {
				    if text <> null then { return text; } endif;
				    return 'none';
				}
				main() {
				    var s : String := null;
				    log(describe(s));
				}
				""");

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(List.of("none"), logs(result));
	}

	@Test
	@DisplayName("#306: a contextual query takes null")
	void contextualQueryTakesNull() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				query String::orElse(fallback : String) : String {
				    return if fallback = null then self else fallback endif;
				}
				main() {
				    var s : String := null;
				    log('x'.orElse(s));
				}
				""");

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(List.of("x"), logs(result));
	}

	@Test
	@DisplayName("#306: the standard library still refuses null — 'a' + null (#270)")
	void standardLibraryStillRefusesNull() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
				    var s : String := null;
				    log('a'.concat(s));
				}
				""");

		assertFalse(result.isSuccess());
		assertTrue(messages(result).contains("Null argument for operation 'concat'"),
				() -> "diagnostics: " + result.diagnostics());
	}

	// --- #308: null or invalid where a statement needs a value ---

	@Test
	@DisplayName("#308: an invalid if-condition takes no branch, the else included, and is an error")
	void invalidIfConditionIsAnError() throws QvtoParseException {
		QvtoExecutionResult result = runOnEmptyPackage("""
				    var a : EAnnotation := p.eAnnotations->first();
				    if a = null then { log('then'); } else { log('else'); } endif;
				    log('after');
				""");

		// OCL v2.4 §10.3 IfExpEval [1]: neither true nor false — "otherwise the result is invalid"
		assertFalse(result.isSuccess());
		assertError(result, "if-condition must be Boolean, got: invalid — no branch is taken");
		assertEquals(List.of("after"), logs(result));
	}

	@Test
	@DisplayName("#308: a null if-condition is an error too")
	void nullIfConditionIsAnError() throws QvtoParseException {
		QvtoExecutionResult result = runOnEmptyPackage("""
				    var b : Boolean := null;
				    if (b) { log('then'); } else { log('else'); } endif;
				""");

		assertFalse(result.isSuccess());
		assertError(result, "if-condition must be Boolean, got: null — no branch is taken");
		assertEquals(List.of(), logs(result));
	}

	@Test
	@DisplayName("#308: a false condition still takes the else branch")
	void falseConditionTakesElse() throws QvtoParseException {
		QvtoExecutionResult result = runOnEmptyPackage("""
				    if p.eAnnotations->notEmpty() then { log('then'); } else { log('else'); } endif;
				""");

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(List.of("else"), logs(result));
	}

	@Test
	@DisplayName("#308: an invalid switch condition is an error and stops at that alternative")
	void invalidSwitchConditionIsAnError() throws QvtoParseException {
		QvtoExecutionResult result = runOnEmptyPackage("""
				    var a : EAnnotation := p.eAnnotations->first();
				    switch {
				        case (a = null) log('first');
				        case (true) log('second');
				        else log('else');
				    };
				""");

		assertFalse(result.isSuccess());
		assertError(result, "switch-condition must be Boolean, got: invalid — no branch is taken");
		assertEquals(List.of(), logs(result));
	}

	@Test
	@DisplayName("#308: assigning a property of an invalid object is an error")
	void assignmentToAnInvalidTargetIsAnError() throws QvtoParseException {
		QvtoExecutionResult result = runOnEmptyPackage("""
				    var a : EAnnotation := p.eAnnotations->first();
				    a.source := 'x';
				""");

		assertFalse(result.isSuccess());
		assertError(result, "Cannot assign to property 'source': the target is invalid");
	}

	@Test
	@DisplayName("#308: assigning a property of null is an error")
	void assignmentToANullTargetIsAnError() throws QvtoParseException {
		QvtoExecutionResult result = runOnEmptyPackage("""
				    var a : EAnnotation := null;
				    a.source := 'x';
				""");

		assertFalse(result.isSuccess());
		assertError(result, "Cannot assign to property 'source': the target is null");
	}

	// --- helpers ---

	private static QvtoExecutionResult runOnEmptyPackage(String mainBody) throws QvtoParseException {
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("p");
		return executeWithExtents("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(in src : ECORE);
				main() {
				    var p : EPackage := src.rootObjects()![EPackage];
				""" + mainBody + """
				}
				""", new BasicQvtoModelExtent(pkg));
	}

	private static void assertError(QvtoExecutionResult result, String message) {
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR && d.getMessage().equals(message)),
				() -> "expected the error '" + message + "', got " + result.diagnostics());
	}

	private static List<String> messages(QvtoExecutionResult result) {
		return result.diagnostics().stream().map(Diagnostic::getMessage).toList();
	}

	private static List<String> logs(QvtoExecutionResult result) {
		return messages(result).stream()
				.filter(m -> m.startsWith("log: "))
				.map(m -> m.substring("log: ".length()))
				.toList();
	}
}
