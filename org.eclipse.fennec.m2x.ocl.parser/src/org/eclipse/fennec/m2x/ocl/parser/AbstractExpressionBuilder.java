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
package org.eclipse.fennec.m2x.ocl.parser;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.notify.impl.AdapterImpl;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.fennec.m2x.model.ocl.BooleanLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.CollectionItem;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.CollectionLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.CollectionRange;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.EnumLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.IfExp;
import org.eclipse.fennec.m2x.model.ocl.IntegerLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.InvalidLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.IterateExp;
import org.eclipse.fennec.m2x.model.ocl.IteratorExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.NullLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2x.model.ocl.RealLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.TypeExp;
import org.eclipse.fennec.m2x.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;

/**
 * Shared OCL expression building logic for use by both OCL and M2T parsers.
 *
 * <p>Provides all context-free helper methods for constructing EMF OCL AST nodes:
 * type resolution, name resolution, expression creation, and utility methods.
 * Grammar-specific visitor code remains in concrete builder classes
 * ({@link OclAstBuilder} for OCL, M2tExpressionBuilder for MOFM2T).
 *
 * <p>This class manages the expression building state: context type, package registry,
 * and the {@link OclEnvironment} scope chain for variable resolution.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class AbstractExpressionBuilder {

	/** Shared OCL factory instance. */
	protected static final OclFactory FACTORY = OclFactory.eINSTANCE;

	/** Iterator names recognized for implicit iterator shorthand (§11.9). */
	public static final Set<String> ITERATOR_NAMES = Set.of(
			"select", "reject", "collect", "collectNested", "forAll", "exists",
			"any", "one", "isUnique", "sortedBy", "closure");

	private final EClassifier contextType;
	private final EPackage.Registry packageRegistry;

	/** Aliases a Complete OCL document introduced with {@code import alias : path}. */
	private Map<String, String> packageAliases = Map.of();
	private OclEnvironment environment;
	private final List<Resource.Diagnostic> diagnostics = new ArrayList<>();

	/**
	 * Creates a new expression builder for the given context type.
	 *
	 * @param contextType the classifier that defines the type of {@code self}
	 * @param packageRegistry optional package registry for classifier resolution
	 */
	public AbstractExpressionBuilder(EClassifier contextType, EPackage.Registry packageRegistry) {
		this.contextType = Objects.requireNonNull(contextType, "contextType must not be null");
		this.packageRegistry = packageRegistry;
		Variable selfVar = FACTORY.createVariable();
		selfVar.setName("self");
		selfVar.setType(createClassifierType(contextType));
		this.environment = OclEnvironment.root(selfVar);
	}

	// ==================== Diagnostics ====================

	/**
	 * Returns the diagnostics collected while building, in the order they occurred.
	 *
	 * <p>An unresolvable name is recorded here rather than substituted silently
	 * (D42, #66). The caller — the parser support of the respective engine — turns a
	 * non-empty list into a parse exception once the whole unit has been visited, so
	 * that every problem is reported instead of only the first.
	 *
	 * @return the collected diagnostics, never {@code null}
	 */
	public List<Resource.Diagnostic> getDiagnostics() {
		return diagnostics;
	}

	/**
	 * Records an error diagnostic.
	 *
	 * @param message the message, phrased for a template or expression author
	 */
	public void addError(String message) {
		diagnostics.add(new OclParseDiagnostic(message, 0, 0));
	}

	// ==================== State Accessors ====================

	/**
	 * Makes the aliases of a Complete OCL document usable in qualified names.
	 *
	 * <p>{@code import c : company} lets the rest of the document say {@code c::Person};
	 * without this the alias resolves to nothing and the type comes out unknown, far from
	 * the line that introduced it.
	 *
	 * @param aliases alias to package path, must not be {@code null}
	 */
	public void registerPackageAliases(Map<String, String> aliases) {
		this.packageAliases = Map.copyOf(Objects.requireNonNull(aliases, "aliases must not be null"));
	}

	public EClassifier getContextType() {
		return contextType;
	}

	public EPackage.Registry getPackageRegistry() {
		return packageRegistry;
	}

	public OclEnvironment getEnvironment() {
		return environment;
	}

	public void setEnvironment(OclEnvironment environment) {
		this.environment = Objects.requireNonNull(environment, "environment must not be null");
	}

	/**
	 * Registers an alias name for the {@code self} variable (G-10: §12.12.5 form [B]).
	 */
	public void registerSelfAlias(String alias) {
		Variable selfVar = environment.lookup("self").orElseThrow();
		Variable aliasVar = FACTORY.createVariable();
		aliasVar.setName(alias);
		aliasVar.setType(selfVar.getType());
		this.environment = this.environment.nested(aliasVar);
	}

	// ==================== Literal Builders ====================

	public IntegerLiteralExp buildIntegerLiteral(String text) {
		IntegerLiteralExp exp = FACTORY.createIntegerLiteralExp();
		exp.setIntegerSymbol(Long.parseLong(text));
		return exp;
	}

	public RealLiteralExp buildRealLiteral(String text) {
		RealLiteralExp exp = FACTORY.createRealLiteralExp();
		exp.setRealSymbol(Double.parseDouble(text));
		return exp;
	}

	/**
	 * Builds a string literal from raw token texts (handles adjacent concatenation G-04).
	 *
	 * @param rawTokenTexts the raw string tokens including quotes (e.g. {@code 'hello'})
	 */
	public StringLiteralExp buildStringLiteral(List<String> rawTokenTexts) {
		StringLiteralExp exp = FACTORY.createStringLiteralExp();
		StringBuilder sb = new StringBuilder();
		for (String text : rawTokenTexts) {
			sb.append(unescapeString(text.substring(1, text.length() - 1)));
		}
		exp.setStringSymbol(sb.toString());
		return exp;
	}

	public BooleanLiteralExp buildBooleanLiteral(boolean value) {
		BooleanLiteralExp exp = FACTORY.createBooleanLiteralExp();
		exp.setBooleanSymbol(value);
		return exp;
	}

	public NullLiteralExp buildNullLiteral() {
		return FACTORY.createNullLiteralExp();
	}

	public InvalidLiteralExp buildInvalidLiteral() {
		return FACTORY.createInvalidLiteralExp();
	}

	public UnlimitedNaturalLiteralExp buildUnlimitedNaturalLiteral() {
		UnlimitedNaturalLiteralExp exp = FACTORY.createUnlimitedNaturalLiteralExp();
		exp.setUnlimitedNaturalSymbol(-1L);
		return exp;
	}

	// ==================== Path Name Expression ====================

	/**
	 * Resolves a path name to a variable, enum literal, or type expression.
	 */
	public OclExpression buildPathNameExp(List<String> segments) {
		if (segments.size() == 1) {
			String name = segments.get(0);
			return environment.lookup(name)
					.<OclExpression>map(this::createVariableExp)
					.orElseGet(() -> resolveImplicitProperty(name));
		}
		if (segments.size() == 2) {
			EnumLiteralExp enumExp = tryResolveEnumLiteral(segments);
			if (enumExp != null) {
				return enumExp;
			}
		}
		return resolveQualifiedName(segments);
	}

	// ==================== Unary / Binary Operations ====================

	public OperationCallExp createUnaryOperation(String opName, OclExpression operand) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setOwnedSource(operand);
		resolveOperation(exp, opName);
		return exp;
	}

	public OperationCallExp createBinaryOperation(String opName, OclExpression left,
			OclExpression right) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setOwnedSource(left);
		exp.getOwnedArguments().add(right);
		resolveOperation(exp, opName);
		return exp;
	}

	// ==================== Self / Variable ====================

	public VariableExp buildSelfExp() {
		return createVariableExp(environment.lookup("self").orElseThrow());
	}

	public VariableExp createVariableExp(Variable variable) {
		VariableExp exp = FACTORY.createVariableExp();
		exp.setReferredVariable(variable);
		exp.setType(variable.getType());
		return exp;
	}

	// ==================== Implicit Operation Call ====================

	/**
	 * Builds an implicit operation call (unqualified, source is self).
	 */
	public OperationCallExp buildImplicitOperationCall(String opName,
			List<OclExpression> args, boolean isPre) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setIsImplicit(true);
		exp.setIsPre(isPre);
		VariableExp selfRef = createVariableExp(environment.lookup("self").orElseThrow());
		exp.setOwnedSource(selfRef);
		exp.getOwnedArguments().addAll(args);
		resolveOperation(exp, opName);
		return exp;
	}

	// ==================== Collection Literal ====================

	public CollectionLiteralExp buildCollectionLiteral(String kindText,
			List<CollectionLiteralPart> parts) {
		CollectionLiteralExp exp = FACTORY.createCollectionLiteralExp();
		exp.setKind(resolveCollectionKind(kindText));
		if (parts != null) {
			exp.getOwnedParts().addAll(parts);
		}
		return exp;
	}

	public CollectionItem buildCollectionItem(OclExpression item) {
		CollectionItem ci = FACTORY.createCollectionItem();
		ci.setOwnedItem(item);
		return ci;
	}

	public CollectionRange buildCollectionRange(OclExpression first, OclExpression last) {
		CollectionRange range = FACTORY.createCollectionRange();
		range.setOwnedFirst(first);
		range.setOwnedLast(last);
		return range;
	}

	// ==================== Tuple Literal ====================

	public TupleLiteralExp buildTupleLiteral(List<TupleLiteralPart> parts) {
		TupleLiteralExp exp = FACTORY.createTupleLiteralExp();
		exp.getOwnedParts().addAll(parts);
		return exp;
	}

	public TupleLiteralPart buildTupleLiteralPart(String name, OclType type,
			OclExpression init) {
		TupleLiteralPart part = FACTORY.createTupleLiteralPart();
		part.setName(name);
		if (type != null) {
			part.setType(type);
		}
		part.setOwnedInit(init);
		return part;
	}

	// ==================== Map Literal ====================

	public MapLiteralExp buildMapLiteral(List<MapLiteralPart> parts) {
		MapLiteralExp exp = FACTORY.createMapLiteralExp();
		if (parts != null) {
			exp.getOwnedParts().addAll(parts);
		}
		return exp;
	}

	public MapLiteralPart buildMapLiteralPart(OclExpression key, OclExpression value) {
		MapLiteralPart part = FACTORY.createMapLiteralPart();
		part.setOwnedKey(key);
		part.setOwnedValue(value);
		return part;
	}

	// ==================== Navigation ====================

	/**
	 * Builds a property call (dot navigation). Handles implicit collect for collections.
	 */
	public OclExpression buildPropertyCall(OclExpression source, String propName,
			boolean isSafe, boolean isPre) {
		if (source.getType() instanceof CollectionType sourceColType) {
			return buildImplicitCollect(source, propName, isSafe, isPre, sourceColType);
		}
		PropertyCallExp exp = FACTORY.createPropertyCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setIsPre(isPre);
		resolveProperty(exp, propName);
		return exp;
	}

	/**
	 * Builds an implicit collect for collection property access (§7.6.1).
	 */
	public IteratorExp buildImplicitCollect(OclExpression source, String propName,
			boolean isSafe, boolean isPre, CollectionType sourceColType) {
		Variable iterVar = FACTORY.createVariable();
		iterVar.setName("_implicit");
		if (sourceColType.getElementType() != null) {
			iterVar.setType(copyType(sourceColType.getElementType()));
		}

		VariableExp iterRef = FACTORY.createVariableExp();
		iterRef.setReferredVariable(iterVar);
		if (iterVar.getType() != null) {
			iterRef.setType(copyType(iterVar.getType()));
		}

		PropertyCallExp bodyExp = FACTORY.createPropertyCallExp();
		bodyExp.setOwnedSource(iterRef);
		bodyExp.setIsSafe(isSafe);
		bodyExp.setIsPre(isPre);
		resolveProperty(bodyExp, propName);

		IteratorExp collectExp = FACTORY.createIteratorExp();
		collectExp.setOwnedSource(source);
		collectExp.setName("collect");
		collectExp.getOwnedIterators().add(iterVar);
		collectExp.setOwnedBody(bodyExp);

		OclType bodyType = bodyExp.getType();
		if (bodyType != null) {
			CollectionType resultType = FACTORY.createCollectionType();
			resultType.setElementType(copyType(bodyType));
			resultType.setKind(CollectionKind.SEQUENCE);
			collectExp.setType(resultType);
		}
		return collectExp;
	}

	/**
	 * Builds a dot operation call (e.g. {@code source.op(args)}).
	 */
	public OperationCallExp buildDotOperationCall(OclExpression source, String opName,
			List<OclExpression> args, boolean isSafe, boolean isPre) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setIsPre(isPre);
		exp.getOwnedArguments().addAll(args);
		resolveOperation(exp, opName);
		return exp;
	}

	// ==================== Iterator / Iterate / Collection Ops ====================

	/**
	 * Builds an iterator expression (e.g. {@code ->select(x | body)}).
	 *
	 * <p>Note: the caller must manage environment push/pop for iterator variables.
	 */
	public IteratorExp buildIteratorExp(OclExpression source, String iterName,
			List<Variable> iterVars, OclExpression body, boolean isSafe) {
		IteratorExp exp = FACTORY.createIteratorExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setName(iterName);
		ArrowCallMarker.mark(exp);
		exp.getOwnedIterators().addAll(iterVars);
		exp.setOwnedBody(body);

		OclType elementType = inferElementType(source);
		if (elementType != null) {
			switch (iterName) {
				case "select", "reject", "sortedBy", "closure":
					exp.setType(elementType);
					break;
				default:
					break;
			}
		}
		return exp;
	}

	/**
	 * Validates iterator variable count (§11.9).
	 *
	 * @throws IllegalArgumentException if too many variables
	 */
	public void validateIteratorVarCount(String iterName, int varCount) {
		int maxVars = switch (iterName) {
			case "forAll", "exists" -> 3;
			default -> 1;
		};
		if (varCount > maxVars) {
			throw new IllegalArgumentException("Iterator '" + iterName + "' allows at most "
					+ maxVars + " variable(s), but got " + varCount);
		}
	}

	/**
	 * Builds an iterate expression (e.g. {@code ->iterate(x; acc = init | body)}).
	 *
	 * <p>Note: the caller must manage environment push/pop.
	 */
	public IterateExp buildIterateExp(OclExpression source, Variable iterVar,
			Variable accumulator, OclExpression body, boolean isSafe) {
		IterateExp exp = FACTORY.createIterateExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		ArrowCallMarker.mark(exp);
		exp.getOwnedIterators().add(iterVar);
		exp.setOwnedResult(accumulator);
		exp.setOwnedBody(body);
		return exp;
	}

	/**
	 * Builds a collection operation call (e.g. {@code ->includes(x)}).
	 * If the operation name is a known iterator and there is exactly one argument,
	 * it creates an implicit iterator instead.
	 *
	 * <p>Note: the caller must manage environment push/pop for implicit iterators.
	 * Use {@link #buildImplicitIterator} for that case.
	 */
	public OperationCallExp buildCollectionOperationCall(OclExpression source,
			String opName, List<OclExpression> args, boolean isSafe) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		ArrowCallMarker.mark(exp);
		exp.getOwnedArguments().addAll(args);
		resolveOperation(exp, opName);
		return exp;
	}

	/**
	 * Builds an implicit iterator for shorthand syntax (e.g. {@code ->any(expr)}).
	 *
	 * <p>Note: the caller must manage environment push/pop.
	 */
	public IteratorExp buildImplicitIterator(OclExpression source, String iterName,
			OclExpression body, boolean isSafe) {
		IteratorExp exp = FACTORY.createIteratorExp();
		exp.setOwnedSource(source);
		exp.setName(iterName);
		exp.setIsSafe(isSafe);
		ArrowCallMarker.mark(exp);

		OclType elementType = inferElementType(source);
		Variable iterVar = FACTORY.createVariable();
		iterVar.setName("_implicit");
		if (elementType != null) {
			iterVar.setType(elementType);
		}
		exp.getOwnedIterators().add(iterVar);
		exp.setOwnedBody(body);

		if (elementType != null) {
			switch (iterName) {
				case "select", "reject", "sortedBy", "closure":
					exp.setType(elementType);
					break;
				default:
					break;
			}
		}
		return exp;
	}

	/**
	 * Creates the implicit iterator variable and pushes it into the environment.
	 * Call this before visiting the body expression, then call {@link #buildImplicitIterator}
	 * after visiting the body.
	 *
	 * @return the implicit iterator variable
	 */
	public Variable pushImplicitIteratorEnv(OclExpression source) {
		OclType elementType = inferElementType(source);
		Variable iterVar = FACTORY.createVariable();
		iterVar.setName("_implicit");
		if (elementType != null) {
			iterVar.setType(elementType);
		}
		this.environment = this.environment.nestedImplicit(iterVar);
		return iterVar;
	}

	// ==================== If / Let ====================

	/**
	 * Builds an if-then-else expression with optional elseif chain.
	 */
	public IfExp buildIfExp(OclExpression condition, OclExpression thenExp,
			List<OclExpression> elseIfConditions, List<OclExpression> elseIfExps,
			OclExpression elseExp) {
		IfExp ifExp = FACTORY.createIfExp();
		ifExp.setOwnedCondition(condition);
		ifExp.setOwnedThen(thenExp);

		if (elseIfConditions != null && !elseIfConditions.isEmpty()) {
			IfExp current = ifExp;
			for (int i = 0; i < elseIfConditions.size(); i++) {
				IfExp nested = FACTORY.createIfExp();
				nested.setOwnedCondition(elseIfConditions.get(i));
				nested.setOwnedThen(elseIfExps.get(i));
				current.setOwnedElse(nested);
				current = nested;
			}
			current.setOwnedElse(elseExp);
		} else {
			ifExp.setOwnedElse(elseExp);
		}
		return ifExp;
	}

	/**
	 * Creates a variable with optional type, suitable for let bindings or iterator vars.
	 */
	public Variable createVariable(String name, OclType type, OclExpression init) {
		Variable var = FACTORY.createVariable();
		var.setName(name);
		if (type != null) {
			var.setType(type);
		}
		if (init != null) {
			var.setOwnedInit(init);
		}
		return var;
	}

	// ==================== Type Expressions ====================

	/**
	 * Builds a TypeExp wrapping the given type.
	 */
	public TypeExp buildTypeExp(OclType type) {
		TypeExp exp = FACTORY.createTypeExp();
		exp.setReferredType(type);
		return exp;
	}

	// ==================== Type Resolution ====================

	public OclType createPrimitiveType(String name) {
		var type = FACTORY.createPrimitiveType();
		type.setName(name);
		return type;
	}

	public ClassifierType createClassifierType(EClassifier classifier) {
		ClassifierType type = FACTORY.createClassifierType();
		type.setReferredClassifier(classifier);
		type.setName(classifier.getName());
		return type;
	}

	public CollectionType createCollectionTypeForFeature(EStructuralFeature feature) {
		CollectionType colType = FACTORY.createCollectionType();
		colType.setElementType(createClassifierType(feature.getEType()));
		if (feature instanceof EReference ref) {
			if (ref.isOrdered()) {
				colType.setKind(ref.isUnique() ? CollectionKind.ORDERED_SET : CollectionKind.SEQUENCE);
			} else {
				colType.setKind(ref.isUnique() ? CollectionKind.SET : CollectionKind.BAG);
			}
		} else {
			colType.setKind(CollectionKind.SEQUENCE);
		}
		return colType;
	}

	public OclType buildCollectionType(String kindText, OclType elementType) {
		CollectionKind kind = resolveCollectionKind(kindText);
		CollectionType type = switch (kind) {
			case SET -> FACTORY.createSetType();
			case ORDERED_SET -> FACTORY.createOrderedSetType();
			case BAG -> FACTORY.createBagType();
			case SEQUENCE -> FACTORY.createSequenceType();
			default -> FACTORY.createCollectionType();
		};
		type.setKind(kind);
		type.setElementType(elementType);
		return type;
	}

	public OclType buildMapType(OclType keyType, OclType valueType) {
		var type = FACTORY.createMapType();
		type.setKeyType(keyType);
		type.setValueType(valueType);
		return type;
	}

	public OclType buildTupleType(List<String> partNames, List<OclType> partTypes) {
		var type = FACTORY.createTupleType();
		for (int i = 0; i < partNames.size(); i++) {
			var part = FACTORY.createTuplePart();
			part.setName(partNames.get(i));
			part.setType(partTypes.get(i));
			type.getOwnedParts().add(part);
		}
		return type;
	}

	public OclType buildTypeFromPath(List<String> segments) {
		return createClassifierType(resolveClassifier(segments));
	}

	// ==================== Name Resolution ====================

	public void resolveProperty(PropertyCallExp exp, String propName) {
		EClassifier sourceType = getSourceClassifier(exp.getOwnedSource());
		if (sourceType instanceof EClass eClass) {
			EStructuralFeature feature = eClass.getEStructuralFeature(propName);
			if (feature != null) {
				exp.setReferredProperty(feature);
				EClassifier featureType = feature.getEType();
				if (featureType != null) {
					if (feature.isMany()) {
						exp.setType(createCollectionTypeForFeature(feature));
					} else {
						exp.setType(createClassifierType(featureType));
					}
				}
				return;
			}
		}
		EAttribute synth = EcoreFactory.eINSTANCE.createEAttribute();
		synth.setName(propName);
		exp.setReferredProperty(synth);
	}

	public void resolveOperation(OperationCallExp exp, String opName) {
		exp.setName(opName);
		EClassifier sourceType = getSourceClassifier(exp.getOwnedSource());
		if (sourceType instanceof EClass eClass) {
			int argCount = exp.getOwnedArguments().size();
			EOperation bestMatch = null;
			for (EOperation op : eClass.getEAllOperations()) {
				if (opName.equals(op.getName())) {
					if (op.getEParameters().size() == argCount) {
						bestMatch = op;
						break;
					}
					if (bestMatch == null) {
						bestMatch = op;
					}
				}
			}
			if (bestMatch != null) {
				exp.setReferredOperation(bestMatch);
			}
		}
	}

	public EClassifier resolveClassifier(List<String> segments) {
		if (segments.size() == 1) {
			String name = segments.get(0);
			if (contextType instanceof EClass contextClass) {
				EClassifier found = contextClass.getEPackage().getEClassifier(name);
				if (found != null) {
					return found;
				}
			}
			EClassifier fromRegistry = findInRegistry(name);
			if (fromRegistry != null) {
				return fromRegistry;
			}
			return unresolvedName(segments);
		}
		String classifierName = segments.get(segments.size() - 1);
		String packageName = String.join("::", segments.subList(0, segments.size() - 1));
		packageName = packageAliases.getOrDefault(packageName, packageName);

		if (contextType instanceof EClass contextClass) {
			EPackage ctxPkg = contextClass.getEPackage();
			if (ctxPkg.getName().equals(packageName) || ctxPkg.getNsURI().equals(packageName)) {
				EClassifier found = ctxPkg.getEClassifier(classifierName);
				if (found != null) {
					return found;
				}
			}
		}
		if (packageRegistry != null) {
			for (Object key : packageRegistry.keySet().toArray()) {
				EPackage pkg = packageRegistry.getEPackage((String) key);
				if (pkg != null && (pkg.getName().equals(packageName)
						|| pkg.getNsURI().equals(packageName))) {
					EClassifier found = pkg.getEClassifier(classifierName);
					if (found != null) {
						return found;
					}
				}
			}
		}
		return unresolvedName(segments);
	}

	public OclExpression resolveImplicitProperty(String name) {
		Optional<Variable> implicitIter = environment.lookupImplicitIterator();
		if (implicitIter.isPresent()) {
			Variable iterVar = implicitIter.get();
			EStructuralFeature feature = resolveFeatureOnType(iterVar.getType(), name);
			if (feature != null) {
				PropertyCallExp exp = FACTORY.createPropertyCallExp();
				exp.setIsImplicit(true);
				exp.setOwnedSource(createVariableExp(iterVar));
				exp.setReferredProperty(feature);
				EClassifier featureType = feature.getEType();
				if (featureType != null) {
					if (feature.isMany()) {
						exp.setType(createCollectionTypeForFeature(feature));
					} else {
						exp.setType(createClassifierType(featureType));
					}
				}
				return exp;
			}
		}
		if (contextType instanceof EClass eClass) {
			EStructuralFeature feature = eClass.getEStructuralFeature(name);
			if (feature != null) {
				PropertyCallExp exp = FACTORY.createPropertyCallExp();
				exp.setIsImplicit(true);
				exp.setOwnedSource(createVariableExp(environment.lookup("self").orElseThrow()));
				exp.setReferredProperty(feature);
				EClassifier featureType = feature.getEType();
				if (featureType != null) {
					if (feature.isMany()) {
						exp.setType(createCollectionTypeForFeature(feature));
					} else {
						exp.setType(createClassifierType(featureType));
					}
				}
				return exp;
			}
		}
		if (contextType instanceof EClass contextClass) {
			for (EClassifier classifier : contextClass.getEPackage().getEClassifiers()) {
				if (classifier.getName().equals(name)) {
					TypeExp typeExp = FACTORY.createTypeExp();
					typeExp.setReferredType(createClassifierType(classifier));
					return typeExp;
				}
			}
		}
		EClassifier fromRegistry = findInRegistry(name);
		if (fromRegistry != null) {
			TypeExp typeExp = FACTORY.createTypeExp();
			typeExp.setReferredType(createClassifierType(fromRegistry));
			return typeExp;
		}
		Variable extVar = FACTORY.createVariable();
		extVar.setName(name);
		return createVariableExp(extVar);
	}

	public EnumLiteralExp tryResolveEnumLiteral(List<String> segments) {
		String enumName = segments.get(0);
		String literalName = segments.get(1);
		if (contextType instanceof EClass contextClass) {
			EnumLiteralExp result = findEnumLiteralInPackage(
					contextClass.getEPackage(), enumName, literalName);
			if (result != null) {
				return result;
			}
		}
		if (packageRegistry != null) {
			for (Object key : packageRegistry.keySet().toArray()) {
				EPackage pkg = packageRegistry.getEPackage((String) key);
				if (pkg != null) {
					EnumLiteralExp result = findEnumLiteralInPackage(pkg, enumName, literalName);
					if (result != null) {
						return result;
					}
				}
			}
		}
		return null;
	}

	public OclExpression resolveQualifiedName(List<String> segments) {
		EClassifier resolved = resolveClassifier(segments);
		TypeExp exp = FACTORY.createTypeExp();
		exp.setReferredType(createClassifierType(resolved));
		return exp;
	}

	// ==================== Type Utilities ====================

	public OclType copyType(OclType type) {
		if (type instanceof ClassifierType ct) {
			return createClassifierType(ct.getReferredClassifier());
		}
		if (type instanceof CollectionType colType) {
			CollectionType copy = FACTORY.createCollectionType();
			if (colType.getElementType() != null) {
				copy.setElementType(copyType(colType.getElementType()));
			}
			copy.setKind(colType.getKind());
			return copy;
		}
		return null;
	}

	public OclType inferElementType(OclExpression source) {
		if (source == null) {
			return null;
		}
		OclType type = source.getType();
		if (type instanceof CollectionType colType && colType.getElementType() != null) {
			return copyType(colType.getElementType());
		}
		if (type instanceof ClassifierType ct && ct.getReferredClassifier() != null) {
			return createClassifierType(ct.getReferredClassifier());
		}
		return null;
	}

	public EClassifier getSourceClassifier(OclExpression source) {
		if (source == null) {
			return contextType;
		}
		OclType type = source.getType();
		if (type instanceof CollectionType colType
				&& colType.getElementType() instanceof ClassifierType ct) {
			return ct.getReferredClassifier();
		}
		if (type instanceof ClassifierType ct) {
			return ct.getReferredClassifier();
		}
		return null;
	}

	public EStructuralFeature resolveFeatureOnType(OclType type, String featureName) {
		if (type instanceof ClassifierType ct
				&& ct.getReferredClassifier() instanceof EClass eClass) {
			return eClass.getEStructuralFeature(featureName);
		}
		return null;
	}

	// ==================== String / Enum Utilities ====================

	public static CollectionKind resolveCollectionKind(String kindText) {
		return switch (kindText) {
			case "Set" -> CollectionKind.SET;
			case "OrderedSet" -> CollectionKind.ORDERED_SET;
			case "Bag" -> CollectionKind.BAG;
			case "Sequence" -> CollectionKind.SEQUENCE;
			default -> CollectionKind.COLLECTION;
		};
	}

	public static String unescapeString(String s) {
		StringBuilder sb = new StringBuilder(s.length());
		for (int i = 0; i < s.length(); i++) {
			char c = s.charAt(i);
			if (c == '\\' && i + 1 < s.length()) {
				char next = s.charAt(i + 1);
				switch (next) {
					case 'n' -> { sb.append('\n'); i++; }
					case 't' -> { sb.append('\t'); i++; }
					case 'r' -> { sb.append('\r'); i++; }
					case 'f' -> { sb.append('\f'); i++; }
					case 'b' -> { sb.append('\b'); i++; }
					case '\\' -> { sb.append('\\'); i++; }
					case '\'' -> { sb.append('\''); i++; }
					case '"' -> { sb.append('"'); i++; }
					case 'x' -> {
						if (i + 3 < s.length()) {
							int hexVal = Integer.parseInt(s.substring(i + 2, i + 4), 16);
							sb.append((char) hexVal);
							i += 3;
						} else {
							sb.append(c);
						}
					}
					case 'u' -> {
						if (i + 5 < s.length()) {
							int uniVal = Integer.parseInt(s.substring(i + 2, i + 6), 16);
							sb.append((char) uniVal);
							i += 5;
						} else {
							sb.append(c);
						}
					}
					case '0', '1', '2', '3', '4', '5', '6', '7' -> {
						int start = i + 1;
						int end = start + 1;
						while (end < s.length() && end - start < 3
								&& s.charAt(end) >= '0' && s.charAt(end) <= '7') {
							end++;
						}
						int octalVal = Integer.parseInt(s.substring(start, end), 8);
						sb.append((char) octalVal);
						i = end - 1;
					}
					default -> { sb.append(c); }
				}
			} else {
				sb.append(c);
			}
		}
		return sb.toString();
	}

	/**
	 * Extracts the text from an escaped identifier ({@code _'keyword'}).
	 *
	 * @param rawText the raw escaped identifier text including the {@code _'} prefix
	 * @return the unescaped identifier text
	 */
	public static String extractEscapedIdentifier(String rawText) {
		String inner = rawText.substring(2, rawText.length() - 1);
		return inner.replace("''", "'");
	}

	// ==================== Internal Helpers ====================

	private EClassifier findInRegistry(String name) {
		if (packageRegistry == null) {
			return null;
		}
		for (Object key : packageRegistry.keySet().toArray()) {
			EPackage pkg = packageRegistry.getEPackage((String) key);
			if (pkg != null) {
				EClassifier found = pkg.getEClassifier(name);
				if (found != null) {
					return found;
				}
			}
		}
		return null;
	}

	/**
	 * Records a name that resolved nowhere and returns the context type, so that the
	 * remaining visit does not run into a {@code null}. The unit is rejected once the
	 * visit finishes — see {@link #getDiagnostics()}.
	 *
	 * <p>The wording follows what the name turned out to be: if the first segment names
	 * an enumeration, the author wrote a literal that does not exist (OCL v2.4 §9.3.9);
	 * otherwise the name was meant to be a type.
	 */
	private EClassifier unresolvedName(List<String> segments) {
		String name = String.join("::", segments);
		if (segments.size() == 2 && findEnum(segments.get(0)) != null) {
			addError("Unknown enumeration literal (" + name + ")");
		} else {
			addError("Unknown type (" + name + ")");
		}
		return contextType;
	}

	/**
	 * Finds an enumeration by name in the context type's package or in the registry.
	 */
	private EEnum findEnum(String enumName) {
		if (contextType instanceof EClass contextClass) {
			EEnum found = findEnumInPackage(contextClass.getEPackage(), enumName);
			if (found != null) {
				return found;
			}
		}
		if (packageRegistry != null) {
			for (Object key : packageRegistry.keySet().toArray()) {
				EPackage pkg = packageRegistry.getEPackage((String) key);
				if (pkg != null) {
					EEnum found = findEnumInPackage(pkg, enumName);
					if (found != null) {
						return found;
					}
				}
			}
		}
		return null;
	}

	private static EEnum findEnumInPackage(EPackage pkg, String enumName) {
		for (EClassifier classifier : pkg.getEClassifiers()) {
			if (classifier instanceof EEnum eEnum && eEnum.getName().equals(enumName)) {
				return eEnum;
			}
		}
		return null;
	}

	private static EnumLiteralExp findEnumLiteralInPackage(EPackage pkg,
			String enumName, String literalName) {
		for (EClassifier classifier : pkg.getEClassifiers()) {
			if (classifier instanceof EEnum eEnum && eEnum.getName().equals(enumName)) {
				EEnumLiteral literal = eEnum.getEEnumLiteral(literalName);
				if (literal != null) {
					EnumLiteralExp exp = FACTORY.createEnumLiteralExp();
					exp.setReferredLiteral(literal);
					return exp;
				}
			}
		}
		return null;
	}

	// ==================== ArrowCallMarker ====================

	/**
	 * Marker adapter to flag AST nodes created from arrow calls ({@code ->}).
	 * Used by the evaluator to apply implicit {@code oclAsSet()} on null sources (§11.2.3).
	 */
	public static final class ArrowCallMarker extends AdapterImpl {

		public static void mark(EObject target) {
			target.eAdapters().add(new ArrowCallMarker());
		}

		public static boolean isArrowCall(EObject target) {
			return target.eAdapters().stream().anyMatch(ArrowCallMarker.class::isInstance);
		}

		@Override
		public boolean isAdapterForType(Object type) {
			return type == ArrowCallMarker.class;
		}
	}
}
