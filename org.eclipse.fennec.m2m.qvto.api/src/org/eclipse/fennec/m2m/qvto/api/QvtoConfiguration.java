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
package org.eclipse.fennec.m2m.qvto.api;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.eclipse.fennec.m2m.ocl.api.OclConfiguration;

/**
 * Immutable configuration for creating {@link QvtoEngine} instances.
 *
 * <p>Bundles the mandatory {@link OclConfiguration} for the underlying OCL
 * evaluator, plus optional blackbox libraries and unit resolvers. Use the
 * {@link Builder} to construct instances:
 * <pre>
 * QvtoConfiguration config = QvtoConfiguration.builder(oclConfig)
 *     .addBlackboxLibrary(myLibrary)
 *     .addUnitResolver(myResolver)
 *     .build();
 * </pre>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class QvtoConfiguration {

	private final OclConfiguration oclConfiguration;
	private final List<QvtoBlackboxLibrary> blackboxLibraries;
	private final List<QvtoUnitResolver> unitResolvers;

	private QvtoConfiguration(Builder builder) {
		this.oclConfiguration = builder.oclConfiguration;
		this.blackboxLibraries = Collections.unmodifiableList(new ArrayList<>(builder.blackboxLibraries));
		this.unitResolvers = Collections.unmodifiableList(new ArrayList<>(builder.unitResolvers));
	}

	public OclConfiguration oclConfiguration() {
		return oclConfiguration;
	}

	public List<QvtoBlackboxLibrary> blackboxLibraries() {
		return blackboxLibraries;
	}

	public List<QvtoUnitResolver> unitResolvers() {
		return unitResolvers;
	}

	public static Builder builder(OclConfiguration oclConfiguration) {
		return new Builder(oclConfiguration);
	}

	public static final class Builder {

		private final OclConfiguration oclConfiguration;
		private final List<QvtoBlackboxLibrary> blackboxLibraries = new ArrayList<>();
		private final List<QvtoUnitResolver> unitResolvers = new ArrayList<>();

		private Builder(OclConfiguration oclConfiguration) {
			this.oclConfiguration = Objects.requireNonNull(oclConfiguration, "oclConfiguration must not be null");
		}

		public Builder addBlackboxLibrary(QvtoBlackboxLibrary library) {
			this.blackboxLibraries.add(Objects.requireNonNull(library, "library must not be null"));
			return this;
		}

		public Builder blackboxLibraries(List<QvtoBlackboxLibrary> libraries) {
			this.blackboxLibraries.clear();
			this.blackboxLibraries.addAll(Objects.requireNonNull(libraries, "libraries must not be null"));
			return this;
		}

		public Builder addUnitResolver(QvtoUnitResolver resolver) {
			this.unitResolvers.add(Objects.requireNonNull(resolver, "resolver must not be null"));
			return this;
		}

		public Builder unitResolvers(List<QvtoUnitResolver> resolvers) {
			this.unitResolvers.clear();
			this.unitResolvers.addAll(Objects.requireNonNull(resolvers, "resolvers must not be null"));
			return this;
		}

		public QvtoConfiguration build() {
			return new QvtoConfiguration(this);
		}
	}
}
