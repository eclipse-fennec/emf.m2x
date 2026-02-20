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
package org.eclipse.fennec.m2m.qvto.engine.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2m.model.imperativeocl.AltExp;
import org.eclipse.fennec.m2m.model.imperativeocl.AssertExp;
import org.eclipse.fennec.m2m.model.imperativeocl.AssignExp;
import org.eclipse.fennec.m2m.model.imperativeocl.BlockExp;
import org.eclipse.fennec.m2m.model.imperativeocl.BreakExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ComputeExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ContinueExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ForExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ImperativeIterateExp;
import org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp;
import org.eclipse.fennec.m2m.model.imperativeocl.LogExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ReturnExp;
import org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind;
import org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp;
import org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp;
import org.eclipse.fennec.m2m.model.imperativeocl.WhileExp;
import org.eclipse.fennec.m2m.model.imperativeocl.util.ImperativeOclSwitch;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2m.model.ocl.Variable;
import org.eclipse.fennec.m2m.model.ocl.VariableExp;
import org.eclipse.fennec.m2m.model.qvtoperational.ConstructorBody;
import org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2m.model.ocl.ClassifierType;
import org.eclipse.fennec.m2m.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2m.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingCallExp;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.ObjectExp;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationBody;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveExp;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveInExp;
import org.eclipse.fennec.m2m.model.qvtoperational.VarParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.util.QvtOperationalSwitch;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2m.ocl.api.OclResult;
import org.eclipse.fennec.m2m.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2m.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.engine.internal.QvtoControlFlowException.BreakException;
import org.eclipse.fennec.m2m.qvto.engine.internal.QvtoControlFlowException.ContinueException;
import org.eclipse.fennec.m2m.qvto.engine.internal.QvtoControlFlowException.ReturnException;

/**
 * Imperative AST interpreter for QVT-O transformations.
 *
 * <p>Uses a 3-level switch dispatch: ImperativeOCL → QvtOperational → OCL delegation.
 * OCL sub-expressions are delegated to {@link OclEngineImpl} via snapshot contexts.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoEvaluator {

	private static final String SOURCE_ID = "org.eclipse.fennec.m2m.qvto.engine";

	/**
	 * Internal sentinel indicating the switch did not handle the node.
	 * Both inner switches return this from defaultCase().
	 */
	private static final Object UNHANDLED = new Object();

	/** Sentinel for wrapping null values (EMF switches treat null as "not handled"). */
	private static final Object WRAPPED_NULL = new Object();

	private final OclEngineImpl oclEngine;
	private final QvtoEvalEnvironment env;
	private final QvtoEvaluationOptions options;
	private final OperationalTransformation transformation;
	private final QvtoExtentManager extentManager;
	private final QvtoTraceManager traceManager = new QvtoTraceManager();
	private final List<Diagnostic> diagnostics = new ArrayList<>();
	private final ImperativeDispatch imperativeSwitch = new ImperativeDispatch();
	private final QvtOpDispatch qvtOpSwitch = new QvtOpDispatch();
	private int stackDepth;

	/**
	 * Creates a new evaluator for a single transformation execution.
	 */
	public QvtoEvaluator(OclEngineImpl oclEngine, QvtoEvalEnvironment env,
			QvtoEvaluationOptions options, OperationalTransformation transformation,
			QvtoExtentManager extentManager) {
		this.oclEngine = Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		this.env = Objects.requireNonNull(env, "env must not be null");
		this.options = Objects.requireNonNull(options, "options must not be null");
		this.transformation = Objects.requireNonNull(transformation, "transformation must not be null");
		this.extentManager = Objects.requireNonNull(extentManager, "extentManager must not be null");
	}

	/**
	 * Executes the transformation's main() entry operation.
	 *
	 * @return the collected diagnostics
	 */
	public List<Diagnostic> execute() {
		// Bind model parameters as variables (e.g. 'in s : SRC' → extent)
		EList<ModelParameter> modelParams = transformation.getModelParameter();
		for (int i = 0; i < modelParams.size(); i++) {
			ModelParameter mp = modelParams.get(i);
			QvtoModelExtent extent = extentManager.getExtent(i);
			if (mp.getName() != null && extent != null) {
				env.define(mp.getName(), extent);
			}
		}

		ImperativeOperation mainOp = findMainOperation();
		if (mainOp == null) {
			addError("No main() entry operation found in transformation '"
					+ transformation.getName() + "'");
			return diagnostics;
		}
		try {
			callOperation(mainOp, null, new Object[0]);
		} catch (ReturnException e) {
			// main() returned — normal
		}
		return diagnostics;
	}

	/**
	 * Evaluates a QVT-O expression using 3-level dispatch.
	 */
	Object eval(OclExpression expr) {
		if (expr == null) {
			return null;
		}

		// 1. ImperativeOCL switch
		Object result = imperativeSwitch.doSwitch(expr);
		if (result != UNHANDLED) {
			return unwrapNull(result);
		}

		// 2. QvtOperational switch (Phase B: mappings, object exp, etc.)
		result = qvtOpSwitch.doSwitch(expr);
		if (result != UNHANDLED) {
			return unwrapNull(result);
		}

		// 3. QVT-O model extent operations (objectsOfType, objects)
		if (expr instanceof OperationCallExp opCall) {
			result = handleExtentOperation(opCall);
			if (result != UNHANDLED) {
				return unwrapNull(result);
			}
		}

		// 4. OCL delegation
		return evalOcl(expr);
	}

	/**
	 * Delegates an expression to the OCL evaluator with a snapshot of the
	 * current QVT-O environment as OclContext.
	 */
	private Object evalOcl(OclExpression expr) {
		Map<String, Object> vars = env.allVisibleVariables();
		EObject self = null;
		Object selfObj = vars.get("self");
		if (selfObj instanceof EObject eo) {
			self = eo;
		}
		OclContext oclCtx = self != null
				? OclContext.of(self, vars)
				: OclContext.of(vars);
		// Use LENIENT null handling for QVT-O — module-level operations have no self
		OclEvaluationOptions oclOpts = OclEvaluationOptions.lenient();
		OclResult oclResult = oclEngine.evaluateWithDiagnostics(expr, oclCtx, oclOpts);
		diagnostics.addAll(oclResult.diagnostics());
		return oclResult.value();
	}

	/**
	 * Handles QVT-O model extent operations: objectsOfType, objects.
	 * Returns {@link #UNHANDLED} if the expression is not an extent operation.
	 */
	private Object handleExtentOperation(OperationCallExp opCall) {
		if (opCall.getOwnedSource() == null) {
			return UNHANDLED;
		}
		Object source = eval(opCall.getOwnedSource());
		if (!(source instanceof QvtoModelExtent extent)) {
			return UNHANDLED;
		}

		String opName = opCall.getName();
		if ("objectsOfType".equals(opName) && !opCall.getOwnedArguments().isEmpty()) {
			Object typeArg = eval(opCall.getOwnedArguments().get(0));
			EClass filterType = null;
			if (typeArg instanceof EClass ec) {
				filterType = ec;
			} else if (typeArg instanceof ClassifierType ct
					&& ct.getReferredClassifier() instanceof EClass ec) {
				filterType = ec;
			}
			if (filterType != null) {
				EClass ft = filterType;
				List<EObject> result = new ArrayList<>();
				for (EObject eo : extent.getContents()) {
					if (ft.isInstance(eo)) {
						result.add(eo);
					}
				}
				return wrapNull(result);
			}
			return wrapNull(new ArrayList<>(extent.getContents()));
		}
		if ("objects".equals(opName)) {
			return wrapNull(new ArrayList<>(extent.getContents()));
		}
		return UNHANDLED;
	}

	/**
	 * Calls an imperative operation (helper/query/mapping entry).
	 *
	 * @param operation the operation to call
	 * @param self the context object (may be null)
	 * @param args the operation arguments
	 * @return the operation result
	 */
	Object callOperation(ImperativeOperation operation, Object self, Object[] args) {
		if (operation instanceof MappingOperation mappingOp) {
			return callMapping(mappingOp, self, args);
		}
		if (++stackDepth > options.maxStackDepth()) {
			--stackDepth;
			addError("Maximum stack depth exceeded: " + options.maxStackDepth());
			return null;
		}
		try {
			env.pushScope();
			bindOperationParams(operation, self, args);

			// Execute body
			OperationBody body = operation.getBody();
			Object lastResult = null;
			if (body != null) {
				for (OclExpression expr : body.getContent()) {
					lastResult = eval(expr);
				}
			}

			// Return result variable value (if exists), otherwise last expression value
			EList<VarParameter> resultParams = operation.getResult();
			if (!resultParams.isEmpty()) {
				String resultName = resultParams.get(0).getName();
				return env.lookup(resultName);
			}
			return lastResult;
		} catch (ReturnException e) {
			return e.value;
		} finally {
			env.popScope();
			--stackDepth;
		}
	}

	/**
	 * Executes a mapping operation with full mapping semantics:
	 * when-guard, init/population/end, result creation, where-postcondition, trace recording.
	 */
	private Object callMapping(MappingOperation mappingOp, Object self, Object[] args) {
		// Disjuncts: try each disjunct, first with passing when-guard wins
		EList<MappingOperation> disjuncts = mappingOp.getDisjunct();
		if (disjuncts != null && !disjuncts.isEmpty()) {
			for (MappingOperation disjunct : disjuncts) {
				Object result = callMapping(disjunct, self, args);
				if (result != null) {
					return result;
				}
			}
			return null;
		}

		if (++stackDepth > options.maxStackDepth()) {
			--stackDepth;
			addError("Maximum stack depth exceeded: " + options.maxStackDepth());
			return null;
		}
		try {
			env.pushScope();
			bindOperationParams(mappingOp, self, args);

			// when-guard: all must be true, otherwise skip
			for (OclExpression whenExpr : mappingOp.getWhen()) {
				Object whenVal = eval(whenExpr);
				if (!Boolean.TRUE.equals(whenVal)) {
					return null;
				}
			}

			// Create result objects for output parameters with EClass type
			EList<VarParameter> resultParams = mappingOp.getResult();
			for (VarParameter rp : resultParams) {
				Object existing = env.lookup(rp.getName());
				if (existing == null) {
					EClassifier type = rp.getEType();
					if (type instanceof EClass eClass && !eClass.isAbstract()) {
						EObject created = EcoreUtil.create(eClass);
						env.assign(rp.getName(), created);
						if (resultParams.size() == 1) {
							env.assign("result", created);
						}
						addToDefaultExtent(created);
					}
				}
			}

			// Execute mapping body: init → population → end
			OperationBody body = mappingOp.getBody();
			if (body instanceof MappingBody mappingBody) {
				for (OclExpression initExpr : mappingBody.getInitSection()) {
					eval(initExpr);
				}
				for (OclExpression contentExpr : mappingBody.getContent()) {
					eval(contentExpr);
				}
				// Inherited mappings: execute only population section
				for (MappingOperation inherited : mappingOp.getInherited()) {
					executePopulationOnly(inherited, self, args);
				}
				// Merged mappings: execute only population section
				for (MappingOperation merged : mappingOp.getMerged()) {
					executePopulationOnly(merged, self, args);
				}
				for (OclExpression endExpr : mappingBody.getEndSection()) {
					eval(endExpr);
				}
			} else if (body != null) {
				for (OclExpression expr : body.getContent()) {
					eval(expr);
				}
			}

			// where-postcondition
			OclExpression whereExpr = mappingOp.getWhere();
			if (whereExpr != null) {
				Object whereVal = eval(whereExpr);
				if (!Boolean.TRUE.equals(whereVal)) {
					addWarning("Mapping '" + mappingOp.getName()
							+ "': where-postcondition failed");
				}
			}

			// Collect result
			Object result = null;
			if (!resultParams.isEmpty()) {
				result = env.lookup(resultParams.get(0).getName());
			}

			// Record trace
			traceManager.addRecord(mappingOp.getName(), self, result);

			return result;
		} catch (ReturnException e) {
			Object result = e.value;
			if (result == null && !mappingOp.getResult().isEmpty()) {
				result = env.lookup(mappingOp.getResult().get(0).getName());
			}
			traceManager.addRecord(mappingOp.getName(), self, result);
			return result;
		} finally {
			env.popScope();
			--stackDepth;
		}
	}

	/**
	 * Executes only the population section of a mapping body (for inherited/merged).
	 */
	private void executePopulationOnly(MappingOperation mapping, Object self, Object[] args) {
		OperationBody body = mapping.getBody();
		if (body instanceof MappingBody mappingBody) {
			for (OclExpression expr : mappingBody.getContent()) {
				eval(expr);
			}
		} else if (body != null) {
			for (OclExpression expr : body.getContent()) {
				eval(expr);
			}
		}
	}

	/**
	 * Binds operation parameters (self, formals, results) in the current scope.
	 */
	private void bindOperationParams(ImperativeOperation operation, Object self, Object[] args) {
		// Bind 'self' if context parameter exists
		if (self != null) {
			env.define("self", self);
		}
		VarParameter ctxParam = operation.getContext();
		if (ctxParam != null && self != null) {
			env.define(ctxParam.getName(), self);
		}

		// Bind parameters
		EList<VarParameter> resultParams = operation.getResult();
		List<?> formalParams = operation.getEParameters();
		int argIdx = 0;
		for (int i = 0; i < formalParams.size(); i++) {
			Object param = formalParams.get(i);
			if (param instanceof VarParameter vp) {
				if (resultParams.contains(vp)) {
					continue;
				}
				Object value = argIdx < args.length ? args[argIdx++] : null;
				env.define(vp.getName(), value);
			}
		}

		// Bind result variables
		for (VarParameter rp : resultParams) {
			Object initValue = null;
			if (rp.getOwnedInit() != null) {
				initValue = eval(rp.getOwnedInit());
			}
			env.define(rp.getName(), initValue);
			if (resultParams.size() == 1) {
				env.define("result", initValue);
			}
		}
	}

	/**
	 * Adds an EObject to the default output extent if available.
	 */
	private void addToDefaultExtent(EObject eObject) {
		QvtoModelExtent extent = extentManager.getDefaultOutputExtent();
		if (extent != null) {
			extent.add(eObject);
		}
	}

	/**
	 * Returns the collected diagnostics.
	 */
	public List<Diagnostic> getDiagnostics() {
		return diagnostics;
	}

	// --- Internal helpers ---

	private ImperativeOperation findMainOperation() {
		// Find the module EClass (named like the transformation)
		EClass moduleClass = findModuleClass();
		if (moduleClass == null) {
			return null;
		}
		for (EOperation op : moduleClass.getEOperations()) {
			if (op instanceof ImperativeOperation impOp && "main".equals(op.getName())) {
				return impOp;
			}
		}
		return null;
	}

	private EClass findModuleClass() {
		String name = transformation.getName();
		return transformation.getEClassifiers().stream()
				.filter(EClass.class::isInstance)
				.map(EClass.class::cast)
				.filter(c -> c.getName().equals(name))
				.findFirst()
				.orElse(null);
	}

	/**
	 * Finds an imperative operation by name in the transformation's module class.
	 */
	ImperativeOperation findOperation(String name, Object contextType) {
		EClass moduleClass = findModuleClass();
		if (moduleClass == null) {
			return null;
		}
		for (EOperation op : moduleClass.getEOperations()) {
			if (op instanceof ImperativeOperation impOp && name.equals(op.getName())) {
				return impOp;
			}
		}
		return null;
	}

	private void addError(String message) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.ERROR, SOURCE_ID, 0, message, null));
	}

	private void addWarning(String message) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0, message, null));
	}

	private void addInfo(String message) {
		diagnostics.add(new BasicDiagnostic(Diagnostic.INFO, SOURCE_ID, 0, message, null));
	}

	private static Object wrapNull(Object value) {
		return value == null ? WRAPPED_NULL : value;
	}

	private static Object unwrapNull(Object value) {
		return value == WRAPPED_NULL ? null : value;
	}

	/**
	 * Coerces a value to match the expected EMF structural feature type.
	 * OCL evaluates integer literals as Long, but EMF EInt expects Integer.
	 */
	private static Object coerceForFeature(EStructuralFeature sf, Object value) {
		if (value instanceof Long l && sf instanceof EAttribute attr) {
			String typeName = attr.getEAttributeType().getName();
			if ("EInt".equals(typeName) || "EIntegerObject".equals(typeName)) {
				return l.intValue();
			}
			if ("EShort".equals(typeName) || "EShortObject".equals(typeName)) {
				return l.shortValue();
			}
			if ("EByte".equals(typeName) || "EByteObject".equals(typeName)) {
				return l.byteValue();
			}
		}
		return value;
	}

	// =========================================================================
	// Inner Switch: ImperativeOCL expressions
	// =========================================================================

	private class ImperativeDispatch extends ImperativeOclSwitch<Object> {

		@Override
		public Object caseBlockExp(BlockExp exp) {
			Object lastResult = null;
			for (OclExpression bodyExp : exp.getBody()) {
				lastResult = eval(bodyExp);
			}
			return wrapNull(lastResult);
		}

		@Override
		public Object caseVariableInitExp(VariableInitExp exp) {
			Variable var = exp.getReferredVariable();
			Object value = null;
			if (var.getOwnedInit() != null) {
				value = eval(var.getOwnedInit());
			}
			env.define(var.getName(), value);
			return wrapNull(value);
		}

		@Override
		public Object caseAssignExp(AssignExp exp) {
			// Evaluate value(s)
			List<OclExpression> values = exp.getValue();
			Object value = null;
			if (!values.isEmpty()) {
				value = eval(values.get(0));
			}

			OclExpression left = exp.getLeft();
			if (left instanceof VariableExp varExp) {
				// Variable assignment
				String varName = varExp.getReferredVariable().getName();
				if (exp.isIsReset()) {
					// = (reset): replace value
					env.assign(varName, value);
				} else {
					// += (append): add to collection
					Object current = env.lookup(varName);
					if (current instanceof Collection<?>) {
						@SuppressWarnings("unchecked")
						Collection<Object> col = (Collection<Object>) current;
						col.add(value);
					} else {
						// No collection yet — just set value
						env.assign(varName, value);
					}
				}
			} else if (left instanceof PropertyCallExp propExp) {
				// Property assignment: eObject.eSet(feature, value)
				Object source = eval(propExp.getOwnedSource());
				EStructuralFeature sf = propExp.getReferredProperty();
				if (source instanceof EObject eo && sf != null) {
					// Resolve the feature from the actual EClass to handle
					// cross-package identity (dynamic Ecore models)
					EStructuralFeature actualSf = eo.eClass().getEStructuralFeature(sf.getName());
					if (actualSf == null) {
						actualSf = sf;
					}
					Object coerced = coerceForFeature(actualSf, value);
					if (exp.isIsReset()) {
						eo.eSet(actualSf, coerced);
					} else {
						// += on EReference/EAttribute list
						Object current = eo.eGet(actualSf);
						if (current instanceof Collection<?>) {
							@SuppressWarnings("unchecked")
							Collection<Object> col = (Collection<Object>) current;
							col.add(coerced);
						} else {
							eo.eSet(actualSf, coerced);
						}
					}
				}
			}
			return wrapNull(value);
		}

		@Override
		public Object caseWhileExp(WhileExp exp) {
			Object lastResult = null;
			while (true) {
				Object condition = eval(exp.getCondition());
				if (!Boolean.TRUE.equals(condition)) {
					break;
				}
				try {
					lastResult = eval(exp.getBody());
				} catch (BreakException e) {
					break;
				} catch (ContinueException e) {
					continue;
				}
			}
			return wrapNull(lastResult);
		}

		@Override
		public Object caseForExp(ForExp exp) {
			Object source = eval(exp.getOwnedSource());
			if (!(source instanceof Collection<?> coll)) {
				addError("for source must be a Collection, got: "
						+ (source == null ? "null" : source.getClass().getSimpleName()));
				return WRAPPED_NULL;
			}

			List<Variable> iterVars = exp.getOwnedIterators();
			String iterName = iterVars.isEmpty() ? "_it" : iterVars.get(0).getName();
			OclExpression condition = exp.getCondition();
			OclExpression body = exp.getOwnedBody();
			Object lastResult = null;

			for (Object element : coll) {
				env.pushScope();
				try {
					env.define(iterName, element);
					// Optional condition filter
					if (condition != null) {
						Object condVal = eval(condition);
						if (!Boolean.TRUE.equals(condVal)) {
							continue;
						}
					}
					lastResult = eval(body);
				} catch (BreakException e) {
					break;
				} catch (ContinueException e) {
					continue;
				} finally {
					env.popScope();
				}
			}
			return wrapNull(lastResult);
		}

		@Override
		public Object caseImperativeIterateExp(ImperativeIterateExp exp) {
			Object source = eval(exp.getOwnedSource());
			if (!(source instanceof Collection<?> coll)) {
				addError("imperativeIterate source must be a Collection, got: "
						+ (source == null ? "null" : source.getClass().getSimpleName()));
				return WRAPPED_NULL;
			}

			List<Variable> iterVars = exp.getOwnedIterators();
			String iterName = iterVars.isEmpty() ? "_it" : iterVars.get(0).getName();
			Variable target = exp.getTarget();
			OclExpression condition = exp.getCondition();
			OclExpression body = exp.getOwnedBody();

			// Initialize target variable
			Object targetValue = null;
			if (target != null && target.getOwnedInit() != null) {
				targetValue = eval(target.getOwnedInit());
			}
			if (target != null) {
				env.define(target.getName(), targetValue);
			}

			for (Object element : coll) {
				env.pushScope();
				try {
					env.define(iterName, element);
					if (condition != null) {
						Object condVal = eval(condition);
						if (!Boolean.TRUE.equals(condVal)) {
							continue;
						}
					}
					Object bodyResult = eval(body);
					if (target != null) {
						env.assign(target.getName(), bodyResult);
					}
				} catch (BreakException e) {
					break;
				} catch (ContinueException e) {
					continue;
				} finally {
					env.popScope();
				}
			}

			return wrapNull(target != null ? env.lookup(target.getName()) : null);
		}

		@Override
		public Object caseSwitchExp(SwitchExp exp) {
			for (AltExp alt : exp.getAlternativePart()) {
				Object condition = eval(alt.getCondition());
				if (Boolean.TRUE.equals(condition)) {
					return wrapNull(eval(alt.getBody()));
				}
			}
			if (exp.getElsePart() != null) {
				return wrapNull(eval(exp.getElsePart()));
			}
			return WRAPPED_NULL;
		}

		@Override
		public Object caseComputeExp(ComputeExp exp) {
			Variable retVar = exp.getReturnedElement();
			Object initValue = null;
			if (retVar.getOwnedInit() != null) {
				initValue = eval(retVar.getOwnedInit());
			}
			env.pushScope();
			try {
				env.define(retVar.getName(), initValue);
				eval(exp.getBody());
				return wrapNull(env.lookup(retVar.getName()));
			} finally {
				env.popScope();
			}
		}

		@Override
		public Object caseReturnExp(ReturnExp exp) {
			Object value = exp.getValue() != null ? eval(exp.getValue()) : null;
			throw new ReturnException(value);
		}

		@Override
		public Object caseBreakExp(BreakExp exp) {
			throw new BreakException();
		}

		@Override
		public Object caseContinueExp(ContinueExp exp) {
			throw new ContinueException();
		}

		@Override
		public Object caseLogExp(LogExp exp) {
			// Evaluate arguments (inherited from OperationCallExp)
			StringBuilder sb = new StringBuilder();
			for (OclExpression arg : exp.getOwnedArguments()) {
				Object val = eval(arg);
				if (sb.length() > 0) {
					sb.append(' ');
				}
				sb.append(val);
			}

			// Check optional condition
			if (exp.getCondition() != null) {
				Object cond = eval(exp.getCondition());
				if (!Boolean.TRUE.equals(cond)) {
					return WRAPPED_NULL; // Condition false — skip logging
				}
			}

			addInfo("log: " + sb);
			return WRAPPED_NULL;
		}

		@Override
		public Object caseInstantiationExp(InstantiationExp exp) {
			// ObjectExp extends InstantiationExp — EMF switch catches it here via
			// supertype dispatch. Delegate to QvtOpDispatch for full object semantics.
			if (exp instanceof ObjectExp) {
				return UNHANDLED;
			}
			EClass eClass = exp.getInstantiatedClass();
			if (eClass == null || eClass.isAbstract()) {
				addError("Cannot instantiate: " + (eClass == null ? "null" : eClass.getName()));
				return WRAPPED_NULL;
			}
			EObject created = EcoreUtil.create(eClass);
			addToDefaultExtent(created);
			return wrapNull(created);
		}

		@Override
		public Object caseAssertExp(AssertExp exp) {
			Object condition = eval(exp.getAssertion());
			if (!Boolean.TRUE.equals(condition)) {
				SeverityKind severity = exp.getSeverity();
				int diagSeverity = switch (severity) {
					case WARNING -> Diagnostic.WARNING;
					case FATAL -> Diagnostic.CANCEL;
					default -> Diagnostic.ERROR;
				};

				String message = "Assertion failed";
				if (exp.getLog() != null) {
					// Evaluate log arguments for the message
					StringBuilder sb = new StringBuilder();
					for (OclExpression arg : exp.getLog().getOwnedArguments()) {
						Object val = eval(arg);
						if (sb.length() > 0) {
							sb.append(' ');
						}
						sb.append(val);
					}
					if (sb.length() > 0) {
						message = sb.toString();
					}
				}

				diagnostics.add(new BasicDiagnostic(diagSeverity, SOURCE_ID, 0, message, null));
			}
			return WRAPPED_NULL;
		}

		@Override
		public Object defaultCase(EObject object) {
			return UNHANDLED;
		}
	}

	// =========================================================================
	// Inner Switch: QvtOperational expressions
	// =========================================================================

	private class QvtOpDispatch extends QvtOperationalSwitch<Object> {

		@Override
		public Object caseObjectExp(ObjectExp exp) {
			EClass eClass = exp.getInstantiatedClass();
			if (eClass == null || eClass.isAbstract()) {
				addError("Cannot instantiate: " + (eClass == null ? "null" : eClass.getName()));
				return WRAPPED_NULL;
			}
			EObject created = EcoreUtil.create(eClass);

			// Bind the referred object variable
			Variable refObj = exp.getReferredObject();
			String varName = (refObj != null && refObj.getName() != null)
					? refObj.getName() : "_objectExp";

			env.pushScope();
			try {
				env.define(varName, created);
				env.define("self", created);

				// Execute constructor body
				ConstructorBody body = exp.getBody();
				if (body != null) {
					for (OclExpression bodyExpr : body.getContent()) {
						eval(bodyExpr);
					}
				}
			} finally {
				env.popScope();
			}

			addToDefaultExtent(created);
			return wrapNull(created);
		}

		@Override
		public Object caseMappingCallExp(MappingCallExp exp) {
			// Evaluate source (context object)
			Object sourceObj = null;
			if (exp.getOwnedSource() != null) {
				sourceObj = eval(exp.getOwnedSource());
			}

			// Evaluate arguments
			EList<OclExpression> argExprs = exp.getOwnedArguments();
			Object[] args = new Object[argExprs.size()];
			for (int i = 0; i < argExprs.size(); i++) {
				args[i] = eval(argExprs.get(i));
			}

			// Find target operation
			String opName = exp.getName();
			ImperativeOperation targetOp = findOperation(opName, sourceObj);
			if (targetOp == null) {
				addError("Mapping not found: " + opName);
				return WRAPPED_NULL;
			}

			return wrapNull(callOperation(targetOp, sourceObj, args));
		}

		@Override
		public Object caseResolveExp(ResolveExp exp) {
			if (exp.isIsDeferred()) {
				addWarning("Late resolve not yet supported (Phase C)");
				return WRAPPED_NULL;
			}

			// Source object
			Object sourceObj = null;
			if (exp.getOwnedSource() != null) {
				sourceObj = eval(exp.getOwnedSource());
			}

			// Target type
			EClass targetType = null;
			Variable target = exp.getTarget();
			if (target != null && target.getType() instanceof EClass tc) {
				targetType = tc;
			}

			// Resolve
			List<EObject> candidates;
			if (exp instanceof ResolveInExp resolveIn && resolveIn.getInMapping() != null) {
				candidates = traceManager.resolveIn(
						resolveIn.getInMapping().getName(), sourceObj, targetType);
			} else if (exp.isIsInverse()) {
				candidates = traceManager.invResolve(sourceObj, targetType);
			} else {
				candidates = traceManager.resolve(sourceObj, targetType);
			}

			// Apply condition filter
			if (exp.getCondition() != null && target != null) {
				String targetVarName = target.getName() != null ? target.getName() : "_target";
				List<EObject> filtered = new ArrayList<>();
				for (EObject candidate : candidates) {
					env.pushScope();
					try {
						env.define(targetVarName, candidate);
						Object condVal = eval(exp.getCondition());
						if (Boolean.TRUE.equals(condVal)) {
							filtered.add(candidate);
						}
					} finally {
						env.popScope();
					}
				}
				candidates = filtered;
			}

			if (exp.isOne()) {
				return wrapNull(candidates.isEmpty() ? null : candidates.get(0));
			}
			return wrapNull(candidates);
		}

		@Override
		public Object caseResolveInExp(ResolveInExp exp) {
			// Delegate to caseResolveExp which already handles ResolveInExp
			return caseResolveExp(exp);
		}

		@Override
		public Object defaultCase(EObject object) {
			return UNHANDLED;
		}
	}
}
