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
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.Module;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.Status;
import org.eclipse.fennec.m2x.ocl.api.OclOperation;
import org.eclipse.fennec.m2x.ocl.api.OclOperationProvider;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * Bridge that exposes QVT-O helpers and queries as {@link OclOperationProvider}
 * so the OCL evaluator can find and invoke them during expression evaluation.
 *
 * <p>This implements the mutual recursion aspect of D25: when the OCL evaluator
 * encounters an operation call it doesn't know, it consults custom providers
 * including this one. This provider delegates back to the {@link QvtoEvaluator}
 * to execute the imperative operation body.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class QvtoOperationProvider implements OclOperationProvider {

	private static final AnyType ANY_TYPE = OclStandardLibrary.INSTANCE.oclAny();

	private final OperationalTransformation transformation;
	private final QvtoEvaluator evaluator;
	private final QvtoOperationResolver operationResolver;
	private final QvtoBlackboxRegistry blackboxRegistry;
	private final Map<String, int[]> stringCounters = new HashMap<>();
	private List<OclOperation> operations;

	public QvtoOperationProvider(OperationalTransformation transformation, QvtoEvaluator evaluator,
			QvtoOperationResolver operationResolver, QvtoBlackboxRegistry blackboxRegistry) {
		this.transformation = Objects.requireNonNull(transformation, "transformation must not be null");
		this.evaluator = Objects.requireNonNull(evaluator, "evaluator must not be null");
		this.operationResolver = Objects.requireNonNull(operationResolver, "operationResolver must not be null");
		this.blackboxRegistry = blackboxRegistry; // nullable
	}

	@Override
	public List<OclOperation> getOperations() {
		if (operations == null) {
			operations = buildOperations();
		}
		return operations;
	}

	private List<OclOperation> buildOperations() {
		List<OclOperation> ops = new ArrayList<>();

		// Built-in model extent operations
		// §8.3.5.3: objectsOfType — exact type match only (not subtypes)
		ops.add(new OclOperation("objectsOfType", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent && args.length > 0) {
						EClass filterType = resolveEClassArg(args[0]);
						if (filterType != null) {
							List<EObject> result = new ArrayList<>();
							for (EObject eo : extent.getContents()) {
								if (eo.eClass() == filterType) {
									result.add(eo);
								}
							}
							return result;
						}
					}
					return List.of();
				}));
		// §8.3.5.2: objectsOfKind — includes subtypes
		ops.add(new OclOperation("objectsOfKind", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent && args.length > 0) {
						EClass filterType = resolveEClassArg(args[0]);
						if (filterType != null) {
							List<EObject> result = new ArrayList<>();
							for (EObject eo : extent.getContents()) {
								if (filterType.isInstance(eo)) {
									result.add(eo);
								}
							}
							return result;
						}
					}
					return List.of();
				}));
		// §8.3.5.1: objects — all objects
		ops.add(new OclOperation("objects", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent) {
						return new ArrayList<>(extent.getContents());
					}
					return List.of();
				}));
		// §8.3.5.4: rootObjects — top-level objects not contained by others
		ops.add(new OclOperation("rootObjects", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent) {
						return new ArrayList<>(extent.getContents());
					}
					return List.of();
				}));
		// §8.3.5.5: addElement — add element to extent
		ops.add(new OclOperation("addElement", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent && args.length > 0
							&& args[0] instanceof EObject eo) {
						checkExtentWritable(extent);
						extent.add(eo);
					}
					return null;
				}));
		// §8.1.3: addObject — legacy alias for addElement
		ops.add(new OclOperation("addObject", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent && args.length > 0
							&& args[0] instanceof EObject eo) {
						checkExtentWritable(extent);
						extent.add(eo);
					}
					return null;
				}));
		// §8.3.5.6: removeElement — remove element from extent
		ops.add(new OclOperation("removeElement", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent && args.length > 0
							&& args[0] instanceof EObject eo) {
						checkExtentWritable(extent);
						extent.getContents().remove(eo);
					}
					return null;
				}));
		// §8.3.5.8: copy — deep copy of model and its extent
		ops.add(new OclOperation("copy", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent) {
						List<EObject> copies = new ArrayList<>();
						for (EObject eo : extent.getContents()) {
							copies.add(EcoreUtil.copy(eo));
						}
						BasicQvtoModelExtent copyExtent = new BasicQvtoModelExtent();
						copyExtent.setContents(copies);
						return copyExtent;
					}
					return null;
				}));

		// §8.3.5.9: createEmptyModel — create new empty model extent
		ops.add(new OclOperation("createEmptyModel", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent) {
						return new BasicQvtoModelExtent();
					}
					return null;
				}));

		// §8.3.5.7: asTransformation() — cast a QVT-conformant model to a Transformation instance
		ops.add(new OclOperation("asTransformation", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoModelExtent extent) {
						for (EObject root : extent.getContents()) {
							if (root instanceof OperationalTransformation ot) {
								return new QvtoTransformationInstance(ot,
										QvtoOperationResolver.findModuleClassIn(ot),
										Map.of(), evaluator.getEngine());
							}
							// Also search nested contents
							for (var it = root.eAllContents(); it.hasNext(); ) {
								EObject child = it.next();
								if (child instanceof OperationalTransformation ot) {
									return new QvtoTransformationInstance(ot,
											QvtoOperationResolver.findModuleClassIn(ot),
											Map.of(), evaluator.getEngine());
								}
							}
						}
					}
					return null;
				}));

		// §8.3.9.6 / Eclipse CollectionTypeOperations: asList() on any Collection → mutable List
		ops.add(new OclOperation("asList", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof Collection<?> coll) {
						return new ArrayList<>(coll);
					}
					return null;
				}));

		// §8.2.2.12: UnlinkExp — feature.unlink(item)
		// Removes item from a multivalued property (in-place mutation).
		// Residence of removed object is unaffected.
		ops.add(new OclOperation("unlink", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof List<?> list && args.length > 0) {
						@SuppressWarnings("unchecked")
						List<Object> mutableList = (List<Object>) list;
						mutableList.remove(args[0]);
					}
					return self;
				}));

		// §8.3.4: Element operations (on EObject)
		// §8.3.4.3: metaClassName() : String
		ops.add(new OclOperation("metaClassName", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo) {
						return eo.eClass().getName();
					}
					return null;
				}));
		// §8.3.4.4: subobjects() : Set(Element)
		ops.add(new OclOperation("subobjects", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo) {
						return new ArrayList<>(eo.eContents());
					}
					return List.of();
				}));
		// §8.3.4.5: allSubobjects() : Set(Element)
		ops.add(new OclOperation("allSubobjects", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo) {
						List<EObject> result = new ArrayList<>();
						for (var iter = eo.eAllContents(); iter.hasNext(); ) {
							result.add(iter.next());
						}
						return result;
					}
					return List.of();
				}));
		// §8.3.4.6: subobjectsOfType(type) — exact type
		ops.add(new OclOperation("subobjectsOfType", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo && args.length > 0) {
						EClass filterType = resolveEClassArg(args[0]);
						if (filterType != null) {
							List<EObject> result = new ArrayList<>();
							for (EObject child : eo.eContents()) {
								if (child.eClass() == filterType) {
									result.add(child);
								}
							}
							return result;
						}
					}
					return List.of();
				}));
		// §8.3.4.8: subobjectsOfKind(type) — includes subtypes
		ops.add(new OclOperation("subobjectsOfKind", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo && args.length > 0) {
						EClass filterType = resolveEClassArg(args[0]);
						if (filterType != null) {
							List<EObject> result = new ArrayList<>();
							for (EObject child : eo.eContents()) {
								if (filterType.isInstance(child)) {
									result.add(child);
								}
							}
							return result;
						}
					}
					return List.of();
				}));
		// §8.3.4.7: allSubobjectsOfType(type) — exact type, recursive
		ops.add(new OclOperation("allSubobjectsOfType", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo && args.length > 0) {
						EClass filterType = resolveEClassArg(args[0]);
						if (filterType != null) {
							List<EObject> result = new ArrayList<>();
							for (var iter = eo.eAllContents(); iter.hasNext(); ) {
								EObject desc = iter.next();
								if (desc.eClass() == filterType) {
									result.add(desc);
								}
							}
							return result;
						}
					}
					return List.of();
				}));
		// §8.3.4.9: allSubobjectsOfKind(type) — includes subtypes, recursive
		ops.add(new OclOperation("allSubobjectsOfKind", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo && args.length > 0) {
						EClass filterType = resolveEClassArg(args[0]);
						if (filterType != null) {
							List<EObject> result = new ArrayList<>();
							for (var iter = eo.eAllContents(); iter.hasNext(); ) {
								EObject desc = iter.next();
								if (filterType.isInstance(desc)) {
									result.add(desc);
								}
							}
							return result;
						}
					}
					return List.of();
				}));
		// §8.3.4.10: clone() — shallow copy (skip containments)
		ops.add(new OclOperation("clone", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo) {
						EcoreUtil.Copier copier = new EcoreUtil.Copier() {
							private static final long serialVersionUID = 1L;
							@Override
							protected void copyContainment(EReference ref,
									EObject src, EObject tgt) {
								// shallow: skip containment references
							}
						};
						EObject result = copier.copy(eo);
						copier.copyReferences();
						return result;
					}
					return null;
				}));
		// §8.3.4.11: Element::deepclone() — deep copy (recursive)
		// §8.3.9.13: List(T)::deepclone() — new list with each element deep-cloned
		ops.add(new OclOperation("deepclone", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo) {
						return EcoreUtil.copy(eo);
					}
					if (self instanceof Collection<?> coll) {
						List<Object> result = new ArrayList<>();
						for (Object elem : coll) {
							if (elem instanceof EObject eo) {
								result.add(EcoreUtil.copy(eo));
							} else {
								result.add(elem); // primitives are immutable
							}
						}
						return result;
					}
					return null;
				}));
		// §8.3.4 (MOF reflective): container() — containing object
		ops.add(new OclOperation("container", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof EObject eo) {
						return eo.eContainer();
					}
					return null;
				}));

		// Module-level helpers/queries/mappings
		EClass moduleClass = operationResolver.findModuleClass();
		if (moduleClass == null) {
			return ops;
		}

		addModuleOperations(ops, moduleClass, null);

		// Also export operations from imported modules (§8.1.4 Library access)
		for (ModuleImport mi : transformation.getModuleImport()) {
			Module importedModule = mi.getImportedModule();
			if (importedModule != null) {
				EClass importedModuleClass = QvtoOperationResolver.findModuleClassIn(importedModule);
				if (importedModuleClass != null) {
					// §8.4: selective visibility via importedNames
					Set<String> visibleNames = mi.getImportedNames().isEmpty()
							? null  // null = all visible
							: Set.copyOf(mi.getImportedNames());
					addModuleOperations(ops, importedModuleClass, visibleNames);
				}
			}
		}

		// §8.3.6: Transformation instance operations
		addTransformationInstanceOperations(ops);

		// §8.3.6: Status property operations
		addStatusOperations(ops);

		// §8.3.16.31–35: String counter operations
		addStringCounterOperations(ops);

		return ops;
	}

	/**
	 * §8.3.6: Operations on transformation instances (transform, parallelTransform).
	 */
	private void addTransformationInstanceOperations(List<OclOperation> ops) {
		// transform(...) : Status — no-arg (pre-bound) or with model extent arguments (§8.1.21)
		ops.add(new OclOperation("transform", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoTransformationInstance instance) {
						if (args.length > 0 && instance.getModelBindings().isEmpty()) {
							// §8.1.21: transform(model1, model2, ...) — bind at call time
							instance = QvtoTransformationOperations.bindModels(instance, args);
						}
						return QvtoTransformationOperations.handleTransform(instance);
					}
					return null;
				}));

		// parallelTransform(...) : Status — no-arg or with model extent arguments
		ops.add(new OclOperation("parallelTransform", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof QvtoTransformationInstance instance) {
						if (args.length > 0 && instance.getModelBindings().isEmpty()) {
							instance = QvtoTransformationOperations.bindModels(instance, args);
						}
						return QvtoTransformationOperations.handleParallelTransform(instance);
					}
					return null;
				}));

		// wait(statuses : Set(Status)) : Void
		ops.add(new OclOperation("wait", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (args.length > 0) {
						QvtoTransformationOperations.handleWait(args[0]);
					}
					return null;
				}));
	}

	/**
	 * §8.3.6: Property access on Status objects (succeeded(), failed(), raisedException()).
	 */
	private void addStatusOperations(List<OclOperation> ops) {
		ops.add(new OclOperation("succeeded", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof Status s) {
						return s.isSucceeded();
					}
					return null;
				}));

		ops.add(new OclOperation("failed", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof Status s) {
						return s.isFailed();
					}
					return null;
				}));

		ops.add(new OclOperation("raisedException", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof Status s) {
						return s.getRaisedException();
					}
					return null;
				}));
	}

	/**
	 * §8.3.16.31–35: String counter operations for generating unique names.
	 *
	 * <p>Static operations (31–34) are invoked as {@code String.startStrCounter('Foo')}
	 * where the source is a PrimitiveType "String". Instance operation (35)
	 * {@code addSuffixNumber()} is invoked on a String value.
	 */
	private void addStringCounterOperations(List<OclOperation> ops) {
		// §8.3.16.31: String::startStrCounter(s:String) : Void — initialize/reset counter to 0
		ops.add(new OclOperation("startStrCounter", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof PrimitiveType pt && "String".equals(pt.getName())
							&& args.length > 0 && args[0] instanceof String s) {
						stringCounters.put(s, new int[]{0});
					}
					return null;
				}));

		// §8.3.16.32: String::getStrCounter(s:String) : Integer — get current value or null
		ops.add(new OclOperation("getStrCounter", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof PrimitiveType pt && "String".equals(pt.getName())
							&& args.length > 0 && args[0] instanceof String s) {
						int[] counter = stringCounters.get(s);
						return counter != null ? (long) counter[0] : null;
					}
					return null;
				}));

		// §8.3.16.33: String::incrStrCounter(s:String) : Integer — increment and return new value
		ops.add(new OclOperation("incrStrCounter", ANY_TYPE, List.of(ANY_TYPE), ANY_TYPE,
				(self, args) -> {
					if (self instanceof PrimitiveType pt && "String".equals(pt.getName())
							&& args.length > 0 && args[0] instanceof String s) {
						int[] counter = stringCounters.get(s);
						if (counter == null) {
							counter = new int[]{0};
							stringCounters.put(s, counter);
						}
						return (long) ++counter[0];
					}
					return null;
				}));

		// §8.3.16.34: String::restartAllStrCounter() : Void — reset all counters to 0
		ops.add(new OclOperation("restartAllStrCounter", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof PrimitiveType pt && "String".equals(pt.getName())) {
						for (int[] counter : stringCounters.values()) {
							counter[0] = 0;
						}
					}
					return null;
				}));

		// §8.3.16.35: String::addSuffixNumber() : String — append counter value as suffix
		ops.add(new OclOperation("addSuffixNumber", ANY_TYPE, List.of(), ANY_TYPE,
				(self, args) -> {
					if (self instanceof String s) {
						int[] counter = stringCounters.get(s);
						if (counter == null) {
							// Not started → start counter, return string without suffix
							stringCounters.put(s, new int[]{0});
							return s;
						}
						// Get current value, then increment
						int value = counter[0];
						counter[0]++;
						return s + value;
					}
					return null;
				}));
	}

	/**
	 * Adds operations from a module class, optionally filtered by visible names.
	 *
	 * @param ops the operations list to add to
	 * @param moduleClass the module's EClass containing EOperations
	 * @param visibleNames if non-null, only operations with names in this set are visible
	 */
	private void addModuleOperations(List<OclOperation> ops, EClass moduleClass,
			Set<String> visibleNames) {
		for (EOperation eOp : moduleClass.getEOperations()) {
			if (eOp instanceof ImperativeOperation impOp) {
				String name = impOp.getName();
				if (name == null || "main".equals(name)) {
					continue;
				}
				// §8.4: selective import — skip operations not in importedNames
				if (visibleNames != null && !visibleNames.contains(name)) {
					continue;
				}
				// §8.2.1.10: Use context type for dispatch
				OclType ownerType = resolveOwnerType(impOp);

				if (impOp.isIsBlackbox()) {
					// Blackbox operation — delegate to library's invoke()
					ops.add(new OclOperation(name, ownerType, List.of(), ANY_TYPE,
							(self, args) -> invokeBlackbox(impOp, self, args)));
				} else {
					ops.add(new OclOperation(
							name,
							ownerType,
							List.of(),
							ANY_TYPE,
							(self, args) -> {
								// §8.1.19: contextual operation on null → propagate null
								if (self == null && impOp.getContext() != null) {
									return null;
								}
								return evaluator.callOperation(impOp, self, args);
							}
					));
				}
			}
		}
	}

	/**
	 * Invokes a blackbox operation by finding its library and delegating to invoke().
	 */
	private Object invokeBlackbox(ImperativeOperation impOp, Object self, Object[] args) {
		if (blackboxRegistry == null) {
			return null;
		}
		// Find the library that owns this operation
		// The operation's containing EClass is the module class, whose containing
		// EPackage is the Library module with the library name
		EClass moduleClass = impOp.getEContainingClass();
		if (moduleClass == null) {
			return null;
		}
		EPackage modulePackage = moduleClass.getEPackage();
		if (!(modulePackage instanceof Module module)) {
			return null;
		}

		// Find the library by matching module name against all registered libraries
		String moduleName = module.getName();
		for (QvtoBlackboxLibrary lib : blackboxRegistry.getLibraries()) {
			if (moduleName.equals(lib.getModuleName())) {
				QvtoBlackboxInvocationContextImpl ctx = new QvtoBlackboxInvocationContextImpl(
						self, evaluator.diagnostics(), evaluator.extentManager(),
						evaluator.configProperties(), evaluator.packageRegistry());
				return lib.invoke(impOp.getName(), ctx, args);
			}
		}
		return null;
	}

	/**
	 * §8.2.1.10: Resolves the owner type for an imperative operation from its context parameter.
	 * Returns ANY_TYPE for non-contextual operations.
	 */
	private OclType resolveOwnerType(ImperativeOperation impOp) {
		var ctxParam = impOp.getContext();
		if (ctxParam == null || ctxParam.getEType() == null) {
			return ANY_TYPE;
		}
		EClassifier classifier = ctxParam.getEType();
		if (classifier instanceof EClass ec) {
			ClassifierType ct = OclFactory.eINSTANCE.createClassifierType();
			ct.setReferredClassifier(ec);
			return ct;
		}
		if (classifier instanceof EDataType dt) {
			// Map Ecore primitive names to OCL names (EString→String, EInt→Integer, etc.) and
			// answer with the library's instance; an unknown name still gets a fresh type.
			String oclName = mapEcoreToOclPrimitiveName(dt.getName());
			return OclStandardLibrary.INSTANCE.primitive(oclName).map(OclType.class::cast)
					.orElseGet(() -> {
						PrimitiveType pt = OclFactory.eINSTANCE.createPrimitiveType();
						pt.setName(oclName);
						return pt;
					});
		}
		return ANY_TYPE;
	}

	/**
	 * §8.1.3.2: Guards mutation operations on read-only extents.
	 */
	private static void checkExtentWritable(QvtoModelExtent extent) {
		if (extent.isReadOnly()) {
			throw new QvtoControlFlowException.RaiseException(
					"InvalidModelMutation",
					"Cannot modify read-only model extent (in-parameter)");
		}
	}

	/**
	 * Maps Ecore EDataType names to OCL primitive type names.
	 */
	private static String mapEcoreToOclPrimitiveName(String ecoreName) {
		return switch (ecoreName) {
			case "EString" -> "String";
			case "EInt", "EIntegerObject" -> "Integer";
			case "EDouble", "EDoubleObject", "EFloat", "EFloatObject" -> "Real";
			case "EBoolean", "EBooleanObject" -> "Boolean";
			case "EBigInteger" -> "UnlimitedNatural";
			default -> ecoreName;
		};
	}

	private static EClass resolveEClassArg(Object arg) {
		if (arg instanceof EClass ec) {
			return ec;
		}
		if (arg instanceof ClassifierType ct && ct.getReferredClassifier() instanceof EClass ec) {
			return ec;
		}
		return null;
	}
}
