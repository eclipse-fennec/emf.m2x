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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for {@code result} variable edge-cases
 * (§8.1.18, §8.2.1.17, §8.2.1.19).
 *
 * <p>{@code result} is the name of the unique result parameter. When there
 * are multiple result parameters, it is a tuple. When a single result parameter
 * is explicitly named, no pre-defined {@code result} variable exists.
 *
 * <p>Eclipse reference: {@code useresultinsameout.qvto},
 * {@code collectionMappingResult.qvto}, {@code resolvebeforeoutcompletion.qvto}
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eResultEdgeCaseTest extends AbstractQvtoEngineTest {

	// ==== P7-03: `result` Edge-Cases (§8.1.18, §8.2.1.17, §8.2.1.19) ====

	@Test
	void result_accessInInitBeforeInstantiation() throws Exception {
		// §8.2.1.19: result is null in init before explicit assignment or auto-instantiation
		// Eclipse resolvebeforeoutcompletion.qvto: result := pack.main1() in init
		EObject src = createSourceElement("elem", 10);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        init {
				            log(if result = null then 'init:null' else 'init:set' endif);
				            result := object TargetElement { name := 'from_init'; };
				        }
				        value := self.value;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// §8.2.1.15: "The initial value of each out and result parameter is null" — the object
		// init assigns is the result, no second instance is created (#332)
		assertLogged(result, "init:null");
		assertEquals(1, outExtent.getContents().size());
		assertEquals("from_init", eGet(outExtent.getContents().get(0), "name"));
		assertEquals(10, eGet(outExtent.getContents().get(0), "value"));
	}

	@Test
	void result_propertyAssignmentInInit_isAnError() throws Exception {
		// §8.2.1.15: in init the result is still null, so assigning one of its properties has no
		// target (#332); before, the result was instantiated ahead of init
		EObject src = createSourceElement("elem", 10);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        init {
				            result.name := 'from_init';
				        }
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", new BasicQvtoModelExtent(src), emptyExtent());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("the target is null")),
				() -> result.diagnostics().toString());
	}

	@Test
	void result_accessibleInEndSection() throws Exception {
		// §8.2.1.19: result accessible in end section — reflects population result
		// Eclipse collectionMappingResult.qvto: assert fatal (result->notEmpty()) in end
		EObject src = createSourceElement("item", 5);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := 'populated';
				        value := self.value;
				        end {
				            log(result.name);
				        }
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "populated");
	}

	@Test
	void result_passedAsArgument() throws Exception {
		// §8.2.1.17: result can be passed as argument to operations
		// Eclipse useresultinsameout.qvto: getPackClassifiers(result)
		EObject src = createSourceElement("orig", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    helper decorateName(target : TargetElement) : String {
				        return 'decorated:' + target.name;
				    }
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.name;
				        value := self.value;
				        end {
				            log(decorateName(result));
				        }
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "decorated:orig");
	}

	@Test
	void result_inHelperReturnValue() throws Exception {
		// §8.2.1.17: result in helper = return value
		// Helper with result parameter — result is the return value
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper compute() : Integer {
				        result := 42;
				    }
				    main() {
				        var v := compute();
				        log(v.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
	}

	@Test
	void result_reassignedInInit() throws Exception {
		// §8.2.1.19: result := in init prevents auto-instantiation
		// Eclipse resolvebeforeoutcompletion.qvto: result := pack.main1() in init
		EObject src1 = createSourceElement("first", 1);
		EObject src2 = createSourceElement("second", 2);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.name;
				        value := self.value;
				    }
				    mapping SourceElement::copyFirst() : TargetElement {
				        init {
				            result := s.objectsOfType(SourceElement)->any(e | e.name = 'first').map toTarget();
				        }
				        name := result.name + '_copied';
				    }
				    main() {
				        s.objectsOfType(SourceElement)->any(e | e.name = 'second').map copyFirst();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		// result was set in init from another mapping, then name overwritten in body
		assertTrue(outExtent.getContents().size() >= 1);
		boolean found = outExtent.getContents().stream()
				.anyMatch(obj -> "first_copied".equals(eGet(obj, "name")));
		assertTrue(found, "Expected 'first_copied' in output, got: "
				+ outExtent.getContents().stream().map(o -> String.valueOf(eGet(o, "name"))).toList());
	}

	@Test
	void result_implicitPropertyAccess() throws Exception {
		// §8.2.1.19: In mapping body, bare property names target result (not self)
		// name := 'foo' is equivalent to result.name := 'foo'
		EObject src = createSourceElement("src", 7);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := 'explicit';
				        value := 99;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
		assertEquals("explicit", eGet(outExtent.getContents().get(0), "name"));
		assertEquals(99, eGet(outExtent.getContents().get(0), "value"));
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

	private static Object eGet(EObject obj, String featureName) {
		return obj.eGet(obj.eClass().getEStructuralFeature(featureName));
	}

	@Test
	void trace_isRecordedBeforePopulation_soTheMappingFindsItsOwnResult() throws Exception {
		// §8.1.6: "The trace … is created immediately after the instantiation and hence can be
		// used within any inner mapping operation invoked within the population section" (#332)
		EObject src = createSourceElement("elem", 10);
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := if self.resolveone(TargetElement) = result then 'found' else 'missing' endif;
				        log('again:' + (self.map toTarget() = result).repr());
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", new BasicQvtoModelExtent(src), emptyExtent());
		assertSuccess(result);
		assertLogged(result, "again:true");
	}

	@Test
	void recursiveSelfMapping_endsAtTheTrace_notInAStackOverflow() throws Exception {
		// The same mapping on the same source again is answered from the trace (§8.1.11.1);
		// before #332 it recursed until the Java stack overflowed out of execute()
		EObject src = createSourceElement("elem", 10);
		QvtoModelExtent outExtent = emptyExtent();
		executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.map toTarget().name + 'x';
				    }
				    main() {
				        s.objectsOfType(SourceElement)->map toTarget();
				    }
				}
				""", new BasicQvtoModelExtent(src), outExtent);
		assertEquals(1, outExtent.getContents().size());
	}

	@Test
	void collectionResult_startsEmpty() throws Exception {
		// §8.2.1.15: "Collection types are initialized with empty collections" (#332)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    mapping names() : r : Sequence(String) {
				        init {
				            log('init-size:' + r->size().repr());
				        }
				        r += 'a';
				    }
				    main() {
				        log('size:' + map names()->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "init-size:0");
		assertLogged(result, "size:1");
	}
}
