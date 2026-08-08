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

import java.util.function.UnaryOperator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngineImpl;
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
		QvtdEngineImpl engine = engineWith(builder -> builder.packageRegistry(registry));

		RelationalTransformation transformation = engine.parse(TRANSFORMATION, "registryTest");

		assertNotNull(transformation);
		assertEquals(bookshelfPackage, usedPackage(transformation),
				"the engine must resolve the typed model against the supplied registry");
	}

	@Test
	@DisplayName("without a configured registry the static one applies")
	void staticRegistryAppliesWhenNoneConfigured() throws QvtdParseException {
		EPackage.Registry.INSTANCE.put(NS_URI, bookshelfPackage);

		QvtdEngineImpl engine = engineWith(builder -> builder);

		RelationalTransformation transformation = engine.parse(TRANSFORMATION, "registryTest");

		assertNotNull(transformation);
		assertEquals(bookshelfPackage, usedPackage(transformation));
	}

	// --- helpers ---

	private QvtdEngineImpl engineWith(UnaryOperator<QvtdConfiguration.Builder> customizer) {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		return new QvtdEngineImpl(customizer.apply(QvtdConfiguration.builder(oclConfig)).build());
	}

	/**
	 * Returns the {@link EPackage} the transformation's typed models resolved to.
	 */
	private EPackage usedPackage(RelationalTransformation transformation) {
		return transformation.getModelParameter().stream()
				.map(TypedModel::getUsedPackage)
				.flatMap(java.util.List::stream)
				.findFirst()
				.orElse(null);
	}
}
