/**
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
package org.eclipse.fennec.m2x.model.qvtoperational.util;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EOperation;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EParameter;
import org.eclipse.emf.ecore.EStructuralFeature;
import org.eclipse.emf.ecore.ETypedElement;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.fennec.m2x.model.imperativeocl.ImperativeExpression;
import org.eclipse.fennec.m2x.model.imperativeocl.InstantiationExp;

import org.eclipse.fennec.m2x.model.ocl.CallExp;
import org.eclipse.fennec.m2x.model.ocl.FeatureCallExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OperationCallExp;

import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2x.model.qvtoperational.ConstructorBody;
import org.eclipse.fennec.m2x.model.qvtoperational.ContextualProperty;
import org.eclipse.fennec.m2x.model.qvtoperational.EntryOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.Helper;
import org.eclipse.fennec.m2x.model.qvtoperational.ImperativeCallExp;
import org.eclipse.fennec.m2x.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.Library;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingCallExp;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2x.model.qvtoperational.MappingParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2x.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2x.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2x.model.qvtoperational.ObjectExp;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationBody;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.ResolveExp;
import org.eclipse.fennec.m2x.model.qvtoperational.ResolveInExp;
import org.eclipse.fennec.m2x.model.qvtoperational.Status;
import org.eclipse.fennec.m2x.model.qvtoperational.VarParameter;

/**
 * <!-- begin-user-doc -->
 * The <b>Switch</b> for the model's inheritance hierarchy.
 * It supports the call {@link #doSwitch(EObject) doSwitch(object)}
 * to invoke the <code>caseXXX</code> method for each class of the model,
 * starting with the actual class of the object
 * and proceeding up the inheritance hierarchy
 * until a non-null result is returned,
 * which is the result of the switch.
 * <!-- end-user-doc -->
 * @see org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage
 * @generated
 */
public class QvtOperationalSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static QvtOperationalPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public QvtOperationalSwitch() {
		if (modelPackage == null) {
			modelPackage = QvtOperationalPackage.eINSTANCE;
		}
	}

	/**
	 * Checks whether this is a switch for the given package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @param ePackage the package in question.
	 * @return whether this is a switch for the given package.
	 * @generated
	 */
	@Override
	protected boolean isSwitchFor(EPackage ePackage) {
		return ePackage == modelPackage;
	}

	/**
	 * Calls <code>caseXXX</code> for each class of the model until one returns a non null result; it yields that result.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @return the first non-null result returned by a <code>caseXXX</code> call.
	 * @generated
	 */
	@Override
	protected T doSwitch(int classifierID, EObject theEObject) {
		switch (classifierID) {
			case QvtOperationalPackage.OPERATION_BODY: {
				OperationBody operationBody = (OperationBody)theEObject;
				T result = caseOperationBody(operationBody);
				if (result == null) result = caseEModelElement(operationBody);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.MAPPING_BODY: {
				MappingBody mappingBody = (MappingBody)theEObject;
				T result = caseMappingBody(mappingBody);
				if (result == null) result = caseOperationBody(mappingBody);
				if (result == null) result = caseEModelElement(mappingBody);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.CONSTRUCTOR_BODY: {
				ConstructorBody constructorBody = (ConstructorBody)theEObject;
				T result = caseConstructorBody(constructorBody);
				if (result == null) result = caseOperationBody(constructorBody);
				if (result == null) result = caseEModelElement(constructorBody);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.VAR_PARAMETER: {
				VarParameter varParameter = (VarParameter)theEObject;
				T result = caseVarParameter(varParameter);
				if (result == null) result = caseEParameter(varParameter);
				if (result == null) result = caseETypedElement(varParameter);
				if (result == null) result = caseENamedElement(varParameter);
				if (result == null) result = caseEModelElement(varParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.MAPPING_PARAMETER: {
				MappingParameter mappingParameter = (MappingParameter)theEObject;
				T result = caseMappingParameter(mappingParameter);
				if (result == null) result = caseVarParameter(mappingParameter);
				if (result == null) result = caseEParameter(mappingParameter);
				if (result == null) result = caseETypedElement(mappingParameter);
				if (result == null) result = caseENamedElement(mappingParameter);
				if (result == null) result = caseEModelElement(mappingParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.MODEL_PARAMETER: {
				ModelParameter modelParameter = (ModelParameter)theEObject;
				T result = caseModelParameter(modelParameter);
				if (result == null) result = caseVarParameter(modelParameter);
				if (result == null) result = caseEParameter(modelParameter);
				if (result == null) result = caseETypedElement(modelParameter);
				if (result == null) result = caseENamedElement(modelParameter);
				if (result == null) result = caseEModelElement(modelParameter);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.IMPERATIVE_OPERATION: {
				ImperativeOperation imperativeOperation = (ImperativeOperation)theEObject;
				T result = caseImperativeOperation(imperativeOperation);
				if (result == null) result = caseEOperation(imperativeOperation);
				if (result == null) result = caseETypedElement(imperativeOperation);
				if (result == null) result = caseENamedElement(imperativeOperation);
				if (result == null) result = caseEModelElement(imperativeOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.HELPER: {
				Helper helper = (Helper)theEObject;
				T result = caseHelper(helper);
				if (result == null) result = caseImperativeOperation(helper);
				if (result == null) result = caseEOperation(helper);
				if (result == null) result = caseETypedElement(helper);
				if (result == null) result = caseENamedElement(helper);
				if (result == null) result = caseEModelElement(helper);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.MAPPING_OPERATION: {
				MappingOperation mappingOperation = (MappingOperation)theEObject;
				T result = caseMappingOperation(mappingOperation);
				if (result == null) result = caseImperativeOperation(mappingOperation);
				if (result == null) result = caseEOperation(mappingOperation);
				if (result == null) result = caseETypedElement(mappingOperation);
				if (result == null) result = caseENamedElement(mappingOperation);
				if (result == null) result = caseEModelElement(mappingOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.CONSTRUCTOR: {
				Constructor constructor = (Constructor)theEObject;
				T result = caseConstructor(constructor);
				if (result == null) result = caseImperativeOperation(constructor);
				if (result == null) result = caseEOperation(constructor);
				if (result == null) result = caseETypedElement(constructor);
				if (result == null) result = caseENamedElement(constructor);
				if (result == null) result = caseEModelElement(constructor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.ENTRY_OPERATION: {
				EntryOperation entryOperation = (EntryOperation)theEObject;
				T result = caseEntryOperation(entryOperation);
				if (result == null) result = caseImperativeOperation(entryOperation);
				if (result == null) result = caseEOperation(entryOperation);
				if (result == null) result = caseETypedElement(entryOperation);
				if (result == null) result = caseENamedElement(entryOperation);
				if (result == null) result = caseEModelElement(entryOperation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.MODULE: {
				org.eclipse.fennec.m2x.model.qvtoperational.Module module = (org.eclipse.fennec.m2x.model.qvtoperational.Module)theEObject;
				T result = caseModule(module);
				if (result == null) result = caseEPackage(module);
				if (result == null) result = caseENamedElement(module);
				if (result == null) result = caseEModelElement(module);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.OPERATIONAL_TRANSFORMATION: {
				OperationalTransformation operationalTransformation = (OperationalTransformation)theEObject;
				T result = caseOperationalTransformation(operationalTransformation);
				if (result == null) result = caseModule(operationalTransformation);
				if (result == null) result = caseEPackage(operationalTransformation);
				if (result == null) result = caseENamedElement(operationalTransformation);
				if (result == null) result = caseEModelElement(operationalTransformation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.LIBRARY: {
				Library library = (Library)theEObject;
				T result = caseLibrary(library);
				if (result == null) result = caseModule(library);
				if (result == null) result = caseEPackage(library);
				if (result == null) result = caseENamedElement(library);
				if (result == null) result = caseEModelElement(library);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.MODEL_TYPE: {
				ModelType modelType = (ModelType)theEObject;
				T result = caseModelType(modelType);
				if (result == null) result = caseEClass(modelType);
				if (result == null) result = caseEClassifier(modelType);
				if (result == null) result = caseENamedElement(modelType);
				if (result == null) result = caseEModelElement(modelType);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.MODULE_IMPORT: {
				ModuleImport moduleImport = (ModuleImport)theEObject;
				T result = caseModuleImport(moduleImport);
				if (result == null) result = caseEModelElement(moduleImport);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.BLACKBOX_OPERATION_DESCRIPTOR: {
				BlackboxOperationDescriptor blackboxOperationDescriptor = (BlackboxOperationDescriptor)theEObject;
				T result = caseBlackboxOperationDescriptor(blackboxOperationDescriptor);
				if (result == null) result = caseEModelElement(blackboxOperationDescriptor);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.CONTEXTUAL_PROPERTY: {
				ContextualProperty contextualProperty = (ContextualProperty)theEObject;
				T result = caseContextualProperty(contextualProperty);
				if (result == null) result = caseEStructuralFeature(contextualProperty);
				if (result == null) result = caseETypedElement(contextualProperty);
				if (result == null) result = caseENamedElement(contextualProperty);
				if (result == null) result = caseEModelElement(contextualProperty);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.STATUS: {
				Status status = (Status)theEObject;
				T result = caseStatus(status);
				if (result == null) result = caseEModelElement(status);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.IMPERATIVE_CALL_EXP: {
				ImperativeCallExp imperativeCallExp = (ImperativeCallExp)theEObject;
				T result = caseImperativeCallExp(imperativeCallExp);
				if (result == null) result = caseOperationCallExp(imperativeCallExp);
				if (result == null) result = caseImperativeExpression(imperativeCallExp);
				if (result == null) result = caseFeatureCallExp(imperativeCallExp);
				if (result == null) result = caseCallExp(imperativeCallExp);
				if (result == null) result = caseOclExpression(imperativeCallExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.MAPPING_CALL_EXP: {
				MappingCallExp mappingCallExp = (MappingCallExp)theEObject;
				T result = caseMappingCallExp(mappingCallExp);
				if (result == null) result = caseImperativeCallExp(mappingCallExp);
				if (result == null) result = caseOperationCallExp(mappingCallExp);
				if (result == null) result = caseImperativeExpression(mappingCallExp);
				if (result == null) result = caseFeatureCallExp(mappingCallExp);
				if (result == null) result = caseCallExp(mappingCallExp);
				if (result == null) result = caseOclExpression(mappingCallExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.OBJECT_EXP: {
				ObjectExp objectExp = (ObjectExp)theEObject;
				T result = caseObjectExp(objectExp);
				if (result == null) result = caseInstantiationExp(objectExp);
				if (result == null) result = caseImperativeExpression(objectExp);
				if (result == null) result = caseOclExpression(objectExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.RESOLVE_EXP: {
				ResolveExp resolveExp = (ResolveExp)theEObject;
				T result = caseResolveExp(resolveExp);
				if (result == null) result = caseCallExp(resolveExp);
				if (result == null) result = caseOclExpression(resolveExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case QvtOperationalPackage.RESOLVE_IN_EXP: {
				ResolveInExp resolveInExp = (ResolveInExp)theEObject;
				T result = caseResolveInExp(resolveInExp);
				if (result == null) result = caseResolveExp(resolveInExp);
				if (result == null) result = caseCallExp(resolveInExp);
				if (result == null) result = caseOclExpression(resolveInExp);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Body</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Body</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationBody(OperationBody object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping Body</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping Body</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingBody(MappingBody object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constructor Body</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constructor Body</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstructorBody(ConstructorBody object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Var Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Var Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseVarParameter(VarParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingParameter(MappingParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Parameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Parameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelParameter(ModelParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Imperative Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Imperative Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseImperativeOperation(ImperativeOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Helper</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Helper</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseHelper(Helper object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingOperation(MappingOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Constructor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Constructor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseConstructor(Constructor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Entry Operation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Entry Operation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEntryOperation(EntryOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModule(org.eclipse.fennec.m2x.model.qvtoperational.Module object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operational Transformation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operational Transformation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationalTransformation(OperationalTransformation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Library</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Library</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLibrary(Library object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Model Type</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Model Type</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModelType(ModelType object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module Import</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module Import</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModuleImport(ModuleImport object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Blackbox Operation Descriptor</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Blackbox Operation Descriptor</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBlackboxOperationDescriptor(BlackboxOperationDescriptor object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Contextual Property</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Contextual Property</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseContextualProperty(ContextualProperty object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Status</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Status</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseStatus(Status object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Imperative Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Imperative Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseImperativeCallExp(ImperativeCallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Mapping Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Mapping Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMappingCallExp(MappingCallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Object Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Object Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseObjectExp(ObjectExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resolve Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resolve Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResolveExp(ResolveExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Resolve In Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Resolve In Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseResolveInExp(ResolveInExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EModel Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EModel Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEModelElement(EModelElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ENamed Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ENamed Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseENamedElement(ENamedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>ETyped Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>ETyped Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseETypedElement(ETypedElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EParameter</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EParameter</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEParameter(EParameter object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EOperation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EOperation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEOperation(EOperation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EPackage</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EPackage</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEPackage(EPackage object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClassifier</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClassifier</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEClassifier(EClassifier object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EClass</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EClass</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEClass(EClass object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EStructural Feature</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EStructural Feature</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseEStructuralFeature(EStructuralFeature object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOclExpression(OclExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseCallExp(CallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Feature Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Feature Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFeatureCallExp(FeatureCallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Operation Call Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Operation Call Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseOperationCallExp(OperationCallExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Imperative Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Imperative Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseImperativeExpression(ImperativeExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Instantiation Exp</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Instantiation Exp</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInstantiationExp(InstantiationExp object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch, but this is the last case anyway.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>EObject</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject)
	 * @generated
	 */
	@Override
	public T defaultCase(EObject object) {
		return null;
	}

} //QvtOperationalSwitch
