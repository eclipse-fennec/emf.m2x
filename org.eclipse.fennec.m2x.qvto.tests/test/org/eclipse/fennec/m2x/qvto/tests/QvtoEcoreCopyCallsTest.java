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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Operation calls and late resolves in an Ecore-to-Ecore copy, as reported in #296–#300.
 *
 * <p>The input is a package with {@code Derived} (first) extending {@code Base}, and an attribute
 * of type {@code EString} on {@code Base}. {@code Derived} comes first so that a supertype can
 * only be found by a late resolve. The Ecore package itself is a second input extent, as the
 * types model of the reports.
 */
class QvtoEcoreCopyCallsTest extends AbstractQvtoEngineTest {

	private static final String HEADER = """
			modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
			transformation T(in src : ECORE, in types : ECORE, out tgt : ECORE) {
			""";

	@Test
	@DisplayName("#296: an operation of the standard library on an xselect source")
	void standardOperationOnAnImperativeSource() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("""
				    main() {
				        log(types.rootObjects()[EPackage]->first().name);
				    }
				}
				""", out);

		assertSuccess(result);
		assertLogged(result, "ecore");
	}

	@Test
	@DisplayName("#296: a model operation on a source that comes from an xselect")
	void modelOperationAfterAnImperativeSource() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("""
				    main() {
				        src.objectsOfType(EAttribute)->map toAttribute();
				    }
				    mapping EAttribute::toAttribute() : EAttribute {
				        name := self.name;
				        eType := types.rootObjects()[EPackage]->first().getEClassifier('EString');
				    }
				}
				""", out);

		assertSuccess(result);
		EAttribute copy = (EAttribute) out.getContents().get(0);
		assertSame(EcorePackage.Literals.ESTRING, copy.getEType());
	}

	@Test
	@DisplayName("#296/§8.2.2.11: an operation on a late resolve defers the whole assignment")
	void operationOnALateResolve() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("""
				    main() {
				        src.objectsOfType(EClass)->map toClass();
				    }
				    mapping EClass::toClass() : EClass {
				        name := self.name;
				        eSuperTypes := self.eSuperTypes->first().late resolveone(EClass)->asOrderedSet();
				    }
				}
				""", out);

		assertSuccess(result);
		assertSupertypeCopied(out);
	}

	@Test
	@DisplayName("#297: a late resolve inside a block is deferred")
	void lateResolveInsideABlock() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("""
				    main() {
				        src.objectsOfType(EClass)->map toClass();
				    }
				    mapping EClass::toClass() : EClass {
				        name := self.name;
				        if self.eSuperTypes->notEmpty() then {
				            eSuperTypes += self.eSuperTypes->first().late resolveone(EClass);
				        } endif;
				    }
				}
				""", out);

		assertSuccess(result);
		assertSupertypeCopied(out);
	}

	@Test
	@DisplayName("#297/§8.2.2.11: a null appended to a property is omitted")
	void nullAppendedToAPropertyIsOmitted() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("""
				    main() {
				        src.objectsOfType(EClass)->map toClass();
				    }
				    mapping EClass::toClass() : EClass {
				        name := self.name;
				        if true then {
				            eSuperTypes += self.eSuperTypes->first().resolveone(EClass);
				        } endif;
				    }
				}
				""", out);

		// Derived is mapped before Base, so the eager resolve finds nothing: null, and omitted
		assertSuccess(result);
		for (EObject copy : out.getContents()) {
			assertTrue(((EClass) copy).getESuperTypes().isEmpty());
		}
	}

	@Test
	@DisplayName("#298: ![T] is xselectOne by type")
	void selectOneByType() throws QvtoParseException {
		QvtoExecutionResult result = run("""
				    main() {
				        var root := src.rootObjects()![EPackage];
				        log(root.eClassifiers->size().repr());
				    }
				}
				""", emptyExtent());

		assertSuccess(result);
		assertLogged(result, "2");
	}

	@Test
	@DisplayName("#299: ->any(true) without iterator variable is an iterator")
	void anyWithoutIteratorVariable() throws QvtoParseException {
		QvtoExecutionResult result = run("""
				    main() {
				        var root := src.objectsOfType(EPackage)->any(true);
				        log(root.name);
				    }
				}
				""", emptyExtent());

		assertSuccess(result);
		assertLogged(result, "in");
	}

	@Test
	@DisplayName("#299: the body of an iterator without variable sees the element")
	void implicitIteratorVariableInTheBody() throws QvtoParseException {
		QvtoExecutionResult result = run("""
				    main() {
				        log(src.objectsOfType(EClass)->select(name = 'Base')->size().repr());
				        log(src.objectsOfType(EClass)->collect(name)->sortedBy(n | n)->first());
				        log(src.objectsOfType(EClass)->exists(name = 'Derived').repr());
				    }
				}
				""", emptyExtent());

		assertSuccess(result);
		assertEquals(List.of("1", "Base", "true"), logs(result));
	}

	@Test
	@DisplayName("#300: an EOperation on a transformation property's value")
	void modelOperationOnAPropertyValue() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("""
				    property ecorePackage : EPackage = null;
				    main() {
				        this.ecorePackage := types.objectsOfType(EPackage)->any(x | true);
				        src.objectsOfType(EAttribute)->map toAttribute();
				    }
				    mapping EAttribute::toAttribute() : EAttribute {
				        name := self.name;
				        eType := this.ecorePackage.getEClassifier('EString');
				    }
				}
				""", out);

		assertSuccess(result);
		EAttribute copy = (EAttribute) out.getContents().get(0);
		assertSame(EcorePackage.Literals.ESTRING, copy.getEType());
	}

	@Test
	@DisplayName("#301: objects(), objectsOfType() and objectsOfKind() see the contained objects")
	void extentOperationsSeeContainedObjects() throws QvtoParseException {
		QvtoExecutionResult result = run("""
				    main() {
				        log(src.objects()->size().repr());
				        log(src.objectsOfType(EClass)->size().repr());
				        log(src.objectsOfKind(EClassifier)->size().repr());
				        log(src.rootObjects()->size().repr());
				        log(src.objectsOfType(EClass)->collect(c | c.name)->first());
				    }
				}
				""", emptyExtent());

		// package, two classes, one attribute, and the EGenericTypes EMF contains for the
		// attribute's type and Derived's supertype; the last line is evaluated by OCL (#301)
		assertSuccess(result);
		assertEquals(List.of("6", "2", "2", "1", "Derived"), logs(result));
	}

	@Test
	@DisplayName("#301: an object put into a containment of the same extent is no root")
	void containedObjectIsNoRoot() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("""
				    main() {
				        var p := object EPackage { name := 'p'; };
				        var c := object EClass { name := 'c'; };
				        p.eClassifiers += c;
				        log(tgt.rootObjects()->size().repr());
				        log(tgt.objects()->size().repr());
				    }
				}
				""", out);

		assertSuccess(result);
		assertEquals(List.of("1", "2"), logs(result));
	}

	@Test
	@DisplayName("#296: an imperative source is evaluated once")
	void imperativeSourceIsEvaluatedOnce() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("""
				    main() {
				        log((object EClass { name := 'once'; })->size().repr());
				    }
				}
				""", out);

		assertSuccess(result);
		assertEquals(List.of("1"), logs(result));
		assertEquals(1, out.getContents().size(), () -> "created " + out.getContents());
	}

	@Test
	@DisplayName("#297: a late resolve whose source is an iterator of the right-hand side")
	void lateResolveOnAnIteratorOfTheRightHandSide() throws QvtoParseException {
		QvtoModelExtent out = emptyExtent();
		QvtoExecutionResult result = run("""
				    main() {
				        src.objectsOfType(EClass)->map toClass();
				    }
				    mapping EClass::toClass() : EClass {
				        name := self.name;
				        eSuperTypes := self.eSuperTypes->collect(s | s.late resolveone(EClass));
				    }
				}
				""", out);

		assertSuccess(result);
		assertSupertypeCopied(out);
	}

	@Test
	@DisplayName("#297: what EMF refuses is an error, not an exception out of execute")
	void refusedAssignmentIsAnError() throws QvtoParseException {
		QvtoExecutionResult result = run("""
				    main() {
				        src.objectsOfType(EClass)->map toClass();
				    }
				    mapping EClass::toClass() : EClass {
				        name := self.name;
				        if true then {
				            eSuperTypes += self.name;
				        } endif;
				    }
				}
				""", emptyExtent());

		assertTrue(result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains("Cannot assign to EClass::eSuperTypes")),
				() -> "diagnostics: " + result.diagnostics());
	}

	// --- helpers ---

	private static QvtoExecutionResult run(String body, QvtoModelExtent out) throws QvtoParseException {
		QvtoModelExtent in = new BasicQvtoModelExtent(inputPackage());
		QvtoModelExtent types = new BasicQvtoModelExtent(EcorePackage.eINSTANCE);
		return executeWithExtents(HEADER + body, in, types, out);
	}

	private static EPackage inputPackage() {
		EcoreFactory factory = EcoreFactory.eINSTANCE;
		EPackage pkg = factory.createEPackage();
		pkg.setName("in");
		EClass base = factory.createEClass();
		base.setName("Base");
		EAttribute attribute = factory.createEAttribute();
		attribute.setName("label");
		attribute.setEType(EcorePackage.Literals.ESTRING);
		base.getEStructuralFeatures().add(attribute);
		EClass derived = factory.createEClass();
		derived.setName("Derived");
		derived.getESuperTypes().add(base);
		pkg.getEClassifiers().add(derived);
		pkg.getEClassifiers().add(base);
		return pkg;
	}

	private static void assertSupertypeCopied(QvtoModelExtent out) {
		EClass derived = copyNamed(out, "Derived");
		EClass base = copyNamed(out, "Base");
		assertEquals(List.of(base), derived.getESuperTypes());
		assertTrue(base.getESuperTypes().isEmpty());
	}

	private static EClass copyNamed(QvtoModelExtent out, String name) {
		return out.getContents().stream()
				.map(EClass.class::cast)
				.filter(c -> name.equals(c.getName()))
				.findFirst()
				.orElseThrow(() -> new AssertionError("no copy of " + name + " in " + out.getContents()));
	}

	private static void assertSuccess(QvtoExecutionResult result) {
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		assertTrue(logs(result).contains(expected),
				() -> "expected a log of " + expected + ", got " + result.diagnostics());
	}

	private static List<String> logs(QvtoExecutionResult result) {
		return result.diagnostics().stream()
				.map(d -> d.getMessage())
				.filter(m -> m.startsWith("log: "))
				.map(m -> m.substring("log: ".length()))
				.toList();
	}
}
