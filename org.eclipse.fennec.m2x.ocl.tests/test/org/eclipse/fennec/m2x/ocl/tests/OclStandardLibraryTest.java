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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.LetExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.Test;

/**
 * The OCL standard library is one {@link EPackage} with one instance per predefined type
 * (#154) — the counterpart of Eclipse OCL's {@code oclstdlib.ecore}, possible because
 * {@code OclType} is an {@code EClassifier} now (OCL v2.4 §8.2: types are Classifiers).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclStandardLibraryTest {

	private static final OclStandardLibrary LIBRARY = OclStandardLibrary.INSTANCE;

	@Test
	void library_isARegisteredEPackage() {
		EPackage registered = EPackage.Registry.INSTANCE.getEPackage(OclStandardLibrary.NS_URI);
		assertSame(LIBRARY.ePackage(), registered, "the library is in the global registry");
		assertEquals("oclstdlib", LIBRARY.ePackage().getName());
		assertNotNull(LIBRARY.ePackage().eResource(), "a proxy pointing here resolves via eResource()");
		assertEquals(OclStandardLibrary.NS_URI, LIBRARY.ePackage().eResource().getURI().toString());
	}

	@Test
	void predefinedTypes_areTheClassifiersOfThePackage() {
		List<String> names = LIBRARY.ePackage().getEClassifiers().stream()
				.map(EClassifier::getName).toList();
		assertEquals(List.of("Boolean", "Integer", "Real", "String", "UnlimitedNatural",
				"OclAny", "OclVoid", "OclInvalid"), names);
	}

	@Test
	void primitiveType_isAnEDataTypeWithAnInstanceClass() {
		assertInstanceOf(EDataType.class, LIBRARY.integer());
		assertEquals(Long.class, LIBRARY.integer().getInstanceClass());
		assertEquals(Double.class, LIBRARY.real().getInstanceClass());
		assertEquals(String.class, LIBRARY.string().getInstanceClass());
		assertEquals(Boolean.class, LIBRARY.booleanType().getInstanceClass());
	}

	@Test
	void type_byName() {
		assertSame(LIBRARY.integer(), LIBRARY.type("Integer").orElseThrow());
		assertSame(LIBRARY.oclAny(), LIBRARY.type("OclAny").orElseThrow());
		assertSame(LIBRARY.oclVoid(), LIBRARY.type("OclVoid").orElseThrow());
		assertSame(LIBRARY.oclInvalid(), LIBRARY.type("OclInvalid").orElseThrow());
		assertEquals(Optional.empty(), LIBRARY.type("Foo"));
		assertEquals(Optional.empty(), LIBRARY.primitive("OclAny"), "OclAny is not a primitive type");
	}

	// The point of it all: a parsed expression references the library's Integer, not a fresh
	// object — and two expressions reference the same one.
	@Test
	void parsedExpressions_shareTheLibraryInstance() throws OclParseException {
		OclParserSupport support = new OclParserSupport();
		OclExpression first = support.parse("let x : Integer = 1 in x", EcorePackage.Literals.EOBJECT);
		OclExpression second = support.parse("let y : Integer = 2 in y", EcorePackage.Literals.EOBJECT);
		EClassifier typeOfX = letVariableType(first);
		EClassifier typeOfY = letVariableType(second);
		assertSame(LIBRARY.integer(), typeOfX);
		assertSame(typeOfX, typeOfY, "one Integer for the whole language");
	}

	// A reference to Integer serializes like a reference to EString: by nsURI and name, into
	// a package a fresh resource set finds in the registry. In a compiled unit the predefined
	// types are external references, not satellites.
	@Test
	void referenceToALibraryType_serializesAsAnExternalReference() {
		URI uri = EcoreUtil.getURI(LIBRARY.integer());
		assertEquals(OclStandardLibrary.NS_URI + "#//Integer", uri.toString());
		EObject resolved = new ResourceSetImpl().getEObject(uri, true);
		assertSame(LIBRARY.integer(), resolved, "a fresh resource set resolves it to the very instance");
	}

	@Test
	void everyType_isAnEClassifier() {
		for (OclType type : LIBRARY.types().values()) {
			assertInstanceOf(EClassifier.class, type);
			assertSame(LIBRARY.ePackage(), ((EClassifier) type).getEPackage());
		}
		assertTrue(LIBRARY.integer() instanceof PrimitiveType);
	}

	// ---- Helpers ----

	private static EClassifier letVariableType(OclExpression expression) {
		return ((LetExp) expression).getOwnedVariable().getType();
	}
}
