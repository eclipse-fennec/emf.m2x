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
package org.eclipse.fennec.m2x.m2t.parser;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Implementation of {@link Resource.Diagnostic} for MOFM2T parse errors,
 * carrying line, column, and message information from ANTLR4.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tParseDiagnostic implements Resource.Diagnostic {

	private final String message;
	private final int line;
	private final int column;
	private final String location;

	M2tParseDiagnostic(String message, int line, int column, String location) {
		this.message = message;
		this.line = line;
		this.column = column;
		this.location = location;
	}

	M2tParseDiagnostic(String message, int line, int column) {
		this(message, line, column, null);
	}

	@Override
	public String getMessage() {
		return message;
	}

	@Override
	public String getLocation() {
		return location;
	}

	@Override
	public int getLine() {
		return line;
	}

	@Override
	public int getColumn() {
		return column;
	}

	@Override
	public String toString() {
		return "[" + line + ":" + column + "] " + message;
	}
}
