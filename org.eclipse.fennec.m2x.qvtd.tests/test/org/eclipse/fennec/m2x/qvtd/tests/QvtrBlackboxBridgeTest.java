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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtbase.QvtbaseFactory;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrBlackboxBridge;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrDiagnosticSink;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link QvtrBlackboxBridge}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtrBlackboxBridgeTest {

	/** What the bridge under test reported; a fresh list per test. */
	private final List<Diagnostic> diagnostics = new ArrayList<>();

	// ── isBlackboxAllowed ────────────────────────────────────────────

	@Test
	void isBlackboxAllowed_emptyAllowList_allowsAll() {
		QvtrBlackboxBridge bridge = createBridge(Set.of(), true);
		assertTrue(bridge.isBlackboxAllowed("anyOperation"));
		assertTrue(bridge.isBlackboxAllowed("anotherOne"));
	}

	@Test
	void isBlackboxAllowed_withAllowList_allowsListed() {
		QvtrBlackboxBridge bridge = createBridge(Set.of("TypeMap", "Helpers"), true);
		assertTrue(bridge.isBlackboxAllowed("TypeMap"));
		assertTrue(bridge.isBlackboxAllowed("Helpers"));
	}

	@Test
	void isBlackboxAllowed_withAllowList_blocksUnlisted() {
		QvtrBlackboxBridge bridge = createBridge(Set.of("TypeMap"), true);
		assertFalse(bridge.isBlackboxAllowed("Dangerous"));
		assertFalse(bridge.isBlackboxAllowed("OtherLib"));
	}

	// ── evaluateBlackboxQuery ────────────────────────────────────────

	@Test
	void evaluateBlackboxQuery_blackboxDisabled_returnsNull() {
		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(createLibrary("helpers", "TypeMap", args -> "MAPPED"));

		QvtrBlackboxBridge bridge = createBridgeWithRegistry(registry, false, Set.of());
		Function query = QvtbaseFactory.eINSTANCE.createFunction();
		query.setName("TypeMap");

		OperationCallExp callExpr = OclFactory.eINSTANCE.createOperationCallExp();
		callExpr.setName("TypeMap");

		Object result = bridge.evaluateBlackboxQuery(query, callExpr, Map.of());
		assertNull(result, "Should return null when blackbox is disabled");
		assertReported("Blackbox query 'TypeMap' cannot be served: blackboxes are not enabled");
	}

	@Test
	void evaluateBlackboxQuery_blackboxEnabled_delegatesToLibrary() {
		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(createLibrary("helpers", "TypeMap", args -> "NUMBER"));

		QvtrBlackboxBridge bridge = createBridgeWithRegistry(registry, true, Set.of());
		Function query = QvtbaseFactory.eINSTANCE.createFunction();
		query.setName("TypeMap");

		OperationCallExp callExpr = OclFactory.eINSTANCE.createOperationCallExp();
		callExpr.setName("TypeMap");
		// No arguments for simplicity

		Object result = bridge.evaluateBlackboxQuery(query, callExpr, Map.of());
		assertEquals("NUMBER", result);
	}

	@Test
	void evaluateBlackboxQuery_noRegistry_returnsNull() {
		QvtrBlackboxBridge bridge = createBridgeWithRegistry(null, true, Set.of());
		Function query = QvtbaseFactory.eINSTANCE.createFunction();
		query.setName("TypeMap");

		OperationCallExp callExpr = OclFactory.eINSTANCE.createOperationCallExp();
		callExpr.setName("TypeMap");

		Object result = bridge.evaluateBlackboxQuery(query, callExpr, Map.of());
		assertNull(result, "Should return null when no registry");
		assertReported("Blackbox query 'TypeMap' cannot be served: no blackbox registry is configured");
	}

	@Test
	void evaluateBlackboxQuery_noLibraryServesIt_isReported() {
		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(createLibrary("helpers", "OtherQuery", args -> "X"));
		QvtrBlackboxBridge bridge = createBridgeWithRegistry(registry, true, Set.of());

		assertNull(bridge.evaluateBlackboxQuery(query("TypeMap"), call("TypeMap"), Map.of()));
		assertReported("Blackbox query 'TypeMap' cannot be served: no registered library serves it");
	}

	@Test
	void evaluateBlackboxQuery_allowListExcludesTheLibrary_isReported() {
		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(createLibrary("helpers", "TypeMap", args -> "NUMBER"));
		QvtrBlackboxBridge bridge = createBridgeWithRegistry(registry, true, Set.of("trusted"));

		assertNull(bridge.evaluateBlackboxQuery(query("TypeMap"), call("TypeMap"), Map.of()));
		assertReported("Blackbox query 'TypeMap' cannot be served: the allow-list names none of the"
				+ " libraries that serve it (helpers)");
	}

	@Test
	void evaluateBlackboxQuery_unservedQuery_isReportedOncePerRun() {
		// A query is called once per binding; one diagnostic says it, a thousand would bury it
		QvtrBlackboxBridge bridge = createBridgeWithRegistry(null, false, Set.of());
		for (int i = 0; i < 3; i++) {
			bridge.evaluateBlackboxQuery(query("TypeMap"), call("TypeMap"), Map.of());
		}
		assertEquals(1, diagnostics.size(), () -> "diagnostics: " + diagnostics);
	}

	/** Exactly one diagnostic, an error with this message (#283). */
	private void assertReported(String message) {
		assertEquals(1, diagnostics.size(), () -> "diagnostics: " + diagnostics);
		assertEquals(Diagnostic.ERROR, diagnostics.get(0).getSeverity());
		assertEquals(message, diagnostics.get(0).getMessage());
	}

	private static Function query(String name) {
		Function query = QvtbaseFactory.eINSTANCE.createFunction();
		query.setName(name);
		return query;
	}

	private static OperationCallExp call(String name) {
		OperationCallExp callExpr = OclFactory.eINSTANCE.createOperationCallExp();
		callExpr.setName(name);
		return callExpr;
	}

	// ── Helpers ──────────────────────────────────────────────────────

	@FunctionalInterface
	interface BlackboxInvoker {
		Object invoke(Object[] args);
	}

	private QvtrBlackboxBridge createBridge(Set<String> allowedModules, boolean enabled) {
		return createBridgeWithRegistry(null, enabled, allowedModules);
	}

	private QvtrBlackboxBridge createBridgeWithRegistry(BasicQvtdBlackboxRegistry registry,
			boolean enabled, Set<String> allowedModules) {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtdConfiguration.Builder builder = QvtdConfiguration.builder(oclConfig)
				.blackboxEnabled(enabled)
				.allowedBlackboxModules(allowedModules);
		if (registry != null) {
			builder.blackboxRegistry(registry);
		}
		QvtdConfiguration config = builder.build();

		return new QvtrBlackboxBridge(config, List.of(),
				null, // extentManager not needed for these tests
				null, // context not needed for these tests
				QvtrDiagnosticSink.into(diagnostics),
				(expr, bindings) -> null); // OCL callback stub
	}

	private QvtdBlackboxLibrary createLibrary(String moduleName, String opName,
			BlackboxInvoker invoker) {
		return new QvtdBlackboxLibrary() {
			@Override
			public String getModuleName() { return moduleName; }
			@Override
			public String getUnitQualifiedName() { return moduleName; }
			@Override
			public List<String> getUsedPackageURIs() { return List.of(); }
			@Override
			public List<String> getOperationNames() {
				return List.of(opName);
			}

			@Override
			public Object invoke(String operationName, Object self, Object[] args) {
				if (opName.equals(operationName)) {
					return invoker.invoke(args);
				}
				return null;
			}
		};
	}
}
