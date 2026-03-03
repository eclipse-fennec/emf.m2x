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
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * OSGi integration tests that verify EMF delegate factories (invocation,
 * setting, validation) are correctly wired via the emf.osgi whiteboard
 * pattern.
 *
 * <p>Each test method injects the corresponding delegate factory service
 * (filtered by {@code emf.configuratorName}) as a synchronization barrier,
 * ensuring the factory is registered in the EMF registry before the test
 * body executes. A {@link ResourceSet} is injected to provide the
 * package registry for the test EPackage.
 */
@RequireOCL
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class OclDelegateOSGiTest {

	private static final String DELEGATE_URI = "http://www.eclipse.org/fennec/m2x/ocl/1.0";
	private static final String CONFIGURATOR_FILTER = "(emf.configuratorName=" + DELEGATE_URI + ")";
	private static final String NS_URI = "http://test/oclDelegateOSGiTest";

	// --- Invocation Delegate ---

	@Test
	void invocationDelegate_isAdult_true(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EOperation.Internal.InvocationDelegate.Factory> invAware) throws Exception {

		assertNotNull(invAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		EObject emp = m.createEmployee("Alice", 30);
		Object result = ((InternalEObject) emp).eInvoke(m.isAdultOp, null);
		assertEquals(true, result);
	}

	@Test
	void invocationDelegate_isAdult_false(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EOperation.Internal.InvocationDelegate.Factory> invAware) throws Exception {

		assertNotNull(invAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		EObject emp = m.createEmployee("Child", 10);
		Object result = ((InternalEObject) emp).eInvoke(m.isAdultOp, null);
		assertEquals(false, result);
	}

	@Test
	void invocationDelegate_greeting(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EOperation.Internal.InvocationDelegate.Factory> invAware) throws Exception {

		assertNotNull(invAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		EObject emp = m.createEmployee("Bob", 25);
		Object result = ((InternalEObject) emp).eInvoke(m.greetingOp, null);
		assertEquals("Hello, Bob", result);
	}

	// --- Setting Delegate (derived features) ---

	@Test
	void settingDelegate_nameLength(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EStructuralFeature.Internal.SettingDelegate.Factory> settAware) {

		assertNotNull(settAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		EObject emp = m.createEmployee("Alice", 30);
		Object nameLength = emp.eGet(m.employeeClass.getEStructuralFeature("nameLength"));
		assertEquals(5, nameLength);
	}

	@Test
	void settingDelegate_nameLength_differentName(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EStructuralFeature.Internal.SettingDelegate.Factory> settAware) {

		assertNotNull(settAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		EObject emp = m.createEmployee("Bob", 25);
		Object nameLength = emp.eGet(m.employeeClass.getEStructuralFeature("nameLength"));
		assertEquals(3, nameLength);
	}

	// --- Validation Delegate ---

	@Test
	void validationDelegate_validConstraint(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EValidator.ValidationDelegate> valAware) {

		assertNotNull(valAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		EObject emp = m.createEmployee("Alice", 30);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		assertNotNull(delegate, "ValidationDelegate should be registered via emf.osgi whiteboard");
		assertTrue(delegate.validate(m.employeeClass, emp, null, "agePositive", "self.age > 0"));
	}

	@Test
	void validationDelegate_violatedConstraint(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EValidator.ValidationDelegate> valAware) {

		assertNotNull(valAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		EObject emp = m.createEmployee("Alice", -5);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		assertNotNull(delegate, "ValidationDelegate should be registered via emf.osgi whiteboard");
		assertFalse(delegate.validate(m.employeeClass, emp, null, "agePositive", "self.age > 0"));
	}

	// --- Test model helper ---

	private static TestModel createTestModel(ResourceSet rs) {
		EPackage testPackage = EcoreFactory.eINSTANCE.createEPackage();
		testPackage.setName("oclDelegateOSGiTest");
		testPackage.setNsURI(NS_URI);
		testPackage.setNsPrefix("odot");

		EAnnotation ecoreAnn = EcoreFactory.eINSTANCE.createEAnnotation();
		ecoreAnn.setSource("http://www.eclipse.org/emf/2002/Ecore");
		ecoreAnn.getDetails().put("invocationDelegates", DELEGATE_URI);
		ecoreAnn.getDetails().put("settingDelegates", DELEGATE_URI);
		ecoreAnn.getDetails().put("validationDelegates", DELEGATE_URI);
		testPackage.getEAnnotations().add(ecoreAnn);

		EClass employeeClass = EcoreFactory.eINSTANCE.createEClass();
		employeeClass.setName("Employee");
		testPackage.getEClassifiers().add(employeeClass);

		EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		employeeClass.getEStructuralFeatures().add(nameAttr);

		EAttribute ageAttr = EcoreFactory.eINSTANCE.createEAttribute();
		ageAttr.setName("age");
		ageAttr.setEType(EcorePackage.Literals.EINT);
		employeeClass.getEStructuralFeatures().add(ageAttr);

		EAttribute nameLengthAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameLengthAttr.setName("nameLength");
		nameLengthAttr.setEType(EcorePackage.Literals.EINT);
		nameLengthAttr.setDerived(true);
		nameLengthAttr.setTransient(true);
		nameLengthAttr.setVolatile(true);
		addAnnotation(nameLengthAttr, "derivation", "self.name.size()");
		employeeClass.getEStructuralFeatures().add(nameLengthAttr);

		EOperation isAdultOp = EcoreFactory.eINSTANCE.createEOperation();
		isAdultOp.setName("isAdult");
		isAdultOp.setEType(EcorePackage.Literals.EBOOLEAN);
		addAnnotation(isAdultOp, "body", "self.age >= 18");
		employeeClass.getEOperations().add(isAdultOp);

		EOperation greetingOp = EcoreFactory.eINSTANCE.createEOperation();
		greetingOp.setName("greeting");
		greetingOp.setEType(EcorePackage.Literals.ESTRING);
		addAnnotation(greetingOp, "body", "'Hello, '.concat(self.name)");
		employeeClass.getEOperations().add(greetingOp);

		rs.getPackageRegistry().put(NS_URI, testPackage);

		return new TestModel(testPackage, employeeClass, isAdultOp, greetingOp);
	}

	private static void addAnnotation(org.eclipse.emf.ecore.EModelElement element,
			String key, String value) {
		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(DELEGATE_URI);
		ann.getDetails().put(key, value);
		element.getEAnnotations().add(ann);
	}

	private record TestModel(EPackage testPackage, EClass employeeClass,
			EOperation isAdultOp, EOperation greetingOp) {

		EObject createEmployee(String name, int age) {
			EFactory factory = testPackage.getEFactoryInstance();
			EObject emp = factory.create(employeeClass);
			emp.eSet(employeeClass.getEStructuralFeature("name"), name);
			emp.eSet(employeeClass.getEStructuralFeature("age"), age);
			return emp;
		}
	}
}
