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
import static org.junit.jupiter.api.Assertions.assertTimeoutPreemptively;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.function.Supplier;
import java.time.Duration;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicLong;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrEvalEnvironment;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrEvaluator;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrRun;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrTraceManager;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Security hardening tests for the QVT-R engine.
 *
 * <p>Tests verify that resource exhaustion attacks from untrusted QVT-R
 * transformations are mitigated by configurable limits. Each test uses
 * intentionally low limits for fast, deterministic execution.
 *
 * <p>See {@code qvtd-security-analysis.md} for the full threat model
 * (vectors R-1 through R-12).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdSecurityHardeningTest {

	private static EcoreHelper ecoreHelper;
	private static EPackage umlPackage;

	@BeforeAll
	static void setUp() throws IOException {
		ecoreHelper = new EcoreHelper(QvtdSecurityHardeningTest.class);
		umlPackage = ecoreHelper.loadEcore("simpleuml.ecore");
		// Register simplerdbms in EPackage.Registry so parser can resolve Schema etc.
		ecoreHelper.loadEcore("simplerdbms.ecore");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	// ── R-1 / M-R2: Relation call depth limit ─────────────────────────

	@Test
	void r1_recursiveRelation_exceedingDepth_terminates() throws QvtdParseException {
		// Mutually recursive where-clauses: A calls B, B calls A
		String source = """
				transformation recursiveTest(uml : simpleuml, rdbms : simplerdbms) {

				    top relation Init {
				        n : String;
				        checkonly domain uml p : Package { name = n };
				        enforce domain rdbms s : Schema { name = n };
				        where { RecurseA(p, s); }
				    }

				    relation RecurseA {
				        n : String;
				        checkonly domain uml p : Package { name = n };
				        enforce domain rdbms s : Schema { name = n };
				        where { RecurseB(p, s); }
				    }

				    relation RecurseB {
				        n : String;
				        checkonly domain uml p : Package { name = n };
				        enforce domain rdbms s : Schema { name = n };
				        where { RecurseA(p, s); }
				    }
				}
				""";

		QvtdEngine engine = createEngine(b -> b.maxRelationDepth(5));
		RelationalTransformation t = engine.parse(source, "recursiveTest");

		QvtdModelExtent umlExtent = createUmlExtent("TestPkg");
		QvtdModelExtent rdbmsExtent = new BasicQvtdModelExtent();
		QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		assertThrows(QvtdExecutionException.class, () -> engine.execute(t, ctx),
				"Should throw when relation depth limit exceeded");
	}

	// ── R-7 / M-R4: Timeout enforcement ───────────────────────────────

	@Test
	void r7_timeout_exceedingDeadline_terminates() throws QvtdParseException {
		// The deadline is driven, not raced: the clock stands still while the deadline
		// is computed and jumps past it before the first per-binding check. Making the
		// test depend on wall-clock speed made it fail intermittently (#61) — and a
		// timeout test that passes because the machine was slow proves nothing.
		String source = """
				transformation timeoutTest(uml : simpleuml, rdbms : simplerdbms) {
				    top relation PackageToSchema {
				        n : String;
				        checkonly domain uml p : Package { name = n };
				        enforce domain rdbms s : Schema { name = n };
				    }
				}
				""";

		QvtdConfiguration config = QvtdConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.timeoutMs(1)
				.build();
		RelationalTransformation t = QvtdEngines.create(config).parse(source, "timeoutTest");

		QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
				Map.of("uml", createUmlExtentMany(3), "rdbms", new BasicQvtdModelExtent()));

		AtomicLong now = new AtomicLong();
		QvtrEvaluator evaluator = new QvtrEvaluator(
				OclEngines.create(new OclParserSupport()), new QvtrEvalEnvironment(),
				QvtrRun.of(t, ctx, config),
				// first reading sets the deadline, every later one is past it
				() -> now.getAndAdd(TimeUnit.SECONDS.toNanos(1)));

		QvtdExecutionException failure = assertThrows(QvtdExecutionException.class,
				evaluator::execute, "Should throw when the deadline has passed");
		assertTrue(failure.getMessage().contains("timeout"), failure::getMessage);
	}

	@Test
	void r7_timeout_withinDeadline_succeeds() throws QvtdParseException {
		String source = """
				transformation normalTest(uml : simpleuml, rdbms : simplerdbms) {
				    top relation PackageToSchema {
				        n : String;
				        checkonly domain uml p : Package { name = n };
				        enforce domain rdbms s : Schema { name = n };
				    }
				}
				""";

		// Generous timeout — should not trigger
		QvtdEngine engine = createEngine(b -> b.timeoutMs(30_000));
		RelationalTransformation t = engine.parse(source, "normalTest");

		QvtdModelExtent umlExtent = createUmlExtent("TestPkg");
		QvtdModelExtent rdbmsExtent = new BasicQvtdModelExtent();
		QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		QvtdExecutionResult result = engine.execute(t, ctx);
		assertTrue(result.isSuccess(), "Should succeed within timeout");
	}

	// ── Config defaults ───────────────────────────────────────────────

	@Test
	void config_defaults_haveCorrectValues() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtdConfiguration config = QvtdConfiguration.builder(oclConfig).build();

		assertEquals(200, config.maxRelationDepth());
		assertEquals(10_000, config.maxBindings());
		assertEquals(0, config.timeoutMs());
		assertEquals(100_000, config.maxTraceRecords());
		assertFalse(config.blackboxEnabled());
		assertEquals(Set.of(), config.allowedBlackboxModules());
	}

	@Test
	void config_builderMethods_setValues() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
				.maxRelationDepth(50)
				.maxBindings(500)
				.timeoutMs(5000)
				.maxTraceRecords(1000)
				.blackboxEnabled(true)
				.allowedBlackboxModules(Set.of("mylib"))
				.build();

		assertEquals(50, config.maxRelationDepth());
		assertEquals(500, config.maxBindings());
		assertEquals(5000, config.timeoutMs());
		assertEquals(1000, config.maxTraceRecords());
		assertTrue(config.blackboxEnabled());
		assertEquals(Set.of("mylib"), config.allowedBlackboxModules());
	}

	// ── R-2 / M-R3: Binding set limit ────────────────────────────────

	@Test
	void r2_crossProduct_exceedingBindingLimit_terminates() throws QvtdParseException {
		// Many classes in source → pattern matcher generates cross-product bindings
		String source = """
				transformation bindingTest(uml : simpleuml, rdbms : simplerdbms) {
				    top relation ClassToTable {
				        cn : String;
				        checkonly domain uml c : Class { name = cn };
				        enforce domain rdbms t : Table { name = cn };
				    }
				}
				""";

		// Very low binding limit — should trigger on 50+ classes
		QvtdEngine engine = createEngine(b -> b.maxBindings(3));
		RelationalTransformation t = engine.parse(source, "bindingTest");

		QvtdModelExtent umlExtent = createUmlExtentMany(50);
		QvtdModelExtent rdbmsExtent = new BasicQvtdModelExtent();
		QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		assertThrows(QvtdExecutionException.class, () -> engine.execute(t, ctx),
				"Should throw when binding limit exceeded");
	}

	// ── R-4 / M-R5: Blackbox enforcement ──────────────────────────────

	@Test
	void r4_blackboxDisabled_byDefault() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtdConfiguration config = QvtdConfiguration.builder(oclConfig).build();

		assertFalse(config.blackboxEnabled(),
				"Blackbox should be disabled by default (D29)");
	}

	@Test
	@DisplayName("R-4/M-R5: a blackbox query of a module the allow-list does not name is not invoked")
	void r4_blackboxAllowList_blocksUnlisted() throws Exception {
		// Before #180 this test asserted only that the transformation succeeded — which it does
		// either way — while production compared the allow-list against the *operation* name and
		// never consulted it for queries at all. The sentinel is what makes the guard real.
		AtomicBoolean invoked = new AtomicBoolean();
		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(library("helpers", "TypeMap", () -> {
			invoked.set(true);
			return "BLOCKED_SHOULD_NOT_REACH";
		}));

		QvtdExecutionResult result = runWithBlackbox(registry, Set.of("otherlib"));

		assertFalse(invoked.get(), "the library of an unlisted module must not be invoked");
		assertTrue(result.diagnostics().stream().noneMatch(d -> String.valueOf(d.getMessage()).contains("BLOCKED")),
				"and nothing of it reaches the run");
	}

	@Test
	@DisplayName("R-4/M-R5: the same query of a module the allow-list names is invoked")
	void r4_blackboxAllowList_allowsListed() throws Exception {
		AtomicBoolean invoked = new AtomicBoolean();
		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(library("helpers", "TypeMap", () -> {
			invoked.set(true);
			return "NUMBER";
		}));

		runWithBlackbox(registry, Set.of("helpers"));

		assertTrue(invoked.get(), "the allow-list names the module, so the library is used");
	}

	@Test
	@DisplayName("R-4: a blackbox that throws is an error diagnostic, not a silent fallthrough")
	void r4_failingBlackbox_isReported() throws Exception {
		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(library("helpers", "TypeMap", () -> {
			throw new IllegalStateException("library on fire");
		}));

		QvtdExecutionResult result = runWithBlackbox(registry, Set.of());

		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("library on fire")),
				() -> "the failure has to reach the caller: " + result.diagnostics());
	}

	@Test
	void r8_traceLimit_dropsRecordsAtLimit() {
		QvtrTraceManager traceManager = new QvtrTraceManager(5);

		// Create a mock relation
		Relation relation = QvtrelationFactory.eINSTANCE.createRelation();
		relation.setName("TestRelation");

		// Record 5 traces — should all succeed
		for (int i = 0; i < 5; i++) {
			traceManager.record(relation, Map.of("var", "val" + i));
		}
		assertEquals(5, traceManager.lookup(relation, List.of()).size(),
				"Should store exactly maxTraceRecords entries");

		// Record a 6th — should be silently dropped
		traceManager.record(relation, Map.of("var", "val_overflow"));
		assertEquals(5, traceManager.lookup(relation, List.of()).size(),
				"Should not exceed maxTraceRecords limit");
	}

	// ── Helpers ───────────────────────────────────────────────────────

	@FunctionalInterface
	interface ConfigCustomizer {
		void customize(QvtdConfiguration.Builder builder);
	}

	private QvtdEngine createEngine(ConfigCustomizer customizer) {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtdConfiguration.Builder builder = QvtdConfiguration.builder(oclConfig);
		customizer.customize(builder);
		return QvtdEngines.create(builder.build());
	}

	private QvtdModelExtent createUmlExtent(String packageName) {
		EClass pkgClass = ecoreHelper.getEClass(umlPackage, "Package");
		EObject pkg = EcoreUtil.create(pkgClass);
		pkg.eSet(pkgClass.getEStructuralFeature("name"), packageName);
		return new BasicQvtdModelExtent(List.of(pkg));
	}

	@SuppressWarnings("unchecked")
	private QvtdModelExtent createUmlExtentMany(int count) {
		EClass pkgClass = ecoreHelper.getEClass(umlPackage, "Package");
		EObject root = EcoreUtil.create(pkgClass);
		root.eSet(pkgClass.getEStructuralFeature("name"), "root");

		EClass classClass = ecoreHelper.getEClass(umlPackage, "Class");
		for (int i = 0; i < count; i++) {
			EObject clazz = EcoreUtil.create(classClass);
			clazz.eSet(classClass.getEStructuralFeature("name"), "C" + i);
			clazz.eSet(classClass.getEStructuralFeature("kind"), "persistent");
			((List<EObject>) root.eGet(pkgClass.getEStructuralFeature("ownedClass"))).add(clazz);
		}
		return new BasicQvtdModelExtent(List.of(root));
	}

	@Test
	@DisplayName("R-1: a query that calls itself ends in a diagnostic, not a StackOverflowError")
	void r1_recursiveQuery_exceedingDepth_terminates() throws Exception {
		// Relations have had a depth counter all along; queries had none, and a StackOverflowError
		// is an Error — the engine's catch (Exception) never saw it, the thread died (#181)
		String source = """
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					query Countdown(n : Integer) : Integer { Countdown(n + 1) }

					top relation R {
						cn : String;
						checkonly domain uml c : Class { name = cn };
						enforce domain rdbms t : Table { name = cn };
						when { Countdown(0) > 0; }
					}
				}
				""";
		QvtdEngine engine = createEngine(b -> b.maxRelationDepth(20));
		RelationalTransformation t = engine.parse(source, "T");
		QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
				Map.of("uml", createUmlExtentMany(1), "rdbms", new BasicQvtdModelExtent()));

		// The limit is reached inside a guard, which OCL evaluates — so it arrives as a
		// diagnostic and the run fails, rather than as a thrown exception. What matters is that
		// it arrives at all: before #181 the JVM stack ran out and the thread died.
		QvtdExecutionResult result = assertTimeoutPreemptively(Duration.ofSeconds(20),
				() -> engine.execute(t, ctx));
		assertFalse(result.isSuccess(), "a run whose guard blew the depth limit is not a success");
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> String.valueOf(d.getMessage()).contains("Query call depth limit exceeded")),
				() -> "the limit has to be named: " + result.diagnostics());
	}

	// ── helpers for the blackbox tests (#180) ─────────────────────────

	/** A library that serves exactly one operation of one module, and says so. */
	private static QvtdBlackboxLibrary library(String moduleName, String operationName, Supplier<Object> body) {
		return new QvtdBlackboxLibrary() {
			@Override
			public String getModuleName() {
				return moduleName;
			}

			@Override
			public String getUnitQualifiedName() {
				return moduleName;
			}

			@Override
			public List<String> getUsedPackageURIs() {
				return List.of();
			}

			@Override
			public List<String> getOperationNames() {
				return List.of(operationName);
			}

			@Override
			public Object invoke(String name, Object self, Object[] args) {
				return operationName.equals(name) ? body.get() : null;
			}
		};
	}

	/**
	 * Runs a transformation whose enforced value comes from a blackbox query, with blackboxes
	 * enabled and the given allow-list.
	 */
	private QvtdExecutionResult runWithBlackbox(QvtdBlackboxRegistry registry, Set<String> allowed)
			throws QvtdParseException {
		String source = """
				transformation T(uml : simpleuml, rdbms : simplerdbms) {
					query TypeMap(umlType : String) : String;

					top relation R {
						cn : String;
						checkonly domain uml c : Class { name = cn };
						enforce domain rdbms t : Table { name = TypeMap(cn) };
					}
				}
				""";
		QvtdEngine engine = createEngine(b -> b.blackboxRegistry(registry)
				.blackboxEnabled(true).allowedBlackboxModules(allowed));
		RelationalTransformation t = engine.parse(source, "T");
		QvtdModelExtent rdbmsExtent = new BasicQvtdModelExtent();
		return engine.execute(t, QvtdExecutionContext.enforce("rdbms",
				Map.of("uml", createUmlExtentMany(1), "rdbms", rdbmsExtent)));
	}


}
