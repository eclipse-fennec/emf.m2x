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
import java.io.Reader;
import java.io.UncheckedIOException;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.LinkOption;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.Map;
import java.util.Objects;
import java.util.Properties;

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

	/** Where the unique ids of the generated files are kept, in the output directory (#368). */
	public static final String UNIQUE_IDS = ".m2t-ids";

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

	/**
	 * {@inheritDoc}
	 *
	 * <p>Appended to {@code <file>.lost} next to the file, one block per area, so that a later
	 * loss does not overwrite an earlier one.
	 */
	@Override
	public void lostContent(String filePath, Map<String, String> areas, Charset charset) {
		Path target = resolve(filePath + ".lost");
		StringBuilder text = new StringBuilder();
		areas.forEach((id, content) -> text.append("[protected '").append(id).append("']\n").append(content)
				.append(content.endsWith("\n") ? "" : "\n").append("[/protected]\n"));
		try {
			Files.writeString(target, text, charset, StandardOpenOption.CREATE, StandardOpenOption.WRITE,
					StandardOpenOption.APPEND, LinkOption.NOFOLLOW_LINKS);
		} catch (IOException e) {
			throw new UncheckedIOException("Cannot keep the lost content of " + filePath, e);
		}
	}

	@Override
	public String pathOfUniqueId(String uniqueId) {
		return uniqueIds().getProperty(uniqueId);
	}

	@Override
	public void recordUniqueIds(Map<String, String> pathsByUniqueId) {
		if (pathsByUniqueId.isEmpty()) {
			return;
		}
		Properties ids = uniqueIds();
		pathsByUniqueId.forEach(ids::setProperty);
		try (Writer writer = Files.newBufferedWriter(resolve(UNIQUE_IDS), StandardCharsets.UTF_8)) {
			ids.store(writer, "MOFM2T [file] unique ids (§7.3): uniqId=path");
		} catch (IOException e) {
			throw new UncheckedIOException("Cannot write " + UNIQUE_IDS, e);
		}
	}

	/** The unique ids of earlier generations, from {@code .m2t-ids}; empty when there is none. */
	private Properties uniqueIds() {
		Properties ids = new Properties();
		Path index = resolve(UNIQUE_IDS);
		if (Files.isRegularFile(index, LinkOption.NOFOLLOW_LINKS)) {
			try (Reader reader = Files.newBufferedReader(index, StandardCharsets.UTF_8)) {
				ids.load(reader);
			} catch (IOException e) {
				throw new UncheckedIOException("Cannot read " + UNIQUE_IDS, e);
			}
		}
		return ids;
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
		Path target = outputDirectory.resolve(withoutFileScheme(filePath)).toAbsolutePath().normalize();
		requireContained(target, filePath);
		return target;
	}

	/**
	 * A {@code file:} URI as a path (MOFM2T §7.3 writes {@code 'file:\\' + c.name + '.java'}, #368):
	 * without an authority — {@code file:x}, {@code file:/x}, {@code file:\x} — it is relative
	 * to the output directory; with one — {@code file:///abs/x} — it is the absolute path, which
	 * must lie inside the output directory like any other.
	 */
	private static String withoutFileScheme(String filePath) {
		if (!filePath.regionMatches(true, 0, "file:", 0, 5)) {
			return filePath;
		}
		String path = filePath.substring(5).replace('\\', '/');
		if (path.startsWith("//")) {
			// file://host/path or file:///path: the path after the authority, absolute
			int slash = path.indexOf('/', 2);
			return slash < 0 ? "/" : path.substring(slash);
		}
		return path.replaceFirst("^/+", "");
	}

	private void requireContained(Path resolved, String filePath) {
		if (!resolved.startsWith(outputDirectory)) {
			throw new SecurityException(
					"File path escapes the output directory: " + filePath);
		}
	}
}
