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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeSet;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * OCL v2.4 §9.3: {@code _post} is the escape OCL 2.0 and 2.2 used for a keyword, deprecated and
 * kept for compatibility. The keywords are those of the grammar, OCL's included — not the ones
 * {@code qvtoIdentifier} refuses, which is a different and moving set (#430).
 */
class QvtoDeprecatedEscapeTest {

	private static final String NS_URI = "http://example.org/m2x/qvto/deprecated-escape";

	/** A keyword literal of an ANTLR grammar: {@code 'post'}. */
	private static final Pattern KEYWORD = Pattern.compile("'([A-Za-z][A-Za-z0-9_]*)'");

	@Test
	@DisplayName("_<keyword> resolves to <keyword>, for every keyword of the grammar")
	void everyKeyword_isReachableThroughTheDeprecatedEscape() {
		Set<String> keywords = grammarKeywords();
		assertTrue(keywords.contains("post"), "'post' has to be among the grammar's keywords");

		EPackage kw = packageWithFeaturesNamed(keywords);
		EClass item = (EClass) kw.getEClassifier("Item");
		QvtoEngine engine = engine(kw);

		List<String> unresolved = new ArrayList<>();
		for (String word : keywords) {
			try {
				OperationalTransformation t = engine.parse(probe("_" + word), "T");
				if (referredProperty(t) != item.getEStructuralFeature(word)) {
					unresolved.add(word);
				}
			} catch (QvtoParseException notParsed) {
				unresolved.add(word + " (" + notParsed.getMessage() + ")");
			}
		}
		assertEquals(List.of(), unresolved, "keywords the deprecated escape does not reach");
	}

	@Test
	@DisplayName("a feature really called _post is reached by the explicit escape, not the deprecated one")
	void aFeatureNamedWithALeadingUnderscore_needsTheExplicitEscape() throws Exception {
		EPackage kw = packageWithFeaturesNamed(Set.of("post"));
		EClass item = (EClass) kw.getEClassifier("Item");
		EAttribute underscored = EcoreFactory.eINSTANCE.createEAttribute();
		underscored.setName("_post");
		underscored.setEType(EcorePackage.Literals.ESTRING);
		item.getEStructuralFeatures().add(underscored);

		// The deprecated escape is a spelling of the name, resolved before any lookup, so it
		// reaches 'post' even where a feature '_post' exists. That one keeps _'_post' (§9.3.4).
		assertSame(item.getEStructuralFeature("post"),
				referredProperty(engine(kw).parse(probe("_post"), "T")));
		assertSame(underscored,
				referredProperty(engine(kw).parse(probe("_\'_post\'"), "T")));
	}

	@Test
	@DisplayName("an underscore before a word that is not a keyword stays part of the name")
	void anUnderscorePrefixedName_staysAsItIs() throws Exception {
		EPackage kw = packageWithFeaturesNamed(Set.of("name"));
		EClass item = (EClass) kw.getEClassifier("Item");

		OperationalTransformation t = engine(kw).parse(probe("_name"), "T");

		assertEquals("_name", referredProperty(t).getName());
		assertNotNull(item);
	}

	@Test
	@DisplayName("_post is reported as deprecated: a WARNING at its place when the transformation runs")
	void theDeprecatedEscape_isWarnedAbout() throws Exception {
		EPackage kw = packageWithFeaturesNamed(Set.of("post"));
		EClass item = (EClass) kw.getEClassifier("Item");
		EStructuralFeature post = item.getEStructuralFeature("post");

		QvtoEngine engine = engine(kw);
		OperationalTransformation t = engine.parse("""
				modeltype KW uses '%s';
				transformation T(in src : KW, out dst : KW);
				main() { src.rootObjects()[Item]->map copy(); }
				mapping Item::copy() : Item { result.post := self._post; }
				""".formatted(NS_URI), "T");

		EObject in = EcoreUtil.create(item);
		in.eSet(post, "hello");
		BasicQvtoModelExtent src = new BasicQvtoModelExtent(in);
		BasicQvtoModelExtent dst = new BasicQvtoModelExtent();

		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(List.of(src, dst), Map.of()));

		assertTrue(result.isSuccess(), "" + result.diagnostics());
		assertEquals("hello", dst.getContents().get(0).eGet(post), "the escape reached the feature");
		assertTrue(result.diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
				&& d.getMessage().equals(
						"T:4:50: '_post' is a deprecated escape of 'post'; write _'post' (§9.3)")),
				"" + result.diagnostics());
	}

	// ==================== Helpers ====================

	/** Every keyword literal of the QVT-O grammar and the OCL grammar it imports. */
	private static Set<String> grammarKeywords() {
		Set<String> keywords = new TreeSet<>();
		for (String grammar : List.of("/grammar/QvtO.g4", "/grammar/Ocl.g4")) {
			Matcher matcher = KEYWORD.matcher(withoutComments(resource(grammar)));
			while (matcher.find()) {
				keywords.add(matcher.group(1));
			}
		}
		return keywords;
	}

	private static String resource(String path) {
		try (InputStream in = QvtoDeprecatedEscapeTest.class.getResourceAsStream(path)) {
			assertNotNull(in, path + " has to be on the classpath");
			return new String(in.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return fail("cannot read " + path, e);
		}
	}

	private static String withoutComments(String grammar) {
		return grammar.replaceAll("(?s)/\\*.*?\\*/", "").replaceAll("//[^\n]*", "");
	}

	private static EPackage packageWithFeaturesNamed(Set<String> names) {
		EPackage kw = EcoreFactory.eINSTANCE.createEPackage();
		kw.setName("kw");
		kw.setNsPrefix("kw");
		kw.setNsURI(NS_URI);
		EClass item = EcoreFactory.eINSTANCE.createEClass();
		item.setName("Item");
		for (String name : names) {
			EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
			attribute.setName(name);
			attribute.setEType(EcorePackage.Literals.ESTRING);
			item.getEStructuralFeatures().add(attribute);
		}
		kw.getEClassifiers().add(item);
		return kw;
	}

	private static String probe(String access) {
		return """
				modeltype KW uses '%s';
				transformation T(in src : KW);
				main() {}
				query Item::probe() : String { self.%s }
				""".formatted(NS_URI, access);
	}

	private static QvtoEngine engine(EPackage kw) {
		return QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(kw).build());
	}

	private static EStructuralFeature referredProperty(OperationalTransformation t) {
		for (Iterator<EObject> it = EcoreUtil.getAllContents(t, true); it.hasNext();) {
			if (it.next() instanceof PropertyCallExp call) {
				return call.getReferredProperty();
			}
		}
		return fail("no property call in the parsed transformation");
	}
}
