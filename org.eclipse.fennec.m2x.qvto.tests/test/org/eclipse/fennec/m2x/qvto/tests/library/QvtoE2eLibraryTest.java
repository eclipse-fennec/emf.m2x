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
package org.eclipse.fennec.m2x.qvto.tests.library;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O library support.
 *
 * Spec references:
 * - §8.1.4 (p68): Libraries — reusable module without main()
 * - §8.2.1.2 (p95): Library — subclass of Module, no entry point
 * - §8.2.1.3 (p95): Module — operations, properties, imports
 * - §8.2.1.4 (p97): ModuleImport — access vs extension semantics
 */
class QvtoE2eLibraryTest extends AbstractQvtoEngineTest {

	// ---- Basic inline library: helper callable from transformation ----

	// §8.1.4: Library helper accessible via implicit access import
	@Test
	void inlineLibrary_helperCallable() throws Exception {
		QvtoExecutionResult result = execute("""
				library StringUtils {
				    helper greet(name : String) : String {
				        return 'Hello ' + name;
				    }
				}
				transformation test() {
				    main() {
				        log(greet('World'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Hello World");
	}

	// §8.1.4: Library query callable from transformation
	@Test
	void inlineLibrary_queryCallable() throws Exception {
		QvtoExecutionResult result = execute("""
				library MathLib {
				    query add(a : Integer, b : Integer) : Integer {
				        return a + b;
				    }
				}
				transformation test() {
				    main() {
				        log(add(3, 7).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "10");
	}

	// §8.1.4: Multiple helpers in library
	@Test
	void inlineLibrary_multipleHelpers() throws Exception {
		QvtoExecutionResult result = execute("""
				library Utils {
				    helper prefix(s : String) : String {
				        return 'pre-' + s;
				    }
				    helper suffix(s : String) : String {
				        return s + '-suf';
				    }
				}
				transformation test() {
				    main() {
				        log(prefix('x'));
				        log(suffix('y'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "pre-x");
		assertLogged(result, "y-suf");
	}

	// §8.1.4: Library helper used in mapping
	@Test
	void inlineLibrary_helperInMapping() throws Exception {
		EObject src = createSourceElement("elem", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				library NameLib {
				    helper formatName(n : String) : String {
				        return 'mapped-' + n;
				    }
				}
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := formatName(self.name);
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		EObject target = outExtent.getContents().get(0);
		assertEquals("mapped-elem", eGet(target, "name"));
	}

	// §8.1.4: Two inline libraries
	@Test
	void inlineLibrary_twoLibraries() throws Exception {
		QvtoExecutionResult result = execute("""
				library Lib1 {
				    helper fromLib1() : String {
				        return 'lib1';
				    }
				}
				library Lib2 {
				    helper fromLib2() : String {
				        return 'lib2';
				    }
				}
				transformation test() {
				    main() {
				        log(fromLib1() + ':' + fromLib2());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "lib1:lib2");
	}

	// §8.1.4: Library helper with context type (contextual operation)
	@Test
	void inlineLibrary_contextualHelper() throws Exception {
		EObject src = createSourceElement("test", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				library ElemLib {
				    helper SourceElement::describe() : String {
				        return self.name + '=' + self.value.toString();
				    }
				}
				transformation test(in s : SRC, out t : TGT) {
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            log(e.describe());
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "test=5");
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

	private static Object eGet(EObject obj, String featureName) {
		return obj.eGet(obj.eClass().getEStructuralFeature(featureName));
	}
}
