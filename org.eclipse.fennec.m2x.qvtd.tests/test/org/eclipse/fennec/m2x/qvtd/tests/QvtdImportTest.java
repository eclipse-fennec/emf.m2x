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
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdEngine;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnit;
import org.eclipse.fennec.m2x.qvtd.api.QvtdUnitResolver;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngines;
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
 * <p>An imported unit contributes its relations, the way an {@code extends} does. What the
 * importing transformation declares itself wins, so an import cannot quietly replace a
 * relation.
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
	@DisplayName("an imported relation runs")
	void importedRelationIsExecuted() throws Exception {
		QvtdEngine engine = engine(builder -> builder
				.addUnitResolver(library())
				.unitResolverEnabled(true));

		QvtdModelExtent target = run(engine).target();

		assertEquals(1, target.getContents().size(),
				"the relation that produces the Book came in through the import");
		assertEquals("Moby Dick", title(target.getContents().get(0)));
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

	// --- helpers ---

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

	private Run run(QvtdEngine engine) throws Exception {
		RelationalTransformation transformation = engine.parse(IMPORTER, "importer");

		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookClass.getEStructuralFeature("title"), "Moby Dick");

		QvtdModelExtent source = QvtdModelExtent.of(book);
		QvtdModelExtent target = QvtdModelExtent.of();
		QvtdExecutionResult result = engine.execute(transformation,
				QvtdExecutionContext.enforce("target",
						Map.of("source", source, "target", target)));
		return new Run(result, target);
	}

	private String title(EObject book) {
		return (String) book.eGet(((EClass) book.eClass()).getEStructuralFeature("title"));
	}
}
