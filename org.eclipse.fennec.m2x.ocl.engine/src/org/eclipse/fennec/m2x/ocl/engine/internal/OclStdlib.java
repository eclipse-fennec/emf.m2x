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

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.StringTokenizer;
import java.util.regex.Pattern;
import java.util.regex.PatternSyntaxException;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.ClassifierType;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.MapType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.ocl.api.OclEvaluationOptions;
import org.eclipse.fennec.m2x.ocl.api.OclInvalid;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

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
		if (source instanceof Long l) {
			return dispatchInteger(name, l, args);
		}
		if (source instanceof Integer i) {
			return dispatchInteger(name, (long) i, args);
		}
		if (source instanceof Double d) {
			return dispatchReal(name, d, args);
		}
		if (source instanceof Float f) {
			return dispatchReal(name, (double) f, args);
		}
		if (source instanceof String s) {
			return dispatchString(name, s, args, options, locale);
		}
		if (source instanceof Map<?, ?> m) {
			return dispatchMap(name, m, args);
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
			return dispatchCollection(name, c, args, options);
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
			case "toString" -> source == null ? OclInvalid.INSTANCE : String.valueOf(source);
			// QVT-O §8.3.3.1: Object::repr() — textual representation of any object
			case "repr" -> String.valueOf(source);
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
		if (source instanceof Long || source instanceof Integer) return LIBRARY.integer();
		if (source instanceof Double || source instanceof Float) return LIBRARY.real();
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
		if (left instanceof EObject le && right instanceof EObject re) {
			return EcoreUtil.equals(le, re);
		}
		// Numeric cross-type equality: 5.0 = 5 → true (OCL v2.4 §11.5.1)
		if (left instanceof Number ln && right instanceof Number rn) {
			return Double.compare(ln.doubleValue(), rn.doubleValue()) == 0;
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
			if (matchesPrimitiveType(source, pt.getName())) {
				return true;
			}
			// Spec §11.5.1: UnlimitedNatural conforms to Integer conforms to Real
			if (source instanceof OclUnlimitedNatural) {
				return "Integer".equals(pt.getName()) || "Real".equals(pt.getName());
			}
			if (source instanceof Long || source instanceof Integer) {
				return "Real".equals(pt.getName());
			}
			return false;
		}
		if (typeArg instanceof CollectionType) {
			return source instanceof Collection<?>;
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

	static boolean matchesPrimitiveType(Object source, String typeName) {
		return switch (typeName) {
			case "Integer" -> source instanceof Long || source instanceof Integer;
			case "Real" -> source instanceof Double || source instanceof Float;
			case "String" -> source instanceof String;
			case "Boolean" -> source instanceof Boolean;
			case "UnlimitedNatural" -> source instanceof OclUnlimitedNatural
					|| source instanceof Long || source instanceof Integer;
			case "OclVoid" -> source == null;
			default -> false;
		};
	}

	private static Object oclAsType(Object source, Object typeArg) {
		// Spec §11.3.2: null.oclAsType(type) → self (null conforms to all types)
		if (source == null) {
			return null;
		}
		// Handle primitive types (PrimitiveType has no EClassifier)
		if (typeArg instanceof PrimitiveType pt) {
			return matchesPrimitiveType(source, pt.getName()) ? source : OclInvalid.INSTANCE;
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
		OclType elementType = targetType.getElementType();
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
	private static boolean allElementsConform(Collection<?> coll, OclType elementType) {
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
				if (elem != null && !matchesPrimitiveType(elem, typeName)) {
					return false;
				}
			}
			return true;
		}
		// ClassifierType check via EClassifier.isInstance()
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
		// The OCL types are EClassifiers themselves since #154, so the wrapper has to be
		// unwrapped before the general case: asked as an EClassifier, a ClassifierType would
		// answer for itself instead of for the class it refers to.
		if (typeArg instanceof ClassifierType ct) {
			return ct.getReferredClassifier();
		}
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

	private static Object dispatchInteger(String name, Long source, Object[] args) {
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
		return switch (name) {
			case "+" -> {
				if (args.length == 0) yield source; // unary +
				Object arg = args[0];
				if (arg instanceof Double d) yield source + d;
				yield source + asLong(arg);
			}
			case "-" -> {
				if (args.length == 0) yield -source; // unary -
				Object arg = args[0];
				if (arg instanceof Double d) yield source - d;
				yield source - asLong(arg);
			}
			case "*" -> {
				Object arg = args[0];
				if (arg instanceof Double d) yield source * d;
				yield source * asLong(arg);
			}
			case "/" -> {
				Object arg = args[0];
				double divisor = arg instanceof Double d ? d : asLong(arg);
				if (divisor == 0.0) yield OclInvalid.INSTANCE;
				yield source / divisor; // always returns Real (Double)
			}
			case "div" -> {
				long divisor = asLong(args[0]);
				if (divisor == 0) yield OclInvalid.INSTANCE;
				yield source / divisor;
			}
			case "mod" -> {
				long divisor = asLong(args[0]);
				if (divisor == 0) yield OclInvalid.INSTANCE;
				yield source % divisor;
			}
			case "abs" -> Math.abs(source);
			case "max" -> {
				Object arg = args[0];
				if (arg instanceof Double d) yield Math.max(source, d);
				yield Math.max(source, asLong(arg));
			}
			case "min" -> {
				Object arg = args[0];
				if (arg instanceof Double d) yield Math.min(source, d);
				yield Math.min(source, asLong(arg));
			}
			case "<" -> source < asNumber(args[0]).doubleValue();
			case "<=" -> source <= asNumber(args[0]).doubleValue();
			case ">" -> source > asNumber(args[0]).doubleValue();
			case ">=" -> source >= asNumber(args[0]).doubleValue();
			case "toReal" -> (double) source;
			case "toInteger" -> source;
			case "floor" -> source;
			case "ceiling" -> source;
			case "round" -> source;
			case "toString" -> source.toString();
			case "range" -> {
				long start, end;
				if (args.length == 1 && args[0] instanceof Long e) {
					// source.range(end) — source is start
					start = source;
					end = e;
				} else if (args.length >= 2 && args[0] instanceof Long s && args[1] instanceof Long e) {
					// source.range(start, end) — source ignored (§8.3.17 static-style)
					start = s;
					end = e;
				} else {
					yield OclInvalid.INSTANCE;
				}
				List<Object> result = new ArrayList<>();
				for (long i = start; i <= end; i++) {
					result.add(i);
				}
				yield result;
			}
			default -> NOT_FOUND;
		};
	}

	// --- Real (OCL v2.4 Section 11.5) ---

	private static Object dispatchReal(String name, Double source, Object[] args) {
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
		return switch (name) {
			case "+" -> {
				if (args.length == 0) yield source;
				yield source + asNumber(args[0]).doubleValue();
			}
			case "-" -> {
				if (args.length == 0) yield -source;
				yield source - asNumber(args[0]).doubleValue();
			}
			case "*" -> source * asNumber(args[0]).doubleValue();
			case "/" -> {
				double divisor = asNumber(args[0]).doubleValue();
				if (divisor == 0.0) yield OclInvalid.INSTANCE;
				yield source / divisor;
			}
			case "abs" -> Math.abs(source);
			case "max" -> Math.max(source, asNumber(args[0]).doubleValue());
			case "min" -> Math.min(source, asNumber(args[0]).doubleValue());
			case "floor" -> (long) Math.floor(source);
			case "ceiling" -> (long) Math.ceil(source);
			case "round" -> (long) Math.round(source);
			case "<" -> source < asNumber(args[0]).doubleValue();
			case "<=" -> source <= asNumber(args[0]).doubleValue();
			case ">" -> source > asNumber(args[0]).doubleValue();
			case ">=" -> source >= asNumber(args[0]).doubleValue();
			case "toInteger" -> (long) source.doubleValue();
			case "toReal" -> source;
			case "toString" -> source.toString();
			default -> NOT_FOUND;
		};
	}

	// --- String (OCL v2.4 Section 11.7) ---

	private static Object dispatchString(String name, String source, Object[] args,
			OclEvaluationOptions options, Locale locale) {
		return switch (name) {
			case "size" -> (long) source.length();
			case "+" , "concat" -> source + asString(args[0]);
			case "substring" -> {
				int lower = (int) asLong(args[0]);
				int upper = (int) asLong(args[1]);
				// OCL v2.5 §11.5.1: 1 <= lower AND lower <= upper AND upper <= size
				if (lower < 1 || upper > source.length() || lower > upper) {
					yield OclInvalid.INSTANCE;
				}
				yield source.substring(lower - 1, upper);
			}
			case "toUpperCase", "toUpper" -> source.toUpperCase(locale);
			case "toLowerCase", "toLower" -> source.toLowerCase(locale);
			case "trim" -> source.trim();
			case "indexOf" -> {
				// OCL v2.4 §11.5.3: "or zero if s is not a substring of self"
				int idx = source.indexOf(asString(args[0]));
				yield (long) (idx + 1); // 1-based, 0 if not found
			}
			case "lastIndexOf" -> {
				// Eclipse extension (not in OCL v2.4 spec), consistent with indexOf
				int idx = source.lastIndexOf(asString(args[0]));
				yield (long) (idx + 1); // 1-based, 0 if not found
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
				int idx = (int) asLong(args[0]);
				if (idx < 1 || idx > source.length()) {
					yield OclInvalid.INSTANCE;
				}
				yield String.valueOf(source.charAt(idx - 1));
			}
			case "characters" -> {
				List<String> chars = new ArrayList<>(source.length());
				for (int i = 0; i < source.length(); i++) {
					chars.add(String.valueOf(source.charAt(i)));
				}
				yield chars;
			}
			case "toInteger" -> {
				try {
					yield Long.parseLong(source.trim());
				} catch (NumberFormatException e) {
					yield OclInvalid.INSTANCE;
				}
			}
			case "toReal" -> {
				try {
					yield Double.parseDouble(source.trim());
				} catch (NumberFormatException e) {
					yield OclInvalid.INSTANCE;
				}
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
				try {
					yield source.replaceAll(pattern, asString(args[1]));
				} catch (PatternSyntaxException e) {
					yield OclInvalid.INSTANCE;
				}
			}
			case "replaceFirst" -> {
				String pattern = asString(args[0]);
				if (pattern.length() > options.maxRegexLength()) {
					yield OclInvalid.INSTANCE;
				}
				try {
					yield source.replaceFirst(pattern, asString(args[1]));
				} catch (PatternSyntaxException e) {
					yield OclInvalid.INSTANCE;
				}
			}
			case "equalsIgnoreCase" -> source.equalsIgnoreCase(asString(args[0]));
			case "startsWith" -> source.startsWith(asString(args[0]));
			case "endsWith" -> source.endsWith(asString(args[0]));
			case "<" -> source.compareTo(asString(args[0])) < 0;
			case "<=" -> source.compareTo(asString(args[0])) <= 0;
			case ">" -> source.compareTo(asString(args[0])) > 0;
			case ">=" -> source.compareTo(asString(args[0])) >= 0;
			case "toString" -> source;
			// QVT-O §8.3.16.2: length (deprecated synonym for size)
			case "length" -> (long) source.length();
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
			case "firstToUpper" -> source.isEmpty() ? source
					: Character.toUpperCase(source.charAt(0)) + source.substring(1);
			// QVT-O §8.3.16.8: lastToUpper
			case "lastToUpper" -> source.isEmpty() ? source
					: source.substring(0, source.length() - 1)
					+ Character.toUpperCase(source.charAt(source.length() - 1));
			// QVT-O §8.3.16.9: indexOf — 1-indexed, returns -1 if not found
			// (Note: OCL indexOf returns 0 if not found; QVT-O version returns -1)
			// QVT-O §8.3.16.13: normalizeSpace
			case "normalizeSpace" -> source.trim().replaceAll("\\s+", " ");
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
			// QVT-O §8.3.16.17: find — 1-indexed, returns -1 if not found
			case "find" -> {
				int idx = source.indexOf(asString(args[0]));
				yield (long) (idx < 0 ? -1 : idx + 1);
			}
			// QVT-O §8.3.16.18: rfind — 1-indexed from right, returns -1 if not found
			case "rfind" -> {
				int idx = source.lastIndexOf(asString(args[0]));
				yield (long) (idx < 0 ? -1 : idx + 1);
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
			case "matchInteger" -> {
				try {
					Long.parseLong(source.trim());
					yield true;
				} catch (NumberFormatException e) {
					yield false;
				}
			}
			// QVT-O §8.3.16.24/25: matchFloat/matchReal
			case "matchFloat", "matchReal" -> {
				try {
					Double.parseDouble(source.trim());
					yield true;
				} catch (NumberFormatException e) {
					yield false;
				}
			}
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
			case "asInteger" -> {
				try {
					yield Long.parseLong(source.trim());
				} catch (NumberFormatException e) {
					yield null;
				}
			}
			// QVT-O §8.3.16.29/30: asFloat/asReal — returns null on failure
			case "asFloat", "asReal" -> {
				try {
					yield Double.parseDouble(source.trim());
				} catch (NumberFormatException e) {
					yield null;
				}
			}
			// QVT-O §8.3.16.1 + §8.4.4: format / % — placeholder substitution
			case "format", "%" -> {
				try {
					Object arg = args[0];
					if (arg instanceof Collection<?> c) {
						yield String.format(source, c.toArray());
					} else {
						yield String.format(source, arg);
					}
				} catch (Exception e) {
					yield OclInvalid.INSTANCE;
				}
			}
			default -> NOT_FOUND;
		};
	}

	// --- Collection (OCL v2.4 Section 11.7) ---

	private static Object dispatchCollection(String name, Collection<?> source, Object[] args,
			OclEvaluationOptions options) {
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
				flatten(source, flat);
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
				if (source instanceof Set<?> && !(source instanceof OclOrderedSet<?>)) {
					Set<Object> result = new OclSet<>(source);
					result.retainAll(other);
					yield result;
				}
				if (source instanceof OclBag<?> && other instanceof Set<?>) {
					// Bag->intersection(Set) → Set
					OclSet<Object> result = new OclSet<>();
					for (Object e : source) {
						if (oclContains(other, e)) {
							result.add(e); // OclSet deduplicates
						}
					}
					yield result;
				}
				// Bag->intersection(Bag): frequency-based (min frequency)
				// Other: frequency-based intersection
				List<Object> remaining = new ArrayList<>(other);
				List<Object> result;
				if (source instanceof OclBag<?>) {
					result = new OclBag<>();
				} else if (source instanceof OclOrderedSet<?>) {
					result = new OclOrderedSet<>();
				} else {
					result = new ArrayList<>();
				}
				for (Object e : source) {
					if (remaining.remove(e)) {
						result.add(e);
					}
				}
				yield result;
			}
			case "-" -> {
				if (!(args[0] instanceof Collection<?> other)) yield OclInvalid.INSTANCE;
				if (source instanceof Set<?> && !(source instanceof OclOrderedSet<?>)) {
					Set<Object> result = new OclSet<>(source);
					result.removeAll(other);
					yield result;
				}
				// Bag/Sequence: frequency-based subtraction (subtract per occurrence)
				List<Object> toRemove = new ArrayList<>(other);
				List<Object> result;
				if (source instanceof OclBag<?>) {
					result = new OclBag<>(source);
				} else if (source instanceof OclOrderedSet<?>) {
					result = new OclOrderedSet<>(source);
				} else {
					result = new ArrayList<>(source);
				}
				for (Object e : toRemove) {
					result.remove(e);
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
				long longSum = 0;
				double doubleSum = 0;
				boolean allLong = true;
				for (Object e : source) {
					if (e instanceof Long l) {
						longSum += l;
						doubleSum += l;
					} else if (e instanceof Number n) {
						doubleSum += n.doubleValue();
						allLong = false;
					} else {
						yield OclInvalid.INSTANCE;
					}
				}
				if (allLong) {
					yield longSum;
				}
				yield doubleSum;
			}
			case "max" -> {
				Object max = null;
				for (Object e : source) {
					if (!(e instanceof Comparable)) yield OclInvalid.INSTANCE;
					if (max == null || compareOcl(e, max) > 0) {
						max = e;
					}
				}
				yield max == null ? OclInvalid.INSTANCE : max;
			}
			case "min" -> {
				Object min = null;
				for (Object e : source) {
					if (!(e instanceof Comparable)) yield OclInvalid.INSTANCE;
					if (min == null || compareOcl(e, min) < 0) {
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
				@SuppressWarnings("unchecked")
				var list = (Collection<Object>) source;
				list.add(args[0]);
				yield null; // returns Void
			}
			// QVT-O §8.3.9.35: List::remove(element) — mutating remove ALL equal elements
			case "remove" -> {
				if (source instanceof List<?> list) {
					list.removeIf(e -> Objects.equals(e, args[0]));
				}
				yield null; // returns Void
			}
			// QVT-O §8.3.9.38: List::removeAt(index) — mutating remove at 1-based index, returns removed
			case "removeAt" -> {
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
				if (source instanceof List<?> list && !list.isEmpty()) {
					yield list.remove(0);
				}
				yield OclInvalid.INSTANCE;
			}
			// QVT-O §8.3.9.40: List::removeLast() — mutating remove last, returns removed
			case "removeLast" -> {
				if (source instanceof List<?> list && !list.isEmpty()) {
					yield list.remove(list.size() - 1);
				}
				yield OclInvalid.INSTANCE;
			}
			// QVT-O §8.3.9.36: List::removeAll(elements) — mutating remove all matching
			case "removeAll" -> {
				if (args[0] instanceof Collection<?> toRemove && source instanceof List<?> list) {
					list.removeIf(e -> toRemove.stream().anyMatch(r -> Objects.equals(e, r)));
				}
				yield null; // returns Void
			}
			// QVT-O §8.3.9.28: List::joinfields(sep, begin, end) — string join
			case "joinfields" -> {
				String sep = args.length > 0 ? String.valueOf(args[0]) : "";
				String begin = args.length > 1 ? String.valueOf(args[1]) : "";
				String end = args.length > 2 ? String.valueOf(args[2]) : "";
				StringBuilder sb = new StringBuilder(begin);
				int pos = 0;
				for (Object e : source) {
					if (pos++ > 0) sb.append(sep);
					sb.append(String.valueOf(e));
				}
				sb.append(end);
				yield sb.toString();
			}
			default -> NOT_FOUND;
		};
	}

	// --- Ordered Collection: Sequence, OrderedSet (OCL v2.4 Section 11.7.3/11.7.4) ---

	private static Object dispatchOrderedCollection(String name, List<?> source, Object[] args) {
		return switch (name) {
			case "first" -> source.isEmpty() ? OclInvalid.INSTANCE : source.get(0);
			case "last" -> source.isEmpty() ? OclInvalid.INSTANCE : source.get(source.size() - 1);
			case "at" -> {
				int idx = (int) asLong(args[0]);
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
				int idx = (int) asLong(args[0]);
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
				int lower = (int) asLong(args[0]);
				int upper = (int) asLong(args[1]);
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

	private static Object dispatchMap(String name, Map<?, ?> source, Object[] args) {
		return switch (name) {
			case "size" -> (long) source.size();
			case "isEmpty" -> source.isEmpty();
			case "notEmpty" -> !source.isEmpty();
			case "includes" -> source.containsKey(args[0]);
			case "excludes" -> !source.containsKey(args[0]);
			case "includesValue" -> source.containsValue(args[0]);
			case "excludesValue" -> !source.containsValue(args[0]);
			case "get", "at" -> source.get(args[0]);
			case "keys" -> new OclSet<>(source.keySet());
			case "values" -> new ArrayList<>(source.values());
			case "including" -> {
				Map<Object, Object> result = new LinkedHashMap<>(source);
				result.put(args[0], args[1]);
				yield result;
			}
			case "excluding" -> {
				Map<Object, Object> result = new LinkedHashMap<>(source);
				result.remove(args[0]);
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
			case "hasKey" -> source.containsKey(args[0]);
			// QVT-O §8.3.8.3: Dict::defaultget(k, default) — get with fallback
			case "defaultget" -> {
				Object val = source.get(args[0]);
				yield val != null && source.containsKey(args[0]) ? val : args[1];
			}
			// QVT-O §8.3.8.4: Dict::put(k, v) — mutable insert/update
			case "put" -> {
				@SuppressWarnings("unchecked")
				var mutableMap = (Map<Object, Object>) source;
				mutableMap.put(args[0], args[1]);
				yield null; // returns Void
			}
			// QVT-O §8.3.8.5: Dict::clear() — mutable remove all
			case "clear" -> {
				source.clear();
				yield null; // returns Void
			}
			default -> NOT_FOUND;
		};
	}

	private static int compareOcl(Object a, Object b) {
		return OclCollectionUtil.compareOcl(a, b);
	}

	// --- Collection helpers ---

	private static Collection<Object> preserveCollectionKind(Collection<?> source, List<Object> elements) {
		return OclCollectionUtil.preserveCollectionKind(source, elements);
	}

	private static void flatten(Collection<?> source, List<Object> result) {
		for (Object element : source) {
			if (element instanceof Collection<?> nested) {
				flatten(nested, result);
			} else {
				result.add(element);
			}
		}
	}

	// --- Type coercion helpers (safe — no ClassCastException) ---

	private static boolean asBoolean(Object value) {
		return value instanceof Boolean b ? b : false;
	}

	private static long asLong(Object value) {
		if (value instanceof Long l) return l;
		if (value instanceof Integer i) return i;
		if (value instanceof Number n) return n.longValue();
		return 0L;
	}

	private static Number asNumber(Object value) {
		return value instanceof Number n ? n : 0L;
	}

	private static String asString(Object value) {
		return value instanceof String s ? s : String.valueOf(value);
	}
}
