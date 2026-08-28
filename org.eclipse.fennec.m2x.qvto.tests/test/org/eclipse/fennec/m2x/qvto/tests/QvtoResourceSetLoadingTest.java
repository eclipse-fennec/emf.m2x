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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.URIHandler;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.resource.impl.URIHandlerImpl;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.engine.ResourceSetUnitResolver;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * {@code parse(URI)} and imported units are read through the resource set's URI
 * converter, so anything the resource set can reach is reachable — not just files.
 *
 * <p>The tests serve their content from an in-memory {@link URIHandler} under a scheme
 * that has no file system behind it. If the engine still went through
 * {@code Files.readString}, none of this could load.
 */
class QvtoResourceSetLoadingTest {

	private static final String TRANSFORMATION = """
			transformation loaded() {
			    main() {
			    }
			}
			""";

	@Test
	@DisplayName("a transformation is read through the URI converter, not the file system")
	void parseUriGoesThroughTheUriConverter() throws QvtoParseException {
		ResourceSet resourceSet = resourceSetServing(Map.of(
				"mem:/transforms/loaded.qvto", TRANSFORMATION));

		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.resourceSet(resourceSet)
				.build());

		OperationalTransformation t =
				engine.parse(URI.createURI("mem:/transforms/loaded.qvto"));

		assertNotNull(t);
		assertEquals("loaded", t.getName());
	}

	@Test
	@DisplayName("an imported unit is resolved below the base URI")
	void unitResolverReadsThroughTheResourceSet() {
		ResourceSet resourceSet = resourceSetServing(Map.of(
				"mem:/units/my/company/Helpers.qvto", "library my.company.Helpers;"));

		ResourceSetUnitResolver resolver =
				new ResourceSetUnitResolver(resourceSet, URI.createURI("mem:/units/"));

		Optional<QvtoUnit> unit = resolver.resolveUnit("my.company.Helpers");

		assertTrue(unit.isPresent(), "the unit must be found below the base URI");
		QvtoUnit.SourceUnit source = (QvtoUnit.SourceUnit) unit.get();
		assertEquals("my.company.Helpers", source.qualifiedName());
		assertTrue(source.source().contains("library my.company.Helpers"), source.source());
	}

	@Test
	@DisplayName("an unknown unit is answered with empty, so the next resolver gets its turn")
	void unknownUnitYieldsEmpty() {
		ResourceSetUnitResolver resolver = new ResourceSetUnitResolver(
				resourceSetServing(Map.of()), URI.createURI("mem:/units/"));

		assertTrue(resolver.resolveUnit("no.such.Unit").isEmpty());
	}

	// --- helpers ---

	/**
	 * A resource set that serves the given URIs from memory and nothing else.
	 */
	private static ResourceSet resourceSetServing(Map<String, String> contents) {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getURIConverter().getURIHandlers().add(0, new URIHandlerImpl() {

			@Override
			public boolean canHandle(URI uri) {
				return "mem".equals(uri.scheme());
			}

			@Override
			public InputStream createInputStream(URI uri, Map<?, ?> options) throws java.io.IOException {
				String content = contents.get(uri.toString());
				if (content == null) {
					throw new java.io.FileNotFoundException(uri.toString());
				}
				return new ByteArrayInputStream(content.getBytes(StandardCharsets.UTF_8));
			}

			@Override
			public OutputStream createOutputStream(URI uri, Map<?, ?> options) throws java.io.IOException {
				throw new java.io.IOException("read only");
			}
		});
		return resourceSet;
	}
}
