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
package org.eclipse.fennec.m2x.qvto.tests.intermediate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * Tests for GAP-14: {@code metamodel}/{@code package} blocks and {@code enum} declarations.
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.4.6 (p.163)
 * concrete syntax for metamodel notation.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eMetamodelEnumTest extends AbstractQvtoEngineTest {

	// ==== enum (standalone) ====

	@Test
	void enum_stringLiterals() throws Exception {
		String source = """
				transformation test() {
				    enum Direction { "North", "South", "East", "West" };
				    main() {
				        log('ok');
				    }
				}
				""";
		QvtoExecutionResult result = execute(source);
		assertSuccess(result);
		assertLogged(result, "ok");
		// Verify enum was created in intermediate package
		OperationalTransformation t = parse(source);
		EPackage intermPkg = findIntermediatePackage(t);
		assertNotNull(intermPkg, "intermediate package");
		assertTrue(intermPkg.getEClassifier("Direction") instanceof EEnum,
				"Direction should be EEnum");
		EEnum dirEnum = (EEnum) intermPkg.getEClassifier("Direction");
		assertEquals(4, dirEnum.getELiterals().size());
		assertEquals("North", dirEnum.getELiterals().get(0).getName());
		assertEquals("West", dirEnum.getELiterals().get(3).getName());
	}

	@Test
	void enum_identifierLiterals() throws Exception {
		String source = """
				transformation test() {
				    enum Color { Red, Green, Blue };
				    main() {
				        log('ok');
				    }
				}
				""";
		QvtoExecutionResult result = execute(source);
		assertSuccess(result);
		OperationalTransformation t = parse(source);
		EPackage intermPkg = findIntermediatePackage(t);
		assertNotNull(intermPkg, "intermediate package");
		EEnum colorEnum = (EEnum) intermPkg.getEClassifier("Color");
		assertNotNull(colorEnum, "Color enum");
		assertEquals(3, colorEnum.getELiterals().size());
		assertEquals("Red", colorEnum.getELiterals().get(0).getName());
		assertEquals(0, colorEnum.getELiterals().get(0).getValue());
		assertEquals(2, colorEnum.getELiterals().get(2).getValue());
	}

	@Test
	void enum_empty() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    enum Empty {};
				    main() {
				        log('ok');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "ok");
	}

	// ==== metamodel block ====

	@Test
	void metamodel_withClass() throws Exception {
		String source = """
				transformation test() {
				    metamodel SimpleUML {
				        intermediate class ModelElement {
				            name : String;
				        };
				        intermediate class Class extends ModelElement {
				            isAbstract : Boolean;
				        };
				    };
				    main() {
				        log('ok');
				    }
				}
				""";
		QvtoExecutionResult result = execute(source);
		assertSuccess(result);
		assertLogged(result, "ok");
		// Verify sub-package was created
		OperationalTransformation t = parse(source);
		EPackage intermPkg = findIntermediatePackage(t);
		assertNotNull(intermPkg, "intermediate package");
		EPackage simpleUml = intermPkg.getESubpackages().stream()
				.filter(p -> "SimpleUML".equals(p.getName()))
				.findFirst().orElse(null);
		assertNotNull(simpleUml, "SimpleUML sub-package");
		assertNotNull(simpleUml.getEClassifier("ModelElement"));
		assertNotNull(simpleUml.getEClassifier("Class"));
	}

	@Test
	void package_keyword() throws Exception {
		// 'package' is equivalent to 'metamodel'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    package MyPkg {
				        intermediate class Item {
				            label : String;
				        };
				    };
				    main() {
				        log('ok');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "ok");
	}

	@Test
	void metamodel_withEnum() throws Exception {
		String source = """
				transformation test() {
				    metamodel Schema {
				        enum ParameterDirectionKind { "in", "inout", "out" };
				        intermediate class Parameter {
				            name : String;
				        };
				    };
				    main() {
				        log('ok');
				    }
				}
				""";
		QvtoExecutionResult result = execute(source);
		assertSuccess(result);
		OperationalTransformation t = parse(source);
		EPackage intermPkg = findIntermediatePackage(t);
		assertNotNull(intermPkg);
		EPackage schemaPkg = intermPkg.getESubpackages().stream()
				.filter(p -> "Schema".equals(p.getName()))
				.findFirst().orElse(null);
		assertNotNull(schemaPkg, "Schema sub-package");
		EEnum dirEnum = (EEnum) schemaPkg.getEClassifier("ParameterDirectionKind");
		assertNotNull(dirEnum, "ParameterDirectionKind enum");
		assertEquals(3, dirEnum.getELiterals().size());
	}

	@Test
	void metamodel_withExceptionAndPrimitive() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    metamodel Types {
				        primitive Double;
				        exception ValidationError {};
				    };
				    main() {
				        log('ok');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "ok");
	}

	@Test
	void metamodel_withDatatype() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    metamodel Domain {
				        datatype Address {
				            street : String;
				            city : String;
				        };
				    };
				    main() {
				        log('ok');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "ok");
	}

	@Test
	void metamodel_specExample() throws Exception {
		// Full example from spec §8.4.6, p.163
		String source = """
				transformation test() {
				    metamodel SimpleUML {
				        primitive Double;
				        enum ParameterDirectionKind { "in", "inout", "out" };
				        intermediate class ModelElement {
				            name : String;
				        };
				        intermediate class Class extends ModelElement {
				            <<id>> uuid : String [1];
				        };
				        exception VeryStrangeException {};
				    };
				    main() {
				        log('ok');
				    }
				}
				""";
		QvtoExecutionResult result = execute(source);
		assertSuccess(result);
		assertLogged(result, "ok");
		// Verify full metamodel structure
		OperationalTransformation t = parse(source);
		EPackage intermPkg = findIntermediatePackage(t);
		assertNotNull(intermPkg, "intermediate package");
		EPackage simpleUml = intermPkg.getESubpackages().stream()
				.filter(p -> "SimpleUML".equals(p.getName()))
				.findFirst().orElse(null);
		assertNotNull(simpleUml, "SimpleUML package");
		// 5 classifiers: Double, ParameterDirectionKind, ModelElement, Class, VeryStrangeException
		assertEquals(5, simpleUml.getEClassifiers().size());
	}

	// ---- Helpers ----

	private static EPackage findIntermediatePackage(OperationalTransformation t) {
		return t.getESubpackages().stream()
				.filter(p -> "_INTERMEDIATE".equals(p.getName()))
				.findFirst().orElse(null);
	}

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected));
		assertTrue(found, "Expected log containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
	}
}
