/**
 * Copyright (c) 2012 - 2023 Data In Motion and others.
 * All rights reserved.
 *
 * This program and the accompanying materials are made
 * available under the terms of the Eclipse Public License 2.0
 * which is available at https://www.eclipse.org/legal/epl-2.0/
 *
 * SPDX-License-Identifier: EPL-2.0
 *
 * Contributors:
 *     Data In Motion - initial API and implementation
 */
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.annotation.require.RequireOCL;
import org.eclipse.fennec.m2x.ocl.example.model.oclexample.OCLExampleFactory;
import org.eclipse.fennec.m2x.ocl.example.model.oclexample.Person;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.osgi.test.common.annotation.InjectService;
import org.osgi.test.common.service.ServiceAware;
import org.osgi.test.junit5.context.BundleContextExtension;
import org.osgi.test.junit5.service.ServiceExtension;

/**
 * OSGi integration tests that verify OCL constraint validation on a
 * generated EMF model from a <b>separate bundle</b>.
 *
 * <p>This reproduces the cross-bundle classloading scenario: the OCL engine
 * lives in the {@code ocl.engine} bundle while the model ({@link Person})
 * lives in the {@code ocl.example.model} bundle. The validation delegate
 * must be able to evaluate OCL constraints without requiring visibility
 * to the model's implementation classes.
 */
@RequireOCL
@ExtendWith(BundleContextExtension.class)
@ExtendWith(ServiceExtension.class)
class EMFAnnotationOSGiTest {


	// --- Diagnostician-based validation (EMF standard path) ---

	@Test
	void diagnostician_validPerson_noErrors(
			@InjectService(filter = "(emf.configuratorName=http://www.eclipse.org/fennec/m2x/ocl/1.0)")
			ServiceAware<EValidator.ValidationDelegate> valAware) {

		assertNotNull(valAware.getService());

		Person person = OCLExampleFactory.eINSTANCE.createPerson();
		person.setFirstName("Alice");
		person.setLastName("Smith");
		person.setPhone("1234567890");
		person.setAge(25);

		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(person);
		assertEquals(Diagnostic.OK, diagnostic.getSeverity(),
				"Valid person should pass all constraints, but got: " + diagnostic);
	}

	@Test
	void diagnostician_invalidPhone_error(
			@InjectService(filter = "(emf.configuratorName=http://www.eclipse.org/fennec/m2x/ocl/1.0)")
			ServiceAware<EValidator.ValidationDelegate> valAware) {

		assertNotNull(valAware.getService());

		Person person = OCLExampleFactory.eINSTANCE.createPerson();
		person.setFirstName("Bob");
		person.setLastName("Jones");
		person.setPhone("not-a-phone");
		person.setAge(25);

		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(person);
		assertTrue(diagnostic.getSeverity() >= Diagnostic.ERROR,
				"Invalid phone should fail ValidPhoneNumber constraint");
	}

	@Test
	void diagnostician_underageAge_error(
			@InjectService(filter = "(emf.configuratorName=http://www.eclipse.org/fennec/m2x/ocl/1.0)")
			ServiceAware<EValidator.ValidationDelegate> valAware) {

		assertNotNull(valAware.getService());

		Person person = OCLExampleFactory.eINSTANCE.createPerson();
		person.setFirstName("Charlie");
		person.setLastName("Brown");
		person.setPhone("1234567890");
		person.setAge(15);

		Diagnostic diagnostic = Diagnostician.INSTANCE.validate(person);
		assertTrue(diagnostic.getSeverity() >= Diagnostic.ERROR,
				"Age 15 should fail AgeAppropriate constraint (self.age > 18)");
	}

	// --- Direct OclEngine evaluation on cross-bundle model ---

	@Test
	void oclEngine_evaluateConstraint_onCrossBundleModel(
			@InjectService ServiceAware<OclEngine> engineAware) throws OclParseException {

		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		Person person = OCLExampleFactory.eINSTANCE.createPerson();
		person.setFirstName("Alice");
		person.setLastName("Smith");
		person.setPhone("1234567890");
		person.setAge(25);

		Object result = engine.evaluate("self.age > 18", OclContext.of(person));
		assertEquals(true, result);
	}

	@Test
	void oclEngine_evaluateConstraint_crossBundleModel_violatedConstraint(
			@InjectService ServiceAware<OclEngine> engineAware) throws OclParseException {

		OclEngine engine = engineAware.getService();
		assertNotNull(engine);

		Person person = OCLExampleFactory.eINSTANCE.createPerson();
		person.setFirstName("Young");
		person.setLastName("Person");
		person.setPhone("1234567890");
		person.setAge(10);

		Object result = engine.evaluate("self.age > 18", OclContext.of(person));
		assertEquals(false, result);
	}
}
