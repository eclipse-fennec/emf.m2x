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

import java.util.Collection;
import java.util.Map;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.EMap;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.junit.jupiter.api.Test;

/**
 * Tests for the {@code useEMFTypes} evaluation option
 * (<a href="https://github.com/eclipse-fennec/emf.m2x/issues/4">issue #4</a>).
 *
 * <p>Default: OCL-spec-native Java types (ArrayList, OclOrderedSet, OclBag,
 * OclSet, LinkedHashMap). With {@code useEMFTypes=true}: top-level
 * {@link EList}/{@link EMap} wrapping for interop with EMF-shaped callers.
 */
class OclUseEMFTypesTest extends AbstractOclTest {

	private static EObject makeCompany() {
		return createCompany("Acme",
				createPerson("Jane", 30, 50000.0, true),
				createPerson("John", 25, 40000.0, false),
				createPerson("Alice", 40, 60000.0, true));
	}

	// --- Default behaviour: plain Java collections ---

	@Test
	void default_sequenceIsArrayList() throws Exception {
		Object result = engine.evaluate(
				"self.employees->asSequence()", OclContext.of(makeCompany()));
		assertTrue(result instanceof Collection);
		assertFalse(result instanceof EList);
	}

	@Test
	void default_mapLiteralIsJavaMap() throws Exception {
		Object result = engine.evaluate(
				"Map{'a' with 1, 'b' with 2}", OclContext.of(makeCompany()));
		assertTrue(result instanceof Map);
		assertFalse(result instanceof EMap);
	}

	// --- useEMFTypes=true: top-level EList / EMap ---

	@Test
	void useEMFTypes_sequenceIsEList() throws Exception {
		OclEvaluationOptions options = OclEvaluationOptions.strict().withUseEMFTypes(true);
		Object result = engine.evaluate(
				parse("self.employees->asSequence()"), OclContext.of(makeCompany()), options);

		assertTrue(result instanceof EList,
				"expected EList, got " + result.getClass().getName());
		assertEquals(3, ((EList<?>) result).size());
	}

	@Test
	void useEMFTypes_orderedSetIsEList() throws Exception {
		OclEvaluationOptions options = OclEvaluationOptions.strict().withUseEMFTypes(true);
		Object result = engine.evaluate(
				parse("self.employees->asOrderedSet()"), OclContext.of(makeCompany()), options);
		assertTrue(result instanceof EList);
		assertEquals(3, ((EList<?>) result).size());
	}

	@Test
	void useEMFTypes_bagIsEList() throws Exception {
		OclEvaluationOptions options = OclEvaluationOptions.strict().withUseEMFTypes(true);
		Object result = engine.evaluate(
				parse("self.employees->asBag()"), OclContext.of(makeCompany()), options);
		assertTrue(result instanceof EList);
		assertEquals(3, ((EList<?>) result).size());
	}

	@Test
	void useEMFTypes_setIsEList() throws Exception {
		OclEvaluationOptions options = OclEvaluationOptions.strict().withUseEMFTypes(true);
		Object result = engine.evaluate(
				parse("self.employees->asSet()"), OclContext.of(makeCompany()), options);
		assertTrue(result instanceof EList);
		assertEquals(3, ((EList<?>) result).size());
	}

	@Test
	void useEMFTypes_eListIsUnmodifiable() throws Exception {
		OclEvaluationOptions options = OclEvaluationOptions.strict().withUseEMFTypes(true);
		EList<?> result = (EList<?>) engine.evaluate(
				parse("self.employees->asSequence()"), OclContext.of(makeCompany()), options);
		assertThrows(UnsupportedOperationException.class, () -> result.clear());
	}

	@Test
	void useEMFTypes_mapLiteralIsEMap() throws Exception {
		OclEvaluationOptions options = OclEvaluationOptions.strict().withUseEMFTypes(true);
		Object result = engine.evaluate(
				parse("Map{'a' with 1, 'b' with 2}"), OclContext.of(makeCompany()), options);

		assertTrue(result instanceof EMap,
				"expected EMap, got " + result.getClass().getName());
		EMap<?, ?> map = (EMap<?, ?>) result;
		assertEquals(2, map.size());
		assertEquals(1, ((Number) map.get("a")).intValue());
		assertEquals(2, ((Number) map.get("b")).intValue());
	}

	// --- Edge cases ---

	@Test
	void useEMFTypes_singleValueIsPassedThrough() throws Exception {
		OclEvaluationOptions options = OclEvaluationOptions.strict().withUseEMFTypes(true);
		Object result = engine.evaluate(
				parse("self.employees->size()"), OclContext.of(makeCompany()), options);
		assertEquals(3, ((Number) result).intValue());
	}

	@Test
	void useEMFTypes_stringIsPassedThrough() throws Exception {
		OclEvaluationOptions options = OclEvaluationOptions.strict().withUseEMFTypes(true);
		Object result = engine.evaluate(
				parse("'hello'"), OclContext.of(makeCompany()), options);
		assertEquals("hello", result);
	}

	private static org.eclipse.fennec.m2x.model.ocl.OclExpression parse(String expr) throws Exception {
		return engine.parse(expr, companyClass);
	}
}
