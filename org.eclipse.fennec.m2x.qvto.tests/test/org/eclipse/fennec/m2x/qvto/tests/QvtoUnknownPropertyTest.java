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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.util.EcoreUtil;
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
 * A property the source type does not declare is reported by {@code parse}, not left for
 * {@code execute} to stumble over — the second half of #430.
 *
 * <p>Only once the whole unit stands: a unit adds features to its classes after the expressions
 * that read them, so an {@code intermediate property} and an opposite are not mistakes.
 */
class QvtoUnknownPropertyTest {

	private static final String NS_URI = "http://example.org/m2x/qvto/unknown-property";

	@Test
	@DisplayName("an unknown property of a known type is reported at parse, with its place")
	void anUnknownProperty_isReportedByParse() {
		QvtoParseException rejected = assertThrows(QvtoParseException.class,
				() -> parse("mapping Item::copy() : Item { result.name := self.nmae; }"));

		assertTrue(rejected.getMessage().contains("Unknown property (Item::nmae)"),
				rejected.getMessage());
		assertTrue(rejected.getErrors().stream().anyMatch(
				d -> d.getMessage().contains("Unknown property (Item::nmae)")
						&& d.getLine() == 4 && d.getColumn() == 50),
				"reported at the name, not at the unit: " + rejected.getErrors().stream()
						.map(d -> d.getLine() + ":" + d.getColumn() + " " + d.getMessage()).toList());
	}

	@Test
	@DisplayName("an unknown property in an assignment target is reported too")
	void anUnknownAssignmentTarget_isReportedByParse() {
		QvtoParseException rejected = assertThrows(QvtoParseException.class,
				() -> parse("mapping Item::copy() : Item { result.nmae := self.name; }"));

		assertTrue(rejected.getMessage().contains("Unknown property (Item::nmae)"),
				rejected.getMessage());
	}

	@Test
	@DisplayName("an intermediate property is not unknown, wherever it is declared")
	void anIntermediateProperty_isAccepted() throws Exception {
		// declared after the mapping that reads it: the check waits for the whole unit
		OperationalTransformation t = parse("""
				mapping Item::copy() : Item { result.name := self.extra; }
				intermediate property Item::extra : String;""");

		assertEquals("", run(t));
	}

	@Test
	@DisplayName("a property of a supertype is not unknown")
	void anInheritedProperty_isAccepted() throws Exception {
		assertEquals("hello", run(parse("mapping Item::copy() : Item { result.name := self.name; }")));
	}

	@Test
	@DisplayName("a property on a source of unknown type is still left to the run")
	void anUnknownSourceType_isNotReported() throws Exception {
		// oclAsType(OclAny) loses the type, so the parser cannot say — and must not guess
		parse("mapping Item::copy() : Item { result.name := self.oclAsType(OclAny).anything.toString(); }");
	}

	// ==================== Helpers ====================

	private static EPackage metamodel() {
		EPackage kw = EcoreFactory.eINSTANCE.createEPackage();
		kw.setName("kw");
		kw.setNsPrefix("kw");
		kw.setNsURI(NS_URI);
		EClass item = EcoreFactory.eINSTANCE.createEClass();
		item.setName("Item");
		EAttribute name = EcoreFactory.eINSTANCE.createEAttribute();
		name.setName("name");
		name.setEType(EcorePackage.Literals.ESTRING);
		item.getEStructuralFeatures().add(name);
		kw.getEClassifiers().add(item);
		return kw;
	}

	private static QvtoEngine engine;
	private static EPackage registered;

	private static OperationalTransformation parse(String body) throws QvtoParseException {
		registered = metamodel();
		engine = QvtoEngines.create(QvtoConfiguration
				.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.registerPackage(registered).build());
		return engine.parse("""
				modeltype KW uses '%s';
				transformation T(in src : KW, out dst : KW);
				main() { src.rootObjects()[Item]->map copy(); }
				%s
				""".formatted(NS_URI, body), "T");
	}

	private static Object run(OperationalTransformation t) {
		EClass item = (EClass) registered.getEClassifier("Item");
		EObject in = EcoreUtil.create(item);
		in.eSet(item.getEStructuralFeature("name"), "hello");
		BasicQvtoModelExtent src = new BasicQvtoModelExtent(in);
		BasicQvtoModelExtent dst = new BasicQvtoModelExtent();
		QvtoExecutionResult result = engine.execute(t,
				QvtoExecutionContext.of(List.of(src, dst), Map.of()));
		assertTrue(result.isSuccess(), "" + result.diagnostics());
		Object value = dst.getContents().get(0).eGet(item.getEStructuralFeature("name"));
		return value == null ? "" : value;
	}
}
