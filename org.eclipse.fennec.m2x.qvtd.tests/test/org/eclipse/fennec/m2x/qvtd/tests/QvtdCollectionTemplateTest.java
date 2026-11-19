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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Collection templates (§7.11.2.3, §7.11.2.4, #354): members match distinct elements, the rest is
 * what they leave, a Sequence's members stand at its head, the template's where filters, a
 * single-valued property is a singleton collection — and the kind is kept in the AST.
 */
class QvtdCollectionTemplateTest extends AbstractQvtdEngineTest {

	/** A relation creating one Table per binding, named by {@code %s}, over the template {@code %s}. */
	private static String tables(String declarations, String template, String tableName) {
		return """
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation R {
						%s
						checkonly domain uml p : Package { ownedClass = %s };
						enforce domain rdbms t : Table { name = %s };
					}
				}
				""".formatted(declarations, template, tableName);
	}

	private static List<String> run(String source, String... classes) throws QvtdParseException {
		EObject pkg = createPackage("P");
		for (String name : classes) {
			addClassToPackage(pkg, createClass(name, null));
		}
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(source, "rdbms", Map.of("uml", QvtdModelExtent.of(pkg), "rdbms", rdbms));
		assertTrue(result.isSuccess(), "" + result.diagnostics());
		return rdbms.getContents().stream().map(o -> getName(o)).sorted().toList();
	}

	@Test
	@DisplayName("the rest excludes the element a variable member matched")
	void collectionTemplate_restExcludesVariableMember() throws QvtdParseException {
		assertEquals(List.of("C1:2", "C2:2", "C3:2"), run(tables("c : Class; rest : Set(Class);",
				"cs : Set(Class) { c ++ rest }", "c.name.concat(':').concat(rest->size().toString())"), "C1", "C2", "C3"));
	}

	@Test
	@DisplayName("one element: the rest is empty")
	void collectionTemplate_singleElementRestEmpty() throws QvtdParseException {
		assertEquals(List.of("C1:0"), run(tables("c : Class; rest : Set(Class);",
				"cs : Set(Class) { c ++ rest }", "c.name.concat(':').concat(rest->size().toString())"), "C1"));
	}

	@Test
	@DisplayName("two members match distinct elements")
	void collectionTemplate_twoMembersDistinct() throws QvtdParseException {
		assertEquals(List.of("AB", "BA"), run(tables("c1, c2 : Class;",
				"cs : Set(Class) { c1, c2 ++ _ }", "c1.name.concat(c2.name)"), "A", "B"));
	}

	@Test
	@DisplayName("two members, one element: no match")
	void collectionTemplate_twoMembersOneElement_noMatch() throws QvtdParseException {
		assertEquals(List.of(), run(tables("c1, c2 : Class;",
				"cs : Set(Class) { c1, c2 ++ _ }", "c1.name.concat(c2.name)"), "A"));
	}

	@Test
	@DisplayName("no rest: the members must take every element, in each order")
	void collectionTemplate_noRest_permutations() throws QvtdParseException {
		String source = tables("c1, c2 : Class;", "cs : Set(Class) { c1, c2 }", "c1.name.concat(c2.name)");
		assertEquals(List.of("AB", "BA"), run(source, "A", "B"));
		assertEquals(List.of(), run(source, "A", "B", "C"), "a third element is left over");
	}

	@Test
	@DisplayName("Sequence: the member is the head of the sequence")
	void sequenceTemplate_memberAtHead() throws QvtdParseException {
		assertEquals(List.of("A"), run(tables("c : Class; rest : Sequence(Class);",
				"cs : Sequence(Class) { c ++ rest }", "c.name"), "A", "B", "C"));
	}

	@Test
	@DisplayName("Sequence: a member that matches only a later element does not match")
	void sequenceTemplate_memberNotAtHead_noMatch() throws QvtdParseException {
		assertEquals(List.of(), run(tables("rest : Sequence(Class);",
				"cs : Sequence(Class) { c : Class { name = 'B' } ++ rest }", "c.name"), "A", "B"));
	}

	@Test
	@DisplayName("the template's where condition filters the match")
	void collectionTemplate_where() throws QvtdParseException {
		assertEquals(List.of("A", "B"), run(tables("c : Class;", "cs : Set(Class) { c ++ _ } { cs->size() > 1 }", "c.name"),
				"A", "B"));
		assertEquals(List.of(), run(tables("c : Class;", "cs : Set(Class) { c ++ _ } { cs->size() > 5 }", "c.name"),
				"A", "B"));
	}

	private static final String OWNING_PACKAGE = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				top relation R {
					cn : String;
					q : Package;
					checkonly domain uml c : Class { name = cn, owningPackage = qs : Set(Package) { q } };
					enforce domain rdbms t : Table { name = cn };
				}
			}
			""";

	@Test
	@DisplayName("a single-valued property is a singleton collection")
	void collectionTemplate_singleValuedProperty() throws QvtdParseException {
		assertEquals(List.of("A"), run(OWNING_PACKAGE, "A"));
	}

	@Test
	@DisplayName("a single-valued property that is null is the empty collection: no member matches")
	void collectionTemplate_singleValuedNull_noMatch() throws QvtdParseException {
		QvtdModelExtent rdbms = emptyExtent();
		executeEnforce(OWNING_PACKAGE, "rdbms", Map.of("uml", QvtdModelExtent.of(createClass("Loose", null)), "rdbms", rdbms));
		assertEquals(List.of(), rdbms.getContents());
	}

	@Test
	@DisplayName("a Bag template on an ordered set property is a parse error")
	void collectionTemplate_disallowedKind_parseError() {
		QvtdParseException e = assertThrows(QvtdParseException.class,
				() -> parse(tables("c : Class;", "cs : Bag(Class) { c ++ _ }", "c.name")));
		assertTrue(e.getMessage().contains("A Bag template cannot match 'ownedClass', which is a OrderedSet"), e.getMessage());
	}

	@Test
	@DisplayName("a collection template as the root of a domain is a parse error")
	void collectionTemplateRoot_rejected() {
		QvtdParseException e = assertThrows(QvtdParseException.class, () -> parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation R {
						c : Class;
						checkonly domain uml cs : Set(Class) { c ++ _ };
						enforce domain rdbms t : Table {};
					}
				}
				"""));
		assertTrue(e.getMessage().contains("A collection template cannot be the root of a domain"), e.getMessage());
	}

	private static final String QUERY = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				query names(s : Set(String)) : Sequence(String) { s->asSequence() }
				top relation R {
					c : Class;
					rest : Sequence(Class);
					checkonly domain uml p : Package { ownedClass = cs : Sequence(Class) { c ++ rest } };
					enforce domain rdbms t : Table { name = c.name };
				}
			}
			""";

	@Test
	@DisplayName("a query keeps the collection types of its parameter and result")
	void query_collectionTypes_kept() throws QvtdParseException {
		RelationalTransformation t = parse(QUERY);
		Function names = null;
		for (var it = t.eAllContents(); names == null && it.hasNext();) {
			if (it.next() instanceof Function f) {
				names = f;
			}
		}
		assertEquals(CollectionKind.SET,
				assertInstanceOf(CollectionType.class, names.getEParameters().get(0).getEType()).getKind());
		assertEquals(CollectionKind.SEQUENCE, assertInstanceOf(CollectionType.class, names.getEType()).getKind());
	}

	@Test
	@DisplayName("a compiled unit with collection types is self-contained")
	void compile_collectionTypes_selfContained() throws QvtdParseException {
		assertEquals(List.of(), SatelliteCollector.find(engine.compile(QUERY, "T")));
	}
}
