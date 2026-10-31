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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * QVT 1.3 §7.13.2 and §7.13.4: one accepted and one rejected form per production of the
 * relations textual syntax (#362). What a construct means is tested elsewhere; this class only
 * asks whether the parser takes what the grammar allows and refuses what it does not.
 */
@DisplayName("§7.13.4: Relations textual syntax")
class QvtdConcreteSyntaxTest extends AbstractQvtdEngineTest {

	/** A transformation over simpleuml/simplerdbms whose body is {@code %s}. */
	private static final String TRANSFORMATION = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
			%s
			}
			""";

	/** A relation of that transformation whose body is {@code %s}. */
	private static String relation(String body) {
		return TRANSFORMATION.formatted("""
				top relation R {
				%s
				}
				""".formatted(body));
	}

	private static void accepted(String source) throws QvtdParseException {
		assertNotNull(parse(source));
	}

	private static void rejected(String source) {
		assertThrows(QvtdParseException.class, () -> parse(source));
	}

	@Nested
	@DisplayName("<topLevel> ::= <import>* <transformation>*")
	class TopLevel {

		@Test
		void aRelationOutsideATransformation_isRejected() {
			rejected("""
					top relation R {
						checkonly domain uml p : Package {};
					}
					""");
		}

		@Test
		void anImportBeforeTheTransformation_isAccepted() throws QvtdParseException {
			accepted("import shared.Library;\n" + TRANSFORMATION.formatted(""));
		}

		@Test
		void anImportAfterTheTransformation_isRejected() {
			rejected(TRANSFORMATION.formatted("") + "import shared.Library;\n");
		}
	}

	@Nested
	@DisplayName("<import> ::= 'import' [<identifier> ':'] <URI> ('::' <identifier>)* ['::*'] ';'")
	class Import {

		@Test
		void anImportWithoutATarget_isRejected() {
			rejected("import ;\n" + TRANSFORMATION.formatted(""));
		}

		@Test
		void anImportWithoutASemicolon_isRejected() {
			rejected("import shared.Library\n" + TRANSFORMATION.formatted(""));
		}
	}

	@Nested
	@DisplayName("<transformation> ::= 'transformation' <transformationId> '(' <modelDecl> (',' <modelDecl>)* ')' ['extends' …] '{' … '}'")
	class Transformation {

		@Test
		void aModelDeclWithSeveralMetamodels_isAccepted() throws QvtdParseException {
			accepted("""
					transformation T(uml : { simpleuml, simplerdbms }, rdbms : simplerdbms) {
					}
					""");
		}

		@Test
		void aTransformationWithoutModels_isRejected() {
			rejected("""
					transformation T() {
					}
					""");
		}

		@Test
		void extends_isAccepted() throws QvtdParseException {
			accepted("""
					transformation Base(uml : simpleuml, rdbms : simplerdbms) {
					}
					transformation T(uml : simpleuml, rdbms : simplerdbms) extends Base {
					}
					""");
		}

		@Test
		void aKeyAfterARelation_isRejected() {
			// '{' <keyDecl>* ( <relation> | <query> )* '}' — keys come first
			rejected(TRANSFORMATION.formatted("""
					top relation R {
						checkonly domain uml p : Package {};
					}
					key Table {name};
					"""));
		}
	}

	@Nested
	@DisplayName("<keyDecl> ::= 'key' <classId> '{' <keyProperty> (, <keyProperty>)* '}' ';'")
	class KeyDecl {

		@Test
		void aKeyOfSeveralProperties_isAccepted() throws QvtdParseException {
			accepted(TRANSFORMATION.formatted("key Table {name, schema};"));
		}

		@Test
		void anOppositeKeyProperty_isAccepted() throws QvtdParseException {
			accepted(TRANSFORMATION.formatted("key Table {name, opposite(Schema::ownedTable)};"));
		}

		@Test
		void aKeyWithoutProperties_isRejected() {
			rejected(TRANSFORMATION.formatted("key Table {};"));
		}

		@Test
		void anOppositeWithADot_isRejected() {
			// <classId> '::' <identifier> — the normative grammar has no '.' form (#361)
			rejected(TRANSFORMATION.formatted("key Table {name, opposite(Schema.ownedTable)};"));
		}
	}

	@Nested
	@DisplayName("<relation> ::= ['top'] ['abstract'] 'relation' <identifier> ['overrides' <identifier>] '{' … '}'")
	class Relation {

		@Test
		void abstractAndOverrides_areAccepted() throws QvtdParseException {
			accepted(TRANSFORMATION.formatted("""
					abstract relation Base {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
					}
					top relation R overrides Base {
						checkonly domain uml p : Package {};
						enforce domain rdbms s : Schema {};
					}
					"""));
		}

		@Test
		void aRelationWithoutADomain_isRejected() {
			// (<domain> | <primitiveTypeDomain>)+
			rejected(relation("n : String;"));
		}

		@Test
		void whenAfterWhere_isRejected() {
			rejected(relation("""
					checkonly domain uml p : Package {};
					where {}
					when {}
					"""));
		}
	}

	@Nested
	@DisplayName("<varDeclaration> ::= <identifier> (, <identifier>)* ':' <typeCS> ['=' <OclExpressionCS>] ';'")
	class VarDeclaration {

		@Test
		void severalNamesAndAnInitializer_areAccepted() throws QvtdParseException {
			accepted(relation("""
					a, b : String;
					n : String = 'X';
					checkonly domain uml p : Package { name = a, ownedClass = c : Class { name = b } };
					enforce domain rdbms s : Schema { name = a + b + n };
					"""));
		}

		@Test
		void aDeclarationWithoutAType_isRejected() {
			rejected(relation("""
					n : ;
					checkonly domain uml p : Package {};
					"""));
		}
	}

	@Nested
	@DisplayName("<domain> ::= [<checkEnforceQualifier>] 'domain' <modelId> <template> (',' <template>)* … ';'")
	class Domain {

		@Test
		void aDomainWithoutQualifier_isAccepted() throws QvtdParseException {
			accepted(relation("""
					domain uml p : Package {};
					enforce domain rdbms s : Schema {};
					"""));
		}

		@Test
		void severalTemplatesInOneDomain_areAccepted() throws QvtdParseException {
			accepted(relation("""
					checkonly domain uml p : Package {}, c : Class {};
					enforce domain rdbms s : Schema {};
					"""));
		}

		@Test
		void defaultValues_areAccepted() throws QvtdParseException {
			accepted(relation("""
					n : String;
					checkonly domain uml p : Package { name = n };
					enforce domain rdbms s : Schema { name = n }
						default_values { n = 'unnamed'; };
					"""));
		}

		@Test
		void emptyDefaultValues_areRejected() {
			// 'default_values' '{' (<assignmentExp>)+ '}'
			rejected(relation("""
					checkonly domain uml p : Package {};
					enforce domain rdbms s : Schema {} default_values { };
					"""));
		}

		@Test
		void aDomainWithoutATemplate_isRejected() {
			rejected(relation("checkonly domain uml ;"));
		}
	}

	@Nested
	@DisplayName("<primitiveTypeDomain> ::= 'primitive' 'domain' <identifier> ':' <typeCS> ';'")
	class PrimitiveTypeDomain {

		@Test
		void aPrimitiveDomain_isAccepted() throws QvtdParseException {
			accepted(TRANSFORMATION.formatted("""
					relation R {
						primitive domain n : String;
						checkonly domain uml p : Package { name = n };
						enforce domain rdbms s : Schema { name = n };
					}
					"""));
		}

		@Test
		void aPrimitiveDomainWithoutAType_isRejected() {
			rejected(TRANSFORMATION.formatted("""
					relation R {
						primitive domain n;
						checkonly domain uml p : Package {};
					}
					"""));
		}
	}

	@Nested
	@DisplayName("<template> / <objectTemplate> / <propertyTemplate>")
	class ObjectTemplate {

		@Test
		void aTemplateGuard_isAccepted() throws QvtdParseException {
			// <template> ::= (<objectTemplate> | <collectionTemplate>) ['{' <OclExpressionCS> '}']
			accepted(relation("""
					pn : String;
					checkonly domain uml p : Package { name = pn } { pn <> '' };
					enforce domain rdbms s : Schema { name = pn };
					"""));
		}

		@Test
		void anAnonymousRoot_isAccepted() throws QvtdParseException {
			// <objectTemplate> ::= [<identifier>] ':' <pathNameCS> '{' … '}'
			accepted(relation("""
					checkonly domain uml : Package {};
					enforce domain rdbms s : Schema {};
					"""));
		}

		@Test
		void anOppositePropertyTemplate_isAccepted() throws QvtdParseException {
			accepted(relation("""
					checkonly domain uml a : Attribute { opposite(Class::ownedAttribute) = c : Class {} };
					enforce domain rdbms s : Schema {};
					"""));
		}

		@Test
		void aTemplateWithoutBraces_isRejected() {
			rejected(relation("""
					checkonly domain uml p : Package;
					enforce domain rdbms s : Schema {};
					"""));
		}

		@Test
		void aPropertyTemplateWithoutEquals_isRejected() {
			rejected(relation("""
					pn : String;
					checkonly domain uml p : Package { name pn };
					enforce domain rdbms s : Schema {};
					"""));
		}
	}

	@Nested
	@DisplayName("<collectionTemplate> / <memberSelection>")
	class CollectionTemplate {

		@Test
		void membersAndRest_areAccepted() throws QvtdParseException {
			accepted(relation("""
					c : Class;
					rest : Set(Class);
					checkonly domain uml p : Package { ownedClass = cs : Set(Class) { c ++ rest } };
					enforce domain rdbms s : Schema {};
					"""));
		}

		@Test
		void wildcards_areAccepted() throws QvtdParseException {
			accepted(relation("""
					c : Class;
					checkonly domain uml p : Package { ownedClass = cs : Set(Class) { _, c ++ _ } };
					enforce domain rdbms s : Schema {};
					"""));
		}

		@Test
		void aRestWithoutAMember_isRejected() {
			// <memberSelection> starts with a member; '++' only follows one
			rejected(relation("""
					checkonly domain uml p : Package { ownedClass = cs : Set(Class) { ++ rest } };
					enforce domain rdbms s : Schema {};
					"""));
		}

		@Test
		void aCollectionTemplateWithoutElementType_isRejected() {
			rejected(relation("""
					checkonly domain uml p : Package { ownedClass = cs : Set { c } };
					enforce domain rdbms s : Schema {};
					"""));
		}
	}

	@Nested
	@DisplayName("<when> / <where> ::= '{' (<OclExpressionCS> ';')* '}'")
	class WhenWhere {

		@Test
		void emptyClauses_areAccepted() throws QvtdParseException {
			accepted(relation("""
					checkonly domain uml p : Package {};
					enforce domain rdbms s : Schema {};
					when {}
					where {}
					"""));
		}

		@Test
		void predicatesWithoutSemicolons_areRejected() {
			rejected(relation("""
					pn : String;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema {};
					when { pn <> 'a' pn <> 'b' }
					"""));
		}
	}

	@Nested
	@DisplayName("<query> ::= 'query' <queryId> '(' … ')' ':' <typeCS> (';' | '{' <OclExpressionCS> '}')")
	class Query {

		@Test
		void aQueryWithBody_andOneWithout_areAccepted() throws QvtdParseException {
			accepted(TRANSFORMATION.formatted("""
					query Upper(s : String) : String { s.toUpperCase() }
					query External(s : String) : String;
					"""));
		}

		@Test
		void aQueryWithoutResultType_isRejected() {
			rejected(TRANSFORMATION.formatted("query Upper(s : String) { s.toUpperCase() }"));
		}
	}

	@Nested
	@DisplayName("§7.13.2 Keywords")
	class Keywords {

		@Test
		void aReservedWordAsName_isRejected() {
			rejected(relation("""
					relation : String;
					checkonly domain uml p : Package { name = relation };
					"""));
		}

		@Test
		@Disabled("#361: 'import' is accepted as a name")
		void everyReservedWordAsName_isRejected() {
			// "All these keywords are reserved words, they cannot be used as identifiers"
			List<String> accepted = new ArrayList<>();
			for (String word : List.of("checkonly", "domain", "enforce", "extends", "implementedby",
					"import", "key", "overrides", "primitive", "query", "relation", "top",
					"transformation", "when", "where")) {
				try {
					parse(relation("""
							%s : String;
							checkonly domain uml p : Package { name = %s };
							enforce domain rdbms s : Schema {};
							""".formatted(word, word)));
					accepted.add(word);
				} catch (QvtdParseException expected) {
					// reserved, as it should be
				}
			}
			assertEquals(List.of(), accepted, "reserved words the parser takes as names");
		}

		@Test
		void anEscapedReservedWord_isAccepted() throws QvtdParseException {
			// "Reserved words … may be used within identifiers using OCL's underscore-prefixed-
			// string-literal escape"
			accepted(relation("""
					_'key' : String;
					checkonly domain uml p : Package { name = _'key' };
					enforce domain rdbms s : Schema { name = _'key' };
					"""));
		}

		@Test
		@Disabled("#361: abstract, opposite and default_values are reserved by the grammar")
		void wordsOutsideTheReservedList_areNames() throws QvtdParseException {
			// abstract, opposite and default_values are not in the §7.13.2 list (#361)
			accepted(relation("""
					abstract, opposite, default_values : String;
					checkonly domain uml p : Package { name = abstract };
					enforce domain rdbms s : Schema { name = opposite + default_values };
					"""));
		}
	}
}
