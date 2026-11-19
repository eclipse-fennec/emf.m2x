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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Dictionary;
import java.util.Hashtable;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxInvocationContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.annotation.require.RequireQVTO;
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
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * A blackbox library published as a service reaches {@code DefaultQvtoEngine} (#269).
 *
 * <p>The engine binds {@code QvtoBlackboxLibrary} services through a dynamic {@code 0..n}
 * reference. Each test configures the engine with its own name in the allow-list and injects
 * the engine by that name, so it is handed the instance that carries its configuration rather
 * than one another test left behind (see {@code QvtoServiceDiscoveryOSGiTest}).
 */
@RequireOCL
@RequireQVTO
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class QvtoBlackboxServiceOSGiTest {

	private static final String LIBRARY = "osgi.lib";

	private static final String TRANSFORMATION = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			import osgi.lib;
			transformation Main(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := answer();
			        };
			    }
			}
			""";

	@Test
	@DisplayName("a library registered as a service is called by the transformation")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.blackboxEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.allowedBlackboxModules", value = { LIBRARY, "registered" }, type = Type.Array)
	})
	void registeredLibraryIsCalled(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvto.allowedBlackboxModules=registered)") QvtoEngine engine)
			throws Exception {
		ServiceRegistration<QvtoBlackboxLibrary> registration = register(context, "from-service", 0);
		try {
			assertEquals("from-service", run(engine));
		} finally {
			registration.unregister();
		}
	}

	@Test
	@DisplayName("without qvto.blackboxEnabled a registered library stays out of reach")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.allowedBlackboxModules", value = { LIBRARY, "disabled" }, type = Type.Array)
	})
	void disabledLeavesLibraryOutOfReach(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvto.allowedBlackboxModules=disabled)") QvtoEngine engine)
			throws Exception {
		ServiceRegistration<QvtoBlackboxLibrary> registration = register(context, "from-service", 0);
		try {
			assertUnresolved(engine, "blackboxes are off, the import must not resolve");
		} finally {
			registration.unregister();
		}
	}

	@Test
	@DisplayName("the allow-list narrows which registered libraries may be imported")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.blackboxEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.allowedBlackboxModules", value = "narrowed")
	})
	void allowListNarrows(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvto.allowedBlackboxModules=narrowed)") QvtoEngine engine)
			throws Exception {
		ServiceRegistration<QvtoBlackboxLibrary> registration = register(context, "from-service", 0);
		try {
			assertUnresolved(engine, "osgi.lib is not on the allow-list, the import must not resolve");
		} finally {
			registration.unregister();
		}
	}

	@Test
	@DisplayName("libraries come and go while the same engine instance keeps running")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.blackboxEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.allowedBlackboxModules", value = { LIBRARY, "dynamic" }, type = Type.Array)
	})
	void librariesComeAndGo(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvto.allowedBlackboxModules=dynamic)") QvtoEngine engine)
			throws Exception {
		assertUnresolved(engine, "no library registered yet");

		ServiceRegistration<QvtoBlackboxLibrary> first = register(context, "first", 0);
		assertEquals("first", run(engine));
		first.unregister();
		assertUnresolved(engine, "the library is gone, the import must not resolve any more");

		ServiceRegistration<QvtoBlackboxLibrary> second = register(context, "second", 0);
		try {
			assertEquals("second", run(engine));
		} finally {
			second.unregister();
		}
	}

	@Test
	@DisplayName("of two libraries with the same name the higher service.ranking answers")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.blackboxEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.allowedBlackboxModules", value = { LIBRARY, "ranked" }, type = Type.Array)
	})
	void higherRankingAnswers(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(qvto.allowedBlackboxModules=ranked)") QvtoEngine engine)
			throws Exception {
		ServiceRegistration<QvtoBlackboxLibrary> high = register(context, "high", 10);
		ServiceRegistration<QvtoBlackboxLibrary> low = register(context, "low", 0);
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

	@Test
	@DisplayName("blackboxes.cardinality.minimum keeps the engine away until its library is there")
	@WithConfiguration(pid = "DefaultQvtoEngine", properties = {
			@Property(key = "qvto.blackboxEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "qvto.allowedBlackboxModules", value = { LIBRARY, "waiting" }, type = Type.Array),
			@Property(key = "blackboxes.cardinality.minimum", value = "1", scalar = Scalar.Integer)
	})
	void cardinalityMinimumWaitsForLibrary(@InjectBundleContext BundleContext context,
			@InjectService(cardinality = 0, filter = "(qvto.allowedBlackboxModules=waiting)")
			ServiceAware<QvtoEngine> engines) throws Exception {
		assertNull(engines.waitForService(500),
				"the engine must not be available before a library is bound");

		ServiceRegistration<QvtoBlackboxLibrary> registration = register(context, "awaited", 0);
		try {
			QvtoEngine engine = engines.waitForService(5000);
			assertNotNull(engine, "the engine must come up once its library is there");
			assertEquals("awaited", run(engine));
		} finally {
			registration.unregister();
		}
	}

	// ==================== Helpers ====================

	private static ServiceRegistration<QvtoBlackboxLibrary> register(BundleContext context,
			String answer, int ranking) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put(Constants.SERVICE_RANKING, ranking);
		return context.registerService(QvtoBlackboxLibrary.class, new AnswerLibrary(answer),
				properties);
	}

	/** Runs the transformation and asserts that it failed because osgi.lib did not resolve. */
	private static void assertUnresolved(QvtoEngine engine, String why) throws Exception {
		QvtoExecutionResult result = execute(engine, newPackage());
		assertFalse(result.isSuccess(), why);
		String diagnostics = result.diagnostics().toString();
		assertTrue(diagnostics.contains(LIBRARY), () -> why + ", but failed otherwise: " + diagnostics);
	}

	/** The name the transformation wrote; the run must succeed. */
	private static String run(QvtoEngine engine) throws Exception {
		EObject pkg = newPackage();
		QvtoExecutionResult result = execute(engine, pkg);
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return (String) pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME);
	}

	private static EObject newPackage() {
		EObject pkg = EcorePackage.eINSTANCE.getEFactoryInstance()
				.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "untouched");
		return pkg;
	}

	private static QvtoExecutionResult execute(QvtoEngine engine, EObject pkg) throws Exception {
		BasicQvtoModelExtent extent = new BasicQvtoModelExtent();
		extent.add(pkg);
		return engine.execute(engine.parse(TRANSFORMATION, "Main"), QvtoExecutionContext.of(extent));
	}

	/** A library {@code osgi.lib} whose one operation {@code answer()} returns a fixed text. */
	private static final class AnswerLibrary implements QvtoBlackboxLibrary {

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
		public List<BlackboxOperationDescriptor> getOperationDescriptors() {
			BlackboxOperationDescriptor operation =
					QvtOperationalFactory.eINSTANCE.createBlackboxOperationDescriptor();
			operation.setName("answer");
			operation.setReturnType(EcorePackage.Literals.ESTRING);
			return List.of(operation);
		}

		@Override
		public Object invoke(String operationName, QvtoBlackboxInvocationContext context,
				Object[] args) {
			return "answer".equals(operationName) ? answer : null;
		}
	}
}
