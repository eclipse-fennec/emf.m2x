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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.imperativeocl.VariableInitExp;
import org.eclipse.fennec.m2x.model.ocl.IntegerLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.parser.typing.OclTypeRules;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Static typing of QVT-O (GAP-3 stage B, #378): the OCL parts are typed by the shared rules, the
 * ImperativeOCL expressions by §8.2.2, calls of helpers and queries by their declared signatures;
 * what is known not to fit is a parse error. Typing is gradual — an unknown type is never one.
 */
class QvtoStaticTypingTest extends AbstractQvtoEngineTest {

	/** A transformation over the source model whose main body is {@code %s} and that declares {@code %s}. */
	private static String transformation(String declarations, String main) {
		return """
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC);
				%s
				main() {
				%s
				}
				""".formatted(declarations, main);
	}

	/** The type of the variable {@code name} declared in the main body, as its name. */
	private static String typeOfVariable(String declarations, String main, String name) throws QvtoParseException {
		OperationalTransformation t = parse(transformation(declarations, main));
		List<EObject> nodes = new ArrayList<>();
		t.eAllContents().forEachRemaining(nodes::add);
		for (EObject node : nodes) {
			if (node instanceof VariableInitExp init && name.equals(init.getReferredVariable().getName())) {
				return OclTypeRules.nameOf(init.getReferredVariable().getType());
			}
		}
		throw new AssertionError("no variable " + name);
	}

	private static void assertTypeError(String declarations, String main, String fragment) {
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> parse(transformation(declarations, main)));
		assertTrue(failure.getErrors().stream().map(Resource.Diagnostic::getMessage).anyMatch(m -> m.contains(fragment)),
				() -> "expected '" + fragment + "', got " + failure.getErrors().stream().map(Resource.Diagnostic::getMessage).toList());
	}

	@Nested
	@DisplayName("what QVT-O expressions are typed with")
	class Types {

		@Test
		void oclExpressionsAndVariables() throws QvtoParseException {
			assertEquals("Integer", typeOfVariable("", "var x := 1 + 2;", "x"));
			assertEquals("Real", typeOfVariable("", "var x := 1 / 2;", "x"));
			assertEquals("Sequence(String)", typeOfVariable("", "var x := Sequence{1, 2}->collect(i | i.toString());", "x"));
		}

		@Test
		@DisplayName("a helper or query call has its declared result type, also above the declaration")
		void helperAndQueryCalls() throws QvtoParseException {
			assertEquals("Integer", typeOfVariable("query twice(i : Integer) : Integer = i * 2;",
					"var x := twice(3);", "x"));
			assertEquals("Integer", typeOfVariable("query SourceElement::size() : Integer = self.name.size();",
					"var e : SourceElement := null; var x := e.size();", "x"));
			// §8.2.1.10: a contextual operation called with self as the first argument
			assertEquals("String", typeOfVariable("query SourceElement::label(p : String) : String = p + self.name;",
					"var e : SourceElement := null; var x := label(e, '>');", "x"));
		}

		@Test
		@DisplayName("the imperative iterators: xcollect a Sequence of the body, xselect of the source or the type it names")
		void imperativeIterators() throws QvtoParseException {
			assertEquals("Sequence(String)", typeOfVariable("", "var x := Sequence{1, 2}->xcollect(i | i.toString());", "x"));
			assertEquals("Sequence(Integer)", typeOfVariable("", "var x := Sequence{1, 2}->xselect(i | i > 1);", "x"));
			assertEquals("Integer", typeOfVariable("", "var x := Sequence{1, 2}->xselectOne(i | i > 1);", "x"));
		}

		@Test
		@DisplayName("object and new have the class they create; a mapping call its declared result")
		void instantiationAndMappingCalls() throws QvtoParseException {
			assertEquals("SourceElement", typeOfVariable("", "var x := object SourceElement {};", "x"));
			assertEquals("SourceElement", typeOfVariable("", "var x := new SourceElement();", "x"));
			assertEquals("SourceElement", typeOfVariable(
					"mapping SourceElement::copy() : SourceElement { name := self.name; }",
					"var e : SourceElement := null; var x := e.map copy();", "x"));
		}

		@Test
		@DisplayName("library operations of QVT-O (§8.3): String, List, Dict")
		void qvtoLibrary() throws QvtoParseException {
			assertEquals("String", typeOfVariable("", "var x := 'abc'.firstToUpper();", "x"));
			assertEquals("Boolean", typeOfVariable("", "var d : Dict(String, Integer) := Dict{}; var x := d.hasKey('a');", "x"));
			assertEquals("Integer", typeOfVariable("", "var d : Dict(String, Integer) := Dict{}; var x := d.get('a');", "x"));
		}
	}

	@Nested
	@DisplayName("known types that do not fit are parse errors")
	class Errors {

		@Test
		@DisplayName("a variable initialized with what its declared type does not take (#339)")
		void variableInitialization() {
			assertTypeError("", "var x : String := 5;", "The variable 'x' is declared as String but initialized with a Integer");
		}

		@Test
		@DisplayName("an assignment of what the target does not take (#339)")
		void assignment() {
			assertTypeError("", "var x : String := ''; x := 5;", "A Integer cannot be assigned to a String");
			assertTypeError("", "var l : Sequence(String) := Sequence{}; l += 5;",
					"A Integer cannot be assigned to a Sequence(String)");
		}

		@Test
		@DisplayName("a collection assigned to a single-valued target (#339)")
		void collectionToSingleValued() {
			assertTypeError("", "var e : SourceElement := null; e.name := Sequence{'a', 'b'};",
					"A Sequence(String) cannot be assigned to a single-valued String");
		}

		@Test
		@DisplayName("an argument of a helper or query that does not fit its parameter (#345)")
		void helperArguments() {
			assertTypeError("query isElement(e : Element) : Boolean = true;", "var b := isElement(5);",
					"Argument 1 of isElement is a Integer, but the parameter 'e' is a EObject");
			assertTypeError("query twice(i : Integer) : Integer = i * 2;", "var b := twice('x');",
					"Argument 1 of twice is a String, but the parameter 'i' is a Integer");
			assertTypeError("query SourceElement::label(p : String) : String = p;",
					"var e : SourceElement := null; var b := label(e, 5);",
					"Argument 2 of label is a Integer, but the parameter 'p' is a String");
		}

		@Test
		void libraryArguments() {
			assertTypeError("", "var x := 'abc'.substring('a', 2);", "No operation String::substring(String, Integer)");
			assertTypeError("", "var x := 1 + true;", "No operation Integer::+(Boolean)");
		}

		@Test
		@DisplayName("a condition that is neither Boolean, element nor collection (§8.4.5(2))")
		void conditions() {
			assertTypeError("", "var x := if (5) then 1 else 2 endif;", "The condition of an if is a Integer, not a Boolean");
		}
	}

	@Nested
	@DisplayName("what fits passes")
	class Passes {

		@Test
		@DisplayName("an element or a collection as condition, a subtype assigned, an unknown type")
		void conformingAndUnknown() throws QvtoParseException {
			QvtoModelExtent extent = new BasicQvtoModelExtent();
			extent.getContents().add(createSourceElement("a", 1));
			QvtoExecutionResult result = executeWithExtents(transformation(
					"query twice(i : Integer) : Integer = i * 2;", """
					var e : SourceElement := s.rootObjects()->first().oclAsType(SourceElement);
					var r : Real := twice(2);
					var o : OclAny := e;
					var n := if (e) then 'T' else 'F' endif;
					var l : Sequence(String) := Sequence{};
					l += Sequence{'x', 'y'};
					log(n + r.toString() + l->size().toString());
					"""), extent);
			assertTrue(result.isSuccess(), () -> "" + result.diagnostics());
		}

		@Test
		@DisplayName("a helper with a default value takes the shorter call")
		void defaultValues() throws QvtoParseException {
			assertEquals("Integer", typeOfVariable("query plus(i : Integer, j : Integer = 1) : Integer = i + j;",
					"var x := plus(3);", "x"));
		}

		@Test
		void typedNodesAreOclExpressions() throws QvtoParseException {
			OperationalTransformation t = parse(transformation("", "var x := 1;"));
			assertTrue(t.eAllContents().hasNext());
			List<OclExpression> untypedLiterals = new ArrayList<>();
			t.eAllContents().forEachRemaining(node -> {
				if (node instanceof IntegerLiteralExp literal && literal.getType() == null) {
					untypedLiterals.add(literal);
				}
			});
			assertEquals(List.of(), untypedLiterals);
		}
	}

	@Nested
	@DisplayName("the static rules of transformations and operations (#346, #347)")
	class Structure {

		@Test
		@DisplayName("a model parameter whose type is no model type (§8.2.1.5)")
		void aModelParameterOfNoModelType() {
			QvtoParseException failure = assertThrows(QvtoParseException.class, () -> parse("""
					transformation T(in s : String);
					main() {}
					"""));
			assertTrue(failure.getErrors().stream().anyMatch(d -> d.getMessage()
					.contains("Model parameter 's' has the type 'String', which is no model type of the transformation")),
					() -> "" + failure.getErrors());
		}

		@Test
		@DisplayName("a model parameter of a collection of a model type is one (§8.2.1.5)")
		void aModelParameterOfACollectionOfAModelType() throws QvtoParseException {
			parse("""
					modeltype SRC uses 'http://test/source/1.0';
					transformation T(in many : Sequence(SRC));
					main() {}
					""");
		}

		@Test
		@DisplayName("a type name two model types both have is ambiguous unless qualified (§8.4.8.1)")
		void anAmbiguousTypeName() throws QvtoParseException {
			EPackage first = sharedPackage("first", "urn:fennec:test:first");
			EPackage second = sharedPackage("second", "urn:fennec:test:second");
			try {
				String header = """
						modeltype A uses 'urn:fennec:test:first';
						modeltype B uses 'urn:fennec:test:second';
						transformation T(in a : A, in b : B);
						""";
				QvtoParseException failure = assertThrows(QvtoParseException.class,
						() -> parse(header + "main() { var x : Shared := null; }"));
				assertTrue(failure.getErrors().stream().anyMatch(d -> d.getMessage().contains("Type 'Shared' is ambiguous")),
						() -> "" + failure.getErrors());
				parse(header + "main() { var x : first::Shared := null; }");
			} finally {
				EPackage.Registry.INSTANCE.remove(first.getNsURI());
				EPackage.Registry.INSTANCE.remove(second.getNsURI());
			}
		}

		private EPackage sharedPackage(String name, String nsUri) {
			EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.setName(name);
			ePackage.setNsPrefix(name);
			ePackage.setNsURI(nsUri);
			EClass shared = EcoreFactory.eINSTANCE.createEClass();
			shared.setName("Shared");
			ePackage.getEClassifiers().add(shared);
			EPackage.Registry.INSTANCE.put(nsUri, ePackage);
			return ePackage;
		}

		@Test
		@DisplayName("a disjunct candidate with another number of parameters (§8.1.14.1)")
		void aDisjunctOfAnotherArity() {
			QvtoParseException failure = assertThrows(QvtoParseException.class, () -> parse(transformation("""
					mapping SourceElement::convert() : SourceElement disjuncts withValue;
					mapping SourceElement::withValue(v : Integer) : SourceElement { name := self.name; }
					""", "")));
			assertTrue(failure.getErrors().stream().anyMatch(d -> d.getMessage().contains(
					"mapping 'withValue' has 1 parameter(s), but the disjunction 'convert' it is a candidate of has 0")),
					() -> "" + failure.getErrors());
		}

		@Test
		@DisplayName("a disjunct candidate whose result is not covariant (§8.1.14.1)")
		void aDisjunctWithAnIncompatibleResult() {
			QvtoParseException failure = assertThrows(QvtoParseException.class, () -> parse("""
					modeltype SRC uses 'http://test/source/1.0';
					modeltype TGT uses 'http://test/target/1.0';
					transformation T(in s : SRC, out t : TGT);
					mapping SourceElement::convert() : SpecialSourceElement disjuncts toTarget;
					mapping SourceElement::toTarget() : TargetElement { name := self.name; }
					main() {}
					"""));
			assertTrue(failure.getErrors().stream().anyMatch(d -> d.getMessage().contains(
					"mapping 'toTarget' returns a TargetElement, which is no SpecialSourceElement")),
					() -> "" + failure.getErrors());
		}

		@Test
		@DisplayName("an override of an inherited operation is recorded as overridden (§8.2.1.10)")
		void anOverrideIsRecorded() throws QvtoParseException {
			OperationalTransformation t = parse("""
					library Base() {
					    query twice(i : Integer) : Integer = i * 2;
					}
					transformation T() extends Base {
					    query twice(i : Integer) : Integer = i * 3;
					    main() { log(twice(1).toString()); }
					}
					""");
			QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of());
			assertTrue(result.isSuccess(), () -> "" + result.diagnostics());
			ImperativeOperation twice = (ImperativeOperation) t.getEClassifiers().stream()
					.filter(EClass.class::isInstance).map(EClass.class::cast)
					.flatMap(c -> c.getEOperations().stream()).filter(o -> "twice".equals(o.getName())).findFirst()
					.orElseThrow();
			assertEquals("Base", ((EClass) twice.getOverridden().eContainer()).getName());
		}

		@Test
		@DisplayName("an override whose parameter does not comply with the overridden one's (§8.2.1.10)")
		void anIncompliantOverride() throws QvtoParseException {
			QvtoExecutionResult result = execute("""
					library Base() {
					    query twice(i : Integer) : Integer = i * 2;
					}
					transformation T() extends Base {
					    query twice(i : String) : Integer = 1;
					    main() { log('x'); }
					}
					""");
			assertTrue(!result.isSuccess());
			assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(
					"Operation 'twice' overrides the one of Base, but its parameter 'i' is a String, which does not comply with Integer")),
					() -> "" + result.diagnostics());
		}
	}
}
