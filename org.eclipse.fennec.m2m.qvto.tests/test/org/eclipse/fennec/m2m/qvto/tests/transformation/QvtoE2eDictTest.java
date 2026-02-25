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
 * End-to-end tests for {@code Dict} (§8.3.8, §8.2.2.25-27).
 *
 * <p>{@code Dict(KeyT, T)} is a mutable dictionary with 9 operations:
 * get, hasKey, defaultget, put, clear, size, values, keys, isEmpty.
 *
 * <p>Eclipse reference: {@code stdlibDict/stdlibDict.qvto},
 * {@code dicttype/dicttype.qvto}
 *
 * <p><b>SPEC-FIRST:</b> Tests written against QVT-O v1.3 §8.3.8.
 * Failures are implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2eDictTest extends AbstractQvtoEngineTest {

	// ==== P8-01: Dict (MutableMap) §8.3.8 ====

	// ---- Dict Literal Syntax (§8.2.2.27) ----

	@Test
	void dict_emptyLiteral() throws Exception {
		// §8.2.2.27: Dict{} creates empty dictionary
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{};
				        log(d->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
	}

	@Test
	void dict_literalWithEntries() throws Exception {
		// §8.2.2.27: Dict literal with key=value pairs
		// Eclipse: Dict { 'key1' = 10, 'key2' = 20 }
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1, 'b' = 2};
				        log(d->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
	}

	@Test
	void dict_literalDuplicateKeyLastWins() throws Exception {
		// §8.2.2.27: duplicate key — last value wins (Eclipse behavior)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1, 'a' = 99};
				        log(d->get('a').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "99");
	}

	// ---- §8.3.8.1: get(k:KeyT) : T ----

	@Test
	void dict_getExistingKey() throws Exception {
		// §8.3.8.1: get returns value for existing key
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'x' = 42};
				        log(d->get('x').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
	}

	@Test
	void dict_getMissingKeyReturnsNull() throws Exception {
		// §8.3.8.1: get returns null for missing key
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'x' = 42};
				        log(d->get('y').oclIsUndefined().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	// ---- §8.3.8.2: hasKey(k:KeyT) : Boolean ----

	@Test
	void dict_hasKeyTrue() throws Exception {
		// §8.3.8.2: hasKey returns true when key exists
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1};
				        log(d->hasKey('a').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	@Test
	void dict_hasKeyFalse() throws Exception {
		// §8.3.8.2: hasKey returns false when key does not exist
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1};
				        log(d->hasKey('z').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "false");
	}

	// ---- §8.3.8.3: defaultget(k:KeyT, default:T) : T ----

	@Test
	void dict_defaultgetExistingKey() throws Exception {
		// §8.3.8.3: defaultget returns value when key exists
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 10};
				        log(d->defaultget('a', 999).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "10");
	}

	@Test
	void dict_defaultgetMissingKey() throws Exception {
		// §8.3.8.3: defaultget returns default when key is missing
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 10};
				        log(d->defaultget('z', 999).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "999");
	}

	// ---- §8.3.8.4: put(k:KeyT, v:T) : Void ----

	@Test
	void dict_putAddsEntry() throws Exception {
		// §8.3.8.4: put adds a new key-value pair
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{};
				        d->put('a', 42);
				        log(d->get('a').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "42");
	}

	@Test
	void dict_putOverwritesExisting() throws Exception {
		// §8.3.8.4: put replaces value when key already exists
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1};
				        d->put('a', 99);
				        log(d->get('a').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "99");
	}

	@Test
	void dict_putIsMutable() throws Exception {
		// §8.3.8.4: put modifies the dictionary in-place (mutable semantics)
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{};
				        d->put('x', 1);
				        d->put('y', 2);
				        log(d->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
	}

	// ---- §8.3.8.5: clear() : Void ----

	@Test
	void dict_clearEmptiesDict() throws Exception {
		// §8.3.8.5: clear removes all entries
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1, 'b' = 2};
				        d->clear();
				        log(d->isEmpty().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	@Test
	void dict_clearIsMutable() throws Exception {
		// §8.3.8.5: clear modifies the dictionary in-place
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1, 'b' = 2};
				        d->clear();
				        log(d->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
	}

	// ---- §8.3.8.6: size() : Integer ----

	@Test
	void dict_sizeWithEntries() throws Exception {
		// §8.3.8.6: size returns number of entries
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1, 'b' = 2, 'c' = 3};
				        log(d->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "3");
	}

	@Test
	void dict_sizeEmptyDict() throws Exception {
		// §8.3.8.6: size returns 0 for empty dict
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{};
				        log(d->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "0");
	}

	// ---- §8.3.8.7: values() : List(T) ----

	@Test
	void dict_valuesReturnsList() throws Exception {
		// §8.3.8.7: values returns a List of all values
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 10};
				        var vals : List(Integer) := d->values();
				        log(vals->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "1");
	}

	@Test
	void dict_valuesContainsAllValues() throws Exception {
		// §8.3.8.7: values contains all values from the dict
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 10, 'b' = 20};
				        var vals := d->values();
				        log(vals->includes(10).repr());
				        log(vals->includes(20).repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	// ---- §8.3.8.8: keys() : List(KeyT) ----

	@Test
	void dict_keysReturnsList() throws Exception {
		// §8.3.8.8: keys returns a List of all keys
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 10, 'b' = 20};
				        var k := d->keys();
				        log(k->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
	}

	@Test
	void dict_keysContainsAllKeys() throws Exception {
		// §8.3.8.8: keys contains all keys from the dict
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 10, 'b' = 20};
				        var k := d->keys();
				        log(k->includes('a').repr());
				        log(k->includes('b').repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	// ---- §8.3.8.9: isEmpty() : Boolean ----

	@Test
	void dict_isEmptyTrue() throws Exception {
		// §8.3.8.9: isEmpty returns true for empty dict
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{};
				        log(d->isEmpty().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "true");
	}

	@Test
	void dict_isEmptyFalse() throws Exception {
		// §8.3.8.9: isEmpty returns false for non-empty dict
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1};
				        log(d->isEmpty().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "false");
	}

	// ---- Combined / Integration Tests ----

	@Test
	void dict_putThenGetRoundtrip() throws Exception {
		// Round-trip: put value, get it back
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(String, String) := Dict{};
				        d->put('name', 'Alice');
				        d->put('city', 'Berlin');
				        log(d->get('name'));
				        log(d->get('city'));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "Alice");
		assertLogged(result, "Berlin");
	}

	@Test
	void dict_integerKeyType() throws Exception {
		// §8.2.2.25: key type can be Integer
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d : Dict(Integer, String) := Dict{1 = 'one', 2 = 'two'};
				        log(d->get(1));
				        log(d->get(2));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "one");
		assertLogged(result, "two");
	}

	@Test
	void dict_usedAsHelperParameter() throws Exception {
		// Dict passed as helper parameter
		QvtoExecutionResult result = execute("""
				transformation test() {
				    helper describeDict(d : Dict(String, Integer)) : String {
				        return 'size=' + d->size().repr();
				    }
				    main() {
				        var d : Dict(String, Integer) := Dict{'a' = 1, 'b' = 2};
				        log(describeDict(d));
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "size=2");
	}

	@Test
	void dict_mutationAfterAssignment() throws Exception {
		// Dict is mutable — mutation visible through all references
		QvtoExecutionResult result = execute("""
				transformation test() {
				    main() {
				        var d1 : Dict(String, Integer) := Dict{'a' = 1};
				        var d2 := d1;
				        d2->put('b', 2);
				        log(d1->size().repr());
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "2");
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
