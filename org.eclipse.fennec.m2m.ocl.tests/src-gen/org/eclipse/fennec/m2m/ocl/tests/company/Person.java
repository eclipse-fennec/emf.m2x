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
package org.eclipse.fennec.m2m.ocl.tests.company;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Person</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A person employed by a company.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getAge <em>Age</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getSalary <em>Salary</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#isIsMarried <em>Is Married</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getStatus <em>Status</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getEmployer <em>Employer</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getPerson()
 * @model
 * @generated
 */
@ProviderType
public interface Person extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getPerson_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Age</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Age</em>' attribute.
	 * @see #setAge(int)
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getPerson_Age()
	 * @model
	 * @generated
	 */
	int getAge();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getAge <em>Age</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Age</em>' attribute.
	 * @see #getAge()
	 * @generated
	 */
	void setAge(int value);

	/**
	 * Returns the value of the '<em><b>Salary</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Salary</em>' attribute.
	 * @see #setSalary(double)
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getPerson_Salary()
	 * @model
	 * @generated
	 */
	double getSalary();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getSalary <em>Salary</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Salary</em>' attribute.
	 * @see #getSalary()
	 * @generated
	 */
	void setSalary(double value);

	/**
	 * Returns the value of the '<em><b>Is Married</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Is Married</em>' attribute.
	 * @see #setIsMarried(boolean)
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getPerson_IsMarried()
	 * @model
	 * @generated
	 */
	boolean isIsMarried();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#isIsMarried <em>Is Married</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Is Married</em>' attribute.
	 * @see #isIsMarried()
	 * @generated
	 */
	void setIsMarried(boolean value);

	/**
	 * Returns the value of the '<em><b>Status</b></em>' attribute.
	 * The literals are from the enumeration {@link org.eclipse.fennec.m2m.ocl.tests.company.Status}.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Status</em>' attribute.
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.Status
	 * @see #setStatus(Status)
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getPerson_Status()
	 * @model
	 * @generated
	 */
	Status getStatus();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getStatus <em>Status</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Status</em>' attribute.
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.Status
	 * @see #getStatus()
	 * @generated
	 */
	void setStatus(Status value);

	/**
	 * Returns the value of the '<em><b>Employer</b></em>' container reference.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2m.ocl.tests.company.Company#getEmployees <em>Employees</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Employer</em>' container reference.
	 * @see #setEmployer(Company)
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getPerson_Employer()
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.Company#getEmployees
	 * @model opposite="employees" transient="false"
	 * @generated
	 */
	Company getEmployer();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getEmployer <em>Employer</em>}' container reference.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Employer</em>' container reference.
	 * @see #getEmployer()
	 * @generated
	 */
	void setEmployer(Company value);

} // Person
