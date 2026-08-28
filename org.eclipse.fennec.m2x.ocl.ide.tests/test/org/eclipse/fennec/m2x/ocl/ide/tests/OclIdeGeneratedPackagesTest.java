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
package org.eclipse.fennec.m2x.ocl.ide.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.InputStream;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilderFactory;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;
import org.eclipse.fennec.m2x.ocl.ide.OclIdePackages;
import org.junit.jupiter.api.Test;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.NodeList;

/**
 * The IDE bundle declares the OCL metamodel and the standard library as EPackages (#157).
 *
 * <p>An Eclipse IDE fills {@code EPackage.Registry.INSTANCE} from {@code plugin.xml}: the
 * {@code generated_package} extension names a class, and the registry loads it and takes its
 * static {@code eINSTANCE}. The registry itself cannot run here; what can is exactly the contract
 * it relies on — each declared class is loadable, carries {@code eINSTANCE}, and that package has
 * the declared nsURI — plus the code-side list the file has to agree with.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclIdeGeneratedPackagesTest {

	private static final String EXTENSION_POINT = "org.eclipse.emf.ecore.generated_package";

	@Test
	void everyDeclaredPackage_isLoadableAndCarriesItsNsUri() throws Exception {
		Map<String, String> declared = declaredPackages();
		assertTrue(declared.size() >= 2, "plugin.xml declares at least the metamodel and the library");
		for (Map.Entry<String, String> entry : declared.entrySet()) {
			EPackage ePackage = eInstanceOf(entry.getValue());
			assertEquals(entry.getKey(), ePackage.getNsURI(),
					"the class named for " + entry.getKey() + " yields a package of that nsURI");
		}
	}

	@Test
	void pluginXml_andOclIdePackages_agree() throws Exception {
		List<String> inFile = new ArrayList<>(declaredPackages().keySet());
		List<String> inCode = OclIdePackages.DECLARED.stream().map(EPackage::getNsURI).toList();
		assertEquals(inCode, inFile, "plugin.xml and OclIdePackages.DECLARED list the same packages");
	}

	@Test
	void standardLibrary_isDeclared() throws Exception {
		assertTrue(declaredPackages().containsKey(OclStandardLibrary.NS_URI));
		assertSame(OclStandardLibrary.INSTANCE.ePackage(), eInstanceOf(OclStandardLibrary.class.getName()));
	}

	// What the registration is for: a reference into the library resolves in a fresh resource set
	// before any OCL has been parsed. In this JVM the registry entry comes from class
	// initialization; in the IDE it comes from plugin.xml — the resolution is the same.
	@Test
	void libraryReference_resolvesInAFreshResourceSet() {
		URI integer = URI.createURI(OclStandardLibrary.NS_URI + "#//Integer");
		EObject resolved = new ResourceSetImpl().getEObject(integer, true);
		assertSame(OclStandardLibrary.INSTANCE.integer(), resolved);
	}

	// ---- Helpers ----

	/** nsURI → class, in file order, from the plugin.xml the IDE bundle ships. */
	private static Map<String, String> declaredPackages() throws Exception {
		Map<String, String> declared = new LinkedHashMap<>();
		try (InputStream in = OclIdePackages.class.getResourceAsStream("/plugin.xml")) {
			assertNotNull(in, "plugin.xml is packaged at the root of the IDE bundle");
			Document document = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(in);
			NodeList extensions = document.getElementsByTagName("extension");
			for (int i = 0; i < extensions.getLength(); i++) {
				Element extension = (Element) extensions.item(i);
				if (!EXTENSION_POINT.equals(extension.getAttribute("point"))) {
					continue;
				}
				NodeList packages = extension.getElementsByTagName("package");
				for (int j = 0; j < packages.getLength(); j++) {
					Element pkg = (Element) packages.item(j);
					declared.put(pkg.getAttribute("uri"), pkg.getAttribute("class"));
				}
			}
		}
		return declared;
	}

	/** What EMF's registry reader does with the declared class. */
	private static EPackage eInstanceOf(String className) throws Exception {
		Class<?> type = Class.forName(className);
		Field field = type.getField("eINSTANCE");
		Object value = field.get(null);
		assertTrue(value instanceof EPackage, className + ".eINSTANCE is an EPackage");
		return (EPackage) value;
	}
}
