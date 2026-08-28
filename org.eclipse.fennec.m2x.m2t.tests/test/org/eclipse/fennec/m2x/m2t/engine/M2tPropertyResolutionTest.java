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
package org.eclipse.fennec.m2x.m2t.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.Test;

/**
 * A property of a typed template parameter resolves to the metamodel's feature, not to a
 * synthetic placeholder (#153). Measured on the module of the issue: four placeholders before
 * #154/#158 nested the parameters — none is the expectation now.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tPropertyResolutionTest {

	private static final String MODULE = """
			[module m(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public main(c : EClass)][file ('out', false)][c.name/][c.ePackage.name/][/file][/template]
			""";

	private final M2tEngine engine = M2tEngines.create(
			M2tConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build()).build());

	@Test
	void typedParameterProperty_resolvesToTheMetamodelFeature() throws Exception {
		Module module = engine.parse(MODULE, "m");
		List<EAttribute> placeholders = SatelliteCollector.find(module).stream()
				.filter(EAttribute.class::isInstance).map(EAttribute.class::cast).toList();
		assertEquals(List.of(), placeholders, "no synthetic EAttribute stands in for a resolvable property");
		for (Iterator<EObject> it = module.eAllContents(); it.hasNext();) {
			if (it.next() instanceof PropertyCallExp call && "name".equals(call.getReferredProperty().getName())) {
				assertSame(EcorePackage.Literals.ENAMED_ELEMENT__NAME, call.getReferredProperty(),
						"c.name is EClass.name of Ecore, the very feature");
			}
		}
	}
}
