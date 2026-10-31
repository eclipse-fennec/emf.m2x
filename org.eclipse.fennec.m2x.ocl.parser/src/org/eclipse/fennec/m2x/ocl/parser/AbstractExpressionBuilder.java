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

import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Deque;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.function.Consumer;
import java.util.function.Predicate;

import org.antlr.v4.runtime.ParserRuleContext;
import org.eclipse.emf.ecore.EAnnotation;
import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EEnumLiteral;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;
import org.eclipse.emf.ecore.EcoreFactory;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.BooleanLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.CallExp;
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
import org.eclipse.fennec.m2x.model.ocl.LetExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.MapLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.MapType;
import org.eclipse.fennec.m2x.model.ocl.NullLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.model.ocl.PropertyCallExp;
import org.eclipse.fennec.m2x.model.ocl.RealLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.TupleLiteralPart;
import org.eclipse.fennec.m2x.model.ocl.TuplePart;
import org.eclipse.fennec.m2x.model.ocl.TupleType;
import org.eclipse.fennec.m2x.model.ocl.TypeExp;
import org.eclipse.fennec.m2x.model.ocl.UnlimitedNaturalLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;
import org.eclipse.fennec.m2x.ocl.api.ParseDiagnostics;
import org.eclipse.fennec.m2x.ocl.parser.typing.OclLibrarySignatures;
import org.eclipse.fennec.m2x.ocl.parser.typing.OclTypeRules;

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

	/**
	 * Which thing a name means, in this scope (#194). The builder keeps what it does with the
	 * answer — reporting a miss, choosing a placeholder, setting a type on an expression.
	 *
	 * <p>The scope it holds is what the unit declares: {@code modeltype} / typed models /
	 * {@code [module m(Ecore)]} / a Complete OCL {@code import}, plus the aliases such a
	 * document introduces. OCL v2.4 §9.3 resolves a simple name in the context namespace and
	 * what is imported into it, not in every package the JVM happens to know — until #158 a
	 * scan of the whole registry did the latter, and which name resolved depended on what else
	 * was registered, including the AST metamodels of the four languages.
	 */
	private final OclNameResolver names;

	private OclEnvironment environment;
	/** Implicit iterators pushed for a body, awaiting {@link #buildImplicitIterator}. */
	private final Deque<Variable> pushedImplicitIterators = new ArrayDeque<>();

	/**
	 * Held, not inherited: this class is a helper beside the visitors, and each of those already
	 * extends the ANTLR visitor generated for its grammar. See {@link ParseDiagnostics}.
	 */
	private final ParseDiagnostics diagnostics = new ParseDiagnostics();

	/** The library operations this language knows: OCL's, and what QVT-O or MTL add (#378). */
	private OclLibrarySignatures signatures = OclLibrarySignatures.OCL;

	/**
	 * What the type checker found wrong (#378): an expression whose types are known and do not
	 * fit. Each is a parse error as well; kept here too, so a caller can tell them apart.
	 */
	private final List<Resource.Diagnostic> typeFindings = new ArrayList<>();

	/**
	 * Creates a new expression builder for the given context type.
	 *
	 * @param contextType the classifier that defines the type of {@code self}
	 * @param packageRegistry optional package registry for classifier resolution
	 */
	public AbstractExpressionBuilder(EClassifier contextType, EPackage.Registry packageRegistry) {
		this.contextType = Objects.requireNonNull(contextType, "contextType must not be null");
		this.packageRegistry = packageRegistry;
		this.names = new OclNameResolver(contextType, packageRegistry);
		Variable selfVar = FACTORY.createVariable();
		selfVar.setName("self");
		selfVar.setType(contextType);
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
		return diagnostics.getDiagnostics();
	}

	/**
	 * Returns what the type checker found: expressions whose types are known and do not fit
	 * (OCL v2.4 §8.3, GAP-3, #378).
	 *
	 * @return the findings, in the order they occurred, never {@code null}
	 */
	public List<Resource.Diagnostic> getTypeFindings() {
		return Collections.unmodifiableList(typeFindings);
	}

	/**
	 * Records a type error at the current position: a parse error, and a type finding.
	 *
	 * @param message the message, phrased for an expression author
	 */
	public void addTypeFinding(String message) {
		typeFindings.add(diagnostics.at(message));
		addError(message);
	}

	/**
	 * Sets the library operations of the language being parsed — OCL's and what the language
	 * adds to them.
	 *
	 * @param signatures the library, must not be {@code null}
	 */
	public void useSignatures(OclLibrarySignatures signatures) {
		this.signatures = Objects.requireNonNull(signatures, "signatures must not be null");
	}

	/**
	 * A feature declared in the unit being parsed: what a Complete OCL {@code def:} adds to a
	 * classifier (OCL v2.4 §12.5), a QVT-O helper or query (§8.2.1.12).
	 *
	 * @param owner the classifier the feature belongs to; {@code null} for an operation of the
	 *            unit itself, which an implicit call reaches from anywhere in it
	 * @param name the feature name
	 * @param parameterNames the parameter names of an operation, {@code null} for an attribute
	 * @param parameterTypes the parameter types of an operation, {@code null} for an attribute
	 * @param minArguments how many arguments a call needs at least — fewer than the parameters
	 *            where some have a default value
	 * @param type the declared type
	 */
	public record Definition(EClassifier owner, String name, List<String> parameterNames,
			List<EClassifier> parameterTypes, int minArguments, EClassifier type) {

		/**
		 * A definition every parameter of which has to be passed.
		 *
		 * @param owner the classifier the feature belongs to
		 * @param name the feature name
		 * @param parameterNames the parameter names, {@code null} for an attribute
		 * @param parameterTypes the parameter types, {@code null} for an attribute
		 * @param type the declared type
		 */
		public Definition(EClassifier owner, String name, List<String> parameterNames,
				List<EClassifier> parameterTypes, EClassifier type) {
			this(owner, name, parameterNames, parameterTypes, parameterTypes == null ? 0 : parameterTypes.size(),
					type);
		}

		boolean isOperation() {
			return parameterTypes != null;
		}

		boolean appliesTo(EClassifier receiver) {
			EClassifier o = OclTypeRules.normalize(owner);
			EClassifier r = OclTypeRules.normalize(receiver);
			return r == o || r instanceof EClass rc && o instanceof EClass oc && oc.isSuperTypeOf(rc);
		}

		boolean takes(int arguments) {
			return arguments >= minArguments && arguments <= parameterTypes.size();
		}
	}

	/** The features the document's {@code def:}s add, known before any expression is built. */
	private final List<Definition> definitions = new ArrayList<>();

	/**
	 * Makes the features of a document's {@code def:}s known, so that a call of a defined
	 * operation has its declared type and its arguments are checked against its parameters
	 * (#324, #378). A document collects them before it builds a single expression.
	 *
	 * @param defined the definitions
	 */
	public void declareDefinitions(Collection<Definition> defined) {
		definitions.addAll(Objects.requireNonNull(defined, "defined must not be null"));
	}

	/** @return the library operations of the language being parsed */
	public OclLibrarySignatures signatures() {
		return signatures;
	}

	/**
	 * Tells this builder where the visitor currently is, so that a problem it finds is reported
	 * at a place rather than at line 0.
	 *
	 * <p>Needed because this class is handed segments and types, not parse trees — see
	 * {@link ParseDiagnostics} for what the cursor can and cannot say.
	 *
	 * @param context the node being entered, {@code null} leaves the position untouched
	 */
	public void positionAt(ParserRuleContext context) {
		if (context != null && context.getStart() != null) {
			diagnostics.positionAt(context.getStart().getLine(),
					context.getStart().getCharPositionInLine());
		}
	}

	/**
	 * Records an error diagnostic.
	 *
	 * @param message the message, phrased for a template or expression author
	 */
	public void addError(String message) {
		if (diagnosticSink != null) {
			diagnosticSink.accept(diagnostics.at(message));
		} else {
			diagnostics.addError(message);
		}
	}

	/** Where errors go instead of this builder's own list, if a host collects them itself. */
	private Consumer<Resource.Diagnostic> diagnosticSink;

	/**
	 * Hands every error this builder finds to the host's own collection instead of keeping it —
	 * for a host that builds its expressions itself and uses this builder for the shared OCL
	 * nodes, as QVT-O does (#378).
	 *
	 * @param sink receives each error, positioned at this builder's cursor
	 */
	public void setDiagnosticSink(Consumer<Resource.Diagnostic> sink) {
		this.diagnosticSink = Objects.requireNonNull(sink, "sink must not be null");
	}

	/**
	 * Tells this builder where the host is, by line and column.
	 *
	 * @param line 1-based line
	 * @param column 0-based column
	 */
	public void positionAt(int line, int column) {
		diagnostics.positionAt(line, column);
	}

	/** Whether a name is a variable in scope, for a host that keeps its own variables. */
	private Predicate<String> variableInScope;

	/**
	 * Lets a host that keeps its own variables say which names are in scope — a name in a type
	 * position that is a variable of the host is left to evaluation (#378).
	 *
	 * @param inScope answers whether a name is a variable in scope
	 */
	public void setVariableLookup(Predicate<String> inScope) {
		this.variableInScope = Objects.requireNonNull(inScope, "inScope must not be null");
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
		names.registerPackageAliases(aliases);
	}

	/**
	 * Adds a package to the scope unqualified names resolve in — the language's declared
	 * metamodels. Idempotent.
	 *
	 * @param ePackage the declared package
	 */
	public void importPackage(EPackage ePackage) {
		names.importPackage(ePackage);
	}

	/**
	 * Returns the packages in scope for unqualified names: the context type's package first,
	 * then the imported ones in declaration order.
	 */
	public List<EPackage> scopePackages() {
		return names.scopePackages();
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
		// The alias denotes self itself, so the expression refers to the variable evaluation
		// binds. A variable of its own name was never bound: every form-[B] invariant evaluated
		// to "Unresolved variable" (#324)
		this.environment = this.environment.alias(alias, environment.lookup("self").orElseThrow());
	}

	/**
	 * Declares a variable the expression may refer to and evaluation will bind: a parameter of
	 * the operation whose body, pre- or postcondition this is, or {@code result} in a
	 * postcondition (OCL v2.4 §12.2.1 {@code parameterVariable}, {@code resultVariable}, #324).
	 * Declared, the name is a variable — not an unresolved name the parser might read as a
	 * property of an implicit source.
	 *
	 * @param name the variable name
	 * @param type its type, {@code null} if unknown
	 */
	public void declareVariable(String name, EClassifier type) {
		this.environment = this.environment.nested(createVariable(name, type, null));
	}

	// ==================== Literal Builders ====================

	public IntegerLiteralExp buildIntegerLiteral(String text) {
		IntegerLiteralExp exp = FACTORY.createIntegerLiteralExp();
		try {
			exp.setIntegerSymbol(Long.parseLong(text));
		} catch (NumberFormatException e) {
			// The literal is held as an ELong; larger Integers are reached by arithmetic or
			// toInteger(), which are unbounded (#322). Said in words, not as Java's message
			addError("Integer literal out of range (64 bit): " + text);
		}
		exp.setType(LIBRARY.integer());
		return exp;
	}

	public RealLiteralExp buildRealLiteral(String text) {
		RealLiteralExp exp = FACTORY.createRealLiteralExp();
		exp.setRealSymbol(Double.parseDouble(text));
		exp.setType(LIBRARY.real());
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
		exp.setType(LIBRARY.string());
		return exp;
	}

	public BooleanLiteralExp buildBooleanLiteral(boolean value) {
		BooleanLiteralExp exp = FACTORY.createBooleanLiteralExp();
		exp.setBooleanSymbol(value);
		exp.setType(LIBRARY.booleanType());
		return exp;
	}

	public NullLiteralExp buildNullLiteral() {
		NullLiteralExp exp = FACTORY.createNullLiteralExp();
		exp.setType(LIBRARY.oclVoid());
		return exp;
	}

	public InvalidLiteralExp buildInvalidLiteral() {
		InvalidLiteralExp exp = FACTORY.createInvalidLiteralExp();
		exp.setType(LIBRARY.oclInvalid());
		return exp;
	}

	public UnlimitedNaturalLiteralExp buildUnlimitedNaturalLiteral() {
		UnlimitedNaturalLiteralExp exp = FACTORY.createUnlimitedNaturalLiteralExp();
		exp.setUnlimitedNaturalSymbol(-1L);
		exp.setType(LIBRARY.unlimitedNatural());
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
		if (segments.size() >= 3) {
			// pkg::Enum::LITERAL — a path-qualified literal (OCL v2.4 §7.5.3). Before #133 the
			// three segments were read as a type name and failed silently as an always-false
			// comparison; now the enumeration is looked up by its qualified name.
			EClassifier qualified = findClassifier(segments.subList(0, segments.size() - 1));
			if (qualified instanceof EEnum eEnum) {
				String literalName = segments.get(segments.size() - 1);
				EEnumLiteral literal = eEnum.getEEnumLiteral(literalName);
				if (literal != null) {
					EnumLiteralExp exp = FACTORY.createEnumLiteralExp();
					exp.setReferredLiteral(literal);
					return exp;
				}
				addError("Unknown enumeration literal (" + String.join("::", segments) + ")");
				TypeExp placeholder = FACTORY.createTypeExp();
				placeholder.setReferredType(eEnum);
				return placeholder;
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
		// A variable without a declared type is used with the type of its initial value. The
		// variable itself keeps none: under specStrict an undeclared let type is an error (#326)
		EClassifier type = variable.getType();
		if (type == null && variable.getOwnedInit() != null) {
			type = copyType(variable.getOwnedInit().getType());
		}
		exp.setType(type);
		return exp;
	}

	// ==================== Implicit Operation Call ====================

	/**
	 * Builds an implicit operation call — an unqualified {@code op(args)}. Its source is the
	 * innermost implicit iterator that has the operation, otherwise {@code self} (OCL v2.4
	 * §9.3.35 [D] {@code lookupImplicitSourceForOperation}, §7.4.7
	 * {@code aCollection->collect(oclAsType(String))}, #320).
	 */
	public OperationCallExp buildImplicitOperationCall(String opName,
			List<OclExpression> args, boolean isPre) {
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setIsImplicit(true);
		exp.setIsPre(isPre);
		Variable source = implicitSourceForOperation(opName);
		exp.setOwnedSource(createVariableExp(source));
		exp.getOwnedArguments().addAll(args);
		resolveOperation(exp, opName);
		return exp;
	}

	/**
	 * Checks the qualifier of {@code source.Type::feature} (OCL v2.4 §9.3.36 [3]/[4], §9.3.35
	 * [I]/[J], §7.5.8): it must name a classifier, and the source must conform to it. Before,
	 * the qualifier was dropped unread, so {@code self.Nonsense::name} evaluated (#325).
	 *
	 * @param source the source expression, whose static type may be unknown
	 * @param qualifier the qualifying path
	 */
	public void checkQualifier(OclExpression source, List<String> qualifier) {
		EClassifier qualifierType = names.findClassifier(qualifier);
		if (qualifierType == null) {
			addError("Unknown type (" + String.join("::", qualifier) + ")");
			return;
		}
		EClassifier sourceType = source.getType();
		if (sourceType instanceof EClass sourceClass && qualifierType instanceof EClass qualifierClass
				&& !qualifierClass.isSuperTypeOf(sourceClass)) {
			addError("The source type " + sourceClass.getName() + " does not conform to "
					+ qualifierClass.getName() + " (" + String.join("::", qualifier) + ")");
		}
	}

	/**
	 * Checks the qualifier of an unqualified-source call {@code Type::op(args)} (§9.3.35 [G]).
	 * Ecore has no static operations, so the only reading Fennec has is a qualified call on
	 * {@code self} (§7.5.8): the qualifier must be a classifier the context class conforms to.
	 * An unknown type or an unrelated classifier is an error instead of a silent call on
	 * {@code self} (#325).
	 *
	 * @param qualifier the qualifying path
	 * @param opName the operation name, for the message
	 */
	public void checkStaticQualifier(List<String> qualifier, String opName) {
		EClassifier qualifierType = names.findClassifier(qualifier);
		String path = String.join("::", qualifier);
		if (qualifierType == null) {
			addError("Unknown type (" + path + ")");
		} else if (!(qualifierType instanceof EClass qualifierClass && contextType instanceof EClass contextClass
				&& qualifierClass.isSuperTypeOf(contextClass))) {
			addError("'" + path + "::" + opName + "' is no static operation: Ecore has none, and "
					+ path + " is not a supertype of the context");
		}
	}

	/** The OclAny operations (§11.3.1), which every type has. */
	private static final Set<String> OCL_ANY_OPERATIONS = Set.of("=", "<>", "oclIsKindOf", "oclIsTypeOf",
			"oclAsType", "oclIsUndefined", "oclIsInvalid", "oclIsNew", "oclIsInState", "oclType", "oclAsSet",
			"toString", "oclLocale");

	/**
	 * The variable an unqualified operation call applies to. A candidate of a known class
	 * qualifies when the class has an operation of that name; an OclAny operation fits every
	 * candidate. A candidate whose type the parser does not know — the iterator over a collection
	 * literal, say — qualifies unless the context class itself has an operation of that name:
	 * the call cannot be decided statically then, and the context class is the one place known
	 * to answer it.
	 */
	private Variable implicitSourceForOperation(String opName) {
		Variable self = environment.lookup("self").orElseThrow();
		boolean selfHasIt = contextType instanceof EClass contextClass && hasOperationNamed(contextClass, opName);
		for (Variable candidate : environment.lookupImplicitSources()) {
			EClassifier type = candidate.getType();
			if (OCL_ANY_OPERATIONS.contains(opName)) {
				return candidate;
			}
			if (type instanceof EClass eClass) {
				if (hasOperationNamed(eClass, opName)) {
					return candidate;
				}
			} else if (!selfHasIt) {
				return candidate;
			}
		}
		return self;
	}

	private static boolean hasOperationNamed(EClass eClass, String opName) {
		for (EOperation op : eClass.getEAllOperations()) {
			if (opName.equals(op.getName())) {
				return true;
			}
		}
		return false;
	}

	// ==================== Collection Literal ====================

	public CollectionLiteralExp buildCollectionLiteral(String kindText,
			List<CollectionLiteralPart> parts) {
		CollectionLiteralExp exp = FACTORY.createCollectionLiteralExp();
		exp.setKind(resolveCollectionKind(kindText));
		// §8.3.7 CollectionLiteralExp [1] / §9.3.10 [1]: "In a literal the collection type may
		// not be Collection" — Collection(T) stays valid as a type (#326)
		if (exp.getKind() == CollectionKind.COLLECTION) {
			addError("'Collection' is abstract and has no literal; use Set, OrderedSet, Bag or Sequence");
		}
		if (parts != null) {
			exp.getOwnedParts().addAll(parts);
		}
		// Typed by its kind, so that an iterator over it has a result type too (#378); the
		// element type where every part has the same one, unknown otherwise (no common
		// supertype computation here — that is the type checker's, GAP-3)
		exp.setType(collectionType(exp.getKind(), commonPartType(exp.getOwnedParts())));
		return exp;
	}

	private EClassifier commonPartType(List<CollectionLiteralPart> parts) {
		List<EClassifier> types = new ArrayList<>();
		for (CollectionLiteralPart part : parts) {
			types.add(part instanceof CollectionItem item && item.getOwnedItem() != null
					? item.getOwnedItem().getType()
					: part instanceof CollectionRange ? LIBRARY.integer() : null);
		}
		// §8.3.7 CollectionLiteralExp: the element type is the common supertype of the parts
		return copyType(OclTypeRules.commonSuperType(types));
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
		// §8.3.7 TupleLiteralExp [2]: part->isUnique(attribute.name) (#326)
		rejectDuplicateNames("tuple part", parts.stream().map(TupleLiteralPart::getName).toList());
		TupleLiteralExp exp = FACTORY.createTupleLiteralExp();
		exp.getOwnedParts().addAll(parts);
		exp.setType(tupleType(parts));
		return exp;
	}

	/** §8.3.7 TupleLiteralExp: a tuple type of the parts' names and types. */
	private TupleType tupleType(List<TupleLiteralPart> parts) {
		TupleType type = FACTORY.createTupleType();
		for (TupleLiteralPart part : parts) {
			TuplePart typePart = FACTORY.createTuplePart();
			typePart.setName(part.getName());
			EClassifier partType = part.getType() != null ? part.getType()
					: part.getOwnedInit() != null ? part.getOwnedInit().getType() : null;
			typePart.setType(copyType(partType));
			type.getOwnedParts().add(typePart);
		}
		return type;
	}

	public TupleLiteralPart buildTupleLiteralPart(String name, EClassifier type,
			OclExpression init) {
		TupleLiteralPart part = FACTORY.createTupleLiteralPart();
		part.setName(name);
		if (type != null) {
			part.setType(type);
			// §8.3.7 TupleLiteralPart [1]: the initial value conforms to the declared type
			checkDeclaredType("tuple part", name, type, init);
		} else if (init != null && init.getType() != null) {
			part.setType(copyType(init.getType()));
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
		exp.setType(mapType(exp.getOwnedParts()));
		return exp;
	}

	/** A map type of the common supertypes of the keys and of the values. */
	private MapType mapType(List<MapLiteralPart> parts) {
		List<EClassifier> keyTypes = new ArrayList<>();
		List<EClassifier> valueTypes = new ArrayList<>();
		for (MapLiteralPart part : parts) {
			keyTypes.add(part.getOwnedKey() != null ? part.getOwnedKey().getType() : null);
			valueTypes.add(part.getOwnedValue() != null ? part.getOwnedValue().getType() : null);
		}
		MapType type = FACTORY.createMapType();
		type.setKeyType(copyType(OclTypeRules.commonSuperType(keyTypes)));
		type.setValueType(copyType(OclTypeRules.commonSuperType(valueTypes)));
		return type;
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
		iterVar.setName(nextImplicitName());
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

		EClassifier bodyType = bodyExp.getType();
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
	public OclExpression buildDotOperationCall(OclExpression source, String opName,
			List<OclExpression> args, boolean isSafe, boolean isPre) {
		// §7.6.2: "collection.propertyname(par1, par2, …)" and
		// "collection->collect(propertyname(par1, par2, …))" are identical (#320)
		if (source.getType() instanceof CollectionType || source instanceof CollectionLiteralExp) {
			return buildImplicitOperationCollect(source, opName, args, isSafe, isPre);
		}
		OperationCallExp exp = FACTORY.createOperationCallExp();
		exp.setOwnedSource(source);
		exp.setIsSafe(isSafe);
		exp.setIsPre(isPre);
		exp.getOwnedArguments().addAll(args);
		resolveOperation(exp, opName);
		return exp;
	}

	/**
	 * Builds the implicit collect of an operation called with a dot on a collection (§7.6.2).
	 * The arguments are evaluated once per element, in the scope of the call.
	 */
	private IteratorExp buildImplicitOperationCollect(OclExpression source, String opName,
			List<OclExpression> args, boolean isSafe, boolean isPre) {
		Variable iterVar = FACTORY.createVariable();
		iterVar.setName(nextImplicitName());
		EClassifier elementType = source.getType() instanceof CollectionType colType
				? colType.getElementType() : null;
		if (elementType != null) {
			iterVar.setType(copyType(elementType));
		}
		OperationCallExp bodyExp = FACTORY.createOperationCallExp();
		bodyExp.setOwnedSource(createVariableExp(iterVar));
		bodyExp.setIsSafe(isSafe);
		bodyExp.setIsPre(isPre);
		bodyExp.getOwnedArguments().addAll(args);
		resolveOperation(bodyExp, opName);

		IteratorExp collectExp = FACTORY.createIteratorExp();
		collectExp.setOwnedSource(source);
		collectExp.setName("collect");
		collectExp.getOwnedIterators().add(iterVar);
		collectExp.setOwnedBody(bodyExp);
		return collectExp;
	}

	/**
	 * A name for a parser-created implicit iterator that differs from every one in scope. At
	 * evaluation variables are found by name, so two nested {@code _implicit} iterators hid the
	 * outer one from the inner body (#320). The outermost keeps the plain name.
	 */
	/**
	 * A name for the iterator variable of an iterate that declares none (§9.3.26, #320).
	 *
	 * @return a name distinct from every implicit iterator in scope
	 */
	public String implicitIteratorName() {
		return nextImplicitName();
	}

	private String nextImplicitName() {
		int depth = environment.implicitIteratorDepth();
		return depth == 0 ? "_implicit" : "_implicit" + depth;
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
		markArrowCall(exp);
		exp.getOwnedIterators().addAll(iterVars);
		exp.setOwnedBody(body);

		EClassifier resultType = iteratorResultType(iterName, source, body);
		if (resultType != null) {
			exp.setType(resultType);
		}
		checkConditionBody(iterName, body);
		return exp;
	}

	/** The iterators whose body is a condition (§11.9). */
	private static final Set<String> BOOLEAN_BODY_ITERATORS = Set.of("select", "reject", "forAll", "exists", "any",
			"one");

	/** Records a condition body known not to be a Boolean (§11.9: "body : Boolean"). */
	private void checkConditionBody(String iterName, OclExpression body) {
		EClassifier bodyType = body != null ? body.getType() : null;
		if (BOOLEAN_BODY_ITERATORS.contains(iterName) && !OclTypeRules.conformsTo(bodyType, LIBRARY.booleanType())) {
			addTypeFinding("The body of " + iterName + " is a " + OclTypeRules.nameOf(bodyType) + ", not a Boolean");
		}
	}

	/**
	 * The type of a predefined iterator's result, OCL v2.4 §11.9: {@code select}/{@code reject}
	 * keep the source's collection kind; {@code sortedBy} and {@code closure} answer an ordered
	 * collection for an ordered source and a unique one for a unique source; {@code collect} a
	 * Bag of the flattened body type (a Sequence for an ordered source), {@code collectNested}
	 * the same without flattening; {@code any} the element type; the quantifiers Boolean. Before,
	 * the selecting iterators were typed with the source's element type and the collecting ones
	 * not at all, so a dot after them navigated the element instead of collecting (#320, #378).
	 *
	 * @return the result type, or {@code null} where the source's collection type is unknown
	 */
	private EClassifier iteratorResultType(String iterName, OclExpression source, OclExpression body) {
		switch (iterName) {
			case "forAll", "exists", "one", "isUnique":
				return LIBRARY.booleanType();
			case "any":
				return inferElementType(source);
			default:
				break;
		}
		if (!(source.getType() instanceof CollectionType sourceType)) {
			return null;
		}
		CollectionKind kind = sourceType.getKind();
		boolean ordered = kind == CollectionKind.SEQUENCE || kind == CollectionKind.ORDERED_SET;
		boolean unique = kind == CollectionKind.SET || kind == CollectionKind.ORDERED_SET;
		EClassifier element = inferElementType(source);
		EClassifier bodyType = body != null ? body.getType() : null;
		return switch (iterName) {
			case "select", "reject" -> collectionType(kind, element);
			case "sortedBy" -> collectionType(unique ? CollectionKind.ORDERED_SET : CollectionKind.SEQUENCE, element);
			case "closure" -> collectionType(ordered ? CollectionKind.ORDERED_SET : CollectionKind.SET, element);
			case "collect" -> collectionType(ordered ? CollectionKind.SEQUENCE : CollectionKind.BAG,
					bodyType instanceof CollectionType nested ? copyType(nested.getElementType()) : copyType(bodyType));
			case "collectNested" -> collectionType(ordered ? CollectionKind.SEQUENCE : CollectionKind.BAG,
					copyType(bodyType));
			default -> null;
		};
	}

	private CollectionType collectionType(CollectionKind kind, EClassifier elementType) {
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
		markArrowCall(exp);
		exp.getOwnedIterators().add(iterVar);
		exp.setOwnedResult(accumulator);
		exp.setOwnedBody(body);
		// §8.3.7 IterateExp [1]: the type of the iterate is the type of the result variable
		if (accumulator != null && accumulator.getType() != null) {
			exp.setType(copyType(accumulator.getType()));
		}
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
		markArrowCall(exp);
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
		markArrowCall(exp);

		EClassifier elementType = inferElementType(source);
		Variable iterVar = pushedImplicitIterators.pollFirst();
		if (iterVar == null) {
			iterVar = FACTORY.createVariable();
			iterVar.setName("_implicit");
			if (elementType != null) {
				iterVar.setType(elementType);
			}
		}
		exp.getOwnedIterators().add(iterVar);
		exp.setOwnedBody(body);

		EClassifier resultType = iteratorResultType(iterName, source, body);
		if (resultType != null) {
			exp.setType(resultType);
		}
		checkConditionBody(iterName, body);
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
		EClassifier elementType = inferElementType(source);
		Variable iterVar = FACTORY.createVariable();
		iterVar.setName(nextImplicitName());
		if (elementType != null) {
			iterVar.setType(elementType);
		}
		this.environment = this.environment.nestedImplicit(iterVar);
		// The iterator the body refers to is the one the expression declares (#320)
		pushedImplicitIterators.addFirst(iterVar);
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
		typeIf(ifExp);
		return ifExp;
	}

	/**
	 * Types an {@code if} from its branches, the nested ones of an elseif chain first (§8.3.7
	 * IfExp: the common supertype of then and else), and records a condition known not to be
	 * a Boolean (IfExp [1]).
	 */
	private void typeIf(IfExp ifExp) {
		if (ifExp.getOwnedElse() instanceof IfExp nested && nested.getType() == null) {
			typeIf(nested);
		}
		EClassifier condition = ifExp.getOwnedCondition() != null ? ifExp.getOwnedCondition().getType() : null;
		if (!isCondition(condition)) {
			addTypeFinding("The condition of an if is a " + OclTypeRules.nameOf(condition) + ", not a Boolean");
		}
		EClassifier thenType = ifExp.getOwnedThen() != null ? ifExp.getOwnedThen().getType() : null;
		EClassifier elseType = ifExp.getOwnedElse() != null ? ifExp.getOwnedElse().getType() : null;
		EClassifier type = OclTypeRules.commonSuperType(thenType, elseType);
		if (type != null) {
			ifExp.setType(copyType(type));
		}
	}

	/**
	 * Types an OCL node a host built itself, by the rules the {@code build…} methods of this class
	 * apply, and reports what is known not to fit (#378). For a host such as QVT-O that constructs
	 * the shared OCL nodes on its own; a node that already has a type keeps it.
	 *
	 * @param node the node, its parts already typed
	 */
	public void typeNode(OclExpression node) {
		switch (node) {
			case IntegerLiteralExp literal -> setIfUntyped(literal, LIBRARY.integer());
			case RealLiteralExp literal -> setIfUntyped(literal, LIBRARY.real());
			case StringLiteralExp literal -> setIfUntyped(literal, LIBRARY.string());
			case BooleanLiteralExp literal -> setIfUntyped(literal, LIBRARY.booleanType());
			case UnlimitedNaturalLiteralExp literal -> setIfUntyped(literal, LIBRARY.unlimitedNatural());
			case NullLiteralExp literal -> setIfUntyped(literal, LIBRARY.oclVoid());
			case InvalidLiteralExp literal -> setIfUntyped(literal, LIBRARY.oclInvalid());
			case EnumLiteralExp literal -> setIfUntyped(literal,
					literal.getReferredLiteral() != null ? literal.getReferredLiteral().getEEnum() : null);
			case IfExp ifExp -> typeIf(ifExp);
			case LetExp letExp -> typeLet(letExp);
			case IterateExp iterate -> {
				if (iterate.getOwnedResult() != null) {
					setIfUntyped(iterate, copyType(iterate.getOwnedResult().getType()));
				}
			}
			case IteratorExp iterator -> {
				setIfUntyped(iterator, iteratorResultType(iterator.getName(), iterator.getOwnedSource(),
						iterator.getOwnedBody()));
				checkConditionBody(iterator.getName(), iterator.getOwnedBody());
			}
			case CollectionLiteralExp literal -> setIfUntyped(literal,
					collectionType(literal.getKind(), commonPartType(literal.getOwnedParts())));
			case TupleLiteralExp literal -> setIfUntyped(literal, tupleType(literal.getOwnedParts()));
			case MapLiteralExp literal -> setIfUntyped(literal, mapType(literal.getOwnedParts()));
			default -> {
				// what this builder does not build, the host types itself
			}
		}
	}

	private void setIfUntyped(OclExpression node, EClassifier type) {
		if (node.getType() == null && type != null) {
			node.setType(type);
		}
	}

	/**
	 * Whether a value of this type may stand as a condition: a Boolean, and where the language
	 * allows implicit conditions also an element or a collection (QVT-O §8.4.5(2)).
	 */
	private boolean isCondition(EClassifier type) {
		if (OclTypeRules.conformsTo(type, LIBRARY.booleanType())) {
			return true;
		}
		EClassifier normalized = OclTypeRules.normalize(type);
		return implicitConditions && (normalized instanceof EClass || normalized instanceof CollectionType);
	}

	/** Whether an operation of a model class may come from a unit this one imports. */
	private boolean openOperationScope;

	/**
	 * Says that the unit imports others, whose operations on model classes this builder does not
	 * know: a call on a model class that matches no library signature is then left to the
	 * evaluator instead of being an error (#378).
	 *
	 * @param open whether operations of imported units may be called
	 */
	public void setOpenOperationScope(boolean open) {
		this.openOperationScope = open;
	}

	/** Whether an element or a collection may stand as a condition (QVT-O §8.4.5(2)). */
	private boolean implicitConditions;

	/**
	 * Allows an element or a collection as a condition — QVT-O compares an element with
	 * {@code null} and a collection by its size (§8.4.5(2), #378).
	 *
	 * @param allowed whether such conditions are allowed
	 */
	public void setImplicitConditions(boolean allowed) {
		this.implicitConditions = allowed;
	}

	/**
	 * Types a {@code let}: the type of its body (§8.3.7 LetExp). The four languages build the
	 * expression themselves and hand it here once it is complete.
	 *
	 * @param letExp the let expression, the outermost of a chain of them
	 */
	public void typeLet(LetExp letExp) {
		if (letExp.getOwnedIn() instanceof LetExp nested && nested.getType() == null) {
			typeLet(nested);
		}
		if (letExp.getOwnedIn() != null && letExp.getOwnedIn().getType() != null) {
			letExp.setType(copyType(letExp.getOwnedIn().getType()));
		}
	}

	/**
	 * Creates a variable with optional type, suitable for let bindings or iterator vars.
	 */
	private void rejectDuplicateNames(String what, List<String> declared) {
		Set<String> seen = new HashSet<>();
		for (String name : declared) {
			if (!seen.add(name)) {
				addError("Duplicate " + what + " '" + name + "'");
			}
		}
	}

	/**
	 * Rejects a variable that hides one already in scope (OCL v2.4 §9.3.41 [1]
	 * {@code env.lookup(varName).oclIsUndefined()}, §9.3.25 [A] for iterator variables). The
	 * spec's {@code lookup} searches the enclosing scopes as well, so shadowing is an error —
	 * deliberately stricter than Eclipse OCL, which only looks at the innermost scope (#326).
	 * The parser's own {@code _implicit} iterators are exempt; they never appear in source.
	 */
	public void rejectShadowing(String what, String name) {
		if (name != null && !name.startsWith("_implicit") && environment.lookup(name).isPresent()) {
			addError("Variable name already used: '" + name + "' (" + what + " hides a variable in scope)");
		}
	}

	/**
	 * Checks that a literal initial value conforms to a declared type (OCL v2.4 §8.3.7
	 * Variable [1], TupleLiteralPart [1], IterateExp [2]). Only literals carry a type the
	 * parser knows for certain; anything else waits for the type checker (GAP-3, #326).
	 */
	public void checkDeclaredType(String what, String name, EClassifier declared, OclExpression init) {
		String literalType = literalTypeName(init);
		if (literalType == null && init != null && !OclTypeRules.conformsTo(init.getType(), declared)) {
			addTypeFinding("The " + what + " '" + name + "' is declared as " + OclTypeRules.nameOf(declared)
					+ " but initialized with a " + OclTypeRules.nameOf(init.getType()));
			return;
		}
		if (literalType == null || declared == null || conformsToDeclared(literalType, declared)) {
			return;
		}
		addError("The " + what + " '" + name + "' is declared as " + declared.getName()
				+ " but initialized with a " + literalType);
	}

	/** The predefined type of a literal, or {@code null} when the expression is no such literal. */
	private static String literalTypeName(OclExpression init) {
		if (init instanceof IntegerLiteralExp) return "Integer";
		if (init instanceof RealLiteralExp) return "Real";
		if (init instanceof StringLiteralExp) return "String";
		if (init instanceof BooleanLiteralExp) return "Boolean";
		if (init instanceof UnlimitedNaturalLiteralExp) return "UnlimitedNatural";
		return null;
	}

	private static boolean conformsToDeclared(String literalType, EClassifier declared) {
		if (declared instanceof AnyType || !(declared instanceof PrimitiveType)) {
			// OclAny takes everything; a collection, tuple or model type never takes a primitive
			// literal, except that an Ecore data type may wrap one — left to the type checker
			return declared instanceof AnyType || !(declared instanceof CollectionType
					|| declared instanceof TupleType || declared instanceof EClass);
		}
		String target = declared.getName();
		return switch (literalType) {
			case "Integer" -> "Integer".equals(target) || "Real".equals(target);
			case "UnlimitedNatural" -> "UnlimitedNatural".equals(target) || "Integer".equals(target)
					|| "Real".equals(target);
			default -> literalType.equals(target);
		};
	}

	/**
	 * Creates the variables of an iterator and checks their names: none may hide a variable in
	 * scope, and no two may be the same (§9.3.25 [A], #326).
	 *
	 * @param names the declared names
	 * @param types the declared types, {@code null} where none was declared
	 * @return the variables, in order
	 */
	public List<Variable> declareIteratorVariables(List<String> names, List<EClassifier> types) {
		List<Variable> variables = new ArrayList<>();
		Set<String> seen = new HashSet<>();
		for (int i = 0; i < names.size(); i++) {
			String name = names.get(i);
			rejectShadowing("iterator variable", name);
			if (!seen.add(name)) {
				addError("Variable name already used: '" + name + "' (iterator variable)");
			}
			variables.add(createVariable(name, types.get(i), null));
		}
		return variables;
	}

	/**
	 * Creates the iterator and result variables of iterate after §9.3.26 and §8.3.7: the names
	 * hide nothing in scope and differ from each other, and a literal initial value conforms
	 * to the declared result type (#326).
	 *
	 * @return the iterator variable and the accumulator, in that order
	 */
	public List<Variable> declareIterateVariables(String iterName, EClassifier iterType, String accName,
			EClassifier accType, OclExpression accInit) {
		rejectShadowing("iterator variable", iterName);
		rejectShadowing("accumulator", accName);
		if (accName.equals(iterName)) {
			addError("Variable name already used: '" + accName + "' (accumulator)");
		}
		checkDeclaredType("accumulator", accName, accType, accInit);
		return List.of(createVariable(iterName, iterType, null), createVariable(accName, accType, accInit));
	}

	/**
	 * Creates a let variable after §9.3.41: the name hides nothing in scope, and a literal
	 * initial value conforms to the declared type (#326).
	 */
	public Variable declareLetVariable(String name, EClassifier type, OclExpression init) {
		rejectShadowing("let variable", name);
		checkDeclaredType("let variable", name, type, init);
		return createVariable(name, type, init);
	}

	public Variable createVariable(String name, EClassifier type, OclExpression init) {
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
	public TypeExp buildTypeExp(EClassifier type) {
		TypeExp exp = FACTORY.createTypeExp();
		exp.setReferredType(type);
		return exp;
	}

	// ==================== Type Resolution ====================

	/**
	 * The predefined type of that name from the {@link OclStandardLibrary} — one {@code Integer}
	 * for the whole language, not one per use site (#154). A name the library does not know is
	 * still created here, so that a typo or a user-defined name keeps its old behaviour.
	 */
	public OclType createPrimitiveType(String name) {
		return LIBRARY.type(name).orElseGet(() -> {
			var type = FACTORY.createPrimitiveType();
			type.setName(name);
			return type;
		});
	}

	/**
	 * Initialized with the first builder, before any name is resolved. Until #154 the OCL
	 * metamodel reached the global registry as a side effect of the evaluator's first primitive
	 * type, so whether a registry scan saw it depended on what had been evaluated before.
	 */
	private static final OclStandardLibrary LIBRARY = OclStandardLibrary.INSTANCE;

	public CollectionType createCollectionTypeForFeature(EStructuralFeature feature) {
		CollectionType colType = FACTORY.createCollectionType();
		colType.setElementType(feature.getEType());
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

	public OclType buildCollectionType(String kindText, EClassifier elementType) {
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

	public OclType buildMapType(EClassifier keyType, EClassifier valueType) {
		var type = FACTORY.createMapType();
		type.setKeyType(keyType);
		type.setValueType(valueType);
		return type;
	}

	public OclType buildTupleType(List<String> partNames, List<EClassifier> partTypes) {
		// §8.2.2 TupleType [2]: part names are unique (#326)
		rejectDuplicateNames("tuple part", partNames);
		var type = FACTORY.createTupleType();
		for (int i = 0; i < partNames.size(); i++) {
			var part = FACTORY.createTuplePart();
			part.setName(partNames.get(i));
			part.setType(partTypes.get(i));
			type.getOwnedParts().add(part);
		}
		return type;
	}

	public EClassifier buildTypeFromPath(List<String> segments) {
		return resolveClassifier(segments);
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
						exp.setType(featureType);
					}
				}
				return;
			}
			// Silent by default, and deliberately so: a property this type does not declare may
			// still be one the evaluator finds — a Complete OCL def: registered by an earlier
			// parse, a library property such as oclLocale, a feature of the runtime type where
			// the static one is EObject (a template over a dynamic model). What the parser always
			// does is mark what it could not resolve, so that everything downstream can tell it
			// from a resolved feature. A caller that parses whole documents and wants the typo
			// reported switches strict resolution on (#153).
			if (strictPropertyResolution && !names.isDefinedProperty(propName)) {
				addError("Unknown property (" + eClass.getName() + "::" + propName + ")");
			}
			// a def: attribute of the type has its declared type (§12.5, #378)
			for (Definition definition : definitions) {
				if (!definition.isOperation() && definition.name().equals(propName) && definition.appliesTo(eClass)
						&& definition.type() != null) {
					exp.setType(copyType(definition.type()));
					break;
				}
			}
		}
		exp.setReferredProperty(unresolvedProperty(propName));
	}

	/**
	 * Source of the annotation that marks a property the parser could not resolve, because the
	 * type of the source was not known when the expression was built. The placeholder lets the
	 * evaluator look the property up by name at runtime — what a template over a dynamic model
	 * needs — and the annotation lets everything else (the fingerprint, a validator, a reader
	 * of a stored unit) tell it from a resolved feature (#153).
	 */
	public static final String UNRESOLVED_PROPERTY_ANNOTATION = "http://www.eclipse.org/fennec/m2x/ocl/unresolved";

	/** Whether a property the source type does not declare is an error — off by default (#153). */
	private boolean strictPropertyResolution;

	/**
	 * The property names a {@code def:} of the document adds. They belong to their context
	 * classifier for every expression of that document, including one written before the
	 * {@code def:} — which is why a document collects them before it builds a single expression.
	 */


	/**
	 * Reports a navigation to a property the source type does not declare, instead of leaving a
	 * marked placeholder for the evaluator to resolve reflectively.
	 *
	 * <p>Off by default, because a property the parser cannot see may be there at evaluation
	 * time: a {@code def:} registered by an earlier parse call, a library property such as
	 * {@code oclLocale}, or a feature of the runtime type where the static one is
	 * {@code EObject}. A caller that hands the parser everything at once — an editor, a
	 * validator — turns it on and gets the typo reported at its position.
	 *
	 * @param strict {@code true} to report unknown properties
	 */
	public void setStrictPropertyResolution(boolean strict) {
		this.strictPropertyResolution = strict;
	}

	/**
	 * Declares the property names a {@code def:} of the same document adds, so that strict
	 * resolution does not report them.
	 *
	 * @param names the defined property names
	 */
	public void declareDefinedProperties(Collection<String> names) {
		this.names.declareDefinedProperties(names);
	}

	/**
	 * A placeholder for a property whose owner type is not known at parse time — marked as such.
	 *
	 * @param propName the property name as written
	 * @return the marked placeholder
	 */
	public static EAttribute unresolvedProperty(String propName) {
		EAttribute placeholder = EcoreFactory.eINSTANCE.createEAttribute();
		placeholder.setName(propName);
		EAnnotation mark = EcoreFactory.eINSTANCE.createEAnnotation();
		mark.setSource(UNRESOLVED_PROPERTY_ANNOTATION);
		placeholder.getEAnnotations().add(mark);
		return placeholder;
	}

	/**
	 * Whether a feature is a parser placeholder for a property that could not be resolved.
	 *
	 * @param feature the feature
	 * @return {@code true} for a placeholder
	 */
	public static boolean isUnresolvedProperty(EStructuralFeature feature) {
		return feature != null && feature.getEAnnotation(UNRESOLVED_PROPERTY_ANNOTATION) != null;
	}

	/**
	 * The operations whose argument is a type rather than a value: OCL v2.4 §13.2 —
	 * "A TypeExp is an expression used to refer to an existing type within an expression. It
	 * is used in particular to pass the reference of the type when invoking the operations
	 * oclIsKindOf, oclIsTypeOf, and oclAsType."
	 */
	private static final Set<String> TYPE_ARGUMENT_OPERATIONS =
			Set.of("oclIsKindOf", "oclIsTypeOf", "oclAsType");

	public void resolveOperation(OperationCallExp exp, String opName) {
		exp.setName(opName);
		rejectUnknownTypeArgument(exp, opName);
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
			if (bestMatch != null && bestMatch.getEParameters().size() != argCount) {
				// §8.3.7 OperationCallExp [2]: "exactly as many arguments as the referred operation
				// has parameters". The class has operations of this name, none of this arity —
				// calling the first one anyway dropped or invented arguments silently (#314)
				addError("Operation " + eClass.getName() + "::" + opName + " expects "
						+ bestMatch.getEParameters().size() + " argument(s), got " + argCount);
				return;
			}
			if (bestMatch != null) {
				exp.setReferredOperation(bestMatch);
			}
		}
		typeOperationCall(exp);
	}

	/**
	 * Types an operation call: a metamodel operation by its declared type, a library operation
	 * by its signature (§11). An arrow on a single object applies to the object as a set
	 * (§7.5.3). A library operation whose arguments are known not to fit any signature of that
	 * name is a finding.
	 */
	private void typeOperationCall(OperationCallExp exp) {
		if (exp.getReferredOperation() instanceof EOperation operation) {
			// §8.3.7 OperationCallExp [1]: each argument conforms to its parameter
			for (int i = 0; i < operation.getEParameters().size() && i < exp.getOwnedArguments().size(); i++) {
				EParameter parameter = operation.getEParameters().get(i);
				EClassifier argument = exp.getOwnedArguments().get(i).getType();
				if (!OclTypeRules.conformsTo(argument, typeOf(parameter))) {
					addTypeFinding("Argument " + (i + 1) + " of " + operation.getName() + " is a "
							+ OclTypeRules.nameOf(argument) + ", but the parameter '" + parameter.getName() + "' is a "
							+ OclTypeRules.nameOf(typeOf(parameter)));
				}
			}
			exp.setType(typeOf(operation));
			return;
		}
		EClassifier receiver = exp.getOwnedSource() != null ? exp.getOwnedSource().getType() : null;
		if (typeDefinedOperationCall(exp, receiver)) {
			return;
		}
		if (exp.isIsArrow() && receiver != null && !(OclTypeRules.normalize(receiver) instanceof CollectionType)
				&& !(OclTypeRules.normalize(receiver) instanceof MapType)) {
			receiver = collectionType(CollectionKind.SET, copyType(receiver));
		}
		OclLibrarySignatures.Resolution resolution = signatures.resolve(receiver, exp.getName(),
				exp.getOwnedArguments());
		if (resolution.type() != null) {
			exp.setType(copyType(resolution.type()));
		}
		boolean mayBeImported = openOperationScope && OclTypeRules.normalize(receiver) instanceof EClass;
		if (resolution.problem() != null && !mayBeImported) {
			addTypeFinding(resolution.problem());
		}
	}

	/**
	 * Types a call of an operation a {@code def:} defines for the receiver's type, and checks its
	 * arguments against the declared parameters (§8.3.7 OperationCallExp [1], #324).
	 *
	 * @return whether a definition of that name and arity applies
	 */
	private boolean typeDefinedOperationCall(OperationCallExp exp, EClassifier receiver) {
		boolean unitLevel = exp.isIsImplicit() || exp.getOwnedSource() == null;
		boolean named = false;
		for (Definition definition : definitions) {
			if (!definition.isOperation() || !definition.name().equals(exp.getName())) {
				continue;
			}
			boolean applies = definition.owner() == null ? unitLevel
					: receiver != null && definition.appliesTo(receiver);
			if (!applies) {
				continue;
			}
			named = true;
			if (!definition.takes(exp.getOwnedArguments().size())) {
				continue;
			}
			for (int i = 0; i < exp.getOwnedArguments().size(); i++) {
				EClassifier argument = exp.getOwnedArguments().get(i).getType();
				EClassifier parameter = definition.parameterTypes().get(i);
				if (!OclTypeRules.conformsTo(argument, parameter)) {
					addTypeFinding("Argument " + (i + 1) + " of " + definition.name() + " is a "
							+ OclTypeRules.nameOf(argument) + ", but the parameter '" + definition.parameterNames().get(i)
							+ "' is a " + OclTypeRules.nameOf(parameter));
				}
			}
			if (definition.type() != null) {
				exp.setType(copyType(definition.type()));
			}
			return true;
		}
		// a feature of the unit of that name, only not of this arity: whether an overload with
		// defaults or of an imported unit takes the call is the evaluator's to find out
		return named;
	}

	/**
	 * The type of a typed element of a metamodel: its type, or a collection of it where it is
	 * many-valued — ordered and unique as declared (§8.3.7 PropertyCallExp, OperationCallExp).
	 *
	 * @param element the property, operation or parameter
	 * @return the type, {@code null} where the element has none
	 */
	public EClassifier typeOf(ETypedElement element) {
		EClassifier type = element.getEType();
		if (type == null || !element.isMany()) {
			return type;
		}
		CollectionKind kind = element.isOrdered()
				? element.isUnique() ? CollectionKind.ORDERED_SET : CollectionKind.SEQUENCE
				: element.isUnique() ? CollectionKind.SET : CollectionKind.BAG;
		return collectionType(kind, type);
	}

	/**
	 * Reports a name that stands in a type position and denotes no type.
	 *
	 * <p>An unqualified name that resolves to nothing becomes an external variable, because
	 * {@link org.eclipse.fennec.m2x.ocl.api.OclContext} can bind variables at evaluation time
	 * and a name unknown at parse time is therefore not necessarily wrong. In the argument of
	 * {@code oclIsKindOf}, {@code oclIsTypeOf} or {@code oclAsType} it <em>is</em> wrong: the
	 * spec has a type there and nothing else, so a name that names no type has no reading left.
	 *
	 * <p>Without this, the mistake survived until evaluation and then reported "Unresolved
	 * variable" — the wrong noun, at the wrong time, and only for a caller who asked for
	 * diagnostics. {@code evaluate} returned {@code false}, which for {@code oclIsKindOf} is
	 * an answer nobody questions. Eclipse OCL rejects an unresolvable name in every position
	 * ({@code AbstractOCLAnalyzer.simpleUndefinedName}); this narrows only the position where
	 * no other reading exists.
	 *
	 * <p>A name that <em>is</em> bound — a let variable, an iterator variable, a parameter —
	 * stays accepted here. Whether a bound value may serve as a type is a question about the
	 * value, and answering it belongs to evaluation, not to name resolution.
	 */
	private void rejectUnknownTypeArgument(OperationCallExp exp, String opName) {
		if (!TYPE_ARGUMENT_OPERATIONS.contains(opName) || exp.getOwnedArguments().size() != 1) {
			return;
		}
		if (!(exp.getOwnedArguments().get(0) instanceof VariableExp varExp)) {
			return;
		}
		Variable referred = varExp.getReferredVariable();
		String name = referred == null ? null : referred.getName();
		if (name != null && environment.lookup(name).isEmpty()
				&& (variableInScope == null || !variableInScope.test(name))) {
			addError("Unknown type (" + name + ")");
		}
	}

	public EClassifier resolveClassifier(List<String> segments) {
		EClassifier found = findClassifier(segments);
		return found != null ? found : unresolvedName(segments);
	}

	/**
	 * Looks a classifier up by its simple or qualified name without reporting a miss — for a
	 * caller that has an alternative reading of the name to try first.
	 *
	 * @param segments the name, simple or {@code pkg::…::Name}
	 * @return the classifier, or {@code null} if nothing in scope has that name
	 */
	public EClassifier findClassifier(List<String> segments) {
		return names.findClassifier(segments);
	}

	/**
	 * Resolves an unqualified name as a property of an implicit source: the implicit iterators
	 * and iterator variables from the innermost scope outward, then {@code self} (OCL v2.4
	 * §9.4.1 [10], §7.8 "the most inner scope", #320).
	 */
	public OclExpression resolveImplicitProperty(String name) {
		for (Variable iterVar : environment.lookupImplicitSources()) {
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
						exp.setType(featureType);
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
						exp.setType(featureType);
					}
				}
				return exp;
			}
		}
		if (contextType instanceof EClass contextClass) {
			for (EClassifier classifier : contextClass.getEPackage().getEClassifiers()) {
				if (classifier.getName().equals(name)) {
					TypeExp typeExp = FACTORY.createTypeExp();
					typeExp.setReferredType(classifier);
					return typeExp;
				}
			}
		}
		EClassifier inScope = names.findInScope(name);
		if (inScope != null) {
			TypeExp typeExp = FACTORY.createTypeExp();
			typeExp.setReferredType(inScope);
			return typeExp;
		}
		Variable extVar = FACTORY.createVariable();
		extVar.setName(name);
		return createVariableExp(extVar);
	}

	public EnumLiteralExp tryResolveEnumLiteral(List<String> segments) {
		EEnumLiteral literal = names.findEnumLiteral(segments.get(0), segments.get(1));
		if (literal == null) {
			return null;
		}
		EnumLiteralExp exp = FACTORY.createEnumLiteralExp();
		exp.setReferredLiteral(literal);
		exp.setType(literal.getEEnum());
		return exp;
	}

	public OclExpression resolveQualifiedName(List<String> segments) {
		EClassifier resolved = resolveClassifier(segments);
		TypeExp exp = FACTORY.createTypeExp();
		exp.setReferredType(resolved);
		return exp;
	}

	// ==================== Type Utilities ====================

	/**
	 * A type for a second owner. Only a collection type is a per-use object that has to be copied;
	 * a metamodel classifier and a standard-library type are shared and are returned as they are
	 * — one {@code Integer}, one {@code Person} (#154, #156).
	 */
	public EClassifier copyType(EClassifier type) {
		if (type instanceof CollectionType colType) {
			CollectionType copy = FACTORY.createCollectionType();
			if (colType.getElementType() != null) {
				copy.setElementType(copyType(colType.getElementType()));
			}
			copy.setKind(colType.getKind());
			return copy;
		}
		return type;
	}

	public EClassifier inferElementType(OclExpression source) {
		if (source == null) {
			return null;
		}
		EClassifier type = source.getType();
		if (type instanceof CollectionType colType) {
			// A collection whose element type is unknown has elements of unknown type — not
			// elements of its own collection type, which turned an iterator over tuples into a
			// collection and t.year into an implicit collect over a tuple (#378)
			return colType.getElementType() != null ? copyType(colType.getElementType()) : null;
		}
		return type;
	}

	/**
	 * The classifier a call on {@code source} is resolved against: the element type of a
	 * collection, otherwise the type itself. Since #156 that is the {@code EClassifier} directly.
	 */
	public EClassifier getSourceClassifier(OclExpression source) {
		if (source == null) {
			return contextType;
		}
		EClassifier type = source.getType();
		if (type instanceof CollectionType colType) {
			return colType.getElementType();
		}
		return type;
	}

	public EStructuralFeature resolveFeatureOnType(EClassifier type, String featureName) {
		if (type instanceof EClass eClass) {
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
		// §9.3.4 [B]: the characters between the quotes are StringChars, with the escapes of a
		// string literal (_'\''); a doubled quote stays accepted for compatibility (#325)
		String inner = rawText.substring(2, rawText.length() - 1);
		return unescapeString(inner.replace("''", "\\'"));
	}

	// ==================== Internal Helpers ====================

	/**
	 * A classifier of that simple name in the scope — the context type's package or a declared
	 * one. Never the whole registry: see {@link #importedPackages}.
	 */

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
	 * Finds an enumeration by name in the scope — the context type's package or a declared one.
	 */
	private EEnum findEnum(String enumName) {
		for (EPackage pkg : scopePackages()) {
			EEnum found = findEnumInPackage(pkg, enumName);
			if (found != null) {
				return found;
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


	/**
	 * Records that a call was written with the arrow operator.
	 *
	 * <p>OCL v2.4 §9.3.35 [B]: an arrow call on a single object is evaluated as if the source
	 * were {@code oclAsSet()}, so this is what {@code x->op()} means where {@code x} is not a
	 * collection — part of the meaning of the expression, and therefore part of the AST.
	 *
	 * <p>It used to be an EMF {@code Adapter}. Adapters are not model content: XMI does not
	 * write them, so a compiled unit lost every arrow on its way through a store and came back
	 * meaning something else — {@code p->size()} answering "Unknown operation: size" where the
	 * freshly parsed unit answered 1 (#171).
	 *
	 * @param call the call to mark, must not be {@code null}
	 */
	protected static void markArrowCall(CallExp call) {
		call.setIsArrow(true);
	}
}
