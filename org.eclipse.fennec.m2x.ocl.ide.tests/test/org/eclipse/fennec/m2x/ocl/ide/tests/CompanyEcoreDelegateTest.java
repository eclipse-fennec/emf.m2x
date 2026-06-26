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
package org.eclipse.fennec.m2x.ocl.ide.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.BasicExtendedMetaData;
import org.eclipse.emf.ecore.util.ExtendedMetaData;
import org.eclipse.emf.ecore.xmi.impl.EcoreResourceFactoryImpl;
import org.eclipse.fennec.m2x.ocl.ide.OclInvocationDelegateFactory;
import org.eclipse.fennec.m2x.ocl.ide.OclSettingDelegateFactory;
import org.eclipse.fennec.m2x.ocl.ide.OclValidationDelegate;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Loads the hand-written {@code /opt/git/m2m/company.ecore} test model and
 * exercises every OCL annotation in it through the {@code ocl.ide} delegates,
 * proving the file is correct independently of any running Eclipse.
 */
class CompanyEcoreDelegateTest {

	private static final String URI_FENNEC = "http://www.eclipse.org/fennec/m2x/ocl/1.0";
	private static final String ECORE_PATH = "/opt/git/m2m/company.ecore";

	private EPackage companyPkg;

	@BeforeEach
	void setup() {
		for (String uri : new String[] { URI_FENNEC }) {
			EValidator.ValidationDelegate.Registry.INSTANCE.put(uri, new OclValidationDelegate());
			EStructuralFeature.Internal.SettingDelegate.Factory.Registry.INSTANCE
					.put(uri, new OclSettingDelegateFactory());
			EOperation.Internal.InvocationDelegate.Factory.Registry.INSTANCE
					.put(uri, new OclInvocationDelegateFactory());
		}

		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("ecore", new EcoreResourceFactoryImpl());
		Resource res = rs.getResource(URI.createFileURI(new File(ECORE_PATH).getAbsolutePath()), true);
		companyPkg = (EPackage) res.getContents().get(0);
		// register so dynamic instances resolve their package
		rs.getPackageRegistry().put(companyPkg.getNsURI(), companyPkg);
		ExtendedMetaData emd = new BasicExtendedMetaData(rs.getPackageRegistry());
		rs.getResource(URI.createFileURI(new File(ECORE_PATH).getAbsolutePath()), true)
				.setURI(res.getURI());
		assertNotNull(emd);
	}

	private EObject create(String className) {
		EClass c = (EClass) companyPkg.getEClassifier(className);
		assertNotNull(c, "EClass " + className + " not found");
		return companyPkg.getEFactoryInstance().create(c);
	}

	@Test
	void employee_settingDelegates() {
		EObject emp = create("Employee");
		EClass c = emp.eClass();
		emp.eSet(c.getEStructuralFeature("name"), "Alice");
		emp.eSet(c.getEStructuralFeature("age"), 30);
		emp.eSet(c.getEStructuralFeature("salary"), 1000.0);

		assertEquals(5, emp.eGet(c.getEStructuralFeature("nameLength")));
		assertEquals(Boolean.TRUE, emp.eGet(c.getEStructuralFeature("adult")));
	}

	@Test
	void derivedFeature_invalidSource_isSignalledNotReturnedAsMarker() {
		// name is left unset (null); self.name.size() is OCL-invalid. The setting
		// delegate must signal that as an exception (like Eclipse OCL) so the
		// reflective editor shows an empty value, instead of returning the
		// OclInvalid marker (which rendered as the literal text "OclInvalid").
		EObject emp = create("Employee");
		EClass c = emp.eClass();
		emp.eSet(c.getEStructuralFeature("age"), 30);
		assertThrows(IllegalStateException.class,
				() -> emp.eGet(c.getEStructuralFeature("nameLength")));
	}

	@Test
	void employee_invocationDelegates() throws Exception {
		EObject emp = create("Employee");
		EClass c = emp.eClass();
		emp.eSet(c.getEStructuralFeature("name"), "Bob");
		emp.eSet(c.getEStructuralFeature("salary"), 1000.0);

		EOperation greeting = c.getEOperations().stream()
				.filter(o -> o.getName().equals("greeting")).findFirst().orElseThrow();
		assertEquals("Hello, Bob", ((InternalEObject) emp).eInvoke(greeting, null));
	}

	@Test
	void employee_validationDelegate() {
		EClass c = (EClass) companyPkg.getEClassifier("Employee");
		EObject emp = create("Employee");
		emp.eSet(c.getEStructuralFeature("name"), "Alice");
		emp.eSet(c.getEStructuralFeature("age"), 30);
		emp.eSet(c.getEStructuralFeature("salary"), 1000.0);

		EValidator.ValidationDelegate delegate =
				EValidator.ValidationDelegate.Registry.INSTANCE.getValidationDelegate(URI_FENNEC);
		assertNotNull(delegate);
		assertTrue(delegate.validate(c, emp, null, "agePositive", "self.age > 0"));

		emp.eSet(c.getEStructuralFeature("age"), -5);
		assertFalse(delegate.validate(c, emp, null, "agePositive", "self.age > 0"));
	}

	@Test
	void department_collectionDerivations() {
		EObject dept = create("Department");
		EClass dc = dept.eClass();

		EObject e1 = create("Employee");
		e1.eSet(e1.eClass().getEStructuralFeature("name"), "Alice");
		e1.eSet(e1.eClass().getEStructuralFeature("salary"), 1000.0);
		EObject e2 = create("Employee");
		e2.eSet(e2.eClass().getEStructuralFeature("name"), "Bob");
		e2.eSet(e2.eClass().getEStructuralFeature("salary"), 1500.0);

		@SuppressWarnings("unchecked")
		var employees = (java.util.List<EObject>) dept.eGet(dc.getEStructuralFeature("employees"));
		employees.add(e1);
		employees.add(e2);

		assertEquals(2, dept.eGet(dc.getEStructuralFeature("headcount")));
		assertEquals(2500.0, dept.eGet(dc.getEStructuralFeature("totalSalary")));
	}
}
