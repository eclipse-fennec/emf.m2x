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
package org.eclipse.fennec.m2m.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2m.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2m.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for advanced Tuple semantics (P9-04).
 *
 * <p>Based on Eclipse reference tests:
 * <ul>
 *   <li>{@code tuples/tuples.qvto} — Collection of Tuples, Tuple as parameter</li>
 *   <li>{@code bug413131/bug413131.qvto} — Nested Tuples, null in fields</li>
 *   <li>{@code bug415024/bug415024.qvto} — Mutable List in Tuple</li>
 * </ul>
 *
 * <p><b>SPEC-FIRST:</b> Tests written against OCL v2.4 §7.4.3 TupleType
 * (parts have unique names and types) and QVT-O v1.3 extensions.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eTupleAdvancedTest extends AbstractQvtoEngineTest {

	// ==== P9-04: Tuple Advanced ====

	// ---- Nested tuples ----

	@Test
	void tuple_nestedTuple() throws Exception {
		// Eclipse bug413131: Tuple(obj : Tuple(name : String, size : Integer), size : Integer)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var t := Tuple {
				            obj = Tuple { name = 'inner', size = 10 },
				            size = 5
				        };
				        log('inner-name:' + t.obj.name);
				        log('inner-size:' + t.obj.size.repr());
				        log('outer-size:' + t.size.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "inner-name:inner");
		assertLogged(result, "inner-size:10");
		assertLogged(result, "outer-size:5");
	}

	// ---- null in tuple fields ----

	@Test
	void tuple_nullInStringField() throws Exception {
		// Eclipse bug413131: Tuple { name = null, size = 10 }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var t := Tuple { name = null, size = 10 };
				        log('name-null:' + (t.name = null).toString());
				        log('size:' + t.size.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name-null:true");
		assertLogged(result, "size:10");
	}

	@Test
	void tuple_nullInIntegerField() throws Exception {
		// Eclipse bug413131: Tuple { name = 'hello', size = null }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var t := Tuple { name = 'hello', size = null };
				        log('name:' + t.name);
				        log('size-null:' + (t.size = null).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:hello");
		assertLogged(result, "size-null:true");
	}

	@Test
	void tuple_nestedNullInnerTuple() throws Exception {
		// Eclipse bug413131: t4 := Tuple {obj = null, size = 5}
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var t : Tuple(obj : Tuple(name : String, size : Integer), size : Integer)
				            := Tuple { obj = null, size = 5 };
				        log('obj-null:' + (t.obj = null).toString());
				        log('size:' + t.size.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "obj-null:true");
		assertLogged(result, "size:5");
	}

	// ---- Tuple as module property ----

	@Test
	void tuple_asModuleProperty() throws Exception {
		// Eclipse bug413131: property t6 = Tuple {obj = Tuple{...}, size = 10};
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property config : Tuple(name : String, version : Integer)
				        = Tuple { name = 'app', version = 1 };
				    main() {
				        log('name:' + config.name);
				        log('version:' + config.version.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "name:app");
		assertLogged(result, "version:1");
	}

	// ---- Tuple equality ----

	@Test
	void tuple_equalityByParts() throws Exception {
		// OCL §7.4.3: Two tuples are equal iff all their parts are equal
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var t1 := Tuple { a = 1, b = 'x' };
				        var t2 := Tuple { a = 1, b = 'x' };
				        var t3 := Tuple { a = 2, b = 'x' };
				        log('eq:' + (t1 = t2).toString());
				        log('neq:' + (t1 = t3).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "eq:true");
		assertLogged(result, "neq:false");
	}

	// ---- Tuple in collection ----

	@Test
	void tuple_inSequenceCollection() throws Exception {
		// Eclipse tuples.qvto: Set(Tuple(name:String))
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var items : Sequence(Tuple(label : String, value : Integer))
				            := Sequence {
				                Tuple { label = 'a', value = 1 },
				                Tuple { label = 'b', value = 2 },
				                Tuple { label = 'c', value = 3 }
				            };
				        log('size:' + items->size().repr());
				        log('first:' + items->at(1).label);
				        log('last-val:' + items->at(3).value.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
		assertLogged(result, "first:a");
		assertLogged(result, "last-val:3");
	}

	@Test
	void tuple_collectOverTuples() throws Exception {
		// Eclipse tuples.qvto: tuplesVar->collect(t| tupleToClass(t))
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var pairs := Sequence {
				            Tuple { name = 'alpha', val = 10 },
				            Tuple { name = 'beta', val = 20 }
				        };
				        var names := pairs->collect(p | p.name);
				        log('names:' + names->at(1) + ',' + names->at(2));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "names:alpha,beta");
	}

	// ---- Tuple variable reassignment ----

	@Test
	void tuple_variableReassignment() throws Exception {
		// Eclipse bug413131: t4 := Tuple{...}; t4 := Tuple{...};
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var t := Tuple { x = 1, y = 2 };
				        log('before:' + t.x.repr() + ',' + t.y.repr());
				        t := Tuple { x = 10, y = 20 };
				        log('after:' + t.x.repr() + ',' + t.y.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "before:1,2");
		assertLogged(result, "after:10,20");
	}

	// ---- Tuple passed to helper ----

	@Test
	void tuple_passedToHelper() throws Exception {
		// Eclipse tuples.qvto: mapping tupleToClass(in tup:Tuple(name:String))
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper describe(t : Tuple(name : String, age : Integer)) : String {
				        return t.name + ' is ' + t.age.repr();
				    }
				    main() {
				        var person := Tuple { name = 'Alice', age = 30 };
				        log(describe(person));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Alice is 30");
	}

	// ---- Mutable List field in Tuple ----

	@Test
	void tuple_mutableListField() throws Exception {
		// Eclipse bug415024: var t := Tuple {a = Set{1}, b = List{1}}; t.b += 3;
		// List parts are mutable, Set parts are not
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var t := Tuple { items = List {1, 2} };
				        t.items += 3;
				        log('size:' + t.items->size().repr());
				        log('last:' + t.items->at(3).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
		assertLogged(result, "last:3");
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
