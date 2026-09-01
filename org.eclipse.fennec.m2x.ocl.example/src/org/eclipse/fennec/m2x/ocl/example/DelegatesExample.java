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

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.ECollections;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.util.Diagnostician;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.engine.OclEngines;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

/**
 * The annotation path: the OCL sits <em>inside</em> the {@code .ecore} as EAnnotations, and
 * EMF's own machinery evaluates it once the engine's delegates are installed —
 * {@link Diagnostician} runs the {@code validYear} invariant, {@code eGet} computes the
 * derived {@code bookCount}, {@code eInvoke} runs the {@code booksBy} body. No OCL API
 * appears after {@code installDelegates()}; that is the point of delegates.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class DelegatesExample {

	private DelegatesExample() {}

	/** What the run observed — EMF's answers, not the OCL engine's. */
	public record Report(Diagnostic validation, int bookCount, int melvilleBooks) {}

	public static void main(String[] args) throws Exception {
		Report report = run();
		System.out.println("Diagnostician: " + report.validation().getSeverity()
				+ (report.validation().getChildren().isEmpty() ? ""
						: " — " + report.validation().getChildren().get(0).getMessage()));
		System.out.println("eGet(bookCount): " + report.bookCount());
		System.out.println("eInvoke(booksBy('Herman Melville')): " + report.melvilleBooks() + " book(s)");
	}

	/**
	 * Installs the delegates, lets EMF validate, get and invoke, and takes them back out.
	 *
	 * @return what EMF answered
	 * @throws Exception if anything on the way refuses
	 */
	public static Report run() throws Exception {
		Path base = ExampleFiles.baseDir();
		EPackage library = ExampleFiles.loadEcore(base.resolve("model/library.ecore"));
		EObject city = ExampleFiles.loadInstance(base.resolve("model/library.xmi"), library);

		OclEngine engine = OclEngines.create(new OclParserSupport(ExampleFiles.registryOf(library)));
		engine.installDelegates();
		try {
			// Codex Gigas (1229) violates validYear — Diagnostician reports it without a
			// single OCL API call in sight
			Diagnostic validation = Diagnostician.INSTANCE.validate(city);

			int bookCount = (Integer) city.eGet(city.eClass().getEStructuralFeature("bookCount"));

			EClass libraryClass = (EClass) library.getEClassifier("Library");
			EOperation booksBy = libraryClass.getEOperations().stream()
					.filter(operation -> "booksBy".equals(operation.getName())).findFirst().orElseThrow();
			List<?> melville = (List<?>) city.eInvoke(booksBy, ECollections.asEList("Herman Melville"));

			return new Report(validation, bookCount, melville.size());
		} finally {
			engine.uninstallDelegates();
		}
	}
}
