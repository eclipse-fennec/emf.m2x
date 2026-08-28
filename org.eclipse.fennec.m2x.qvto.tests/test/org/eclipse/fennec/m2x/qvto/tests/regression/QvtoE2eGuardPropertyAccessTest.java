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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Regression guards: module properties, configuration properties and
 * {@code this} keep their values when they are read from a {@code when} or
 * {@code where} guard.
 *
 * <p>Background: {@code MappingOperation::when} / {@code where} are contained by
 * the {@code MappingOperation}, not by an {@code OperationBody}
 * ({@code qvtoperational.ecore:116-119}). Any mechanism that gives parser-created
 * satellites a home therefore takes a different code path for guards than for
 * bodies. In PR #128 that path ended in {@code OperationalTransformation::ownedVariable},
 * which the evaluator reads as the module-property list and initializes to
 * {@code null} — a {@code property threshold : Integer = 10;} read from a guard
 * silently evaluated as {@code null} for the whole run. The review found it, no
 * test did: every guard in the E2E suites was {@code self.value > 0},
 * {@code true} or {@code false}.
 *
 * <p>These tests pin the observable behaviour so the same class of defect cannot
 * return with the satellite container of the compiled-unit work (#135/#137).
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.1.9 (module
 * properties), §8.2.1.15 (guards). Failures are implementation gaps, NOT test
 * errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eGuardPropertyAccessTest extends AbstractQvtoEngineTest {

	// ==== Module property read from a when guard ====

	// §8.1.9 + §8.2.1.15: a module property read from a when guard carries its
	// initialized value, not null
	@Test
	void moduleProperty_inWhenGuard_hasInitializedValue() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property threshold : Integer = 10;
				    mapping guarded()
				        when { threshold = 10; } {
				        log('guard-saw-10');
				    }
				    main() {
				        map guarded();
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "guard-saw-10");
		assertLogged(result, "done");
	}

	// §8.1.9: a module property compared in a guard is not null
	@Test
	void moduleProperty_inWhenGuard_isNotNull() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property threshold : Integer = 10;
				    mapping guarded()
				        when { threshold <> null; } {
				        log('not-null');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "not-null");
	}

	// §8.2.1.15: a false comparison against the real value still skips the body
	@Test
	void moduleProperty_inWhenGuard_falseComparisonSkipsBody() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property threshold : Integer = 10;
				    mapping guarded()
				        when { threshold > 50; } {
				        log('should-not-run');
				    }
				    main() {
				        map guarded();
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "done");
		assertNotLogged(result, "should-not-run");
	}

	// §8.1.9: String-typed module property in a guard
	@Test
	void moduleProperty_string_inWhenGuard() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property prefix : String = 'PRE';
				    mapping guarded()
				        when { prefix = 'PRE'; } {
				        log('prefix-ok');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "prefix-ok");
	}

	// §8.1.9: Boolean-typed module property used directly as the guard condition
	@Test
	void moduleProperty_boolean_isTheGuardCondition() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property enabled : Boolean = true;
				    mapping guarded()
				        when { enabled; } {
				        log('enabled-ran');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "enabled-ran");
	}

	// §8.1.9: collection-typed module property in a guard
	@Test
	void moduleProperty_collection_inWhenGuard() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property allowed : Sequence(String) = Sequence {'a', 'b'};
				    mapping guarded()
				        when { allowed->includes('a'); } {
				        log('allowed-ok');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "allowed-ok");
	}

	// §8.1.9: a value written in main is visible in the guard of a later mapping
	@Test
	void moduleProperty_mutatedInMain_visibleInGuard() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property threshold : Integer = 10;
				    mapping guarded()
				        when { threshold = 42; } {
				        log('guard-saw-42');
				    }
				    main() {
				        threshold := 42;
				        map guarded();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "guard-saw-42");
	}

	// §8.1.9: the guard reads the property through a helper
	@Test
	void moduleProperty_readViaHelper_inWhenGuard() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property threshold : Integer = 10;
				    query currentThreshold() : Integer {
				        return threshold;
				    }
				    mapping guarded()
				        when { currentThreshold() = 10; } {
				        log('helper-guard-ok');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "helper-guard-ok");
	}

	// ==== this ====

	// §8.1.9: `this` in a guard resolves to the module instance and its properties
	@Test
	void thisKeyword_inWhenGuard_readsModuleProperty() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property threshold : Integer = 10;
				    mapping guarded()
				        when { this.threshold = 10; } {
				        log('this-guard-ok');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "this-guard-ok");
	}

	// ==== Configuration property ====

	// §8.1.9: a configuration property read from a guard carries the value
	// supplied by the execution context
	@Test
	void configProperty_inWhenGuard_hasSuppliedValue() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("limit", 7));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property limit : Integer;
				    mapping guarded()
				        when { limit = 7; } {
				        log('config-guard-ok');
				    }
				    main() {
				        map guarded();
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "config-guard-ok");
	}

	// §8.1.9: Boolean configuration property as the guard condition
	@Test
	void configProperty_boolean_isTheGuardCondition() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("flag", true));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property flag : Boolean;
				    mapping guarded()
				        when { flag; } {
				        log('flag-guard-ok');
				    }
				    main() {
				        map guarded();
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "flag-guard-ok");
	}

	// ==== where guard ====

	// §8.2.1.15: a module property is readable from a where guard too
	@Test
	void moduleProperty_inWhereGuard() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property threshold : Integer = 10;
				    mapping guarded()
				        where { threshold = 10; } {
				        log('where-body-ran');
				    }
				    main() {
				        map guarded();
				        log('done');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "where-body-ran");
		assertLogged(result, "done");
	}

	// ==== Guard combined with self ====

	// §8.2.1.15: guard mixing the mapping's self with a module property
	@Test
	void moduleProperty_combinedWithSelf_inWhenGuard() throws Exception {
		EObject src = createSourceElement("elem", 20);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
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
				        log('created:' + t.rootObjects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "created:1");
	}

	// §8.2.1.15: the same guard filters out the element below the threshold
	@Test
	void moduleProperty_combinedWithSelf_filtersOut() throws Exception {
		EObject src = createSourceElement("elem", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
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
				        log('created:' + t.rootObjects()->size().repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "created:0");
	}

	// ==== Guard in a disjunct branch ====

	// §8.1.14.1: disjunct candidates whose guards read a module property
	@Test
	void moduleProperty_inDisjunctGuards_selectsBranch() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property mode : String = 'b';
				    mapping alt1() when { mode = 'a'; } {
				        log('alt1-ran');
				    }
				    mapping alt2() when { mode = 'b'; } {
				        log('alt2-ran');
				    }
				    mapping dispatch() disjuncts alt1, alt2 {}
				    main() {
				        map dispatch();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "alt2-ran");
		assertNotLogged(result, "alt1-ran");
	}

	// ==== Property value survives many guard evaluations ====

	// §8.1.9: the property is not consumed by evaluating a guard repeatedly
	@Test
	void moduleProperty_survivesRepeatedGuardEvaluation() throws Exception {
		EObject first = createSourceElement("a", 20);
		EObject second = createSourceElement("b", 30);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(first, second);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
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
				        log('created:' + t.rootObjects()->size().repr());
				        log('threshold-after:' + threshold.repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "created:2");
		assertLogged(result, "threshold-after:10");
	}

	// ---- Helpers ----

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected));
		assertTrue(found, "Expected log containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
	}

	private static void assertNotLogged(QvtoExecutionResult result, String unexpected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(unexpected));
		assertTrue(!found, "Did not expect log containing '" + unexpected
				+ "' but diagnostics were: " + result.diagnostics());
	}
}
