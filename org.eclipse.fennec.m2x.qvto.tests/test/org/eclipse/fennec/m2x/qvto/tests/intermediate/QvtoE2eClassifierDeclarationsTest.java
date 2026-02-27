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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for GAP-15: {@code exception}, {@code datatype}, and {@code primitive}
 * classifier declarations.
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.4 (p.166-167)
 * concrete syntax for classifier declarations.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eClassifierDeclarationsTest extends AbstractQvtoEngineTest {

	// ==== exception classifier ====

	@Test
	void exception_simple() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    exception MyError {};
				    main() {
				        try {
				            raise MyError;
				        } except (MyError) {
				            log('caught MyError');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "caught MyError");
	}

	@Test
	void exception_withFeatures() throws Exception {
		String source = """
				transformation test() {
				    exception AppError {
				        message : String;
				        code : Integer;
				    };
				    main() {
				        log('ok');
				    }
				}
				""";
		QvtoExecutionResult result = execute(source);
		assertSuccess(result);
		assertLogged(result, "ok");
		// Verify exception class was created with features
		OperationalTransformation t = parse(source);
		assertFalse(t.getIntermediateClass().isEmpty());
		EClass exc = t.getIntermediateClass().stream()
				.filter(c -> "AppError".equals(c.getName()))
				.findFirst().orElse(null);
		assertNotNull(exc, "AppError exception class");
		assertEquals(2, exc.getEStructuralFeatures().size());
		EAnnotation kind = exc.getEAnnotation("fennec:intermediate:kind");
		assertNotNull(kind, "exception kind annotation");
		assertEquals("true", kind.getDetails().get("exception"));
	}

	@Test
	void exception_extends() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    exception BaseError {};
				    exception ChildError extends BaseError {};
				    main() {
				        try {
				            raise ChildError;
				        } except (BaseError) {
				            log('caught via base');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "caught via base");
	}

	@Test
	void exception_multipleInheritance() throws Exception {
		// Eclipse QVT-O bug419299 test: exception ExcExtAB extends ExcA, ExcB
		QvtoExecutionResult result = execute("""
				transformation test() {
				    exception ExcA {};
				    exception ExcB {};
				    exception ExcAB extends ExcA, ExcB {};
				    main() {
				        try {
				            raise ExcAB;
				        } except (ExcA) {
				            log('caught via ExcA');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "caught via ExcA");
	}

	@Test
	void exception_multipleCatchClauses() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    exception ErrorA {};
				    exception ErrorB {};
				    main() {
				        try {
				            raise ErrorB;
				        } except (ErrorA) {
				            log('caught A');
				        } except (ErrorB) {
				            log('caught B');
				        };
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "caught B");
	}

	// ==== datatype classifier ====

	@Test
	void datatype_simple() throws Exception {
		String source = """
				transformation test() {
				    datatype Coordinate {
				        x : Integer;
				        y : Integer;
				    };
				    main() {
				        log('ok');
				    }
				}
				""";
		QvtoExecutionResult result = execute(source);
		assertSuccess(result);
		assertLogged(result, "ok");
		// Verify datatype class was created with features
		OperationalTransformation t = parse(source);
		EClass dt = t.getIntermediateClass().stream()
				.filter(c -> "Coordinate".equals(c.getName()))
				.findFirst().orElse(null);
		assertNotNull(dt, "Coordinate datatype class");
		assertEquals(2, dt.getEStructuralFeatures().size());
		EAnnotation kind = dt.getEAnnotation("fennec:intermediate:kind");
		assertNotNull(kind, "datatype kind annotation");
		assertEquals("true", kind.getDetails().get("datatype"));
	}

	@Test
	void datatype_usedAsIntermediate() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    datatype Point {
				        x : Integer;
				        y : Integer;
				    };
				    main() {
				        var p := object Point { x := 3; y := 4; };
				        log('x:' + p.x.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "x:3");
	}

	// ==== primitive classifier ====

	@Test
	void primitive_simple() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    primitive MyPrim;
				    main() {
				        log('ok');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "ok");
	}

	// ==== mixed declarations ====

	@Test
	void mixed_classAndExceptionAndDatatype() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    intermediate class Node {
				        name : String;
				    };
				    exception ProcessError {};
				    datatype Metadata {
				        key : String;
				    };
				    primitive Token;
				    main() {
				        var n := object Node { name := 'test'; };
				        log('name:' + n.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:test");
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
