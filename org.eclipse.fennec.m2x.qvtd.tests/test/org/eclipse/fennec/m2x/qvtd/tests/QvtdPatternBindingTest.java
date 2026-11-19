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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * How a domain pattern binds its variables (#355) and how a relation's variables are declared
 * and initialized (#357).
 */
class QvtdPatternBindingTest extends AbstractQvtdEngineTest {

	private static String transformation(String body) {
		return "transformation T(uml : simpleuml, rdbms : simplerdbms) {\n" + body + "\n}\n";
	}

	private static List<String> names(QvtdModelExtent extent) {
		return extent.getContents().stream().map(o -> getName(o)).sorted().toList();
	}

	private static Relation relation(String source, String name) throws QvtdParseException {
		return parse(source).getRule().stream().filter(r -> name.equals(r.getName()))
				.map(Relation.class::cast).findFirst().orElseThrow();
	}

	private static RelationDomain domain(Relation relation, String model) {
		return relation.getDomain().stream().map(RelationDomain.class::cast)
				.filter(d -> d.getTypedModel() != null && model.equals(d.getTypedModel().getName()))
				.findFirst().orElseThrow();
	}

	private static List<Variable> variablesNamed(Relation relation, String name) {
		return relation.getVariable().stream().filter(v -> name.equals(v.getName())).toList();
	}

	@Nested
	@DisplayName("#355: pattern matching")
	class Matching {

		private static final String TWO_TEMPLATES = transformation("""
				top relation PackageClassToTable {
					pn, cn : String;
					checkonly domain uml p : Package { name = pn }, c : Class { name = cn };
					enforce domain rdbms t : Table { name = pn.concat('_').concat(cn) };
				}
				""");

		@Test
		@DisplayName("two templates of one domain are bound jointly: the cross product of their roots")
		void domain_twoTemplates_crossProduct() throws QvtdParseException {
			EObject p = createPackage("P");
			addClassToPackage(p, createClass("A", null));
			addClassToPackage(p, createClass("B", null));
			QvtdModelExtent rdbms = emptyExtent();

			QvtdExecutionResult result = executeEnforce(TWO_TEMPLATES, "rdbms", Map.of("uml", QvtdModelExtent.of(p), "rdbms", rdbms));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(List.of("P_A", "P_B"), names(rdbms));
		}

		@Test
		@DisplayName("two templates of one domain: without a match for the second there is no binding")
		void domain_twoTemplates_secondRootMissing_noBinding() throws QvtdParseException {
			QvtdModelExtent rdbms = emptyExtent();
			QvtdExecutionResult result = executeEnforce(TWO_TEMPLATES, "rdbms",
					Map.of("uml", QvtdModelExtent.of(createPackage("P")), "rdbms", rdbms));
			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(List.of(), names(rdbms));
		}

		@Test
		@DisplayName("two templates of one domain, checked: the joint binding without its Table is reported")
		void checkOnly_twoTemplates_missingTable_inconsistent() throws QvtdParseException {
			EObject p = createPackage("P");
			addClassToPackage(p, createClass("A", null));
			QvtdExecutionResult result = executeCheckOnly(TWO_TEMPLATES,
					Map.of("uml", QvtdModelExtent.of(p), "rdbms", emptyExtent()));
			assertFalse(result.isConsistent(), "" + result.diagnostics());
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(
					"no match for p = Package 'P', c = Class 'A'")), "" + result.diagnostics());
		}

		private static final String SAME_VARIABLE_TWICE = transformation("""
				top relation NameIsKind {
					n : String;
					checkonly domain uml c : Class { name = n, kind = n };
					enforce domain rdbms t : Table { type = 'hit' };
				}
				""");

		@Test
		@DisplayName("a variable bound to null is bound: name = null, kind = null matches")
		void binding_bothNull_matches() throws QvtdParseException {
			QvtdModelExtent rdbms = emptyExtent();
			executeEnforce(SAME_VARIABLE_TWICE, "rdbms",
					Map.of("uml", QvtdModelExtent.of(createClass(null, null)), "rdbms", rdbms));
			assertEquals(1, rdbms.getContents().size());
		}

		@Test
		@DisplayName("a variable bound to null is not rebound: name = null, kind = 'x' does not match")
		void binding_nullThenValue_noMatch() throws QvtdParseException {
			QvtdModelExtent rdbms = emptyExtent();
			executeEnforce(SAME_VARIABLE_TWICE, "rdbms",
					Map.of("uml", QvtdModelExtent.of(createClass(null, "x")), "rdbms", rdbms));
			assertEquals(List.of(), rdbms.getContents());
		}

		private static String owningClassOfType(String type) {
			return transformation("""
					top relation AttributeToColumn {
						an : String;
						checkonly domain uml a : Attribute { name = an, owningClass = k : %s {} };
						enforce domain rdbms s : Schema { name = an };
					}
					""".formatted(type));
		}

		private static QvtdModelExtent attributeOfAClass() {
			EObject cls = createClass("C", null);
			addAttributeToClass(cls, createAttribute("id", "Integer"));
			return QvtdModelExtent.of(cls);
		}

		@Test
		@DisplayName("a nested template on a single-valued reference matches an object of its type")
		void nestedTemplate_singleValued_rightType_matches() throws QvtdParseException {
			QvtdModelExtent rdbms = emptyExtent();
			executeEnforce(owningClassOfType("Class"), "rdbms", Map.of("uml", attributeOfAClass(), "rdbms", rdbms));
			assertEquals(List.of("id"), names(rdbms));
		}

		@Test
		@DisplayName("a nested template on a single-valued reference does not match an object of another type")
		void nestedTemplate_singleValued_wrongType_noMatch() throws QvtdParseException {
			QvtdModelExtent rdbms = emptyExtent();
			executeEnforce(owningClassOfType("Package"), "rdbms", Map.of("uml", attributeOfAClass(), "rdbms", rdbms));
			assertEquals(List.of(), rdbms.getContents());
		}

		/** Klass.attribute : Attr[*], containment, with no eOpposite — §7.3's case for opposite(). */
		private record Klasses(QvtdEngine engine, EClass klass, EClass attr, EReference attribute) {

			EObject create(EClass type, String name) {
				EObject object = EcoreUtil.create(type);
				object.eSet(type.getEStructuralFeature("name"), name);
				return object;
			}
		}

		private static Klasses klasses() {
			EPackage mm = EcoreFactory.eINSTANCE.createEPackage();
			mm.setName("klasses");
			mm.setNsURI("http://example.org/m2x/qvtr-klasses/1.0");
			mm.setNsPrefix("klasses");
			EClass klass = named(mm, "Klass");
			EClass attr = named(mm, "Attr");
			EReference attribute = EcoreFactory.eINSTANCE.createEReference();
			attribute.setName("attribute");
			attribute.setEType(attr);
			attribute.setUpperBound(-1);
			attribute.setContainment(true);
			klass.getEStructuralFeatures().add(attribute);
			EPackage.Registry registry = new EPackageRegistryImpl();
			registry.put(mm.getNsURI(), mm);
			registry.put(rdbmsPackage.getNsURI(), rdbmsPackage);
			QvtdEngine engine = QvtdEngines.create(QvtdConfiguration.builder(
					OclConfiguration.builder(new OclParserSupport()).build()).packageRegistry(registry).build());
			return new Klasses(engine, klass, attr, attribute);
		}

		private static EClass named(EPackage pkg, String name) {
			EClass eClass = EcoreFactory.eINSTANCE.createEClass();
			eClass.setName(name);
			EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
			nameAttr.setName("name");
			nameAttr.setEType(EcorePackage.Literals.ESTRING);
			eClass.getEStructuralFeatures().add(nameAttr);
			pkg.getEClassifiers().add(eClass);
			return eClass;
		}

		private static final String OPPOSITE = """
				transformation T(m : klasses, rdbms : simplerdbms) {
					top relation OwnedAttrToSchema {
						an : String;
						%s domain m a : Attr { name = an, opposite(Klass::attribute) = k : Klass { name = 'K' } };
						%s domain rdbms s : Schema { name = an };
					}
				}
				""";

		@SuppressWarnings("unchecked")
		@Test
		@DisplayName("opposite() of a role without eOpposite matches the attribute its Klass holds")
		void opposite_nonNavigable_matches() throws QvtdParseException {
			Klasses k = klasses();
			EObject klass = k.create(k.klass(), "K");
			((List<EObject>) klass.eGet(k.attribute())).add(k.create(k.attr(), "owned"));
			EObject orphan = k.create(k.attr(), "orphan");
			QvtdModelExtent rdbms = QvtdModelExtent.of();

			QvtdExecutionResult result = k.engine().execute(k.engine().parse(OPPOSITE.formatted("checkonly", "enforce"), "T"),
					QvtdExecutionContext.enforce("rdbms", Map.of("m", QvtdModelExtent.of(klass, orphan), "rdbms", rdbms)));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(List.of("owned"), names(rdbms), "the orphan belongs to no Klass");
		}

		@SuppressWarnings("unchecked")
		@Test
		@DisplayName("opposite() of a role without eOpposite, enforced: the Klass holds the new attribute")
		void opposite_nonNavigable_enforceAddsToOwner() throws QvtdParseException {
			Klasses k = klasses();
			EObject schema = EcoreUtil.create(ecoreHelper.getEClass(rdbmsPackage, "Schema"));
			schema.eSet(schema.eClass().getEStructuralFeature("name"), "id");
			QvtdModelExtent m = QvtdModelExtent.of();

			QvtdExecutionResult result = k.engine().execute(k.engine().parse(OPPOSITE.formatted("enforce", "checkonly"), "T"),
					QvtdExecutionContext.enforce("m", Map.of("m", m, "rdbms", QvtdModelExtent.of(schema))));

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, m.getContents().size(), "the Klass is the root");
			EObject klass = m.getContents().get(0);
			assertEquals(List.of("id"), ((List<EObject>) klass.eGet(k.attribute())).stream().map(a -> getName(a)).toList());
		}
	}

	@Nested
	@DisplayName("#357: variables and their initializers")
	class Variables {

		@Test
		@DisplayName("an initializer is the variable's value")
		void varInit_usedAsValue() throws QvtdParseException {
			QvtdModelExtent rdbms = emptyExtent();
			QvtdExecutionResult result = executeEnforce(transformation("""
					top relation R {
						n : String = 'FROM_INIT';
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema { name = n };
					}
					"""), "rdbms", Map.of("uml", QvtdModelExtent.of(createPackage("P")), "rdbms", rdbms));
			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(List.of("FROM_INIT"), names(rdbms));
		}

		@Test
		@DisplayName("an initializer is a fixed value: it constrains what the domains match")
		void varInit_constrainsMatch() throws QvtdParseException {
			QvtdModelExtent rdbms = emptyExtent();
			executeEnforce(transformation("""
					top relation R {
						n : String = 'X';
						checkonly domain uml p : Package { name = n };
						enforce domain rdbms s : Schema { name = n };
					}
					"""), "rdbms", Map.of("uml", QvtdModelExtent.of(createPackage("X"), createPackage("Y")), "rdbms", rdbms));
			assertEquals(List.of("X"), names(rdbms));
		}

		@Test
		@DisplayName("a list of variables with an initializer: each gets its own")
		void varInit_list_eachVariableInitialized() throws QvtdParseException {
			Relation r = relation(transformation("""
					top relation R {
						a, b : String = 'x';
						checkonly domain uml p : Package { name = a };
						enforce domain rdbms s : Schema { name = b };
					}
					"""), "R");
			Variable a = variablesNamed(r, "a").get(0);
			Variable b = variablesNamed(r, "b").get(0);
			assertNotNull(a.getOwnedInit());
			assertNotNull(b.getOwnedInit());
			assertNotSame(a.getOwnedInit(), b.getOwnedInit());
		}

		@Test
		@DisplayName("a nested template binds the declared variable of its name, not a second one")
		void ast_nestedTemplateReusesDeclaredVariable() throws QvtdParseException {
			Relation r = relation(transformation("""
					top relation R {
						cn : String;
						p : Package;
						checkonly domain uml c : Class { name = cn, owningPackage = p : Package {} };
						enforce domain rdbms t : Table { name = cn };
					}
					"""), "R");
			List<Variable> ps = variablesNamed(r, "p");
			assertEquals(1, ps.size());
			ObjectTemplateExp cls = (ObjectTemplateExp) domain(r, "uml").getPattern().get(0).getTemplateExpression();
			ObjectTemplateExp nested = (ObjectTemplateExp) cls.getPart().get(1).getValue();
			assertSame(ps.get(0), nested.getBindsTo());
		}

		@Test
		@DisplayName("an undeclared nested template variable is one of the relation's variables")
		void ast_undeclaredNestedVariableInRelationVariables() throws QvtdParseException {
			Relation r = relation(transformation("""
					top relation R {
						cn : String;
						checkonly domain uml c : Class { name = cn, owningPackage = q : Package {} };
						enforce domain rdbms t : Table { name = cn };
					}
					"""), "R");
			assertEquals(1, variablesNamed(r, "q").size());
		}

		@Test
		@DisplayName("the rest of a collection template is the declared variable")
		void ast_restIsDeclaredVariable() throws QvtdParseException {
			Relation r = relation(transformation("""
					top relation R {
						c : Class;
						rest : Set(Class);
						checkonly domain uml p : Package { ownedClass = cs : Set(Class) { c ++ rest } };
						enforce domain rdbms s : Schema {};
					}
					"""), "R");
			ObjectTemplateExp pkg = (ObjectTemplateExp) domain(r, "uml").getPattern().get(0).getTemplateExpression();
			CollectionTemplateExp cs = (CollectionTemplateExp) pkg.getPart().get(0).getValue();
			assertSame(variablesNamed(r, "rest").get(0), cs.getRest());
		}

		@Test
		@DisplayName("Pattern.bindsTo holds the variables the domain pattern and the when clause bind")
		void ast_patternBindsTo() throws QvtdParseException {
			Relation r = relation(transformation("""
					top relation PackageToSchema {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
					}
					top relation ClassToTable {
						cn : String;
						p : Package;
						s : Schema;
						checkonly domain uml c : Class { owningPackage = p, name = cn };
						enforce domain rdbms t : Table { schema = s, name = cn };
						when { PackageToSchema(p, s); }
					}
					"""), "ClassToTable");
			assertEquals(List.of("c", "p", "cn"),
					domain(r, "uml").getPattern().get(0).getBindsTo().stream().map(Variable::getName).toList());
			assertEquals(List.of("p", "s"), r.getWhen().getBindsTo().stream().map(Variable::getName).toList());
		}

		@Test
		@DisplayName("a template binding a declared variable of an unrelated type is a parse error")
		void templateVariable_typeConflict_diagnostic() {
			QvtdParseException e = assertThrows(QvtdParseException.class, () -> parse(transformation("""
					top relation R {
						p : Schema;
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
					}
					""")));
			assertTrue(e.getMessage().contains("Variable 'p' is declared as Schema, but the template binds it to a Package"),
					e.getMessage());
		}
	}
}
