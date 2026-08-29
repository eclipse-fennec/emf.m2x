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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.ocl.api.CompleteOclContribution;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.test.common.annotation.InjectBundleContext;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * A bundle contributes a Complete OCL document as a service, and the engine picks it up (#195).
 *
 * <p>What the {@link CompleteOclContribution} javadoc has promised since 1.0. A Complete OCL
 * document adds constraints and {@code def:} operations to a metamodel without touching the
 * {@code .ecore} (OCL v2.4 §12), which is what lets one bundle ship rules for a model it does
 * not own — the model bundle stays free of OCL annotations, and the rules are versioned with
 * the bundle that cares about them.
 *
 * <p>The reference is dynamic, so the engine that is already running has to notice both the
 * arrival and the departure. That is what these two tests are about; the programmatic half is
 * covered by `OclCompleteOclContributionTest` outside the framework.
 */
@RequireOCL
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class CompleteOclContributionOSGiTest {

	private static final String NS_URI = "http://example.org/m2x/contribution/1.0";

	private static final String DOCUMENT = """
			package staff
			context Person
			  def: isAdult() : Boolean = self.age >= 18
			endpackage
			""";

	@Test
	@DisplayName("a contributed def: operation becomes callable, and goes again with the service")
	void aContributedDocumentAppliesWhileItIsRegistered(@InjectBundleContext BundleContext context)
			throws Exception {
		EPackage staff = staffPackage();
		EPackage.Registry.INSTANCE.put(NS_URI, staff);
		EObject person = person(staff, 30);
		try {
			ServiceRegistration<CompleteOclContribution> registration = context.registerService(
					CompleteOclContribution.class, contribution(), null);
			try {
				// The engine is taken after the contribution, because a dynamic reference on a
				// prototype component reaches the instances built from then on
				Object adult = engineOf(context).evaluate("self.isAdult()", OclContext.of(person));

				assertEquals(Boolean.TRUE, adult,
						"the contributed def: is what makes this expression mean anything");
			} finally {
				registration.unregister();
			}

			Object afterwards = engineOf(context).evaluate("self.isAdult()", OclContext.of(person));

			assertNotEquals(Boolean.TRUE, afterwards,
					"unregistering the service takes the definition back out");
			assertEquals(OclInvalid.INSTANCE, afterwards);
		} finally {
			EPackage.Registry.INSTANCE.remove(NS_URI);
		}
	}

	private static OclEngine engineOf(BundleContext context) {
		ServiceReference<OclEngine> reference = context.getServiceReference(OclEngine.class);
		return context.getService(reference);
	}

	private static CompleteOclContribution contribution() {
		return new CompleteOclContribution() {

			@Override
			public URI getDocumentUri() {
				return URI.createURI("mem:/staff.ocl");
			}

			@Override
			public String getDocumentText() {
				return DOCUMENT;
			}
		};
	}

	private static EPackage staffPackage() {
		EPackage staff = EcoreFactory.eINSTANCE.createEPackage();
		staff.setName("staff");
		staff.setNsURI(NS_URI);
		staff.setNsPrefix("staff");
		EClass personClass = EcoreFactory.eINSTANCE.createEClass();
		personClass.setName("Person");
		EAttribute age = EcoreFactory.eINSTANCE.createEAttribute();
		age.setName("age");
		age.setEType(EcorePackage.Literals.EINT);
		personClass.getEStructuralFeatures().add(age);
		staff.getEClassifiers().add(personClass);
		return staff;
	}

	private static EObject person(EPackage staff, int age) {
		EClass personClass = (EClass) staff.getEClassifier("Person");
		EObject person = EcoreUtil.create(personClass);
		person.eSet(personClass.getEStructuralFeature("age"), age);
		return person;
	}
}
