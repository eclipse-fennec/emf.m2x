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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.junit.jupiter.api.Test;

/**
 * An arrow call on a single object means {@code oclAsSet()} first (OCL v2.4 §9.3.35[B]):
 * {@code p->size()} is 1. The parser records "this was written with an arrow" as an
 * {@code Adapter} on the AST node — and adapters are not part of the model, so they do not
 * survive XMI. A compiled unit loaded from a store must still say 1.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoCompiledUnitArrowSemanticsTest {

	private static final String MAIN = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			transformation Main(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := p->size().repr();
			        };
			    }
			}
			""";

	private final QvtoEngine engine = QvtoEngines.create(
			QvtoConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build()).build());

	@Test
	void arrowOnASingleObject_isOclAsSet_beforeAndAfterAStoreRoundTrip() throws Exception {
		CompiledUnit compiled = engine.compile(MAIN, "Main");
		assertEquals("1", run((OperationalTransformation) compiled.getUnit()), "fresh: p->size() is Set{p}->size()");

		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitKey key = store.store("qvto", new PackagedUnit(compiled));
		PackagedUnit loaded = (PackagedUnit) store.load(key).orElseThrow();
		assertEquals("1", run((OperationalTransformation) loaded.document().getUnit()),
				"loaded: the arrow must still mean oclAsSet — it is semantics of the unit, not of the parse");
	}

	private String run(OperationalTransformation transformation) {
		EObject pkg = EcoreUtil.create(EcorePackage.Literals.EPACKAGE);
		pkg.eSet(EcorePackage.Literals.ENAMED_ELEMENT__NAME, "Original");
		QvtoExecutionResult result = engine.execute(transformation,
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(pkg))));
		assertTrue(result.isSuccess(), () -> "execution failed: " + result.diagnostics());
		return (String) pkg.eGet(EcorePackage.Literals.ENAMED_ELEMENT__NAME);
	}
}
