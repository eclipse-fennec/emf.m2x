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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.util.List;

import org.eclipse.emf.common.util.BasicEList;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.fennec.m2x.model.ocl.MessageExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclEngineImpl;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclEvalEnvironment;
import org.eclipse.fennec.m2x.ocl.engine.internal.OclEvaluator;
import org.eclipse.fennec.m2x.ocl.engine.internal.PreStateSnapshot;
import org.eclipse.fennec.m2x.ocl.engine.internal.PropertyAccessorCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Tests for GAP-2 (@pre support), GAP-7 (oclIsNew), and GAP-6 (MessageExp).
 *
 * <p>Unit tests use programmatic AST nodes with manually constructed
 * {@link PreStateSnapshot}. Integration tests use EMF InvocationDelegates
 * loaded from {@code prestate-test.ecore}.
 */
class OclPreStateTest {

	private static final OclFactory F = OclFactory.eINSTANCE;

	static OclEngine engine;
	static EcoreHelper ecoreHelper;

	// company.ecore types (for unit tests with navigation chains)
	static EPackage companyPackage;
	static EClass companyClass;
	static EClass personClass;
	static EStructuralFeature salaryFeature;
	static EStructuralFeature personNameFeature;
	static EStructuralFeature personAgeFeature;
	static EStructuralFeature employerFeature;
	static EStructuralFeature companyNameFeature;
	static EStructuralFeature employeesFeature;

	// prestate-test.ecore types (for integration tests)
	static EPackage prestatePackage;
	static EClass accountClass;
	static EClass customerClass;
	static EStructuralFeature balanceFeature;
	static EStructuralFeature accountNameFeature;
	static EStructuralFeature ownerFeature;
	static EStructuralFeature customerNameFeature;
	static EStructuralFeature customerAgeFeature;

	@BeforeAll
	static void setUp() throws IOException {
		engine = OclEngines.create(new OclParserSupport());
		ecoreHelper = new EcoreHelper(OclPreStateTest.class);

		companyPackage = ecoreHelper.loadEcore("company.ecore");
		companyClass = ecoreHelper.getEClass(companyPackage, "Company");
		personClass = ecoreHelper.getEClass(companyPackage, "Person");
		salaryFeature = ecoreHelper.getFeature(personClass, "salary");
		personNameFeature = ecoreHelper.getFeature(personClass, "name");
		personAgeFeature = ecoreHelper.getFeature(personClass, "age");
		employerFeature = ecoreHelper.getFeature(personClass, "employer");
		companyNameFeature = ecoreHelper.getFeature(companyClass, "name");
		employeesFeature = ecoreHelper.getFeature(companyClass, "employees");

		prestatePackage = ecoreHelper.loadEcore("prestate-test.ecore");
		accountClass = ecoreHelper.getEClass(prestatePackage, "Account");
		customerClass = ecoreHelper.getEClass(prestatePackage, "Customer");
		balanceFeature = ecoreHelper.getFeature(accountClass, "balance");
		accountNameFeature = ecoreHelper.getFeature(accountClass, "name");
		ownerFeature = ecoreHelper.getFeature(accountClass, "owner");
		customerNameFeature = ecoreHelper.getFeature(customerClass, "name");
		customerAgeFeature = ecoreHelper.getFeature(customerClass, "age");
	}

	@AfterAll
	static void tearDown() {
		engine.uninstallDelegates();
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	// ---------------------------------------------------------------
	// Unit Tests: Simple @pre Property
	// ---------------------------------------------------------------

	@Nested
	class PrePropertySimpleTests {

		@Test
		void preProperty_returnsOldValueAfterChange() {
			// AST: self.salary@pre
			EObject person = createPerson("Alice", 50000.0);

			PropertyCallExp preCall = propertyCallPre(salaryFeature, selfExp());

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(preCall, 50000.0).build();

			person.eSet(salaryFeature, 75000.0);

			OclResult result = evaluateWithSnapshot(preCall, person, snapshot);
			assertEquals(50000.0, result.value());
		}

		@Test
		void preProperty_mixedCurrentAndPre() {
			// AST: self.salary > self.salary@pre → true after raise
			EObject person = createPerson("Bob", 40000.0);

			PropertyCallExp currentSalary = propertyCall(salaryFeature, selfExp());
			PropertyCallExp preSalary = propertyCallPre(salaryFeature, selfExp());

			OperationCallExp gt = opCall(">", currentSalary, preSalary);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(preSalary, 40000.0).build();

			person.eSet(salaryFeature, 60000.0);

			assertEquals(true, evaluateWithSnapshot(gt, person, snapshot).value());
		}

		@Test
		void preProperty_withoutSnapshot_fallsThroughToCurrentValue() {
			// EMPTY snapshot → no captured pre-value → normal eGet
			EObject person = createPerson("Charlie", 30000.0);

			PropertyCallExp preCall = propertyCallPre(salaryFeature, selfExp());

			OclResult result = evaluateWithSnapshot(preCall, person, PreStateSnapshot.EMPTY);
			assertEquals(30000.0, result.value());
		}

		@Test
		void preProperty_twoPrePropertiesInSameExpression() {
			// AST: self.salary@pre + self.age@pre (two @pre nodes)
			EObject person = createPerson("Dana", 50000.0);
			person.eSet(personAgeFeature, 30);

			PropertyCallExp preSalary = propertyCallPre(salaryFeature, selfExp());
			PropertyCallExp preAge = propertyCallPre(personAgeFeature, selfExp());

			OperationCallExp plus = opCall("+", preSalary, preAge);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(preSalary, 50000.0)
					.putPreValue(preAge, 30L)  // EInt → widened to Long internally
					.build();

			// Change both
			person.eSet(salaryFeature, 80000.0);
			person.eSet(personAgeFeature, 35);

			OclResult result = evaluateWithSnapshot(plus, person, snapshot);
			// 50000.0 + 30L: stdlib dispatches "+" on Double source with Long arg
			// OclStdlib adds these as doubles: 50000.0 + 30.0 = 50030.0
			assertEquals(50030.0, result.value());
		}

		@Test
		void preProperty_preOnAttributeCurrentOnSameAttribute() {
			// AST: self.salary@pre <> self.salary → true after change
			EObject person = createPerson("Eve", 60000.0);

			PropertyCallExp preSalary = propertyCallPre(salaryFeature, selfExp());
			PropertyCallExp currentSalary = propertyCall(salaryFeature, selfExp());

			OperationCallExp neq = opCall("<>", preSalary, currentSalary);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(preSalary, 60000.0).build();

			person.eSet(salaryFeature, 90000.0);

			assertEquals(true, evaluateWithSnapshot(neq, person, snapshot).value());
		}

		@Test
		void preProperty_equalityWhenUnchanged() {
			// AST: self.salary@pre = self.salary → true when salary didn't change
			EObject person = createPerson("Frank", 40000.0);

			PropertyCallExp preSalary = propertyCallPre(salaryFeature, selfExp());
			PropertyCallExp currentSalary = propertyCall(salaryFeature, selfExp());

			OperationCallExp eq = opCall("=", preSalary, currentSalary);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(preSalary, 40000.0).build();

			// Don't change salary — pre and current should be equal
			assertEquals(true, evaluateWithSnapshot(eq, person, snapshot).value());
		}
	}

	// ---------------------------------------------------------------
	// Unit Tests: Navigation Chains with @pre
	// ---------------------------------------------------------------

	@Nested
	class PrePropertyNavigationChainTests {

		@Test
		void navigationChain_employerPreThenName() {
			// AST: self.employer@pre.name
			// → navigate to pre-state employer, then read current name
			EObject company1 = createCompany("OldCorp");
			EObject company2 = createCompany("NewCorp");
			EObject person = createPersonInCompany("Alice", 50000.0, company1);

			// self.employer@pre → captures company1
			PropertyCallExp employerPre = propertyCallPre(employerFeature, selfExp());

			// .name (current, on the captured employer)
			PropertyCallExp name = propertyCall(companyNameFeature, employerPre);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(employerPre, company1).build();

			// Move person to company2
			moveEmployee(person, company2);

			OclResult result = evaluateWithSnapshot(name, person, snapshot);
			assertEquals("OldCorp", result.value());
		}

		@Test
		void navigationChain_employerThenNamePre() {
			// AST: self.employer.name@pre
			// → navigate to CURRENT employer, read pre-state name
			EObject company = createCompany("OrigName");
			EObject person = createPersonInCompany("Bob", 40000.0, company);

			// self.employer (current)
			PropertyCallExp employer = propertyCall(employerFeature, selfExp());
			// .name@pre (on current employer, but pre-state name)
			PropertyCallExp namePre = propertyCallPre(companyNameFeature, employer);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(namePre, "OrigName").build();

			// Change company name
			company.eSet(companyNameFeature, "RenamedCorp");

			OclResult result = evaluateWithSnapshot(namePre, person, snapshot);
			assertEquals("OrigName", result.value());
		}

		@Test
		void navigationChain_bothPreOnChain() {
			// AST: self.employer@pre.name@pre
			// → navigate to pre-state employer, read pre-state name of THAT employer
			EObject company1 = createCompany("Corp1");
			EObject company2 = createCompany("Corp2");
			EObject person = createPersonInCompany("Charlie", 30000.0, company1);

			// self.employer@pre
			PropertyCallExp employerPre = propertyCallPre(employerFeature, selfExp());
			// .name@pre
			PropertyCallExp namePre = propertyCallPre(companyNameFeature, employerPre);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(employerPre, company1)
					.putPreValue(namePre, "Corp1")
					.build();

			// Move person AND rename old company
			moveEmployee(person, company2);
			company1.eSet(companyNameFeature, "Corp1-Renamed");

			OclResult result = evaluateWithSnapshot(namePre, person, snapshot);
			// Should return "Corp1" (pre-state name of pre-state employer)
			assertEquals("Corp1", result.value());
		}

		@Test
		void navigationChain_preReferenceWithCurrentProperty_differentFromCurrentRefCurrentProp() {
			// Verify that employer@pre.name gives old employer's name,
			// while employer.name gives new employer's name
			EObject oldCompany = createCompany("OldCo");
			EObject newCompany = createCompany("NewCo");
			EObject person = createPersonInCompany("Dana", 50000.0, oldCompany);

			// employer@pre.name
			PropertyCallExp employerPre = propertyCallPre(employerFeature, selfExp());
			PropertyCallExp nameOnPre = propertyCall(companyNameFeature, employerPre);

			// employer.name (current)
			PropertyCallExp employerCurrent = propertyCall(employerFeature, selfExp());
			PropertyCallExp nameOnCurrent = propertyCall(companyNameFeature, employerCurrent);

			// employer@pre.name <> employer.name
			OperationCallExp neq = opCall("<>", nameOnPre, nameOnCurrent);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(employerPre, oldCompany).build();

			moveEmployee(person, newCompany);

			assertEquals(true, evaluateWithSnapshot(neq, person, snapshot).value());
		}
	}

	// ---------------------------------------------------------------
	// Unit Tests: @pre on OperationCallExp
	// ---------------------------------------------------------------

	@Nested
	class PreOperationCallTests {

		@Test
		void preOnOperationCall_returnsPreStateResult() {
			// AST: self.name.size()@pre → captures size of name before change
			EObject person = createPerson("Alice", 50000.0);

			// self.name
			PropertyCallExp nameCall = propertyCall(personNameFeature, selfExp());
			// .size()@pre
			OperationCallExp sizePre = F.createOperationCallExp();
			sizePre.setName("size");
			sizePre.setOwnedSource(nameCall);
			sizePre.setIsPre(true);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(sizePre, 5L)  // "Alice".size() = 5
					.build();

			// Change name to something longer
			person.eSet(personNameFeature, "Alexandra");

			OclResult result = evaluateWithSnapshot(sizePre, person, snapshot);
			assertEquals(5L, result.value());
		}

		@Test
		void preOnOperationCall_comparedWithCurrentCall() {
			// AST: self.name.size()@pre < self.name.size()
			EObject person = createPerson("Bob", 40000.0);

			// self.name.size()@pre
			PropertyCallExp name1 = propertyCall(personNameFeature, selfExp());
			OperationCallExp sizePre = F.createOperationCallExp();
			sizePre.setName("size");
			sizePre.setOwnedSource(name1);
			sizePre.setIsPre(true);

			// self.name.size() (current)
			PropertyCallExp name2 = propertyCall(personNameFeature, selfExp());
			OperationCallExp sizeCurrent = F.createOperationCallExp();
			sizeCurrent.setName("size");
			sizeCurrent.setOwnedSource(name2);

			OperationCallExp lt = opCall("<", sizePre, sizeCurrent);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(sizePre, 3L)  // "Bob".size() = 3
					.build();

			person.eSet(personNameFeature, "Robert");  // size 6

			assertEquals(true, evaluateWithSnapshot(lt, person, snapshot).value());
		}
	}

	// ---------------------------------------------------------------
	// Unit Tests: oclIsNew
	// ---------------------------------------------------------------

	@Nested
	class OclIsNewTests {

		@Test
		void oclIsNew_newObject_returnsTrue() {
			EObject existing = createPerson("Existing", 10000.0);
			EObject newObj = createPerson("New", 20000.0);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.addPreExistingObject(existing).build();

			OperationCallExp isNew = oclIsNewCall(selfExp());

			assertEquals(true, evaluateWithSnapshot(isNew, newObj, snapshot).value());
		}

		@Test
		void oclIsNew_existingObject_returnsFalse() {
			EObject existing = createPerson("Existing", 10000.0);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.addPreExistingObject(existing).build();

			OperationCallExp isNew = oclIsNewCall(selfExp());

			assertEquals(false, evaluateWithSnapshot(isNew, existing, snapshot).value());
		}

		@Test
		void oclIsNew_emptySnapshot_alwaysTrue() {
			// EMPTY has no pre-existing objects → everything is "new"
			EObject person = createPerson("Anyone", 10000.0);

			OperationCallExp isNew = oclIsNewCall(selfExp());

			assertEquals(true, evaluateWithSnapshot(isNew, person, PreStateSnapshot.EMPTY).value());
		}

		@Test
		void oclIsNew_nonEObject_returnsInvalid() {
			StringLiteralExp strLit = F.createStringLiteralExp();
			strLit.setStringSymbol("hello");

			OperationCallExp isNew = oclIsNewCall(strLit);

			EObject person = createPerson("Ctx", 10000.0);
			assertEquals(OclInvalid.INSTANCE, evaluateWithSnapshot(isNew, person, PreStateSnapshot.EMPTY).value());
		}

		@Test
		void oclIsNew_multiplePreExistingObjects() {
			EObject p1 = createPerson("P1", 10000.0);
			EObject p2 = createPerson("P2", 20000.0);
			EObject p3 = createPerson("P3", 30000.0);
			EObject newObj = createPerson("New", 40000.0);

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.addPreExistingObject(p1)
					.addPreExistingObject(p2)
					.addPreExistingObject(p3)
					.build();

			OperationCallExp isNew = oclIsNewCall(selfExp());

			assertFalse((Boolean) evaluateWithSnapshot(isNew, p1, snapshot).value());
			assertFalse((Boolean) evaluateWithSnapshot(isNew, p2, snapshot).value());
			assertFalse((Boolean) evaluateWithSnapshot(isNew, p3, snapshot).value());
			assertTrue((Boolean) evaluateWithSnapshot(isNew, newObj, snapshot).value());
		}

		@Test
		void oclIsNew_combinedWithPreProperty() {
			// Test: oclIsNew on self AND salary@pre in same snapshot
			EObject existing = createPerson("Existing", 50000.0);
			EObject newObj = createPerson("New", 60000.0);

			PropertyCallExp preSalary = propertyCallPre(salaryFeature, selfExp());
			OperationCallExp isNew = oclIsNewCall(selfExp());

			PreStateSnapshot.Builder builder = PreStateSnapshot.builder();
			builder.addPreExistingObject(existing);
			builder.putPreValue(preSalary, 50000.0);
			PreStateSnapshot snapshot = builder.build();

			// oclIsNew on existing → false
			assertEquals(false, evaluateWithSnapshot(isNew, existing, snapshot).value());
			// salary@pre on existing → 50000
			assertEquals(50000.0, evaluateWithSnapshot(preSalary, existing, snapshot).value());
			// oclIsNew on new → true
			assertEquals(true, evaluateWithSnapshot(isNew, newObj, snapshot).value());
		}
	}

	// ---------------------------------------------------------------
	// Unit Tests: MessageExp (GAP-6)
	// ---------------------------------------------------------------

	@Nested
	class MessageExpTests {

		@Test
		void messageExp_returnsInvalidWithDiagnostic() {
			MessageExp msgExp = F.createMessageExp();
			EObject person = createPerson("Test", 10000.0);

			OclEvalEnvironment env = OclEvalEnvironment.root(OclContext.of(person));
			OclEvaluator evaluator = new OclEvaluator(env, OclEvaluationOptions.strict(), List.of(), PropertyAccessorCache.getDefault());

			OclResult result = evaluator.evaluate(msgExp);
			assertEquals(OclInvalid.INSTANCE, result.value());
			assertFalse(result.diagnostics().isEmpty());
			assertTrue(result.diagnostics().get(0).getMessage().contains("MessageExp"));
		}
	}

	// ---------------------------------------------------------------
	// Unit Tests: PreStateSnapshot
	// ---------------------------------------------------------------

	@Nested
	class PreStateSnapshotTests {

		@Test
		void emptySnapshot_hasNoPreValues() {
			PropertyCallExp node = propertyCallPre(salaryFeature, selfExp());
			assertFalse(PreStateSnapshot.EMPTY.hasPreValue(node));
		}

		@Test
		void emptySnapshot_nothingExistedBefore() {
			EObject person = createPerson("Test", 10000.0);
			assertFalse(PreStateSnapshot.EMPTY.existedBefore(person));
		}

		@Test
		void snapshot_identityBasedKeyLookup() {
			// Two distinct PropertyCallExp for the same feature → different entries
			PropertyCallExp node1 = propertyCallPre(salaryFeature, selfExp());
			PropertyCallExp node2 = propertyCallPre(salaryFeature, selfExp());

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(node1, 100.0)
					.putPreValue(node2, 200.0)
					.build();

			assertTrue(snapshot.hasPreValue(node1));
			assertTrue(snapshot.hasPreValue(node2));
			assertEquals(100.0, snapshot.getPreValue(node1));
			assertEquals(200.0, snapshot.getPreValue(node2));
		}

		@Test
		void snapshot_nullPreValueIsDistinguishedFromMissing() {
			PropertyCallExp node = propertyCallPre(salaryFeature, selfExp());
			PropertyCallExp missing = propertyCallPre(salaryFeature, selfExp());

			PreStateSnapshot snapshot = PreStateSnapshot.builder()
					.putPreValue(node, null)
					.build();

			assertTrue(snapshot.hasPreValue(node));
			assertEquals(null, snapshot.getPreValue(node));
			assertFalse(snapshot.hasPreValue(missing));
		}
	}

	// ---------------------------------------------------------------
	// Integration Tests: InvocationDelegate with pre/post (ecore-based)
	// ---------------------------------------------------------------

	@Nested
	class InvocationDelegateTests {

		@Test
		void bodyWithPost_postconditionPasses() throws Exception {
			engine.installDelegates();

			EObject account = createAccount("Test", 1000.0);
			EOperation computeBonus = getOperation(accountClass, "computeBonus");

			Object result = ((InternalEObject) account).eInvoke(computeBonus, null);
			assertEquals(100.0, result);
		}

		@Test
		void bodyWithPost_postconditionFails() {
			engine.installDelegates();

			EObject account = createAccount("Test", 1000.0);
			EOperation computeLoss = getOperation(accountClass, "computeLoss");

			InvocationTargetException ex = assertThrows(InvocationTargetException.class,
					() -> ((InternalEObject) account).eInvoke(computeLoss, null));
			assertInstanceOf(IllegalStateException.class, ex.getCause());
			assertTrue(ex.getCause().getMessage().contains("Postcondition violated"));
		}

		@Test
		void preconditionFails() {
			engine.installDelegates();

			EObject account = createAccount("Test", 100.0);
			EOperation withdraw = getOperation(accountClass, "withdraw");

			EList<Object> args = new BasicEList<>();
			args.add(500.0);

			InvocationTargetException ex = assertThrows(InvocationTargetException.class,
					() -> ((InternalEObject) account).eInvoke(withdraw, args));
			assertInstanceOf(IllegalStateException.class, ex.getCause());
			assertTrue(ex.getCause().getMessage().contains("Precondition violated"));
		}

		@Test
		void preconditionPasses_bodyExecutes() throws Exception {
			engine.installDelegates();

			EObject account = createAccount("Test", 1000.0);
			EOperation withdraw = getOperation(accountClass, "withdraw");

			EList<Object> args = new BasicEList<>();
			args.add(200.0);

			Object result = ((InternalEObject) account).eInvoke(withdraw, args);
			assertEquals(800.0, result);
		}

		@Test
		void preAndPostBothPass_withParameter() throws Exception {
			engine.installDelegates();

			EObject account = createAccount("Bank", 5000.0);
			EOperation getInterest = getOperation(accountClass, "getInterest");

			EList<Object> args = new BasicEList<>();
			args.add(0.05);

			Object result = ((InternalEObject) account).eInvoke(getInterest, args);
			// body: 5000 * 0.05 = 250.0, post: result = self.balance * rate → 250.0 = 250.0 ✓
			assertEquals(250.0, result);
		}

		@Test
		void preAndPostWithParameter_preFails() {
			engine.installDelegates();

			EObject account = createAccount("Bank", 5000.0);
			EOperation getInterest = getOperation(accountClass, "getInterest");

			EList<Object> args = new BasicEList<>();
			args.add(1.5);  // rate > 1.0 → pre fails

			InvocationTargetException ex = assertThrows(InvocationTargetException.class,
					() -> ((InternalEObject) account).eInvoke(getInterest, args));
			assertInstanceOf(IllegalStateException.class, ex.getCause());
			assertTrue(ex.getCause().getMessage().contains("Precondition violated"));
		}

		@Test
		void postOnly_noBody_returnsNull() throws Exception {
			engine.installDelegates();

			EObject account = createAccount("PostOnly", 100.0);
			EOperation postOnlyOp = getOperation(accountClass, "postOnlyOp");

			Object result = ((InternalEObject) account).eInvoke(postOnlyOp, null);
			// No body → result is null; post: self.balance >= 0.0 → true ✓
			assertEquals(null, result);
		}

		@Test
		void postWithResultAndStringOp() throws Exception {
			engine.installDelegates();

			EObject account = createAccount("TestAccount", 1000.0);
			EOperation nameLength = getOperation(accountClass, "nameLength");

			Object result = ((InternalEObject) account).eInvoke(nameLength, null);
			// body: self.name.size() = 11, post: result > 0 → ✓
			assertEquals(11, result);
		}

		// --- @pre in postconditions (exercises PreStateScanCollector.capturePreState) ---

		@Test
		void postWithAtPre_deposit_capturesPreBalance() throws Exception {
			engine.installDelegates();

			EObject account = createAccount("Test", 1000.0);
			EOperation deposit = getOperation(accountClass, "deposit");

			EList<Object> args = new BasicEList<>();
			args.add(500.0);

			Object result = ((InternalEObject) account).eInvoke(deposit, args);
			// body: self.balance + amount = 1000.0 + 500.0 = 1500.0
			// post: result = self.balance@pre + amount → 1500.0 = 1000.0 + 500.0 ✓
			assertEquals(1500.0, result);
		}

		@Test
		void postWithAtPre_getNameSnapshot_capturesPreName() throws Exception {
			engine.installDelegates();

			EObject account = createAccount("OriginalName", 1000.0);
			EOperation getNameSnapshot = getOperation(accountClass, "getNameSnapshot");

			Object result = ((InternalEObject) account).eInvoke(getNameSnapshot, null);
			// body: self.name = "OriginalName"
			// post: result = self.name@pre → "OriginalName" = "OriginalName" ✓
			assertEquals("OriginalName", result);
		}

		@Test
		void postWithAtPre_doubleBalance_capturesPreBalance() throws Exception {
			engine.installDelegates();

			EObject account = createAccount("Test", 750.0);
			EOperation doubleBalance = getOperation(accountClass, "doubleBalance");

			Object result = ((InternalEObject) account).eInvoke(doubleBalance, null);
			// body: self.balance * 2.0 = 750.0 * 2.0 = 1500.0
			// post: result = self.balance@pre * 2.0 → 1500.0 = 750.0 * 2.0 ✓
			assertEquals(1500.0, result);
		}
	}

	// ---------------------------------------------------------------
	// AST Builder Helpers
	// ---------------------------------------------------------------

	private static VariableExp selfExp() {
		Variable selfVar = F.createVariable();
		selfVar.setName("self");
		VariableExp varExp = F.createVariableExp();
		varExp.setReferredVariable(selfVar);
		return varExp;
	}

	private static PropertyCallExp propertyCall(EStructuralFeature feature, OclExpression source) {
		PropertyCallExp call = F.createPropertyCallExp();
		call.setReferredProperty(feature);
		call.setOwnedSource(source);
		return call;
	}

	private static PropertyCallExp propertyCallPre(EStructuralFeature feature, OclExpression source) {
		PropertyCallExp call = propertyCall(feature, source);
		call.setIsPre(true);
		return call;
	}

	private static OperationCallExp opCall(String name, OclExpression source, OclExpression arg) {
		OperationCallExp call = F.createOperationCallExp();
		call.setName(name);
		call.setOwnedSource(source);
		call.getOwnedArguments().add(arg);
		return call;
	}

	private static OperationCallExp oclIsNewCall(OclExpression source) {
		OperationCallExp call = F.createOperationCallExp();
		call.setName("oclIsNew");
		call.setOwnedSource(source);
		return call;
	}

	@Nested
	@DisplayName("Delegate options")
	class DelegateOptionsTests {

		@Test
		@DisplayName("delegate evaluation runs with the options set for it, not with the defaults")
		void delegateOptionsGovernPostconditionEvaluation() throws OclParseException {
			// setDelegateOptions exists so EMF-delegate evaluation can differ from direct
			// evaluation, and evaluatePostcondition is the one path that reads them. Until #111
			// nothing tested that, so the setter could have had no effect at all.
			//
			// The observable knob here is maxDepth rather than null handling: LENIENT null
			// handling does not currently reach property navigation (#112), and a test must not
			// quietly encode a defect as the expected outcome.
			OclEngineImpl impl = (OclEngineImpl) engine;
			OclExpression nested = engine.parse("self.employer.name", personClass);
			EObject employed = createPersonInCompany("Alice", 1000, createCompany("ACME"));
			OclEvaluationOptions before = engine.getDelegateOptions();
			try {
				assertEquals("ACME", impl.evaluatePostcondition(nested,
						OclContext.of(employed), PreStateSnapshot.EMPTY),
						"with the default options the navigation answers");

				engine.setDelegateOptions(OclEvaluationOptions.strict().withMaxDepth(1));

				assertEquals(1, engine.getDelegateOptions().maxDepth(), "the setter round-trips");
				assertSame(OclInvalid.INSTANCE, impl.evaluatePostcondition(nested,
						OclContext.of(employed), PreStateSnapshot.EMPTY),
						"the delegate limit is what the delegate path evaluates under");
				assertEquals(1000, engine.getDefaultOptions().maxDepth(),
						"and direct evaluation keeps the engine's own options");
			} finally {
				// the engine is shared across this suite
				engine.setDelegateOptions(before);
			}
		}
	}

	// ---------------------------------------------------------------
	// Evaluation Helpers
	// ---------------------------------------------------------------

	private static OclResult evaluateWithSnapshot(OclExpression expression, EObject self,
			PreStateSnapshot snapshot) {
		OclEvalEnvironment env = OclEvalEnvironment.root(OclContext.of(self));
		OclEvaluator evaluator = new OclEvaluator(env, OclEvaluationOptions.strict(), List.of(), PropertyAccessorCache.getDefault());
		evaluator.setPreStateSnapshot(snapshot);
		return evaluator.evaluate(expression);
	}

	// ---------------------------------------------------------------
	// Model Instance Helpers
	// ---------------------------------------------------------------

	private static EObject createPerson(String name, double salary) {
		EObject person = companyPackage.getEFactoryInstance().create(personClass);
		person.eSet(personNameFeature, name);
		person.eSet(salaryFeature, salary);
		return person;
	}

	@SuppressWarnings("unchecked")
	private static EObject createPersonInCompany(String name, double salary, EObject company) {
		EObject person = createPerson(name, salary);
		((List<EObject>) company.eGet(employeesFeature)).add(person);
		return person;
	}

	private static EObject createCompany(String name) {
		EObject company = companyPackage.getEFactoryInstance().create(companyClass);
		company.eSet(companyNameFeature, name);
		return company;
	}

	@SuppressWarnings("unchecked")
	private static void moveEmployee(EObject person, EObject newCompany) {
		((List<EObject>) newCompany.eGet(employeesFeature)).add(person);
	}

	private static EObject createAccount(String name, double balance) {
		EObject account = prestatePackage.getEFactoryInstance().create(accountClass);
		account.eSet(accountNameFeature, name);
		account.eSet(balanceFeature, balance);
		return account;
	}

	private static EOperation getOperation(EClass eClass, String name) {
		return eClass.getEOperations().stream()
				.filter(op -> name.equals(op.getName()))
				.findFirst()
				.orElseThrow(() -> new IllegalArgumentException("Operation not found: " + name));
	}
}
