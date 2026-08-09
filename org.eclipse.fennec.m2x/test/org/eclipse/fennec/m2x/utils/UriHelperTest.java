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
package org.eclipse.fennec.m2x.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.nio.file.Path;

import org.eclipse.emf.common.util.URI;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The conversions callers need to reach the EMF URI the engines take.
 */
class UriHelperTest {

	@Test
	@DisplayName("a java.net.URI keeps its encoded form")
	void javaUriKeepsItsEncoding() {
		java.net.URI source = java.net.URI.create("file:/srv/my%20templates/report.mtl");

		URI converted = UriHelper.fromJavaUri(source);

		assertEquals("file:/srv/my%20templates/report.mtl", converted.toString());
		assertEquals("report.mtl", converted.lastSegment());
	}

	@Test
	@DisplayName("a path becomes an absolute file URI")
	void pathBecomesAFileUri() {
		URI converted = UriHelper.fromPath(Path.of("templates", "report.mtl"));

		assertTrue(converted.isFile(), () -> "expected a file URI, got " + converted);
		assertTrue(converted.toFileString().endsWith("report.mtl"), converted::toString);
		assertTrue(Path.of(converted.toFileString()).isAbsolute(),
				() -> "a relative path would resolve against the working directory: " + converted);
	}

	@Test
	@DisplayName("a file goes the same way as its path")
	void fileGoesThroughThePath() {
		File file = new File("templates/report.mtl");

		assertEquals(UriHelper.fromPath(file.toPath()), UriHelper.fromFile(file));
	}

	@Test
	@DisplayName("an EMF URI converts back for APIs that speak java.net.URI")
	void emfUriConvertsBack() {
		URI source = URI.createURI("platform:/plugin/com.acme/templates/report.mtl");

		assertEquals(source.toString(), UriHelper.toJavaUri(source).toString());
	}

	@Test
	@DisplayName("null is rejected rather than turned into a broken URI")
	void nullIsRejected() {
		assertThrows(NullPointerException.class, () -> UriHelper.fromJavaUri(null));
		assertThrows(NullPointerException.class, () -> UriHelper.fromPath(null));
		assertThrows(NullPointerException.class, () -> UriHelper.fromFile(null));
		assertThrows(NullPointerException.class, () -> UriHelper.toJavaUri(null));
	}
}
