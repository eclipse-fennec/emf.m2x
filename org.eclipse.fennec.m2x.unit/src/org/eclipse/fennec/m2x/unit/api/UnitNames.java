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
package org.eclipse.fennec.m2x.unit.api;

import java.util.Objects;

/**
 * What has to happen to a unit name before it is put anywhere that reads syntax.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class UnitNames {

	private UnitNames() {
	}

	/**
	 * Escapes a unit name for use as a value in an OSGi filter.
	 *
	 * <p>A qualified name comes out of a transformation, and QVT-O lets an identifier be written
	 * escaped — {@code _'anything at all'} — so it can contain the characters an LDAP filter gives
	 * meaning to. Concatenated into {@code "(unit.name=" + name + ")"}, a name like
	 * {@code foo)(unit.name=*} turns a lookup for one unit into a lookup for every unit, and the
	 * catch for {@code InvalidSyntaxException} does not see it: the result is well-formed, just
	 * not the question that was asked (#183).
	 *
	 * <p>OSGi Core §3.2.7: a value escapes {@code \}, {@code *}, {@code (} and {@code )} with a
	 * backslash.
	 *
	 * @param value the value to put into a filter
	 * @return the escaped value, never {@code null}
	 */
	public static String escapeFilterValue(String value) {
		Objects.requireNonNull(value, "value must not be null");
		StringBuilder escaped = new StringBuilder(value.length() + 8);
		for (int i = 0; i < value.length(); i++) {
			char c = value.charAt(i);
			if (c == '\\' || c == '*' || c == '(' || c == ')') {
				escaped.append('\\');
			}
			escaped.append(c);
		}
		return escaped.toString();
	}
}
