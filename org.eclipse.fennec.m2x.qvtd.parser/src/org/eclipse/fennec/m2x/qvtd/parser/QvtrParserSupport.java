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
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.antlr.v4.runtime.CharStreams;
import org.antlr.v4.runtime.CommonTokenStream;
import org.antlr.v4.runtime.Token;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.qvtbase.Domain;
import org.eclipse.fennec.m2x.model.qvtbase.Rule;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
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

	static {
		// Ecore registers itself in the global registry when its package class initializes,
		// and nothing guarantees that happened before the first parse (see QvtoParserSupport).
		EcorePackage.eINSTANCE.getNsURI();
	}


	/** Where every expression node this support parsed stood — see {@link #positionOf}. */
	private final Map<EObject, SourcePosition> nodePositions =
			Collections.synchronizedMap(new IdentityHashMap<>());

	/**
	 * Where a node stood, for a diagnostic that knows the node and not the place.
	 *
	 * @param node the expression node, may be {@code null}
	 * @return the position, or {@code null} when this support did not parse that node
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
	 * Where an {@code import} of the given name was written, for a link diagnostic (#264).
	 *
	 * @param transformation the transformation carrying the import, must not be {@code null}
	 * @param importedName   the imported name as {@link #IMPORTS_ANNOTATION} records it
	 * @return the position of its first declaration, or {@code null} when this support did not
	 *         parse that transformation or it does not import the name
	 */
	public SourcePosition positionOfImport(RelationalTransformation transformation, String importedName) {
		Objects.requireNonNull(transformation, "transformation must not be null");
		EAnnotation annotation = transformation.getEAnnotation(IMPORTS_ANNOTATION);
		if (annotation == null) {
			return null;
		}
		for (Map.Entry<String, String> detail : annotation.getDetails()) {
			if (Objects.equals(importedName, detail.getValue()) && detail instanceof EObject entry) {
				return nodePositions.get(entry);
			}
		}
		return null;
	}

	/**
	 * Annotation on a parsed transformation carrying the unit names its {@code import}
	 * declarations asked for (§7.11.1), keyed by their position.
	 *
	 * <p>The names are unresolved here — resolving them needs unit resolvers and an
	 * allow-list, which belong to the engine, not to the parser.
	 */
	public static final String IMPORTS_ANNOTATION = "qvtr.imports";

	/**
	 * Annotation on a parsed transformation carrying the calls of relations it does not declare
	 * while it imports units (#360): each reference is a {@code RelationCallExp} without its
	 * relation, the detail of the same index the name it called — {@code R} or {@code T.R}.
	 * The link phase resolves them against the imported transformations.
	 */
	public static final String PENDING_CALLS_ANNOTATION = "qvtr.pendingCalls";

	/** Annotation carrying the name of the transformation a transformation extends (§7.11.1.1). */
	public static final String EXTENDS_ANNOTATION = "qvtr.extends";

	/**
	 * Parses the transformation of a QVT-R source using the given package registry for
	 * metamodel resolution.
	 *
	 * <p>A unit may hold several transformations (§7.13.7 topLevelCS). This returns the one no
	 * other transformation of the unit extends; where there are several such, it is an error to
	 * leave the choice open — {@link #parse(String, String, String, EPackage.Registry)} names one,
	 * {@link #parseAll} returns them all (#360).
	 *
	 * <p>The registry is always explicit here: the fallback to
	 * {@link EPackage.Registry#INSTANCE} belongs to
	 * {@code QvtdConfiguration.packageRegistry()} and is applied there, once (D42).
	 *
	 * @param source the QVT-R source text
	 * @param unitName the name of the compilation unit (for error messages)
	 * @param registry the package registry for metamodel lookup
	 * @return the parsed transformation AST
	 * @throws QvtdParseException if the source contains syntax errors, or several
	 *             transformations none of which extends the others
	 */
	public RelationalTransformation parse(String source, String unitName,
			EPackage.Registry registry) throws QvtdParseException {
		List<RelationalTransformation> all = parseAll(source, unitName, registry);
		Set<RelationalTransformation> bases = new HashSet<>();
		for (RelationalTransformation t : all) {
			if (t.getExtends() instanceof RelationalTransformation base) {
				bases.add(base);
			}
		}
		List<RelationalTransformation> candidates = all.stream().filter(t -> !bases.contains(t)).toList();
		if (candidates.size() != 1) {
			throw new QvtdParseException("'" + unitName + "' holds the transformations "
					+ candidates.stream().map(RelationalTransformation::getName).toList()
					+ "; name the one to parse, or parse them all", List.of());
		}
		return candidates.get(0);
	}

	/**
	 * Parses one named transformation of a QVT-R source.
	 *
	 * @param source the QVT-R source text
	 * @param unitName the name of the compilation unit (for error messages)
	 * @param transformationName the name of the transformation to return
	 * @param registry the package registry for metamodel lookup
	 * @return the parsed transformation AST
	 * @throws QvtdParseException if the source contains syntax errors or no transformation of
	 *             that name
	 */
	public RelationalTransformation parse(String source, String unitName, String transformationName,
			EPackage.Registry registry) throws QvtdParseException {
		Objects.requireNonNull(transformationName, "transformationName must not be null");
		List<RelationalTransformation> all = parseAll(source, unitName, registry);
		for (RelationalTransformation t : all) {
			if (transformationName.equals(t.getName())) {
				return t;
			}
		}
		throw new QvtdParseException("'" + unitName + "' holds no transformation '" + transformationName
				+ "', only " + all.stream().map(RelationalTransformation::getName).toList(), List.of());
	}

	/**
	 * Parses every transformation of a QVT-R source (§7.13.7 topLevelCS: a set of them, #360).
	 *
	 * @param source the QVT-R source text
	 * @param unitName the name of the compilation unit (for error messages)
	 * @param registry the package registry for metamodel lookup
	 * @return the transformations in the order the source declares them; a source without
	 *         any yields one empty transformation named after the unit
	 * @throws QvtdParseException if the source contains syntax errors
	 */
	public List<RelationalTransformation> parseAll(String source, String unitName,
			EPackage.Registry registry) throws QvtdParseException {
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(unitName, "unitName must not be null");
		Objects.requireNonNull(registry, "registry must not be null");

		QvtRParser parser = createParser(source);
		QvtrErrorListener errorListener = configureErrorHandling(parser);

		QvtRParser.CompilationUnitEntryContext tree = parser.compilationUnitEntry();

		checkErrors(errorListener, unitName);

		QvtrUnitBuilder builder = new QvtrUnitBuilder(registry);
		List<RelationalTransformation> result = builder.visitCompilationUnitEntry(tree);
		checkResolutionErrors(builder.getDiagnostics());
		// One merged map: node identities are unique, and a transformation that imports others
		// evaluates their nodes too. Each entry names the unit it came from (#116).
		builder.getNodePositions().forEach((node, position) ->
				nodePositions.put(node, position.inUnit(unitName)));

		List<String> warnings = deprecatedEscapes(parser, unitName);
		for (RelationalTransformation t : result) {
			recordWarnings(t, warnings);
			// Ensure the transformation has a name
			if (t.getName() == null || "_unnamed".equals(t.getName())) {
				t.setName(unitName);
			}
			// §7.5: Validate binding restrictions on all relations
			validateBindings(t, unitName, builder.getNodePositions());
		}
		return result;
	}

	/**
	 * Gives a transformation the queries of another it calls a relation of, those it does not
	 * declare itself — the called relation's expressions name them (#360).
	 *
	 * @param child the calling transformation, must not be {@code null}
	 * @param base the transformation the called relation comes from, must not be {@code null}
	 */
	public static void inheritQueries(RelationalTransformation child, RelationalTransformation base) {
		Objects.requireNonNull(child, "child must not be null");
		Objects.requireNonNull(base, "base must not be null");
		QvtrUnitBuilder.mergeQueries(child, base);
	}

	/**
	 * Inherits from a base transformation another unit holds, as {@code extends} does within one
	 * unit (§7.11.1.1): the base's rules, keys and queries the transformation does not declare
	 * itself, and then the {@code overrides} that name an inherited rule (#360).
	 *
	 * @param child the extending transformation, must not be {@code null}
	 * @param base the transformation it extends, must not be {@code null}
	 */
	public static void inherit(RelationalTransformation child, RelationalTransformation base) {
		Objects.requireNonNull(child, "child must not be null");
		Objects.requireNonNull(base, "base must not be null");
		QvtrUnitBuilder.mergeInheritedRules(child, base);
		for (Rule rule : child.getRule()) {
			EAnnotation ann = rule.getEAnnotation("qvtr.overrides");
			String name = ann != null ? ann.getDetails().get("name") : null;
			if (name == null || rule.getOverrides() != null) {
				continue;
			}
			for (Rule candidate : child.getRule()) {
				if (candidate != rule && name.equals(candidate.getName())) {
					rule.setOverrides(candidate);
				}
			}
		}
	}

	/**
	 * Annotation on a parsed transformation carrying the warnings of its parse — the parse has
	 * no channel for them, so a run reports them (#361).
	 */
	public static final String WARNINGS_ANNOTATION = "qvtr.warnings";

	/** One warning per {@code _<reserved word>}: the deprecated escape §7.13.2 still allows. */
	private static List<String> deprecatedEscapes(QvtRParser parser, String unitName) {
		List<String> warnings = new ArrayList<>();
		for (Token token : ((CommonTokenStream) parser.getTokenStream()).getTokens()) {
			String text = token.getText();
			if (token.getType() == QvtRLexer.IDENTIFIER && !QvtrExpressionBuilder.unescapeDeprecated(text).equals(text)) {
				warnings.add(new SourcePosition(unitName, token.getLine(), token.getCharPositionInLine()) + ": '" + text
						+ "' is a deprecated escape of '" + text.substring(1) + "'; write _'" + text.substring(1)
						+ "' (§7.13.2)");
			}
		}
		return warnings;
	}

	private static void recordWarnings(RelationalTransformation transformation, List<String> warnings) {
		if (warnings.isEmpty()) {
			return;
		}
		EAnnotation annotation = EcoreFactory.eINSTANCE.createEAnnotation();
		annotation.setSource(WARNINGS_ANNOTATION);
		for (int i = 0; i < warnings.size(); i++) {
			annotation.getDetails().put(String.valueOf(i), warnings.get(i));
		}
		transformation.getEAnnotations().add(annotation);
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
	 * The §7.5 check of one direction: for each relation that enforces a domain of the target
	 * model, the variables it needs there but nothing binds in that direction (rule 2.1). A
	 * variable only the target domain binds passes the parse-time check, which cannot know the
	 * direction, and is reported here, before a run (decided in #359).
	 *
	 * @param transformation the transformation
	 * @param targetModelName the model the run enforces
	 * @return one message per relation and variable, empty if the direction is executable
	 */
	public List<String> unboundInDirection(RelationalTransformation transformation, String targetModelName) {
		Objects.requireNonNull(transformation, "transformation must not be null");
		QvtrBindingValidator validator = new QvtrBindingValidator(Map.of());
		List<String> messages = new ArrayList<>();
		for (Rule rule : transformation.getRule()) {
			if (!(rule instanceof Relation relation) || relation.isIsAbstract()) {
				continue;
			}
			for (Domain domain : relation.getDomain()) {
				if (domain instanceof RelationDomain target && target.isIsEnforceable()
						&& target.getTypedModel() != null && target.getTypedModel().getName().equals(targetModelName)) {
					for (String variable : validator.unboundForTarget(relation, target)) {
						messages.add("§7.5: Variable '" + variable + "' of relation '" + relation.getName()
								+ "' is not bound when enforcing '" + targetModelName
								+ "': nothing but the enforced domain binds it");
					}
				}
			}
		}
		return messages;
	}

	/**
	 * Validates §7.5 binding restrictions on all relations in the transformation.
	 * Reports unresolved variable references, binding order violations and undeclared names
	 * at binding sites (#278), the latter at the position the name was written.
	 */
	private void validateBindings(RelationalTransformation transformation, String unitName,
			Map<EObject, SourcePosition> positions) throws QvtdParseException {
		QvtrBindingValidator validator = new QvtrBindingValidator(positions);
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
