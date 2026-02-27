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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O Collection-of-Models parameters (§8.1.1, §8.2.1.5).
 *
 * <p>QVT v1.3 §8.1.1 allows collection types for model parameters:
 * {@code transformation ManyToOne(in many: Sequence(SRC), out one: TGT);}
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.1.1 and §8.2.1.5.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eCollectionOfModelsTest extends AbstractQvtoEngineTest {

	// ==== Parse Tests ====

	@Test
	void sequenceModelParam_parsesCollectionKind() throws Exception {
		// §8.1.1: 'in many : Sequence(SRC)' → collectionKind=SEQUENCE, EType=ModelType(SRC)
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in many : Sequence(SRC), out t : TGT) {
				    main() {}
				}
				""");
		assertEquals(2, t.getModelParameter().size());
		ModelParameter mp = t.getModelParameter().get(0);
		assertEquals("many", mp.getName());
		assertTrue(mp.isSetCollectionKind());
		assertEquals(CollectionKind.SEQUENCE, mp.getCollectionKind());
		assertNotNull(mp.getEType(), "EType should be linked to ModelType SRC");
		assertEquals("SRC", mp.getEType().getName());

		// Second param should be normal (no collection)
		ModelParameter mp2 = t.getModelParameter().get(1);
		assertEquals("t", mp2.getName());
		assertFalse(mp2.isSetCollectionKind());
	}

	@Test
	void setModelParam_parsesCollectionKind() throws Exception {
		// §8.1.1: 'in many : Set(SRC)' → collectionKind=SET
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in many : Set(SRC), out t : TGT) {
				    main() {}
				}
				""");
		ModelParameter mp = t.getModelParameter().get(0);
		assertTrue(mp.isSetCollectionKind());
		assertEquals(CollectionKind.SET, mp.getCollectionKind());
	}

	@Test
	void mixedParams_normalAndCollection() throws Exception {
		// Mixed: first param is Sequence(SRC), second is normal TGT
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in many : Sequence(SRC), out t : TGT) {
				    main() {}
				}
				""");
		assertEquals(2, t.getModelParameter().size());

		ModelParameter collParam = t.getModelParameter().get(0);
		assertTrue(collParam.isSetCollectionKind());
		assertEquals(CollectionKind.SEQUENCE, collParam.getCollectionKind());
		assertEquals("SRC", collParam.getEType().getName());

		ModelParameter normalParam = t.getModelParameter().get(1);
		assertFalse(normalParam.isSetCollectionKind());
		assertEquals("TGT", normalParam.getEType().getName());
	}

	@Test
	void unnamedCollectionParam_derivesName() throws Exception {
		// §8.4: Unnamed collection param 'in Sequence(SRC)' → name derived from inner type 'SRC'
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in Sequence(SRC), out TGT) {
				    main() {}
				}
				""");
		ModelParameter mp = t.getModelParameter().get(0);
		assertEquals("SRC", mp.getName());
		assertTrue(mp.isSetCollectionKind());
		assertEquals(CollectionKind.SEQUENCE, mp.getCollectionKind());
	}

	// ==== E2E Tests ====

	@Test
	void sequenceOfModels_objectsOfType_aggregates() throws Exception {
		// §8.1.1: 2 source extents bound as Sequence(SRC), objectsOfType aggregates across both
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSourceElement("b", 2);
		QvtoModelExtent extent1 = new BasicQvtoModelExtent(src1);
		QvtoModelExtent extent2 = new BasicQvtoModelExtent(src2);
		QvtoModelExtent outExtent = emptyExtent();

		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in many : Sequence(SRC), out t : TGT) {
				    main() {
				        log('count:' + many.objectsOfType(SourceElement)->size().repr());
				    }
				}
				""");
		QvtoExecutionContext ctx = QvtoExecutionContext.builder()
				.addModelExtents("many", extent1, extent2)
				.addModelExtent("t", outExtent)
				.build();
		QvtoExecutionResult result = engine.execute(t, ctx);
		assertSuccess(result);
		assertLogged(result, "count:2");
	}

	@Test
	void sequenceOfModels_objects_aggregates() throws Exception {
		// §8.1.1: many.objects() returns all objects from all extents
		EObject src1 = createSourceElement("x", 10);
		EObject src2 = createSourceElement("y", 20);
		EObject src3 = createSourceElement("z", 30);
		QvtoModelExtent extent1 = new BasicQvtoModelExtent(src1, src2);
		QvtoModelExtent extent2 = new BasicQvtoModelExtent(src3);
		QvtoModelExtent outExtent = emptyExtent();

		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in many : Sequence(SRC), out t : TGT) {
				    main() {
				        log('total:' + many.objects()->size().repr());
				    }
				}
				""");
		QvtoExecutionContext ctx = QvtoExecutionContext.builder()
				.addModelExtents("many", extent1, extent2)
				.addModelExtent("t", outExtent)
				.build();
		QvtoExecutionResult result = engine.execute(t, ctx);
		assertSuccess(result);
		assertLogged(result, "total:3");
	}

	@Test
	void sequenceOfModels_singleOutputExtent() throws Exception {
		// Collection input + normal output: mappings create objects in the single output extent
		EObject src1 = createSourceElement("a", 1);
		EObject src2 = createSourceElement("b", 2);
		QvtoModelExtent extent1 = new BasicQvtoModelExtent(src1);
		QvtoModelExtent extent2 = new BasicQvtoModelExtent(src2);
		QvtoModelExtent outExtent = emptyExtent();

		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in many : Sequence(SRC), out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.name;
				        value := self.value;
				    }
				    main() {
				        many.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""");
		QvtoExecutionContext ctx = QvtoExecutionContext.builder()
				.addModelExtents("many", extent1, extent2)
				.addModelExtent("t", outExtent)
				.build();
		QvtoExecutionResult result = engine.execute(t, ctx);
		assertSuccess(result);
		assertEquals(2, outExtent.getContents().size(), "Both source elements should be mapped to output");
	}

	@Test
	void builderApi_namedBindings() throws Exception {
		// Builder API with named single-extent bindings (non-collection) — backward compat
		EObject src = createSourceElement("elem", 42);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();

		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : TargetElement {
				        name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->forEach(e) {
				            e.map toTarget();
				        };
				    }
				}
				""");
		QvtoExecutionContext ctx = QvtoExecutionContext.builder()
				.addModelExtent("s", inExtent)
				.addModelExtent("t", outExtent)
				.build();
		QvtoExecutionResult result = engine.execute(t, ctx);
		assertSuccess(result);
		assertEquals(1, outExtent.getContents().size());
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
}
