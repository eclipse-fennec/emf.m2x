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

import java.util.Set;
import java.util.function.UnaryOperator;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The engine can find a unit resolver nobody handed it.
 *
 * <p>A transformation names what it imports, so the engine knows at link time which name it
 * needs. Discovery lets an implementation answer for that name without any configuration
 * mentioning it — {@link java.util.ServiceLoader} here, the service registry under OSGi.
 *
 * <p>{@link DiscoveredTestUnitResolver} is declared in {@code META-INF/services} and named
 * in no configuration, so an import it satisfies can only have been resolved by discovery.
 *
 * <p>It is a decision, not a default. {@code unitResolverEnabled} alone leaves an empty
 * allow-list, which permits every name — so turning discovery on there would quietly let
 * anything on the class path answer an import (see {@code QvtoAllowListSemanticsTest}).
 */
class QvtoUnitDiscoveryTest {

	private static final String TRANSFORMATION = """
			modeltype ECORE uses '%s';
			import discovered.library;
			transformation discovery(in source : ECORE, out target : ECORE) {
			    main() {
			    }
			}
			""".formatted(EcorePackage.eNS_URI);

	@BeforeAll
	static void registerEcore() {
		EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
	}

	@Test
	@DisplayName("a resolver on the class path answers an import no configuration mentions")
	void discoveredResolverAnswers() throws Exception {
		QvtoEngine engine = engine(builder -> builder
				.unitResolverEnabled(true)
				.discoverUnitResolvers(true));

		assertTrue(run(engine).isSuccess());
	}

	@Test
	@DisplayName("without the opt-in nothing is discovered")
	void discoveryIsOff() throws Exception {
		QvtoEngine engine = engine(builder -> builder.unitResolverEnabled(true));

		assertFalse(run(engine).isSuccess(),
				"the same resolver is on the class path — only the opt-in is missing");
	}

	@Test
	@DisplayName("discovery widens who may answer, not which names may be asked")
	void theAllowListStillNarrows() throws Exception {
		QvtoEngine engine = engine(builder -> builder
				.unitResolverEnabled(true)
				.discoverUnitResolvers(true)
				.allowedUnitModules(Set.of("something.else")));

		assertFalse(run(engine).isSuccess(),
				"the name is checked before any resolver, discovered or configured, is asked");
	}

	@Test
	@DisplayName("the enable flag still gates it")
	void disabledResolversMeanNoDiscovery() throws Exception {
		QvtoEngine engine = engine(builder -> builder.discoverUnitResolvers(true));

		assertFalse(run(engine).isSuccess(),
				"discovery is a source of resolvers, not a way around the gate");
	}

	// --- helpers ---

	private static QvtoEngine engine(UnaryOperator<QvtoConfiguration.Builder> customizer) {
		return QvtoEngines.create(customizer.apply(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())).build());
	}

	private static QvtoExecutionResult run(QvtoEngine engine) throws Exception {
		return engine.execute(engine.parse(TRANSFORMATION, "discovery"),
				QvtoExecutionContext.of(new BasicQvtoModelExtent(), new BasicQvtoModelExtent()));
	}
}
