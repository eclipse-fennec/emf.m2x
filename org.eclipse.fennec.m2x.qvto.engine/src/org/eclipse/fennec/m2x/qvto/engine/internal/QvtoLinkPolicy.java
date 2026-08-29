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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.List;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.parser.QvtoParserSupport;

/**
 * What a link may reach, and how far: the D29 extension controls of one engine.
 *
 * <p>Where an import is looked up, which blackbox modules are allowed, how many libraries one
 * link may pull in — decided once by the engine, from its configuration and its enable flags,
 * and then the same for compiling, for linking and for binding a prepared unit. Passed one
 * parameter at a time it was seven of them through three signatures, and every one of those
 * call sites could have got the order of the two {@code Set<String>} allow-lists wrong without
 * the compiler noticing (#185). Together they also read as what they are: a policy.
 *
 * @param parserSupport the parser a resolved source is parsed with
 * @param unitResolvers where an import may be looked up, in order, already capped
 * @param packageRegistry the metamodels a resolved unit may refer to
 * @param blackboxRegistry the blackbox libraries, or {@code null} if none apply
 * @param allowedBlackboxModules blackbox module names a link may use; empty means no restriction
 * @param allowedUnitModules unit names a link may resolve; empty means no restriction
 * @param maxBlackboxLibraries how many blackbox libraries one link may pull in
 * @author Data In Motion Consulting
 * @since 1.0
 */
public record QvtoLinkPolicy(
		QvtoParserSupport parserSupport,
		List<QvtoUnitResolver> unitResolvers,
		EPackage.Registry packageRegistry,
		QvtoBlackboxRegistry blackboxRegistry,
		Set<String> allowedBlackboxModules,
		Set<String> allowedUnitModules,
		int maxBlackboxLibraries) {

	public QvtoLinkPolicy {
		Objects.requireNonNull(parserSupport, "parserSupport must not be null");
		Objects.requireNonNull(unitResolvers, "unitResolvers must not be null");
		Objects.requireNonNull(packageRegistry, "packageRegistry must not be null");
		Objects.requireNonNull(allowedBlackboxModules, "allowedBlackboxModules must not be null");
		Objects.requireNonNull(allowedUnitModules, "allowedUnitModules must not be null");
		unitResolvers = List.copyOf(unitResolvers);
		allowedBlackboxModules = Set.copyOf(allowedBlackboxModules);
		allowedUnitModules = Set.copyOf(allowedUnitModules);
	}

	/**
	 * The same policy with no unit resolvers — binding a prepared unit resolves nothing: what
	 * the unit needed was decided when it was compiled.
	 *
	 * @return a policy that resolves no imports
	 */
	public QvtoLinkPolicy withoutUnitResolvers() {
		return new QvtoLinkPolicy(parserSupport, List.of(), packageRegistry, blackboxRegistry,
				allowedBlackboxModules, Set.of(), maxBlackboxLibraries);
	}
}
