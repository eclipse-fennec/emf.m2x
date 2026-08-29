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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The OCL expressions QVT-O evaluates itself rather than handing to the OCL engine (#193).
 *
 * <p>They used to be an {@code instanceof} chain in {@code eval}, which meant a new expression
 * type had to be added in two places and would otherwise be silently half-implemented — handed
 * to the OCL engine, which does not know QVT-O's semantics. They are one generated switch now,
 * and these are the six behaviours that switch is responsible for.
 */
class QvtoDispatchTest extends AbstractQvtoEngineTest {

	@Test
	@DisplayName("an extent operation is QVT-O's, not OCL's")
	void extentOperation() throws QvtoParseException {
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    main() {
				        log(m.objectsOfType(EPackage)->size().repr());
				    }
				}
				""", new BasicQvtoModelExtent(
						org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEPackage()));

		assertLogged(result, "1");
	}

	@Test
	@DisplayName("an element operation is QVT-O's")
	void elementOperation() throws QvtoParseException {
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    main() {
				        m.objectsOfType(EPackage)->forEach(p) { log(p.metaClassName()); };
				    }
				}
				""", new BasicQvtoModelExtent(
						org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEPackage()));

		assertLogged(result, "EPackage");
	}

	@Test
	@DisplayName("an iterator over an extent operation reaches the extent")
	void iteratorOverAnExtentOperation() throws QvtoParseException {
		QvtoExecutionResult result = executeWithExtents("""
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				transformation T(inout m : ECORE) {
				    main() {
				        log(m.objectsOfType(EPackage)->select(p | true)->size().repr());
				    }
				}
				""", new BasicQvtoModelExtent(
						org.eclipse.emf.ecore.EcoreFactory.eINSTANCE.createEPackage()));

		assertLogged(result, "1");
	}

	@Test
	@DisplayName("an if with an imperative branch is evaluated by QVT-O")
	void ifWithAnImperativeBranch() throws QvtoParseException {
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var x : String := if true then 'yes' else 'no' endif;
					log(x);
				}
				""");

		assertLogged(result, "yes");
	}

	@Test
	@DisplayName("null reads as the empty string in a concatenation")
	void nullConcatenatesAsEmpty() throws QvtoParseException {
		// QVT-O's reading, where OCL's strict mode refuses the argument
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var missing : String := null;
					log('x' + missing);
				}
				""");

		assertLogged(result, "x");
	}

	@Test
	@DisplayName("an imperative expression as a value is evaluated by QVT-O")
	void imperativeExpressionAsAValue() throws QvtoParseException {
		// A compute is an imperative expression, so the OCL engine cannot evaluate it — which
		// is why this dispatch exists. Calling a *standard-library* operation on one still does
		// not work (the call falls through to OCL, which then meets the compute): pre-existing,
		// unchanged here, and not what this switch is responsible for.
		QvtoExecutionResult result = execute("""
				transformation T();
				main() {
					var count : Integer := 0;
					var text : String := compute (r : String) { count := count + 1; r := 'a'; };
					log(text);
					log(count.repr());
				}
				""");

		assertLogged(result, "a");
		assertLogged(result, "1");
	}

	// --- helpers ---

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(expected)),
				() -> "expected a log of " + expected + ", got " + result.diagnostics());
	}

	private static QvtoExecutionResult executeWithExtents(String source, QvtoModelExtent extent)
			throws QvtoParseException {
		return execute(source, org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext.of(extent));
	}
}
