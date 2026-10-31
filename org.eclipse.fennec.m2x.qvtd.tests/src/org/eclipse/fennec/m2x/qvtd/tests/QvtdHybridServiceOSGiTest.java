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
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.annotation.require.RequireQVTD;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.api.annotation.require.RequireQVTO;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.Property.Scalar;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * A QVT-R/QVT-O hybrid transformation under OSGi (§7.11.3.6, #286): a QVT-O unit published as a
 * service and marked as relation implementation is loaded by {@code QvtoRelationImplementations},
 * which {@code DefaultQvtdEngine} binds as provider when {@code qvtd.implementationProvidersEnabled}
 * is on. Each test configures the engine with its own {@code test.case} and injects it by that.
 */
@RequireOCL
@RequireQVTD
@RequireQVTO
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class QvtdHybridServiceOSGiTest {

	private static final String UNIT = "osgi.hybrid.Impl";

	/** Copies each package; the target package is made by the QVT-O mapping copyPackage. */
	private static final String RELATIONS = """
			transformation T(source : ecore, target : ecore) {
				top relation R {
					n : String;
					checkonly domain source p : EPackage { name = n };
					enforce domain target q : EPackage { name = n } implementedby copyPackage(p);
				}
			}
			""";

	/** The implementation: it marks what it creates with the nsURI 'by-qvto'. */
	private static String operational(String refined) {
		return """
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation Impl(in source : ECORE, out target : ECORE) refines %s;
				mapping EPackage::copyPackage() : EPackage {
					name := self.name;
					nsURI := 'by-qvto';
				}
				""".formatted(refined);
	}

	@Test
	@DisplayName("a marked QVT-O unit serves the implementedby clause")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = {
			@Property(key = "qvtd.implementationProvidersEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "test.case", value = "served") })
	void servedByTheUnit(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(test.case=served)") QvtdEngine engine) throws Exception {
		ServiceRegistration<QvtoUnitResolver> unit = register(context, operational("T"));
		try {
			assertEquals("by-qvto", run(engine));
		} finally {
			unit.unregister();
		}
	}

	@Test
	@DisplayName("without qvtd.implementationProvidersEnabled the bound provider is not used, and the run says so")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = { @Property(key = "test.case", value = "disabled") })
	void disabled(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(test.case=disabled)") QvtdEngine engine) throws Exception {
		ServiceRegistration<QvtoUnitResolver> unit = register(context, operational("T"));
		try {
			QvtdExecutionResult result = execute(engine, QvtdModelExtent.of(List.of()));
			assertFalse(result.isSuccess());
			assertTrue(result.diagnostics().toString().contains("qvtd.implementationProvidersEnabled is off"),
					() -> "diagnostics: " + result.diagnostics());
		} finally {
			unit.unregister();
		}
	}

	@Test
	@DisplayName("units come and go while the same engine keeps running")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = {
			@Property(key = "qvtd.implementationProvidersEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "test.case", value = "dynamic") })
	void unitsComeAndGo(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(test.case=dynamic)") QvtdEngine engine) throws Exception {
		assertNotServed(engine, "no unit registered yet");
		ServiceRegistration<QvtoUnitResolver> unit = register(context, operational("T"));
		assertEquals("by-qvto", run(engine));
		unit.unregister();
		assertNotServed(engine, "the unit is gone");
	}

	@Test
	@DisplayName("a unit that refines another relational transformation does not serve this one")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = {
			@Property(key = "qvtd.implementationProvidersEnabled", value = "true", scalar = Scalar.Boolean),
			@Property(key = "test.case", value = "refines") })
	void refinesAnother(@InjectBundleContext BundleContext context,
			@InjectService(timeout = 5000, filter = "(test.case=refines)") QvtdEngine engine) throws Exception {
		ServiceRegistration<QvtoUnitResolver> unit = register(context, operational("Other"));
		try {
			assertNotServed(engine, "the unit refines Other, not T");
		} finally {
			unit.unregister();
		}
	}

	// ==================== Helpers ====================

	/** Publishes the QVT-O source as a unit marked as relation implementation. */
	private static ServiceRegistration<QvtoUnitResolver> register(BundleContext context, String source) {
		Dictionary<String, Object> properties = new Hashtable<>();
		properties.put("qvto.unit.name", UNIT);
		properties.put("qvto.relation.implementation", "true");
		QvtoUnit unit = new QvtoUnit.SourceUnit(UNIT, URI.createURI("mem:/osgi/hybrid/Impl.qvto"), source);
		return context.registerService(QvtoUnitResolver.class,
				name -> UNIT.equals(name) ? Optional.of(unit) : Optional.empty(), properties);
	}

	/** The nsURI of the one package the run created; the run must succeed. */
	private static String run(QvtdEngine engine) throws Exception {
		QvtdModelExtent target = QvtdModelExtent.of(List.of());
		QvtdExecutionResult result = execute(engine, target);
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(1, target.getContents().size(), () -> "one package expected: " + target.getContents());
		return ((EPackage) target.getContents().get(0)).getNsURI();
	}

	private static void assertNotServed(QvtdEngine engine, String why) throws Exception {
		QvtdExecutionResult result = execute(engine, QvtdModelExtent.of(List.of()));
		assertFalse(result.isSuccess(), () -> why + ", diagnostics: " + result.diagnostics());
		assertTrue(result.diagnostics().toString().contains("'copyPackage'"),
				() -> why + ", but failed otherwise: " + result.diagnostics());
	}

	private static QvtdExecutionResult execute(QvtdEngine engine, QvtdModelExtent target) throws Exception {
		EPackage source = EcoreFactory.eINSTANCE.createEPackage();
		source.setName("company");
		Map<String, QvtdModelExtent> extents = Map.of(
				"source", QvtdModelExtent.of(List.<EObject>of(source)), "target", target);
		return engine.execute(engine.parse(RELATIONS, "T"), QvtdExecutionContext.enforce("target", extents));
	}
}
