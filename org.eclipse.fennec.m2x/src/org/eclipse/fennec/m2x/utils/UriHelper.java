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
package org.eclipse.fennec.m2x.utils;

import java.io.File;
import java.nio.file.Path;
import java.util.Objects;

import org.eclipse.emf.common.util.URI;

/**
 * Converts the URI forms a caller is likely to hold into the {@link URI EMF URI} the
 * M2X engines speak.
 *
 * <p>The engines take EMF URIs, because everything around them is EMF: the resource
 * set, the URI converter that reads the sources, the resolvers, the models. Callers
 * rarely start there — they hold a {@link Path} from a directory walk, a
 * {@link java.net.URI} from an HTTP layer, or a {@link File} from a dialog. This class
 * is where that translation lives, so it is done once and correctly.
 *
 * <pre>
 * engine.parse(UriHelper.fromPath(Path.of("/srv/templates/report.mtl")));
 * engine.parse(UriHelper.fromJavaUri(request.getUri()));
 * </pre>
 *
 * <p><b>Why not just {@code URI.createURI(something.toString())}:</b> that is right for
 * a URI and wrong for a file path. A path is not a URI — on Windows
 * {@code C:\\templates\\report.mtl} has a drive letter and backslashes, and
 * {@code createURI} would keep them verbatim, producing a URI whose scheme is {@code C}.
 * {@link #fromPath(Path)} and {@link #fromFile(File)} use {@code createFileURI}, which
 * is the one that knows about this.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class UriHelper {

	private UriHelper() {
		// static helper
	}

	/**
	 * Converts a {@link java.net.URI} into an EMF URI.
	 *
	 * <p>The string form of a {@code java.net.URI} is already encoded, which is what
	 * {@link URI#createURI(String)} expects, so nothing is lost on the way.
	 *
	 * @param uri the URI to convert, must not be {@code null}
	 * @return the same URI as an EMF URI
	 */
	public static URI fromJavaUri(java.net.URI uri) {
		Objects.requireNonNull(uri, "uri must not be null");
		return URI.createURI(uri.toString());
	}

	/**
	 * Converts a file system path into a {@code file:} EMF URI.
	 *
	 * <p>The path is made absolute first: a relative path would otherwise produce a URI
	 * that resolves against whatever the current working directory happens to be.
	 *
	 * @param path the path to convert, must not be {@code null}
	 * @return the path as a {@code file:} URI
	 */
	public static URI fromPath(Path path) {
		Objects.requireNonNull(path, "path must not be null");
		return URI.createFileURI(path.toAbsolutePath().toString());
	}

	/**
	 * Converts a file into a {@code file:} EMF URI.
	 *
	 * @param file the file to convert, must not be {@code null}
	 * @return the file as a {@code file:} URI
	 */
	public static URI fromFile(File file) {
		Objects.requireNonNull(file, "file must not be null");
		return fromPath(file.toPath());
	}

	/**
	 * Converts an EMF URI back into a {@link java.net.URI}.
	 *
	 * <p>For handing a URI that came out of the engines — the location of a resolved
	 * unit, for instance — to an API that speaks {@code java.net.URI}.
	 *
	 * @param uri the URI to convert, must not be {@code null}
	 * @return the same URI as a {@code java.net.URI}
	 * @throws IllegalArgumentException if the URI cannot be expressed as a
	 *         {@code java.net.URI} — EMF accepts forms that {@code java.net.URI} rejects
	 */
	public static java.net.URI toJavaUri(URI uri) {
		Objects.requireNonNull(uri, "uri must not be null");
		return java.net.URI.create(uri.toString());
	}
}
