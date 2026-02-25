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
package org.eclipse.fennec.m2x.qvto.parser;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.eclipse.emf.ecore.resource.Resource;

/**
 * ANTLR4 error listener that collects all parse errors as {@link Resource.Diagnostic} instances.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoErrorListener extends BaseErrorListener {

	private final List<Resource.Diagnostic> errors = new ArrayList<>();

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
			int line, int charPositionInLine, String msg, RecognitionException e) {
		errors.add(new QvtoParseDiagnostic(msg, line, charPositionInLine));
	}

	/**
	 * Returns the collected errors.
	 *
	 * @return unmodifiable list of parse errors
	 */
	List<Resource.Diagnostic> getErrors() {
		return Collections.unmodifiableList(errors);
	}

	/**
	 * Returns whether any errors were collected.
	 *
	 * @return {@code true} if errors were collected
	 */
	boolean hasErrors() {
		return !errors.isEmpty();
	}
}
