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
package org.eclipse.fennec.m2x.qvto.tests.resolve;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Trace records and resolution against QVT v1.3 §8.1.11, §8.2.1.22, §8.2.1.23 (#334).
 */
class QvtoTraceResolveSpecConformanceTest extends AbstractQvtoEngineTest {

	private static QvtoExecutionResult run(String body) throws Exception {
		EClass containerClass = ecoreHelper.getEClass(sourcePackage, "SourceContainer");
		EObject container = EcoreUtil.create(containerClass);
		return executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				""" + body, new BasicQvtoModelExtent(createSourceElement("a", 1), createSourceElement("b", 2), container),
				emptyExtent());
	}

	@Test
	void resolve_seesEveryResultOfAMapping() throws Exception {
		// §8.1.11.3: resolve yields out-parameters union result-parameters
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::two() : first : TargetElement, second : TargetElement {
				        first.name := 'one'; second.name := 'two';
				    }
				    main() {
				        var e := s.objectsOfType(SourceElement)->first();
				        e.map two();
				        log('all:' + e.resolve()->size().repr());
				        log('typed:' + e.resolve(TargetElement)->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "all:2");
		assertLogged(result, "typed:2");
	}

	@Test
	void aNonContextualMapping_isResolvedFromItsFirstObjectArgument() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping toT(e : SourceElement) : TargetElement { name := e.name; }
				    main() {
				        var e := s.objectsOfType(SourceElement)->first();
				        map toT(e);
				        log('resolved:' + e.resolveone(TargetElement).name);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "resolved:a");
	}

	@Test
	void aVoidMapping_runsOnceForTheSameSource() throws Exception {
		// §8.1.11.2: the duplicate execution is suppressed, with or without a result
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::noResult() { init { log('nr-body'); } }
				    main() {
				        var e := s.objectsOfType(SourceElement)->first();
				        e.map noResult();
				        e.map noResult();
				    }
				}
				""");
		assertSuccess(result);
		assertTrue(result.diagnostics().stream().filter(d -> d.getMessage().contains("nr-body")).count() == 1,
				() -> result.diagnostics().toString());
	}

	@Test
	void resolveIn_countsTheInvokedDisjunctingMapping() throws Exception {
		// §8.1.11.4: (invoked-mapping = MAPPING) or (executed-mapping = MAPPING)
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::c1() : TargetElement { name := 'c1'; }
				    mapping SourceElement::d() : TargetElement disjuncts SourceElement::c1 {}
				    main() {
				        var e := s.objectsOfType(SourceElement)->first();
				        e.map d();
				        log('viaD:' + e.resolveIn(SourceElement::d, TargetElement)->size().repr());
				        log('viaC1:' + e.resolveIn(SourceElement::c1, TargetElement)->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "viaD:1");
		assertLogged(result, "viaC1:1");
	}

	@Test
	void resolveIn_distinguishesMappingsOfTheSameNameOnOtherTypes() throws Exception {
		// §8.2.1.23: "by a unique mapping operation"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toT() : TargetElement { name := 'elem'; }
				    mapping SourceContainer::toT() : TargetElement { name := 'cont'; }
				    main() {
				        s.objectsOfType(SourceElement)->map toT();
				        s.objectsOfType(SourceContainer)->map toT();
				        log('elems:' + resolveIn(SourceElement::toT, TargetElement)->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "elems:2");
	}

	@Test
	void resolveone_onACollection_isASequence() throws Exception {
		// §8.2.1.22: on a collection the operator is a shorthand for xcollect
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toT() : TargetElement { name := self.name; }
				    main() {
				        var elems := s.objectsOfType(SourceElement);
				        elems->map toT();
				        var res := elems->resolveone(TargetElement);
				        log('size:' + res->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
	}

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "Expected log containing '" + expected + "' but diagnostics were: " + result.diagnostics());
	}
}
