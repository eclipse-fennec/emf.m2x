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
package org.eclipse.fennec.m2x.qvto.tests.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.junit.jupiter.api.Test;

/**
 * Several resolvers for one import (#141): all are asked, a failing one is an error the caller can
 * tell from "not found", two disagreeing ones are a conflict, and a source beside the compiled
 * document of the same text is no conflict.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoMultiSourceResolutionTest {

	private static final String HELPER_LIB = """
			library HelperLib {
			    helper greet() : String {
			        return 'hello';
			    }
			}
			""";
	private static final String HELPER_LIB_V2 = HELPER_LIB.replace("'hello'", "'other'");
	private static final String MAIN = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			import HelperLib;
			transformation Main(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := greet();
			        };
			    }
			}
			""";

	@Test
	void twoResolvers_withTheSameLibrary_agree_andTheFirstWins() throws Exception {
		QvtoEngine engine = engine(serving("HelperLib", HELPER_LIB, "mem:/first"), serving("HelperLib", HELPER_LIB, "mem:/second"));
		CompiledUnit compiled = engine.compile(MAIN, "Main");
		assertEquals(1, compiled.getManifest().getDependencyEntry().size());
		assertEquals("hello", runOn(engine, (OperationalTransformation) engine.parse(MAIN, "Main")));
	}

	@Test
	void twoResolvers_withDifferentLibraries_areAConflict_atCompileAndAtLink() throws Exception {
		QvtoEngine engine = engine(serving("HelperLib", HELPER_LIB, "mem:/first"), serving("HelperLib", HELPER_LIB_V2, "mem:/second"));

		QvtoParseException failure = assertThrows(QvtoParseException.class, () -> engine.compile(MAIN, "Main"));
		assertTrue(failure.getMessage().contains("disagree"), failure.getMessage());

		QvtoExecutionResult result = execute(engine, engine.parse(MAIN, "Main"));
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("disagree")),
				() -> "the link reports the conflict: " + result.diagnostics());
	}

	@Test
	void aFailingResolver_isAnError_notNotFound_evenWhenAnotherHasTheLibrary() throws Exception {
		QvtoUnitResolver broken = name -> {
			throw new IllegalStateException("disk on fire");
		};
		QvtoEngine engine = engine(broken, serving("HelperLib", HELPER_LIB, "mem:/copy"));

		QvtoParseException failure = assertThrows(QvtoParseException.class, () -> engine.compile(MAIN, "Main"));
		assertTrue(failure.getMessage().contains("disk on fire"), failure.getMessage());
		assertTrue(failure.getMessage().contains("failed"), failure.getMessage());

		QvtoExecutionResult result = execute(engine, engine.parse(MAIN, "Main"));
		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getMessage().contains("disk on fire")),
				() -> "the link names the failing source: " + result.diagnostics());
	}

	@Test
	void aSourceAndTheCompiledDocumentOfTheSameText_agree() throws Exception {
		QvtoEngine plain = engine();
		CompiledUnit library = plain.compile(HELPER_LIB, "HelperLib");
		QvtoUnitResolver document = name -> "HelperLib".equals(name)
				? Optional.of(new QvtoUnit.CompiledUnit("HelperLib", (OperationalTransformation) library.getUnit()))
				: Optional.empty();
		QvtoEngine agreeing = engine(serving("HelperLib", HELPER_LIB, "mem:/text"), document);
		assertEquals(library.getManifest().getUnitFingerprint(),
				agreeing.compile(MAIN, "Main").getManifest().getDependencyEntry().get(0).getFingerprint());

		QvtoEngine disagreeing = engine(serving("HelperLib", HELPER_LIB_V2, "mem:/text"), document);
		assertTrue(assertThrows(QvtoParseException.class, () -> disagreeing.compile(MAIN, "Main"))
				.getMessage().contains("source fingerprint"));
	}

	// ==== helpers ====

	private static QvtoUnitResolver serving(String unit, String text, String uri) {
		return name -> unit.equals(name) ? Optional.of(new QvtoUnit.SourceUnit(name, URI.createURI(uri), text)) : Optional.empty();
	}

	private static QvtoEngine engine(QvtoUnitResolver... resolvers) {
		QvtoConfiguration.Builder builder = QvtoConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build());
		for (QvtoUnitResolver resolver : resolvers) {
			builder.addUnitResolver(resolver);
		}
		return QvtoEngines.create(builder.unitResolverEnabled(resolvers.length > 0).build());
	}

	private static QvtoExecutionResult execute(QvtoEngine engine, OperationalTransformation transformation) {
		EObject pkg = EcoreUtil.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");
		return engine.execute(transformation, QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg))));
	}

	private static String runOn(QvtoEngine engine, OperationalTransformation transformation) {
		EObject pkg = EcoreUtil.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");
		QvtoExecutionResult result = engine.execute(transformation, QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg))));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		return (String) pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME);
	}
}
