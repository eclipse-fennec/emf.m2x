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
@org.osgi.annotation.bundle.Export
@org.osgi.annotation.versioning.Version("1.0")
@org.osgi.annotation.bundle.Capability(
		namespace = "org.eclipse.fennec.m2x",
		name = "ocl.parser",
		attribute = {
				"provider=eclipse-fennec",
				"implementation=antlr4",
				"version:Version=1.0"
		})
package org.eclipse.fennec.m2x.ocl.parser;
