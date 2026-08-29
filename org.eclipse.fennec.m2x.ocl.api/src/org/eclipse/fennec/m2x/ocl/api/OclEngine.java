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
package org.eclipse.fennec.m2x.ocl.api;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Main facade for parsing, evaluating, and validating OCL expressions.
 *
 * <p>Implementations must be usable as plain Java objects with constructor injection
 * (OSGi-optional, see architecture section 10). The typical standalone usage is:
 * <pre>
 * OclConfiguration config = OclConfiguration.builder(parser).build();
 * OclEngine engine = OclEngines.create(config);
 * Object result = engine.evaluate("self.name", OclContext.of(myEObject));
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ProviderType
public interface OclEngine {

	// --- Parsing ---

	/**
	 * Parses an OCL expression string in the context of the given classifier.
	 *
	 * @param expression the OCL expression text
	 * @param contextType the {@code EClassifier} that defines the type of {@code self}
	 * @return the parsed expression AST with types resolved
	 * @throws OclParseException if the expression contains syntax or type errors
	 */
	OclExpression parse(String expression, EClassifier contextType) throws OclParseException;

	/**
	 * Parses a Complete OCL document, producing a list of constraints.
	 *
	 * <p>A Complete OCL document can define invariants, pre/postconditions,
	 * derived properties, and operation bodies for existing classifiers:
	 * <pre>
	 * package company
	 * context Employee
	 *   inv nameNotEmpty: self.name.size() &gt; 0
	 * endpackage
	 * </pre>
	 *
	 * @param oclDocument the Complete OCL document text
	 * @return the list of parsed constraints with context references populated
	 * @throws OclParseException if the document contains syntax or type errors
	 */
	List<Constraint> parseDocument(String oclDocument) throws OclParseException;

	/**
	 * Parses a Complete OCL document using the given resource set for package resolution.
	 *
	 * @param oclDocument the Complete OCL document text
	 * @param resourceSet the resource set whose package registry is used for classifier resolution
	 * @return the list of parsed constraints with context references populated
	 * @throws OclParseException if the document contains syntax or type errors
	 */
	List<Constraint> parseDocument(String oclDocument, ResourceSet resourceSet) throws OclParseException;

	/**
	 * Parses a Complete OCL document and registers any {@code def:} constraints
	 * so that the defined properties and operations are available during evaluation.
	 *
	 * <p>This method combines {@link #parseDocument(String)} with runtime registration:
	 * after parsing, all {@code def:} constraints are stored internally. Subsequent
	 * calls to {@link #evaluate(OclExpression, OclContext)} will resolve references
	 * to def-properties and def-operations defined in the loaded document.
	 *
	 * <p>Example:
	 * <pre>
	 * engine.loadDocument("""
	 *     context Person
	 *       def: isAdult : Boolean = self.age >= 18
	 *       inv adultCheck: self.isAdult
	 *     """);
	 * </pre>
	 *
	 * @param oclDocument the Complete OCL document text
	 * @return the list of parsed constraints
	 * @throws OclParseException if the document contains syntax or type errors
	 */
	List<Constraint> loadDocument(String oclDocument) throws OclParseException;

	// --- Evaluation ---

	/**
	 * Evaluates a pre-parsed expression against an evaluation context using default options.
	 *
	 * <p>Returns {@link OclInvalid#INSTANCE} if evaluation produces the OCL {@code invalid}
	 * value, and {@code null} for the OCL {@code void}/{@code null} value.
	 *
	 * <p><b>Return-type mapping</b> (default, OCL-spec-native):
	 * <ul>
	 *   <li>OCL {@code Integer} / {@code UnlimitedNatural} → {@link Integer} (narrowed from {@code Long} when it fits)</li>
	 *   <li>OCL {@code Sequence(T)} → {@link java.util.ArrayList}</li>
	 *   <li>OCL {@code OrderedSet(T)} → {@code OclOrderedSet} (extends {@code ArrayList})</li>
	 *   <li>OCL {@code Bag(T)} → {@code OclBag} (extends {@code ArrayList})</li>
	 *   <li>OCL {@code Set(T)} → {@code OclSet} (extends {@code AbstractSet})</li>
	 *   <li>OCL {@code Map(K,V)} → {@link java.util.LinkedHashMap}</li>
	 * </ul>
	 * When {@link OclEvaluationOptions#useEMFTypes()} is {@code true}, the top-level
	 * {@link java.util.Collection} result is wrapped as
	 * {@link org.eclipse.emf.common.util.EList} and a top-level {@link java.util.Map}
	 * as {@link org.eclipse.emf.common.util.EMap} (see
	 * <a href="https://github.com/eclipse-fennec/emf.m2x/issues/4">issue #4</a>).
	 *
	 * @param expression the parsed OCL expression
	 * @param context the evaluation context providing {@code self}, optional extent, and variables
	 * @return the evaluation result
	 */
	Object evaluate(OclExpression expression, OclContext context);

	/**
	 * Evaluates a pre-parsed expression against an evaluation context with explicit options.
	 *
	 * <p>See {@link #evaluate(OclExpression, OclContext)} for the default return-type
	 * mapping. Set {@link OclEvaluationOptions#useEMFTypes()} to {@code true} to get a
	 * top-level {@link org.eclipse.emf.common.util.EList} or
	 * {@link org.eclipse.emf.common.util.EMap} instead of the native Java collection.
	 *
	 * @param expression the parsed OCL expression
	 * @param context the evaluation context providing {@code self}, optional extent, and variables
	 * @param options evaluation options controlling null handling, error recovery, depth, timeout,
	 *        and the top-level return-type shape ({@code useEMFTypes})
	 * @return the evaluation result
	 */
	Object evaluate(OclExpression expression, OclContext context, OclEvaluationOptions options);

	/**
	 * Convenience method that parses and evaluates an expression in one step.
	 *
	 * <p>The context type for parsing is derived from {@code context.self().eClass()}.
	 *
	 * @param expression the OCL expression text
	 * @param context the evaluation context providing {@code self}, optional extent, and variables
	 * @return the evaluation result
	 * @throws OclParseException if the expression contains syntax or type errors
	 */
	Object evaluate(String expression, OclContext context) throws OclParseException;

	// --- Evaluation with Diagnostics ---

	/**
	 * Evaluates a pre-parsed expression and returns both the result value and
	 * any diagnostics (errors, warnings) collected during evaluation.
	 *
	 * <p>Unlike {@link #evaluate(OclExpression, OclContext, OclEvaluationOptions)},
	 * this method never throws on evaluation errors — instead, errors are captured
	 * in the returned {@link OclResult#diagnostics()}.
	 *
	 * <p>The behavior depends on the {@link OclEvaluationOptions}:
	 * <ul>
	 *   <li>{@code STRICT + FAIL_FAST}: First error stops evaluation,
	 *       value is {@link OclInvalid#INSTANCE}, diagnostics contains the error.</li>
	 *   <li>{@code STRICT + COLLECT_ERRORS}: {@code OclInvalid} propagates through
	 *       the expression, all errors are collected.</li>
	 *   <li>{@code LENIENT + COLLECT_ERRORS}: Best-effort result with fallback values,
	 *       all warnings and errors are collected.</li>
	 * </ul>
	 *
	 * @param expression the parsed OCL expression
	 * @param context the evaluation context providing {@code self}, optional extent, and variables
	 * @param options evaluation options controlling null handling, error recovery, depth, and timeout
	 * @return the evaluation result with diagnostics
	 */
	OclResult evaluateWithDiagnostics(OclExpression expression, OclContext context,
			OclEvaluationOptions options);

	// --- Validation ---

	/**
	 * Validates a parsed expression for type correctness and well-formedness.
	 *
	 * <p><b>Not implemented yet</b> (GAP-3): the default engine throws
	 * {@link UnsupportedOperationException}. What parsing can resolve it reports as it parses,
	 * so an expression that came out of {@code parse} has been checked as far as this
	 * implementation checks anything.
	 *
	 * @param expression the parsed OCL expression to validate
	 * @param contextType the {@code EClassifier} that defines the type of {@code self}
	 * @return a list of diagnostics; empty if the expression is valid
	 * @throws UnsupportedOperationException if the engine has no type checker
	 */
	List<Diagnostic> validate(OclExpression expression, EClassifier contextType);

	// --- Extension: Complete OCL Documents ---

	/**
	 * Registers a Complete OCL document contribution (non-OSGi usage).
	 *
	 * <p>In an OSGi environment, contributions are discovered automatically via the
	 * whiteboard pattern ({@code @Component(service = CompleteOclContribution.class)}).
	 *
	 * @param contribution the Complete OCL contribution to register
	 */
	void registerCompleteOclDocument(CompleteOclContribution contribution);

	/**
	 * Unregisters a previously registered Complete OCL document contribution.
	 *
	 * @param contribution the Complete OCL contribution to unregister
	 */
	void unregisterCompleteOclDocument(CompleteOclContribution contribution);

	// --- EMF delegate integration ---

	/**
	 * Registers this engine as the EMF delegate for operation bodies, derived features
	 * and validation constraints annotated with the OCL delegate URI.
	 *
	 * @see #uninstallDelegates()
	 */
	void installDelegates();

	/**
	 * Removes the delegate registrations made by {@link #installDelegates()}.
	 */
	void uninstallDelegates();

	/**
	 * Returns the options delegate evaluation runs with.
	 *
	 * @return the delegate options, never {@code null}
	 */
	OclEvaluationOptions getDelegateOptions();

	/**
	 * Sets the options delegate evaluation runs with.
	 *
	 * @param options the delegate options, must not be {@code null}
	 */
	void setDelegateOptions(OclEvaluationOptions options);

	// --- Performance ---

	/**
	 * Parses and caches the OCL annotations of a package ahead of first use, so that the
	 * first evaluation does not pay for parsing.
	 *
	 * @param ePackage the package to warm up, must not be {@code null}
	 */
	void warmUp(EPackage ePackage);

	/**
	 * Returns the expression cache this engine parses through.
	 *
	 * @return the expression cache, never {@code null}
	 */
	OclExpressionCache getExpressionCache();

	// --- Composition ---

	/**
	 * Returns the options this engine evaluates with when none are given.
	 *
	 * <p>For engines composing on top of OCL: they evaluate with the host engine's
	 * settings plus their own operation providers, rather than inventing settings the
	 * caller never configured.
	 *
	 * @return the default evaluation options, never {@code null}
	 */
	OclEvaluationOptions getDefaultOptions();

	/**
	 * Returns the operation providers active for the given options.
	 *
	 * <p>For engines composing on top of OCL: QVT-O and M2T resolve their own operations
	 * through the same dispatch and have to know what is in play. Ordinary evaluation
	 * does not need this.
	 *
	 * @param options the evaluation options for this evaluation
	 * @return the active providers, in dispatch order
	 */
	List<OclOperationProvider> getOperationProviders(OclEvaluationOptions options);
}
