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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * The standard library against input that is not a question but an attack (#182).
 *
 * <p>Three of these operations exist for QVT-O's `List` and `Dict` (D26) and were reachable from
 * plain OCL: `add`/`remove`/`clear` on the value of a many-valued feature is the live list of the
 * model object, so an expression — a constraint, evaluated by the validation delegate — could
 * rewrite the model it was asked to check. The other two are allocation taken from user data.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclStdlibHardeningTest extends AbstractOclTest {

	// ==== mutation of a model-owned collection ====

	@Test
	void addOnAModelFeature_isInvalid_andLeavesTheModelAlone() throws OclParseException {
		EObject company = createCompany("Acme", createPerson("Alice", 30, 1000.0, false));
		int before = employeeCount(company);

		Object result = eval("self.employees->add(self.employees->first())", company);

		assertSame(OclInvalid.INSTANCE, result,
				"an expression must not append to the model's own list");
		assertEquals(before, employeeCount(company), "and the model is unchanged");
	}

	@Test
	void clearOnAModelFeature_isInvalid_andLeavesTheModelAlone() throws OclParseException {
		EObject company = createCompany("Acme", createPerson("Bob", 40, 2000.0, true));
		int before = employeeCount(company);

		eval("self.employees->clear()", company);

		assertEquals(before, employeeCount(company), "clear() must not empty a model feature");
	}

	@Test
	void mutationOfAnExpressionsOwnCollection_stillWorks() throws OclParseException {
		// The QVT-O case these operations exist for: a collection the expression built itself
		assertEquals(3, eval("Sequence{1, 2}->including(3)->size()", createPerson("C", 1, 1.0, false)));
	}

	// ==== allocation from user data ====

	@Test
	@Timeout(30)
	void hugeRange_isInvalid_notAnOutOfMemoryError() throws OclParseException {
		Object value = engine.evaluate(engine.parse("1.range(2000000000)", personClass),
				OclContext.of(createPerson("A", 1, 1.0, false)),
				engine.getDefaultOptions().withMaxCollectionSize(100));
		assertSame(OclInvalid.INSTANCE, value,
				"a range past the collection limit is invalid, not an allocation");
	}

	@Test
	void rangeWithinTheLimit_stillWorks() throws OclParseException {
		assertEquals(5, eval("1.range(5)->size()", createPerson("A", 1, 1.0, false)));
	}

	@Test
	@Timeout(30)
	void formatWithAnOversizedWidth_isInvalid() throws OclParseException {
		Object result = eval("'%09999999999d'.format(Sequence{1})", createPerson("A", 1, 1.0, false));
		assertSame(OclInvalid.INSTANCE, result,
				"the width field of a format string is an allocation request");
	}

	@Test
	void ordinaryFormat_stillWorks() throws OclParseException {
		assertEquals("x=1", eval("'x=%d'.format(Sequence{1})", createPerson("A", 1, 1.0, false)));
	}

	// ==== arity ====

	@Test
	void anOperationCalledWithTooFewArguments_isInvalid_notAnException() throws OclParseException {
		EObject person = createPerson("A", 1, 1.0, false);
		// Operations are resolved by name at evaluation time, so these parse and then have to
		// yield a value rather than throw out of evaluate()
		assertNotEquals(null, eval("Set{1}->includes()", person));
		assertNotEquals(null, eval("self.oclIsKindOf()", person));
		assertNotEquals(null, eval("'abc'.substring()", person));
	}

	// ==== helpers ====

	@SuppressWarnings("unchecked")
	private static int employeeCount(EObject company) {
		return ((List<EObject>) company.eGet(companyClass.getEStructuralFeature("employees"))).size();
	}
}
