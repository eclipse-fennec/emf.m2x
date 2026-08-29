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
package org.eclipse.fennec.m2x.qvto.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2x.model.trace.Trace;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxInvocationContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitPrepareException;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * The QVT-O limits that are enforced, held to it (#175, part of #188).
 *
 * <p>Each of these covers a mitigation the QVT-O security analysis names and that no test
 * would have missed: the suite reached the option values ({@code maxTraceRecords} was
 * asserted to <em>be</em> a million) without ever reaching what they do.
 */
class QvtoLimitEnforcementTest extends AbstractQvtoEngineTest {

	@Nested
	@DisplayName("Q-6: the trace record limit")
	class TraceRecords {

		private static final String TRANSFORMATION = """
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation trace(in s : SRC, out t : TGT) {
				    mapping SourceElement::toTarget() : r : TargetElement {
				        r.name := self.name;
				    }
				    main() {
				        s.objectsOfType(SourceElement)->collect(e | e.map toTarget());
				    }
				}
				""";

		@Test
		@DisplayName("records past the limit are dropped, and the run still succeeds")
		void recordsPastTheLimitAreDropped() throws QvtoParseException {
			// The trace grows with the model, so it is the one structure a transformation can
			// make arbitrarily large without writing anything arbitrarily large itself
			QvtoModelExtent source = new BasicQvtoModelExtent(
					createSourceElement("a", 1), createSourceElement("b", 2),
					createSourceElement("c", 3), createSourceElement("d", 4),
					createSourceElement("e", 5));
			QvtoExecutionResult result = executeWithExtents(TRANSFORMATION,
					QvtoEvaluationOptions.defaults().withTracing(true).withMaxTraceRecords(3),
					source, emptyExtent());

			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			Trace trace = result.trace();
			assertEquals(3, trace.getTraceRecords().size(),
					"the limit is what the trace holds, not what the transformation produced");
		}

		@Test
		@DisplayName("under the limit every mapping is traced")
		void everyMappingIsTracedUnderTheLimit() throws QvtoParseException {
			QvtoModelExtent source = new BasicQvtoModelExtent(
					createSourceElement("a", 1), createSourceElement("b", 2));
			QvtoExecutionResult result = executeWithExtents(TRANSFORMATION,
					QvtoEvaluationOptions.defaults().withTracing(true).withMaxTraceRecords(10),
					source, emptyExtent());

			assertEquals(2, result.trace().getTraceRecords().size());
		}
	}

	@Nested
	@DisplayName("Q-4: the loop limit, where a collection is iterated")
	class IteratorExpressions {

		@Test
		@DisplayName("xcollect is bounded like while and forEach are")
		void xcollectIsBounded() throws QvtoParseException {
			// The limit had three checkpoints and one of them — the ImperativeIterateExp case —
			// is not reached by anything the parser builds, so the x-variants ran unbounded
			// (#175). QVT v1.3 §8.2.2.7 defines them in terms of forEach, which is bounded.
			QvtoExecutionResult result = execute("""
					transformation T();
					main() {
						var items := Sequence{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
						var doubled := items->xcollect(i | i * 2);
						log(doubled->size().repr());
					}
					""", QvtoExecutionContext.of(),
					QvtoEvaluationOptions.defaults().withMaxLoopIterations(5));

			assertFalse(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getMessage().contains("Maximum loop iterations exceeded")),
					() -> "diagnostics: " + result.diagnostics());
		}

		@Test
		@DisplayName("select and collect are bounded as well")
		void oclStyleIteratorsAreBounded() throws QvtoParseException {
			QvtoExecutionResult result = execute("""
					transformation T();
					main() {
						var items := Sequence{1, 2, 3, 4, 5, 6, 7, 8, 9, 10};
						var some := items->select(i | i > 5);
						log(some->size().repr());
					}
					""", QvtoExecutionContext.of(),
					QvtoEvaluationOptions.defaults().withMaxLoopIterations(5));

			assertFalse(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		}

		// Written first as items->xcollect(...)->size(): that form fails with "Unknown iterator:
		// xcollect" because an x-variant nested inside a larger expression is evaluated by the
		// OCL engine, which does not have QVT-O's iterators. Pre-existing, unrelated to the
		// limit, and reported separately.
		@Test
		@DisplayName("under the limit they run")
		void withinTheLimitTheyRun() throws QvtoParseException {
			QvtoExecutionResult result = execute("""
					transformation T();
					main() {
						var items := Sequence{1, 2, 3};
						var doubled := items->xcollect(i | i * 2);
						log(doubled->size().repr());
					}
					""", QvtoExecutionContext.of(),
					QvtoEvaluationOptions.defaults().withMaxLoopIterations(5));

			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		}
	}

	@Nested
	@DisplayName("§8.2.1.9: instantiation")
	class Instantiation {

		@Test
		@DisplayName("an abstract class cannot be instantiated")
		void abstractClassIsRefused() throws QvtoParseException {
			// An object expression naming an abstract class would produce an instance of
			// something the metamodel says has none — untested until now (#175)
			// EClassifier is abstract in Ecore, so no test metamodel of its own is needed
			QvtoExecutionResult result = executeWithExtents("""
					modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
					transformation abstractTest(inout m : ECORE) {
					    main() {
					        var made := object EClassifier {};
					    }
					}
					""", new BasicQvtoModelExtent());

			assertFalse(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getMessage().contains("Cannot instantiate")),
					() -> "diagnostics: " + result.diagnostics());
		}
	}

	@Nested
	@DisplayName("what a loaded unit is held to")
	class LoadValidation {

		@Test
		@DisplayName("an import stub without a name is refused")
		void importStubWithoutAName() throws Exception {
			// The two findings of QvtoUnitBinder.validate that QvtoLoadValidationTest does not
			// reach. A stub with no name cannot be bound to anything, so it must not travel
			CompiledUnit compiled = compileImporting();
			firstImportStub(compiled).setName(null);

			UnitPrepareException failure = assertThrows(UnitPrepareException.class,
					() -> engineWithResolver().unitBinder().validate(compiled));

			assertTrue(failure.getMessage().contains("import stub without a name"),
					failure::getMessage);
		}

		@Test
		@DisplayName("a transformation without a name is refused")
		void transformationWithoutAName() throws Exception {
			CompiledUnit compiled = compileImporting();
			((OperationalTransformation) compiled.getUnit()).setName(null);

			UnitPrepareException failure = assertThrows(UnitPrepareException.class,
					() -> engineWithResolver().unitBinder().validate(compiled));

			assertTrue(failure.getMessage().contains("no name"), failure::getMessage);
		}

		private QvtoEngine engineWithResolver() {
			return QvtoEngines.create(QvtoConfiguration.builder(
					OclConfiguration.builder(new OclParserSupport()).build())
					.addUnitResolver(name -> Optional.of(new QvtoUnit.SourceUnit(name,
							URI.createURI("mem:/" + name + ".qvto"), "library imported.lib;\n")))
					.unitResolverEnabled(true)
					.build());
		}

		private CompiledUnit compileImporting() throws Exception {
			return engineWithResolver().compile("""
					modeltype ECORE uses '%s';
					import imported.lib;
					transformation importer(inout m : ECORE) {
					    main() {
					    }
					}
					""".formatted(EcorePackage.eNS_URI), "importer",
					UnitCompileOptions.of(DependencyMode.REBIND));
		}

		private Module firstImportStub(CompiledUnit compiled) {
			OperationalTransformation transformation = (OperationalTransformation) compiled.getUnit();
			return transformation.getModuleImport().stream()
					.map(ModuleImport::getImportedModule)
					.filter(java.util.Objects::nonNull)
					.findFirst()
					.orElseThrow(() -> new AssertionError("no import stub in the compiled unit"));
		}
	}

	@Nested
	@DisplayName("D29: which blackbox libraries a transformation may reach")
	class BlackboxControls {

		private static final String IMPORTING = """
				modeltype ECORE uses 'http://www.eclipse.org/emf/2002/Ecore';
				import %s;
				transformation blackbox(inout m : ECORE) {
				    main() {
				    }
				}
				""";

		@Test
		@DisplayName("a module not on a non-empty allow-list is refused")
		void unlistedModuleIsRefused() throws Exception {
			QvtoExecutionResult result = run("lib.one", Set.of("lib.other"), 10);

			assertFalse(result.isSuccess(),
					"the allow-list names modules, and this one is not among them");
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getMessage().contains("lib.one")),
					() -> "the refused module is named: " + result.diagnostics());
		}

		@Test
		@DisplayName("a module on the allow-list resolves")
		void listedModuleResolves() throws Exception {
			QvtoExecutionResult result = run("lib.one", Set.of("lib.one"), 10);

			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
		}

		@Test
		@DisplayName("an empty allow-list restricts nothing")
		void emptyAllowListRestrictsNothing() throws Exception {
			// The enable flag is the gate; the list narrows once the gate is open (D29). The
			// opposite reading has been written down twice in this project, so it is pinned here
			// for blackboxes as QvtoAllowListSemanticsTest pins it for units.
			assertTrue(run("lib.one", Set.of(), 10).isSuccess());
		}

		@Test
		@DisplayName("the library ceiling is counted over the whole link")
		void maxBlackboxLibrariesCapsTheLink() throws Exception {
			// Counted over the link rather than per import, so a chain of imports cannot walk
			// past the ceiling one module at a time
			QvtoExecutionResult underTheCeiling =
					runImporting(List.of("lib.one", "lib.two"), Set.of(), 2);
			assertTrue(underTheCeiling.isSuccess(),
					() -> "two libraries under a ceiling of two: " + underTheCeiling.diagnostics());

			QvtoExecutionResult pastIt =
					runImporting(List.of("lib.one", "lib.two", "lib.three"), Set.of(), 2);
			assertFalse(pastIt.isSuccess(), "the third is past it");
			assertTrue(pastIt.diagnostics().stream()
					.anyMatch(d -> d.getMessage().contains("lib.three")),
					() -> "the module that did not fit is named: " + pastIt.diagnostics());
		}

		@Test
		@DisplayName("the enable flag is the gate, and it is shut by default")
		void disabledBlackboxesRefuseEvenAnAllowedModule() throws Exception {
			BasicQvtoBlackboxRegistry registry = registryWith("lib.one");
			QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(
					OclConfiguration.builder(new OclParserSupport()).build())
					.blackboxRegistry(registry)
					.allowedBlackboxModules(Set.of("lib.one"))
					// blackboxEnabled not set — defaults to false
					.build());

			assertFalse(execute(engine, List.of("lib.one")).isSuccess());
		}

		// --- helpers ---

		private QvtoExecutionResult run(String module, Set<String> allowed, int ceiling)
				throws Exception {
			return runImporting(List.of(module), allowed, ceiling);
		}

		private QvtoExecutionResult runImporting(List<String> modules, Set<String> allowed,
				int ceiling) throws Exception {
			BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
			modules.forEach(name -> registry.register(library(name)));
			QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(
					OclConfiguration.builder(new OclParserSupport()).build())
					.blackboxRegistry(registry)
					.blackboxEnabled(true)
					.allowedBlackboxModules(allowed)
					.maxBlackboxLibraries(ceiling)
					.build());
			return execute(engine, modules);
		}

		private BasicQvtoBlackboxRegistry registryWith(String module) {
			BasicQvtoBlackboxRegistry registry = new BasicQvtoBlackboxRegistry();
			registry.register(library(module));
			return registry;
		}

		private QvtoExecutionResult execute(QvtoEngine engine, List<String> modules)
				throws Exception {
			String source = IMPORTING.formatted(String.join(";\nimport ", modules));
			return engine.execute(engine.parse(source, "blackbox"),
					QvtoExecutionContext.of(new BasicQvtoModelExtent()));
		}

		/** A library offering one operation, named after its module. */
		private QvtoBlackboxLibrary library(String moduleName) {
			return new QvtoBlackboxLibrary() {

				@Override
				public String getModuleName() {
					return moduleName;
				}

				@Override
				public String getUnitQualifiedName() {
					return moduleName;
				}

				@Override
				public List<String> getUsedPackageURIs() {
					return List.of();
				}

				@Override
				public List<BlackboxOperationDescriptor> getOperationDescriptors() {
					BlackboxOperationDescriptor operation =
							QvtOperationalFactory.eINSTANCE.createBlackboxOperationDescriptor();
					operation.setName("greet" + moduleName.replace(".", ""));
					operation.setReturnType(EcorePackage.Literals.ESTRING);
					return List.of(operation);
				}

				@Override
				public Object invoke(String operationName, QvtoBlackboxInvocationContext context,
						Object[] args) {
					return moduleName;
				}
			};
		}
	}

	@Nested
	@DisplayName("§8.1.3.2: an in-parameter is read-only at the engine, not only at the API")
	class ReadOnlyInExtents {

		@Test
		@DisplayName("assigning to a property of an in-parameter object raises InvalidModelMutation")
		void propertyAssignmentOnAnInParameterIsRefused() throws QvtoParseException {
			// The extent API refuses mutation; this is the other half — an expression inside the
			// transformation reaching an object of an `in` extent and writing to it
			EObject source = createSourceElement("original", 1);
			QvtoExecutionResult result = executeWithExtents("""
					modeltype SRC uses 'http://test/source/1.0';
					modeltype TGT uses 'http://test/target/1.0';
					transformation readOnly(in s : SRC, out t : TGT) {
					    main() {
					        s.objectsOfType(SourceElement)->forEach(e) {
					            e.name := 'rewritten';
					        };
					    }
					}
					""", new BasicQvtoModelExtent(source), emptyExtent());

			assertFalse(result.isSuccess(), "writing to an in-parameter is not allowed");
			assertTrue(result.diagnostics().stream()
					.anyMatch(d -> d.getMessage().contains("read-only")),
					() -> "diagnostics: " + result.diagnostics());
			assertEquals("original", source.eGet(source.eClass().getEStructuralFeature("name")),
					"and the object is unchanged");
		}

		@Test
		@DisplayName("the same assignment on an inout parameter is allowed")
		void propertyAssignmentOnAnInoutParameterIsAllowed() throws QvtoParseException {
			EObject source = createSourceElement("original", 1);
			QvtoExecutionResult result = executeWithExtents("""
					modeltype SRC uses 'http://test/source/1.0';
					transformation writable(inout s : SRC) {
					    main() {
					        s.objectsOfType(SourceElement)->forEach(e) {
					            e.name := 'rewritten';
					        };
					    }
					}
					""", new BasicQvtoModelExtent(source));

			assertTrue(result.isSuccess(), () -> "diagnostics: " + result.diagnostics());
			assertEquals("rewritten", source.eGet(source.eClass().getEStructuralFeature("name")),
					"this is what the read-only case is being compared against");
		}
	}
}
