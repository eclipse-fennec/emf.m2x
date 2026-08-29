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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.imperativeocl.ImperativeIterateExp;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * QVT v1.3 §8.2.2.7 — the imperative iterate variants are imperative loops.
 *
 * <p>The parser used to build a plain OCL {@code IteratorExp} for {@code xcollect} and its
 * siblings, so nothing constructed the metaclass the spec names, the evaluator's
 * implementation of it was never reached, and the loop limit that lives there did not apply
 * (#175). Three things follow from building the right metaclass, and each is pinned here.
 */
class QvtoImperativeIterateTest extends AbstractQvtoEngineTest {

	@Test
	@DisplayName("the AST says which variant it is")
	void theAstCarriesTheVariant() throws QvtoParseException {
		OperationalTransformation transformation = parse("""
				transformation T();
				main() {
					var items := Sequence{1, 2, 3};
					var doubled := items->xcollect(i | i * 2);
				}
				""");

		ImperativeIterateExp iterate = firstImperativeIterate(transformation);
		assertEquals("xcollect", iterate.getName());
	}

	@Test
	@DisplayName("the [condition] shorthand is xselect, and ![condition] is xselectOne")
	void theShorthandsAreImperativeToo() throws QvtoParseException {
		assertEquals("xselect", firstImperativeIterate(parse("""
				transformation T();
				main() {
					var items := Sequence{1, 2, 3};
					var big := items[i | i > 1];
				}
				""")).getName());

		assertEquals("xselectOne", firstImperativeIterate(parse("""
				transformation T();
				main() {
					var items := Sequence{1, 2, 3};
					var one := items![i | i > 1];
				}
				""")).getName());
	}

	@Test
	@DisplayName("xcollect flattens what the body produced")
	void xcollectFlattens() throws QvtoParseException {
		// §8.2.2.7: "BODY->flatten()->forEach(target)" — an OCL collect would nest
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var items := Sequence{1, 2};
					var pairs := items->xcollect(i | Sequence{i, i * 10});
					log(pairs->size().repr());
				}
				""");

		assertSuccessAndLogged(result, "4");
	}

	@Test
	@DisplayName("xcollect never collects a null")
	void xcollectDropsNulls() throws QvtoParseException {
		// §8.2.2.7: "if (target <> null) res += target"
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var items := Sequence{1, 2, 3};
					var some := items->xcollect(i | if i = 2 then null else i endif);
					log(some->size().repr());
				}
				""");

		assertSuccessAndLogged(result, "2");
	}

	@Test
	@DisplayName("xselectOne answers the first match")
	void xselectOneTakesTheFirst() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var items := Sequence{1, 2, 3, 4};
					var first := items![i | i > 2];
					log(first.repr());
				}
				""");

		assertSuccessAndLogged(result, "3");
	}

	@Test
	@Disabled("An x-variant nested inside a larger expression is evaluated by the OCL engine, "
			+ "which knows neither QVT-O's iterator names nor its metaclasses. Pre-existing: "
			+ "before the AST carried the right metaclass the same expression failed with "
			+ "'Unknown iterator: xcollect', now with 'Unsupported expression type'. The fix is "
			+ "a callback from the OCL evaluator into the QVT-O one for nodes it does not know, "
			+ "which is the D25 hybrid boundary and its own piece of work.")
	@DisplayName("a variant can be used inside a larger expression")
	void theVariantsChain() throws QvtoParseException {
		// The two-statement form works and is covered above; this is the chained one.
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var items := Sequence{1, 2, 3};
					log(items->xcollect(i | i * 2)->size().repr());
				}
				""");

		assertSuccessAndLogged(result, "3");
	}

	// --- helpers ---

	private static void assertSuccessAndLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected)),
				() -> "expected a log of " + expected + ", got " + result.diagnostics());
	}

	private static ImperativeIterateExp firstImperativeIterate(OperationalTransformation t) {
		for (Iterator<EObject> it = t.eAllContents(); it.hasNext();) {
			EObject node = it.next();
			if (node instanceof ImperativeIterateExp iterate) {
				return iterate;
			}
		}
		return assertInstanceOf(ImperativeIterateExp.class, null,
				"the transformation holds no imperative iterate expression");
	}
}
