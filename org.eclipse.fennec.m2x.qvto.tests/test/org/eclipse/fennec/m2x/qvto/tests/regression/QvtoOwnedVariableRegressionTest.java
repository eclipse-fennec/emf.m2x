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
package org.eclipse.fennec.m2x.qvto.tests.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Structural regression guards for {@code OperationalTransformation::ownedVariable}.
 *
 * <p>{@code ownedVariable} is the module-property list and nothing else: the
 * evaluator iterates it and defines every entry as a module-level variable, an
 * entry without an init expression as {@code null}
 * ({@code QvtoEvaluator#execute}). Anything else parked in that slot therefore
 * shadows or overwrites a real value — silently, and only for the run.
 *
 * <p>That is what happened in PR #128, where the repair pass for parser
 * satellites used {@code ownedVariable} as a fallback home for variables with no
 * enclosing {@code OperationBody} — which every satellite created while visiting
 * a {@code when} / {@code where} guard is, because guards are contained by the
 * {@code MappingOperation} ({@code qvtoperational.ecore:116-119}). The behaviour
 * side is covered by {@link QvtoE2eGuardPropertyAccessTest}; these tests pin the
 * structure directly, so a future satellite container (#135/#137) cannot reuse
 * this semantic slot unnoticed even where no behavioural test happens to look.
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.1.9 / §8.2.1.1
 * (Module::ownedVariable). Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoOwnedVariableRegressionTest extends AbstractQvtoEngineTest {

	// §8.2.1.1: a transformation without properties owns no variables
	@Test
	void ownedVariable_noProperties_isEmpty() throws Exception {
		OperationalTransformation t = parse("""
				transformation test() {
				    mapping guarded() when { true; } {
				        log('x');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertEquals(List.of(), names(t), "ownedVariable must hold module properties only");
	}

	// §8.1.9: exactly the declared module properties, in declaration order
	@Test
	void ownedVariable_holdsDeclaredPropertiesOnly() throws Exception {
		OperationalTransformation t = parse("""
				transformation test() {
				    property threshold : Integer = 10;
				    property prefix : String = 'PRE';
				    main() {
				        log(prefix);
				    }
				}
				""");
		assertEquals(List.of("threshold", "prefix"), names(t));
	}

	// §8.2.1.15: guards do not add entries — the case PR #128 broke
	@Test
	void ownedVariable_guardsAddNothing() throws Exception {
		OperationalTransformation t = parse("""
				transformation test() {
				    property threshold : Integer = 10;
				    mapping guarded()
				        when { threshold > 5; }
				        where { threshold <> null; } {
				        log('ran');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertEquals(List.of("threshold"), names(t));
	}

	// §8.1.18: the environment variables self, result and this are not module properties
	@Test
	void ownedVariable_containsNoEnvironmentVariables() throws Exception {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    property threshold : Integer = 10;
				    mapping SourceElement::toTarget() : TargetElement
				        when { self.value > threshold; } {
				        name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""");
		assertEquals(List.of("threshold"), names(t));
		assertNoName(t, "self");
		assertNoName(t, "result");
		assertNoName(t, "this");
	}

	// §8.1.3: model parameters are ModelParameters, not module properties
	@Test
	void ownedVariable_containsNoModelParameters() throws Exception {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        log('x');
				    }
				}
				""");
		assertEquals(2, t.getModelParameter().size());
		assertEquals(List.of(), names(t));
		assertNoName(t, "s");
		assertNoName(t, "t");
	}

	// §8.1.9: configuration properties live in configProperty, not in ownedVariable
	@Test
	void ownedVariable_containsNoConfigurationProperties() throws Exception {
		OperationalTransformation t = parse("""
				transformation test() {
				    configuration property limit : Integer;
				    property threshold : Integer = 10;
				    main() {
				        log(limit.repr());
				    }
				}
				""");
		assertEquals(List.of("threshold"), names(t));
		assertNoName(t, "limit");
		assertEquals(1, t.getConfigProperty().size());
	}

	// §8.1.10: intermediate classes and their default expressions add nothing
	@Test
	void ownedVariable_intermediateClassesAddNothing() throws Exception {
		OperationalTransformation t = parse("""
				transformation test() {
				    property threshold : Integer = 10;
				    intermediate class Holder { count : Integer = 0; }
				    main() {
				        var h := object Holder {};
				        log(h.count.repr());
				    }
				}
				""");
		assertEquals(List.of("threshold"), names(t));
		assertNoName(t, "h");
	}

	// §8.1.10: intermediate properties live in intermediateProperty
	@Test
	void ownedVariable_intermediatePropertiesAddNothing() throws Exception {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::tag : String;
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) { e.tag := 'x'; };
				    }
				}
				""");
		assertEquals(List.of(), names(t));
		assertNoName(t, "tag");
		assertEquals(1, t.getIntermediateProperty().size());
	}

	// §8.2.1: body-local variables, loop variables and helper parameters stay local
	@Test
	void ownedVariable_localVariablesAddNothing() throws Exception {
		OperationalTransformation t = parse("""
				transformation test() {
				    property threshold : Integer = 10;
				    query scale(factor : Integer) : Integer {
				        var local := factor * threshold;
				        return local;
				    }
				    main() {
				        var values := Sequence {1, 2, 3};
				        values->forEach(v) { log(scale(v).repr()); };
				        log(values->select(w | w > 1)->size().repr());
				    }
				}
				""");
		assertEquals(List.of("threshold"), names(t));
		assertNoName(t, "local");
		assertNoName(t, "values");
		assertNoName(t, "v");
		assertNoName(t, "w");
		assertNoName(t, "factor");
	}

	// §8.1.9: every module property carries its own init expression — an entry
	// without one is defined as null by the evaluator
	@Test
	void ownedVariable_declaredPropertiesKeepTheirInit() throws Exception {
		OperationalTransformation t = parse("""
				transformation test() {
				    property threshold : Integer = 10;
				    property prefix : String = 'PRE';
				    mapping guarded() when { threshold = 10; } {}
				    main() {
				        map guarded();
				    }
				}
				""");
		assertEquals(List.of("threshold", "prefix"), names(t));
		for (Variable v : t.getOwnedVariable()) {
			assertTrue(v.getOwnedInit() != null,
					() -> "module property '" + v.getName() + "' lost its init expression");
		}
	}

	// ---- Helpers ----

	private static List<String> names(OperationalTransformation t) {
		return t.getOwnedVariable().stream().map(Variable::getName).toList();
	}

	private static void assertNoName(OperationalTransformation t, String name) {
		assertTrue(!names(t).contains(name),
				() -> "'" + name + "' must not appear in ownedVariable, but it holds " + names(t));
	}
}
