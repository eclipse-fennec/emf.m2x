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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.ConstraintKind;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for GAP-11: Cross-package type resolution in the parser.
 *
 * <p>Verifies that Complete OCL document parsing can resolve classifiers
 * from packages other than the context type's package, using the
 * global {@link EPackage.Registry}.
 */
class OclCrossPackageTypeTest {

	static OclEngine engine;
	static OclParserSupport parser;
	static EPackage orderPackage;
	static EPackage productPackage;
	static EClass orderClass;
	static EClass productClass;

	@BeforeAll
	static void setUp() {
		parser = new OclParserSupport();
		engine = OclEngines.create(parser);

		// Create "product" package with Product class
		productPackage = EcoreFactory.eINSTANCE.createEPackage();
		productPackage.setName("product");
		productPackage.setNsURI("http://test/product/1.0");
		productPackage.setNsPrefix("product");

		productClass = EcoreFactory.eINSTANCE.createEClass();
		productClass.setName("Product");
		EAttribute productName = EcoreFactory.eINSTANCE.createEAttribute();
		productName.setName("name");
		productName.setEType(EcorePackage.Literals.ESTRING);
		productClass.getEStructuralFeatures().add(productName);
		EAttribute price = EcoreFactory.eINSTANCE.createEAttribute();
		price.setName("price");
		price.setEType(EcorePackage.Literals.EDOUBLE);
		productClass.getEStructuralFeatures().add(price);
		productPackage.getEClassifiers().add(productClass);

		// Create "order" package with Order class referencing Product
		orderPackage = EcoreFactory.eINSTANCE.createEPackage();
		orderPackage.setName("order");
		orderPackage.setNsURI("http://test/order/1.0");
		orderPackage.setNsPrefix("order");

		orderClass = EcoreFactory.eINSTANCE.createEClass();
		orderClass.setName("Order");
		EAttribute orderId = EcoreFactory.eINSTANCE.createEAttribute();
		orderId.setName("id");
		orderId.setEType(EcorePackage.Literals.EINT);
		orderClass.getEStructuralFeatures().add(orderId);
		EAttribute quantity = EcoreFactory.eINSTANCE.createEAttribute();
		quantity.setName("quantity");
		quantity.setEType(EcorePackage.Literals.EINT);
		orderClass.getEStructuralFeatures().add(quantity);
		EReference itemRef = EcoreFactory.eINSTANCE.createEReference();
		itemRef.setName("item");
		itemRef.setEType(productClass);
		orderClass.getEStructuralFeatures().add(itemRef);
		orderPackage.getEClassifiers().add(orderClass);

		// Register both packages in the global registry
		EPackage.Registry.INSTANCE.put(productPackage.getNsURI(), productPackage);
		EPackage.Registry.INSTANCE.put(orderPackage.getNsURI(), orderPackage);
	}

	@AfterAll
	static void tearDown() {
		EPackage.Registry.INSTANCE.remove(productPackage.getNsURI());
		EPackage.Registry.INSTANCE.remove(orderPackage.getNsURI());
	}

	@Test
	void parseDocument_invariant_samePackage() throws OclParseException {
		// The document names the packages it uses (#158): a bare 'Order' resolved before only
		// because every registered package was searched
		String doc = """
				import order
				context Order
				inv positiveQuantity: self.quantity > 0
				""";
		List<Constraint> constraints = parser.parseDocument(doc);
		assertFalse(constraints.isEmpty(), "Should parse at least one constraint");
		assertEquals(ConstraintKind.INV, constraints.get(0).getKind());
		assertEquals("positiveQuantity", constraints.get(0).getName());
		assertEquals(orderClass, constraints.get(0).getContextClassifier());
	}

	@Test
	void parseDocument_invariant_crossPackageNavigation() throws OclParseException {
		// Order.item navigates to Product (cross-package reference)
		String doc = """
				import order
				context Order
				inv itemNotNull: not self.item.oclIsUndefined()
				""";
		List<Constraint> constraints = parser.parseDocument(doc);
		assertFalse(constraints.isEmpty());

		// Evaluate the constraint against an order with an item
		EObject product = productPackage.getEFactoryInstance().create(productClass);
		product.eSet(productClass.getEStructuralFeature("name"), "Widget");
		product.eSet(productClass.getEStructuralFeature("price"), 9.99);

		EObject order = orderPackage.getEFactoryInstance().create(orderClass);
		order.eSet(orderClass.getEStructuralFeature("id"), 1);
		order.eSet(orderClass.getEStructuralFeature("quantity"), 5);
		order.eSet(orderClass.getEStructuralFeature("item"), product);

		Object result = engine.evaluate(constraints.get(0).getSpecification(),
				OclContext.of(order));
		assertEquals(true, result);
	}

	@Test
	void parseDocument_resolvesClassifierFromOtherPackage() throws OclParseException {
		// oclIsKindOf(Product) where Product is in a different package from Order
		// Product is in another package than Order; the document imports both — cross-package
		// resolution is by declaration, not by scanning the registry (#158)
		String doc = """
				import order
				import product
				context Order
				inv hasProduct: self.item.oclIsKindOf(Product)
				""";
		List<Constraint> constraints = parser.parseDocument(doc);
		assertFalse(constraints.isEmpty());
		assertNotNull(constraints.get(0).getSpecification());

		// Evaluate
		EObject product = productPackage.getEFactoryInstance().create(productClass);
		product.eSet(productClass.getEStructuralFeature("name"), "Gadget");
		product.eSet(productClass.getEStructuralFeature("price"), 19.99);

		EObject order = orderPackage.getEFactoryInstance().create(orderClass);
		order.eSet(orderClass.getEStructuralFeature("id"), 2);
		order.eSet(orderClass.getEStructuralFeature("quantity"), 1);
		order.eSet(orderClass.getEStructuralFeature("item"), product);

		Object result = engine.evaluate(constraints.get(0).getSpecification(),
				OclContext.of(order));
		assertEquals(true, result);
	}

	@Test
	void parseDocument_multiplePackageContexts() throws OclParseException {
		// Constraints on both packages in one document
		String doc = """
				import product
				import order
				context Product
				inv positivePrice: self.price > 0.0

				context Order
				inv positiveQuantity: self.quantity > 0
				""";
		List<Constraint> constraints = parser.parseDocument(doc);
		assertEquals(2, constraints.size());
		assertEquals(productClass, constraints.get(0).getContextClassifier());
		assertEquals(orderClass, constraints.get(1).getContextClassifier());
	}

	@Test
	void parseDocument_withPackageDeclaration() throws OclParseException {
		String doc = """
				package order
				context Order
				inv positiveQuantity: self.quantity > 0
				endpackage
				""";
		List<Constraint> constraints = parser.parseDocument(doc);
		assertFalse(constraints.isEmpty());
		assertEquals(orderClass, constraints.get(0).getContextClassifier());
	}
}
