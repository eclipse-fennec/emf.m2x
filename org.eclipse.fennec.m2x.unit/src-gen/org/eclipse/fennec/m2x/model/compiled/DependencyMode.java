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
package org.eclipse.fennec.m2x.model.compiled;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.eclipse.emf.common.util.Enumerator;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the literals of the enumeration '<em><b>Dependency Mode</b></em>',
 * and utility methods for working with them.
 * <!-- end-user-doc -->
 * <!-- begin-model-doc -->
 * How a dependency was bound when the unit was compiled. embed: the dependency's AST was copied into the unit — absolutely reproducible, never picks up a fix. pin: a reference to exactly one fingerprint — reproducible while that version stays resolvable, fixes arrive on recompile. rebind: a reference by name, bound at prepare time — not reproducible, fixes arrive automatically, so prepare has to record what it actually bound.
 * <!-- end-model-doc -->
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getDependencyMode()
 * @model
 * @generated
 */
@ProviderType
public enum DependencyMode implements Enumerator {
	/**
	 * The '<em><b>Embed</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EMBED_VALUE
	 * @generated
	 * @ordered
	 */
	EMBED(0, "embed", "embed"),

	/**
	 * The '<em><b>Pin</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PIN_VALUE
	 * @generated
	 * @ordered
	 */
	PIN(1, "pin", "pin"),

	/**
	 * The '<em><b>Rebind</b></em>' literal object.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #REBIND_VALUE
	 * @generated
	 * @ordered
	 */
	REBIND(2, "rebind", "rebind");

	/**
	 * The '<em><b>Embed</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #EMBED
	 * @model name="embed"
	 * @generated
	 * @ordered
	 */
	public static final int EMBED_VALUE = 0;

	/**
	 * The '<em><b>Pin</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #PIN
	 * @model name="pin"
	 * @generated
	 * @ordered
	 */
	public static final int PIN_VALUE = 1;

	/**
	 * The '<em><b>Rebind</b></em>' literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #REBIND
	 * @model name="rebind"
	 * @generated
	 * @ordered
	 */
	public static final int REBIND_VALUE = 2;

	/**
	 * An array of all the '<em><b>Dependency Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static final DependencyMode[] VALUES_ARRAY =
		new DependencyMode[] {
			EMBED,
			PIN,
			REBIND,
		};

	/**
	 * A public read-only list of all the '<em><b>Dependency Mode</b></em>' enumerators.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static final List<DependencyMode> VALUES = Collections.unmodifiableList(Arrays.asList(VALUES_ARRAY));

	/**
	 * Returns the '<em><b>Dependency Mode</b></em>' literal with the specified literal value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param literal the literal.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static DependencyMode get(String literal) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			DependencyMode result = VALUES_ARRAY[i];
			if (result.toString().equals(literal)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Dependency Mode</b></em>' literal with the specified name.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param name the name.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static DependencyMode getByName(String name) {
		for (int i = 0; i < VALUES_ARRAY.length; ++i) {
			DependencyMode result = VALUES_ARRAY[i];
			if (result.getName().equals(name)) {
				return result;
			}
		}
		return null;
	}

	/**
	 * Returns the '<em><b>Dependency Mode</b></em>' literal with the specified integer value.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the integer value.
	 * @return the matching enumerator or <code>null</code>.
	 * @generated
	 */
	public static DependencyMode get(int value) {
		switch (value) {
			case EMBED_VALUE: return EMBED;
			case PIN_VALUE: return PIN;
			case REBIND_VALUE: return REBIND;
		}
		return null;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final int value;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String name;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private final String literal;

	/**
	 * Only this class can construct instances.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private DependencyMode(int value, String name, String literal) {
		this.value = value;
		this.name = name;
		this.literal = literal;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getValue() {
	  return value;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getName() {
	  return name;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String getLiteral() {
	  return literal;
	}

	/**
	 * Returns the literal value of the enumerator, which is its string representation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		return literal;
	}
	
} //DependencyMode
