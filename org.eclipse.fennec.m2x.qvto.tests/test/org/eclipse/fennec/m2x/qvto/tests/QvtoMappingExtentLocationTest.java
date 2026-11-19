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
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.qvtoperational.DirectionKind;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * QVT v1.3 §8.2.1.16 / §8.4 — a mapping parameter may name the extent of its object
 * ({@code T@extent}), and with two out parameters of the same model type that is the only way
 * to choose one (#302).
 */
class QvtoMappingExtentLocationTest extends AbstractQvtoEngineTest {

	private static final String HEADER = """
			modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
			transformation T(in src : ECORE, out first : ECORE, out second : ECORE) {
			    main() {
			        src.objectsOfType(EClass)->map toClass();
			    }
			""";

	@Test
	@DisplayName("an unnamed result created in the extent it names")
	void unnamedResultInTheNamedExtent() throws QvtoParseException {
		QvtoModelExtent first = emptyExtent();
		QvtoModelExtent second = emptyExtent();
		QvtoExecutionResult result = run("""
				    mapping EClass::toClass() : EClass@second {
				        name := self.name;
				    }
				}
				""", first, second);

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(List.of(), first.getContents());
		assertEquals(List.of("A"), names(second));
	}

	@Test
	@DisplayName("a named result created in the extent it names")
	void namedResultInTheNamedExtent() throws QvtoParseException {
		QvtoModelExtent first = emptyExtent();
		QvtoModelExtent second = emptyExtent();
		QvtoExecutionResult result = run("""
				    mapping EClass::toClass() : c : EClass@second {
				        c.name := self.name;
				    }
				}
				""", first, second);

		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(List.of(), first.getContents());
		assertEquals(List.of("A"), names(second));
	}

	@Test
	@DisplayName("without an extent the result goes where the model type leads")
	void withoutAnExtentTheInferredOne() throws QvtoParseException {
		QvtoModelExtent first = emptyExtent();
		QvtoModelExtent second = emptyExtent();
		QvtoExecutionResult result = run("""
				    mapping EClass::toClass() : EClass {
				        name := self.name;
				    }
				}
				""", first, second);

		// §8.2.1.16 inference: the first out parameter whose model type contains EClass
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		assertEquals(List.of("A"), names(first));
		assertEquals(List.of(), second.getContents());
	}

	@Test
	@DisplayName("the AST carries the extent on the mapping parameters")
	void theAstCarriesTheExtent() throws QvtoParseException {
		OperationalTransformation transformation = parse(HEADER + """
				    mapping EClass::toClass(in pkg : EPackage@src) : EClass@second {
				        name := self.name;
				    }
				}
				""");

		MappingOperation mapping = findMapping(transformation, "toClass");
		ModelParameter src = modelParameter(transformation, "src");
		ModelParameter second = modelParameter(transformation, "second");

		MappingParameter input = assertInstanceOf(MappingParameter.class, mapping.getEParameters().get(0));
		assertSame(src, input.getExtent());

		MappingParameter output = assertInstanceOf(MappingParameter.class, mapping.getResult().get(0));
		assertSame(second, output.getExtent());
		assertEquals(DirectionKind.OUT, output.getKind());
	}

	@Test
	@DisplayName("an extent that is no model parameter is a parse error")
	void unknownExtentIsAParseError() {
		QvtoParseException failure = assertThrows(QvtoParseException.class, () -> parse(HEADER + """
				    mapping EClass::toClass() : EClass@third {
				        name := self.name;
				    }
				}
				"""));

		assertTrue(failure.getMessage().contains("Unknown extent 'third'"), failure::getMessage);
	}

	// --- helpers ---

	private static QvtoExecutionResult run(String body, QvtoModelExtent first, QvtoModelExtent second)
			throws QvtoParseException {
		EClass a = EcoreFactory.eINSTANCE.createEClass();
		a.setName("A");
		return executeWithExtents(HEADER + body, new BasicQvtoModelExtent(a), first, second);
	}

	private static List<String> names(QvtoModelExtent extent) {
		return extent.getContents().stream().map(o -> ((EClass) o).getName()).toList();
	}

	private static MappingOperation findMapping(OperationalTransformation transformation, String name) {
		for (var iter = transformation.eAllContents(); iter.hasNext(); ) {
			EObject next = iter.next();
			if (next instanceof MappingOperation mapping && name.equals(mapping.getName())) {
				return mapping;
			}
		}
		throw new AssertionError("no mapping " + name);
	}

	private static ModelParameter modelParameter(OperationalTransformation transformation, String name) {
		return transformation.getModelParameter().stream()
				.filter(mp -> name.equals(mp.getName()))
				.findFirst()
				.orElseThrow();
	}
}
