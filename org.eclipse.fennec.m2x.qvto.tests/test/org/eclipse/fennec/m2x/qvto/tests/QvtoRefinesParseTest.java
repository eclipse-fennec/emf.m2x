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

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.Test;

/**
 * Tests for GAP-13: refines keyword (§8.4.7).
 * <ul>
 *   <li>{@code <transformation_refine> ::= 'refines' <moduleref>}</li>
 *   <li>{@code <mapping_refinement> ::= 'refines' <scoped_identifier>}</li>
 * </ul>
 * Eclipse supports transformation refines; mapping refines is spec-only.
 */
class QvtoRefinesParseTest extends AbstractQvtoParserTest {

	// ==================== Transformation refines ====================

	@Test
	void transformationRefines_parsesSuccessfully() throws QvtoParseException {
		// §8.4.7: transformation T(...) refines OtherT { ... }
		// Eclipse: QVTOParser.gi line 338
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT) refines BaseTransformation;
				""");
		assertNotNull(t);
	}

	@Test
	void transformationRefines_storesAnnotation() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT) refines BaseTransformation;
				""");
		EAnnotation ann = t.getEAnnotation("http://www.eclipse.org/fennec/m2x/qvto/refines");
		assertNotNull(ann, "Should have refines annotation");
		assertEquals("BaseTransformation", ann.getDetails().get("refinedModule"));
	}

	@Test
	void transformationRefines_withBody_parsesSuccessfully() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT) refines BaseTransformation {
				    main() { }
				}
				""");
		assertNotNull(t);
	}

	@Test
	void transformationWithoutRefines_stillWorks() throws QvtoParseException {
		// Regression: existing form must still work
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				""");
		assertNotNull(t);
	}

	// ==================== Mapping refines ====================

	@Test
	void mappingRefines_parsesSuccessfully() throws QvtoParseException {
		// §8.4.7: <mapping_refinement> ::= 'refines' <scoped_identifier>
		// Note: Eclipse does NOT support this — spec-only
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : target::TargetElement
				    refines baseMapping
				{
				    name := self.name;
				}
				""");
		assertNotNull(t);
	}

	@Test
	void mappingRefinesWithOtherExtensions_parsesSuccessfully() throws QvtoParseException {
		// Mapping with both inherits and refines
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : target::TargetElement
				    inherits toBase
				    refines baseMapping
				{
				    name := self.name;
				}
				""");
		assertNotNull(t);
	}
}
