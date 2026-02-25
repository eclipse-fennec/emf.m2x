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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for {@link OclEngineImpl#warmUp(EPackage)}.
 *
 * <p>Verifies that warm-up pre-populates:
 * <ul>
 *   <li>Property accessor cache (LambdaMetafactory accessors for generated models)</li>
 *   <li>Expression parse cache (OCL annotations on features, operations, classes)</li>
 * </ul>
 */
class OclWarmUpTest {

	private static final String DELEGATE_URI = "http://www.eclipse.org/fennec/m2x/ocl/1.0";

	static OclParserSupport parser;
	static EcoreHelper ecoreHelper;
	static EPackage companyPackage;

	@BeforeAll
	static void setUp() throws IOException {
		parser = new OclParserSupport();
		ecoreHelper = new EcoreHelper(OclWarmUpTest.class);
		companyPackage = ecoreHelper.loadEcore("company.ecore");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	// --- Accessor cache warm-up ---

	@Test
	void warmUp_populatesAccessorCache_companyPackage() {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(256);
		OclEngineImpl engine = new OclEngineImpl(parser, cache);

		// warmUp should not throw
		assertDoesNotThrow(() -> engine.warmUp(companyPackage));

		// After warm-up, evaluation of self.name should work (accessor already cached)
		EClass personClass = ecoreHelper.getEClass(companyPackage, "Person");
		EObject person = companyPackage.getEFactoryInstance().create(personClass);
		person.eSet(personClass.getEStructuralFeature("name"), "Alice");

		Object result = assertDoesNotThrow(
				() -> engine.evaluate("self.name", OclContext.of(person)));
		assertEquals("Alice", result);
	}

	@Test
	void warmUp_skipsAbstractClasses() {
		// Create a package with an abstract class
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("test");
		pkg.setNsURI("http://test/warmup/abstract");
		pkg.setNsPrefix("test");

		EClass abstractClass = EcoreFactory.eINSTANCE.createEClass();
		abstractClass.setName("AbstractBase");
		abstractClass.setAbstract(true);
		EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		abstractClass.getEStructuralFeatures().add(nameAttr);
		pkg.getEClassifiers().add(abstractClass);

		OclEngineImpl engine = new OclEngineImpl(parser);

		// Should not throw (abstract classes are skipped)
		assertDoesNotThrow(() -> engine.warmUp(pkg));
	}

	// --- Expression parse cache warm-up ---

	@Test
	void warmUp_preParsesDerivationAnnotations() {
		EPackage pkg = createPackageWithDerivedFeature();
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(256);
		OclEngineImpl engine = new OclEngineImpl(parser, cache);

		engine.warmUp(pkg);

		// The derivation expression should be in the cache
		assertTrue(cache.size() > 0, "Cache should contain pre-parsed derivation expression");
	}

	@Test
	void warmUp_preParsesConstraintAnnotations() {
		EPackage pkg = createPackageWithConstraint();
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(256);
		OclEngineImpl engine = new OclEngineImpl(parser, cache);

		engine.warmUp(pkg);

		// The constraint expression should be in the cache
		assertTrue(cache.size() > 0, "Cache should contain pre-parsed constraint expression");
	}

	@Test
	void warmUp_preParsesOperationBodyAnnotations() {
		EPackage pkg = createPackageWithOperationBody();
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(256);
		OclEngineImpl engine = new OclEngineImpl(parser, cache);

		engine.warmUp(pkg);

		assertTrue(cache.size() > 0, "Cache should contain pre-parsed body expression");
	}

	@Test
	void warmUp_withoutCache_onlyWarmsAccessors() {
		// Engine without expression cache — should still work (accessor warmup only)
		OclEngineImpl engine = new OclEngineImpl(parser);
		assertDoesNotThrow(() -> engine.warmUp(companyPackage));
	}

	@Test
	void warmUp_ignoresParseErrors() {
		// Create a package with an invalid OCL expression
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("broken");
		pkg.setNsURI("http://test/warmup/broken");
		pkg.setNsPrefix("broken");

		EClass cls = EcoreFactory.eINSTANCE.createEClass();
		cls.setName("Broken");
		EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		cls.getEStructuralFeatures().add(nameAttr);

		// Add invalid OCL annotation
		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(DELEGATE_URI);
		ann.getDetails().put("derivation", "self.???invalid");
		nameAttr.getEAnnotations().add(ann);

		pkg.getEClassifiers().add(cls);

		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(256);
		OclEngineImpl engine = new OclEngineImpl(parser, cache);

		// Should not throw even with invalid OCL
		assertDoesNotThrow(() -> engine.warmUp(pkg));
	}

	@Test
	void warmUp_multiplePackages() {
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(256);
		OclEngineImpl engine = new OclEngineImpl(parser, cache);

		EPackage pkg1 = createPackageWithDerivedFeature();
		EPackage pkg2 = createPackageWithConstraint();

		engine.warmUp(pkg1);
		long sizeAfterFirst = cache.size();
		assertTrue(sizeAfterFirst > 0);

		engine.warmUp(pkg2);
		assertTrue(cache.size() > sizeAfterFirst, "Second warmUp should add more entries");
	}

	@Test
	void warmUp_cachedExpressionsUsedByEvaluation() {
		EPackage pkg = createPackageWithDerivedFeature();
		OclLruExpressionCache cache = OclLruExpressionCache.ofSize(256);
		OclEngineImpl engine = new OclEngineImpl(parser, cache);

		engine.warmUp(pkg);
		long missesAfterWarmup = cache.missCount();

		// Now parse the same expression — should be a cache hit
		EClass cls = (EClass) pkg.getEClassifier("Item");
		assertDoesNotThrow(() -> engine.parse("self.name.size()", cls));

		assertEquals(missesAfterWarmup, cache.missCount(),
				"No additional cache misses expected after warmUp");
	}

	// --- Helpers ---

	private static EPackage createPackageWithDerivedFeature() {
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("derived");
		pkg.setNsURI("http://test/warmup/derived");
		pkg.setNsPrefix("derived");

		EClass cls = EcoreFactory.eINSTANCE.createEClass();
		cls.setName("Item");

		EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		cls.getEStructuralFeatures().add(nameAttr);

		EAttribute derivedAttr = EcoreFactory.eINSTANCE.createEAttribute();
		derivedAttr.setName("nameLength");
		derivedAttr.setEType(EcorePackage.Literals.EINT);
		derivedAttr.setDerived(true);
		derivedAttr.setTransient(true);
		derivedAttr.setVolatile(true);

		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(DELEGATE_URI);
		ann.getDetails().put("derivation", "self.name.size()");
		derivedAttr.getEAnnotations().add(ann);

		cls.getEStructuralFeatures().add(derivedAttr);
		pkg.getEClassifiers().add(cls);

		return pkg;
	}

	private static EPackage createPackageWithConstraint() {
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("constrained");
		pkg.setNsURI("http://test/warmup/constrained");
		pkg.setNsPrefix("constrained");

		EClass cls = EcoreFactory.eINSTANCE.createEClass();
		cls.setName("Employee");

		EAttribute nameAttr = EcoreFactory.eINSTANCE.createEAttribute();
		nameAttr.setName("name");
		nameAttr.setEType(EcorePackage.Literals.ESTRING);
		cls.getEStructuralFeatures().add(nameAttr);

		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(DELEGATE_URI);
		ann.getDetails().put("nameNotEmpty", "self.name.size() > 0");
		cls.getEAnnotations().add(ann);

		pkg.getEClassifiers().add(cls);
		return pkg;
	}

	private static EPackage createPackageWithOperationBody() {
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("ops");
		pkg.setNsURI("http://test/warmup/ops");
		pkg.setNsPrefix("ops");

		EClass cls = EcoreFactory.eINSTANCE.createEClass();
		cls.setName("Calculator");

		EAttribute valueAttr = EcoreFactory.eINSTANCE.createEAttribute();
		valueAttr.setName("value");
		valueAttr.setEType(EcorePackage.Literals.EINT);
		cls.getEStructuralFeatures().add(valueAttr);

		org.eclipse.emf.ecore.EOperation op = EcoreFactory.eINSTANCE.createEOperation();
		op.setName("doubled");
		op.setEType(EcorePackage.Literals.EINT);

		EAnnotation ann = EcoreFactory.eINSTANCE.createEAnnotation();
		ann.setSource(DELEGATE_URI);
		ann.getDetails().put("body", "self.value * 2");
		op.getEAnnotations().add(ann);

		cls.getEOperations().add(op);
		pkg.getEClassifiers().add(cls);
		return pkg;
	}
}
