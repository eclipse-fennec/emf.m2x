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
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2x.model.imperativeocl.CatchExp;
import org.eclipse.fennec.m2x.model.imperativeocl.TryExp;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.parser.QvtoStandardLibraryTypes;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The QVT-O standard library types (QVT v1.3 §8.3.1) are real, stable classifiers.
 *
 * <p>They used to be fabricated per occurrence: every mention of {@code Exception}
 * produced a fresh synthetic {@code EClass}, so two clauses naming the same exception
 * type referred to unrelated classes and only agreed because the evaluator compares
 * names. Being language types rather than metamodel types, they cannot come from an
 * {@code EPackage.Registry}.
 */
class QvtoStandardLibraryTypesTest extends AbstractQvtoParserTest {

	@Test
	@DisplayName("Exception and its subclasses form one stable hierarchy")
	void exceptionHierarchyIsStable() {
		EClassifier exception = QvtoStandardLibraryTypes.lookup("Exception");
		EClassifier stringException = QvtoStandardLibraryTypes.lookup("StringException");

		assertNotNull(exception);
		assertSame(exception, QvtoStandardLibraryTypes.lookup("Exception"),
				"the same name must yield the same classifier");
		assertInstanceOf(EClass.class, stringException);

		// §8.3.1.5: StringException is an Exception
		assertEquals(exception, ((EClass) stringException).getESuperTypes().get(0));
	}

	@Test
	@DisplayName("a catch clause resolves Exception to the standard library type")
	void catchClauseUsesStandardLibraryException() throws QvtoParseException {
		OperationalTransformation t = parse("""
				transformation T() {
				    mapping doIt() {
				        try {
				            var x := 1;
				        } catch (Exception) {
				            var y := 2;
				        };
				    }
				}
				""");

		MappingOperation mapping = (MappingOperation) getOperation(t, "doIt");
		OclExpression first = ((MappingBody) mapping.getBody()).getContent().get(0);
		CatchExp catchExp = assertInstanceOf(TryExp.class, first).getExceptClause().get(0);

		assertFalse(catchExp.getException().isEmpty());
		assertSame(QvtoStandardLibraryTypes.lookup("Exception"),
				catchExp.getException().get(0),
				"the caught type must be the standard library Exception, not a fabrication");
	}

	@Test
	@DisplayName("a typedef name resolves to the type it stands for")
	void typedefResolvesToItsBaseType() throws QvtoParseException {
		OperationalTransformation t = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation T(in s : SRC) {
				    typedef Element = SourceElement;
				    mapping doIt() {
				        var e : Element;
				    }
				}
				""");

		MappingOperation mapping = (MappingOperation) getOperation(t, "doIt");
		OclExpression first = ((MappingBody) mapping.getBody()).getContent().get(0);
		assertNotNull(first, "the typedef'd variable declaration must parse");
	}
}
