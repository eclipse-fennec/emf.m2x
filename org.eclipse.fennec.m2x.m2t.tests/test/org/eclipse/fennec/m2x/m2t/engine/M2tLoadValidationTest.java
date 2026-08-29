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

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.charset.StandardCharsets;
import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * A manipulated MOFM2T unit is rejected when it is loaded or prepared (#177, part of #188).
 *
 * <p>The twin of {@code QvtoLoadValidationTest}. The M2T binder had the same checks and no
 * test at all, while the M2T security analysis lists them as the load-time defence: what a
 * store hands back is input, and a stored unit is executed without being parsed again.
 */
class M2tLoadValidationTest {

	private static final String MODULE = """
			[module gen(_'http://www.eclipse.org/emf/2002/Ecore')/]
			[template public main(c : EClass)]
			[for (f : EStructuralFeature | c.eStructuralFeatures)]
			[f.name/]
			[/for]
			[/template]
			""";

	private final M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(
			OclConfiguration.builder(new OclParserSupport()).build()).build());

	@Test
	@DisplayName("a variable use whose declaration left the document is rejected")
	void aVariableUseWhoseDeclarationLeftTheDocument_isRejected() throws Exception {
		CompiledUnit compiled = engine.compile(MODULE, "gen");
		VariableExp use = firstVariableUse(compiled);
		Variable elsewhere = OclFactory.eINSTANCE.createVariable();
		elsewhere.setName("f");
		use.setReferredVariable(elsewhere);

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().validate(compiled));

		assertTrue(failure.getMessage().contains("declared outside the document"),
				failure::getMessage);
	}

	@Test
	@DisplayName("a variable use without a declaration is rejected")
	void aVariableUseWithoutDeclaration_isRejected() throws Exception {
		CompiledUnit compiled = engine.compile(MODULE, "gen");
		firstVariableUse(compiled).setReferredVariable(null);

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().validate(compiled));

		assertTrue(failure.getMessage().contains("without declaration"), failure::getMessage);
	}

	@Test
	@DisplayName("a unit of another language under the m2t tag is rejected")
	void aUnitOfAnotherLanguage_isRejected() throws Exception {
		CompiledUnit compiled = engine.compile(MODULE, "gen");
		compiled.setUnit(EcoreFactory.eINSTANCE.createEPackage());

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().validate(compiled));

		assertTrue(failure.getMessage().contains("declared a MOFM2T unit"), failure::getMessage);
	}

	@Test
	@DisplayName("a module without a name is rejected")
	void aModuleWithoutAName_isRejected() throws Exception {
		CompiledUnit compiled = engine.compile(MODULE, "gen");
		((org.eclipse.fennec.m2x.model.m2t.Module) compiled.getUnit()).setName("  ");

		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> engine.unitBinder().validate(compiled));

		assertTrue(failure.getMessage().contains("no name"), failure::getMessage);
	}

	@Test
	@DisplayName("a tampered document is rejected by the store, before prepare sees it")
	void aTamperedDocument_isRejectedByTheStore() throws Exception {
		InMemoryUnitStoreBackend backend = new InMemoryUnitStoreBackend();
		UnitStore store = new DefaultUnitStore(backend);
		UnitKey key = store.store("m2t", new PackagedUnit(engine.compile(MODULE, "gen")));

		byte[] bytes = backend.get(key).orElseThrow();
		String original = new String(bytes, StandardCharsets.UTF_8);
		String tampered = original.replace("main", "sneak");
		assertTrue(!tampered.equals(original), "the tampering changed something");
		backend.put(key, tampered.getBytes(StandardCharsets.UTF_8));

		UnitPreparer preparer = UnitPreparer.withDefaults(store, engine.unitBinder());
		UnitPrepareException failure = assertThrows(UnitPrepareException.class,
				() -> preparer.prepare(key));

		assertTrue(failure.getCause() instanceof UnitStoreException, String.valueOf(failure.getCause()));
		assertTrue(failure.getMessage().contains("changed after it was sealed"), failure::getMessage);
	}

	private static VariableExp firstVariableUse(CompiledUnit compiled) {
		for (Iterator<EObject> it = compiled.eAllContents(); it.hasNext();) {
			if (it.next() instanceof VariableExp use) {
				return use;
			}
		}
		throw new AssertionError("no variable use in the compiled module");
	}
}
