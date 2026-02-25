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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
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
 * Tests for delegate factory edge cases: all three overloads of validate(),
 * missing annotations, EDataType validation, non-boolean constraint results,
 * parse errors, setting delegate "initial" fallback, and invocation error paths.
 */
class OclDelegateFactoryEdgeCaseTest extends AbstractOclTest {

	private static final String DELEGATE_URI = OclDelegateUtil.DELEGATE_URI;

	static EPackage testPkg;
	static EClass itemClass;
	static EDataType myDataType;

	@BeforeAll
	static void setUpModel() {
		testPkg = EcoreFactory.eINSTANCE.createEPackage();
		testPkg.setName("edgeCaseTest");
		testPkg.setNsURI("http://test/edgeCaseTest");
		testPkg.setNsPrefix("edgeCaseTest");

		// Activate delegates on the EPackage
		EAnnotation ecoreAnn = EcoreFactory.eINSTANCE.createEAnnotation();
		ecoreAnn.setSource("http://www.eclipse.org/emf/2002/Ecore");
		ecoreAnn.getDetails().put("invocationDelegates", DELEGATE_URI);
		ecoreAnn.getDetails().put("settingDelegates", DELEGATE_URI);
		ecoreAnn.getDetails().put("validationDelegates", DELEGATE_URI);
		testPkg.getEAnnotations().add(ecoreAnn);

		itemClass = EcoreFactory.eINSTANCE.createEClass();
		itemClass.setName("Item");
		testPkg.getEClassifiers().add(itemClass);

		EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		itemClass.getEStructuralFeatures().add(nameAttr);

		EAttribute priceAttr = EcoreFactory.eINSTANCE.createEAttribute();
		priceAttr.setName("price");
		priceAttr.setEType(EcorePackage.Literals.EDOUBLE);
		itemClass.getEStructuralFeatures().add(priceAttr);

		// Derived feature with "derivation" annotation
		EAttribute descLen = EcoreFactory.eINSTANCE.createEAttribute();
		descLen.setName("nameLength");
		descLen.setEType(EcorePackage.Literals.EINT);
		descLen.setDerived(true);
		descLen.setTransient(true);
		descLen.setVolatile(true);
		addAnnotation(descLen, "derivation", "self.name.size()");
		itemClass.getEStructuralFeatures().add(descLen);

		// Derived feature with only "initial" annotation (no "derivation")
		EAttribute halfPrice = EcoreFactory.eINSTANCE.createEAttribute();
		halfPrice.setName("halfPrice");
		halfPrice.setEType(EcorePackage.Literals.EDOUBLE);
		halfPrice.setDerived(true);
		halfPrice.setTransient(true);
		halfPrice.setVolatile(true);
		addAnnotation(halfPrice, "initial", "self.price / 2.0");
		itemClass.getEStructuralFeatures().add(halfPrice);

		// Derived feature with NO OCL annotation at all
		EAttribute noAnnotation = EcoreFactory.eINSTANCE.createEAttribute();
		noAnnotation.setName("noAnnotation");
		noAnnotation.setEType(EcorePackage.Literals.EINT);
		noAnnotation.setDerived(true);
		noAnnotation.setTransient(true);
		noAnnotation.setVolatile(true);
		itemClass.getEStructuralFeatures().add(noAnnotation);

		// Operation with "body" annotation
		EOperation isExpensive = EcoreFactory.eINSTANCE.createEOperation();
		isExpensive.setName("isExpensive");
		isExpensive.setEType(EcorePackage.Literals.EBOOLEAN);
		addAnnotation(isExpensive, "body", "self.price > 100.0");
		itemClass.getEOperations().add(isExpensive);

		// Operation with NO "body" annotation
		EOperation noBodyOp = EcoreFactory.eINSTANCE.createEOperation();
		noBodyOp.setName("noBodied");
		noBodyOp.setEType(EcorePackage.Literals.ESTRING);
		itemClass.getEOperations().add(noBodyOp);

		// Validation constraint via annotation on the EClass
		addAnnotation(itemClass, "pricePositive", "self.price > 0.0");

		// EDataType for EDataType validation test
		myDataType = EcoreFactory.eINSTANCE.createEDataType();
		myDataType.setName("PositiveInt");
		myDataType.setInstanceTypeName("int");
		testPkg.getEClassifiers().add(myDataType);

		EPackage.Registry.INSTANCE.put(testPkg.getNsURI(), testPkg);
		engine.installDelegates();
	}

	@AfterAll
	static void tearDown() {
		engine.uninstallDelegates();
		EPackage.Registry.INSTANCE.remove(testPkg.getNsURI());
	}

	private static void addAnnotation(org.eclipse.emf.ecore.EModelElement element,
			String key, String value) {
		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(DELEGATE_URI);
		ann.getDetails().put(key, value);
		element.getEAnnotations().add(ann);
	}

	private static EObject createItem(String name, double price) {
		EFactory factory = testPkg.getEFactoryInstance();
		EObject item = factory.create(itemClass);
		item.eSet(itemClass.getEStructuralFeature("name"), name);
		item.eSet(itemClass.getEStructuralFeature("price"), price);
		return item;
	}

	// === Validation delegate: EOperation overload ===

	@Test
	void validate_withEOperation() {
		EObject item = createItem("Widget", 50.0);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		EOperation op = itemClass.getEOperations().get(0);
		// validate(EClass, EObject, Map, EOperation, String expression)
		boolean result = delegate.validate(itemClass, item, null, op, "self.price > 0.0");
		assertTrue(result);
	}

	@Test
	void validate_withEOperation_violatedConstraint() {
		EObject item = createItem("Free", 0.0);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		EOperation op = itemClass.getEOperations().get(0);
		boolean result = delegate.validate(itemClass, item, null, op, "self.price > 0.0");
		assertFalse(result);
	}

	// === Validation delegate: null expression with annotation lookup ===

	@Test
	void validate_nullExpression_lookupFromAnnotation() {
		EObject item = createItem("Widget", 50.0);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		// expression=null → should look up "pricePositive" from EAnnotation on itemClass
		boolean result = delegate.validate(itemClass, item, null, "pricePositive", null);
		assertTrue(result);
	}

	@Test
	void validate_nullExpression_violatedFromAnnotation() {
		EObject item = createItem("Free", -10.0);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		boolean result = delegate.validate(itemClass, item, null, "pricePositive", null);
		assertFalse(result);
	}

	@Test
	void validate_nullExpression_missingConstraint_throwsException() {
		EObject item = createItem("Widget", 50.0);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		assertThrows(IllegalStateException.class, () ->
				delegate.validate(itemClass, item, null, "nonExistentConstraint", null));
	}

	// === Validation delegate: non-boolean result ===

	@Test
	void validate_nonBooleanResult_returnsTrue() {
		// Expression returns a non-null, non-boolean, non-OclInvalid value → true
		EObject item = createItem("Widget", 50.0);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		boolean result = delegate.validate(itemClass, item, null,
				"nameCheck", "self.name");
		assertTrue(result);
	}

	// === Validation delegate: parse error ===

	@Test
	void validate_parseError_throwsIllegalState() {
		EObject item = createItem("Widget", 50.0);
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		assertThrows(IllegalStateException.class, () ->
				delegate.validate(itemClass, item, null,
						"bad", "self.!!!invalid!!!"));
	}

	// === Validation delegate: EDataType overload ===

	@Test
	void validate_edataType_validExpression() {
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		// EDataType validation with valid expression
		boolean result = delegate.validate(myDataType, 42, null,
				"positive", "self > 0");
		assertTrue(result);
	}

	@Test
	void validate_edataType_invalidExpression() {
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		// EDataType with unparseable expression → false
		boolean result = delegate.validate(myDataType, 42, null,
				"bad", "!!!invalid!!!");
		assertFalse(result);
	}

	@Test
	void validate_edataType_nullExpression_throwsException() {
		EValidator.ValidationDelegate delegate =
				(EValidator.ValidationDelegate) EValidator.ValidationDelegate.Registry.INSTANCE.get(DELEGATE_URI);
		assertThrows(IllegalStateException.class, () ->
				delegate.validate(myDataType, 42, null, "missing", null));
	}

	// === Setting delegate: "initial" fallback ===

	@Test
	void settingDelegate_initialFallback() {
		EObject item = createItem("Widget", 100.0);
		Object halfPrice = item.eGet(itemClass.getEStructuralFeature("halfPrice"));
		assertEquals(50.0, halfPrice);
	}

	@Test
	void settingDelegate_derivation() {
		EObject item = createItem("Hello", 10.0);
		Object nameLength = item.eGet(itemClass.getEStructuralFeature("nameLength"));
		assertEquals(5, nameLength);
	}

	@Test
	void settingDelegate_missingAnnotation_throwsException() {
		// Feature with no OCL annotation → should throw when creating delegate
		EStructuralFeature noAnn = itemClass.getEStructuralFeature("noAnnotation");
		EStructuralFeature.Internal.SettingDelegate.Factory factory =
				(EStructuralFeature.Internal.SettingDelegate.Factory)
				EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE.get(DELEGATE_URI);
		assertThrows(IllegalStateException.class, () ->
				factory.createSettingDelegate((EStructuralFeature.Internal) noAnn));
	}

	// === Invocation delegate: missing body ===

	@Test
	void invocationDelegate_missingBody_throwsException() {
		EOperation noBody = itemClass.getEOperations().get(1);
		EOperation.Internal.InvocationDelegate.Factory factory =
				(EOperation.Internal.InvocationDelegate.Factory)
				EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE.get(DELEGATE_URI);
		assertThrows(IllegalStateException.class, () ->
				factory.createInvocationDelegate((EOperation.Internal) noBody));
	}

	// === Invocation delegate: normal call ===

	@Test
	void invocationDelegate_isExpensive_true() throws InvocationTargetException {
		EObject item = createItem("Expensive", 200.0);
		EOperation op = itemClass.getEOperations().get(0);
		Object result = ((InternalEObject) item).eInvoke(op, null);
		assertEquals(true, result);
	}

	@Test
	void invocationDelegate_isExpensive_false() throws InvocationTargetException {
		EObject item = createItem("Cheap", 5.0);
		EOperation op = itemClass.getEOperations().get(0);
		Object result = ((InternalEObject) item).eInvoke(op, null);
		assertEquals(false, result);
	}
}
