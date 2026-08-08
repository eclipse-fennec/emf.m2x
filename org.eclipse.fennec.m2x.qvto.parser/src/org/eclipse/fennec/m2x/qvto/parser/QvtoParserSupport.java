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

import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;

/**
 * Entry point for QVT-O parsing. Parses QVT-O transformation source text into
 * an EMF AST rooted at {@link OperationalTransformation}.
 *
 * <p>This class is designed for direct instantiation as a plain Java object (OSGi-optional).
 * The {@code QvtoEngine} implementation uses this class to parse transformation sources.
 *
 * <pre>
 * QvtoParserSupport parser = new QvtoParserSupport();
 * OperationalTransformation t = parser.parse(source, "MyTransformation");
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoParserSupport {

	/**
	 * EAnnotation source used to mark stub modules that need link-time resolution.
	 * Inline-defined modules (parsed in the same compilation unit) do NOT carry this
	 * annotation, allowing the linker to distinguish stubs from forward declarations.
	 */
	public static final String LINKER_STUB_ANNOTATION = QvtoUnitBuilder.LINKER_STUB_ANNOTATION;

	/**
	 * Parses a QVT-O transformation source using the given package registry
	 * for metamodel resolution.
	 *
	 * <p>The registry is always explicit here: the fallback to
	 * {@link EPackage.Registry#INSTANCE} belongs to
	 * {@code QvtoConfiguration.packageRegistry()} and is applied there, once (D42).
	 *
	 * @param source the QVT-O source text
	 * @param unitName the name of the compilation unit (for error messages)
	 * @param registry the package registry for metamodel lookup
	 * @return the parsed transformation AST
	 * @throws QvtoParseException if the source contains syntax errors
	 */
	public OperationalTransformation parse(String source, String unitName,
			EPackage.Registry registry) throws QvtoParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		Objects.requireNonNull(registry, "registry must not be null");

		QvtOParser parser = createParser(source);
		QvtoErrorListener errorListener = configureErrorHandling(parser);

		QvtOParser.CompilationUnitEntryContext tree = parser.compilationUnitEntry();

		checkErrors(errorListener, unitName);

		QvtoUnitBuilder builder = new QvtoUnitBuilder(registry);
		OperationalTransformation result = builder.visitCompilationUnitEntry(tree);

		// Ensure the transformation has a name
		if (result.getName() == null || "_unnamed".equals(result.getName())) {
			result.setName(unitName);
		}

		return result;
	}

	private QvtOParser createParser(String input) {
		QvtOLexer lexer = new QvtOLexer(CharStreams.fromString(input));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new QvtOParser(tokens);
	}

	private QvtoErrorListener configureErrorHandling(QvtOParser parser) {
		QvtoErrorListener errorListener = new QvtoErrorListener();

		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		QvtOLexer lexer = (QvtOLexer) parser.getTokenStream().getTokenSource();
		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);

		return errorListener;
	}

	private void checkErrors(QvtoErrorListener errorListener, String unitName)
			throws QvtoParseException {
		if (errorListener.hasErrors()) {
			List<Resource.Diagnostic> errors = errorListener.getErrors();
			String message = "QVT-O parse error in '" + unitName + "': "
					+ errors.get(0).getMessage();
			throw new QvtoParseException(message, errors);
		}
	}
}
