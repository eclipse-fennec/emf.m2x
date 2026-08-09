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
package org.eclipse.fennec.m2x.qvto.tests.blackbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.URI;
import java.util.Optional;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for {@code from ... import} syntax (§8.4).
 *
 * <p>Tests selective import with both blackbox libraries and regular QVT-O modules.
 */
class QvtoE2eFromImportTest {

	private static QvtoEngine engine;

	@BeforeAll
	static void setUpEngine() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();

		// Blackbox registry with the test library
		BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
		registry.register(new QvtoE2eBlackboxTest.TestStringLibrary());

		// Unit resolver for a regular QVT-O library
		QvtoUnitResolver resolver = qualifiedName -> {
			if ("HelperLib".equals(qualifiedName)) {
				return Optional.of(new QvtoUnit.SourceUnit("HelperLib",
						URI.createURI("inline:HelperLib"), HELPER_LIB_SOURCE));
			}
			return Optional.empty();
		};

		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
				.blackboxRegistry(registry)
				.addUnitResolver(resolver)
				.blackboxEnabled(true)
				.unitResolverEnabled(true)
				.build();
		engine = QvtoEngines.create(config);
	}

	private static final String HELPER_LIB_SOURCE = """
			library HelperLib {
			    helper greet() : String {
			        return 'hello';
			    }
			    helper farewell() : String {
			        return 'goodbye';
			    }
			}
			""";

	// ==================== Selective import from blackbox ====================

	/**
	 * §8.4: from mylib import trimAll — only trimAll visible, reverseChars not.
	 */
	@Test
	void fromImport_selectiveNames_blackbox() throws Exception {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				from mylib import trimAll;
				transformation Main(inout m : ECORE) {
				    main() {
				        m.objectsOfType(EPackage)->forEach(p) {
				            p.name := trimAll('  selective  ');
				        };
				    }
				}
				""";

		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		OperationalTransformation t = engine.parse(source, "Main");
		QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertEquals("selective", pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
	}

	// ==================== Wildcard import from blackbox ====================

	/**
	 * §8.4: from mylib import * — all operations visible.
	 */
	@Test
	void fromImport_wildcard_blackbox() throws Exception {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				from mylib import *;
				transformation Main(inout m : ECORE) {
				    main() {
				        m.objectsOfType(EPackage)->forEach(p) {
				            p.name := trimAll('  wildcard  ');
				        };
				    }
				}
				""";

		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		OperationalTransformation t = engine.parse(source, "Main");
		QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertEquals("wildcard", pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
	}

	// ==================== Selective import from regular module ====================

	/**
	 * §8.4: from HelperLib import greet — only greet visible, farewell not.
	 */
	@Test
	void fromImport_selectiveNames_regularModule() throws Exception {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				from HelperLib import greet;
				transformation Main(inout m : ECORE) {
				    main() {
				        m.objectsOfType(EPackage)->forEach(p) {
				            p.name := greet();
				        };
				    }
				}
				""";

		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		OperationalTransformation t = engine.parse(source, "Main");
		QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertEquals("hello", pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
	}

	// ==================== Selective: non-named ops invisible ====================

	/**
	 * §8.4: from HelperLib import greet — farewell should NOT be callable.
	 * Calling farewell results in an error because the operation is not visible.
	 */
	@Test
	void fromImport_selective_onlyNamedVisible() throws Exception {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				from HelperLib import greet;
				transformation Main(inout m : ECORE) {
				    main() {
				        m.objectsOfType(EPackage)->forEach(p) {
				            p.name := farewell();
				        };
				    }
				}
				""";

		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);

		OperationalTransformation t = engine.parse(source, "Main");
		QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(extent));

		// farewell() is not imported — should produce an error diagnostic
		assertNotNull(result);
		boolean hasFarewellError = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("farewell"));
		assertTrue(hasFarewellError,
				"farewell() should not be visible; expected error diagnostic but got: "
						+ result.diagnostics());
	}

	// ==================== Helper methods ====================

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

}
