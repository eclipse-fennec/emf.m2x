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
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Conditions that are neither true nor false, beyond the if and switch of #308 (#310).
 *
 * <p>A: an imperative statement that cannot decide does not run, with an error. B and D: a
 * filter answers invalid as the OCL engine's select does. C: a mapping guard that cannot be
 * decided keeps the mapping from running, with an error; an undecidable postcondition is one.
 */
class QvtoUndefinedConditionTest extends AbstractQvtoEngineTest {

	// --- A: imperative statements ---

	@Test
	@DisplayName("A: an undefined while-condition ends the loop with an error")
	void whileConditionUndefined() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
				    var b : Boolean := null;
				    while (b) { log('body'); };
				    log('after');
				}
				""");

		assertError(result, "while-condition must be Boolean, got: null — the loop ends");
		assertEquals(List.of("after"), logs(result));
	}

	@Test
	@DisplayName("A: an undefined forEach-condition skips that element with an error, and goes on")
	void forEachConditionUndefined() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
				    Sequence{1, 2, 3}->forEach(i | if i = 2 then null else true endif) {
				        log(i.repr());
				    };
				}
				""");

		// §8.2.2.6: "if (condition) body" for each element
		assertError(result, "forEach-condition must be Boolean, got: null — the element is skipped");
		assertEquals(List.of("1", "3"), logs(result));
	}

	@Test
	@DisplayName("A: an undefined log-condition logs nothing, with an error")
	void logConditionUndefined() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
				    var b : Boolean := null;
				    log('hidden') when b;
				}
				""");

		assertError(result, "log-condition must be Boolean, got: null — nothing is logged");
		assertEquals(List.of(), logs(result));
	}

	@Test
	@DisplayName("A: an undefined xselect-condition skips that element with an error")
	void xselectConditionUndefined() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
				    var kept := Sequence{1, 2, 3}[i | if i = 2 then null else true endif];
				    log(kept->size().repr());
				}
				""");

		// §8.2.2.7: "if (SELECTOR <> null and CONDITION)" for each element
		assertError(result, "xselect-condition must be Boolean, got: null — the element is skipped");
		assertEquals(List.of("2"), logs(result));
	}

	@Test
	@DisplayName("A: false conditions are as quiet as ever")
	void falseConditionsAreQuiet() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
				    while (false) { log('body'); };
				    Sequence{1, 2}->forEach(i | i > 1) { log(i.repr()); };
				    log('hidden') when false;
				    log(Sequence{1, 2}[i | i > 1]->size().repr());
				}
				""");

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(List.of("2", "1"), logs(result));
	}

	// --- B: the boolean iterators answer as the OCL engine does ---

	@Test
	@DisplayName("B: select and reject with a body neither true nor false are invalid")
	void selectAndRejectAreInvalid() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
				    var selected := Sequence{1, 2}->select(i | if i = 2 then null else true endif);
				    var rejected := Sequence{1, 2}->reject(i | if i = 2 then null else true endif);
				    log(selected.oclIsInvalid().repr());
				    log(rejected.oclIsInvalid().repr());
				    log(Sequence{1, 2}->select(i | if i = 2 then null else true endif)->oclIsInvalid().repr());
				}
				""");

		// The last line is evaluated by the OCL engine — the same answer as QVT-O's own
		assertEquals(List.of("true", "true", "true"), logs(result));
	}

	@Test
	@DisplayName("B: forAll, exists and any follow OCL's precedence (§11.9.1)")
	void quantifiersFollowOcl() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
				    var forAllFalse := Sequence{1, 2}->forAll(i | if i = 1 then null else false endif);
				    var forAllNull := Sequence{1, 2}->forAll(i | if i = 1 then null else true endif);
				    var existsTrue := Sequence{1, 2}->exists(i | if i = 1 then null else true endif);
				    var existsNull := Sequence{1, 2}->exists(i | if i = 1 then null else false endif);
				    var anyNull := Sequence{1, 2}->any(i | if i = 1 then null else false endif);
				    log(forAllFalse.repr());
				    log(forAllNull.oclIsUndefined().repr());
				    log(existsTrue.repr());
				    log(existsNull.oclIsUndefined().repr());
				    log(anyNull.oclIsUndefined().repr());
				}
				""");

		assertEquals(List.of("false", "true", "true", "true", "true"), logs(result));
	}

	// --- C: mapping guards ---

	@Test
	@DisplayName("C: an undefined when-guard keeps the mapping from running, with an error")
	void whenGuardUndefined() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = runOnOneClass("""
				    mapping EClass::copy() : EClass when { self.eSuperTypes->first() = null } {
				        name := self.name;
				    }
				""", out);

		assertError(result, "when-condition must be Boolean, got: invalid — mapping 'copy' is not executed");
		assertEquals(List.of(), out.getContents());
	}

	@Test
	@DisplayName("C: an undefined where-postcondition is an error")
	void whereConditionUndefined() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = runOnOneClass("""
				    mapping EClass::copy() : EClass where { self.eSuperTypes->first() = null } {
				        name := self.name;
				    }
				""", out);

		assertError(result, "where-condition must be Boolean, got: invalid"
				+ " — the postcondition of mapping 'copy' cannot be checked");
	}

	@Test
	@DisplayName("C: a violated where-postcondition stays a warning")
	void whereConditionFalseStaysAWarning() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = runOnOneClass("""
				    mapping EClass::copy() : EClass where { false } {
				        name := self.name;
				    }
				""", out);

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertTrue(messages(result).contains("Mapping 'copy': where-postcondition failed"));
	}

	// --- D: resolve condition ---

	@Test
	@DisplayName("D: a resolve whose condition is undefined for a candidate is invalid")
	void resolveConditionUndefined() throws QvtoParseException {
		QvtoExecutionResult result = runOnOneClass("""
				    mapping EClass::copy() : EClass {
				        name := self.name;
				    }
				    main() {
				        src.objectsOfType(EClass)->map copy();
				        var c := src.objectsOfType(EClass)->first();
				        log(c.resolveone(t : EClass | t.eSuperTypes->first() = null).oclIsInvalid().repr());
				    }
				""", emptyExtent());

		assertEquals(List.of("true"), logs(result));
	}

	// --- helpers ---

	private static QvtoExecutionResult runOnOneClass(String elements, QvtoModelExtent out)
			throws QvtoParseException {
		EClass c = EcoreFactory.eINSTANCE.createEClass();
		c.setName("C");
		String main = elements.contains("main()") ? "" : """
				    main() {
				        src.objectsOfType(EClass)->map copy();
				    }
				""";
		return executeWithExtents("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(in src : ECORE, out tgt : ECORE) {
				""" + elements + main + """
				}
				""", new BasicQvtoModelExtent(c), out);
	}

	private static void assertError(QvtoExecutionResult result, String message) {
		assertFalse(result.isSuccess(), () -> "expected an error: " + result.diagnostics());
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
