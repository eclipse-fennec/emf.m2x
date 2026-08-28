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
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Enum literals against a <em>generated</em> model (#133). A generated package's {@code eGet}
 * returns the Java enum constant, not the {@code EEnumLiteral}; an enum literal expression has to
 * evaluate to the same value, or {@code status = Status::DEPRECATED} is false forever. The
 * compiled-unit metamodel is the generated model here — {@code CompiledUnitManifest.dependencyMode}
 * is a {@code DependencyMode} (the OCL metamodel's own enums are out: {@code Set}, {@code inv} and
 * their siblings are OCL keywords and cannot be written as path names).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclGeneratedEnumTest extends AbstractOclTest {

	static CompiledUnitManifest pinned;
	static CompiledUnitManifest embedded;

	@BeforeAll
	static void setUpManifests() {
		pinned = CompiledFactory.eINSTANCE.createCompiledUnitManifest();
		pinned.setDependencyMode(DependencyMode.PIN);
		embedded = CompiledFactory.eINSTANCE.createCompiledUnitManifest();
		embedded.setDependencyMode(DependencyMode.EMBED);
	}

	@Test
	void generatedModel_eGetReturnsTheJavaEnum_notTheLiteral() {
		Object value = pinned.eGet(pinned.eClass().getEStructuralFeature("dependencyMode"));
		assertSame(DependencyMode.PIN, value, "the premise of #133: a generated model holds the Enumerator");
	}

	@Test
	void equality_holdsAgainstAGeneratedModel() throws OclParseException {
		assertEquals(true, eval("self.dependencyMode = DependencyMode::pin", pinned));
		assertEquals(false, eval("self.dependencyMode = DependencyMode::embed", pinned));
		assertEquals(true, eval("self.dependencyMode = DependencyMode::embed", embedded));
	}

	@Test
	void inequality_holdsAgainstAGeneratedModel() throws OclParseException {
		assertEquals(true, eval("self.dependencyMode <> DependencyMode::embed", pinned));
		assertEquals(false, eval("self.dependencyMode <> DependencyMode::pin", pinned));
	}

	@Test
	void enumLiteralExpression_evaluatesToTheModelsValue() throws OclParseException {
		assertSame(DependencyMode.PIN, eval("DependencyMode::pin", pinned),
				"what the expression yields is what the model holds — one value, not two representations");
	}

	@Test
	void collectionMembership_bridgesTheTwoRepresentations() throws OclParseException {
		assertEquals(true, eval("Set{DependencyMode::pin, DependencyMode::embed}->includes(self.dependencyMode)", pinned));
		assertEquals(false, eval("Set{DependencyMode::embed}->includes(self.dependencyMode)", pinned));
		assertEquals(1, eval("Set{DependencyMode::pin, DependencyMode::pin}->size()", pinned),
				"the unique collection sees one value, hashed the same way it is compared");
	}

	@Test
	void ifExpression_branchesOnTheComparison() throws OclParseException {
		assertEquals("pin", eval("if self.dependencyMode = DependencyMode::pin then 'pin' else 'other' endif", pinned));
		assertEquals("other", eval("if self.dependencyMode = DependencyMode::pin then 'pin' else 'other' endif", embedded));
	}

	// ==== path-qualified literals, dynamic model ====

	@Test
	void packageQualifiedLiteral_resolves() throws OclParseException {
		var person = createPerson("Alice", 25, 30000.0, false);
		var status = (EEnum) companyPackage.getEClassifier("Status");
		person.eSet(personClass.getEStructuralFeature("status"), status.getEEnumLiteral("JUNIOR").getInstance());
		assertEquals(true, eval("self.status = company::Status::JUNIOR", person));
		assertEquals(false, eval("self.status = company::Status::SENIOR", person));
	}

	@Test
	void packageQualifiedLiteral_thatDoesNotExist_isAParseError() {
		var person = createPerson("Alice", 25, 30000.0, false);
		OclParseException failure = assertThrows(OclParseException.class,
				() -> engine.evaluate("self.status = company::Status::NOBODY", OclContext.of(person)));
		assertTrue(failure.getMessage().contains("Unknown enumeration literal"), failure.getMessage());
	}
}
