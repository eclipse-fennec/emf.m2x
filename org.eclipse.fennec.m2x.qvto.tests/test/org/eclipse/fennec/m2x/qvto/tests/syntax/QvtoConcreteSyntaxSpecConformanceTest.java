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
package org.eclipse.fennec.m2x.qvto.tests.syntax;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * The concrete syntax of QVT v1.3 §8.4 and the notation examples of §8.1/§8.2 (#346).
 */
class QvtoConcreteSyntaxSpecConformanceTest extends AbstractQvtoEngineTest {

	private static final String HEADER = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			""";

	private static QvtoExecutionResult run(String transformation) throws Exception {
		return executeWithExtents(HEADER + transformation,
				new BasicQvtoModelExtent(createSourceElement("a", 1)), emptyExtent());
	}

	@Test
	void aModelParameter_withoutDirection_isIn() throws Exception {
		// §8.2.1.15: "The default direction kind is in"
		QvtoExecutionResult result = run("""
				transformation test(src : SRC, out t : TGT) {
				    main() { log('n=' + src.rootObjects()->size().repr()); }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "n=1");
	}

	@Test
	void parameters_mayBeUnnamed_andHaveDefaults() throws Exception {
		// §8.1.8: constructor Column::Column(String, String); §8.4 <init_part>
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    constructor TargetElement::TargetElement(String, String) { name := 'made'; }
				    helper h(x : Integer = 3) : Integer { return x; }
				    main() {
				        log('ctor=' + new TargetElement('a', 'b').name);
				        log('default=' + h().repr() + ' given=' + h(5).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "ctor=made");
		assertLogged(result, "default=3 given=5");
	}

	@Test
	void helperResults_mayBeNamed_andSeveral() throws Exception {
		// §8.2.1.12: "When more than one result is declared in the signature of a helper
		// operation … returns a tuple"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    helper one() : r : String { r := 'x'; }
				    helper two() : a : String, b : Integer { a := 'y'; b := 2; }
				    main() {
				        var t2 := two();
				        log('one=' + one() + ' two=' + t2.a + t2.b.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "one=x two=y2");
	}

	@Test
	void theLastSemicolons_mayBeOmitted() throws Exception {
		// §8.4 <expression_list> ::= <expression_semi_list> ';'?
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        log('a');
				        var v := switch { case (true) { 'blk' } else { 'other' } };
				        log('b' + v)
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "log: a");
		assertLogged(result, "log: b");
	}

	@Test
	void theWhileDeclarator_takesEqualsAndNoType() throws Exception {
		// §8.1.17; <while_exp> ::= 'while' '(' (<declarator> ';')? <expression> ')'
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        while (i = 0; i < 3) { i := i + 1; log('i=' + i.repr()); };
				        while (j : Integer := 0; j < 1) { j := j + 1; log('j=' + j.repr()); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "i=3");
		assertLogged(result, "j=1");
	}

	@Test
	void aMappingId_mayNameTheTransformation() throws Exception {
		// §8.4.8: <mapping_id> ::= (<transformation_id> ':::')? (<type_id> '::')? <mapping_name>
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toT() : TargetElement { name := self.name; }
				    main() {
				        log('m=' + s.rootObjects()->first().oclAsType(SourceElement).map test:::SourceElement::toT().name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "m=a");
	}

	@Test
	void metamodelNotation_classesExceptionsAndDeclarations() throws Exception {
		// §8.4.6: metamodel { class … }, exception E;, intermediate class IC;
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    metamodel MM {
				        abstract class Base { }
				        class A extends Base { x : String; }
				    }
				    exception E;
				    exception E2 extends E;
				    intermediate class IC;
				    main() {
				        var a := object A { x := 'v'; };
				        log('a=' + a.x);
				        try { raise E2('boom'); } except (E) { log('caught'); };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "a=v");
		assertLogged(result, "caught");
	}

	@Test
	void anAbstractClass_isAbstract() throws Exception {
		OperationalTransformation transformation = parse(HEADER + """
				transformation test(in s : SRC, out t : TGT) {
				    intermediate abstract class Base { }
				    main() { }
				}
				""");
		EClassifier base = transformation.getIntermediateClass().stream()
				.filter(c -> "Base".equals(c.getName())).findFirst().orElseThrow();
		assertTrue(((EClass) base).isAbstract());
	}

	@Test
	void propertyKeys_andTheDeprecatedTypedef() throws Exception {
		// §8.4 <property_key> ::= 'derived' | 'literal' | 'configuration' | 'property';
		// §8.4.7.1 typedef X = T [cond]
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    derived property d : String = 'derived';
				    literal property l : Integer = 7;
				    configuration c : String;
				    typedef Positive = Integer [self > 0];
				    main() { log('d=' + d + ' l=' + l.repr()); }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "d=derived l=7");
	}

	@Test
	void aBodylessMapping_withAGuard_andABodylessMain_parse() throws Exception {
		// §8.2.1.15: mapping Package::packageToSchema() : Schema when { … };  <entry>: main();
		parse(HEADER + """
				transformation test(in s : SRC, out t : TGT);
				mapping SourceElement::m() : TargetElement when { self.value > 0 };
				main();
				""");
	}

	@Test
	void aModuleType_comesBeforeAModelType() throws Exception {
		// §8.4.8.1: "First, a type definition existing at the level of the current module … is searched"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    intermediate class TargetElement { foo : String; }
				    main() {
				        var e := object TargetElement { foo := 'mine'; };
				        log('foo=' + e.foo);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "foo=mine");
	}

	@Test
	void xor_keepsTheOclPrecedence_andTraditionalComparisonIsAccepted() throws Exception {
		// Design decision (#346): xor binds tighter than or as in OCL v2.4 (the QVT EBNF puts both
		// on one level); == and != are accepted without the directive of §8.4.4
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    main() { log('xor=' + (true xor true or true).repr() + ' eq=' + (1 == 1).repr() + (1 != 2).repr()); }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "xor=false eq=truetrue");
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
