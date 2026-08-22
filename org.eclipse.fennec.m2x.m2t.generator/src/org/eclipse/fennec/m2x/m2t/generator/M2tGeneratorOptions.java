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
package org.eclipse.fennec.m2x.m2t.generator;

import java.io.File;
import java.util.Optional;

import aQute.bnd.service.generate.Options;

/**
 * The command line of the {@code fennecM2T} generator.
 *
 * <p>The generation itself is configured through the attributes of the
 * {@code -generate} instruction, not here — see {@link M2tGenerator}. What remains on
 * the command line is the output directory, and only because bnd hands it to a
 * generator this way too:
 *
 * <pre>
 * -generate: model/&#42;.ecore; \
 *     output = gen-src/; \
 *     generate = "fennecM2T --output gen-src"
 * </pre>
 *
 * <p>The {@code output} attribute is the one to use; this option exists so that a
 * command line stays possible where the attribute cannot be repeated.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public interface M2tGeneratorOptions extends Options {

	/**
	 * The directory generated files are written below, relative to the project.
	 *
	 * @return the output directory, or empty when the {@code output} attribute decides
	 */
	Optional<File> output();
}
