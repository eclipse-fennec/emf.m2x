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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxInvocationContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngineImpl;
import org.junit.jupiter.api.Test;

/**
 * Security hardening tests for the QVT-O engine.
 *
 * <p>Tests verify that resource exhaustion attacks from untrusted QVT-O
 * transformations are mitigated by configurable limits. Each test uses
 * intentionally low limits for fast, deterministic execution.
 *
 * <p>See {@code qvto-security-analysis.md} for the full threat model
 * (vectors Q-1 through Q-9).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoSecurityHardeningTest extends AbstractQvtoEngineTest {

	// ── Q-4 / M-8: While-loop iteration limit ─────────────────────────

	@Test
	void q4_whileLoop_exceedingLimit_terminates() throws QvtoParseException {
		QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
				.withMaxLoopIterations(10);

		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var i : Integer := 0;
					while (true) {
						i := i + 1;
					};
				}
				""", opts);

		assertFalse(result.isSuccess());
		assertTrue(hasDiagnosticContaining(result, "Maximum loop iterations exceeded"));
	}

	@Test
	void q4_whileLoop_withinLimit_succeeds() throws QvtoParseException {
		QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
				.withMaxLoopIterations(100);

		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var i : Integer := 0;
					while (i < 5) {
						i := i + 1;
					};
				}
				""", opts);

		assertTrue(result.isSuccess());
	}

	// ── Q-4 / M-8: For-loop iteration limit ───────────────────────────

	@Test
	void q4_forLoop_exceedingLimit_terminates() throws QvtoParseException {
		QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
				.withMaxLoopIterations(5);

		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var items := Sequence{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
					items->forEach(i) {
						log(i.repr());
					};
				}
				""", opts);

		assertFalse(result.isSuccess());
		assertTrue(hasDiagnosticContaining(result, "Maximum loop iterations exceeded"));
	}

	// ── Q-5 / M-3: Timeout enforcement ────────────────────────────────

	@Test
	void q5_timeout_exceedingDeadline_terminates() throws QvtoParseException {
		QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
				.withTimeout(Duration.ofMillis(50))
				.withMaxLoopIterations(Integer.MAX_VALUE);

		long start = System.nanoTime();
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var i : Integer := 0;
					while (true) {
						i := i + 1;
					};
				}
				""", opts);
		long elapsed = System.nanoTime() - start;

		assertFalse(result.isSuccess());
		assertTrue(hasDiagnosticContaining(result, "timeout"));
		// Should terminate reasonably quickly — within 5x the configured timeout
		assertTrue(elapsed < Duration.ofMillis(250).toNanos(),
				"Timeout enforcement too slow: " + Duration.ofNanos(elapsed).toMillis() + "ms");
	}

	@Test
	void q5_timeout_withinDeadline_succeeds() throws QvtoParseException {
		QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
				.withTimeout(Duration.ofSeconds(5));

		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var x := 1 + 2;
				}
				""", opts);

		assertTrue(result.isSuccess());
	}

	// ── Q-5: Stack depth limit (inherited, verify in QVT-O context) ───

	@Test
	void q5_recursiveMapping_exceedingStackDepth_terminates() throws QvtoParseException {
		QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
				.withMaxStackDepth(5);

		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					recurse(0);
				}
				helper recurse(depth : Integer) : Integer {
					return recurse(depth + 1);
				}
				""", opts);

		assertFalse(result.isSuccess());
		assertTrue(hasDiagnosticContaining(result, "Maximum stack depth exceeded"));
	}

	// ── Q-7 / M-4b: Diagnostics flooding limit ───────────────────────

	@Test
	void q7_diagnosticsFlooding_truncatesAtLimit() throws QvtoParseException {
		QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults()
				.withMaxDiagnostics(5)
				.withMaxLoopIterations(100);

		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var i : Integer := 0;
					while (i < 20) {
						log("message " + i.repr());
						i := i + 1;
					};
				}
				""", opts);

		// Should have at most maxDiagnostics + 1 (the truncation warning)
		assertTrue(result.diagnostics().size() <= 6,
				"Diagnostics not truncated: " + result.diagnostics().size());
		assertTrue(hasDiagnosticContaining(result, "truncated"));
	}

	// ── Options validation ────────────────────────────────────────────

	@Test
	void options_defaults_haveCorrectValues() {
		QvtoEvaluationOptions opts = QvtoEvaluationOptions.defaults();

		assertEquals(1000, opts.maxStackDepth());
		assertEquals(1_000_000, opts.maxLoopIterations());
		assertEquals(10_000, opts.maxDiagnostics());
		assertEquals(1_000_000, opts.maxTraceRecords());
		assertFalse(opts.tracingEnabled());
		assertNotNull(opts.oclOptions());
		assertEquals(OclEvaluationOptions.NullHandling.STRICT,
				opts.oclOptions().nullHandling());
	}

	@Test
	void options_withMethods_createNewInstances() {
		QvtoEvaluationOptions base = QvtoEvaluationOptions.defaults();
		QvtoEvaluationOptions modified = base
				.withMaxStackDepth(50)
				.withMaxLoopIterations(500)
				.withMaxDiagnostics(100)
				.withMaxTraceRecords(200)
				.withTimeout(Duration.ofSeconds(10));

		// Original unchanged
		assertEquals(1000, base.maxStackDepth());
		assertEquals(1_000_000, base.maxLoopIterations());

		// Modified has new values
		assertEquals(50, modified.maxStackDepth());
		assertEquals(500, modified.maxLoopIterations());
		assertEquals(100, modified.maxDiagnostics());
		assertEquals(200, modified.maxTraceRecords());
		assertEquals(Duration.ofSeconds(10), modified.timeout());
	}

	// ── D29: Extension Security Controls ─────────────────────────────

	@Test
	void d29_defaults_allDisabled() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
		assertFalse(config.blackboxEnabled());
		assertFalse(config.unitResolverEnabled());
		assertEquals(Set.of(), config.allowedBlackboxModules());
		assertEquals(Set.of(), config.allowedUnitModules());
		assertEquals(10, config.maxBlackboxLibraries());
		assertEquals(5, config.maxUnitResolvers());
	}

	@Test
	void d29_blackboxDisabled_importFails() throws QvtoParseException {
		// Engine with blackbox registry but blackboxEnabled=false (default)
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
		registry.register(new D29TestBlackboxLibrary());

		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
				.blackboxRegistry(registry)
				// blackboxEnabled NOT set — defaults to false
				.build();
		QvtoEngineImpl eng = new QvtoEngineImpl(config);

		var t = eng.parse("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				import d29lib;
				transformation Main(inout m : ECORE) {
				    main() {
				        log(d29op('test'));
				    }
				}
				""", "Main");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE));
		QvtoExecutionResult result = eng.execute(t, QvtoExecutionContext.of(extent));

		// Should fail because blackbox is disabled — cannot resolve import
		assertNotNull(result);
		assertTrue(hasDiagnosticContaining(result, "Cannot resolve import")
				|| hasDiagnosticContaining(result, "Link error"),
				"Blackbox disabled → import should fail: " + result.diagnostics());
	}

	@Test
	void d29_unitResolverDisabled_importFails() throws QvtoParseException {
		// Engine with unit resolver but unitResolverEnabled=false (default)
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
				.addUnitResolver(name -> java.util.Optional.of(
						new QvtoUnit.SourceUnit(name,
								URI.createURI("inline:" + name),
								"library " + name + " { helper greet() : String { return 'hi'; } }")))
				// unitResolverEnabled NOT set — defaults to false
				.build();
		QvtoEngineImpl eng = new QvtoEngineImpl(config);

		var t = eng.parse("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				import SomeLib;
				transformation Main(inout m : ECORE) {
				    main() {
				        log(greet());
				    }
				}
				""", "Main");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE));
		QvtoExecutionResult result = eng.execute(t, QvtoExecutionContext.of(extent));

		// Should fail because unit resolver is disabled — cannot resolve import
		assertNotNull(result);
		assertTrue(hasDiagnosticContaining(result, "Cannot resolve import")
				|| hasDiagnosticContaining(result, "Link error"),
				"Unit resolver disabled → import should fail: " + result.diagnostics());
	}

	// ── Helper ────────────────────────────────────────────────────────

	/**
	 * Execute transformation with custom options but no extents.
	 */
	private static QvtoExecutionResult execute(String source, QvtoEvaluationOptions options)
			throws QvtoParseException {
		return execute(source, org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext.of(), options);
	}

	private static boolean hasDiagnosticContaining(QvtoExecutionResult result, String substring) {
		return result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().toLowerCase().contains(substring.toLowerCase()));
	}

	// ── Minimal blackbox library for D29 tests ───────────────────────

	private static class D29TestBlackboxLibrary implements QvtoBlackboxLibrary {

		@Override
		public String getModuleName() {
			return "d29lib";
		}

		@Override
		public String getUnitQualifiedName() {
			return "d29lib";
		}

		@Override
		public List<String> getUsedPackageURIs() {
			return List.of();
		}

		@Override
		public List<BlackboxOperationDescriptor> getOperationDescriptors() {
			BlackboxOperationDescriptor op = QvtOperationalFactory.eINSTANCE
					.createBlackboxOperationDescriptor();
			op.setName("d29op");
			op.setReturnType(EcorePackage.Literals.ESTRING);
			op.getParameterTypes().add(EcorePackage.Literals.ESTRING);
			return List.of(op);
		}

		@Override
		public Object invoke(String operationName, QvtoBlackboxInvocationContext context,
				Object[] args) {
			return args.length > 0 ? args[0] : null;
		}
	}
}
