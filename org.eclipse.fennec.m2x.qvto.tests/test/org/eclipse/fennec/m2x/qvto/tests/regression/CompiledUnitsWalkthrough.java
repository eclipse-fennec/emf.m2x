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

import java.nio.file.Files;
import java.nio.file.Path;
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
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.store.PackagedUnit;
import org.eclipse.fennec.m2x.unit.store.UnitDocuments;
import org.eclipse.fennec.m2x.unit.store.UnitXmi;

/**
 * Every delivery shape of a compiled unit, each as one flat sequence — the walkthrough that
 * drove the #210 architecture and now documents its outcome. Same shapes as
 * {@link CompiledUnitsGuideExamplesTest}, runnable as a Java application:
 *
 * <ul>
 * <li>{@link #main} — the pinned store pipeline: compile → store → prepare → execute, the
 * library shared through the store, the context given once to the preparer (#212);</li>
 * <li>{@link #adhoc} — no units at all: the library arrives as source through a resolver at
 * parse time, the model is an extent either way;</li>
 * <li>{@link #embedded} — one self-contained document: library and dynamic metamodel travel
 * inside it, and "machine B" runs it with zero registry lines;</li>
 * <li>{@link #referenced} — a unit by URI (#214): the resolver names where the document lives,
 * loading and binding happen at consumption, in the consumer's context.</li>
 * </ul>
 *
 * <p>The fifth shape — the object medium, a {@code UnitStore} over the emf.osgi
 * {@code EObjectRegistry} (#213) — lives in {@code org.eclipse.fennec.m2x.unit.registry}:
 * plain in {@code RegistryUnitStoreTest}, wired from files alone in
 * {@code RegistryUnitStoreFileOSGiTest}. It needs the registry bundles on the path, which this
 * bundle deliberately does not.
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

		// The store is dumb — key ↔ document, no registry, no context (#211)
		UnitStore store = new DefaultUnitStore(new InMemoryUnitStoreBackend());

		// The compile side: the dynamic metamodel is contributed, never assembled into a
		// registry (#212); imports resolve from the store, and the resolver decides nothing —
		// binding is the consumer's business (#214)
		QvtoEngine compiler = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(shelf)
				.addUnitResolver(new QvtoStoreUnitResolver(store))
				.unitResolverEnabled(true)
				.build());

		store.put(compiler.compile(titlesLib, "shelf.Titles"));
		UnitKey key = store.put(compiler.compile(announce, "Announce"));

		// The runtime side: the runner needs no resolution context of its own — the context is
		// given once, to the preparer; the PreparedContext carries it, execute uses only that (#212)
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
		referenced();
	}

	/** No units: the library arrives as source through a resolver at parse time — never as an extent. */
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

	/** One self-contained document — and a machine that has nothing gets everything from it. */
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

		// No registry assembled: the dynamic metamodel is contributed — local to this
		// configuration, nothing leaks into the global registry (#212)
		QvtoEngine compiler = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(shelf)
				.addUnitResolver(name -> "shelf.Titles".equals(name)
						? Optional.of(new QvtoUnit.SourceUnit(name, URI.createURI("mem:/shelf.Titles.qvto"), titlesLib))
						: Optional.empty())
				.unitResolverEnabled(true)
				.build());

		// One call, and the result is the finished document: the library and the metamodel
		// copy travel inside it
		CompiledUnit archive = compiler.compile(announce, "Announce",
				UnitCompileOptions.of(DependencyMode.EMBED));

		System.out.println("embedded units:    "
				+ archive.getEmbedded().stream().map(u -> u.getManifest().getQualifiedName()).toList());
		System.out.println("embedded packages: "
				+ archive.getPackages().stream().map(EPackage::getNsURI).toList());

		// Storing is dumb: key ↔ document (#211)
		UnitStore transported = new DefaultUnitStore(new InMemoryUnitStoreBackend());
		UnitKey key = transported.put(archive);

		// Machine B, in full: zero registry lines. The preparer builds its context internally
		// (local → global), the PreparedContext carries it, execute uses only the context.
		// Storing needs no preparer either — packaging is the compile side's job (#210).
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

	/** A unit by URI (#214): the resolver names where it lives; binding happens at consumption. */
	public static void referenced() throws Exception {
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

		// The library as a document in a file — what a build drops somewhere, sealed and stamped
		QvtoEngine bare = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(shelf)
				.build());
		UnitDocuments.Sealed sealed = UnitDocuments.seal(bare.compile(titlesLib, "shelf.Titles"),
				DefaultUnitFingerprintService.INSTANCE);
		Path file = Files.createTempDirectory("m2x-walkthrough").resolve("shelf.Titles.xmi");
		Files.write(file, UnitXmi.write(sealed.document(), sealed.key()));

		// The resolver names the location and decides nothing else; loading and binding happen
		// at consumption, in this engine's context, validation funnel included
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(shelf)
				.addUnitResolver(name -> "shelf.Titles".equals(name)
						? Optional.of(new QvtoUnit.ResourceUnit(name, URI.createFileURI(file.toString())))
						: Optional.empty())
				.unitResolverEnabled(true)
				.build());

		OperationalTransformation ast = engine.parse(announce, "Announce");

		EObject book = shelf.getEFactoryInstance().create(bookClass);
		book.eSet(title, "moby dick");

		QvtoExecutionResult result = engine.execute(ast,
				QvtoExecutionContext.of(new BasicQvtoModelExtent(List.of(book))));

		// A pin records the fingerprint of the document the URI holds
		CompiledUnit pinned = engine.compile(announce, "Announce");

		System.out.println("referenced success: " + result.isSuccess());
		System.out.println("referenced title:   " + book.eGet(title));
		System.out.println("referenced pin:     "
				+ pinned.getManifest().getDependencyEntry().get(0).getFingerprint());
	}
}
