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
package org.eclipse.fennec.m2m.ocl.parser;

import org.eclipse.emf.ecore.resource.Resource;

/**
 * Implementation of {@link Resource.Diagnostic} for OCL parse errors,
 * carrying line, column, and message information from ANTLR4.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclParseDiagnostic implements Resource.Diagnostic {

	private final String message;
	private final int line;
	private final int column;
	private final String location;

	OclParseDiagnostic(String message, int line, int column, String location) {
		this.message = message;
		this.line = line;
		this.column = column;
		this.location = location;
	}

	OclParseDiagnostic(String message, int line, int column) {
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
