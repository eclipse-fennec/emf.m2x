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

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.fennec.m2x.model.qvtoperational.ResolveExp;

/**
 * Captures a deferred (late) resolve task to be executed after all mappings complete.
 *
 * <p>The source is evaluated eagerly at the point of the {@code late resolve(...)}
 * expression. The actual resolve and assignment happen during the deferred phase.
 *
 * @param resolveExp the resolve expression AST node
 * @param targetObject the l-value EObject to assign the resolved value to
 * @param targetFeature the structural feature to set on the target object
 * @param isReset whether this is a reset assignment ({@code :=}) vs append ({@code +=})
 * @param capturedSource the eagerly evaluated source object for the resolve
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
record DeferredResolveTask(
		ResolveExp resolveExp,
		EObject targetObject,
		EStructuralFeature targetFeature,
		boolean isReset,
		Object capturedSource) {
}
