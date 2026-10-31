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
package org.eclipse.fennec.m2x.ocl.parser.typing;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.IfExp;
import org.eclipse.fennec.m2x.model.ocl.IteratorExp;
import org.eclipse.fennec.m2x.model.ocl.MapType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * Checks the types of an expression that has already been built (OCL v2.4 §8.3.7, GAP-3, #378):
 * what {@code OclEngine.validate} answers for an expression that may not have come from the
 * parser — built by hand, loaded from a stored unit.
 *
 * <p>The parser applies the same rules while it builds and reports them as parse errors, so an
 * expression it produced passes. Unknown types pass as well: typing is gradual.
 */
public final class OclTypeChecker {

	/** The source of the diagnostics this checker reports. */
	public static final String SOURCE = "org.eclipse.fennec.m2x.ocl.parser.typing";

	/** The iterators whose body is a condition (§11.9). */
	private static final Set<String> BOOLEAN_BODY = Set.of("select", "reject", "forAll", "exists", "any", "one");

	private final OclLibrarySignatures signatures;

	/**
	 * Creates a checker for the given library.
	 *
	 * @param signatures the library operations of the language, must not be {@code null}
	 */
	public OclTypeChecker(OclLibrarySignatures signatures) {
		this.signatures = Objects.requireNonNull(signatures, "signatures must not be null");
	}

	/**
	 * Checks an expression and everything it contains.
	 *
	 * @param expression the expression, must not be {@code null}
	 * @return one ERROR per type that is known and does not fit, empty if there is none
	 */
	public List<Diagnostic> check(OclExpression expression) {
		Objects.requireNonNull(expression, "expression must not be null");
		List<Diagnostic> diagnostics = new ArrayList<>();
		List<EObject> nodes = new ArrayList<>();
		nodes.add(expression);
		expression.eAllContents().forEachRemaining(nodes::add);
		for (EObject node : nodes) {
			String problem = switch (node) {
				case OperationCallExp call -> checkOperationCall(call);
				case IfExp ifExp -> checkCondition(ifExp);
				case IteratorExp iterator -> checkIteratorBody(iterator);
				case Variable variable -> checkInitialValue(variable);
				default -> null;
			};
			if (problem != null) {
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE, 0, problem, new Object[] { node }));
			}
		}
		return diagnostics;
	}

	private String checkOperationCall(OperationCallExp call) {
		if (call.getReferredOperation() instanceof EOperation operation) {
			List<EParameter> parameters = operation.getEParameters();
			if (parameters.size() != call.getOwnedArguments().size()) {
				return "Operation " + operation.getName() + " expects " + parameters.size() + " argument(s), got "
						+ call.getOwnedArguments().size();
			}
			for (int i = 0; i < parameters.size(); i++) {
				EClassifier argument = call.getOwnedArguments().get(i).getType();
				EClassifier parameter = typeOf(parameters.get(i));
				if (!OclTypeRules.conformsTo(argument, parameter)) {
					return "Argument " + (i + 1) + " of " + operation.getName() + " is a " + OclTypeRules.nameOf(argument)
							+ ", but the parameter '" + parameters.get(i).getName() + "' is a "
							+ OclTypeRules.nameOf(parameter);
				}
			}
			return null;
		}
		if (call.getName() == null) {
			return null;
		}
		EClassifier receiver = call.getOwnedSource() != null ? call.getOwnedSource().getType() : null;
		EClassifier normalized = OclTypeRules.normalize(receiver);
		if (call.isIsArrow() && normalized != null && !(normalized instanceof CollectionType)
				&& !(normalized instanceof MapType)) {
			receiver = OclTypeRules.collectionType(CollectionKind.SET, receiver);
		}
		return signatures.resolve(receiver, call.getName(), call.getOwnedArguments()).problem();
	}

	private static EClassifier typeOf(EParameter parameter) {
		EClassifier type = parameter.getEType();
		if (type == null || !parameter.isMany()) {
			return type;
		}
		CollectionKind kind = parameter.isOrdered()
				? parameter.isUnique() ? CollectionKind.ORDERED_SET : CollectionKind.SEQUENCE
				: parameter.isUnique() ? CollectionKind.SET : CollectionKind.BAG;
		return OclTypeRules.collectionType(kind, type);
	}

	private static String checkCondition(IfExp ifExp) {
		EClassifier condition = ifExp.getOwnedCondition() != null ? ifExp.getOwnedCondition().getType() : null;
		return OclTypeRules.conformsTo(condition, OclStandardLibrary.INSTANCE.booleanType()) ? null
				: "The condition of an if is a " + OclTypeRules.nameOf(condition) + ", not a Boolean";
	}

	private static String checkIteratorBody(IteratorExp iterator) {
		if (!BOOLEAN_BODY.contains(iterator.getName()) || iterator.getOwnedBody() == null) {
			return null;
		}
		EClassifier body = iterator.getOwnedBody().getType();
		return OclTypeRules.conformsTo(body, OclStandardLibrary.INSTANCE.booleanType()) ? null
				: "The body of " + iterator.getName() + " is a " + OclTypeRules.nameOf(body) + ", not a Boolean";
	}

	private static String checkInitialValue(Variable variable) {
		OclExpression init = variable.getOwnedInit();
		if (init == null || variable.getType() == null || OclTypeRules.conformsTo(init.getType(), variable.getType())) {
			return null;
		}
		return "The variable '" + variable.getName() + "' is declared as " + OclTypeRules.nameOf(variable.getType())
				+ " but initialized with a " + OclTypeRules.nameOf(init.getType());
	}
}
