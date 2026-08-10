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
package org.eclipse.fennec.m2x.ocl.metadata;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.logging.Level;
import java.util.logging.Logger;

import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.ocl.api.OclDelegates;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionParser;
import org.eclipse.fennec.m2x.ocl.api.OclParseException;

/**
 * Compiles the OCL a model carries, once, when the model registers.
 *
 * <p>A model states its own constraints — invariants on a class, the derivation of a derived
 * feature, the body of an operation — and until something asks for one of them, each is
 * nothing but a string in an annotation. The first request then pays for parsing, which
 * makes the first validation of a model measurably slower than the second for no reason
 * anyone chose.
 *
 * <p>Registration is when the work belongs: the model has just arrived, its version is known,
 * and everything it declares can be compiled and filed under that version. From then on the
 * delegates find compiled expressions instead of strings, and a version that goes takes its
 * compiled OCL with it.
 *
 * <p><b>Driven by the model, not by the metadata handler.</b> The trigger is the arrival of an
 * {@code EPackage} — in OSGi its service, in plain Java a call — and deliberately not
 * {@code MetadataHandler.onPackageRegistered}. A version's metadata tree is published only
 * after every handler has run, and handlers run in registration order, so content pushed from
 * inside a handler reaches that tree or misses it depending on which handler was wired first.
 * Reacting to the model itself has no such dependency: whatever is filed is placed as soon as
 * a tree exists, before or after.
 *
 * <p>A constraint that does not parse is logged and skipped. A model with one broken
 * invariant still registers — refusing it here would turn a validation error into a model
 * that cannot be loaded at all, which is not this handler's decision to make. The delegate
 * will report it when someone asks for it.
 *
 * @since 1.0
 */
public class OclConstraintPrecompiler {

	private static final Logger LOG = Logger.getLogger(OclConstraintPrecompiler.class.getName());

	private final OclExpressionParser parser;
	private final OclExpressionCache cache;

	/**
	 * @param parser what compiles the expressions, must not be {@code null}
	 * @param cache  where the compiled expressions go, must not be {@code null} — the
	 *               registry-backed one when there is a registry to file them in
	 */
	public OclConstraintPrecompiler(OclExpressionParser parser, OclExpressionCache cache) {
		this.parser = Objects.requireNonNull(parser, "parser must not be null");
		this.cache = Objects.requireNonNull(cache, "cache must not be null");
	}

	/**
	 * Compiles everything the given model version declares.
	 *
	 * @param ePackage the model version to compile against — its own instance, because that is
	 *     what the compiled expressions will hold; {@code null} is ignored
	 */
	public void compile(EPackage ePackage) {
		if (ePackage == null) {
			return;
		}
		for (EClassifier classifier : ePackage.getEClassifiers()) {
			if (classifier instanceof EClass eClass) {
				precompile(eClass);
			}
		}
	}

	private void precompile(EClass eClass) {
		for (String expression : invariantsOf(eClass)) {
			compile(expression, eClass);
		}
		for (EStructuralFeature feature : eClass.getEStructuralFeatures()) {
			String derivation = detail(feature, OclDelegates.DERIVATION_KEY);
			if (derivation != null) {
				compile(derivation, eClass);
			}
		}
		for (EOperation operation : eClass.getEOperations()) {
			String body = detail(operation, OclDelegates.BODY_KEY);
			if (body != null) {
				compile(body, eClass);
			}
		}
	}

	/**
	 * The invariant bodies of a class: Ecore's own annotation names them, and the delegate
	 * annotation carries one body per name.
	 */
	private List<String> invariantsOf(EClass eClass) {
		EAnnotation names = eClass.getEAnnotation(OclDelegates.ECORE_URI);
		if (names == null) {
			return List.of();
		}
		String constraints = names.getDetails().get(OclDelegates.CONSTRAINTS_KEY);
		if (constraints == null || constraints.isBlank()) {
			return List.of();
		}
		List<String> bodies = new ArrayList<>();
		for (String name : constraints.trim().split("\\s+")) {
			String body = detail(eClass, name);
			if (body != null) {
				bodies.add(body);
			}
		}
		return bodies;
	}

	/** The detail of the Fennec source, or of the legacy Pivot one that is also served. */
	private static String detail(EModelElement element, String key) {
		for (String source : List.of(OclDelegates.DELEGATE_URI, OclDelegates.LEGACY_PIVOT_URI)) {
			EAnnotation annotation = element.getEAnnotation(source);
			if (annotation != null) {
				String value = annotation.getDetails().get(key);
				if (value != null) {
					return value;
				}
			}
		}
		return null;
	}

	private void compile(String expression, EClass contextType) {
		if (cache.get(expression, contextType) != null) {
			return;
		}
		try {
			cache.put(expression, contextType, parser.parse(expression, contextType));
		} catch (OclParseException | RuntimeException failure) {
			// One unusable constraint must not keep a model from registering; the delegate
			// reports it when someone asks for it.
			LOG.log(Level.WARNING, failure, () -> "Cannot precompile OCL on "
					+ contextType.getName() + ": " + expression);
		}
	}
}
