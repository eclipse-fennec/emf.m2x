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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;

/**
 * What the EMF delegate factories need from an engine, beyond {@link OclEngine}.
 *
 * <p>The delegates used to take {@code OclEngineImpl}, which meant the component had to
 * publish the implementation class as a service type so they could bind to it. Nothing
 * outside this bundle could use that type — it is not exported — so it was a service type
 * that existed purely to let one internal class find another.
 *
 * <p>This is that role instead: one method the public API has no reason to carry, since
 * {@code @pre} snapshots exist only for postcondition delegates.
 *
 * @since 1.0
 */
public interface OclDelegateSupport extends OclEngine {

	/**
	 * Evaluates a postcondition against the pre-state snapshot taken before the operation
	 * ran, which is what makes {@code @pre} resolvable (OCL §7.5.7).
	 *
	 * @param expression the postcondition
	 * @param context    the evaluation context
	 * @param snapshot   the values captured before the operation, must not be {@code null}
	 * @return the result of the postcondition
	 */
	Object evaluatePostcondition(OclExpression expression, OclContext context,
			PreStateSnapshot snapshot);
}
