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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.IntegerLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.LetExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleType;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;
import org.eclipse.fennec.m2x.ocl.parser.typing.OclLibrarySignatures;
import org.eclipse.fennec.m2x.ocl.parser.typing.OclTypeRules;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The static type checker (OCL v2.4 §8.2, §8.3.7, §11, GAP-3, #378): every expression the parser
 * builds has its type, an expression whose types are known and do not fit is a parse error, and
 * {@code validate} applies the same rules to an expression that did not come from the parser.
 * Typing is gradual: a type the parser does not know is never an error.
 */
class OclStaticTypingTest extends AbstractOclTest {

	private static final OclStandardLibrary LIBRARY = OclStandardLibrary.INSTANCE;

	private static String typeOf(String expression) throws OclParseException {
		return OclTypeRules.nameOf(engine.parse(expression, personClass).getType());
	}

	@Nested
	@DisplayName("every expression has its type")
	class Types {

		@Test
		void literals() throws OclParseException {
			assertEquals("Integer", typeOf("1"));
			assertEquals("Real", typeOf("1.5"));
			assertEquals("String", typeOf("'a'"));
			assertEquals("Boolean", typeOf("true"));
			assertEquals("UnlimitedNatural", typeOf("*"));
			assertEquals("OclVoid", typeOf("null"));
			assertEquals("OclInvalid", typeOf("invalid"));
			assertEquals("Status", typeOf("Status::SENIOR"));
		}

		@Test
		@DisplayName("arithmetic follows the library signatures: Integer, Real, and '/' is a Real")
		void arithmetic() throws OclParseException {
			assertEquals("Integer", typeOf("1 + 2"));
			assertEquals("Real", typeOf("1 + 2.5"));
			assertEquals("Real", typeOf("1 / 2"));
			assertEquals("Integer", typeOf("7.div(2)"));
			assertEquals("Integer", typeOf("-1"));
			assertEquals("Boolean", typeOf("1 < 2.5"));
			assertEquals("String", typeOf("'a' + 'b'"));
			assertEquals("Integer", typeOf("'abc'.size()"));
		}

		@Test
		@DisplayName("collection operations keep or change the kind as §11.7 says")
		void collectionOperations() throws OclParseException {
			assertEquals("Sequence(Integer)", typeOf("Set{1, 2}->asSequence()"));
			assertEquals("Bag(Integer)", typeOf("Set{1, 2}->union(Bag{3})"));
			assertEquals("Set(Integer)", typeOf("Set{1, 2}->union(Set{3})"));
			assertEquals("Set(Real)", typeOf("Set{1, 2}->including(2.5)"));
			assertEquals("Sequence(OclAny)", typeOf("Sequence{1}->including('a')"));
			assertEquals("Integer", typeOf("Sequence{1, 2}->first()"));
			assertEquals("Integer", typeOf("Set{1, 2}->size()"));
			assertEquals("Set(Tuple(first : Integer, second : String))", typeOf("Set{1}->product(Set{'a'})"));
			assertEquals("Sequence(Integer)", typeOf("Sequence{Sequence{1}, Sequence{2}}->flatten()"));
		}

		@Test
		@DisplayName("oclAsType answers the type it names; oclIsKindOf a Boolean")
		void typeOperations() throws OclParseException {
			assertSame(personClass, engine.parse("self.oclAsType(Person)", personClass).getType());
			assertEquals("Boolean", typeOf("self.oclIsKindOf(Person)"));
			assertEquals("Set(Person)", typeOf("Set{self}->selectByKind(Person)"));
		}

		@Test
		@DisplayName("a metamodel operation has its declared type, a property its feature's")
		void metamodelFeatures() throws OclParseException {
			assertEquals("Integer", typeOf("self.getAge()"));
			assertEquals("Boolean", typeOf("self.transfer(self, 1.0)"));
			assertEquals("Integer", typeOf("self.age"));
			assertEquals("Real", typeOf("self.salary"));
			assertSame(companyClass, engine.parse("self.employer", personClass).getType());
		}

		@Test
		@DisplayName("if, let, iterate, tuple and collection literal are typed from their parts")
		void compoundExpressions() throws OclParseException {
			assertEquals("Real", typeOf("if true then 1 else 2.5 endif"));
			assertEquals("Integer", typeOf("if true then 1 else null endif"));
			assertEquals("Integer", typeOf("let x : Integer = 1 in x + 1"));
			assertEquals("Integer", typeOf("Sequence{1, 2}->iterate(i; acc : Integer = 0 | acc + i)"));
			assertEquals("Set(Real)", typeOf("Set{1, 2.5}"));
			assertEquals("Sequence(OclAny)", typeOf("Sequence{1, 'a'}"));
			TupleType tuple = assertInstanceOf(TupleType.class, engine.parse("Tuple{a = 1, b = 'x'}", personClass).getType());
			assertEquals(List.of("Integer", "String"),
					tuple.getOwnedParts().stream().map(p -> OclTypeRules.nameOf(p.getType())).toList());
		}

		@Test
		@DisplayName("a let variable without a declared type keeps none; its uses have the initial value's type")
		void untypedLet_isUsedWithTheInitialValuesType() throws OclParseException {
			// The declaration stays as written, so specStrict can still report it (#326)
			LetExp let = assertInstanceOf(LetExp.class, engine.parse("let x = 1 in x + 1", personClass));
			assertNull(let.getOwnedVariable().getType());
			assertEquals("Integer", OclTypeRules.nameOf(let.getType()));
		}

		@Test
		@DisplayName("a source of unknown type: what every signature of that name answers, or nothing")
		void unknownReceiver() throws OclParseException {
			// v is bound only at evaluation
			assertEquals("Integer", typeOf("v.size()"));
			assertEquals("Boolean", typeOf("v.oclIsUndefined()"));
			assertEquals("?", typeOf("v.first()"));
			assertEquals("?", typeOf("v + 1"));
		}

		@Test
		@DisplayName("a dot after an operation answering a collection collects (§7.6.2)")
		void aDotOnATypedCollectionCollects() throws OclParseException {
			// asSequence() is typed now, so .name is an implicit collect over the persons
			EObject company = createCompany("C", createPerson("Ann", 40, 1.0, false), createPerson("Bob", 20, 1.0, false));
			assertEquals(List.of("Ann", "Bob"), eval("self.employees->asSequence().name", company));
		}
	}

	@Nested
	@DisplayName("known types that do not fit are parse errors")
	class TypeErrors {

		@Test
		void anArgumentOfAMetamodelOperation() throws OclParseException {
			assertTypeError("self.transfer('x', 1.0)", createPerson("A", 1, 1.0, false),
					"Argument 1 of transfer is a String, but the parameter 'target' is a Person");
			assertTypeError("self.setAge('old')", createPerson("A", 1, 1.0, false),
					"Argument 1 of setAge is a String, but the parameter 'newAge' is a Integer");
		}

		@Test
		void aDeclaredTypeTheInitialValueDoesNotConformTo() {
			assertTypeError("let n : String = self.age in n", createPerson("A", 1, 1.0, false),
					"The let variable 'n' is declared as String but initialized with a Integer");
		}

		@Test
		@DisplayName("what conforms is not an error: Integer to Real, a subclass to its class, null to anything")
		void whatConformsPasses() throws OclParseException {
			EObject person = createPerson("A", 1, 1.0, false);
			assertEquals(3.5, eval("let r : Real = 1 in r + 2.5", person));
			assertEquals(true, eval("let p : OclAny = self in p = self", person));
			assertEquals(true, eval("let s : String = null in s.oclIsUndefined()", person));
			assertEquals(1, eval("let c : Collection(Integer) = Set{1} in c->size()", person));
		}

		@Test
		@DisplayName("an unknown type is never an error")
		void unknownTypesPass() throws OclParseException {
			EObject person = createPerson("A", 1, 1.0, false);
			assertEquals(2L, ((Number) evalWith("1 + v", person, Map.of("v", 1L))).longValue());
			assertEquals("ab", evalWith("'a'.concat(v)", person, Map.of("v", "b")));
		}
	}

	@Nested
	@DisplayName("def: the parameter and result types a Complete OCL document declares (#324)")
	class Definitions {

		private static final String DOC = """
				package company
				context Person
				inv usesTwice: self.twice(self.age) > 0
				def: twice(i : Integer) : Integer = i * 2
				def: label : String = self.name.concat('!')
				endpackage
				""";

		@Test
		@DisplayName("a defined operation is typed, also where it is used before the def")
		void aDefinedOperationIsTyped() throws OclParseException {
			OclExpression inv = engine.parseDocument(DOC).get(0).getSpecification();
			OperationCallExp greater = assertInstanceOf(OperationCallExp.class, inv);
			assertEquals("Integer", OclTypeRules.nameOf(greater.getOwnedSource().getType()));
		}

		@Test
		@DisplayName("an argument that does not fit the declared parameter is an error")
		void anArgumentOfTheWrongType_isAnError() {
			OclParseException failure = assertThrows(OclParseException.class, () -> engine.parseDocument("""
					package company
					context Person
					def: twice(i : Integer) : Integer = i * 2
					inv: self.twice('x') > 0
					endpackage
					"""));
			assertTrue(failure.getMessage().contains("Argument 1 of twice is a String, but the parameter 'i' is a Integer"),
					failure::getMessage);
		}

		@Test
		@DisplayName("a defined attribute has its declared type")
		void aDefinedAttributeIsTyped() throws OclParseException {
			OclExpression inv = engine.parseDocument("""
					package company
					context Person
					def: label : String = self.name.concat('!')
					inv: self.label.size() > 0
					endpackage
					""").get(1).getSpecification();
			OperationCallExp greater = assertInstanceOf(OperationCallExp.class, inv);
			OperationCallExp size = assertInstanceOf(OperationCallExp.class, greater.getOwnedSource());
			assertEquals("String", OclTypeRules.nameOf(size.getOwnedSource().getType()));
		}
	}

	@Nested
	@DisplayName("validate applies the same rules to an expression the parser did not build")
	class Validate {

		@Test
		void aParsedExpressionIsValid() throws OclParseException {
			assertEquals(List.of(), engine.validate(engine.parse("self.age + 1 > 2", personClass), personClass));
		}

		@Test
		void aHandBuiltIllTypedExpressionIsReported() {
			OperationCallExp plus = OclFactory.eINSTANCE.createOperationCallExp();
			plus.setName("+");
			IntegerLiteralExp one = OclFactory.eINSTANCE.createIntegerLiteralExp();
			one.setIntegerSymbol(1L);
			one.setType(LIBRARY.integer());
			StringLiteralExp text = OclFactory.eINSTANCE.createStringLiteralExp();
			text.setStringSymbol("a");
			text.setType(LIBRARY.string());
			plus.setOwnedSource(one);
			plus.getOwnedArguments().add(text);

			List<Diagnostic> diagnostics = engine.validate(plus, personClass);

			assertEquals(1, diagnostics.size(), "" + diagnostics);
			assertEquals(Diagnostic.ERROR, diagnostics.get(0).getSeverity());
			assertTrue(diagnostics.get(0).getMessage().contains("No operation Integer::+(String)"),
					diagnostics.get(0).getMessage());
		}

		@Test
		void anUntypedHandBuiltExpressionPasses() {
			// nothing is known, so nothing is wrong
			OperationCallExp plus = OclFactory.eINSTANCE.createOperationCallExp();
			plus.setName("+");
			plus.setOwnedSource(OclFactory.eINSTANCE.createIntegerLiteralExp());
			plus.getOwnedArguments().add(OclFactory.eINSTANCE.createStringLiteralExp());
			assertEquals(List.of(), engine.validate(plus, personClass));
		}
	}

	@Nested
	@DisplayName("conformance and the common supertype (§8.2.1)")
	class Rules {

		@Test
		void primitives() {
			assertTrue(OclTypeRules.conformsTo(LIBRARY.integer(), LIBRARY.real()));
			assertTrue(OclTypeRules.conformsTo(LIBRARY.unlimitedNatural(), LIBRARY.integer()));
			assertFalse(OclTypeRules.conformsTo(LIBRARY.real(), LIBRARY.integer()));
			assertFalse(OclTypeRules.conformsTo(LIBRARY.string(), LIBRARY.booleanType()));
			assertTrue(OclTypeRules.conformsTo(LIBRARY.oclVoid(), LIBRARY.string()));
			assertTrue(OclTypeRules.conformsTo(LIBRARY.string(), LIBRARY.oclAny()));
		}

		@Test
		@DisplayName("an Ecore data type is the OCL type it stands for")
		void ecoreDataTypes() {
			assertSame(LIBRARY.string(), OclTypeRules.normalize(EcorePackage.Literals.ESTRING));
			assertSame(LIBRARY.integer(), OclTypeRules.normalize(EcorePackage.Literals.EINT));
			assertSame(LIBRARY.integer(), OclTypeRules.normalize(EcorePackage.Literals.ELONG));
			assertSame(LIBRARY.real(), OclTypeRules.normalize(EcorePackage.Literals.EDOUBLE));
			assertSame(LIBRARY.booleanType(), OclTypeRules.normalize(EcorePackage.Literals.EBOOLEAN));
			assertTrue(OclTypeRules.conformsTo(EcorePackage.Literals.EINT, LIBRARY.real()));
		}

		@Test
		void collectionsAndClasses() {
			EClass employee = EcoreFactory.eINSTANCE.createEClass();
			employee.getESuperTypes().add(personClass);
			assertTrue(OclTypeRules.conformsTo(employee, personClass));
			assertFalse(OclTypeRules.conformsTo(personClass, employee));
			assertTrue(OclTypeRules.conformsTo(OclTypeRules.collectionType(CollectionKind.SET, employee),
					OclTypeRules.collectionType(CollectionKind.COLLECTION, personClass)));
			assertFalse(OclTypeRules.conformsTo(OclTypeRules.collectionType(CollectionKind.ORDERED_SET, personClass),
					OclTypeRules.collectionType(CollectionKind.SEQUENCE, personClass)));
		}

		@Test
		void commonSuperType() {
			assertSame(LIBRARY.real(), OclTypeRules.commonSuperType(LIBRARY.integer(), LIBRARY.real()));
			assertSame(LIBRARY.oclAny(), OclTypeRules.commonSuperType(LIBRARY.integer(), LIBRARY.string()));
			EClass employee = EcoreFactory.eINSTANCE.createEClass();
			employee.getESuperTypes().add(personClass);
			EClass manager = EcoreFactory.eINSTANCE.createEClass();
			manager.getESuperTypes().add(personClass);
			assertSame(personClass, OclTypeRules.commonSuperType(employee, manager));
			EClassifier sets = OclTypeRules.commonSuperType(OclTypeRules.collectionType(CollectionKind.SET, LIBRARY.integer()),
					OclTypeRules.collectionType(CollectionKind.BAG, LIBRARY.real()));
			assertEquals("Collection(Real)", OclTypeRules.nameOf(sets));
			assertNull(OclTypeRules.commonSuperType(LIBRARY.integer(), null), "unknown stays unknown");
		}

		@Test
		@DisplayName("a library extended with a malformed signature fails when it is built")
		void aMalformedSignatureFailsEarly() {
			assertThrows(IllegalArgumentException.class,
					() -> OclLibrarySignatures.OCL.extend(b -> b.op("String", "broken", "Set(")));
		}

		@Test
		void collectionTypesAreTheKindsSubclass() {
			assertInstanceOf(CollectionType.class, OclTypeRules.collectionType(CollectionKind.SEQUENCE, null));
		}
	}
}
