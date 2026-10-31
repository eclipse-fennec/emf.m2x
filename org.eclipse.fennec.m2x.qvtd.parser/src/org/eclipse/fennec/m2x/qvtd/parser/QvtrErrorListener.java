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
package org.eclipse.fennec.m2x.qvtd.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

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
class QvtrErrorListener extends BaseErrorListener {

	/**
	 * The shared collector: the list and the diagnostic format are the same for every language,
	 * and only this class has to stay here — it extends ANTLR's {@code BaseErrorListener}, which
	 * the API bundle cannot see.
	 */
	private final ParseDiagnostics errors = new ParseDiagnostics();

	@Override
	public void syntaxError(Recognizer<?, ?> recognizer, Object offendingSymbol,
			int line, int charPositionInLine, String msg, RecognitionException e) {
		errors.addError(collapseIdentifiers(msg), line, charPositionInLine);
	}

	/**
	 * The tokens that are all "an identifier" where one is expected: the words OCL and QVT-R
	 * allow as names besides {@code IDENTIFIER} (#361). Listed one by one they buried what the
	 * message is about.
	 */
	private static final Set<String> IDENTIFIER_TOKENS = Set.of("IDENTIFIER", "ESCAPED_IDENTIFIER", "'body'",
			"'context'", "'def'", "'derive'", "'endpackage'", "'init'", "'inv'", "'package'", "'post'", "'pre'",
			"'static'", "'include'", "'library'", "'abstract'", "'opposite'", "'default_values'");

	private static final Pattern EXPECTING = Pattern.compile("expecting \\{([^}]*)\\}");

	/** Replaces the identifier tokens of an expectation set by the single word "identifier". */
	static String collapseIdentifiers(String msg) {
		Matcher matcher = EXPECTING.matcher(msg);
		if (!matcher.find()) {
			return msg;
		}
		List<String> expected = new ArrayList<>(List.of(matcher.group(1).split(", ")));
		if (!expected.contains("IDENTIFIER")) {
			return msg;
		}
		expected.removeIf(IDENTIFIER_TOKENS::contains);
		expected.add("identifier");
		String replacement = expected.size() == 1 ? "expecting identifier"
				: "expecting {" + String.join(", ", expected) + "}";
		return msg.substring(0, matcher.start()) + replacement + msg.substring(matcher.end());
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
