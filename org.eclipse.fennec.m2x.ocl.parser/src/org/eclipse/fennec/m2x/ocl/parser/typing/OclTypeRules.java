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

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayDeque;
import java.util.Deque;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.fennec.m2x.model.ocl.AnyType;
import org.eclipse.fennec.m2x.model.ocl.CollectionKind;
import org.eclipse.fennec.m2x.model.ocl.CollectionType;
import org.eclipse.fennec.m2x.model.ocl.InvalidType;
import org.eclipse.fennec.m2x.model.ocl.MapType;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.PrimitiveType;
import org.eclipse.fennec.m2x.model.ocl.TuplePart;
import org.eclipse.fennec.m2x.model.ocl.TupleType;
import org.eclipse.fennec.m2x.model.ocl.VoidType;
import org.eclipse.fennec.m2x.ocl.api.OclStandardLibrary;

/**
 * Type conformance and the common supertype (OCL v2.4 §8.2, §11.2–§11.7).
 *
 * <p>Typing is gradual: {@code null} stands for a type the parser does not know — an external
 * variable, a blackbox result, a value of a dynamic model — and conforms to everything, so an
 * unknown type never produces a finding. What is reported is only what is known to be wrong.
 *
 * <p>An Ecore data type is read as the OCL type it stands for: {@code EString} is
 * {@code String}, {@code EInt} and its kin are {@code Integer}, {@code EDouble} and
 * {@code EFloat} are {@code Real}, {@code EBoolean} is {@code Boolean} — the same equivalence
 * the evaluator applies to values.
 */
public final class OclTypeRules {

	private static final OclStandardLibrary LIBRARY = OclStandardLibrary.INSTANCE;
	private static final OclFactory FACTORY = OclFactory.eINSTANCE;

	private static final Map<Class<?>, String> PRIMITIVE_BY_CLASS = Map.ofEntries(
			Map.entry(String.class, "String"), Map.entry(Character.class, "String"), Map.entry(char.class, "String"),
			Map.entry(Boolean.class, "Boolean"), Map.entry(boolean.class, "Boolean"),
			Map.entry(Integer.class, "Integer"), Map.entry(int.class, "Integer"),
			Map.entry(Long.class, "Integer"), Map.entry(long.class, "Integer"),
			Map.entry(Short.class, "Integer"), Map.entry(short.class, "Integer"),
			Map.entry(Byte.class, "Integer"), Map.entry(byte.class, "Integer"),
			Map.entry(BigInteger.class, "Integer"),
			Map.entry(Double.class, "Real"), Map.entry(double.class, "Real"),
			Map.entry(Float.class, "Real"), Map.entry(float.class, "Real"),
			Map.entry(BigDecimal.class, "Real"));

	private OclTypeRules() {
	}

	/**
	 * The OCL type an Ecore classifier stands for: a data type of a primitive instance class is
	 * the library's primitive type; everything else is itself.
	 *
	 * @param type the type, may be {@code null}
	 * @return the normalized type, {@code null} for {@code null} and for a type that says nothing
	 *         about its values ({@code EJavaObject})
	 */
	public static EClassifier normalize(EClassifier type) {
		if (type instanceof EDataType dataType && !(type instanceof EEnum) && !(type instanceof PrimitiveType)
				&& !(type instanceof CollectionType) && !(type instanceof TupleType) && !(type instanceof MapType)) {
			Class<?> instanceClass = dataType.getInstanceClass();
			String name = instanceClass != null ? PRIMITIVE_BY_CLASS.get(instanceClass) : null;
			if (name != null) {
				return LIBRARY.primitive(name).orElseThrow();
			}
		}
		if (type instanceof EDataType dataType && !(type instanceof EEnum) && !(type instanceof OclType)
				&& dataType.getEPackage() == null && dataType.getInstanceClass() == null && type.getName() != null) {
			// a data type standing for a library type by name, as QVT-O stores the primitive
			// context and parameter types of its operations (§8.2.1.10)
			EClassifier named = LIBRARY.type(type.getName()).orElse(null);
			if (named != null) {
				return named;
			}
		}
		if (type == EcorePackage.Literals.EJAVA_OBJECT || type == EcorePackage.Literals.EJAVA_CLASS) {
			// "any Java object" says nothing about the value: unknown, not OclAny, which would make
			// every use of it an error
			return null;
		}
		return type;
	}

	/**
	 * Whether a value of the one type may stand where the other is expected (§8.2.1, §11.2).
	 *
	 * @param actual the type of the value, {@code null} if unknown
	 * @param expected the expected type, {@code null} if unknown
	 * @return {@code true} unless both types are known and the first does not conform
	 */
	public static boolean conformsTo(EClassifier actual, EClassifier expected) {
		EClassifier a = normalize(actual);
		EClassifier e = normalize(expected);
		if (a == null || e == null || a == e) {
			return true;
		}
		// OclVoid and OclInvalid conform to every type (§11.2.2, §11.2.3)
		if (a instanceof VoidType || a instanceof InvalidType) {
			return true;
		}
		if (e instanceof AnyType) {
			return !(a instanceof InvalidType);
		}
		if (e == EcorePackage.Literals.EOBJECT) {
			return a instanceof EClass;
		}
		if (a instanceof PrimitiveType pa && e instanceof PrimitiveType pe) {
			return primitiveConforms(pa.getName(), pe.getName());
		}
		if (a instanceof CollectionType ca && e instanceof CollectionType ce) {
			return kindConforms(ca.getKind(), ce.getKind()) && conformsTo(ca.getElementType(), ce.getElementType());
		}
		if (a instanceof TupleType ta && e instanceof TupleType te) {
			return tupleConforms(ta, te);
		}
		if (a instanceof MapType ma && e instanceof MapType me) {
			return conformsTo(ma.getKeyType(), me.getKeyType()) && conformsTo(ma.getValueType(), me.getValueType());
		}
		if (a instanceof EClass ca && e instanceof EClass ce) {
			return ce.isSuperTypeOf(ca);
		}
		if (a instanceof EEnum || e instanceof EEnum) {
			return sameClassifier(a, e);
		}
		if (a instanceof EDataType && e instanceof EDataType) {
			// two data types of no primitive meaning — compared by what they carry
			return sameClassifier(a, e) || instanceClassConforms((EDataType) a, (EDataType) e);
		}
		return false;
	}

	private static boolean primitiveConforms(String actual, String expected) {
		return switch (actual) {
			// §11.5.4: UnlimitedNatural conforms to Integer, and Integer to Real
			case "UnlimitedNatural" -> expected.equals("Integer") || expected.equals("Real");
			case "Integer" -> expected.equals("Real");
			default -> false;
		};
	}

	private static boolean kindConforms(CollectionKind actual, CollectionKind expected) {
		return expected == CollectionKind.COLLECTION || actual == expected;
	}

	private static boolean tupleConforms(TupleType actual, TupleType expected) {
		if (actual.getOwnedParts().size() != expected.getOwnedParts().size()) {
			return false;
		}
		for (TuplePart part : expected.getOwnedParts()) {
			TuplePart match = part(actual, part.getName());
			if (match == null || !conformsTo(match.getType(), part.getType())) {
				return false;
			}
		}
		return true;
	}

	private static TuplePart part(TupleType tuple, String name) {
		for (TuplePart part : tuple.getOwnedParts()) {
			if (name != null && name.equals(part.getName())) {
				return part;
			}
		}
		return null;
	}

	private static boolean sameClassifier(EClassifier a, EClassifier b) {
		return a == b || (a.getName() != null && a.getName().equals(b.getName()) && a.getEPackage() != null
				&& b.getEPackage() != null && a.getEPackage().getNsURI() != null
				&& a.getEPackage().getNsURI().equals(b.getEPackage().getNsURI()));
	}

	private static boolean instanceClassConforms(EDataType actual, EDataType expected) {
		Class<?> a = actual.getInstanceClass();
		Class<?> e = expected.getInstanceClass();
		return a == null || e == null || e.isAssignableFrom(a);
	}

	/**
	 * The most specific type both given types conform to (§8.2.1 commonSuperType): the type of
	 * an {@code if} with these branches, of a collection literal with these parts.
	 *
	 * @param a one type, {@code null} if unknown
	 * @param b the other, {@code null} if unknown
	 * @return the common supertype, {@code null} if either is unknown
	 */
	public static EClassifier commonSuperType(EClassifier a, EClassifier b) {
		EClassifier na = normalize(a);
		EClassifier nb = normalize(b);
		if (na == null || nb == null) {
			return null;
		}
		if (na instanceof VoidType || na instanceof InvalidType) {
			return nb;
		}
		if (nb instanceof VoidType || nb instanceof InvalidType) {
			return na;
		}
		if (conformsTo(na, nb)) {
			return nb;
		}
		if (conformsTo(nb, na)) {
			return na;
		}
		if (na instanceof CollectionType ca && nb instanceof CollectionType cb) {
			CollectionKind kind = ca.getKind() == cb.getKind() ? ca.getKind() : CollectionKind.COLLECTION;
			EClassifier element = ca.getElementType() == null || cb.getElementType() == null ? null
					: commonSuperType(ca.getElementType(), cb.getElementType());
			return collectionType(kind, element);
		}
		if (na instanceof EClass ca && nb instanceof EClass cb) {
			EClass common = commonSuperClass(ca, cb);
			return common != null ? common : LIBRARY.oclAny();
		}
		return LIBRARY.oclAny();
	}

	/** The nearest class both classes specialize, breadth first from the first one. */
	private static EClass commonSuperClass(EClass a, EClass b) {
		Deque<EClass> queue = new ArrayDeque<>(List.of(a));
		Set<EClass> seen = new HashSet<>();
		while (!queue.isEmpty()) {
			EClass current = queue.poll();
			if (!seen.add(current)) {
				continue;
			}
			if (current.isSuperTypeOf(b)) {
				return current;
			}
			queue.addAll(current.getESuperTypes());
		}
		return null;
	}

	/**
	 * The common supertype of several types.
	 *
	 * @param types the types; an unknown one makes the result unknown
	 * @return the common supertype, {@code null} if the list is empty or a type is unknown
	 */
	public static EClassifier commonSuperType(List<? extends EClassifier> types) {
		if (types.isEmpty()) {
			return null;
		}
		Set<EClassifier> distinct = new LinkedHashSet<>(types);
		EClassifier result = null;
		boolean first = true;
		for (EClassifier type : distinct) {
			if (type == null) {
				return null;
			}
			result = first ? normalize(type) : commonSuperType(result, type);
			first = false;
			if (result == null) {
				return null;
			}
		}
		return result;
	}

	/**
	 * A new collection type of the given kind — the subclass of {@code CollectionType} the kind
	 * names, so that a type built here is the type the builder would build.
	 *
	 * @param kind the kind
	 * @param elementType the element type, {@code null} if unknown
	 * @return the collection type
	 */
	public static CollectionType collectionType(CollectionKind kind, EClassifier elementType) {
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
	 * A readable name for a type, for diagnostics: {@code Set(Integer)}, {@code Person}.
	 *
	 * @param type the type, may be {@code null}
	 * @return its name, {@code ?} for an unknown type
	 */
	public static String nameOf(EClassifier type) {
		EClassifier t = normalize(type);
		if (t == null) {
			return "?";
		}
		if (t instanceof CollectionType c) {
			String kind = switch (c.getKind()) {
				case SET -> "Set";
				case ORDERED_SET -> "OrderedSet";
				case BAG -> "Bag";
				case SEQUENCE -> "Sequence";
				default -> "Collection";
			};
			return kind + "(" + nameOf(c.getElementType()) + ")";
		}
		if (t instanceof TupleType tuple) {
			StringBuilder name = new StringBuilder("Tuple(");
			for (int i = 0; i < tuple.getOwnedParts().size(); i++) {
				TuplePart part = tuple.getOwnedParts().get(i);
				name.append(i == 0 ? "" : ", ").append(part.getName()).append(" : ").append(nameOf(part.getType()));
			}
			return name.append(')').toString();
		}
		if (t instanceof MapType map) {
			return "Dict(" + nameOf(map.getKeyType()) + ", " + nameOf(map.getValueType()) + ")";
		}
		return t.getName();
	}
}
