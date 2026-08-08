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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.function.UnaryOperator;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.impl.EPackageRegistryImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

/**
 * Type resolution against an explicitly supplied {@link EPackage.Registry} (D42).
 *
 * <p>The metamodel is a dynamic one — {@code Novel} extends {@code Book} — and is never
 * registered globally unless a test says so. Without a registry every type name in the
 * template degrades to {@code EObject}, which breaks two things at once: {@code oclIsKindOf}
 * answers {@code false} for the element's own type, and template override dispatch
 * (MOFM2T §8.1.3) never selects an override, because
 * {@code EcorePackage.Literals.EOBJECT.isSuperTypeOf(dynamicEClass)} is {@code false}.
 */
class M2tPackageRegistryTest {

	private static final String NS_URI = "http://example.org/m2x/m2t-registry/1.0";

	private static final String TEMPLATE = """
			[module probe(_'http://example.org/m2x/m2t-registry/1.0')/]
			[template public main(b : Book)]
			[file ('out.txt', false)]
			kindOfNovel=[b.oclIsKindOf(Novel)/]
			kindOfBook=[b.oclIsKindOf(Book)/]
			dispatch=[render(b)/]
			[/file]
			[/template]
			[template public render(b : Book)]BASE[/template]
			[template public render(n : Novel) overrides render]OVERRIDE[/template]
			""";

	private EPackage bookshelfPackage;
	private EClass bookClass;
	private EClass novelClass;
	private EPackage.Registry registry;

	@BeforeEach
	void setUp() {
		bookshelfPackage = EcoreFactory.eINSTANCE.createEPackage();
		bookshelfPackage.setName("bookshelf");
		bookshelfPackage.setNsURI(NS_URI);
		bookshelfPackage.setNsPrefix("bookshelf");

		bookClass = EcoreFactory.eINSTANCE.createEClass();
		bookClass.setName("Book");
		EAttribute title = EcoreFactory.eINSTANCE.createEAttribute();
		title.setName("title");
		title.setEType(EcorePackage.Literals.ESTRING);
		bookClass.getEStructuralFeatures().add(title);

		novelClass = EcoreFactory.eINSTANCE.createEClass();
		novelClass.setName("Novel");
		novelClass.getESuperTypes().add(bookClass);

		bookshelfPackage.getEClassifiers().add(bookClass);
		bookshelfPackage.getEClassifiers().add(novelClass);

		registry = new EPackageRegistryImpl();
		registry.put(NS_URI, bookshelfPackage);
	}

	@AfterEach
	void tearDown() {
		EPackage.Registry.INSTANCE.remove(NS_URI);
	}

	@Test
	@DisplayName("template parameter types resolve to the real EClass")
	void parameterTypesResolve() throws Exception {
		M2tEngineImpl engine = engineWith(builder -> builder.packageRegistry(registry));

		Module module = engine.parse(TEMPLATE, "probe");

		assertEquals(bookClass, declaredType(module, "main"));
		assertEquals(novelClass, declaredType(module, "render", 1));
	}

	@Test
	@DisplayName("oclIsKindOf and override dispatch work on a custom metamodel")
	void kindOfAndOverrideDispatchWork() throws Exception {
		M2tEngineImpl engine = engineWith(builder -> builder.packageRegistry(registry));

		String output = generate(engine);

		assertTrue(output.contains("kindOfNovel=true"), output);
		assertTrue(output.contains("kindOfBook=true"), output);
		assertTrue(output.contains("dispatch=OVERRIDE"),
				() -> "MOFM2T §8.1.3: the override declared for Novel must win\n" + output);
	}

	@Test
	@DisplayName("without a configured registry the static one applies")
	void staticRegistryAppliesWhenNoneConfigured() throws Exception {
		EPackage.Registry.INSTANCE.put(NS_URI, bookshelfPackage);

		M2tEngineImpl engine = engineWith(UnaryOperator.identity());

		String output = generate(engine);

		assertTrue(output.contains("kindOfNovel=true"), output);
		assertTrue(output.contains("dispatch=OVERRIDE"), output);
	}

	// --- helpers ---

	private M2tEngineImpl engineWith(UnaryOperator<M2tConfiguration.Builder> customizer) {
		OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
		return new M2tEngineImpl(customizer.apply(M2tConfiguration.builder(oclConfig)).build());
	}

	private String generate(M2tEngineImpl engine) throws Exception {
		Module module = engine.parse(TEMPLATE, "probe");
		engine.link(module);

		EObject novel = EcoreUtil.create(novelClass);
		novel.eSet(novelClass.getEStructuralFeature("title"), "Moby Dick");

		M2tResult result = engine.execute(module, M2tContext.of(novel));
		assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		return result.generatedFiles().get("out.txt");
	}

	private EClass declaredType(Module module, String templateName) {
		return declaredType(module, templateName, 0);
	}

	/**
	 * Returns the declared parameter type of the {@code index}-th template with that name.
	 */
	private EClass declaredType(Module module, String templateName, int index) {
		return module.getOwnedModuleElement().stream()
				.filter(Template.class::isInstance)
				.map(Template.class::cast)
				.filter(template -> templateName.equals(template.getName()))
				.skip(index)
				.findFirst()
				.map(template -> template.getParameter().get(0).getType())
				.filter(ClassifierType.class::isInstance)
				.map(ClassifierType.class::cast)
				.map(type -> (EClass) type.getReferredClassifier())
				.orElse(null);
	}
}
