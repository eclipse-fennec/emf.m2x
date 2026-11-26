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
 * OCL v2.4 §9.3: {@code _post} is the escape OCL 2.0 and 2.2 used for a keyword, deprecated and
 * kept for compatibility. The keywords are the M2T grammar's own (#430).
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
	@DisplayName("an underscore before a word that is not a keyword stays part of the name")
	void anUnderscorePrefixedName_staysAsItIs() throws Exception {
		// 'label' is not a keyword, so _label stays _label — and the metamodel has no such feature
		M2tResult result = generate("label", "[i._label/]");

		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() >= Diagnostic.ERROR),
				"a name that is not a deprecated escape has to stay unresolved: " + result.diagnostics());
	}

	@Test
	@DisplayName("_post is reported as deprecated, at its place")
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
		EPackage kw = EcoreFactory.eINSTANCE.createEPackage();
		kw.setName("kw");
		kw.setNsPrefix("kw");
		kw.setNsURI(NS_URI);
		EClass item = EcoreFactory.eINSTANCE.createEClass();
		item.setName("Item");
		EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
		attribute.setName(feature);
		attribute.setEType(EcorePackage.Literals.ESTRING);
		item.getEStructuralFeatures().add(attribute);
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
		instance.eSet(attribute, "value");
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
