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
import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;

/**
 * Entry point for QVT-R parsing. Parses QVT-Relations transformation source text into
 * an EMF AST rooted at {@link RelationalTransformation}.
 *
 * <p>This class is designed for direct instantiation as a plain Java object (OSGi-optional).
 *
 * <pre>
 * QvtrParserSupport parser = new QvtrParserSupport();
 * RelationalTransformation t = parser.parse(source, "MyTransformation");
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrParserSupport {

	/**
	 * Parses a QVT-R transformation source using the given package registry
	 * for metamodel resolution.
	 *
	 * <p>The registry is always explicit here: the fallback to
	 * {@link EPackage.Registry#INSTANCE} belongs to
	 * {@code QvtdConfiguration.packageRegistry()} and is applied there, once (D42).
	 *
	 * @param source the QVT-R source text
	 * @param unitName the name of the compilation unit (for error messages)
	 * @param registry the package registry for metamodel lookup
	 * @return the parsed transformation AST
	 * @throws QvtdParseException if the source contains syntax errors
	 */
	public RelationalTransformation parse(String source, String unitName,
			EPackage.Registry registry) throws QvtdParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		Objects.requireNonNull(registry, "registry must not be null");

		QvtRParser parser = createParser(source);
		QvtrErrorListener errorListener = configureErrorHandling(parser);

		QvtRParser.CompilationUnitEntryContext tree = parser.compilationUnitEntry();

		checkErrors(errorListener, unitName);

		QvtrUnitBuilder builder = new QvtrUnitBuilder(registry);
		RelationalTransformation result = builder.visitCompilationUnitEntry(tree);
		checkResolutionErrors(builder.getDiagnostics());

		// Ensure the transformation has a name
		if (result.getName() == null || "_unnamed".equals(result.getName())) {
			result.setName(unitName);
		}

		// §7.5: Validate binding restrictions on all relations
		validateBindings(result, unitName);

		return result;
	}

	private QvtRParser createParser(String input) {
		QvtRLexer lexer = new QvtRLexer(CharStreams.fromString(input));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new QvtRParser(tokens);
	}

	private QvtrErrorListener configureErrorHandling(QvtRParser parser) {
		QvtrErrorListener errorListener = new QvtrErrorListener();

		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		QvtRLexer lexer = (QvtRLexer) parser.getTokenStream().getTokenSource();
		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);

		return errorListener;
	}

	/**
	 * Validates §7.5 binding restrictions on all relations in the transformation.
	 * Reports unresolved variable references and binding order violations.
	 */
	private void validateBindings(RelationalTransformation transformation, String unitName)
			throws QvtdParseException {
		QvtrBindingValidator validator = new QvtrBindingValidator();
		List<Resource.Diagnostic> allDiagnostics = new ArrayList<>();

		for (Rule rule : transformation.getRule()) {
			if (rule instanceof Relation relation) {
				allDiagnostics.addAll(validator.validate(relation));
			}
		}

		if (!allDiagnostics.isEmpty()) {
			String message = "QVT-R binding validation error in '" + unitName + "': "
					+ allDiagnostics.get(0).getMessage();
			throw new QvtdParseException(message, allDiagnostics);
		}
	}

	/**
	 * Rejects a unit whose metamodels or type names could not be resolved (#66).
	 *
	 * <p>Collected rather than thrown on first sight, so a transformation with several
	 * unknown names reports all of them at once — the contract syntax errors already have.
	 *
	 * @param diagnostics the diagnostics collected while building
	 * @throws QvtdParseException if any were collected
	 */
	private void checkResolutionErrors(List<Resource.Diagnostic> diagnostics)
			throws QvtdParseException {
		if (!diagnostics.isEmpty()) {
			throw new QvtdParseException("QVT-R resolution error: "
					+ diagnostics.get(0).getMessage(), List.copyOf(diagnostics));
		}
	}

	private void checkErrors(QvtrErrorListener errorListener, String unitName)
			throws QvtdParseException {
		if (errorListener.hasErrors()) {
			List<Resource.Diagnostic> errors = errorListener.getErrors();
			String message = "QVT-R parse error in '" + unitName + "': "
					+ errors.get(0).getMessage();
			throw new QvtdParseException(message, errors);
		}
	}
}
