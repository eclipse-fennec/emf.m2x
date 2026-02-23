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
package org.eclipse.fennec.m2m.qvto.tests.controlflow;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * E2E tests for QVT-O VariableInitExp (§8.2.2.10) and AssignExp (§8.2.2.11).
 * <p>
 * Phase 6, Task P6-01.
 * <p>
 * Spec references:
 * <ul>
 *   <li>§8.2.2.10 VariableInitExp — var declaration, type inference, withResult, defaults</li>
 *   <li>§8.2.2.11 AssignExp — := (reset), += (append), default keyword, composite assignment</li>
 * </ul>
 */
class QvtoE2eVarAssignTest extends AbstractQvtoEngineTest {

	// ---- VariableInitExp (§8.2.2.10) ----

	@Test
	void varInit_typeInferenceFromValue() throws Exception {
		// §8.2.2.10: Type can be omitted when derived from initialization value
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := 42;
				        log(x.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
	}

	@Test
	void varInit_defaultValues() throws Exception {
		// §8.2.2.10: Default values — zero for numeric, empty string, empty collection, null otherwise
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var i : Integer;
				        var s : String;
				        log('i=' + i.toString());
				        log('s=' + s);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "i=0");
		assertLogged(result, "s=");
	}

	@Test
	void varInit_withResult() throws Exception {
		// §8.2.2.10: '::=' notation — withResult=true, the expression returns the init value
		// Eclipse ref: varInitExpWithResult_260985.qvto — "name := var c ::= 'abc'"
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := var y ::= 'hello';
				        log('x=' + x);
				        log('y=' + y);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "x=hello");
		assertLogged(result, "y=hello");
	}

	@Test
	void varInit_withoutResult() throws Exception {
		// §8.2.2.10: ':=' notation — withResult=false, the expression returns null
		// So 'var x := var y := ...' should assign null to x, not the init value of y
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : String := '';
				        var y : String := '';
				        x := var y := 'inner';
				        log('x=' + x);
				        log('y=' + y);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "x=");
		assertLogged(result, "y=inner");
	}

	@Test
	void varInit_multipleDeclarations() throws Exception {
		// §8.2.2.10: Multiple variable declarations grouped with unique var keyword
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := 'abc', i := 7;
				        log(x + '-' + i.toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "abc-7");
	}

	@Test
	void varInit_equalsAlternative() throws Exception {
		// §8.2.2.10: "=" symbol can be used instead of ":=" to initialize a variable
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x = 'hello';
				        log(x);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "hello");
	}

	// ---- AssignExp (§8.2.2.11) ----

	@Test
	void assign_singleValuedReset() throws Exception {
		// §8.2.2.11: ':=' is reset — replaces the existing value
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := 'old';
				        x := 'new';
				        log(x);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "new");
	}

	@Test
	void assign_multiValuedAppend() throws Exception {
		// §8.2.2.11: '+=' appends to a multi-valued variable (isReset=false)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var list : Sequence(Integer) := Sequence{1, 2};
				        list += 3;
				        log(list->size().toString());
				        log(list->at(3).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
	}

	@Test
	void assign_multiValuedReset() throws Exception {
		// §8.2.2.11: ':=' on multi-valued variable resets the list
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var list : Sequence(Integer) := Sequence{1, 2, 3};
				        list := Sequence{10, 20};
				        log(list->size().toString());
				        log(list->at(1).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
		assertLogged(result, "10");
	}

	@Test
	void assign_propertyOnEObject() throws Exception {
		// §8.2.2.11: Assignment to a property on an EObject (feature assign)
		// §8.2.2.7: rootObjects()[SourceElement] is xselect shorthand
		EObject src = createSourceElement("orig", 1);
		QvtoModelExtent inExtent = new BasicQvtoModelExtent(src);
		QvtoModelExtent outExtent = emptyExtent();

		QvtoExecutionResult result = executeWithExtents("""
				modeltype SOURCE uses source('http://test/source');
				modeltype TARGET uses target('http://test/target');
				transformation test(in s : SOURCE, out t : TARGET) {
				    main() {
				        s.rootObjects()[SourceElement]->forEach(e) {
				            var te := object TargetElement {
				                label := 'mapped-' + e.name;
				            };
				            te.label := 'reassigned';
				            log(te.label);
				        };
				    }
				}
				""", inExtent, outExtent);
		assertSuccess(result);
		assertLogged(result, "reassigned");
	}

	@Test
	void assign_defaultKeyword() throws Exception {
		// §8.2.2.11: 'default' keyword — replaces null RHS with the default expression
		// Spec: "left := if value <> null then value else defaultValue endif"
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : String;
				        x := null default 'fallback';
				        log(x);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "fallback");
	}

	@Test
	void assign_defaultKeyword_notUsedWhenNonNull() throws Exception {
		// §8.2.2.11: 'default' keyword — not used when RHS is non-null
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x : String;
				        x := 'actual' default 'fallback';
				        log(x);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "actual");
	}

	@Test
	void assign_reassignMultipleTimes() throws Exception {
		// §8.2.2.11: Multiple assignments to same variable — each := replaces
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var x := 'a';
				        x := 'b';
				        x := 'c';
				        log(x);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "c");
	}

	@Test
	void assign_chainedWithResult() throws Exception {
		// §8.2.2.10+11: Chained assignment using ::= (withResult=true)
		// var a := var b ::= 'value' — both a and b get 'value'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var a := var b ::= 'shared';
				        log('a=' + a);
				        log('b=' + b);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "a=shared");
		assertLogged(result, "b=shared");
	}

	// ---- Helpers ----

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
