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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A relation's expressions run with the options of the OCL engine the QVT-R engine runs on
 * (#258). Until #258 the evaluator built {@code OclEvaluationOptions.lenient()} on the spot, so
 * what {@code OclConfiguration} or {@code ocl.*} configured never reached them — although the
 * user guide promised that "a supplied engine is used as it is: ... its evaluation settings".
 */
class QvtdOclOptionsTest extends AbstractQvtdEngineTest {

	/** A when-clause with an OCL range of a thousand elements. */
	private static final String RANGE_OF_A_THOUSAND = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package { name = pn };
					checkonly domain rdbms s : Schema { name = pn };
					when { Sequence{1..1000}->size() = 1000; }
				}
			}
			""";

	@Test
	@DisplayName("a collection limit configured on the OCL engine applies inside a relation")
	void defaultsOfTheOclEngine_areHonoured() throws QvtdParseException {
		QvtdExecutionResult result = run(engineWithCollectionLimit(100));

		assertTrue(hasDiagnosticContaining(result, "exceeds maximum allowed size"),
				() -> "diagnostics: " + result.diagnostics());
	}

	@Test
	@DisplayName("within the configured limit the same relation evaluates without complaint")
	void withinTheConfiguredLimit_noDiagnostic() throws QvtdParseException {
		QvtdExecutionResult result = run(engineWithCollectionLimit(1000));

		assertFalse(hasDiagnosticContaining(result, "exceeds maximum allowed size"),
				() -> "diagnostics: " + result.diagnostics());
	}

	private QvtdExecutionResult run(QvtdEngine engine) throws QvtdParseException {
		RelationalTransformation t = engine.parse(RANGE_OF_A_THOUSAND, "T");
		// Both domains have a candidate, so the relation binds and its when-clause is evaluated
		return engine.execute(t, QvtdExecutionContext.checkOnly(Map.of(
				"uml", extent(umlPackage, "Package", "root"),
				"rdbms", extent(rdbmsPackage, "Schema", "root"))));
	}

	private static QvtdEngine engineWithCollectionLimit(int maxCollectionSize) {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport())
				.maxCollectionSize(maxCollectionSize).build();
		return QvtdEngines.create(QvtdConfiguration.builder(oclConfig).build());
	}

	private static QvtdModelExtent extent(EPackage metamodel, String className, String name) {
		EClass eClass = ecoreHelper.getEClass(metamodel, className);
		EObject element = EcoreUtil.create(eClass);
		element.eSet(eClass.getEStructuralFeature("name"), name);
		return new BasicQvtdModelExtent(List.of(element));
	}

	private static boolean hasDiagnosticContaining(QvtdExecutionResult result, String substring) {
		return result.diagnostics().stream().anyMatch(d -> d.getMessage().contains(substring));
	}
}
