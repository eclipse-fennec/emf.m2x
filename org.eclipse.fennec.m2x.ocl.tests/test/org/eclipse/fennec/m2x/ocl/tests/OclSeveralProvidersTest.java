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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Several operation providers work side by side.
 *
 * <p>The OCL specification has no notion of one source of additional operations: it does not
 * use the word "blackbox", §11.8.1 makes the standard library extensible without saying how
 * often, and §7.3.5 lets a file carry any number of {@code package} statements with their
 * {@code def:} declarations. A runtime holding the MOFM2T standard library and a set of
 * domain operations is the ordinary case, not an exotic one.
 *
 * <p>Plain Java always allowed it — {@code addOperationProvider} appends to a list — and no
 * test said so, so nothing kept it that way. The OSGi side did not allow it at all until the
 * reference became {@code MULTIPLE}; that half is covered by
 * {@code OclOperationProviderOSGiTest}. This is the same promise for the plain path, tested
 * the same way: both operations resolve on one engine.
 */
class OclSeveralProvidersTest {

	@Test
	@DisplayName("operations from two providers resolve on one engine")
	void bothProvidersContribute() throws OclParseException {
		OclEngine engine = engineWith(provider("first", 42), provider("second", 7));

		assertEquals(42, evaluate(engine, "self.first()"));
		assertEquals(7, evaluate(engine, "self.second()"));
	}

	@Test
	@DisplayName("the order they were added is the order they are asked")
	void theFirstProviderToDefineANameWins() throws OclParseException {
		// Two providers offering the same name is a configuration mistake, not a crash.
		// Which one answers has to be predictable, and the predictable answer is the one
		// that was added first.
		OclEngine engine = engineWith(provider("shared", 1), provider("shared", 2));

		assertEquals(1, evaluate(engine, "self.shared()"));
	}

	@Test
	@DisplayName("the gate still decides, however many providers there are")
	void theGateIsWhatProtects() throws OclParseException {
		// Security by default sits here, not in the number of providers: with the gate shut
		// no config-registered operation resolves, no matter how many were handed in.
		OclEngine engine = OclEngines.create(OclConfiguration.builder(new OclParserSupport())
				.addOperationProvider(provider("first", 42))
				.addOperationProvider(provider("second", 7))
				// customOperationsEnabled not set — defaults to false
				.build());

		assertInstanceOf(OclInvalid.class, evaluate(engine, "self.first()"));
		assertInstanceOf(OclInvalid.class, evaluate(engine, "self.second()"));
	}

	@Test
	@DisplayName("both providers are reported as active")
	void bothAreActive() {
		OclEngine engine = engineWith(provider("first", 42), provider("second", 7));

		assertEquals(2, engine.getOperationProviders(
				OclEvaluationOptions.strict().withCustomOperationsEnabled(true)).size());
	}

	// --- helpers ---

	private static OclOperationProvider provider(String operationName, int result) {
		AnyType anyType = OclFactory.eINSTANCE.createAnyType();
		anyType.setName("OclAny");
		PrimitiveType intType = OclFactory.eINSTANCE.createPrimitiveType();
		intType.setName("Integer");
		return () -> List.of(OclOperation.of(operationName, anyType, intType,
				(self, args) -> result));
	}

	private static OclEngine engineWith(OclOperationProvider... providers) {
		OclConfiguration.Builder builder = OclConfiguration.builder(new OclParserSupport())
				.customOperationsEnabled(true);
		for (OclOperationProvider provider : providers) {
			builder.addOperationProvider(provider);
		}
		return OclEngines.create(builder.build());
	}

	private static Object evaluate(OclEngine engine, String expression) throws OclParseException {
		EClass context = EcoreFactory.eINSTANCE.createEClass();
		context.setName("Probe");
		// Parsed first, because the options overload takes an expression — and the options
		// are what carry the second half of the D29 gate.
		return engine.evaluate(engine.parse(expression, context), OclContext.of(context),
				OclEvaluationOptions.strict().withCustomOperationsEnabled(true));
	}
}
