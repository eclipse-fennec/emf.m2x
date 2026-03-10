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
import static org.junit.jupiter.api.Assertions.assertNull;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.fennec.m2x.model.qvtbase.Transformation;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
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
	void transformationRefines_setsRefinedReference() throws QvtoParseException {
		// D40: refines creates a stub Transformation on the 'refined' EReference
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT) refines BaseTransformation;
				""");
		Transformation refined = t.getRefined();
		assertNotNull(refined, "Should have refined reference set");
		assertEquals("BaseTransformation", refined.getName());
	}

	@Test
	void transformationWithoutRefines_hasNoRefinedReference() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				""");
		assertNull(t.getRefined(), "Should not have refined reference");
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
	void transformationRefines_qualifiedNameWithColons_setsRefinedReference() throws QvtoParseException {
		// §8.4.7: <moduleref> ::= <scoped_identifier> — spec uses '::'
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT) refines pkg::BaseTransformation;
				""");
		Transformation refined = t.getRefined();
		assertNotNull(refined, "Should have refined reference set");
		assertEquals("pkg::BaseTransformation", refined.getName());
	}

	@Test
	void transformationRefines_qualifiedNameWithDots_setsRefinedReference() throws QvtoParseException {
		// Eclipse uses '.' separator — we support both for compatibility
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT) refines pkg.BaseTransformation;
				""");
		Transformation refined = t.getRefined();
		assertNotNull(refined, "Should have refined reference set");
		// Normalized to '::' internally
		assertEquals("pkg::BaseTransformation", refined.getName());
	}

	// transformationWithoutRefines_hasNoRefinedReference above serves as regression test

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
	void mappingRefines_storesAnnotation() throws QvtoParseException {
		// §8.4.7: mapping refines stores name as EAnnotation for engine-level resolution
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
		MappingOperation mapping = findMapping(t, "toTarget");
		assertNotNull(mapping, "Should find mapping 'toTarget'");
		EAnnotation ann = mapping.getEAnnotation("http://www.eclipse.org/fennec/m2x/qvto/refines");
		assertNotNull(ann, "Should have refines annotation on mapping");
		assertEquals("baseMapping", ann.getDetails().get("refinedRelation"));
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

	@Test
	void mappingRefines_scopedName_storesAnnotation() throws QvtoParseException {
		// §8.4.7: <mapping_refinement> ::= 'refines' <scoped_identifier>
		// <scoped_identifier> supports CtxType::baseMapping
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : target::TargetElement
				    refines CtxType::baseMapping
				{
				    name := self.name;
				}
				""");
		MappingOperation mapping = findMapping(t, "toTarget");
		assertNotNull(mapping, "Should find mapping 'toTarget'");
		EAnnotation ann = mapping.getEAnnotation("http://www.eclipse.org/fennec/m2x/qvto/refines");
		assertNotNull(ann, "Should have refines annotation on mapping");
		assertEquals("CtxType::baseMapping", ann.getDetails().get("refinedRelation"));
	}

	@Test
	void mappingRefines_withInheritsAndMerges_parsesSuccessfully() throws QvtoParseException {
		// §8.2.1.15: MappingOperation may have inherits, merges, and refines simultaneously
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : target::TargetElement
				    inherits toBase
				    merges toOther
				    refines baseMapping
				{
				    name := self.name;
				}
				""");
		assertNotNull(t);
		MappingOperation mapping = findMapping(t, "toTarget");
		assertNotNull(mapping, "Should find mapping 'toTarget'");
		EAnnotation ann = mapping.getEAnnotation("http://www.eclipse.org/fennec/m2x/qvto/refines");
		assertNotNull(ann, "Should have refines annotation even with inherits+merges");
	}

	@Test
	void multipleMappingsRefine_eachHasOwnAnnotation() throws QvtoParseException {
		// §8.2.1.15: each MappingOperation independently has refinedRelation
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation T(in s : SRC, out t : TGT);
				mapping SourceElement::toTarget() : target::TargetElement
				    refines baseMapping
				{
				    name := self.name;
				}
				mapping SourceElement::toOther() : target::TargetElement
				    refines otherBaseMapping
				{
				    name := self.name;
				}
				""");
		MappingOperation m1 = findMapping(t, "toTarget");
		assertNotNull(m1, "Should find mapping 'toTarget'");
		EAnnotation ann1 = m1.getEAnnotation("http://www.eclipse.org/fennec/m2x/qvto/refines");
		assertNotNull(ann1, "toTarget should have refines annotation");
		assertEquals("baseMapping", ann1.getDetails().get("refinedRelation"));

		MappingOperation m2 = findMapping(t, "toOther");
		assertNotNull(m2, "Should find mapping 'toOther'");
		EAnnotation ann2 = m2.getEAnnotation("http://www.eclipse.org/fennec/m2x/qvto/refines");
		assertNotNull(ann2, "toOther should have refines annotation");
		assertEquals("otherBaseMapping", ann2.getDetails().get("refinedRelation"));
	}

	private MappingOperation findMapping(OperationalTransformation t, String name) {
		// Mappings are stored on the module class (EClass within EPackage)
		for (var classifier : t.getEClassifiers()) {
			if (classifier instanceof org.eclipse.emf.ecore.EClass ec) {
				for (EOperation op : ec.getEOperations()) {
					if (op instanceof MappingOperation mo && name.equals(mo.getName())) {
						return mo;
					}
				}
			}
		}
		return null;
	}
}
