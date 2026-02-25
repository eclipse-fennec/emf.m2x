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
package org.eclipse.fennec.m2x.qvto.tests.config;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O configuration properties.
 * Tests cover String/Boolean/Integer config properties, default values,
 * and property access from mapping and helper bodies.
 */
class QvtoE2eConfigPropertyTest extends AbstractQvtoEngineTest {

	// ---- String configuration property ----

	@Test
	void configProperty_string_accessibleInMain() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("name", "TestValue"));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property name : String;
				    main() {
				        log(name);
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "TestValue");
	}

	// ---- Boolean configuration property ----

	@Test
	void configProperty_boolean_true() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("flag", true));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property flag : Boolean;
				    main() {
				        log(flag.toString());
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "true");
	}

	@Test
	void configProperty_boolean_false() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("flag", false));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property flag : Boolean;
				    main() {
				        log(flag.toString());
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "false");
	}

	// ---- Property used in mapping body ----

	@Test
	void configProperty_usedInMapping() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("prefix", "PRE"));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property prefix : String;
				    mapping createElem() : r : TargetElement {
				        r.name := prefix + '-element';
				    }
				    main() {
				        var elem := map createElem();
				        log(elem.name);
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "PRE-element");
	}

	// ---- Property used in helper ----

	@Test
	void configProperty_usedInHelper() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("multiplier", 3));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property multiplier : Integer;
				    helper scale(n : Integer) : Integer {
				        return n * multiplier;
				    }
				    main() {
				        log(scale(7).toString());
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "21");
	}

	// ---- Missing property (null/default) ----

	@Test
	void configProperty_missing_isNull() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of());
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property name : String;
				    main() {
				        log(if name = null then 'null' else name endif);
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "null");
	}

	// ---- Multiple config properties ----

	@Test
	void configProperty_multiple() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("first", "A", "second", "B"));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property first : String;
				    configuration property second : String;
				    main() {
				        log(first + '-' + second);
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "A-B");
	}

	// ---- Config property in conditional ----

	@Test
	void configProperty_inConditional() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("debug", true));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property debug : Boolean;
				    main() {
				        if (debug) then {
				            log('debug-mode');
				        } endif;
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "debug-mode");
	}

	// ---- P2-13: Configuration Properties vertieft ----

	// §8.2.1.1: Integer config property with arithmetic
	@Test
	void configProperty_integer_arithmetic() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of("limit", 100));
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property limit : Integer;
				    main() {
				        var doubled := limit * 2;
				        log(doubled.toString());
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "200");
	}

	// §8.2.1.1 + Eclipse invalidConfigProp.qvto: Unset config properties are null/undefined
	@Test
	void configProperty_unset_isUndefined() throws Exception {
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionContext ctx = QvtoExecutionContext.of(
				List.of(outExtent),
				Map.of());
		QvtoExecutionResult result = execute("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    configuration property intProp : Integer;
				    configuration property boolProp : Boolean;
				    main() {
				        log(if intProp = null then 'int-null' else 'int-set' endif);
				        log(if boolProp = null then 'bool-null' else 'bool-set' endif);
				    }
				}
				""", ctx);
		assertSuccess(result);
		assertLogged(result, "int-null");
		assertLogged(result, "bool-null");
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
