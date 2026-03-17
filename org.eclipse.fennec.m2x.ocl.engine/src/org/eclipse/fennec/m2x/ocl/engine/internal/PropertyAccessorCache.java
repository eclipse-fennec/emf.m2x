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

import java.lang.invoke.CallSite;
import java.lang.invoke.LambdaMetafactory;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.util.concurrent.ConcurrentHashMap;
import java.util.function.Function;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EStructuralFeature;

/**
 * Cache for fast property accessors that bypass the EMF reflective {@code eGet()} chain.
 *
 * <p>For generated EMF classes, this cache resolves the typed getter method (e.g.
 * {@code PersonImpl.getName()}) and wraps it via {@link LambdaMetafactory} into a
 * {@link PropertyAccessor}. The JIT compiler can inline these accessors, making
 * property access nearly as fast as a direct method call (~5 ns vs ~40 ns for eGet).
 *
 * <p>For dynamic EMF objects (no generated class), the cache falls back to
 * {@code eGet()} since there are no typed getters to resolve.
 *
 * <p>Thread-safe: uses {@link ConcurrentHashMap} for lock-free reads.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class PropertyAccessorCache {

	/**
	 * Default shared instance for backward compatibility and convenience.
	 * Engines that need isolated caches should create their own instances
	 * via {@link #PropertyAccessorCache()}.
	 */
	private static final PropertyAccessorCache DEFAULT = new PropertyAccessorCache();

	/**
	 * Cache key: (implementation class, structural feature) → accessor.
	 * Uses the EStructuralFeature identity to avoid collisions between features
	 * from different EPackage instances that share the same feature ID (e.g.
	 * dynamic EObjects all using the same impl class).
	 */
	private record AccessorKey(Class<?> implClass, EStructuralFeature feature) {
	}

	private final ConcurrentHashMap<AccessorKey, PropertyAccessor> cache = new ConcurrentHashMap<>();

	private static final MethodHandles.Lookup ENGINE_LOOKUP = MethodHandles.lookup();

	/**
	 * Creates a new, empty property accessor cache.
	 */
	public PropertyAccessorCache() {
	}

	/**
	 * Returns the default shared instance.
	 *
	 * @return the default instance
	 */
	public static PropertyAccessorCache getDefault() {
		return DEFAULT;
	}

	/**
	 * Returns a fast accessor for the given feature on objects of the given class.
	 *
	 * <p>On first call for a (class, feature) pair, attempts to resolve the typed
	 * getter via reflection and wrap it with {@link LambdaMetafactory}. On failure
	 * (dynamic objects, security restrictions), falls back to {@code eGet()}.
	 *
	 * @param eo the EObject instance (used to determine the implementation class)
	 * @param sf the structural feature to access
	 * @return a fast accessor, never {@code null}
	 */
	/**
	 * Pre-populates the cache for all structural features of the given EObject's class.
	 * This avoids first-access cache misses during evaluation.
	 *
	 * @param eo a representative instance of the class to warm up
	 */
	public void warmUp(EObject eo) {
		for (EStructuralFeature sf : eo.eClass().getEAllStructuralFeatures()) {
			getAccessor(eo, sf);
		}
	}

	PropertyAccessor getAccessor(EObject eo, EStructuralFeature sf) {
		AccessorKey key = new AccessorKey(eo.getClass(), sf);
		PropertyAccessor accessor = cache.get(key);
		if (accessor != null) {
			return accessor;
		}
		return cache.computeIfAbsent(key, k -> createAccessor(k.implClass(), k.feature()));
	}

	private static PropertyAccessor createAccessor(Class<?> implClass, EStructuralFeature sf) {
		// Generated EMF classes have a containerClass — use LambdaMetafactory for
		// JIT-inlineable direct getter calls (~5 ns vs ~40 ns for eGet)
		if (sf.getContainerClass() != null) {
			String getterName = getterName(sf);
			try {
				Method getter = findGetter(implClass, getterName);
				if (getter != null) {
					return createLambdaAccessor(implClass, getter);
				}
			} catch (Throwable t) {
				// Fall through to eGet fallback
			}
		}
		// Dynamic EMF objects (no generated class): cache an eGet-based accessor.
		// This captures the resolved EStructuralFeature instance, avoiding repeated
		// name-based feature lookups (O(n) scan over all features) on every access.
		return target -> ((EObject) target).eGet(sf);
	}

	/**
	 * Creates a LambdaMetafactory-based accessor that the JIT can inline.
	 *
	 * <p>Uses a {@link MethodHandles.Lookup} obtained from the model's own class
	 * via {@link MethodHandles#privateLookupIn}, so the generated lambda can
	 * resolve the model's implementation classes in OSGi environments where the
	 * engine bundle cannot see the model bundle's classes.
	 */
	private static PropertyAccessor createLambdaAccessor(Class<?> implClass, Method getter) throws Throwable {
		// Obtain a lookup rooted in the model class's module/classloader,
		// not the engine bundle's. This is critical for OSGi cross-bundle access.
		MethodHandles.Lookup lookup = MethodHandles.privateLookupIn(implClass, ENGINE_LOOKUP);
		MethodHandle mh = lookup.unreflect(getter);

		// Use LambdaMetafactory to generate a Function<Object, Object> that the JIT inlines
		CallSite site = LambdaMetafactory.metafactory(
				lookup,
				"apply",
				MethodType.methodType(Function.class),
				MethodType.methodType(Object.class, Object.class),     // erased type
				mh,
				MethodType.methodType(autobox(getter.getReturnType()), getter.getDeclaringClass()) // specific type
		);

		Function<Object, Object> fn = (Function<Object, Object>) site.getTarget().invokeExact();
		return fn::apply;
	}

	/**
	 * Derives the getter name from an EStructuralFeature.
	 * EMF convention: {@code isXxx()} for boolean, {@code getXxx()} for everything else.
	 */
	private static String getterName(EStructuralFeature sf) {
		String name = sf.getName();
		String capitalized = Character.toUpperCase(name.charAt(0)) + name.substring(1);
		if (sf.getEType() != null && "EBoolean".equals(sf.getEType().getName())) {
			// EMF generates isXxx() for EBoolean
			if (name.startsWith("is")) {
				return "is" + Character.toUpperCase(name.charAt(2)) + name.substring(3);
			}
			return "is" + capitalized;
		}
		return "get" + capitalized;
	}

	/**
	 * Finds the getter method on the class or its interfaces.
	 */
	private static Method findGetter(Class<?> clazz, String getterName) {
		try {
			Method m = clazz.getMethod(getterName);
			m.setAccessible(true);
			return m;
		} catch (NoSuchMethodException e) {
			return null;
		}
	}

	/**
	 * Maps primitive types to their boxed equivalents for LambdaMetafactory.
	 */
	private static Class<?> autobox(Class<?> type) {
		if (type == int.class) return Integer.class;
		if (type == long.class) return Long.class;
		if (type == double.class) return Double.class;
		if (type == float.class) return Float.class;
		if (type == boolean.class) return Boolean.class;
		if (type == byte.class) return Byte.class;
		if (type == short.class) return Short.class;
		if (type == char.class) return Character.class;
		return type;
	}
}
