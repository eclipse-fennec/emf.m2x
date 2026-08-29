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

import java.io.IOException;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Objects;

import org.eclipse.fennec.m2x.m2t.api.M2tGenerationStrategy;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;

/**
 * {@link M2tGenerationStrategy} that writes generated files below a directory on the
 * local file system.
 *
 * <pre>
 * M2tConfiguration config = M2tConfiguration.builder(oclConfig)
 *     .generationStrategy(new FileSystemGenerationStrategy(Path.of("/tmp/output")))
 *     .build();
 * </pre>
 *
 * <p>Missing parent directories are created on demand, and reading existing content is
 * supported so that protected areas (D31) survive regeneration.
 *
 * <p><b>Path containment (T-4).</b> A template controls its own {@code [file (...)]}
 * paths, so every path is resolved against the output directory and rejected if it
 * would escape — including via {@code ..} segments, an absolute path, or a symbolic
 * link inside the output directory pointing outside of it.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class FileSystemGenerationStrategy implements M2tGenerationStrategy {

	private final Path outputDirectory;

	/**
	 * Creates a strategy writing below the given directory.
	 *
	 * @param outputDirectory the base directory for generated files, must not be {@code null}
	 */
	public FileSystemGenerationStrategy(Path outputDirectory) {
		Objects.requireNonNull(outputDirectory, "outputDirectory must not be null");
		this.outputDirectory = outputDirectory.toAbsolutePath().normalize();
	}

	/**
	 * Returns the base directory for generated files.
	 *
	 * @return the output directory, absolute and normalized
	 */
	public Path getOutputDirectory() {
		return outputDirectory;
	}

	/**
	 * {@inheritDoc}
	 *
	 * <p>The file itself is opened with {@link LinkOption#NOFOLLOW_LINKS}: a symbolic link
	 * where the generated file goes is refused rather than followed, so a link planted in the
	 * output directory cannot make a template truncate a file outside of it. Checking for the
	 * link first and opening afterwards would leave the gap between the two open; the option
	 * makes the refusal part of the open itself.
	 */
	@Override
	public Writer createWriter(String filePath, OpenModeKind mode, Charset charset) {
		Path target = resolve(filePath);
		try {
			Path parent = target.getParent();
			if (parent != null) {
				Files.createDirectories(parent);
				requireContained(parent.toRealPath(), filePath);
			}
			if (mode == OpenModeKind.APPEND) {
				return Files.newBufferedWriter(target, charset, StandardOpenOption.CREATE,
						StandardOpenOption.WRITE, StandardOpenOption.APPEND,
						LinkOption.NOFOLLOW_LINKS);
			}
			return Files.newBufferedWriter(target, charset, StandardOpenOption.CREATE,
					StandardOpenOption.WRITE, StandardOpenOption.TRUNCATE_EXISTING,
					LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
			// The template only ever named the relative path, and the diagnostic travels
			// wherever the result does — the absolute output directory is not its business
			throw new UncheckedIOException("Cannot write " + filePath, e);
		}
	}

	@Override
	public String readExistingContent(String filePath, Charset charset) {
		Path target = resolve(filePath);
		if (!Files.isRegularFile(target, LinkOption.NOFOLLOW_LINKS)) {
			return null;
		}
		try {
			return Files.readString(target, charset);
		} catch (IOException e) {
			throw new UncheckedIOException("Cannot read existing content of " + filePath, e);
		}
	}

	/**
	 * Resolves a template-supplied path against the output directory and verifies that
	 * it stays inside it.
	 *
	 * @param filePath the path from the {@code [file (...)]} block
	 * @return the absolute, normalized target path
	 * @throws SecurityException if the path escapes the output directory
	 */
	private Path resolve(String filePath) {
		Objects.requireNonNull(filePath, "filePath must not be null");
		Path target = outputDirectory.resolve(filePath).toAbsolutePath().normalize();
		requireContained(target, filePath);
		return target;
	}

	private void requireContained(Path resolved, String filePath) {
		if (!resolved.startsWith(outputDirectory)) {
			throw new SecurityException(
					"File path escapes the output directory: " + filePath);
		}
	}
}
