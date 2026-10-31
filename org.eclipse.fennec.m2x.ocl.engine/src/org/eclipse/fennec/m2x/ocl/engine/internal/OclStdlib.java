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
package org.eclipse.fennec.m2x.ocl.engine.internal;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.Formatter;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.emf.common.util.EList;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.MapType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclFeatureValue;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;
import org.eclipse.fennec.m2x.ocl.api.OclValues;

/**
 * OCL Standard Library operation dispatch.
 *
 * <p>Implements all built-in operations for OclAny, Boolean, Integer, Real,
 * String, Collection, OrderedCollection, and Map as defined in the OCL v2.4
 * specification (Sections 11.2–11.7, 11.9).
 *
 * <p>Dispatch is performed by runtime type of the source value, then by
 * operation name using switch expressions. This is deliberately simple
 * and avoids reflection or map-based lookup for performance.
 *
 * <p>All methods return {@link #NOT_FOUND} to signal "operation not found",
 * which the caller ({@link OclEvaluator}) distinguishes from a valid
 * {@code null} return (OclVoid).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class OclStdlib {

	/** Sentinel for "operation not found" — distinct from null (OclVoid) and OclInvalid. */
	static final Object NOT_FOUND = new Object();

	/**
	 * The answer to a call whose arguments do not fit the operation's signature (OCL v2.4
	 * §8.3.7 OperationCallExp [1]/[2]). The result is {@code invalid}; the message is for the
	 * evaluator to report as an ERROR (#314).
	 */
	record ArgumentError(String message) {
	}

	/** What a parameter of a predefined operation accepts. */
	private enum Param {
		/** Integer: a finite whole number. */
		INTEGER("Integer"),
		/** Real, Integer or UnlimitedNatural — the numbers an arithmetic operation takes. */
		NUMBER("Integer or Real"),
		STRING("String"),
		BOOLEAN("Boolean");

		private final String label;

		Param(String label) {
			this.label = label;
		}

		boolean accepts(Object value) {
			return switch (this) {
				case INTEGER -> OclNumbers.isInteger(value);
				case NUMBER -> value instanceof Number || value instanceof OclUnlimitedNatural;
				case STRING -> value instanceof String;
				case BOOLEAN -> value instanceof Boolean;
			};
		}
	}

	private static final Param[] ONE_NUMBER = { Param.NUMBER };
	private static final Param[] ONE_INTEGER = { Param.INTEGER };
	private static final Param[] ONE_STRING = { Param.STRING };
	private static final Param[] TWO_STRINGS = { Param.STRING, Param.STRING };
	private static final Param[] ONE_BOOLEAN = { Param.BOOLEAN };

	/**
	 * Signatures of the predefined operations of the primitive types (OCL v2.4 §11.5) whose
	 * arguments used to be coerced silently — {@code 1 + 'motorcycle'} was 1 (§7.4.5, #314).
	 * Operations not listed here keep their own checks.
	 */
	private static final Map<String, Map<String, Param[]>> SIGNATURES = Map.of(
			"Integer", Map.ofEntries(
					Map.entry("+", ONE_NUMBER), Map.entry("-", ONE_NUMBER), Map.entry("*", ONE_NUMBER),
					Map.entry("/", ONE_NUMBER), Map.entry("div", ONE_INTEGER), Map.entry("mod", ONE_INTEGER),
					Map.entry("max", ONE_NUMBER), Map.entry("min", ONE_NUMBER),
					Map.entry("<", ONE_NUMBER), Map.entry("<=", ONE_NUMBER),
					Map.entry(">", ONE_NUMBER), Map.entry(">=", ONE_NUMBER)),
			"Real", Map.ofEntries(
					Map.entry("+", ONE_NUMBER), Map.entry("-", ONE_NUMBER), Map.entry("*", ONE_NUMBER),
					Map.entry("/", ONE_NUMBER), Map.entry("max", ONE_NUMBER), Map.entry("min", ONE_NUMBER),
					Map.entry("<", ONE_NUMBER), Map.entry("<=", ONE_NUMBER),
					Map.entry(">", ONE_NUMBER), Map.entry(">=", ONE_NUMBER)),
			"UnlimitedNatural", Map.of(
					"max", ONE_NUMBER, "min", ONE_NUMBER,
					"<", ONE_NUMBER, "<=", ONE_NUMBER, ">", ONE_NUMBER, ">=", ONE_NUMBER),
			"Boolean", Map.of(
					"and", ONE_BOOLEAN, "or", ONE_BOOLEAN, "xor", ONE_BOOLEAN, "implies", ONE_BOOLEAN),
			"String", Map.ofEntries(
					Map.entry("+", ONE_STRING), Map.entry("concat", ONE_STRING),
					Map.entry("substring", new Param[] { Param.INTEGER, Param.INTEGER }),
					Map.entry("indexOf", ONE_STRING), Map.entry("lastIndexOf", ONE_STRING),
					Map.entry("at", ONE_INTEGER), Map.entry("equalsIgnoreCase", ONE_STRING),
					Map.entry("startsWith", ONE_STRING), Map.entry("endsWith", ONE_STRING),
					Map.entry("<", ONE_STRING), Map.entry("<=", ONE_STRING),
					Map.entry(">", ONE_STRING), Map.entry(">=", ONE_STRING),
					Map.entry("matches", ONE_STRING), Map.entry("replaceAll", TWO_STRINGS),
					Map.entry("replaceFirst", TWO_STRINGS), Map.entry("substituteAll", TWO_STRINGS),
					Map.entry("substituteFirst", TWO_STRINGS)));

	private static String primitiveTypeName(Object source) {
		if (source instanceof OclUnlimitedNatural) return "UnlimitedNatural";
		if (OclNumbers.isInteger(source)) return "Integer";
		if (OclNumbers.isReal(source)) return "Real";
		if (source instanceof String) return "String";
		if (source instanceof Boolean) return "Boolean";
		return null;
	}

	/**
	 * Checks a call on a primitive value against {@link #SIGNATURES}: as many arguments as the
	 * operation has parameters, each of a conforming type. Unary minus takes none.
	 *
	 * @return the error, or {@code null} when the call fits or the operation is not listed
	 */
	private static ArgumentError checkSignature(String name, Object source, Object[] args) {
		String typeName = primitiveTypeName(source);
		Param[] params = typeName == null ? null : SIGNATURES.get(typeName).get(name);
		if (params == null || ("-".equals(name) && args.length == 0)) {
			return null;
		}
		String operation = typeName + "::" + name;
		if (args.length != params.length) {
			return new ArgumentError("Operation '" + operation + "' expects " + params.length
					+ " argument(s), got " + args.length);
		}
		for (int i = 0; i < params.length; i++) {
			if (!params[i].accepts(args[i])) {
				return new ArgumentError("Argument " + (i + 1) + " of '" + operation + "' must be "
						+ params[i].label + ", got " + args[i].getClass().getSimpleName());
			}
		}
		return null;
	}

	private OclStdlib() {
	}

	/**
	 * Dispatches a standard library operation.
	 *
	 * @param name the operation name
	 * @param source the evaluated source (may be null for OclVoid)
	 * @param args the evaluated arguments
	 * @param options evaluation options with security limits
	 * @param locale the prevailing OCL locale for locale-sensitive operations
	 * @return the result, or {@link #NOT_FOUND} if the operation is not a stdlib operation
	 */
	static Object dispatch(String name, Object source, Object[] args,
			OclEvaluationOptions options, Locale locale) {
		try {
			ArgumentError mismatch = checkSignature(name, source, args);
			if (mismatch != null) {
				return mismatch;
			}
			return withinLimits(dispatchByType(name, source, args, options, locale), options);
		} catch (ArrayIndexOutOfBoundsException wrongArity) {
			// Operations are resolved by name at evaluation time, so `Set{1}->includes()` or
			// `self.oclIsKindOf()` reach an implementation that indexes args[0]. Before #182 that
			// threw out of evaluate(); an operation called with the wrong number of arguments is
			// simply not applicable, which in OCL is `invalid` (v2.4 §11.2.3).
			return OclInvalid.INSTANCE;
		}
	}

	/**
	 * The size limits, applied to what an operation produced.
	 *
	 * <p>The limits on ranges, products and {@code allInstances} bound what an expression can
	 * <em>create</em>; this bounds what it can <em>grow</em>. {@code acc.concat(acc)} or
	 * {@code acc->union(acc)} in an {@code iterate} doubles on every step, so thirty steps of a
	 * sixty-character expression are a gigabyte, at a depth no limit notices. Operations whose
	 * result can be far larger than the limit before this check runs — {@code replaceAll},
	 * {@code joinfields}, {@code format} — bound themselves while producing it.
	 *
	 * @return the result, or {@code invalid} if it is a string longer than
	 *         {@code maxStringLength} or a collection larger than {@code maxCollectionSize}
	 */
	private static Object withinLimits(Object result, OclEvaluationOptions options) {
		if (result instanceof String s && s.length() > options.maxStringLength()) {
			return OclInvalid.INSTANCE;
		}
		if (result instanceof Collection<?> c && c.size() > options.maxCollectionSize()) {
			return OclInvalid.INSTANCE;
		}
		return result;
	}

	private static Object dispatchByType(String name, Object source, Object[] args,
			OclEvaluationOptions options, Locale locale) {
		// QVT v1.3 §8.3.17: Integer::range(start, end) is called on the type (#344)
		if ("range".equals(name) && args.length == 2 && source instanceof PrimitiveType type
				&& "Integer".equals(type.getName())) {
			return range(args[0], args[1], options);
		}
		// OclAny operations apply to all types (including null/invalid)
		Object result = dispatchOclAny(name, source, args);
		if (result != NOT_FOUND) {
			return result;
		}

		// Type-specific dispatch
		if (source instanceof OclUnlimitedNatural) {
			return dispatchUnlimitedNatural(name, source, args);
		}
		if (source instanceof Boolean b) {
			return dispatchBoolean(name, b, args);
		}
		if (OclNumbers.isInteger(source)) {
			return dispatchInteger(name, (Number) source, args, options);
		}
		if (OclNumbers.isReal(source)) {
			// NaN and the infinities a model's EDouble may hold are no Real values (§11.4.1, #322)
			if (!OclNumbers.isFinite((Number) source)) {
				return OclInvalid.INSTANCE;
			}
			return dispatchReal(name, OclNumbers.bigDecimal((Number) source), args);
		}
		if (source instanceof String s) {
			return dispatchString(name, s, args, options, locale);
		}
		if (source instanceof Map<?, ?> m) {
			return dispatchMap(name, m, args, options);
		}
		if (source instanceof Collection<?> c) {
			// Ordered collections: Sequence (plain List), OrderedSet — NOT Bag
			if (source instanceof OclOrderedSet<?> os) {
				Object ordered = dispatchOrderedCollection(name, os, args);
				if (ordered != NOT_FOUND) return ordered;
			} else if (source instanceof List<?> l && !(source instanceof OclBag<?>)) {
				Object ordered = dispatchOrderedCollection(name, l, args);
				if (ordered != NOT_FOUND) return ordered;
			}
			return dispatchCollection(name, c, args, options, locale);
		}

		return NOT_FOUND;
	}

	// --- OclAny (OCL v2.4 Section 11.2) ---

	private static Object dispatchOclAny(String name, Object source, Object[] args) {
		return switch (name) {
			case "=" -> oclEquals(source, args[0]);
			case "<>" -> {
				Object eq = oclEquals(source, args[0]);
				yield eq == OclInvalid.INSTANCE ? OclInvalid.INSTANCE : !((Boolean) eq);
			}
			case "oclIsUndefined" -> source == null || source == OclInvalid.INSTANCE;
			case "oclIsInvalid" -> source == OclInvalid.INSTANCE;
			case "oclIsKindOf" -> oclIsKindOf(source, args[0]);
			case "oclIsTypeOf" -> oclIsTypeOf(source, args[0]);
			case "oclAsType" -> oclAsType(source, args[0]);
			case "oclAsSet" -> {
				// §11.3.3: invalid.oclAsSet() → invalid
				if (source == OclInvalid.INSTANCE) {
					yield OclInvalid.INSTANCE;
				}
				// §11.3.1: value.oclAsSet() → Set{value} (value is the sole element)
				// §11.3.2: null.oclAsSet() → Set{}
				// Collections are NOT flattened — Set{1,2}.oclAsSet() → Set{Set{1,2}}
				Set<Object> set = new OclSet<>();
				if (source != null) {
					set.add(source);
				}
				yield set;
			}
			case "oclType" -> oclType(source);
			case "oclContainer" -> oclContainer(source);
			case "oclContents" -> oclContents(source);
			// §11.2.4: toString is not among the operations defined on invalid (#321)
			case "toString" -> source == null || source == OclInvalid.INSTANCE
					? OclInvalid.INSTANCE : OclValues.toText(source);
			// QVT-O §8.3.3.1: Object::repr() — textual representation of any object
			case "repr" -> OclValues.repr(source);
			// §8.4.4: stereotypedBy — UML-specific, not supported (P10-07)
			case "stereotypedBy" -> OclInvalid.INSTANCE;
			default -> NOT_FOUND;
		};
	}

	// The predefined types come from the standard library, so oclType() = oclType() holds and
	// the value the evaluator reports is the very object the parser referenced (#154).
	private static final OclStandardLibrary LIBRARY = OclStandardLibrary.INSTANCE;

	private static Object oclType(Object source) {
		// OCL v2.5 §11.2.1: null.oclType() = OclVoid, invalid.oclType() = OclInvalid
		if (source == null) return LIBRARY.oclVoid();
		if (source == OclInvalid.INSTANCE) return LIBRARY.oclInvalid();
		if (source instanceof OclUnlimitedNatural) return LIBRARY.unlimitedNatural();
		if (source instanceof EObject eo) return eo.eClass();
		if (OclNumbers.isInteger(source)) return LIBRARY.integer();
		if (OclNumbers.isReal(source)) return LIBRARY.real();
		if (source instanceof String) return LIBRARY.string();
		if (source instanceof Boolean) return LIBRARY.booleanType();
		if (source instanceof OclOrderedSet<?>) return createCollectionType(CollectionKind.ORDERED_SET);
		if (source instanceof OclBag<?>) return createCollectionType(CollectionKind.BAG);
		if (source instanceof Set<?>) return createCollectionType(CollectionKind.SET);
		if (source instanceof List<?>) return createCollectionType(CollectionKind.SEQUENCE);
		if (source instanceof Collection<?>) return createCollectionType(CollectionKind.COLLECTION);
		if (source instanceof Map<?, ?>) {
			return OclFactory.eINSTANCE.createMapType();
		}
		return OclInvalid.INSTANCE;
	}

	private static CollectionType createCollectionType(CollectionKind kind) {
		CollectionType ct = OclFactory.eINSTANCE.createCollectionType();
		ct.setKind(kind);
		// The type is named after its kind, as the parser names Set(T) (#321)
		ct.setName(kind.getName());
		return ct;
	}

	/**
	 * OCL §11.3.1 oclContainer() : OclElement[?]
	 *
	 * <p>Returns the object for which self is a composed content,
	 * or null if there is no such object (root element).
	 * For non-EObject values, returns OclInvalid.
	 */
	private static Object oclContainer(Object source) {
		if (source == null || source == OclInvalid.INSTANCE) {
			return OclInvalid.INSTANCE;
		}
		if (source instanceof EObject eo) {
			return eo.eContainer(); // null for root elements
		}
		return OclInvalid.INSTANCE;
	}

	/**
	 * OCL §11.3.1 oclContents() : Set(OclElement)
	 *
	 * <p>Returns the composed contents of self as a Set.
	 * For non-EObject values, returns OclInvalid.
	 */
	private static Object oclContents(Object source) {
		if (source == null || source == OclInvalid.INSTANCE) {
			return OclInvalid.INSTANCE;
		}
		if (source instanceof EObject eo) {
			return new OclSet<>(eo.eContents());
		}
		return OclInvalid.INSTANCE;
	}

	private static Object oclEquals(Object left, Object right) {
		// OCL v2.5: any operation on invalid (except oclIsInvalid/oclIsUndefined) yields invalid
		if (left == OclInvalid.INSTANCE || right == OclInvalid.INSTANCE) {
			return OclInvalid.INSTANCE;
		}
		// UnlimitedNatural equality: * = * is true, * = anything else is false
		if (left instanceof OclUnlimitedNatural || right instanceof OclUnlimitedNatural) {
			return left instanceof OclUnlimitedNatural && right instanceof OclUnlimitedNatural;
		}
		// Collection equality — kind must match
		if (left instanceof Collection<?> || right instanceof Collection<?>) {
			return collectionEquals(left, right);
		}
		// Tuple (Map) equality — recursive OCL equality on values
		if (left instanceof Map<?, ?> lm && right instanceof Map<?, ?> rm) {
			if (!lm.keySet().equals(rm.keySet())) return false;
			for (var entry : lm.entrySet()) {
				if (!Boolean.TRUE.equals(oclEquals(entry.getValue(), rm.get(entry.getKey())))) return false;
			}
			return true;
		}
		// OclType equality by name (so cached singletons and fresh instances compare equal)
		if (left instanceof OclType lt && right instanceof OclType rt) {
			return Objects.equals(lt.getName(), rt.getName());
		}
		// OCL v2.4 §11.3.1: "True if self is the same object as object2" — object values are
		// identities (Annex A.2.4), not structures; OclSet already hashes them by identity (#318)
		if (left instanceof EObject le && right instanceof EObject re) {
			return le == re;
		}
		// Numeric cross-type equality: 5.0 = 5 → true (OCL v2.4 §11.5.1), exact beyond 2^53 (#317)
		if (left instanceof Number ln && right instanceof Number rn) {
			return OclNumbers.equal(ln, rn);
		}
		return Objects.equals(left, right);
	}

	/**
	 * Collection equality per OCL spec: collections of different kinds are never equal.
	 * Set: unordered, unique (element equality). Bag: unordered, non-unique (frequency equality).
	 * Sequence: ordered, non-unique (positional equality). OrderedSet: ordered, unique (positional equality).
	 */
	private static Boolean collectionEquals(Object left, Object right) {
		if (!(left instanceof Collection<?> lc) || !(right instanceof Collection<?> rc)) {
			return false;
		}
		int lKind = collectionKindTag(lc);
		int rKind = collectionKindTag(rc);
		if (lKind != rKind) {
			return false;
		}
		return switch (lKind) {
			case 0 -> setEquals(lc, rc); // SET: unordered, unique — OCL element equality
			case 1 -> elementWiseEquals(lc, rc); // ORDERED_SET: order-sensitive
			case 2 -> elementWiseEquals(lc, rc); // SEQUENCE: order-sensitive
			case 3 -> bagEquals(lc, rc); // BAG: frequency-based
			default -> false;
		};
	}

	/** Returns 0=SET, 1=ORDERED_SET, 2=SEQUENCE, 3=BAG. */
	private static int collectionKindTag(Collection<?> c) {
		if (c instanceof OclOrderedSet<?>) return 1;
		if (c instanceof OclBag<?>) return 3;
		if (c instanceof Set<?>) return 0;
		return 2; // List → Sequence
	}

	/** Set equality: same size and each element in left has an OCL-equal match in right. */
	private static boolean setEquals(Collection<?> left, Collection<?> right) {
		if (left.size() != right.size()) return false;
		for (Object e : left) {
			boolean found = false;
			for (Object r : right) {
				if (Boolean.TRUE.equals(oclEquals(e, r))) { found = true; break; }
			}
			if (!found) return false;
		}
		return true;
	}

	/** Order-sensitive element comparison using OCL equality. */
	private static boolean elementWiseEquals(Collection<?> left, Collection<?> right) {
		if (left.size() != right.size()) return false;
		var li = left.iterator();
		var ri = right.iterator();
		while (li.hasNext()) {
			if (!Boolean.TRUE.equals(oclEquals(li.next(), ri.next()))) return false;
		}
		return true;
	}

	/** Frequency-based comparison for Bags (order-insensitive). */
	private static boolean bagEquals(Collection<?> left, Collection<?> right) {
		if (left.size() != right.size()) return false;
		// Count frequencies in left, then verify against right
		List<Object> remaining = new ArrayList<>(right);
		for (Object e : left) {
			boolean found = false;
			for (int i = 0; i < remaining.size(); i++) {
				if (Boolean.TRUE.equals(oclEquals(e, remaining.get(i)))) {
					remaining.remove(i);
					found = true;
					break;
				}
			}
			if (!found) return false;
		}
		return remaining.isEmpty();
	}

	private static boolean oclContains(Collection<?> col, Object element) {
		return col.stream().anyMatch(e -> Boolean.TRUE.equals(oclEquals(e, element)));
	}

	private static Object oclIsKindOf(Object source, Object typeArg) {
		// Spec §11.3.2/§11.3.3: null.oclIsKindOf(type) → invalid, invalid.oclIsKindOf(type) → invalid
		if (source == OclInvalid.INSTANCE || source == null) {
			return OclInvalid.INSTANCE;
		}
		// Check primitive type match first
		if (typeArg instanceof PrimitiveType pt) {
			return conformsToPrimitive(source, pt.getName());
		}
		// §7.5.13: Set(T) does not conform to Bag(T), and Collection(T1) conforms to
		// Collection(T2) only when T1 conforms to T2 — kind and elements both count (#321)
		if (typeArg instanceof CollectionType ct) {
			return source instanceof Collection<?> coll && collectionKindConforms(coll, ct.getKind())
					&& (ct.getElementType() == null || allElementsConform(coll, ct.getElementType()));
		}
		EClassifier classifier = extractClassifier(typeArg);
		if (classifier == null) {
			// Check for OclAny — everything is a kind of OclAny
			if (typeArg instanceof OclType ot && "OclAny".equals(ot.getName())) {
				return true;
			}
			return false;
		}
		return classifier.isInstance(source);
	}

	private static Object oclIsTypeOf(Object source, Object typeArg) {
		// Spec §11.3.2/§11.3.3: null.oclIsTypeOf(type) → invalid, invalid.oclIsTypeOf(type) → invalid
		if (source == OclInvalid.INSTANCE || source == null) {
			return OclInvalid.INSTANCE;
		}
		// Check primitive type match first
		if (typeArg instanceof PrimitiveType pt) {
			return matchesPrimitiveType(source, pt.getName());
		}
		if (typeArg instanceof CollectionType ct) {
			return collectionIsTypeOf(source, ct);
		}
		EClassifier classifier = extractClassifier(typeArg);
		if (classifier == null) {
			return false;
		}
		if (source instanceof EObject eo) {
			return eo.eClass() == classifier;
		}
		return classifier.isInstance(source) && classifier.getInstanceClass() != null
				&& classifier.getInstanceClass().equals(source.getClass());
	}

	/**
	 * Whether {@code source} is exactly of the predefined type {@code typeName}. A finite
	 * integer is an Integer, never an UnlimitedNatural: UnlimitedNatural conforms to Integer,
	 * not the reverse (§11.4.5), so only {@code *} is of type UnlimitedNatural (#321).
	 */
	static boolean matchesPrimitiveType(Object source, String typeName) {
		return switch (typeName) {
			case "Integer" -> OclNumbers.isInteger(source);
			case "Real" -> OclNumbers.isReal(source);
			case "String" -> source instanceof String;
			case "Boolean" -> source instanceof Boolean;
			case "UnlimitedNatural" -> source instanceof OclUnlimitedNatural;
			case "OclVoid" -> source == null;
			default -> false;
		};
	}

	/**
	 * Whether {@code source} conforms to the predefined type {@code typeName} (§8.2.1, §11.4):
	 * its exact type, or a supertype — Integer conforms to Real, UnlimitedNatural to Integer
	 * and Real, everything to OclAny.
	 */
	static boolean conformsToPrimitive(Object source, String typeName) {
		if ("OclAny".equals(typeName) || matchesPrimitiveType(source, typeName)) {
			return true;
		}
		return switch (typeName) {
			case "Real" -> OclNumbers.isInteger(source) || source instanceof OclUnlimitedNatural;
			case "Integer" -> source instanceof OclUnlimitedNatural;
			default -> false;
		};
	}

	/**
	 * {@code oclIsTypeOf} for a collection type: the exact kind ({@code Collection} is abstract,
	 * no value is of that type) and the element type as the type of the elements. The dynamic
	 * element type is the least common supertype of the elements; it equals {@code T} when all
	 * elements conform to {@code T} and at least one is exactly of type {@code T}, or when the
	 * collection is empty (#321).
	 */
	private static boolean collectionIsTypeOf(Object source, CollectionType type) {
		if (!(source instanceof Collection<?> coll) || type.getKind() == CollectionKind.COLLECTION
				|| !collectionKindConforms(coll, type.getKind())) {
			return false;
		}
		EClassifier elementType = type.getElementType();
		if (elementType == null || coll.isEmpty()) {
			return true;
		}
		if (!allElementsConform(coll, elementType)) {
			return false;
		}
		for (Object element : coll) {
			if (element != null && Boolean.TRUE.equals(oclIsTypeOf(element, elementType))) {
				return true;
			}
		}
		return false;
	}

	private static Object oclAsType(Object source, Object typeArg) {
		// Spec §11.3.2: null.oclAsType(type) → self (null conforms to all types)
		if (source == null) {
			return null;
		}
		// Handle primitive types (PrimitiveType has no EClassifier)
		if (typeArg instanceof PrimitiveType pt) {
			return oclAsPrimitive(source, pt.getName());
		}
		// Handle collection types — check kind and element type conformance
		if (typeArg instanceof CollectionType ct) {
			return oclAsTypeCollection(source, ct);
		}
		// Handle map types
		if (typeArg instanceof MapType) {
			return source instanceof Map<?, ?> ? source : OclInvalid.INSTANCE;
		}
		// Handle AnyType — everything is OclAny
		if (typeArg instanceof AnyType) {
			return source;
		}
		EClassifier classifier = extractClassifier(typeArg);
		if (classifier == null) {
			return OclInvalid.INSTANCE;
		}
		if (classifier.isInstance(source)) {
			return source;
		}
		return OclInvalid.INSTANCE;
	}

	/**
	 * {@code oclAsType} to a predefined type (§11.3.1): the value when it conforms, otherwise
	 * invalid. Integer conforms to Real, so {@code 5.oclAsType(Real)} is the Real 5.0 (#321).
	 * A non-negative Integer is accepted as an UnlimitedNatural, which is how Eclipse OCL
	 * reads a multiplicity value; a negative one is invalid. {@code *} is not an Integer or
	 * Real value (§11.4.5), so its cast to either is invalid.
	 */
	private static Object oclAsPrimitive(Object source, String typeName) {
		if (matchesPrimitiveType(source, typeName) || "OclAny".equals(typeName)) {
			return source;
		}
		if ("Real".equals(typeName) && OclNumbers.isInteger(source)) {
			return OclNumbers.bigDecimal((Number) source);
		}
		if ("UnlimitedNatural".equals(typeName) && OclNumbers.isInteger(source)
				&& OclNumbers.signum((Number) source) >= 0) {
			return source;
		}
		return OclInvalid.INSTANCE;
	}

	/**
	 * Handles {@code oclAsType()} for collection types (OCL v2.4 §11.7).
	 * Checks that (1) the source is a collection, (2) the collection kind matches,
	 * and (3) all elements conform to the target element type.
	 */
	private static Object oclAsTypeCollection(Object source, CollectionType targetType) {
		if (!(source instanceof Collection<?> coll)) {
			return OclInvalid.INSTANCE;
		}
		// Check collection kind conformance
		CollectionKind targetKind = targetType.getKind();
		if (!collectionKindConforms(coll, targetKind)) {
			return OclInvalid.INSTANCE;
		}
		// Check element type conformance (if element type is specified)
		EClassifier elementType = targetType.getElementType();
		if (elementType != null && !allElementsConform(coll, elementType)) {
			return OclInvalid.INSTANCE;
		}
		return source;
	}

	/**
	 * Checks whether the runtime collection kind conforms to the target kind.
	 * {@code COLLECTION} (unqualified) accepts any collection kind.
	 */
	private static boolean collectionKindConforms(Collection<?> coll, CollectionKind targetKind) {
		return switch (targetKind) {
			case COLLECTION -> true; // Collection(T) accepts any collection kind
			case SET -> coll instanceof OclSet<?>;
			case BAG -> coll instanceof OclBag<?>;
			case ORDERED_SET -> coll instanceof OclOrderedSet<?>;
			case SEQUENCE -> coll instanceof List<?> && !(coll instanceof OclBag<?>)
					&& !(coll instanceof OclOrderedSet<?>);
		};
	}

	/**
	 * Checks whether all elements in the collection conform to the given element type.
	 */
	private static boolean allElementsConform(Collection<?> coll, EClassifier elementType) {
		// AnyType: all elements conform
		if (elementType instanceof AnyType) {
			return true;
		}
		// PrimitiveType check
		if (elementType instanceof PrimitiveType pt) {
			String typeName = pt.getName();
			if ("OclAny".equals(typeName)) {
				return true;
			}
			for (Object elem : coll) {
				if (elem != null && !conformsToPrimitive(elem, typeName)) {
					return false;
				}
			}
			return true;
		}
		// Metamodel classifier check via EClassifier.isInstance()
		EClassifier classifier = extractClassifier(elementType);
		if (classifier == null) {
			return true; // Cannot verify — assume conformant
		}
		for (Object elem : coll) {
			if (elem != null && !classifier.isInstance(elem)) {
				return false;
			}
		}
		return true;
	}

	private static EClassifier extractClassifier(Object typeArg) {
		// The OCL types are EClassifiers themselves since #154; a metamodel classifier is any
		// EClassifier that is not one of them (#156)
		if (typeArg instanceof OclType) {
			// Primitive and other predefined types — no metamodel classifier behind them
			return null;
		}
		if (typeArg instanceof EClassifier ec) {
			return ec;
		}
		return null;
	}

	// --- Boolean (OCL v2.4 Section 11.4) ---

	private static Object dispatchBoolean(String name, Boolean source, Object[] args) {
		return switch (name) {
			case "not" -> !source;
			case "and" -> source && asBoolean(args[0]);
			case "or" -> source || asBoolean(args[0]);
			case "xor" -> source ^ asBoolean(args[0]);
			case "implies" -> !source || asBoolean(args[0]);
			case "toString" -> source.toString();
			default -> NOT_FOUND;
		};
	}

	// --- UnlimitedNatural (OCL v2.5 §11.5.5) ---

	private static Object dispatchUnlimitedNatural(String name, Object source, Object[] args) {
		boolean argUnlimited = args.length > 0 && args[0] instanceof OclUnlimitedNatural;
		return switch (name) {
			case "<" -> false; // * is not less than anything
			case "<=" -> argUnlimited;
			case ">" -> !argUnlimited;
			case ">=" -> true;
			case "max" -> argUnlimited ? source : OclUnlimitedNatural.INSTANCE;
			case "min" -> {
				if (argUnlimited) yield OclUnlimitedNatural.INSTANCE;
				yield args[0]; // finite arg is smaller than *
			}
			case "toInteger" -> OclInvalid.INSTANCE;
			case "toString" -> "*";
			// Arithmetic on * is undefined per spec
			case "+", "-", "*", "/", "div", "mod", "abs",
				 "floor", "ceiling", "round", "toReal" -> OclInvalid.INSTANCE;
			default -> NOT_FOUND;
		};
	}

	// --- Integer (OCL v2.4 Section 11.5) ---

	private static Object dispatchInteger(String name, Number source, Object[] args,
			OclEvaluationOptions options) {
		// Guard: if arg is OclUnlimitedNatural, special-case comparison/max/min
		if (args.length > 0 && args[0] instanceof OclUnlimitedNatural) {
			return switch (name) {
				case "<", "<=" -> true; // any finite < *
				case ">", ">=" -> false;
				case "max" -> OclUnlimitedNatural.INSTANCE;
				case "min" -> source;
				case "+", "-", "*", "/", "div", "mod" -> OclInvalid.INSTANCE;
				default -> NOT_FOUND;
			};
		}
		if (args.length > 0 && args[0] instanceof Number n && !OclNumbers.isFinite(n)) {
			return OclInvalid.INSTANCE; // NaN or an infinity from the model (#322)
		}
		// Integer arithmetic is exact: it continues in BigInteger where a long would overflow
		// (§11.4.2, #322); with a Real argument the operation is Real's (Integer conforms to Real)
		return switch (name) {
			case "+" -> args.length == 0 ? source : OclNumbers.add(source, (Number) args[0]);
			case "-" -> args.length == 0 ? OclNumbers.negate(source) : OclNumbers.subtract(source, (Number) args[0]);
			case "*" -> OclNumbers.multiply(source, (Number) args[0]);
			case "/" -> {
				Object quotient = OclNumbers.divide(source, (Number) args[0]);
				yield quotient == null ? OclInvalid.INSTANCE : quotient;
			}
			case "div" -> {
				BigInteger divisor = OclNumbers.bigInteger((Number) args[0]);
				if (divisor.signum() == 0) yield OclInvalid.INSTANCE;
				yield OclNumbers.integer(OclNumbers.bigInteger(source).divide(divisor));
			}
			case "mod" -> {
				BigInteger divisor = OclNumbers.bigInteger((Number) args[0]);
				if (divisor.signum() == 0) yield OclInvalid.INSTANCE;
				yield OclNumbers.integer(OclNumbers.bigInteger(source).remainder(divisor));
			}
			case "abs" -> OclNumbers.abs(source);
			case "max" -> {
				Number arg = (Number) args[0];
				Number larger = OclNumbers.compare(source, arg) >= 0 ? source : arg;
				yield OclNumbers.isReal(arg) ? OclNumbers.bigDecimal(larger) : larger;
			}
			case "min" -> {
				Number arg = (Number) args[0];
				Number smaller = OclNumbers.compare(source, arg) <= 0 ? source : arg;
				yield OclNumbers.isReal(arg) ? OclNumbers.bigDecimal(smaller) : smaller;
			}
			case "<" -> OclNumbers.compare(source, (Number) args[0]) < 0;
			case "<=" -> OclNumbers.compare(source, (Number) args[0]) <= 0;
			case ">" -> OclNumbers.compare(source, (Number) args[0]) > 0;
			case ">=" -> OclNumbers.compare(source, (Number) args[0]) >= 0;
			case "toReal" -> OclNumbers.bigDecimal(source);
			case "toInteger" -> source;
			case "floor" -> source;
			case "ceiling" -> source;
			case "round" -> source;
			case "toString" -> source.toString();
			case "range" -> args.length == 1 ? range(source, args[0], options)
					// source.range(start, end) — source ignored (§8.3.17 static-style)
					: range(args[0], args[1], options);
			default -> NOT_FOUND;
		};
	}

	/**
	 * Whether a collection is a feature of a model object rather than a value an expression built.
	 *
	 * <p>The mutating operations below exist for QVT-O's {@code List} and {@code Dict} (D26), whose
	 * values are ordinary Java collections built during evaluation. Reading a many-valued feature
	 * yields EMF's own list — the live contents of the model object — and calling {@code add} or
	 * {@code clear} on that rewrites the user's model from inside an expression. OCL is
	 * side-effect free (v2.4 §7.1), and a constraint evaluated by the validation delegate is the
	 * worst place to learn otherwise, so the model-owned case yields {@code invalid} (#182).
	 *
	 * @param source the collection an operation was called on
	 * @return {@code true} if it belongs to a model object
	 */
	private static boolean belongsToAModel(Object source) {
		// A navigation yields an OclFeatureValue (#319); an EList still reaches here from a
		// provider or a variable bound by the host
		return source instanceof OclFeatureValue || source instanceof EList<?>;
	}

	/**
	 * {@code Integer::range(start, end)}: the Integers from start to end, an Integer of any size
	 * (#322) as a bound. Bounded like the literal form Sequence{1..n} (OclEvaluator), which this
	 * operation is the spelled-out version of — 1.range(2000000000) used to be an OOM (#182).
	 */
	private static Object range(Object start, Object end, OclEvaluationOptions options) {
		if (!OclNumbers.isInteger(start) || !OclNumbers.isInteger(end)) {
			return OclInvalid.INSTANCE;
		}
		BigInteger first = OclNumbers.bigInteger((Number) start);
		BigInteger last = OclNumbers.bigInteger((Number) end);
		BigInteger size = last.subtract(first).add(BigInteger.ONE);
		if (size.compareTo(BigInteger.valueOf(options.maxCollectionSize())) > 0) {
			return OclInvalid.INSTANCE;
		}
		List<Object> result = new ArrayList<>();
		for (BigInteger i = first; i.compareTo(last) <= 0; i = i.add(BigInteger.ONE)) {
			result.add(OclNumbers.integer(i));
		}
		return result;
	}

	/**
	 * A hole of a QVT-O format string: %(key), Java's argument index (n$ or <), flags, width,
	 * .precision and the conversion — or %%.
	 */
	private static final Pattern FORMAT_HOLE = Pattern.compile(
			"%(?:\\(([^)]*)\\))?(\\d+\\$|<)?([-#+ 0,(]*)(\\d+)?(?:\\.(\\d+))?([a-zA-Z%])");

	/**
	 * QVT v1.3 §8.3.16.1 {@code String::format(value)}: "The value, necessarily a tuple if more
	 * than a hole, … Formatting an object (with %s) implies the invocation of Object::repr() … A
	 * dictionary can be passed … holes have the syntax %(key)s". A single hole takes the value
	 * as it is — a collection too; several holes take the parts of a tuple, or the elements of a
	 * collection, in order. Numbers are written in the evaluation's locale, {@link Locale#ROOT}
	 * without one, not the JVM's. Before #344 the value went to {@link Formatter} as varargs: a
	 * tuple and a Dict were invalid, a collection was spread, and %f wrote "1,500000" under a
	 * German JVM.
	 *
	 * <p>The width field of a hole is an allocation request: {@code '%999999999s'.format('x')}
	 * asks for a gigabyte of padding. {@link Formatter} writes the padding character by
	 * character into its {@link Appendable}, so a sink that refuses to grow past the limit stops
	 * the call before the padding exists (#182).
	 *
	 * @return the formatted string, or {@code invalid} if a hole has no value, the format is
	 *         malformed or the result would exceed the limit
	 */
	private static Object formatQvto(String format, Object value, int maxSize, Locale locale) {
		Matcher hole = FORMAT_HOLE.matcher(format);
		List<Object> positional = null;
		int next = 0;
		Object previous = null;
		BoundedOutput out = new BoundedOutput(maxSize);
		try (Formatter formatter = new Formatter(out, locale != null ? locale : Locale.ROOT)) {
			int copiedUpTo = 0;
			while (hole.find()) {
				out.append(format, copiedUpTo, hole.start());
				copiedUpTo = hole.end();
				char conversion = hole.group(6).charAt(0);
				if (conversion == '%') {
					out.append('%');
					continue;
				}
				Object argument;
				if (hole.group(1) != null) {
					if (!(value instanceof Map<?, ?> dict) || !dict.containsKey(hole.group(1))) {
						return OclInvalid.INSTANCE;
					}
					argument = dict.get(hole.group(1));
				} else if ("<".equals(hole.group(2))) {
					argument = previous;
				} else {
					if (positional == null) {
						positional = holeCount(format) > 1 ? partsOf(value)
								: Collections.singletonList(value);
					}
					int index = hole.group(2) != null
							? Integer.parseInt(hole.group(2).substring(0, hole.group(2).length() - 1)) - 1
							: next++;
					if (index < 0 || index >= positional.size()) {
						return OclInvalid.INSTANCE;
					}
					argument = positional.get(index);
				}
				previous = argument;
				String spec = "%" + hole.group(3) + (hole.group(4) != null ? hole.group(4) : "")
						+ (hole.group(5) != null ? "." + hole.group(5) : "") + conversion;
				formatter.format(spec, formatArgument(conversion, argument));
				formatter.flush();
			}
			out.append(format, copiedUpTo, format.length());
			return out.toString();
		} catch (RuntimeException malformedOrTooLarge) {
			return OclInvalid.INSTANCE;
		}
	}

	/** The number of values the positional holes take: their count, or the highest n$. */
	private static int holeCount(String format) {
		Matcher hole = FORMAT_HOLE.matcher(format);
		int sequential = 0;
		int highest = 0;
		while (hole.find()) {
			if (hole.group(6).charAt(0) == '%' || hole.group(1) != null || "<".equals(hole.group(2))) {
				continue;
			}
			if (hole.group(2) != null) {
				highest = Math.max(highest, Integer.parseInt(hole.group(2).substring(0, hole.group(2).length() - 1)));
			} else {
				sequential++;
			}
		}
		return Math.max(sequential, highest);
	}

	/** The values of several holes: the parts of a tuple or the elements of a collection, in order. */
	private static List<Object> partsOf(Object value) {
		if (value instanceof Map<?, ?> tuple) {
			return new ArrayList<>(tuple.values());
		}
		if (value instanceof Collection<?> collection) {
			return new ArrayList<>(collection);
		}
		return Collections.singletonList(value);
	}

	/** A value as the conversion takes it: %s the repr(), numbers as Java numbers. */
	private static Object formatArgument(char conversion, Object argument) {
		return switch (Character.toLowerCase(conversion)) {
			case 's' -> OclValues.repr(argument);
			case 'd', 'x', 'o' -> OclNumbers.isInteger(argument) ? OclNumbers.bigInteger((Number) argument) : argument;
			case 'f', 'e', 'g' -> argument instanceof Number number ? OclNumbers.bigDecimal(number) : argument;
			default -> argument;
		};
	}

	/**
	 * {@code replaceAll} / {@code replaceFirst} with a bound on the size of the result.
	 *
	 * <p>A replacement can be larger than what it replaces, and a group reference in it
	 * ({@code $0}) repeats the match: {@code 'a'.replaceAll('a', 'aaaaaaaaaa')} in an
	 * {@code iterate} grows tenfold per step. The bound is checked per match before the
	 * replacement is expanded, with the worst case for the expansion — the literal length plus
	 * one match length per {@code $} in the replacement — so no chunk larger than the limit is
	 * ever built.
	 *
	 * @return the result, or {@code invalid} if the pattern is malformed, the replacement refers
	 *         to a group the pattern does not have, or the result would exceed the limit
	 */
	private static Object replaceBounded(String source, String pattern, String replacement,
			boolean all, int maxSize) {
		int references = 0;
		for (int i = 0; i < replacement.length(); i++) {
			if (replacement.charAt(i) == '$') references++;
		}
		try {
			Matcher matcher = Pattern.compile(pattern).matcher(source);
			StringBuilder out = new StringBuilder();
			int copiedUpTo = 0;
			while (matcher.find()) {
				long worstCase = (long) out.length() + (matcher.start() - copiedUpTo)
						+ replacement.length() + (long) references * (matcher.end() - matcher.start());
				if (worstCase > maxSize) {
					return OclInvalid.INSTANCE;
				}
				matcher.appendReplacement(out, replacement);
				copiedUpTo = matcher.end();
				if (!all) {
					break;
				}
			}
			if ((long) out.length() + (source.length() - copiedUpTo) > maxSize) {
				return OclInvalid.INSTANCE;
			}
			matcher.appendTail(out);
			return out.toString();
		} catch (IllegalArgumentException | IndexOutOfBoundsException e) {
			// A malformed pattern (PatternSyntaxException is an IllegalArgumentException) or a
			// replacement naming a group the pattern does not have; String.replaceAll threw both
			return OclInvalid.INSTANCE;
		}
	}

	/** A {@link StringBuilder} sink that refuses to grow past its limit. */
	private static final class BoundedOutput implements Appendable {

		private final StringBuilder out = new StringBuilder();
		private final int limit;

		BoundedOutput(int limit) {
			this.limit = limit;
		}

		@Override
		public Appendable append(CharSequence csq) {
			String text = String.valueOf(csq);
			reserve(text.length());
			out.append(text);
			return this;
		}

		@Override
		public Appendable append(CharSequence csq, int start, int end) {
			reserve(end - start);
			out.append(String.valueOf(csq), start, end);
			return this;
		}

		@Override
		public Appendable append(char c) {
			reserve(1);
			out.append(c);
			return this;
		}

		/**
		 * Unchecked on purpose: {@link Formatter} swallows {@link java.io.IOException} from its
		 * sink and carries on with the next character, which for a huge width is a huge loop.
		 * A runtime exception leaves {@code format()} at once.
		 */
		private void reserve(int more) {
			if ((long) out.length() + more > limit) {
				throw new OutputLimitExceeded();
			}
		}

		@Override
		public String toString() {
			return out.toString();
		}
	}

	/** Thrown by {@link BoundedOutput} when a formatted result would exceed the limit. */
	private static final class OutputLimitExceeded extends RuntimeException {

		private static final long serialVersionUID = 1L;

		OutputLimitExceeded() {
			super("result exceeds the string-length limit", null, false, false);
		}
	}

	// --- Real (OCL v2.4 Section 11.5) ---

	private static Object dispatchReal(String name, BigDecimal source, Object[] args) {
		// Guard: if arg is OclUnlimitedNatural, arithmetic is invalid
		if (args.length > 0 && args[0] instanceof OclUnlimitedNatural) {
			return switch (name) {
				case "<", "<=" -> true; // any finite < *
				case ">", ">=" -> false;
				case "max" -> OclUnlimitedNatural.INSTANCE;
				case "min" -> source;
				case "+", "-", "*", "/", "div", "mod" -> OclInvalid.INSTANCE;
				default -> NOT_FOUND;
			};
		}
		if (args.length > 0 && args[0] instanceof Number n && !OclNumbers.isFinite(n)) {
			return OclInvalid.INSTANCE; // NaN or an infinity from the model (#322)
		}
		// Real is a BigDecimal: 0.1 + 0.2 = 0.3 (§11.4.1, #322)
		return switch (name) {
			case "+" -> args.length == 0 ? source : OclNumbers.add(source, (Number) args[0]);
			case "-" -> args.length == 0 ? OclNumbers.negate(source) : OclNumbers.subtract(source, (Number) args[0]);
			case "*" -> OclNumbers.multiply(source, (Number) args[0]);
			case "/" -> {
				Object quotient = OclNumbers.divide(source, (Number) args[0]);
				yield quotient == null ? OclInvalid.INSTANCE : quotient;
			}
			case "abs" -> OclNumbers.abs(source);
			case "max" -> OclNumbers.compare(source, (Number) args[0]) >= 0 ? source
					: OclNumbers.bigDecimal((Number) args[0]);
			case "min" -> OclNumbers.compare(source, (Number) args[0]) <= 0 ? source
					: OclNumbers.bigDecimal((Number) args[0]);
			case "floor" -> OclNumbers.floor(source);
			case "ceiling" -> OclNumbers.ceiling(source);
			case "round" -> OclNumbers.round(source);
			case "<" -> OclNumbers.compare(source, (Number) args[0]) < 0;
			case "<=" -> OclNumbers.compare(source, (Number) args[0]) <= 0;
			case ">" -> OclNumbers.compare(source, (Number) args[0]) > 0;
			case ">=" -> OclNumbers.compare(source, (Number) args[0]) >= 0;
			case "toInteger" -> OclNumbers.integer(source.toBigInteger());
			case "toReal" -> source;
			case "toString" -> OclNumbers.realToString(source);
			default -> NOT_FOUND;
		};
	}

	// --- String (OCL v2.4 Section 11.7) ---

	private static final Pattern LEADING_OR_TRAILING_WHITE_SPACE =
			Pattern.compile("^\\p{IsWhite_Space}+|\\p{IsWhite_Space}+$");

	private static String stripWhiteSpace(String source) {
		return LEADING_OR_TRAILING_WHITE_SPACE.matcher(source).replaceAll("");
	}

	private static Object dispatchString(String name, String source, Object[] args,
			OclEvaluationOptions options, Locale locale) {
		return switch (name) {
			// §11.4.3: a String is a sequence of characters — Unicode code points, not the UTF-16
			// units Java counts, so '😀'.size() is 1 (#329)
			case "size" -> (long) source.codePointCount(0, source.length());
			case "+" , "concat" -> {
				String other = asString(args[0]);
				// Checked before the allocation, not after: the sum is known
				if ((long) source.length() + other.length() > options.maxStringLength()) {
					yield OclInvalid.INSTANCE;
				}
				yield source + other;
			}
			case "substring" -> {
				int lower = asIndex(args[0]);
				int upper = asIndex(args[1]);
				int size = source.codePointCount(0, source.length());
				// OCL v2.5 §11.5.1: 1 <= lower AND lower <= upper AND upper <= size
				if (lower < 1 || upper > size || lower > upper) {
					yield OclInvalid.INSTANCE;
				}
				yield source.substring(source.offsetByCodePoints(0, lower - 1), source.offsetByCodePoints(0, upper));
			}
			case "toUpperCase", "toUpper" -> source.toUpperCase(locale);
			case "toLowerCase", "toLower" -> source.toLowerCase(locale);
			// Any Unicode white space, not only the characters up to U+0020 (#344)
			case "trim" -> stripWhiteSpace(source);
			case "indexOf" -> {
				// OCL v2.4 §11.5.3: "or zero if s is not a substring of self"
				int idx = source.indexOf(asString(args[0]));
				yield idx < 0 ? 0L : (long) source.codePointCount(0, idx) + 1; // 1-based, 0 if not found
			}
			case "lastIndexOf" -> {
				// Eclipse extension (not in OCL v2.4 spec), consistent with indexOf
				int idx = source.lastIndexOf(asString(args[0]));
				yield idx < 0 ? 0L : (long) source.codePointCount(0, idx) + 1; // 1-based, 0 if not found
			}
			case "substituteAll" -> {
				// Eclipse extension: literal (non-regex) replacement of all occurrences
				yield source.replace(asString(args[0]), asString(args[1]));
			}
			case "substituteFirst" -> {
				// Eclipse extension: literal (non-regex) replacement of first occurrence
				String target = asString(args[0]);
				int idx = source.indexOf(target);
				if (idx < 0) yield OclInvalid.INSTANCE; // target not found
				yield source.substring(0, idx) + asString(args[1]) + source.substring(idx + target.length());
			}
			case "tokenize" -> {
				// Eclipse extension: StringTokenizer-like operation
				String delimiters = args.length > 0 ? asString(args[0]) : " \t\n\r\f";
				boolean returnDelims = args.length > 1 && Boolean.TRUE.equals(args[1]);
				List<String> tokens = new ArrayList<>();
				if (delimiters.isEmpty()) {
					// Empty delimiter: entire string is one token (if non-empty)
					if (!source.isEmpty()) {
						tokens.add(source);
					}
				} else {
					StringTokenizer st = new StringTokenizer(source, delimiters, returnDelims);
					while (st.hasMoreTokens()) {
						tokens.add(st.nextToken());
					}
				}
				yield tokens;
			}
			case "at" -> {
				int idx = asIndex(args[0]);
				if (idx < 1 || idx > source.codePointCount(0, source.length())) {
					yield OclInvalid.INSTANCE;
				}
				yield Character.toString(source.codePointAt(source.offsetByCodePoints(0, idx - 1)));
			}
			case "characters" -> {
				List<String> chars = new ArrayList<>(source.length());
				source.codePoints().forEach(cp -> chars.add(Character.toString(cp)));
				yield chars;
			}
			case "toInteger" -> {
				Number parsed = parseInteger(source);
				yield parsed == null ? OclInvalid.INSTANCE : parsed;
			}
			case "toReal" -> {
				// Decimal notation only: NaN, Infinity, 1f or 0x1p3 are no Real (§11.4.1, #322)
				BigDecimal parsed = parseReal(source);
				yield parsed == null ? OclInvalid.INSTANCE : parsed;
			}
			case "toBoolean" -> {
				// Eclipse: exact match only, no trim/lowercase, non-"true" → false
				yield "true".equals(source);
			}
			case "matches" -> {
				String pattern = asString(args[0]);
				if (pattern.length() > options.maxRegexLength()) {
					yield OclInvalid.INSTANCE;
				}
				try {
					yield source.matches(pattern);
				} catch (PatternSyntaxException e) {
					yield OclInvalid.INSTANCE;
				}
			}
			case "replaceAll" -> {
				String pattern = asString(args[0]);
				if (pattern.length() > options.maxRegexLength()) {
					yield OclInvalid.INSTANCE;
				}
				yield replaceBounded(source, pattern, asString(args[1]), true, options.maxStringLength());
			}
			case "replaceFirst" -> {
				String pattern = asString(args[0]);
				if (pattern.length() > options.maxRegexLength()) {
					yield OclInvalid.INSTANCE;
				}
				yield replaceBounded(source, pattern, asString(args[1]), false, options.maxStringLength());
			}
			// §11.5.3 post: result = (self.toUpperCase() = s.toUpperCase()) — with the locale, so
			// 'ß' equals 'SS' and the Turkish dotted I is told apart (#329)
			case "equalsIgnoreCase" -> source.toUpperCase(locale).equals(asString(args[0]).toUpperCase(locale));
			case "startsWith" -> source.startsWith(asString(args[0]));
			case "endsWith" -> source.endsWith(asString(args[0]));
			// §11.1/§11.5.3: Unicode collation in the locale of oclLocale, not code-point order (#329)
			case "<" -> collate(source, asString(args[0]), locale) < 0;
			case "<=" -> collate(source, asString(args[0]), locale) <= 0;
			case ">" -> collate(source, asString(args[0]), locale) > 0;
			case ">=" -> collate(source, asString(args[0]), locale) >= 0;
			case "toString" -> source;
			// QVT-O §8.3.16.2: length (deprecated synonym for size)
			// Counted in code points, as size() is (#329, #344)
			case "length" -> (long) source.codePointCount(0, source.length());
			// QVT-O §8.3.16.3: substringBefore
			case "substringBefore" -> {
				int idx = source.indexOf(asString(args[0]));
				yield idx < 0 ? null : source.substring(0, idx);
			}
			// QVT-O §8.3.16.4: substringAfter
			case "substringAfter" -> {
				String match = asString(args[0]);
				int idx = source.indexOf(match);
				yield idx < 0 ? null : source.substring(idx + match.length());
			}
			// QVT-O §8.3.16.7: firstToUpper
			// A code point, titlecased: ǆ → ǅ, and a character outside the BMP stays whole (#344)
			case "firstToUpper" -> source.isEmpty() ? source
					: Character.toString(Character.toTitleCase(source.codePointAt(0)))
					+ source.substring(Character.charCount(source.codePointAt(0)));
			// QVT-O §8.3.16.8: lastToUpper
			case "lastToUpper" -> {
				if (source.isEmpty()) {
					yield source;
				}
				int last = source.offsetByCodePoints(source.length(), -1);
				yield source.substring(0, last) + Character.toString(Character.toUpperCase(source.codePointAt(last)));
			}
			// QVT-O §8.3.16.9 indexOf: the OCL operation answers, 0 if not found (OCL v2.4 §11.4.3;
			// QVT's "-1" is not taken over, the library being OCL's) (#344)
			// QVT-O §8.3.16.13: normalizeSpace — any Unicode white space (#344)
			case "normalizeSpace" -> stripWhiteSpace(source).replaceAll("\\p{IsWhite_Space}+", " ");
			// QVT-O §8.3.16.14: replace (literal, all occurrences)
			case "replace" -> source.replace(asString(args[0]), asString(args[1]));
			// QVT-O §8.3.16.15: match (full-string regex)
			case "match" -> {
				String pattern = asString(args[0]);
				if (pattern.length() > options.maxRegexLength()) {
					yield OclInvalid.INSTANCE;
				}
				try {
					yield Pattern.matches(pattern, source);
				} catch (PatternSyntaxException e) {
					yield OclInvalid.INSTANCE;
				}
			}
			// QVT-O §8.3.16.17: find — the 1-based position in code points (#344), 0 if not found:
			// the spec does not say; positions start at 1, indexOf answers 0 (OCL v2.4 §11.4.3) and
			// so does Eclipse — decided with the user (#344)
			case "find" -> {
				int idx = source.indexOf(asString(args[0]));
				yield (long) (idx < 0 ? 0 : source.codePointCount(0, idx) + 1);
			}
			// QVT-O §8.3.16.18: rfind — the position of the last occurrence, 0 if not found
			case "rfind" -> {
				int idx = source.lastIndexOf(asString(args[0]));
				yield (long) (idx < 0 ? 0 : source.codePointCount(0, idx) + 1);
			}
			// QVT-O §8.3.16.19: isQuoted
			case "isQuoted" -> {
				String q = asString(args[0]);
				yield source.length() >= q.length() * 2
						&& source.startsWith(q) && source.endsWith(q);
			}
			// QVT-O §8.3.16.20: quotify
			case "quotify" -> asString(args[0]) + source + asString(args[0]);
			// QVT-O §8.3.16.21: unquotify
			case "unquotify" -> {
				String q = asString(args[0]);
				if (source.length() >= q.length() * 2
						&& source.startsWith(q) && source.endsWith(q)) {
					yield source.substring(q.length(), source.length() - q.length());
				}
				yield source;
			}
			// QVT-O §8.3.16.22: matchBoolean — "true"/"false"/"0"/"1" case-insensitive
			case "matchBoolean" -> {
				String lower = source.trim().toLowerCase();
				yield "true".equals(lower) || "false".equals(lower)
						|| "0".equals(lower) || "1".equals(lower);
			}
			// QVT-O §8.3.16.23: matchInteger
			case "matchInteger" -> parseInteger(source) != null;
			// QVT-O §8.3.16.24/25: matchFloat/matchReal
			case "matchFloat", "matchReal" -> parseReal(source) != null;
			// QVT-O §8.3.16.26: matchIdentifier — letter first, then alphanumeric
			case "matchIdentifier" -> {
				if (source.isEmpty() || !Character.isLetter(source.charAt(0))) {
					yield false;
				}
				boolean valid = true;
				for (int i = 1; i < source.length(); i++) {
					if (!Character.isLetterOrDigit(source.charAt(i))) {
						valid = false;
						break;
					}
				}
				yield valid;
			}
			// QVT-O §8.3.16.27: asBoolean — "true"/"1" → true, "false"/"0" → false, else null
			case "asBoolean" -> {
				String lower = source.trim().toLowerCase();
				if ("true".equals(lower) || "1".equals(lower)) yield true;
				if ("false".equals(lower) || "0".equals(lower)) yield false;
				yield null; // unparseable
			}
			// QVT-O §8.3.16.28: asInteger — returns null on failure (unlike OCL toInteger → invalid)
			case "asInteger" -> parseInteger(source);
			// QVT-O §8.3.16.29/30: asFloat/asReal — returns null on failure
			case "asFloat", "asReal" -> parseReal(source);
			// QVT-O §8.3.16.1 + §8.4.4: format / % — placeholder substitution
			case "format", "%" -> formatQvto(source, args[0], options.maxStringLength(), locale);
			default -> NOT_FOUND;
		};
	}

	// --- Collection (OCL v2.4 Section 11.7) ---

	private static Object dispatchCollection(String name, Collection<?> source, Object[] args,
			OclEvaluationOptions options, Locale locale) {
		return switch (name) {
			case "size" -> (long) source.size();
			case "isEmpty" -> source.isEmpty();
			case "notEmpty" -> !source.isEmpty();
			case "includes" -> oclContains(source, args[0]);
			case "excludes" -> !oclContains(source, args[0]);
			case "includesAll" -> {
				if (!(args[0] instanceof Collection<?> other)) yield OclInvalid.INSTANCE;
				yield other.stream().allMatch(e -> oclContains(source, e));
			}
			case "excludesAll" -> {
				if (!(args[0] instanceof Collection<?> other)) yield OclInvalid.INSTANCE;
				yield other.stream().noneMatch(e -> oclContains(source, e));
			}
			case "count" -> source.stream().filter(e -> Boolean.TRUE.equals(oclEquals(e, args[0]))).count();
			case "flatten" -> {
				List<Object> flat = new ArrayList<>();
				flattenInto(source, flat);
				yield preserveCollectionKind(source, flat);
			}
			case "including" -> {
				if (source instanceof OclOrderedSet<?>) {
					OclOrderedSet<Object> result = new OclOrderedSet<>(source);
					result.add(args[0]);
					yield result;
				}
				if (source instanceof OclBag<?>) {
					OclBag<Object> result = new OclBag<>(source);
					result.add(args[0]);
					yield result;
				}
				if (source instanceof Set<?>) {
					Set<Object> result = new OclSet<>(source);
					result.add(args[0]);
					yield result;
				}
				List<Object> result = new ArrayList<>(source);
				result.add(args[0]);
				yield result;
			}
			case "excluding" -> {
				// OCL v2.5 §11.7.2: excluding removes ALL occurrences
				// Uses OCL equality (§11.5.1: 4 = 4.0) for element matching
				Object toRemove = args[0];
				if (source instanceof OclOrderedSet<?>) {
					OclOrderedSet<Object> result = new OclOrderedSet<>(source);
					result.removeIf(e -> OclEqualityUtil.oclEquals(e, toRemove));
					yield result;
				}
				if (source instanceof Set<?>) {
					Set<Object> result = new OclSet<>(source);
					result.remove(toRemove); // OclSet uses OCL equality
					yield result;
				}
				// Bag and Sequence: remove ALL occurrences using OCL equality
				if (source instanceof OclBag<?>) {
					OclBag<Object> result = new OclBag<>(source);
					result.removeIf(e -> OclEqualityUtil.oclEquals(e, toRemove));
					yield result;
				}
				List<Object> result = new ArrayList<>(source);
				result.removeIf(e -> OclEqualityUtil.oclEquals(e, toRemove));
				yield result;
			}
			case "union" -> {
				if (!(args[0] instanceof Collection<?> other)) yield OclInvalid.INSTANCE;
				// §11.7.2: Set->union(Set)→Set, Set->union(Bag)→Bag
				// §11.7.4: Bag->union(Bag)→Bag, Bag->union(Set)→Bag
				if (source instanceof Set<?> && !(source instanceof OclOrderedSet<?>)) {
					if (other instanceof OclBag<?>) {
						// Set->union(Bag) → Bag
						OclBag<Object> bag = new OclBag<>(source);
						bag.addAll(other);
						yield bag;
					}
					Set<Object> result = new OclSet<>(source);
					result.addAll(other);
					yield result;
				}
				List<Object> result;
				if (source instanceof OclBag<?>) {
					result = new OclBag<>(source);
				} else if (source instanceof OclOrderedSet<?>) {
					result = new OclOrderedSet<>(source);
				} else {
					result = new ArrayList<>(source);
				}
				result.addAll(other);
				yield result;
			}
			case "intersection" -> {
				if (!(args[0] instanceof Collection<?> other)) yield OclInvalid.INSTANCE;
				// §11.7.2: Set->intersection(Set)→Set, Set->intersection(Bag)→Set
				// §11.7.4: Bag->intersection(Bag)→Bag, Bag->intersection(Set)→Set
				// Membership is OCL equality (1 = 1.0, #317), never Java equals — retainAll and
				// List.remove asked the argument's equals and lost Set{1}->intersection(Bag{1.0})
				boolean uniqueSource = source instanceof Set<?> || source instanceof OclOrderedSet<?>;
				if ((source instanceof Set<?> && !(source instanceof OclOrderedSet<?>))
						|| (source instanceof OclBag<?> && isUniqueCollection(other))) {
					Set<Object> lookup = asOclSet(other);
					Set<Object> result = new OclSet<>();
					for (Object e : source) {
						if (lookup.contains(e)) {
							result.add(e);
						}
					}
					yield result;
				}
				// Bag->intersection(Bag) and the rest: frequency-based (min frequency)
				Map<Object, Integer> remaining = frequencies(other);
				List<Object> result;
				if (source instanceof OclBag<?>) {
					result = new OclBag<>();
				} else if (uniqueSource) {
					result = new OclOrderedSet<>();
				} else {
					result = new ArrayList<>();
				}
				for (Object e : source) {
					if (takeOne(remaining, e)) {
						result.add(e);
					}
				}
				yield result;
			}
			case "-" -> {
				if (!(args[0] instanceof Collection<?> other)) yield OclInvalid.INSTANCE;
				// §11.7.2 Set - Set: elements of self not in s, by OCL equality (#317)
				if (source instanceof Set<?> && !(source instanceof OclOrderedSet<?>)) {
					Set<Object> result = new OclSet<>(source);
					for (Object o : other) {
						result.remove(o); // OclSet.remove uses the OCL lookup key
					}
					yield result;
				}
				// Bag/Sequence/OrderedSet: frequency-based subtraction (subtract per occurrence)
				Map<Object, Integer> toRemove = frequencies(other);
				List<Object> result;
				if (source instanceof OclBag<?>) {
					result = new OclBag<>();
				} else if (source instanceof OclOrderedSet<?>) {
					result = new OclOrderedSet<>();
				} else {
					result = new ArrayList<>();
				}
				for (Object e : source) {
					if (!takeOne(toRemove, e)) {
						result.add(e);
					}
				}
				yield result;
			}
			case "symmetricDifference" -> {
				if (!(args[0] instanceof Collection<?> other)) yield OclInvalid.INSTANCE;
				Set<Object> result = new OclSet<>(source);
				for (Object o : other) {
					if (!result.remove(o)) {
						result.add(o);
					}
				}
				yield result;
			}
			case "asSet" -> new OclSet<>(source);
			case "asBag" -> new OclBag<>(source);
			case "asSequence" -> new ArrayList<>(source);
			case "asOrderedSet" -> new OclOrderedSet<>(source);
			case "selectByKind" -> {
				List<Object> filtered = new ArrayList<>();
				for (Object e : source) {
					if (Boolean.TRUE.equals(oclIsKindOf(e, args[0]))) {
						filtered.add(e);
					}
				}
				yield preserveCollectionKind(source, filtered);
			}
			case "selectByType" -> {
				List<Object> filtered = new ArrayList<>();
				for (Object e : source) {
					if (Boolean.TRUE.equals(oclIsTypeOf(e, args[0]))) {
						filtered.add(e);
					}
				}
				yield preserveCollectionKind(source, filtered);
			}
			case "sum" -> {
				// §11.7.1: elements of a type with '+'. Numbers add exactly (#322); Strings
				// concatenate, as String is summable in Eclipse OCL with '' as its zero (#322)
				boolean strings = !source.isEmpty() && source.stream().allMatch(String.class::isInstance);
				if (strings) {
					StringBuilder text = new StringBuilder();
					for (Object e : source) {
						text.append((String) e);
					}
					yield text.toString();
				}
				Number total = 0L;
				for (Object e : source) {
					if (!(e instanceof Number n) || !OclNumbers.isFinite(n)) {
						yield OclInvalid.INSTANCE;
					}
					total = OclNumbers.add(total, n);
				}
				yield total;
			}
			case "max" -> {
				// §11.7.1: elements of a type with max — numbers (UnlimitedNatural included) and,
				// as in Eclipse OCL, Strings; Boolean has no max (#322)
				Object max = null;
				for (Object e : source) {
					if (!hasMaxMin(e)) yield OclInvalid.INSTANCE;
					if (e instanceof OclUnlimitedNatural) {
						max = e;
					} else if (max == null || (!(max instanceof OclUnlimitedNatural) && compareOcl(e, max, locale) > 0)) {
						max = e;
					}
				}
				yield max == null ? OclInvalid.INSTANCE : max;
			}
			case "min" -> {
				Object min = null;
				for (Object e : source) {
					if (!hasMaxMin(e)) yield OclInvalid.INSTANCE;
					if (e instanceof OclUnlimitedNatural) {
						if (min == null) min = e;
					} else if (min == null || min instanceof OclUnlimitedNatural || compareOcl(e, min, locale) < 0) {
						min = e;
					}
				}
				yield min == null ? OclInvalid.INSTANCE : min;
			}
			case "product" -> {
				if (!(args[0] instanceof Collection<?> other)) yield OclInvalid.INSTANCE;
				// Well-formedness (§11.7.1): collection cannot contain invalid values
				for (Object a : source) {
					if (a == OclInvalid.INSTANCE) yield OclInvalid.INSTANCE;
				}
				for (Object b : other) {
					if (b == OclInvalid.INSTANCE) yield OclInvalid.INSTANCE;
				}
				long productSize = (long) source.size() * other.size();
				if (productSize > options.maxCollectionSize()) {
					yield OclInvalid.INSTANCE;
				}
				Set<Map<String, Object>> result = new OclSet<>();
				for (Object a : source) {
					for (Object b : other) {
						Map<String, Object> tuple = new LinkedHashMap<>();
						tuple.put("first", a);
						tuple.put("second", b);
						result.add(tuple);
					}
				}
				yield result;
			}
			case "toString" -> source.toString();
			// QVT-O §8.3.9.3: List::add(object) — mutating append
			case "add" -> {
				if (belongsToAModel(source)) {
					yield OclInvalid.INSTANCE; // #182: not through an expression
				}
				if (!isList(source)) {
					yield notAList(name, source);
				}
				@SuppressWarnings("unchecked")
				var list = (Collection<Object>) source;
				list.add(args[0]);
				yield null; // returns Void
			}
			// QVT-O §8.3.9.35: List::remove(element) — mutating remove ALL equal elements
			case "remove" -> {
				if (belongsToAModel(source)) {
					yield OclInvalid.INSTANCE; // #182: not through an expression
				}
				if (!isList(source)) {
					yield notAList(name, source);
				}
				if (source instanceof List<?> list) {
					// §8.3.9.35 post reject(e = element): OCL equality, 1 = 1.0 (#317)
					list.removeIf(e -> OclEqualityUtil.oclEquals(e, args[0]));
				}
				yield null; // returns Void
			}
			// QVT-O §8.3.9.38: List::removeAt(index) — mutating remove at 1-based index, returns removed
			case "removeAt" -> {
				if (belongsToAModel(source)) {
					yield OclInvalid.INSTANCE; // #182: not through an expression
				}
				if (!isList(source)) {
					yield notAList(name, source);
				}
				if (source instanceof List<?> list && args[0] instanceof Long idx) {
					int i = idx.intValue() - 1; // 1-based to 0-based
					if (i >= 0 && i < list.size()) {
						yield list.remove(i);
					}
				}
				yield OclInvalid.INSTANCE;
			}
			// QVT-O §8.3.9.39: List::removeFirst() — mutating remove first, returns removed
			case "removeFirst" -> {
				if (belongsToAModel(source)) {
					yield OclInvalid.INSTANCE; // #182: not through an expression
				}
				if (!isList(source)) {
					yield notAList(name, source);
				}
				if (source instanceof List<?> list && !list.isEmpty()) {
					yield list.remove(0);
				}
				yield OclInvalid.INSTANCE;
			}
			// QVT-O §8.3.9.40: List::removeLast() — mutating remove last, returns removed
			case "removeLast" -> {
				if (belongsToAModel(source)) {
					yield OclInvalid.INSTANCE; // #182: not through an expression
				}
				if (!isList(source)) {
					yield notAList(name, source);
				}
				if (source instanceof List<?> list && !list.isEmpty()) {
					yield list.remove(list.size() - 1);
				}
				yield OclInvalid.INSTANCE;
			}
			// QVT-O §8.3.9.36: List::removeAll(elements) — mutating remove all matching
			case "removeAll" -> {
				if (belongsToAModel(source)) {
					yield OclInvalid.INSTANCE; // #182: not through an expression
				}
				if (!isList(source)) {
					yield notAList(name, source);
				}
				if (args[0] instanceof Collection<?> toRemove && source instanceof List<?> list) {
					list.removeIf(e -> toRemove.stream().anyMatch(r -> OclEqualityUtil.oclEquals(e, r)));
				}
				yield null; // returns Void
			}
			// QVT-O §8.3.9.28: List::joinfields(sep, begin, end) — string join
			case "joinfields" -> {
				String sep = args.length > 0 ? String.valueOf(args[0]) : "";
				String begin = args.length > 1 ? String.valueOf(args[1]) : "";
				String end = args.length > 2 ? String.valueOf(args[2]) : "";
				BoundedOutput out = new BoundedOutput(options.maxStringLength());
				try {
					out.append(begin);
					int pos = 0;
					for (Object e : source) {
						if (pos++ > 0) out.append(sep);
						// A Real as OCL writes it (1.0e10 → 10000000000.0), null as nothing, as
						// QVT-O's string concatenation reads it (#343)
						out.append(e == null ? "" : OclValues.toText(e));
					}
					out.append(end);
				} catch (OutputLimitExceeded tooLarge) {
					yield OclInvalid.INSTANCE;
				}
				yield out.toString();
			}
			default -> NOT_FOUND;
		};
	}

	/**
	 * Whether a collection is a QVT-O List — a Sequence, as List is its alias (D26) — the only
	 * collection the mutating operations change: "Collections are immutable" (QVT v1.3 §8.3.11.2).
	 */
	private static boolean isList(Collection<?> source) {
		return source instanceof List<?> && !(source instanceof OclBag<?>) && !(source instanceof OclOrderedSet<?>);
	}

	private static ArgumentError notAList(String operation, Collection<?> source) {
		String kind = source instanceof OclOrderedSet<?> ? "an OrderedSet"
				: source instanceof OclBag<?> ? "a Bag" : "a Set";
		return new ArgumentError("'" + operation + "' changes a List, and " + kind
				+ " is immutable (QVT v1.3 §8.3.11.2)");
	}

	// --- Ordered Collection: Sequence, OrderedSet (OCL v2.4 Section 11.7.3/11.7.4) ---

	private static Object dispatchOrderedCollection(String name, List<?> source, Object[] args) {
		return switch (name) {
			case "first" -> source.isEmpty() ? OclInvalid.INSTANCE : source.get(0);
			case "last" -> source.isEmpty() ? OclInvalid.INSTANCE : source.get(source.size() - 1);
			case "at" -> {
				int idx = asIndex(args[0]);
				if (idx < 1 || idx > source.size()) yield OclInvalid.INSTANCE;
				yield source.get(idx - 1); // 1-based
			}
			case "indexOf" -> {
				for (int i = 0; i < source.size(); i++) {
					if (Boolean.TRUE.equals(oclEquals(source.get(i), args[0]))) {
						yield (long) (i + 1);
					}
				}
				yield OclInvalid.INSTANCE; // not found → invalid
			}
			case "reverse" -> {
				List<Object> result = new ArrayList<>(source);
				Collections.reverse(result);
				yield source instanceof OclOrderedSet<?> ? new OclOrderedSet<>(result) : result;
			}
			case "append" -> {
				if (source instanceof OclOrderedSet<?>) {
					// OCL §11.7.3: if already present, move to end
					OclOrderedSet<Object> result = new OclOrderedSet<>();
					for (Object e : source) {
						if (!Boolean.TRUE.equals(oclEquals(e, args[0]))) result.add(e);
					}
					result.add(args[0]);
					yield result;
				}
				List<Object> result = new ArrayList<>(source);
				result.add(args[0]);
				yield result;
			}
			case "prepend" -> {
				if (source instanceof OclOrderedSet<?>) {
					// OCL §11.7.3: if already present, move to front
					OclOrderedSet<Object> result = new OclOrderedSet<>();
					result.add(args[0]);
					for (Object e : source) {
						if (!Boolean.TRUE.equals(oclEquals(e, args[0]))) result.add(e);
					}
					yield result;
				}
				List<Object> result = new ArrayList<>(source.size() + 1);
				result.add(args[0]);
				result.addAll(source);
				yield result;
			}
			case "insertAt" -> {
				// QVT v1.3 §8.3.9.26: List::insertAt(object, index) : Void modifies the list. Told
				// from OCL's insertAt(index, object) : Sequence by the index — an Integer in the
				// second place and none in the first; with two Integers OCL's form is meant (#343)
				if (!OclNumbers.isInteger(args[0]) && OclNumbers.isInteger(args[1])) {
					if (!isList(source)) {
						yield notAList(name, source);
					}
					int at = asIndex(args[1]);
					if (at < 1 || at > source.size() + 1) yield OclInvalid.INSTANCE;
					@SuppressWarnings("unchecked")
					var list = (List<Object>) source;
					list.add(at - 1, args[0]);
					yield null; // returns Void
				}
				int idx = asIndex(args[0]);
				if (idx < 1 || idx > source.size() + 1) yield OclInvalid.INSTANCE;
				if (source instanceof OclOrderedSet<?>) {
					OclOrderedSet<Object> result = new OclOrderedSet<>(source);
					if (!result.contains(args[1])) {
						result.add(idx - 1, args[1]);
					}
					yield result;
				}
				List<Object> result = new ArrayList<>(source);
				result.add(idx - 1, args[1]);
				yield result;
			}
			case "subSequence", "subOrderedSet" -> {
				int lower = asIndex(args[0]);
				int upper = asIndex(args[1]);
				if (lower < 1 || upper > source.size() || lower > upper) {
					yield OclInvalid.INSTANCE;
				}
				List<Object> sub = new ArrayList<>(source.subList(lower - 1, upper));
				yield source instanceof OclOrderedSet<?> ? new OclOrderedSet<>(sub) : sub;
			}
			default -> NOT_FOUND;
		};
	}

	// --- Map (OCL v2.5) ---

	private static Object dispatchMap(String name, Map<?, ?> source, Object[] args, OclEvaluationOptions options) {
		return switch (name) {
			case "size" -> (long) source.size();
			case "isEmpty" -> source.isEmpty();
			case "notEmpty" -> !source.isEmpty();
			// Keys are found by OCL equality, so a Dict(Real) answers get(1) for the key 1.0 (#317)
			case "includes" -> dictKey(source, args[0]) != NOT_FOUND;
			case "excludes" -> dictKey(source, args[0]) == NOT_FOUND;
			case "includesValue" -> source.values().stream().anyMatch(v -> OclEqualityUtil.oclEquals(v, args[0]));
			case "excludesValue" -> source.values().stream().noneMatch(v -> OclEqualityUtil.oclEquals(v, args[0]));
			case "get", "at" -> {
				Object key = dictKey(source, args[0]);
				yield key == NOT_FOUND ? null : source.get(key);
			}
			// OCL v2.5 Map::keys() : Set(K); QVT v1.3 §8.3.8.8 Dict::keys() : List(KeyT) "Returns a
			// new list of keys" — a Dict is OCL's Map (D26), so the QVT-O engine says which (#343)
			case "keys" -> options.qvtCollections() ? new ArrayList<>(source.keySet()) : new OclSet<>(source.keySet());
			case "values" -> new ArrayList<>(source.values());
			case "including" -> {
				Map<Object, Object> result = new LinkedHashMap<>(source);
				result.put(args[0], args[1]);
				yield result;
			}
			case "excluding" -> {
				Map<Object, Object> result = new LinkedHashMap<>(source);
				result.remove(dictKey(source, args[0]));
				yield result;
			}
			case "union" -> {
				if (!(args[0] instanceof Map<?, ?> other)) yield OclInvalid.INSTANCE;
				Map<Object, Object> result = new LinkedHashMap<>(source);
				result.putAll(other);
				yield result;
			}
			case "toString" -> source.toString();
			// QVT-O §8.3.8.2: Dict::hasKey(k) — check key existence
			case "hasKey" -> dictKey(source, args[0]) != NOT_FOUND;
			// QVT-O §8.3.8.3: Dict::defaultget(k, default) — get with fallback
			case "defaultget" -> {
				// The default "if the key does not exist" — not for a key whose value is null (#343)
				Object key = dictKey(source, args[0]);
				yield key == NOT_FOUND ? args[1] : source.get(key);
			}
			// QVT-O §8.3.8.4: Dict::put(k, v) — mutable insert/update
			case "put" -> {
				if (belongsToAModel(source)) {
					yield OclInvalid.INSTANCE; // #182: not through an expression
				}
				@SuppressWarnings("unchecked")
				var mutableMap = (Map<Object, Object>) source;
				Object existing = dictKey(source, args[0]);
				mutableMap.put(existing == NOT_FOUND ? args[0] : existing, args[1]);
				yield null; // returns Void
			}
			// QVT-O §8.3.8.5: Dict::clear() — mutable remove all
			case "clear" -> {
				if (belongsToAModel(source)) {
					yield OclInvalid.INSTANCE; // #182: not through an expression
				}
				source.clear();
				yield null; // returns Void
			}
			default -> NOT_FOUND;
		};
	}

	private static int compareOcl(Object a, Object b) {
		return OclCollectionUtil.compareOcl(a, b);
	}

	/**
	 * The key of the Dict that is OCL-equal to {@code key} — the key itself on a hash hit,
	 * otherwise the one {@code oclEquals} matches — or {@link #NOT_FOUND} (#317).
	 */
	private static Object dictKey(Map<?, ?> dict, Object key) {
		if (dict.containsKey(key)) {
			return key;
		}
		for (Object candidate : dict.keySet()) {
			if (OclEqualityUtil.oclEquals(candidate, key)) {
				return candidate;
			}
		}
		return NOT_FOUND;
	}

	// --- String helpers ---

	/**
	 * Compares two strings by the Unicode Collation Algorithm in the given locale (OCL v2.4
	 * §11.1: "Comparison of strings … conforms to the Unicode Collation algorithm"; §11.5.3:
	 * "using the locale defined by looking up oclLocale", #329). A {@link Collator} is not
	 * thread-safe, so each call takes its own instance; equal strings by the collator that
	 * differ in their code points are ordered by those, so the order stays total.
	 */
	static int collate(String left, String right, Locale locale) {
		int byCollation = Collator.getInstance(locale).compare(left, right);
		return byCollation != 0 ? byCollation : left.compareTo(right);
	}

	/** {@link OclCollectionUtil#compareOcl} with strings collated in the given locale. */
	static int compareOcl(Object a, Object b, Locale locale) {
		if (a instanceof String left && b instanceof String right) {
			return collate(left, right, locale);
		}
		return OclCollectionUtil.compareOcl(a, b);
	}

	// --- Number helpers ---

	/** Whether a collection element supports {@code max}/{@code min} (§11.7.1). */
	private static boolean hasMaxMin(Object element) {
		return element instanceof OclUnlimitedNatural || element instanceof String
				|| (element instanceof Number n && OclNumbers.isFinite(n));
	}

	/** An OCL Integer from its decimal text, or {@code null} — exact beyond 64 bits (#322). */
	private static Number parseInteger(String text) {
		try {
			return OclNumbers.integer(new BigInteger(text.trim()));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	/**
	 * An OCL Real from its decimal text, or {@code null}. {@code BigDecimal} reads exactly the
	 * decimal notations, so the Java-only spellings {@code NaN}, {@code Infinity}, {@code 1f} and
	 * {@code 0x1p3} that {@code Double.parseDouble} took are refused (#322).
	 */
	private static BigDecimal parseReal(String text) {
		try {
			return OclNumbers.real(new BigDecimal(text.trim()));
		} catch (NumberFormatException e) {
			return null;
		}
	}

	// --- Collection helpers ---

	private static boolean isUniqueCollection(Collection<?> collection) {
		return collection instanceof Set<?> || collection instanceof OclOrderedSet<?>;
	}

	@SuppressWarnings("unchecked")
	private static Set<Object> asOclSet(Collection<?> collection) {
		return collection instanceof OclSet<?> set ? (Set<Object>) set : new OclSet<>(collection);
	}

	/** Counts the elements of a collection per OCL value (lookup key, #317). */
	private static Map<Object, Integer> frequencies(Collection<?> collection) {
		Map<Object, Integer> counts = new HashMap<>();
		for (Object e : collection) {
			counts.merge(OclEqualityUtil.lookupKey(e), 1, Integer::sum);
		}
		return counts;
	}

	/** Takes one occurrence of {@code element}'s value from the counts; false if none is left. */
	private static boolean takeOne(Map<Object, Integer> counts, Object element) {
		Object key = OclEqualityUtil.lookupKey(element);
		Integer count = counts.get(key);
		if (count == null) {
			return false;
		}
		if (count == 1) {
			counts.remove(key);
		} else {
			counts.put(key, count - 1);
		}
		return true;
	}

	private static Collection<Object> preserveCollectionKind(Collection<?> source, List<Object> elements) {
		return OclCollectionUtil.preserveCollectionKind(source, elements);
	}

	static void flattenInto(Collection<?> source, List<Object> result) {
		for (Object element : source) {
			if (element instanceof Collection<?> nested) {
				flattenInto(nested, result);
			} else {
				result.add(element);
			}
		}
	}

	// --- Type coercion helpers (safe — no ClassCastException) ---

	private static boolean asBoolean(Object value) {
		return value instanceof Boolean b ? b : false;
	}

	/**
	 * A position argument as a Java index. A value outside the {@code int} range becomes
	 * {@code -1}, which every caller treats as out of range — a cast would wrap 2^32 + 1 to 1.
	 */
	private static int asIndex(Object value) {
		if (value instanceof Number n && OclNumbers.isInteger(n)) {
			BigInteger position = OclNumbers.bigInteger(n);
			return position.bitLength() < 32 ? position.intValue() : -1;
		}
		return -1;
	}

	private static String asString(Object value) {
		return value instanceof String s ? s : String.valueOf(value);
	}
}
