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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for N5: object expression with optional iterator (§8.4.7).
 */
class QvtoObjectIteratorParseTest extends AbstractQvtoParserTest {

	@Test
	void objectExp_withIterator_parsesSuccessfully() throws QvtoParseException {
		// §8.4.7: 'object' ('(' <iter_declarator> ')')? <object_declarator> <expression_block>
		OperationalTransformation t = parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    mapping doIt() {
				        object (x : ecore::EClass) ecore::EPackage {};
				    }
				}
				""");
		assertNotNull(t);
	}

	@Test
	void objectExp_withIteratorAndInit_parsesSuccessfully() throws QvtoParseException {
		// iter_declarator with init expression
		OperationalTransformation t = parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    mapping doIt() {
				        object (x : ecore::EClass := null) ecore::EPackage {};
				    }
				}
				""");
		assertNotNull(t);
	}

	@Test
	void objectExp_withoutIterator_stillWorks() throws QvtoParseException {
		// Regression: standard object without iterator
		OperationalTransformation t = parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    mapping doIt() {
				        object ecore::EPackage {};
				    }
				}
				""");
		assertNotNull(t);
	}

	@Test
	void objectExp_withNamedVarAndIterator_parsesSuccessfully() throws QvtoParseException {
		// Full form: object (iter : Type) name : Type { ... }
		OperationalTransformation t = parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    mapping doIt() {
				        object (x : ecore::EClass) p : ecore::EPackage {};
				    }
				}
				""");
		assertNotNull(t);
	}
}
