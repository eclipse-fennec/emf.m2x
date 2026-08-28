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
package org.eclipse.fennec.m2x.qvto.tests.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
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
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.Test;
import java.util.ArrayList;
import java.util.HashMap;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * A compiled QVT-O unit is one self-contained document (#137, acceptance criteria).
 *
 * <p>{@code parse()} returns the in-memory graph, and that graph is <em>not</em> storable: the
 * parser creates objects the tree references but that no feature contains. Measured before
 * the compiled unit existed — and pinned below, because the distinction is the point:
 *
 * <table>
 *   <caption>uncontained objects referenced from a parse() result</caption>
 *   <tr><th>source</th><th>uncontained</th><th>{@code save()}</th></tr>
 *   <tr><td>minimal, no import, no modeltype</td><td>1 — {@code PrimitiveType(Integer)}</td>
 *       <td>fails</td></tr>
 *   <tr><td>modeltypes, mapping, guard, intermediate class</td>
 *       <td>13 — 4 {@code Variable}, 4 {@code ClassifierType}, 2 {@code ModelType},
 *           1 {@code PrimitiveType}, 1 default expression, 1 {@code EFactory}</td>
 *       <td>fails</td></tr>
 * </table>
 *
 * <p>{@code compile()} gives those objects a home beside the script. What these tests
 * deliberately do <em>not</em> demand: references into the metamodels stay external. A
 * referenced {@code EPackage} lives in its own resource and must keep living there, otherwise
 * a shared unit could not lend its types — the reason the containment approach of PR #128 was
 * rejected.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoAstSelfContainmentTest extends AbstractQvtoEngineTest {

	private static final String MINIMAL = """
			transformation test() {
			    property threshold : Integer = 10;
			    main() {
			        log('x');
			    }
			}
			""";

	// The module property is read from the when guard on purpose: that is the shape PR #128
	// broke, and it has to survive a round trip with its value intact.
	private static final String WITH_MAPPING = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			transformation test(in s : SRC, out t : TGT) {
			    property threshold : Integer = 10;
			    intermediate class Holder { count : Integer = 0; }
			    mapping SourceElement::toTarget() : TargetElement
			        when { self.value > threshold; } {
			        name := self.name;
			    }
			    main() {
			        var h := object Holder {};
			        log('count:' + h.count.repr());
			        s.objectsOfType(SourceElement)->map toTarget();
			        log('created:' + t.objectsOfType(TargetElement)->size().repr());
			    }
			}
			""";

	// ==== parse() versus compile() ====

	// parse() is the in-memory form and stays so: this pins that the two are different, and
	// that compile() is where self-containment is bought.
	@Test
	void parse_isNotSelfContained() throws Exception {
		assertFalse(SatelliteCollector.find(parse(WITH_MAPPING)).isEmpty(),
				"a transformation with a mapping references variables and types parse() leaves uncontained");
	}

	// Since #154 the predefined types come from the standard library, an EPackage in the
	// registry: Integer is an external reference like EString, not a satellite. A minimal
	// transformation therefore has no satellite at all.
	@Test
	void compile_minimalUnit_isSelfContained() throws Exception {
		CompiledUnit compiled = engine.compile(MINIMAL, "test");
		assertEquals(List.of(), SatelliteCollector.find(compiled));
		assertEquals(List.of(), compiled.getSatellite(),
				"Integer comes from the standard library and needs no container");
	}

	@Test
	void compile_unitWithMapping_isSelfContained() throws Exception {
		CompiledUnit compiled = engine.compile(WITH_MAPPING, "test");
		assertEquals(List.of(), SatelliteCollector.find(compiled));
		// Measured 13 before #154 and 11 after (Integer from the library, one wrapper per
		// classifier). The count is not asserted — #153 and #156 lower it further.
		assertFalse(compiled.getSatellite().isEmpty(), "the parser's satellites are in the container");
	}

	@Test
	void compile_fillsIdentityAndManifest() throws Exception {
		CompiledUnit compiled = engine.compile(WITH_MAPPING, "test");
		assertNotNull(compiled.getId());
		assertFalse(compiled.getId().isBlank());
		assertEquals("qvto", compiled.getManifest().getLanguage());
		assertEquals("test", compiled.getManifest().getQualifiedName());
	}

	// The script inside the compiled unit is the transformation parse() would have returned,
	// and it executes exactly as before — compile() adds a document around the graph.
	@Test
	void compiledUnit_executesLikeTheParsedTransformation() throws Exception {
		QvtoExecutionResult parsed = run(parse(WITH_MAPPING), 20);
		assertSuccess(parsed);

		CompiledUnit compiled = engine.compile(WITH_MAPPING, "test");
		OperationalTransformation transformation = (OperationalTransformation) compiled.getUnit();
		assertSame(compiled, transformation.eContainer());

		QvtoExecutionResult result = run(transformation, 20);
		assertSuccess(result);
		assertLogged(result, "created:1");
		assertLogged(result, "count:0");
		assertEquals(logs(parsed), logs(result), "the document around the graph changes nothing");
	}

	// ==== One type per identity (#154) ====

	// SourceElement is referenced from the mapping's context and from objectsOfType; the unit
	// builder recreates its expression builder eleven times, and a per-builder cache left two
	// wrappers for one class. One unit, one wrapper per classifier.
	@Test
	void oneClassifierTypePerClassifier() throws Exception {
		OperationalTransformation t = parse(WITH_MAPPING);
		Map<EClassifier, List<ClassifierType>> wrappers = new HashMap<>();
		Set<ClassifierType> seen = Collections.newSetFromMap(new IdentityHashMap<>());
		t.eAllContents().forEachRemaining(o -> {
			for (EReference reference : o.eClass().getEAllReferences()) {
				if (reference.isContainment() || !o.eIsSet(reference)) {
					continue;
				}
				Object value = o.eGet(reference);
				for (Object target : value instanceof List<?> list ? list : List.of(value)) {
					if (target instanceof ClassifierType ct && ct.getReferredClassifier() != null
							&& seen.add(ct)) {
						wrappers.computeIfAbsent(ct.getReferredClassifier(), k -> new ArrayList<>()).add(ct);
					}
				}
			}
		});
		assertFalse(wrappers.isEmpty());
		wrappers.forEach((classifier, list) -> assertEquals(1, list.size(),
				"one wrapper for " + classifier.getName() + ", found " + list.size()));
	}

	// The predefined types are the standard library's instances — Integer in a module property
	// and Integer in an intermediate class are the same object.
	@Test
	void predefinedTypes_areTheLibraryInstances() throws Exception {
		OperationalTransformation t = parse(WITH_MAPPING);
		List<PrimitiveType> integers = new ArrayList<>();
		t.eAllContents().forEachRemaining(o -> {
			if (o instanceof Variable v && v.getType() instanceof PrimitiveType pt
					&& "Integer".equals(pt.getName())) {
				integers.add(pt);
			}
		});
		assertFalse(integers.isEmpty());
		for (PrimitiveType integer : integers) {
			assertSame(OclStandardLibrary.INSTANCE.integer(), integer);
		}
	}

	// ==== Round trip: save, load in a fresh resource set, execute ====

	@Test
	void compile_minimalUnit_savesAsXmi() throws Exception {
		roundTrip(engine.compile(MINIMAL, "test"));
	}

	@Test
	void reloaded_isStillSelfContained() throws Exception {
		CompiledUnit reloaded = roundTrip(engine.compile(WITH_MAPPING, "test"));
		assertEquals(List.of(), SatelliteCollector.find(reloaded));
	}

	@Test
	void reloaded_executesWithTheSameResult() throws Exception {
		CompiledUnit compiled = engine.compile(WITH_MAPPING, "test");
		QvtoExecutionResult before = run((OperationalTransformation) compiled.getUnit(), 20);
		assertSuccess(before);

		CompiledUnit reloaded = roundTrip(compiled);
		QvtoExecutionResult after = run((OperationalTransformation) reloaded.getUnit(), 20);
		assertSuccess(after);
		assertLogged(after, "created:1");
		assertEquals(logs(before), logs(after));
	}

	// Regression guard for the PR #128 failure: the module property the guard reads keeps its
	// value across the round trip — the guard filters, it does not compare against null.
	@Test
	void reloaded_guardStillReadsTheModuleProperty() throws Exception {
		CompiledUnit reloaded = roundTrip(engine.compile(WITH_MAPPING, "test"));
		OperationalTransformation transformation = (OperationalTransformation) reloaded.getUnit();

		QvtoExecutionResult above = run(transformation, 20);
		assertSuccess(above);
		assertLogged(above, "created:1");

		QvtoExecutionResult below = run(transformation, 5);
		assertSuccess(below);
		assertLogged(below, "created:0");
	}

	// The default expression of an intermediate class is a satellite parked in an annotation
	// — the kind the PR #128 repair pass skipped without a diagnostic. It survives.
	@Test
	void reloaded_intermediateClassDefaultSurvives() throws Exception {
		CompiledUnit reloaded = roundTrip(engine.compile(WITH_MAPPING, "test"));
		QvtoExecutionResult result = run((OperationalTransformation) reloaded.getUnit(), 20);
		assertSuccess(result);
		assertLogged(result, "count:0");
	}

	// ==== Copy isolation ====

	// A copy-isolating store returns copies; a copy of a self-contained document points at
	// nothing of the original. Without the container, a copy pointed its references straight
	// back at the original's satellites.
	@Test
	void copy_pointsAtNothingOfTheOriginal() throws Exception {
		CompiledUnit compiled = engine.compile(WITH_MAPPING, "test");
		CompiledUnit copy = EcoreUtil.copy(compiled);

		assertEquals(0, referencesInto(copy, compiled));
		QvtoExecutionResult result = run((OperationalTransformation) copy.getUnit(), 20);
		assertSuccess(result);
		assertLogged(result, "created:1");
	}

	// ---- Helpers ----

	private static QvtoExecutionResult run(OperationalTransformation transformation, int value) {
		QvtoModelExtent in = new BasicQvtoModelExtent(createSourceElement("elem", value));
		QvtoModelExtent out = emptyExtent();
		return engine.execute(transformation, QvtoExecutionContext.of(in, out));
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

	/**
	 * A resource set that knows the metamodels by nsURI and nothing else — the way a runtime
	 * that loads a stored unit would.
	 */
	private static ResourceSet freshResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("*", new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(CompiledPackage.eNS_URI, CompiledPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(QvtOperationalPackage.eNS_URI,
				QvtOperationalPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(OclPackage.eNS_URI, OclPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(sourcePackage.getNsURI(), sourcePackage);
		resourceSet.getPackageRegistry().put(targetPackage.getNsURI(), targetPackage);
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

	private static List<String> logs(QvtoExecutionResult result) {
		return result.diagnostics().stream().map(d -> d.getMessage()).toList();
	}

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected));
		assertTrue(found, "Expected log containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
	}
}
