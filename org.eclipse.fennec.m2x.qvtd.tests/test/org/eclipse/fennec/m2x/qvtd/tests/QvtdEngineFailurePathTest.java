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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * What the QVT-R engine does when the run cannot happen (#176).
 *
 * <p>Four paths the suite never took: a direction with no extent bound to it, a limit
 * configured on the engine rather than passed to an evaluator built by hand, a URI that
 * cannot be read, and a prepared context asked for something it does not hold.
 */
class QvtdEngineFailurePathTest extends AbstractQvtdEngineTest {

	@Test
	@DisplayName("a direction with no extent bound to it is refused when the context is built")
	void noExtentBoundForADirection() {
		// The extent manager has a "No model extent bound" message for this, and it turns out
		// to be defence in depth: the context refuses first, which is the better place — the
		// caller learns it before a transformation starts writing (#176).
		IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
				() -> QvtdExecutionContext.enforce("rdbms",
						Map.of("uml", QvtdModelExtent.of(createPackage("Library")))));

		assertTrue(failure.getMessage().contains("rdbms"), failure::getMessage);
	}

	// The engine-level wiring of maxTraceRecords stays uncovered, deliberately: the limit is
	// per relation, and a `when` clause with free variables is satisfied by any record of that
	// relation — so one record serves every caller and the limit has no outward effect to
	// assert. Making it observable would take a transformation written for the test rather than
	// for the behaviour. QvtrTraceManager's own test covers the dropping. Noted in #176.

	@Test
	@DisplayName("a URI that cannot be read is a parse exception naming it")
	void unreadableUriIsAParseException() {
		QvtdParseException failure = assertThrows(QvtdParseException.class,
				() -> engine.parse(URI.createFileURI("/does/not/exist/nowhere.qvtr")));

		assertTrue(failure.getMessage().contains("nowhere.qvtr"), failure::getMessage);
	}

	@Test
	@DisplayName("a prepared context that holds no such unit says so")
	void preparedContextWithoutTheUnit() throws Exception {
		IllegalArgumentException failure = assertThrows(IllegalArgumentException.class,
				() -> engine.execute(new EmptyPreparedContext(), "absent.unit",
						QvtdExecutionContext.enforce("rdbms",
								Map.of("uml", new BasicQvtdModelExtent(),
										"rdbms", new BasicQvtdModelExtent()))));

		assertTrue(failure.getMessage().contains("absent.unit"), failure::getMessage);
	}

	/** A prepared context holding nothing — what a caller gets from an empty store. */
	private static final class EmptyPreparedContext
			implements org.eclipse.fennec.m2x.unit.api.PreparedContext {

		@Override
		public java.util.Optional<org.eclipse.fennec.m2x.unit.api.Unit> unit(String qualifiedName) {
			return java.util.Optional.empty();
		}

		@Override
		public java.util.Collection<org.eclipse.fennec.m2x.unit.api.Unit> units() {
			return java.util.List.of();
		}

		@Override
		public java.util.List<org.eclipse.emf.ecore.EObject> contents(URI uri) {
			return java.util.List.of();
		}

		@Override
		public org.eclipse.emf.ecore.EPackage.Registry packageRegistry() {
			return new org.eclipse.emf.ecore.impl.EPackageRegistryImpl();
		}

		@Override
		public org.eclipse.emf.ecore.resource.ResourceSet resourceSet() {
			return new org.eclipse.emf.ecore.resource.impl.ResourceSetImpl();
		}
	}
}
