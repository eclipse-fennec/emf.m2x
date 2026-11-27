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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.function.UnaryOperator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Metamodel resolution against an explicitly supplied {@link EPackage.Registry} (D42).
 *
 * <p>The metamodel used here is never registered globally unless a test says so, so the
 * typed model {@code bookshelf} of the transformation can only resolve through a registry
 * the caller handed to the engine.
 */
class QvtdPackageRegistryTest {

	private static final String NS_URI = "http://example.org/m2x/qvtr-registry/1.0";

	private static final String TRANSFORMATION = """
			transformation registryTest(source : bookshelf, target : bookshelf) {
			    top relation BookToBook {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = t };
			    }
			}
			""";

	private EPackage bookshelfPackage;
	private EPackage.Registry registry;

	@BeforeEach
	void setUp() {
		bookshelfPackage = EcoreFactory.eINSTANCE.createEPackage();
		bookshelfPackage.setName("bookshelf");
		bookshelfPackage.setNsURI(NS_URI);
		bookshelfPackage.setNsPrefix("bookshelf");

		EClass bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		bookshelfPackage.getEClassifiers().add(bookClass);

		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, bookshelfPackage);
	}

	@AfterEach
	void tearDown() {
		EPackage.Registry.INSTANCE.remove(NS_URI);
	}

	@Test
	@DisplayName("a typed model resolves through the registry supplied to the engine")
	void typedModelResolvesThroughSuppliedRegistry() throws QvtdParseException {
		QvtdEngine engine = engineWith(builder -> builder.packageRegistry(registry));

		RelationalTransformation transformation = engine.parse(TRANSFORMATION, "registryTest");

		assertNotNull(transformation);
		assertEquals(bookshelfPackage, usedPackage(transformation),
				"the engine must resolve the typed model against the supplied registry");
	}

	@Test
	@DisplayName("a resource set is enough — its package registry is used")
	void resourceSetSuppliesTheRegistry() throws QvtdParseException {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getPackageRegistry().put(NS_URI, bookshelfPackage);

		QvtdEngine engine = engineWith(builder -> builder.resourceSet(resourceSet));

		assertEquals(bookshelfPackage, usedPackage(engine.parse(TRANSFORMATION, "registryTest")));
	}

	@Test
	@DisplayName("without a configured registry the static one applies")
	void staticRegistryAppliesWhenNoneConfigured() throws QvtdParseException {
		EPackage.Registry.INSTANCE.put(NS_URI, bookshelfPackage);

		QvtdEngine engine = engineWith(builder -> builder);

		RelationalTransformation transformation = engine.parse(TRANSFORMATION, "registryTest");

		assertNotNull(transformation);
		assertEquals(bookshelfPackage, usedPackage(transformation));
	}

	@Test
	@DisplayName("an unknown metamodel is reported instead of silently unresolved")
	void unknownMetamodelIsReported() {
		QvtdEngine engine = engineWith(builder -> builder);

		QvtdParseException failure = assertThrows(QvtdParseException.class,
				() -> engine.parse(TRANSFORMATION, "registryTest"),
				"neither the supplied nor the static registry knows this metamodel");

		assertTrue(failure.getErrors().stream()
				.anyMatch(d -> d.getMessage().contains("Unknown metamodel (bookshelf)")),
				() -> "diagnostics: " + failure.getErrors());
	}

	@Test
	@DisplayName("#458: a package-qualified class resolves in its package, not by its simple name")
	void qualifiedClass_resolvesByItsQualifier() throws QvtdParseException {
		assertCopiedInto("b", """
				transformation T(s : a, t : b) {
				    top relation Copy {
				        l : String;
				        checkonly domain s x : a::Item { label = l };
				        enforce domain t y : b::Item { label = l };
				    }
				}
				""");
	}

	@Test
	@DisplayName("#458: an unqualified class resolves in the metamodel of its domain's typed model first")
	void unqualifiedClass_resolvesInItsDomainsMetamodelFirst() throws QvtdParseException {
		assertCopiedInto("b", """
				transformation T(s : a, t : b) {
				    top relation Copy {
				        l : String;
				        checkonly domain s x : Item { label = l };
				        enforce domain t y : Item { label = l };
				    }
				}
				""");
	}

	/** Runs a copy between two packages {@code a} and {@code b} that both have a class {@code Item}. */
	private void assertCopiedInto(String expectedPackage, String source) throws QvtdParseException {
		EPackage a = itemPackage("a");
		EPackage b = itemPackage("b");
		EPackage.Registry twoPackages = new EPackageRegistryImpl();
		twoPackages.put(a.getNsURI(), a);
		twoPackages.put(b.getNsURI(), b);
		QvtdEngine engine = engineWith(builder -> builder.packageRegistry(twoPackages));

		EClass sourceItem = (EClass) a.getEClassifier("Item");
		EObject item = a.getEFactoryInstance().create(sourceItem);
		item.eSet(sourceItem.getEStructuralFeature("label"), "x");
		QvtdModelExtent s = QvtdModelExtent.of(item);
		QvtdModelExtent t = QvtdModelExtent.of();

		QvtdExecutionResult result = engine.execute(engine.parse(source, "T"),
				QvtdExecutionContext.enforce("t", Map.of("s", s, "t", t)));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(1, t.getContents().size());
		EObject copy = t.getContents().get(0);
		assertEquals(expectedPackage, copy.eClass().getEPackage().getName());
		assertEquals("x", copy.eGet(copy.eClass().getEStructuralFeature("label")));
	}

	private static EPackage itemPackage(String name) {
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName(name);
		pkg.setNsURI("http://example.org/m2x/qvtr-458/" + name);
		pkg.setNsPrefix(name);
		EClass item = EcoreFactory.eINSTANCE.createEClass();
		item.setName("Item");
		EAttribute label = EcoreFactory.eINSTANCE.createEAttribute();
		label.setName("label");
		label.setEType(EcorePackage.Literals.ESTRING);
		item.getEStructuralFeatures().add(label);
		pkg.getEClassifiers().add(item);
		return pkg;
	}

	// --- helpers ---

	private QvtdEngine engineWith(UnaryOperator<QvtdConfiguration.Builder> customizer) {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		return QvtdEngines.create(customizer.apply(QvtdConfiguration.builder(oclConfig)).build());
	}

	/**
	 * Returns the {@link EPackage} the transformation's typed models resolved to.
	 */
	private EPackage usedPackage(RelationalTransformation transformation) {
		return transformation.getModelParameter().stream()
				.map(TypedModel::getUsedPackage)
				.flatMap(List::stream)
				.findFirst()
				.orElse(null);
	}
}
