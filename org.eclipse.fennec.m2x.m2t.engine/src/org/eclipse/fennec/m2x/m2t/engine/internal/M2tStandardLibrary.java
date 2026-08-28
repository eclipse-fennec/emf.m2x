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
import java.util.Map;
import java.util.Objects;
import java.util.StringTokenizer;
import java.util.WeakHashMap;

import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * MOFM2T v1.0 §8.3 Standard Library — additional String operations.
 *
 * <p>Provides 13 String operations defined by the MOFM2T specification:
 * <ul>
 *   <li>{@code substitute(r, t)} — replace occurrences of r with t</li>
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
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tStandardLibrary implements OclOperationProvider {

	// The predefined types, from the one standard library rather than a private copy (#154)
	private static final PrimitiveType STRING_TYPE = OclStandardLibrary.INSTANCE.string();
	private static final PrimitiveType INTEGER_TYPE = OclStandardLibrary.INSTANCE.integer();
	private static final PrimitiveType BOOLEAN_TYPE = OclStandardLibrary.INSTANCE.booleanType();

	/**
	 * Stateful tokenizer cache for strtok(). Keyed by source string.
	 * Uses WeakHashMap so entries are cleaned up when the string is GC'd.
	 */
	private final Map<String, StringTokenizer> tokenizerCache =
			Collections.synchronizedMap(new WeakHashMap<>());

	@Override
	public List<OclOperation> getOperations() {
		List<OclOperation> ops = new ArrayList<>();

		// substitute(r, t) : String
		ops.add(new OclOperation("substitute", STRING_TYPE,
				List.of(STRING_TYPE, STRING_TYPE), STRING_TYPE,
				(self, args) -> {
					String s = Objects.toString(self, "");
					String r = Objects.toString(args[0], "");
					String t = Objects.toString(args[1], "");
					return s.replace(r, t);
				}));

		// index(r) : Integer — 1-based, -1 if not found
		ops.add(new OclOperation("index", STRING_TYPE,
				List.of(STRING_TYPE), INTEGER_TYPE,
				(self, args) -> {
					String s = Objects.toString(self, "");
					String r = Objects.toString(args[0], "");
					int idx = s.indexOf(r);
					return idx < 0 ? (long) -1 : (long) (idx + 1);
				}));

		// first(n) : String
		ops.add(new OclOperation("first", STRING_TYPE,
				List.of(INTEGER_TYPE), STRING_TYPE,
				(self, args) -> {
					String s = Objects.toString(self, "");
					int n = Math.max(0, ((Number) args[0]).intValue());
					return n >= s.length() ? s : s.substring(0, n);
				}));

		// last(n) : String
		ops.add(new OclOperation("last", STRING_TYPE,
				List.of(INTEGER_TYPE), STRING_TYPE,
				(self, args) -> {
					String s = Objects.toString(self, "");
					int n = Math.max(0, ((Number) args[0]).intValue());
					return n >= s.length() ? s : s.substring(s.length() - n);
				}));

		// strstr(r) : Boolean
		ops.add(new OclOperation("strstr", STRING_TYPE,
				List.of(STRING_TYPE), BOOLEAN_TYPE,
				(self, args) -> Objects.toString(self, "").contains(Objects.toString(args[0], ""))));

		// toUpper() : String
		ops.add(OclOperation.of("toUpper", STRING_TYPE, STRING_TYPE,
				(self, args) -> Objects.toString(self, "").toUpperCase()));

		// toLower() : String
		ops.add(OclOperation.of("toLower", STRING_TYPE, STRING_TYPE,
				(self, args) -> Objects.toString(self, "").toLowerCase()));

		// strtok(s1, flag) : String — flag=0 first call, flag=1 subsequent
		ops.add(new OclOperation("strtok", STRING_TYPE,
				List.of(STRING_TYPE, INTEGER_TYPE), STRING_TYPE,
				(self, args) -> {
					String s = Objects.toString(self, "");
					String delims = Objects.toString(args[0], "");
					int flag = ((Number) args[1]).intValue();
					StringTokenizer tokenizer;
					if (flag == 0) {
						tokenizer = new StringTokenizer(s, delims);
						tokenizerCache.put(s, tokenizer);
					} else {
						tokenizer = tokenizerCache.get(s);
						if (tokenizer == null) {
							return "";
						}
					}
					return tokenizer.hasMoreTokens() ? tokenizer.nextToken() : "";
				}));

		// strcmp(s1) : Integer
		ops.add(new OclOperation("strcmp", STRING_TYPE,
				List.of(STRING_TYPE), INTEGER_TYPE,
				(self, args) -> (long) Objects.toString(self, "").compareTo(Objects.toString(args[0], ""))));

		// isAlpha() : Boolean
		ops.add(OclOperation.of("isAlpha", STRING_TYPE, BOOLEAN_TYPE,
				(self, args) -> {
					String s = Objects.toString(self, "");
					return !s.isEmpty() && s.chars().allMatch(Character::isLetter);
				}));

		// isAlphanum() : Boolean
		ops.add(OclOperation.of("isAlphanum", STRING_TYPE, BOOLEAN_TYPE,
				(self, args) -> {
					String s = Objects.toString(self, "");
					return !s.isEmpty() && s.chars().allMatch(Character::isLetterOrDigit);
				}));

		// toUpperFirst() : String
		ops.add(OclOperation.of("toUpperFirst", STRING_TYPE, STRING_TYPE,
				(self, args) -> {
					String s = Objects.toString(self, "");
					if (s.isEmpty()) return s;
					return Character.toUpperCase(s.charAt(0)) + s.substring(1);
				}));

		// toLowerFirst() : String
		ops.add(OclOperation.of("toLowerFirst", STRING_TYPE, STRING_TYPE,
				(self, args) -> {
					String s = Objects.toString(self, "");
					if (s.isEmpty()) return s;
					return Character.toLowerCase(s.charAt(0)) + s.substring(1);
				}));

		return List.copyOf(ops);
	}
}
