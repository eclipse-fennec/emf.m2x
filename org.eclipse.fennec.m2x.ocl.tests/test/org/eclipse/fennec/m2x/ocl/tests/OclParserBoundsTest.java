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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Timeout;

/**
 * What the parser does with input that is not an expression but an attack: too long, or nested
 * deeper than the recursion can take (#181).
 *
 * <p>The evaluator's limits protect evaluation; nothing protected parsing, and both ANTLR and the
 * AST visitor recurse — so a deeply nested expression ended in a `StackOverflowError`, an `Error`
 * that no `catch (Exception)` in the engines sees. Every failure of the parser is an
 * `OclParseException` now, and a length bound keeps most of it from being reached at all.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclParserBoundsTest extends AbstractOclTest {

	private static OclParserSupport parser() {
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(companyPackage.getNsURI(), companyPackage);
		return new OclParserSupport(registry);
	}

	@Test
	void inputLongerThanTheLimit_isRejectedBeforeParsing() {
		OclParserSupport parser = parser().maxInputLength(100);
		String longInput = "self.name.size()" + " + 1".repeat(200);

		OclParseException failure = assertThrows(OclParseException.class,
				() -> parser.parse(longInput, personClass));
		assertTrue(failure.getMessage().contains("longer than 100 characters"), failure.getMessage());
	}

	@Test
	void inputWithinTheLimit_parses() {
		assertDoesNotThrow(() -> parser().maxInputLength(100).parse("self.name.size() > 0", personClass));
	}

	@Test
	@Timeout(30)
	void deeplyNestedInput_isAParseException_notAStackOverflow() {
		// 20 000 levels: comfortably past the default stack, well inside the length bound
		String nested = "(".repeat(20_000) + "1" + ")".repeat(20_000);

		OclParseException failure = assertThrows(OclParseException.class,
				() -> parser().parse(nested, personClass),
				"a StackOverflowError would escape every catch (Exception) in the engines");
		assertTrue(failure.getMessage() != null && !failure.getMessage().isBlank(), "with a message");
	}

	@Test
	@Timeout(30)
	void deeplyNestedDocument_isAParseException_too() {
		String nested = "package company context Person inv deep: "
				+ "(".repeat(20_000) + "true" + ")".repeat(20_000) + " endpackage";
		assertThrows(OclParseException.class, () -> parser().parseDocument(nested));
	}

	@Test
	void maxInputLength_mustBePositive() {
		assertThrows(IllegalArgumentException.class, () -> parser().maxInputLength(0));
	}
}
