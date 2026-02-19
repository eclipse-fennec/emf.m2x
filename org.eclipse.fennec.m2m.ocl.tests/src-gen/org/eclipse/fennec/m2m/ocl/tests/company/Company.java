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

import org.eclipse.emf.common.util.EList;

import org.eclipse.emf.ecore.EObject;

import org.osgi.annotation.versioning.ProviderType;

/**
 * <!-- begin-user-doc -->
 * A representation of the model object '<em><b>Company</b></em>'.
 * <!-- end-user-doc -->
 *
 * <!-- begin-model-doc -->
 * A company with employees.
 * <!-- end-model-doc -->
 *
 * <p>
 * The following features are supported:
 * </p>
 * <ul>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.Company#getName <em>Name</em>}</li>
 *   <li>{@link org.eclipse.fennec.m2m.ocl.tests.company.Company#getEmployees <em>Employees</em>}</li>
 * </ul>
 *
 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getCompany()
 * @model
 * @generated
 */
@ProviderType
public interface Company extends EObject {
	/**
	 * Returns the value of the '<em><b>Name</b></em>' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Name</em>' attribute.
	 * @see #setName(String)
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getCompany_Name()
	 * @model
	 * @generated
	 */
	String getName();

	/**
	 * Sets the value of the '{@link org.eclipse.fennec.m2m.ocl.tests.company.Company#getName <em>Name</em>}' attribute.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param value the new value of the '<em>Name</em>' attribute.
	 * @see #getName()
	 * @generated
	 */
	void setName(String value);

	/**
	 * Returns the value of the '<em><b>Employees</b></em>' containment reference list.
	 * The list contents are of type {@link org.eclipse.fennec.m2m.ocl.tests.company.Person}.
	 * It is bidirectional and its opposite is '{@link org.eclipse.fennec.m2m.ocl.tests.company.Person#getEmployer <em>Employer</em>}'.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the value of the '<em>Employees</em>' containment reference list.
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.CompanyPackage#getCompany_Employees()
	 * @see org.eclipse.fennec.m2m.ocl.tests.company.Person#getEmployer
	 * @model opposite="employer" containment="true"
	 * @generated
	 */
	EList<Person> getEmployees();

} // Company
