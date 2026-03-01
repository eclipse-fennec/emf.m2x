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
package org.eclipse.fennec.m2x.model.m2t.impl;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EDataType;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;

import org.eclipse.emf.ecore.impl.EFactoryImpl;

import org.eclipse.emf.ecore.plugin.EcorePlugin;

import org.eclipse.fennec.m2x.model.m2t.FileBlock;
import org.eclipse.fennec.m2x.model.m2t.ForBlock;
import org.eclipse.fennec.m2x.model.m2t.IfBlock;
import org.eclipse.fennec.m2x.model.m2t.InitSection;
import org.eclipse.fennec.m2x.model.m2t.LetBlock;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.M2tPackage;
import org.eclipse.fennec.m2x.model.m2t.Macro;
import org.eclipse.fennec.m2x.model.m2t.MacroInvocation;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock;
import org.eclipse.fennec.m2x.model.m2t.Query;
import org.eclipse.fennec.m2x.model.m2t.QueryInvocation;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.model.m2t.TextExpression;
import org.eclipse.fennec.m2x.model.m2t.TraceBlock;
import org.eclipse.fennec.m2x.model.m2t.VisibilityKind;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Factory</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class M2tFactoryImpl extends EFactoryImpl implements M2tFactory {
	/**
	 * Creates the default factory implementation.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public static M2tFactory init() {
		try {
			M2tFactory theM2tFactory = (M2tFactory)EPackage.Registry.INSTANCE.getEFactory(M2tPackage.eNS_URI);
			if (theM2tFactory != null) {
				return theM2tFactory;
			}
		}
		catch (Exception exception) {
			EcorePlugin.INSTANCE.log(exception);
		}
		return new M2tFactoryImpl();
	}

	/**
	 * Creates an instance of the factory.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public M2tFactoryImpl() {
		super();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EObject create(EClass eClass) {
		switch (eClass.getClassifierID()) {
			case M2tPackage.MODULE: return createModule();
			case M2tPackage.INIT_SECTION: return createInitSection();
			case M2tPackage.TEMPLATE: return createTemplate();
			case M2tPackage.QUERY: return createQuery();
			case M2tPackage.MACRO: return createMacro();
			case M2tPackage.FOR_BLOCK: return createForBlock();
			case M2tPackage.IF_BLOCK: return createIfBlock();
			case M2tPackage.LET_BLOCK: return createLetBlock();
			case M2tPackage.FILE_BLOCK: return createFileBlock();
			case M2tPackage.PROTECTED_AREA_BLOCK: return createProtectedAreaBlock();
			case M2tPackage.TRACE_BLOCK: return createTraceBlock();
			case M2tPackage.TEXT_EXPRESSION: return createTextExpression();
			case M2tPackage.TEMPLATE_INVOCATION: return createTemplateInvocation();
			case M2tPackage.QUERY_INVOCATION: return createQueryInvocation();
			case M2tPackage.MACRO_INVOCATION: return createMacroInvocation();
			default:
				throw new IllegalArgumentException("The class '" + eClass.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Object createFromString(EDataType eDataType, String initialValue) {
		switch (eDataType.getClassifierID()) {
			case M2tPackage.OPEN_MODE_KIND:
				return createOpenModeKindFromString(eDataType, initialValue);
			case M2tPackage.VISIBILITY_KIND:
				return createVisibilityKindFromString(eDataType, initialValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public String convertToString(EDataType eDataType, Object instanceValue) {
		switch (eDataType.getClassifierID()) {
			case M2tPackage.OPEN_MODE_KIND:
				return convertOpenModeKindToString(eDataType, instanceValue);
			case M2tPackage.VISIBILITY_KIND:
				return convertVisibilityKindToString(eDataType, instanceValue);
			default:
				throw new IllegalArgumentException("The datatype '" + eDataType.getName() + "' is not a valid classifier");
		}
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public org.eclipse.fennec.m2x.model.m2t.Module createModule() {
		ModuleImpl module = new ModuleImpl();
		return module;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public InitSection createInitSection() {
		InitSectionImpl initSection = new InitSectionImpl();
		return initSection;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Template createTemplate() {
		TemplateImpl template = new TemplateImpl();
		return template;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Query createQuery() {
		QueryImpl query = new QueryImpl();
		return query;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public Macro createMacro() {
		MacroImpl macro = new MacroImpl();
		return macro;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ForBlock createForBlock() {
		ForBlockImpl forBlock = new ForBlockImpl();
		return forBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public IfBlock createIfBlock() {
		IfBlockImpl ifBlock = new IfBlockImpl();
		return ifBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public LetBlock createLetBlock() {
		LetBlockImpl letBlock = new LetBlockImpl();
		return letBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public FileBlock createFileBlock() {
		FileBlockImpl fileBlock = new FileBlockImpl();
		return fileBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public ProtectedAreaBlock createProtectedAreaBlock() {
		ProtectedAreaBlockImpl protectedAreaBlock = new ProtectedAreaBlockImpl();
		return protectedAreaBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TraceBlock createTraceBlock() {
		TraceBlockImpl traceBlock = new TraceBlockImpl();
		return traceBlock;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TextExpression createTextExpression() {
		TextExpressionImpl textExpression = new TextExpressionImpl();
		return textExpression;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public TemplateInvocation createTemplateInvocation() {
		TemplateInvocationImpl templateInvocation = new TemplateInvocationImpl();
		return templateInvocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public QueryInvocation createQueryInvocation() {
		QueryInvocationImpl queryInvocation = new QueryInvocationImpl();
		return queryInvocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public MacroInvocation createMacroInvocation() {
		MacroInvocationImpl macroInvocation = new MacroInvocationImpl();
		return macroInvocation;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public OpenModeKind createOpenModeKindFromString(EDataType eDataType, String initialValue) {
		OpenModeKind result = OpenModeKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertOpenModeKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public VisibilityKind createVisibilityKindFromString(EDataType eDataType, String initialValue) {
		VisibilityKind result = VisibilityKind.get(initialValue);
		if (result == null) throw new IllegalArgumentException("The value '" + initialValue + "' is not a valid enumerator of '" + eDataType.getName() + "'");
		return result;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public String convertVisibilityKindToString(EDataType eDataType, Object instanceValue) {
		return instanceValue == null ? null : instanceValue.toString();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public M2tPackage getM2tPackage() {
		return (M2tPackage)getEPackage();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @deprecated
	 * @generated
	 */
	@Deprecated
	public static M2tPackage getPackage() {
		return M2tPackage.eINSTANCE;
	}

} //M2tFactoryImpl
