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

import java.io.IOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.fennec.m2x.m2t.api.M2tGenerationStrategy;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;

/**
 * What happens to a generation's files after the template has run: the protected-area merge,
 * and the write.
 *
 * <p>Its own class because it is its own job, with its own rules, and because those rules are
 * where several of the findings of #177 and #184 live — they belong somewhere a reader can
 * find them rather than at the end of {@code execute}:
 *
 * <ul>
 * <li>the merge comes first and completely: the merger needs the whole generated and the whole
 * existing content before it can decide anything, so output cannot be streamed through the
 * strategy while the template is being evaluated;</li>
 * <li>an {@code APPEND} file is extended rather than regenerated, so merging its existing
 * content in and then appending the result would duplicate it;</li>
 * <li>every failure is per file: one unwritable path does not cost a generation its other nine
 * files, and a file whose existing content could not even be read is not written either —
 * the failure was reported once, and writing would report it again;</li>
 * <li>a buffered writer does its largest write inside {@code close()}, so the flush happens
 * where a failure can still be reported, and a close that fails is reported rather than
 * swallowed.</li>
 * </ul>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class M2tOutputWriter {

	private final M2tGenerationStrategy strategy;
	private final boolean protectedAreasEnabled;
	private final Charset defaultCharset;
	private final M2tEvaluator diagnostics;

	/**
	 * @param strategy where the files go, must not be {@code null}
	 * @param protectedAreasEnabled whether existing protected areas are merged back in (D31)
	 * @param defaultCharset the charset for a file that declared none
	 * @param diagnostics where a per-file failure is reported
	 */
	M2tOutputWriter(M2tGenerationStrategy strategy, boolean protectedAreasEnabled,
			Charset defaultCharset, M2tEvaluator diagnostics) {
		this.strategy = Objects.requireNonNull(strategy, "strategy must not be null");
		this.protectedAreasEnabled = protectedAreasEnabled;
		this.defaultCharset = Objects.requireNonNull(defaultCharset, "defaultCharset must not be null");
		this.diagnostics = Objects.requireNonNull(diagnostics, "diagnostics must not be null");
	}

	/**
	 * Merges and writes, and answers what the result should carry.
	 *
	 * @param generated what the templates produced, by file path
	 * @param openModes the open mode each file was opened with
	 * @param charsets the charset each file declared
	 * @return the content that was written — without the files that could not be
	 */
	Map<String, String> write(Map<String, String> generated, Map<String, OpenModeKind> openModes,
			Map<String, Charset> charsets) {
		Map<String, String> files = protectedAreasEnabled
				? merged(generated, openModes, charsets)
				: generated;
		for (Map.Entry<String, String> entry : files.entrySet()) {
			writeOne(entry.getKey(), entry.getValue(),
					openModes.getOrDefault(entry.getKey(), OpenModeKind.OVERWRITE),
					charsetFor(entry.getKey(), charsets));
		}
		return files;
	}

	private Map<String, String> merged(Map<String, String> generated,
			Map<String, OpenModeKind> openModes, Map<String, Charset> charsets) {
		M2tProtectedAreaMerger merger = new M2tProtectedAreaMerger();
		Map<String, String> files = new LinkedHashMap<>(generated);
		Set<String> unreadable = new HashSet<>();
		for (Map.Entry<String, String> entry : files.entrySet()) {
			if (openModes.get(entry.getKey()) == OpenModeKind.APPEND) {
				continue;
			}
			try {
				String existing = strategy.readExistingContent(entry.getKey(),
						charsetFor(entry.getKey(), charsets));
				if (existing != null) {
					entry.setValue(merger.merge(entry.getValue(), existing));
				}
			} catch (RuntimeException failure) {
				diagnostics.addGenerationError(entry.getKey(), failure);
				unreadable.add(entry.getKey());
			}
		}
		files.keySet().removeAll(unreadable);
		return Map.copyOf(files);
	}

	private void writeOne(String filePath, String content, OpenModeKind mode, Charset charset) {
		try {
			Writer writer = strategy.createWriter(filePath, mode, charset);
			if (writer == null) {
				return;
			}
			boolean written = false;
			try {
				writer.write(content);
				// Inside the try that reports: a buffered writer writes most of the file here
				writer.flush();
				written = true;
			} finally {
				try {
					strategy.closeWriter(filePath, writer);
				} catch (RuntimeException closeFailure) {
					// Only when the write itself succeeded — otherwise the write's own failure
					// is the one worth reporting and this would replace it
					if (written) {
						throw closeFailure;
					}
				}
			}
		} catch (IOException | RuntimeException e) {
			diagnostics.addGenerationError(filePath, e);
		}
	}

	private Charset charsetFor(String filePath, Map<String, Charset> charsets) {
		Charset declared = charsets.get(filePath);
		return declared != null ? declared : defaultCharset;
	}
}
