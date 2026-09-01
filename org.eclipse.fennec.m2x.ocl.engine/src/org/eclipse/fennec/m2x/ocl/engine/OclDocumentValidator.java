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
package org.eclipse.fennec.m2x.ocl.engine;

import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.DiagnosticChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EValidator;
import org.eclipse.fennec.m2x.model.ocl.Constraint;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;

/**
 * Validates objects against the {@code inv:} constraints of the Complete OCL documents an
 * engine has registered — option (c) of #204: the delegates read annotations, this validator
 * reads registrations, and mutating a shared EPackage is required by neither.
 *
 * <p>Plain Java: hand it to a {@code Diagnostician} or call
 * {@link #validate(EObject, DiagnosticChain, Map)} directly. Under EMF's registry discipline it
 * is the caller who decides where to file it ({@code EValidator.Registry}) — registering is a
 * deployment decision, not this class's.
 *
 * <p>What a document registration put into effect, its removal takes back out — this validator
 * always answers from the engine's current registrations.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclDocumentValidator implements EValidator {

	/** The diagnostic source of every finding this validator files. */
	public static final String DIAGNOSTIC_SOURCE = "org.eclipse.fennec.m2x.ocl.document";

	private final OclEngine engine;

	/**
	 * Creates a validator over an engine's document registrations.
	 *
	 * @param engine the engine whose registered documents answer
	 */
	public OclDocumentValidator(OclEngine engine) {
		this.engine = Objects.requireNonNull(engine, "engine must not be null");
	}

	@Override
	public boolean validate(EObject eObject, DiagnosticChain diagnostics, Map<Object, Object> context) {
		return validate(eObject.eClass(), eObject, diagnostics, context);
	}

	@Override
	public boolean validate(EClass eClass, EObject eObject, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		boolean valid = true;
		Set<EClassifier> classifiers = new LinkedHashSet<>();
		classifiers.add(eClass);
		classifiers.addAll(eClass.getEAllSuperTypes());
		for (EClassifier classifier : classifiers) {
			for (Constraint invariant : engine.documentInvariants(classifier)) {
				valid &= check(invariant, eObject, diagnostics);
			}
		}
		return valid;
	}

	@Override
	public boolean validate(EDataType eDataType, Object value, DiagnosticChain diagnostics,
			Map<Object, Object> context) {
		// documents state invariants on classes; there is nothing registered for a data type
		return true;
	}

	private boolean check(Constraint invariant, EObject eObject, DiagnosticChain diagnostics) {
		String name = invariant.getName() == null ? "<unnamed>" : invariant.getName();
		Object result;
		try {
			result = engine.evaluate(invariant.getSpecification(), OclContext.of(eObject));
		} catch (RuntimeException failure) {
			if (diagnostics != null) {
				diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0,
						"The '" + name + "' constraint could not be evaluated: " + failure.getMessage(),
						new Object[] { eObject }));
			}
			return false;
		}
		if (Boolean.TRUE.equals(result)) {
			return true;
		}
		if (diagnostics != null) {
			diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, DIAGNOSTIC_SOURCE, 0,
					"The '" + name + "' constraint is violated on '" + eObject.eClass().getName() + "'",
					new Object[] { eObject }));
		}
		return false;
	}
}
