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
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.lang.annotation.Annotation;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.ErrorRecovery;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions.NullHandling;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.engine.OclConfigurationHelper;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineConfiguration;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OclConfiguration} engine-wide defaults and
 * {@link OclConfigurationHelper} mapping from OSGi Metatype annotation.
 */
class OclConfigurationDefaultsTest extends AbstractOclTest {

	// --- OclConfiguration default values ---

	@Test
	void oclConfiguration_defaultValues_matchStrictDefaults() {
		OclConfiguration config = OclConfiguration.builder(new OclParserSupport()).build();
		assertEquals(NullHandling.STRICT, config.nullHandling());
		assertEquals(ErrorRecovery.FAIL_FAST, config.errorRecovery());
		assertEquals(1000, config.maxDepth());
		assertEquals(0, config.timeoutMs());
		assertEquals(1_000_000, config.maxCollectionSize());
		assertEquals(100_000, config.maxClosureIterations());
		assertEquals(1000, config.maxRegexLength());
		assertFalse(config.customOperationsEnabled());
	}

	// --- OclConfiguration custom values ---

	@Test
	void oclConfiguration_customValues_storedCorrectly() {
		OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
				.nullHandling(NullHandling.LENIENT)
				.errorRecovery(ErrorRecovery.COLLECT_ERRORS)
				.maxDepth(500)
				.timeoutMs(5000)
				.maxCollectionSize(10_000)
				.maxClosureIterations(200)
				.maxRegexLength(50)
				.customOperationsEnabled(true)
				.build();

		assertEquals(NullHandling.LENIENT, config.nullHandling());
		assertEquals(ErrorRecovery.COLLECT_ERRORS, config.errorRecovery());
		assertEquals(500, config.maxDepth());
		assertEquals(5000, config.timeoutMs());
		assertEquals(10_000, config.maxCollectionSize());
		assertEquals(200, config.maxClosureIterations());
		assertEquals(50, config.maxRegexLength());
	}

	// --- OclConfigurationHelper mapping ---

	@Test
	void oclConfigurationHelper_from_mapsAnnotationToBuilder() {
		OclEngineConfiguration annotation = stubConfig(
				5, 500, 50, 100, 3000, "LENIENT", "COLLECT_ERRORS", true);

		OclConfiguration config = OclConfigurationHelper.from(
				annotation, new OclParserSupport(), null);

		assertEquals(NullHandling.LENIENT, config.nullHandling());
		assertEquals(ErrorRecovery.COLLECT_ERRORS, config.errorRecovery());
		assertEquals(5, config.maxDepth());
		assertEquals(3000, config.timeoutMs());
		assertEquals(500, config.maxCollectionSize());
		assertEquals(50, config.maxClosureIterations());
		assertEquals(100, config.maxRegexLength());
	}

	// --- Engine uses config defaults for evaluate(expr, ctx) ---

	@Test
	void oclEngineImpl_evaluateWithoutOptions_usesConfigDefaults() throws OclParseException {
		OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
				.maxDepth(3)
				.build();
		OclEngineImpl customEngine = new OclEngineImpl(config);

		EObject self = createPerson("Alice", 30, 50000.0, true);

		// A deeply recursive expression should hit the maxDepth=3 limit
		// let a : Integer = 1 in let b : Integer = a in let c : Integer = b in let d : Integer = c in d
		// This expression has 4 nested lets → depth > 3
		assertInstanceOf(OclInvalid.class,
				customEngine.evaluate("let a : Integer = 1 in let b : Integer = a in let c : Integer = b in let d : Integer = c in d",
						OclContext.of(self)));
	}

	@Test
	void oclEngineImpl_evaluateWithExplicitOptions_overridesConfigDefaults() throws OclParseException {
		OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
				.maxDepth(3)
				.build();
		OclEngineImpl customEngine = new OclEngineImpl(config);

		EObject self = createPerson("Alice", 30, 50000.0, true);

		// Same expression but with explicit high maxDepth → should succeed
		OclEvaluationOptions overrideOptions = OclEvaluationOptions.strict().withMaxDepth(1000);
		Object result = customEngine.evaluate(
				customEngine.parse("let a : Integer = 1 in let b : Integer = a in let c : Integer = b in let d : Integer = c in d", personClass),
				OclContext.of(self),
				overrideOptions);
		assertEquals(1, result);
	}

	// --- Engine uses config nullHandling default ---

	@Test
	void oclEngineImpl_lenientConfig_nullNavigationReturnsNull() throws OclParseException {
		OclConfiguration config = OclConfiguration.builder(new OclParserSupport())
				.nullHandling(NullHandling.LENIENT)
				.build();
		OclEngineImpl lenientEngine = new OclEngineImpl(config);

		EObject self = createPerson("Alice", 30, 50000.0, true);
		// null.oclIsUndefined() with lenient config should return true (null propagates as null)
		Object result = lenientEngine.evaluate("null.oclIsUndefined()", OclContext.of(self));
		assertEquals(true, result);
	}

	// --- Validation ---

	@Test
	void oclConfiguration_maxDepth_rejectsNonPositive() {
		assertThrows(IllegalArgumentException.class, () ->
				OclConfiguration.builder(new OclParserSupport()).maxDepth(0).build());
	}

	@Test
	void oclConfiguration_timeoutMs_rejectsNegative() {
		assertThrows(IllegalArgumentException.class, () ->
				OclConfiguration.builder(new OclParserSupport()).timeoutMs(-1).build());
	}

	// --- Helper ---

	@SuppressWarnings("all")
	private static OclEngineConfiguration stubConfig(int maxDepth, int maxCollectionSize,
			int maxClosureIterations, int maxRegexLength, long timeout,
			String nullHandling, String errorRecovery, boolean customOpsEnabled) {
		return new OclEngineConfiguration() {
			@Override public Class<? extends Annotation> annotationType() { return OclEngineConfiguration.class; }
			@Override public int maxDepth() { return maxDepth; }
			@Override public int maxCollectionSize() { return maxCollectionSize; }
			@Override public int maxClosureIterations() { return maxClosureIterations; }
			@Override public int maxRegexLength() { return maxRegexLength; }
			@Override public long timeout() { return timeout; }
			@Override public String nullHandling() { return nullHandling; }
			@Override public String errorRecovery() { return errorRecovery; }
			@Override public boolean customOperationsEnabled() { return customOpsEnabled; }
		};
	}
}
