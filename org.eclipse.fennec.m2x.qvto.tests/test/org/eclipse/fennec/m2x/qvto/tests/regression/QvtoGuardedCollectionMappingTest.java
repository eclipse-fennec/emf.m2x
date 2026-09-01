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
package org.eclipse.fennec.m2x.qvto.tests.regression;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.junit.jupiter.api.Test;

/**
 * A guarded-out mapping contributes nothing to a mapped collection (#228): a collection mapping
 * call is the imperative collect (QVT v1.3 §8.2.1.21), whose semantics never lets null into the
 * result — {@code if (target <> null) res += target} (§8.2.2.7). Before the fix the null reached
 * a containment assignment, which EMF refuses.
 */
class QvtoGuardedCollectionMappingTest {

	@Test
	void aGuardedOutMapping_contributesNothingToTheMappedCollection() throws Exception {
		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI("http://example.org/m2x/guarded-map/1.0");
		shelf.setNsPrefix("shelf");
		EClass box = EcoreFactory.eINSTANCE.createEClass();
		box.setName("Box");
		EClass item = EcoreFactory.eINSTANCE.createEClass();
		item.setName("Item");
		EAttribute weight = EcoreFactory.eINSTANCE.createEAttribute();
		weight.setName("weight");
		weight.setEType(EcorePackage.Literals.EINT);
		item.getEStructuralFeatures().add(weight);
		EReference items = EcoreFactory.eINSTANCE.createEReference();
		items.setName("items");
		items.setEType(item);
		items.setUpperBound(-1);
		items.setContainment(true);
		box.getEStructuralFeatures().add(items);
		shelf.getEClassifiers().add(box);
		shelf.getEClassifiers().add(item);

		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(shelf)
				.build());

		// Three items, one guarded out — the containment assignment must see two, never a null
		EObject light = itemOf(shelf, 1);
		EObject heavy = itemOf(shelf, 100);
		EObject medium = itemOf(shelf, 10);
		QvtoExecutionResult result = engine.execute(engine.parse("""
				modeltype SHELF uses 'http://example.org/m2x/guarded-map/1.0';
				transformation Pack(inout m : SHELF) {
				    main() {
				        var b := object Box {};
				        b.items := m.objectsOfType(Item)->map keepLight();
				    }
				    mapping Item::keepLight() : Item
				        when { self.weight < 50 } {
				        weight := self.weight;
				    }
				}
				""", "Pack"), QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(light, heavy, medium))));

		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
	}

	private static EObject itemOf(EPackage shelf, int weight) {
		EClass item = (EClass) shelf.getEClassifier("Item");
		EObject instance = shelf.getEFactoryInstance().create(item);
		instance.eSet(item.getEStructuralFeature("weight"), weight);
		return instance;
	}
}
