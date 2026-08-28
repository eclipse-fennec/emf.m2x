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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Unit</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * The compiled unit: one document holding everything a unit needs to be stored, loaded and executed. Four parts, each with one role. The metadata say what this is and how it was built. The unit is the script itself, an EPackage in every language (QVT-O Module, qvtbase Transformation, MOFM2T Module). The satellites are the parser's heap made persistent: objects the script references but that no metamodel feature contains. The embedded units are the dependencies bound under the embed mode, each a complete compiled unit again — a library lends its types, so its satellites stay its own.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getId <em>Id</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getNamespace <em>Namespace</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getVersion <em>Version</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getDescription <em>Description</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getManifest <em>Manifest</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getUnit <em>Unit</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getSatellite <em>Satellite</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getEmbedded <em>Embedded</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnit()
 * @model
 * @generated
 */
@ProviderType
public interface CompiledUnit extends EObject {
	/**
	 * Returns the value of the '<em><b>Id</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Unique across all compiled units, independent of the name: two versions of lib.Strings share a qualified name and have different ids. A UUID minted at compile time.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Id</em>' attribute.
	 * @see #setId(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnit_Id()
	 * @model id="true" required="true"
	 * @generated
	 */
	String getId();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getId <em>Id</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Id</em>' attribute.
	 * @see #getId()
	 * @generated
	 */
	void setId(String value);

	/**
	 * Returns the value of the '<em><b>Namespace</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Groups units for versioning: the units of one namespace are versioned together.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Namespace</em>' attribute.
	 * @see #setNamespace(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnit_Namespace()
	 * @model
	 * @generated
	 */
	String getNamespace();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getNamespace <em>Namespace</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Namespace</em>' attribute.
	 * @see #getNamespace()
	 * @generated
	 */
	void setNamespace(String value);

	/**
	 * Returns the value of the '<em><b>Version</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The declared version — what someone claims this is. The fingerprint in the manifest is what the content measurably is; both are needed, the version to talk about a unit and the fingerprint to check one.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Version</em>' attribute.
	 * @see #setVersion(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnit_Version()
	 * @model
	 * @generated
	 */
	String getVersion();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getVersion <em>Version</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Version</em>' attribute.
	 * @see #getVersion()
	 * @generated
	 */
	void setVersion(String value);

	/**
	 * Returns the value of the '<em><b>Description</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Human-readable, for a store listing.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Description</em>' attribute.
	 * @see #setDescription(String)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnit_Description()
	 * @model
	 * @generated
	 */
	String getDescription();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getDescription <em>Description</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Description</em>' attribute.
	 * @see #getDescription()
	 * @generated
	 */
	void setDescription(String value);

	/**
	 * Returns the value of the '<em><b>Manifest</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * How the unit was built and what it was built against — see CompiledUnitManifest.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Manifest</em>' containment reference.
	 * @see #setManifest(CompiledUnitManifest)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnit_Manifest()
	 * @model containment="true" required="true"
	 * @generated
	 */
	CompiledUnitManifest getManifest();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getManifest <em>Manifest</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Manifest</em>' containment reference.
	 * @see #getManifest()
	 * @generated
	 */
	void setManifest(CompiledUnitManifest value);

	/**
	 * Returns the value of the '<em><b>Unit</b></em>' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * The script: the parsed AST with its language-level root. Typed as EPackage because all three unit roots inherit from it, which is the one point this neutral model can hold on to without knowing a language.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Unit</em>' containment reference.
	 * @see #setUnit(EPackage)
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnit_Unit()
	 * @model containment="true" required="true"
	 * @generated
	 */
	EPackage getUnit();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit#getUnit <em>Unit</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Unit</em>' containment reference.
	 * @see #getUnit()
	 * @generated
	 */
	void setUnit(EPackage value);

	/**
	 * Returns the value of the '<em><b>Satellite</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.emf.ecore.EObject}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Parser-created objects the script references but that no feature of the metamodel contains: self, result, this, type instances, model types, synthetic classifiers, default expressions of intermediate classes. Pure containment with no semantics and no identity of their own — a satellite means nothing outside its unit, which is why the metadata live on the unit and not here. The evaluator never reads this list; it is what makes the unit storable and copyable, nothing more. Typed EObject: the container does not need to know what it holds, only that it holds it.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Satellite</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnit_Satellite()
	 * @model containment="true"
	 * @generated
	 */
	EList<EObject> getSatellite();

	/**
	 * Returns the value of the '<em><b>Embedded</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2x.model.compiled.CompiledUnit}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * <!-- begin-model-doc -->
	 * Dependencies bound under the embed mode, each a complete compiled unit with its own metadata, manifest and satellites. Under pin and rebind a dependency is a DependencyEntry in the manifest instead and is resolved at prepare time.
	 * <!-- end-model-doc -->
	 * @return the value of the '<em>Embedded</em>' containment reference list.
	 * @see org.eclipse.fennec.m2x.model.compiled.CompiledPackage#getCompiledUnit_Embedded()
	 * @model containment="true"
	 * @generated
	 */
	EList<CompiledUnit> getEmbedded();

} // CompiledUnit
