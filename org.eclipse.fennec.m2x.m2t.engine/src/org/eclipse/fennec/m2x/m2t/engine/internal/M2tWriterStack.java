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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.nio.charset.Charset;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;

import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;

/**
 * Writer stack for managing nested text output during MOFM2T template evaluation.
 *
 * <p>The stack supports:
 * <ul>
 *   <li>Default string output (stdout) — always at the bottom</li>
 *   <li>File output — pushed when entering a {@code [file]} block</li>
 * </ul>
 *
 * <p>Text is always written to the topmost writer. When a file writer is popped,
 * the generated content is stored in the generated files map.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tWriterStack {

	private final Deque<WriterEntry> stack = new ArrayDeque<>();
	private final Map<String, String> generatedFiles = new LinkedHashMap<>();
	private final Map<String, String> fileUniqueIds = new LinkedHashMap<>();
	private final Map<String, OpenModeKind> fileOpenModes = new LinkedHashMap<>();
	private final Map<String, Charset> fileCharsets = new LinkedHashMap<>();
	private final long maxOutputSize;
	private long totalOutputSize;
	private boolean outputLimitExceeded;

	/**
	 * Creates a new writer stack with a default string writer for stdout.
	 *
	 * @param maxOutputSize maximum total output size in characters (T-7 protection),
	 *        or {@code 0} for unlimited
	 */
	public M2tWriterStack(long maxOutputSize) {
		this.maxOutputSize = maxOutputSize;
		stack.push(new WriterEntry(null, new StringBuilder()));
	}

	/**
	 * Creates a new writer stack with unlimited output size.
	 */
	public M2tWriterStack() {
		this(0);
	}

	/**
	 * Pushes a new string writer for in-memory output.
	 */
	public void pushStringWriter() {
		stack.push(new WriterEntry(null, new StringBuilder()));
	}

	/**
	 * Pushes a new file writer for file output.
	 *
	 * @param filePath the output file path
	 * @param mode overwrite or append
	 * @param charset the output charset
	 */
	public void pushFileWriter(String filePath, OpenModeKind mode, Charset charset) {
		Objects.requireNonNull(filePath, "filePath must not be null");
		StringBuilder sb = new StringBuilder();
		if (mode == OpenModeKind.APPEND && generatedFiles.containsKey(filePath)) {
			sb.append(generatedFiles.get(filePath));
		}
		// Only the first block for a path decides how the file is opened and encoded;
		// further APPEND blocks for the same path are concatenated in memory above.
		fileOpenModes.putIfAbsent(filePath, mode == null ? OpenModeKind.OVERWRITE : mode);
		if (charset != null) {
			fileCharsets.putIfAbsent(filePath, charset);
		}
		stack.push(new WriterEntry(filePath, sb));
	}

	/**
	 * Pops the topmost writer and returns its content.
	 * If it was a file writer, the content is stored in the generated files map.
	 *
	 * @return the content of the popped writer
	 * @throws IllegalStateException if only the default writer remains
	 */
	public String popWriter() {
		if (stack.size() <= 1) {
			throw new IllegalStateException("Cannot pop the default writer");
		}
		WriterEntry entry = stack.pop();
		String content = entry.buffer.toString();
		if (entry.filePath != null) {
			generatedFiles.put(entry.filePath, content);
		}
		return content;
	}

	/**
	 * Appends text to the current (topmost) writer.
	 *
	 * @param text the text to append
	 * @return {@code true} if text was appended, {@code false} if the output size
	 *         limit was exceeded (T-7)
	 */
	public boolean append(String text) {
		if (text == null) {
			return true;
		}
		if (outputLimitExceeded) {
			return false;
		}
		if (maxOutputSize > 0) {
			totalOutputSize += text.length();
			if (totalOutputSize > maxOutputSize) {
				outputLimitExceeded = true;
				return false;
			}
		}
		stack.peek().buffer.append(text);
		return true;
	}

	/**
	 * Returns {@code true} if the output size limit has been exceeded.
	 */
	public boolean isOutputLimitExceeded() {
		return outputLimitExceeded;
	}

	/**
	 * Returns the content of the default (stdout) writer.
	 *
	 * @return the stdout content
	 */
	public String getStdout() {
		// The bottom of the stack is the default writer
		WriterEntry[] entries = stack.toArray(WriterEntry[]::new);
		return entries[entries.length - 1].buffer.toString();
	}

	/**
	 * Returns all generated files (path → content).
	 *
	 * @return unmodifiable map of generated files
	 */
	public Map<String, String> getGeneratedFiles() {
		return Map.copyOf(generatedFiles);
	}

	/**
	 * Associates a unique identifier with a file path (MOFM2T §8.1.17).
	 * Used to track renamed files across regenerations for protected area preservation.
	 *
	 * @param filePath the file path
	 * @param uniqId the unique identifier
	 */
	public void setFileUniqueId(String filePath, String uniqId) {
		if (filePath != null && uniqId != null) {
			fileUniqueIds.put(filePath, uniqId);
		}
	}

	/**
	 * Returns the file unique ID mappings (filePath → uniqId).
	 *
	 * @return unmodifiable map of unique IDs
	 */
	public Map<String, String> getFileUniqueIds() {
		return Map.copyOf(fileUniqueIds);
	}

	/**
	 * Returns the open mode each generated file was first opened with
	 * (file path &rarr; mode).
	 *
	 * <p>Within one execution, APPEND blocks targeting the same path are already
	 * concatenated into a single entry of {@link #getGeneratedFiles()}. This map tells
	 * a {@code M2tGenerationStrategy} whether the accumulated content replaces an
	 * existing file on disk or extends it.
	 *
	 * @return unmodifiable map of open modes
	 */
	public Map<String, OpenModeKind> getFileOpenModes() {
		return Map.copyOf(fileOpenModes);
	}

	/**
	 * Returns the charset each generated file was first opened with
	 * (file path &rarr; charset), for files that declared one.
	 *
	 * @return unmodifiable map of charsets
	 */
	public Map<String, Charset> getFileCharsets() {
		return Map.copyOf(fileCharsets);
	}

	/**
	 * Returns the current stack depth.
	 */
	public int depth() {
		return stack.size();
	}

	/**
	 * The document currently being written, or {@code null} when output is not going into a
	 * {@code [file]} block.
	 *
	 * <p>For diagnostics: a warning that names the template and the document it was producing is
	 * something an author can act on, while the same warning on its own is not.
	 *
	 * @return the file path of the innermost file writer, or {@code null}
	 */
	public String currentTargetName() {
		for (WriterEntry entry : stack) {
			if (entry.filePath() != null) {
				return entry.filePath();
			}
		}
		return null;
	}

	private record WriterEntry(String filePath, StringBuilder buffer) {
	}
}
