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

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A diagnostic reported while a transformation runs says where the expression stands (#116).
 *
 * <p>The OCL engine knows which node it stumbled over, the parser recorded where that node stood,
 * and the two meet in the evaluator. The unit is part of the position, because a transformation
 * that imports others evaluates their expressions too and a bare line names the wrong file.
 */
class QvtoRuntimeDiagnosticPositionTest extends AbstractQvtoEngineTest {

	@Test
	@DisplayName("a problem while running is reported at all")
	void runtimeDiagnosticIsForwarded() throws QvtoParseException {
		var transformation = engine.parse("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation T(inout m : ECORE);
				main() {
				    var c := m.rootObjects()![EClass];
				    log(c.eSuperTypes->first().name);
				}
				""", "positions");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent(
				EcorePackage.eINSTANCE.getEFactoryInstance().create(EcorePackage.Literals.ECLASS));
		QvtoExecutionResult result = engine.execute(transformation,
				QvtoExecutionContext.of(extent));

		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("Null source for property 'eSuperTypes'")),
				() -> "the expression's own report has to arrive: " + messages(result));
	}

	@Test
	@Disabled("#116: the position does not arrive yet — QVT-O's builders are driven by direct "
			+ "calls rather than visit(ParseTree), so the recording anchors are incomplete and no "
			+ "node of the expression is in the position map")
	@DisplayName("a problem while running names the unit, the line and the column")
	void runtimeDiagnosticCarriesItsPosition() throws QvtoParseException {
		// eSuperTypes of a fresh EClass is empty, so first() has nothing — the expression on
		// line 5 is the one that goes wrong, and the message has to say so.
		var transformation = engine.parse("""
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation T(inout m : ECORE);
				main() {
				    var c := m.rootObjects()![EClass];
				    log(c.eSuperTypes->first().name);
				}
				""", "positions");

		BasicQvtoModelExtent extent = new BasicQvtoModelExtent(
				EcorePackage.eINSTANCE.getEFactoryInstance().create(EcorePackage.Literals.ECLASS));
		QvtoExecutionResult result = engine.execute(transformation,
				QvtoExecutionContext.of(extent));

		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().startsWith("positions:5:")),
				() -> "expected the unit and line of the expression: " + messages(result));
	}

	private static List<String> messages(QvtoExecutionResult result) {
		return result.diagnostics().stream().map(d -> d.getMessage()).toList();
	}
}
