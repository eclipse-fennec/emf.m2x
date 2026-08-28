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

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.Test;

/**
 * A property of a relation variable resolves to the metamodel's feature, not to a synthetic
 * placeholder (#153). Measured on the relation of the issue: two placeholders ({@code name} on
 * {@code Package} and {@code Schema}) before the relation variables were nested — none is the
 * expectation now.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdPropertyResolutionTest extends AbstractQvtdEngineTest {

	private static final String TRANSFORMATION = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
			    top relation PackageToSchema {
			        pn : String;
			        checkonly domain uml p : Package { name = pn };
			        enforce domain rdbms s : Schema { name = pn };
			        where { p.name.size() > 0 and s.name = p.name; }
			    }
			}
			""";

	@Test
	void relationVariableProperty_resolvesToTheMetamodelFeature() throws Exception {
		RelationalTransformation transformation = parse(TRANSFORMATION);
		List<EAttribute> placeholders = SatelliteCollector.find(transformation).stream()
				.filter(EAttribute.class::isInstance).map(EAttribute.class::cast).toList();
		assertEquals(List.of(), placeholders,
				"no synthetic EAttribute stands in for a resolvable property: " + placeholders.stream().map(EAttribute::getName).toList());
	}
}
