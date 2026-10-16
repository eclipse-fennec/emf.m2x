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
package org.eclipse.fennec.m2x.qvtd.engine.internal;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.qvtbase.Function;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationImplementation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary;
import org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxRegistry;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationResult;

/**
 * Handles blackbox library invocation and {@code implementedby} delegation
 * (§7.8, §7.11.3.6).
 *
 * <p>Extracted from {@link QvtrEvaluator} for testability and separation
 * of concerns. Security enforcement (M-R5) is centralized here.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtrBlackboxBridge {

	private final QvtdConfiguration config;
	private final List<RelationImplementationProvider> implementationProviders;
	private final QvtrExtentManager extentManager;
	private final QvtdExecutionContext context;
	private final QvtrDiagnosticSink diagnostics;
	private final QvtrOclCallback oclCallback;
	/** Diagnostic source id, as used by the rest of the QVT-R engine. */
	private static final String SOURCE = QvtrDiagnosticSink.SOURCE;
	/** The blackbox modules this run has reached into, for the D29 ceiling. */
	private final Set<String> usedLibraries = new HashSet<>();
	/** Queries already reported as unserved this run, so each is reported once (#283). */
	private final Set<String> unservedReported = new HashSet<>();

	/**
	 * Creates a bridge for one run.
	 *
	 * <p>The blackbox registry is the configuration's — it used to be a parameter of its own,
	 * which allowed a caller to gate one registry with the flags of another (#185).
	 *
	 * @param config the configuration, and with it the registry and the D29 controls
	 * @param implementationProviders the providers for {@code implementedby} relations
	 * @param extentManager the extents of the run
	 * @param context the execution context
	 * @param diagnostics where a failure is reported
	 * @param oclCallback how an OCL expression of the transformation is evaluated
	 */
	public QvtrBlackboxBridge(QvtdConfiguration config,
			List<RelationImplementationProvider> implementationProviders,
			QvtrExtentManager extentManager, QvtdExecutionContext context,
			QvtrDiagnosticSink diagnostics, QvtrOclCallback oclCallback) {
		this.config = Objects.requireNonNull(config, "config must not be null");
		this.implementationProviders = implementationProviders;
		this.extentManager = extentManager;
		this.context = context;
		this.diagnostics = Objects.requireNonNull(diagnostics, "diagnostics must not be null");
		this.oclCallback = oclCallback;
	}

	/** The registry of this run, or {@code null} when blackboxes are off or none are configured. */
	private QvtdBlackboxRegistry blackboxRegistry() {
		return config.blackboxRegistry();
	}

	/** What became of a relation's {@code implementedby} clause for the enforced direction. */
	enum Implementation {
		/** The relation has no {@code implementedby} for this direction; QVT-R enforces it. */
		NONE,
		/** The operation was invoked; whether the relation now holds is the caller's check. */
		INVOKED,
		/** The operation could not be invoked or failed; that has been reported. */
		FAILED
	}

	/**
	 * Invokes the operation a relation's {@code implementedby} clause names for the enforced
	 * direction (QVT v1.3 §7.11.3.6).
	 *
	 * <p>The operation is the one the clause names — {@code RelationImplementation.impl} — asked
	 * for by that name: first from the registered {@link RelationImplementationProvider}s (D39),
	 * then from the blackbox libraries. Before #282 the providers were asked for the relation's
	 * name, so a QVT-O mapping named after the operation was never called. The arguments are the
	 * clause's, evaluated in the current bindings — the root objects of the relation's domains
	 * (§10, rule 4.5). What the operation returns, if it is an object nobody contains yet, is
	 * added to the target extent.
	 *
	 * <p>An operation nobody serves is an error, not a reason for QVT-R to enforce the domain
	 * itself: {@code impl} has multiplicity {@code [1]}, so a clause without an operation behind it
	 * has no reading (#282).
	 *
	 * @return what happened; the caller checks the relation after {@link Implementation#INVOKED}
	 */
	public Implementation invokeImplementedBy(Relation relation, TypedModel targetModel,
			Map<String, Object> bindings) {
		for (RelationImplementation impl : relation.getOperationalImpl()) {
			TypedModel direction = impl.getInDirectionOf();
			if (direction == null || !targetModel.getName().equals(direction.getName())
					|| impl.getImpl() == null) {
				continue;
			}
			String opName = impl.getImpl().getName();
			List<Object> arguments = evaluateArguments(impl, bindings);

			// §7.8 / D39: a RelationImplementationProvider (QVT-O) first
			for (RelationImplementationProvider provider : implementationProviders) {
				if (provider.canProvide(opName)) {
					RelationImplementationResult result = provider.invoke(opName, arguments, context);
					diagnostics.addAll(result.diagnostics());
					if (result.diagnostics().stream().anyMatch(d -> d.getSeverity() >= Diagnostic.ERROR)) {
						return Implementation.FAILED;
					}
					place(result.value(), targetModel);
					return Implementation.INVOKED;
				}
			}

			// Then a blackbox library (M-R5: the configuration gate applies)
			QvtdBlackboxLibrary library = libraryFor(opName);
			if (library == null) {
				String reason = unservedReason(opName);
				if (reason != null) { // null: the library ceiling, reported where it was hit
					diagnostics.error("Operation '" + opName + "' of the implementedby clause of relation '"
							+ relation.getName() + "' cannot be served: " + reason);
				}
				return Implementation.FAILED;
			}
			// One library, chosen by what it declares — a failure of that library is a failure of
			// the transformation, not a reason to ask the next one (#180)
			try {
				place(library.invoke(opName, null, arguments.toArray()), targetModel);
				return Implementation.INVOKED;
			} catch (RuntimeException failure) {
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE, 0,
						"Blackbox operation '" + opName + "' of library '" + library.getModuleName()
								+ "' failed: " + failure, new Object[] { failure }));
				return Implementation.FAILED;
			}
		}
		return Implementation.NONE;
	}

	/** The clause's argument expressions, evaluated in the current bindings. */
	private List<Object> evaluateArguments(RelationImplementation impl, Map<String, Object> bindings) {
		List<Object> values = new ArrayList<>();
		EAnnotation ann = impl.getEAnnotation("qvtr.implementedby.args");
		if (ann != null) {
			for (EObject argExpr : ann.getReferences()) {
				values.add(argExpr instanceof OclExpression expr ? oclCallback.evaluate(expr, bindings) : null);
			}
		}
		return values;
	}

	/** Adds what an operation returned to the target extent, if it is a new root. */
	private void place(Object value, TypedModel targetModel) {
		if (value instanceof EObject created && created.eContainer() == null
				&& !extentManager.getExtent(targetModel).getContents().contains(created)) {
			extentManager.getExtent(targetModel).add(created);
		}
	}

	/**
	 * Evaluates a blackbox query by delegating to the registered blackbox
	 * library (§7.8). Searches all registered libraries for an operation
	 * matching the query name.
	 */
	public Object evaluateBlackboxQuery(Function query, OperationCallExp callExpr,
			Map<String, Object> callerBindings) {
		if (blackboxRegistry() == null || !config.blackboxEnabled()) {
			reportUnserved(query.getName());
			return null;
		}

		// Evaluate arguments
		List<OclExpression> args = callExpr.getOwnedArguments();
		Object[] argValues = new Object[args.size()];
		for (int i = 0; i < args.size(); i++) {
			argValues[i] = oclCallback.evaluate(args.get(i), callerBindings);
		}

		// The library that declares the query, gated by the allow-list and the ceiling — before
		// #180 this loop invoked every registered library and swallowed every exception, so the
		// allow-list did not apply to queries at all
		String queryName = query.getName();
		QvtdBlackboxLibrary library = libraryFor(queryName);
		if (library == null) {
			reportUnserved(queryName);
			return null;
		}
		try {
			return library.invoke(queryName, null, argValues);
		} catch (RuntimeException failure) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE, 0,
					"Blackbox query '" + queryName + "' of library '" + library.getModuleName()
							+ "' failed: " + failure, new Object[] { failure }));
			return null;
		}
	}

	/**
	 * The library that serves an operation: one that declares the name, whose module the
	 * allow-list permits, and that fits under the library ceiling (§M-R5, D29).
	 *
	 * <p>Everything the configuration says is decided here, before anything is invoked. The
	 * allow-list names <em>modules</em> — {@code QvtdConfiguration.allowedBlackboxModules} — and
	 * is compared against {@link QvtdBlackboxLibrary#getModuleName()}; before #180 it was compared
	 * against the operation name, so it could only ever match by coincidence, and queries did not
	 * consult it at all.
	 *
	 * @param operationName the operation to serve
	 * @return the library, or {@code null} if none may serve it
	 */
	QvtdBlackboxLibrary libraryFor(String operationName) {
		if (blackboxRegistry() == null || !config.blackboxEnabled()) {
			return null;
		}
		for (QvtdBlackboxLibrary library : blackboxRegistry().getLibraries()) {
			if (!library.getOperationNames().contains(operationName)) {
				continue;
			}
			if (!isBlackboxAllowed(library.getModuleName())) {
				continue;
			}
			// D29: a ceiling on how many distinct libraries one run may reach into, counted over
			// the run rather than per call, so a chain of queries cannot walk past it one at a time
			String moduleName = library.getModuleName();
			if (!usedLibraries.contains(moduleName) && usedLibraries.size() >= config.maxBlackboxLibraries()) {
				diagnostics.error("Blackbox library '" + moduleName + "' is past the limit of "
						+ config.maxBlackboxLibraries() + " libraries per run");
				return null;
			}
			usedLibraries.add(moduleName);
			return library;
		}
		return null;
	}

	/**
	 * Whether the allow-list permits a blackbox <em>module</em> (M-R5). An empty list restricts
	 * nothing; a non-empty one names the modules that may be reached.
	 *
	 * @param moduleName the library's module name
	 * @return {@code true} if the module may be used
	 */
	/**
	 * Reports a blackbox query that is called but cannot be served, once per query and run (#283).
	 *
	 * <p>The query still evaluates to {@code null}, but no longer in silence: before #283 the
	 * caller saw only the consequence — an unbound variable, a failed predicate — or, in an
	 * enforced template, nothing at all while {@code null} was written into the target. A library
	 * that would serve the query but is past the library ceiling has been reported by
	 * {@link #libraryFor(String)} already.
	 *
	 * @param queryName the query that was called
	 */
	private void reportUnserved(String queryName) {
		String reason = unservedReason(queryName);
		if (reason != null && unservedReported.add(queryName)) {
			diagnostics.error("Blackbox query '" + queryName + "' cannot be served: " + reason);
		}
	}

	/**
	 * Why no blackbox library serves an operation, or {@code null} if one would but is past the
	 * library ceiling — reported where the ceiling was hit.
	 */
	private String unservedReason(String operationName) {
		if (!config.blackboxEnabled()) {
			return implementationProviders.isEmpty() ? "blackboxes are not enabled"
					: "no provider serves it, and blackboxes are not enabled";
		}
		if (blackboxRegistry() == null) {
			return "no blackbox registry is configured";
		}
		List<QvtdBlackboxLibrary> serving = blackboxRegistry().getLibraries().stream()
				.filter(library -> library.getOperationNames().contains(operationName))
				.toList();
		if (serving.isEmpty()) {
			return "no registered library serves it";
		}
		if (serving.stream().anyMatch(library -> isBlackboxAllowed(library.getModuleName()))) {
			return null;
		}
		return "the allow-list names none of the libraries that serve it ("
				+ serving.stream().map(QvtdBlackboxLibrary::getModuleName).sorted()
						.collect(Collectors.joining(", "))
				+ ")";
	}

	public boolean isBlackboxAllowed(String moduleName) {
		Set<String> allowed = config.allowedBlackboxModules();
		return allowed.isEmpty() || allowed.contains(moduleName);
	}
}
