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
package org.eclipse.fennec.m2x.ocl.fingerprint;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.emf.osgi.fingerprint.util.FingerprintHelper;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Two versions of one model do not share a compiled expression.
 *
 * <p>The cache that ships with the engine keys by nsURI, which names a model but not a
 * version of it. Two versions live at once — what the fingerprint work exists for — then
 * share an entry, and the one compiled first decides what the other gets. Measured on the
 * engine before this existed:
 *
 * <pre>
 * self.oclIsKindOf(Book) on v1 = true
 * self.oclIsKindOf(Book) on v2 = false   ← the same class, called not itself
 * </pre>
 *
 * <p>Navigation survives such a mix-up by accident, because feature access falls back to
 * resolving by name on the runtime class. An expression naming a type has no such fallback,
 * which is why that is what the test asks about.
 *
 * <p>The engines here share one cache on purpose: under OSGi they do, since the expression
 * cache is a singleton service bound to every engine.
 */
class FingerprintExpressionCacheTest {

	private static final String NS_URI = "http://example.org/m2x/fingerprint/1.0";

	private static final String EXPRESSION = "self.oclIsKindOf(Book)";

	@Test
	@DisplayName("a book of the second version is still a Book")
	void twoVersionsDoNotShareOneCompiledExpression() throws Exception {
		EPackage v1 = bookPackage(false);
		EPackage v2 = bookPackage(true);
		OclExpressionCache shared = fingerprintCache();

		OclEngine first = engine(v1, shared);
		OclEngine second = engine(v2, shared);

		assertEquals(Boolean.TRUE, evaluate(first, v1), "a v1 book is a v1 Book");
		assertEquals(Boolean.TRUE, evaluate(second, v2),
				"a v2 book is a v2 Book — unless the cached expression came from v1");
	}

	@Test
	@DisplayName("and the other way round, so neither order hides it")
	void theOrderDoesNotMatter() throws Exception {
		EPackage v1 = bookPackage(false);
		EPackage v2 = bookPackage(true);
		OclExpressionCache shared = fingerprintCache();

		assertEquals(Boolean.TRUE, evaluate(engine(v2, shared), v2));
		assertEquals(Boolean.TRUE, evaluate(engine(v1, shared), v1));
	}

	@Test
	@DisplayName("the same content is still one entry — this is not identity in disguise")
	void equalContentSharesAnEntry() throws Exception {
		// Two loads of the same model are the same version, and reusing what was compiled
		// for one is the whole point of caching. A key based on package identity could not
		// tell this apart from the case above.
		EPackage once = bookPackage(false);
		EPackage twice = bookPackage(false);
		OclExpressionCache shared = fingerprintCache();

		evaluate(engine(once, shared), once);
		long afterFirst = shared.size();
		evaluate(engine(twice, shared), twice);

		assertEquals(afterFirst, shared.size(), "equal content, one entry");
		assertTrue(shared.hitCount() > 0, "and the second engine hit it");
	}

	@Test
	@DisplayName("differing content gives differing keys")
	void differingContentGivesDifferingFingerprints() {
		assertNotEquals(
				FingerprintHelper.getDefaultFingerprintService().fingerprint(bookPackage(false), "ocl"),
				FingerprintHelper.getDefaultFingerprintService().fingerprint(bookPackage(true), "ocl"),
				"one added attribute is a different model version");
	}

	// --- helpers ---

	private static OclExpressionCache fingerprintCache() {
		return new FingerprintExpressionCache(OclLruExpressionCache.ofSize(64),
				FingerprintHelper.getDefaultFingerprintService());
	}

	/** {@code Book { pages }}, and with {@code chapters} on top for the second version. */
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

	private static OclEngine engine(EPackage ePackage, OclExpressionCache cache) {
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(NS_URI, ePackage);
		return OclEngines.create(OclConfiguration.builder(new OclParserSupport(registry))
				.expressionCache(cache)
				.build());
	}

	private static Object evaluate(OclEngine engine, EPackage ePackage) throws Exception {
		EClass book = (EClass) ePackage.getEClassifier("Book");
		EObject instance = EcoreUtil.create(book);
		return engine.evaluate(EXPRESSION, OclContext.of(instance));
	}
}
