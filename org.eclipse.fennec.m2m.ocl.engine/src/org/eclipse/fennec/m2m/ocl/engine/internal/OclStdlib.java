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
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.regex.PatternSyntaxException;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2m.model.ocl.ClassifierType;
import org.eclipse.fennec.m2m.model.ocl.OclType;
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

		return NOT_FOUND;
	}

	// --- OclAny (OCL v2.4 Section 11.2) ---

	private static Object dispatchOclAny(String name, Object source, Object[] args) {
		return switch (name) {
			case "=" -> oclEquals(source, args[0]);
			case "<>" -> !((Boolean) oclEquals(source, args[0]));
			case "oclIsUndefined" -> source == null;
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
			case "toString" -> String.valueOf(source);
			default -> NOT_FOUND;
		};
	}

	private static Boolean oclEquals(Object left, Object right) {
		if (left instanceof EObject le && right instanceof EObject re) {
			return org.eclipse.emf.ecore.util.EcoreUtil.equals(le, re);
		}
		return Objects.equals(left, right);
	}

	private static Boolean oclIsKindOf(Object source, Object typeArg) {
		EClassifier classifier = extractClassifier(typeArg);
		if (classifier == null) {
			return false;
		}
		return classifier.isInstance(source);
	}

	private static Boolean oclIsTypeOf(Object source, Object typeArg) {
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
			case "toString" -> source.toString();
			default -> NOT_FOUND;
		};
	}

	// --- String (OCL v2.4 Section 11.7) ---

	@SuppressWarnings("unchecked")
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
			case "<" -> source.compareTo(asString(args[0])) < 0;
			case "<=" -> source.compareTo(asString(args[0])) <= 0;
			case ">" -> source.compareTo(asString(args[0])) > 0;
			case ">=" -> source.compareTo(asString(args[0])) >= 0;
			case "toString" -> source;
			default -> NOT_FOUND;
		};
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
