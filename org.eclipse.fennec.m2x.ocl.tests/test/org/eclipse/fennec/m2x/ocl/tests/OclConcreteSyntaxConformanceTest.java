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
package org.eclipse.fennec.m2x.ocl.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Concrete syntax of OCL v2.4 Chapter 9 found missing by the audit (#320, #323, #325).
 */
class OclConcreteSyntaxConformanceTest extends AbstractOclTest {

	static EObject self;
	static EClass node;
	static EObject nodeInstance;

	@BeforeAll
	static void setUp() {
		self = createPerson("Alice", 30, 50000.0, true);
		// A class whose features are named like Complete OCL keywords
		EPackage pkg = EcoreFactory.eINSTANCE.createEPackage();
		pkg.setName("kw");
		pkg.setNsURI("http://test/kw");
		pkg.setNsPrefix("kw");
		node = EcoreFactory.eINSTANCE.createEClass();
		node.setName("Node");
		pkg.getEClassifiers().add(node);
		for (String name : new String[] { "body", "context", "init", "derive", "pre", "post", "package" }) {
			EAttribute attribute = EcoreFactory.eINSTANCE.createEAttribute();
			attribute.setName(name);
			attribute.setEType(EcorePackage.Literals.ESTRING);
			node.getEStructuralFeatures().add(attribute);
		}
		nodeInstance = pkg.getEFactoryInstance().create(node);
		nodeInstance.eSet(node.getEStructuralFeature("body"), "B");
		nodeInstance.eSet(node.getEStructuralFeature("context"), "C");
		nodeInstance.eSet(node.getEStructuralFeature("pre"), "P");
	}

	static Object evalOnNode(String expression) throws OclParseException {
		return engine.evaluate(engine.parse(expression, node), OclContext.of(nodeInstance));
	}

	/** §13.2 (4): Complete OCL keywords are not reserved in Essential OCL. */
	@Nested
	class DocumentKeywordsAsNames {

		@Test
		void featureNamedLikeAKeyword_isNavigable() throws OclParseException {
			assertEquals("B", evalOnNode("self.body"));
			assertEquals("C", evalOnNode("self.context"));
			assertEquals("P", evalOnNode("self.pre"));
			assertEquals("B", evalOnNode("body"));
		}

		@Test
		void keywordAsVariableName() throws OclParseException {
			assertEquals(2, eval("let init : Integer = 2 in init", self));
			assertEquals(3, eval("Sequence{1, 2}->iterate(i; inv : Integer = 0 | inv + i)", self));
		}

		@Test
		void atPre_stillParses() throws OclParseException {
			assertEquals("Alice", eval("self.name@pre", self));
		}
	}

	/** §9.3.4 [B]: escaped names use the escapes of a string literal. */
	@Nested
	class EscapedNames {

		@Test
		void specEscape_andDoubledQuote_areBothAccepted() throws OclParseException {
			assertEquals("it's", eval("Tuple{_'it\\'s' = 'it\\'s'}._'it\\'s'", self));
			assertEquals(1, eval("Tuple{_'it''s' = 1}._'it\\'s'", self));
			assertEquals(2, eval("let _'a\\tb' : Integer = 2 in _'a\\tb'", self));
		}

		@Test
		void supplementaryPlaneLetter_isAnIdentifierCharacter() throws OclParseException {
			// U+1D49C MATHEMATICAL SCRIPT CAPITAL A (§9.3.4 [#x10000-#xEFFFF])
			assertEquals(7, eval("let 𝒜 : Integer = 7 in 𝒜", self));
		}
	}

	/** §9.3.26: iterate may omit its iterator variable (#320). */
	@Nested
	class IterateWithoutIterator {

		@Test
		void implicitIterator_inIterate() throws OclParseException {
			assertEquals(3, eval("Sequence{1, 2, 3}->iterate(acc : Integer = 0 | acc + 1)", self));
			assertEquals("abc", eval("Sequence{'a', 'b', 'c'}->iterate(acc : String = '' | acc.concat(toString()))", self));
		}

		@Test
		void explicitIterator_stillWorks() throws OclParseException {
			assertEquals(6, eval("Sequence{1, 2, 3}->iterate(i : Integer; acc : Integer = 0 | acc + i)", self));
		}
	}

	/** §7.5.14 / §9.3.35: '@pre' is postfixed to the operation name, before the parameters (#323). */
	@Nested
	class AtPreOnOperations {

		@Test
		void atPreBeforeTheParameters_parses() throws OclParseException {
			assertEquals(5, eval("self.name.size@pre()", self));
			assertEquals(5, eval("self.name.size()@pre", self));
		}

		@Test
		void atPreOnBothSides_isRejectedOnlyIfMalformed() {
			assertThrows(OclParseException.class, () -> eval("self.name.size@pre@pre()", self));
		}
	}
}
