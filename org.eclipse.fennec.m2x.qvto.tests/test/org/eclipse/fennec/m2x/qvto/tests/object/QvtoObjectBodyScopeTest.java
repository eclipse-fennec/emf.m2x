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
package org.eclipse.fennec.m2x.qvto.tests.object;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * The body of an object expression is the innermost scope: an unqualified property and the named
 * object variable refer to the object being created, before iterator variables and self
 * (§8.2.1.17, §8.2.2.24, #383).
 */
class QvtoObjectBodyScopeTest extends AbstractQvtoEngineTest {

	private static QvtoExecutionResult run(String body) throws Exception {
		return executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				%s
				}
				""".formatted(body), new BasicQvtoModelExtent(createSourceElement("a", 1), createSourceElement("b", 2)),
				emptyExtent());
	}

	@Test
	void anUnqualifiedProperty_inAnObjectBodyInsideANamedIterator_isTheNewObjects() throws Exception {
		QvtoExecutionResult result = run("""
				    helper SourceElement::make() : Sequence(TargetElement) {
				        var ids : OrderedSet(SourceElement) := s.objectsOfType(SourceElement)->sortedBy(value)->asOrderedSet();
				        return ids->collect(i | object TargetElement@t {
				            name := i.name + '!';
				        })->asSequence();
				    }
				    main() {
				        var made := s.rootObjects()->first().oclAsType(SourceElement).make();
				        log('made=' + made->collect(x | x.name)->joinfields(',', '', ''));
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "made=a!,b!");
	}

	@Test
	void aNamedObjectVariable_insideAnIterator_inAContextualMapping_isTheNewObject() throws Exception {
		QvtoExecutionResult result = run("""
				    query SourceElement::names() : Sequence(String) = Sequence{'x', 'y'};
				    mapping SourceElement::toT() : TargetContainer {
				        name := self.name;
				        elements += self.names()->collect(n | object c : TargetElement@t {
				            c.name := n + '-made';
				        });
				    }
				    main() {
				        var r := s.rootObjects()->first().oclAsType(SourceElement).map toT();
				        log('children=' + r.elements->collect(x | x.name)->joinfields(',', '', '') + ' name=' + r.name);
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "children=x-made,y-made name=a");
	}

	@Test
	void anUnnamedObject_insideAnIterator_inAContextualMapping_isTheImplicitTarget() throws Exception {
		// emf.matter's Cluster::toPackage before its workaround: object EClass { name := … } in a
		// collect over Strings, in a mapping whose self also has a name
		QvtoExecutionResult result = run("""
				    query SourceElement::names() : Sequence(String) = Sequence{'x', 'y'};
				    mapping SourceElement::toT() : TargetContainer {
				        name := self.name;
				        elements += self.names()->collect(n | object TargetElement {
				            name := n + '-made';
				            label := 'l';
				        });
				    }
				    main() {
				        var r := s.rootObjects()->first().oclAsType(SourceElement).map toT();
				        log('children=' + r.elements->collect(x | x.name)->joinfields(',', '', '') + ' name=' + r.name);
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "children=x-made,y-made name=a");
	}

	@Test
	void aNamedObject_withoutExtent_insideAnIterator_inAContextualMapping() throws Exception {
		QvtoExecutionResult result = run("""
				    query SourceElement::names() : Sequence(String) = Sequence{'x', 'y'};
				    mapping SourceElement::toT() : TargetContainer {
				        name := self.name;
				        elements += self.names()->collect(n | object c : TargetElement {
				            c.name := n + '-made';
				        });
				    }
				    main() {
				        var r := s.rootObjects()->first().oclAsType(SourceElement).map toT();
				        log('children=' + r.elements->collect(x | x.name)->joinfields(',', '', '') + ' name=' + r.name);
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "children=x-made,y-made name=a");
	}

	@Test
	void theXcollectIterator_doesNotTakeTheObjectsProperty() throws Exception {
		QvtoExecutionResult result = run("""
				    main() {
				        var ids : OrderedSet(SourceElement) := s.objectsOfType(SourceElement)->sortedBy(value)->asOrderedSet();
				        var made := ids->xcollect(i | object TargetElement@t { name := i.name + '!'; });
				        log('made=' + made->collect(x | x.name)->joinfields(',', '', ''));
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "made=a!,b!");
	}

	@Test
	void theObjectShorthandIterator_doesNotTakeTheObjectsProperty() throws Exception {
		QvtoExecutionResult result = run("""
				    main() {
				        var ids : OrderedSet(SourceElement) := s.objectsOfType(SourceElement)->sortedBy(value)->asOrderedSet();
				        var made := ids->object(a) TargetElement@t { name := a.name + '!'; };
				        log('made=' + made->collect(x | x.name)->joinfields(',', '', ''));
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "made=a!,b!");
	}

	@Test
	void theIterateIterator_doesNotTakeTheObjectsProperty() throws Exception {
		QvtoExecutionResult result = run("""
				    main() {
				        var ids : OrderedSet(SourceElement) := s.objectsOfType(SourceElement)->sortedBy(value)->asOrderedSet();
				        var made := ids->iterate(i; acc : Sequence(TargetElement) = Sequence{} | acc->append(object TargetElement@t { name := i.name + '!'; }));
				        log('made=' + made->collect(x | x.name)->joinfields(',', '', ''));
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "made=a!,b!");
	}

	@Test
	void anAnonymousIterator_doesNotTakeTheObjectsProperty() throws Exception {
		QvtoExecutionResult result = run("""
				    main() {
				        var ids : OrderedSet(SourceElement) := s.objectsOfType(SourceElement)->sortedBy(value)->asOrderedSet();
				        var made := ids->collect(object TargetElement@t { name := 'x'; });
				        log('made=' + made->collect(x | x.name)->joinfields(',', '', ''));
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "made=x,x");
	}

	@Test
	void nestedObjects_eachAnswerForTheirOwnBody() throws Exception {
		QvtoExecutionResult result = run("""
				    main() {
				        var ids : OrderedSet(SourceElement) := s.objectsOfType(SourceElement)->sortedBy(value)->asOrderedSet();
				        var outer := ids->collect(o | object TargetContainer@t {
				            name := 'outer-' + o.name;
				            elements += ids->collect(i | object TargetElement@t { name := o.name + i.name; });
				        })->first();
				        log('outer=' + outer.name + ' inner=' + outer.elements->collect(x | x.name)->joinfields(',', '', ''));
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "outer=outer-a inner=aa,ab");
	}

	@Test
	void anIteratorInsideAnObjectBody_stillTakesTheName() throws Exception {
		// The object body ends the search outward, not inward: a collect inside it binds its own
		// iterator (#320)
		QvtoExecutionResult result = run("""
				    main() {
				        var ids : OrderedSet(SourceElement) := s.objectsOfType(SourceElement)->sortedBy(value)->asOrderedSet();
				        var made := object TargetElement@t { label := ids->collect(p | name)->joinfields(',', '', ''); };
				        log('label=' + made.label);
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "label=a,b");
	}

	@Test
	void aCallersVariable_isNotVisibleInTheCalledMapping() throws Exception {
		// emf.matter: concrete->forEach(c) { … c.map toPackage(); } — inside the mapping, c is the
		// caller's variable and must not be seen (§8.2.1.15: a mapping's scope is its own)
		QvtoExecutionResult result = run("""
				    query SourceElement::names() : Sequence(String) = Sequence{'x', 'y'};
				    mapping SourceElement::toT() : TargetContainer {
				        name := self.name;
				        elements += self.names()->collect(n | object c : TargetElement {
				            c.name := n + '-made';
				        });
				        elements += self.names()->collect(n | object TargetElement {
				            name := n + '-anon';
				        });
				    }
				    main() {
				        s.objectsOfType(SourceElement)->sortedBy(value)->forEach(c) {
				            var r := c.map toT();
				            log('children=' + r.elements->collect(x | x.name)->joinfields(',', '', '') + ' name=' + r.name);
				        };
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "children=x-made,y-made,x-anon,y-anon name=a");
	}

	@Test
	void aHelper_seesNoLocalOfItsCaller_butModulePropertiesAndModels() throws Exception {
		// §8.2.1.17: "contained in the scope of the operation definition" — lexical scoping
		QvtoExecutionResult result = run("""
				    property shared : String = 'module';
				    helper peek() : String { return secret.oclIsUndefined().repr() + ' ' + shared + ' ' + s.rootObjects()->size().repr(); }
				    main() {
				        var secret := 'caller-local';
				        log('peek=' + peek());
				    }
				""");
		assertLogged(result, "peek=true module 2");
	}

	@Test
	void aMappingCalledInACallersObjectBody_hasItsOwnImplicitTarget() throws Exception {
		// The caller's object expression is not the implicit target inside the called mapping
		QvtoExecutionResult result = run("""
				    mapping SourceElement::toT() : TargetElement { name := 'mapped-' + self.name; }
				    main() {
				        var e := s.objectsOfType(SourceElement)->sortedBy(value)->first();
				        var holder := object TargetContainer@t {
				            name := 'holder';
				            elements += e.map toT();
				        };
				        log('holder=' + holder.name + ' child=' + holder.elements->first().name);
				    }
				""");
		assertSuccess(result);
		assertLogged(result, "holder=holder child=mapped-a");
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
