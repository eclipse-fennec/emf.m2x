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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationException;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * MOFM2T v1.0 §8.3 Standard Library — additional String operations.
 *
 * <p>Provides the 13 String operations defined by the MOFM2T specification. Every occurrence
 * is replaced by {@code substituteAll}, which the OCL standard library already provides
 * (Eclipse extension) and which it answers before this library is asked:
 * <ul>
 *   <li>{@code substitute(r, t)} — replace the first occurrence of r with t</li>
 *   <li>{@code index(r)} — 1-based index of substring r, or -1</li>
 *   <li>{@code first(n)} — first n characters</li>
 *   <li>{@code last(n)} — last n characters</li>
 *   <li>{@code strstr(r)} — true if self contains r</li>
 *   <li>{@code toUpper()} — uppercase</li>
 *   <li>{@code toLower()} — lowercase</li>
 *   <li>{@code strtok(s1, flag)} — stateful tokenizer</li>
 *   <li>{@code strcmp(s1)} — lexicographic comparison</li>
 *   <li>{@code isAlpha()} — only alphabetical characters</li>
 *   <li>{@code isAlphanum()} — only alphanumeric characters</li>
 *   <li>{@code toUpperFirst()} — first char uppercase</li>
 *   <li>{@code toLowerFirst()} — first char lowercase</li>
 * </ul>
 *
 * <p>A {@code null} or {@code invalid} source or argument, an empty search string and a
 * negative length make the call {@code invalid} with an ERROR, as a {@code null} argument does
 * for every predefined operation (#306, #373). Case mapping does not depend on the default
 * locale: a generator on a Turkish system writes {@code I} for {@code 'i'.toUpper()} as well.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tStandardLibrary implements OclOperationProvider {

	// The predefined types, from the one standard library rather than a private copy (#154)
	private static final PrimitiveType STRING_TYPE = OclStandardLibrary.INSTANCE.string();
	private static final PrimitiveType INTEGER_TYPE = OclStandardLibrary.INSTANCE.integer();
	private static final PrimitiveType BOOLEAN_TYPE = OclStandardLibrary.INSTANCE.booleanType();

	/**
	 * Stateful tokenizer cache for {@code strtok(delimiters, flag)}, keyed by source string.
	 *
	 * <p>Keying by the string is what the operation specifies: {@code flag = 0} starts a
	 * tokenization of that string and {@code flag = 1} continues it, with nothing else
	 * identifying which tokenization is meant. Two templates tokenizing the same string
	 * value therefore share one position — inherent to the operation, not to this cache.
	 * What is not inherent is how long that state lives: this library is built per
	 * evaluation ({@code M2tEvaluator}), so the state ends with the generation. The
	 * Acceleo reference keeps the same map {@code static}, shared by every generation in
	 * the JVM.
	 */
	private final Map<String, StringTokenizer> tokenizerCache =
			Collections.synchronizedMap(new WeakHashMap<>());

	@Override
	public List<OclOperation> getOperations() {
		List<OclOperation> ops = new ArrayList<>();

		// substitute(r, t) : String — §8.3.1 "Substitutes substring r in self by substring t":
		// the first occurrence, as Acceleo 3.7 reads the singular (#373)
		ops.add(new OclOperation("substitute", STRING_TYPE,
				List.of(STRING_TYPE, STRING_TYPE), STRING_TYPE,
				(self, args) -> {
					String s = source(self, "substitute");
					String r = searchString(args[0], "substitute");
					String t = argument(args[1], "substitute");
					int at = s.indexOf(r);
					return at < 0 ? s : s.substring(0, at) + t + s.substring(at + r.length());
				}));

		// index(r) : Integer — 1-based, -1 if not found
		ops.add(new OclOperation("index", STRING_TYPE,
				List.of(STRING_TYPE), INTEGER_TYPE,
				(self, args) -> {
					int idx = source(self, "index").indexOf(argument(args[0], "index"));
					return idx < 0 ? (long) -1 : (long) (idx + 1);
				}));

		// first(n) : String
		ops.add(new OclOperation("first", STRING_TYPE,
				List.of(INTEGER_TYPE), STRING_TYPE,
				(self, args) -> {
					String s = source(self, "first");
					int n = length(args[0], "first");
					return n >= s.length() ? s : s.substring(0, n);
				}));

		// last(n) : String
		ops.add(new OclOperation("last", STRING_TYPE,
				List.of(INTEGER_TYPE), STRING_TYPE,
				(self, args) -> {
					String s = source(self, "last");
					int n = length(args[0], "last");
					return n >= s.length() ? s : s.substring(s.length() - n);
				}));

		// strstr(r) : Boolean
		ops.add(new OclOperation("strstr", STRING_TYPE,
				List.of(STRING_TYPE), BOOLEAN_TYPE,
				(self, args) -> source(self, "strstr").contains(argument(args[0], "strstr"))));

		// toUpper() : String
		ops.add(OclOperation.of("toUpper", STRING_TYPE, STRING_TYPE,
				(self, args) -> source(self, "toUpper").toUpperCase(Locale.ROOT)));

		// toLower() : String
		ops.add(OclOperation.of("toLower", STRING_TYPE, STRING_TYPE,
				(self, args) -> source(self, "toLower").toLowerCase(Locale.ROOT)));

		// strtok(s1, flag) : String — "The parameter flag should be 0 when strtok is called for
		// the first time, 1 subsequently" (§8.3.1); a continuation without a start starts, as in
		// Acceleo, any other flag is invalid (#373)
		ops.add(new OclOperation("strtok", STRING_TYPE,
				List.of(STRING_TYPE, INTEGER_TYPE), STRING_TYPE,
				(self, args) -> {
					String s = source(self, "strtok");
					String delims = argument(args[0], "strtok");
					if (!(args[1] instanceof Number number)) {
						throw new OclOperationException("Null argument for operation 'strtok'");
					}
					long flag = number.longValue();
					if (flag != 0 && flag != 1) {
						throw new OclOperationException("strtok takes the flag 0 to start and 1 to continue, not " + flag);
					}
					StringTokenizer tokenizer = flag == 0 ? null : tokenizerCache.get(s);
					if (tokenizer == null) {
						tokenizer = new StringTokenizer(s, delims);
						tokenizerCache.put(s, tokenizer);
					}
					return tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "";
				}));

		// strcmp(s1) : Integer — the sign of C's strcmp(self, s1), as Acceleo 3.7; the spec's
		// sign sentence reads the other way round and is taken as an erratum (#373)
		ops.add(new OclOperation("strcmp", STRING_TYPE,
				List.of(STRING_TYPE), INTEGER_TYPE,
				(self, args) -> (long) source(self, "strcmp").compareTo(argument(args[0], "strcmp"))));

		// isAlpha() : Boolean — "consists only of alphabetical characters": true for ''
		ops.add(OclOperation.of("isAlpha", STRING_TYPE, BOOLEAN_TYPE,
				(self, args) -> source(self, "isAlpha").codePoints().allMatch(Character::isLetter)));

		// isAlphanum() : Boolean
		ops.add(OclOperation.of("isAlphanum", STRING_TYPE, BOOLEAN_TYPE,
				(self, args) -> source(self, "isAlphanum").codePoints().allMatch(Character::isLetterOrDigit)));

		// toUpperFirst() : String
		ops.add(OclOperation.of("toUpperFirst", STRING_TYPE, STRING_TYPE,
				(self, args) -> {
					String s = source(self, "toUpperFirst");
					if (s.isEmpty()) return s;
					return Character.toUpperCase(s.charAt(0)) + s.substring(1);
				}));

		// toLowerFirst() : String
		ops.add(OclOperation.of("toLowerFirst", STRING_TYPE, STRING_TYPE,
				(self, args) -> {
					String s = source(self, "toLowerFirst");
					if (s.isEmpty()) return s;
					return Character.toLowerCase(s.charAt(0)) + s.substring(1);
				}));

		return List.copyOf(ops);
	}

	/** The String an operation is called on; {@code null} or {@code invalid} rejects the call. */
	private static String source(Object self, String operation) {
		if (self == null || self == OclInvalid.INSTANCE) {
			throw new OclOperationException("Null source for operation '" + operation + "'");
		}
		return self.toString();
	}

	/** A String argument; {@code null} or {@code invalid} rejects the call. */
	private static String argument(Object value, String operation) {
		if (value == null || value == OclInvalid.INSTANCE) {
			throw new OclOperationException("Null argument for operation '" + operation + "'");
		}
		return value.toString();
	}

	/** The search string of a substitution: an empty one has no occurrence to replace. */
	private static String searchString(Object value, String operation) {
		String search = argument(value, operation);
		if (search.isEmpty()) {
			throw new OclOperationException("Empty search string for operation '" + operation + "'");
		}
		return search;
	}

	/** A length: an Integer of zero or more. */
	private static int length(Object value, String operation) {
		if (!(value instanceof Number number)) {
			throw new OclOperationException("Null argument for operation '" + operation + "'");
		}
		long n = number.longValue();
		if (n < 0) {
			throw new OclOperationException("Negative length " + n + " for operation '" + operation + "'");
		}
		return (int) Math.min(n, Integer.MAX_VALUE);
	}
}
