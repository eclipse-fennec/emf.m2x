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

import java.util.List;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.engine.QvtoStoreUnitResolver;
import org.eclipse.fennec.m2x.unit.api.PreparedContext;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.prepare.UnitPreparer;
import org.eclipse.fennec.m2x.unit.store.DefaultUnitStore;
import org.eclipse.fennec.m2x.unit.store.InMemoryUnitStoreBackend;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;

/**
 * The compile → store → prepare → execute pipeline as one flat sequence, for the
 * architecture discussion of issue #210. Same shapes as {@link CompiledUnitsGuideExamplesTest}.
 *
 * @author Data In Motion Consulting
 */
public final class CompiledUnitsWalkthrough {

	public static void main(String[] args) throws Exception {
		String titlesLib = """
				library shelf.Titles {
				    helper prefix(s : String) : String {
				        return 'now reading: ' + s;
				    }
				}
				""";
		String announce = """
				modeltype SHELF uses 'http://example.org/m2x/guide/shelf/1.0';
				import shelf.Titles;
				transformation Announce(inout m : SHELF) {
				    main() {
				        m.objectsOfType(Book)->forEach(b) {
				            b.title := prefix(b.title);
				        };
				    }
				}
				""";

		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI("http://example.org/m2x/guide/shelf/1.0");
		shelf.setNsPrefix("shelf");
		EClass bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		shelf.getEClassifiers().add(bookClass);

		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());

		QvtoEngine compiler = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(shelf)
				.addUnitResolver(new QvtoStoreUnitResolver(store))
				.unitResolverEnabled(true)
				.build());

		store.put(compiler.compile(titlesLib, "shelf.Titles"));
		UnitKey key = store.put(compiler.compile(announce, "Announce"));

		QvtoEngine runner = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.build());

		UnitPreparer preparer = new UnitPreparer(store, List.of(runner.unitBinder()))
				.registerPackage(shelf);
		PreparedContext prepared = preparer.prepare(key);

		EObject book = shelf.getEFactoryInstance().create(bookClass);
		book.eSet(title, "moby dick");

		QvtoExecutionResult result = runner.execute(prepared, "Announce",
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(book))));

		System.out.println("success:  " + result.isSuccess());
		System.out.println("title:    " + book.eGet(title));
		System.out.println("key:      " + key);

		adhoc();
		embedded();
	}

	public static void adhoc() throws Exception {
		String titlesLib = """
				library shelf.Titles {
				    helper prefix(s : String) : String {
				        return 'now reading: ' + s;
				    }
				}
				""";
		String announce = """
				modeltype SHELF uses 'http://example.org/m2x/guide/shelf/1.0';
				import shelf.Titles;
				transformation Announce(inout m : SHELF) {
				    main() {
				        m.objectsOfType(Book)->forEach(b) {
				            b.title := prefix(b.title);
				        };
				    }
				}
				""";

		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI("http://example.org/m2x/guide/shelf/1.0");
		shelf.setNsPrefix("shelf");
		EClass bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		shelf.getEClassifiers().add(bookClass);

		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(shelf)
				.addUnitResolver(name -> "shelf.Titles".equals(name)
						? Optional.of(new QvtoUnit.SourceUnit(name, URI.createURI("mem:/shelf.Titles.qvto"), titlesLib))
						: Optional.empty())
				.unitResolverEnabled(true)
				.build());

		OperationalTransformation ast = engine.parse(announce, "Announce");

		EObject book = shelf.getEFactoryInstance().create(bookClass);
		book.eSet(title, "moby dick");

		QvtoExecutionResult result = engine.execute(ast,
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(book))));

		System.out.println("adhoc success:  " + result.isSuccess());
		System.out.println("adhoc title:    " + book.eGet(title));
	}

	public static void embedded() throws Exception {
		String titlesLib = """
				library shelf.Titles {
				    helper prefix(s : String) : String {
				        return 'now reading: ' + s;
				    }
				}
				""";
		String announce = """
				modeltype SHELF uses 'http://example.org/m2x/guide/shelf/1.0';
				import shelf.Titles;
				transformation Announce(inout m : SHELF) {
				    main() {
				        m.objectsOfType(Book)->forEach(b) {
				            b.title := prefix(b.title);
				        };
				    }
				}
				""";

		EPackage shelf = EcoreFactory.eINSTANCE.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI("http://example.org/m2x/guide/shelf/1.0");
		shelf.setNsPrefix("shelf");
		EClass bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		shelf.getEClassifiers().add(bookClass);

		// seit #212: kein Registry-Bau mehr — das Metamodell wird BEIGETRAGEN (lokal, leakt nie global)
		// compiler mit UnitResolver für shelf.Titles
		QvtoEngine compiler = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(shelf)
				.addUnitResolver(name -> "shelf.Titles".equals(name)
						? Optional.of(new QvtoUnit.SourceUnit(name, URI.createURI("mem:/shelf.Titles.qvto"), titlesLib))
						: Optional.empty())
				.unitResolverEnabled(true)
				.build());

		// hier bekommen wir schon die finale comiled unit
		CompiledUnit archive = compiler.compile(announce, "Announce",
				UnitCompileOptions.of(DependencyMode.EMBED));

		System.out.println("embedded units:    "
				+ archive.getEmbedded().stream().map(u -> u.getManifest().getQualifiedName()).toList());
		System.out.println("embedded packages: "
				+ archive.getPackages().stream().map(EPackage::getNsURI).toList());

		// hier speichern wir diese compiled unit — der Store ist dumm, keine Registry mehr (#211)
		UnitStore transported = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitKey key = transported.put(archive);

		// Maschine B seit #212: NULL Registry-Zeilen. Der Preparer baut seinen Kontext intern
		// (lokal → global), der PreparedContext trägt ihn, execute nutzt nur den Kontext —
		// der Runner braucht keine eigene Registry für Stored-Execution.
		// (Speichern braucht keinen Preparer: Packaging ist Compile-Seite, siehe #210-Symmetrie.)
		QvtoEngine runner = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.build());
		UnitPreparer preparer = new UnitPreparer(transported, List.of(runner.unitBinder()));
		PreparedContext prepared = preparer.prepare(key);

		EPackage carried = ((PackagedUnit) prepared.unit("Announce").orElseThrow()).document().getPackages().get(0);
		EClass carriedBook = (EClass) carried.getEClassifier("Book");
		EObject book01 = carried.getEFactoryInstance().create(carriedBook);
		book01.eSet(carriedBook.getEStructuralFeature("title"), "moby dick");
		EObject book02 = carried.getEFactoryInstance().create(carriedBook);
		book02.eSet(carriedBook.getEStructuralFeature("title"), "mopsy duck");

		QvtoExecutionResult result = runner.execute(prepared, "Announce",
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(book01, book02))));

		System.out.println("embedded success:  " + result.isSuccess());
		System.out.println("embedded title:    " + book01.eGet(carriedBook.getEStructuralFeature("title")));
		System.out.println("embedded title:    " + book02.eGet(carriedBook.getEStructuralFeature("title")));
	}
}
