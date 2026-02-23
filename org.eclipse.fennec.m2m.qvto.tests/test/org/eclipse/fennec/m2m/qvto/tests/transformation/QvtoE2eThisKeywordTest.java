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
package org.eclipse.fennec.m2m.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for the {@code this} keyword (§8.1.18).
 *
 * <p>{@code this} represents the transformation instance and can be used
 * to access transformation-level properties and operations.
 *
 * <p>Eclipse reference: {@code moduleProperty.qvto}
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eThisKeywordTest extends AbstractQvtoEngineTest {

	// ==== P7-01: `this` keyword (§8.1.18) ====

	@Test
	void this_readModuleProperty() throws Exception {
		// §8.1.18: this represents the transformation instance — can access properties
		// Eclipse moduleProperty.qvto: this.propStrOrderedSet
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property greeting : String = 'hello';
				    main() {
				        log(this.greeting);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "hello");
	}

	@Test
	void this_writeAndReadModuleProperty() throws Exception {
		// §8.1.18: this can write to transformation properties
		// Eclipse moduleProperty.qvto: this.propStrOrderedSet += localOrderedSet
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property counter : Integer = 0;
				    main() {
				        this.counter := 42;
				        log(this.counter.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
	}

	@Test
	void this_implicitAccess_sameAsExplicit() throws Exception {
		// §8.1.18: this can be used implicitly — property access without this. prefix
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property label : String = 'default';
				    main() {
				        label := 'updated';
				        log(label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "updated");
	}

	@Test
	void this_callOwnHelper() throws Exception {
		// §8.1.18: this can call operations owned by the transformation
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper greet() : String {
				        return 'hi';
				    }
				    main() {
				        log(this.greet());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "hi");
	}

	@Test
	void this_insideHelper_stillTransformation() throws Exception {
		// §8.1.18: this always refers to transformation — even inside helpers
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property msg : String = 'from_transformation';
				    helper readMsg() : String {
				        return this.msg;
				    }
				    main() {
				        log(readMsg());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "from_transformation");
	}

	@Test
	void this_propertyInitializedWithExpression() throws Exception {
		// §8.1.18 + Eclipse moduleProperty.qvto: property with initial value
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property count : Integer = 10 + 5;
				    main() {
				        log(this.count.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "15");
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
