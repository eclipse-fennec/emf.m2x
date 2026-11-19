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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * §8.1.3.3: "The Models passed to nested transformation calls by Transformation::transform() or
 * Transformation::parallelTransform() may not be modified after the call" — while a parallel
 * transformation runs on a model, neither a second one nor the caller may change it (#385).
 */
class QvtoParallelTransformModelTest {

	private static final String HEADER = "modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');\n";

	private static QvtoEngine engine;

	private static QvtoUnit.SourceUnit unit(String name, String source) {
		return new QvtoUnit.SourceUnit(name, URI.createURI("inline:" + name), HEADER + source);
	}

	@BeforeAll
	static void setUpEngine() {
		QvtoUnitResolver resolver = name -> switch (name) {
			case "Nested" -> Optional.of(unit(name, """
					transformation Nested(inout m : ECORE) {
					    main() { m.objectsOfType(EPackage)->forEach(p) { p.name := p.name + '-nested'; }; }
					}
					"""));
			case "Appender" -> Optional.of(unit(name, """
					transformation Appender(inout m : ECORE) {
					    main() { m.objectsOfType(EPackage)->forEach(p) { p.name := p.name + '-appended'; }; }
					}
					"""));
			case "Reader" -> Optional.of(unit(name, """
					transformation Reader(in m : ECORE) {
					    main() { log('read ' + m.objectsOfType(EPackage)->size().repr()); }
					}
					"""));
			default -> Optional.empty();
		};
		// Starts a parallel transformation only after a delay, so the caller's next statement
		// surely runs while the transformation is still pending
		Executor delayed = CompletableFuture.delayedExecutor(300, TimeUnit.MILLISECONDS);
		engine = QvtoEngines.create(QvtoConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(resolver)
				.unitResolverEnabled(true)
				.parallelExecutor(delayed)
				.build());
	}

	private static EPackage pkg(String name) {
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName(name);
		return pkg;
	}

	private static QvtoExecutionResult run(String main, QvtoModelExtent... extents) throws Exception {
		return engine.execute(engine.parse(HEADER + main, "Main"), QvtoExecutionContext.of(extents));
	}

	private static List<String> messages(QvtoExecutionResult result, int severity) {
		return result.diagnostics().stream().filter(d -> d.getSeverity() == severity)
				.map(Diagnostic::getMessage).toList();
	}

	@Test
	void twoParallelTransformations_onSeparateModels_bothApply() throws Exception {
		EPackage a = pkg("A");
		EPackage b = pkg("B");
		QvtoExecutionResult result = run("""
				transformation Main(inout a : ECORE, inout b : ECORE) access Nested, Appender {
				    main() {
				        var s1 := new Nested(a).parallelTransform();
				        var s2 := new Appender(b).parallelTransform();
				        wait(Set{s1, s2});
				        log('ok=' + (s1.succeeded() and s2.succeeded()).repr());
				    }
				}
				""", new BasicQvtoModelExtent(a), new BasicQvtoModelExtent(b));
		assertEquals(List.of("log: ok=true"), messages(result, Diagnostic.INFO), result.diagnostics().toString());
		assertEquals("A-nested", a.getName());
		assertEquals("B-appended", b.getName());
	}

	@Test
	void aSecondParallelTransformation_onAModelInUse_fails_andDoesNotRun() throws Exception {
		EPackage p = pkg("P");
		QvtoExecutionResult result = run("""
				transformation Main(inout m : ECORE) access Nested, Appender {
				    main() {
				        var s1 := new Nested(m).parallelTransform();
				        var s2 := new Appender(m).parallelTransform();
				        wait(Set{s1, s2});
				        log('s1=' + s1.succeeded().repr() + ' s2=' + s2.succeeded().repr());
				    }
				}
				""", new BasicQvtoModelExtent(p));
		assertEquals(List.of("log: s1=true s2=false"), messages(result, Diagnostic.INFO), result.diagnostics().toString());
		assertEquals("P-nested", p.getName());
		List<String> errors = messages(result, Diagnostic.ERROR);
		assertEquals(1, errors.size(), result.diagnostics().toString());
		assertTrue(errors.get(0).contains("model 'm' is in use by the running transformation 'Nested'")
				&& errors.get(0).contains("§8.1.3.3"), errors.get(0));
	}

	@Test
	void twoParallelReaders_mayShareAModel() throws Exception {
		QvtoExecutionResult result = run("""
				transformation Main(inout m : ECORE) access Reader {
				    main() {
				        var s1 := new Reader(m).parallelTransform();
				        var s2 := new Reader(m).parallelTransform();
				        wait(Set{s1, s2});
				        log('s1=' + s1.succeeded().repr() + ' s2=' + s2.succeeded().repr());
				    }
				}
				""", new BasicQvtoModelExtent(pkg("P")));
		assertTrue(messages(result, Diagnostic.INFO).contains("log: s1=true s2=true"), result.diagnostics().toString());
		assertEquals(List.of(), messages(result, Diagnostic.ERROR));
	}

	@Test
	void theCaller_mayNotModifyAModelInUse_untilItsTransformationEnds() throws Exception {
		EPackage p = pkg("P");
		QvtoExecutionResult result = run("""
				transformation Main(inout m : ECORE) access Reader {
				    main() {
				        var s := new Reader(m).parallelTransform();
				        m.objectsOfType(EPackage)->forEach(q) { q.name := 'changed'; };
				    }
				}
				""", new BasicQvtoModelExtent(p));
		String failure = result.diagnostics().toString();
		assertTrue(failure.contains("InvalidModelMutation") && failure.contains("'Reader'")
				&& failure.contains("§8.1.3.3"), failure);
		assertEquals("P", p.getName());
	}

	@Test
	void afterWait_theCallerMayModifyTheModelAgain() throws Exception {
		EPackage p = pkg("P");
		QvtoExecutionResult result = run("""
				transformation Main(inout m : ECORE) access Nested {
				    main() {
				        var s := new Nested(m).parallelTransform();
				        wait(Set{s});
				        m.objectsOfType(EPackage)->forEach(q) { q.name := q.name + '-after'; };
				    }
				}
				""", new BasicQvtoModelExtent(p));
		assertEquals(List.of(), messages(result, Diagnostic.ERROR), result.diagnostics().toString());
		assertEquals("P-nested-after", p.getName());
	}

	@Test
	void afterASynchronousTransformWithAnInParameter_theCallerMayModifyTheModel() throws Exception {
		// §8.1.3.3 restricts the model while the nested transformation runs; an in parameter
		// of the nested one does not make the caller's inout model read-only for good
		EPackage p = pkg("P");
		QvtoExecutionResult result = run("""
				transformation Main(inout m : ECORE) access Reader {
				    main() {
				        var s := new Reader(m).transform();
				        m.objectsOfType(EPackage)->forEach(q) { q.name := 'changed'; };
				        object EPackage@m { name := 'added'; };
				    }
				}
				""", new BasicQvtoModelExtent(p));
		assertEquals(List.of(), messages(result, Diagnostic.ERROR), result.diagnostics().toString());
		assertEquals("changed", p.getName());
	}

}
