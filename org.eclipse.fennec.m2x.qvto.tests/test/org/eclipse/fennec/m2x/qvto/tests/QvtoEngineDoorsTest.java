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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The three ways to get a QVT-O engine.
 *
 * <p>A caller should only have to know the door that fits, and writing a transformation
 * should not require knowing that OCL is involved at all. That is door 1: the configuration
 * carries no OCL side, and the factory supplies a default engine. Door 2 runs on an engine
 * that already exists — the injected service under OSGi. Door 3 configures the OCL side too.
 */
class QvtoEngineDoorsTest {

	private static final String TRANSFORMATION = """
			modeltype ECORE uses '%s';
			transformation doors(in source : ECORE, out target : ECORE) {
			    main() {
			    }
			}
			""".formatted(EcorePackage.eNS_URI);

	@BeforeAll
	static void registerEcore() {
		// The transformation names Ecore as its metamodel and no registry is configured
		// here, so the static one has to know it (D42).
		EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
	}

	@Test
	@DisplayName("door 1: no OCL knowledge — QVT-O settings alone are enough")
	void withoutAnyOclKnowledge() throws Exception {
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder().build());

		assertNotNull(engine.parse(TRANSFORMATION, "doors"));
		assertNotNull(engine.getOclEngine(), "the factory has to supply one");
	}

	@Test
	@DisplayName("door 2: the engine it was given is the engine that runs")
	void withASuppliedEngine() throws Exception {
		OclEngine ocl = OclEngines.create(new OclParserSupport());

		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(ocl).build());

		assertSame(ocl, engine.getOclEngine(),
				"a second engine next to the configured one is the bug this closes");
		assertNotNull(engine.parse(TRANSFORMATION, "doors"));
	}

	@Test
	@DisplayName("door 3: the OCL side configured too")
	void withAnOclConfiguration() throws Exception {
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).maxDepth(64).build()).build());

		assertNotNull(engine.parse(TRANSFORMATION, "doors"));
		assertNotNull(engine.getOclEngine());
	}

	@Test
	@DisplayName("each engine gets its own OCL engine unless one is handed in")
	void defaultEnginesAreNotShared() {
		QvtoEngine first = QvtoEngines.create(QvtoConfiguration.builder().build());
		QvtoEngine second = QvtoEngines.create(QvtoConfiguration.builder().build());

		assertTrue(first.getOclEngine() != second.getOclEngine(),
				"sharing one would share its caches between unrelated callers");
	}
}
