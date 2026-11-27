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
import static org.junit.jupiter.api.Assertions.assertThrows;
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
 * OCL v2.4 §9.4.1: a property name with a leading underscore "is first looked up in the metamodel
 * with the underscore prefix, and if no value is found, the symbol is looked up again without the
 * underscore prefix" — the escape OCL 2.0 and 2.2 used, deprecated by §9.3 and kept for
 * compatibility (#430).
 *
 * <p>Second, not first: the name as written wins, and only a miss falls back. Nothing outside a
 * metamodel lookup is touched, so a variable or parameter keeps the name it was declared with.
 */
class QvtoDeprecatedEscapeTest {

	private static final String NS_URI = "http://example.org/m2x/qvto/deprecated-escape";

	/** A keyword literal of an ANTLR grammar: {@code 'post'}. */
	private static final Pattern KEYWORD = Pattern.compile("'([A-Za-z][A-Za-z0-9_]*)'");

	@Test
	@DisplayName("_post reaches the feature post when the metamodel has no _post")
	void aMissingUnderscoreName_fallsBackToTheNameWithoutIt() throws Exception {
		EPackage kw = packageWithFeaturesNamed(Set.of("post"));
		EClass item = (EClass) kw.getEClassifier("Item");

		OperationalTransformation t = engine(kw).parse(probe("_post"), "T");

		assertSame(item.getEStructuralFeature("post"), referredProperty(t));
	}

	@Test
	@DisplayName("a feature really called _post wins: the lookup as written comes first (§9.4.1)")
	void anExistingUnderscoreName_isNotShadowed() throws Exception {
		EPackage kw = packageWithFeaturesNamed(Set.of("post"));
		EClass item = (EClass) kw.getEClassifier("Item");
		EAttribute underscored = EcoreFactory.eINSTANCE.createEAttribute();
		underscored.setName("_post");
		underscored.setEType(EcorePackage.Literals.ESTRING);
		item.getEStructuralFeatures().add(underscored);

		assertSame(underscored, referredProperty(engine(kw).parse(probe("_post"), "T")),
				"_post has to reach the feature of that name, not post");
		assertSame(item.getEStructuralFeature("post"),
				referredProperty(engine(kw).parse(probe("_'post'"), "T")));
	}

	@Test
	@DisplayName("an underscore name with nothing to fall back to is rejected, not invented")
	void anUnderscorePrefixedName_isNotInvented() {
		EPackage kw = packageWithFeaturesNamed(Set.of("post"));

		QvtoParseException rejected = assertThrows(QvtoParseException.class,
				() -> engine(kw).parse(probe("_label"), "T"));

		assertTrue(rejected.getMessage().contains("Unknown property (Item::_label)"),
				"neither _label nor label is a feature, so the name is reported as written: "
						+ rejected.getMessage());
	}

	@Test
	@DisplayName("_<keyword> reaches the feature <keyword>, for every keyword of the grammar")
	void everyKeyword_isReachableThroughTheDeprecatedEscape() {
		// Not the set the code keys on — it keys on nothing. These are the names an author would
		// have escaped in OCL 2.0, so they are what the compatibility behaviour is for.
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
	@DisplayName("a variable declared with a leading underscore keeps its name")
	void anUnderscoreVariable_isNotRewritten() throws Exception {
		// _result used to be rewritten to result before anything was looked up, which made the
		// local the mapping's own result variable: the transformation ran and wrote nothing.
		assertEquals("local", copyWith(
				"var _result : String := 'local'; result.post := _result;"));
	}

	@Test
	@DisplayName("a variable called _self does not rebind self")
	void anUnderscoreSelfVariable_isNotRewritten() throws Exception {
		assertEquals("local", copyWith("var _self : String := 'local'; result.post := _self;"));
	}

	@Test
	@DisplayName("a parameter declared with a leading underscore keeps its name")
	void anUnderscoreParameter_isNotRewritten() throws Exception {
		EPackage kw = packageWithFeaturesNamed(Set.of("post"));
		EClass item = (EClass) kw.getEClassifier("Item");
		QvtoEngine engine = engine(kw);
		OperationalTransformation t = engine.parse("""
				modeltype KW uses '%s';
				transformation T(in src : KW, out dst : KW);
				main() { src.rootObjects()[Item]->map copy(); }
				mapping Item::copy() : Item { result.post := self.q('p'); }
				query Item::q(_in : String) : String { _in }
				""".formatted(NS_URI), "T");

		Run run = run(engine, t, item);

		assertTrue(run.result().isSuccess(), "" + run.result().diagnostics());
		assertEquals(List.of(), run.result().diagnostics().stream().map(Object::toString).toList(),
				"a parameter name is not a metamodel lookup, so nothing is deprecated here");
		assertEquals("p", run.post());
	}

	@Test
	@DisplayName("the fallback is reported as deprecated: a WARNING at the name when the transformation runs")
	void theDeprecatedEscape_isWarnedAbout() throws Exception {
		EPackage kw = packageWithFeaturesNamed(Set.of("post"));
		EClass item = (EClass) kw.getEClassifier("Item");

		QvtoEngine engine = engine(kw);
		OperationalTransformation t = engine.parse("""
				modeltype KW uses '%s';
				transformation T(in src : KW, out dst : KW);
				main() { src.rootObjects()[Item]->map copy(); }
				mapping Item::copy() : Item { result.post := self._post; }
				""".formatted(NS_URI), "T");

		Run run = run(engine, t, item);

		assertTrue(run.result().isSuccess(), "" + run.result().diagnostics());
		assertEquals("hello", run.post(), "the escape reached the feature");
		assertTrue(run.result().diagnostics().stream().anyMatch(d -> d.getSeverity() == Diagnostic.WARNING
				&& d.getMessage().equals(
						"T:4:50: '_post' is a deprecated escape of 'post'; write _'post' (§9.3)")),
				"" + run.result().diagnostics());
	}

	// ==================== Helpers ====================

	/** Runs {@code body} as the whole of {@code Item::copy}, and returns the copied post. */
	private static String copyWith(String body) throws Exception {
		EPackage kw = packageWithFeaturesNamed(Set.of("post"));
		EClass item = (EClass) kw.getEClassifier("Item");
		QvtoEngine engine = engine(kw);
		OperationalTransformation t = engine.parse("""
				modeltype KW uses '%s';
				transformation T(in src : KW, out dst : KW);
				main() { src.rootObjects()[Item]->map copy(); }
				mapping Item::copy() : Item { %s }
				""".formatted(NS_URI, body), "T");

		Run run = run(engine, t, item);
		assertTrue(run.result().isSuccess(), "" + run.result().diagnostics());
		return (String) run.post();
	}

	/** A run of a transformation: what it reported, and the {@code post} it copied. */
	private record Run(QvtoExecutionResult result, Object post) {}

	private static Run run(QvtoEngine engine, OperationalTransformation t, EClass item) {
		EStructuralFeature post = item.getEStructuralFeature("post");
		EObject in = EcoreUtil.create(item);
		in.eSet(post, "hello");
		BasicQvtoModelExtent src = new BasicQvtoModelExtent(in);
		BasicQvtoModelExtent dst = new BasicQvtoModelExtent();
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(List.of(src, dst), Map.of()));
		return new Run(result, dst.getContents().isEmpty() ? null
				: dst.getContents().get(0).eGet(post));
	}

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
