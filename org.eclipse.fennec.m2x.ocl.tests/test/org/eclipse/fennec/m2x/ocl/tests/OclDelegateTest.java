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
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EFactory;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclDelegateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for EMF delegate registration: invocation, setting, and validation delegates.
 *
 * <p>Creates a small in-memory Ecore model with OCL annotations, installs delegates
 * via {@code engine.installDelegates()}, and verifies that EMF delegate calls
 * are evaluated by the OCL engine.
 */
class OclDelegateTest extends AbstractOclTest {

	private static final String DELEGATE_URI = OclDelegateUtil.DELEGATE_URI;

	static EPackage testPackage;
	static EClass employeeClass;
	static EClass companyClass;
	static EOperation findEmployeesOp;
	static EAttribute employeesNames;

	@BeforeAll
	static void setUpDelegateModel() {
		// Create a small Ecore model with OCL-annotated features and operations
		testPackage = EcoreFactory.eINSTANCE.createEPackage();
		testPackage.setName("delegateTest");
		testPackage.setNsURI("http://test/delegateTest");
		testPackage.setNsPrefix("delegateTest");

		// EMF requires these EAnnotations on the EPackage to activate delegates
		EAnnotation ecoreAnn = EcoreFactory.eINSTANCE.createEAnnotation();
		ecoreAnn.setSource("http://www.eclipse.org/emf/2002/Ecore");
		ecoreAnn.getDetails().put("invocationDelegates", DELEGATE_URI);
		ecoreAnn.getDetails().put("settingDelegates", DELEGATE_URI);
		ecoreAnn.getDetails().put("validationDelegates", DELEGATE_URI);
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

		// salary : EDouble
		EAttribute salaryAttr = EcoreFactory.eINSTANCE.createEAttribute();
		salaryAttr.setName("salary");
		salaryAttr.setEType(EcorePackage.Literals.EDOUBLE);
		employeeClass.getEStructuralFeatures().add(salaryAttr);

		// derived feature: nameLength : EInt (derivation = self.name.size())
		EAttribute nameLengthAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameLengthAttr.setName("nameLength");
		nameLengthAttr.setEType(EcorePackage.Literals.EINT);
		nameLengthAttr.setDerived(true);
		nameLengthAttr.setTransient(true);
		nameLengthAttr.setVolatile(true);
		addAnnotation(nameLengthAttr, "derivation", "self.name.size()");
		employeeClass.getEStructuralFeatures().add(nameLengthAttr);

		// operation: isAdult() : EBoolean (body = self.age >= 18)
		EOperation isAdultOp = EcoreFactory.eINSTANCE.createEOperation();
		isAdultOp.setName("isAdult");
		isAdultOp.setEType(EcorePackage.Literals.EBOOLEAN);
		addAnnotation(isAdultOp, "body", "self.age >= 18");
		employeeClass.getEOperations().add(isAdultOp);

		// operation: greeting() : EString (body = 'Hello, '.concat(self.name))
		EOperation greetingOp = EcoreFactory.eINSTANCE.createEOperation();
		greetingOp.setName("greeting");
		greetingOp.setEType(EcorePackage.Literals.ESTRING);
		addAnnotation(greetingOp, "body", "'Hello, '.concat(self.name)");
		employeeClass.getEOperations().add(greetingOp);

		// Company with containment reference to Employee[*] — used to exercise
		// multi-valued EOperation bodies (see https://github.com/eclipse-fennec/emf.m2x/issues/3).
		companyClass = EcoreFactory.eINSTANCE.createEClass();
		companyClass.setName("Company");
		testPackage.getEClassifiers().add(companyClass);

		EAttribute companyNameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		companyNameAttr.setName("name");
		companyNameAttr.setEType(EcorePackage.Literals.ESTRING);
		companyClass.getEStructuralFeatures().add(companyNameAttr);

		EReference employeesRef = EcoreFactory.eINSTANCE.createEReference();
		employeesRef.setName("employees");
		employeesRef.setEType(employeeClass);
		employeesRef.setContainment(true);
		employeesRef.setUpperBound(-1);
		companyClass.getEStructuralFeatures().add(employeesRef);

		// operation: findEmployeesByNamePrefix(prefix: EString) : Employee[*]
		// body = self.employees->select(e | e.name.startsWith(prefix))->asSequence()
		findEmployeesOp = EcoreFactory.eINSTANCE.createEOperation();
		findEmployeesOp.setName("findEmployeesByNamePrefix");
		findEmployeesOp.setEType(employeeClass);
		findEmployeesOp.setUpperBound(-1);
		EParameter prefixParam = EcoreFactory.eINSTANCE.createEParameter();
		prefixParam.setName("prefix");
		prefixParam.setEType(EcorePackage.Literals.ESTRING);
		findEmployeesOp.getEParameters().add(prefixParam);
		addAnnotation(findEmployeesOp, "body",
				"self.employees->select(e | e.name.startsWith(prefix))->asSequence()");
		companyClass.getEOperations().add(findEmployeesOp);
		
		//derived many attribute
		employeesNames = EcoreFactory.eINSTANCE.createEAttribute();
		employeesNames.setName("employeesNames");
		employeesNames.setEType(EcorePackage.Literals.ESTRING);
		employeesNames.setUpperBound(-1);
		addAnnotation(employeesNames, "derivation",
				"self.employees->collect(e | e.name)->asSequence()");
		companyClass.getEStructuralFeatures().add(employeesNames);

		// Register the package
		EPackage.Registry.INSTANCE.put(testPackage.getNsURI(), testPackage);

		// Install delegates
		engine.installDelegates();
	}

	@AfterAll
	static void tearDownDelegates() {
		engine.uninstallDelegates();
		EPackage.Registry.INSTANCE.remove(testPackage.getNsURI());
	}

	private static void addAnnotation(org.eclipse.emf.ecore.EModelElement element,
			String key, String value) {
		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(DELEGATE_URI);
		ann.getDetails().put(key, value);
		element.getEAnnotations().add(ann);
	}

	private static EObject createEmployee(String name, int age, double salary) {
		EFactory factory = testPackage.getEFactoryInstance();
		EObject emp = factory.create(employeeClass);
		emp.eSet(employeeClass.getEStructuralFeature("name"), name);
		emp.eSet(employeeClass.getEStructuralFeature("age"), age);
		emp.eSet(employeeClass.getEStructuralFeature("salary"), salary);
		return emp;
	}

	@SuppressWarnings("unchecked")
	private static EObject createDelegateCompany(String name, EObject... employees) {
		EFactory factory = testPackage.getEFactoryInstance();
		EObject company = factory.create(companyClass);
		company.eSet(companyClass.getEStructuralFeature("name"), name);
		List<EObject> empList = (List<EObject>) company.eGet(companyClass.getEStructuralFeature("employees"));
		for (EObject emp : employees) {
			empList.add(emp);
		}
		return company;
	}

	// --- Delegate registration ---

	@Test
	void installDelegates_registersInvocationDelegate() {
		assertNotNull(EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE.get(DELEGATE_URI));
	}

	@Test
	void installDelegates_registersSettingDelegate() {
		assertNotNull(EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE.get(DELEGATE_URI));
	}

	@Test
	void installDelegates_registersValidationDelegate() {
		assertNotNull(EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI));
	}

	// --- Setting delegate (derived features) ---

	@Test
	void settingDelegate_nameLength() {
		EObject emp = createEmployee("Alice", 30, 50000.0);
		Object nameLength = emp.eGet(employeeClass.getEStructuralFeature("nameLength"));
		assertEquals(5, nameLength);
	}

	@Test
	void settingDelegate_nameLength_differentName() {
		EObject emp = createEmployee("Bob", 25, 40000.0);
		Object nameLength = emp.eGet(employeeClass.getEStructuralFeature("nameLength"));
		assertEquals(3, nameLength);
	}

	@Test
	void settingDelegate_derivedFeature_isSet() {
		EObject emp = createEmployee("Alice", 30, 50000.0);
		assertTrue(emp.eIsSet(employeeClass.getEStructuralFeature("nameLength")));
	}
	
	@Test
	void settingDelegate_multiValueDerivedFeature_returnsEList() {
		EObject jane = createEmployee("Jane", 30, 50000.0);
		EObject john = createEmployee("John", 25, 40000.0);
		EObject alice = createEmployee("Alice", 40, 60000.0);
		EObject company = createDelegateCompany("Acme", jane, john, alice);
		
		Object result = company.eGet(employeesNames);
		assertTrue(result instanceof EList,
				"Expected EList, got: " + (result == null ? "null" : result.getClass().getName()));
		EList<?> list = (EList<?>) result;
		assertEquals(3, list.size());
		assertTrue(list.contains("Jane"));
		assertTrue(list.contains("John"));
		assertTrue(list.contains("Alice"));
	}

	// --- Invocation delegate (operations) ---

	@Test
	void invocationDelegate_isAdult_true() throws Exception {
		EObject emp = createEmployee("Alice", 30, 50000.0);
		EOperation isAdultOp = employeeClass.getEOperations().get(0);
		Object result = ((InternalEObject) emp).eInvoke(isAdultOp, null);
		assertEquals(true, result);
	}

	@Test
	void invocationDelegate_isAdult_false() throws Exception {
		EObject emp = createEmployee("Child", 10, 0.0);
		EOperation isAdultOp = employeeClass.getEOperations().get(0);
		Object result = ((InternalEObject) emp).eInvoke(isAdultOp, null);
		assertEquals(false, result);
	}

	@Test
	void invocationDelegate_greeting() throws Exception {
		EObject emp = createEmployee("Bob", 25, 40000.0);
		EOperation greetingOp = employeeClass.getEOperations().get(1);
		Object result = ((InternalEObject) emp).eInvoke(greetingOp, null);
		assertEquals("Hello, Bob", result);
	}

	/**
	 * Regression test for <a href="https://github.com/eclipse-fennec/emf.m2x/issues/3">issue #3</a>:
	 * An EOperation with {@code upperBound != 1} whose OCL body ends with
	 * {@code ->asSequence()} / {@code ->asOrderedSet()} must return an
	 * {@link EList}, otherwise callers of generated EMF accessors hit a
	 * {@code ClassCastException} when the result is cast to {@code EList}.
	 */
	@Test
	void invocationDelegate_multiValuedOperation_returnsEList() throws Exception {
		EObject jane = createEmployee("Jane", 30, 50000.0);
		EObject john = createEmployee("John", 25, 40000.0);
		EObject alice = createEmployee("Alice", 40, 60000.0);
		EObject company = createDelegateCompany("Acme", jane, john, alice);

		EList<Object> args = new BasicEList<>();
		args.add("J");
		Object result = ((InternalEObject) company).eInvoke(findEmployeesOp, args);

		assertTrue(result instanceof EList,
				"Expected EList, got: " + (result == null ? "null" : result.getClass().getName()));
		EList<?> list = (EList<?>) result;
		assertEquals(2, list.size());
		assertTrue(list.contains(jane));
		assertTrue(list.contains(john));
	}

	/**
	 * Regression test for <a href="https://github.com/eclipse-fennec/emf.m2x/issues/3">issue #3</a>:
	 * Returned {@link EList} from the delegate must be unmodifiable — an
	 * {@code ->asSequence()} result is a fresh collection, not a live view,
	 * so mutating it from the caller must not silently succeed.
	 */
	@Test
	void invocationDelegate_multiValuedOperation_returnsUnmodifiableEList() throws Exception {
		EObject jane = createEmployee("Jane", 30, 50000.0);
		EObject company = createDelegateCompany("Acme", jane);

		EList<Object> args = new BasicEList<>();
		args.add("J");
		EList<?> result = (EList<?>) ((InternalEObject) company).eInvoke(findEmployeesOp, args);

		assertThrows(UnsupportedOperationException.class, () -> result.clear());
	}

	/**
	 * Regression test for <a href="https://github.com/eclipse-fennec/emf.m2x/issues/3">issue #3</a>:
	 * An OCL expression that invokes a multi-valued EOperation and chains
	 * further OCL operations on the result must keep working — i.e.
	 * {@code caseOperationCallExp} in the evaluator must accept whatever the
	 * delegate returns as an OCL collection.
	 */
	@Test
	void invocationDelegate_multiValuedOperation_usableInOclExpression() throws Exception {
		EObject jane = createEmployee("Jane", 30, 50000.0);
		EObject john = createEmployee("John", 25, 40000.0);
		EObject alice = createEmployee("Alice", 40, 60000.0);
		EObject company = createDelegateCompany("Acme", jane, john, alice);

		Object size = engine.evaluate(
				"self.findEmployeesByNamePrefix('J')->size()", OclContext.of(company));
		assertEquals(2, ((Number) size).intValue());

		Object names = engine.evaluate(
				"self.findEmployeesByNamePrefix('J')->collect(e | e.name)->asSequence()",
				OclContext.of(company));
		assertTrue(names instanceof java.util.Collection);
		java.util.Collection<?> nameColl = (java.util.Collection<?>) names;
		assertEquals(2, nameColl.size());
		assertTrue(nameColl.contains("Jane"));
		assertTrue(nameColl.contains("John"));
	}

	// --- Validation delegate ---

	@Test
	void validationDelegate_exists() {
		Object delegate = EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		assertNotNull(delegate);
	}

	@Test
	void validationDelegate_validConstraint() {
		EObject emp = createEmployee("Alice", 30, 50000.0);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		boolean valid = delegate.validate(employeeClass, emp, null,
				"agePositive", "self.age > 0");
		assertTrue(valid);
	}

	@Test
	void validationDelegate_violatedConstraint() {
		EObject emp = createEmployee("Alice", -5, 50000.0);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		boolean valid = delegate.validate(employeeClass, emp, null,
				"agePositive", "self.age > 0");
		assertFalse(valid);
	}

	// --- Uninstall delegates ---

	@Test
	void uninstallAndReinstall() {
		engine.uninstallDelegates();
		assertNull(EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE.get(DELEGATE_URI));
		assertNull(EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE.get(DELEGATE_URI));
		assertNull(EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI));

		// Reinstall for other tests
		engine.installDelegates();
		assertNotNull(EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE.get(DELEGATE_URI));
	}
}
