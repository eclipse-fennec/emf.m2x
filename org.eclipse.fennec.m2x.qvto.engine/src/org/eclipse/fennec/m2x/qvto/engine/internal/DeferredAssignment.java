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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.model.imperativeocl.AssignExp;
import org.eclipse.fennec.m2x.model.qvtoperational.ResolveExp;

/**
 * A deferred assignment (QVT v1.3 §8.2.2.11): an assignment whose right-hand side involves a
 * {@code late} resolve, performed as a whole once the entry operation has finished.
 *
 * <p>What is captured when the assignment is reached is what §8.2.1.22 lists: the property to
 * assign, and the source object of each late resolve. The right-hand side itself is evaluated
 * at the end, against the full trace and with the variables that were visible at that point.
 *
 * @param assignment the assignment
 * @param target the object whose property is assigned
 * @param feature the property
 * @param variables the variables visible where the assignment was reached
 * @param resolveSources the source object of each late resolve whose source could be
 *        evaluated there — not one that depends on a variable of the right-hand side
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
record DeferredAssignment(
		AssignExp assignment,
		EObject target,
		EStructuralFeature feature,
		Map<String, Object> variables,
		Map<ResolveExp, Object> resolveSources) {
}
