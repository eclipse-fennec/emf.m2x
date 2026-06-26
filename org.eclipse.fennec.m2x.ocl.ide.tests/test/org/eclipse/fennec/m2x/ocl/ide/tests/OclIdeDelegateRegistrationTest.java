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
package org.eclipse.fennec.m2x.ocl.ide.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.fennec.m2x.ocl.ide.OclInvocationDelegateFactory;
import org.eclipse.fennec.m2x.ocl.ide.OclSettingDelegateFactory;
import org.eclipse.fennec.m2x.ocl.ide.OclValidationDelegate;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Verifies the OCL IDE integration: registering the {@code ocl.ide} delegates in
 * EMF's delegate registries (exactly what the Eclipse extension registry does at
 * runtime from {@code plugin.xml}) makes the generic-editor evaluation paths
 * &mdash; {@code eGet} of a derived feature, {@code eInvoke} of an operation, and
 * constraint validation &mdash; evaluate the annotated OCL with the Fennec engine.
 *
 * <p>The delegates are registered under both the Fennec URI and the legacy
 * Eclipse OCL Pivot URI, mirroring {@code plugin.xml}.
 */
class OclIdeDelegateRegistrationTest {

	private static final String FENNEC_URI = "http://www.eclipse.org/fennec/m2x/ocl/1.0";
	private static final String PIVOT_URI = "http://www.eclipse.org/emf/2002/Ecore/OCL/Pivot";

	@BeforeEach
	void registerDelegates() {
		for (String uri : new String[] { FENNEC_URI, PIVOT_URI }) {
			EValidator.ValidationDelegate.Registry.INSTANCE.put(uri, new OclValidationDelegate());
			EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE
					.put(uri, new OclSettingDelegateFactory());
			EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE
					.put(uri, new OclInvocationDelegateFactory());
		}
	}

	@AfterEach
	void unregisterDelegates() {
		for (String uri : new String[] { FENNEC_URI, PIVOT_URI }) {
			EValidator.ValidationDelegate.Registry.INSTANCE.remove(uri);
			EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE.remove(uri);
			EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE.remove(uri);
		}
	}

	// --- Setting delegate: derived feature read via eGet ---

	@Test
	void derivedFeature_isEvaluatedOnEGet() {
		TestModel m = createTestModel(FENNEC_URI);
		EObject alice = m.createEmployee("Alice", 30);
		assertEquals(5, alice.eGet(m.nameLength), "self.name.size() for 'Alice'");

		EObject bob = m.createEmployee("Bob", 25);
		assertEquals(3, bob.eGet(m.nameLength), "self.name.size() for 'Bob'");
	}

	// --- Invocation delegate: operation body invoked via eInvoke ---

	@Test
	void operationBody_isEvaluatedOnEInvoke() throws Exception {
		TestModel m = createTestModel(FENNEC_URI);

		EObject adult = m.createEmployee("Alice", 30);
		assertEquals(Boolean.TRUE, ((InternalEObject) adult).eInvoke(m.isAdultOp, null));

		EObject child = m.createEmployee("Kid", 10);
		assertEquals(Boolean.FALSE, ((InternalEObject) child).eInvoke(m.isAdultOp, null));

		EObject bob = m.createEmployee("Bob", 25);
		assertEquals("Hello, Bob", ((InternalEObject) bob).eInvoke(m.greetingOp, null));
	}

	// --- Validation delegate: invariant constraint ---

	@Test
	void invariant_isEvaluatedByValidationDelegate() {
		TestModel m = createTestModel(FENNEC_URI);
		EValidator.ValidationDelegate delegate =
				EValidator.ValidationDelegate.Registry.INSTANCE.getValidationDelegate(FENNEC_URI);
		assertNotNull(delegate, "validation delegate must be registered");

		assertTrue(delegate.validate(m.employeeClass, m.createEmployee("Alice", 30),
				null, "agePositive", "self.age > 0"));
		assertFalse(delegate.validate(m.employeeClass, m.createEmployee("Alice", -5),
				null, "agePositive", "self.age > 0"));
	}

	// --- Legacy Eclipse OCL Pivot URI is served by the same delegates ---

	@Test
	void legacyPivotUri_derivedFeature_isEvaluated() {
		TestModel m = createTestModel(PIVOT_URI);
		EObject alice = m.createEmployee("Alice", 30);
		assertEquals(5, alice.eGet(m.nameLength),
				"derivation annotated under the legacy Pivot URI must still evaluate");
	}

	// --- Test model helper ---

	/**
	 * Builds a dynamic EPackage whose delegate annotations point at {@code uri},
	 * so EMF routes derived-feature/operation/constraint evaluation to the
	 * delegate registered under that URI.
	 */
	private static TestModel createTestModel(String uri) {
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("ideTest");
		pkg.setNsURI("http://test/oclIdeTest/" + uri.hashCode());
		pkg.setNsPrefix("ideTest");

		EAnnotation ecoreAnn = EcoreFactory.eINSTANCE.createEAnnotation();
		ecoreAnn.setSource("http://www.eclipse.org/emf/2002/Ecore");
		ecoreAnn.getDetails().put("settingDelegates", uri);
		ecoreAnn.getDetails().put("invocationDelegates", uri);
		ecoreAnn.getDetails().put("validationDelegates", uri);
		pkg.getEAnnotations().add(ecoreAnn);

		EClass employeeClass = EcoreFactory.eINSTANCE.createEClass();
		employeeClass.setName("Employee");
		pkg.getEClassifiers().add(employeeClass);

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
		addAnnotation(nameLengthAttr, uri, "derivation", "self.name.size()");
		employeeClass.getEStructuralFeatures().add(nameLengthAttr);

		EOperation isAdultOp = EcoreFactory.eINSTANCE.createEOperation();
		isAdultOp.setName("isAdult");
		isAdultOp.setEType(EcorePackage.Literals.EBOOLEAN);
		addAnnotation(isAdultOp, uri, "body", "self.age >= 18");
		employeeClass.getEOperations().add(isAdultOp);

		EOperation greetingOp = EcoreFactory.eINSTANCE.createEOperation();
		greetingOp.setName("greeting");
		greetingOp.setEType(EcorePackage.Literals.ESTRING);
		addAnnotation(greetingOp, uri, "body", "'Hello, '.concat(self.name)");
		employeeClass.getEOperations().add(greetingOp);

		ResourceSet rs = new ResourceSetImpl();
		rs.getPackageRegistry().put(pkg.getNsURI(), pkg);

		return new TestModel(pkg, employeeClass,
				employeeClass.getEStructuralFeature("nameLength"), isAdultOp, greetingOp);
	}

	private static void addAnnotation(EModelElement element, String uri, String key, String value) {
		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(uri);
		ann.getDetails().put(key, value);
		element.getEAnnotations().add(ann);
	}

	private record TestModel(EPackage pkg, EClass employeeClass, EStructuralFeature nameLength,
			EOperation isAdultOp, EOperation greetingOp) {

		EObject createEmployee(String name, int age) {
			EFactory factory = pkg.getEFactoryInstance();
			EObject emp = factory.create(employeeClass);
			emp.eSet(employeeClass.getEStructuralFeature("name"), name);
			emp.eSet(employeeClass.getEStructuralFeature("age"), age);
			return emp;
		}
	}
}
