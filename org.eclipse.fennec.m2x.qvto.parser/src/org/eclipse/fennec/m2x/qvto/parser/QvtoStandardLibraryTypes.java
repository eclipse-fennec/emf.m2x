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

import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;

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

		// §8.3.1.4: base class of all exceptions
		EClass exception = createClass(ePackage, "Exception");
		// §8.3.1.5: shorthand raised by a string-valued RaiseExp
		createClass(ePackage, "StringException", exception);
		// §8.3.1.6: raised by a fatal AssertExp
		createClass(ePackage, "AssertionFailed", exception);
		// §8.3.1: "Void" is QVT-O's synonym for OclVoid, used as the return type of
		// side-effect-only helpers. Held here as one stable classifier so that every
		// occurrence refers to the same type rather than to a fresh fabrication.
		createClass(ePackage, "Void");

		return ePackage;
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
		Map<String, EClassifier> types = new java.util.HashMap<>();
		for (EClassifier classifier : PACKAGE.getEClassifiers()) {
			types.put(classifier.getName(), classifier);
		}
		return Map.copyOf(types);
	}
}
