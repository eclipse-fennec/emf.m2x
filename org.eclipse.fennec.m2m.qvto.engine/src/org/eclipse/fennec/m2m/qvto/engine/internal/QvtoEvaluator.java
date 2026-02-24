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
import java.util.LinkedHashMap;
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
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2m.model.imperativeocl.AltExp;
import org.eclipse.fennec.m2m.model.imperativeocl.AssertExp;
import org.eclipse.fennec.m2m.model.imperativeocl.AssignExp;
import org.eclipse.fennec.m2m.model.imperativeocl.BlockExp;
import org.eclipse.fennec.m2m.model.imperativeocl.BreakExp;
import org.eclipse.fennec.m2m.model.imperativeocl.CatchExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ComputeExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ContinueExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ForExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ImperativeIterateExp;
import org.eclipse.fennec.m2m.model.imperativeocl.InstantiationExp;
import org.eclipse.fennec.m2m.model.imperativeocl.LogExp;
import org.eclipse.fennec.m2m.model.imperativeocl.RaiseExp;
import org.eclipse.fennec.m2m.model.imperativeocl.ReturnExp;
import org.eclipse.fennec.m2m.model.imperativeocl.SeverityKind;
import org.eclipse.fennec.m2m.model.imperativeocl.SwitchExp;
import org.eclipse.fennec.m2m.model.imperativeocl.TryExp;
import org.eclipse.fennec.m2m.model.imperativeocl.VariableInitExp;
import org.eclipse.fennec.m2m.model.imperativeocl.WhileExp;
import org.eclipse.fennec.m2m.model.imperativeocl.util.ImperativeOclSwitch;
import org.eclipse.fennec.m2m.model.ocl.ClassifierType;
import org.eclipse.fennec.m2m.model.ocl.IfExp;
import org.eclipse.fennec.m2m.model.ocl.IteratorExp;
import org.eclipse.fennec.m2m.model.ocl.OclExpression;
import org.eclipse.fennec.m2m.model.ocl.OclType;
import org.eclipse.fennec.m2m.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2m.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2m.model.ocl.Variable;
import org.eclipse.fennec.m2m.model.ocl.VariableExp;
import org.eclipse.fennec.m2m.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2m.model.qvtoperational.ConstructorBody;
import org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty;
import org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingCallExp;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.ObjectExp;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationBody;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveExp;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveInExp;
import org.eclipse.fennec.m2m.model.qvtoperational.VarParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.util.QvtOperationalSwitch;
import org.eclipse.fennec.m2m.model.trace.Trace;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;
import org.eclipse.fennec.m2m.ocl.api.OclContext;
import org.eclipse.fennec.m2m.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2m.ocl.api.OclResult;
import org.eclipse.fennec.m2m.ocl.engine.OclEngineImpl;
import org.eclipse.fennec.m2m.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2m.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2m.qvto.engine.internal.QvtoControlFlowException.BreakException;
import org.eclipse.fennec.m2m.qvto.engine.internal.QvtoControlFlowException.ContinueException;
import org.eclipse.fennec.m2m.qvto.engine.internal.QvtoControlFlowException.FatalAssertionException;
import org.eclipse.fennec.m2m.qvto.engine.internal.QvtoControlFlowException.RaiseException;
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

	/** Sentinel indicating a mapping's when-guard failed (§8.1.14: disjunct candidate selection). */
	private static final Object GUARD_FAILED = new Object();

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

	// §8.1.10: Intermediate property storage — per-instance values for ContextualProperty
	private final QvtoIntermediatePropertyStore intermediateStore;

	// §8.1.16 / §8.3.19: tag "alias" — maps alias name → (EClass, real feature name)
	private final QvtoAliasRegistry aliasRegistry;
	private final QvtoOperationResolver operationResolver;
	private final QvtoModelOperations modelOperations;

	// Late resolve support
	private final List<DeferredResolveTask> deferredTasks = new ArrayList<>();
	private boolean isDeferredExecution;
	// L-value capture: set by caseAssignExp before RHS evaluation
	private EObject pendingLvalueOwner;
	private EStructuralFeature pendingLvalueFeature;
	private boolean pendingLvalueIsReset;

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
		this.intermediateStore = new QvtoIntermediatePropertyStore(transformation, this::eval);
		this.aliasRegistry = new QvtoAliasRegistry(transformation, extentManager);
		this.operationResolver = new QvtoOperationResolver(transformation);
		this.modelOperations = new QvtoModelOperations(this::eval);
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

		// Register configuration properties — undefined ones get null
		for (EStructuralFeature configProp : transformation.getConfigProperty()) {
			String propName = configProp.getName();
			if (propName != null && env.lookup(propName) == null) {
				env.define(propName, null);
			}
		}

		// §8.1.18: 'this' represents the transformation instance
		env.define("this", transformation);

		// §8.3.19: Build alias registry from tag "alias" declarations
		aliasRegistry.build();

		// Initialize module-level properties (property name : Type = init;)
		for (Variable moduleVar : transformation.getOwnedVariable()) {
			String varName = moduleVar.getName();
			if (varName != null) {
				Object value = null;
				if (moduleVar.getOwnedInit() != null) {
					value = eval(moduleVar.getOwnedInit());
				}
				env.define(varName, value);
			}
		}

		ImperativeOperation mainOp = operationResolver.findMainOperation();
		if (mainOp == null) {
			addError("No main() entry operation found in transformation '"
					+ transformation.getName() + "'");
			return diagnostics;
		}
		try {
			callOperation(mainOp, null, new Object[0]);
		} catch (ReturnException e) {
			// main() returned — normal
		} catch (FatalAssertionException e) {
			// §8.2.2.20: fatal assert terminates execution
			addError("Assertion failed: " + e.message);
		} catch (RaiseException e) {
			// §8.2.2.15: uncaught raise terminates execution
			addError("Uncaught exception: " + e.exceptionType
					+ (e.argument != null ? " - " + e.argument : ""));
		}
		processDeferredTasks();
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
			result = modelOperations.handleExtentOperation(opCall);
			if (result != QvtoModelOperations.UNHANDLED) {
				return QvtoModelOperations.unwrapNull(result);
			}
			// §8.3.4: Element operations (metaClassName, subobjects, clone, etc.)
			result = modelOperations.handleElementOperation(opCall);
			if (result != QvtoModelOperations.UNHANDLED) {
				return QvtoModelOperations.unwrapNull(result);
			}
		}

		// 4. IteratorExp — evaluate source through QVT-O dispatch so extent
		// operations (rootObjects, objectsOfType) and imperative expressions work
		if (expr instanceof IteratorExp iterExp) {
			return unwrapNull(evalIteratorExp(iterExp));
		}

		// 5. IfExp — evaluate here to ensure imperative body expressions
		// (BlockExp, etc.) are dispatched through QVT-O eval, not OCL eval
		if (expr instanceof IfExp ifExp) {
			return evalIfExp(ifExp);
		}

		// 6. §8.1.10: Intermediate property read — intercept before OCL delegation
		if (expr instanceof PropertyCallExp propCall) {
			Object propResult = evalIntermediatePropertyRead(propCall);
			if (propResult != UNHANDLED) {
				return unwrapNull(propResult);
			}
		}

		// 6.5. QVT-O lenient null handling for string concat — OCL strict mode
		// rejects null arguments, but QVT-O should coerce null to "" in string context
		if (expr instanceof OperationCallExp opCall && "+".equals(opCall.getName())) {
			Object source = eval(opCall.getOwnedSource());
			if (source instanceof String || source == null) {
				Object arg = !opCall.getOwnedArguments().isEmpty()
						? eval(opCall.getOwnedArguments().get(0)) : null;
				String left = source != null ? source.toString() : "";
				String right = arg != null ? arg.toString() : "";
				return left + right;
			}
		}

		// 7. OCL delegation
		return evalOcl(expr);
	}

	/**
	 * Evaluates an OCL IfExp using the QVT-O dispatch chain so that imperative
	 * body expressions (BlockExp, etc.) are handled correctly.
	 * §8.2.2.8: imperative if — else is optional (returns null when absent).
	 */
	private Object evalIfExp(IfExp ifExp) {
		Object condVal = eval(ifExp.getOwnedCondition());
		if (Boolean.TRUE.equals(condVal)) {
			return eval(ifExp.getOwnedThen());
		} else if (ifExp.getOwnedElse() != null) {
			return eval(ifExp.getOwnedElse());
		}
		return null;
	}

	/**
	 * Evaluates an OCL IteratorExp (select, reject, collect, etc.) using the
	 * QVT-O dispatch chain for source evaluation, so that extent operations
	 * (rootObjects, objectsOfType) and imperative expressions are handled.
	 */
	private Object evalIteratorExp(IteratorExp iterExp) {
		Object sourceVal = eval(iterExp.getOwnedSource());
		if (!(sourceVal instanceof Collection<?> coll)) {
			// Fallback to OCL for non-collection sources
			return evalOcl(iterExp);
		}

		List<Variable> iterVars = iterExp.getOwnedIterators();
		String iterName = iterVars.isEmpty() ? "_it" : iterVars.get(0).getName();
		OclExpression body = iterExp.getOwnedBody();
		String opName = iterExp.getName();

		return switch (opName) {
			case "select" -> {
				List<Object> result = new ArrayList<>();
				for (Object elem : coll) {
					env.pushScope();
					try {
						env.define(iterName, elem);
						Object bodyVal = eval(body);
						if (Boolean.TRUE.equals(bodyVal)) {
							result.add(elem);
						}
					} finally {
						env.popScope();
					}
				}
				yield result;
			}
			case "reject" -> {
				List<Object> result = new ArrayList<>();
				for (Object elem : coll) {
					env.pushScope();
					try {
						env.define(iterName, elem);
						Object bodyVal = eval(body);
						if (!Boolean.TRUE.equals(bodyVal)) {
							result.add(elem);
						}
					} finally {
						env.popScope();
					}
				}
				yield result;
			}
			case "collect" -> {
				List<Object> result = new ArrayList<>();
				for (Object elem : coll) {
					env.pushScope();
					try {
						env.define(iterName, elem);
						Object bodyVal = eval(body);
						// collect flattens one level (OCL §11.9.1)
						if (bodyVal instanceof Collection<?> nested) {
							result.addAll(nested);
						} else {
							result.add(bodyVal);
						}
					} finally {
						env.popScope();
					}
				}
				yield result;
			}
			case "forAll" -> {
				for (Object elem : coll) {
					env.pushScope();
					try {
						env.define(iterName, elem);
						if (!Boolean.TRUE.equals(eval(body))) {
							yield Boolean.FALSE;
						}
					} finally {
						env.popScope();
					}
				}
				yield Boolean.TRUE;
			}
			case "exists" -> {
				for (Object elem : coll) {
					env.pushScope();
					try {
						env.define(iterName, elem);
						if (Boolean.TRUE.equals(eval(body))) {
							yield Boolean.TRUE;
						}
					} finally {
						env.popScope();
					}
				}
				yield Boolean.FALSE;
			}
			case "any" -> {
				for (Object elem : coll) {
					env.pushScope();
					try {
						env.define(iterName, elem);
						if (Boolean.TRUE.equals(eval(body))) {
							yield elem;
						}
					} finally {
						env.popScope();
					}
				}
				yield wrapNull(null);
			}
			default -> evalOcl(iterExp);
		};
	}

	/**
	 * §8.1.10: Checks if a PropertyCallExp refers to an intermediate property
	 * and reads the value from intermediate storage if so.
	 *
	 * @return the value, or UNHANDLED if this is not an intermediate property
	 */
	private Object evalIntermediatePropertyRead(PropertyCallExp propCall) {
		String propName = propCall.getReferredProperty() != null
				? propCall.getReferredProperty().getName() : null;
		if (propName == null) {
			return UNHANDLED;
		}
		Object sourceObj = eval(propCall.getOwnedSource());
		if (sourceObj instanceof EObject eo) {
			ContextualProperty cp = intermediateStore.findIntermediateProperty(eo, propName);
			if (cp != null) {
				return wrapNull(intermediateStore.getIntermediatePropertyValue(eo, propName));
			}
		}
		return UNHANDLED;
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
		// §8.1.10: Intermediate property interceptor for OCL sub-expressions
		OclContext oclCtx;
		if (intermediateStore.hasIntermediateProperties()) {
			oclCtx = new OclContext(self, null, vars, null, intermediateStore::interceptIntermediateProperty);
		} else {
			oclCtx = self != null
					? OclContext.of(self, vars)
					: OclContext.of(vars);
		}
		// Use LENIENT null handling for QVT-O — module-level operations have no self
		OclEvaluationOptions oclOpts = OclEvaluationOptions.lenient();
		OclResult oclResult = oclEngine.evaluateWithDiagnostics(expr, oclCtx, oclOpts);
		diagnostics.addAll(oclResult.diagnostics());
		return oclResult.value();
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
			Object result = callMapping(mappingOp, self, args);
			return result == GUARD_FAILED ? null : result;
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
				// §8.4: Multi-result → return as Tuple (Map)
				if (resultParams.size() > 1) {
					return collectMultiResult(resultParams);
				}
				String resultName = resultParams.get(0).getName();
				Object resultValue = env.lookup(resultName);
				// §8.2.1.12: For expression bodies (= expr), the result variable is
				// uninitialized — use the last evaluated expression as return value
				if (resultValue == null && lastResult != null) {
					return lastResult;
				}
				return resultValue;
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
	 * Executes a mapping in strict mode (xmap): when-guard is a pre-condition.
	 * If the when-guard fails, this is an assertion failure (§8.2.1.15).
	 */
	private Object callMappingStrict(MappingOperation mappingOp, Object self, Object[] args) {
		// Strict mode: check when-guards as pre-conditions
		if (++stackDepth > options.maxStackDepth()) {
			--stackDepth;
			addError("Maximum stack depth exceeded: " + options.maxStackDepth());
			return null;
		}
		try {
			env.pushScope();
			bindOperationParams(mappingOp, self, args);
			for (OclExpression whenExpr : mappingOp.getWhen()) {
				Object whenVal = eval(whenExpr);
				if (!Boolean.TRUE.equals(whenVal)) {
					addError("xmap pre-condition failed for mapping: " + mappingOp.getName());
					return null;
				}
			}
		} finally {
			env.popScope();
			--stackDepth;
		}
		// Pre-condition passed — delegate to standard callMapping for body execution
		return callMapping(mappingOp, self, args);
	}

	/**
	 * Executes a mapping operation with full mapping semantics:
	 * when-guard, init/population/end, result creation, where-postcondition, trace recording.
	 */
	private Object callMapping(MappingOperation mappingOp, Object self, Object[] args) {
		// Disjuncts: try each disjunct, first with passing when-guard wins (§8.1.14.1)
		EList<MappingOperation> disjuncts = mappingOp.getDisjunct();
		if (disjuncts != null && !disjuncts.isEmpty()) {
			for (MappingOperation disjunct : disjuncts) {
				Object result = callMapping(disjunct, self, args);
				if (result != GUARD_FAILED) {
					return result; // First candidate whose guard passes wins (even if result is null)
				}
			}
			return null; // No candidate matched
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
					return GUARD_FAILED;
				}
			}

			// Implicit inhibition (§8.2.1.15 p106): check trace for cached result
			if (traceManager != null) {
				Object cached = traceManager.lookupCachedResult(mappingOp, self, args);
				if (cached != null) {
					return cached;
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

			// Execute mapping body (§8.2.1.15 p106-107):
			// init → inherited → population → end → merged
			OperationBody body = mappingOp.getBody();
			if (body instanceof MappingBody mappingBody) {
				// 1. Init section
				for (OclExpression initExpr : mappingBody.getInitSection()) {
					eval(initExpr);
				}
				// 2. Inherited mappings: after init+instantiation, before population (§8.1.15)
				for (MappingOperation inherited : mappingOp.getInherited()) {
					executePopulationOnly(inherited, self, args);
				}
				// 3. Population section (main body)
				for (OclExpression contentExpr : mappingBody.getContent()) {
					eval(contentExpr);
				}
				// 4. End section
				for (OclExpression endExpr : mappingBody.getEndSection()) {
					eval(endExpr);
				}
				// 5. Merged mappings: after end section (§8.1.15)
				for (MappingOperation merged : mappingOp.getMerged()) {
					executePopulationOnly(merged, self, args);
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
				if (resultParams.size() > 1) {
					result = collectMultiResult(resultParams);
				} else {
					result = env.lookup(resultParams.get(0).getName());
				}
			}

			// Record trace (§8.1.11.1: context, in-params, result)
			traceManager.addRecord(mappingOp, self, args, result);

			return result;
		} catch (ReturnException e) {
			Object result = e.value;
			if (result == null && !mappingOp.getResult().isEmpty()) {
				result = env.lookup(mappingOp.getResult().get(0).getName());
			}
			traceManager.addRecord(mappingOp, self, args, result);
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
		// §8.1.15: A merged/inherited mapping is not invoked if its when-guard is not satisfied
		for (OclExpression whenExpr : mapping.getWhen()) {
			Object whenVal = eval(whenExpr);
			if (!Boolean.TRUE.equals(whenVal)) {
				return;
			}
		}
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
	 * Adds an EObject to the appropriate output extent. First tries to match
	 * by metamodel (EPackage), then falls back to the default output extent.
	 */
	private void addToDefaultExtent(EObject eObject) {
		QvtoModelExtent extent = extentManager.getExtentForClassifier(eObject.eClass());
		if (extent == null) {
			extent = extentManager.getDefaultOutputExtent();
		}
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

	/**
	 * Returns the EMF trace model built during execution.
	 */
	public Trace getTrace() {
		return traceManager.getTrace();
	}

	/**
	 * Returns the operation resolver for use by {@link QvtoOperationProvider}.
	 */
	public QvtoOperationResolver getOperationResolver() {
		return operationResolver;
	}



	/**
	 * Returns the default value for a type when no initializer is provided.
	 * §8.2.2.10: "zero for a numeric type, the empty string for a string,
	 * and null for all other elements"
	 */
	private static Object defaultValueForType(OclType type) {
		if (type == null) {
			return null;
		}
		String typeName = type.getName();
		if (typeName == null) {
			return null;
		}
		return switch (typeName) {
			case "Integer" -> 0;
			case "Real" -> 0.0;
			case "Boolean" -> Boolean.FALSE;
			case "String" -> "";
			default -> null;
		};
	}

	/**
	 * Calls a constructor operation on a created object.
	 * §8.2.1.13 + Eclipse semantics: self is moved to result inside the constructor body,
	 * so unqualified property assignments resolve to the created object.
	 */
	private void callConstructor(Constructor ctor, EObject created, List<Object> args) {
		if (++stackDepth > options.maxStackDepth()) {
			--stackDepth;
			addError("Maximum stack depth exceeded: " + options.maxStackDepth());
			return;
		}
		try {
			env.pushScope();

			// Bind parameters from args
			List<?> formalParams = ctor.getEParameters();
			EList<VarParameter> resultParams = ctor.getResult();
			int argIdx = 0;
			for (int i = 0; i < formalParams.size(); i++) {
				Object param = formalParams.get(i);
				if (param instanceof VarParameter vp) {
					if (resultParams != null && resultParams.contains(vp)) {
						continue;
					}
					Object value = argIdx < args.size() ? args.get(argIdx++) : null;
					env.define(vp.getName(), value);
				}
			}

			// §8.2.1.13/Eclipse: self → result (constructor body uses 'result' for the object)
			env.define("result", created);
			// Also define _objectExp for resolveImplicitPropertyTarget
			env.define("_objectExp", created);

			// Execute constructor body
			OperationBody body = ctor.getBody();
			if (body != null) {
				for (OclExpression expr : body.getContent()) {
					eval(expr);
				}
			}
		} catch (ReturnException e) {
			// Constructor body may have return — ignore the value
		} finally {
			env.popScope();
			--stackDepth;
		}
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

	/**
	 * Processes all deferred (late) resolve tasks after all mappings have completed.
	 * Each task re-evaluates its resolve expression (which now has access to the full
	 * trace) and assigns the result to the captured l-value property.
	 */
	@SuppressWarnings("unchecked")
	private void processDeferredTasks() {
		if (deferredTasks.isEmpty()) {
			return;
		}
		isDeferredExecution = true;
		try {
			for (DeferredResolveTask task : deferredTasks) {
				// Re-evaluate the resolve expression — isDeferredExecution causes
				// caseResolveExp to fall through to normal resolve logic
				pendingLvalueOwner = null; // Clear to avoid re-capture
				Object sourceObj = task.capturedSource();
				env.pushScope();
				try {
					if (sourceObj != null) {
						env.define("self", sourceObj);
					}
					Object resolved = qvtOpSwitch.doSwitch(task.resolveExp());
					resolved = unwrapNull(resolved);

					if (resolved != null && task.targetObject() != null
							&& task.targetFeature() != null) {
						EStructuralFeature sf = task.targetObject().eClass()
								.getEStructuralFeature(task.targetFeature().getName());
						if (sf == null) {
							sf = task.targetFeature();
						}
						if (task.isReset()) {
							task.targetObject().eSet(sf, coerceForFeature(sf, resolved));
						} else {
							Object current = task.targetObject().eGet(sf);
							if (current instanceof Collection<?>) {
								((Collection<Object>) current).add(
										coerceForFeature(sf, resolved));
							} else {
								task.targetObject().eSet(sf, coerceForFeature(sf, resolved));
							}
						}
					}
				} finally {
					env.popScope();
				}
			}
		} finally {
			isDeferredExecution = false;
			deferredTasks.clear();
		}
	}

	private static Object wrapNull(Object value) {
		return value == null ? WRAPPED_NULL : value;
	}

	private static Object unwrapNull(Object value) {
		return value == WRAPPED_NULL ? null : value;
	}

	/**
	 * §8.4: Collects multiple named result variables into a Tuple (Map).
	 */
	private Map<String, Object> collectMultiResult(EList<VarParameter> resultParams) {
		Map<String, Object> tuple = new LinkedHashMap<>();
		for (VarParameter rp : resultParams) {
			tuple.put(rp.getName(), env.lookup(rp.getName()));
		}
		return tuple;
	}

	/**
	 * §8.2.1.17: Resolves an unqualified property name against the implicit context
	 * object. Checks 'self' (ObjectExp context) and 'result' (mapping body context).
	 *
	 * @return the EObject that owns the feature, or null if not resolvable
	 */
	private EObject resolveImplicitPropertyTarget(String featureName) {
		// ObjectExp: check the _objectExp variable first (innermost object scope)
		Object objectExp = env.lookup("_objectExp");
		if (objectExp instanceof EObject eo
				&& (eo.eClass().getEStructuralFeature(featureName) != null
						|| resolveAlias(featureName, eo) != null)) {
			return eo;
		}
		// §8.2.1.17: Mapping body — result has priority over self for implicit properties,
		// because mapping body assignments target the result object.
		Object result = env.lookup("result");
		if (result instanceof EObject eo
				&& (eo.eClass().getEStructuralFeature(featureName) != null
						|| resolveAlias(featureName, eo) != null)) {
			return eo;
		}
		// self context (constructor body, standalone helper)
		Object self = env.lookup("self");
		if (self instanceof EObject eo
				&& (eo.eClass().getEStructuralFeature(featureName) != null
						|| resolveAlias(featureName, eo) != null)) {
			return eo;
		}
		return null;
	}

	/**
	 * §8.3.19: Resolves a tag "alias" name to the real feature name for the given EObject.
	 * @return the real feature name, or null if not an alias
	 */
	String resolveAlias(String name, EObject target) {
		return aliasRegistry.resolveAlias(name, target);
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
			} else if (var.getType() != null) {
				// §8.2.2.10: Default values when no init — zero for numeric,
				// empty string for String, empty collection for Collection, null otherwise
				value = defaultValueForType(var.getType());
			}
			env.define(var.getName(), value);
			// §8.2.2.10: withResult=true (::=) → expression returns the init value
			// withResult=false (:= or =) → expression returns null (statement, no value)
			return exp.isWithResult() ? wrapNull(value) : WRAPPED_NULL;
		}

		@Override
		public Object caseAssignExp(AssignExp exp) {
			OclExpression left = exp.getLeft();

			// Capture l-value before evaluating RHS (needed for late resolve)
			if (left instanceof PropertyCallExp propLeft) {
				Object lvalSource = eval(propLeft.getOwnedSource());
				if (lvalSource instanceof EObject eo) {
					EStructuralFeature sf = propLeft.getReferredProperty();
					EStructuralFeature actualSf = eo.eClass().getEStructuralFeature(sf.getName());
					pendingLvalueOwner = eo;
					pendingLvalueFeature = actualSf != null ? actualSf : sf;
					pendingLvalueIsReset = exp.isIsReset();
				}
			}

			// Evaluate value(s)
			List<OclExpression> values = exp.getValue();
			Object value = null;
			if (!values.isEmpty()) {
				value = eval(values.get(0));
			}

			// §8.2.2.11: defaultValue — replace null with the default expression
			if (value == null && exp.getDefaultValue() != null) {
				value = eval(exp.getDefaultValue());
			}

			// Clear pending l-value after RHS evaluation
			pendingLvalueOwner = null;
			pendingLvalueFeature = null;
			if (left instanceof VariableExp varExp) {
				String varName = varExp.getReferredVariable().getName();
				// §8.2.1.17: Implicit property assignment — inside object expressions
				// (self = created object) and mapping bodies with single result
				// (result = created object), unqualified names resolve as properties
				// of the context object.
				if (!env.contains(varName)) {
					EObject target = resolveImplicitPropertyTarget(varName);
					if (target != null) {
						// §8.3.19: Resolve alias to real feature name
						String realName = resolveAlias(varName, target);
						EStructuralFeature sf = target.eClass().getEStructuralFeature(
								realName != null ? realName : varName);
						Object coerced = coerceForFeature(sf, value);
						if (exp.isIsReset()) {
							target.eSet(sf, coerced);
						} else {
							Object current = target.eGet(sf);
							if (current instanceof Collection<?>) {
								// Eclipse bug449445: invalid is silently ignored on +=
								if (coerced != OclInvalid.INSTANCE) {
									@SuppressWarnings("unchecked")
									Collection<Object> col = (Collection<Object>) current;
									col.add(coerced);
								}
							} else {
								target.eSet(sf, coerced);
							}
						}
						return wrapNull(value);
					}
				}
				// Variable assignment
				if (exp.isIsReset()) {
					env.assign(varName, value);
				} else {
					// += (append): add to collection
					Object current = env.lookup(varName);
					if (current instanceof Collection<?>) {
						// Eclipse bug449445: invalid is silently ignored on +=
						if (value != OclInvalid.INSTANCE) {
							@SuppressWarnings("unchecked")
							Collection<Object> col = (Collection<Object>) current;
							col.add(value);
						}
					} else {
						env.assign(varName, value);
					}
				}
			} else if (left instanceof PropertyCallExp propExp) {
				// Property assignment: eObject.eSet(feature, value) or tuple.part
				Object source = eval(propExp.getOwnedSource());
				EStructuralFeature sf = propExp.getReferredProperty();
				// Tuple part assignment (tuples are Map<String, Object>)
				if (source instanceof Map<?, ?> && sf != null) {
					@SuppressWarnings("unchecked")
					Map<String, Object> tuple = (Map<String, Object>) source;
					String partName = sf.getName();
					if (exp.isIsReset()) {
						tuple.put(partName, value);
					} else {
						// += on tuple part: only valid for mutable collections (List)
						Object current = tuple.get(partName);
						if (current instanceof Collection<?>) {
							@SuppressWarnings("unchecked")
							Collection<Object> col = (Collection<Object>) current;
							if (value instanceof Collection<?> rhs) {
								col.addAll((Collection<?>) rhs);
							} else {
								col.add(value);
							}
						} else {
							tuple.put(partName, value);
						}
					}
				} else if (source instanceof EObject eo && sf != null) {
					// §8.1.10: Check intermediate property first
					ContextualProperty icp = intermediateStore.findIntermediateProperty(eo, sf.getName());
					if (icp != null) {
						// Eclipse bug449445: invalid → null for property assignment,
						// or clear if current value is a collection
						if (value == OclInvalid.INSTANCE && exp.isIsReset()) {
							Object current = intermediateStore.getIntermediatePropertyValue(eo, sf.getName());
							if (current instanceof Collection<?>) {
								@SuppressWarnings("unchecked")
								Collection<Object> col = (Collection<Object>) current;
								col.clear();
							} else {
								intermediateStore.setIntermediatePropertyValue(eo, sf.getName(), null);
							}
						} else if (value == OclInvalid.INSTANCE) {
							// += invalid: silently ignored
						} else {
							intermediateStore.setIntermediatePropertyValue(eo, sf.getName(), value);
						}
						return wrapNull(value);
					}
					// Resolve the feature from the actual EClass to handle
					// cross-package identity (dynamic Ecore models)
					EStructuralFeature actualSf = eo.eClass().getEStructuralFeature(sf.getName());
					if (actualSf == null) {
						actualSf = sf;
					}
					// Eclipse bug449445: invalid → null for EObject property assignment
					Object coerced = coerceForFeature(actualSf,
							value == OclInvalid.INSTANCE ? null : value);
					if (exp.isIsReset()) {
						if (value == OclInvalid.INSTANCE) {
							// Clear collection or set null
							Object current = eo.eGet(actualSf);
							if (current instanceof Collection<?>) {
								@SuppressWarnings("unchecked")
								Collection<Object> col = (Collection<Object>) current;
								col.clear();
							} else {
								eo.eSet(actualSf, null);
							}
						} else {
							eo.eSet(actualSf, coerced);
						}
					} else {
						// += on EReference/EAttribute list
						if (coerced != OclInvalid.INSTANCE) {
							Object current = eo.eGet(actualSf);
							if (current instanceof Collection<?>) {
								@SuppressWarnings("unchecked")
								Collection<Object> col = (Collection<Object>) current;
								if (coerced instanceof Collection<?> rhs) {
									for (Object item : rhs) {
										col.add(coerceForFeature(actualSf, item));
									}
								} else {
									col.add(coerced);
								}
							} else {
								eo.eSet(actualSf, coerced);
							}
						}
					}
				}
			}
			return wrapNull(value);
		}

		@Override
		public Object caseWhileExp(WhileExp exp) {
			// §8.2.2.4: WhileExp returns null
			while (true) {
				Object condition = eval(exp.getCondition());
				if (!Boolean.TRUE.equals(condition)) {
					break;
				}
				try {
					eval(exp.getBody());
				} catch (BreakException e) {
					break;
				} catch (ContinueException e) {
					continue;
				}
			}
			return WRAPPED_NULL;
		}

		@Override
		public Object caseForExp(ForExp exp) {
			Object source = eval(exp.getOwnedSource());
			if (!(source instanceof Collection<?> coll)) {
				addError("for source must be a Collection, got: "
						+ (source == null ? "null" : source.getClass().getSimpleName()));
				return WRAPPED_NULL;
			}

			// §8.2.2.6: non-ordered collections are implicitly converted to ordered
			// (Set → OrderedSet/LinkedHashSet, Bag → Sequence/ArrayList)
			Iterable<?> iterable = coll;
			if (coll instanceof java.util.Set<?> && !(coll instanceof java.util.SequencedSet<?>)) {
				iterable = new java.util.LinkedHashSet<>(coll);
			}

			List<Variable> iterVars = exp.getOwnedIterators();
			String iterName = iterVars.isEmpty() ? "_it" : iterVars.get(0).getName();
			OclExpression condition = exp.getCondition();
			OclExpression body = exp.getOwnedBody();
			boolean isForOne = "forOne".equals(exp.getName());

			for (Object element : iterable) {
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
					eval(body);
					// §8.2.2.6: forOne executes body only for the first matching element
					if (isForOne) {
						break;
					}
				} catch (BreakException e) {
					break;
				} catch (ContinueException e) {
					continue;
				} finally {
					env.popScope();
				}
			}
			// §8.2.2.6: ForExp returns null
			return WRAPPED_NULL;
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
		public Object caseTryExp(TryExp exp) {
			try {
				Object lastResult = WRAPPED_NULL;
				for (OclExpression bodyExpr : exp.getTryBody()) {
					lastResult = wrapNull(eval(bodyExpr));
				}
				return lastResult;
			} catch (RaiseException | FatalAssertionException ex) {
				// §8.2.2.13: search except clauses for matching type
				String exType = (ex instanceof RaiseException re) ? re.exceptionType : "AssertionFailed";

				for (CatchExp catchExp : exp.getExceptClause()) {
					if (matchesExceptClause(catchExp, exType)) {
						Object result = WRAPPED_NULL;
						for (OclExpression bodyExpr : catchExp.getBody()) {
							result = wrapNull(eval(bodyExpr));
						}
						return result;
					}
				}
				// No matching clause — re-throw
				throw ex;
			}
		}

		private boolean matchesExceptClause(CatchExp catchExp, String exType) {
			// Empty exception list = catch-all
			if (catchExp.getException().isEmpty()) {
				return true;
			}
			// Check if any declared exception type matches
			for (EClassifier declaredType : catchExp.getException()) {
				String typeName = declaredType.getName();
				if (typeName.equals(exType) || "Exception".equals(typeName)) {
					return true;
				}
			}
			return false;
		}

		@Override
		public Object caseRaiseExp(RaiseExp exp) {
			// §8.2.2.15: raise produces an exception
			String exType = "Exception";
			String argument = null;
			if (exp.getException() != null) {
				exType = exp.getException().getName();
			}
			if (exp.getArgument() != null) {
				Object argVal = eval(exp.getArgument());
				argument = argVal != null ? argVal.toString() : null;
			} else if (exp.getException() == null) {
				// raise with only string literal — stored as argument in the AST
				// This case is handled by the parser setting argument
			}
			throw new RaiseException(exType, argument);
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

			// §8.2.2.22: Create the instance BEFORE constructor runs
			EObject created = EcoreUtil.create(eClass);
			addToDefaultExtent(created);

			// Evaluate arguments
			List<Object> argValues = new ArrayList<>();
			for (OclExpression arg : exp.getArgument()) {
				argValues.add(eval(arg));
			}

			// §8.2.1.13: Look up matching constructor in module
			Constructor ctor = operationResolver.findConstructor(eClass, argValues.size());
			if (ctor != null) {
				callConstructor(ctor, created, argValues);
			} else if (!argValues.isEmpty()) {
				// §8.2.2.22: No constructor found but args present → error
				addError("No constructor found for " + eClass.getName()
						+ " with " + argValues.size() + " arguments");
			}
			// else: no args, implicit default constructor → no-op (object already created)

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

				// §8.2.2.20: fatal severity terminates with AssertionFailed
				// Don't add diagnostic here — if caught by try/except, transformation continues.
				// Uncaught fatal adds diagnostic in execute().
				if (severity == SeverityKind.FATAL) {
					throw new FatalAssertionException(message);
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

			// §8.2.1.24: Check referredObject variable for update semantics
			Variable refObj = exp.getReferredObject();
			String varName = (refObj != null && refObj.getName() != null)
					? refObj.getName() : "_objectExp";

			// Only check for update when variable was explicitly named (§8.2.1.24)
			// Default names "result" / "_objectExp" mean no explicit variable reference
			boolean isExplicitVar = !"result".equals(varName) && !"_objectExp".equals(varName);
			Object existing = isExplicitVar ? env.lookup(varName) : null;
			boolean isUpdate = (existing instanceof EObject);
			EObject target = isUpdate ? (EObject) existing : EcoreUtil.create(eClass);

			// §8.1.10: Apply default values from intermediate class feature definitions
			if (!isUpdate) {
				intermediateStore.initIntermediateClassDefaults(target, eClass);
			}

			env.pushScope();
			try {
				env.define(varName, target);
				// Dedicated variable for resolveImplicitPropertyTarget lookups
				env.define("_objectExp", target);
				// §8.2.1.17/§8.2.1.18: self is NOT rebound inside object expressions.
				// self always represents the contextual argument of the enclosing operation.
				// Unqualified property assignments resolve to the created object via _objectExp.

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

			// Only add to extent for newly created objects (not updates)
			if (!isUpdate) {
				addToDefaultExtent(target);
				// §8.2.1.24: Assign newly created object to the referred variable in outer scope
				if (isExplicitVar) {
					env.assign(varName, target);
				}
			}
			return wrapNull(target);
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

			// Find all candidate operations (§8.1.14.2: implicit disjunction)
			String opName = exp.getName();
			List<ImperativeOperation> candidates = operationResolver.findAllOperations(opName);
			if (candidates.isEmpty()) {
				addError("Mapping not found: " + opName);
				return WRAPPED_NULL;
			}

			boolean isStrict = exp.isIsStrict();

			// Single candidate: direct call
			if (candidates.size() == 1) {
				ImperativeOperation targetOp = candidates.get(0);
				if (isStrict && targetOp instanceof MappingOperation mappingOp) {
					return wrapNull(callMappingStrict(mappingOp, sourceObj, args));
				}
				return wrapNull(callOperation(targetOp, sourceObj, args));
			}

			// Multiple candidates: implicit disjunction (§8.1.14.2)
			// Filter by context type compatibility, then sort most-derived first
			List<ImperativeOperation> sorted = QvtoOperationResolver.filterAndSortByType(candidates, sourceObj);
			for (ImperativeOperation candidate : sorted) {
				if (candidate instanceof MappingOperation mappingOp) {
					Object result = isStrict
							? callMappingStrict(mappingOp, sourceObj, args)
							: callMapping(mappingOp, sourceObj, args);
					if (result != GUARD_FAILED) {
						return wrapNull(result);
					}
				}
			}
			// No candidate matched (§8.1.14: returns null)
			return WRAPPED_NULL;
		}

		@Override
		public Object caseResolveExp(ResolveExp exp) {
			if (exp.isIsDeferred() && !isDeferredExecution) {
				// Deferred (late) resolve: capture source eagerly, defer actual resolve
				// §8.1.11.3: null source = search all records
				Object sourceObj = exp.getOwnedSource() != null
						? eval(exp.getOwnedSource()) : null;
				if (pendingLvalueOwner != null) {
					deferredTasks.add(new DeferredResolveTask(
							exp, pendingLvalueOwner, pendingLvalueFeature,
							pendingLvalueIsReset, sourceObj));
				} else {
					addWarning("Late resolve only supported in property assignments");
				}
				return WRAPPED_NULL;
			}

			// Source object: §8.1.11.3 — when no explicit source, search ALL trace records (null)
			Object sourceObj = null;
			if (exp.getOwnedSource() != null) {
				sourceObj = eval(exp.getOwnedSource());
			}
			// else: sourceObj stays null → resolve searches all records

			// §8.1.11.7: coll->resolve(Type) = coll->xcollect(resolve(Type))
			// When source is a Collection, iterate per-element and collect results
			if (sourceObj instanceof Collection<?> coll) {
				List<EObject> allResults = new ArrayList<>();
				for (Object elem : coll) {
					allResults.addAll(resolveForSource(exp, elem));
				}
				if (exp.isOne()) {
					return wrapNull(allResults.isEmpty() ? null : allResults.get(0));
				}
				return wrapNull(allResults);
			}

			List<EObject> candidates = resolveForSource(exp, sourceObj);

			if (exp.isOne()) {
				return wrapNull(candidates.isEmpty() ? null : candidates.get(0));
			}
			return wrapNull(candidates);
		}

		/** Resolves candidates for a single source object (or null for all records). */
		private List<EObject> resolveForSource(ResolveExp exp, Object sourceObj) {
			// Target type — may be EClass directly or wrapped in ClassifierType
			EClass targetType = null;
			Variable target = exp.getTarget();
			if (target != null) {
				if (target.getType() instanceof EClass tc) {
					targetType = tc;
				} else if (target.getType() instanceof ClassifierType ct
						&& ct.getReferredClassifier() instanceof EClass tc) {
					targetType = tc;
				}
			}

			// Resolve — dispatch based on inverse/in-mapping flags
			List<EObject> candidates;
			if (exp instanceof ResolveInExp resolveIn && resolveIn.getInMapping() != null) {
				String mappingName = resolveIn.getInMapping().getName();
				if (exp.isIsInverse()) {
					candidates = traceManager.invResolveIn(mappingName, sourceObj, targetType);
				} else {
					candidates = traceManager.resolveIn(mappingName, sourceObj, targetType);
				}
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
			return candidates;
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
