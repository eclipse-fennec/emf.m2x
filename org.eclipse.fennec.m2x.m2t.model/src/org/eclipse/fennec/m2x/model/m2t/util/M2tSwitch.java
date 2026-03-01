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
package org.eclipse.fennec.m2x.model.m2t.util;

import org.eclipse.emf.ecore.EModelElement;
import org.eclipse.emf.ecore.ENamedElement;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.util.Switch;

import org.eclipse.fennec.m2x.model.m2t.Block;
import org.eclipse.fennec.m2x.model.m2t.FileBlock;
import org.eclipse.fennec.m2x.model.m2t.ForBlock;
import org.eclipse.fennec.m2x.model.m2t.IfBlock;
import org.eclipse.fennec.m2x.model.m2t.InitSection;
import org.eclipse.fennec.m2x.model.m2t.LetBlock;
import org.eclipse.fennec.m2x.model.m2t.M2tPackage;
import org.eclipse.fennec.m2x.model.m2t.Macro;
import org.eclipse.fennec.m2x.model.m2t.MacroInvocation;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
import org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock;
import org.eclipse.fennec.m2x.model.m2t.Query;
import org.eclipse.fennec.m2x.model.m2t.QueryInvocation;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateExpression;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.model.m2t.TextExpression;
import org.eclipse.fennec.m2x.model.m2t.TraceBlock;

import org.eclipse.fennec.m2x.model.ocl.OclExpression;

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
 * @see org.eclipse.fennec.m2x.model.m2t.M2tPackage
 * @generated
 */
public class M2tSwitch<T> extends Switch<T> {
	/**
	 * The cached model package
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected static M2tPackage modelPackage;

	/**
	 * Creates an instance of the switch.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public M2tSwitch() {
		if (modelPackage == null) {
			modelPackage = M2tPackage.eINSTANCE;
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
			case M2tPackage.MODULE: {
				org.eclipse.fennec.m2x.model.m2t.Module module = (org.eclipse.fennec.m2x.model.m2t.Module)theEObject;
				T result = caseModule(module);
				if (result == null) result = caseEPackage(module);
				if (result == null) result = caseENamedElement(module);
				if (result == null) result = caseEModelElement(module);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.MODULE_ELEMENT: {
				ModuleElement moduleElement = (ModuleElement)theEObject;
				T result = caseModuleElement(moduleElement);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.BLOCK: {
				Block block = (Block)theEObject;
				T result = caseBlock(block);
				if (result == null) result = caseTemplateExpression(block);
				if (result == null) result = caseOclExpression(block);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.INIT_SECTION: {
				InitSection initSection = (InitSection)theEObject;
				T result = caseInitSection(initSection);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.TEMPLATE_EXPRESSION: {
				TemplateExpression templateExpression = (TemplateExpression)theEObject;
				T result = caseTemplateExpression(templateExpression);
				if (result == null) result = caseOclExpression(templateExpression);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.TEMPLATE: {
				Template template = (Template)theEObject;
				T result = caseTemplate(template);
				if (result == null) result = caseBlock(template);
				if (result == null) result = caseModuleElement(template);
				if (result == null) result = caseTemplateExpression(template);
				if (result == null) result = caseOclExpression(template);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.QUERY: {
				Query query = (Query)theEObject;
				T result = caseQuery(query);
				if (result == null) result = caseModuleElement(query);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.MACRO: {
				Macro macro = (Macro)theEObject;
				T result = caseMacro(macro);
				if (result == null) result = caseBlock(macro);
				if (result == null) result = caseModuleElement(macro);
				if (result == null) result = caseTemplateExpression(macro);
				if (result == null) result = caseOclExpression(macro);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.FOR_BLOCK: {
				ForBlock forBlock = (ForBlock)theEObject;
				T result = caseForBlock(forBlock);
				if (result == null) result = caseBlock(forBlock);
				if (result == null) result = caseTemplateExpression(forBlock);
				if (result == null) result = caseOclExpression(forBlock);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.IF_BLOCK: {
				IfBlock ifBlock = (IfBlock)theEObject;
				T result = caseIfBlock(ifBlock);
				if (result == null) result = caseBlock(ifBlock);
				if (result == null) result = caseTemplateExpression(ifBlock);
				if (result == null) result = caseOclExpression(ifBlock);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.LET_BLOCK: {
				LetBlock letBlock = (LetBlock)theEObject;
				T result = caseLetBlock(letBlock);
				if (result == null) result = caseBlock(letBlock);
				if (result == null) result = caseTemplateExpression(letBlock);
				if (result == null) result = caseOclExpression(letBlock);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.FILE_BLOCK: {
				FileBlock fileBlock = (FileBlock)theEObject;
				T result = caseFileBlock(fileBlock);
				if (result == null) result = caseBlock(fileBlock);
				if (result == null) result = caseTemplateExpression(fileBlock);
				if (result == null) result = caseOclExpression(fileBlock);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.PROTECTED_AREA_BLOCK: {
				ProtectedAreaBlock protectedAreaBlock = (ProtectedAreaBlock)theEObject;
				T result = caseProtectedAreaBlock(protectedAreaBlock);
				if (result == null) result = caseBlock(protectedAreaBlock);
				if (result == null) result = caseTemplateExpression(protectedAreaBlock);
				if (result == null) result = caseOclExpression(protectedAreaBlock);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.TRACE_BLOCK: {
				TraceBlock traceBlock = (TraceBlock)theEObject;
				T result = caseTraceBlock(traceBlock);
				if (result == null) result = caseBlock(traceBlock);
				if (result == null) result = caseTemplateExpression(traceBlock);
				if (result == null) result = caseOclExpression(traceBlock);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.TEXT_EXPRESSION: {
				TextExpression textExpression = (TextExpression)theEObject;
				T result = caseTextExpression(textExpression);
				if (result == null) result = caseTemplateExpression(textExpression);
				if (result == null) result = caseOclExpression(textExpression);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.TEMPLATE_INVOCATION: {
				TemplateInvocation templateInvocation = (TemplateInvocation)theEObject;
				T result = caseTemplateInvocation(templateInvocation);
				if (result == null) result = caseTemplateExpression(templateInvocation);
				if (result == null) result = caseOclExpression(templateInvocation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.QUERY_INVOCATION: {
				QueryInvocation queryInvocation = (QueryInvocation)theEObject;
				T result = caseQueryInvocation(queryInvocation);
				if (result == null) result = caseTemplateExpression(queryInvocation);
				if (result == null) result = caseOclExpression(queryInvocation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			case M2tPackage.MACRO_INVOCATION: {
				MacroInvocation macroInvocation = (MacroInvocation)theEObject;
				T result = caseMacroInvocation(macroInvocation);
				if (result == null) result = caseTemplateExpression(macroInvocation);
				if (result == null) result = caseOclExpression(macroInvocation);
				if (result == null) result = defaultCase(theEObject);
				return result;
			}
			default: return defaultCase(theEObject);
		}
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
	public T caseModule(org.eclipse.fennec.m2x.model.m2t.Module object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Module Element</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Module Element</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseModuleElement(ModuleElement object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Block</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Block</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseBlock(Block object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Init Section</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Init Section</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseInitSection(InitSection object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Template Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Template Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTemplateExpression(TemplateExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Template</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Template</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTemplate(Template object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Query</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Query</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseQuery(Query object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Macro</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Macro</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMacro(Macro object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>For Block</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>For Block</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseForBlock(ForBlock object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>If Block</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>If Block</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseIfBlock(IfBlock object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Let Block</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Let Block</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseLetBlock(LetBlock object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>File Block</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>File Block</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseFileBlock(FileBlock object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Protected Area Block</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Protected Area Block</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseProtectedAreaBlock(ProtectedAreaBlock object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Trace Block</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Trace Block</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTraceBlock(TraceBlock object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Text Expression</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Text Expression</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTextExpression(TextExpression object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Template Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Template Invocation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseTemplateInvocation(TemplateInvocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Query Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Query Invocation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseQueryInvocation(QueryInvocation object) {
		return null;
	}

	/**
	 * Returns the result of interpreting the object as an instance of '<em>Macro Invocation</em>'.
	 * <!-- begin-user-doc -->
	 * This implementation returns null;
	 * returning a non-null result will terminate the switch.
	 * <!-- end-user-doc -->
	 * @param object the target of the switch.
	 * @return the result of interpreting the object as an instance of '<em>Macro Invocation</em>'.
	 * @see #doSwitch(org.eclipse.emf.ecore.EObject) doSwitch(EObject)
	 * @generated
	 */
	public T caseMacroInvocation(MacroInvocation object) {
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

} //M2tSwitch
