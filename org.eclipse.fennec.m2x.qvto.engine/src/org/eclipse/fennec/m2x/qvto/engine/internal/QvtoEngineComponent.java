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

import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.engine.QvtoConfigurationHelper;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngineConfiguration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceScope;
import org.osgi.service.component.annotations.ServiceScope;
import org.osgi.service.metatype.annotations.Designate;

/**
 * Publishes the {@link QvtoEngine} as a prototype-scoped service.
 *
 * <p>The configuration policy is optional, so there is a working QVT-O engine without any
 * configuration at all, and configuring it does not mean registering a different one.
 *
 * <p>The OCL engine is a mandatory reference, not something this component builds. There is
 * always one to bind — {@code DefaultOclEngine} is optional-policy too — and taking it as a
 * service is what makes the engine a consumer configured, with its cache and its providers,
 * the engine that actually evaluates the transformation. It is bound prototype-required so
 * that every QVT-O engine gets its own, rather than sharing one accessor cache across
 * unrelated consumers.
 *
 * <p>OCL limits are therefore configured on {@code DefaultOclEngine} through {@code ocl.*};
 * this component's own configuration only carries QVT-O's settings:
 * <pre>
 * "DefaultQvtoEngine": { "qvto.blackboxEnabled": true },
 * "DefaultOclEngine":  { "ocl.maxDepth": 500 }
 * </pre>
 *
 * <p>Blackbox libraries and unit resolvers are deliberately absent: a transformation names
 * what it imports, so the engine resolves those by name when it parses rather than having
 * them pushed in here (#90).
 *
 * @since 1.0
 */
@Designate(ocd = QvtoEngineConfiguration.class)
@Component(name = "DefaultQvtoEngine", service = QvtoEngine.class, scope = ServiceScope.PROTOTYPE, configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class QvtoEngineComponent extends QvtoEngineImpl {

	@Activate
	public QvtoEngineComponent(
			QvtoEngineConfiguration config,
			@Reference(scope = ReferenceScope.PROTOTYPE_REQUIRED) OclEngine oclEngine) {
		super(QvtoConfigurationHelper.from(config, oclEngine));
	}
}
