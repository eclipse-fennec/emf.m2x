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
package org.eclipse.fennec.m2m.ocl.api;

import java.util.List;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.ocl.Constraint;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.osgi.annotation.versioning.ProviderType;

/**
 * Main facade for parsing, evaluating, and validating OCL expressions.
 *
 * <p>Implementations must be usable as plain Java objects with constructor injection
 * (OSGi-optional, see architecture section 10). The typical standalone usage is:
 * <pre>
 * OclEngine engine = new OclEngineImpl(parser, stdlib);
 * Object result = engine.evaluate("self.name", myEObject);
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
	 * @return the parsed expression AST
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

	// --- Evaluation without extent ---

	/**
	 * Evaluates a pre-parsed expression against a context object using default options.
	 *
	 * <p>Returns {@link OclInvalid#INSTANCE} if evaluation produces the OCL {@code invalid}
	 * value, and {@code null} for the OCL {@code void}/{@code null} value.
	 *
	 * @param expression the parsed OCL expression
	 * @param context the {@code EObject} that serves as {@code self}
	 * @return the evaluation result
	 */
	Object evaluate(OclExpression expression, EObject context);

	/**
	 * Evaluates a pre-parsed expression against a context object with explicit options.
	 *
	 * @param expression the parsed OCL expression
	 * @param context the {@code EObject} that serves as {@code self}
	 * @param options evaluation options controlling null handling, error recovery, depth, and timeout
	 * @return the evaluation result
	 */
	Object evaluate(OclExpression expression, EObject context, OclEvaluationOptions options);

	/**
	 * Convenience method that parses and evaluates an expression in one step.
	 *
	 * @param expression the OCL expression text
	 * @param context the {@code EObject} that serves as {@code self}; its
	 *        {@code eClass()} is used as the context type for parsing
	 * @return the evaluation result
	 * @throws OclParseException if the expression contains syntax or type errors
	 */
	Object evaluate(String expression, EObject context) throws OclParseException;

	// --- Evaluation with extent (for allInstances()) ---

	/**
	 * Evaluates a pre-parsed expression with a model extent for {@code allInstances()} support.
	 *
	 * @param expression the parsed OCL expression
	 * @param context the {@code EObject} that serves as {@code self}
	 * @param extent the model extent providing access to all instances
	 * @return the evaluation result
	 */
	Object evaluate(OclExpression expression, EObject context, OclModelExtent extent);

	/**
	 * Evaluates a pre-parsed expression with a model extent and explicit options.
	 *
	 * @param expression the parsed OCL expression
	 * @param context the {@code EObject} that serves as {@code self}
	 * @param extent the model extent providing access to all instances
	 * @param options evaluation options
	 * @return the evaluation result
	 */
	Object evaluate(OclExpression expression, EObject context, OclModelExtent extent,
			OclEvaluationOptions options);

	// --- Validation ---

	/**
	 * Validates a parsed expression for type correctness and well-formedness.
	 *
	 * @param expression the parsed OCL expression to validate
	 * @param contextType the {@code EClassifier} that defines the type of {@code self}
	 * @return a list of diagnostics; empty if the expression is valid
	 */
	List<Diagnostic> validate(OclExpression expression, EClassifier contextType);

	// --- Extension: Custom Operations ---

	/**
	 * Registers a provider of custom OCL operations (non-OSGi usage).
	 *
	 * <p>In an OSGi environment, providers are discovered automatically via the
	 * whiteboard pattern ({@code @Component(service = OclOperationProvider.class)}).
	 *
	 * @param provider the operation provider to register
	 */
	void registerOperations(OclOperationProvider provider);

	/**
	 * Unregisters a previously registered operation provider.
	 *
	 * @param provider the operation provider to unregister
	 */
	void unregisterOperations(OclOperationProvider provider);

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
}
