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
package org.eclipse.fennec.m2x.qvtd.api.annotation.require;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import org.osgi.annotation.bundle.Requirement;

/**
 * Marks a bundle as requiring the Fennec QVT-R (Relations) engine capability.
 *
 * <p>Apply this annotation to a type or {@code package-info.java} to declare
 * an OSGi requirement on the {@code org.eclipse.fennec.m2x / qvtd.engine}
 * capability. The OSGi resolver will ensure that a bundle providing
 * this capability (i.e. the {@code qvtd.engine} bundle) is present at runtime.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Documented
@Retention(RetentionPolicy.CLASS)
@Target({ ElementType.TYPE, ElementType.PACKAGE })
@Requirement(namespace = "org.eclipse.fennec.m2x", name = "qvtd.engine")
public @interface RequireQVTD {
}
