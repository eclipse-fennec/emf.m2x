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
package org.eclipse.fennec.m2m.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;

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
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.fennec.m2m.ocl.engine.internal.OclDelegateUtil;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for InvocationDelegate with actual EParameter arguments.
 * Verifies that operation parameters are properly bound as OCL variables
 * in the body expression during eInvoke.
 */
class OclInvocationDelegateArgsTest extends AbstractOclTest {

	private static final String DELEGATE_URI = OclDelegateUtil.DELEGATE_URI;

	static EPackage testPkg;
	static EClass calcClass;
	static EOperation addOp;
	static EOperation greetOp;
	static EOperation isOlderThanOp;
	static EOperation multiplyOp;

	@BeforeAll
	static void setUpModel() {
		testPkg = EcoreFactory.eINSTANCE.createEPackage();
		testPkg.setName("invArgTest");
		testPkg.setNsURI("http://test/invArgTest");
		testPkg.setNsPrefix("invArgTest");

		EAnnotation ecoreAnn = EcoreFactory.eINSTANCE.createEAnnotation();
		ecoreAnn.setSource("http://www.eclipse.org/emf/2002/Ecore");
		ecoreAnn.getDetails().put("invocationDelegates", DELEGATE_URI);
		testPkg.getEAnnotations().add(ecoreAnn);

		calcClass = EcoreFactory.eINSTANCE.createEClass();
		calcClass.setName("Calculator");
		testPkg.getEClassifiers().add(calcClass);

		// value : EInt (attribute on the class)
		EAttribute valueAttr = EcoreFactory.eINSTANCE.createEAttribute();
		valueAttr.setName("value");
		valueAttr.setEType(EcorePackage.Literals.EINT);
		calcClass.getEStructuralFeatures().add(valueAttr);

		// name : EString
		EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		calcClass.getEStructuralFeatures().add(nameAttr);

		// add(a : EInt, b : EInt) : EInt — body: a + b
		addOp = createOperation("add", EcorePackage.Literals.EINT, "a + b");
		addParam(addOp, "a", EcorePackage.Literals.EINT);
		addParam(addOp, "b", EcorePackage.Literals.EINT);
		calcClass.getEOperations().add(addOp);

		// greet(prefix : EString) : EString — body: prefix.concat(self.name)
		greetOp = createOperation("greet", EcorePackage.Literals.ESTRING,
				"prefix.concat(self.name)");
		addParam(greetOp, "prefix", EcorePackage.Literals.ESTRING);
		calcClass.getEOperations().add(greetOp);

		// isOlderThan(threshold : EInt) : EBoolean — body: self.value > threshold
		isOlderThanOp = createOperation("isOlderThan", EcorePackage.Literals.EBOOLEAN,
				"self.value > threshold");
		addParam(isOlderThanOp, "threshold", EcorePackage.Literals.EINT);
		calcClass.getEOperations().add(isOlderThanOp);

		// multiply(factor : EInt) : EInt — body: self.value * factor
		multiplyOp = createOperation("multiply", EcorePackage.Literals.EINT,
				"self.value * factor");
		addParam(multiplyOp, "factor", EcorePackage.Literals.EINT);
		calcClass.getEOperations().add(multiplyOp);

		EPackage.Registry.INSTANCE.put(testPkg.getNsURI(), testPkg);
		engine.installDelegates();
	}

	@AfterAll
	static void tearDownDelegates() {
		engine.uninstallDelegates();
		EPackage.Registry.INSTANCE.remove(testPkg.getNsURI());
	}

	private static EOperation createOperation(String name,
			org.eclipse.emf.ecore.EClassifier returnType, String body) {
		EOperation op = EcoreFactory.eINSTANCE.createEOperation();
		op.setName(name);
		op.setEType(returnType);
		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(DELEGATE_URI);
		ann.getDetails().put("body", body);
		op.getEAnnotations().add(ann);
		return op;
	}

	private static void addParam(EOperation op, String name,
			org.eclipse.emf.ecore.EClassifier type) {
		EParameter param = EcoreFactory.eINSTANCE.createEParameter();
		param.setName(name);
		param.setEType(type);
		op.getEParameters().add(param);
	}

	private static EObject createCalc(int value, String name) {
		EFactory factory = testPkg.getEFactoryInstance();
		EObject calc = factory.create(calcClass);
		calc.eSet(calcClass.getEStructuralFeature("value"), value);
		calc.eSet(calcClass.getEStructuralFeature("name"), name);
		return calc;
	}

	@SuppressWarnings("unchecked")
	private static EList<Object> args(Object... values) {
		@SuppressWarnings("rawtypes")
		EList list = new BasicEList<>();
		for (Object v : values) {
			list.add(v);
		}
		return list;
	}

	// --- Two-parameter operation: a + b ---

	@Test
	void add_twoIntegers() throws Exception {
		EObject calc = createCalc(0, "Calc");
		Object result = ((InternalEObject) calc).eInvoke(addOp, args(3, 7));
		assertEquals(10, result);
	}

	@Test
	void add_negativeValues() throws Exception {
		EObject calc = createCalc(0, "Calc");
		Object result = ((InternalEObject) calc).eInvoke(addOp, args(-5, 15));
		assertEquals(10, result);
	}

	@Test
	void add_zeros() throws Exception {
		EObject calc = createCalc(0, "Calc");
		Object result = ((InternalEObject) calc).eInvoke(addOp, args(0, 0));
		assertEquals(0, result);
	}

	// --- One-parameter with self: prefix.concat(self.name) ---

	@Test
	void greet_withPrefix() throws Exception {
		EObject calc = createCalc(0, "World");
		Object result = ((InternalEObject) calc).eInvoke(greetOp, args("Hello, "));
		assertEquals("Hello, World", result);
	}

	@Test
	void greet_emptyPrefix() throws Exception {
		EObject calc = createCalc(0, "Alice");
		Object result = ((InternalEObject) calc).eInvoke(greetOp, args(""));
		assertEquals("Alice", result);
	}

	// --- Parameter compared with self property: self.value > threshold ---

	@Test
	void isOlderThan_true() throws Exception {
		EObject calc = createCalc(30, "Calc");
		Object result = ((InternalEObject) calc).eInvoke(isOlderThanOp, args(18));
		assertEquals(true, result);
	}

	@Test
	void isOlderThan_false() throws Exception {
		EObject calc = createCalc(10, "Calc");
		Object result = ((InternalEObject) calc).eInvoke(isOlderThanOp, args(18));
		assertEquals(false, result);
	}

	@Test
	void isOlderThan_equal() throws Exception {
		EObject calc = createCalc(18, "Calc");
		Object result = ((InternalEObject) calc).eInvoke(isOlderThanOp, args(18));
		assertEquals(false, result);
	}

	// --- Parameter in arithmetic with self: self.value * factor ---

	@Test
	void multiply_byFactor() throws Exception {
		EObject calc = createCalc(7, "Calc");
		Object result = ((InternalEObject) calc).eInvoke(multiplyOp, args(6));
		assertEquals(42, result);
	}

	@Test
	void multiply_byZero() throws Exception {
		EObject calc = createCalc(100, "Calc");
		Object result = ((InternalEObject) calc).eInvoke(multiplyOp, args(0));
		assertEquals(0, result);
	}

	@Test
	void multiply_byOne() throws Exception {
		EObject calc = createCalc(42, "Calc");
		Object result = ((InternalEObject) calc).eInvoke(multiplyOp, args(1));
		assertEquals(42, result);
	}
}
