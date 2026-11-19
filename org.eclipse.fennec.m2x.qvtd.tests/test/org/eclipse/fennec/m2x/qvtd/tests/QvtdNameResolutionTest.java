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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Names a relation uses resolve or are positioned errors (#358), and §7.5 sees what is bound in
 * which direction (#359).
 */
class QvtdNameResolutionTest extends AbstractQvtdEngineTest {

	private static final String PACKAGE_TO_SCHEMA = """
			top relation PackageToSchema {
				pn : String;
				checkonly domain uml p : Package { name = pn };
				enforce domain rdbms s : Schema { name = pn };
			}
			""";

	private static String transformation(String body) {
		return "transformation T(uml : simpleuml, rdbms : simplerdbms) {\n" + body + "\n}\n";
	}

	private static List<String> schemas(String source, String... packages) throws QvtdParseException {
		QvtdModelExtent uml = QvtdModelExtent.of();
		for (String name : packages) {
			uml.getContents().add(createPackage(name));
		}
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = executeEnforce(source, "rdbms", Map.of("uml", uml, "rdbms", rdbms));
		assertTrue(result.isSuccess(), "" + result.diagnostics());
		return rdbms.getContents().stream().map(o -> getName(o)).sorted().toList();
	}

	/** A parse error with the message and the line the name stands on. */
	private static void rejected(String source, String message, int line) {
		QvtdParseException e = assertThrows(QvtdParseException.class, () -> parse(source));
		assertTrue(e.getErrors().stream().anyMatch(d -> d.getMessage().contains(message) && d.getLine() == line),
				() -> "expected '" + message + "' at line " + line + ": "
						+ e.getErrors().stream().map(d -> d.getLine() + ": " + d.getMessage()).toList());
	}

	@Nested
	@DisplayName("#358: unresolved names are positioned parse errors")
	class Unresolved {

		@Test
		void unknownTypedModel() {
			rejected(transformation("""
					top relation R {
						checkonly domain uml p : Package {};
						enforce domain zzz s : Schema {};
					}
					"""), "Unknown model 'zzz'", 4);
		}

		@Test
		void unknownTemplateClass() {
			rejected(transformation("""
					top relation R {
						checkonly domain uml p : Pakage {};
						enforce domain rdbms s : Schema {};
					}
					"""), "Unknown class (Pakage)", 3);
		}

		@Test
		void classFromForeignMetamodel() {
			rejected(transformation("""
					top relation R {
						checkonly domain uml x : Schema {};
						enforce domain rdbms s : Schema {};
					}
					"""), "Class Schema is not in a metamodel of model 'uml'", 3);
		}

		@Test
		void oppositeUnknownProperty() {
			rejected(transformation("""
					top relation R {
						checkonly domain uml a : Attribute { opposite(Class::nope) = c : Class {} };
						enforce domain rdbms s : Schema {};
					}
					"""), "Unknown property (opposite(Class::nope))", 3);
		}

		@Test
		void defaultValuesUnknownVariable() {
			rejected(transformation("""
					top relation R {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {} default_values { zz = 'x'; };
					}
					"""), "default_values assigns 'zz', which is no variable of relation 'R'", 4);
		}

		@Test
		void relationCallWrongArity() {
			rejected(transformation(PACKAGE_TO_SCHEMA + """
					top relation R {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
						when { PackageToSchema(p); }
					}
					"""), "Relation 'PackageToSchema' has 2 root variables, but is called with 1 arguments", 10);
		}

		@Test
		void relationCallWrongTypes() {
			rejected(transformation(PACKAGE_TO_SCHEMA + """
					top relation R {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
						when { PackageToSchema(s, p); }
					}
					"""), "Argument 1 of the call of 'PackageToSchema' is a Schema, but its root variable 'p' is a Package", 10);
		}

		@Test
		void relationCallUnknownRelation() {
			rejected(transformation("""
					top relation R {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
						when { Foo(p, s); }
					}
					"""), "Unknown relation or query 'Foo'", 5);
		}

		@Test
		void relationCallUnknownQualifier() {
			rejected(transformation(PACKAGE_TO_SCHEMA + """
					top relation R {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
						when { Other.PackageToSchema(p, s); }
					}
					"""), "Unknown transformation 'Other' in Other.PackageToSchema(…)", 10);
		}

		@Test
		void duplicateRelation() {
			rejected(transformation(PACKAGE_TO_SCHEMA + PACKAGE_TO_SCHEMA), "Duplicate relation 'PackageToSchema'", 7);
		}

		@Test
		void duplicateModelParameter() {
			rejected("""
					transformation T(uml : simpleuml, uml : simplerdbms) {
					}
					""", "Duplicate model parameter 'uml'", 1);
		}

		@Test
		void extendsUnknownTransformation() {
			rejected("""
					transformation T(uml : simpleuml, rdbms : simplerdbms) extends Nope {
					}
					""", "Transformation 'T' extends 'Nope', which is no transformation of this unit", 1);
		}

		@Test
		@DisplayName("a class resolves only in the metamodels of the declared model types")
		void classOfAnUndeclaredMetamodel_rejected() {
			rejected(transformation("""
					top relation R {
						checkonly domain uml c : EClass {};
						enforce domain rdbms s : Schema {};
					}
					"""), "Unknown class (EClass)", 3);
		}
	}

	@Nested
	@DisplayName("#358: relation calls, roots and domains")
	class Relations {

		@Test
		@DisplayName("a relation call qualified with the transformation's own name")
		void relationCallQualifiedWithOwnTransformation() throws QvtdParseException {
			assertEquals(List.of("HR"), schemas(transformation(PACKAGE_TO_SCHEMA + """
					top relation ClassToTable {
						cn : String;
						p : Package;
						s : Schema;
						checkonly domain uml c : Class { owningPackage = p, name = cn };
						enforce domain rdbms t : Table { schema = s, name = cn };
						when { T.PackageToSchema(p, s); }
					}
					"""), "HR"));
		}

		@Test
		@DisplayName("a relation call is a Boolean usable anywhere: when { not Marked(p); }, with a single-domain relation as predicate")
		void relationCall_negated() throws QvtdParseException {
			assertEquals(List.of("A", "B"), schemas(transformation("""
					top relation Marked {
						checkonly domain uml p : Package { name = 'X' };
					}
					top relation PackageToSchema {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
						when { not Marked(p); }
					}
					"""), "A", "B", "X"));
		}

		@Test
		@DisplayName("an anonymous domain root gets an implicit variable")
		void anonymousRoot_matches() throws QvtdParseException {
			assertEquals(List.of("HR"), schemas(transformation("""
					top relation R {
						pn : String;
						checkonly domain uml : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
					}
					"""), "HR"));
		}
	}

	@Nested
	@DisplayName("#359: §7.5 binding order")
	class BindingOrder {

		@Test
		@DisplayName("a declared variable nothing binds is an error")
		void section75_declaredNeverBound_targetExpression() {
			rejected(transformation("""
					top relation R {
						pn, x : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn.concat(x) };
					}
					"""), "Variable 'x'", 0);
		}

		@Test
		@DisplayName("a when clause reading a declared variable nothing binds is an error")
		void section75_whenReadsDeclaredUnbound() {
			rejected(transformation("""
					top relation R {
						pn, y : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
						when { y <> 'X'; }
					}
					"""), "Variable 'y'", 0);
		}

		@Test
		@DisplayName("an initialized variable is bound")
		void section75_initializedVariable_valid() throws QvtdParseException {
			assertEquals(List.of("HRa"), schemas(transformation("""
					top relation R {
						pn : String;
						x : String = 'a';
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn.concat(x) };
					}
					"""), "HR"));
		}

		@Test
		@DisplayName("a when clause may read what a source domain binds")
		void section75_whenReadsSourceDomainVariable_valid() throws QvtdParseException {
			assertEquals(List.of("HR"), schemas(transformation("""
					top relation R {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
						when { pn <> 'X'; }
					}
					"""), "HR", "X"));
		}

		private static final String ONLY_TARGET_BINDS = transformation("""
				top relation R {
					pn, x : String;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema { name = x };
					where { pn <> x; }
				}
				""");

		@Test
		@DisplayName("a variable only the enforced domain binds: an error before the run, nothing created")
		void section75_onlyTargetBinds_errorInThatDirection() throws QvtdParseException {
			QvtdModelExtent rdbms = emptyExtent();
			QvtdExecutionResult result = executeEnforce(ONLY_TARGET_BINDS, "rdbms",
					Map.of("uml", QvtdModelExtent.of(createPackage("HR")), "rdbms", rdbms));
			assertFalse(result.isSuccess());
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.ERROR && d.getMessage().contains(
					"§7.5: Variable 'x' of relation 'R' is not bound when enforcing 'rdbms'")), "" + result.diagnostics());
			assertEquals(List.of(), rdbms.getContents());
		}

		private static final String ONE_WAY = transformation("""
				top relation R {
					pn, x : String;
					%s domain uml p : Package { name = pn, ownedClass = c : Class { name = x } };
					%s domain rdbms s : Schema { name = x };
					where { pn <> x; }
				}
				""");

		@Test
		@DisplayName("a relation executable in one direction only: it runs that way, the other way is an error")
		void section75_executableInOneDirectionOnly() throws QvtdParseException {
			// x: both domains bind it; pn: only the uml domain
			QvtdExecutionResult towardsRdbms = executeEnforce(ONE_WAY.formatted("checkonly", "enforce"), "rdbms",
					Map.of("uml", QvtdModelExtent.of(), "rdbms", QvtdModelExtent.of()));
			assertTrue(towardsRdbms.diagnostics().stream().noneMatch(d -> d.getMessage().contains("§7.5")),
					"" + towardsRdbms.diagnostics());

			QvtdExecutionResult towardsUml = executeEnforce(ONE_WAY.formatted("enforce", "checkonly"), "uml",
					Map.of("uml", QvtdModelExtent.of(), "rdbms", QvtdModelExtent.of()));
			assertTrue(towardsUml.diagnostics().stream().anyMatch(d -> d.getMessage().contains(
					"§7.5: Variable 'pn' of relation 'R' is not bound when enforcing 'uml'")), "" + towardsUml.diagnostics());
		}

		@Test
		@DisplayName("a when clause reading the enforced domain's root: an error before the run")
		void section75_whenReadsTargetRoot() throws QvtdParseException {
			QvtdExecutionResult result = executeEnforce(transformation("""
					top relation R {
						pn : String;
						checkonly domain uml p : Package { name = pn };
						enforce domain rdbms s : Schema { name = pn };
						when { s.name <> 'X'; }
					}
					"""), "rdbms", Map.of("uml", QvtdModelExtent.of(createPackage("HR")), "rdbms", emptyExtent()));
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(
					"§7.5: Variable 's' of relation 'R' is not bound when enforcing 'rdbms'")), "" + result.diagnostics());
		}
	}
}
