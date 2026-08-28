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

import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
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

	static {
		// Ecore registers itself in the global registry when its package class initializes,
		// and nothing guarantees that happened before the first parse: a plain-JVM caller
		// whose first EMF call is `modeltype ECORE uses ecore('…')` would otherwise be told
		// the metamodel cannot be resolved. Touching it here makes the parser self-sufficient.
		EcorePackage.eINSTANCE.getNsURI();
	}


	/**
	 * Where every expression node this support parsed stood, across all units.
	 *
	 * <p>One map rather than one per transformation: node identities are unique, so a merged map
	 * is unambiguous — and it is what an evaluation needs, because a transformation that imports
	 * others evaluates their nodes too. Each entry names the unit it came from (#116).
	 */
	private final Map<EObject, SourcePosition> nodePositions =
			java.util.Collections.synchronizedMap(new IdentityHashMap<>());

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
		checkResolutionErrors(builder.getDiagnostics());

		// Kept with the transformation rather than returned: the parse signature is API, and the
		// positions are only interesting to whoever evaluates this transformation later (#116).
		// The unit is stamped here because with imports a bare line names the wrong file.
		builder.getNodePositions().forEach((node, position) ->
				nodePositions.put(node, position.inUnit(unitName)));

		// Ensure the transformation has a name
		if (result.getName() == null || "_unnamed".equals(result.getName())) {
			result.setName(unitName);
		}

		return result;
	}

	/**
	 * Where a node stood, for a diagnostic that knows the node and not the place.
	 *
	 * <p>Walks up the containment chain: ANTLR dispatches to the generated visit method for each
	 * rule, so only the node a visit was entered for is recorded, while a diagnostic is usually
	 * about an inner one. The enclosing expression's position is the honest answer for it — the
	 * expression rather than the exact token.
	 *
	 * @param node the expression node, may be {@code null}
	 * @return the position, or {@code null} when this support parsed no node in that chain
	 */
	public SourcePosition positionOf(EObject node) {
		for (EObject current = node; current != null; current = current.eContainer()) {
			SourcePosition position = nodePositions.get(current);
			if (position != null) {
				return position;
			}
		}
		return null;
	}

	/**
	 * Forgets the recorded positions, for a caller that drops what it parsed.
	 */
	public void clearNodePositions() {
		nodePositions.clear();
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

	/**
	 * Rejects a unit whose metamodels could not be resolved (#66).
	 *
	 * <p>Collected rather than thrown on first sight, so a transformation declaring
	 * several unknown metamodels reports all of them at once — the contract syntax
	 * errors already have.
	 *
	 * @param diagnostics the diagnostics collected while building
	 * @throws QvtoParseException if any were collected
	 */
	private void checkResolutionErrors(List<Resource.Diagnostic> diagnostics)
			throws QvtoParseException {
		if (!diagnostics.isEmpty()) {
			throw new QvtoParseException("QVT-O resolution error: "
					+ diagnostics.get(0).getMessage(), List.copyOf(diagnostics));
		}
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
