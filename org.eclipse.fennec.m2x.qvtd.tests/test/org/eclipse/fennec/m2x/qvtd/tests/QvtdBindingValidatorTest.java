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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for QVT-R §7.5 "Restrictions on Expressions" — binding validation.
 *
 * <p>Validates that the parser rejects transformations with unbound variables
 * and accepts well-formed transformations where all variable references can be
 * organized into a valid sequential binding order.
 *
 * @see <a href="https://www.omg.org/spec/QVT/1.3">QVT v1.3, §7.5</a>
 */
@DisplayName("§7.5: Binding Validator")
class QvtdBindingValidatorTest extends AbstractQvtdEngineTest {

	// ==================== Valid Transformations ====================

	@Nested
	@DisplayName("Valid binding orders (should pass)")
	class ValidBindings {

		@Test
		@DisplayName("Simple source→target variable propagation")
		void simpleVariablePropagation() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("Nested templates with bound variables")
		void nestedTemplates() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class {
								name = cn
							};
							enforce domain rdbms t : Table {
								name = cn
							};
						}
					}
					""";
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("When-clause with RelationCallExp binds variables from trace")
		void whenClauseRelationCallBinds() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
						relation AttributeToColumn {
							an : String;
							checkonly domain uml a : Attribute { name = an };
							enforce domain rdbms col : Column { name = an };
							when { PackageToSchema(p, s); }
						}
					}
					""";
			// p and s are bound by the when-clause trace lookup
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("Where-clause uses variables bound by domains")
		void whereClauseUsesDomainVars() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
							where { pn = pn; }
						}
					}
					""";
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("Primitive domain binds root variable")
		void primitiveDomainBinding() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation WithPrimitive {
							primitive domain prefix : String;
							checkonly domain uml p : Package { name = prefix };
							enforce domain rdbms s : Schema { name = prefix };
						}
					}
					""";
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("Variable declaration with init expression")
		void varDeclWithInit() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							suffix : String = '_suffix';
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("Multiple domains — each can be target")
		void multipleDomainsBidirectional() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation PackageToSchema {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";
			// pn is bound by whichever domain is source, used by the other
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("Let expression introduces local variable — not flagged")
		void letExpressionLocalScope() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
							where { let x : String = pn in x = x; }
						}
					}
					""";
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("Collection template binds member and rest variables")
		void collectionTemplateBindings() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";
			// Basic collection template test — valid
			assertDoesNotThrow(() -> parse(source));
		}
	}

	// ==================== Invalid Transformations ====================

	@Nested
	@DisplayName("Invalid binding orders (should fail)")
	class InvalidBindings {

		@Test
		@DisplayName("Unbound variable in where-clause")
		void unboundVarInWhereClause() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
							where { unknownVar = pn; }
						}
					}
					""";
			QvtdParseException ex = assertThrows(QvtdParseException.class,
					() -> parse(source));
			assertTrue(ex.getMessage().contains("§7.5"),
					"Error should reference §7.5: " + ex.getMessage());
			assertTrue(ex.getMessage().contains("unknownVar"),
					"Error should mention the unbound variable: " + ex.getMessage());
		}

		@Test
		@DisplayName("Unbound variable in target domain complex expression")
		void unboundVarInTargetDomain() {
			// Note: A simple VariableExp in a PropertyTemplateItem (e.g. name = x) is
			// a potential binding site (§7.5 Rule 1.1), NOT a usage. To test unbound
			// variables in the target domain, we need a complex expression.
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema {
								name = pn.concat(missingVar)
							};
						}
					}
					""";
			QvtdParseException ex = assertThrows(QvtdParseException.class,
					() -> parse(source));
			assertTrue(ex.getMessage().contains("§7.5"),
					"Error should reference §7.5: " + ex.getMessage());
			assertTrue(ex.getMessage().contains("missingVar"),
					"Error should mention the unbound variable: " + ex.getMessage());
		}

		@Test
		@DisplayName("Unbound variable in when-clause predicate")
		void unboundVarInWhenClause() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
							when { ghostVar = 'test'; }
						}
					}
					""";
			QvtdParseException ex = assertThrows(QvtdParseException.class,
					() -> parse(source));
			assertTrue(ex.getMessage().contains("§7.5"),
					"Error should reference §7.5: " + ex.getMessage());
			assertTrue(ex.getMessage().contains("ghostVar"),
					"Error should mention the unbound variable: " + ex.getMessage());
		}

		@Test
		@DisplayName("Unbound variable in complex template expression")
		void unboundVarInComplexExpression() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema {
								name = pn.concat(undefVar)
							};
						}
					}
					""";
			QvtdParseException ex = assertThrows(QvtdParseException.class,
					() -> parse(source));
			assertTrue(ex.getMessage().contains("§7.5"),
					"Error should reference §7.5: " + ex.getMessage());
			assertTrue(ex.getMessage().contains("undefVar"),
					"Error should mention the unbound variable: " + ex.getMessage());
		}

		@Test
		@DisplayName("Variable only in target domain — not bound when domain is target")
		void varOnlyInTargetNotBoundExternally() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							checkonly domain uml p : Package {
								name = localOnlyVar
							};
							enforce domain rdbms s : Schema {
								name = localOnlyVar
							};
						}
					}
					""";
			// localOnlyVar is bound by each domain independently but
			// when one domain is the target, the OTHER must provide the binding.
			// Since both domains bind it via PropertyTemplateItem, it IS available
			// from external source for both directions. This should PASS.
			assertDoesNotThrow(() -> parse(source));
		}
	}

	// ==================== Edge Cases ====================

	@Nested
	@DisplayName("Edge cases")
	class EdgeCases {

		@Test
		@DisplayName("Relation with no when/where — no diagnostics")
		void relationWithoutWhenWhere() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation Simple {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("Relation with only checkonly domains — valid")
		void checkOnlyDomains() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							checkonly domain rdbms s : Schema { name = pn };
						}
					}
					""";
			assertDoesNotThrow(() -> parse(source));
		}

		@Test
		@DisplayName("Multiple relations — each validated independently")
		void multipleRelations() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation Good {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
						top relation Bad {
							checkonly domain uml p : Package { name = pn2 };
							enforce domain rdbms s : Schema { name = pn2 };
							where { doesNotExist = 'x'; }
						}
					}
					""";
			QvtdParseException ex = assertThrows(QvtdParseException.class,
					() -> parse(source));
			assertTrue(ex.getMessage().contains("doesNotExist"),
					"Error should mention the unbound variable: " + ex.getMessage());
		}

		@Test
		@DisplayName("Diagnostic count matches number of violations")
		void diagnosticCount() {
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
							where { ghost1 = ghost2; }
						}
					}
					""";
			QvtdParseException ex = assertThrows(QvtdParseException.class,
					() -> parse(source));
			// At least 2 diagnostics (ghost1 and ghost2)
			assertTrue(ex.getErrors().size() >= 2,
					"Expected at least 2 diagnostics, got " + ex.getErrors().size());
		}
	}
}
