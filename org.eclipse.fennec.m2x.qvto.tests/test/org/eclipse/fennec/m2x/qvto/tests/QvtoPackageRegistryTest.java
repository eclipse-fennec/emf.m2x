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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngineImpl;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Metamodel resolution against an explicitly supplied {@link EPackage.Registry} (D42).
 *
 * <p>The metamodel used here is never registered globally unless a test says so, so a
 * {@code modeltype … uses '<nsURI>'} declaration can only resolve through a registry the
 * caller handed to the engine.
 */
class QvtoPackageRegistryTest {

	private static final String NS_URI = "http://example.org/m2x/qvto-registry/1.0";

	private static final String TRANSFORMATION = """
			modeltype LIB uses '%s';
			transformation registryTest(in l : LIB, out o : LIB) {
			    main() {
			    }
			}
			""".formatted(NS_URI);

	private EPackage libraryPackage;
	private EPackage.Registry registry;

	@BeforeEach
	void setUp() {
		libraryPackage = EcoreFactory.eINSTANCE.createEPackage();
		libraryPackage.setName("library");
		libraryPackage.setNsURI(NS_URI);
		libraryPackage.setNsPrefix("library");

		EClass bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		libraryPackage.getEClassifiers().add(bookClass);

		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, libraryPackage);
	}

	@AfterEach
	void tearDown() {
		EPackage.Registry.INSTANCE.remove(NS_URI);
	}

	@Test
	@DisplayName("a modeltype resolves through the registry supplied to the engine")
	void modeltypeResolvesThroughSuppliedRegistry() throws QvtoParseException {
		QvtoEngineImpl engine = engineWith(builder -> builder.packageRegistry(registry));

		OperationalTransformation transformation = engine.parse(TRANSFORMATION, "registryTest");

		assertNotNull(transformation);
		assertEquals(libraryPackage, resolvedMetamodel(transformation),
				"the engine must resolve the modeltype against the supplied registry");
	}

	@Test
	@DisplayName("without a configured registry the static one applies")
	void staticRegistryAppliesWhenNoneConfigured() throws QvtoParseException {
		EPackage.Registry.INSTANCE.put(NS_URI, libraryPackage);

		QvtoEngineImpl engine = engineWith(builder -> builder);

		OperationalTransformation transformation = engine.parse(TRANSFORMATION, "registryTest");

		assertNotNull(transformation);
		assertEquals(libraryPackage, resolvedMetamodel(transformation));
	}

	@Test
	@Disabled("#63 — an unresolvable modeltype nsURI is silently dropped instead of reported")
	@DisplayName("an unknown metamodel URI is reported, not silently ignored")
	void unknownMetamodelUriFails() {
		QvtoEngineImpl engine = engineWith(builder -> builder);

		assertThrows(QvtoParseException.class,
				() -> engine.parse(TRANSFORMATION, "registryTest"),
				"neither the supplied nor the static registry knows this nsURI");
	}

	// --- helpers ---

	private QvtoEngineImpl engineWith(
			java.util.function.UnaryOperator<QvtoConfiguration.Builder> customizer) {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		return new QvtoEngineImpl(customizer.apply(QvtoConfiguration.builder(oclConfig)).build());
	}

	/**
	 * Returns the {@link EPackage} the transformation's single modeltype resolved to.
	 */
	private EPackage resolvedMetamodel(OperationalTransformation transformation) {
		return transformation.getModelParameter().stream()
				.map(parameter -> parameter.getEType())
				.filter(ModelType.class::isInstance)
				.map(ModelType.class::cast)
				.flatMap(modelType -> modelType.getMetamodel().stream())
				.findFirst()
				.orElse(null);
	}
}
