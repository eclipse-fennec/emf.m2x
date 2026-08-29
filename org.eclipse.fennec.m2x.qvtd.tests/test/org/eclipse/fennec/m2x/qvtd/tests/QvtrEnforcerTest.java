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
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.TypedModel;
import org.eclipse.fennec.m2x.model.qvtrelation.Key;
import org.eclipse.fennec.m2x.model.qvtrelation.QvtrelationFactory;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationalTransformation;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.QvttemplateFactory;
import org.eclipse.fennec.m2x.qvtd.api.BasicQvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.QvtdModelExtent;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrDiagnosticSink;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrEnforcer;
import org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrExtentManager;
import org.eclipse.fennec.m2x.utils.EcoreHelper;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link QvtrEnforcer}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtrEnforcerTest {

	private static EcoreHelper ecoreHelper;
	private static EPackage umlPackage;
	private static EPackage rdbmsPackage;

	@BeforeAll
	static void setUp() throws IOException {
		ecoreHelper = new EcoreHelper(QvtrEnforcerTest.class);
		umlPackage = ecoreHelper.loadEcore("simpleuml.ecore");
		rdbmsPackage = ecoreHelper.loadEcore("simplerdbms.ecore");
	}

	@AfterAll
	static void tearDown() {
		if (ecoreHelper != null) {
			ecoreHelper.releaseAll();
		}
	}

	// ── resolveFeature ───────────────────────────────────────────────

	@Test
	void resolveFeature_directFeature_resolves() {
		QvtrEnforcer enforcer = createEnforcer();
		EClass tableClass = ecoreHelper.getEClass(rdbmsPackage, "Table");
		EObject table = EcoreUtil.create(tableClass);

		PropertyTemplateItem item = QvttemplateFactory.eINSTANCE.createPropertyTemplateItem();
		item.setReferredProperty(tableClass.getEStructuralFeature("name"));

		EStructuralFeature resolved = enforcer.resolveFeature(item, table);
		assertNotNull(resolved);
		assertEquals("name", resolved.getName());
	}

	@Test
	void resolveFeature_nullProperty_returnsNull() {
		QvtrEnforcer enforcer = createEnforcer();
		EClass tableClass = ecoreHelper.getEClass(rdbmsPackage, "Table");
		EObject table = EcoreUtil.create(tableClass);

		PropertyTemplateItem item = QvttemplateFactory.eINSTANCE.createPropertyTemplateItem();
		// No referredProperty

		assertNull(enforcer.resolveFeature(item, table));
	}

	@Test
	void resolveFeature_byName_whenFeatureFromDifferentEClass() {
		QvtrEnforcer enforcer = createEnforcer();
		EClass tableClass = ecoreHelper.getEClass(rdbmsPackage, "Table");
		EClass schemaClass = ecoreHelper.getEClass(rdbmsPackage, "Schema");
		EObject table = EcoreUtil.create(tableClass);

		// Use the "name" feature from Schema (different EClass instance, same name)
		PropertyTemplateItem item = QvttemplateFactory.eINSTANCE.createPropertyTemplateItem();
		item.setReferredProperty(schemaClass.getEStructuralFeature("name"));

		EStructuralFeature resolved = enforcer.resolveFeature(item, table);
		assertNotNull(resolved, "Should resolve by name fallback");
		assertEquals("name", resolved.getName());
	}

	// ── resolveOppositeFeature ───────────────────────────────────────

	@Test
	void resolveOppositeFeature_withOpposite_resolves() {
		QvtrEnforcer enforcer = createEnforcer();
		EClass schemaClass = ecoreHelper.getEClass(rdbmsPackage, "Schema");
		EClass tableClass = ecoreHelper.getEClass(rdbmsPackage, "Table");

		// Schema.ownedTable <-> Table.schema
		EReference ownedTable = (EReference) schemaClass.getEStructuralFeature("ownedTable");
		assertNotNull(ownedTable.getEOpposite(), "ownedTable should have opposite");

		EStructuralFeature resolved = enforcer.resolveOppositeFeature(ownedTable, tableClass);
		assertNotNull(resolved);
		assertEquals("schema", resolved.getName());
	}

	// ── findKey ──────────────────────────────────────────────────────

	@Test
	void findKey_noKeys_returnsNull() {
		QvtrEnforcer enforcer = createEnforcer();
		EClass tableClass = ecoreHelper.getEClass(rdbmsPackage, "Table");

		assertNull(enforcer.findKey(tableClass), "No keys defined → null");
	}

	@Test
	void findKey_withMatchingKey_returnsKey() {
		EClass tableClass = ecoreHelper.getEClass(rdbmsPackage, "Table");
		RelationalTransformation t = QvtrelationFactory.eINSTANCE.createRelationalTransformation();

		Key key = QvtrelationFactory.eINSTANCE.createKey();
		key.setIdentifies(tableClass);
		key.getPart().add(tableClass.getEStructuralFeature("name"));
		t.getOwnedKey().add(key);

		QvtrEnforcer enforcer = createEnforcerWithTransformation(t);
		Key found = enforcer.findKey(tableClass);
		assertNotNull(found);
		assertSame(key, found);
	}

	// ── evaluatePropertyValue ────────────────────────────────────────

	@Test
	void evaluatePropertyValue_variableExp_resolvesClassNameFromBindings() {
		QvtrEnforcer enforcer = createEnforcer();

		// Simulate: cn bound from UML Class.name in source domain match
		EClass umlClass = ecoreHelper.getEClass(umlPackage, "Class");
		EObject umlEmployee = EcoreUtil.create(umlClass);
		umlEmployee.eSet(umlClass.getEStructuralFeature("name"), "Employee");

		Variable var = OclFactory.eINSTANCE.createVariable();
		var.setName("cn");

		VariableExp varExp = OclFactory.eINSTANCE.createVariableExp();
		varExp.setReferredVariable(var);

		PropertyTemplateItem item = QvttemplateFactory.eINSTANCE.createPropertyTemplateItem();
		item.setValue(varExp);

		// cn = "Employee" (extracted from umlEmployee.name by source matching)
		Map<String, Object> bindings = Map.of("c", umlEmployee, "cn", "Employee");
		Object result = enforcer.evaluatePropertyValue(item, bindings);
		assertEquals("Employee", result);
	}

	@Test
	void evaluatePropertyValue_variableExp_unboundReturnsNull() {
		QvtrEnforcer enforcer = createEnforcer();

		Variable var = OclFactory.eINSTANCE.createVariable();
		var.setName("unknown");

		VariableExp varExp = OclFactory.eINSTANCE.createVariableExp();
		varExp.setReferredVariable(var);

		PropertyTemplateItem item = QvttemplateFactory.eINSTANCE.createPropertyTemplateItem();
		item.setValue(varExp);

		Object result = enforcer.evaluatePropertyValue(item, Map.of());
		assertNull(result, "Unbound variable should return null");
	}

	@Test
	void evaluatePropertyValue_literalExp_delegatesToCallback() {
		// OCL callback that evaluates StringLiteralExp
		QvtrEnforcer enforcer = createEnforcerWithCallback(
				(expr, bindings) -> {
					if (expr instanceof StringLiteralExp sle) {
						return sle.getStringSymbol();
					}
					return null;
				});

		StringLiteralExp literal = OclFactory.eINSTANCE.createStringLiteralExp();
		literal.setStringSymbol("Persistent");

		PropertyTemplateItem item = QvttemplateFactory.eINSTANCE.createPropertyTemplateItem();
		item.setValue(literal);

		Object result = enforcer.evaluatePropertyValue(item, Map.of());
		assertEquals("Persistent", result);
	}

	// ── enforceObjectTemplate ────────────────────────────────────────

	@Test
	void enforceObjectTemplate_createsTableFromUmlClass() {
		EClass umlClass = ecoreHelper.getEClass(umlPackage, "Class");
		EClass tableClass = ecoreHelper.getEClass(rdbmsPackage, "Table");

		// Source: UML Class "Employee" (as if matched in source domain)
		EObject umlEmployee = EcoreUtil.create(umlClass);
		umlEmployee.eSet(umlClass.getEStructuralFeature("name"), "Employee");

		QvtdModelExtent rdbmsExtent = new BasicQvtdModelExtent();
		QvtrEnforcer enforcer = createEnforcerWithExtent("rdbms", rdbmsExtent);

		// Build target template: t : Table { name = cn }
		ObjectTemplateExp template = QvttemplateFactory.eINSTANCE.createObjectTemplateExp();
		template.setReferredClass(tableClass);

		Variable bindVar = OclFactory.eINSTANCE.createVariable();
		bindVar.setName("t");
		template.setBindsTo(bindVar);

		// Property: name = cn (VariableExp referencing "cn" from bindings)
		Variable cnVar = OclFactory.eINSTANCE.createVariable();
		cnVar.setName("cn");
		VariableExp cnVarExp = OclFactory.eINSTANCE.createVariableExp();
		cnVarExp.setReferredVariable(cnVar);

		PropertyTemplateItem nameItem = QvttemplateFactory.eINSTANCE.createPropertyTemplateItem();
		nameItem.setReferredProperty(tableClass.getEStructuralFeature("name"));
		nameItem.setValue(cnVarExp);
		template.getPart().add(nameItem);

		// Bindings from source domain match: c → UML Class, cn → "Employee"
		Map<String, Object> bindings = new HashMap<>();
		bindings.put("c", umlEmployee);
		bindings.put("cn", "Employee");

		TypedModel typedModel = createTypedModel("rdbms");

		EObject created = enforcer.enforceObjectTemplate(template, bindings, typedModel);
		assertNotNull(created, "Should create a new Table instance");
		assertEquals("Employee", created.eGet(tableClass.getEStructuralFeature("name")),
				"Table name should come from UML Class name via binding 'cn'");
		assertSame(created, bindings.get("t"), "Should bind variable 't'");
	}

	@Test
	void enforceObjectTemplate_abstractClass_returnsNullWithDiagnostic() {
		EClass abstractClass = EcoreFactory.eINSTANCE.createEClass();
		abstractClass.setName("Abstract");
		abstractClass.setAbstract(true);

		List<Diagnostic> diagnostics = new ArrayList<>();
		QvtrEnforcer enforcer = createEnforcerWithDiagnostics(diagnostics);

		ObjectTemplateExp template = QvttemplateFactory.eINSTANCE.createObjectTemplateExp();
		template.setReferredClass(abstractClass);

		TypedModel typedModel = createTypedModel("rdbms");
		EObject result = enforcer.enforceObjectTemplate(template, new HashMap<>(), typedModel);
		assertNull(result, "Should not instantiate abstract class");
		assertEquals(1, diagnostics.size(), "Should have one error diagnostic");
	}

	@Test
	void enforceObjectTemplate_alreadyBound_updatesExistingFromUmlSource() {
		EClass umlClass = ecoreHelper.getEClass(umlPackage, "Class");
		EClass tableClass = ecoreHelper.getEClass(rdbmsPackage, "Table");

		// Source: UML Class renamed from "OldName" to "Employee"
		EObject umlEmployee = EcoreUtil.create(umlClass);
		umlEmployee.eSet(umlClass.getEStructuralFeature("name"), "Employee");

		QvtdModelExtent rdbmsExtent = new BasicQvtdModelExtent();
		QvtrEnforcer enforcer = createEnforcerWithExtent("rdbms", rdbmsExtent);

		// Existing target table from previous enforcement
		EObject existing = EcoreUtil.create(tableClass);
		existing.eSet(tableClass.getEStructuralFeature("name"), "OldName");

		// Build target template: t : Table { name = cn }
		ObjectTemplateExp template = QvttemplateFactory.eINSTANCE.createObjectTemplateExp();
		template.setReferredClass(tableClass);

		Variable bindVar = OclFactory.eINSTANCE.createVariable();
		bindVar.setName("t");
		template.setBindsTo(bindVar);

		// Property: name = cn (from source binding)
		Variable cnVar = OclFactory.eINSTANCE.createVariable();
		cnVar.setName("cn");
		VariableExp cnVarExp = OclFactory.eINSTANCE.createVariableExp();
		cnVarExp.setReferredVariable(cnVar);

		PropertyTemplateItem nameItem = QvttemplateFactory.eINSTANCE.createPropertyTemplateItem();
		nameItem.setReferredProperty(tableClass.getEStructuralFeature("name"));
		nameItem.setValue(cnVarExp);
		template.getPart().add(nameItem);

		// Bindings: t already bound to existing table, cn from UML source match
		Map<String, Object> bindings = new HashMap<>();
		bindings.put("t", existing);
		bindings.put("c", umlEmployee);
		bindings.put("cn", "Employee");

		TypedModel typedModel = createTypedModel("rdbms");
		EObject result = enforcer.enforceObjectTemplate(template, bindings, typedModel);
		assertSame(existing, result, "Should reuse existing object");
		assertEquals("Employee", existing.eGet(tableClass.getEStructuralFeature("name")),
				"Table name should be updated from UML Class name");
	}

	// ── Helpers ──────────────────────────────────────────────────────

	private QvtrEnforcer createEnforcer() {
		return createEnforcerWithTransformation(
				QvtrelationFactory.eINSTANCE.createRelationalTransformation());
	}

	private QvtrEnforcer createEnforcerWithTransformation(RelationalTransformation t) {
		return new QvtrEnforcer(null, null, t, QvtrDiagnosticSink.into(new ArrayList<>()),
				(expr, bindings) -> {
					if (expr instanceof StringLiteralExp sle) {
						return sle.getStringSymbol();
					}
					return null;
				}, null);
	}

	private QvtrEnforcer createEnforcerWithCallback(
			org.eclipse.fennec.m2x.qvtd.engine.internal.QvtrOclCallback callback) {
		return new QvtrEnforcer(null, null,
				QvtrelationFactory.eINSTANCE.createRelationalTransformation(),
				QvtrDiagnosticSink.into(new ArrayList<>()), callback, null);
	}

	private QvtrEnforcer createEnforcerWithDiagnostics(List<Diagnostic> diagnostics) {
		return new QvtrEnforcer(null, null,
				QvtrelationFactory.eINSTANCE.createRelationalTransformation(),
				QvtrDiagnosticSink.into(diagnostics),
				(expr, bindings) -> {
					if (expr instanceof StringLiteralExp sle) {
						return sle.getStringSymbol();
					}
					return null;
				}, null);
	}

	private QvtrEnforcer createEnforcerWithExtent(String modelName, QvtdModelExtent extent) {
		RelationalTransformation t = QvtrelationFactory.eINSTANCE.createRelationalTransformation();
		t.setName("test");

		// Create a TypedModel on the transformation
		org.eclipse.fennec.m2x.model.qvtbase.TypedModel tm =
				org.eclipse.fennec.m2x.model.qvtbase.QvtbaseFactory.eINSTANCE.createTypedModel();
		tm.setName(modelName);
		t.getModelParameter().add(tm);

		QvtdExecutionContext ctx = QvtdExecutionContext.enforce(modelName,
				Map.of(modelName, extent));
		QvtrExtentManager extentManager = new QvtrExtentManager(t, ctx);

		return new QvtrEnforcer(extentManager, null, t, QvtrDiagnosticSink.into(new ArrayList<>()),
				(expr, bindings) -> {
					if (expr instanceof StringLiteralExp sle) {
						return sle.getStringSymbol();
					}
					return null;
				}, null);
	}

	private TypedModel createTypedModel(String name) {
		TypedModel tm = org.eclipse.fennec.m2x.model.qvtbase.QvtbaseFactory.eINSTANCE.createTypedModel();
		tm.setName(name);
		return tm;
	}
}
