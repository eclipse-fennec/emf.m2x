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

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Argument conformance of operation calls (OCL v2.4 §7.4.5, §8.3.7 OperationCallExp [1]/[2],
 * #314): a non-conforming argument or a wrong number of arguments is reported, never coerced.
 */
class OclArgumentConformanceTest extends AbstractOclTest {

	static EObject self;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	private static final String[] NON_CONFORMING = {
			"1 + 'motorcycle'", // §7.4.5 verbatim
			"23 * false", // §7.4.5 verbatim
			"1 + true", "1.max('x')", "1 < 'a'", "1.mod(2.5)", "1.div(2.5)",
			"2.5 + 'a'", "3.0 * true", "2.5.max('x')",
			"'a' + 1", "'a'.concat(1)", "'a' < 1", "'abc'.indexOf(1)", "'abc'.equalsIgnoreCase(1)",
			"'abc'.substring('a', 2)", "'abc'.at(1.5)",
			"*.max('a')" };

	@Test
	void nonConformingArgument_isInvalidWithError() throws OclParseException {
		for (String expression : NON_CONFORMING) {
			assertInvalid(expression, self);
			assertHasError(expression, self, "must be");
		}
	}

	@Test
	void surplusArgument_ofStandardOperation_isInvalidWithError() throws OclParseException {
		assertInvalid("self.name.concat('a', 'b')", self);
		assertHasError("self.name.concat('a', 'b')", self, "expects 1 argument(s), got 2");
	}

	@Test
	void conformingArguments_stillWork() throws OclParseException {
		assertEquals(3.5, eval("1 + 2.5", self));
		assertEquals(3, eval("7.div(2)", self));
		assertEquals(true, eval("1 < 1.5", self));
		assertEquals("ab", eval("'a' + 'b'", self));
		assertEquals(-1, eval("-1", self));
		assertEquals(true, eval("1.max(*) = *", self));
	}

	@Test
	void modelOperation_withWrongArity_isAParseError() {
		EClass member = EcoreFactory.eINSTANCE.createEClass();
		member.setName("Member");
		EOperation isAdult = EcoreFactory.eINSTANCE.createEOperation();
		isAdult.setName("isAdult");
		isAdult.setEType(EcorePackage.Literals.EBOOLEAN);
		member.getEOperations().add(isAdult);

		OclParseException failure = assertThrows(OclParseException.class,
				() -> engine.parse("self.isAdult(1, 2)", member));
		assertTrue(failure.getMessage().contains("Member::isAdult expects 0 argument(s), got 2"),
				failure::getMessage);
	}
}
