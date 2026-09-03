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
package org.eclipse.fennec.m2x.ocl.parser;

import java.util.List;
import java.util.Objects;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;
import org.osgi.service.component.annotations.ServiceScope;

/**
 * Entry point for OCL parsing. Provides methods to parse single expressions
 * and Complete OCL documents into EMF AST nodes.
 *
 * <p>Implements the {@link OclExpressionParser} interface from {@code ocl.api},
 * allowing the engine to depend on the parser abstraction rather than the concrete class.
 *
 * <p>This class is designed for use by the {@code OclEngine} implementation
 * and can be instantiated as a plain Java object (OSGi-optional):
 * <pre>
 * OclExpressionParser parser = new OclParserSupport();
 * OclExpression expr = parser.parse("self.name", employeeEClass);
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(scope = ServiceScope.PROTOTYPE, property = "parser.type=DEFAULT")
public class OclParserSupport implements OclExpressionParser {

	private final EPackage.Registry packageRegistry;
	private boolean strictPropertyResolution;
	private int maxInputLength = DEFAULT_MAX_INPUT_LENGTH;

	/**
	 * Creates a parser that resolves classifier names against the global
	 * {@link EPackage.Registry#INSTANCE}.
	 *
	 * <p>This is the plain-Java case, where the static registry is the correct
	 * answer and no model version ambiguity exists. Callers that hold their own
	 * packages — under OSGi, or wherever two versions of one nsURI can coexist —
	 * use {@link #OclParserSupport(ResourceSet)} instead (D42).
	 */
	public OclParserSupport() {
		this(EPackage.Registry.INSTANCE);
	}

	/**
	 * Creates a parser that resolves classifier names against the given registry.
	 *
	 * @param packageRegistry the registry used for classifier resolution, must not be {@code null}
	 */
	public OclParserSupport(EPackage.Registry packageRegistry) {
		this.packageRegistry = Objects.requireNonNull(packageRegistry,
				"packageRegistry must not be null");
	}

	/**
	 * Creates a parser that resolves classifier names against the package registry of
	 * the given resource set.
	 *
	 * <p>This is the form to reach for: a {@link ResourceSet} is what EMF hands around,
	 * and under OSGi it is what {@code emf.osgi} publishes — a configured, isolated stack
	 * comes as a resource set, not as a bare registry (D42).
	 *
	 * <p>It is also the constructor Declarative Services uses. The component binds the
	 * {@code ResourceSet} that {@code emf.osgi} registers as a prototype-scoped service, as a
	 * <em>mandatory</em> reference: the parser exists once the resource set does, so a
	 * metamodel registered for the resource set only ({@code emf.model.scope=resourceset})
	 * resolves from the first expression on, and never falls back to the global registry
	 * unnoticed (#245). Where several resource sets are published,
	 * {@code "resourceSet.target"} picks one.
	 *
	 * <p>Only the resource set's {@linkplain ResourceSet#getPackageRegistry() package
	 * registry} is used. Nothing is loaded through it: template and document sources are
	 * passed to the parser as text.
	 *
	 * @param resourceSet the resource set whose package registry resolves classifier
	 *        names, must not be {@code null}
	 */
	@Activate
	public OclParserSupport(
			@Reference(name = "resourceSet", scope = ReferenceScope.PROTOTYPE_REQUIRED) ResourceSet resourceSet) {
		this(Objects.requireNonNull(resourceSet, "resourceSet must not be null")
				.getPackageRegistry());
	}

	/**
	 * Returns the registry this parser resolves classifier names against.
	 *
	 * @return the package registry, never {@code null}
	 */
	public EPackage.Registry getPackageRegistry() {
		return packageRegistry;
	}

	/**
	 * Parses an OCL expression string in the context of the given classifier.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the classifier that defines the type of {@code self}
	 * @return the parsed expression AST with types resolved
	 * @throws OclParseException if the expression contains syntax or type errors
	 */
	/**
	 * Reports a navigation to a property the source type does not declare, instead of leaving it
	 * for the evaluator to resolve reflectively (#153).
	 *
	 * <p>Off by default: a property the parser cannot see may be there at evaluation time — a
	 * Complete OCL {@code def:} registered by an earlier parse call, a library property such as
	 * {@code oclLocale}, a feature of the runtime type where the static one is {@code EObject}.
	 * A caller that hands the parser the whole document — an editor, a validator, a build check —
	 * turns this on and gets the typo reported with its position. Within one document a
	 * {@code def:} is seen wherever it stands, so strict resolution does not report it.
	 *
	 * @param strict {@code true} to report unknown properties as parse errors
	 * @return this parser, for chaining
	 */
	public OclParserSupport strictPropertyResolution(boolean strict) {
		this.strictPropertyResolution = strict;
		return this;
	}

	public OclExpression parse(String expression, EClassifier contextType) throws OclParseException {
		requireWithinLimit(expression);
		try {
			OclParser parser = createParser(expression);
			OclErrorListener errorListener = configureErrorHandling(parser);

			OclParser.ExpressionEntryContext tree = parser.expressionEntry();

			checkErrors(errorListener, expression);

			OclAstBuilder builder = new OclAstBuilder(contextType, packageRegistry, strictPropertyResolution);
			OclExpression result = builder.visitExpressionEntry(tree);
			checkResolutionErrors(builder.support.getDiagnostics());
			return result;
		} catch (OclParseException alreadyReported) {
			throw alreadyReported;
		} catch (RuntimeException | StackOverflowError failure) {
			throw asParseException(failure);
		}
	}

	/**
	 * Parses a Complete OCL document, producing a list of constraints.
	 * Uses the registry this parser was created with for classifier resolution
	 * (the global {@link EPackage.Registry#INSTANCE} unless one was supplied).
	 *
	 * @param oclDocument the Complete OCL document text
	 * @return the list of parsed constraints
	 * @throws OclParseException if the document contains syntax or type errors
	 */
	@Override
	public List<Constraint> parseDocument(String oclDocument) throws OclParseException {
		return parseDocument(oclDocument, packageRegistry);
	}

	/**
	 * Parses a Complete OCL document using the given resource set for package resolution.
	 *
	 * @param oclDocument the Complete OCL document text
	 * @param resourceSet the resource set whose package registry is used
	 * @return the list of parsed constraints
	 * @throws OclParseException if the document contains syntax or type errors
	 */
	@Override
	public List<Constraint> parseDocument(String oclDocument, ResourceSet resourceSet)
			throws OclParseException {
		return parseDocument(oclDocument, resourceSet.getPackageRegistry());
	}

	private List<Constraint> parseDocument(String oclDocument, EPackage.Registry registry)
			throws OclParseException {
		requireWithinLimit(oclDocument);
		try {
			OclParser parser = createParser(oclDocument);
			OclErrorListener errorListener = configureErrorHandling(parser);

			OclParser.CompleteOclDocumentEntryContext tree = parser.completeOclDocumentEntry();

			checkErrors(errorListener, oclDocument);

			OclDocumentBuilder builder = new OclDocumentBuilder(registry);
			builder.setStrictPropertyResolution(strictPropertyResolution);
			List<Constraint> result = builder.buildDocument(tree);
			checkResolutionErrors(builder.getDiagnostics());
			return result;
		} catch (OclParseException alreadyReported) {
			throw alreadyReported;
		} catch (RuntimeException | StackOverflowError failure) {
			throw asParseException(failure);
		}
	}

	/**
	 * The longest input this parser accepts, in characters. Default {@value #DEFAULT_MAX_INPUT_LENGTH}.
	 *
	 * <p>The evaluator's {@code maxDepth} protects evaluation; nothing protected parsing. ANTLR
	 * and the AST visitor are both recursive, so deeply nested input — {@code ((((…))))} — ends in
	 * a {@code StackOverflowError} before any evaluation limit applies (#181). A length bound is
	 * the cheap half of the answer: it cannot tell nesting from length, but it is what keeps a
	 * remote or generated expression from reaching the recursion at all. The other half is below:
	 * whatever still escapes the recursion arrives as an {@code OclParseException}.
	 *
	 * @param maxInputLength the maximum number of characters, positive
	 * @return this parser, for chaining
	 * @since 1.0
	 */
	public OclParserSupport maxInputLength(int maxInputLength) {
		if (maxInputLength <= 0) {
			throw new IllegalArgumentException("maxInputLength must be positive");
		}
		this.maxInputLength = maxInputLength;
		return this;
	}

	/** Default for {@link #maxInputLength(int)}: 1 MB of text, far above any hand-written unit. */
	public static final int DEFAULT_MAX_INPUT_LENGTH = 1_000_000;

	private void requireWithinLimit(String input) throws OclParseException {
		Objects.requireNonNull(input, "input must not be null");
		if (input.length() > maxInputLength) {
			throw new OclParseException("OCL input is longer than " + maxInputLength
					+ " characters (" + input.length() + ")");
		}
	}

	/**
	 * Everything the parser and the AST builder can throw, as one kind of failure.
	 *
	 * <p>A caller of {@code parse} expects an {@code OclParseException}. It used to get whatever
	 * the visitor produced — {@code NoSuchElementException} from an {@code orElseThrow},
	 * {@code ClassCastException} from a visitor cast, {@code StackOverflowError} from the
	 * recursion — which is what a fuzzer finds first (#181).
	 */
	private static OclParseException asParseException(Throwable failure) {
		if (failure instanceof StackOverflowError) {
			return new OclParseException("OCL input is nested too deeply to parse");
		}
		String message = failure.getMessage();
		return new OclParseException(message == null || message.isBlank()
				? "Cannot parse the OCL input: " + failure.getClass().getSimpleName()
				: message, failure instanceof Exception e ? e : null);
	}

	private OclParser createParser(String input) {
		OclLexer lexer = new OclLexer(CharStreams.fromString(input));
		CommonTokenStream tokens = new CommonTokenStream(lexer);
		return new OclParser(tokens);
	}

	private OclErrorListener configureErrorHandling(OclParser parser) {
		OclErrorListener errorListener = new OclErrorListener();

		// Remove default console error listener
		parser.removeErrorListeners();
		parser.addErrorListener(errorListener);

		// Also configure the lexer
		OclLexer lexer = (OclLexer) parser.getTokenStream().getTokenSource();
		lexer.removeErrorListeners();
		lexer.addErrorListener(errorListener);

		return errorListener;
	}

	/**
	 * Rejects a unit whose names could not be resolved (#66).
	 *
	 * <p>Collected rather than thrown on first sight, so a source with several unknown
	 * names reports all of them at once — the same contract syntax errors already have.
	 *
	 * @param diagnostics the diagnostics collected while building
	 * @throws OclParseException if any were collected
	 */
	private void checkResolutionErrors(List<Resource.Diagnostic> diagnostics)
			throws OclParseException {
		if (!diagnostics.isEmpty()) {
			throw new OclParseException("OCL resolution error: "
					+ diagnostics.get(0).getMessage(), List.copyOf(diagnostics));
		}
	}

	private void checkErrors(OclErrorListener errorListener, String input)
			throws OclParseException {
		if (errorListener.hasErrors()) {
			List<Resource.Diagnostic> errors = errorListener.getErrors();
			String message = "OCL parse error: " + errors.get(0).getMessage();
			throw new OclParseException(message, errors);
		}
	}
}
