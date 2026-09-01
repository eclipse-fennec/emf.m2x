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
package org.eclipse.fennec.m2x.ocl.example;

import java.nio.file.Path;
import java.util.List;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclDocumentValidator;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

/**
 * The document path, ad hoc: the {@code .ocl} file is parsed and loaded into the engine —
 * its {@code def:} features answer in ad-hoc expressions, {@code derive:} and {@code body:}
 * are visible to OCL evaluation, {@code inv:} feeds the {@link OclDocumentValidator}. The
 * {@code .ecore} is not touched: everything the document adds lives beside the metamodel.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class AdHocExample {

	private AdHocExample() {}

	/** An engine with the document in effect, and the instance model to ask about. */
	public record Session(OclEngine engine, EObject city) {}

	public static void main(String[] args) throws Exception {
		Session session = run();
		OclEngine engine = session.engine();
		OclContext city = OclContext.of(session.city());

		System.out.println("def label:      " + engine.evaluate("self.books->first().label", city));
		System.out.println("def isVintage:  " + engine.evaluate("self.books->select(b | b.isVintage)->size()", city)
				+ " of " + engine.evaluate("self.books->size()", city) + " books");
		System.out.println("def authorCount: " + engine.evaluate("self.authorCount", city));
		System.out.println("closure:        " + engine.evaluate(
				"self.shelves->closure(s | s.shelves)->collect(s | s.name)->sortedBy(n | n)", city));
		System.out.println("tuples:         " + engine.evaluate(
				"self.books->collect(b | Tuple{author = b.author, year = b.year})->select(t | t.year < 1500)->size()",
				city));

		// derive: wins inside OCL evaluation; EMF's eGet stays raw — that boundary belongs
		// to the delegate annotations (see DelegatesExample)
		EObject mobyDick = (EObject) engine.evaluate("self.books->first()", city);
		System.out.println("derive:         evaluate=" + engine.evaluate("self.displayName", OclContext.of(mobyDick))
				+ "  eGet=" + mobyDick.eGet(mobyDick.eClass().getEStructuralFeature("displayName")));

		OclDocumentValidator validator = new OclDocumentValidator(engine);
		for (Object member : (List<?>) session.city().eGet(session.city().eClass().getEStructuralFeature("members"))) {
			EObject eMember = (EObject) member;
			BasicDiagnostic diagnostics = new BasicDiagnostic();
			boolean valid = validator.validate(eMember, diagnostics, null);
			System.out.println("inv borrowLimit: " + eMember.eGet(eMember.eClass().getEStructuralFeature("name"))
					+ " → " + (valid ? "ok" : diagnostics.getChildren().get(0).getMessage())
					+ ", canBorrow() = " + engine.evaluate("self.canBorrow()", OclContext.of(eMember)));
		}
	}

	/**
	 * Parses the document from the file and puts it into effect on a fresh engine.
	 *
	 * @return the session
	 * @throws Exception if anything on the way refuses
	 */
	public static Session run() throws Exception {
		Path base = ExampleFiles.baseDir();
		EPackage library = ExampleFiles.loadEcore(base.resolve("model/library.ecore"));

		OclEngine engine = OclEngines.create(new OclParserSupport(ExampleFiles.registryOf(library)));
		engine.loadDocument(ExampleFiles.read(base.resolve("documents/library.ocl")));

		EObject city = ExampleFiles.loadInstance(base.resolve("model/library.xmi"), library);
		return new Session(engine, city);
	}
}
