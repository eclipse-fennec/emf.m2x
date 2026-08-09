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
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for custom OCL operations via {@link OclOperationProvider}.
 * D29: Operations are now configured via {@link OclConfiguration} with
 * {@code customOperationsEnabled(true)} and activated per-evaluation via
 * {@link OclEvaluationOptions#withCustomOperationsEnabled(boolean)}.
 */
class OclCustomOperationTest extends AbstractOclTest {

	static EObject self;

	private static final OclEvaluationOptions CUSTOM_OPTS =
			OclEvaluationOptions.strict().withCustomOperationsEnabled(true);

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
	}

	/** Creates a custom engine with the given provider. */
	private static OclEngine engineWith(OclOperationProvider provider) {
		OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
				.addOperationProvider(provider)
				.customOperationsEnabled(true)
				.build();
		return OclEngines.create(config);
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

		OclEngine eng = engineWith(() -> List.of(op));
		var parsed = eng.parse("self.name.shout()", personClass);
		assertEquals("ALICE!", eng.evaluate(parsed, OclContext.of(self), CUSTOM_OPTS));
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

		OclEngine eng = engineWith(() -> List.of(op));
		var parsed = eng.parse("self.age.addN(5)", personClass);
		assertEquals(35, eng.evaluate(parsed, OclContext.of(self), CUSTOM_OPTS));
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

		OclEngine eng = engineWith(() -> List.of(doubleOp, exclaim));
		var parsedDouble = eng.parse("self.age.double()", personClass);
		var parsedExclaim = eng.parse("self.name.exclaim()", personClass);
		assertEquals(60, eng.evaluate(parsedDouble, OclContext.of(self), CUSTOM_OPTS));
		assertEquals("Alice!!!", eng.evaluate(parsedExclaim, OclContext.of(self), CUSTOM_OPTS));
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

		OclEngine eng = engineWith(() -> List.of(isLong));
		var parsed = eng.parse("self.name.isLongName()", personClass);
		assertEquals(true, eng.evaluate(parsed, OclContext.of(self), CUSTOM_OPTS));
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

		OclEngine eng = engineWith(() -> List.of(reverse));
		var parsed = eng.parse("self.name.reverse()", personClass);
		assertEquals("ecilA", eng.evaluate(parsed, OclContext.of(self), CUSTOM_OPTS));
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

}
