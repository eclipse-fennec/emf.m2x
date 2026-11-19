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
package org.eclipse.fennec.m2x.qvto.tests.mapping;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Mapping reuse and candidate selection against QVT v1.3 §8.1.14, §8.1.15, §8.2.1.15 (#333).
 */
class QvtoMappingReuseSpecConformanceTest extends AbstractQvtoEngineTest {

	private static final String HEADER = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			""";

	private static QvtoExecutionResult run(String body) throws Exception {
		return executeWithExtents(HEADER + body,
				new BasicQvtoModelExtent(createSourceElement("plain", 1), createSpecialSourceElement("special", 2, "t")),
				emptyExtent());
	}

	@Test
	void inherits_bindsTheBaseMappingsOwnNames_andRunsItsInitAndEnd() throws Exception {
		// §8.2.1.15: inherited mappings follow the standard invocation semantics
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::base(p : String) : r : TargetElement {
				        init { log('base-init:' + p); }
				        name := p;
				        end { log('base-end'); }
				    }
				    mapping SourceElement::derived(q : String) : z : TargetElement inherits SourceElement::base {
				        label := 'derived';
				    }
				    main() {
				        var e := s.objectsOfType(SourceElement)->first();
				        var res := e.map derived('given');
				        log('name:' + res.name + ' label:' + res.label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "base-init:given");
		assertLogged(result, "base-end");
		assertLogged(result, "name:given label:derived");
	}

	@Test
	void merges_runAfterEnd_withTheirOwnResultName() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::extra() : rr : TargetElement {
				        label := 'merged';
				        end { log('merged-end'); }
				    }
				    mapping SourceElement::main1() : TargetElement merges SourceElement::extra {
				        name := 'main';
				    }
				    main() {
				        var res := s.objectsOfType(SourceElement)->first().map main1();
				        log('name:' + res.name + ' label:' + res.label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "merged-end");
		assertLogged(result, "name:main label:merged");
	}

	@Test
	void overloads_areSelectedByArgumentCount() throws Exception {
		// §8.2.1.15: "overloads as a consequence of matching name and argument count"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::m() : TargetElement { label := 'zero'; }
				    mapping SourceElement::m(x : String) : TargetElement { label := x; }
				    main() {
				        var e := s.objectsOfType(SourceElement)->first();
				        log('one:' + e.map m('ARG').label);
				        log('none:' + e.map m().label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "one:ARG");
		assertLogged(result, "none:zero");
	}

	@Test
	void aMappingOnANonConformingSource_answersNull() throws Exception {
		// §8.2.1.15: "the type compliance of the actual object parameters … If this fails, null is returned"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SpecialSourceElement::only() : TargetElement { name := 'ran'; }
				    main() {
				        var plain := s.objectsOfType(SourceElement)->first();
				        log('plain:' + plain.map only().oclIsUndefined().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "plain:true");
	}

	@Test
	void aNullSource_answersNull_withoutRunningTheBody() throws Exception {
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::m() : TargetElement { log('ran-with-null-self'); }
				    main() {
				        var e : SourceElement := null;
				        log('null:' + e.map m().oclIsUndefined().repr());
				    }
				}
				""");
		assertTrue(result.diagnostics().stream().noneMatch(d -> d.getMessage().contains("ran-with-null-self")),
				() -> result.diagnostics().toString());
	}

	@Test
	void explicitDisjuncts_checkTheCandidateTypes() throws Exception {
		// §8.1.14.1: "each source and argument must conform to the type of the corresponding candidate"
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SpecialSourceElement::sp() : TargetElement { label := 'special'; }
				    mapping SourceElement::gen() : TargetElement { label := 'gen'; }
				    mapping SourceElement::pick() : TargetElement disjuncts SpecialSourceElement::sp, SourceElement::gen {}
				    main() {
				        var plain := s.objectsOfType(SourceElement)->first();
				        log('picked:' + plain.map pick().label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "picked:gen");
	}

	@Test
	void strictDisjunction_selectsTheFirstConformingCandidate_andFailsItsPrecondition() throws Exception {
		// §8.1.14.3: strict — the predicate is a pre-condition, not a guard
		QvtoExecutionResult result = run("""
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::c3a() : TargetElement when { false } { label := 'c3a'; }
				    mapping SourceElement::c3b() : TargetElement { label := 'c3b'; }
				    mapping SourceElement::d3() : TargetElement disjuncts SourceElement::c3a, SourceElement::c3b {}
				    main() {
				        s.objectsOfType(SourceElement)->first().xmap d3();
				    }
				}
				""");
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("pre-condition failed for mapping: c3a")),
				() -> result.diagnostics().toString());
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
