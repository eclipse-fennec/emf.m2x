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
package org.eclipse.fennec.m2x.qvtd.example;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;

/**
 * The example's files, loaded the plain-EMF way: a {@link ResourceSetImpl}, the XMI factory,
 * and the metamodels registered so the instance model resolves. Nothing here is specific to
 * the engines — this is what any EMF program does before a transformation enters the picture.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class ExampleFiles {

	private ExampleFiles() {}

	/**
	 * The project directory the example's files are addressed from — the working directory,
	 * which is where both the IDE and the build run this example.
	 *
	 * @return the base directory
	 */
	public static Path baseDir() {
		Path base = Path.of("");
		if (!Files.exists(base.resolve("model/library.ecore"))) {
			throw new IllegalStateException("run from org.eclipse.fennec.m2x.qvtd.example/ — "
					+ "the example addresses model/ and transforms/ relative to the working directory, "
					+ "which is " + base.toAbsolutePath());
		}
		return base;
	}

	/**
	 * Loads a metamodel from an {@code .ecore} file.
	 *
	 * @param ecore the file
	 * @return the package, never {@code null}
	 */
	public static EPackage loadEcore(Path ecore) {
		Resource resource = resourceSet().getResource(URI.createFileURI(ecore.toAbsolutePath().toString()), true);
		return (EPackage) resource.getContents().get(0);
	}

	/**
	 * Loads an instance model whose metamodels are the given packages.
	 *
	 * @param xmi the file
	 * @param packages the metamodels the instance refers to by nsURI
	 * @return the root object, never {@code null}
	 */
	public static EObject loadInstance(Path xmi, EPackage... packages) {
		ResourceSet resourceSet = resourceSet();
		for (EPackage ePackage : packages) {
			resourceSet.getPackageRegistry().put(ePackage.getNsURI(), ePackage);
		}
		Resource resource = resourceSet.getResource(URI.createFileURI(xmi.toAbsolutePath().toString()), true);
		return resource.getContents().get(0);
	}

	/**
	 * Reads a transformation source.
	 *
	 * @param qvto the file
	 * @return the source text, never {@code null}
	 */
	public static String read(Path qvto) {
		try {
			return Files.readString(qvto);
		} catch (IOException e) {
			throw new IllegalStateException("cannot read " + qvto + ": " + e.getMessage(), e);
		}
	}

	private static ResourceSet resourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put(Resource.Factory.Registry.DEFAULT_EXTENSION, new XMIResourceFactoryImpl());
		return resourceSet;
	}
}
