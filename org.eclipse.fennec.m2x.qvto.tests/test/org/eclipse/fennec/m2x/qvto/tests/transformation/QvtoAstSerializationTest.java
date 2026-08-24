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

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * The parsed AST is a persistable model (#127): one self-contained containment
 * tree whose only external references point at objects living in a Resource
 * (the metamodel packages), so it can be saved as XMI, reloaded, and executed.
 */
class QvtoAstSerializationTest extends AbstractQvtoEngineTest {

	private static final String TRANSFORMATION = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			transformation test(in s : SRC, out t : TGT) {
			    mapping SourceElement::toTarget() : r : TargetElement
			        when { self.value >= 0 } {
			        var prefix : String := 'x_';
			        r.name := prefix + self.name;
			        r.value := self.value;
			    }
			    main() {
			        s.objectsOfType(SourceElement)->select(e | e.value < 100)
			            ->collect(e | e.map toTarget());
			    }
			}
			""";

	// ---- Self-containment ----

	@Test
	void parsedAstIsSelfContained() throws Exception {
		OperationalTransformation ast = parse(TRANSFORMATION);
		List<EObject> dangling = danglingSatellites(ast);
		assertTrue(dangling.isEmpty(), () -> "AST cross-references " + dangling.size()
				+ " objects contained in no resource and no containment tree: " + dangling);
	}

	@Test
	void modelTypesAreContainedInTheModule() throws Exception {
		OperationalTransformation ast = parse(TRANSFORMATION);
		assertEquals(2, ast.getUsedModelType().size());
		ast.getUsedModelType().forEach(mt ->
				assertEquals(ast, mt.eContainer(), () -> mt.getName() + " is not owned by the module"));
	}

	// ---- XMI roundtrip ----

	@Test
	void astSavesToXmiWithoutDanglingHrefs() throws Exception {
		OperationalTransformation ast = parse(TRANSFORMATION);
		byte[] xmi = saveToXmi(ast);
		assertTrue(xmi.length > 0);
	}

	@Test
	void reloadedAstExecutesLikeTheOriginal() throws Exception {
		byte[] xmi = saveToXmi(parse(TRANSFORMATION));

		OperationalTransformation reloaded = loadFromXmi(xmi);
		assertNotNull(reloaded.getEntry(), "entry operation lost in roundtrip");

		QvtoModelExtent inExtent = new BasicQvtoModelExtent(
				createSourceElement("alpha", 10), createSourceElement("beta", 20));
		QvtoModelExtent outExtent = emptyExtent();
		QvtoExecutionResult result = engine.execute(reloaded, QvtoExecutionContext.of(inExtent, outExtent));

		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		assertEquals(2, outExtent.getContents().size());
		EObject first = outExtent.getContents().get(0);
		assertEquals("x_alpha", first.eGet(first.eClass().getEStructuralFeature("name")));
		assertEquals(10, first.eGet(first.eClass().getEStructuralFeature("value")));
	}

	// ---- Helpers ----

	private static byte[] saveToXmi(OperationalTransformation ast) throws Exception {
		Resource resource = newResourceSet().createResource(URI.createURI("test.xmi"));
		resource.getContents().add(ast);
		ByteArrayOutputStream out = new ByteArrayOutputStream();
		resource.save(out, null); // DanglingHREFException here means the AST is not self-contained
		return out.toByteArray();
	}

	private static OperationalTransformation loadFromXmi(byte[] xmi) throws Exception {
		Resource resource = newResourceSet().createResource(URI.createURI("test.xmi"));
		resource.load(new ByteArrayInputStream(xmi), null);
		return (OperationalTransformation) resource.getContents().get(0);
	}

	private static ResourceSet newResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("xmi", new XMIResourceFactoryImpl());
		return resourceSet;
	}

	/**
	 * Mirrors the cross-reference walk from #127: every object reachable through a
	 * persisted (non-transient, non-derived) cross-reference must live in a Resource
	 * or inside the AST's own containment tree.
	 */
	private static List<EObject> danglingSatellites(OperationalTransformation ast) {
		Set<EObject> tree = new HashSet<>();
		tree.add(ast);
		ast.eAllContents().forEachRemaining(tree::add);

		List<EObject> dangling = new ArrayList<>();
		Set<EObject> reported = new HashSet<>();
		for (EObject owner : tree) {
			for (EReference ref : owner.eClass().getEAllReferences()) {
				if (ref.isContainment() || ref.isContainer() || ref.isTransient() || ref.isDerived()) {
					continue;
				}
				Object value = owner.eGet(ref);
				List<?> targets;
				if (value instanceof List<?> list) {
					targets = list;
				} else if (value instanceof EObject eo) {
					targets = List.of(eo);
				} else {
					targets = List.of();
				}
				for (Object t : targets) {
					if (!(t instanceof EObject target)) {
						continue;
					}
					EObject root = EcoreUtil.getRootContainer(target);
					if (root.eResource() == null && !tree.contains(root) && reported.add(root)) {
						dangling.add(root);
					}
				}
			}
		}
		return dangling;
	}
}
