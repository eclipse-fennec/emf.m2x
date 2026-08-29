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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.stream.Stream;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Where reflection is allowed to live in the OCL engine (#179).
 *
 * <p>The security analysis said "no reflection, by design", and that was not true:
 * {@code PropertyAccessorCache} builds property accessors with {@code MethodHandles},
 * {@code Class.getMethod} and {@code LambdaMetafactory}. It is not attacker-steerable — the
 * getter name is derived from the metamodel feature, never from expression text — but a
 * security document that describes the code has to describe it correctly, and the sentence
 * only stays correct if the boundary it draws is held.
 *
 * <p>So: reflection in exactly one class, and a new use anywhere else fails here. The scan is
 * skipped where the sources are not beside the tests (a bundle running alone), which is why
 * it also asserts that it found the file it expects.
 */
class OclReflectionBoundaryTest {

	/** Package-relative directories of the OCL bundles, seen from the tests project. */
	private static final List<String> SOURCE_ROOTS = List.of(
			"../org.eclipse.fennec.m2x.ocl.api/src",
			"../org.eclipse.fennec.m2x.ocl.parser/src",
			"../org.eclipse.fennec.m2x.ocl.engine/src");

	/** The one class that may use them. */
	private static final String ALLOWED = "PropertyAccessorCache.java";

	private static final List<String> REFLECTION_APIS = List.of(
			"java.lang.reflect.Method", "MethodHandles.", "LambdaMetafactory.",
			"Class.forName(", ".setAccessible(", ".getDeclaredMethod(", ".getMethod(");

	@Test
	@DisplayName("only PropertyAccessorCache reflects")
	void reflectionLivesInOnePlace() throws IOException {
		Set<String> found = new TreeSet<>();
		int scanned = 0;
		for (String root : SOURCE_ROOTS) {
			Path directory = Path.of(root);
			assumeTrue(Files.isDirectory(directory), () -> "sources are not beside the tests: " + root);
			try (Stream<Path> files = Files.walk(directory)) {
				for (Path file : files.filter(p -> p.toString().endsWith(".java")).toList()) {
					scanned++;
					if (usesReflection(file)) {
						found.add(file.getFileName().toString());
					}
				}
			}
		}

		assertEquals(Set.of(ALLOWED), found,
				"reflection belongs in one place, and the security analysis says which");
		assertEquals(true, scanned > 50, "the scan has to have seen the sources: " + scanned);
	}

	/** Code lines only — a javadoc mentioning {@code LambdaMetafactory} is not a use. */
	private static boolean usesReflection(Path file) throws IOException {
		for (String line : Files.readAllLines(file)) {
			String code = line.strip();
			if (code.startsWith("*") || code.startsWith("//") || code.startsWith("/*")) {
				continue;
			}
			if (REFLECTION_APIS.stream().anyMatch(code::contains)) {
				return true;
			}
		}
		return false;
	}
}
