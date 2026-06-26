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
package org.eclipse.fennec.m2x.ocl.ide;

import org.eclipse.fennec.m2x.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

/**
 * Lazily-created, process-wide OCL engine shared by the delegate registrations.
 *
 * <p>The Eclipse extension registry instantiates each registered delegate
 * (validation / setting / invocation) via its public no-argument constructor.
 * Rather than wire a fresh parser and engine per delegate, all three share the
 * single engine returned by {@link #get()}. The engine and its expression cache
 * are thread-safe, so a single instance is sufficient for the whole workbench.
 *
 * <p>This composition of parser + engine lives here (and not in the engine
 * bundle) so that the engine remains parser-agnostic; the IDE bundle is the
 * place that wires a concrete default stack.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class SharedOclEngine {

	private static volatile OclEngineImpl instance;

	private SharedOclEngine() {
		// no instances
	}

	/**
	 * Returns the shared OCL engine, creating it on first access.
	 *
	 * @return the shared {@link OclEngineImpl}
	 */
	static OclEngineImpl get() {
		OclEngineImpl result = instance;
		if (result == null) {
			synchronized (SharedOclEngine.class) {
				result = instance;
				if (result == null) {
					result = new OclEngineImpl(new OclParserSupport());
					instance = result;
				}
			}
		}
		return result;
	}
}
