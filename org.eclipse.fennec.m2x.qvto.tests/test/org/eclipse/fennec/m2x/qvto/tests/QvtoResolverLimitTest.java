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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

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
 * {@code maxUnitResolvers} is a limit, not a note.
 *
 * <p>It is offered by the builder, by the OSGi configuration and by the user guide, all of
 * which present it as a security setting (D29). No engine code read it: a configuration
 * could name any number of resolvers and every one of them was consulted. The value existed
 * only to be stored and read back, which is why the security tests passed while asserting
 * nothing about it.
 *
 * <p>What the limit is for: every resolver is code that gets asked to produce a compilation
 * unit for a name a transformation chose. Bounding how many of them can be reached bounds
 * how far one {@code import} can travel.
 */
class QvtoResolverLimitTest {

	private static final String TRANSFORMATION = """
			modeltype ECORE uses '%s';
			import unknown.library;
			transformation limited(in source : ECORE, out target : ECORE) {
			    main() {
			    }
			}
			""".formatted(EcorePackage.eNS_URI);

	@BeforeAll
	static void registerEcore() {
		EPackage.Registry.INSTANCE.put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
	}

	@Test
	@DisplayName("no more resolvers are consulted than the limit allows")
	void limitCapsHowManyResolversAreAsked() throws Exception {
		AtomicInteger asked = new AtomicInteger();
		List<QvtoUnitResolver> resolvers = countingResolvers(10, asked);

		QvtoEngine engine = engine(resolvers, 3);

		// The import cannot be satisfied — what matters is how far the engine got trying.
		assertFalse(run(engine).isSuccess());
		assertEquals(3, asked.get(),
				"the fourth resolver is past the limit and must not be reached");
	}

	@Test
	@DisplayName("a configuration within the limit is untouched")
	void resolversWithinTheLimitAreAllAsked() throws Exception {
		AtomicInteger asked = new AtomicInteger();
		List<QvtoUnitResolver> resolvers = countingResolvers(3, asked);

		QvtoEngine engine = engine(resolvers, 5);

		assertFalse(run(engine).isSuccess());
		assertEquals(3, asked.get(), "three resolvers, a limit of five — all three are asked");
	}

	@Test
	@DisplayName("the limit is a ceiling on reach, not a reason to fail the build")
	void aResolverWithinTheLimitStillResolves() throws Exception {
		AtomicInteger asked = new AtomicInteger();
		List<QvtoUnitResolver> resolvers = new ArrayList<>(countingResolvers(2, asked));
		resolvers.add(name -> Optional.of(new QvtoUnit.SourceUnit(name,
				URI.createURI("mem:/" + name + ".qvto"), """
				library unknown.library;
				""")));

		QvtoEngine engine = engine(resolvers, 5);

		assertTrue(run(engine).isSuccess(), "the third resolver answers, and it is within the limit");
		assertEquals(2, asked.get(), "the two before it were asked, in order");
	}

	// --- helpers ---

	private static List<QvtoUnitResolver> countingResolvers(int count, AtomicInteger asked) {
		List<QvtoUnitResolver> resolvers = new ArrayList<>(count);
		for (int i = 0; i < count; i++) {
			resolvers.add(name -> {
				asked.incrementAndGet();
				return Optional.empty();
			});
		}
		return resolvers;
	}

	private static QvtoEngine engine(List<QvtoUnitResolver> resolvers, int maxUnitResolvers) {
		return QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.unitResolvers(resolvers)
				.unitResolverEnabled(true)
				.maxUnitResolvers(maxUnitResolvers)
				.build());
	}

	private static QvtoExecutionResult run(QvtoEngine engine) throws Exception {
		return engine.execute(engine.parse(TRANSFORMATION, "limited"),
				QvtoExecutionContext.of(new BasicQvtoModelExtent(), new BasicQvtoModelExtent()));
	}
}
