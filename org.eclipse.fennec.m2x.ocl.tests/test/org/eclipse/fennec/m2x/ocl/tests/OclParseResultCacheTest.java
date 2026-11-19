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
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.ocl.api.OclCacheKey;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclParseResult;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The engine caches the outcome of a parse, not only a successful one (#420): a text that
 * does not parse is reported again without being parsed again, and a parse with declared
 * variables is cached under a key that includes them.
 */
class OclParseResultCacheTest {

	private static final String BROKEN = "self.name +";

	static EcoreHelper ecoreHelper;
	static EClass personClass;
	static EClass companyClass;

	@BeforeAll
	static void setUp() throws IOException {
		ecoreHelper = new EcoreHelper(OclParseResultCacheTest.class);
		EPackage companyPackage = ecoreHelper.loadEcore("company.ecore");
		personClass = ecoreHelper.getEClass(companyPackage, "Person");
		companyClass = ecoreHelper.getEClass(companyPackage, "Company");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	// --- negative cache ---

	@Test
	@DisplayName("a text that does not parse is not parsed again")
	void failureIsRemembered() {
		CountingParser parser = new CountingParser();
		OclEngine engine = OclEngines.create(parser, OclLruExpressionCache.ofSize(16));

		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));
		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));

		assertEquals(1, parser.calls.get(), "the second call answers from the cache");
	}

	@Test
	@DisplayName("the remembered failure reports what the first one did, in a fresh exception")
	void rethrownFailureIsFresh() {
		OclEngine engine = OclEngines.create(new OclParserSupport(), OclLruExpressionCache.ofSize(16));

		OclParseException first = assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));
		OclParseException second = assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));

		assertNotSame(first, second, "a shared instance would carry the first caller's stack");
		assertEquals(first.getMessage(), second.getMessage());
		assertEquals(first.getErrors(), second.getErrors());
		assertFalse(second.getErrors().isEmpty(), "the diagnostics survive the cache");
	}

	@Test
	@DisplayName("invalidating a failure parses the text again")
	void invalidateCoversFailures() {
		CountingParser parser = new CountingParser();
		OclExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(parser, cache);

		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));
		cache.invalidate(BROKEN, personClass);
		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));

		assertEquals(2, parser.calls.get());
	}

	@Test
	@DisplayName("invalidateAll drops failures too")
	void invalidateAllCoversFailures() {
		CountingParser parser = new CountingParser();
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(parser, cache);

		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));
		assertEquals(1, cache.failureCount());
		cache.invalidateAll();
		assertEquals(0, cache.size());
		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));

		assertEquals(2, parser.calls.get());
	}

	@Test
	@DisplayName("a failure is a hit, counted once")
	void failureCountsAsHit() {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(new OclParserSupport(), cache);

		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));
		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));

		assertEquals(1, cache.missCount());
		assertEquals(1, cache.hitCount());
	}

	// --- bounding failures ---

	@Test
	@DisplayName("failures are bounded by a limit of their own")
	void failuresAreBounded() {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16, 2);
		OclEngine engine = OclEngines.create(new OclParserSupport(), cache);

		for (int i = 0; i < 10; i++) {
			String broken = "self.name + " + i + " +";
			assertThrows(OclParseException.class, () -> engine.parse(broken, personClass));
		}

		assertEquals(2, cache.failureCount(), "no more failures than the limit");
	}

	@Test
	@DisplayName("a flood of broken expressions does not displace one that works")
	void failuresDoNotEvictParses() throws OclParseException {
		CountingParser parser = new CountingParser();
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(1, 4);
		OclEngine engine = OclEngines.create(parser, cache);

		OclExpression good = engine.parse("self.name", personClass);
		for (int i = 0; i < 100; i++) {
			String broken = "self.name + " + i + " +";
			assertThrows(OclParseException.class, () -> engine.parse(broken, personClass));
		}

		assertSame(good, engine.parse("self.name", personClass), "still cached");
		assertEquals(101, parser.calls.get());
	}

	@Test
	@DisplayName("a failure limit of 0 keeps no failure")
	void zeroFailuresKeepsNone() {
		CountingParser parser = new CountingParser();
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16, 0);
		OclEngine engine = OclEngines.create(parser, cache);

		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));
		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));

		assertEquals(0, cache.failureCount());
		assertEquals(2, parser.calls.get());
	}

	@Test
	@DisplayName("a negative limit is refused")
	void negativeFailureLimitIsRefused() {
		assertThrows(IllegalArgumentException.class, () -> OclLruExpressionCache.ofSize(16, -1));
	}

	@Test
	@DisplayName("a success stored for a failed key replaces the failure")
	void successReplacesFailure() {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclCacheKey key = OclCacheKey.of("self.name", personClass);
		OclExpression parsed = OclFactory.eINSTANCE.createBooleanLiteralExp();

		cache.store(key, new OclParseResult.Failed("broken", List.of()));
		cache.store(key, new OclParseResult.Parsed(parsed));

		assertEquals(0, cache.failureCount());
		assertSame(parsed, cache.get("self.name", personClass));
	}

	// --- variables ---

	@Test
	@DisplayName("a parse with variables is cached")
	void parseWithVariablesIsCached() throws OclParseException {
		CountingParser parser = new CountingParser();
		OclEngine engine = OclEngines.create(parser, OclLruExpressionCache.ofSize(16));
		Map<String, EClassifier> variables = Map.of("limit", EcorePackage.Literals.EINT);

		OclExpression first = engine.parse("self.age > limit", personClass, variables);
		OclExpression second = engine.parse("self.age > limit", personClass, variables);

		assertSame(first, second);
		assertEquals(1, parser.calls.get());
	}

	@Test
	@DisplayName("the order in which variables are declared does not matter")
	void variableOrderDoesNotMatter() throws OclParseException {
		CountingParser parser = new CountingParser();
		OclEngine engine = OclEngines.create(parser, OclLruExpressionCache.ofSize(16));
		Map<String, EClassifier> ab = new LinkedHashMap<>();
		ab.put("a", EcorePackage.Literals.EINT);
		ab.put("b", EcorePackage.Literals.ESTRING);
		Map<String, EClassifier> ba = new LinkedHashMap<>();
		ba.put("b", EcorePackage.Literals.ESTRING);
		ba.put("a", EcorePackage.Literals.EINT);

		OclExpression first = engine.parse("self.age > a", personClass, ab);
		OclExpression second = engine.parse("self.age > a", personClass, ba);

		assertSame(first, second);
		assertEquals(1, parser.calls.get());
	}

	@Test
	@DisplayName("other variable types, other names or none at all are other entries")
	void variablesArePartOfTheKey() throws OclParseException {
		CountingParser parser = new CountingParser();
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(16);
		OclEngine engine = OclEngines.create(parser, cache);

		engine.parse("self.age > 1", personClass, Map.of("limit", EcorePackage.Literals.EINT));
		engine.parse("self.age > 1", personClass, Map.of("limit", EcorePackage.Literals.ESTRING));
		engine.parse("self.age > 1", personClass, Map.of("other", EcorePackage.Literals.EINT));
		engine.parse("self.age > 1", personClass);

		assertEquals(4, cache.size());
		assertEquals(4, parser.calls.get());
	}

	@Test
	@DisplayName("a parse with variables that fails is remembered too")
	void failureWithVariablesIsRemembered() {
		CountingParser parser = new CountingParser();
		OclEngine engine = OclEngines.create(parser, OclLruExpressionCache.ofSize(16));
		Map<String, EClassifier> variables = Map.of("limit", EcorePackage.Literals.EINT);

		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass, variables));
		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass, variables));

		assertEquals(1, parser.calls.get());
	}

	// --- SPI compatibility ---

	@Test
	@DisplayName("a cache written against get/put keeps working and caches what it did before")
	void legacyCacheKeepsWorking() throws OclParseException {
		CountingParser parser = new CountingParser();
		LegacyCache cache = new LegacyCache();
		OclEngine engine = OclEngines.create(parser, cache);

		OclExpression first = engine.parse("self.name", personClass);
		assertSame(first, engine.parse("self.name", personClass), "successes still cached");
		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));
		assertThrows(OclParseException.class, () -> engine.parse(BROKEN, personClass));
		Map<String, EClassifier> variables = Map.of("limit", EcorePackage.Literals.EINT);
		engine.parse("self.age > limit", personClass, variables);
		engine.parse("self.age > limit", personClass, variables);

		assertEquals(5, parser.calls.get(), "1 success, 2 failures, 2 parses with variables");
		assertEquals(1, cache.entries.size(), "only the success without variables reached put");
	}

	// --- key ---

	@Test
	@DisplayName("the key sorts its variables and writes unknown types as ?")
	void keyText() {
		Map<String, EClassifier> variables = new LinkedHashMap<>();
		variables.put("b", null);
		variables.put("a", companyClass);
		OclCacheKey key = OclCacheKey.of("x", personClass, variables);

		assertTrue(key.hasVariables());
		assertEquals("a:" + companyClass.getEPackage().getNsURI() + "#Company,b:?", key.variablesText());
		Map<String, EClassifier> reversed = new LinkedHashMap<>();
		reversed.put("a", companyClass);
		reversed.put("b", null);
		assertEquals(key, OclCacheKey.of("x", personClass, reversed), "declaration order does not matter");
		assertEquals("y", key.withExpression("y").expression());
		assertEquals(key.variables(), key.withExpression("y").variables());
		assertFalse(OclCacheKey.of("x", personClass).hasVariables());
	}

	// --- helpers ---

	/** Counts the parses that reach the real parser. */
	private static final class CountingParser implements OclExpressionParser {

		private final OclParserSupport delegate = new OclParserSupport();
		private final AtomicInteger calls = new AtomicInteger();

		@Override
		public OclExpression parse(String expression, EClassifier contextType) throws OclParseException {
			calls.incrementAndGet();
			return delegate.parse(expression, contextType);
		}

		@Override
		public OclExpression parse(String expression, EClassifier contextType, Map<String, EClassifier> variables)
				throws OclParseException {
			calls.incrementAndGet();
			return delegate.parse(expression, contextType, variables);
		}

		@Override
		public List<Constraint> parseDocument(String oclDocument) throws OclParseException {
			return delegate.parseDocument(oclDocument);
		}
	}

	/** A cache that implements only the original methods, as one written before #420 does. */
	private static final class LegacyCache implements OclExpressionCache {

		private final Map<String, OclExpression> entries = new LinkedHashMap<>();

		@Override
		public OclExpression get(String expression, EClassifier contextType) {
			return entries.get(contextType.getName() + "#" + expression);
		}

		@Override
		public void put(String expression, EClassifier contextType, OclExpression parsed) {
			entries.put(contextType.getName() + "#" + expression, parsed);
		}

		@Override
		public void invalidate(String expression, EClassifier contextType) {
			entries.remove(contextType.getName() + "#" + expression);
		}

		@Override
		public void invalidateAll() {
			entries.clear();
		}

		@Override
		public long size() {
			return entries.size();
		}

		@Override
		public long hitCount() {
			return 0;
		}

		@Override
		public long missCount() {
			return 0;
		}
	}
}
