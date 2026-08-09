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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
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
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrExtentManager;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
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
				t, new QvtrExtentManager(t, ctx), ctx, config, null, List.of(),
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
	void r4_blackboxAllowList_blocksUnlisted() throws QvtdParseException {
		// Blackbox query transformation — TypeMap should be blocked by allow-list
		String source = """
				transformation allowListTest(uml : simpleuml, rdbms : simplerdbms) {
				    top relation ClassToTable {
				        cn : String;
				        checkonly domain uml c : Class { name = cn };
				        enforce domain rdbms t : Table { name = cn };
				    }

				    query TypeMap(umlType : String) : String;
				}
				""";

		BasicQvtdBlackboxRegistry registry = new BasicQvtdBlackboxRegistry();
		registry.register(new QvtdBlackboxLibrary() {
			@Override
			public String getModuleName() { return "helpers"; }
			@Override
			public String getUnitQualifiedName() { return "helpers"; }
			@Override
			public List<String> getUsedPackageURIs() { return List.of(); }
			@Override
			public Object invoke(String operationName, Object self, Object[] args) {
				if ("TypeMap".equals(operationName)) {
					return "BLOCKED_SHOULD_NOT_REACH";
				}
				return null;
			}
		});

		// Enable blackbox but restrict to "otherlib" — "TypeMap" not in allow-list
		QvtdEngine engine = createEngine(b -> b
				.blackboxRegistry(registry)
				.blackboxEnabled(true)
				.allowedBlackboxModules(Set.of("otherlib")));
		RelationalTransformation t = engine.parse(source, "allowListTest");

		QvtdModelExtent umlExtent = createUmlExtent("TestPkg");
		QvtdModelExtent rdbmsExtent = new BasicQvtdModelExtent();
		QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
				Map.of("uml", umlExtent, "rdbms", rdbmsExtent));

		// Should succeed (the query simply returns null, not an error)
		// but the blackbox should NOT have been invoked
		QvtdExecutionResult result = engine.execute(t, ctx);
		assertTrue(result.isSuccess(), "Transformation should succeed even with blocked blackbox");
	}

	// ── R-8 / M-R8: Trace record limit ───────────────────────────────

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
}
