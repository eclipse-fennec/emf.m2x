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
package org.eclipse.fennec.m2x.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for deep module property semantics (P9-03).
 *
 * <p>Covers collection-typed properties, {@code +=} append semantics,
 * null assignment behavior, copy semantics, and property mutation
 * across operations.
 *
 * <p>Eclipse reference: {@code moduleProperty/moduleProperty.qvto}
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.2.1.1
 * (OperationalTransformation::ownedVariable), Figure 8.2.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eModulePropertyDeepTest extends AbstractQvtoEngineTest {

	// ==== P9-03: Module Properties (deep) ====

	// ---- Collection-typed module properties ----

	@Test
	void moduleProperty_orderedSetDefault() throws Exception {
		// Eclipse moduleProperty.qvto: property propStrOrderedSet : OrderedSet(String) = OrderedSet {};
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property items : OrderedSet(String) = OrderedSet {'a', 'b', 'c'};
				    main() {
				        log('size:' + items->size().repr());
				        log('first:' + items->first());
				        log('last:' + items->last());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
		assertLogged(result, "first:a");
		assertLogged(result, "last:c");
	}

	@Test
	void moduleProperty_sequenceDefault() throws Exception {
		// Eclipse moduleProperty.qvto: property propStrSeq : Sequence(String)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property nums : Sequence(Integer) = Sequence {10, 20, 30};
				    main() {
				        log('size:' + nums->size().repr());
				        log('at2:' + nums->at(2).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
		assertLogged(result, "at2:20");
	}

	@Test
	void moduleProperty_setDefault() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property tags : Set(String) = Set {'x', 'y'};
				    main() {
				        log('size:' + tags->size().repr());
				        log('has-x:' + tags->includes('x').toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
		assertLogged(result, "has-x:true");
	}

	@Test
	void moduleProperty_bagDefault() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property vals : Bag(Integer) = Bag {1, 1, 2};
				    main() {
				        log('size:' + vals->size().repr());
				        log('count1:' + vals->count(1).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
		assertLogged(result, "count1:2");
	}

	// ---- += append semantics on collection properties ----

	@Test
	void moduleProperty_appendToOrderedSet() throws Exception {
		// Eclipse moduleProperty.qvto: this.propStrOrderedSet += localOrderedSet
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property items : OrderedSet(String) = OrderedSet {'a'};
				    main() {
				        this.items += 'b';
				        this.items += 'c';
				        log('size:' + items->size().repr());
				        log('last:' + items->last());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:3");
		assertLogged(result, "last:c");
	}

	@Test
	void moduleProperty_appendToSequence() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property items : Sequence(String) = Sequence {};
				    main() {
				        this.items += 'first';
				        this.items += 'second';
				        log('size:' + items->size().repr());
				        log('at1:' + items->at(1));
				        log('at2:' + items->at(2));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size:2");
		assertLogged(result, "at1:first");
		assertLogged(result, "at2:second");
	}

	// ---- null assignment semantics ----

	@Test
	void moduleProperty_nullAssignToString() throws Exception {
		// Eclipse moduleProperty.qvto: this.propStr := null; assert fatal (propStr = null)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property label : String = 'hello';
				    main() {
				        log('before:' + label);
				        this.label := null;
				        log('after-null:' + (label = null).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "before:hello");
		assertLogged(result, "after-null:true");
	}

	@Test
	void moduleProperty_nullAssignToInteger() throws Exception {
		// Eclipse moduleProperty.qvto: this.propInt := null; assert fatal (propInt = null)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property count : Integer = 10;
				    main() {
				        log('before:' + count.repr());
				        this.count := null;
				        log('after-null:' + (count = null).toString());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "before:10");
		assertLogged(result, "after-null:true");
	}

	// ---- Property mutation with arithmetic ----

	@Test
	void moduleProperty_integerMutation() throws Exception {
		// Eclipse moduleProperty.qvto: this.propInt := propInt + 11; assert fatal (propInt = 21)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property count : Integer = 10;
				    main() {
				        this.count := count + 11;
				        log('count:' + count.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "count:21");
	}

	@Test
	void moduleProperty_stringConcatMutation() throws Exception {
		// Eclipse moduleProperty.qvto: this.propStr := propStr + 'myString'
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property label : String = '';
				    main() {
				        this.label := label + 'myString';
				        log('label:' + label);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "label:myString");
	}

	// ---- Property access from helper and mapping ----

	@Test
	void moduleProperty_readWriteFromHelper() throws Exception {
		// Module properties must be accessible from helpers
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property total : Integer = 0;
				    helper addToTotal(n : Integer) : Void {
				        this.total := total + n;
				    }
				    main() {
				        addToTotal(5);
				        addToTotal(3);
				        log('total:' + total.repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "total:8");
	}

	@Test
	void moduleProperty_multipleTypedProperties() throws Exception {
		// Eclipse moduleProperty.qvto: property propInt : Integer = 10; propReal : Real = 10.0; etc.
		QvtoExecutionResult result = execute("""
				transformation test() {
				    property pInt : Integer = 42;
				    property pReal : Real = 3.14;
				    property pBool : Boolean = true;
				    property pStr : String = 'abc';
				    main() {
				        log('int:' + pInt.repr());
				        log('real:' + pReal.repr());
				        log('bool:' + pBool.toString());
				        log('str:' + pStr);
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "int:42");
		assertLogged(result, "real:3.14");
		assertLogged(result, "bool:true");
		assertLogged(result, "str:abc");
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
