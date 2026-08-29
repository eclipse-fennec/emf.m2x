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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.annotation.require.RequireQVTD;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceObjects;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.annotation.Property;
import org.osgi.test.common.annotation.Property.Scalar;
import org.osgi.test.common.annotation.config.WithConfiguration;
import org.osgi.test.junit5.cm.ConfigurationExtension;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * QVT-R composes over the {@code OclEngine} service.
 *
 * <p>The component's configuration policy is optional, so a bundle that merely wants to run a transformation gets a working engine without configuring anything, and configuring one later does not mean registering a different service.
 *
 * <p>Before this, the engine built an OCL engine of its own in its constructor. Under OSGi
 * that meant the engine a consumer had configured — with its cache and its operation
 * providers — was not the one that ran; a second one was created next to it.
 */
@RequireOCL
@RequireQVTD
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
@ExtendWith(ConfigurationExtension.class)
class QvtdEngineComponentOSGiTest {

	@Test
	@DisplayName("there is an engine without configuring anything")
	void engineIsThereByDefault(@InjectService(timeout = 5000) QvtdEngine engine) {
		assertNotNull(engine);
		assertNotNull(engine.getOclEngine(),
				"the engine has to run on the OCL service, not on one it built itself");
	}

	@Test
	@DisplayName("every consumer gets its own engine")
	void prototypeScope(@InjectBundleContext BundleContext context) {
		// Engines carry caches. Handing the same one to unrelated consumers would make
		// one consumer's warm-up and another's memory footprint the same thing.
		//
		// It takes ServiceObjects to see that: a plain getService hands out the one
		// bundle-scoped instance no matter how the component is scoped.
		ServiceObjects<QvtdEngine> services =
				context.getServiceObjects(context.getServiceReference(QvtdEngine.class));
		QvtdEngine first = services.getService();
		QvtdEngine second = services.getService();
		try {
			assertNotSame(first, second);
			assertNotSame(first.getOclEngine(), second.getOclEngine(),
					"the OCL engine is bound prototype-required, so it must not be shared either");
		} finally {
			services.ungetService(first);
			services.ungetService(second);
		}
	}

	@Test
	@DisplayName("configuring it does not replace it — the policy is optional")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = {
			@Property(key = "qvtd.maxRelationDepth", value = "64", scalar = Scalar.Integer)
	})
	void configuredEngineIsStillTheEngine(@InjectService(timeout = 5000) QvtdEngine engine) {
		assertNotNull(engine);
		assertNotNull(engine.getOclEngine());
	}

	@Test
	@DisplayName("the configured limit is the one that applies")
	@WithConfiguration(pid = "DefaultQvtdEngine", properties = {
			@Property(key = "qvtd.maxRelationDepth", value = "4", scalar = Scalar.Integer)
	})
	void configuredLimitApplies(@InjectBundleContext BundleContext context) throws Exception {
		// Asserting that the engine is still there proves nothing about the configuration: if
		// the helper ignored it entirely, that assertion still holds (#178). Two relations
		// calling each other run into the limit, and the message names the number that was
		// configured — which only the configured engine can say.
		EPackage shelf = shelfPackage();
		EPackage.Registry.INSTANCE.put(shelf.getNsURI(), shelf);
		try {
			// The configuration is delivered to the component asynchronously, so the service
			// registered right now may still be the one built before it arrived. Retried until
			// the engine answers under the configured limit, rather than asserting on whichever
			// instance happened to be there first.
			String message = awaitConfiguredLimit(context, shelf);

			assertTrue(message.contains("(4)"),
					() -> "the configured limit is what the engine ran under: " + message);
		} finally {
			EPackage.Registry.INSTANCE.remove(shelf.getNsURI());
		}
	}

	/**
	 * Runs the recursive transformation until the depth limit reported is the configured one.
	 *
	 * @return the last message seen, configured or not
	 */
	private static String awaitConfiguredLimit(BundleContext context, EPackage shelf)
			throws Exception {
		String message = "no limit was reached at all";
		long deadline = System.nanoTime() + TimeUnit.SECONDS.toNanos(10);
		do {
			QvtdEngine engine = context.getService(context.getServiceReference(QvtdEngine.class));
			RelationalTransformation transformation = engine.parse("""
					transformation loop(source : shelf, target : shelf) {
					    top relation Init {
					        n : String;
					        checkonly domain source b1 : Book { title = n };
					        enforce domain target b2 : Book { title = n };
					        where { RecurseA(b1, b2); }
					    }
					    relation RecurseA {
					        n : String;
					        checkonly domain source b1 : Book { title = n };
					        enforce domain target b2 : Book { title = n };
					        where { RecurseB(b1, b2); }
					    }
					    relation RecurseB {
					        n : String;
					        checkonly domain source b1 : Book { title = n };
					        enforce domain target b2 : Book { title = n };
					        where { RecurseA(b1, b2); }
					    }
					}
					""", "loop");

			EClass bookClass = (EClass) shelf.getEClassifier("Book");
			EObject book = EcoreUtil.create(bookClass);
			book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");
			QvtdExecutionContext execution = QvtdExecutionContext.enforce("target",
					Map.of("source", QvtdModelExtent.of(book), "target", QvtdModelExtent.of()));

			QvtdExecutionException failure = assertThrows(QvtdExecutionException.class,
					() -> engine.execute(transformation, execution));
			message = failure.getMessage();
			if (message.contains("(4)")) {
				return message;
			}
			Thread.sleep(100);
		} while (System.nanoTime() < deadline);
		return message;
	}

	/** A metamodel built here, so this test needs no resource of its own. */
	private static EPackage shelfPackage() {
		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI("http://example.org/m2x/osgi-shelf/1.0");
		shelf.setNsPrefix("shelf");
		EClass book = EcoreFactory.eINSTANCE.createEClass();
		book.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		book.getEStructuralFeatures().add(title);
		shelf.getEClassifiers().add(book);
		return shelf;
	}
}
