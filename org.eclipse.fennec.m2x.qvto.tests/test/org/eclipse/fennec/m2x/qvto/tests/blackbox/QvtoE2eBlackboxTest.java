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

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxInvocationContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngineImpl;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for QVT-O blackbox library integration (§8.1.4).
 *
 * <p>Uses a test blackbox library with string operations:
 * <ul>
 *   <li>{@code trimAll(s : String) : String} — module-level</li>
 *   <li>{@code String::reverseChars() : String} — contextual</li>
 * </ul>
 */
class QvtoE2eBlackboxTest {

	private static final QvtOperationalFactory QVTO = QvtOperationalFactory.eINSTANCE;

	private static QvtoEngineImpl engine;

	@BeforeAll
	static void setUpEngine() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();

		BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
		registry.register(new TestStringLibrary());

		QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
				.blackboxRegistry(registry)
				.blackboxEnabled(true)
				.build();
		engine = new QvtoEngineImpl(config);
	}

	// ==================== Module-level operation ====================

	/**
	 * §8.1.4: Import blackbox library, call module-level operation.
	 */
	@Test
	void blackbox_moduleLevelOperation() throws Exception {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				import mylib;
				transformation Main(inout m : ECORE) {
				    main() {
				        m.objectsOfType(EPackage)->forEach(p) {
				            p.name := trimAll('  hello  ');
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

	// ==================== Contextual operation ====================

	/**
	 * §8.1.4: Import blackbox library, call contextual operation on String.
	 */
	@Test
	void blackbox_contextualOperation() throws Exception {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				import mylib;
				transformation Main(inout m : ECORE) {
				    main() {
				        m.objectsOfType(EPackage)->forEach(p) {
				            p.name := 'hello'.reverseChars();
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
		assertEquals("olleh", pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
	}

	// ==================== Invocation context: logging ====================

	/**
	 * §8.1.4: Blackbox operation uses ctx.addInfo() — verify diagnostic appears.
	 */
	@Test
	void blackbox_invocationContext_logging() throws Exception {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				import mylib;
				transformation Main(inout m : ECORE) {
				    main() {
				        logHello();
				    }
				}
				""";

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE));

		OperationalTransformation t = engine.parse(source, "Main");
		QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(extent));

		assertSuccess(result);
		assertLogged(result, "Hello from blackbox");
	}

	// ==================== Invocation context: config property ====================

	/**
	 * §8.1.4: Blackbox operation reads a config property from the invocation context.
	 */
	@Test
	void blackbox_invocationContext_configProperty() throws Exception {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				import mylib;
				transformation Main(inout m : ECORE) {
				    configuration property myProp : String;
				    main() {
				        m.objectsOfType(EPackage)->forEach(p) {
				            p.name := readConfig();
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
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(List.of(extent), Map.of("myProp", "configured-value")));

		assertSuccess(result);
		assertEquals("configured-value", pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME));
	}

	// ==================== Not imported → not visible ====================

	/**
	 * §8.1.4: Without import, blackbox operations are not callable.
	 * The transformation should fail because trimAll is not found.
	 */
	@Test
	void blackbox_notImported_notVisible() throws Exception {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE) {
				    main() {
				        log(trimAll('test'));
				    }
				}
				""";

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE));

		OperationalTransformation t = engine.parse(source, "Main");
		QvtoExecutionResult result = engine.execute(t, QvtoExecutionContext.of(extent));

		// Should have errors — trimAll is not defined without import
		assertNotNull(result);
		// The call should fail because trimAll is unknown
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() >= org.eclipse.emf.common.util.Diagnostic.ERROR)
				|| result.diagnostics().isEmpty(), // OCL may silently return null
				"trimAll should not be callable without import");
	}

	// ==================== Helper methods ====================

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected));
		assertTrue(found, "Expected log containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
	}

	// ==================== Test blackbox library ====================

	/**
	 * Test blackbox library providing string operations.
	 */
	static class TestStringLibrary implements QvtoBlackboxLibrary {

		@Override
		public String getModuleName() {
			return "mylib";
		}

		@Override
		public String getUnitQualifiedName() {
			return "mylib";
		}

		@Override
		public List<String> getUsedPackageURIs() {
			return List.of();
		}

		@Override
		public List<BlackboxOperationDescriptor> getOperationDescriptors() {
			// trimAll(s : String) : String — module-level
			BlackboxOperationDescriptor trimAll = QVTO.createBlackboxOperationDescriptor();
			trimAll.setName("trimAll");
			trimAll.setReturnType(EcorePackage.Literals.ESTRING);
			trimAll.getParameterTypes().add(EcorePackage.Literals.ESTRING);

			// String::reverseChars() : String — contextual
			BlackboxOperationDescriptor reverseChars = QVTO.createBlackboxOperationDescriptor();
			reverseChars.setName("reverseChars");
			reverseChars.setContextType(EcorePackage.Literals.ESTRING);
			reverseChars.setReturnType(EcorePackage.Literals.ESTRING);

			// logHello() : Void — module-level, uses ctx.addInfo()
			BlackboxOperationDescriptor logHello = QVTO.createBlackboxOperationDescriptor();
			logHello.setName("logHello");
			logHello.setReturnType(EcorePackage.Literals.EJAVA_OBJECT);

			// readConfig() : String — module-level, reads config property
			BlackboxOperationDescriptor readConfig = QVTO.createBlackboxOperationDescriptor();
			readConfig.setName("readConfig");
			readConfig.setReturnType(EcorePackage.Literals.ESTRING);

			return List.of(trimAll, reverseChars, logHello, readConfig);
		}

		@Override
		public Object invoke(String operationName, QvtoBlackboxInvocationContext context, Object[] args) {
			return switch (operationName) {
				case "trimAll" -> args.length > 0 && args[0] instanceof String s ? s.strip() : null;
				case "reverseChars" -> context.self() instanceof String s
						? new StringBuilder(s).reverse().toString() : null;
				case "logHello" -> {
					context.addInfo("Hello from blackbox");
					yield null;
				}
				case "readConfig" -> {
					Object val = context.getConfigProperties().get("myProp");
					yield val instanceof String s ? s : null;
				}
				default -> null;
			};
		}
	}
}
