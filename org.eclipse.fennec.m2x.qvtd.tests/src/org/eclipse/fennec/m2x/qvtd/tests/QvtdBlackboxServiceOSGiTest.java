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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.annotation.require.RequireQVTD;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.Constants;
import org.osgi.framework.ServiceRegistration;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.Property.Scalar;
import org.osgi.test.common.annotation.Property.Type;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * A blackbox library published as a service reaches {@code DefaultQvtdEngine} (#269).
 *
 * <p>The engine binds {@code QvtdBlackboxLibrary} services through a dynamic {@code 0..n}
 * reference. Each test configures the engine with its own name in the allow-list and injects
 * the engine by that name, so it is handed the instance that carries its configuration.
 */
@RequireOCL
@RequireQVTD
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class QvtdBlackboxServiceOSGiTest {

	private static final String LIBRARY = "osgi.lib";

	/** Copies each package, its name passed through the blackbox query Answer. */
	private static final String TRANSFORMATION = """
			transformation T(source : ecore, target : ecore) {
				query Answer(n : String) : String;

				top relation R {
					n : String; m : String;
					checkonly domain source p : EPackage { name = n };
					enforce domain target q : EPackage { name = m };
					where { m = Answer(n); }
				}
			}
			""";

	@Test
	@DisplayName("a library registered as a service serves the transformation's query")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = {
			@Property(key = "qvtd.blackboxEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvtd.allowedBlackboxModules", value = { LIBRARY, "registered" }, type = Type.Array)
	})
	void registeredLibraryIsCalled(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvtd.allowedBlackboxModules=registered)") QvtdEngine engine)
			throws Exception {
		ServiceRegistration<QvtdBlackboxLibrary> registration = register(context, "from-service", 0);
		try {
			assertEquals("from-service", run(engine));
		} finally {
			registration.unregister();
		}
	}

	@Test
	@DisplayName("without qvtd.blackboxEnabled a registered library is not called")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = {
			@Property(key = "qvtd.allowedBlackboxModules", value = { LIBRARY, "disabled" }, type = Type.Array)
	})
	void disabledLeavesLibraryOutOfReach(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvtd.allowedBlackboxModules=disabled)") QvtdEngine engine)
			throws Exception {
		ServiceRegistration<QvtdBlackboxLibrary> registration = register(context, "from-service", 0);
		try {
			assertNotServed(engine, "blackboxes are off, the query must not be served");
		} finally {
			registration.unregister();
		}
	}

	@Test
	@DisplayName("libraries come and go while the same engine instance keeps running")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = {
			@Property(key = "qvtd.blackboxEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvtd.allowedBlackboxModules", value = { LIBRARY, "dynamic" }, type = Type.Array)
	})
	void librariesComeAndGo(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvtd.allowedBlackboxModules=dynamic)") QvtdEngine engine)
			throws Exception {
		assertNotServed(engine, "no library registered yet");

		ServiceRegistration<QvtdBlackboxLibrary> first = register(context, "first", 0);
		assertEquals("first", run(engine));
		first.unregister();
		assertNotServed(engine, "the library is gone, the query must not be served any more");

		ServiceRegistration<QvtdBlackboxLibrary> second = register(context, "second", 0);
		try {
			assertEquals("second", run(engine));
		} finally {
			second.unregister();
		}
	}

	@Test
	@DisplayName("of two libraries with the same name the higher service.ranking answers")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = {
			@Property(key = "qvtd.blackboxEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvtd.allowedBlackboxModules", value = { LIBRARY, "ranked" }, type = Type.Array)
	})
	void higherRankingAnswers(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvtd.allowedBlackboxModules=ranked)") QvtdEngine engine)
			throws Exception {
		ServiceRegistration<QvtdBlackboxLibrary> low = register(context, "low", 0);
		ServiceRegistration<QvtdBlackboxLibrary> high = register(context, "high", 10);
		try {
			assertEquals("high", run(engine));
			high.unregister();
			high = null;
			assertEquals("low", run(engine), "with the winner gone, the other one answers");
		} finally {
			if (high != null) {
				high.unregister();
			}
			low.unregister();
		}
	}

	// ==================== Helpers ====================

	private static ServiceRegistration<QvtdBlackboxLibrary> register(BundleContext context,
			String answer, int ranking) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put(Constants.SERVICE_RANKING, ranking);
		return context.registerService(QvtdBlackboxLibrary.class, new AnswerLibrary(answer),
				properties);
	}

	/** The name of the one package the transformation created; the run must succeed. */
	private static String run(QvtdEngine engine) throws Exception {
		QvtdModelExtent target = QvtdModelExtent.of(List.of());
		QvtdExecutionResult result = execute(engine, target);
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(1, target.getContents().size(), () -> "one package expected: " + target.getContents());
		return ((EPackage) target.getContents().get(0)).getName();
	}

	/** Runs the transformation and asserts that no library served the query Answer (#283). */
	private static void assertNotServed(QvtdEngine engine, String why) throws Exception {
		QvtdModelExtent target = QvtdModelExtent.of(List.of());
		QvtdExecutionResult result = execute(engine, target);
		String diagnostics = result.diagnostics().toString();
		assertFalse(result.isSuccess(), () -> why + ", diagnostics: " + diagnostics);
		assertTrue(diagnostics.contains("Blackbox query 'Answer' cannot be served"),
				() -> why + ", but failed otherwise: " + diagnostics);
	}

	private static QvtdExecutionResult execute(QvtdEngine engine, QvtdModelExtent target)
			throws Exception {
		EPackage source = EcoreFactory.eINSTANCE.createEPackage();
		source.setName("untouched");
		Map<String, QvtdModelExtent> extents = Map.of(
				"source", QvtdModelExtent.of(List.<EObject>of(source)), "target", target);
		return engine.execute(engine.parse(TRANSFORMATION, "T"),
				QvtdExecutionContext.enforce("target", extents));
	}

	/** A library {@code osgi.lib} whose one operation {@code Answer} returns a fixed text. */
	private static final class AnswerLibrary implements QvtdBlackboxLibrary {

		private final String answer;

		AnswerLibrary(String answer) {
			this.answer = answer;
		}

		@Override
		public String getModuleName() {
			return LIBRARY;
		}

		@Override
		public String getUnitQualifiedName() {
			return LIBRARY;
		}

		@Override
		public List<String> getUsedPackageURIs() {
			return List.of();
		}

		@Override
		public List<String> getOperationNames() {
			return List.of("Answer");
		}

		@Override
		public Object invoke(String operationName, Object self, Object[] args) {
			return "Answer".equals(operationName) ? answer : null;
		}
	}
}
