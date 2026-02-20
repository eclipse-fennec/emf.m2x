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
package org.eclipse.fennec.m2m.ocl.tests.company.impl;

import org.eclipse.emf.common.notify.Notification;
import org.eclipse.emf.common.notify.NotificationChain;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.InternalEObject;
import org.eclipse.emf.ecore.impl.ENotificationImpl;
import org.eclipse.emf.ecore.impl.MinimalEObjectImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2m.ocl.tests.company.Company;
import org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage;
import org.eclipse.fennec.m2m.ocl.tests.company.Person;
import org.eclipse.fennec.m2m.ocl.tests.company.Status;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model object '<em><b>Person</b></em>'.
 * <!-- end-user-doc -->
 * <p>
 * The following features are implemented:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.impl.PersonImpl#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.impl.PersonImpl#getAge <em>Age</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.impl.PersonImpl#getSalary <em>Salary</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.impl.PersonImpl#isIsMarried <em>Is Married</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.impl.PersonImpl#getStatus <em>Status</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.impl.PersonImpl#getEmployer <em>Employer</em>}</li>
 * </ul>
 *
 * @generated
 */
public class PersonImpl extends MinimalEObjectImpl.Container implements Person {
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
	 * The default value of the '{@link #getAge() <em>Age</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAge()
	 * @generated
	 * @ordered
	 */
	protected static final int AGE_EDEFAULT = 0;

	/**
	 * The cached value of the '{@link #getAge() <em>Age</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getAge()
	 * @generated
	 * @ordered
	 */
	protected int age = AGE_EDEFAULT;

	/**
	 * The default value of the '{@link #getSalary() <em>Salary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalary()
	 * @generated
	 * @ordered
	 */
	protected static final double SALARY_EDEFAULT = 0.0;

	/**
	 * The cached value of the '{@link #getSalary() <em>Salary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getSalary()
	 * @generated
	 * @ordered
	 */
	protected double salary = SALARY_EDEFAULT;

	/**
	 * The default value of the '{@link #isIsMarried() <em>Is Married</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsMarried()
	 * @generated
	 * @ordered
	 */
	protected static final boolean IS_MARRIED_EDEFAULT = false;

	/**
	 * The cached value of the '{@link #isIsMarried() <em>Is Married</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #isIsMarried()
	 * @generated
	 * @ordered
	 */
	protected boolean isMarried = IS_MARRIED_EDEFAULT;

	/**
	 * The default value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
	protected static final Status STATUS_EDEFAULT = Status.JUNIOR;

	/**
	 * The cached value of the '{@link #getStatus() <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #getStatus()
	 * @generated
	 * @ordered
	 */
	protected Status status = STATUS_EDEFAULT;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected PersonImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	protected EClass eStaticClass() {
		return CompanyPackage.Literals.PERSON;
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
			eNotify(new ENotificationImpl(this, Notification.SET, CompanyPackage.PERSON__NAME, oldName, name));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public int getAge() {
		return age;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setAge(int newAge) {
		int oldAge = age;
		age = newAge;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompanyPackage.PERSON__AGE, oldAge, age));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public double getSalary() {
		return salary;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setSalary(double newSalary) {
		double oldSalary = salary;
		salary = newSalary;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompanyPackage.PERSON__SALARY, oldSalary, salary));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public boolean isIsMarried() {
		return isMarried;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setIsMarried(boolean newIsMarried) {
		boolean oldIsMarried = isMarried;
		isMarried = newIsMarried;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompanyPackage.PERSON__IS_MARRIED, oldIsMarried, isMarried));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Status getStatus() {
		return status;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setStatus(Status newStatus) {
		Status oldStatus = status;
		status = newStatus == null ? STATUS_EDEFAULT : newStatus;
		if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompanyPackage.PERSON__STATUS, oldStatus, status));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Company getEmployer() {
		if (eContainerFeatureID() != CompanyPackage.PERSON__EMPLOYER) return null;
		return (Company)eInternalContainer();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public NotificationChain basicSetEmployer(Company newEmployer, NotificationChain msgs) {
		msgs = eBasicSetContainer((InternalEObject)newEmployer, CompanyPackage.PERSON__EMPLOYER, msgs);
		return msgs;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void setEmployer(Company newEmployer) {
		if (newEmployer != eInternalContainer() || (eContainerFeatureID() != CompanyPackage.PERSON__EMPLOYER && newEmployer != null)) {
			if (EcoreUtil.isAncestor(this, newEmployer))
				throw new IllegalArgumentException("Recursive containment not allowed for " + toString());
			NotificationChain msgs = null;
			if (eInternalContainer() != null)
				msgs = eBasicRemoveFromContainer(msgs);
			if (newEmployer != null)
				msgs = ((InternalEObject)newEmployer).eInverseAdd(this, CompanyPackage.COMPANY__EMPLOYEES, Company.class, msgs);
			msgs = basicSetEmployer(newEmployer, msgs);
			if (msgs != null) msgs.dispatch();
		}
		else if (eNotificationRequired())
			eNotify(new ENotificationImpl(this, Notification.SET, CompanyPackage.PERSON__EMPLOYER, newEmployer, newEmployer));
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public NotificationChain eInverseAdd(InternalEObject otherEnd, int featureID, NotificationChain msgs) {
		switch (featureID) {
			case CompanyPackage.PERSON__EMPLOYER:
				if (eInternalContainer() != null)
					msgs = eBasicRemoveFromContainer(msgs);
				return basicSetEmployer((Company)otherEnd, msgs);
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
			case CompanyPackage.PERSON__EMPLOYER:
				return basicSetEmployer(null, msgs);
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
			case CompanyPackage.PERSON__EMPLOYER:
				return eInternalContainer().eInverseRemove(this, CompanyPackage.COMPANY__EMPLOYEES, Company.class, msgs);
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
			case CompanyPackage.PERSON__NAME:
				return getName();
			case CompanyPackage.PERSON__AGE:
				return getAge();
			case CompanyPackage.PERSON__SALARY:
				return getSalary();
			case CompanyPackage.PERSON__IS_MARRIED:
				return isIsMarried();
			case CompanyPackage.PERSON__STATUS:
				return getStatus();
			case CompanyPackage.PERSON__EMPLOYER:
				return getEmployer();
		}
		return super.eGet(featureID, resolve, coreType);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public void eSet(int featureID, Object newValue) {
		switch (featureID) {
			case CompanyPackage.PERSON__NAME:
				setName((String)newValue);
				return;
			case CompanyPackage.PERSON__AGE:
				setAge((Integer)newValue);
				return;
			case CompanyPackage.PERSON__SALARY:
				setSalary((Double)newValue);
				return;
			case CompanyPackage.PERSON__IS_MARRIED:
				setIsMarried((Boolean)newValue);
				return;
			case CompanyPackage.PERSON__STATUS:
				setStatus((Status)newValue);
				return;
			case CompanyPackage.PERSON__EMPLOYER:
				setEmployer((Company)newValue);
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
			case CompanyPackage.PERSON__NAME:
				setName(NAME_EDEFAULT);
				return;
			case CompanyPackage.PERSON__AGE:
				setAge(AGE_EDEFAULT);
				return;
			case CompanyPackage.PERSON__SALARY:
				setSalary(SALARY_EDEFAULT);
				return;
			case CompanyPackage.PERSON__IS_MARRIED:
				setIsMarried(IS_MARRIED_EDEFAULT);
				return;
			case CompanyPackage.PERSON__STATUS:
				setStatus(STATUS_EDEFAULT);
				return;
			case CompanyPackage.PERSON__EMPLOYER:
				setEmployer((Company)null);
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
			case CompanyPackage.PERSON__NAME:
				return NAME_EDEFAULT == null ? name != null : !NAME_EDEFAULT.equals(name);
			case CompanyPackage.PERSON__AGE:
				return age != AGE_EDEFAULT;
			case CompanyPackage.PERSON__SALARY:
				return salary != SALARY_EDEFAULT;
			case CompanyPackage.PERSON__IS_MARRIED:
				return isMarried != IS_MARRIED_EDEFAULT;
			case CompanyPackage.PERSON__STATUS:
				return status != STATUS_EDEFAULT;
			case CompanyPackage.PERSON__EMPLOYER:
				return getEmployer() != null;
		}
		return super.eIsSet(featureID);
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
		result.append(", age: ");
		result.append(age);
		result.append(", salary: ");
		result.append(salary);
		result.append(", isMarried: ");
		result.append(isMarried);
		result.append(", status: ");
		result.append(status);
		result.append(')');
		return result.toString();
	}

} //PersonImpl
