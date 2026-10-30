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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Model types, extents and model assignment against QVT v1.3 §8.1.2, §8.1.3, §8.1.10, §8.2.1.6 (#337).
 */
class QvtoModelTypeSpecConformanceTest extends AbstractQvtoEngineTest {

	private static EObject targetElement(String name) {
		EObject element = EcoreUtil.create(ecoreHelper.getEClass(targetPackage, "TargetElement"));
		element.eSet(element.eClass().getEStructuralFeature("name"), name);
		return element;
	}

	@Test
	void aViolatedWhereCondition_keepsTheTransformationFromRunning() throws Exception {
		// §8.2.1.6: "model compliance implies that all the extra conditions … are satisfied"
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0' where { self.objects()->size() > 100 };
				transformation test(in s : SRC) {
				    main() { log('ran'); }
				}
				""", new BasicQvtoModelExtent(createSourceElement("a", 1)));
		assertError(result, "Model 's' does not satisfy the where condition of model type 'SRC'");
		assertNotLogged(result, "ran");
	}

	@Test
	void aHoldingWhereCondition_letsItRun() throws Exception {
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0' where { self.objects()->size() = 1; self.objects()->notEmpty() };
				transformation test(in s : SRC) {
				    main() { log('ran'); }
				}
				""", new BasicQvtoModelExtent(createSourceElement("a", 1)));
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "ran");
	}

	@Test
	void aStrictModelType_rejectsAForeignObject() throws Exception {
		// §8.1.2: strict — the model holds only instances of the model type's packages
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC "strict" uses 'http://test/source/1.0';
				transformation test(in s : SRC) {
				    main() { log('ran'); }
				}
				""", new BasicQvtoModelExtent(createSourceElement("a", 1), targetElement("foreign")));
		assertError(result, "Model 's' does not conform to the strict model type 'SRC': TargetElement is not in its metamodel");
		assertNotLogged(result, "ran");
	}

	@Test
	void aStrictModelType_acceptsItsOwnObjects() throws Exception {
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC "strict" uses 'http://test/source/1.0';
				transformation test(in s : SRC) {
				    main() { log('ran'); }
				}
				""", new BasicQvtoModelExtent(createSourceElement("a", 1)));
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "ran");
	}

	@Test
	void theConformanceKind_defaultsToEffective() throws Exception {
		// §8.2.1.6: "The default value is effective"
		OperationalTransformation transformation = parse("""
				modeltype SRC uses source("http://test/source/1.0");
				transformation test(in s : SRC) { main() {} }
				""");
		ModelType modelType = (ModelType) transformation.getModelParameter().get(0).getEType();
		assertEquals("effective", modelType.getConformanceKind());
		// §8.4 <packageref> ::= <scoped_identifier> ( '(' <uri> ')' )? with a double-quoted uri,
		// as in the spec's own example
		assertEquals("http://test/source/1.0", modelType.getMetamodel().get(0).getNsURI());
	}

	@Test
	void anIntermediateClassInstance_staysOutOfTheOutputModel() throws Exception {
		// §8.1.10: intermediate data does "not exist outside the scope of the transformation"
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    intermediate class Tmp { label : String; }
				    main() {
				        var tmp := object Tmp { label := 'tmp'; };
				        object TargetElement { name := tmp.label; };
				        log('tmps:' + Tmp.allInstances()->size().repr());
				    }
				}
				""", out);
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "tmps:1");
		assertEquals(List.of("TargetElement"), out.getContents().stream().map(e -> e.eClass().getName()).toList());
	}

	@Test
	void twoOutModelsOfTheSameType_makeTheInferredExtentAWarning() throws Exception {
		// §8.1.3.4: "a programming hazard that can be diagnosed by tooling"
		QvtoModelExtent a = emptyExtent();
		QvtoModelExtent b = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out a : TGT, out b : TGT) {
				    main() {
				        object TargetElement { name := 'one'; };
				        object TargetElement { name := 'two'; };
				        object TargetElement@b { name := 'chosen'; };
				    }
				}
				""", a, b);
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertEquals(1, result.diagnostics().stream()
				.filter(d -> d.getSeverity() == Diagnostic.WARNING && d.getMessage().contains("extent of TargetElement is ambiguous"))
				.count(), () -> result.diagnostics().toString());
		assertEquals(2, a.getContents().size());
		assertEquals(1, b.getContents().size());
	}

	@Test
	void aModelAssignment_displacesThePreviousMembers() throws Exception {
		// §8.1.3.3: "A model assignment displaces any previous members associated with the extent
		// in favor of the root model elements of the replacement Model"
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = executeWithExtents("""
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(out t : TGT) {
				    main() {
				        object TargetElement { name := 'old'; };
				        var m := t.copy();
				        m.removeElement(m.rootObjects()->first());
				        m.addElement(object TargetElement@m { name := 'new'; });
				        t := m;
				        log('roots:' + t.rootObjects()->size().repr());
				    }
				}
				""", out);
		assertTrue(result.isSuccess(), () -> result.diagnostics().toString());
		assertLogged(result, "roots:1");
		assertEquals(List.of("new"), out.getContents().stream()
				.map(e -> e.eGet(e.eClass().getEStructuralFeature("name"))).toList());
	}

	private static void assertError(QvtoExecutionResult result, String message) {
		assertNotNull(result);
		assertFalse(result.isSuccess(), () -> result.diagnostics().toString());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.ERROR && d.getMessage().equals(message)),
				() -> "Expected the error '" + message + "' but diagnostics were: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "Expected log containing '" + expected + "' but diagnostics were: " + result.diagnostics());
	}

	private static void assertNotLogged(QvtoExecutionResult result, String unexpected) {
		assertTrue(result.diagnostics().stream().noneMatch(d -> d.getMessage().equals("log: " + unexpected)),
				() -> "Did not expect log '" + unexpected + "' but diagnostics were: " + result.diagnostics());
	}
}
