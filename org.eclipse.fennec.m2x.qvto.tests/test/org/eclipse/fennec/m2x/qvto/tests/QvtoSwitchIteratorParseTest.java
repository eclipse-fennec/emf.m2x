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

import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for N6: switch with iterator declarator (§8.4.7).
 */
class QvtoSwitchIteratorParseTest extends AbstractQvtoParserTest {

	@Test
	void switchWithIterator_parsesSuccessfully() throws QvtoParseException {
		// §8.4.7: 'switch' ('(' <iter_declarator> ')')? <switch_body>
		OperationalTransformation t = parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    mapping doIt() {
				        switch (x := 42) {
				            case (x > 0) 'positive';
				            else 'zero';
				        };
				    }
				}
				""");
		assertNotNull(t);
	}

	@Test
	void switchWithIteratorNameOnly_parsesSuccessfully() throws QvtoParseException {
		// Eclipse LPG rule 443: switchDeclaratorCS ::= IDENTIFIER
		OperationalTransformation t = parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    mapping doIt() {
				        switch (x) {
				            case (true) 1;
				        };
				    }
				}
				""");
		assertNotNull(t);
	}

	@Test
	void switchWithIteratorAndType_parsesSuccessfully() throws QvtoParseException {
		// Full form: switch (x : Integer := expr)
		OperationalTransformation t = parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    mapping doIt() {
				        switch (x : Integer := 42) {
				            case (x > 0) 'positive';
				        };
				    }
				}
				""");
		assertNotNull(t);
	}

	@Test
	void switchWithoutIterator_stillWorks() throws QvtoParseException {
		// Regression: standard switch without iterator
		OperationalTransformation t = parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    mapping doIt() {
				        switch {
				            case (true) 1;
				        };
				    }
				}
				""");
		assertNotNull(t);
	}

	// ==================== Arrow-Switch form ====================

	@Test
	void arrowSwitchCall_parsesSuccessfully() throws QvtoParseException {
		// §8.2.2.8: coll->switch(i) { ... } = coll->xcollect(i | switch { ... })
		OperationalTransformation t = parse("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    mapping doIt(elems : Sequence(ecore::EClass)) {
				        elems->switch (x) {
				            case (x.name = 'Foo') 'found';
				            else 'not found';
				        };
				    }
				}
				""");
		assertNotNull(t);
	}
}
