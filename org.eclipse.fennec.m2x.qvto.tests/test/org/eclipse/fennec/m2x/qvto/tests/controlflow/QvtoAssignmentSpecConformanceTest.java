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
package org.eclipse.fennec.m2x.qvto.tests.controlflow;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Assignment semantics against QVT v1.3 §8.2.2.2, §8.2.2.10 and §8.2.2.11 (#339).
 */
class QvtoAssignmentSpecConformanceTest extends AbstractQvtoEngineTest {

	private static QvtoExecutionResult run(String main) throws Exception {
		return execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    intermediate property TargetElement::tags : Sequence(String);
				    main() {
				%s
				    }
				}
				""".formatted(main));
	}

	@Test
	void append_flattensACollection() throws Exception {
		// "Multiple valued Variable append: left := late value->flatten()->iterate(…)"
		QvtoExecutionResult result = run("""
				var l := Sequence{1};
				l += Sequence{2, 3};
				log('size=' + l->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "size=3");
	}

	@Test
	void append_doesNotChangeASharedCollection() throws Exception {
		QvtoExecutionResult result = run("""
				var a := Sequence{1};
				var b := a;
				b += 2;
				log('a=' + a->size().repr() + ' b=' + b->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "a=1 b=2");
	}

	@Test
	void resetToNull_doesNotClearASharedCollection() throws Exception {
		QvtoExecutionResult result = run("""
				var a := Sequence{1, 2};
				var b := a;
				b := null;
				log('a=' + a->size().repr() + ' b=' + b.oclIsUndefined().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "a=2 b=true");
	}

	@Test
	void appendToASingleValuedVariable_isAnError() throws Exception {
		// "Equivalent Single valued Property or Variable append: invalid – append is not possible
		// for single values"
		QvtoExecutionResult result = run("""
				var x := 1;
				x += 2;
				log('x=' + x.repr());
				""");
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.ERROR
				&& d.getMessage().contains("append is not possible for single values")), () -> result.diagnostics().toString());
		assertLogged(result, "x=1");
	}

	@Test
	void appendToADeclaredCollection_startsFromItsEmptyDefault() throws Exception {
		// §8.2.2.10: "an empty collection for a collection"
		QvtoExecutionResult result = run("""
				var c : Sequence(String);
				c += 'x';
				var st : Set(Integer);
				st += Sequence{1, 1, 2};
				var d : Dict(String, Integer);
				d->put('k', 1);
				log('c=' + c->size().repr() + ' st=' + st->size().repr() + ' d=' + d->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "c=1 st=2 d=1");
	}

	@Test
	void assignToADeclaredSet_keepsItASet() throws Exception {
		// "For all assignments, the type of the LHS is unchanged by the assignment"
		QvtoExecutionResult result = run("""
				var st : Set(Integer) := Set{};
				st := Sequence{1, 1, 2};
				var os : OrderedSet(String) := OrderedSet{};
				os := Sequence{'a', 'b', 'a'};
				log('st=' + st->size().repr() + ' set=' + st.oclIsKindOf(Set(Integer)).repr() + ' os=' + os->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "st=2 set=true os=2");
	}

	@Test
	void appendNull_takesTheDefault() throws Exception {
		QvtoExecutionResult result = run("""
				var l : Sequence(String) := Sequence{};
				var n : String := null;
				l += n default 'd';
				log('l=' + l->first());
				""");
		assertSuccess(result);
		assertLogged(result, "l=d");
	}

	@Test
	void aCompositeAssignment_buildsASequence() throws Exception {
		// "A complex assignment is equivalent to a simple assignment comprising a Sequence of the
		// compound expression values"
		QvtoExecutionResult result = run("""
				var q : Sequence(String) := Sequence{};
				q := { 'a'; 'b'; };
				log('q=' + q->size().repr() + q->last());
				""");
		assertSuccess(result);
		assertLogged(result, "q=2b");
	}

	@Test
	void aCompositeAssignment_toAMultiValuedProperty() throws Exception {
		QvtoExecutionResult result = run("""
				var e := object TargetElement {};
				e.tags := { 'a'; null; 'b'; };
				log('tags=' + e.tags->size().repr());
				""");
		assertSuccess(result);
		// A property drops the null element
		assertLogged(result, "tags=2");
	}

	@Test
	void aBlock_returnsNull() throws Exception {
		// §8.2.2.2: "do { … } // executes the body and returns null"
		QvtoExecutionResult result = run("""
				var x := do { 'a'; };
				log('x=' + x.oclIsUndefined().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "x=true");
	}

	@Test
	void theWithResultNotation_returnsTheInitValue() throws Exception {
		QvtoExecutionResult result = run("""
				var x := var y ::= 'init';
				log('x=' + x);
				""");
		assertSuccess(result);
		assertLogged(result, "x=init");
	}

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "Expected log containing '" + expected + "' but diagnostics were: " + result.diagnostics());
	}
}
