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
import java.math.RoundingMode;

import org.eclipse.fennec.m2x.ocl.api.OclValues;

/**
 * The OCL number values (#322): Integer and Real are the mathematical concepts (OCL v2.4
 * §11.4.1, §11.4.2), not 64-bit machine numbers.
 *
 * <p>An Integer is a {@code Long} while it fits — the common case and the one EMF hands in —
 * and a {@link BigInteger} once an operation leaves the 64-bit range, as Eclipse OCL does. A
 * Real is a {@link BigDecimal}, so {@code 0.1 + 0.2 = 0.3} holds; a {@code Double} or
 * {@code Float} from a model is read as the decimal its shortest representation spells. NaN and
 * the infinities are no Real values.
 *
 * <p>Values are compared and hashed by their mathematical value: {@code 1 = 1.0}, and two
 * Integers beyond 2^53 that differ in their last digit are different (#317).
 */
final class OclNumbers {

	/** Division keeps at least this many fraction digits, as Eclipse OCL does. */
	private static final int MINIMUM_DIVISION_SCALE = 32;

	private static final BigInteger LONG_MIN = BigInteger.valueOf(Long.MIN_VALUE);
	private static final BigInteger LONG_MAX = BigInteger.valueOf(Long.MAX_VALUE);

	private OclNumbers() {
	}

	/** Whether the value is an OCL Integer: a whole-number Java type. */
	static boolean isInteger(Object value) {
		return value instanceof Long || value instanceof Integer || value instanceof Short
				|| value instanceof Byte || value instanceof BigInteger;
	}

	/** Whether the value is an OCL Real: a decimal or floating-point Java type. */
	static boolean isReal(Object value) {
		return value instanceof BigDecimal || value instanceof Double || value instanceof Float;
	}

	/**
	 * Whether the value is a finite number — NaN and the infinities of a model's EDouble are no
	 * Real values (§11.4.1).
	 */
	static boolean isFinite(Number value) {
		if (value instanceof Double d) {
			return Double.isFinite(d);
		}
		if (value instanceof Float f) {
			return Float.isFinite(f);
		}
		return true;
	}

	/** The integer as a {@code BigInteger}. */
	static BigInteger bigInteger(Number integer) {
		return integer instanceof BigInteger big ? big : BigInteger.valueOf(integer.longValue());
	}

	/**
	 * The number as a {@code BigDecimal}; a {@code Double} is taken by its shortest decimal
	 * representation, so the 0.1 a model stores is 0.1.
	 *
	 * @throws NumberFormatException for NaN and the infinities
	 */
	static BigDecimal bigDecimal(Number number) {
		if (number instanceof BigDecimal decimal) {
			return decimal;
		}
		if (number instanceof BigInteger big) {
			return new BigDecimal(big);
		}
		if (number instanceof Double || number instanceof Float) {
			return BigDecimal.valueOf(number.doubleValue());
		}
		return BigDecimal.valueOf(number.longValue());
	}

	/** An integer result: a {@code Long} when it fits, a {@code BigInteger} otherwise. */
	static Number integer(BigInteger value) {
		return value.compareTo(LONG_MIN) >= 0 && value.compareTo(LONG_MAX) <= 0
				? Long.valueOf(value.longValue()) : value;
	}

	/** A real result, without the trailing zeros an operation left behind. */
	static BigDecimal real(BigDecimal value) {
		return value.signum() == 0 ? BigDecimal.ZERO : value.stripTrailingZeros();
	}

	static Number add(Number left, Number right) {
		if (left instanceof Long || left instanceof Integer) {
			if (right instanceof Long || right instanceof Integer) {
				long l = left.longValue();
				long r = right.longValue();
				long sum = l + r;
				if (((l ^ sum) & (r ^ sum)) >= 0) {
					return sum;
				}
			}
		}
		if (isInteger(left) && isInteger(right)) {
			return integer(bigInteger(left).add(bigInteger(right)));
		}
		return real(bigDecimal(left).add(bigDecimal(right)));
	}

	static Number subtract(Number left, Number right) {
		if ((left instanceof Long || left instanceof Integer) && (right instanceof Long || right instanceof Integer)) {
			long l = left.longValue();
			long r = right.longValue();
			long difference = l - r;
			if (((l ^ r) & (l ^ difference)) >= 0) {
				return difference;
			}
		}
		if (isInteger(left) && isInteger(right)) {
			return integer(bigInteger(left).subtract(bigInteger(right)));
		}
		return real(bigDecimal(left).subtract(bigDecimal(right)));
	}

	static Number multiply(Number left, Number right) {
		if ((left instanceof Long || left instanceof Integer) && (right instanceof Long || right instanceof Integer)) {
			long l = left.longValue();
			long r = right.longValue();
			long high = Math.multiplyHigh(l, r);
			long product = l * r;
			if ((high == 0 && product >= 0) || (high == -1 && product < 0)) {
				return product;
			}
		}
		if (isInteger(left) && isInteger(right)) {
			return integer(bigInteger(left).multiply(bigInteger(right)));
		}
		return real(bigDecimal(left).multiply(bigDecimal(right)));
	}

	/**
	 * {@code /} (§11.5.1, §11.5.2): always a Real; {@code null} for a zero divisor.
	 */
	static BigDecimal divide(Number left, Number right) {
		BigDecimal divisor = bigDecimal(right);
		if (divisor.signum() == 0) {
			return null;
		}
		BigDecimal dividend = bigDecimal(left);
		int scale = Math.max(dividend.scale() - divisor.scale(), MINIMUM_DIVISION_SCALE);
		return real(dividend.divide(divisor, scale, RoundingMode.HALF_EVEN));
	}

	static Number negate(Number value) {
		if (value instanceof Long || value instanceof Integer) {
			long v = value.longValue();
			if (v != Long.MIN_VALUE) {
				return -v;
			}
		}
		if (isInteger(value)) {
			return integer(bigInteger(value).negate());
		}
		return real(bigDecimal(value).negate());
	}

	static Number abs(Number value) {
		return signum(value) < 0 ? negate(value) : value;
	}

	static int signum(Number value) {
		if (value instanceof Long || value instanceof Integer) {
			return Long.signum(value.longValue());
		}
		return isInteger(value) ? bigInteger(value).signum() : bigDecimal(value).signum();
	}

	/** The mathematical order of two numbers; exact for every Integer and Real (#317). */
	static int compare(Number left, Number right) {
		if ((left instanceof Long || left instanceof Integer) && (right instanceof Long || right instanceof Integer)) {
			return Long.compare(left.longValue(), right.longValue());
		}
		if (isInteger(left) && isInteger(right)) {
			return bigInteger(left).compareTo(bigInteger(right));
		}
		return bigDecimal(left).compareTo(bigDecimal(right));
	}

	/** Mathematical equality, {@code 1 = 1.0} included (§11.5.1). NaN equals NaN, for hashing. */
	static boolean equal(Number left, Number right) {
		if (!isFinite(left) || !isFinite(right)) {
			return Double.compare(left.doubleValue(), right.doubleValue()) == 0;
		}
		return compare(left, right) == 0;
	}

	/**
	 * A hash key for the number whose {@code equals} agrees with {@link #equal}: whole values
	 * become a {@code Long} or {@code BigInteger}, fractional ones a stripped {@code BigDecimal}.
	 */
	static Object key(Number value) {
		if (!isFinite(value)) {
			return Double.valueOf(value.doubleValue());
		}
		if (value instanceof Long) {
			return value;
		}
		if (value instanceof Integer || value instanceof Short || value instanceof Byte) {
			return Long.valueOf(value.longValue());
		}
		if (value instanceof BigInteger big) {
			return integer(big);
		}
		BigDecimal decimal = bigDecimal(value);
		if (decimal.signum() == 0 || decimal.scale() <= 0 || decimal.stripTrailingZeros().scale() <= 0) {
			return integer(decimal.toBigIntegerExact());
		}
		return decimal.stripTrailingZeros();
	}

	/** {@code floor}: the largest integer not greater than the value. */
	static Number floor(Number value) {
		return isInteger(value) ? value : integer(bigDecimal(value).setScale(0, RoundingMode.FLOOR).toBigIntegerExact());
	}

	/** {@code ceiling}, an Eclipse extension. */
	static Number ceiling(Number value) {
		return isInteger(value) ? value
				: integer(bigDecimal(value).setScale(0, RoundingMode.CEILING).toBigIntegerExact());
	}

	/**
	 * {@code round} (§11.5.1): "The integer that is closest to self. When there are two such
	 * integers, the largest one" — half up towards positive infinity.
	 */
	static Number round(Number value) {
		if (isInteger(value)) {
			return value;
		}
		return integer(bigDecimal(value).add(new BigDecimal("0.5")).setScale(0, RoundingMode.FLOOR)
				.toBigIntegerExact());
	}

	/**
	 * The text of a Real (§11.5.1 {@code toString}): plain decimal notation, never an exponent,
	 * with at least one fraction digit — {@code 3.0}, {@code 0.1}, {@code 100000000000000000000.0}.
	 */
	static String realToString(BigDecimal value) {
		return OclValues.toText(value);
	}
}
