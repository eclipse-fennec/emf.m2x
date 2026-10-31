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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.Key;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomainAssignment;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * QVT 1.3 §7.13.7: the abstract syntax each concrete construct maps to (#362). The engine tests
 * show what a transformation does; these show that the AST says what the text says, which is
 * what tooling and compiled units read.
 */
@DisplayName("§7.13.7: Concrete to abstract syntax mapping")
class QvtdAbstractSyntaxMappingTest extends AbstractQvtdEngineTest {

	private static Relation relation(RelationalTransformation t, String name) {
		return t.getRule().stream().filter(r -> name.equals(r.getName()))
				.map(Relation.class::cast).findFirst().orElseThrow(() -> new AssertionError("no relation " + name));
	}

	private static RelationDomain domain(Relation r, String modelId) {
		return r.getDomain().stream().map(RelationDomain.class::cast)
				.filter(d -> modelId.equals(d.getTypedModel() != null ? d.getTypedModel().getName() : d.getName()))
				.findFirst().orElseThrow(() -> new AssertionError("no domain " + modelId));
	}

	private static TemplateExp template(RelationDomain d) {
		return d.getPattern().get(0).getTemplateExpression();
	}

	private static List<String> names(List<? extends ENamedElement> elements) {
		return elements.stream().map(ENamedElement::getName).toList();
	}

	private static List<String> varNames(List<Variable> variables) {
		return variables.stream().map(Variable::getName).toList();
	}

	@Test
	@DisplayName("transformationCS: name, modelParameter with usedPackage, extends, ownedKey, rule, queries")
	void transformation() throws QvtdParseException {
		RelationalTransformation t = parse("""
				transformation Base(uml : simpleuml, rdbms : simplerdbms) {
				}
				transformation T(uml : { simpleuml, simplerdbms }, rdbms : simplerdbms) extends Base {
					key Table {name};
					query Upper(s : String) : String { s.toUpperCase() }
					top relation R {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
					}
				}
				""");
		assertEquals("T", t.getName());
		assertEquals(List.of("uml", "rdbms"), t.getModelParameter().stream().map(TypedModel::getName).toList());
		assertEquals(List.of("simpleuml", "simplerdbms"), names(t.getModelParameter().get(0).getUsedPackage()));
		assertNotNull(t.getExtends(), "extends Base");
		assertEquals("Base", t.getExtends().getName());
		assertEquals(1, t.getOwnedKey().size());
		assertEquals(List.of("R"), names(t.getRule()));
		List<Function> queries = allOf(t, Function.class);
		assertEquals(List.of("Upper"), names(queries));
		assertNotNull(queries.get(0).getQueryExpression(), "the query's body");
	}

	@Test
	@DisplayName("keyDeclCS: identifies, part, oppositePart")
	void key() throws QvtdParseException {
		RelationalTransformation t = parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					key Table {name, opposite(Schema::ownedTable)};
				}
				""");
		Key key = t.getOwnedKey().get(0);
		assertEquals("Table", key.getIdentifies().getName());
		assertEquals(List.of("name"), names(key.getPart()));
		assertEquals(List.of("ownedTable"), names(key.getOppositePart()));
		assertEquals("Schema", key.getOppositePart().get(0).getEContainingClass().getName());
	}

	@Test
	@DisplayName("relationCS: isTopLevel, isAbstract, variable, when, where")
	void relation() throws QvtdParseException {
		RelationalTransformation t = parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					abstract relation Base {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
					}
					top relation R overrides Base {
						pn, sn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = sn };
						when { pn <> ''; }
						where { sn = pn; pn <> 'x'; }
					}
				}
				""");
		Relation base = relation(t, "Base");
		Relation r = relation(t, "R");
		assertFalse(base.isIsTopLevel());
		assertTrue(base.isIsAbstract());
		assertTrue(r.isIsTopLevel());
		assertFalse(r.isIsAbstract());
		assertTrue(varNames(r.getVariable()).containsAll(List.of("pn", "sn", "p", "s")),
				"declared and root variables: " + varNames(r.getVariable()));
		assertEquals(1, r.getWhen().getPredicate().size());
		assertEquals(2, r.getWhere().getPredicate().size());
	}

	@Test
	@Disabled("#356: Rule.overrides is not set — the overriding relation is linked by name only")
	@DisplayName("relationCS: overrides refers to the overridden relation")
	void overrides() throws QvtdParseException {
		RelationalTransformation t = parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					abstract relation Base {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
					}
					top relation R overrides Base {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
					}
				}
				""");
		assertSame(relation(t, "Base"), relation(t, "R").getOverrides());
	}

	@Test
	@DisplayName("varDeclarationCS: one Variable per name, the initializer as its init")
	void variableDeclaration() throws QvtdParseException {
		Relation r = relation(parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation R {
						a, b : String;
						n : String = 'X';
						checkonly domain uml p : Package { name = n };
						enforce domain rdbms s : Schema { name = a + b };
					}
				}
				"""), "R");
		Variable n = r.getVariable().stream().filter(v -> "n".equals(v.getName())).findFirst().orElseThrow();
		assertNotNull(n.getOwnedInit(), "n : String = 'X'");
		assertTrue(varNames(r.getVariable()).containsAll(List.of("a", "b")));
	}

	@Test
	@DisplayName("modelDomainCS: isCheckable/isEnforceable, typedModel, rootVariable, pattern")
	void modelDomain() throws QvtdParseException {
		RelationalTransformation t = parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation R {
						checkonly domain uml p : Package {}, c : Class {};
						enforce domain rdbms s : Schema {};
					}
					top relation Plain {
						domain uml p : Package {};
						enforce domain rdbms s : Schema {};
					}
				}
				""");
		Relation r = relation(t, "R");
		RelationDomain uml = domain(r, "uml");
		RelationDomain rdbms = domain(r, "rdbms");
		assertTrue(uml.isIsCheckable());
		assertFalse(uml.isIsEnforceable());
		assertTrue(rdbms.isIsCheckable());
		assertTrue(rdbms.isIsEnforceable());
		assertSame(t.getModelParameter().get(0), uml.getTypedModel());
		assertEquals(List.of("p", "c"), uml.getRootVariable().stream().map(Variable::getName).toList(),
				"a domain with two templates has two root variables");
		assertSame(uml.getRootVariable().get(0), template(uml).getBindsTo());

		// No qualifier: neither checkable nor enforceable (§7.13.7 modelDomainCS)
		RelationDomain plain = domain(relation(t, "Plain"), "uml");
		assertFalse(plain.isIsCheckable());
		assertFalse(plain.isIsEnforceable());
	}

	@Test
	@DisplayName("modelDomainCS: default_values become RelationDomainAssignments of variables")
	void defaultValues() throws QvtdParseException {
		Relation r = relation(parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation R {
						n : String;
						checkonly domain uml p : Package { name = n };
						enforce domain rdbms s : Schema { name = n }
							default_values { n = 'unnamed'; };
					}
				}
				"""), "R");
		List<RelationDomainAssignment> assignments = domain(r, "rdbms").getDefaultAssignment();
		assertEquals(1, assignments.size());
		Variable n = r.getVariable().stream().filter(v -> "n".equals(v.getName())).findFirst().orElseThrow();
		assertSame(n, assignments.get(0).getVariable(), "§7.11.3.7: it assigns the relation's variable n");
		assertNotNull(assignments.get(0).getValueExp());
	}

	@Test
	@DisplayName("primitiveTypeDomainCS: rootVariable with the name and type")
	void primitiveDomain() throws QvtdParseException {
		Relation r = relation(parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					relation R {
						primitive domain n : String;
						checkonly domain uml p : Package { name = n };
						enforce domain rdbms s : Schema { name = n };
					}
				}
				"""), "R");
		RelationDomain primitive = r.getDomain().stream().map(RelationDomain.class::cast)
				.filter(d -> d.getTypedModel() == null).findFirst().orElseThrow();
		Variable n = primitive.getRootVariable().get(0);
		assertEquals("n", n.getName());
		assertEquals("String", n.getType().getName());
	}

	@Test
	@DisplayName("objectTemplateCS / propertyTemplateCS: referredClass, part, isOpposite, where")
	void objectTemplate() throws QvtdParseException {
		Relation r = relation(parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation R {
						an : String;
						checkonly domain uml a : Attribute {
							name = an,
							opposite(Class::ownedAttribute) = c : Class {}
						} { an <> '' };
						enforce domain rdbms s : Schema {};
					}
				}
				"""), "R");
		ObjectTemplateExp attribute = assertInstanceOf(ObjectTemplateExp.class, template(domain(r, "uml")));
		assertEquals("Attribute", attribute.getReferredClass().getName());
		assertNotNull(attribute.getWhere(), "the template guard { an <> '' }");
		List<PropertyTemplateItem> parts = attribute.getPart();
		assertEquals(2, parts.size());
		assertEquals("name", parts.get(0).getReferredProperty().getName());
		assertFalse(parts.get(0).isIsOpposite());
		assertEquals("ownedAttribute", parts.get(1).getReferredProperty().getName());
		assertTrue(parts.get(1).isIsOpposite());
	}

	private static CollectionTemplateExp collectionTemplate(String kind) throws QvtdParseException {
		Relation r = relation(parse("""
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					top relation R {
						c : Class;
						rest : %s(Class);
						checkonly domain uml p : Package { ownedClass = cs : %s(Class) { c ++ rest } };
						enforce domain rdbms s : Schema {};
					}
				}
				""".formatted(kind, kind)), "R");
		ObjectTemplateExp pkg = (ObjectTemplateExp) template(domain(r, "uml"));
		return assertInstanceOf(CollectionTemplateExp.class, pkg.getPart().get(0).getValue());
	}

	@Test
	@DisplayName("collectionTemplateCS: member and rest")
	void collectionTemplateMembers() throws QvtdParseException {
		CollectionTemplateExp cs = collectionTemplate("Set");
		assertEquals(1, cs.getMember().size());
		assertEquals("rest", cs.getRest().getName());
	}

	@Test
	@DisplayName("collectionTemplateCS: referredCollectionType is the collection type of each kind")
	void collectionTemplateType() throws QvtdParseException {
		// Not Bag: ownedClass is an ordered set, which §7.11.2.4 lets a Set, Sequence or
		// OrderedSet template match — a Bag template is a parse error (QvtdCollectionTemplateTest)
		for (CollectionKind kind : List.of(CollectionKind.SET, CollectionKind.SEQUENCE,
				CollectionKind.ORDERED_SET)) {
			CollectionType type = assertInstanceOf(CollectionType.class,
					collectionTemplate(kind.getLiteral()).getReferredCollectionType(), kind + ": referredCollectionType");
			assertEquals(kind, type.getKind(), kind + ": the collection kind");
			assertEquals("Class", type.getElementType().getName());
		}
	}

	private static <T> List<T> allOf(EObject root, Class<T> type) {
		List<T> found = new ArrayList<>();
		root.eAllContents().forEachRemaining(o -> {
			if (type.isInstance(o)) {
				found.add(type.cast(o));
			}
		});
		return found;
	}
}
