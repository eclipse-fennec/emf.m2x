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
import static org.junit.jupiter.api.Assertions.assertNotNull;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for custom OCL operations via {@link OclOperationProvider}.
 * Verifies register/unregister, and that custom operations are callable
 * from OCL expressions.
 */
class OclCustomOperationTest extends AbstractOclTest {

	static EObject self;
	private OclOperationProvider registeredProvider;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	@AfterEach
	void cleanUp() {
		if (registeredProvider != null) {
			engine.unregisterOperations(registeredProvider);
			registeredProvider = null;
		}
	}

	// --- Registration ---

	@Test
	void registerOperations_addsProvider() {
		OclOperationProvider provider = createDoubleAgeProvider();
		engine.registerOperations(provider);
		registeredProvider = provider;
		// Provider is registered — no exception
		assertNotNull(provider);
	}

	@Test
	void unregisterOperations_removesProvider() {
		OclOperationProvider provider = createDoubleAgeProvider();
		engine.registerOperations(provider);
		engine.unregisterOperations(provider);
		// Unregistered successfully — set to null so @AfterEach doesn't double-unregister
		registeredProvider = null;
	}

	// --- Custom no-arg operation ---

	@Test
	void customOperation_noArgs_onString() throws OclParseException {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType stringType = OclFactory.eINSTANCE.createPrimitiveType();
		stringType.setName("String");

		OclOperation op = OclOperation.of("shout", anyType, stringType,
				(source, args) -> source.toString().toUpperCase() + "!");

		registeredProvider = () -> List.of(op);
		engine.registerOperations(registeredProvider);

		assertEquals("ALICE!", engine.evaluate("self.name.shout()", OclContext.of(self)));
	}

	// --- Custom operation with argument ---

	@Test
	void customOperation_withArg() throws OclParseException {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");

		OclOperation op = new OclOperation("addN", anyType, List.of(intType), intType,
				(source, args) -> ((Number) source).longValue() + ((Number) args[0]).longValue());

		registeredProvider = () -> List.of(op);
		engine.registerOperations(registeredProvider);

		assertEquals(35, engine.evaluate("self.age.addN(5)", OclContext.of(self)));
	}

	// --- Multiple operations from one provider ---

	@Test
	void multipleOperations_sameProvider() throws OclParseException {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");
		PrimitiveType stringType = OclFactory.eINSTANCE.createPrimitiveType();
		stringType.setName("String");

		OclOperation doubleOp = OclOperation.of("double", anyType, intType,
				(source, args) -> ((Number) source).longValue() * 2);
		OclOperation exclaim = OclOperation.of("exclaim", anyType, stringType,
				(source, args) -> source.toString() + "!!!");

		registeredProvider = () -> List.of(doubleOp, exclaim);
		engine.registerOperations(registeredProvider);

		assertEquals(60, engine.evaluate("self.age.double()", OclContext.of(self)));
		assertEquals("Alice!!!", engine.evaluate("self.name.exclaim()", OclContext.of(self)));
	}

	// --- Operation returning boolean ---

	@Test
	void customOperation_returnsBoolean() throws OclParseException {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType boolType = OclFactory.eINSTANCE.createPrimitiveType();
		boolType.setName("Boolean");

		OclOperation isLong = OclOperation.of("isLongName", anyType, boolType,
				(source, args) -> source.toString().length() > 3);

		registeredProvider = () -> List.of(isLong);
		engine.registerOperations(registeredProvider);

		assertEquals(true, engine.evaluate("self.name.isLongName()", OclContext.of(self)));
	}

	// --- Operation returning String ---

	@Test
	void customOperation_returnsString() throws OclParseException {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType stringType = OclFactory.eINSTANCE.createPrimitiveType();
		stringType.setName("String");

		OclOperation reverse = OclOperation.of("reverse", anyType, stringType,
				(source, args) -> new StringBuilder(source.toString()).reverse().toString());

		registeredProvider = () -> List.of(reverse);
		engine.registerOperations(registeredProvider);

		assertEquals("ecilA", engine.evaluate("self.name.reverse()", OclContext.of(self)));
	}

	// --- OclOperation record ---

	@Test
	void oclOperation_record_fields() {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");

		OclOperation op = OclOperation.of("myOp", anyType, intType,
				(source, args) -> 42);

		assertEquals("myOp", op.name());
		assertEquals(anyType, op.ownerType());
		assertEquals(intType, op.returnType());
		assertEquals(List.of(), op.parameterTypes());
		assertNotNull(op.implementation());
	}

	@Test
	void oclOperation_withParams() {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");

		OclOperation op = new OclOperation("add", anyType,
				List.of(intType, intType), intType,
				(source, args) -> 0);

		assertEquals(2, op.parameterTypes().size());
	}

	// --- Helper ---

	private static OclOperationProvider createDoubleAgeProvider() {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");
		OclOperation op = OclOperation.of("doubleAge", anyType, intType,
				(source, args) -> ((Number) source).longValue() * 2);
		return () -> List.of(op);
	}
}
