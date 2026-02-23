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
package org.eclipse.fennec.m2m.qvto.engine.internal;

import java.util.Arrays;
import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;

/**
 * Lightweight trace record linking a mapping invocation's source to its result.
 *
 * <p>Used for fast resolve lookups at runtime. The EMF-based {@code Trace} model
 * is built in parallel by {@link QvtoTraceManager} for export purposes.
 *
 * @param mappingName the name of the mapping that produced this record
 * @param mappingOp the mapping operation AST node
 * @param source the source object passed to the mapping
 * @param args the in/inout parameter values (snapshot for inhibition matching)
 * @param result the result object created by the mapping
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
record QvtoTraceRecord(String mappingName, MappingOperation mappingOp, Object source, Object[] args, Object result) {

	/**
	 * Checks whether the given arguments match this record's args for inhibition.
	 * §8.1.11.2: Class instances compared by identity, DataType values by deep equality.
	 */
	boolean argsMatch(Object[] otherArgs) {
		if (args.length != otherArgs.length) {
			return false;
		}
		for (int i = 0; i < args.length; i++) {
			if (!valueMatches(args[i], otherArgs[i])) {
				return false;
			}
		}
		return true;
	}

	private static boolean valueMatches(Object a, Object b) {
		if (a == b) {
			return true;
		}
		if (a == null || b == null) {
			return false;
		}
		// §8.1.11.2: Class instances compared as objects (identity)
		if (a instanceof EObject || b instanceof EObject) {
			return a == b;
		}
		// §8.1.11.2: DataType values compared by deep value equality
		return Objects.equals(a, b);
	}
}
