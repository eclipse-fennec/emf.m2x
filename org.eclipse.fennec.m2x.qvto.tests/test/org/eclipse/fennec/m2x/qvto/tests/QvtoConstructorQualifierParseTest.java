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

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for N8: qualifier before constructor (§8.4.7).
 * Spec EBNF: {@code <constructor_header> ::= <qualifier>* 'constructor' <scoped_identifier> <simple_signature>}
 */
class QvtoConstructorQualifierParseTest extends AbstractQvtoParserTest {

	@Test
	void blackboxConstructor_parsesSuccessfully() throws QvtoParseException {
		// §8.4.7: qualifier* constructor ...
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC);
				blackbox constructor SourceElement::SourceElement(n : String);
				""");
		assertNotNull(t);
	}

	@Test
	void blackboxConstructor_setsIsBlackbox() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC);
				blackbox constructor SourceElement::SourceElement(n : String);
				""");
		Constructor ctor = getModuleClass(t).getEOperations().stream()
				.filter(Constructor.class::isInstance)
				.map(Constructor.class::cast)
				.findFirst()
				.orElseThrow(() -> new AssertionError("No constructor found"));
		assertTrue(ctor.isIsBlackbox(), "Constructor should be marked as blackbox");
	}

	@Test
	void constructorWithoutQualifier_stillWorks() throws QvtoParseException {
		// Regression: existing form must still work
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC);
				constructor SourceElement::SourceElement(n : String) {
				    name := n;
				}
				""");
		assertNotNull(t);
	}

	@Test
	void constructorDeclaration_withoutBody_parsesSuccessfully() throws QvtoParseException {
		// §8.4.7: <constructor_decl> ::= <constructor_header> ';'
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC);
				constructor SourceElement::SourceElement(n : String);
				""");
		Constructor ctor = getModuleClass(t).getEOperations().stream()
				.filter(Constructor.class::isInstance)
				.map(Constructor.class::cast)
				.findFirst()
				.orElseThrow(() -> new AssertionError("No constructor found"));
		assertNotNull(ctor);
	}

	@Test
	void constructorWithPackageScope_parsesSuccessfully() throws QvtoParseException {
		// Eclipse pattern: xhtml::TdType::TdType (package::Type::CtorName)
		// §8.4.7: <scoped_identifier> ::= <identifier> ('::' <identifier>)*
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC);
				constructor source::SourceElement::SourceElement(n : String) {
				    name := n;
				}
				""");
		assertNotNull(t);
	}

	@Test
	void blackboxConstructorWithPackageScope_parsesAndSetsBlackbox() throws QvtoParseException {
		// Combined: qualifier + package-scoped constructor declaration
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(inout m : SRC);
				blackbox constructor source::SourceElement::SourceElement(n : String);
				""");
		Constructor ctor = getModuleClass(t).getEOperations().stream()
				.filter(Constructor.class::isInstance)
				.map(Constructor.class::cast)
				.findFirst()
				.orElseThrow(() -> new AssertionError("No constructor found"));
		assertTrue(ctor.isIsBlackbox(), "Constructor should be marked as blackbox");
	}
}
