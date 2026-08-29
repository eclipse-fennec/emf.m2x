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

import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
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
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;

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

	/**
	 * Invokes an implementedby operational implementation (§7.11.3.6).
	 * Searches the relation's {@code operationalImpl} list for an implementation
	 * matching the target direction and delegates first to registered
	 * {@link RelationImplementationProvider}s (D39, Phase 4b), then falls back
	 * to the blackbox registry.
	 *
	 * @return {@code true} if an implementation was found and invoked
	 */
	public boolean invokeImplementedBy(Relation relation, TypedModel targetModel,
			Map<String, Object> bindings) {
		for (RelationImplementation impl : relation.getOperationalImpl()) {
			TypedModel direction = impl.getInDirectionOf();
			if (direction != null && targetModel.getName().equals(direction.getName())) {
				EOperation op = impl.getImpl();
				if (op == null) {
					continue;
				}
				String opName = op.getName();

				// §7.8 / D39: Try RelationImplementationProviders first (QVT-O hybrid)
				String qualifiedName = relation.getName();
				for (RelationImplementationProvider provider : implementationProviders) {
					if (provider.canProvide(qualifiedName)) {
						QvtdExecutionResult result = provider.executeRelation(qualifiedName, context);
						if (result.isSuccess()) {
							return true;
						}
						diagnostics.addAll(result.diagnostics());
						return true; // invoked, even if errors
					}
				}

				// Fallback: blackbox registry (M-R5: enforce config gate)
				if (blackboxRegistry() == null || !config.blackboxEnabled()) {
					continue;
				}
				QvtdBlackboxLibrary library = libraryFor(opName);
				if (library == null) {
					continue;
				}

				// Evaluate argument expressions from annotation
				Object[] argValues = new Object[0];
				EAnnotation ann = impl.getEAnnotation("qvtr.implementedby.args");
				if (ann != null) {
					List<EObject> argExprs = ann.getReferences();
					argValues = new Object[argExprs.size()];
					for (int i = 0; i < argExprs.size(); i++) {
						if (argExprs.get(i) instanceof OclExpression argExpr) {
							argValues[i] = oclCallback.evaluate(argExpr, bindings);
						}
					}
				}

				// One library, chosen by what it declares — a failure of that library is a
				// failure of the transformation, not a reason to ask the next one (#180)
				try {
					Object result = library.invoke(opName, null, argValues);
					if (result instanceof EObject created && created.eContainer() == null) {
						extentManager.getExtent(targetModel).add(created);
					}
					return true;
				} catch (RuntimeException failure) {
					diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE, 0,
							"Blackbox operation '" + opName + "' of library '" + library.getModuleName()
									+ "' failed: " + failure, new Object[] { failure }));
					return true; // invoked, and it failed — reported, not retried elsewhere
				}
			}
		}
		return false;
	}

	/**
	 * Evaluates a blackbox query by delegating to the registered blackbox
	 * library (§7.8). Searches all registered libraries for an operation
	 * matching the query name.
	 */
	public Object evaluateBlackboxQuery(Function query, OperationCallExp callExpr,
			Map<String, Object> callerBindings) {
		if (blackboxRegistry() == null || !config.blackboxEnabled()) {
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
	public boolean isBlackboxAllowed(String moduleName) {
		Set<String> allowed = config.allowedBlackboxModules();
		return allowed.isEmpty() || allowed.contains(moduleName);
	}
}
