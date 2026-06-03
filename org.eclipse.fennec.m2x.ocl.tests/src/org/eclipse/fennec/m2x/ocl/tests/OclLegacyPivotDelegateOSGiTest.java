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
 * OSGi integration tests for legacy-namespace interop: the Fennec OCL delegate
 * factories must also be registered under the legacy Eclipse OCL Pivot delegate
 * URI ({@code http://www.eclipse.org/emf/2002/Ecore/OCL/Pivot}) via the emf.osgi
 * whiteboard, so that models authored against Eclipse OCL evaluate correctly in
 * an OSGi runtime.
 *
 * <p>Mirrors {@link OclDelegateOSGiTest} but filters the delegate factory
 * services and annotates the model under the legacy URI. See
 * <a href="https://github.com/eclipse-fennec/emf.m2x/issues/7">issue #7</a>.
 */
@RequireOCL
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class OclLegacyPivotDelegateOSGiTest {

	private static final String LEGACY_URI = "http://www.eclipse.org/emf/2002/Ecore/OCL/Pivot";
	private static final String CONFIGURATOR_FILTER = "(emf.configuratorName=" + LEGACY_URI + ")";
	private static final String NS_URI = "http://test/oclLegacyPivotDelegateOSGiTest";

	@Test
	void invocationDelegate_underLegacyUri(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EOperation.Internal.InvocationDelegate.Factory> invAware) throws Exception {

		assertNotNull(invAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		assertEquals(true, ((InternalEObject) m.createEmployee("Alice", 30)).eInvoke(m.isAdultOp, null));
		assertEquals(false, ((InternalEObject) m.createEmployee("Kid", 10)).eInvoke(m.isAdultOp, null));
	}

	@Test
	void settingDelegate_underLegacyUri(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EStructuralFeature.Internal.SettingDelegate.Factory> settAware) {

		assertNotNull(settAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		EObject emp = m.createEmployee("Alice", 30);
		Object nameLength = emp.eGet(m.employeeClass.getEStructuralFeature("nameLength"));
		assertEquals(5, nameLength);
	}

	@Test
	void validationDelegate_underLegacyUri(
			@InjectService ServiceAware<ResourceSet> rsAware,
			@InjectService(filter = CONFIGURATOR_FILTER) ServiceAware<EValidator.ValidationDelegate> valAware) {

		assertNotNull(valAware.getService());
		TestModel m = createTestModel(rsAware.getService());

		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(LEGACY_URI);
		assertNotNull(delegate, "ValidationDelegate should be registered under the legacy Pivot URI");
		assertTrue(delegate.validate(m.employeeClass, m.createEmployee("Alice", 30), null, "agePositive", "self.age > 0"));
		assertFalse(delegate.validate(m.employeeClass, m.createEmployee("Bob", -5), null, "agePositive", "self.age > 0"));
	}

	// --- Test model helper ---

	private static TestModel createTestModel(ResourceSet rs) {
		EPackage testPackage = EcoreFactory.eINSTANCE.createEPackage();
		testPackage.setName("oclLegacyPivotDelegateOSGiTest");
		testPackage.setNsURI(NS_URI);
		testPackage.setNsPrefix("olpdot");

		EAnnotation ecoreAnn = EcoreFactory.eINSTANCE.createEAnnotation();
		ecoreAnn.setSource("http://www.eclipse.org/emf/2002/Ecore");
		ecoreAnn.getDetails().put("invocationDelegates", LEGACY_URI);
		ecoreAnn.getDetails().put("settingDelegates", LEGACY_URI);
		ecoreAnn.getDetails().put("validationDelegates", LEGACY_URI);
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
		addLegacyAnnotation(nameLengthAttr, "derivation", "self.name.size()");
		employeeClass.getEStructuralFeatures().add(nameLengthAttr);

		EOperation isAdultOp = EcoreFactory.eINSTANCE.createEOperation();
		isAdultOp.setName("isAdult");
		isAdultOp.setEType(EcorePackage.Literals.EBOOLEAN);
		addLegacyAnnotation(isAdultOp, "body", "self.age >= 18");
		employeeClass.getEOperations().add(isAdultOp);

		rs.getPackageRegistry().put(NS_URI, testPackage);

		return new TestModel(testPackage, employeeClass, isAdultOp);
	}

	private static void addLegacyAnnotation(org.eclipse.emf.ecore.EModelElement element,
			String key, String value) {
		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(LEGACY_URI);
		ann.getDetails().put(key, value);
		element.getEAnnotations().add(ann);
	}

	private record TestModel(EPackage testPackage, EClass employeeClass, EOperation isAdultOp) {

		EObject createEmployee(String name, int age) {
			EFactory factory = testPackage.getEFactoryInstance();
			EObject emp = factory.create(employeeClass);
			emp.eSet(employeeClass.getEStructuralFeature("name"), name);
			emp.eSet(employeeClass.getEStructuralFeature("age"), age);
			return emp;
		}
	}
}
