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
package org.eclipse.fennec.m2x.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for {@code tag} and {@code typedef} (§8.1.16, §8.4).
 *
 * <p>{@code tag "alias"} creates an alternative name for a class or property.
 * {@code typedef} defines a type alias for a complex type (deprecated per §8.2.2.23).
 *
 * <p>Eclipse reference: {@code simpletag/simpletag.qvto},
 * {@code simplerename/simplerename.qvto}
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eTagTypedefTest extends AbstractQvtoEngineTest {

	// ==== P7-06: Tag & Typedef (§8.1.16, §8.4) ====

	@Test
	void tag_parsesSuccessfully() throws Exception {
		// §8.4: tag declaration is parsed without error
		// tag "alias" ScopedName = expression;
		assertDoesNotThrow(() -> parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation test(in m : ECORE) {
				    tag "alias" ecore::EPackage::name = 'pkgName';
				    main() {
				        log('parsed');
				    }
				}
				"""));
	}

	@Test
	void tag_multipleDeclarations() throws Exception {
		// §8.4: multiple tag declarations in one transformation
		assertDoesNotThrow(() -> parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation test(in m : ECORE) {
				    tag "alias" ecore::EPackage::name = 'pkgName';
				    tag "alias" ecore::EClass::name = 'clsName';
				    main() {
				        log('parsed');
				    }
				}
				"""));
	}

	@Test
	void typedef_parsesSuccessfully() throws Exception {
		// §8.1.16: typedef defines a type alias
		// typedef PersonInfo = Tuple{name:String, phone:String};
		assertDoesNotThrow(() -> parse("""
				transformation test() {
				    typedef PersonName = String;
				    main() {
				        log('parsed');
				    }
				}
				"""));
	}

	@Test
	void typedef_tupleAlias() throws Exception {
		// §8.1.16: typedef for a Tuple type
		assertDoesNotThrow(() -> parse("""
				transformation test() {
				    typedef PersonInfo = Tuple(name:String, age:Integer);
				    main() {
				        log('parsed');
				    }
				}
				"""));
	}

	@Test
	void typedef_usedAsVariableType() throws Exception {
		// §8.1.16: typedef alias can be used as a type in variable declarations
		QvtoExecutionResult result = execute("""
				transformation test() {
				    typedef PersonName = String;
				    main() {
				        var name : PersonName := 'John';
				        log(name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "John");
	}

	@Test
	void tag_aliasUsedInObjectInit() throws Exception {
		// §8.1.16 + Eclipse simpletag.qvto:
		// tag "alias" creates alternative property name usable in object initialization
		// tag "alias" EPackage::name = 'pkgName'; → object EPackage { pkgName := 'foo' }
		QvtoExecutionResult result = execute("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation test(in m : ECORE) {
				    tag "alias" ecore::EPackage::name = 'pkgName';
				    main() {
				        var p := object ecore::EPackage {
				            pkgName := 'myPackage';
				        };
				        log(p.name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "myPackage");
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
