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
package org.eclipse.fennec.m2x.qvto.parser;

import java.util.HashMap;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * The M1 classes of the QVT-O standard library (QVT v1.3 §8.3.1).
 *
 * <p>These names are part of the language, not of a user metamodel: a transformation
 * writes {@code except (Exception)} or {@code raise StringException('…')} without
 * declaring anything. They are therefore resolved from here rather than looked up in
 * an {@code EPackage.Registry}.
 *
 * <p>Each name maps to <b>one</b> classifier instance for the lifetime of the JVM. That
 * matters: before this existed, every occurrence of {@code Exception} produced a fresh
 * synthetic {@code EClass}, so two {@code except} clauses naming the same exception type
 * were comparing unrelated classes and only agreed because the evaluator fell back to
 * comparing names.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class QvtoStandardLibraryTypes {

	/** Namespace URI of the synthetic package holding the standard library classes. */
	public static final String NS_URI = "http://www.omg.org/spec/QVT/20160601/QVTOperational/stdlib";

	private static final EPackage PACKAGE = createPackage();

	private static final Map<String, EClassifier> TYPES = index();

	private QvtoStandardLibraryTypes() {
		// static holder
	}

	/**
	 * Returns the standard library classifier for the given name.
	 *
	 * @param name the type name as written in the transformation
	 * @return the classifier, or {@code null} if the name is not a standard library type
	 */
	public static EClassifier lookup(String name) {
		return TYPES.get(name);
	}

	/**
	 * Returns the package holding the standard library classes.
	 *
	 * @return the standard library package
	 */
	public static EPackage getPackage() {
		return PACKAGE;
	}

	private static EPackage createPackage() {
		EPackage ePackage = EcoreFactory.eINSTANCE.createEPackage();
		ePackage.setName("qvtoperational_stdlib");
		ePackage.setNsURI(NS_URI);
		ePackage.setNsPrefix("qvtostdlib");

		// §8.3.1.4: base class of all exceptions; §8.3.1.5/6: StringException and AssertionFailed
		// have "reason : String" — held by the base, so every exception answers it (#340)
		EClass exception = createClass(ePackage, "Exception");
		EAttribute reason = EcoreFactory.eINSTANCE.createEAttribute();
		reason.setName("reason");
		reason.setEType(EcorePackage.Literals.ESTRING);
		exception.getEStructuralFeatures().add(reason);
		// §8.3.1.5: shorthand raised by a string-valued RaiseExp
		createClass(ePackage, "StringException", exception);
		// §8.3.1.6: raised by a fatal AssertExp
		createClass(ePackage, "AssertionFailed", exception);
		// §8.3.1: "Void" is QVT-O's synonym for OclVoid, used as the return type of
		// side-effect-only helpers. Held here as one stable classifier so that every
		// occurrence refers to the same type rather than to a fresh fabrication.
		createClass(ePackage, "Void");
		// §8.3.1.1/§8.3.1.2: Transformation and Model — their values are a transformation
		// instance and a model extent, no model objects, so they are data types (#345)
		createDataType(ePackage, "Transformation");
		createDataType(ePackage, "Model");

		return ePackage;
	}

	private static void createDataType(EPackage owner, String name) {
		EDataType dataType = EcoreFactory.eINSTANCE.createEDataType();
		dataType.setName(name);
		owner.getEClassifiers().add(dataType);
	}

	private static EClass createClass(EPackage owner, String name, EClass... superTypes) {
		EClass eClass = EcoreFactory.eINSTANCE.createEClass();
		eClass.setName(name);
		for (EClass superType : superTypes) {
			eClass.getESuperTypes().add(superType);
		}
		owner.getEClassifiers().add(eClass);
		return eClass;
	}

	private static Map<String, EClassifier> index() {
		Map<String, EClassifier> types = new HashMap<>();
		for (EClassifier classifier : PACKAGE.getEClassifiers()) {
			types.put(classifier.getName(), classifier);
		}
		// §8.3.1: "Object is an M1 type" that every value conforms to; §8.3.2: Any is its
		// deprecated synonym (#345)
		types.put("Object", OclStandardLibrary.INSTANCE.oclAny());
		types.put("Any", OclStandardLibrary.INSTANCE.oclAny());
		// §8.3.4: Element, the type of every model object
		types.put("Element", EcorePackage.Literals.EOBJECT);
		// §8.3.1.3: Status, what transform() answers
		types.put("Status", QvtOperationalPackage.Literals.STATUS);
		return Map.copyOf(types);
	}
}
