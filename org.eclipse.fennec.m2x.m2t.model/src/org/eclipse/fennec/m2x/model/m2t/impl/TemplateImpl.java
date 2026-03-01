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
package org.eclipse.fennec.m2x.model.m2t.impl;

import java.util.Collection;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;

import org.eclipse.emf.ecore.impl.ENotificationImpl;

import org.eclipse.emf.ecore.util.EObjectContainmentEList;
import org.eclipse.emf.ecore.util.EObjectResolvingEList;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.util.InternalEList;

import org.eclipse.fennec.m2x.model.m2t.M2tPackage;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.VisibilityKind;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Template</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl#getVisibility <em>Visibility</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl#getModule <em>Module</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl#getParameter <em>Parameter</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl#getGuard <em>Guard</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl#getOverrides <em>Overrides</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl#isMain <em>Main</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2x.model.m2t.impl.TemplateImpl#getPost <em>Post</em>}</li>
 * </ul>
 *
 * @generated
 */
public class TemplateImpl extends BlockImpl implements Template {
	/**
	 * The default value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected static final String NAME_EDEFAULT = null;

	/**
	 * The cached value of the '{@link #getName() <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getName()
	 * @generated
	 * @ordered
	 */
	protected String name = NAME_EDEFAULT;

	/**
	 * The default value of the '{@link #getVisibility() <em>Visibility</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVisibility()
	 * @generated
	 * @ordered
	 */
	protected static final VisibilityKind VISIBILITY_EDEFAULT = VisibilityKind.PUBLIC;

	/**
	 * The cached value of the '{@link #getVisibility() <em>Visibility</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getVisibility()
	 * @generated
	 * @ordered
	 */
	protected VisibilityKind visibility = VISIBILITY_EDEFAULT;

	/**
	 * The cached value of the '{@link #getParameter() <em>Parameter</em>}' containment reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getParameter()
	 * @generated
	 * @ordered
	 */
	protected EList<Variable> parameter;

	/**
	 * The cached value of the '{@link #getGuard() <em>Guard</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getGuard()
	 * @generated
	 * @ordered
	 */
	protected OclExpression guard;

	/**
	 * The cached value of the '{@link #getOverrides() <em>Overrides</em>}' reference list.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getOverrides()
	 * @generated
	 * @ordered
	 */
	protected EList<Template> overrides;

	/**
	 * The default value of the '{@link #isMain() <em>Main</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMain()
	 * @generated
	 * @ordered
	 */
	protected static final boolean MAIN_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isMain() <em>Main</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isMain()
	 * @generated
	 * @ordered
	 */
	protected boolean main = MAIN_EDEFAULT;

	/**
	 * The cached value of the '{@link #getPost() <em>Post</em>}' containment reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getPost()
	 * @generated
	 * @ordered
	 */
	protected OclExpression post;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected TemplateImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return M2tPackage.Literals.TEMPLATE;
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
	public void setName(String newName) {
		String oldName = name;
		name = newName;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public VisibilityKind getVisibility() {
		return visibility;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setVisibility(VisibilityKind newVisibility) {
		VisibilityKind oldVisibility = visibility;
		visibility = newVisibility == null ? VISIBILITY_EDEFAULT : newVisibility;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE__VISIBILITY, oldVisibility, visibility));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public org.eclipse.fennec.m2x.model.m2t.Module getModule() {
		if (eContainerFeatureID() != M2tPackage.TEMPLATE__MODULE) return null;
		return (org.eclipse.fennec.m2x.model.m2t.Module)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetModule(org.eclipse.fennec.m2x.model.m2t.Module newModule, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newModule, M2tPackage.TEMPLATE__MODULE, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setModule(org.eclipse.fennec.m2x.model.m2t.Module newModule) {
		if (newModule != eInternalContainer() || (eContainerFeatureID() != M2tPackage.TEMPLATE__MODULE && newModule != null)) {
			if (EcoreUtil.isAncestor(this, newModule))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newModule != null)
				msgs = ((InternalEObject)newModule).eInverseAdd(this, M2tPackage.MODULE__OWNED_MODULE_ELEMENT, org.eclipse.fennec.m2x.model.m2t.Module.class, msgs);
			msgs = basicSetModule(newModule, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE__MODULE, newModule, newModule));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Variable> getParameter() {
		if (parameter == null) {
			parameter = new EObjectContainmentEList<Variable>(Variable.class, this, M2tPackage.TEMPLATE__PARAMETER);
		}
		return parameter;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getGuard() {
		return guard;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetGuard(OclExpression newGuard, NotificationChain msgs) {
		OclExpression oldGuard = guard;
		guard = newGuard;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE__GUARD, oldGuard, newGuard);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setGuard(OclExpression newGuard) {
		if (newGuard != guard) {
			NotificationChain msgs = null;
			if (guard != null)
				msgs = ((InternalEObject)guard).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE__GUARD, null, msgs);
			if (newGuard != null)
				msgs = ((InternalEObject)newGuard).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE__GUARD, null, msgs);
			msgs = basicSetGuard(newGuard, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE__GUARD, newGuard, newGuard));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EList<Template> getOverrides() {
		if (overrides == null) {
			overrides = new EObjectResolvingEList<Template>(Template.class, this, M2tPackage.TEMPLATE__OVERRIDES);
		}
		return overrides;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isMain() {
		return main;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setMain(boolean newMain) {
		boolean oldMain = main;
		main = newMain;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE__MAIN, oldMain, main));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public OclExpression getPost() {
		return post;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetPost(OclExpression newPost, NotificationChain msgs) {
		OclExpression oldPost = post;
		post = newPost;
		if (eNotificationRequired()) {
			ENotificationImpl notification = new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE__POST, oldPost, newPost);
			if (msgs == null) msgs = notification; else msgs.add(notification);
		}
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setPost(OclExpression newPost) {
		if (newPost != post) {
			NotificationChain msgs = null;
			if (post != null)
				msgs = ((InternalEObject)post).eInverseRemove(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE__POST, null, msgs);
			if (newPost != null)
				msgs = ((InternalEObject)newPost).eInverseAdd(this, EOPPOSITE_FEATURE_BASE - M2tPackage.TEMPLATE__POST, null, msgs);
			msgs = basicSetPost(newPost, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, M2tPackage.TEMPLATE__POST, newPost, newPost));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case M2tPackage.TEMPLATE__MODULE:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetModule((org.eclipse.fennec.m2x.model.m2t.Module)otherEnd, msgs);
		}
		return super.eInverseAdd(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseRemove(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case M2tPackage.TEMPLATE__MODULE:
				return basicSetModule(null, msgs);
			case M2tPackage.TEMPLATE__PARAMETER:
				return ((InternalEList<?>)getParameter()).basicRemove(otherEnd, msgs);
			case M2tPackage.TEMPLATE__GUARD:
				return basicSetGuard(null, msgs);
			case M2tPackage.TEMPLATE__POST:
				return basicSetPost(null, msgs);
		}
		return super.eInverseRemove(otherEnd, featureID, msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eBasicRemoveFromContainerFeature(NotificationChain msgs) {
		switch (eContainerFeatureID()) {
			case M2tPackage.TEMPLATE__MODULE:
				return eInternalContainer().eInverseRemove(this, M2tPackage.MODULE__OWNED_MODULE_ELEMENT, org.eclipse.fennec.m2x.model.m2t.Module.class, msgs);
		}
		return super.eBasicRemoveFromContainerFeature(msgs);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object eGet(int featureID, boolean resolve, boolean coreType) {
		switch (featureID) {
			case M2tPackage.TEMPLATE__NAME:
				return getName();
			case M2tPackage.TEMPLATE__VISIBILITY:
				return getVisibility();
			case M2tPackage.TEMPLATE__MODULE:
				return getModule();
			case M2tPackage.TEMPLATE__PARAMETER:
				return getParameter();
			case M2tPackage.TEMPLATE__GUARD:
				return getGuard();
			case M2tPackage.TEMPLATE__OVERRIDES:
				return getOverrides();
			case M2tPackage.TEMPLATE__MAIN:
				return isMain();
			case M2tPackage.TEMPLATE__POST:
				return getPost();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@SuppressWarnings("unchecked")
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case M2tPackage.TEMPLATE__NAME:
				setName((String)newValue);
				return;
			case M2tPackage.TEMPLATE__VISIBILITY:
				setVisibility((VisibilityKind)newValue);
				return;
			case M2tPackage.TEMPLATE__MODULE:
				setModule((org.eclipse.fennec.m2x.model.m2t.Module)newValue);
				return;
			case M2tPackage.TEMPLATE__PARAMETER:
				getParameter().clear();
				getParameter().addAll((Collection<? extends Variable>)newValue);
				return;
			case M2tPackage.TEMPLATE__GUARD:
				setGuard((OclExpression)newValue);
				return;
			case M2tPackage.TEMPLATE__OVERRIDES:
				getOverrides().clear();
				getOverrides().addAll((Collection<? extends Template>)newValue);
				return;
			case M2tPackage.TEMPLATE__MAIN:
				setMain((Boolean)newValue);
				return;
			case M2tPackage.TEMPLATE__POST:
				setPost((OclExpression)newValue);
				return;
		}
		super.eSet(featureID, newValue);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eUnset(int featureID) {
		switch (featureID) {
			case M2tPackage.TEMPLATE__NAME:
				setName(NAME_EDEFAULT);
				return;
			case M2tPackage.TEMPLATE__VISIBILITY:
				setVisibility(VISIBILITY_EDEFAULT);
				return;
			case M2tPackage.TEMPLATE__MODULE:
				setModule((org.eclipse.fennec.m2x.model.m2t.Module)null);
				return;
			case M2tPackage.TEMPLATE__PARAMETER:
				getParameter().clear();
				return;
			case M2tPackage.TEMPLATE__GUARD:
				setGuard((OclExpression)null);
				return;
			case M2tPackage.TEMPLATE__OVERRIDES:
				getOverrides().clear();
				return;
			case M2tPackage.TEMPLATE__MAIN:
				setMain(MAIN_EDEFAULT);
				return;
			case M2tPackage.TEMPLATE__POST:
				setPost((OclExpression)null);
				return;
		}
		super.eUnset(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean eIsSet(int featureID) {
		switch (featureID) {
			case M2tPackage.TEMPLATE__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case M2tPackage.TEMPLATE__VISIBILITY:
				return visibility != VISIBILITY_EDEFAULT;
			case M2tPackage.TEMPLATE__MODULE:
				return getModule() != null;
			case M2tPackage.TEMPLATE__PARAMETER:
				return parameter != null && !parameter.isEmpty();
			case M2tPackage.TEMPLATE__GUARD:
				return guard != null;
			case M2tPackage.TEMPLATE__OVERRIDES:
				return overrides != null && !overrides.isEmpty();
			case M2tPackage.TEMPLATE__MAIN:
				return main != MAIN_EDEFAULT;
			case M2tPackage.TEMPLATE__POST:
				return post != null;
		}
		return super.eIsSet(featureID);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eBaseStructuralFeatureID(int derivedFeatureID, Class<?> baseClass) {
		if (baseClass == ModuleElement.class) {
			switch (derivedFeatureID) {
				case M2tPackage.TEMPLATE__NAME: return M2tPackage.MODULE_ELEMENT__NAME;
				case M2tPackage.TEMPLATE__VISIBILITY: return M2tPackage.MODULE_ELEMENT__VISIBILITY;
				case M2tPackage.TEMPLATE__MODULE: return M2tPackage.MODULE_ELEMENT__MODULE;
				default: return -1;
			}
		}
		return super.eBaseStructuralFeatureID(derivedFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int eDerivedStructuralFeatureID(int baseFeatureID, Class<?> baseClass) {
		if (baseClass == ModuleElement.class) {
			switch (baseFeatureID) {
				case M2tPackage.MODULE_ELEMENT__NAME: return M2tPackage.TEMPLATE__NAME;
				case M2tPackage.MODULE_ELEMENT__VISIBILITY: return M2tPackage.TEMPLATE__VISIBILITY;
				case M2tPackage.MODULE_ELEMENT__MODULE: return M2tPackage.TEMPLATE__MODULE;
				default: return -1;
			}
		}
		return super.eDerivedStructuralFeatureID(baseFeatureID, baseClass);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String toString() {
		if (eIsProxy()) return super.toString();

		StringBuilder result = new StringBuilder(super.toString());
		result.append(" (name: ");
		result.append(name);
		result.append(", visibility: ");
		result.append(visibility);
		result.append(", main: ");
		result.append(main);
		result.append(')');
		return result.toString();
	}

} //TemplateImpl
