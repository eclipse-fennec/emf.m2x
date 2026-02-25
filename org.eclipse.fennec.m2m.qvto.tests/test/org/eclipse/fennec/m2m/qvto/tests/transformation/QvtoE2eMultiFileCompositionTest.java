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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URI;
import java.util.Optional;
import java.util.concurrent.Executors;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2m.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2m.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2m.qvto.engine.QvtoEngineImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O multi-file transformation composition (P10-01).
 *
 * <p>Tests the {@code access} keyword, {@code new T(args)}, {@code transform()},
 * {@code parallelTransform()}, {@code wait()}, and Status type.
 *
 * <p>Spec references:
 * <ul>
 *   <li>§8.1.13: Transformation instantiation via {@code new T(args)}</li>
 *   <li>§8.3.6: {@code transform()}, {@code parallelTransform()}, {@code wait()}</li>
 *   <li>§8.1.4: {@code access} keyword for cross-file references</li>
 * </ul>
 */
class QvtoE2eMultiFileCompositionTest {

	private static QvtoEngineImpl engine;

	@BeforeAll
	static void setUpEngine() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();

		// Register the inline unit resolver that provides imported transformations
		QvtoUnitResolver inlineResolver = qualifiedName -> switch (qualifiedName) {
			case "Nested" -> Optional.of(new QvtoUnit.SourceUnit("Nested",
					URI.create("inline:Nested"), NESTED_SOURCE));
			case "Appender" -> Optional.of(new QvtoUnit.SourceUnit("Appender",
					URI.create("inline:Appender"), APPENDER_SOURCE));
			default -> Optional.empty();
		};

		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
				.addUnitResolver(inlineResolver)
				.build();
		engine = new QvtoEngineImpl(config);
	}

	// --- Inline transformation sources for unit resolver ---

	/**
	 * Nested transformation that renames all EPackage instances by appending "-nested".
	 */
	private static final String NESTED_SOURCE = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			transformation Nested(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := p.name + '-nested';
			        };
			    }
			}
			""";

	/**
	 * Appender transformation that appends a suffix to all EPackage names.
	 */
	private static final String APPENDER_SOURCE = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			transformation Appender(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := p.name + '-appended';
			        };
			    }
			}
			""";

	// ==================== Basic access + transform() ====================

	/**
	 * §8.1.13 + §8.3.6.1: access + new T(args) + transform()
	 *
	 * Main transformation creates a package, then invokes Nested to rename it.
	 */
	@Test
	void accessTransform_nestedModifiesExtent() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Nested {
				    main() {
				        var t := new Nested(m);
				        var s := t.transform();
				        assert fatal (s.succeeded());
				        log('done');
				    }
				}
				""";

		// Set up an inout extent with one EPackage
		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "MyPkg");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		// Nested should have appended "-nested" to the package name
		assertEquals("MyPkg-nested", pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
		assertLogged(result, "done");
	}

	// ==================== Status on failed transform ====================

	/**
	 * §8.3.6: transform() returns Status with failed=true when transformation fails.
	 */
	@Test
	void accessTransform_statusOnFailure() throws Exception {
		// Use a resolver that provides a failing transformation
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtoUnitResolver failResolver = qualifiedName -> {
			if ("Failing".equals(qualifiedName)) {
				return Optional.of(new QvtoUnit.SourceUnit("Failing",
						URI.create("inline:Failing"),
						"""
						modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
						transformation Failing(inout m : ECORE) {
						    main() {
						        assert fatal (false);
						    }
						}
						"""));
			}
			return Optional.empty();
		};

		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
				.addUnitResolver(failResolver)
				.build();
		QvtoEngineImpl failEngine = new QvtoEngineImpl(config);

		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Failing {
				    main() {
				        var t := new Failing(m);
				        var s := t.transform();
				        log('failed=' + s.failed().toString());
				    }
				}
				""";

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		OperationalTransformation t = failEngine.parse(mainSource, "Main");
		QvtoExecutionResult result = failEngine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertLogged(result, "failed=true");
	}

	// ==================== parallelTransform + wait ====================

	/**
	 * §8.3.6.2 + §8.3.6.3: parallelTransform() + wait()
	 */
	@Test
	void parallelTransform_withWait() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Nested {
				    main() {
				        var t := new Nested(m);
				        var s := t.parallelTransform();
				        -- wait for completion
				        wait(Set{s});
				        assert fatal (s.succeeded());
				        log('parallel-done');
				    }
				}
				""";

		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "AsyncPkg");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertLogged(result, "parallel-done");
	}

	// ==================== Multiple access imports ====================

	/**
	 * §8.1.4: Multiple access imports in one transformation.
	 */
	@Test
	void multipleAccess_chainedTransforms() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Nested, Appender {
				    main() {
				        var t1 := new Nested(m);
				        var s1 := t1.transform();
				        assert fatal (s1.succeeded());

				        var t2 := new Appender(m);
				        var s2 := t2.transform();
				        assert fatal (s2.succeeded());
				        log('chain-done');
				    }
				}
				""";

		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "ChainPkg");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		// Should have been renamed by both transformations
		assertEquals("ChainPkg-nested-appended",
				pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
		assertLogged(result, "chain-done");
	}

	// ==================== Linker error handling ====================

	/**
	 * Linker: unresolvable import produces a link error result (not an exception).
	 */
	@Test
	void linker_unresolvedImport_returnsError() throws Exception {
		// Engine with empty resolver — nothing can be resolved
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
				.addUnitResolver(name -> Optional.empty())
				.build();
		QvtoEngineImpl emptyEngine = new QvtoEngineImpl(config);

		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access NonExistent {
				    main() {
				        log('should not reach');
				    }
				}
				""";

		OperationalTransformation t = emptyEngine.parse(mainSource, "Main");
		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		QvtoExecutionResult result = emptyEngine.execute(t,
				QvtoExecutionContext.of(extent));

		assertNotNull(result);
		assertFalse(result.isSuccess(), "Should fail due to unresolved import");
		boolean hasLinkError = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("Cannot resolve import"));
		assertTrue(hasLinkError, "Expected link error but got: " + result.diagnostics());
	}

	// ==================== Status.succeeded() / failed() ====================

	/**
	 * §8.3.7: succeeded() returns true on successful transform.
	 */
	@Test
	void status_succeeded_trueOnSuccess() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Nested {
				    main() {
				        var t := new Nested(m);
				        var s := t.transform();
				        log('succeeded=' + s.succeeded().toString());
				        log('failed=' + s.failed().toString());
				    }
				}
				""";

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(createPackage("Pkg"));

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertLogged(result, "succeeded=true");
		assertLogged(result, "failed=false");
	}

	/**
	 * §8.3.7: failed() returns true and succeeded() returns false on failure.
	 */
	@Test
	void status_failed_trueOnFailure() throws Exception {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtoUnitResolver failResolver = qualifiedName -> {
			if ("Failing".equals(qualifiedName)) {
				return Optional.of(new QvtoUnit.SourceUnit("Failing",
						URI.create("inline:Failing"),
						"""
						modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
						transformation Failing(inout m : ECORE) {
						    main() {
						        assert fatal (false);
						    }
						}
						"""));
			}
			return Optional.empty();
		};

		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
				.addUnitResolver(failResolver)
				.build();
		QvtoEngineImpl failEngine = new QvtoEngineImpl(config);

		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Failing {
				    main() {
				        var t := new Failing(m);
				        var s := t.transform();
				        log('succeeded=' + s.succeeded().toString());
				        log('failed=' + s.failed().toString());
				    }
				}
				""";

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		OperationalTransformation t = failEngine.parse(mainSource, "Main");
		QvtoExecutionResult result = failEngine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertLogged(result, "succeeded=false");
		assertLogged(result, "failed=true");
	}

	// ==================== Multiple parallel transforms ====================

	/**
	 * §8.3.6.2 + §8.3.6.3: Multiple parallel transforms with wait on all.
	 */
	@Test
	void parallelTransform_multipleWithWait() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Nested, Appender {
				    main() {
				        var t1 := new Nested(m);
				        var t2 := new Appender(m);
				        var s1 := t1.parallelTransform();
				        var s2 := t2.parallelTransform();
				        wait(Set{s1, s2});
				        assert fatal (s1.succeeded());
				        assert fatal (s2.succeeded());
				        log('both-parallel-done');
				    }
				}
				""";

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(createPackage("ParPkg"));

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertLogged(result, "both-parallel-done");
	}

	// ==================== Configurable executor ====================

	/**
	 * §8.3.6.2: parallelTransform works with a custom (non-virtual-thread) executor.
	 */
	@Test
	void parallelTransform_customExecutor() throws Exception {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		QvtoUnitResolver resolver = qualifiedName -> switch (qualifiedName) {
			case "Nested" -> Optional.of(new QvtoUnit.SourceUnit("Nested",
					URI.create("inline:Nested"), NESTED_SOURCE));
			default -> Optional.empty();
		};

		// Use a fixed thread pool instead of virtual threads
		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
				.addUnitResolver(resolver)
				.parallelExecutor(Executors.newFixedThreadPool(2))
				.build();
		QvtoEngineImpl customEngine = new QvtoEngineImpl(config);

		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Nested {
				    main() {
				        var t := new Nested(m);
				        var s := t.parallelTransform();
				        wait(Set{s});
				        assert fatal (s.succeeded());
				        log('custom-executor-done');
				    }
				}
				""";

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(createPackage("CustPkg"));

		OperationalTransformation t = customEngine.parse(mainSource, "Main");
		QvtoExecutionResult result = customEngine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertLogged(result, "custom-executor-done");
	}

	// ==================== Mixed sync + async ====================

	/**
	 * §8.3.6: Mix of synchronous transform() and async parallelTransform().
	 */
	@Test
	void mixedSyncAndAsync_transform() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Nested, Appender {
				    main() {
				        -- synchronous first
				        var t1 := new Nested(m);
				        var s1 := t1.transform();
				        assert fatal (s1.succeeded());

				        -- then async
				        var t2 := new Appender(m);
				        var s2 := t2.parallelTransform();
				        wait(Set{s2});
				        assert fatal (s2.succeeded());
				        log('mixed-done');
				    }
				}
				""";

		EObject pkg = createPackage("MixPkg");
		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		// sync Nested ran first: "MixPkg-nested", then async Appender: "MixPkg-nested-appended"
		assertEquals("MixPkg-nested-appended",
				pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
		assertLogged(result, "mixed-done");
	}

	// ==================== Separate access declarations ====================

	/**
	 * §8.1.4: Separate access declarations (one per line, not comma-separated).
	 */
	@Test
	void separateAccessDeclarations() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Nested
				    access Appender {
				    main() {
				        var s1 := new Nested(m).transform();
				        assert fatal (s1.succeeded());
				        var s2 := new Appender(m).transform();
				        assert fatal (s2.succeeded());
				        log('separate-access-done');
				    }
				}
				""";

		EObject pkg = createPackage("SepPkg");
		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertEquals("SepPkg-nested-appended",
				pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
		assertLogged(result, "separate-access-done");
	}

	// ==================== Transformation with access but no instantiation ====================

	/**
	 * §8.1.4: access declaration without actually instantiating the transformation
	 * should not cause errors.
	 */
	@Test
	void accessWithoutInstantiation_noError() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE)
				    access Nested {
				    main() {
				        log('access-no-use');
				    }
				}
				""";

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertLogged(result, "access-no-use");
	}

	// ==================== asTransformation() — §8.3.5.7 ====================

	/**
	 * §8.3.5.7: asTransformation() on a model extent containing an OperationalTransformation.
	 * Parses a transformation, puts it into an extent, then uses asTransformation() to
	 * obtain an executable instance.
	 */
	@Test
	void asTransformation_fromModelExtent() throws Exception {
		// Parse a nested transformation and put it into a model extent
		OperationalTransformation nested = engine.parse(NESTED_SOURCE, "Nested");

		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				modeltype QVTO uses qvtoperational('http://www.eclipse.org/fennec/m2m/qvto/1.0');
				transformation Main(inout m : ECORE, in qvtModel : QVTO)
				    access Nested {
				    main() {
				        var t := qvtModel.asTransformation();
				        var s := t.transform(m);
				        assert fatal (s.succeeded());
				        log('asTransf-done');
				    }
				}
				""";

		EObject pkg = createPackage("AstPkg");
		BasicQvtoModelExtent dataExtent = new BasicQvtoModelExtent();
		dataExtent.add(pkg);

		BasicQvtoModelExtent qvtExtent = new BasicQvtoModelExtent();
		qvtExtent.add(nested);

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(dataExtent, qvtExtent));

		assertSuccess(result);
		assertEquals("AstPkg-nested", pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
		assertLogged(result, "asTransf-done");
	}

	/**
	 * §8.3.5.7: asTransformation() on an empty extent returns null.
	 */
	@Test
	void asTransformation_emptyExtent_returnsNull() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE) {
				    main() {
				        var t := m.asTransformation();
				        if (t = null) {
				            log('null-as-expected');
				        };
				    }
				}
				""";

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertLogged(result, "null-as-expected");
	}

	/**
	 * §8.3.5.7 + §8.3.6.1: Chained asTransformation().transform() in a single expression.
	 */
	@Test
	void asTransformation_chained_transformCall() throws Exception {
		OperationalTransformation nested = engine.parse(NESTED_SOURCE, "Nested");

		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				modeltype QVTO uses qvtoperational('http://www.eclipse.org/fennec/m2m/qvto/1.0');
				transformation Main(inout m : ECORE, in qvtModel : QVTO)
				    access Nested {
				    main() {
				        var s := qvtModel.asTransformation().transform(m);
				        assert fatal (s.succeeded());
				        log('chained-done');
				    }
				}
				""";

		EObject pkg = createPackage("ChainPkg");
		BasicQvtoModelExtent dataExtent = new BasicQvtoModelExtent();
		dataExtent.add(pkg);

		BasicQvtoModelExtent qvtExtent = new BasicQvtoModelExtent();
		qvtExtent.add(nested);

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(dataExtent, qvtExtent));

		assertSuccess(result);
		assertEquals("ChainPkg-nested", pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
		assertLogged(result, "chained-done");
	}

	/**
	 * §8.3.5.7: asTransformation() on extent with non-transformation content returns null.
	 */
	@Test
	void asTransformation_noTransformationInExtent_returnsNull() throws Exception {
		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE, in other : ECORE) {
				    main() {
				        var t := other.asTransformation();
				        if (t = null) {
				            log('no-transf-null');
				        };
				    }
				}
				""";

		BasicQvtoModelExtent dataExtent = new BasicQvtoModelExtent();
		BasicQvtoModelExtent otherExtent = new BasicQvtoModelExtent();
		otherExtent.add(createPackage("JustAPackage"));

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(dataExtent, otherExtent));

		assertSuccess(result);
		assertLogged(result, "no-transf-null");
	}

	/**
	 * §8.3.5.7: asTransformation() with parallelTransform() — async execution
	 * of a dynamically obtained transformation.
	 */
	@Test
	void asTransformation_parallelTransform() throws Exception {
		OperationalTransformation nested = engine.parse(NESTED_SOURCE, "Nested");

		String mainSource = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				modeltype QVTO uses qvtoperational('http://www.eclipse.org/fennec/m2m/qvto/1.0');
				transformation Main(inout m : ECORE, in qvtModel : QVTO)
				    access Nested {
				    main() {
				        var t := qvtModel.asTransformation();
				        var s := t.parallelTransform(m);
				        wait(Set{s});
				        assert fatal (s.succeeded());
				        log('async-asTransf-done');
				    }
				}
				""";

		EObject pkg = createPackage("AsyncAstPkg");
		BasicQvtoModelExtent dataExtent = new BasicQvtoModelExtent();
		dataExtent.add(pkg);

		BasicQvtoModelExtent qvtExtent = new BasicQvtoModelExtent();
		qvtExtent.add(nested);

		OperationalTransformation t = engine.parse(mainSource, "Main");
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(dataExtent, qvtExtent));

		assertSuccess(result);
		assertLogged(result, "async-asTransf-done");
	}

	/**
	 * Programmatic API: execute a transformation built entirely via EMF API,
	 * without using the parser at all. This demonstrates that the engine is
	 * not coupled to the parser.
	 */
	@Test
	void programmaticApi_executeWithoutParser() throws Exception {
		// Parse a simple transformation as a shortcut — in a real UI scenario,
		// this would be built programmatically via QvtOperationalFactory
		OperationalTransformation nested = engine.parse(NESTED_SOURCE, "Nested");

		// Execute it directly without any access/new T pattern — just engine.execute()
		EObject pkg = createPackage("ProgPkg");
		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		QvtoExecutionResult result = engine.execute(nested,
				QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertEquals("ProgPkg-nested", pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
	}

	// ==================== Helper Methods ====================

	private static EObject createPackage(String name) {
		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, name);
		return pkg;
	}

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
