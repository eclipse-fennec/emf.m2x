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
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.Map;

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
	 * <p>A close that fails is reported, not swallowed. A buffered writer does its last —
	 * and largest — write inside {@code close()}, so a disk that is full or a quota that is
	 * exhausted shows up here and nowhere else; silence would let the generation report
	 * success over a truncated file. The engine flushes before it closes and turns whatever
	 * comes out of this method into a generation error for that file.
	 *
	 * @param filePath the file path
	 * @param writer the writer to close
	 * @throws UncheckedIOException if the writer could not be closed
	 */
	default void closeWriter(String filePath, Writer writer) {
		try {
			writer.close();
		} catch (IOException e) {
			throw new UncheckedIOException("Cannot close " + filePath, e);
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

	/**
	 * Receives the user code of protected areas the generation no longer produces.
	 *
	 * <p>MOFM2T §7.2: protected text parts "are preserved and not overwritten by subsequent
	 * model-to-text transformations". An area whose id the templates no longer generate — the
	 * element was renamed, the area moved — would otherwise vanish with the next write; the
	 * engine hands its content here and reports a WARNING (#367). Only areas the user changed
	 * arrive; an untouched default is not code worth keeping. The default does nothing, as an
	 * in-memory generation has nowhere to keep it; {@code FileSystemGenerationStrategy} appends
	 * it to {@code <file>.lost}, as Acceleo does.
	 *
	 * @param filePath the file the areas were in
	 * @param areas the content of each area, by id, in the order of the file
	 * @param charset the charset of the file
	 */
	default void lostContent(String filePath, Map<String, String> areas, Charset charset) {
	}

	/**
	 * The path a file of the given unique id was generated to before.
	 *
	 * <p>MOFM2T §7.3: "a transformation tool can use this id to find a file that was generated
	 * in a previous session even when a modelelement was renamed … This will enable protected
	 * text parts, if any, to be preserved across transformations." When nothing exists under a
	 * file's new path, the engine merges the protected areas of the file this answers, and
	 * reports that as INFO; the old file is not deleted (#368). The default knows no earlier
	 * session.
	 *
	 * @param uniqueId the unique id of a {@code [file]} block
	 * @return the earlier path, or {@code null}
	 */
	default String pathOfUniqueId(String uniqueId) {
		return null;
	}

	/**
	 * Remembers where the files of a generation went, for {@link #pathOfUniqueId(String)} in a
	 * later one. {@code FileSystemGenerationStrategy} keeps them in {@code .m2t-ids} in its
	 * output directory.
	 *
	 * @param pathsByUniqueId the path of each written file, by its unique id
	 */
	default void recordUniqueIds(Map<String, String> pathsByUniqueId) {
	}
}
