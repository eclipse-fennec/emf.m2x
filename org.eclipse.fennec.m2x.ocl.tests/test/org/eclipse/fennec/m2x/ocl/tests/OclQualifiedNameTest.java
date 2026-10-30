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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Qualified names (OCL v2.4 §7.5.7, §7.3.5, §7.4.11, §9.3.35, §9.3.36, #325): nested package
 * paths, qualifiers of property and operation calls, restricted words after '::'.
 */
class OclQualifiedNameTest extends AbstractOclTest {

	static final String ROOT_URI = "http://test/qualified/root";

	static EPackage root;
	static EClass box;
	static EClass item;
	static EClass set;
	static EObject self;

	@BeforeAll
	static void setUp() {
		root = EcoreFactory.eINSTANCE.createEPackage();
		root.setName("root");
		root.setNsURI(ROOT_URI);
		root.setNsPrefix("root");
		box = EcoreFactory.eINSTANCE.createEClass();
		box.setName("Box");
		root.getEClassifiers().add(box);

		EPackage sub = EcoreFactory.eINSTANCE.createEPackage();
		sub.setName("sub");
		sub.setNsURI(ROOT_URI + "/sub");
		sub.setNsPrefix("sub");
		root.getESubpackages().add(sub);
		item = EcoreFactory.eINSTANCE.createEClass();
		item.setName("Item");
		sub.getEClassifiers().add(item);
		set = EcoreFactory.eINSTANCE.createEClass();
		set.setName("Set");
		sub.getEClassifiers().add(set);

		EPackage.Registry.INSTANCE.put(ROOT_URI, root);
		self = createPerson("Alice", 30, 50000.0, true);
	}

	@AfterAll
	static void unregister() {
		EPackage.Registry.INSTANCE.remove(ROOT_URI);
	}

	static Object evalOnBox(String expression) throws OclParseException {
		return engine.evaluate(engine.parse(expression, box), OclContext.of(root.getEFactoryInstance().create(box)));
	}

	/** §7.5.7: Packagename1::Packagename2::Typename. */
	@Nested
	class NestedPackagePaths {

		@Test
		void fullPath_toASubpackageType_resolves() throws OclParseException {
			assertEquals(false, evalOnBox("self.oclIsKindOf(root::sub::Item)"));
			assertEquals(true, evalOnBox("self.oclIsKindOf(root::Box)"));
		}

		@Test
		void restrictedWordAfterPath_namesAType() throws OclParseException {
			// §7.4.11: "A restricted word can only be used as a name when preceded by a '::'"
			assertEquals(false, evalOnBox("self.oclIsKindOf(root::sub::Set)"));
		}

		@Test
		void unknownSubpackage_isAnError() {
			assertThrows(OclParseException.class, () -> engine.parse("self.oclIsKindOf(root::nope::Item)", box));
		}

		@Test
		void document_contextAndPackage_withNestedPath() throws OclParseException {
			List<Constraint> qualified = engine.parseDocument("""
					context root::sub::Item
					inv named: true
					""");
			assertEquals(1, qualified.size());
			List<Constraint> inPackage = engine.parseDocument("""
					package root::sub
					context Item
					inv named: true
					endpackage
					""");
			assertEquals(1, inPackage.size());
		}
	}

	/** §9.3.36 [3]/[4], §9.3.35 [I]/[J]: the qualifier names a classifier the source conforms to. */
	@Nested
	class Qualifiers {

		@Test
		void conformingQualifier_isAccepted() throws OclParseException {
			assertEquals("Alice", eval("self.Person::name", self));
			assertEquals(5, eval("self.Person::name.size()", self));
		}

		@Test
		void unknownQualifier_isAParseError() {
			OclParseException failure = assertThrows(OclParseException.class,
					() -> engine.parse("self.Nonsense::name", personClass));
			assertTrue(failure.getMessage().contains("Unknown type (Nonsense)"), failure::getMessage);
		}

		@Test
		void nonConformingQualifier_isAParseError() {
			OclParseException failure = assertThrows(OclParseException.class,
					() -> engine.parse("self.Company::name", personClass));
			assertTrue(failure.getMessage().contains("does not conform to Company"), failure::getMessage);
		}

		@Test
		void typeQualifiedCall_onTheContextType_isAQualifiedCallOnSelf() throws OclParseException {
			assertEquals(false, eval("Person::oclIsUndefined()", self));
		}

		@Test
		void typeQualifiedCall_onAnUnknownOrUnrelatedType_isAParseError() {
			assertThrows(OclParseException.class, () -> engine.parse("Nonsense::oclIsUndefined()", personClass));
			OclParseException failure = assertThrows(OclParseException.class,
					() -> engine.parse("Company::oclIsUndefined()", personClass));
			assertTrue(failure.getMessage().contains("is no static operation"), failure::getMessage);
		}

		@Test
		void qualifiedCallResult_isEvaluated() throws OclParseException {
			OclResult result = engine.evaluateWithDiagnostics(engine.parse("self.Person::age + 1", personClass),
					OclContext.of(self), engine.getDefaultOptions());
			assertEquals(31L, result.value());
		}
	}
}
