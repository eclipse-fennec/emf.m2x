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
package org.eclipse.fennec.m2m.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclModelExtent;
import org.eclipse.fennec.m2m.ocl.api.OclParseException;
import org.eclipse.fennec.m2m.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2m.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Tests for GAP-19: {@code allInstances()} subtype contract.
 *
 * <p>Verifies that {@code Type.allInstances()} includes instances of subtypes,
 * as required by OCL v2.4 §11.2 and documented in {@link OclModelExtent#getAllInstances}.
 *
 * <p>Uses a programmatic Ecore model with a superclass (Vehicle) and two
 * subclasses (Car, Truck) to test inheritance-aware instance collection.
 */
class OclAllInstancesSubtypeTest {

	static OclEngineImpl engine;
	static EPackage vehiclePackage;
	static EClass vehicleClass;
	static EClass carClass;
	static EClass truckClass;
	static EObject car1;
	static EObject car2;
	static EObject truck1;
	static OclModelExtent extent;

	@BeforeAll
	static void setUp() {
		engine = new OclEngineImpl(new OclParserSupport());

		// Create "vehicle" package with Vehicle superclass
		vehiclePackage = EcoreFactory.eINSTANCE.createEPackage();
		vehiclePackage.setName("vehicle");
		vehiclePackage.setNsURI("http://test/vehicle/1.0");
		vehiclePackage.setNsPrefix("vehicle");

		vehicleClass = EcoreFactory.eINSTANCE.createEClass();
		vehicleClass.setName("Vehicle");
		EAttribute vehicleName = EcoreFactory.eINSTANCE.createEAttribute();
		vehicleName.setName("name");
		vehicleName.setEType(EcorePackage.Literals.ESTRING);
		vehicleClass.getEStructuralFeatures().add(vehicleName);
		EAttribute speed = EcoreFactory.eINSTANCE.createEAttribute();
		speed.setName("maxSpeed");
		speed.setEType(EcorePackage.Literals.EINT);
		vehicleClass.getEStructuralFeatures().add(speed);
		vehiclePackage.getEClassifiers().add(vehicleClass);

		// Car extends Vehicle
		carClass = EcoreFactory.eINSTANCE.createEClass();
		carClass.setName("Car");
		carClass.getESuperTypes().add(vehicleClass);
		EAttribute doors = EcoreFactory.eINSTANCE.createEAttribute();
		doors.setName("doors");
		doors.setEType(EcorePackage.Literals.EINT);
		carClass.getEStructuralFeatures().add(doors);
		vehiclePackage.getEClassifiers().add(carClass);

		// Truck extends Vehicle
		truckClass = EcoreFactory.eINSTANCE.createEClass();
		truckClass.setName("Truck");
		truckClass.getESuperTypes().add(vehicleClass);
		EAttribute payload = EcoreFactory.eINSTANCE.createEAttribute();
		payload.setName("payload");
		payload.setEType(EcorePackage.Literals.EDOUBLE);
		truckClass.getEStructuralFeatures().add(payload);
		vehiclePackage.getEClassifiers().add(truckClass);

		// Create instances
		car1 = vehiclePackage.getEFactoryInstance().create(carClass);
		car1.eSet(vehicleClass.getEStructuralFeature("name"), "Sedan");
		car1.eSet(vehicleClass.getEStructuralFeature("maxSpeed"), 200);
		car1.eSet(carClass.getEStructuralFeature("doors"), 4);

		car2 = vehiclePackage.getEFactoryInstance().create(carClass);
		car2.eSet(vehicleClass.getEStructuralFeature("name"), "Sports");
		car2.eSet(vehicleClass.getEStructuralFeature("maxSpeed"), 300);
		car2.eSet(carClass.getEStructuralFeature("doors"), 2);

		truck1 = vehiclePackage.getEFactoryInstance().create(truckClass);
		truck1.eSet(vehicleClass.getEStructuralFeature("name"), "BigRig");
		truck1.eSet(vehicleClass.getEStructuralFeature("maxSpeed"), 120);
		truck1.eSet(truckClass.getEStructuralFeature("payload"), 20000.0);

		// Create extent that includes all objects, filtering by isInstance (includes subtypes)
		List<EObject> allObjects = new ArrayList<>();
		allObjects.add(car1);
		allObjects.add(car2);
		allObjects.add(truck1);

		extent = eClass -> {
			List<EObject> result = new ArrayList<>();
			for (EObject obj : allObjects) {
				if (eClass.isInstance(obj)) {
					result.add(obj);
				}
			}
			return result;
		};
	}

	private Object evalWithExtent(String expression, EObject self) throws OclParseException {
		return engine.evaluate(expression, new OclContext(self, extent, java.util.Map.of()));
	}

	// === Supertype allInstances includes subtypes ===

	@Test
	void allInstances_supertype_includesSubtypes() throws OclParseException {
		// Vehicle.allInstances() should return all 3 instances (2 Cars + 1 Truck)
		Object result = evalWithExtent("Vehicle.allInstances()", car1);
		assertTrue(result instanceof Collection<?>);
		Collection<?> instances = (Collection<?>) result;
		assertEquals(3, instances.size());
		assertTrue(instances.contains(car1));
		assertTrue(instances.contains(car2));
		assertTrue(instances.contains(truck1));
	}

	@Test
	void allInstances_subtype_onlyOwnType() throws OclParseException {
		// Car.allInstances() should return only 2 Car instances, not Truck
		Object result = evalWithExtent("Car.allInstances()", car1);
		assertTrue(result instanceof Collection<?>);
		Collection<?> instances = (Collection<?>) result;
		assertEquals(2, instances.size());
		assertTrue(instances.contains(car1));
		assertTrue(instances.contains(car2));
	}

	@Test
	void allInstances_otherSubtype_onlyOwnType() throws OclParseException {
		// Truck.allInstances() should return only 1 Truck instance
		Object result = evalWithExtent("Truck.allInstances()", truck1);
		assertTrue(result instanceof Collection<?>);
		Collection<?> instances = (Collection<?>) result;
		assertEquals(1, instances.size());
		assertTrue(instances.contains(truck1));
	}

	// === Supertype allInstances chained with operations ===

	@Test
	void allInstances_supertype_select() throws OclParseException {
		// Select fast vehicles (maxSpeed > 150)
		Object result = evalWithExtent(
				"Vehicle.allInstances()->select(v | v.maxSpeed > 150)", car1);
		assertTrue(result instanceof Collection<?>);
		assertEquals(2, ((Collection<?>) result).size()); // Sedan(200) + Sports(300)
	}

	@Test
	void allInstances_supertype_forAll() throws OclParseException {
		// All vehicles have maxSpeed > 0
		assertEquals(true, evalWithExtent(
				"Vehicle.allInstances()->forAll(v | v.maxSpeed > 0)", car1));
	}

	@Test
	void allInstances_supertype_exists() throws OclParseException {
		// There exists a vehicle with name = 'BigRig' (the truck)
		assertEquals(true, evalWithExtent(
				"Vehicle.allInstances()->exists(v | v.name = 'BigRig')", car1));
	}

	@Test
	void allInstances_supertype_size() throws OclParseException {
		assertEquals(3, evalWithExtent("Vehicle.allInstances()->size()", car1));
	}

	@Test
	void allInstances_subtype_size() throws OclParseException {
		assertEquals(2, evalWithExtent("Car.allInstances()->size()", car1));
	}

	// === allInstances returns Set (no duplicates) ===

	@Test
	void allInstances_supertype_isSet() throws OclParseException {
		assertEquals(true, evalWithExtent(
				"Vehicle.allInstances()->isUnique(v | v)", car1));
	}
}
