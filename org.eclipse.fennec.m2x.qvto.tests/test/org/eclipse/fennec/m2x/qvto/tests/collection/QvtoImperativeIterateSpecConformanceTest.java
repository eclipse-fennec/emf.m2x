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
package org.eclipse.fennec.m2x.qvto.tests.collection;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * The imperative iterate variants and their shorthands against QVT v1.3 §8.1.7, §8.1.8, §8.2.1.21,
 * §8.2.2.6, §8.2.2.7 and §8.2.2.22 (#341).
 */
class QvtoImperativeIterateSpecConformanceTest extends AbstractQvtoEngineTest {

	private static QvtoExecutionResult run(String elements, String main) throws Exception {
		return run(elements, main, emptyExtent());
	}

	private static QvtoExecutionResult run(String elements, String main, QvtoModelExtent out) throws Exception {
		return executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				%s
				    main() {
				        var objs := s.objectsOfType(SourceElement)->sortedBy(value);
				%s
				    }
				}
				""".formatted(elements, main), new BasicQvtoModelExtent(createSourceElement("a", 1),
						createSourceElement("_b", 2), createSourceElement(null, 3)), out);
	}

	@Test
	void anArrowProperty_dropsNulls() throws Exception {
		// §8.2.2.7: "list->prop; // same as list->xcollect(i | i.prop)" — xcollect collects no null
		QvtoExecutionResult result = run("", "log('names=' + objs->name->size().repr());");
		assertSuccess(result);
		assertLogged(result, "names=2");
	}

	@Test
	void xselect_keepsTheKindOfItsSource() throws Exception {
		// §8.2.2.7: xselect : BagOrOrderedSetOrSequenceOrSet(T)
		QvtoExecutionResult result = run("", """
				log('set=' + Set{1, 2, 3}->xselect(i | i > 1)->including(2)->size().repr());
				log('os=' + OrderedSet{1, 2, 3}->xselect(i | i > 1)->append(2)->size().repr());
				log('seq=' + Sequence{1, 2, 3}->xselect(i | i > 1)->including(2)->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "set=2");
		assertLogged(result, "os=2");
		assertLogged(result, "seq=3");
	}

	@Test
	void xcollect_onAnUnorderedSource_isABag() throws Exception {
		// §8.2.2.7: xcollect : BagOrSequence(TT)
		QvtoExecutionResult result = run("", """
				var bag := Set{1, 2, 3}->xcollect(i | 1);
				log('bag=' + bag->size().repr() + ' ' + bag.oclIsKindOf(Bag(Integer)).repr());
				""");
		assertSuccess(result);
		assertLogged(result, "bag=3 true");
	}

	@Test
	void theTargetForm_namesTheCollectedValue() throws Exception {
		// §8.2.2.7: list->xcollectselect(i; res = i.prop | not res.startswith("_"))
		QvtoExecutionResult result = run("", """
				log('kept=' + objs->xcollectselect(i; res = i.name | not res.startsWith('_'))->size().repr());
				log('one=' + objs->xcollectselectOne(i; res = i.name | res.startsWith('_')));
				""");
		assertSuccess(result);
		assertLogged(result, "kept=1");
		assertLogged(result, "one=_b");
	}

	@Test
	void theImperativeVariants_haveAnImplicitIterator() throws Exception {
		// §8.2.2.7 notation "(<body_or_condition>)"
		QvtoExecutionResult result = run("", """
				log('selected=' + objs->xselect(value > 1)->size().repr());
				log('collected=' + objs->xcollect(value)->sum().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "selected=2");
		assertLogged(result, "collected=6");
	}

	@Test
	void anArrowOperation_collectsTheElementsOperation() throws Exception {
		// §8.2.2.7: "these defined shorthand conventions … equally apply to operations"
		QvtoExecutionResult result = run("""
				    query SourceElement::dbl() : Integer { return self.value * 2; }
				""", "log('dbl=' + objs->dbl()->sum().repr());");
		assertSuccess(result);
		assertLogged(result, "dbl=12");
	}

	@Test
	void aPredefinedCollectionOperation_wins() throws Exception {
		QvtoExecutionResult result = run("""
				    query SourceElement::size() : Integer { return 99; }
				""", "log('size=' + objs->size().repr());");
		assertSuccess(result);
		assertLogged(result, "size=3");
	}

	@Test
	void arrowNew_createsOneObjectPerElement() throws Exception {
		// §8.1.8: self.attribute->new(a) Column(a.name, a.type.name)
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("", """
				var made := objs->new(a) TargetElement();
				log('made=' + made->size().repr());
				""", out);
		assertSuccess(result);
		assertLogged(result, "made=3");
		assertEquals(3, out.getContents().size());
	}

	@Test
	void arrowObject_withAnUntypedIterator() throws Exception {
		// §8.1.7: self.attribute->object(a) Column { name := a.name; }
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("", """
				var made := objs->object(a) TargetElement { name := a.name; };
				log('made=' + made->size().repr());
				""", out);
		assertSuccess(result);
		assertLogged(result, "made=3");
		assertEquals(3, out.getContents().size());
	}

	@Test
	void arrowXmap_withADeclarator() throws Exception {
		// §8.2.1.21: self.ownedElement[Class]->xmap(i) i.class2table()
		QvtoExecutionResult result = run("""
				    mapping SourceElement::toTarget() : TargetElement { name := self.name; }
				""", """
				var mapped := objs->xmap(i) i.toTarget();
				log('mapped=' + mapped->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "mapped=3");
	}

	@Test
	void forEach_onASingleValue_iteratesOnce_andOnNull_never() throws Exception {
		// OCL v2.4 §9.3.35 [B]: an arrow on a single value works as on oclAsSet()
		QvtoExecutionResult result = run("", """
				5->forEach(i) { log('five=' + i.repr()); };
				var n : SourceElement := null;
				n->forEach(i) { log('null-iterated'); };
				log('done');
				""");
		assertSuccess(result);
		assertLogged(result, "five=5");
		assertLogged(result, "done");
		assertTrue(result.diagnostics().stream().noneMatch(d -> d.getMessage().contains("null-iterated")),
				() -> result.diagnostics().toString());
	}

	@Test
	void breakInsideAnArrowMap_endsTheLoop() throws Exception {
		QvtoExecutionResult result = run("""
				    mapping SourceElement::toTarget() : TargetElement { name := self.name; }
				""", """
				var seen := 0;
				objs->xcollect(o | if (o.value = 2) then { break; } else { seen := seen + 1; } endif);
				log('seen=' + seen.repr());
				""");
		assertSuccess(result);
		assertLogged(result, "seen=1");
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
