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
package org.eclipse.fennec.m2x.ocl.api;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Objects;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Converts OCL values where they leave OCL — into a model, into text.
 *
 * <p>Inside an evaluation an OCL Integer is a {@code Long}, or a {@link BigInteger} beyond the
 * 64-bit range, and an OCL Real is a {@link BigDecimal} (OCL v2.4 §11.4.1, §11.4.2, #322). A
 * model holds {@code int}, {@code double} and the other Java types its attributes declare, so a
 * value written into a feature, or returned by a derived feature or an operation, is converted
 * to the instance class of the target type; a value written into text uses the OCL notation.
 *
 * @since 1.0
 */
public final class OclValues {

	private OclValues() {
	}

	/**
	 * Converts a number to the instance class of an Ecore type, for {@code eSet} or for the
	 * result of a derived feature or an operation. Anything that is no number, or a target
	 * type that is no number type, passes through.
	 *
	 * @param value the OCL value, may be {@code null}
	 * @param type the target type, may be {@code null}
	 * @return the value as the target's instance class where that is a number type
	 */
	public static Object toEcore(Object value, EClassifier type) {
		if (!(value instanceof Number number) || type == null || type.getInstanceClass() == null) {
			return value;
		}
		return toInstanceOf(number, type.getInstanceClass());
	}

	/**
	 * Converts a number to a Java number type: the primitive and boxed integer and floating
	 * types, {@link BigInteger} and {@link BigDecimal}. Any other target leaves it unchanged.
	 *
	 * @param number the number
	 * @param target the target class
	 * @return the converted number
	 */
	public static Object toInstanceOf(Number number, Class<?> target) {
		Objects.requireNonNull(number, "number must not be null");
		Objects.requireNonNull(target, "target must not be null");
		if (target == int.class || target == Integer.class) {
			return number.intValue();
		}
		if (target == long.class || target == Long.class) {
			return number.longValue();
		}
		if (target == double.class || target == Double.class) {
			return number.doubleValue();
		}
		if (target == float.class || target == Float.class) {
			return number.floatValue();
		}
		if (target == short.class || target == Short.class) {
			return number.shortValue();
		}
		if (target == byte.class || target == Byte.class) {
			return number.byteValue();
		}
		if (target == BigDecimal.class) {
			return number instanceof BigDecimal decimal ? decimal
					: number instanceof BigInteger big ? new BigDecimal(big) : BigDecimal.valueOf(number.doubleValue());
		}
		if (target == BigInteger.class) {
			return number instanceof BigInteger big ? big
					: number instanceof BigDecimal decimal ? decimal.toBigInteger() : BigInteger.valueOf(number.longValue());
		}
		return number;
	}

	/**
	 * The text of a value as OCL's {@code toString} writes it: a Real in plain decimal notation
	 * with at least one fraction digit ({@code 3.0}, {@code 0.1},
	 * {@code 100000000000000000000.0}), anything else by its {@code toString}.
	 *
	 * @param value the value, must not be {@code null}
	 * @return its text
	 */
	public static String toText(Object value) {
		Objects.requireNonNull(value, "value must not be null");
		if (value instanceof BigDecimal decimal) {
			BigDecimal stripped = decimal.signum() == 0 ? BigDecimal.ZERO : decimal.stripTrailingZeros();
			String plain = stripped.toPlainString();
			return stripped.scale() <= 0 ? plain + ".0" : plain;
		}
		return value.toString();
	}

	/**
	 * QVT v1.3 §8.3.3 {@code Object::repr()}: the text of a value, and for a model object its
	 * class with its name — {@code Person 'Ann'}, or {@code Person} without a name — instead of
	 * Java's {@code DynamicEObjectImpl@1b2c (eClass: …)} (#345). What {@code log} and the
	 * {@code %s} of {@code String::format} print.
	 *
	 * @param value the value, may be {@code null}
	 * @return its representation, {@code "null"} for null
	 */
	public static String repr(Object value) {
		if (value == null) {
			return "null";
		}
		if (value instanceof EObject eObject) {
			EStructuralFeature nameFeature = eObject.eClass().getEStructuralFeature("name");
			Object name = nameFeature != null && !nameFeature.isMany() ? eObject.eGet(nameFeature) : null;
			String className = eObject.eClass().getName();
			return name != null ? className + " '" + name + "'" : className;
		}
		return toText(value);
	}
}
