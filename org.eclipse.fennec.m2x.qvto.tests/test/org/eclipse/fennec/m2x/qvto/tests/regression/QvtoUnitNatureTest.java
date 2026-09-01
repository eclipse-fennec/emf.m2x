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
package org.eclipse.fennec.m2x.qvto.tests.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.compiled.UnitNature;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.Test;

/**
 * The manifest says what kind of unit the document holds (#224): a standalone library source
 * compiles into {@code LIBRARY}, everything else is a {@code TRANSFORMATION} — answerable by a
 * holder of the document without loading the AST, and it survives the store round trip.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoUnitNatureTest {

	private static final String HELPER_LIB = """
			library HelperLib {
			    helper greet() : String {
			        return 'hello';
			    }
			}
			""";
	private static final String MAIN = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			import HelperLib;
			transformation Main(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := greet();
			        };
			    }
			}
			""";

	@Test
	void aLibrarySource_compilesIntoALibraryUnit() throws Exception {
		assertEquals(UnitNature.LIBRARY,
				engine().compile(HELPER_LIB, "HelperLib").getManifest().getNature());
	}

	@Test
	void aTransformationSource_compilesIntoATransformationUnit() throws Exception {
		assertEquals(UnitNature.TRANSFORMATION,
				engine().compile(MAIN, "Main").getManifest().getNature());
	}

	@Test
	void theNature_survivesTheStoreRoundTrip() throws Exception {
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		PackagedUnit loaded = (PackagedUnit) store
				.get(store.put(engine().compile(HELPER_LIB, "HelperLib"))).orElseThrow();
		assertEquals(UnitNature.LIBRARY, loaded.document().getManifest().getNature());
	}

	@Test
	void anEmbeddedLibrary_carriesItsNatureInsideTheTransformation() throws Exception {
		CompiledUnit unit = engine().compile(MAIN, "Main", UnitCompileOptions.of(DependencyMode.EMBED));
		assertEquals(UnitNature.TRANSFORMATION, unit.getManifest().getNature());
		assertEquals(UnitNature.LIBRARY, unit.getEmbedded().get(0).getManifest().getNature());
	}

	private static QvtoEngine engine() {
		QvtoUnit.SourceUnit library = new QvtoUnit.SourceUnit("HelperLib",
				URI.createURI("mem:/HelperLib.qvto"), HELPER_LIB);
		return QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(name -> "HelperLib".equals(name) ? Optional.of(library) : Optional.empty())
				.unitResolverEnabled(true)
				.build());
	}
}
