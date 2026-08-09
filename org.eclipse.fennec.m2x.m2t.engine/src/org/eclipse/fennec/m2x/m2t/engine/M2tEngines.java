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
package org.eclipse.fennec.m2x.m2t.engine;

import java.util.Objects;

import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.m2t.engine.internal.M2tEngineImpl;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;

/**
 * Creates {@link M2tEngine} instances outside OSGi.
 *
 * <p>In OSGi the engine arrives as a service and Declarative Services wires it;
 * nothing here is needed. On a flat classpath this factory is where the wiring lives —
 * the implementation class sits in a private package, so a caller cannot depend on it
 * by accident.
 *
 * <pre>
 * OclEngine ocl = OclEngines.create(new OclParserSupport());
 * M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(ocl).build());
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class M2tEngines {

	private M2tEngines() {
		// static factory
	}

	/**
	 * Creates an engine for MOFM2T templates from the given configuration.
	 *
	 * @param configuration the configuration, must not be {@code null}
	 * @return a new engine
	 */
	public static M2tEngine create(M2tConfiguration configuration) {
		Objects.requireNonNull(configuration, "configuration must not be null");
		return new M2tEngineImpl(configuration);
	}
}
