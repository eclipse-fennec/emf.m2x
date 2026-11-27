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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertNotNull;
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
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
 * OCL v2.4 §13.2 (4) names that are keywords of a Complete OCL document only. QVT-O takes its
 * identifier from OCL (§8.4.7), so a feature may be called {@code post} or {@code context}
 * without an escape (#430).
 */
class QvtoDocumentKeywordNameTest {

	/**
	 * The names {@code qvtoDocumentKeyword} admits, read from the grammar rather than copied —
	 * a hand-kept list drifting from the grammar beside it is what #430 was.
	 */
	private static final List<String> DOCUMENT_KEYWORDS = documentKeywords();

	private static final String NS_URI = "http://example.org/m2x/qvto/document-keywords";

	@Test
	@DisplayName("every OCL document keyword is a usable feature name, resolved to the metamodel's feature")
	void everyDocumentKeywordAsName_resolves() throws Exception {
		List<String> rejected = new ArrayList<>();
		for (String word : DOCUMENT_KEYWORDS) {
			EPackage kw = keywordPackage();
			EClass item = (EClass) kw.getEClassifier("Item");
			try {
				OperationalTransformation t = parse(kw, probe(word));
				assertSame(item.getEStructuralFeature(word), referredProperty(t),
						"'" + word + "' has to resolve to the feature of the metamodel");
			} catch (QvtoParseException notAName) {
				rejected.add(word);
			}
		}
		assertEquals(List.of(), rejected, "document keywords the parser still refuses as names");
	}

	@Test
	@DisplayName("import stays reserved — QVT-O uses it as syntax (§8.4), as QVT-R does")
	void importAsName_isRejected() {
		EPackage kw = keywordPackage();
		EAttribute imported = EcoreFactory.eINSTANCE.createEAttribute();
		imported.setName("import");
		imported.setEType(EcorePackage.Literals.ESTRING);
		((EClass) kw.getEClassifier("Item")).getEStructuralFeatures().add(imported);

		assertThrows(QvtoParseException.class, () -> parse(kw, probe("import")));
	}

	@Test
	@DisplayName("a word that is QVT-O syntax in every position stays reserved")
	void expressionKeywordAsName_isRejected() {
		EPackage kw = keywordPackage();
		assertThrows(QvtoParseException.class, () -> parse(kw, probe("then")));
	}

	@Test
	@DisplayName("the escape keeps working for the same names")
	void anEscapedDocumentKeyword_isAccepted() throws Exception {
		EPackage kw = keywordPackage();
		EClass item = (EClass) kw.getEClassifier("Item");

		OperationalTransformation t = parse(kw, probe("_'post'"));

		assertSame(item.getEStructuralFeature("post"), referredProperty(t));
	}

	@Test
	@DisplayName("a transformation reading and writing an unescaped post copies the value")
	void aDocumentKeywordFeature_isReadAndWritten() throws Exception {
		EPackage kw = keywordPackage();
		EClass item = (EClass) kw.getEClassifier("Item");
		EStructuralFeature post = item.getEStructuralFeature("post");

		QvtoEngine engine = engine(kw);
		OperationalTransformation t = engine.parse("""
				modeltype KW uses '%s';
				transformation T(in src : KW, out dst : KW);
				main() { src.rootObjects()[Item]->map copy(); }
				mapping Item::copy() : Item { result.post := self.post; }
				""".formatted(NS_URI), "T");

		EObject in = EcoreUtil.create(item);
		in.eSet(post, "hello");
		BasicQvtoModelExtent src = new BasicQvtoModelExtent(in);
		BasicQvtoModelExtent dst = new BasicQvtoModelExtent();

		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(List.of(src, dst), Map.of()));

		assertEquals(List.of(), result.diagnostics().stream().map(Object::toString).toList());
		assertEquals(1, dst.getContents().size());
		assertEquals("hello", dst.getContents().get(0).eGet(post));
	}

	// ==================== Helpers ====================

	/** The alternatives of the {@code qvtoDocumentKeyword} rule of {@code QvtO.g4}. */
	private static List<String> documentKeywords() {
		String grammar;
		try (InputStream in = QvtoDocumentKeywordNameTest.class.getResourceAsStream("/grammar/QvtO.g4")) {
			assertNotNull(in, "/grammar/QvtO.g4 has to be on the classpath");
			grammar = new String(in.readAllBytes(), StandardCharsets.UTF_8);
		} catch (IOException e) {
			return fail("cannot read /grammar/QvtO.g4", e);
		}
		Matcher rule = Pattern.compile("(?m)^qvtoDocumentKeyword\\s*:(.*?);", Pattern.DOTALL)
				.matcher(grammar);
		assertTrue(rule.find(), "QvtO.g4 has to declare a qvtoDocumentKeyword rule");
		List<String> words = new ArrayList<>();
		Matcher word = Pattern.compile("'([A-Za-z][A-Za-z0-9_]*)'").matcher(rule.group(1));
		while (word.find()) {
			words.add(word.group(1));
		}
		assertTrue(words.contains("post"), "'post' has to be one of them");
		return words;
	}

	/** {@code Item} with one {@code EString} feature per document keyword. */
	private static EPackage keywordPackage() {
		EPackage kw = EcoreFactory.eINSTANCE.createEPackage();
		kw.setName("kw");
		kw.setNsPrefix("kw");
		kw.setNsURI(NS_URI);
		EClass item = EcoreFactory.eINSTANCE.createEClass();
		item.setName("Item");
		for (String word : DOCUMENT_KEYWORDS) {
			EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
			attribute.setName(word);
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

	private static OperationalTransformation parse(EPackage kw, String source)
			throws QvtoParseException {
		return engine(kw).parse(source, "T");
	}

	private static QvtoEngine engine(EPackage kw) {
		return QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(kw).build());
	}

	/** The feature of the single property call in the parsed unit. */
	private static EStructuralFeature referredProperty(OperationalTransformation t) {
		for (Iterator<EObject> it = EcoreUtil.getAllContents(t, true); it.hasNext();) {
			if (it.next() instanceof PropertyCallExp call) {
				return call.getReferredProperty();
			}
		}
		return fail("no property call in the parsed transformation");
	}
}
