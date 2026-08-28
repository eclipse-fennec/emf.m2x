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
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.junit.jupiter.api.Test;

/**
 * The unit fingerprint of a QVT-R transformation (#138).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdUnitFingerprintTest extends AbstractQvtdEngineTest {

	private static final String SOURCE = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				query IsExported(n : String) : Boolean { n.size() > 1 }
				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema { name = pn };
					when { IsExported(pn); }
					where { pn.size() > 0; }
				}
			}
			""";

	private static final String REFORMATTED = """
			-- reformatted
			transformation T(uml : simpleuml, rdbms : simplerdbms)
			{
				query IsExported(n : String) : Boolean {
					n.size() > 1
				}
				top relation PackageToSchema
				{
					pn : String;
					checkonly domain uml   p : Package { name = pn };
					enforce   domain rdbms s : Schema  { name = pn };
					when  { IsExported(pn); }
					where { pn.size() > 0; }
				}
			}
			""";

	private static final String OTHER_LOGIC = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				query IsExported(n : String) : Boolean { n.size() > 2 }
				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema { name = pn };
					when { IsExported(pn); }
					where { pn.size() > 0; }
				}
			}
			""";

	@Test
	void reformattedSource_sameFingerprint() throws Exception {
		assertEquals(fingerprint(SOURCE), fingerprint(REFORMATTED));
	}

	@Test
	void differentLogic_differentFingerprint() throws Exception {
		assertNotEquals(fingerprint(SOURCE), fingerprint(OTHER_LOGIC));
	}

	@Test
	void compiledTwice_sameFingerprint() throws Exception {
		CompiledUnit first = engine.compile(SOURCE, "T");
		CompiledUnit second = engine.compile(SOURCE, "T");
		assertNotEquals(first.getId(), second.getId());
		assertEquals(first.getManifest().getUnitFingerprint(), second.getManifest().getUnitFingerprint());
	}

	private static String fingerprint(String source) throws Exception {
		return engine.compile(source, "T").getManifest().getUnitFingerprint();
	}
}
