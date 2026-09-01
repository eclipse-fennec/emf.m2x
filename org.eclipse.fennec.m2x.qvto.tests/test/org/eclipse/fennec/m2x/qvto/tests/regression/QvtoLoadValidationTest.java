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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitMaterializeException;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.Test;

/**
 * A manipulated compiled unit is rejected when it is loaded or prepared, with a diagnostic
 * (#142, acceptance). The neutral checks are the store's; the QVT-O well-formedness is the
 * binder's.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoLoadValidationTest {

	private static final String MAIN = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			transformation Main(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := p.name + '!';
			        };
			    }
			}
			""";

	private final QvtoEngine engine = QvtoEngines.create(
			QvtoConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build()).build());

	@Test
	void aVariableUseWhoseDeclarationLeftTheDocument_isRejectedAtPrepare() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main");
		VariableExp use = firstVariableUse(compiled);
		Variable elsewhere = OclFactory.eINSTANCE.createVariable();
		elsewhere.setName("p");
		use.setReferredVariable(elsewhere);

		// A reference to nowhere would not even serialize; the binder is what prepare runs on the document
		UnitPrepareException failure = assertThrows(UnitPrepareException.class, () -> engine.unitBinder().validate(compiled));
		assertTrue(failure.getMessage().contains("declared outside the document"), failure.getMessage());
	}

	@Test
	void aVariableUseWithoutDeclaration_isRejectedAtPrepare() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main");
		firstVariableUse(compiled).setReferredVariable(null);
		UnitPrepareException failure = assertThrows(UnitPrepareException.class, () -> engine.unitBinder().validate(compiled));
		assertTrue(failure.getMessage().contains("without declaration"), failure.getMessage());
	}

	@Test
	void aUnitOfAnotherLanguage_underTheQvtoTag_isRejected() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main");
		compiled.setUnit(EcoreFactory.eINSTANCE.createEPackage());
		UnitPrepareException failure = assertThrows(UnitPrepareException.class, () -> engine.unitBinder().validate(compiled));
		assertTrue(failure.getMessage().contains("declared a QVT-O unit"), failure.getMessage());
	}

	@Test
	void aTamperedDocument_isRejectedByTheMaterializer_beforePrepareHandsItOn() throws Exception {
		InMemoryUnitStoreBackend backend = new InMemoryUnitStoreBackend();
		UnitStore store = new DefaultUnitStore(backend);
		var key = store.put(engine.compile(MAIN, "Main"));
		byte[] bytes = backend.get(key).orElseThrow();
		String xmi = new String(bytes, StandardCharsets.UTF_8).replace("\"!\"", "\"?\"");
		assertTrue(!xmi.equals(new String(bytes, StandardCharsets.UTF_8)), "the tampering changed something");
		backend.put(key, xmi.getBytes(StandardCharsets.UTF_8));

		UnitPreparer preparer = UnitPreparer.withDefaults(store, engine.unitBinder());
		UnitPrepareException failure = assertThrows(UnitPrepareException.class, () -> preparer.prepare(key));
		assertTrue(failure.getCause() instanceof UnitMaterializeException, String.valueOf(failure.getCause()));
		assertTrue(failure.getMessage().contains("changed after it was sealed"), failure.getMessage());
	}

	private static VariableExp firstVariableUse(CompiledUnit compiled) {
		for (Iterator<EObject> it = compiled.eAllContents(); it.hasNext();) {
			if (it.next() instanceof VariableExp use) {
				return use;
			}
		}
		throw new AssertionError("no variable use in " + List.of(MAIN));
	}
}
