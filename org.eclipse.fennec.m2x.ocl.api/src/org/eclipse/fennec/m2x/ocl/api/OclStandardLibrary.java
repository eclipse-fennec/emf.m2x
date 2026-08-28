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
package org.eclipse.fennec.m2x.ocl.api;

import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Optional;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.InvalidType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.model.ocl.VoidType;

/**
 * The OCL standard library: one instance of every predefined type, in an ordinary
 * {@link EPackage}.
 *
 * <p>Types are like classes, not like values — there is one {@code Integer} in the language
 * however many numbers a transformation contains (OCL v2.4 §8.2). Until this class existed the
 * parser created a fresh {@code PrimitiveType} at every use site, and so did the evaluator, the
 * MOFM2T standard library and the QVT-O operation provider: four sets of {@code Integer}, none
 * of them contained anywhere, every one of them a satellite in a compiled unit (#154).
 *
 * <p>This is the counterpart of Eclipse OCL's {@code oclstdlib.ecore}, and it works the same
 * way because #154 made {@code OclType} an {@code EClassifier}: the library is a plain EPackage
 * in the {@link EPackage.Registry}, so a reference to {@code Integer} resolves from any resource
 * set — as a reference to {@code EString} does — and serializes as {@code nsURI#//Integer}, the
 * form the unit fingerprint wants (#138). In a compiled unit the predefined types are external
 * references, not satellites.
 *
 * <p>The package is built once, on first use, and registered in the global registry. In an
 * Eclipse IDE the registry is fed from {@code plugin.xml} instead; the IDE bundle has to declare
 * this package there as well (see the ocl.ide bundle).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class OclStandardLibrary {

	/** The namespace URI of the standard library package. Changing it breaks stored units. */
	public static final String NS_URI = "http://www.eclipse.org/fennec/m2x/ocl/stdlib/1.0";

	/** The package name and namespace prefix. */
	public static final String NAME = "oclstdlib";

	/** The one library. */
	public static final OclStandardLibrary INSTANCE = new OclStandardLibrary();

	private final EPackage ePackage;
	private final Map<String, OclType> typesByName;
	private final PrimitiveType integer;
	private final PrimitiveType real;
	private final PrimitiveType string;
	private final PrimitiveType booleanType;
	private final PrimitiveType unlimitedNatural;
	private final AnyType oclAny;
	private final VoidType oclVoid;
	private final InvalidType oclInvalid;

	private OclStandardLibrary() {
		// The library's types are instances of the OCL metamodel; initializing that metamodel
		// here puts it into the global registry deterministically. Before, it got there as a side
		// effect of whichever code created the first OCL object — and a parse that resolved a
		// name through the registry before that point saw a registry without it.
		OclPackage.eINSTANCE.getNsURI();
		EPackage existing = EPackage.Registry.INSTANCE.getEPackage(NS_URI);
		if (existing != null) {
			// Registered before this class was loaded — an IDE's plugin.xml, or a second class
			// loader. Whatever is registered is the library; a second copy would defeat the
			// purpose.
			ePackage = existing;
		} else {
			ePackage = EcoreFactory.eINSTANCE.createEPackage();
			ePackage.setName(NAME);
			ePackage.setNsPrefix(NAME);
			ePackage.setNsURI(NS_URI);
			OclFactory factory = OclFactory.eINSTANCE;
			ePackage.getEClassifiers().add(primitive(factory, "Boolean", Boolean.class));
			ePackage.getEClassifiers().add(primitive(factory, "Integer", Long.class));
			ePackage.getEClassifiers().add(primitive(factory, "Real", Double.class));
			ePackage.getEClassifiers().add(primitive(factory, "String", String.class));
			ePackage.getEClassifiers().add(primitive(factory, "UnlimitedNatural", Long.class));
			AnyType any = factory.createAnyType();
			any.setName("OclAny");
			ePackage.getEClassifiers().add(any);
			VoidType voidType = factory.createVoidType();
			voidType.setName("OclVoid");
			ePackage.getEClassifiers().add(voidType);
			InvalidType invalidType = factory.createInvalidType();
			invalidType.setName("OclInvalid");
			ePackage.getEClassifiers().add(invalidType);
			// A resource whose URI is the nsURI, as a generated package has: that is what a
			// resource set's delegated lookup hands back for a proxy pointing here.
			Resource resource = new ResourceImpl(URI.createURI(NS_URI));
			resource.getContents().add(ePackage);
			EPackage.Registry.INSTANCE.put(NS_URI, ePackage);
		}
		Map<String, OclType> byName = new LinkedHashMap<>();
		ePackage.getEClassifiers().forEach(classifier -> {
			if (classifier instanceof OclType type) {
				byName.put(type.getName(), type);
			}
		});
		typesByName = Collections.unmodifiableMap(byName);
		integer = (PrimitiveType) byName.get("Integer");
		real = (PrimitiveType) byName.get("Real");
		string = (PrimitiveType) byName.get("String");
		booleanType = (PrimitiveType) byName.get("Boolean");
		unlimitedNatural = (PrimitiveType) byName.get("UnlimitedNatural");
		oclAny = (AnyType) byName.get("OclAny");
		oclVoid = (VoidType) byName.get("OclVoid");
		oclInvalid = (InvalidType) byName.get("OclInvalid");
	}

	private static PrimitiveType primitive(OclFactory factory, String name, Class<?> instanceClass) {
		PrimitiveType type = factory.createPrimitiveType();
		type.setName(name);
		// PrimitiveType is an EDataType now; the instance class is what the evaluator hands
		// around for values of this type, and a generated model would say the same.
		type.setInstanceClass(instanceClass);
		return type;
	}

	/** @return the library package, registered under {@link #NS_URI} */
	public EPackage ePackage() {
		return ePackage;
	}

	/**
	 * Returns the predefined type of the given name.
	 *
	 * @param name {@code Integer}, {@code Real}, {@code String}, {@code Boolean},
	 *            {@code UnlimitedNatural}, {@code OclAny}, {@code OclVoid} or {@code OclInvalid}
	 * @return the type, or empty if the name is not a predefined type
	 */
	public Optional<OclType> type(String name) {
		return Optional.ofNullable(typesByName.get(name));
	}

	/**
	 * Returns the predefined primitive type of the given name.
	 *
	 * @param name {@code Integer}, {@code Real}, {@code String}, {@code Boolean} or
	 *            {@code UnlimitedNatural}
	 * @return the type, or empty if the name is not a primitive type
	 */
	public Optional<PrimitiveType> primitive(String name) {
		return typesByName.get(name) instanceof PrimitiveType type
				? Optional.of(type) : Optional.empty();
	}

	/** @return every predefined type by name, in library order */
	public Map<String, OclType> types() {
		return typesByName;
	}

	public PrimitiveType integer() {
		return integer;
	}

	public PrimitiveType real() {
		return real;
	}

	public PrimitiveType string() {
		return string;
	}

	public PrimitiveType booleanType() {
		return booleanType;
	}

	public PrimitiveType unlimitedNatural() {
		return unlimitedNatural;
	}

	public AnyType oclAny() {
		return oclAny;
	}

	public VoidType oclVoid() {
		return oclVoid;
	}

	public InvalidType oclInvalid() {
		return oclInvalid;
	}
}
