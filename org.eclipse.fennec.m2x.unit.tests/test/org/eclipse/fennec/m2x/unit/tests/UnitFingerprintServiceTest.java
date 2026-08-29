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
package org.eclipse.fennec.m2x.unit.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.impl.ResourceImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnitManifest;
import org.eclipse.fennec.m2x.model.compiled.DependencyEntry;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;
import org.eclipse.fennec.m2x.unit.compile.UnitPackager;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * The m2x1 fingerprint: reproducible, canonical, blind to what is not semantics (#138).
 *
 * <p>Built from plain Ecore objects so that the rules are tested without any language in play.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class UnitFingerprintServiceTest {

	private static final UnitFingerprintService SERVICE = DefaultUnitFingerprintService.INSTANCE;
	private static final EcoreFactory F = EcoreFactory.eINSTANCE;

	// ==== The contract ====

	@Test
	void value_isSchemeColonHexDigest() {
		String value = SERVICE.fingerprint(compiled(samplePackage()));
		assertTrue(value.matches("m2x1:[0-9a-f]{64}"), value);
		assertEquals("m2x1", SERVICE.currentScheme());
		assertEquals(Set.of("m2x1"), SERVICE.supportedSchemes());
	}

	@Test
	void unknownScheme_isRejected() {
		assertThrows(IllegalArgumentException.class,
				() -> SERVICE.fingerprintInScheme(compiled(samplePackage()), "fp1"));
	}

	// ==== Reproducible ====

	@Test
	void sameStructure_sameValue_acrossObjectIdentities() {
		assertEquals(SERVICE.fingerprint(compiled(samplePackage())),
				SERVICE.fingerprint(compiled(samplePackage())));
	}

	// The scheme is frozen: this value may only change together with the scheme tag. It is what
	// "the same value across JVM runs" means, checked against a recorded constant.
	@Test
	void m2x1_isFrozen() {
		String value = SERVICE.fingerprint(compiled(samplePackage()));
		assertEquals("m2x1:" + FROZEN_SAMPLE_DIGEST, value,
				"the canonicalization of m2x1 changed — that needs a new scheme tag, not an edit");
	}

	// ==== Reproducible: where a metamodel lives does not enter the value (#142) ====

	@Test
	void metamodelReference_isKeyedByNsUri_whereverTheMetamodelLives() {
		EPackage shelf = F.createEPackage();
		shelf.setName("shelf");
		shelf.setNsURI("http://shelf/1.0");
		shelf.setNsPrefix("shelf");
		EClass book = F.createEClass();
		book.setName("Book");
		shelf.getEClassifiers().add(book);
		EPackage unit = F.createEPackage();
		unit.setName("unit");
		unit.setNsURI("http://unit/1.0");
		EClass sub = F.createEClass();
		sub.setName("Sub");
		sub.getESuperTypes().add(book); // a reference into the metamodel
		unit.getEClassifiers().add(sub);

		String withoutResource = SERVICE.fingerprint(compiled(unit));
		new ResourceImpl(
				URI.createFileURI("/somewhere/on/disk/shelf.ecore")).getContents().add(shelf);
		String fromAFile = SERVICE.fingerprint(compiled(EcoreUtil.copy(unit) instanceof EPackage copy ? relink(copy, book) : unit));
		assertEquals(withoutResource, fromAFile,
				"the file the metamodel was loaded from must not enter the value — the unit did not change");
	}

	private static EPackage relink(EPackage copy, EClass target) {
		((EClass) copy.getEClassifier("Sub")).getESuperTypes().set(0, target);
		return copy;
	}

	// ==== Canonical: what changes the value ====

	@Test
	void attributeValue_changesTheValue() {
		EPackage other = samplePackage();
		((EClass) other.getEClassifier("Person")).setAbstract(true);
		assertNotEquals(SERVICE.fingerprint(compiled(samplePackage())), SERVICE.fingerprint(compiled(other)));
	}

	@Test
	void order_changesTheValue() {
		EPackage other = samplePackage();
		EClass person = (EClass) other.getEClassifier("Person");
		person.getEStructuralFeatures().move(1, 0);
		assertNotEquals(SERVICE.fingerprint(compiled(samplePackage())), SERVICE.fingerprint(compiled(other)));
	}

	@Test
	void addedFeature_changesTheValue() {
		EPackage other = samplePackage();
		EAttribute extra = F.createEAttribute();
		extra.setName("extra");
		extra.setEType(EcorePackage.Literals.ESTRING);
		((EClass) other.getEClassifier("Person")).getEStructuralFeatures().add(extra);
		assertNotEquals(SERVICE.fingerprint(compiled(samplePackage())), SERVICE.fingerprint(compiled(other)));
	}

	// ==== Canonical: what does not ====

	@Test
	void documentationAnnotation_doesNotChangeTheValue() {
		EPackage other = samplePackage();
		EAnnotation documentation = F.createEAnnotation();
		documentation.setSource("http://www.eclipse.org/emf/2002/GenModel");
		documentation.getDetails().put("documentation", "A person.");
		other.getEClassifier("Person").getEAnnotations().add(documentation);
		assertEquals(SERVICE.fingerprint(compiled(samplePackage())), SERVICE.fingerprint(compiled(other)));
	}

	// A reference to a type of a metamodel enters as its URI, not as the object: two trees
	// pointing at EString agree, and a metamodel's own fingerprint is a separate concern.
	@Test
	void externalReference_entersAsUri() {
		EPackage a = samplePackage();
		EPackage b = samplePackage();
		assertEquals(SERVICE.fingerprint(compiled(a)), SERVICE.fingerprint(compiled(b)));
		// and pointing at a different external type is a different value
		EClass person = (EClass) b.getEClassifier("Person");
		((EAttribute) person.getEStructuralFeature("name")).setEType(EcorePackage.Literals.EINT);
		assertNotEquals(SERVICE.fingerprint(compiled(a)), SERVICE.fingerprint(compiled(b)));
	}

	// A reference to an object outside the tree and outside any resource — a satellite — enters
	// as what it says, so two units whose satellites are different objects with the same content
	// agree.
	@Test
	void satellite_entersByContent() {
		EPackage a = samplePackage();
		EPackage b = samplePackage();
		EClass floatingA = F.createEClass();
		floatingA.setName("Floating");
		EClass floatingB = F.createEClass();
		floatingB.setName("Floating");
		((EClass) a.getEClassifier("Person")).getESuperTypes().add(floatingA);
		((EClass) b.getEClassifier("Person")).getESuperTypes().add(floatingB);
		assertEquals(SERVICE.fingerprint(compiled(a)), SERVICE.fingerprint(compiled(b)));
		floatingB.setName("Other");
		assertNotEquals(SERVICE.fingerprint(compiled(a)), SERVICE.fingerprint(compiled(b)));
	}

	// ==== Sources ====

	@Test
	void source_lineEndingsAreNormalized() {
		Unit.Source unix = source("transformation t() {\n  main() {}\n}\n");
		Unit.Source windows = source("transformation t() {\r\n  main() {}\r\n}\r\n");
		assertEquals(SERVICE.fingerprint(unix), SERVICE.fingerprint(windows));
	}

	@Test
	void source_andCompiled_areDifferentKinds() {
		Unit.Source source = source("x");
		assertTrue(SERVICE.fingerprint(source).matches("m2x1:[0-9a-f]{64}"));
		assertNotEquals(SERVICE.fingerprint(source), SERVICE.fingerprint(compiled(samplePackage())));
	}

	// ==== Merkle: dependencies ====

	@Test
	void dependencyFingerprints_foldIn() {
		CompiledUnit alone = compiledUnit(samplePackage());
		CompiledUnit withDependency = compiledUnit(samplePackage());
		DependencyEntry entry = CompiledFactory.eINSTANCE.createDependencyEntry();
		entry.setQualifiedName("lib.Strings");
		entry.setFingerprint("m2x1:abc");
		withDependency.getManifest().getDependencyEntry().add(entry);
		assertNotEquals(SERVICE.fingerprint(alone), SERVICE.fingerprint(withDependency));

		CompiledUnit sameDependencyOtherOrder = compiledUnit(samplePackage());
		DependencyEntry second = CompiledFactory.eINSTANCE.createDependencyEntry();
		second.setQualifiedName("lib.Other");
		second.setFingerprint("m2x1:def");
		DependencyEntry first = CompiledFactory.eINSTANCE.createDependencyEntry();
		first.setQualifiedName("lib.Strings");
		first.setFingerprint("m2x1:abc");
		sameDependencyOtherOrder.getManifest().getDependencyEntry().add(second);
		sameDependencyOtherOrder.getManifest().getDependencyEntry().add(first);
		CompiledUnit ordered = compiledUnit(samplePackage());
		ordered.getManifest().getDependencyEntry().add(copy(first));
		ordered.getManifest().getDependencyEntry().add(copy(second));
		assertEquals(SERVICE.fingerprint(ordered), SERVICE.fingerprint(sameDependencyOtherOrder),
				"the order the entries were listed in does not matter");
	}

	// The metadata of the compiled unit — id, version, description — are not the content
	@Test
	void compiledUnitMetadata_doNotChangeTheValue() {
		CompiledUnit a = compiledUnit(samplePackage());
		CompiledUnit b = compiledUnit(samplePackage());
		a.setId("one");
		b.setId("two");
		b.setVersion("1.2");
		b.setDescription("the same script, described");
		assertEquals(SERVICE.fingerprint(a), SERVICE.fingerprint(b));
	}

	// ---- Fixture ----

	private static final String FROZEN_SAMPLE_DIGEST =
			"9512c2e78e55dd9127bb72251ac44f206b233045dc5c974088fdde4b7fd1ca88";

	private static EPackage samplePackage() {
		EPackage pkg = F.createEPackage();
		pkg.setName("sample");
		pkg.setNsURI("http://sample/1.0");
		pkg.setNsPrefix("sample");
		EClass person = F.createEClass();
		person.setName("Person");
		EAttribute name = F.createEAttribute();
		name.setName("name");
		name.setEType(EcorePackage.Literals.ESTRING);
		EAttribute age = F.createEAttribute();
		age.setName("age");
		age.setEType(EcorePackage.Literals.EINT);
		person.getEStructuralFeatures().add(name);
		person.getEStructuralFeatures().add(age);
		EClass company = F.createEClass();
		company.setName("Company");
		EReference employees = F.createEReference();
		employees.setName("employees");
		employees.setEType(person);
		employees.setUpperBound(-1);
		employees.setContainment(true);
		company.getEStructuralFeatures().add(employees);
		pkg.getEClassifiers().add(person);
		pkg.getEClassifiers().add(company);
		return pkg;
	}

	private static Unit.Compiled compiled(EPackage script) {
		return new Unit.Compiled() {
			@Override
			public String qualifiedName() {
				return "sample";
			}

			@Override
			public EObject root() {
				return script;
			}
		};
	}

	private static Unit.Source source(String text) {
		return new Unit.Source() {
			@Override
			public String qualifiedName() {
				return "sample";
			}

			@Override
			public URI uri() {
				return URI.createURI("mem:/sample");
			}

			@Override
			public String source() {
				return text;
			}
		};
	}

	private static CompiledUnit compiledUnit(EPackage script) {
		CompiledUnit compiled = CompiledFactory.eINSTANCE.createCompiledUnit();
		compiled.setId("id");
		CompiledUnitManifest manifest = CompiledFactory.eINSTANCE.createCompiledUnitManifest();
		compiled.setManifest(manifest);
		compiled.setUnit(script);
		return compiled;
	}

	private static DependencyEntry copy(DependencyEntry entry) {
		DependencyEntry copy = CompiledFactory.eINSTANCE.createDependencyEntry();
		copy.setQualifiedName(entry.getQualifiedName());
		copy.setFingerprint(entry.getFingerprint());
		return copy;
	}
	@Test
	@DisplayName("an escaped quote does not collide with the quote it escapes")
	void escapedQuotes_doNotCollide() {
		// Attribute values are written into the canonical form between quotes. Without escaping
		// the escape character itself, the text a"b and the text a\\"b canonicalize the same way,
		// and two units that say different things get one fingerprint — which is what a
		// fingerprint exists to prevent.
		assertNotEquals(fingerprintOfModuleNamed("a\"b"), fingerprintOfModuleNamed("a\\\"b"));
		assertNotEquals(fingerprintOfModuleNamed("a\\b"), fingerprintOfModuleNamed("a\\\\b"));
		assertEquals(fingerprintOfModuleNamed("a\"b"), fingerprintOfModuleNamed("a\"b"),
				"and the same text still gives the same value");
	}

	private static String fingerprintOfModuleNamed(String name) {
		Module module = M2tFactory.eINSTANCE.createModule();
		module.setName(name);
		module.setNsURI("http://example.org/m2x/escaping/1.0");
		return SERVICE.fingerprint(UnitPackager.compile("m2t", "gen.Escaping", module));
	}

}
