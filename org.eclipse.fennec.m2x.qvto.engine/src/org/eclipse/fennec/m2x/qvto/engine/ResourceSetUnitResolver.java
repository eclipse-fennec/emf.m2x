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
package org.eclipse.fennec.m2x.qvto.engine;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Objects;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;

/**
 * Resolves imported compilation units through a {@link ResourceSet}.
 *
 * <p>An imported unit named {@code my.company.Helpers} is looked for at
 * {@code <base>/my/company/Helpers.qvto}, loaded through the resource set's
 * {@link ResourceSet#getURIConverter() URIConverter}. Whatever that resource set can
 * reach, this resolver can reach — files, bundle resources, archives, or anything a
 * custom {@code URIHandler} serves.
 *
 * <pre>
 * QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
 *     .addUnitResolver(new ResourceSetUnitResolver(resourceSet, URI.createURI("platform:/plugin/com.acme.transforms/")))
 *     .unitResolverEnabled(true)
 *     .build();
 * </pre>
 *
 * <p>Unit resolution stays behind its opt-in: the engine only consults resolvers when
 * {@code unitResolverEnabled} is set, and an allow-list of module names still applies
 * (D29). This class loads what it is asked for; it decides nothing about trust.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class ResourceSetUnitResolver implements QvtoUnitResolver {

	/** Default file extension of a QVT-O compilation unit. */
	public static final String DEFAULT_EXTENSION = "qvto";

	private final ResourceSet resourceSet;
	private final URI baseUri;
	private final String extension;
	private final Charset charset;

	/**
	 * Creates a resolver reading UTF-8 {@code .qvto} units below the given base URI.
	 *
	 * @param resourceSet the resource set whose URI converter reads the units
	 * @param baseUri the base URI the qualified name is resolved against
	 */
	public ResourceSetUnitResolver(ResourceSet resourceSet, URI baseUri) {
		this(resourceSet, baseUri, DEFAULT_EXTENSION, StandardCharsets.UTF_8);
	}

	/**
	 * Creates a resolver with an explicit file extension and charset.
	 *
	 * @param resourceSet the resource set whose URI converter reads the units
	 * @param baseUri the base URI the qualified name is resolved against
	 * @param extension the file extension of a unit, without the dot
	 * @param charset the charset the unit sources are encoded in
	 */
	public ResourceSetUnitResolver(ResourceSet resourceSet, URI baseUri, String extension,
			Charset charset) {
		this.resourceSet = Objects.requireNonNull(resourceSet, "resourceSet must not be null");
		this.baseUri = Objects.requireNonNull(baseUri, "baseUri must not be null");
		this.extension = Objects.requireNonNull(extension, "extension must not be null");
		this.charset = Objects.requireNonNull(charset, "charset must not be null");
	}

	@Override
	public Optional<QvtoUnit> resolveUnit(String qualifiedName) {
		if (qualifiedName == null || qualifiedName.isBlank()) {
			return Optional.empty();
		}
		// A base written with a trailing slash carries an empty last segment; appending
		// to it would produce a doubled separator.
		URI base = baseUri.hasTrailingPathSeparator() ? baseUri.trimSegments(1) : baseUri;
		URI unitUri = base.appendSegments(qualifiedName.split("\\."))
				.appendFileExtension(extension);
		try (InputStream in = resourceSet.getURIConverter().createInputStream(unitUri)) {
			String source = new String(in.readAllBytes(), charset);
			return Optional.of(new QvtoUnit.SourceUnit(qualifiedName,
					java.net.URI.create(unitUri.toString()), source));
		} catch (IOException | RuntimeException e) {
			// Not found through this resolver — the engine asks the next one
			return Optional.empty();
		}
	}
}
