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

import java.util.List;

import org.antlr.v4.runtime.BaseErrorListener;
import org.antlr.v4.runtime.RecognitionException;
import org.antlr.v4.runtime.Recognizer;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.ocl.api.ParseDiagnostics;

/**
 * ANTLR4 error listener that collects all parse errors as {@link Resource.Diagnostic} instances.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tErrorListener extends BaseErrorListener {

	/**
	 * The shared collector: the list and the diagnostic format are the same for every language,
	 * and only this class has to stay here — it extends ANTLR's {@code BaseErrorListener}, which
	 * the API bundle cannot see.
	 */
	private final ParseDiagnostics errors = new ParseDiagnostics();

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
			int line, int charPositionInLine, String msg, RecognitionException e) {
		errors.addError(withHint(msg), line, charPositionInLine);
	}

	/**
	 * Adds what an author needs to know to a message ANTLR words for the grammar (#375): an
	 * '@' in a template is mostly an OCL {@code @pre}, which has no meaning without a
	 * postcondition.
	 */
	static String withHint(String msg) {
		if (msg.startsWith("token recognition error at: '@")) {
			return msg + " — '@pre' is not allowed in templates: a template has no postcondition (MOFM2T §8.2)";
		}
		return msg;
	}

	/**
	 * Returns the collected errors.
	 *
	 * @return unmodifiable list of parse errors
	 */
	List<Resource.Diagnostic> getErrors() {
		return errors.getDiagnostics();
	}

	/**
	 * Returns whether any errors were collected.
	 *
	 * @return {@code true} if errors were collected
	 */
	boolean hasErrors() {
		return errors.hasErrors();
	}
}
