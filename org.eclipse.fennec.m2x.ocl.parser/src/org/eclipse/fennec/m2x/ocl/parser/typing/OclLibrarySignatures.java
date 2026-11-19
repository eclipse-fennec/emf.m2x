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
package org.eclipse.fennec.m2x.ocl.parser.typing;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Consumer;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.MapType;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.model.ocl.TuplePart;
import org.eclipse.fennec.m2x.model.ocl.TupleType;
import org.eclipse.fennec.m2x.model.ocl.TypeExp;
import org.eclipse.fennec.m2x.model.ocl.VoidType;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * The signatures of the standard library operations (OCL v2.4 §11), and of what a language adds
 * to it, written in a small generic notation so that the result type follows from the signature
 * rather than from code per operation — the way Eclipse OCL Pivot's {@code oclstdlib} declares
 * them, not the opcode switch of Eclipse OCL classic.
 *
 * <p>The notation: a library type ({@code Integer}, {@code OclAny}, …); {@code T} the receiver's
 * element type (a map's value type), {@code K} a map's key type; {@code Self} the receiver's
 * type; {@code ArgN} the type of the N-th argument, {@code ArgElemN} its element type,
 * {@code TypeArgN} the type an argument names ({@code oclAsType(Person)}); a collection type
 * {@code Set(P)}, {@code OrderedSet(P)}, {@code Bag(P)}, {@code Sequence(P)}, {@code Collection(P)},
 * or {@code SelfKind(P)} for the receiver's kind; {@code Common(P, Q)} the common supertype;
 * {@code Flat(P)} the element type of a nested collection, flattened; {@code Dict(P, Q)};
 * {@code Tuple(a : P, b : Q)}; {@code Unknown} for a result the signature cannot say anything
 * about — an object's container, say — which is then as unknown as a value of a dynamic model.
 *
 * <p>An operation is looked up along the receiver's chain: a collection's own kind, then
 * {@code Collection}, then {@code OclAny}; {@code UnlimitedNatural}, {@code Integer},
 * {@code Real}, then {@code OclAny}. The first signature whose parameters the arguments conform
 * to wins.
 */
public final class OclLibrarySignatures {

	/** One operation: where it is defined, its name, its parameters and its result. */
	public record Signature(String receiver, String name, List<String> parameters, String result) {

		public Signature {
			Objects.requireNonNull(receiver, "receiver must not be null");
			Objects.requireNonNull(name, "name must not be null");
			parameters = List.copyOf(parameters);
			Objects.requireNonNull(result, "result must not be null");
		}

		@Override
		public String toString() {
			return receiver + "::" + name + "(" + String.join(", ", parameters) + ") : " + result;
		}
	}

	/**
	 * What a lookup found.
	 *
	 * @param type the result type, {@code null} if it is not known
	 * @param signature the signature that matched, {@code null} if none did
	 * @param problem why no signature matched although the receiver is known and has operations
	 *            of that name, {@code null} otherwise
	 * @param argument the index of the first argument that does not fit the closest signature,
	 *            {@code -1} where the problem is the number of arguments or there is none
	 */
	public record Resolution(EClassifier type, Signature signature, String problem, int argument) {

		static final Resolution UNKNOWN = new Resolution(null, null, null, -1);
	}

	private static final OclStandardLibrary LIBRARY = OclStandardLibrary.INSTANCE;

	/** The OCL standard library (§11). */
	public static final OclLibrarySignatures OCL = new OclLibrarySignatures(new LinkedHashMap<>())
			.extend(OclLibrarySignatures::defineOcl);

	private final Map<String, List<Signature>> byReceiverAndName;

	private OclLibrarySignatures(Map<String, List<Signature>> byReceiverAndName) {
		this.byReceiverAndName = byReceiverAndName;
	}

	/**
	 * A library with more operations: what QVT-O (§8.3) or MTL add to OCL's.
	 *
	 * @param definitions adds signatures through the given builder
	 * @return a new library holding this one's signatures and the added ones, added ones after
	 */
	public OclLibrarySignatures extend(Consumer<Builder> definitions) {
		Map<String, List<Signature>> copy = new LinkedHashMap<>();
		byReceiverAndName.forEach((key, list) -> copy.put(key, new ArrayList<>(list)));
		definitions.accept(new Builder(copy));
		return new OclLibrarySignatures(copy);
	}

	/** Adds signatures to a library being extended. */
	public static final class Builder {

		private final Map<String, List<Signature>> target;

		private Builder(Map<String, List<Signature>> target) {
			this.target = target;
		}

		/**
		 * Adds an operation.
		 *
		 * @param receiver the type it is defined on: {@code OclAny}, {@code Boolean},
		 *            {@code Integer}, {@code Real}, {@code UnlimitedNatural}, {@code String},
		 *            {@code Collection}, {@code Set}, {@code OrderedSet}, {@code Bag},
		 *            {@code Sequence}, {@code Map}, {@code OclVoid}
		 * @param name the operation name
		 * @param result the result type, in the notation of this class
		 * @param parameters the parameter types, in the notation of this class
		 * @return this builder
		 */
		public Builder op(String receiver, String name, String result, String... parameters) {
			// parsed once here, so that a typo in a signature fails when the library is built
			TypePattern.parse(result);
			for (String parameter : parameters) {
				TypePattern.parse(parameter);
			}
			target.computeIfAbsent(receiver + "::" + name, k -> new ArrayList<>())
					.add(new Signature(receiver, name, List.of(parameters), result));
			return this;
		}

		/** Adds the same operation to several receivers. */
		public Builder op(List<String> receivers, String name, String result, String... parameters) {
			receivers.forEach(receiver -> op(receiver, name, result, parameters));
			return this;
		}
	}

	/**
	 * Looks an operation up.
	 *
	 * @param receiverType the static type of the source, {@code null} if unknown
	 * @param name the operation name
	 * @param arguments the arguments, typed as far as known
	 * @return the resolution; its type is {@code null} where the result is not known
	 */
	public Resolution resolve(EClassifier receiverType, String name, List<OclExpression> arguments) {
		EClassifier receiver = OclTypeRules.normalize(receiverType);
		if (receiver == null) {
			return resolveWithoutReceiver(name, arguments);
		}
		List<Signature> sameArity = new ArrayList<>();
		boolean named = false;
		for (String chainLink : chain(receiver)) {
			List<Signature> candidates = byReceiverAndName.get(chainLink + "::" + name);
			if (candidates == null) {
				continue;
			}
			named = true;
			for (Signature signature : candidates) {
				if (signature.parameters().size() != arguments.size()) {
					continue;
				}
				sameArity.add(signature);
				if (accepts(signature, receiver, arguments)) {
					return new Resolution(evaluate(signature.result(), receiver, arguments), signature, null, -1);
				}
			}
		}
		if (!named) {
			return Resolution.UNKNOWN;
		}
		String problem = sameArity.isEmpty()
				? "Operation " + OclTypeRules.nameOf(receiver) + "::" + name + " does not take " + arguments.size()
						+ " argument(s)"
				: "No operation " + OclTypeRules.nameOf(receiver) + "::" + name + "(" + argumentNames(arguments)
						+ "); expected " + sameArity.get(0).parameters().stream()
								.map(p -> p.equals("Type") ? "a type" : OclTypeRules.nameOf(evaluate(p, receiver, arguments)))
								.toList();
		return new Resolution(null, null, problem, sameArity.isEmpty() ? -1 : firstMismatch(sameArity.get(0), receiver, arguments));
	}

	/** The first argument the signature does not take, {@code -1} if it takes them all. */
	private static int firstMismatch(Signature signature, EClassifier receiver, List<OclExpression> arguments) {
		for (int i = 0; i < arguments.size(); i++) {
			String parameter = signature.parameters().get(i);
			boolean fits = parameter.equals("Type")
					? arguments.get(i) instanceof TypeExp || arguments.get(i).getType() == null
					: OclTypeRules.conformsTo(arguments.get(i).getType(), evaluate(parameter, receiver, arguments));
			if (!fits) {
				return i;
			}
		}
		return -1;
	}

	/**
	 * An operation on a source of unknown type: its result is still known where every operation
	 * of that name and arity has the same result, whatever the receiver — {@code size()} is an
	 * {@code Integer}, {@code oclIsUndefined()} a {@code Boolean}.
	 */
	private Resolution resolveWithoutReceiver(String name, List<OclExpression> arguments) {
		EClassifier common = null;
		for (Map.Entry<String, List<Signature>> entry : byReceiverAndName.entrySet()) {
			if (!entry.getKey().endsWith("::" + name)) {
				continue;
			}
			for (Signature signature : entry.getValue()) {
				if (signature.parameters().size() != arguments.size()) {
					continue;
				}
				EClassifier type = OclTypeRules.normalize(evaluate(signature.result(), null, arguments));
				// one answer, also a collection: the same type written by every signature
				if (type == null || (common != null && !OclTypeRules.nameOf(common).equals(OclTypeRules.nameOf(type)))
						|| type instanceof CollectionType c && c.getElementType() == null) {
					return Resolution.UNKNOWN;
				}
				common = type;
			}
		}
		return common == null ? Resolution.UNKNOWN : new Resolution(common, null, null, -1);
	}

	private static String argumentNames(List<OclExpression> arguments) {
		return String.join(", ", arguments.stream().map(a -> OclTypeRules.nameOf(a.getType())).toList());
	}

	private static boolean accepts(Signature signature, EClassifier receiver, List<OclExpression> arguments) {
		for (int i = 0; i < arguments.size(); i++) {
			String parameter = signature.parameters().get(i);
			if (parameter.equals("Type")) {
				if (!(arguments.get(i) instanceof TypeExp) && arguments.get(i).getType() != null) {
					return false;
				}
				continue;
			}
			EClassifier expected = evaluate(parameter, receiver, arguments);
			if (!OclTypeRules.conformsTo(arguments.get(i).getType(), expected)) {
				return false;
			}
		}
		return true;
	}

	/** The receivers an operation is looked up on, most specific first. */
	private static List<String> chain(EClassifier receiver) {
		if (receiver instanceof CollectionType collection) {
			String kind = kindName(collection.getKind());
			return kind.equals("Collection") ? List.of("Collection", "OclAny") : List.of(kind, "Collection", "OclAny");
		}
		if (receiver instanceof PrimitiveType primitive) {
			return switch (primitive.getName()) {
				case "UnlimitedNatural" -> List.of("UnlimitedNatural", "Integer", "Real", "OclAny");
				case "Integer" -> List.of("Integer", "Real", "OclAny");
				case "Real" -> List.of("Real", "OclAny");
				case "String" -> List.of("String", "OclAny");
				case "Boolean" -> List.of("Boolean", "OclAny");
				default -> List.of("OclAny");
			};
		}
		if (receiver instanceof MapType) {
			return List.of("Map", "OclAny");
		}
		if (receiver instanceof VoidType) {
			return List.of("OclVoid", "OclAny");
		}
		return List.of("OclAny");
	}

	static String kindName(CollectionKind kind) {
		return switch (kind) {
			case SET -> "Set";
			case ORDERED_SET -> "OrderedSet";
			case BAG -> "Bag";
			case SEQUENCE -> "Sequence";
			default -> "Collection";
		};
	}

	/**
	 * Evaluates a type pattern.
	 *
	 * @param pattern the pattern, in the notation of this class
	 * @param receiver the receiver's type, {@code null} if unknown
	 * @param arguments the arguments
	 * @return the type, {@code null} where it depends on something unknown
	 */
	public static EClassifier evaluate(String pattern, EClassifier receiver, List<OclExpression> arguments) {
		return TypePattern.parse(pattern).evaluate(receiver, arguments);
	}

	// ==================== The notation ====================

	/** A parsed type pattern. */
	private record TypePattern(String name, List<TypePattern> arguments, List<String> partNames) {

		private static final Map<String, TypePattern> CACHE = new ConcurrentHashMap<>();

		static TypePattern parse(String text) {
			return CACHE.computeIfAbsent(text, t -> {
				int[] at = { 0 };
				TypePattern pattern = parse(t, at);
				skipBlanks(t, at);
				if (at[0] != t.length()) {
					throw new IllegalArgumentException("Bad type pattern: " + t);
				}
				return pattern;
			});
		}

		private static TypePattern parse(String text, int[] at) {
			skipBlanks(text, at);
			int start = at[0];
			while (at[0] < text.length() && Character.isLetterOrDigit(text.charAt(at[0]))) {
				at[0]++;
			}
			String name = text.substring(start, at[0]);
			if (name.isEmpty()) {
				throw new IllegalArgumentException("Bad type pattern: " + text);
			}
			skipBlanks(text, at);
			List<TypePattern> arguments = new ArrayList<>();
			List<String> partNames = new ArrayList<>();
			if (at[0] < text.length() && text.charAt(at[0]) == '(') {
				at[0]++;
				do {
					skipBlanks(text, at);
					if (name.equals("Tuple")) {
						int partStart = at[0];
						while (Character.isLetterOrDigit(text.charAt(at[0]))) {
							at[0]++;
						}
						partNames.add(text.substring(partStart, at[0]));
						skipBlanks(text, at);
						expect(text, at, ':');
					}
					arguments.add(parse(text, at));
					skipBlanks(text, at);
				} while (at[0] < text.length() && text.charAt(at[0]) == ',' && ++at[0] > 0);
				expect(text, at, ')');
			}
			return new TypePattern(name, List.copyOf(arguments), List.copyOf(partNames));
		}

		private static void expect(String text, int[] at, char c) {
			skipBlanks(text, at);
			if (at[0] >= text.length() || text.charAt(at[0]) != c) {
				throw new IllegalArgumentException("Bad type pattern, '" + c + "' expected: " + text);
			}
			at[0]++;
		}

		private static void skipBlanks(String text, int[] at) {
			while (at[0] < text.length() && text.charAt(at[0]) == ' ') {
				at[0]++;
			}
		}

		EClassifier evaluate(EClassifier receiver, List<OclExpression> args) {
			switch (name) {
				case "Self":
					return receiver;
				case "Unknown":
					return null;
				case "T":
					if (receiver instanceof CollectionType c) {
						return c.getElementType();
					}
					return receiver instanceof MapType m ? m.getValueType() : null;
				case "K":
					return receiver instanceof MapType m ? m.getKeyType() : null;
				case "Common":
					return OclTypeRules.commonSuperType(arguments.get(0).evaluate(receiver, args),
							arguments.get(1).evaluate(receiver, args));
				case "Flat":
					return flat(arguments.get(0).evaluate(receiver, args));
				case "SelfKind":
					return receiver instanceof CollectionType c
							? OclTypeRules.collectionType(c.getKind(), arguments.get(0).evaluate(receiver, args))
							: null;
				case "Set", "OrderedSet", "Bag", "Sequence", "Collection":
					return OclTypeRules.collectionType(kind(name), arguments.get(0).evaluate(receiver, args));
				case "Dict":
					MapType map = OclFactory.eINSTANCE.createMapType();
					map.setKeyType(arguments.get(0).evaluate(receiver, args));
					map.setValueType(arguments.get(1).evaluate(receiver, args));
					return map;
				case "Tuple":
					TupleType tuple = OclFactory.eINSTANCE.createTupleType();
					for (int i = 0; i < arguments.size(); i++) {
						TuplePart part = OclFactory.eINSTANCE.createTuplePart();
						part.setName(partNames.get(i));
						part.setType(arguments.get(i).evaluate(receiver, args));
						tuple.getOwnedParts().add(part);
					}
					return tuple;
				default:
					break;
			}
			if (name.startsWith("TypeArg")) {
				int index = Integer.parseInt(name.substring(7));
				return index < args.size() && args.get(index) instanceof TypeExp typeExp ? typeExp.getReferredType() : null;
			}
			if (name.startsWith("ArgElem")) {
				int index = Integer.parseInt(name.substring(7));
				return index < args.size() && OclTypeRules.normalize(args.get(index).getType()) instanceof CollectionType c
						? c.getElementType() : null;
			}
			if (name.startsWith("Arg")) {
				int index = Integer.parseInt(name.substring(3));
				return index < args.size() ? args.get(index).getType() : null;
			}
			return LIBRARY.type(name).orElseThrow(() -> new IllegalArgumentException("Unknown type in pattern: " + name));
		}

		private static EClassifier flat(EClassifier type) {
			EClassifier t = OclTypeRules.normalize(type);
			while (t instanceof CollectionType c) {
				t = OclTypeRules.normalize(c.getElementType());
			}
			return t;
		}

		private static CollectionKind kind(String name) {
			return switch (name) {
				case "Set" -> CollectionKind.SET;
				case "OrderedSet" -> CollectionKind.ORDERED_SET;
				case "Bag" -> CollectionKind.BAG;
				case "Sequence" -> CollectionKind.SEQUENCE;
				default -> CollectionKind.COLLECTION;
			};
		}
	}

	// ==================== OCL v2.4 §11 ====================

	private static void defineOcl(Builder b) {
		// §11.2.5 OclAny
		b.op("OclAny", "=", "Boolean", "OclAny")
				.op("OclAny", "<>", "Boolean", "OclAny")
				.op("OclAny", "oclIsNew", "Boolean")
				.op("OclAny", "oclIsUndefined", "Boolean")
				.op("OclAny", "oclIsInvalid", "Boolean")
				.op("OclAny", "oclAsType", "TypeArg0", "Type")
				.op("OclAny", "oclIsTypeOf", "Boolean", "Type")
				.op("OclAny", "oclIsKindOf", "Boolean", "Type")
				.op("OclAny", "oclIsInState", "Boolean", "OclAny")
				.op("OclAny", "oclType", "OclAny")
				.op("OclAny", "oclAsSet", "Set(Self)")
				.op("OclAny", "toString", "String")
				.op("OclAny", "oclContainer", "Unknown")
				.op("OclAny", "oclContents", "Set(Unknown)");

		// §11.5.1 Real
		b.op("Real", "+", "Real", "Real")
				.op("Real", "-", "Real", "Real")
				.op("Real", "*", "Real", "Real")
				.op("Real", "-", "Real")
				.op("Real", "/", "Real", "Real")
				.op("Real", "abs", "Real")
				.op("Real", "floor", "Integer")
				.op("Real", "round", "Integer")
				.op("Real", "max", "Real", "Real")
				.op("Real", "min", "Real", "Real")
				.op("Real", "<", "Boolean", "Real")
				.op("Real", ">", "Boolean", "Real")
				.op("Real", "<=", "Boolean", "Real")
				.op("Real", ">=", "Boolean", "Real");

		// §11.5.2 Integer: the Real operations apply through the chain; these are Integer's own
		b.op("Integer", "-", "Integer")
				.op("Integer", "+", "Integer", "Integer")
				.op("Integer", "-", "Integer", "Integer")
				.op("Integer", "*", "Integer", "Integer")
				.op("Integer", "/", "Real", "Integer")
				.op("Integer", "abs", "Integer")
				.op("Integer", "div", "Integer", "Integer")
				.op("Integer", "mod", "Integer", "Integer")
				.op("Integer", "max", "Integer", "Integer")
				.op("Integer", "min", "Integer", "Integer")
				.op("Integer", "toUnlimitedNatural", "UnlimitedNatural");

		// §11.5.5 UnlimitedNatural
		b.op("UnlimitedNatural", "+", "UnlimitedNatural", "UnlimitedNatural")
				.op("UnlimitedNatural", "*", "UnlimitedNatural", "UnlimitedNatural")
				.op("UnlimitedNatural", "/", "Real", "UnlimitedNatural")
				.op("UnlimitedNatural", "div", "UnlimitedNatural", "UnlimitedNatural")
				.op("UnlimitedNatural", "mod", "UnlimitedNatural", "UnlimitedNatural")
				.op("UnlimitedNatural", "max", "UnlimitedNatural", "UnlimitedNatural")
				.op("UnlimitedNatural", "min", "UnlimitedNatural", "UnlimitedNatural")
				.op("UnlimitedNatural", "toInteger", "Integer");

		// §11.5.3 String
		b.op("String", "+", "String", "String")
				.op("String", "size", "Integer")
				.op("String", "concat", "String", "String")
				.op("String", "substring", "String", "Integer", "Integer")
				.op("String", "toInteger", "Integer")
				.op("String", "toReal", "Real")
				.op("String", "toUpperCase", "String")
				.op("String", "toLowerCase", "String")
				.op("String", "indexOf", "Integer", "String")
				.op("String", "equalsIgnoreCase", "Boolean", "String")
				.op("String", "at", "String", "Integer")
				.op("String", "characters", "Sequence(String)")
				.op("String", "toBoolean", "Boolean")
				.op("String", "<", "Boolean", "String")
				.op("String", ">", "Boolean", "String")
				.op("String", "<=", "Boolean", "String")
				.op("String", ">=", "Boolean", "String")
				// OCL 2.5 / Eclipse OCL additions the evaluator implements
				.op("String", "toUpper", "String")
				.op("String", "toLower", "String")
				.op("String", "trim", "String")
				.op("String", "startsWith", "Boolean", "String")
				.op("String", "endsWith", "Boolean", "String")
				.op("String", "lastIndexOf", "Integer", "String")
				.op("String", "matches", "Boolean", "String")
				.op("String", "replaceAll", "String", "String", "String")
				.op("String", "replaceFirst", "String", "String", "String")
				.op("String", "substituteAll", "String", "String", "String")
				.op("String", "substituteFirst", "String", "String", "String")
				.op("String", "tokenize", "Sequence(String)")
				.op("String", "tokenize", "Sequence(String)", "String")
				.op("String", "tokenize", "Sequence(String)", "String", "Boolean");

		// §11.5.4 Boolean
		b.op("Boolean", "or", "Boolean", "Boolean")
				.op("Boolean", "xor", "Boolean", "Boolean")
				.op("Boolean", "and", "Boolean", "Boolean")
				.op("Boolean", "not", "Boolean")
				.op("Boolean", "implies", "Boolean", "Boolean");

		// §11.7.1 Collection
		b.op("Collection", "size", "Integer")
				.op("Collection", "includes", "Boolean", "OclAny")
				.op("Collection", "excludes", "Boolean", "OclAny")
				.op("Collection", "count", "Integer", "OclAny")
				.op("Collection", "includesAll", "Boolean", "Collection(OclAny)")
				.op("Collection", "excludesAll", "Boolean", "Collection(OclAny)")
				.op("Collection", "isEmpty", "Boolean")
				.op("Collection", "notEmpty", "Boolean")
				.op("Collection", "max", "T")
				.op("Collection", "min", "T")
				.op("Collection", "sum", "T")
				.op("Collection", "product", "Set(Tuple(first : T, second : ArgElem0))", "Collection(OclAny)")
				.op("Collection", "asSet", "Set(T)")
				.op("Collection", "asOrderedSet", "OrderedSet(T)")
				.op("Collection", "asSequence", "Sequence(T)")
				.op("Collection", "asBag", "Bag(T)")
				.op("Collection", "flatten", "SelfKind(Flat(T))")
				.op("Collection", "selectByKind", "SelfKind(TypeArg0)", "Type")
				.op("Collection", "selectByType", "SelfKind(TypeArg0)", "Type")
				.op("Collection", "including", "SelfKind(Common(T, Arg0))", "OclAny")
				.op("Collection", "excluding", "Self", "OclAny");

		// §11.7.2 Set
		b.op("Set", "union", "Set(Common(T, ArgElem0))", "Set(OclAny)")
				.op("Set", "union", "Bag(Common(T, ArgElem0))", "Bag(OclAny)")
				.op("Set", "intersection", "Set(T)", "Set(OclAny)")
				.op("Set", "intersection", "Set(T)", "Bag(OclAny)")
				.op("Set", "-", "Set(T)", "Set(OclAny)")
				.op("Set", "symmetricDifference", "Set(Common(T, ArgElem0))", "Set(OclAny)");

		// §11.7.3 OrderedSet
		b.op("OrderedSet", "append", "OrderedSet(Common(T, Arg0))", "OclAny")
				.op("OrderedSet", "prepend", "OrderedSet(Common(T, Arg0))", "OclAny")
				.op("OrderedSet", "insertAt", "OrderedSet(Common(T, Arg1))", "Integer", "OclAny")
				.op("OrderedSet", "subOrderedSet", "OrderedSet(T)", "Integer", "Integer")
				.op("OrderedSet", "at", "T", "Integer")
				.op("OrderedSet", "indexOf", "Integer", "OclAny")
				.op("OrderedSet", "first", "T")
				.op("OrderedSet", "last", "T")
				.op("OrderedSet", "reverse", "OrderedSet(T)")
				.op("OrderedSet", "union", "OrderedSet(Common(T, ArgElem0))", "OrderedSet(OclAny)")
				.op("OrderedSet", "union", "Set(Common(T, ArgElem0))", "Set(OclAny)")
				.op("OrderedSet", "intersection", "OrderedSet(T)", "Collection(OclAny)")
				.op("OrderedSet", "-", "OrderedSet(T)", "Collection(OclAny)");

		// §11.7.4 Bag
		b.op("Bag", "union", "Bag(Common(T, ArgElem0))", "Collection(OclAny)")
				.op("Bag", "intersection", "Bag(T)", "Bag(OclAny)")
				.op("Bag", "intersection", "Set(T)", "Set(OclAny)");

		// §11.7.5 Sequence
		b.op("Sequence", "union", "Sequence(Common(T, ArgElem0))", "Sequence(OclAny)")
				.op("Sequence", "append", "Sequence(Common(T, Arg0))", "OclAny")
				.op("Sequence", "prepend", "Sequence(Common(T, Arg0))", "OclAny")
				.op("Sequence", "insertAt", "Sequence(Common(T, Arg1))", "Integer", "OclAny")
				.op("Sequence", "subSequence", "Sequence(T)", "Integer", "Integer")
				.op("Sequence", "at", "T", "Integer")
				.op("Sequence", "indexOf", "Integer", "OclAny")
				.op("Sequence", "first", "T")
				.op("Sequence", "last", "T")
				.op("Sequence", "reverse", "Sequence(T)");

		// Beyond §11, as the evaluator implements them (OCL 2.5 generalizes union and
		// intersection to every collection): the kind of the result follows the source — a Set
		// stays a Set, a Bag a Bag, a Sequence a Sequence — and a Bag intersected with a unique
		// collection is a Set
		b.op("Set", "union", "Set(Common(T, ArgElem0))", "Collection(OclAny)")
				.op("Set", "intersection", "Set(T)", "Collection(OclAny)")
				.op("Set", "-", "Set(T)", "Collection(OclAny)")
				.op("Bag", "intersection", "Set(T)", "OrderedSet(OclAny)")
				.op("Bag", "intersection", "Bag(T)", "Collection(OclAny)")
				.op("Bag", "-", "Bag(T)", "Collection(OclAny)")
				.op("Sequence", "union", "Sequence(Common(T, ArgElem0))", "Collection(OclAny)")
				.op("Sequence", "intersection", "Sequence(T)", "Collection(OclAny)")
				.op("Sequence", "-", "Sequence(T)", "Collection(OclAny)")
				.op("OrderedSet", "union", "OrderedSet(Common(T, ArgElem0))", "Collection(OclAny)")
				.op("Collection", "symmetricDifference", "Set(Common(T, ArgElem0))", "Collection(OclAny)")
				.op("OrderedSet", "subSequence", "OrderedSet(T)", "Integer", "Integer")
				.op("Sequence", "subOrderedSet", "Sequence(T)", "Integer", "Integer");
	}
}
