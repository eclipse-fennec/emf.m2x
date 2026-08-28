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

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Iterator;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.eclipse.fennec.m2x.ocl.parser.AbstractExpressionBuilder;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.Test;

/**
 * A property the source type does not declare: marked always, reported on request (#153).
 *
 * <p>The parser cannot decide the case on its own — a {@code def:} of an earlier parse call, a
 * library property such as {@code oclLocale}, or a feature of the runtime type where the static
 * one is {@code EObject} are all properties it cannot see and the evaluator can. So the default
 * stays lenient and leaves a marked placeholder, and a caller that hands over the whole document
 * switches the report on.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclStrictPropertyResolutionTest extends AbstractOclTest {

	@Test
	void lenientByDefault_theTypoBecomesAMarkedPlaceholder() throws OclParseException {
		OclExpression parsed = new OclParserSupport(registry()).parse("self.nam", personClass);
		EStructuralFeature property = firstProperty(parsed);
		assertEquals("nam", property.getName());
		assertTrue(AbstractExpressionBuilder.isUnresolvedProperty(property),
				"the placeholder says of itself that it was not resolved");
	}

	@Test
	void aResolvedProperty_isNotMarked() throws OclParseException {
		OclExpression parsed = new OclParserSupport(registry()).parse("self.name", personClass);
		assertFalse(AbstractExpressionBuilder.isUnresolvedProperty(firstProperty(parsed)),
				"EClass.name of the metamodel, not a placeholder");
	}

	@Test
	void strict_reportsTheTypo() {
		OclParserSupport strict = new OclParserSupport(registry()).strictPropertyResolution(true);
		OclParseException failure = assertThrows(OclParseException.class,
				() -> strict.parse("self.nam", personClass));
		assertTrue(failure.getMessage().contains("Unknown property (Person::nam)"), failure.getMessage());
	}

	@Test
	void strict_acceptsWhatTheTypeDeclares() {
		OclParserSupport strict = new OclParserSupport(registry()).strictPropertyResolution(true);
		assertDoesNotThrow(() -> strict.parse("self.name.size() > 0", personClass));
	}

	@Test
	void strict_acceptsADefOfTheSameDocument_whereverItStands() {
		OclParserSupport strict = new OclParserSupport(registry()).strictPropertyResolution(true);
		String document = """
				package company
				context Person
				inv adult: self.isAdult
				def: isAdult : Boolean = self.age >= 18
				endpackage
				""";
		assertDoesNotThrow(() -> strict.parseDocument(document),
				"the def stands below the invariant that uses it — collected before anything is built");
	}

	@Test
	void strict_reportsATypoInADocument() {
		OclParserSupport strict = new OclParserSupport(registry()).strictPropertyResolution(true);
		String document = """
				package company
				context Person
				inv adult: self.agee >= 18
				endpackage
				""";
		OclParseException failure = assertThrows(OclParseException.class, () -> strict.parseDocument(document));
		assertTrue(failure.getMessage().contains("Unknown property (Person::agee)"), failure.getMessage());
	}

	private static EPackage.Registry registry() {
		EPackage.Registry registry = new EPackageRegistryImpl();
		registry.put(companyPackage.getNsURI(), companyPackage);
		return registry;
	}

	private static EStructuralFeature firstProperty(OclExpression parsed) {
		for (Iterator<EObject> it = parsed.eAllContents(); it.hasNext();) {
			EObject node = it.next();
			if (node instanceof PropertyCallExp call) {
				return call.getReferredProperty();
			}
		}
		if (parsed instanceof PropertyCallExp call) {
			return call.getReferredProperty();
		}
		throw new AssertionError("no property call in " + parsed);
	}
}
