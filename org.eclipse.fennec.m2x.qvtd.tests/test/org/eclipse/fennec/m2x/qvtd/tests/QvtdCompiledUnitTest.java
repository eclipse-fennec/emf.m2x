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
package org.eclipse.fennec.m2x.qvtd.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.QvtbasePackage;
import org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationPackage;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.QvttemplatePackage;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.Test;

/**
 * A compiled QVT-R unit is one self-contained document: it saves, loads in a fresh resource
 * set, copies without pointing back at the original, and enforces the same result afterwards
 * (#137, acceptance criteria).
 *
 * <p>Measured before the compiled unit existed: the transformation below carried twelve objects
 * the tree referenced but nothing contained — six {@code Variable}s (the relation variable
 * {@code pn} four times over), three {@code ClassifierType} wrappers, one {@code PrimitiveType}, two
 * synthetic {@code EAttribute}s — and {@code save()} failed on the first of them. After #154 and
 * #156 four remain: two variables and the two synthetic attributes of #153.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtdCompiledUnitTest extends AbstractQvtdEngineTest {

	// The when clause reads a domain-bound variable through a query — the shape #145 fixed,
	// and the one that has to survive a round trip.
	private static final String TRANSFORMATION = """
			transformation T(uml : simpleuml, rdbms : simplerdbms) {
				query IsExported(n : String) : Boolean { n.size() > 1 }
				top relation PackageToSchema {
					pn : String;
					checkonly domain uml p : Package { name = pn };
					enforce domain rdbms s : Schema { name = pn };
					when { IsExported(pn); }
					where { pn.size() > 0; }
				}
			}
			""";

	// ==== parse() versus compile() ====

	@Test
	void parse_isNotSelfContained() throws Exception {
		assertFalse(SatelliteCollector.find(parse(TRANSFORMATION)).isEmpty());
	}

	@Test
	void compile_isSelfContained() throws Exception {
		CompiledUnit compiled = engine.compile(TRANSFORMATION, "T");
		assertEquals(List.of(), SatelliteCollector.find(compiled));
		// Measured 12 before #154 and fewer after (String from the library, pn resolved to its
		// declaration). The count is not asserted — #153 and #156 lower it further.
		assertFalse(compiled.getSatellite().isEmpty(), "the parser's satellites are in the container");
	}

	@Test
	void compile_fillsIdentityAndManifest() throws Exception {
		CompiledUnit compiled = engine.compile(TRANSFORMATION, "T");
		assertNotNull(compiled.getId());
		assertFalse(compiled.getId().isBlank());
		assertEquals("qvtr", compiled.getManifest().getLanguage());
		assertEquals("T", compiled.getManifest().getQualifiedName());
	}

	@Test
	void compiledUnit_executesLikeTheParsedTransformation() throws Exception {
		CompiledUnit compiled = engine.compile(TRANSFORMATION, "T");
		RelationalTransformation transformation = (RelationalTransformation) compiled.getUnit();
		assertSame(compiled, transformation.eContainer());
		assertEquals(1, enforce(transformation, "HR"), "the schema is created");
	}

	// ==== One Variable per declaration (#154) ====

	// pn is declared once and mentioned four times — in two domains, the when and the where
	// clause. Every mention refers to the declaration, which the relation owns; before #154 each
	// mention created its own Variable, none of them the declared one.
	@Test
	void everyMentionOfPn_refersToTheDeclaredVariable() throws Exception {
		RelationalTransformation t = parse(TRANSFORMATION);
		Relation relation = (Relation) t.getRule().get(0);
		Variable declared = relation.getVariable().stream()
				.filter(v -> "pn".equals(v.getName())).findFirst().orElseThrow();
		List<Variable> referred = new ArrayList<>();
		t.eAllContents().forEachRemaining(o -> {
			if (o instanceof VariableExp exp && exp.getReferredVariable() != null
					&& "pn".equals(exp.getReferredVariable().getName())) {
				referred.add(exp.getReferredVariable());
			}
		});
		assertTrue(referred.size() >= 3, "pn is mentioned at least three times, found " + referred.size());
		for (Variable variable : referred) {
			assertSame(declared, variable, "a mention of pn is the declared pn");
		}
	}

	// ==== Round trip ====

	@Test
	void reloaded_isStillSelfContained() throws Exception {
		CompiledUnit reloaded = roundTrip(engine.compile(TRANSFORMATION, "T"));
		assertEquals(List.of(), SatelliteCollector.find(reloaded));
	}

	@Test
	void reloaded_enforcesTheSameResult() throws Exception {
		CompiledUnit reloaded = roundTrip(engine.compile(TRANSFORMATION, "T"));
		RelationalTransformation transformation = (RelationalTransformation) reloaded.getUnit();
		assertEquals(1, enforce(transformation, "HR"), "name passes the query, a schema is created");
		assertEquals(0, enforce(transformation, "X"), "name fails the query, nothing is created");
	}

	// ==== Copy isolation ====

	@Test
	void copy_pointsAtNothingOfTheOriginal() throws Exception {
		CompiledUnit compiled = engine.compile(TRANSFORMATION, "T");
		CompiledUnit copy = EcoreUtil.copy(compiled);
		assertEquals(0, referencesInto(copy, compiled));
		assertEquals(1, enforce((RelationalTransformation) copy.getUnit(), "HR"));
	}

	// ---- Helpers ----

	private static int enforce(RelationalTransformation transformation, String packageName) {
		QvtdModelExtent uml = QvtdModelExtent.of(createPackage(packageName));
		QvtdModelExtent rdbms = emptyExtent();
		QvtdExecutionResult result = engine.execute(transformation,
				QvtdExecutionContext.enforce("rdbms", Map.of("uml", uml, "rdbms", rdbms)));
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
		return rdbms.getContents().size();
	}

	private static CompiledUnit roundTrip(CompiledUnit compiled) throws Exception {
		Resource resource = freshResourceSet().createResource(URI.createURI("unit.compiled"));
		resource.getContents().add(compiled);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		resource.save(bytes, Map.of());

		ResourceSet in = freshResourceSet();
		Resource loaded = in.createResource(URI.createURI("unit.compiled"));
		loaded.load(new ByteArrayInputStream(bytes.toByteArray()), Map.of());
		EcoreUtil.resolveAll(in);
		return (CompiledUnit) loaded.getContents().get(0);
	}

	private static ResourceSet freshResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("*", new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(CompiledPackage.eNS_URI, CompiledPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(QvtrelationPackage.eNS_URI, QvtrelationPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(QvttemplatePackage.eNS_URI, QvttemplatePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(QvtbasePackage.eNS_URI, QvtbasePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(OclPackage.eNS_URI, OclPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(umlPackage.getNsURI(), umlPackage);
		resourceSet.getPackageRegistry().put(rdbmsPackage.getNsURI(), rdbmsPackage);
		return resourceSet;
	}

	private static int referencesInto(EObject copy, EObject original) {
		Set<EObject> originals = Collections.newSetFromMap(new IdentityHashMap<>());
		originals.add(original);
		original.eAllContents().forEachRemaining(originals::add);
		int leaks = 0;
		for (var it = copy.eAllContents(); it.hasNext();) {
			EObject owner = it.next();
			for (EReference reference : owner.eClass().getEAllReferences()) {
				if (reference.isContainment() || reference.isDerived() || !owner.eIsSet(reference)) {
					continue;
				}
				Object value = owner.eGet(reference);
				for (Object target : value instanceof List<?> list ? list : List.of(value)) {
					if (target instanceof EObject e && originals.contains(e)) {
						leaks++;
					}
				}
			}
		}
		return leaks;
	}
}
