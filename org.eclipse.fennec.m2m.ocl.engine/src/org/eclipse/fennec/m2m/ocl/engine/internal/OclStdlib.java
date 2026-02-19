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
package org.eclipse.fennec.m2m.ocl.engine.internal;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.ocl.ClassifierType;
import org.eclipse.fennec.m2m.model.ocl.CollectionType;
import org.eclipse.fennec.m2m.model.ocl.OclFactory;
import org.eclipse.fennec.m2m.model.ocl.OclType;
import org.eclipse.fennec.m2m.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2m.ocl.api.OclInvalid;

/**
 * OCL Standard Library operation dispatch.
 *
 * <p>Implements all built-in operations for OclAny, Boolean, Integer, Real,
 * and String as defined in the OCL v2.4 specification (Sections 11.2–11.7).
 *
 * <p>Dispatch is performed by runtime type of the source value, then by
 * operation name using switch expressions. This is deliberately simple
 * and avoids reflection or map-based lookup for performance.
 *
 * <p>All methods return {@code null} to signal "operation not found",
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
	 * @return the result, or {@link #NOT_FOUND} if the operation is not a stdlib operation
	 */
	static Object dispatch(String name, Object source, Object[] args) {
		// OclAny operations apply to all types (including null/invalid)
		Object result = dispatchOclAny(name, source, args);
		if (result != NOT_FOUND) {
			return result;
		}

		// Type-specific dispatch
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
			return dispatchString(name, s, args);
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
			return dispatchCollection(name, c, args);
		}

		return NOT_FOUND;
	}

	// --- OclAny (OCL v2.4 Section 11.2) ---

	private static Object dispatchOclAny(String name, Object source, Object[] args) {
		return switch (name) {
			case "=" -> oclEquals(source, args[0]);
			case "<>" -> !((Boolean) oclEquals(source, args[0]));
			case "oclIsUndefined" -> source == null || source == OclInvalid.INSTANCE;
			case "oclIsInvalid" -> source == OclInvalid.INSTANCE;
			case "oclIsKindOf" -> oclIsKindOf(source, args[0]);
			case "oclIsTypeOf" -> oclIsTypeOf(source, args[0]);
			case "oclAsType" -> oclAsType(source, args[0]);
			case "oclAsSet" -> {
				Set<Object> set = new LinkedHashSet<>();
				if (source != null) {
					set.add(source);
				}
				yield set;
			}
			case "oclType" -> oclType(source);
			case "toString" -> String.valueOf(source);
			default -> NOT_FOUND;
		};
	}

	private static Object oclType(Object source) {
		if (source == null) return OclInvalid.INSTANCE;
		if (source == OclInvalid.INSTANCE) return OclInvalid.INSTANCE;
		if (source instanceof EObject eo) return eo.eClass();
		if (source instanceof Long || source instanceof Integer) return createPrimitiveType("Integer");
		if (source instanceof Double || source instanceof Float) return createPrimitiveType("Real");
		if (source instanceof String) return createPrimitiveType("String");
		if (source instanceof Boolean) return createPrimitiveType("Boolean");
		if (source instanceof Collection<?>) return createPrimitiveType("Collection");
		if (source instanceof Map<?, ?>) return createPrimitiveType("Map");
		return OclInvalid.INSTANCE;
	}

	private static PrimitiveType createPrimitiveType(String name) {
		PrimitiveType pt = OclFactory.eINSTANCE.createPrimitiveType();
		pt.setName(name);
		return pt;
	}

	private static Boolean oclEquals(Object left, Object right) {
		// Collection equality — kind must match
		if (left instanceof Collection<?> || right instanceof Collection<?>) {
			return collectionEquals(left, right);
		}
		if (left instanceof EObject le && right instanceof EObject re) {
			return org.eclipse.emf.ecore.util.EcoreUtil.equals(le, re);
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
				if (oclEquals(e, r)) { found = true; break; }
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
			if (!oclEquals(li.next(), ri.next())) return false;
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
				if (oclEquals(e, remaining.get(i))) {
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
		return col.stream().anyMatch(e -> oclEquals(e, element));
	}

	private static Object oclIsKindOf(Object source, Object typeArg) {
		if (source == OclInvalid.INSTANCE) {
			return OclInvalid.INSTANCE;
		}
		// Check primitive type match first
		if (typeArg instanceof PrimitiveType pt) {
			return matchesPrimitiveType(source, pt.getName());
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
		if (source == OclInvalid.INSTANCE) {
			return OclInvalid.INSTANCE;
		}
		// Check primitive type match first
		if (typeArg instanceof PrimitiveType pt) {
			return matchesPrimitiveType(source, pt.getName());
		}
		EClassifier classifier = extractClassifier(typeArg);
		if (classifier == null || source == null) {
			return false;
		}
		if (source instanceof EObject eo) {
			return eo.eClass() == classifier;
		}
		return classifier.isInstance(source) && classifier.getInstanceClass() != null
				&& classifier.getInstanceClass().equals(source.getClass());
	}

	private static boolean matchesPrimitiveType(Object source, String typeName) {
		return switch (typeName) {
			case "Integer" -> source instanceof Long || source instanceof Integer;
			case "Real" -> source instanceof Double || source instanceof Float;
			case "String" -> source instanceof String;
			case "Boolean" -> source instanceof Boolean;
			case "UnlimitedNatural" -> source instanceof Long || source instanceof Integer;
			case "OclVoid" -> source == null;
			default -> false;
		};
	}

	private static Object oclAsType(Object source, Object typeArg) {
		EClassifier classifier = extractClassifier(typeArg);
		if (classifier == null) {
			return OclInvalid.INSTANCE;
		}
		if (classifier.isInstance(source)) {
			return source;
		}
		return OclInvalid.INSTANCE;
	}

	private static EClassifier extractClassifier(Object typeArg) {
		if (typeArg instanceof EClassifier ec) {
			return ec;
		}
		if (typeArg instanceof ClassifierType ct) {
			return ct.getReferredClassifier();
		}
		if (typeArg instanceof OclType) {
			// Primitive types — cannot extract EClassifier
			return null;
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

	// --- Integer (OCL v2.4 Section 11.5) ---

	private static Object dispatchInteger(String name, Long source, Object[] args) {
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
			case "toString" -> source.toString();
			default -> NOT_FOUND;
		};
	}

	// --- Real (OCL v2.4 Section 11.5) ---

	private static Object dispatchReal(String name, Double source, Object[] args) {
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

	private static Object dispatchString(String name, String source, Object[] args) {
		return switch (name) {
			case "size" -> (long) source.length();
			case "+" , "concat" -> source + asString(args[0]);
			case "substring" -> {
				int lower = (int) asLong(args[0]);
				int upper = (int) asLong(args[1]);
				// OCL is 1-based, inclusive on both ends
				if (lower < 1 || upper > source.length() || lower > upper + 1) {
					yield OclInvalid.INSTANCE;
				}
				yield source.substring(lower - 1, upper);
			}
			case "toUpperCase" -> source.toUpperCase();
			case "toLowerCase" -> source.toLowerCase();
			case "trim" -> source.trim();
			case "indexOf" -> {
				int idx = source.indexOf(asString(args[0]));
				yield (long) (idx + 1); // 0 means not found (1-based)
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
				String trimmed = source.trim().toLowerCase();
				if ("true".equals(trimmed)) yield true;
				if ("false".equals(trimmed)) yield false;
				yield OclInvalid.INSTANCE;
			}
			case "matches" -> {
				try {
					yield source.matches(asString(args[0]));
				} catch (PatternSyntaxException e) {
					yield OclInvalid.INSTANCE;
				}
			}
			case "replaceAll" -> {
				try {
					yield source.replaceAll(asString(args[0]), asString(args[1]));
				} catch (PatternSyntaxException e) {
					yield OclInvalid.INSTANCE;
				}
			}
			case "replaceFirst" -> {
				try {
					yield source.replaceFirst(asString(args[0]), asString(args[1]));
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
			default -> NOT_FOUND;
		};
	}

	// --- Collection (OCL v2.4 Section 11.7) ---

	private static Object dispatchCollection(String name, Collection<?> source, Object[] args) {
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
			case "count" -> source.stream().filter(e -> oclEquals(e, args[0])).count();
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
					Set<Object> result = new LinkedHashSet<>(source);
					result.add(args[0]);
					yield result;
				}
				List<Object> result = new ArrayList<>(source);
				result.add(args[0]);
				yield result;
			}
			case "excluding" -> {
				if (source instanceof OclOrderedSet<?>) {
					OclOrderedSet<Object> result = new OclOrderedSet<>(source);
					result.remove(args[0]);
					yield result;
				}
				if (source instanceof OclBag<?>) {
					OclBag<Object> result = new OclBag<>(source);
					result.remove(args[0]);
					yield result;
				}
				if (source instanceof Set<?>) {
					Set<Object> result = new LinkedHashSet<>(source);
					result.remove(args[0]);
					yield result;
				}
				List<Object> result = new ArrayList<>(source);
				result.remove(args[0]);
				yield result;
			}
			case "union" -> {
				if (!(args[0] instanceof Collection<?> other)) yield OclInvalid.INSTANCE;
				if (source instanceof Set<?> && !(source instanceof OclOrderedSet<?>)) {
					Set<Object> result = new LinkedHashSet<>(source);
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
				if (source instanceof Set<?> && !(source instanceof OclOrderedSet<?>)) {
					Set<Object> result = new LinkedHashSet<>(source);
					result.retainAll(other);
					yield result;
				}
				// Bag/Sequence: frequency-based intersection (min frequency)
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
					Set<Object> result = new LinkedHashSet<>(source);
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
				Set<Object> result = new LinkedHashSet<>(source);
				for (Object o : other) {
					if (!result.remove(o)) {
						result.add(o);
					}
				}
				yield result;
			}
			case "asSet" -> new LinkedHashSet<>(source);
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
				Set<Map<String, Object>> result = new LinkedHashSet<>();
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
				int idx = source.indexOf(args[0]);
				yield (long) (idx + 1); // 0 means not found (1-based)
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
						if (!oclEquals(e, args[0])) result.add(e);
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
						if (!oclEquals(e, args[0])) result.add(e);
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
				if (lower < 1 || upper > source.size() || lower > upper + 1) {
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
			case "keys" -> new LinkedHashSet<>(source.keySet());
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
			default -> NOT_FOUND;
		};
	}

	/** Cross-type numeric comparison for OCL: Long vs Double etc. */
	@SuppressWarnings("unchecked")
	private static int compareOcl(Object a, Object b) {
		if (a instanceof Number na && b instanceof Number nb) {
			return Double.compare(na.doubleValue(), nb.doubleValue());
		}
		if (a instanceof Comparable<?> ca) {
			try {
				return ((Comparable<Object>) ca).compareTo(b);
			} catch (ClassCastException e) {
				return 0;
			}
		}
		return 0;
	}

	// --- Collection helpers ---

	/** Returns a collection of the same kind as the source. */
	private static Collection<Object> preserveCollectionKind(Collection<?> source, List<Object> elements) {
		if (source instanceof OclOrderedSet<?>) return new OclOrderedSet<>(elements);
		if (source instanceof OclBag<?>) return new OclBag<>(elements);
		if (source instanceof Set<?>) return new LinkedHashSet<>(elements);
		return elements; // Sequence
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

	// --- Type coercion helpers ---

	private static boolean asBoolean(Object value) {
		return (Boolean) value;
	}

	private static long asLong(Object value) {
		if (value instanceof Long l) return l;
		if (value instanceof Integer i) return i;
		return ((Number) value).longValue();
	}

	private static Number asNumber(Object value) {
		return (Number) value;
	}

	private static String asString(Object value) {
		return (String) value;
	}
}
