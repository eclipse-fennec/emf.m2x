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
package org.eclipse.fennec.m2m.qvto.tests.helper;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O helpers and queries.
 * Tests cover simple helpers, expression bodies, contextual helpers,
 * recursion, overloading, and query composition.
 */
class QvtoE2eHelperQueryTest extends AbstractQvtoEngineTest {

	// ---- Simple helper with body ----

	@Test
	void helper_simpleBody_returnsInteger() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper answer() : Integer {
				        return 42;
				    }
				    main() {
				        log(answer().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
	}

	@Test
	void helper_simpleBody_returnsString() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper greet() : String {
				        return 'hello world';
				    }
				    main() {
				        log(greet());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "hello world");
	}

	@Test
	void helper_simpleBody_returnsBoolean() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper isPositive(n : Integer) : Boolean {
				        return n > 0;
				    }
				    main() {
				        log(isPositive(5).toString());
				        log(isPositive(-1).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
		assertLogged(result, "false");
	}

	// ---- Query (side-effect free) ----

	@Test
	void query_returnsComputedValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query double(n : Integer) : Integer {
				        return n * 2;
				    }
				    main() {
				        log(double(21).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
	}

	@Test
	void query_stringConcatenation() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query fullName(first : String, last : String) : String {
				        return first + ' ' + last;
				    }
				    main() {
				        log(fullName('John', 'Doe'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "John Doe");
	}

	// ---- Contextual helper ----

	@Test
	void helper_contextual_accessesSelf() throws Exception {
		EObject src = createSourceElement("ctx-test", 7);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    helper SourceElement::label() : String {
				        return self.name + ':' + self.value.toString();
				    }
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.label();
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("ctx-test:7", eGet(outExtent.getContents().get(0), "name"));
	}

	// ---- Recursive helper ----

	@Test
	void helper_recursive_factorial() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper factorial(n : Integer) : Integer {
				        return if n <= 1 then 1 else n * factorial(n - 1) endif;
				    }
				    main() {
				        log(factorial(5).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "120");
	}

	@Test
	void helper_recursive_fibonacci() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper fib(n : Integer) : Integer {
				        return if n <= 1 then n else fib(n - 1) + fib(n - 2) endif;
				    }
				    main() {
				        log(fib(10).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "55");
	}

	// ---- Helper with multiple parameters ----

	@Test
	void helper_multipleParams_sum() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper sum3(a : Integer, b : Integer, c : Integer) : Integer {
				        return a + b + c;
				    }
				    main() {
				        log(sum3(10, 20, 30).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "60");
	}

	// ---- Helper returning collection ----

	@Test
	void helper_returnsSequence() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper range(n : Integer) : Sequence(Integer) {
				        var result : Sequence(Integer) := Sequence{};
				        var i : Integer := 1;
				        while (i <= n) {
				            result += i;
				            i := i + 1;
				        };
				        return result;
				    }
				    main() {
				        log(range(3)->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
	}

	// ---- Helper/Query composition ----

	@Test
	void helper_callsQuery() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query square(n : Integer) : Integer {
				        return n * n;
				    }
				    helper sumOfSquares(a : Integer, b : Integer) : Integer {
				        return square(a) + square(b);
				    }
				    main() {
				        log(sumOfSquares(3, 4).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "25");
	}

	@Test
	void query_callsHelper() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper negate(n : Integer) : Integer {
				        return 0 - n;
				    }
				    query absValue(n : Integer) : Integer {
				        return if n < 0 then negate(n) else n endif;
				    }
				    main() {
				        log(absValue(-5).toString());
				        log(absValue(3).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "5");
		assertLogged(result, "3");
	}

	// ---- Helper with OCL collection operations ----

	@Test
	void helper_usesOclSelect() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper filterPositive(nums : Sequence(Integer)) : Sequence(Integer) {
				        return nums->select(n | n > 0);
				    }
				    main() {
				        var result := filterPositive(Sequence{-1, 2, -3, 4, 0});
				        log(result->size().toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
	}

	@Test
	void helper_usesOclCollect() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper doubled(nums : Sequence(Integer)) : Sequence(Integer) {
				        return nums->collect(n | n * 2);
				    }
				    main() {
				        var result := doubled(Sequence{1, 2, 3});
				        result->forEach(n) { log(n.toString()); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
		assertLogged(result, "4");
		assertLogged(result, "6");
	}

	// ---- Helper chain (double dispatch) ----

	@Test
	void helper_chainedCalls() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper step1(n : Integer) : Integer { return n + 1; }
				    helper step2(n : Integer) : Integer { return n * 2; }
				    helper step3(n : Integer) : Integer { return n - 3; }
				    main() {
				        var r := step3(step2(step1(5)));
				        log(r.toString());
				    }
				}
				""");
		assertSuccess(result);
		// step1(5)=6, step2(6)=12, step3(12)=9
		assertLogged(result, "9");
	}

	// ---- Helper with local variables ----

	@Test
	void helper_localVariables() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper compute(x : Integer) : Integer {
				        var a : Integer := x * 2;
				        var b : Integer := a + 10;
				        var c : Integer := b - 3;
				        return c;
				    }
				    main() {
				        log(compute(5).toString());
				    }
				}
				""");
		assertSuccess(result);
		// 5*2=10, 10+10=20, 20-3=17
		assertLogged(result, "17");
	}

	// ---- Helper using string operations ----

	@Test
	void helper_stringOperations() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper process(s : String) : String {
				        return s.toUpperCase().substring(1, 3);
				    }
				    main() {
				        log(process('hello'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "HEL");
	}

	// ---- Contextual helper on target type ----

	@Test
	void helper_contextualOnTargetType() throws Exception {
		EObject src = createSourceElement("src", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    helper TargetElement::describe() : String {
				        return 'Target:' + self.name;
				    }
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        log(r.describe());
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "Target:src");
	}

	// ---- P3-01: Helper vertieft E2E Tests ----

	// §8.2.1.12: Helper creates object via object expression (side-effect)
	@Test
	void helper_createsObjectInMapping() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    helper createTarget(n : String) : TargetElement {
				        return object TargetElement { name := n; };
				    }
				    main() {
				        var elem := createTarget('from-helper');
				        log(elem.name);
				    }
				}
				""", outExtent);
		assertSuccess(result);
		assertLogged(result, "from-helper");
		// object expression auto-adds to default output extent
		assertTrue(outExtent.getContents().size() >= 1,
				"Output extent should contain the created object");
		assertEquals("from-helper", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.2.1.12: Helper returning Tuple, caller uses tuple parts
	@Test
	void helper_tupleReturn_usedByCaller() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper pair(a : String, b : Integer) : Tuple(name : String, value : Integer) {
				        return Tuple{name = a, value = b};
				    }
				    main() {
				        var t := pair('item', 42);
				        log(t.name + ':' + t.value.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "item:42");
	}

	// ---- P3-03: Contextual Helper vertieft E2E Tests ----

	// §8.2.1.10 p101: Two contextual helpers same name on different model types
	@Test
	void helper_sameNameOnSourceAndTarget() throws Exception {
		EObject src = createSourceElement("elem1", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    helper SourceElement::describe() : String {
				        return 'src:' + self.name;
				    }
				    helper TargetElement::describe() : String {
				        return 'tgt:' + self.name;
				    }
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				        log(self.describe());
				        log(r.describe());
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "src:elem1");
		assertLogged(result, "tgt:elem1");
	}

	// ---- P3-02: Query vertieft E2E Tests ----

	// §8.1.9 + Eclipse Simpleuml_To_Rdb: Query used in mapping when-guard
	@Test
	void query_inMappingGuard() throws Exception {
		EObject src1 = createSourceElement("persistent", 1);
		EObject src2 = createSourceElement("transient", 0);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    query SourceElement::isPersistent() : Boolean = self.value > 0;
				    mapping SourceElement::toTarget() : r : TargetElement
				        when { self.isPersistent(); }
				    {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("persistent", eGet(outExtent.getContents().get(0), "name"));
	}

	// §8.1.9: Recursive query (expression body)
	@Test
	void query_recursive_factorial() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    query factorial(n : Integer) : Integer =
				        if n <= 1 then 1 else n * factorial(n - 1) endif;
				    main() {
				        log(factorial(6).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "720");
	}

	// §8.1.9: Helper with expression body (= expr) returns value
	@Test
	void helper_expressionBody_returnsValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper square(n : Integer) : Integer = n * n;
				    main() {
				        log(square(7).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "49");
	}

	// ---- Helpers ----

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected));
		assertTrue(found, "Expected log containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
	}

	private static Object eGet(EObject obj, String featureName) {
		return obj.eGet(obj.eClass().getEStructuralFeature(featureName));
	}
}
