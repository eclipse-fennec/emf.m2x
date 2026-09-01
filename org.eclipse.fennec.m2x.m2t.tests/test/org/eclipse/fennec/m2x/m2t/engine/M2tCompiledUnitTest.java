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
package org.eclipse.fennec.m2x.m2t.engine;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.m2t.M2tPackage;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.unit.satellite.SatelliteCollector;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * A compiled MOFM2T module is one self-contained document: it saves, loads in a fresh resource
 * set, copies without pointing back at the original, and generates the same text afterwards
 * (#137, acceptance criteria).
 *
 * <p>Measured before the compiled unit existed: the module below carried fifteen objects the
 * tree referenced but nothing contained — six {@code Variable}s, three {@code ClassifierType}s,
 * two {@code PrimitiveType}s, four synthetic {@code EAttribute}s — and {@code save()} failed on
 * the first of them.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tCompiledUnitTest {

	private static final String MODULE = """
			[module m(Ecore)/]
			[query public hasOps(c : EClass) : Boolean = c.eOperations->size() > 0/]
			[template public main(c : EClass) ? (c.hasOps())]
			[file ('out', false)]
			[for (op : EOperation | c.eOperations) separator(',')][op.name/][/for]
			[let n : String = c.name][n.toUpper()/][/let]
			[/file]
			[/template]
			""";

	private M2tEngine engine;
	private EClass input;

	@BeforeEach
	void setUp() {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		engine = M2tEngines.create(M2tConfiguration.builder(oclConfig).build());
		input = EcoreFactory.eINSTANCE.createEClass();
		input.setName("Employee");
		var op = EcoreFactory.eINSTANCE.createEOperation();
		op.setName("hire");
		input.getEOperations().add(op);
	}

	// ==== The document ====

	@Test
	void compile_yieldsASelfContainedDocument() throws Exception {
		CompiledUnit compiled = engine.compile(MODULE, "m");
		assertEquals(List.of(), SatelliteCollector.find(compiled));
		// What is left after #154 and #156 (types from the standard library, no wrapper, parameters
		// in scope): the loop and let variables, two per-use collection types and the synthetic
		// EAttribute of #153. Measured: 15 before #154, 8 after, 5 after #156. The count is not
		// asserted — it falls with every follow-up.
		assertFalse(compiled.getSatellite().isEmpty(),
				"the parser's satellites are in the container");
	}

	@Test
	void compile_fillsIdentityAndManifest() throws Exception {
		CompiledUnit compiled = engine.compile(MODULE, "m");
		assertNotNull(compiled.getId());
		assertTrue(!compiled.getId().isBlank());
		assertEquals("m2t", compiled.getManifest().getLanguage());
		assertEquals("m", compiled.getManifest().getQualifiedName());
	}

	@Test
	void twoCompiles_haveDifferentIds() throws Exception {
		assertTrue(!engine.compile(MODULE, "m").getId()
				.equals(engine.compile(MODULE, "m").getId()));
	}

	// The script inside the compiled unit is the module parse() would have returned, and it
	// executes exactly as before — compile() adds a document around the graph, no more.
	@Test
	void compiledUnit_executesLikeTheParsedModule() throws Exception {
		CompiledUnit compiled = engine.compile(MODULE, "m");
		Module module = (Module) compiled.getUnit();
		assertSame(compiled, module.eContainer());
		assertEquals("hireEMPLOYEE", generate(module));
	}

	// ==== One Variable per declaration (#154) ====

	// c is the template's parameter and is mentioned in the guard, the for, the let and the
	// body. Every mention refers to that parameter; before #154 the parameter was unknown to
	// the OCL parser, which created a fresh Variable(c) at each mention.
	@Test
	void everyMentionOfC_refersToTheTemplateParameter() throws Exception {
		Module module = engine.parse(MODULE, "m");
		Template main = (Template) module.getOwnedModuleElement().stream()
				.filter(e -> "main".equals(e.getName())).findFirst().orElseThrow();
		Variable parameter = main.getParameter().get(0);
		List<Variable> referred = new ArrayList<>();
		main.eAllContents().forEachRemaining(o -> {
			if (o instanceof VariableExp exp && exp.getReferredVariable() != null
					&& "c".equals(exp.getReferredVariable().getName())) {
				referred.add(exp.getReferredVariable());
			}
		});
		assertTrue(referred.size() >= 3, "c is mentioned at least three times, found " + referred.size());
		for (Variable variable : referred) {
			assertSame(parameter, variable, "a mention of c is the parameter c");
		}
	}

	// Two inline expressions get two different, deterministic names — not one derived from an
	// identity hash, which changed with every JVM run.
	@Test
	void inlineVariables_haveDeterministicNames() throws Exception {
		Module first = engine.parse(MODULE, "m");
		Module second = engine.parse(MODULE, "m");
		assertEquals(inlineNames(first), inlineNames(second));
		assertTrue(inlineNames(first).stream().allMatch(n -> n.matches("__inline__\\d+")),
				"names are counter-based: " + inlineNames(first));
	}

	// ==== Round trips ====

	@Test
	void savedAndReloaded_generatesTheSameText() throws Exception {
		CompiledUnit compiled = engine.compile(MODULE, "m");
		String before = generate((Module) compiled.getUnit());

		CompiledUnit reloaded = roundTrip(compiled);

		assertEquals(List.of(), SatelliteCollector.find(reloaded),
				"a reloaded unit references nothing outside itself");
		assertEquals(before, generate((Module) reloaded.getUnit()));
	}

	// A copy-isolating store returns copies; a copy of a self-contained document points at
	// nothing of the original. This is what makes the compiled unit storable at all.
	@Test
	void copy_pointsAtNothingOfTheOriginal() throws Exception {
		CompiledUnit compiled = engine.compile(MODULE, "m");
		CompiledUnit copy = EcoreUtil.copy(compiled);

		assertEquals(0, referencesInto(copy, compiled),
				"every reference of the copy resolves within the copy");
		assertEquals(generate((Module) compiled.getUnit()), generate((Module) copy.getUnit()));
	}

	// ---- Helpers ----

	private static List<String> inlineNames(Module module) {
		List<String> names = new ArrayList<>();
		module.eAllContents().forEachRemaining(o -> {
			if (o instanceof Variable v && v.getName() != null && v.getName().startsWith("__inline__")) {
				names.add(v.getName());
			}
		});
		return names;
	}

	private String generate(Module module) {
		M2tResult result = engine.execute(module, M2tContext.of(input));
		assertTrue(result.generatedFiles().containsKey("out"),
				"no file generated: " + result.diagnostics());
		return result.generatedFiles().get("out").strip();
	}

	private static CompiledUnit roundTrip(CompiledUnit compiled) throws Exception {
		ResourceSet out = freshResourceSet();
		Resource resource = out.createResource(URI.createURI("unit.compiled"));
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
		resourceSet.getPackageRegistry().put(M2tPackage.eNS_URI, M2tPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(OclPackage.eNS_URI, OclPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		return resourceSet;
	}

	private static int referencesInto(EObject copy, EObject original) {
		var originals = java.util.Collections.newSetFromMap(
				new java.util.IdentityHashMap<EObject, Boolean>());
		originals.add(original);
		original.eAllContents().forEachRemaining(originals::add);
		int leaks = 0;
		for (var it = copy.eAllContents(); it.hasNext();) {
			EObject owner = it.next();
			for (var reference : owner.eClass().getEAllReferences()) {
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
