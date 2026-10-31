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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * The List, Dict and collection operations of QVT v1.3 §8.3.8-§8.3.15 (#343).
 */
class QvtoListDictSpecConformanceTest extends AbstractQvtoEngineTest {

	private static QvtoExecutionResult run(String main) throws Exception {
		return execute("""
				transformation test() {
				    main() {
				%s
				    }
				}
				""".formatted(main));
	}

	@Test
	void listClone_isAShallowNewList() throws Exception {
		// §8.3.9.11: "Returns a new list that is a shallow clone of self"
		QvtoExecutionResult result = run("""
				var l := List{1, 2};
				var c := l->clone();
				c->add(3);
				log('l=' + l->size().repr() + ' c=' + c->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "l=2 c=3");
	}

	@Test
	void anImmutableCollectionsClone_isItself() throws Exception {
		// §8.3.11.2: "Collections are immutable so a clone returns self"
		QvtoExecutionResult result = run("""
				var s := Set{1, 2};
				log('same=' + (s->clone() = s).repr() + ' defined=' + s->clone().oclIsUndefined().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "same=true defined=false");
	}

	@Test
	void deepclone_clonesNestedLists() throws Exception {
		// §8.3.9.13: "a new list in which each element is in turn a deepclone"
		QvtoExecutionResult result = run("""
				var inner := List{1};
				var outer := List{inner};
				outer->deepclone()->first().oclAsType(List(Integer))->add(2);
				log('inner=' + inner->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "inner=1");
	}

	@Test
	void deepclone_keepsTheKind() throws Exception {
		// §8.3.12.3ff: "Returns a Set/Bag … that is a deep clone"
		QvtoExecutionResult result = run("""
				log('set=' + Set{1, 2}->deepclone()->including(1)->size().repr());
				log('bag=' + Bag{1, 1}->deepclone()->including(1)->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "set=2");
		assertLogged(result, "bag=3");
	}

	@Test
	void mutatingAnImmutableCollection_isAnError() throws Exception {
		// §8.3.11.2: "Collections are immutable"
		QvtoExecutionResult result = run("""
				var s : Set(Integer) := Set{1};
				s->add(2);
				var b : Bag(Integer) := Bag{1};
				b->remove(1);
				log('s=' + s->size().repr() + ' b=' + b->size().repr());
				""");
		assertFalse(result.isSuccess());
		assertError(result, "'add' changes a List, and a Set is immutable");
		assertError(result, "'remove' changes a List, and a Bag is immutable");
		assertLogged(result, "s=1 b=1");
	}

	@Test
	void insertAt_withTheObjectFirst_modifiesTheList() throws Exception {
		// §8.3.9.26: "The list is modifed to consist of self with object inserted at the
		// one-based position index"
		QvtoExecutionResult result = run("""
				var l := List{'a', 'b'};
				l->insertAt('x', 1);
				log('l=' + l->joinfields(',', '', ''));
				log('ocl=' + Sequence{'a'}->insertAt(1, 'y')->joinfields(',', '', ''));
				""");
		assertSuccess(result);
		assertLogged(result, "l=x,a,b");
		assertLogged(result, "ocl=y,a");
	}

	@Test
	void insertAt_outOfRange_isInvalid() throws Exception {
		QvtoExecutionResult result = run("""
				var l := List{'a'};
				log('r=' + l->insertAt('x', 5).oclIsInvalid().repr() + ' size=' + l->size().repr());
				""");
		assertLogged(result, "r=true size=1");
	}

	@Test
	void defaultget_answersAPresentNullValue() throws Exception {
		// §8.3.8: the default only "if the key does not exist"
		QvtoExecutionResult result = run("""
				var d : Dict(String, String) := Dict{};
				d->put('k', null);
				log('present=' + d->defaultget('k', 'dflt').oclIsUndefined().repr());
				log('absent=' + d->defaultget('x', 'dflt'));
				""");
		assertSuccess(result);
		assertLogged(result, "present=true");
		assertLogged(result, "absent=dflt");
	}

	@Test
	void dictKeys_isAList() throws Exception {
		// §8.3.8.8: "keys() : List(KeyT) Returns a new list of keys" — OCL's Map::keys() stays a Set
		QvtoExecutionResult result = run("""
				var d : Dict(String, Integer) := Dict{};
				d->put('a', 1);
				d->put('b', 2);
				var k := d->keys();
				k->add('c');
				log('first=' + k->first() + ' at2=' + k->at(2) + ' list=' + k->size().repr() + ' dict=' + d->size().repr());
				""");
		assertSuccess(result);
		assertLogged(result, "first=a at2=b list=3 dict=2");
	}

	@Test
	void joinfields_writesARealAsOclDoes() throws Exception {
		QvtoExecutionResult result = run("""
				log('j=' + List{1.0e10, null, 'a'}->joinfields('|', '[', ']'));
				""");
		assertSuccess(result);
		assertLogged(result, "j=[10000000000.0||a]");
	}

	@Test
	void removeAt_onAnEmptyList_isInvalid() throws Exception {
		QvtoExecutionResult result = run("""
				var l : List(String) := List{};
				log('at=' + l->removeAt(1).oclIsInvalid().repr() + ' first=' + l->removeFirst().oclIsInvalid().repr()
						+ ' last=' + l->removeLast().oclIsInvalid().repr());
				""");
		assertLogged(result, "at=true first=true last=true");
	}

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertError(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR && d.getMessage().contains(expected)),
				() -> "Expected the error '" + expected + "' but diagnostics were: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "Expected log containing '" + expected + "' but diagnostics were: " + result.diagnostics());
	}
}
