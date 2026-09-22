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

	/**
	 * The specifier grammar has more spellings for a width than {@code %Ns}: an explicit argument
	 * index ({@code %1$Ns}), the relative index ({@code %<Ns}), left-justification and precision.
	 * Every one of them is the same allocation request.
	 */
	@Test
	@Timeout(30)
	void formatWithAnOversizedWidth_isInvalid_howeverTheWidthIsSpelled() throws OclParseException {
		List<String> spellings = List.of(
				"'%50000000s'.format('x')",
				"'%1$50000000s'.format('x')",
				"'%s%<50000000s'.format('x')",
				"'%-50000000s'.format('x')",
				"'%50000000.3s'.format('x')",
				"'%1$50000000d'.format(Sequence{1})",
				"'%1$50000000s'.format(Sequence{'x'})",
				"'a%50000000sb%50000000sc'.format(Sequence{'x', 'y'})");
		EObject person = createPerson("A", 1, 1.0, false);
		for (String expression : spellings) {
			Object value = engine.evaluate(engine.parse(expression, personClass), OclContext.of(person),
					engine.getDefaultOptions().withMaxStringLength(100));
			assertSame(OclInvalid.INSTANCE, value,
					expression + ": a result past the string limit is invalid, not an allocation");
		}
	}

	@Test
	void ordinaryFormat_stillWorks() throws OclParseException {
		assertEquals("x=1", eval("'x=%d'.format(Sequence{1})", createPerson("A", 1, 1.0, false)));
	}

	@Test
	void formatWithinTheLimit_stillWorks_howeverTheWidthIsSpelled() throws OclParseException {
		EObject person = createPerson("A", 1, 1.0, false);
		assertEquals("    x|x    |x", eval("'%1$5s|%<-5s|%<.1s'.format('x')", person));
		assertEquals("  1|ab   ", eval("'%3d|%-5s'.format(Sequence{1, 'ab'})", person));
	}

	@Test
	void formatFillingTheLimitExactly_stillWorks() throws OclParseException {
		Object value = engine.evaluate(engine.parse("'%10s'.format('x')", personClass),
				OclContext.of(createPerson("A", 1, 1.0, false)),
				engine.getDefaultOptions().withMaxStringLength(10));
		assertEquals("         x", value, "the limit is inclusive");
	}

	// ==== growth: what an expression can build up, step by step ====

	/**
	 * The limits on ranges and products bound what an expression creates in one step. Doubling
	 * in an {@code iterate} — or a QVT-O {@code while} — is thirty small steps to a gigabyte,
	 * at a depth of four; the deadline is polled, but thirty steps take milliseconds.
	 */
	@Test
	@Timeout(30)
	void doublingAString_stopsAtTheStringLimit_howeverItIsSpelled() throws OclParseException {
		List<String> spellings = List.of(
				"Sequence{1..40}->iterate(i; acc : String = 'x' | acc.concat(acc))",
				"Sequence{1..40}->iterate(i; acc : String = 'x' | acc + acc)",
				"Sequence{1..40}->iterate(i; acc : String = 'x' | acc.replaceAll('x', 'xxxxxxxxxx'))",
				"Sequence{1..40}->iterate(i; acc : String = 'x' | acc.replaceAll('x+', '$0$0'))",
				"Sequence{1..40}->iterate(i; acc : String = 'x' | acc.replaceFirst('x+', '$0$0'))",
				"Sequence{1..40}->iterate(i; acc : String = 'x' | Sequence{acc, acc}->joinfields('', '', ''))",
				"Sequence{1..40}->iterate(i; acc : String = 'x' | '%s%s'.format(Sequence{acc, acc}))");
		EObject person = createPerson("A", 1, 1.0, false);
		for (String expression : spellings) {
			Object value = engine.evaluate(engine.parse(expression, personClass), OclContext.of(person),
					engine.getDefaultOptions().withMaxStringLength(1000));
			assertSame(OclInvalid.INSTANCE, value,
					expression + ": a string past the limit is invalid, not an allocation");
		}
	}

	@Test
	@Timeout(30)
	void doublingACollection_stopsAtTheCollectionLimit_howeverItIsSpelled() throws OclParseException {
		List<String> spellings = List.of(
				"Sequence{1..40}->iterate(i; acc : Sequence(Integer) = Sequence{1} | acc->union(acc))",
				"Sequence{1..40}->iterate(i; acc : Bag(Integer) = Bag{1} | acc->union(acc))",
				"Sequence{1..40}->iterate(i; acc : Sequence(Integer) = Sequence{1} | acc->including(i)->union(acc))",
				"Sequence{1..40}->iterate(i; acc : Sequence(Integer) = Sequence{1} | acc->append(i)->union(acc))",
				"Sequence{1..40}->iterate(i; acc : Sequence(Integer) = Sequence{1} | acc->prepend(i)->union(acc))",
				"Sequence{1..40}->iterate(i; acc : Sequence(Integer) = Sequence{1} | acc->insertAt(1, i)->union(acc))",
				"Sequence{1..40}->iterate(i; acc : Sequence(Integer) = Sequence{1} | Sequence{acc, acc}->flatten())",
				"Sequence{1..40}->iterate(i; acc : Sequence(Integer) = Sequence{1} | Sequence{acc, acc}->collect(c | c))",
				"Sequence{1..2000}->collect(i | Sequence{1..2000})",
				"Sequence{1..2000}->collect(i | Sequence{1..2000})->flatten()");
		EObject person = createPerson("A", 1, 1.0, false);
		for (String expression : spellings) {
			Object value = engine.evaluate(engine.parse(expression, personClass), OclContext.of(person),
					engine.getDefaultOptions().withMaxCollectionSize(1000));
			assertSame(OclInvalid.INSTANCE, value,
					expression + ": a collection past the limit is invalid, not an allocation");
		}
	}

	@Test
	void growthWithinTheLimits_stillWorks() throws OclParseException {
		EObject person = createPerson("A", 1, 1.0, false);
		OclEvaluationOptions options = engine.getDefaultOptions()
				.withMaxStringLength(64).withMaxCollectionSize(64);
		assertEquals(64, ((String) engine.evaluate(engine.parse(
				"Sequence{1..6}->iterate(i; acc : String = 'x' | acc + acc)", personClass),
				OclContext.of(person), options)).length(), "the string limit is inclusive");
		assertEquals(64, ((java.util.Collection<?>) engine.evaluate(engine.parse(
				"Sequence{1..6}->iterate(i; acc : Sequence(Integer) = Sequence{1} | acc->union(acc))",
				personClass), OclContext.of(person), options)).size(), "the collection limit is inclusive");
		assertEquals("b-b-b", engine.evaluate(engine.parse("'a-a-a'.replaceAll('a', 'b')", personClass),
				OclContext.of(person), options));
		assertEquals("aa-a-a", engine.evaluate(engine.parse("'a-a-a'.replaceFirst('a', '$0$0')", personClass),
				OclContext.of(person), options));
		assertEquals("<a,b>", engine.evaluate(engine.parse(
				"Sequence{'a', 'b'}->joinfields(',', '<', '>')", personClass), OclContext.of(person), options));
		assertEquals(4, ((java.util.Collection<?>) engine.evaluate(engine.parse(
				"Sequence{1..2}->collect(i | Sequence{1..2})", personClass),
				OclContext.of(person), options)).size());
	}

	@Test
	void replaceWithAGroupThePatternDoesNotHave_isInvalid() throws OclParseException {
		assertSame(OclInvalid.INSTANCE, eval("'abc'.replaceAll('b', '$1')", createPerson("A", 1, 1.0, false)));
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
