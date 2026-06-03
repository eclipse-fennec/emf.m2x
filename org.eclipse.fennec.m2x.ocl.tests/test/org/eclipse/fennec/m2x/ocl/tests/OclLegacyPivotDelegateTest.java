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
import org.eclipse.fennec.m2x.ocl.engine.internal.OclDelegateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests legacy-namespace interop: the Fennec OCL engine must also serve the
 * Eclipse OCL Pivot delegate URI ({@value OclDelegateUtil#LEGACY_PIVOT_URI}),
 * so that models authored against Eclipse OCL — whose derived features,
 * operation bodies, and constraints are annotated under that URI rather than
 * the native Fennec URI — evaluate correctly.
 *
 * <p>This mirrors {@link OclDelegateTest} but annotates everything under the
 * legacy Pivot URI and declares it on the package's
 * {@code settingDelegates}/{@code invocationDelegates}/{@code validationDelegates}
 * annotation, exactly as Eclipse OCL emits. See
 * <a href="https://github.com/eclipse-fennec/emf.m2x/issues/7">issue #7</a>.
 */
class OclLegacyPivotDelegateTest extends AbstractOclTest {

	private static final String LEGACY_URI = OclDelegateUtil.LEGACY_PIVOT_URI;

	static EPackage testPackage;
	static EClass employeeClass;

	@BeforeAll
	static void setUpLegacyDelegateModel() {
		testPackage = EcoreFactory.eINSTANCE.createEPackage();
		testPackage.setName("legacyPivotDelegateTest");
		testPackage.setNsURI("http://test/legacyPivotDelegateTest");
		testPackage.setNsPrefix("legacyPivotDelegateTest");

		// EMF activates delegates via the package-level Ecore annotation, whose
		// detail values list the delegate URIs. Eclipse OCL lists the Pivot URI.
		EAnnotation ecoreAnn = EcoreFactory.eINSTANCE.createEAnnotation();
		ecoreAnn.setSource("http://www.eclipse.org/emf/2002/Ecore");
		ecoreAnn.getDetails().put("invocationDelegates", LEGACY_URI);
		ecoreAnn.getDetails().put("settingDelegates", LEGACY_URI);
		ecoreAnn.getDetails().put("validationDelegates", LEGACY_URI);
		testPackage.getEAnnotations().add(ecoreAnn);

		employeeClass = EcoreFactory.eINSTANCE.createEClass();
		employeeClass.setName("Employee");
		testPackage.getEClassifiers().add(employeeClass);

		// name : EString
		EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		employeeClass.getEStructuralFeatures().add(nameAttr);

		// age : EInt
		EAttribute ageAttr = EcoreFactory.eINSTANCE.createEAttribute();
		ageAttr.setName("age");
		ageAttr.setEType(EcorePackage.Literals.EINT);
		employeeClass.getEStructuralFeatures().add(ageAttr);

		// derived feature: nameLength : EInt (derivation = self.name.size())
		// annotated under the LEGACY Pivot URI — this is the crux of issue #7
		EAttribute nameLengthAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameLengthAttr.setName("nameLength");
		nameLengthAttr.setEType(EcorePackage.Literals.EINT);
		nameLengthAttr.setDerived(true);
		nameLengthAttr.setTransient(true);
		nameLengthAttr.setVolatile(true);
		addLegacyAnnotation(nameLengthAttr, "derivation", "self.name.size()");
		employeeClass.getEStructuralFeatures().add(nameLengthAttr);

		// operation: isAdult() : EBoolean (body = self.age >= 18)
		EOperation isAdultOp = EcoreFactory.eINSTANCE.createEOperation();
		isAdultOp.setName("isAdult");
		isAdultOp.setEType(EcorePackage.Literals.EBOOLEAN);
		addLegacyAnnotation(isAdultOp, "body", "self.age >= 18");
		employeeClass.getEOperations().add(isAdultOp);

		EPackage.Registry.INSTANCE.put(testPackage.getNsURI(), testPackage);

		engine.installDelegates();
	}

	@AfterAll
	static void tearDownLegacyDelegates() {
		engine.uninstallDelegates();
		EPackage.Registry.INSTANCE.remove(testPackage.getNsURI());
	}

	private static void addLegacyAnnotation(org.eclipse.emf.ecore.EModelElement element,
			String key, String value) {
		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(LEGACY_URI);
		ann.getDetails().put(key, value);
		element.getEAnnotations().add(ann);
	}

	private static EObject createEmployee(String name, int age) {
		EFactory factory = testPackage.getEFactoryInstance();
		EObject emp = factory.create(employeeClass);
		emp.eSet(employeeClass.getEStructuralFeature("name"), name);
		emp.eSet(employeeClass.getEStructuralFeature("age"), age);
		return emp;
	}

	// --- Delegate registration under the legacy URI ---

	@Test
	void installDelegates_registersSettingDelegateUnderLegacyUri() {
		assertNotNull(EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE.get(LEGACY_URI));
	}

	@Test
	void installDelegates_registersInvocationDelegateUnderLegacyUri() {
		assertNotNull(EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE.get(LEGACY_URI));
	}

	@Test
	void installDelegates_registersValidationDelegateUnderLegacyUri() {
		assertNotNull(EValidator.ValidationDelegate.Registry.INSTANCE.get(LEGACY_URI));
	}

	// --- Setting delegate: derived feature annotated under the legacy URI ---

	@Test
	void settingDelegate_derivedFeatureUnderLegacyUri_evaluates() {
		EObject emp = createEmployee("Alice", 30);
		Object nameLength = emp.eGet(employeeClass.getEStructuralFeature("nameLength"));
		assertEquals(5, nameLength);
	}

	// --- Invocation delegate: operation body annotated under the legacy URI ---

	@Test
	void invocationDelegate_operationUnderLegacyUri_evaluates() throws Exception {
		EObject adult = createEmployee("Alice", 30);
		EObject child = createEmployee("Kid", 10);
		EOperation isAdultOp = employeeClass.getEOperations().get(0);
		assertEquals(true, ((InternalEObject) adult).eInvoke(isAdultOp, null));
		assertEquals(false, ((InternalEObject) child).eInvoke(isAdultOp, null));
	}

	// --- Validation delegate: constraint resolved via the legacy URI ---

	@Test
	void validationDelegate_underLegacyUri_evaluates() {
		EObject valid = createEmployee("Alice", 30);
		EObject invalid = createEmployee("Bob", -5);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(LEGACY_URI);
		assertNotNull(delegate);
		assertTrue(delegate.validate(employeeClass, valid, null, "agePositive", "self.age > 0"));
		assertFalse(delegate.validate(employeeClass, invalid, null, "agePositive", "self.age > 0"));
	}
}
