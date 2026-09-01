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
package org.eclipse.fennec.m2x.unit.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.qvtbase.QvtbaseFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.store.UnitDocuments;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;
import org.junit.jupiter.api.Test;

/**
 * A same-document forward reference survives the XMI round trip (#230): the unit roots inherit
 * EPackage, whose name-segment resolution answers from a cache that goes stale while a document
 * loads — a where clause calling a relation declared later never resolved. Unit documents write
 * reflective {@code @feature.index} fragments instead, which no cache sits under.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitXmiForwardReferenceTest {

	@Test
	void aWhereClauseCallingALaterRelation_survivesTheRoundTrip() throws Exception {
		RelationalTransformation transformation = QvtrelationFactory.eINSTANCE.createRelationalTransformation();
		transformation.setName("Forward");
		transformation.setNsURI("http://example.org/m2x/forward-reference/1.0");
		Relation first = QvtrelationFactory.eINSTANCE.createRelation();
		first.setName("First");
		Relation second = QvtrelationFactory.eINSTANCE.createRelation();
		second.setName("Second");
		// the forward reference: First's where calls Second, which is serialized later
		RelationCallExp call = QvtrelationFactory.eINSTANCE.createRelationCallExp();
		call.setReferredRelation(second);
		var where = QvtbaseFactory.eINSTANCE.createPattern();
		var predicate = QvtbaseFactory.eINSTANCE.createPredicate();
		predicate.setConditionExpression(call);
		where.getPredicate().add(predicate);
		first.setWhere(where);
		transformation.getRule().add(first);
		transformation.getRule().add(second);

		CompiledUnit compiled = UnitPackager.compile("qvtr", "Forward", transformation);
		UnitDocuments.Sealed sealed = UnitDocuments.seal(compiled, DefaultUnitFingerprintService.INSTANCE);
		EObject root = UnitXmi.read(UnitXmi.write(sealed.document(), sealed.key()), sealed.key());

		RelationalTransformation loaded = (RelationalTransformation) ((CompiledUnit) root).getUnit();
		Relation loadedFirst = (Relation) loaded.getRule().get(0);
		RelationCallExp loadedCall = (RelationCallExp) loadedFirst.getWhere().getPredicate().get(0)
				.getConditionExpression();
		Relation referred = loadedCall.getReferredRelation();
		assertFalse(referred.eIsProxy(), "the forward reference resolved");
		assertEquals("Second", referred.getName());
		assertSame(loaded.getRule().get(1), referred, "and it is the document's own second relation");
	}
}
