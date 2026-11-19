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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.UnaryOperator;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * An {@code import} in a QVT-R unit is resolved (§7.11.1).
 *
 * <p>The grammar has accepted {@code import} from the start and nothing consumed it: a
 * transformation could name a unit and the engine carried on as if the line were absent.
 * With it, the whole unit-resolver half of the configuration was unreachable —
 * {@code unitResolvers}, {@code unitResolverEnabled}, {@code allowedUnitModules} and
 * {@code maxUnitResolvers} were read by no engine code, two of them documented as security
 * settings.
 *
 * <p>An import makes the imported unit's names visible (§7.1.1); its relations take part in a
 * run only through {@code extends} or a call (#360).
 */
class QvtdImportTest {

	private static final String NS_URI = "http://example.org/m2x/qvtr-import/1.0";

	/** The imported unit: it is the one that knows how to copy a name. */
	private static final String LIBRARY = """
			transformation shared(source : bookshelf, target : bookshelf) {
			    top relation CopyTitle {
			        t : String;
			        checkonly domain source b1 : Book { title = t };
			        enforce domain target b2 : Book { title = t };
			    }
			}
			""";

	private static final String IMPORTER = """
			import shared.Library;
			transformation importer(source : bookshelf, target : bookshelf) {
			}
			""";

	private EPackage bookshelf;
	private EClass bookClass;
	private EPackage.Registry registry;

	@BeforeEach
	void setUp() {
		bookshelf = EcoreFactory.eINSTANCE.createEPackage();
		bookshelf.setName("bookshelf");
		bookshelf.setNsURI(NS_URI);
		bookshelf.setNsPrefix("bookshelf");

		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);
		bookshelf.getEClassifiers().add(bookClass);

		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, bookshelf);
	}

	@Test
	@DisplayName("an import makes names visible, it does not run the imported unit's top relations")
	void anImportedTopRelation_doesNotRunThroughTheImport() throws Exception {
		// §7.13.1: an import makes the unit's names usable; its relations take part in the
		// importer's execution only through extends or a call (#360) — the importer here declares
		// no relation, so nothing is enforced
		QvtdEngine engine = engine(builder -> builder
				.addUnitResolver(library())
				.unitResolverEnabled(true));

		Run run = run(engine);

		assertTrue(run.result().isSuccess(), () -> "diagnostics: " + run.result().diagnostics());
		assertEquals(List.of(), run.target().getContents());
	}

	@Test
	@DisplayName("extends names an imported transformation: its relations are inherited")
	void extendsAnImportedTransformation_inheritsItsRelations() throws Exception {
		Run run = run(withLibrary(), """
				import shared.Library;
				transformation importer(source : bookshelf, target : bookshelf) extends shared {
				}
				""");

		assertTrue(run.result().isSuccess(), () -> "diagnostics: " + run.result().diagnostics());
		assertEquals(List.of("Moby Dick"), titles(run.target()));
	}

	@Test
	@DisplayName("a relation of an imported transformation is called by its qualified name")
	void aQualifiedCall_reachesTheImportedRelation() throws Exception {
		Run run = run(withLibrary(), callingImporter("shared.CopyTitle(b1, b2)"));

		assertTrue(run.result().isSuccess(), () -> "diagnostics: " + run.result().diagnostics());
		assertEquals(List.of("Moby Dick"), titles(run.target()));
	}

	@Test
	@DisplayName("an import makes the unqualified name of an imported relation visible")
	void anUnqualifiedCall_reachesTheImportedRelation() throws Exception {
		Run run = run(withLibrary(), callingImporter("CopyTitle(b1, b2)"));

		assertTrue(run.result().isSuccess(), () -> "diagnostics: " + run.result().diagnostics());
		assertEquals(List.of("Moby Dick"), titles(run.target()));
	}

	@Test
	@DisplayName("a called relation runs as called, not also as a top relation of the importer")
	void aCalledRelation_isNoTopRelationOfTheImporter() throws Exception {
		QvtdEngine engine = withLibrary();
		RelationalTransformation transformation = engine.parse(callingImporter("shared.CopyTitle(b1, b2)"), "importer");
		executeAndCountRules(engine, transformation);

		assertEquals(List.of("Copy"), transformation.getRule().stream()
				.filter(rule -> ((Relation) rule).isIsTopLevel()).map(Rule::getName).toList());
	}

	@Test
	@DisplayName("a call that no imported transformation answers is a positioned error")
	void aCallNoImportAnswers_isAPositionedError() throws Exception {
		Run run = run(withLibrary(), callingImporter("shared.Nope(b1, b2)"));
		assertFalse(run.result().isSuccess());
		assertTrue(run.result().diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("Unknown relation or query 'shared.Nope'")),
				() -> "diagnostics: " + run.result().diagnostics());

		// compiled with the import embedded, the open call is reported where it was written
		QvtdParseException failure = assertThrows(QvtdParseException.class, () -> withLibrary()
				.compile(callingImporter("shared.Nope(b1, b2)"), "importer", UnitCompileOptions.of(DependencyMode.EMBED)));

		assertTrue(failure.getErrors().stream().anyMatch(d -> d.getMessage().contains("Unknown relation or query 'shared.Nope'")
				&& d.getLine() == 6), () -> "" + failure.getErrors());
	}

	@Test
	@DisplayName("extends naming neither a transformation of the unit nor an imported one is an error")
	void extendsNoImportAnswers_isAnError() throws Exception {
		Run run = run(withLibrary(), """
				import shared.Library;
				transformation importer(source : bookshelf, target : bookshelf) extends other {
				}
				""");

		assertFalse(run.result().isSuccess());
		assertTrue(run.result().diagnostics().stream().anyMatch(d -> d.getMessage()
				.contains("extends 'other', which is neither a transformation of its unit nor an imported one")),
				() -> "diagnostics: " + run.result().diagnostics());
	}

	@Test
	@DisplayName("an import nothing answers for is an error, not a silence")
	void unresolvedImportIsReported() throws Exception {
		QvtdEngine engine = engine(builder -> builder.unitResolverEnabled(true));

		QvtdExecutionResult result = run(engine).result();

		assertFalse(result.isSuccess());
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("Cannot resolve import: shared.Library")),
				() -> "diagnostics: " + result.diagnostics());
	}

	@Test
	@DisplayName("the enable flag is the gate, and it is shut by default")
	void resolversAreUnreachableUntilEnabled() throws Exception {
		QvtdEngine engine = engine(builder -> builder.addUnitResolver(library()));

		assertFalse(run(engine).result().isSuccess(),
				"the resolver is configured but the gate is shut");
	}

	@Test
	@DisplayName("a name not on a non-empty allow-list is refused")
	void allowListNarrows() throws Exception {
		QvtdEngine engine = engine(builder -> builder
				.addUnitResolver(library())
				.unitResolverEnabled(true)
				.allowedUnitModules(Set.of("something.else")));

		assertFalse(run(engine).result().isSuccess());
	}

	@Test
	@DisplayName("an empty allow-list restricts nothing")
	void emptyAllowListMeansNoRestriction() throws Exception {
		QvtdEngine engine = engine(builder -> builder
				.addUnitResolver(library())
				.unitResolverEnabled(true)
				.allowedUnitModules(Set.of()));

		assertTrue(run(engine).result().isSuccess());
	}

	@Test
	@DisplayName("no more resolvers are consulted than the limit allows")
	void limitCapsHowManyResolversAreAsked() throws Exception {
		AtomicInteger asked = new AtomicInteger();
		QvtdEngine engine = engine(builder -> {
			for (int i = 0; i < 10; i++) {
				builder.addUnitResolver(name -> {
					asked.incrementAndGet();
					return Optional.empty();
				});
			}
			return builder.unitResolverEnabled(true).maxUnitResolvers(3);
		});

		assertFalse(run(engine).result().isSuccess());
		assertEquals(3, asked.get(), "the fourth resolver is past the limit");
	}

	@Test
	@DisplayName("executing twice does not import twice")
	void repeatedExecutionIsIdempotent() throws Exception {
		// The linker runs per execution, so a second run resolves the import again. A called
		// relation is copied in once; a second link finds the copy instead of adding another.
		QvtdEngine engine = withLibrary();

		RelationalTransformation transformation = engine.parse(callingImporter("shared.CopyTitle(b1, b2)"), "importer");
		int afterFirst = executeAndCountRules(engine, transformation);
		int afterSecond = executeAndCountRules(engine, transformation);

		assertEquals(afterFirst, afterSecond,
				"the imported relations must not accumulate with every execution");
	}

	// --- helpers ---

	/** An importer whose one top relation creates a Book per Book and calls {@code %s} in its where. */
	private static String callingImporter(String call) {
		return """
				import shared.Library;
				transformation importer(source : bookshelf, target : bookshelf) {
				    top relation Copy {
				        checkonly domain source b1 : Book {};
				        enforce domain target b2 : Book {};
				        where { %s; }
				    }
				}
				""".formatted(call);
	}

	private QvtdEngine withLibrary() {
		return engine(builder -> builder.addUnitResolver(library()).unitResolverEnabled(true));
	}

	private List<Object> titles(QvtdModelExtent extent) {
		return extent.getContents().stream().map(book -> book.eGet(bookClass.getEStructuralFeature("title"))).toList();
	}

	private QvtdUnitResolver library() {
		return name -> "shared.Library".equals(name)
				? Optional.of(new QvtdUnit.SourceUnit(name,
						URI.createURI("mem:/" + name + ".qvtr"), LIBRARY))
				: Optional.empty();
	}

	private QvtdEngine engine(UnaryOperator<QvtdConfiguration.Builder> customizer) {
		return QvtdEngines.create(customizer.apply(QvtdConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.packageRegistry(registry)).build());
	}

	private record Run(QvtdExecutionResult result, QvtdModelExtent target) {
	}

	private int executeAndCountRules(QvtdEngine engine, RelationalTransformation transformation)
			throws Exception {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");
		engine.execute(transformation, QvtdExecutionContext.enforce("target",
				Map.of("source", QvtdModelExtent.of(book), "target", QvtdModelExtent.of())));
		return transformation.getRule().size();
	}

	private Run run(QvtdEngine engine) throws Exception {
		return run(engine, IMPORTER);
	}

	private Run run(QvtdEngine engine, String importer) throws Exception {
		RelationalTransformation transformation = engine.parse(importer, "importer");

		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");

		QvtdModelExtent source = QvtdModelExtent.of(book);
		QvtdModelExtent target = QvtdModelExtent.of();
		QvtdExecutionResult result = engine.execute(transformation,
				QvtdExecutionContext.enforce("target",
						Map.of("source", source, "target", target)));
		return new Run(result, target);
	}
}
