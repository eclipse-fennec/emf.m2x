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
package org.eclipse.fennec.m2x.ocl.metadata;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryEntry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryWriter;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistries;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Compiled expressions live in a named registry, anchored to their model version.
 *
 * <p>An entry carries {@code emf.nsURI} and {@code emf.fingerprint} — the anchoring the
 * registry guide describes — so the metadata bridge can put it on the tree of every live
 * version, including one that registers later. The registry stays the read face here,
 * because a class carries at most one aspect per type id while an OCL context type has as
 * many expressions as someone writes.
 */
class RegistryExpressionCacheTest {

	private static final String NS_URI = "http://example.org/m2x/registry-cache/1.0";

	private static final String EXPRESSION = "self.pages > 100";

	private EObjectRegistryWriter writer;

	@BeforeEach
	void setUp() {
		writer = EObjectRegistries.createRegistry("ocl-compiled-test");
	}

	@Test
	@DisplayName("a compiled expression is filed, anchored to its model version")
	void compiledExpressionIsAnchored() throws Exception {
		EPackage ePackage = bookPackage(false);
		engine(ePackage, cache()).parse(EXPRESSION, bookOf(ePackage));

		EObjectRegistryEntry entry = writer.getRegistry().entries().iterator().next();

		assertEquals(NS_URI, entry.properties().get(RegistryExpressionCache.PROP_NS_URI));
		assertEquals(FingerprintHelper.getDefaultFingerprintService().fingerprint(ePackage, "ocl"),
				entry.properties().get(RegistryExpressionCache.PROP_FINGERPRINT),
				"the fingerprint is what says which version this belongs to");
	}

	@Test
	@DisplayName("the second ask comes back from the registry")
	void theSecondAskIsAHit() throws Exception {
		EPackage ePackage = bookPackage(false);
		RegistryExpressionCache cache = cache();
		OclEngine engine = engine(ePackage, cache);

		OclExpression first = engine.parse(EXPRESSION, bookOf(ePackage));
		OclExpression second = engine.parse(EXPRESSION, bookOf(ePackage));

		assertSame(first, second);
		assertTrue(cache.hitCount() > 0);
	}

	@Test
	@DisplayName("two versions of one model do not answer for each other")
	void versionsAreKeptApart() throws Exception {
		EPackage v1 = bookPackage(false);
		EPackage v2 = bookPackage(true);
		RegistryExpressionCache shared = cache();

		OclExpression fromV1 = engine(v1, shared).parse(EXPRESSION, bookOf(v1));
		OclExpression fromV2 = engine(v2, shared).parse(EXPRESSION, bookOf(v2));

		assertNotEquals(fromV1, fromV2, "same nsURI, different version, different entry");
		assertEquals(2, writer.getRegistry().entries().size());
	}

	@Test
	@DisplayName("a context type with no package has no version to anchor to")
	void withoutAPackageTheDelegateTakesIt() {
		RegistryExpressionCache cache = cache();
		EClass loose = EcoreFactory.eINSTANCE.createEClass();
		loose.setName("Loose");
		OclExpression parsed =
				org.eclipse.fennec.m2x.model.ocl.OclFactory.eINSTANCE.createBooleanLiteralExp();

		cache.put("self.x", loose, parsed);

		assertSame(parsed, cache.get("self.x", loose));
		assertTrue(writer.getRegistry().entries().isEmpty(), "nothing to anchor, nothing filed");
	}

	@Test
	@DisplayName("invalidating removes only what this cache wrote")
	void invalidateIsScopedToItsOwnSource() throws Exception {
		EPackage ePackage = bookPackage(false);
		RegistryExpressionCache cache = cache();
		engine(ePackage, cache).parse(EXPRESSION, bookOf(ePackage));
		writer.put("someone-else", "foreign", EcoreFactory.eINSTANCE.createEAttribute(),
				java.util.Map.of());

		cache.invalidateAll();

		assertNull(cache.get(EXPRESSION, bookOf(ePackage)));
		assertEquals(1, writer.getRegistry().entries().size(),
				"the other source's entry is none of this cache's business");
	}

	// --- helpers ---

	private RegistryExpressionCache cache() {
		return new RegistryExpressionCache(writer,
				FingerprintHelper.getDefaultFingerprintService(), OclLruExpressionCache.ofSize(16));
	}

	private static EClass bookOf(EPackage ePackage) {
		return (EClass) ePackage.getEClassifier("Book");
	}

	private static OclEngine engine(EPackage ePackage, RegistryExpressionCache cache) {
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(NS_URI, ePackage);
		return OclEngines.create(OclConfiguration.builder(new OclParserSupport(registry))
				.expressionCache(cache)
				.build());
	}

	private static EPackage bookPackage(boolean withChapters) {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("bookshelf");
		ePackage.setNsURI(NS_URI);
		ePackage.setNsPrefix("bookshelf");

		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		book.getEStructuralFeatures().add(attribute("pages"));
		if (withChapters) {
			book.getEStructuralFeatures().add(attribute("chapters"));
		}
		ePackage.getEClassifiers().add(book);
		return ePackage;
	}

	private static EAttribute attribute(String name) {
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(name);
		attribute.setEType(EcorePackage.Literals.EINT);
		return attribute;
	}
}
