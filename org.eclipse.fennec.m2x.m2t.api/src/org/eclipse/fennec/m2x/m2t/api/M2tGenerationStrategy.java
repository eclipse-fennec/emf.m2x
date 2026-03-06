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
package org.eclipse.fennec.m2x.m2t.api;

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;

import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.osgi.annotation.versioning.ConsumerType;

/**
 * SPI for controlling how generated text is written to files.
 *
 * <p>Implementations can direct output to the filesystem, in-memory buffers,
 * or any other target. The engine calls {@link #createWriter} for each
 * {@code [file]} block encountered during execution.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ConsumerType
public interface M2tGenerationStrategy {

	/**
	 * Creates a writer for the given file path.
	 *
	 * @param filePath the relative file path (from FileBlock URL expression)
	 * @param mode overwrite or append
	 * @param charset the charset for encoding
	 * @return a writer for the file content
	 */
	Writer createWriter(String filePath, OpenModeKind mode, Charset charset);

	/**
	 * Called when a writer is no longer needed (FileBlock ends).
	 * Default implementation closes the writer.
	 *
	 * @param filePath the file path
	 * @param writer the writer to close
	 */
	default void closeWriter(String filePath, Writer writer) {
		try {
			writer.close();
		} catch (IOException e) {
			// Close errors are non-fatal — file content was already flushed
		}
	}

	/**
	 * Reads existing file content for protected area merging.
	 * Returns {@code null} if the file does not exist.
	 *
	 * @param filePath the file path
	 * @param charset the charset for reading
	 * @return the existing file content, or {@code null}
	 */
	default String readExistingContent(String filePath, Charset charset) {
		return null;
	}
}
