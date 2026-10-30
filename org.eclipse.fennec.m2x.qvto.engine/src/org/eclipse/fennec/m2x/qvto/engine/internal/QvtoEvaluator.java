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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.IdentityHashMap;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.SequencedSet;
import java.util.Set;
import java.util.function.Supplier;

import org.eclipse.emf.common.util.BasicDiagnostic;
import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.common.util.TreeIterator;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.imperativeocl.AltExp;
import org.eclipse.fennec.m2x.model.imperativeocl.AssertExp;
import org.eclipse.fennec.m2x.model.imperativeocl.AssignExp;
import org.eclipse.fennec.m2x.model.imperativeocl.BlockExp;
import org.eclipse.fennec.m2x.model.imperativeocl.BreakExp;
import org.eclipse.fennec.m2x.model.imperativeocl.CatchExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ComputeExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ContinueExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ForExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ImperativeIterateExp;
import org.eclipse.fennec.m2x.model.imperativeocl.InstantiationExp;
import org.eclipse.fennec.m2x.model.imperativeocl.LogExp;
import org.eclipse.fennec.m2x.model.imperativeocl.RaiseExp;
import org.eclipse.fennec.m2x.model.imperativeocl.ReturnExp;
import org.eclipse.fennec.m2x.model.imperativeocl.SeverityKind;
import org.eclipse.fennec.m2x.model.imperativeocl.SwitchExp;
import org.eclipse.fennec.m2x.model.imperativeocl.TransformationInstantiationExp;
import org.eclipse.fennec.m2x.model.imperativeocl.TryExp;
import org.eclipse.fennec.m2x.model.imperativeocl.VariableInitExp;
import org.eclipse.fennec.m2x.model.imperativeocl.WhileExp;
import org.eclipse.fennec.m2x.model.imperativeocl.util.ImperativeOclSwitch;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.IfExp;
import org.eclipse.fennec.m2x.model.ocl.IteratorExp;
import org.eclipse.fennec.m2x.model.ocl.MapType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.ocl.util.OclSwitch;
import org.eclipse.fennec.m2x.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2x.model.qvtoperational.ConstructorBody;
import org.eclipse.fennec.m2x.model.qvtoperational.ContextualProperty;
import org.eclipse.fennec.m2x.model.qvtoperational.DirectionKind;
import org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingCallExp;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2x.model.qvtoperational.ObjectExp;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationBody;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.ResolveExp;
import org.eclipse.fennec.m2x.model.qvtoperational.ResolveInExp;
import org.eclipse.fennec.m2x.model.qvtoperational.VarParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.util.QvtOperationalSwitch;
import org.eclipse.fennec.m2x.model.trace.Trace;
import org.eclipse.fennec.m2x.ocl.api.OclContext;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.ocl.api.OclResult;
import org.eclipse.fennec.m2x.ocl.api.OclValues;
import org.eclipse.fennec.m2x.ocl.api.SourcePosition;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoEvaluationOptions;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoControlFlowException.BreakException;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoControlFlowException.ContinueException;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoControlFlowException.FatalAssertionException;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoControlFlowException.RaiseException;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoControlFlowException.ReturnException;

/**
 * Imperative AST interpreter for QVT-O transformations.
 *
 * <p>Uses a 3-level switch dispatch: ImperativeOCL → QvtOperational → OCL delegation.
 * OCL sub-expressions are delegated to {@link OclEngine} via snapshot contexts.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoEvaluator {

	private static final String SOURCE_ID = "org.eclipse.fennec.m2x.qvto.engine";

	/**
	 * Internal sentinel indicating the switch did not handle the node.
	 * Both inner switches return this from defaultCase().
	 */
	private static final Object UNHANDLED = new Object();

	/** Sentinel for wrapping null values (EMF switches treat null as "not handled"). */
	private static final Object WRAPPED_NULL = new Object();

	/** Sentinel indicating a mapping's when-guard failed (§8.1.14: disjunct candidate selection). */
	private static final Object GUARD_FAILED = new Object();

	private final OclEngine oclEngine;
	private final QvtoEvalEnvironment env;
	private final QvtoEvaluationOptions options;
	/** Built on first use, see {@link #oclOptions()}. */
	private OclEvaluationOptions oclOptions;
	private final OperationalTransformation transformation;
	private final QvtoExtentManager extentManager;
	private final QvtoTraceManager traceManager;
	private final List<Diagnostic> diagnostics = new ArrayList<>();
	private final ImperativeDispatch imperativeSwitch = new ImperativeDispatch();
	private final QvtOpDispatch qvtOpSwitch = new QvtOpDispatch();
	private final OclDispatch oclSwitch = new OclDispatch();
	private int stackDepth;

	/** Deadline for timeout enforcement (nanoseconds), or 0 if no timeout. */
	private long deadlineNanos;

	// §8.1.10: Intermediate property storage — per-instance values for ContextualProperty
	private final QvtoIntermediatePropertyStore intermediateStore;

	// §8.1.16 / §8.3.19: tag "alias" — maps alias name → (EClass, real feature name)
	private final EPackage.Registry packageRegistry;
	private final QvtoAliasRegistry aliasRegistry;
	// §8.3.19: predefined tags "proxy" and "topclasses"
	private final QvtoTagRegistry tagRegistry;
	private final QvtoOperationResolver operationResolver;
	/** The OCL engine's class for each collection kind, learnt from empty literals (#339). */
	private Map<CollectionKind, Class<?>> collectionClasses;
	/** The classes whose ambiguous extent was reported already (#337). */
	private final Set<EClass> ambiguousClasses = new LinkedHashSet<>();
	/** Per operation: its in, inout and out parameters (#336). */
	private final Map<ImperativeOperation, List<VarParameter>> formalParameters = new IdentityHashMap<>();
	/** Per operation: whether it has out or inout parameters (#336). */
	private final Map<ImperativeOperation, Boolean> passesBackOperations = new IdentityHashMap<>();
	/** Per operation name: whether one of that name has out or inout parameters (#336). */
	private final Map<String, Boolean> passesBackByName = new HashMap<>();
	/** The last call the OCL engine made through {@link #callFromOcl} (#336). */
	private OclCall lastOclCall;
	private final QvtoBlackboxRegistry blackboxRegistry;
	private final QvtoModelOperations modelOperations;
	private final QvtoEngineServices engine;
	private final Map<String, Object> configProps;

	/**
	 * D29: the QVT-O operations every OCL sub-evaluation of this run sees.
	 *
	 * <p>Built on first use, because the provider needs this evaluator: it is what evaluates a
	 * QVT-O helper body an OCL expression calls. That cycle is why this used to be set after
	 * construction, from outside, by whoever remembered to (#185).
	 */
	private List<OclOperationProvider> additionalProviders;

	// Late resolve support (§8.2.1.22, §8.2.2.11)
	private final List<DeferredAssignment> deferredAssignments = new ArrayList<>();
	private boolean isDeferredExecution;
	// While a deferred assignment runs: the late resolves' sources captured when it was reached
	private Map<ResolveExp, Object> deferredResolveSources = Map.of();

	/**
	 * Creates an evaluator for a single transformation execution.
	 *
	 * @param oclEngine the OCL engine expressions are evaluated with
	 * @param env the variable environment
	 * @param run what runs, on what, under which options
	 * @param engine what this evaluation needs from the engine that started it
	 */
	public QvtoEvaluator(OclEngine oclEngine, QvtoEvalEnvironment env, QvtoRun run,
			QvtoEngineServices engine) {
		this.oclEngine = Objects.requireNonNull(oclEngine, "oclEngine must not be null");
		this.env = Objects.requireNonNull(env, "env must not be null");
		Objects.requireNonNull(run, "run must not be null");
		this.options = run.options();
		this.transformation = run.transformation();
		this.packageRegistry = run.packageRegistry();
		this.blackboxRegistry = run.blackboxRegistry();
		this.configProps = run.context().configProperties();
		// Derived: extents belong to exactly this transformation and this context
		this.extentManager = new QvtoExtentManager(transformation, run.context());
		this.engine = Objects.requireNonNull(engine, "engine must not be null");
		this.traceManager = new QvtoTraceManager(options.maxTraceRecords());
		this.intermediateStore = new QvtoIntermediatePropertyStore(transformation, this::eval);
		this.aliasRegistry = new QvtoAliasRegistry(transformation, extentManager, packageRegistry, diagnostics);
		this.tagRegistry = new QvtoTagRegistry(transformation);
		this.operationResolver = new QvtoOperationResolver(transformation);
		this.modelOperations = new QvtoModelOperations(this::eval, this::extentOf);
	}

	/**
	 * Returns the package registry this execution resolves metamodels against (D42).
	 *
	 * @return the package registry, never {@code null}
	 */
	public EPackage.Registry packageRegistry() {
		return packageRegistry;
	}

	/**
	 * Executes the transformation's main() entry operation.
	 *
	 * @return the collected diagnostics
	 */
	public List<Diagnostic> execute() {
		prepare();
		if (!modelsConform()) {
			return diagnostics;
		}

		ImperativeOperation mainOp = operationResolver.findMainOperation();
		if (mainOp == null) {
			addError("No main() entry operation found in transformation '"
					+ transformation.getName() + "'");
			return diagnostics;
		}
		invokeGuarded(mainOp, null, new Object[0]);
		processDeferredTasks();
		return diagnostics;
	}

	/**
	 * Invokes one operation of the transformation, outside {@code main()} — what a QVT-R
	 * {@code implementedby} clause calls (QVT v1.3 §7.11.3.6, #282).
	 *
	 * <p>The transformation is set up as for {@link #execute()} — model parameters, configuration
	 * properties, module properties — and then only the operation runs.
	 *
	 * @param operation the operation to invoke
	 * @param self its context object, or {@code null} for an operation without one
	 * @param args its arguments
	 * @return the operation's result, or {@code null}
	 */
	public Object invoke(ImperativeOperation operation, Object self, Object[] args) {
		prepare();
		if (!modelsConform()) {
			return null;
		}
		Object result = invokeGuarded(operation, self, args);
		processDeferredTasks();
		return result;
	}

	/**
	 * Calls an operation the way an entry point is called: a return, a fatal assertion and an
	 * uncaught exception end it and are reported here.
	 */
	private Object invokeGuarded(ImperativeOperation operation, Object self, Object[] args) {
		try {
			return callOperation(operation, self, args);
		} catch (ReturnException e) {
			return e.value;
		} catch (FatalAssertionException e) {
			// §8.2.2.20: fatal assert terminates execution
			addError("Assertion failed: " + e.message);
		} catch (RaiseException e) {
			// §8.2.2.15: uncaught raise terminates execution
			addError("Uncaught exception: " + e.exceptionType
					+ (e.argument != null ? " - " + e.argument : ""));
		} catch (StackOverflowError e) {
			// Every QVT-O call takes many Java frames, so a deep recursion can exhaust the Java
			// stack before maxStackDepth is reached. Reported, not thrown out of execute() (#332)
			addError("Recursion too deep: the Java stack overflowed before maxStackDepth ("
					+ options.maxStackDepth() + ") was reached");
		}
		return null;
	}

	/**
	 * Sets the transformation up for a run: deadline, model parameters, configuration and module
	 * properties, {@code this}, and the alias and tag registries.
	 */
	/**
	 * Checks the in and inout models against their model types before anything runs (§8.2.1.6:
	 * "model compliance implies that all the extra conditions … are satisfied"; §8.1.2). A
	 * {@code strict} model type admits only objects of its own packages; every {@code where}
	 * condition must hold with {@code self} bound to the model. Before #337 both were parsed and
	 * never read. {@code effective} conformance (§8.2.1.6) is name-based and not checked.
	 *
	 * @return whether the transformation may run
	 */
	private boolean modelsConform() {
		boolean conform = true;
		for (ModelParameter mp : transformation.getModelParameter()) {
			if (mp.getKind() == DirectionKind.OUT || mp.isSetCollectionKind()
					|| !(mp.getEType() instanceof ModelType modelType)
					|| !(env.lookup(mp.getName()) instanceof QvtoModelExtent model)) {
				continue;
			}
			if ("strict".equals(modelType.getConformanceKind())) {
				EObject foreign = firstForeignObject(model, modelType);
				if (foreign != null) {
					addError("Model '" + mp.getName() + "' does not conform to the strict model type '"
							+ modelType.getName() + "': " + foreign.eClass().getName() + " is not in its metamodel");
					conform = false;
				}
			}
			if (!modelType.getAdditionalCondition().isEmpty()) {
				env.pushScope();
				try {
					env.define("self", model);
					for (OclExpression condition : modelType.getAdditionalCondition()) {
						if (!Boolean.TRUE.equals(eval(condition))) {
							addError("Model '" + mp.getName() + "' does not satisfy the where condition of model type '"
									+ modelType.getName() + "'");
							conform = false;
							break;
						}
					}
				} finally {
					env.popScope();
				}
			}
		}
		return conform;
	}

	/** The first object of the model whose class is in none of the model type's packages. */
	private static EObject firstForeignObject(QvtoModelExtent model, ModelType modelType) {
		for (EObject root : model.getContents()) {
			if (!inMetamodel(root.eClass(), modelType)) {
				return root;
			}
			for (TreeIterator<EObject> it = root.eAllContents(); it.hasNext();) {
				EObject child = it.next();
				if (!inMetamodel(child.eClass(), modelType)) {
					return child;
				}
			}
		}
		return null;
	}

	/** Whether the class belongs to one of the model type's packages or their subpackages. */
	private static boolean inMetamodel(EClass eClass, ModelType modelType) {
		for (EPackage pkg = eClass.getEPackage(); pkg != null; pkg = pkg.getESuperPackage()) {
			for (EPackage metamodel : modelType.getMetamodel()) {
				if (pkg == metamodel || (pkg.getNsURI() != null && pkg.getNsURI().equals(metamodel.getNsURI()))) {
					return true;
				}
			}
		}
		return false;
	}

	private void prepare() {
		// Set timeout deadline (Q-5 / M-3)
		if (options.timeout() != null) {
			deadlineNanos = System.nanoTime() + options.timeout().toNanos();
		}

		// Bind model parameters as variables (e.g. 'in s : SRC' → extent)
		EList<ModelParameter> modelParams = transformation.getModelParameter();
		for (int i = 0; i < modelParams.size(); i++) {
			ModelParameter mp = modelParams.get(i);
			if (mp.getName() == null) {
				continue;
			}
			if (mp.isSetCollectionKind()) {
				// §8.1.1, §8.2.1.5: Collection-of-models parameter — bind aggregated extent
				QvtoModelExtent aggregated = extentManager.getAggregatedExtent(mp.getName());
				if (aggregated != null) {
					env.define(mp.getName(), aggregated);
				}
			} else {
				// Try name-based first (for Builder API), fall back to positional
				QvtoModelExtent extent = extentManager.getExtent(mp.getName());
				if (extent == null) {
					extent = extentManager.getExtent(i);
				}
				if (extent != null) {
					env.define(mp.getName(), extent);
				}
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
		// §8.3.19: Build predefined tag registry (proxy, topclasses)
		tagRegistry.build();

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
	}

	/**
	 * Evaluates a QVT-O expression using 3-level dispatch.
	 */
	Object eval(OclExpression expr) {
		if (expr == null) {
			return null;
		}

		// One dispatch per metamodel, and nothing else. The three switches are generated from
		// the three packages an expression can come from, and each of them answers UNHANDLED
		// for a node that is not its own. Before #193 the third was an instanceof chain of six
		// special cases in this method, which meant a new expression type had to be added in
		// two places and would otherwise be silently half-implemented — evaluated by the OCL
		// engine, which does not know QVT-O's semantics.
		Object result = imperativeSwitch.doSwitch(expr);
		if (result != UNHANDLED) {
			return unwrapNull(result);
		}
		result = qvtOpSwitch.doSwitch(expr);
		if (result != UNHANDLED) {
			return unwrapNull(result);
		}
		result = oclSwitch.doSwitch(expr);
		if (result != UNHANDLED) {
			return unwrapNull(result);
		}

		// What no QVT-O rule claims is plain OCL, and the OCL engine evaluates it
		return evalOcl(expr);
	}

	/**
	 * The OCL expressions QVT-O evaluates itself, rather than handing to the OCL engine.
	 *
	 * <p>Each of these has a QVT-O meaning the OCL engine cannot know: an extent operation, an
	 * iterator whose source may be one, an {@code if} whose branches may be imperative, an
	 * intermediate property, a {@code +} that treats null as the empty string, an operation
	 * whose source is an imperative expression. A node this switch does not claim answers
	 * {@link #UNHANDLED} and goes to OCL.
	 */
	private class OclDispatch extends OclSwitch<Object> {

		@Override
		public Object caseOperationCallExp(OperationCallExp opCall) {
			// The operation name is checked before the source is evaluated, so that a source
			// with a side effect is not evaluated for an operation this switch does not claim
			// (String::incrStrCounter('Foo').repr() — repr is neither an extent nor an element
			// operation, so the counter must not be incremented here)
			if (opCall.getOwnedSource() != null) {
				String opName = opCall.getName();
				if (QvtoModelOperations.isExtentOperation(opName)) {
					Object handled = modelOperations.handleExtentOperation(opCall);
					if (handled != QvtoModelOperations.UNHANDLED) {
						return wrapNull(QvtoModelOperations.unwrapNull(handled));
					}
				}
				if (QvtoModelOperations.isElementOperation(opName)) {
					// §8.3.4: metaClassName, subobjects, clone and the rest
					Object handled = modelOperations.handleElementOperation(opCall);
					if (handled != QvtoModelOperations.UNHANDLED) {
						return wrapNull(QvtoModelOperations.unwrapNull(handled));
					}
				}
			}
			// §8.2.1.12: out and inout arguments of a helper or mapping called in an OCL
			// expression — the OCL engine evaluates the call, this hands the values back (#336)
			if (opCall.getName() != null && passesBackByName(opCall.getName())) {
				lastOclCall = null;
				Object value = evalOcl(opCall);
				OclCall call = lastOclCall;
				if (call != null && call.args().length == opCall.getOwnedArguments().size()) {
					writeBackArgs(opCall.getOwnedArguments(), call.passed(), call.args());
				}
				return wrapNull(value);
			}
			// QVT-O reads null as the empty string in a string concatenation, where OCL's
			// strict mode rejects the argument
			if ("+".equals(opCall.getName())) {
				Object source = eval(opCall.getOwnedSource());
				if (source instanceof String || source == null) {
					Object arg = opCall.getOwnedArguments().isEmpty() ? null
							: eval(opCall.getOwnedArguments().get(0));
					return (source != null ? source.toString() : "")
							+ (arg != null ? arg.toString() : "");
				}
			}
			// §8.1.13: an operation on an imperative source — "new T(m).transform()",
			// "list[T]->first()" — is the OCL engine's like any other call: it hands the source
			// back to evalNestedInOcl, evaluating it once, and dispatches on its value (#296)
			return UNHANDLED;
		}

		@Override
		public Object caseIteratorExp(IteratorExp iterExp) {
			// Through the QVT-O dispatch, so that an extent operation as the source
			// (rootObjects, objectsOfType) and imperative bodies work
			return wrapNull(evalIteratorExp(iterExp));
		}

		@Override
		public Object caseIfExp(IfExp ifExp) {
			// Here rather than in OCL, so that an imperative branch (a BlockExp, say) is
			// dispatched through this evaluator
			return wrapNull(evalIfExp(ifExp));
		}

		@Override
		public Object defaultCase(EObject object) {
			return UNHANDLED;
		}
	}

	/**
	 * Evaluates an OCL IfExp using the QVT-O dispatch chain so that imperative
	 * body expressions (BlockExp, etc.) are handled correctly.
	 * §8.2.2.8: imperative if — else is optional (returns null when absent).
	 *
	 * <p>A condition that is neither true nor false takes no branch, the else branch included:
	 * "otherwise the result is invalid" (OCL v2.4 §10.3, IfExpEval [1]). Imperatively that is a
	 * statement that does not run, so it is an error of the transformation (#308).
	 */
	private Object evalIfExp(IfExp ifExp) {
		Object condVal = eval(ifExp.getOwnedCondition());
		if (Boolean.TRUE.equals(condVal)) {
			return eval(ifExp.getOwnedThen());
		}
		if (!Boolean.FALSE.equals(condVal)) {
			return undefinedCondition("if", condVal);
		}
		if (ifExp.getOwnedElse() != null) {
			return eval(ifExp.getOwnedElse());
		}
		return null;
	}

	/**
	 * Reports a condition that is neither true nor false (#308).
	 *
	 * @return invalid, the value of the construct
	 */
	/** The value of a condition that is not true, for an assertion message: "" for false. */
	private static String conditionValue(Object condition) {
		if (Boolean.FALSE.equals(condition)) {
			return "";
		}
		return " — the condition is " + (condition == null ? "null"
				: condition == OclInvalid.INSTANCE ? "invalid" : condition.getClass().getSimpleName());
	}

	private Object undefinedCondition(String construct, Object condition) {
		return undefinedCondition(construct, condition, "no branch is taken");
	}

	/**
	 * Reports a condition that is neither true nor false, and what does not happen because of
	 * it (#308, #310). A statement that cannot run is an error of the transformation.
	 *
	 * @return invalid, the value of the construct
	 */
	private Object undefinedCondition(String construct, Object condition, String consequence) {
		addError(construct + "-condition must be Boolean, got: "
				+ (condition == null ? "null"
						: condition == OclInvalid.INSTANCE ? "invalid"
						: condition.getClass().getSimpleName())
				+ " — " + consequence);
		return OclInvalid.INSTANCE;
	}

	/**
	 * Evaluates an OCL IteratorExp (select, reject, collect, etc.) using the
	 * QVT-O dispatch chain for source evaluation, so that extent operations
	 * (rootObjects, objectsOfType) and imperative expressions are handled.
	 *
	 * <p>Bounded by {@code maxLoopIterations}, like the other loops of this evaluator. The
	 * limit had three checkpoints — {@code while}, {@code forEach}/{@code forOne} and
	 * {@code caseImperativeIterateExp} — and the third is not reached by anything the parser
	 * builds, so {@code xcollect} and its siblings ran unbounded (#175). QVT v1.3 §8.2.2.7
	 * defines those variants in terms of {@code forEach}, which is bounded; a source collection
	 * this evaluator will not iterate is refused before the loop rather than counted through it.
	 */
	private Object evalIteratorExp(IteratorExp iterExp) {
		Object sourceVal = eval(iterExp.getOwnedSource());
		if (!(sourceVal instanceof Collection<?> coll)) {
			// Fallback to OCL for non-collection sources
			return evalOcl(iterExp);
		}
		if (coll.size() > options.maxLoopIterations()) {
			addError("Maximum loop iterations exceeded: " + options.maxLoopIterations());
			return WRAPPED_NULL;
		}

		List<Variable> iterVars = iterExp.getOwnedIterators();
		String iterName = iterVars.isEmpty() ? "_it" : iterVars.get(0).getName();
		OclExpression body = iterExp.getOwnedBody();
		String opName = iterExp.getName();

		// The boolean iterators answer as the OCL engine does for the same expression (OCL v2.4
		// §11.9): a body that is neither true nor false makes select and reject invalid, and
		// counts in forAll, exists and any by their own precedence (#310). Read as false, the
		// same expression had one result here and another one in the OCL engine.
		return switch (opName) {
			case "select", "reject" -> {
				boolean keep = "select".equals(opName);
				List<Object> result = new ArrayList<>();
				for (Object elem : coll) {
					env.pushScope();
					try {
						env.define(iterName, elem);
						Object bodyVal = eval(body);
						if (!(bodyVal instanceof Boolean matched)) {
							yield OclInvalid.INSTANCE;
						}
						if (matched == keep) {
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
						// In QVT-O, collect IS the imperative xcollect: an arrow call without
						// brackets means the xcollect construct (§8.2.2.7 notation note), a
						// collection mapping call is its shorthand (§8.2.1.21), and its
						// semantics flattens the body and never lets null into the result —
						// "if (target <> null) res += target" (§8.2.2.7 Semantics, #228). A
						// guarded-out mapping therefore contributes nothing.
						if (bodyVal instanceof Collection<?> nested) {
							for (Object target : nested) {
								if (target != null && target != WRAPPED_NULL) {
									result.add(target);
								}
							}
						} else if (bodyVal != null && bodyVal != WRAPPED_NULL) {
							result.add(bodyVal);
						}
					} finally {
						env.popScope();
					}
				}
				yield result;
			}
			case "forAll", "exists" -> {
				// §11.9.1: the deciding value wins — false for forAll, true for exists — then
				// invalid, then null, then the other one
				Boolean deciding = "exists".equals(opName);
				boolean hasInvalid = false;
				boolean hasNull = false;
				for (Object elem : coll) {
					env.pushScope();
					try {
						env.define(iterName, elem);
						Object bodyVal = eval(body);
						if (deciding.equals(bodyVal)) {
							yield deciding;
						}
						if (bodyVal == OclInvalid.INSTANCE) {
							hasInvalid = true;
						} else if (!(bodyVal instanceof Boolean)) {
							hasNull = true;
						}
					} finally {
						env.popScope();
					}
				}
				yield hasInvalid ? OclInvalid.INSTANCE : hasNull ? WRAPPED_NULL : !deciding;
			}
			case "any" -> {
				boolean hasInvalid = false;
				for (Object elem : coll) {
					env.pushScope();
					try {
						env.define(iterName, elem);
						Object bodyVal = eval(body);
						if (Boolean.TRUE.equals(bodyVal)) {
							yield elem;
						}
						hasInvalid |= bodyVal == OclInvalid.INSTANCE;
					} finally {
						env.popScope();
					}
				}
				// as the OCL engine: no match, and a body that was invalid — invalid
				yield hasInvalid ? OclInvalid.INSTANCE : wrapNull(null);
			}
			default -> evalOcl(iterExp);
		};
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
		// §8.1.10: Intermediate property interceptor for OCL sub-expressions; and the imperative
		// expressions nested in the OCL one come back here (#296)
		// The transformation's models are the extent allInstances() ranges over (#342)
		OclContext oclCtx = new OclContext(self, extentManager::allInstances, vars, null,
				intermediateStore.hasIntermediateProperties()
						? intermediateStore::interceptIntermediateProperty
						: null,
				this::evalNestedInOcl);
		OclResult oclResult = oclEngine.evaluateWithDiagnostics(expr, oclCtx, oclOptions());
		// Placed, not just copied: the OCL engine names the node it stumbled over, and the parser
		// recorded where that node stood — including in an imported unit (#116).
		oclResult.diagnostics().forEach(this::addOclDiagnostic);
		return oclResult.value();
	}



	/**
	 * A QVT-O expression nested in one the OCL engine evaluates — {@code list[T]->first()},
	 * {@code x.late resolveone(T)->asOrderedSet()} (§8.1.13, #296). It is evaluated once, here,
	 * with the iterator and let variables the OCL engine has bound around it.
	 *
	 * @param expr the nested expression
	 * @param oclBindings the variables the OCL evaluation has bound
	 * @return its value
	 */
	private Object evalNestedInOcl(OclExpression expr, Map<String, Object> oclBindings) {
		if (expr.eClass().getEPackage() == OclPackage.eINSTANCE) {
			// An OCL node the OCL engine itself does not know — not ours either
			addError("Unsupported expression type: " + expr.eClass().getName());
			return OclInvalid.INSTANCE;
		}
		env.pushScope();
		try {
			oclBindings.forEach(env::define);
			return eval(expr);
		} finally {
			env.popScope();
		}
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
		if (checkTimeout()) {
			--stackDepth;
			addError("Execution timeout exceeded");
			return null;
		}
		try {
			env.pushScope();
			bindOperationParams(operation, self, args, false);

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
			// §8.2.2.14: "return;" answers the result parameters — a tuple of several (#336)
			return e.valueless ? currentResult(operation.getResult()) : e.value;
		} catch (BreakException | ContinueException e) {
			return loopControlLeftOperation(operation);
		} finally {
			// §8.2.1.12: "the final value of each out, inout and result parameter is returned
			// to the caller" — written into the caller's argument array (#336)
			exportOutArgs(operation, args);
			env.popScope();
			--stackDepth;
		}
	}

	/**
	 * A break or continue that reached the boundary of its operation had no loop of its own to
	 * act on (§8.2.2.17/§8.2.2.18). The parser rejects it; an AST built elsewhere reaches this,
	 * which stops it here instead of letting it end the caller's loop (#338).
	 */
	private Object loopControlLeftOperation(ImperativeOperation operation) {
		addError("break or continue outside of a loop in '" + operation.getName() + "'");
		return null;
	}

	/**
	 * Executes a mapping in strict mode (xmap): when-guard is a pre-condition.
	 * If the when-guard fails, this is an assertion failure (§8.2.1.15).
	 */
	private Object callMappingStrict(MappingOperation mappingOp, Object self, Object[] args) {
		// §8.1.14.3: "For strict evaluation … the first candidate for which the source type
		// conforms is selected without evaluating the predicate as a guard. The predicate is
		// instead evaluated as a pre-condition" (#333)
		MappingOperation target = mappingOp;
		if (!mappingOp.getDisjunct().isEmpty()) {
			target = null;
			for (MappingOperation candidate : mappingOp.getDisjunct()) {
				if (typesConform(candidate, self, args)) {
					target = candidate;
					break;
				}
			}
		} else if (!typesConform(mappingOp, self, args)) {
			target = null;
		}
		if (target == null) {
			// §8.2.1.21: "failure to evaluate the when clause as a pre-condition causes the
			// mapping execution to fail" — a fatal assertion, as in Eclipse (#335)
			throw new FatalAssertionException("xmap pre-condition failed for mapping: " + mappingOp.getName()
					+ " — no candidate conforms to the source and argument types");
		}
		// The predicate is evaluated once, by callMapping, as the pre-condition (#335)
		Object result = callMapping(target, self, args, true);
		return result == GUARD_FAILED ? null : result;
	}

	/**
	 * Calls a mapping by name the way {@code source.map name(args)} does (§8.1.14.2, §8.2.1.15):
	 * the overloads of that name and argument count are the candidates, those whose types the
	 * source and arguments conform to are tried most-derived first, and the first whose guard
	 * holds answers; none → null. Shared by the QVT-O evaluator and the OCL engine, which calls
	 * mappings that stand inside an OCL expression through the operation provider (#333).
	 *
	 * @param strict {@code xmap}: the first conforming candidate is selected and its guard is a
	 *        pre-condition
	 * @return the mapping's result, or {@code null}
	 */
	Object callMappingByName(String opName, Object sourceObj, Object[] args, boolean strict) {
		List<ImperativeOperation> named = operationResolver.findAllOperations(opName);
		if (named.isEmpty()) {
			addError("Mapping not found: " + opName);
			return null;
		}
		List<ImperativeOperation> candidates = named.stream()
				.filter(op -> parameterCount(op) == args.length).toList();
		if (candidates.isEmpty()) {
			addError("Mapping " + opName + " takes no " + args.length + " argument(s)");
			return null;
		}
		// Single candidate: direct call
		if (candidates.size() == 1) {
			ImperativeOperation targetOp = candidates.get(0);
			if (strict && targetOp instanceof MappingOperation mappingOp) {
				return callMappingStrict(mappingOp, sourceObj, args);
			}
			return callOperation(targetOp, sourceObj, args);
		}
		// Multiple candidates: implicit disjunction, most-derived context type first
		for (ImperativeOperation candidate : QvtoOperationResolver.filterAndSortByType(candidates, sourceObj)) {
			if (candidate instanceof MappingOperation mappingOp) {
				if (strict) {
					// §8.1.14.3: the first conforming candidate is selected; its predicate is a
					// pre-condition, not a guard that passes on to the next candidate
					return callMappingStrict(mappingOp, sourceObj, args);
				}
				Object result = callMapping(mappingOp, sourceObj, args);
				if (result != GUARD_FAILED) {
					return result;
				}
			}
		}
		return null; // No candidate matched (§8.1.14: returns null)
	}

	/**
	 * The implicit type predicate of a mapping (§8.1.14.1, §8.2.1.15): a contextual mapping needs
	 * a non-null source of its context type, and every argument of a class-typed parameter must
	 * be an instance of that class (null conforms to every type).
	 */
	private static boolean typesConform(MappingOperation mapping, Object self, Object[] args) {
		VarParameter context = mapping.getContext();
		if (context != null) {
			if (self == null) {
				return false;
			}
			if (context.getEType() instanceof EClass contextClass && !contextClass.isInstance(self)) {
				return false;
			}
		}
		int argIdx = 0;
		for (Object param : mapping.getEParameters()) {
			if (param instanceof VarParameter vp && !mapping.getResult().contains(vp)) {
				Object arg = argIdx < args.length ? args[argIdx] : null;
				argIdx++;
				// What the caller holds in an out argument is overwritten, not passed (#336)
				if (arg != null && vp.getKind() != DirectionKind.OUT
						&& vp.getEType() instanceof EClass parameterClass && !parameterClass.isInstance(arg)) {
					return false;
				}
			}
		}
		return true;
	}

	/** The number of in, inout and out parameters of an operation, results excluded. */
	static int parameterCount(ImperativeOperation operation) {
		int count = 0;
		for (Object param : operation.getEParameters()) {
			if (param instanceof VarParameter vp && !operation.getResult().contains(vp)) {
				count++;
			}
		}
		return count;
	}

	/**
	 * Executes a mapping operation with full mapping semantics:
	 * when-guard, init/population/end, result creation, where-postcondition, trace recording.
	 */
	private Object callMapping(MappingOperation mappingOp, Object self, Object[] args) {
		return callMapping(mappingOp, self, args, false);
	}

	/**
	 * @param strict {@code xmap}: the when clause is a pre-condition, whose failure is fatal
	 *        (§8.2.1.21), not a guard
	 */
	private Object callMapping(MappingOperation mappingOp, Object self, Object[] args, boolean strict) {
		// Disjuncts: try each disjunct, first with passing when-guard wins (§8.1.14.1)
		EList<MappingOperation> disjuncts = mappingOp.getDisjunct();
		if (disjuncts != null && !disjuncts.isEmpty()) {
			for (MappingOperation disjunct : disjuncts) {
				Object result = callMapping(disjunct, self, args);
				if (result != GUARD_FAILED) {
					// §8.1.11.4: the record counts for the invoked mapping too (resolveIn)
					traceManager.markInvoked(mappingOp, traceManager.lookupRecord(disjunct, self, traceKey(disjunct, args)));
					return result; // First candidate whose guard passes wins (even if result is null)
				}
			}
			return null; // No candidate matched
		}

		// §8.2.1.15: "After passing the parameters, the type compliance of the actual object
		// parameters as well as the when clause are checked. If this fails, null is returned" (#333)
		if (!typesConform(mappingOp, self, args)) {
			return GUARD_FAILED;
		}

		if (++stackDepth > options.maxStackDepth()) {
			--stackDepth;
			addError("Maximum stack depth exceeded: " + options.maxStackDepth());
			return null;
		}
		if (checkTimeout()) {
			--stackDepth;
			addError("Execution timeout exceeded");
			return null;
		}
		boolean traced = false;
		boolean ran = false;
		// §8.1.11.2: the record is matched on the context and the in parameters — what the
		// caller holds in an out argument plays no part (#336)
		Object[] keyArgs = traceKey(mappingOp, args);
		try {
			env.pushScope();
			bindOperationParams(mappingOp, self, args, false);

			// when-guard: all must be true, otherwise skip. A guard that cannot be decided is an
			// error, and the mapping does not run (§8.2.1.15, #310)
			for (OclExpression whenExpr : mappingOp.getWhen()) {
				Object whenVal = eval(whenExpr);
				if (!Boolean.TRUE.equals(whenVal)) {
					if (strict) {
						throw new FatalAssertionException("xmap pre-condition failed for mapping: "
								+ mappingOp.getName() + conditionValue(whenVal));
					}
					if (!Boolean.FALSE.equals(whenVal)) {
						undefinedCondition("when", whenVal,
								"mapping '" + mappingOp.getName() + "' is not executed");
					}
					return GUARD_FAILED;
				}
			}

			// Implicit inhibition (§8.2.1.15 p106, §8.1.11.2): an earlier execution with the same
			// context and in parameters answers — with its result, or none for a void mapping (#334)
			if (traceManager != null) {
				QvtoTraceRecord previous = traceManager.lookupRecord(mappingOp, self, keyArgs);
				if (previous != null) {
					importOutArgs(mappingOp, previous.outArgs());
					ran = true;
					return previous.result();
				}
			}
			ran = true;

			// §8.2.1.15: "The initial value of each out and result parameter is null … Collection
			// types are initialized with empty collections." (#332)
			EList<VarParameter> resultParams = mappingOp.getResult();
			for (VarParameter rp : resultParams) {
				if (env.lookup(rp.getName()) == null && rp.getEType() instanceof CollectionType collectionType) {
					env.assign(rp.getName(), emptyCollection(collectionType));
				}
			}

			// Execute mapping body (§8.2.1.15 p106-107):
			// init → instantiation → trace → inherited → population → end → merged
			OperationBody body = mappingOp.getBody();
			if (body instanceof MappingBody mappingBody) {
				// 1. Init section — the results are still null here, so init may assign them
				for (OclExpression initExpr : mappingBody.getInitSection()) {
					eval(initExpr);
				}
			}
			// "At the end of the initialization section, an implicit 'instantiation section' is
			// entered, which provokes the instantiation of all the out parameters … that still have
			// a null value." Then the relation holds: the trace is recorded now, so a mapping
			// reached again from its own population finds it instead of recursing (§8.1.6, #332)
			instantiateResults(resultParams);
			instantiateOutParams(mappingOp);
			recordTrace(mappingOp, self, keyArgs, currentOutArgs(mappingOp, args), currentResult(resultParams));
			traced = true;
			if (body instanceof MappingBody mappingBody) {
				// 2. Inherited mappings: after init+instantiation, before population (§8.1.15)
				for (MappingOperation inherited : mappingOp.getInherited()) {
					invokeReused(inherited, mappingOp, self, args);
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
					invokeReused(merged, mappingOp, self, args);
				}
			} else if (body != null) {
				for (OclExpression expr : body.getContent()) {
					eval(expr);
				}
			} else {
				// §8.4 S.168: bodyless mapping declaration — still execute inherited/merged
				for (MappingOperation inherited : mappingOp.getInherited()) {
					invokeReused(inherited, mappingOp, self, args);
				}
				for (MappingOperation merged : mappingOp.getMerged()) {
					invokeReused(merged, mappingOp, self, args);
				}
			}

			// where-postcondition
			OclExpression whereExpr = mappingOp.getWhere();
			if (whereExpr != null) {
				Object whereVal = eval(whereExpr);
				if (!Boolean.TRUE.equals(whereVal)) {
					// §8.2.1.15: "pre-conditions and post-conditions are assertions; any execution
					// failure is fatal" — one that is violated or cannot be decided ends the
					// transformation, as in Eclipse (MappingPostconditionFailed) (#335)
					throw new FatalAssertionException("where-postcondition failed for mapping: "
							+ mappingOp.getName() + conditionValue(whereVal));
				}
			}

			return currentResult(resultParams);
		} catch (ReturnException e) {
			Object result = e.value;
			if (result == null && !mappingOp.getResult().isEmpty()) {
				result = currentResult(mappingOp.getResult());
			}
			if (!traced) {
				recordTrace(mappingOp, self, keyArgs, currentOutArgs(mappingOp, args), result);
			}
			return result;
		} catch (BreakException | ContinueException e) {
			return loopControlLeftOperation(mappingOp);
		} finally {
			// A mapping that did not run (type or guard failed) leaves the caller's arguments
			// as they were
			if (ran) {
				exportOutArgs(mappingOp, args);
			}
			env.popScope();
			--stackDepth;
		}
	}

	/**
	 * The instantiation section of a mapping (§8.2.1.15): every result of a class type that is
	 * still null after {@code init} is created and added to its extent (§8.2.1.16, T@extent).
	 */
	private void instantiateResults(EList<VarParameter> resultParams) {
		for (VarParameter rp : resultParams) {
			if (env.lookup(rp.getName()) != null) {
				continue;
			}
			if (rp.getEType() instanceof EClass eClass && !eClass.isAbstract()) {
				EObject created = EcoreUtil.create(eClass);
				env.assign(rp.getName(), created);
				if (resultParams.size() == 1) {
					env.assign("result", created);
				}
				addToExtent(created, rp instanceof MappingParameter mp && mp.getExtent() != null
						? mp.getExtent().getName()
						: null);
			}
		}
	}

	/**
	 * The instantiation section for the out parameters of a mapping (§8.2.1.15): "the
	 * instantiation of all the out parameters that are object instances and that still have a
	 * null value" (#336). Before, only the result parameters were created.
	 */
	private void instantiateOutParams(MappingOperation mappingOp) {
		for (VarParameter parameter : formalParameters(mappingOp)) {
			if (parameter.getKind() == DirectionKind.OUT && env.lookup(parameter.getName()) == null
					&& parameter.getEType() instanceof EClass eClass && !eClass.isAbstract()) {
				EObject created = EcoreUtil.create(eClass);
				env.assign(parameter.getName(), created);
				addToExtent(created, parameter instanceof MappingParameter mp && mp.getExtent() != null
						? mp.getExtent().getName()
						: null);
			}
		}
		// §8.1.18: "result … is a tuple when there are multiple result parameters"
		if (mappingOp.getResult().size() > 1) {
			env.assign("result", collectMultiResult(mappingOp.getResult()));
		}
	}

	/**
	 * The in, inout and out parameters of an operation in call order, results excluded —
	 * computed once per operation, as every call asks for them.
	 */
	private List<VarParameter> formalParameters(ImperativeOperation operation) {
		return formalParameters.computeIfAbsent(operation, op -> {
			List<VarParameter> formals = new ArrayList<>();
			for (Object param : op.getEParameters()) {
				if (param instanceof VarParameter vp && !op.getResult().contains(vp)) {
					formals.add(vp);
				}
			}
			return List.copyOf(formals);
		});
	}

	/** Whether an operation hands values back through out or inout parameters. */
	private boolean passesBack(ImperativeOperation operation) {
		return passesBackOperations.computeIfAbsent(operation,
				op -> formalParameters(op).stream().anyMatch(QvtoEvaluator::passesBack));
	}

	/** Whether the final value of a parameter goes back to the caller (§8.2.1.12). */
	private static boolean passesBack(VarParameter parameter) {
		return parameter.getKind() == DirectionKind.OUT || parameter.getKind() == DirectionKind.INOUT;
	}

	/** The arguments a trace record is matched on: the out positions are blanked (§8.1.11.2). */
	private Object[] traceKey(MappingOperation mappingOp, Object[] args) {
		if (!passesBack(mappingOp)) {
			return args;
		}
		List<VarParameter> formals = formalParameters(mappingOp);
		Object[] key = null;
		for (int i = 0; i < formals.size() && i < args.length; i++) {
			if (formals.get(i).getKind() == DirectionKind.OUT && args[i] != null) {
				if (key == null) {
					key = args.clone();
				}
				key[i] = null;
			}
		}
		return key != null ? key : args;
	}

	/** A copy of the arguments carrying the current out and inout values, or null without any. */
	private Object[] currentOutArgs(ImperativeOperation operation, Object[] args) {
		if (!passesBack(operation)) {
			return null;
		}
		Object[] outs = args.clone();
		exportOutArgs(operation, outs);
		return outs;
	}

	/** Writes the current values of the out and inout parameters into the argument array. */
	private void exportOutArgs(ImperativeOperation operation, Object[] args) {
		if (!passesBack(operation)) {
			return;
		}
		List<VarParameter> formals = formalParameters(operation);
		for (int i = 0; i < formals.size() && i < args.length; i++) {
			if (passesBack(formals.get(i))) {
				args[i] = env.lookup(formals.get(i).getName());
			}
		}
	}

	/** Sets the out and inout parameters from an argument array (null: nothing to set). */
	private void importOutArgs(ImperativeOperation operation, Object[] args) {
		if (args == null || !passesBack(operation)) {
			return;
		}
		List<VarParameter> formals = formalParameters(operation);
		for (int i = 0; i < formals.size() && i < args.length; i++) {
			if (passesBack(formals.get(i))) {
				env.assign(formals.get(i).getName(), args[i]);
			}
		}
	}

	/**
	 * Hands the final out and inout values of a call back to the caller (§8.2.1.12: "passed by
	 * reference-to-variable … These updates are visible to the caller"): every argument whose
	 * value the call changed is assigned to its argument expression, a variable or a property.
	 * Another expression has nowhere to take the value (#336).
	 */
	private void writeBackArgs(List<OclExpression> argExprs, Object[] passed, Object[] args) {
		for (int i = 0; i < argExprs.size() && i < args.length; i++) {
			if (args[i] == passed[i]) {
				continue;
			}
			OclExpression argExpr = argExprs.get(i);
			if (argExpr instanceof VariableExp varExp && varExp.getReferredVariable() != null
					&& env.contains(varExp.getReferredVariable().getName())) {
				env.assign(varExp.getReferredVariable().getName(), args[i]);
			} else if (argExpr instanceof PropertyCallExp propExp && propExp.getReferredProperty() != null
					&& eval(propExp.getOwnedSource()) instanceof EObject target
					&& target.eClass().getEAllStructuralFeatures().contains(propExp.getReferredProperty())) {
				checkNotReadOnly(target);
				target.eSet(propExp.getReferredProperty(), args[i]);
			}
		}
	}

	/**
	 * Calls an operation for the OCL engine, which knows only the argument values: the call is
	 * noted, so that the expression that made it can hand the out and inout values back to its
	 * argument expressions (#336).
	 */
	Object callFromOcl(Object[] args, Supplier<Object> call) {
		Object[] passed = args.clone();
		Object result = call.get();
		lastOclCall = new OclCall(passed, args);
		return result;
	}

	/** The arguments of the last call through {@link #callFromOcl}, before and after it. */
	private record OclCall(Object[] passed, Object[] args) {
	}

	/** Whether an operation of this name hands values back through out or inout parameters. */
	private boolean passesBackByName(String name) {
		return passesBackByName.computeIfAbsent(name, n -> operationResolver.findAllOperations(n).stream()
				.anyMatch(this::passesBack));
	}

	/** The mapping's result as it stands: the single result, a tuple of several, or null. */
	private Object currentResult(EList<VarParameter> resultParams) {
		if (resultParams.isEmpty()) {
			return null;
		}
		return resultParams.size() > 1 ? collectMultiResult(resultParams) : env.lookup(resultParams.get(0).getName());
	}

	/** An empty collection of the given type's kind, built the way OCL builds a literal. */
	private Object emptyCollection(CollectionType type) {
		return emptyCollection(type.getKind());
	}

	private Object emptyCollection(CollectionKind kind) {
		CollectionLiteralExp literal = OclFactory.eINSTANCE.createCollectionLiteralExp();
		literal.setKind(kind == CollectionKind.COLLECTION ? CollectionKind.SEQUENCE : kind);
		return eval(literal);
	}

	/**
	 * The model extent an object belongs to: the one holding its root container, or
	 * {@code null} for an object in none (#342).
	 */
	QvtoModelExtent extentOf(EObject object) {
		return extentManager == null ? null : extentManager.extentOf(object);
	}

	/**
	 * Invokes an inherited or merged mapping (§8.2.1.15): "Invocation of the inherited mappings
	 * follows the 'standard' invocation semantics, except that the out parameters may now start
	 * with a non-null value"; for merges "the parameters of the merging mapping are passed to the
	 * parameters of the merged mappings, including the actual value of the out parameters". The
	 * reused mapping runs in a scope of its own — its parameter and result names, its init,
	 * population and end — and hands its results back; it creates no trace record of its own.
	 * Before #333 only its population ran, in the caller's scope.
	 */
	private void invokeReused(MappingOperation reused, MappingOperation caller, Object self, Object[] args) {
		if (!typesConform(reused, self, args)) {
			return;
		}
		EList<VarParameter> callerResults = caller.getResult();
		EList<VarParameter> results = reused.getResult();
		// "including the actual value of the out parameters" — passed positionally (#336)
		Object[] passed = args.clone();
		exportOutArgs(caller, passed);
		// The caller's current results, read before the reused mapping's own names shadow them
		List<Object> current = new ArrayList<>();
		for (VarParameter callerResult : callerResults) {
			current.add(env.lookup(callerResult.getName()));
		}
		List<Object> passedBack = new ArrayList<>();
		env.pushScope();
		try {
			bindOperationParams(reused, self, passed, true);
			for (int i = 0; i < results.size(); i++) {
				Object value = i < current.size() ? current.get(i) : null;
				env.define(results.get(i).getName(), value);
				if (results.size() == 1) {
					env.define("result", value);
				}
			}
			// §8.1.15: a reused mapping is not invoked if its when-guard is not satisfied
			for (OclExpression whenExpr : reused.getWhen()) {
				Object whenVal = eval(whenExpr);
				if (!Boolean.TRUE.equals(whenVal)) {
					if (!Boolean.FALSE.equals(whenVal)) {
						undefinedCondition("when", whenVal,
								"mapping '" + reused.getName() + "' is not executed");
					}
					return;
				}
			}
			OperationBody body = reused.getBody();
			if (body instanceof MappingBody mappingBody) {
				mappingBody.getInitSection().forEach(this::eval);
				instantiateResults(results);
				instantiateOutParams(reused);
				mappingBody.getContent().forEach(this::eval);
				mappingBody.getEndSection().forEach(this::eval);
			} else if (body != null) {
				body.getContent().forEach(this::eval);
			}
			for (VarParameter result : results) {
				passedBack.add(env.lookup(result.getName()));
			}
			exportOutArgs(reused, passed);
		} finally {
			env.popScope();
		}
		for (int i = 0; i < passedBack.size() && i < callerResults.size(); i++) {
			env.assign(callerResults.get(i).getName(), passedBack.get(i));
		}
		importOutArgs(caller, passed);
	}

	/**
	 * Binds operation parameters (self, formals, results) in the current scope.
	 *
	 * @param outsGiven an inherited or merged mapping, whose out parameters start with the
	 *        caller's values; otherwise an out parameter starts null, or empty for a collection
	 *        (§8.2.1.15)
	 */
	private void bindOperationParams(ImperativeOperation operation, Object self, Object[] args, boolean outsGiven) {
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
				if (vp.getKind() == DirectionKind.OUT && !outsGiven) {
					value = vp.getEType() instanceof CollectionType collectionType ? emptyCollection(collectionType) : null;
				}
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
		// §8.1.18: "result … is a tuple when there are multiple result parameters" (#336)
		if (resultParams.size() > 1) {
			Map<String, Object> tuple = new LinkedHashMap<>();
			resultParams.forEach(rp -> tuple.put(rp.getName(), env.lookup(rp.getName())));
			env.define("result", tuple);
		}
	}

	private void addToExtent(EObject eObject, Variable extentRef) {
		addToExtent(eObject, extentRef != null ? extentRef.getName() : null);
	}

	/**
	 * Adds an EObject to the named extent, or — without a name — to the one inferred from its
	 * metamodel.
	 */
	private void addToExtent(EObject eObject, String extentName) {
		if (extentName != null) {
			QvtoModelExtent extent = extentManager.getExtent(extentName);
			if (extent != null) {
				// §8.3.19: topclasses constraint — check if root type is allowed
				if (tagRegistry.hasTopclassConstraints()
						&& !tagRegistry.isTopclassAllowed(extentName, eObject.eClass())) {
					addInfo("topclasses: " + eObject.eClass().getName()
							+ " is not allowed as root in extent '" + extentName + "'");
					return;
				}
				extent.add(eObject);
				return;
			}
		}
		if (transformation.getIntermediateClass().contains(eObject.eClass())) {
			// §8.1.10: intermediate data does not exist outside the transformation — it goes to
			// no output model unless an extent is named (#337)
			extentManager.intermediateData().add(eObject);
			return;
		}
		QvtoModelExtent extent = extentManager.getExtentForClassifier(eObject.eClass());
		if (extent != null && extentManager.isAmbiguous(eObject.eClass()) && ambiguousClasses.add(eObject.eClass())) {
			// §8.1.3.4: "If no extent can be inferred, typically because two ModelParameters use
			// the same ModelType … a programming hazard that can be diagnosed by tooling". The
			// first such parameter takes it, as in Eclipse (#337)
			addWarning("The extent of " + eObject.eClass().getName() + " is ambiguous: it goes to '"
					+ extentManager.getParameterName(extent) + "'; name the extent with @ to choose");
		}
		if (extent == null) {
			extent = extentManager.getDefaultOutputExtent();
		}
		if (extent != null) {
			// §8.3.19: topclasses constraint — check if root type is allowed
			if (tagRegistry.hasTopclassConstraints()) {
				String paramName = extentManager.getParameterName(extent);
				if (paramName != null
						&& !tagRegistry.isTopclassAllowed(paramName, eObject.eClass())) {
					addInfo("topclasses: " + eObject.eClass().getName()
							+ " is not allowed as root in extent '" + paramName + "'");
					return;
				}
			}
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
	 * Returns what this evaluation may ask of the engine that started it.
	 */
	QvtoEngineServices getEngine() {
		return engine;
	}

	/**
	 * Returns the operation resolver for use by {@link QvtoOperationProvider}.
	 */
	public QvtoOperationResolver getOperationResolver() {
		return operationResolver;
	}

	/**
	 * Returns the collected diagnostics list (for blackbox invocation context).
	 */
	List<Diagnostic> diagnostics() {
		return diagnostics;
	}

	/**
	 * Returns the extent manager (for blackbox invocation context).
	 */
	QvtoExtentManager extentManager() {
		return extentManager;
	}

	/**
	 * Returns the config properties (for blackbox invocation context).
	 */
	Map<String, Object> configProperties() {
		return configProps;
	}

	/**
	 * The QVT-O operations of this transformation, as an OCL provider (D29).
	 *
	 * <p>These are passed through to every OCL sub-evaluation via
	 * {@link OclEvaluationOptions#additionalProviders()}. Built here rather than handed in:
	 * the provider evaluates helper bodies through this evaluator, so it cannot exist before
	 * the evaluator does.
	 */
	private List<OclOperationProvider> additionalProviders() {
		if (additionalProviders == null) {
			additionalProviders = List.of(new QvtoOperationProvider(
					transformation, this, operationResolver, blackboxRegistry));
		}
		return additionalProviders;
	}

	/**
	 * The options every OCL sub-expression is evaluated with.
	 *
	 * <p>The base is what the run asked for in {@link QvtoEvaluationOptions#oclOptions()}, else
	 * the defaults of the OCL engine — what {@code OclConfiguration} or the {@code ocl.*}
	 * configuration set. Until #258 the base was {@code OclEvaluationOptions.lenient()} built
	 * here, so neither reached the OCL half of a transformation. On top: LENIENT null handling,
	 * because module-level operations have no {@code self}, and the QVT-O bridge providers
	 * (D29), so the transformation's own helpers and blackboxes are always in reach.
	 */
	private OclEvaluationOptions oclOptions() {
		if (oclOptions == null) {
			OclEvaluationOptions base = options.oclOptions() != null
					? options.oclOptions()
					: oclEngine.getDefaultOptions();
			oclOptions = base
					.withNullHandling(OclEvaluationOptions.NullHandling.LENIENT)
					.withAdditionalProviders(additionalProviders());
		}
		return oclOptions;
	}



	/**
	 * Returns the default value for a type when no initializer is provided.
	 * §8.2.2.10: "zero for a numeric type, the empty string for a string,
	 * and null for all other elements"
	 */
	private Object defaultValueForType(EClassifier type) {
		if (type == null) {
			return null;
		}
		// §8.2.2.10: "an empty collection for a collection" (#339)
		if (type instanceof CollectionType collectionType) {
			return newCollection(collectionKind(collectionType, null), List.of());
		}
		if (type instanceof MapType) {
			return new LinkedHashMap<>();
		}
		String typeName = type.getName();
		if (typeName == null) {
			return null;
		}
		// "zero for a numeric type, the empty string for a string, and null for all other
		// elements" — Boolean included; Eclipse answers false (#339)
		return switch (typeName) {
			case "Integer" -> 0;
			case "Real" -> 0.0;
			case "String" -> "";
			default -> null;
		};
	}

	/**
	 * The source of an arrow construct — {@code forEach}, {@code forOne} and the imperative
	 * iterate variants — as a collection: a single value as if it were {@code oclAsSet()}, null
	 * as the empty set (OCL v2.4 §9.3.35 [B], as #171 did for operations) (#341).
	 *
	 * @return the collection, or invalid
	 */
	private Object arrowSource(Object source) {
		if (source instanceof Collection<?> || source == OclInvalid.INSTANCE) {
			return source;
		}
		Object value = unwrapNull(source);
		return newCollection(CollectionKind.SET, value == null ? List.of() : List.of(value));
	}

	/**
	 * {@code list->foo(args)} as {@code list->xcollect(i | i.foo(args))} (§8.2.2.7: "these defined
	 * shorthand conventions used to collect property values equally apply to operations"): the
	 * contextual helper or query of each element's type called on it, the results flattened
	 * without nulls into a Bag or a Sequence. The operation provider calls this when neither the
	 * standard library nor an operation of the collection itself answers — a predefined
	 * collection operation wins (#341).
	 *
	 * @return the collected results, or invalid if the source is no collection
	 */
	Object collectElementOperation(String name, Object source, Object[] args) {
		if (!(source instanceof Collection<?> collection)) {
			addError("Unknown operation: " + name + " on "
					+ (source == null ? "null" : source.getClass().getSimpleName()));
			return OclInvalid.INSTANCE;
		}
		List<ImperativeOperation> candidates = new ArrayList<>();
		for (ImperativeOperation op : operationResolver.findAllOperations(name)) {
			if (op.getContext() != null && !(op.getContext().getEType() instanceof CollectionType)
					&& parameterCount(op) == args.length) {
				candidates.add(op);
			}
		}
		List<Object> results = new ArrayList<>();
		for (Object element : collection) {
			List<ImperativeOperation> applicable = QvtoOperationResolver.filterAndSortByType(candidates, element);
			if (applicable.isEmpty() || element == null) {
				continue;
			}
			ImperativeOperation op = applicable.get(0);
			Object result = op instanceof MappingOperation
					? callMappingByName(name, element, args.clone(), false)
					: callOperation(op, element, args.clone());
			for (Object value : flatten(result)) {
				if (value != null) {
					results.add(value);
				}
			}
		}
		CollectionKind kind = kindOf(collection);
		return newCollection(kind == CollectionKind.SET || kind == CollectionKind.BAG ? CollectionKind.BAG
				: CollectionKind.SEQUENCE, results);
	}

	/**
	 * The module property a variable reference stands for: the declaration itself, or the
	 * untyped stand-in the parser builds for {@code this.name}; {@code null} for a variable.
	 */
	private Variable moduleProperty(Variable referred) {
		for (Variable moduleVar : transformation.getOwnedVariable()) {
			if (moduleVar == referred || (referred.eContainer() == null && referred.getType() == null
					&& moduleVar.getName() != null && moduleVar.getName().equals(referred.getName()))) {
				return moduleVar;
			}
		}
		return null;
	}

	/**
	 * The value a variable, tuple part or intermediate property holds after an assignment, by
	 * the equivalent assignments of §8.2.2.11 (#339). A multi-valued target — declared as a
	 * collection, or holding one — gets a new collection of its own kind: the right-hand side is
	 * flattened into it, a null element replaced by the default value. A variable keeps the
	 * remaining nulls, a property drops them. The collection the target held is not changed, so
	 * a variable that shares it keeps its elements. Before, {@code +=} added the right-hand
	 * collection as one element and mutated the shared value, and {@code := null} cleared it.
	 *
	 * @param property whether the target is a property
	 * @return the new value, or {@link #UNHANDLED} if the target keeps its value
	 */
	private Object assignedValue(String target, EClassifier declared, Object current, Object value, AssignExp exp,
			boolean property) {
		return assignedValue(target, declared, declared instanceof CollectionType, current, value, exp, property);
	}

	/**
	 * @param declaredMany whether the target is declared multi-valued — a collection type, or a
	 *        feature with an upper bound above one
	 */
	private Object assignedValue(String target, EClassifier declared, boolean declaredMany, Object current,
			Object value, AssignExp exp, boolean property) {
		if (exp.isIsSubtract()) {
			if (!(current instanceof Collection<?> currentCollection)) {
				return UNHANDLED;
			}
			Collection<Object> remaining = newCollection(collectionKind(declared, currentCollection), currentCollection);
			flatten(value).forEach(remaining::remove);
			return remaining;
		}
		if (exp.isIsReset()) {
			// A collection is assigned element by element only to a target declared as one. A
			// null or invalid right-hand side leaves a variable null (not LEFT{null}) and a property
			// empty, as before
			if (!declaredMany || value == OclInvalid.INSTANCE) {
				return value;
			}
			if (value == null) {
				return property ? newCollection(collectionKind(declared, null), List.of()) : null;
			}
			return appended(target, newCollection(collectionKind(declared, null), List.of()), value, exp, property);
		}
		// Eclipse bug449445: invalid is silently ignored on +=
		if (value == OclInvalid.INSTANCE) {
			return UNHANDLED;
		}
		if (current instanceof Collection<?> currentCollection) {
			return appended(target, newCollection(collectionKind(declared, currentCollection), currentCollection),
					value, exp, property);
		}
		if (declaredMany) {
			return appended(target, newCollection(collectionKind(declared, null), List.of()), value, exp, property);
		}
		if (current == null) {
			// An untyped variable that holds nothing yet takes the value
			return value;
		}
		// "Equivalent Single valued Property or Variable append: invalid – append is not
		// possible for single values"
		addError("Cannot append to '" + target + "': append is not possible for single values");
		return UNHANDLED;
	}

	/** Appends the flattened value to the new collection, within the collection size limit. */
	private Object appended(String target, Collection<Object> acc, Object value, AssignExp exp, boolean property) {
		Object defaultValue = UNHANDLED;
		List<Object> elements = flatten(value);
		// The size limit of #257 sees only standard-library results: `t.items += t.items` in a
		// loop doubled on every step (#260)
		int limit = oclOptions().maxCollectionSize();
		if ((long) acc.size() + elements.size() > limit) {
			addError("Collection size " + ((long) acc.size() + elements.size())
					+ " exceeds maximum allowed size: " + limit + " — '" + target + "' is not changed");
			return UNHANDLED;
		}
		for (Object element : elements) {
			Object v = element;
			if (v == null && exp.getDefaultValue() != null) {
				if (defaultValue == UNHANDLED) {
					defaultValue = eval(exp.getDefaultValue());
				}
				v = defaultValue;
			}
			if (v != null || !property) {
				acc.add(v);
			}
		}
		return acc;
	}

	/** {@code value->flatten()} of a single value or a collection, nested collections included. */
	private static List<Object> flatten(Object value) {
		List<Object> elements = new ArrayList<>();
		if (value instanceof Collection<?> collection) {
			for (Object element : collection) {
				elements.addAll(flatten(element));
			}
		} else {
			elements.add(value);
		}
		return elements;
	}

	/** The kind of collection a target holds: its declared kind, else that of its current value. */
	private CollectionKind collectionKind(EClassifier declared, Collection<?> current) {
		if (declared instanceof CollectionType collectionType && collectionType.getKind() != CollectionKind.COLLECTION) {
			return collectionType.getKind();
		}
		return current != null ? kindOf(current) : CollectionKind.SEQUENCE;
	}

	/** Whether a collection is a mutable List — a Sequence, List being its alias (D26). */
	boolean isList(Collection<?> collection) {
		return kindOf(collection) == CollectionKind.SEQUENCE && !(collection instanceof Set<?>);
	}

	/** The kind of a collection value, by the class the OCL engine builds that kind with. */
	CollectionKind kindOf(Collection<?> collection) {
		collectionClasses();
		for (Map.Entry<CollectionKind, Class<?>> entry : collectionClasses.entrySet()) {
			if (entry.getValue() == collection.getClass()) {
				return entry.getKey();
			}
		}
		return collection instanceof Set<?> ? CollectionKind.SET : CollectionKind.SEQUENCE;
	}

	/**
	 * A new collection of the kind, holding the elements. The OCL engine's collection classes
	 * are internal to it: each is learnt once from an empty literal of its kind, and its
	 * {@code (Collection)} constructor builds the copies — a literal evaluated per assignment
	 * would cost a full OCL evaluation every time.
	 */
	@SuppressWarnings("unchecked")
	Collection<Object> newCollection(CollectionKind kind, Collection<?> elements) {
		Class<?> type = collectionClasses().get(kind == CollectionKind.COLLECTION ? CollectionKind.SEQUENCE : kind);
		try {
			return (Collection<Object>) type.getConstructor(Collection.class).newInstance(elements);
		} catch (ReflectiveOperationException e) {
			Collection<Object> fallback = (Collection<Object>) emptyCollection(kind);
			fallback.addAll(elements);
			return fallback;
		}
	}

	private Map<CollectionKind, Class<?>> collectionClasses() {
		if (collectionClasses == null) {
			Map<CollectionKind, Class<?>> classes = new LinkedHashMap<>();
			for (CollectionKind kind : List.of(CollectionKind.SEQUENCE, CollectionKind.ORDERED_SET,
					CollectionKind.SET, CollectionKind.BAG)) {
				classes.put(kind, emptyCollection(kind).getClass());
			}
			collectionClasses = classes;
		}
		return collectionClasses;
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
		if (checkTimeout()) {
			--stackDepth;
			addError("Execution timeout exceeded");
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

	/**
	 * Takes over a diagnostic an OCL expression reported, keeping its severity and adding the place
	 * the expression stands — {@code unit:line:column} when the node is one this engine parsed.
	 */
	private void addOclDiagnostic(Diagnostic diagnostic) {
		SourcePosition position = null;
		if (diagnostic.getData() != null) {
			for (Object entry : diagnostic.getData()) {
				if (entry instanceof EObject node) {
					position = engine.positionOf(node);
					if (position != null) {
						break;
					}
				}
			}
		}
		String message = position == null
				? diagnostic.getMessage()
				: position + " " + diagnostic.getMessage();
		addDiagnostic(diagnostic.getSeverity(), message);
	}

	/**
	 * Records a mapping invocation in the trace, and reports the first record the trace limit
	 * drops (#284).
	 *
	 * <p>Past {@code maxTraceRecords} the trace stops growing, so a resolve operation, or the
	 * reuse of a mapping already applied (§8.1.11.1), may miss what was mapped afterwards and
	 * answer from an incomplete trace. Before #284 that happened in silence and the run reported
	 * success; now it is an error, like every other limit, reported once.
	 */
	private void recordTrace(MappingOperation mappingOp, Object self, Object[] args, Object[] outArgs, Object result) {
		boolean reachedBefore = traceManager.isLimitReached();
		traceManager.addRecord(mappingOp, self, args, outArgs, result);
		if (!reachedBefore && traceManager.isLimitReached()) {
			addError("Maximum trace records exceeded: " + options.maxTraceRecords()
					+ " — resolve operations and mapping reuse may miss what was mapped after it");
		}
	}

	private void addError(String message) {
		addDiagnostic(Diagnostic.ERROR, message);
	}

	private void addWarning(String message) {
		addDiagnostic(Diagnostic.WARNING, message);
	}

	private void addInfo(String message) {
		addDiagnostic(Diagnostic.INFO, message);
	}

	/**
	 * Adds a diagnostic entry, respecting {@code maxDiagnostics} limit (M-4b / Q-7).
	 */
	private void addDiagnostic(int severity, String message) {
		if (diagnostics.size() >= options.maxDiagnostics()) {
			if (diagnostics.size() == options.maxDiagnostics()) {
				diagnostics.add(new BasicDiagnostic(Diagnostic.WARNING, SOURCE_ID, 0,
						"Maximum diagnostics limit reached (" + options.maxDiagnostics()
								+ "), further diagnostics truncated", null));
			}
			return;
		}
		diagnostics.add(new BasicDiagnostic(severity, SOURCE_ID, 0, message, null));
	}

	/**
	 * Checks the timeout deadline (M-3 / Q-5). Called at every stack-depth
	 * check point and in loop iterations for timely termination.
	 *
	 * @return {@code true} if the deadline has been exceeded
	 */
	private boolean checkTimeout() {
		return deadlineNanos != 0 && System.nanoTime() > deadlineNanos;
	}

	/**
	 * Performs the deferred assignments once the entry operation has finished (§8.2.1.22), in
	 * the order they were reached. Each right-hand side is evaluated now, against the full
	 * trace, with the variables and late-resolve sources captured when it was reached.
	 */
	private void processDeferredTasks() {
		if (deferredAssignments.isEmpty()) {
			return;
		}
		isDeferredExecution = true;
		try {
			for (DeferredAssignment deferred : deferredAssignments) {
				env.pushScope();
				deferredResolveSources = deferred.resolveSources();
				try {
					deferred.variables().forEach(env::define);
					Object value = evalAssignedValue(deferred.assignment());
					assignToProperty(deferred.target(), deferred.feature(), value,
							deferred.assignment());
				} finally {
					deferredResolveSources = Map.of();
					env.popScope();
				}
			}
		} finally {
			isDeferredExecution = false;
			deferredAssignments.clear();
		}
	}

	/**
	 * §8.2.2.11: whether the assignment is a deferred one — its right-hand side involves a
	 * late resolve.
	 */
	private static boolean involvesLateResolve(AssignExp exp) {
		List<OclExpression> parts = new ArrayList<>(exp.getValue());
		if (exp.getDefaultValue() != null) {
			parts.add(exp.getDefaultValue());
		}
		for (OclExpression part : parts) {
			if (part instanceof ResolveExp r && r.isIsDeferred()) {
				return true;
			}
			for (var iter = part.eAllContents(); iter.hasNext(); ) {
				if (iter.next() instanceof ResolveExp r && r.isIsDeferred()) {
					return true;
				}
			}
		}
		return false;
	}

	/**
	 * §8.2.1.22: the source object of each late resolve in the assignment, evaluated where the
	 * assignment is reached — except a source that uses a variable the right-hand side itself
	 * declares (an iterator, say), which can only be evaluated with the right-hand side.
	 */
	private Map<ResolveExp, Object> captureLateResolveSources(AssignExp exp) {
		Map<ResolveExp, Object> sources = new IdentityHashMap<>();
		List<OclExpression> parts = new ArrayList<>(exp.getValue());
		if (exp.getDefaultValue() != null) {
			parts.add(exp.getDefaultValue());
		}
		for (OclExpression part : parts) {
			List<EObject> nodes = new ArrayList<>();
			nodes.add(part);
			part.eAllContents().forEachRemaining(nodes::add);
			for (EObject node : nodes) {
				if (node instanceof ResolveExp r && r.isIsDeferred() && r.getOwnedSource() != null
						&& !usesVariableDeclaredIn(r.getOwnedSource(), exp)) {
					sources.put(r, eval(r.getOwnedSource()));
				}
			}
		}
		return sources;
	}

	private static boolean usesVariableDeclaredIn(OclExpression expr, AssignExp assignment) {
		List<EObject> nodes = new ArrayList<>();
		nodes.add(expr);
		expr.eAllContents().forEachRemaining(nodes::add);
		for (EObject node : nodes) {
			if (node instanceof VariableExp ref && ref.getReferredVariable() != null
					&& EcoreUtil.isAncestor(assignment, ref.getReferredVariable())) {
				return true;
			}
		}
		return false;
	}

	/**
	 * §8.2.2.11: the right-hand side of an assignment, with the default value standing in for null.
	 */
	private Object evalAssignedValue(AssignExp exp) {
		List<OclExpression> values = exp.getValue();
		if (values.size() > 1) {
			// §8.2.2.11: "A complex assignment is equivalent to a simple assignment comprising a
			// Sequence of the compound expression values" (#339)
			List<Object> sequence = new ArrayList<>(values.size());
			for (OclExpression valueExp : values) {
				sequence.add(unwrapNull(eval(valueExp)));
			}
			return sequence;
		}
		Object value = values.isEmpty() ? null : eval(values.get(0));
		if (value == null && exp.getDefaultValue() != null) {
			value = eval(exp.getDefaultValue());
		}
		return value;
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
		// A part assigned through "result.a := …" is in the tuple bound to result (#336)
		Map<?, ?> bound = env.contains("result") && env.lookup("result") instanceof Map<?, ?> map ? map : Map.of();
		for (VarParameter rp : resultParams) {
			Object value = env.lookup(rp.getName());
			tuple.put(rp.getName(), value != null ? value : bound.get(rp.getName()));
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
		// Single scope walk for all 3 candidates: _objectExp > result > self (§8.2.1.17)
		return env.lookupImplicitTarget(featureName, this::resolveAlias);
	}

	/**
	 * §8.3.19: Resolves a tag "alias" name to the real feature name for the given EObject.
	 * @return the real feature name, or null if not an alias
	 */
	String resolveAlias(String name, EObject target) {
		return aliasRegistry.resolveAlias(name, target);
	}


	/**
	 * §8.1.3.2: Guards property assignment on EObjects belonging to read-only (in) extents.
	 */
	private void checkNotReadOnly(EObject target) {
		if (extentManager.isReadOnly(target)) {
			throw new RaiseException("InvalidModelMutation",
					"Cannot modify object in read-only model extent (in-parameter)");
		}
	}

	/**
	 * Assigns to a property of a model object: an intermediate property (§8.1.10), a static
	 * one of an intermediate class (§8.4), or the object's own.
	 */
	private void assignToProperty(EObject eo, EStructuralFeature sf, Object value, AssignExp exp) {
		// §8.1.10: Check intermediate property first — intermediate properties
		// are stored externally, so they ARE allowed on in-model objects.
		ContextualProperty icp = intermediateStore.findIntermediateProperty(eo, sf.getName());
		if (icp != null) {
			// A property: a multi-valued one skips null elements (§8.2.2.11, #339)
			Object current = intermediateStore.getIntermediatePropertyValue(eo, sf.getName());
			Object assigned = assignedValue(sf.getName(), icp.getEType(),
					icp.isMany() || icp.getEType() instanceof CollectionType, current, value, exp, true);
			if (assigned != UNHANDLED) {
				intermediateStore.setIntermediatePropertyValue(eo, sf.getName(), assigned);
			}
			return;
		}
		// §8.1.3.2: Guard non-intermediate property assignment on in-model objects
		checkNotReadOnly(eo);
		// Resolve the feature from the actual EClass to handle
		// cross-package identity (dynamic Ecore models)
		EStructuralFeature actualSf = eo.eClass().getEStructuralFeature(sf.getName());
		if (actualSf == null) {
			actualSf = sf;
		}
		// §8.4: Static intermediate class feature → shared store
		if (QvtoIntermediatePropertyStore.isStatic(actualSf)) {
			intermediateStore.setStaticValue(actualSf, value);
			return;
		}
		assignToModelFeature(eo, actualSf, value, exp);
	}

	/**
	 * §8.2.2.11: assigns to, appends to or removes from a feature of a model object.
	 *
	 * <p>A many-valued feature takes the flattened values, and "any residual null values are
	 * omitted". What EMF still refuses — a value of the wrong type, say — is an error of the
	 * transformation, reported as such rather than thrown out of {@code execute} (#297).
	 */
	@SuppressWarnings("unchecked")
	private void assignToModelFeature(EObject eo, EStructuralFeature sf, Object value,
			AssignExp exp) {
		if (sf == null) {
			addError("Unknown property in assignment on " + eo.eClass().getName());
			return;
		}
		// Eclipse bug449445: invalid → null for property assignment, ignored on +=
		Object assigned = value == OclInvalid.INSTANCE ? null : value;
		try {
			if (exp.isIsSubtract()) {
				if (eo.eGet(sf) instanceof Collection<?> current) {
					((Collection<Object>) current).remove(coerceForFeature(sf, assigned));
				}
			} else if (sf.isMany()) {
				List<Object> items = new ArrayList<>();
				collectFeatureValues(sf, assigned, items);
				if (exp.isIsReset()) {
					// §8.2.1: collection assignment uses value-copy semantics
					eo.eSet(sf, items);
				} else {
					((Collection<Object>) eo.eGet(sf)).addAll(items);
				}
			} else if (exp.isIsReset()) {
				// A single-valued feature may hold a collection itself — an intermediate class's
				// Sequence(String) — and gets a copy (§8.2.1: value-copy semantics)
				eo.eSet(sf, assigned instanceof Collection<?> values
						? new ArrayList<>(values)
						: coerceForFeature(sf, assigned));
			} else if (eo.eGet(sf) instanceof Collection<?> current) {
				// += onto the collection such a feature holds
				List<Object> items = new ArrayList<>();
				collectFeatureValues(sf, assigned, items);
				((Collection<Object>) current).addAll(items);
			} else {
				eo.eSet(sf, coerceForFeature(sf, assigned));
			}
		} catch (IllegalArgumentException | ClassCastException | ArrayStoreException e) {
			addError("Cannot assign to " + eo.eClass().getName() + "::" + sf.getName() + ": "
					+ e.getMessage());
		}
	}

	private static void collectFeatureValues(EStructuralFeature sf, Object value, List<Object> into) {
		if (value instanceof Collection<?> values) {
			for (Object item : values) {
				collectFeatureValues(sf, item, into);
			}
		} else if (value != null && value != OclInvalid.INSTANCE) {
			into.add(coerceForFeature(sf, value));
		}
	}

	/**
	 * Coerces a value to match the expected EMF structural feature type.
	 * OCL evaluates an Integer as Long (BigInteger beyond 64 bits) and a Real as BigDecimal
	 * (#322); the feature wants its own instance class — Integer for EInt, Double for EDouble.
	 */
	private static Object coerceForFeature(EStructuralFeature sf, Object value) {
		if (value instanceof Number && sf instanceof EAttribute attr) {
			return OclValues.toEcore(value, attr.getEAttributeType());
		}
		return value;
	}

	// =========================================================================
	// Inner Switch: ImperativeOCL expressions
	// =========================================================================

	private class ImperativeDispatch extends ImperativeOclSwitch<Object> {

		@Override
		public Object caseBlockExp(BlockExp exp) {
			for (OclExpression bodyExp : exp.getBody()) {
				eval(bodyExp);
			}
			// §8.2.2.2: "The returned value is null" — as in Eclipse (#339)
			return WRAPPED_NULL;
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

			// §8.2.2.11: an assignment involving a late resolve is deferred as a whole — wherever
			// it stands, in a block or an if as much as at the top of a mapping body (#297)
			if (!isDeferredExecution && involvesLateResolve(exp) && deferAssignment(exp)) {
				// "This assignment is not executed at the time the assignment is reached ...
				// A null value is returned instead." (§8.2.1.22)
				return WRAPPED_NULL;
			}

			Object value = evalAssignedValue(exp);

			if (left instanceof VariableExp varExp) {
				String varName = varExp.getReferredVariable().getName();
				// §8.2.1.17: Implicit property assignment — inside object expressions
				// (self = created object) and mapping bodies with single result
				// (result = created object), unqualified names resolve as properties
				// of the context object.
				if (!env.contains(varName)) {
					EObject target = resolveImplicitPropertyTarget(varName);
					if (target != null) {
						checkNotReadOnly(target);
						assignToModelFeature(target, implicitFeature(target, varName), value, exp);
						return wrapNull(value);
					}
				}
				// §8.1.3.3: "A model assignment displaces any previous members associated with the
				// extent in favor of the root model elements of the replacement Model" — the
				// variable keeps its extent, whose contents change (#337)
				if (exp.isIsReset() && env.lookup(varName) instanceof QvtoModelExtent target
						&& value instanceof QvtoModelExtent replacement) {
					if (target != replacement) {
						if (target.isReadOnly()) {
							addError("Cannot assign to the model '" + varName + "': it is an in model");
						} else {
							target.setContents(new ArrayList<>(replacement.getContents()));
						}
					}
					return wrapNull(value);
				}
				// Variable assignment (§8.2.2.11): the variable gets a new value, a collection it
				// shared with another variable is not changed (#339)
				// A module property (§8.1.18, also reached as this.name) is assigned as a property
				Variable moduleProperty = moduleProperty(varExp.getReferredVariable());
				Object assigned = moduleProperty != null
						? assignedValue(varName, moduleProperty.getType(), env.lookup(varName), value, exp, true)
						: assignedValue(varName, varExp.getReferredVariable().getType(), env.lookup(varName), value,
								exp, false);
				if (assigned != UNHANDLED) {
					env.assign(varName, assigned);
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
					// A tuple part is assigned like a variable (§8.2.2.11, #339)
					Object assigned = assignedValue(partName, sf.getEType(), tuple.get(partName), value, exp, false);
					if (assigned != UNHANDLED) {
						tuple.put(partName, assigned);
					}
				} else if (source instanceof EObject eo && sf != null) {
					assignToProperty(eo, sf, value, exp);
				} else if (source == null || source == OclInvalid.INSTANCE) {
					// §8.2.2.11: there is no object to assign to — the assignment cannot be
					// performed, and it is not dropped without a word (#308)
					addError("Cannot assign to property '" + (sf != null ? sf.getName() : "?")
							+ "': the target is " + (source == null ? "null" : "invalid"));
				}
			}
			return wrapNull(value);
		}

		/**
		 * §8.2.2.11: records a deferred assignment — the property it assigns and the sources
		 * of its late resolves, as §8.2.1.22 lists them — to be performed at the end.
		 *
		 * @return whether it was deferred; an assignment to a variable is not, as the
		 *         variable is gone by the time the late resolve can run
		 */
		private boolean deferAssignment(AssignExp exp) {
			OclExpression left = exp.getLeft();
			EObject target = null;
			EStructuralFeature feature = null;
			if (left instanceof PropertyCallExp propLeft
					&& eval(propLeft.getOwnedSource()) instanceof EObject eo
					&& propLeft.getReferredProperty() != null) {
				target = eo;
				feature = propLeft.getReferredProperty();
			} else if (left instanceof VariableExp varExp
					&& !env.contains(varExp.getReferredVariable().getName())) {
				String name = varExp.getReferredVariable().getName();
				target = resolveImplicitPropertyTarget(name);
				feature = target != null ? implicitFeature(target, name) : null;
			}
			if (target == null || feature == null) {
				return false;
			}
			deferredAssignments.add(new DeferredAssignment(exp, target, feature,
					env.allVisibleVariables(), captureLateResolveSources(exp)));
			return true;
		}

		/** §8.3.19: the feature an unqualified name stands for, an alias resolved. */
		private EStructuralFeature implicitFeature(EObject target, String name) {
			String realName = resolveAlias(name, target);
			return target.eClass().getEStructuralFeature(realName != null ? realName : name);
		}

		@Override
		public Object caseWhileExp(WhileExp exp) {
			// §8.2.2.4: WhileExp returns null
			int iterations = 0;
			while (true) {
				if (++iterations > options.maxLoopIterations()) {
					addError("Maximum loop iterations exceeded: " + options.maxLoopIterations());
					break;
				}
				if (checkTimeout()) {
					addError("Execution timeout exceeded");
					break;
				}
				Object condition = eval(exp.getCondition());
				if (!Boolean.TRUE.equals(condition)) {
					// §8.2.2.4: iterates "until a condition becomes false" — an undefined one
					// ends the loop too, but not without a word (#310)
					if (!Boolean.FALSE.equals(condition)) {
						undefinedCondition("while", condition, "the loop ends");
					}
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
			Object source = arrowSource(eval(exp.getOwnedSource()));
			if (!(source instanceof Collection<?> coll)) {
				addError("for source must be a Collection, got: invalid");
				return WRAPPED_NULL;
			}

			// §8.2.2.6: non-ordered collections are implicitly converted to ordered
			// (Set → OrderedSet/LinkedHashSet, Bag → Sequence/ArrayList)
			Iterable<?> iterable = coll;
			if (coll instanceof Set<?> && !(coll instanceof SequencedSet<?>)) {
				iterable = new LinkedHashSet<>(coll);
			}

			List<Variable> iterVars = exp.getOwnedIterators();
			String iterName = iterVars.isEmpty() ? "_it" : iterVars.get(0).getName();
			OclExpression condition = exp.getCondition();
			OclExpression body = exp.getOwnedBody();
			boolean isForOne = "forOne".equals(exp.getName());

			int iterations = 0;
			for (Object element : iterable) {
				if (++iterations > options.maxLoopIterations()) {
					addError("Maximum loop iterations exceeded: " + options.maxLoopIterations());
					break;
				}
				if (checkTimeout()) {
					addError("Execution timeout exceeded");
					break;
				}
				env.pushScope();
				try {
					env.define(iterName, element);
					// Optional condition filter
					if (condition != null) {
						Object condVal = eval(condition);
						if (!Boolean.TRUE.equals(condVal)) {
							// §8.2.2.6 defines the condition as "if (condition) body" per element:
							// the if of #308, so this element is skipped with an error (#310)
							if (!Boolean.FALSE.equals(condVal)) {
								undefinedCondition(exp.getName(), condVal, "the element is skipped");
							}
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

		/**
		 * QVT v1.3 §8.2.2.7 — the five imperative iterate variants.
		 *
		 * <p>{@code xcollect}, {@code xselect}, {@code xselectOne}, {@code xcollectselect} and
		 * {@code xcollectselectOne}, told apart by {@code name}. The spec defines each of them
		 * through {@code forEach}, which is why they are bounded by {@code maxLoopIterations}
		 * like every other loop of this evaluator, and why {@code break}, {@code continue},
		 * {@code raise} and {@code return} work inside them where they do not in an OCL
		 * iterator.
		 *
		 * <p>Two properties the OCL counterparts do not have: a {@code null} is never collected
		 * ("if (SELECTOR &lt;&gt; null …)" in every one of the five definitions), and the
		 * collecting variants flatten what the body produced.
		 */
		@Override
		public Object caseImperativeIterateExp(ImperativeIterateExp exp) {
			Object source = arrowSource(eval(exp.getOwnedSource()));
			if (!(source instanceof Collection<?> coll)) {
				addError("imperativeIterate source must be a Collection, got: invalid");
				return WRAPPED_NULL;
			}
			if (coll.size() > options.maxLoopIterations()) {
				addError("Maximum loop iterations exceeded: " + options.maxLoopIterations());
				return WRAPPED_NULL;
			}

			String variant = exp.getName() == null ? "xcollect" : exp.getName();
			List<Variable> iterVars = exp.getOwnedIterators();
			String iterName = iterVars.isEmpty() ? "_it" : iterVars.get(0).getName();
			Variable target = exp.getTarget();
			OclExpression condition = exp.getCondition();
			OclExpression body = exp.getOwnedBody();
			boolean collecting = variant.startsWith("xcollect");
			boolean firstOnly = variant.endsWith("One");

			List<Object> collected = new ArrayList<>();
			for (Object element : coll) {
				if (checkTimeout()) {
					addError("Execution timeout exceeded");
					break;
				}
				env.pushScope();
				try {
					env.define(iterName, element);
					// The candidates of this iteration: what the body produced, flattened, for a
					// collecting variant — the element itself for a selecting one
					List<Object> candidates = new ArrayList<>();
					if (collecting) {
						flatten(eval(body), candidates);
					} else {
						candidates.add(element);
					}
					boolean done = false;
					for (Object candidate : candidates) {
						if (candidate == null || candidate == WRAPPED_NULL) {
							continue; // "if (SELECTOR <> null …)" — never collected
						}
						if (condition != null && !matchesCondition(condition, target, iterName,
								candidate)) {
							continue;
						}
						collected.add(candidate);
						if (firstOnly) {
							done = true;
							break;
						}
					}
					if (done) {
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

			if (firstOnly) {
				return wrapNull(collected.isEmpty() ? null : collected.get(0));
			}
			// §8.2.2.7: xselect answers "BagOrOrderedSetOrSequenceOrSet(T)" — the kind of its
			// source; the collecting variants "BagOrSequence(TT)" — a Bag for an unordered
			// source, a Sequence for an ordered one, as OCL's collect does (#341)
			CollectionKind sourceKind = kindOf(coll);
			CollectionKind resultKind = !collecting ? sourceKind
					: sourceKind == CollectionKind.SET || sourceKind == CollectionKind.BAG ? CollectionKind.BAG
					: CollectionKind.SEQUENCE;
			return newCollection(resultKind, collected);
		}

		/**
		 * Evaluates the condition for one candidate, with the selector variable bound.
		 *
		 * <p>The selector is the target variable where the construct names one
		 * ({@code list->xcollectselect(i; res = i.prop | not res.startsWith('_'))}) and the
		 * iterator otherwise — which is what makes {@code list[i | i.name = 'x']} read the way
		 * it does.
		 */
		private boolean matchesCondition(OclExpression condition, Variable target, String iterName,
				Object candidate) {
			env.pushScope();
			try {
				env.define(target != null ? target.getName() : iterName, candidate);
				// §8.2.2.7 defines the variants with "if (SELECTOR <> null and CONDITION)": the
				// if of #308, so an undefined condition skips the element with an error (#310)
				Object condVal = eval(condition);
				if (!(condVal instanceof Boolean)) {
					undefinedCondition("xselect", condVal, "the element is skipped");
				}
				return Boolean.TRUE.equals(condVal);
			} finally {
				env.popScope();
			}
		}

		/** Appends a value, or the members of a collection value, to the result. */
		private void flatten(Object value, List<Object> into) {
			if (value instanceof Collection<?> nested) {
				for (Object element : nested) {
					flatten(element, into);
				}
				return;
			}
			into.add(value == WRAPPED_NULL ? null : value);
		}

		@Override
		public Object caseSwitchExp(SwitchExp exp) {
			for (AltExp alt : exp.getAlternativePart()) {
				Object condition = eval(alt.getCondition());
				if (Boolean.TRUE.equals(condition)) {
					return wrapNull(eval(alt.getBody()));
				}
				// §8.2.2.8: "almost as nested OCL if expressions" — nor does it go on (#308)
				if (!Boolean.FALSE.equals(condition)) {
					return undefinedCondition("switch", condition);
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
				// Wrap FatalAssertionException as RaiseException for uniform matching
				RaiseException re = (ex instanceof RaiseException r) ? r
						: new RaiseException("AssertionFailed", null);

				for (CatchExp catchExp : exp.getExceptClause()) {
					if (matchesExceptClause(catchExp, re)) {
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

		private boolean matchesExceptClause(CatchExp catchExp, RaiseException re) {
			// Empty exception list = catch-all
			if (catchExp.getException().isEmpty()) {
				return true;
			}
			String exType = re.exceptionType;
			EClassifier exClassifier = re.exceptionClassifier;
			// Check if any declared exception type matches (including inheritance)
			for (EClassifier declaredType : catchExp.getException()) {
				String typeName = declaredType.getName();
				if (typeName.equals(exType) || "Exception".equals(typeName)) {
					return true;
				}
				// §8.4: Check EClass inheritance hierarchy
				if (exClassifier instanceof EClass raisedClass
						&& declaredType instanceof EClass declaredClass) {
					if (raisedClass.getEAllSuperTypes().contains(declaredClass)) {
						return true;
					}
				}
			}
			return false;
		}

		@Override
		public Object caseRaiseExp(RaiseExp exp) {
			// §8.2.2.15: raise produces an exception
			String exType = "Exception";
			String argument = null;
			EClassifier exClassifier = exp.getException();
			if (exClassifier != null) {
				exType = exClassifier.getName();
			}
			if (exp.getArgument() != null) {
				Object argVal = eval(exp.getArgument());
				argument = argVal != null ? argVal.toString() : null;
			}
			throw new RaiseException(exType, argument, exClassifier);
		}

		@Override
		public Object caseReturnExp(ReturnExp exp) {
			Object value = exp.getValue() != null ? eval(exp.getValue()) : null;
			throw new ReturnException(value, exp.getValue() == null);
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
					if (!Boolean.FALSE.equals(cond)) {
						undefinedCondition("log", cond, "nothing is logged");
					}
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
			addToExtent(created, exp.getExtent());

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
		public Object caseTransformationInstantiationExp(TransformationInstantiationExp exp) {
			OperationalTransformation importedTransf = exp.getImportedTransformation();
			if (importedTransf == null) {
				addError("TransformationInstantiationExp: importedTransformation is null");
				return WRAPPED_NULL;
			}
			if (engine == null) {
				addError("TransformationInstantiationExp: no engine available for nested execution");
				return WRAPPED_NULL;
			}

			// Evaluate arguments — each argument is a model extent (variable reference)
			List<Object> argValues = new ArrayList<>();
			for (OclExpression arg : exp.getArgument()) {
				argValues.add(eval(arg));
			}

			// Bind ModelParameter ↔ Argument-Extents
			Map<ModelParameter, QvtoModelExtent> bindings = new LinkedHashMap<>();
			List<ModelParameter> params = importedTransf.getModelParameter();
			for (int i = 0; i < params.size() && i < argValues.size(); i++) {
				Object argVal = argValues.get(i);
				if (argVal instanceof QvtoModelExtent extent) {
					bindings.put(params.get(i), extent);
				}
			}

			// Find the module EClass for the transformation instance
			EClass moduleClass = QvtoOperationResolver.findModuleClassIn(importedTransf);
			if (moduleClass == null) {
				// Create a minimal EClass so DynamicEObjectImpl has something
				moduleClass = EcoreFactory.eINSTANCE.createEClass();
				moduleClass.setName(importedTransf.getName());
			}

			return new QvtoTransformationInstance(importedTransf, moduleClass, bindings, engine);
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
				addToExtent(target, exp.getExtent());
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

			Object[] passed = args.clone();
			Object result = callMappingByName(exp.getName(), sourceObj, args, exp.isIsStrict());
			writeBackArgs(argExprs, passed, args);
			return wrapNull(result);
		}

		@Override
		public Object caseResolveExp(ResolveExp exp) {
			if (exp.isIsDeferred() && !isDeferredExecution) {
				// A late resolve is deferred with the property assignment it is part of
				// (caseAssignExp); anywhere else there is nothing to defer it with
				addWarning("Late resolve only supported in property assignments");
				return WRAPPED_NULL;
			}

			// Source object: §8.1.11.3 — when no explicit source, search ALL trace records (null).
			// A late resolve's source was captured when its assignment was reached (§8.2.1.22).
			Object sourceObj = null;
			if (deferredResolveSources.containsKey(exp)) {
				sourceObj = deferredResolveSources.get(exp);
			} else if (exp.getOwnedSource() != null) {
				sourceObj = eval(exp.getOwnedSource());
			}

			// §8.1.11.7: coll->resolve(Type) = coll->xcollect(resolve(Type))
			// When source is a Collection, iterate per-element and collect results
			if (sourceObj instanceof Collection<?> coll) {
				List<EObject> allResults = new ArrayList<>();
				for (Object elem : coll) {
					List<EObject> resolved = resolveForSource(exp, elem);
					if (resolved == null) {
						return OclInvalid.INSTANCE;
					}
					allResults.addAll(resolved);
				}
				if (exp.isOne()) {
					// §8.2.2.22: on a collection the operator is "a shorthand for invoking it in the
					// body of an xcollect" — one result per element, nulls dropped (#334)
					List<EObject> perElement = new ArrayList<>();
					for (Object elem : coll) {
						List<EObject> resolved = resolveForSource(exp, elem);
						if (resolved != null && !resolved.isEmpty()) {
							perElement.add(resolved.get(0));
						}
					}
					return wrapNull(perElement);
				}
				return wrapNull(allResults);
			}

			List<EObject> candidates = resolveForSource(exp, sourceObj);
			if (candidates == null) {
				return OclInvalid.INSTANCE;
			}

			if (exp.isOne()) {
				return wrapNull(candidates.isEmpty() ? null : candidates.get(0));
			}
			return wrapNull(candidates);
		}

		/**
		 * Resolves candidates for a single source object (or null for all records).
		 *
		 * @return the candidates, or {@code null} when the condition is neither true nor false
		 *         for one of them — the resolve is then invalid
		 */
		private List<EObject> resolveForSource(ResolveExp exp, Object sourceObj) {
			// Target type — the EClass itself (#156)
			EClass targetType = null;
			Variable target = exp.getTarget();
			if (target != null) {
				if (target.getType() instanceof EClass tc) {
					targetType = tc;
				}
			}

			// Resolve — dispatch based on inverse/in-mapping flags
			List<EObject> candidates;
			if (exp instanceof ResolveInExp resolveIn && resolveIn.getInMapping() != null) {
				MappingOperation inMapping = resolveIn.getInMapping();
				if (exp.isIsInverse()) {
					candidates = traceManager.invResolveIn(inMapping, sourceObj, targetType);
				} else {
					candidates = traceManager.resolveIn(inMapping, sourceObj, targetType);
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
						if (!(condVal instanceof Boolean)) {
							// §8.2.1.22: the condition filters as a select does, and a select
							// whose body is neither true nor false is invalid (#310)
							return null;
						}
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
