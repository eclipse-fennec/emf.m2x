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
package org.eclipse.fennec.m2x.qvto.tests.intermediate;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for default value expressions on intermediate classes and
 * intermediate properties ({@code intermediate class Holder { count : Integer = 0; }}).
 *
 * <p>The default expression is an {@code OclExpression} that has no semantic
 * owner in the AST: the parser parks it in an {@code EAnnotation} of the feature
 * ({@code fennec:intermediate:default}). It is therefore one of the satellites
 * the compiled-unit work (#135/#137) has to give a home to — and the one the
 * review of PR #128 found unhandled (finding 2 of the concept, §1.1): the
 * repair pass did not know the kind and skipped it without a diagnostic.
 *
 * <p>These tests pin the runtime behaviour first, so the later store round trip
 * has something to be compared against.
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.1.10 (intermediate
 * classes and properties). Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eIntermediateDefaultValueTest extends AbstractQvtoEngineTest {

	// ==== Intermediate class features with default values ====

	// §8.1.10: Integer default value applies to a freshly created instance
	@Test
	void intermediateClass_integerDefault_applied() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder { count : Integer = 0; }
				    main() {
				        var h := object Holder {};
				        log('count:' + h.count.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:0");
	}

	// §8.1.10: non-zero Integer default
	@Test
	void intermediateClass_integerDefault_nonZero() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder { count : Integer = 7; }
				    main() {
				        var h := object Holder {};
				        log('count:' + h.count.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:7");
	}

	// §8.1.10: String default value
	@Test
	void intermediateClass_stringDefault_applied() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder { label : String = 'none'; }
				    main() {
				        var h := object Holder {};
				        log('label:' + h.label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "label:none");
	}

	// §8.1.10: Boolean default value
	@Test
	void intermediateClass_booleanDefault_applied() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder { active : Boolean = true; }
				    main() {
				        var h := object Holder {};
				        log('active:' + h.active.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "active:true");
	}

	// §8.1.10: the default is a real expression, not a literal slot
	@Test
	void intermediateClass_computedDefault_applied() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder { count : Integer = 2 + 3; }
				    main() {
				        var h := object Holder {};
				        log('count:' + h.count.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:5");
	}

	// §8.1.10: collection-typed default value
	@Test
	void intermediateClass_collectionDefault_applied() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder { tags : Sequence(String) = Sequence {'a', 'b'}; }
				    main() {
				        var h := object Holder {};
				        log('size:' + h.tags->size().repr());
				        log('first:' + h.tags->first());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
		assertLogged(result, "first:a");
	}

	// §8.1.10: an explicit initialization in the object expression wins over the default
	@Test
	void intermediateClass_explicitValue_overridesDefault() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder { count : Integer = 0; }
				    main() {
				        var h := object Holder { count := 42; };
				        log('count:' + h.count.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:42");
	}

	// §8.1.10: the default applies per instance, not once per transformation
	@Test
	void intermediateClass_defaultApplies_perInstance() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder { count : Integer = 1; }
				    main() {
				        var a := object Holder {};
				        a.count := a.count + 10;
				        var b := object Holder {};
				        log('a:' + a.count.repr());
				        log('b:' + b.count.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "a:11");
		assertLogged(result, "b:1");
	}

	// §8.1.10: several features with defaults on the same class
	@Test
	void intermediateClass_multipleDefaults_applied() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder {
				        count : Integer = 3;
				        label : String = 'x';
				        active : Boolean = false;
				    }
				    main() {
				        var h := object Holder {};
				        log('count:' + h.count.repr());
				        log('label:' + h.label);
				        log('active:' + h.active.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:3");
		assertLogged(result, "label:x");
		assertLogged(result, "active:false");
	}

	// §8.1.10: a default value on a feature declared with a multiplicity
	@Test
	void intermediateClass_defaultAndOtherFeature_coexist() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder {
				        count : Integer = 5;
				        name : String;
				    }
				    main() {
				        var h := object Holder { name := 'h1'; };
				        log('name:' + h.name);
				        log('count:' + h.count.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:h1");
		assertLogged(result, "count:5");
	}

	// ==== Intermediate properties with default values ====

	// §8.1.10: an intermediate property with a default reads that default before
	// it is ever written
	@Test
	void intermediateProperty_default_readBeforeWrite() throws Exception {
		EObject src = createSourceElement("elem", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::counter : Integer = 0;
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            log('initial:' + e.counter.repr());
				            e.counter := e.counter + 1;
				            log('after:' + e.counter.repr());
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "initial:0");
		assertLogged(result, "after:1");
	}

	// §8.1.10: the intermediate property default is per element
	@Test
	void intermediateProperty_default_perElement() throws Exception {
		EObject first = createSourceElement("a", 1);
		EObject second = createSourceElement("b", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(first, second);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    intermediate property SourceElement::counter : Integer = 100;
				    main() {
				        var all := s.objectsOfType(SourceElement)->asSequence();
				        all->at(1).counter := 1;
				        log('first:' + all->at(1).counter.repr());
				        log('second:' + all->at(2).counter.repr());
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "first:1");
		assertLogged(result, "second:100");
	}

	// ==== Default expression read from a guard ====

	// §8.1.10 + §8.2.1.15: an intermediate class default is visible in a guard —
	// both the default expression and the guard are parser satellites (#135/#137)
	@Test
	void intermediateClass_default_readFromWhenGuard() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Holder { count : Integer = 0; }
				    property holder : Holder = object Holder {};
				    mapping guarded()
				        when { holder.count = 0; } {
				        log('guard-saw-default');
				    }
				    main() {
				        map guarded();
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "guard-saw-default");
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
}
