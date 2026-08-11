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

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/**
 * The documentation-generation setup of the user guide, as something that runs.
 *
 * <p>§13.1 of {@code docs/m2t-user-guide.md} shows how a generator reaches Java: an output path
 * derived from the model, a link relative to the document that carries it, a slug for a title —
 * none of which a template language expresses, all of which become callable by being registered
 * as OCL operations. This is that configuration, executed, so the guide has something to point
 * at and the two cannot drift apart.
 *
 * <p>It exercises the three receiver kinds the guide's table names, because getting the
 * {@code ownerType} wrong is the most common way for a provider to appear not to work:
 *
 * <ul>
 *   <li>a {@code ClassifierType} around an {@code EClass} — {@code b.docPath()}, callable on
 *       instances of that class and its subclasses</li>
 *   <li>a {@code PrimitiveType("String")} — {@code b.title.kebab()}, callable on attribute
 *       values</li>
 *   <li>a {@code ClassifierType} around {@code EObject} — {@code b.author.mdLinkFrom(b)},
 *       callable on every model element</li>
 * </ul>
 *
 * <p>Two of the guide's warnings are pinned as behaviour rather than prose. A literal {@code [}
 * cannot be typed in a template, so the Markdown link is built in Java. And an <em>unset</em>
 * reference is covered from both sides, because M2T evaluates leniently (D14, #114): navigating
 * from it leaves an empty spot and reports a warning naming the expression, while <em>calling</em>
 * on it proceeds, so a provider dispatches with a null receiver and the guard inside the lambda
 * decides. Neither writes the word {@code OclInvalid} into the document, which is what both did
 * before.
 */
class M2tDocGenerationExampleTest {

	private static final String NS_URI = "http://example.org/m2x/librarydoc/1.0";

	/** The guide's template. The author line varies, so the alternative can be shown too. */
	private static final String TEMPLATE = """
			[module librarydoc(_'http://example.org/m2x/librarydoc/1.0')/]
			[template public main(b : Book)]
			[file (b.docPath(), false)]
			# [b.title/]

			- Slug: [b.title.kebab()/]
			%s
			[/file]
			[/template]
			""";

	private static final String AUTHOR_LINE = "- Author: [b.author.mdLinkFrom(b)/]";

	@TempDir
	Path outputDirectory;

	private EPackage library;
	private EClass bookClass;
	private EClass authorClass;
	private EAttribute bookTitle;
	private EAttribute authorName;
	private EReference bookAuthor;

	@BeforeEach
	void setUp() {
		library = EcoreFactory.eINSTANCE.createEPackage();
		library.setName("librarydoc");
		library.setNsPrefix("librarydoc");
		library.setNsURI(NS_URI);

		authorClass = EcoreFactory.eINSTANCE.createEClass();
		authorClass.setName("Author");
		authorName = attribute("name");
		authorClass.getEStructuralFeatures().add(authorName);

		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		bookTitle = attribute("title");
		bookClass.getEStructuralFeatures().add(bookTitle);
		bookAuthor = EcoreFactory.eINSTANCE.createEReference();
		bookAuthor.setName("author");
		bookAuthor.setEType(authorClass);
		bookAuthor.setContainment(true);
		bookClass.getEStructuralFeatures().add(bookAuthor);

		library.getEClassifiers().add(bookClass);
		library.getEClassifiers().add(authorClass);
	}

	@Test
	@DisplayName("a generator reaches Java for paths, slugs and links")
	void theGuidesSetupGenerates() throws Exception {
		EObject book = book("The Long Way", "Becky Chambers");

		M2tResult result = generate(book);

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		Path written = outputDirectory.resolve("books/the-long-way.md");
		assertTrue(Files.exists(written), () -> "expected " + written + " to be written, files: "
				+ result.generatedFiles().keySet());
		assertEquals("""
				# The Long Way

				- Slug: the-long-way
				- Author: [Becky Chambers](../authors/becky-chambers.md)""",
				Files.readString(written).strip());
	}

	@Test
	@DisplayName("the output path comes from the model, so each element writes its own file")
	void eachElementWritesItsOwnFile() throws Exception {
		generate(book("First Light", "Anna Author"));
		generate(book("Second Sight", "Bo Writer"));

		assertTrue(Files.exists(outputDirectory.resolve("books/first-light.md")));
		assertTrue(Files.exists(outputDirectory.resolve("books/second-sight.md")));
	}

	@Test
	@DisplayName("an operation on an unset reference is reached, so its guard can answer")
	void anUnsetReferenceStillReachesTheOperation() throws Exception {
		// M2T evaluates leniently (D14, #114): a call on a null source proceeds, so the
		// registered provider dispatches with a null receiver and the guard inside the lambda
		// is what decides. Before that, the word OclInvalid landed in the document instead.
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookTitle, "Anonymous Work");

		M2tResult result = generate(book);

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		String document = Files.readString(outputDirectory.resolve("books/anonymous-work.md"));
		assertTrue(document.contains("- Author: —"),
				() -> "mdLinkFrom handles the null receiver itself. Document:\n" + document);
		assertFalse(document.contains("OclInvalid"), "and nothing leaks into the text");
	}

	@Test
	@DisplayName("navigating from an unset reference leaves the spot empty and says so")
	void navigatingFromAnUnsetReferenceWarns() throws Exception {
		// The other half of leniency: navigation answers null, so the document gets an empty
		// spot rather than OclInvalid — and the run reports which expression came up empty, so
		// an author is not left to notice a missing name by reading the output.
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookTitle, "Anonymous Work");

		M2tResult result = generate(book, "- Author: [b.author.name/]");

		String document = Files.readString(outputDirectory.resolve("books/anonymous-work.md"));
		assertTrue(document.contains("- Author:") && !document.contains("OclInvalid"),
				() -> "document was:\n" + document);
		assertTrue(result.isSuccess(), "an empty spot is not a failed generation");
		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
						&& d.getMessage().contains("lenient")),
				() -> "expected a warning naming the null navigation: " + result.diagnostics());
	}

	@Test
	@DisplayName("a diagnostic says which template and which document it came from")
	void diagnosticsNameTheirOrigin() throws Exception {
		// A warning that only says "some property was null" is not actionable in a generator
		// that writes a document per element. The M2T metamodel carries no positions, so the
		// template and the open [file] block are what can be named — and they are enough to
		// find the line by hand. Real line numbers need #110 and #116.
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookTitle, "Anonymous Work");

		M2tResult result = generate(book, "- Author: [b.author.name/]");

		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("[template main → books/anonymous-work.md]")),
				() -> "diagnostics should name template and target: " + result.diagnostics());
	}

	@Test
	@DisplayName("the way around it is a receiver that cannot be null")
	void anOperationOnTheOwnerHandlesTheMissingValue() throws Exception {
		// Put the operation on the element that is always there — the Book — and let it read the
		// reference itself. The guard becomes reachable, because the receiver exists.
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookTitle, "Anonymous Work");

		M2tResult result = generate(book, "- Author: [b.authorLink()/]");

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		String document = Files.readString(outputDirectory.resolve("books/anonymous-work.md"));
		assertTrue(document.contains("- Author: —"), () -> "document was:\n" + document);
	}

	@Test
	@DisplayName("or a guard in the template, which is the other way the guide offers")
	void theTemplateCanGuardInstead() throws Exception {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookTitle, "Anonymous Work");

		// oclIsUndefined is one of the operations that survives a null receiver, which is what
		// makes this readable in the template rather than only in Java.
		M2tResult result = generate(book,
				"- Author: [if (not b.author.oclIsUndefined())][b.author.mdLinkFrom(b)/][else]—[/if]");

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		String document = Files.readString(outputDirectory.resolve("books/anonymous-work.md"));
		assertTrue(document.contains("- Author: —"), () -> "document was:\n" + document);
	}

	// --- the provider the guide calls DocOperations ---

	/**
	 * The generator's own operations. They belong here rather than in the metamodel: an output
	 * path and a Markdown link are concerns of this generator, and a second generator over the
	 * same model would want different ones.
	 */
	private OclOperationProvider docOperations() {
		PrimitiveType string = OclFactory.eINSTANCE.createPrimitiveType();
		string.setName("String");
		ClassifierType bookType = OclFactory.eINSTANCE.createClassifierType();
		bookType.setReferredClassifier(bookClass);
		ClassifierType anyElement = OclFactory.eINSTANCE.createClassifierType();
		anyElement.setReferredClassifier(EcorePackage.Literals.EOBJECT);

		return () -> List.of(
				// on Book: where this element's document goes
				OclOperation.of("docPath", bookType, string,
						(self, args) -> "books/" + kebab(title((EObject) self)) + ".md"),
				// on String: a slug, callable on any attribute value
				OclOperation.of("kebab", string, string, (self, args) -> kebab((String) self)),
				// on every element: a link relative to the document that carries it. A literal
				// '[' cannot be typed in a template, so the finished link is built here. This
				// one takes an argument, so it needs the canonical constructor — the of(…)
				// shorthand covers only operations without parameters.
				new OclOperation("mdLinkFrom", anyElement, List.of(anyElement), string,
						(self, args) -> mdLink((EObject) self,
								args.length > 0 ? args[0] : null)),
				// on Book: the same link, asked of the element that owns the reference. The
				// receiver is never null here, which is the only way the missing-value case can
				// be handled at all — see anUnsetReferenceNeverReachesTheOperation.
				OclOperation.of("authorLink", bookType, string, (self, args) -> {
					EObject writer = (EObject) ((EObject) self).eGet(bookAuthor);
					return mdLink(writer, self);
				}));
	}

	/**
	 * @param target the element to link to, {@code null} when the reference is unset
	 * @param from   the element whose document carries the link — what makes the path relative
	 */
	private String mdLink(EObject target, Object from) {
		if (target == null) {
			return "—";                      // the guard the guide asks for
		}
		String name = String.valueOf(target.eGet(authorName));
		String prefix = from instanceof EObject element && bookClass.isInstance(element)
				? "../authors/"              // a book's document lives one level down
				: "authors/";
		return "[" + name + "](" + prefix + kebab(name) + ".md)";
	}

	private String title(EObject book) {
		return String.valueOf(book.eGet(bookTitle));
	}

	private static String kebab(String text) {
		return text == null ? "" : text.toLowerCase().replaceAll("[^a-z0-9]+", "-")
				.replaceAll("(^-|-$)", "");
	}

	// --- helpers ---

	private M2tResult generate(EObject input) throws Exception {
		return generate(input, AUTHOR_LINE);
	}

	private M2tResult generate(EObject input, String authorLine) throws Exception {
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(NS_URI, library);

		OclConfiguration ocl = OclConfiguration.builder(new OclParserSupport(registry))
				.customOperationsEnabled(true)
				.addOperationProvider(docOperations())
				.build();

		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(ocl)
				.packageRegistry(registry)
				.generationStrategy(new FileSystemGenerationStrategy(outputDirectory))
				.build());

		Module module = engine.parse(TEMPLATE.formatted(authorLine), "librarydoc");
		engine.link(module);
		return engine.execute(module, M2tContext.of(input));
	}

	private EObject book(String title, String author) {
		EObject book = EcoreUtil.create(bookClass);
		book.eSet(bookTitle, title);
		EObject writer = EcoreUtil.create(authorClass);
		writer.eSet(authorName, author);
		book.eSet(bookAuthor, writer);
		return book;
	}

	private EAttribute attribute(String name) {
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(name);
		attribute.setEType(EcorePackage.Literals.ESTRING);
		return attribute;
	}
}
