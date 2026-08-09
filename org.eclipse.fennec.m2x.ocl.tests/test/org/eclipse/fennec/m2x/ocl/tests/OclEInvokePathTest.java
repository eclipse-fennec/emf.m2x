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
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for the eInvoke path in OclEvaluator (lines 399-408).
 *
 * <p>When an OCL OperationCallExp has a {@code referredOperation} set by the parser
 * (matching a real EOperation on the context class), the evaluator uses
 * {@code eInvoke()} to call through the EMF invocation delegate.
 *
 * <p>Uses {@code prestate-test.ecore} which has EOperations with OCL body/pre/post
 * annotations that the InvocationDelegateFactory resolves.
 */
class OclEInvokePathTest {

	static OclEngine engine;
	static EcoreHelper ecoreHelper;
	static EPackage prestatePackage;
	static EClass accountClass;
	static EStructuralFeature balanceFeature;
	static EStructuralFeature accountNameFeature;

	@BeforeAll
	static void setUp() throws IOException {
		engine = OclEngines.create(new OclParserSupport());
		engine.installDelegates();
		ecoreHelper = new EcoreHelper(OclEInvokePathTest.class);
		prestatePackage = ecoreHelper.loadEcore("prestate-test.ecore");
		accountClass = ecoreHelper.getEClass(prestatePackage, "Account");
		balanceFeature = ecoreHelper.getFeature(accountClass, "balance");
		accountNameFeature = ecoreHelper.getFeature(accountClass, "name");
	}

	@AfterAll
	static void tearDown() {
		engine.uninstallDelegates();
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	private static EObject createAccount(String name, double balance) {
		EObject account = prestatePackage.getEFactoryInstance().create(accountClass);
		account.eSet(accountNameFeature, name);
		account.eSet(balanceFeature, balance);
		return account;
	}

	// --- eInvoke via parsed OCL expression ---

	@Test
	void eInvoke_computeBonus_viaParsedExpression() throws OclParseException {
		EObject account = createAccount("Test", 1000.0);

		// Parse an OCL expression that calls a model operation
		// The parser should resolve computeBonus as an EOperation → sets referredOperation
		OclExpression parsed = engine.parse("self.computeBonus()", accountClass);
		Object result = engine.evaluate(parsed, OclContext.of(account));

		// computeBonus body: self.balance * 0.1 = 100.0
		assertEquals(100.0, result);
	}

	@Test
	void eInvoke_withdraw_withParameter() throws OclParseException {
		EObject account = createAccount("Test", 1000.0);

		OclExpression parsed = engine.parse("self.withdraw(200.0)", accountClass);
		Object result = engine.evaluate(parsed, OclContext.of(account));

		// withdraw body: self.balance - amount = 1000.0 - 200.0 = 800.0
		assertEquals(800.0, result);
	}

	@Test
	void eInvoke_withdraw_preconditionFails_returnsInvalid() throws OclParseException {
		EObject account = createAccount("Test", 100.0);

		// amount 500 > balance 100 → precondition fails
		OclExpression parsed = engine.parse("self.withdraw(500.0)", accountClass);
		OclResult result = engine.evaluateWithDiagnostics(parsed,
				OclContext.of(account), engine.getDelegateOptions());

		// eInvoke wraps the exception in InvocationTargetException → evaluator catches it
		assertSame(OclInvalid.INSTANCE, result.value());
	}

	@Test
	void eInvoke_getInterest_withParameter() throws OclParseException {
		EObject account = createAccount("Bank", 5000.0);

		OclExpression parsed = engine.parse("self.getInterest(0.05)", accountClass);
		Object result = engine.evaluate(parsed, OclContext.of(account));

		// body: self.balance * rate = 5000 * 0.05 = 250.0
		assertEquals(250.0, result);
	}

	@Test
	void eInvoke_nameLength_returnsInteger() throws OclParseException {
		EObject account = createAccount("TestAccount", 1000.0);

		OclExpression parsed = engine.parse("self.nameLength()", accountClass);
		Object result = engine.evaluate(parsed, OclContext.of(account));

		// body: self.name.size() = 11
		assertEquals(11, result);
	}

	@Test
	void eInvoke_resultUsedInLargerExpression() throws OclParseException {
		EObject account = createAccount("Test", 1000.0);

		// Use the eInvoke result in a larger OCL expression
		OclExpression parsed = engine.parse(
				"self.computeBonus() + self.getInterest(0.1)", accountClass);
		Object result = engine.evaluate(parsed, OclContext.of(account));

		// computeBonus = 100.0, getInterest(0.1) = 100.0 → 200.0
		assertEquals(200.0, result);
	}

	@Test
	void eInvoke_resultInBooleanContext() throws OclParseException {
		EObject account = createAccount("Test", 1000.0);

		OclExpression parsed = engine.parse(
				"self.computeBonus() > 50.0", accountClass);
		Object result = engine.evaluate(parsed, OclContext.of(account));

		// computeBonus = 100.0 > 50.0 → true
		assertEquals(true, result);
	}
}
