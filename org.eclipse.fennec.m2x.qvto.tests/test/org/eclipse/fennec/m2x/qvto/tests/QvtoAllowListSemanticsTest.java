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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * What an empty allow-list means (D29).
 *
 * <p>It means <em>no restriction</em>, not <em>nothing allowed</em>:
 * {@code QvtoLinker} skips the check when the set is empty. The enable flag is the gate;
 * the allow-list narrows what may pass once the gate is open.
 *
 * <p>Pinned here because the difference is the whole security posture, and because it is
 * easy to write the opposite down: the OSGi attribute descriptions and the user guide both
 * said "empty allows none" for a while, which would have someone leave the list empty
 * believing it closed.
 */
class QvtoAllowListSemanticsTest {

	private static final String TRANSFORMATION = """
			modeltype ECORE uses '%s';
			import some.library;
			transformation allowList(in source : ECORE, out target : ECORE) {
			    main() {
			    }
			}
			""".formatted(EcorePackage.eNS_URI);

	@BeforeAll
	static void registerEcore() {
		EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
	}

	@Test
	@DisplayName("an empty allow-list restricts nothing once resolvers are enabled")
	void emptyMeansNoRestriction() throws Exception {
		assertTrue(run(Set.of()).isSuccess(),
				"an empty list is not a closed door — the import resolves");
	}

	@Test
	@DisplayName("a name on the allow-list passes")
	void listedNameResolves() throws Exception {
		assertTrue(run(Set.of("some.library")).isSuccess());
	}

	@Test
	@DisplayName("a name not on a non-empty allow-list is refused")
	void unlistedNameIsRefused() throws Exception {
		assertFalse(run(Set.of("other.library")).isSuccess(),
				"the list is what narrows, and this name is not on it");
	}

	@Test
	@DisplayName("the enable flag is the gate, and it is shut by default")
	void disabledResolversRefuseEvenAnAllowedName() throws Exception {
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(library())
				.allowedUnitModules(Set.of("some.library"))
				// unitResolverEnabled not set — defaults to false
				.build());

		assertFalse(execute(engine).isSuccess());
	}

	// --- helpers ---

	private static QvtoUnitResolver library() {
		return name -> Optional.of(new QvtoUnit.SourceUnit(name,
				URI.createURI("mem:/" + name + ".qvto"), "library some.library;\n"));
	}

	private static QvtoExecutionResult run(Set<String> allowed) throws Exception {
		return execute(QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(library())
				.unitResolverEnabled(true)
				.allowedUnitModules(allowed)
				.build()));
	}

	private static QvtoExecutionResult execute(QvtoEngine engine) throws Exception {
		return engine.execute(engine.parse(TRANSFORMATION, "allowList"),
				QvtoExecutionContext.of(new BasicQvtoModelExtent(), new BasicQvtoModelExtent()));
	}
}
