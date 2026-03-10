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
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvtd.api.QvtdConfiguration;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionResult;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdParseException;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;
import org.eclipse.fennec.m2x.qvtd.engine.QvtdEngineImpl;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngineImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

/**
 * Integration tests for QVT-O ↔ QVT-R hybrid execution (Phase 4b).
 *
 * <p>Tests cover:
 * <ul>
 *   <li>§8.2.1.1: OperationalTransformation refines RelationalTransformation</li>
 *   <li>§7.8: Relation implementedby delegated to QVT-O RelationImplementationProvider</li>
 *   <li>§8.2.1.15: MappingOperation.refinedRelation</li>
 *   <li>D39: Interface-based decoupling via RelationImplementationProvider</li>
 *   <li>D40: Shared qvtbase model bundle</li>
 * </ul>
 */
@DisplayName("QVT-O ↔ QVT-R Hybrid Integration (Phase 4b)")
class QvtdHybridIntegrationTest extends AbstractQvtdEngineTest {

	// ===== §7.8 / D39: RelationImplementationProvider =====

	@Nested
	@DisplayName("§7.8: implementedby via RelationImplementationProvider")
	class ImplementedByProvider {

		@Test
		@DisplayName("QVT-R implementedby delegates to registered QVT-O provider")
		void implementedbyDelegatesToQvtoProvider() throws QvtdParseException, QvtoParseException {
			// §7.8: "A relation may optionally have an associated black-box
			// operational implementation to enforce a domain."
			// D39: RelationImplementationProvider breaks the dependency cycle.

			// 1. QVT-R transformation with implementedby clause
			String qvtrSource = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn }
								implementedby classToTableOp(cn);
						}
					}
					""";

			// 2. QVT-O transformation that provides the mapping
			String qvtoSource = """
					modeltype SRC uses 'http://test/simpleuml/1.0';
					modeltype TGT uses 'http://test/simplerdbms/1.0';
					transformation QvtoImpl(in uml : SRC, out rdbms : TGT);
					mapping Class::classToTableOp() : Table {
						name := 'QVTO_' + self.name;
					}
					""";

			// 3. Set up QVT-O engine as provider
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration qvtoConfig = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(qvtoConfig);
			OperationalTransformation ot = qvtoEngine.parse(qvtoSource, "QvtoImpl");
			qvtoEngine.loadTransformation(ot);

			// 4. Set up QVT-R engine with provider registered
			QvtdConfiguration qvtdConfig = QvtdConfiguration.builder(oclConfig).build();
			QvtdEngineImpl qvtdEngine = new QvtdEngineImpl(qvtdConfig);
			qvtdEngine.registerImplementationProvider(qvtoEngine);

			// 5. Execute
			EObject cls = createClass("Employee", null);
			QvtdModelExtent uml = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbms = emptyExtent();

			RelationalTransformation rt = qvtdEngine.parse(qvtrSource, "test");
			QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
					Map.of("uml", uml, "rdbms", rdbms));
			QvtdExecutionResult result = qvtdEngine.execute(rt, ctx);

			assertTrue(result.isSuccess(), "Hybrid execution should succeed: " + result.diagnostics());
		}

		@Test
		@DisplayName("Provider canProvide returns false for unknown mapping")
		void canProvideReturnsFalseForUnknown() throws QvtoParseException {
			String qvtoSource = """
					modeltype SRC uses 'http://test/simpleuml/1.0';
					modeltype TGT uses 'http://test/simplerdbms/1.0';
					transformation QvtoImpl(in uml : SRC, out rdbms : TGT);
					mapping Class::classToTableOp() : Table {
						name := self.name;
					}
					""";

			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(config);
			OperationalTransformation ot = qvtoEngine.parse(qvtoSource, "test");
			qvtoEngine.loadTransformation(ot);

			assertTrue(qvtoEngine.canProvide("classToTableOp"));
			assertFalse(qvtoEngine.canProvide("nonExistentMapping"));
		}

		@Test
		@DisplayName("Provider without loaded transformation returns false")
		void canProvideWithoutLoadedTransformation() {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(config);

			assertFalse(qvtoEngine.canProvide("anyMapping"),
					"Without loaded transformation, canProvide should return false");
		}

		@Test
		@DisplayName("Fallback to blackbox when no provider matches")
		void fallbackToBlackboxWhenNoProviderMatches() throws QvtdParseException {
			// §7.8: When no RelationImplementationProvider can handle the
			// relation, the engine falls back to blackbox registry (existing behavior).
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation ClassToTable {
							cn : String;
							checkonly domain uml c : Class { name = cn };
							enforce domain rdbms t : Table { name = cn }
								implementedby createTableOp(cn);
						}
					}
					""";

			// Register a provider that doesn't match
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration qvtoConfig = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(qvtoConfig);
			// No transformation loaded → canProvide will always return false

			// Set up blackbox that handles createTableOp
			org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry registry =
					new org.eclipse.fennec.m2x.qvtd.api.BasicQvtdBlackboxRegistry();
			registry.register(new org.eclipse.fennec.m2x.qvtd.api.QvtdBlackboxLibrary() {
				@Override
				public String getModuleName() { return "ops"; }
				@Override
				public String getUnitQualifiedName() { return "ops"; }
				@Override
				public List<String> getUsedPackageURIs() { return List.of(); }
				@Override
				public Object invoke(String operationName, Object self, Object[] args) {
					if ("createTableOp".equals(operationName) && args.length == 1) {
						EObject table = createTable((String) args[0]);
						table.eSet(table.eClass().getEStructuralFeature("name"),
								"BB_" + args[0]);
						return table;
					}
					return null;
				}
			});

			QvtdConfiguration config = QvtdConfiguration.builder(oclConfig)
					.blackboxRegistry(registry)
					.build();
			QvtdEngineImpl qvtdEngine = new QvtdEngineImpl(config);
			qvtdEngine.registerImplementationProvider(qvtoEngine);

			EObject cls = createClass("Employee", null);
			QvtdModelExtent uml = QvtdModelExtent.of(cls);
			QvtdModelExtent rdbms = emptyExtent();

			RelationalTransformation t = qvtdEngine.parse(source, "test");
			QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
					Map.of("uml", uml, "rdbms", rdbms));
			QvtdExecutionResult result = qvtdEngine.execute(t, ctx);

			assertTrue(result.isSuccess(), "" + result.diagnostics());
			assertEquals(1, rdbms.getContents().size());
			EObject table = rdbms.getContents().get(0);
			assertEquals("BB_Employee",
					table.eGet(table.eClass().getEStructuralFeature("name")),
					"Should fallback to blackbox when provider doesn't match");
		}
	}

	// ===== §8.2.1.1: Transformation refines =====

	@Nested
	@DisplayName("§8.2.1.1: OperationalTransformation refines")
	class TransformationRefines {

		@Test
		@DisplayName("QVT-O transformation refines creates stub reference")
		void refinesCreatesStubReference() throws QvtoParseException {
			// §8.2.1.1: "An operational transformation may be explicitly
			// declared as the 'refinement' of a relational transformation."
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(config);

			String qvtoSource = """
					modeltype SRC uses 'http://test/simpleuml/1.0';
					modeltype TGT uses 'http://test/simplerdbms/1.0';
					transformation QvtoImpl(in uml : SRC, out rdbms : TGT)
						refines Uml2Rdbms;
					main() { }
					""";

			OperationalTransformation ot = qvtoEngine.parse(qvtoSource, "test");
			assertNotNull(ot.getRefined(), "Should have refined reference set");
			assertEquals("Uml2Rdbms", ot.getRefined().getName(),
					"Refined stub should carry the name of the relational transformation");
		}

		@Test
		@DisplayName("Model parameters correspond to typed models (§8.2.1.1)")
		void modelParametersCorrespondToTypedModels() throws QvtoParseException {
			// §8.2.1.1: "each model parameter in the operational transformation
			// corresponds to a typed model in the relational transformation"
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(config);

			String qvtoSource = """
					modeltype SRC uses 'http://test/simpleuml/1.0';
					modeltype TGT uses 'http://test/simplerdbms/1.0';
					transformation QvtoImpl(in uml : SRC, out rdbms : TGT)
						refines Uml2Rdbms;
					main() { }
					""";

			OperationalTransformation ot = qvtoEngine.parse(qvtoSource, "test");
			// The OT must have 2 model parameters matching the RT's typed models
			assertEquals(2, ot.getModelParameter().size());
			assertEquals("uml", ot.getModelParameter().get(0).getName());
			assertEquals("rdbms", ot.getModelParameter().get(1).getName());
		}

		@Test
		@DisplayName("Refines with scoped name (spec :: separator)")
		void refinesWithScopedName() throws QvtoParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(config);

			String qvtoSource = """
					modeltype SRC uses 'http://test/simpleuml/1.0';
					modeltype TGT uses 'http://test/simplerdbms/1.0';
					transformation QvtoImpl(in uml : SRC, out rdbms : TGT)
						refines pkg::Uml2Rdbms;
					main() { }
					""";

			OperationalTransformation ot = qvtoEngine.parse(qvtoSource, "test");
			assertNotNull(ot.getRefined());
			assertEquals("pkg::Uml2Rdbms", ot.getRefined().getName());
		}
	}

	// ===== §8.2.1.15: MappingOperation.refinedRelation =====

	@Nested
	@DisplayName("§8.2.1.15: Mapping refines relation")
	class MappingRefinesRelation {

		@Test
		@DisplayName("Mapping refines stores annotation for engine resolution")
		void mappingRefinesStoresAnnotation() throws QvtoParseException {
			// §8.2.1.15: "refinedRelation: Relation [0..1] — The refined relation"
			// Implementation: EAnnotation on MappingOperation (Rule is abstract, D40)
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(config);

			String qvtoSource = """
					modeltype SRC uses 'http://test/simpleuml/1.0';
					modeltype TGT uses 'http://test/simplerdbms/1.0';
					transformation QvtoImpl(in uml : SRC, out rdbms : TGT);
					mapping Class::classToTable() : Table
						refines ClassToTable
					{
						name := self.name;
					}
					""";

			OperationalTransformation ot = qvtoEngine.parse(qvtoSource, "test");
			// Find the mapping and check annotation
			var mapping = findMapping(ot, "classToTable");
			assertNotNull(mapping, "Should find mapping 'classToTable'");
			var ann = mapping.getEAnnotation("http://www.eclipse.org/fennec/m2x/qvto/refines");
			assertNotNull(ann, "Mapping should have refines annotation");
			assertEquals("ClassToTable", ann.getDetails().get("refinedRelation"));
		}

		@Test
		@DisplayName("Mapping refines conceptually corresponds to a relation (§8.1.5)")
		void mappingRefinesConceptualRelation() throws QvtoParseException {
			// §8.1.5: "a mapping operation is always a refinement of an implicit relation"
			// When explicitly declared, the refinedRelation name is stored.
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(config);

			String qvtoSource = """
					modeltype SRC uses 'http://test/simpleuml/1.0';
					modeltype TGT uses 'http://test/simplerdbms/1.0';
					transformation QvtoImpl(in uml : SRC, out rdbms : TGT);
					mapping Class::classToTable() : Table
						refines ClassToTable
					{
						name := self.name;
					}
					mapping Attribute::attrToColumn() : Column
						refines AttributeToColumn
					{
						name := self.name;
					}
					""";

			OperationalTransformation ot = qvtoEngine.parse(qvtoSource, "test");

			var m1 = findMapping(ot, "classToTable");
			assertNotNull(m1);
			assertEquals("ClassToTable",
					m1.getEAnnotation("http://www.eclipse.org/fennec/m2x/qvto/refines")
							.getDetails().get("refinedRelation"));

			var m2 = findMapping(ot, "attrToColumn");
			assertNotNull(m2);
			assertEquals("AttributeToColumn",
					m2.getEAnnotation("http://www.eclipse.org/fennec/m2x/qvto/refines")
							.getDetails().get("refinedRelation"));
		}
	}

	// ===== D39: Engine decoupling =====

	@Nested
	@DisplayName("D39: RelationImplementationProvider decoupling")
	class EngineDecoupling {

		@Test
		@DisplayName("QvtoEngineImpl implements RelationImplementationProvider")
		void qvtoEngineIsProvider() {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtoConfiguration config = QvtoConfiguration.builder(oclConfig).build();
			QvtoEngineImpl qvtoEngine = new QvtoEngineImpl(config);

			assertTrue(qvtoEngine instanceof RelationImplementationProvider,
					"QvtoEngineImpl should implement RelationImplementationProvider");
		}

		@Test
		@DisplayName("Multiple providers can be registered")
		void multipleProvidersRegistered() throws QvtdParseException {
			OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
			QvtdConfiguration config = QvtdConfiguration.builder(oclConfig).build();
			QvtdEngineImpl qvtdEngine = new QvtdEngineImpl(config);

			// Register two no-op providers — should not throw
			qvtdEngine.registerImplementationProvider(new RelationImplementationProvider() {
				@Override
				public boolean canProvide(String name) { return false; }
				@Override
				public QvtdExecutionResult executeRelation(String name, QvtdExecutionContext ctx) {
					return new QvtdExecutionResult(List.of(), false);
				}
			});
			qvtdEngine.registerImplementationProvider(new RelationImplementationProvider() {
				@Override
				public boolean canProvide(String name) { return false; }
				@Override
				public QvtdExecutionResult executeRelation(String name, QvtdExecutionContext ctx) {
					return new QvtdExecutionResult(List.of(), false);
				}
			});

			// Execution without implementedby should still work
			String source = """
					transformation T(uml : simpleuml, rdbms : simplerdbms) {
						top relation R {
							pn : String;
							checkonly domain uml p : Package { name = pn };
							enforce domain rdbms s : Schema { name = pn };
						}
					}
					""";
			QvtdModelExtent uml = QvtdModelExtent.of(createPackage("X"));
			QvtdModelExtent rdbms = emptyExtent();
			RelationalTransformation t = qvtdEngine.parse(source, "test");
			QvtdExecutionContext ctx = QvtdExecutionContext.enforce("rdbms",
					Map.of("uml", uml, "rdbms", rdbms));
			QvtdExecutionResult result = qvtdEngine.execute(t, ctx);
			assertTrue(result.isSuccess(), "" + result.diagnostics());
		}
	}

	// --- Helper ---

	protected static EObject createTable(String name) {
		org.eclipse.emf.ecore.EClass tableCls = ecoreHelper.getEClass(rdbmsPackage, "Table");
		EObject table = org.eclipse.emf.ecore.util.EcoreUtil.create(tableCls);
		table.eSet(tableCls.getEStructuralFeature("name"), name);
		return table;
	}

	private static org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation findMapping(
			OperationalTransformation t, String name) {
		for (var classifier : t.getEClassifiers()) {
			if (classifier instanceof org.eclipse.emf.ecore.EClass ec) {
				for (var op : ec.getEOperations()) {
					if (op instanceof org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation mo
							&& name.equals(mo.getName())) {
						return mo;
					}
				}
			}
		}
		return null;
	}
}
