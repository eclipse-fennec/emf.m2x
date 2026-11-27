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
package org.eclipse.fennec.m2x.m2t.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import java.util.function.Consumer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tParseException;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngines;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * OCL v2.4 §9.4.1: a property name with a leading underscore "is first looked up in the metamodel
 * with the underscore prefix, and if no value is found, the symbol is looked up again without the
 * underscore prefix" — the escape OCL 2.0 and 2.2 used, deprecated by §9.3 (#430).
 *
 * <p>Second, not first: a feature really called {@code _post} keeps {@code _post}, and a name that
 * is not a metamodel lookup — an iterator variable, a template parameter — is never rewritten.
 */
class M2tDeprecatedEscapeTest {

	private static final String NS_URI = "http://example.org/m2x/m2t/deprecated-escape";

	/** A keyword literal of an ANTLR grammar: {@code 'post'}. */
	private static final Pattern KEYWORD = Pattern.compile("'([A-Za-z][A-Za-z0-9_]*)'");

	@Test
	@DisplayName("_<keyword> reads the feature <keyword>, for every keyword of the grammar")
	void everyKeyword_isReachableThroughTheDeprecatedEscape() {
		Set<String> keywords = grammarKeywords();
		assertTrue(keywords.contains("post"), "'post' has to be among the grammar's keywords");

		List<String> unreached = new ArrayList<>();
		for (String word : keywords) {
			try {
				M2tResult result = generate(word, "[i._" + word + "/]");
				if (!result.isSuccess() || !"value".equals(output(result))) {
					unreached.add(word + " -> '" + output(result) + "' " + result.diagnostics());
				}
			} catch (M2tParseException notParsed) {
				unreached.add(word + " (" + notParsed.getMessage() + ")");
			}
		}
		assertEquals(List.of(), unreached, "keywords the deprecated escape does not reach");
	}

	@Test
	@DisplayName("an underscore name with nothing to fall back to stays unresolved")
	void anUnderscorePrefixedName_isNotInvented() throws Exception {
		// the metamodel has 'post' only, so neither _label nor label is a feature
		M2tResult result = generate("post", "[i._label/]");

		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() >= Diagnostic.ERROR),
				"a name with no second spelling has to stay unresolved: " + result.diagnostics());
	}

	@Test
	@DisplayName("a feature really called _post wins: the lookup as written comes first (§9.4.1)")
	void anExistingUnderscoreName_isNotShadowed() throws Exception {
		M2tResult result = generateOn(item -> {
			addAttribute(item, "post", "keyword");
			addAttribute(item, "_post", "underscored");
		}, "[i._post/]");

		assertTrue(result.isSuccess(), "" + result.diagnostics());
		assertEquals("underscored", output(result));
		assertEquals(List.of(), result.diagnostics().stream().map(Object::toString).toList(),
				"the name as written resolved, so nothing is deprecated here");
	}

	@Test
	@DisplayName("an iterator variable with a leading underscore keeps its name")
	void anUnderscoreIterator_isNotRewritten() throws Exception {
		// _self used to be rewritten to self before anything was looked up
		M2tResult result = generate("post", "[for (_self : String | Sequence{i.post})][_self/][/for]");

		assertTrue(result.isSuccess(), "" + result.diagnostics());
		assertEquals("value", output(result));
	}

	@Test
	@DisplayName("the fallback is reported as deprecated, at its place")
	void theDeprecatedEscape_isWarnedAbout() throws Exception {
		M2tResult result = generate("post", "[i._post/]");

		assertTrue(result.isSuccess(), "" + result.diagnostics());
		assertEquals("value", output(result));
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
				&& d.getMessage().equals(
						"doc:4:3: '_post' is a deprecated escape of 'post'; write _'post' (§9.3)")),
				"" + result.diagnostics());
	}

	// ==================== Helpers ====================

	/** Generates {@code body} over an {@code Item} whose single feature {@code feature} is "value". */
	private static M2tResult generate(String feature, String body) throws M2tParseException {
		return generateOn(item -> addAttribute(item, feature, "value"), body);
	}

	/** Adds an {@code EString} feature, and remembers the value an instance of it carries. */
	private static void addAttribute(EClass item, String name, String value) {
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(name);
		attribute.setEType(EcorePackage.Literals.ESTRING);
		attribute.setDefaultValueLiteral(value);
		item.getEStructuralFeatures().add(attribute);
	}

	/** Generates {@code body} over an {@code Item} the caller gives the features of. */
	private static M2tResult generateOn(Consumer<EClass> features, String body)
			throws M2tParseException {
		EPackage kw = EcoreFactory.eINSTANCE.createEPackage();
		kw.setName("kw");
		kw.setNsPrefix("kw");
		kw.setNsURI(NS_URI);
		EClass item = EcoreFactory.eINSTANCE.createEClass();
		item.setName("Item");
		features.accept(item);
		kw.getEClassifiers().add(item);

		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(NS_URI, kw);
		M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport(registry)).build())
				.packageRegistry(registry)
				.build());

		Module module = engine.parse("""
				[module doc(_'%s')/]
				[template public main(i : Item)]
				[file ('out.txt', false)]
				%s
				[/file]
				[/template]
				""".formatted(NS_URI, body), "doc");
		engine.link(module);

		EObject instance = EcoreUtil.create(item);
		for (EAttribute attribute : item.getEAttributes()) {
			instance.eSet(attribute, attribute.getDefaultValueLiteral());
		}
		return engine.execute(module, M2tContext.of(instance));
	}

	private static String output(M2tResult result) {
		return String.join("", result.generatedFiles().values()).strip();
	}

	private static Set<String> grammarKeywords() {
		Set<String> keywords = new TreeSet<>();
		for (String grammar : List.of("/grammar/M2tLexer.g4", "/grammar/M2tParser.g4")) {
			Matcher matcher = KEYWORD.matcher(withoutComments(resource(grammar)));
			while (matcher.find()) {
				keywords.add(matcher.group(1));
			}
		}
		return keywords;
	}

	private static String resource(String path) {
		try (InputStream in = M2tDeprecatedEscapeTest.class.getResourceAsStream(path)) {
			assertNotNull(in, path + " has to be on the classpath");
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return fail("cannot read " + path, e);
		}
	}

	private static String withoutComments(String grammar) {
		return grammar.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("//[^\n]*", "");
	}
}
