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
package org.eclipse.fennec.m2m.model.qvtoperational.impl;

import org.eclipse.emf.ecore.EAttribute;
import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EEnum;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.EReference;

import org.eclipse.emf.ecore.impl.EPackageImpl;

import org.eclipse.fennec.m2m.model.imperativeocl.ImperativeOclPackage;

import org.eclipse.fennec.m2m.model.imperativeocl.impl.ImperativeOclPackageImpl;

import org.eclipse.fennec.m2m.model.ocl.OclPackage;

import org.eclipse.fennec.m2m.model.qvtoperational.Constructor;
import org.eclipse.fennec.m2m.model.qvtoperational.ConstructorBody;
import org.eclipse.fennec.m2m.model.qvtoperational.ContextualProperty;
import org.eclipse.fennec.m2m.model.qvtoperational.DirectionKind;
import org.eclipse.fennec.m2m.model.qvtoperational.EntryOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.Helper;
import org.eclipse.fennec.m2m.model.qvtoperational.ImperativeCallExp;
import org.eclipse.fennec.m2m.model.qvtoperational.ImperativeOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.ImportKind;
import org.eclipse.fennec.m2m.model.qvtoperational.Library;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingBody;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingCallExp;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingOperation;
import org.eclipse.fennec.m2m.model.qvtoperational.MappingParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.ModelParameter;
import org.eclipse.fennec.m2m.model.qvtoperational.ModelType;
import org.eclipse.fennec.m2m.model.qvtoperational.ModuleImport;
import org.eclipse.fennec.m2m.model.qvtoperational.ObjectExp;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationBody;
import org.eclipse.fennec.m2m.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveExp;
import org.eclipse.fennec.m2m.model.qvtoperational.ResolveInExp;
import org.eclipse.fennec.m2m.model.qvtoperational.VarParameter;

import org.eclipse.fennec.m2m.model.trace.TracePackage;

import org.eclipse.fennec.m2m.model.trace.impl.TracePackageImpl;

/**
 * <!-- begin-user-doc -->
 * An implementation of the model <b>Package</b>.
 * <!-- end-user-doc -->
 * @generated
 */
public class QvtOperationalPackageImpl extends EPackageImpl implements QvtOperationalPackage {
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass operationBodyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mappingBodyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constructorBodyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass varParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mappingParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelParameterEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass imperativeOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass helperEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mappingOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass constructorEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass entryOperationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass moduleEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass operationalTransformationEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass libraryEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass modelTypeEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass moduleImportEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass contextualPropertyEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass imperativeCallExpEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass mappingCallExpEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass objectExpEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resolveExpEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EClass resolveInExpEClass = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum directionKindEEnum = null;

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private EEnum importKindEEnum = null;

	/**
	 * Creates an instance of the model <b>Package</b>, registered with
	 * {@link org.eclipse.emf.ecore.EPackage.Registry EPackage.Registry} by the package
	 * package URI value.
	 * <p>Note: the correct way to create the package is via the static
	 * factory method {@link #init init()}, which also performs
	 * initialization of the package, or returns the registered package,
	 * if one already exists.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see org.eclipse.emf.ecore.EPackage.Registry
	 * @see org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalPackage#eNS_URI
	 * @see #init()
	 * @generated
	 */
	private QvtOperationalPackageImpl() {
		super(eNS_URI, QvtOperationalFactory.eINSTANCE);
	}
	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private static boolean isInited = false;

	/**
	 * Creates, registers, and initializes the <b>Package</b> for this model, and for any others upon which it depends.
	 *
	 * <p>This method is used to initialize {@link QvtOperationalPackage#eINSTANCE} when that field is accessed.
	 * Clients should not invoke it directly. Instead, they should simply access that field to obtain the package.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @see #eNS_URI
	 * @see #createPackageContents()
	 * @see #initializePackageContents()
	 * @generated
	 */
	public static QvtOperationalPackage init() {
		if (isInited) return (QvtOperationalPackage)EPackage.Registry.INSTANCE.getEPackage(QvtOperationalPackage.eNS_URI);

		// Obtain or create and register package
		Object registeredQvtOperationalPackage = EPackage.Registry.INSTANCE.get(eNS_URI);
		QvtOperationalPackageImpl theQvtOperationalPackage = registeredQvtOperationalPackage instanceof QvtOperationalPackageImpl ? (QvtOperationalPackageImpl)registeredQvtOperationalPackage : new QvtOperationalPackageImpl();

		isInited = true;

		// Initialize simple dependencies
		OclPackage.eINSTANCE.eClass();

		// Obtain or create and register interdependencies
		Object registeredPackage = EPackage.Registry.INSTANCE.getEPackage(ImperativeOclPackage.eNS_URI);
		ImperativeOclPackageImpl theImperativeOclPackage = (ImperativeOclPackageImpl)(registeredPackage instanceof ImperativeOclPackageImpl ? registeredPackage : ImperativeOclPackage.eINSTANCE);
		registeredPackage = EPackage.Registry.INSTANCE.getEPackage(TracePackage.eNS_URI);
		TracePackageImpl theTracePackage = (TracePackageImpl)(registeredPackage instanceof TracePackageImpl ? registeredPackage : TracePackage.eINSTANCE);

		// Create package meta-data objects
		theQvtOperationalPackage.createPackageContents();
		theImperativeOclPackage.createPackageContents();
		theTracePackage.createPackageContents();

		// Initialize created meta-data
		theQvtOperationalPackage.initializePackageContents();
		theImperativeOclPackage.initializePackageContents();
		theTracePackage.initializePackageContents();

		// Mark meta-data to indicate it can't be changed
		theQvtOperationalPackage.freeze();

		// Update the registry and return the package
		EPackage.Registry.INSTANCE.put(QvtOperationalPackage.eNS_URI, theQvtOperationalPackage);
		return theQvtOperationalPackage;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOperationBody() {
		return operationBodyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOperationBody_Content() {
		return (EReference)operationBodyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOperationBody_Variable() {
		return (EReference)operationBodyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOperationBody_Operation() {
		return (EReference)operationBodyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMappingBody() {
		return mappingBodyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingBody_InitSection() {
		return (EReference)mappingBodyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingBody_EndSection() {
		return (EReference)mappingBodyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getConstructorBody() {
		return constructorBodyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getVarParameter() {
		return varParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getVarParameter_Kind() {
		return (EAttribute)varParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVarParameter_RepresentedParameter() {
		return (EReference)varParameterEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVarParameter_OwnedInit() {
		return (EReference)varParameterEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVarParameter_CtxOwner() {
		return (EReference)varParameterEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getVarParameter_ResOwner() {
		return (EReference)varParameterEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMappingParameter() {
		return mappingParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingParameter_Extent() {
		return (EReference)mappingParameterEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getModelParameter() {
		return modelParameterEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getImperativeOperation() {
		return imperativeOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getImperativeOperation_IsBlackbox() {
		return (EAttribute)imperativeOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getImperativeOperation_Body() {
		return (EReference)imperativeOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getImperativeOperation_Context() {
		return (EReference)imperativeOperationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getImperativeOperation_Result() {
		return (EReference)imperativeOperationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getImperativeOperation_Overridden() {
		return (EReference)imperativeOperationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getHelper() {
		return helperEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getHelper_IsQuery() {
		return (EAttribute)helperEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMappingOperation() {
		return mappingOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingOperation_When() {
		return (EReference)mappingOperationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingOperation_Where() {
		return (EReference)mappingOperationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingOperation_Disjunct() {
		return (EReference)mappingOperationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingOperation_Inherited() {
		return (EReference)mappingOperationEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getMappingOperation_Merged() {
		return (EReference)mappingOperationEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getConstructor() {
		return constructorEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getEntryOperation() {
		return entryOperationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getModule() {
		return moduleEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getModule_IsBlackbox() {
		return (EAttribute)moduleEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModule_ModuleImport() {
		return (EReference)moduleEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModule_OwnedVariable() {
		return (EReference)moduleEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModule_OwnedTag() {
		return (EReference)moduleEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModule_Entry() {
		return (EReference)moduleEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModule_ConfigProperty() {
		return (EReference)moduleEClass.getEStructuralFeatures().get(5);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModule_UsedModelType() {
		return (EReference)moduleEClass.getEStructuralFeatures().get(6);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getOperationalTransformation() {
		return operationalTransformationEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOperationalTransformation_ModelParameter() {
		return (EReference)operationalTransformationEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOperationalTransformation_IntermediateClass() {
		return (EReference)operationalTransformationEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getOperationalTransformation_IntermediateProperty() {
		return (EReference)operationalTransformationEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getLibrary() {
		return libraryEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getModelType() {
		return modelTypeEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getModelType_ConformanceKind() {
		return (EAttribute)modelTypeEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModelType_AdditionalCondition() {
		return (EReference)modelTypeEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModelType_Metamodel() {
		return (EReference)modelTypeEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getModuleImport() {
		return moduleImportEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getModuleImport_Kind() {
		return (EAttribute)moduleImportEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModuleImport_ImportedModule() {
		return (EReference)moduleImportEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModuleImport_Module() {
		return (EReference)moduleImportEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getModuleImport_Binding() {
		return (EReference)moduleImportEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getContextualProperty() {
		return contextualPropertyEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContextualProperty_InitExpression() {
		return (EReference)contextualPropertyEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContextualProperty_Context() {
		return (EReference)contextualPropertyEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getContextualProperty_Overridden() {
		return (EReference)contextualPropertyEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getImperativeCallExp() {
		return imperativeCallExpEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getImperativeCallExp_IsVirtual() {
		return (EAttribute)imperativeCallExpEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getMappingCallExp() {
		return mappingCallExpEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getMappingCallExp_IsStrict() {
		return (EAttribute)mappingCallExpEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getObjectExp() {
		return objectExpEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getObjectExp_Body() {
		return (EReference)objectExpEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getObjectExp_ReferredObject() {
		return (EReference)objectExpEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getResolveExp() {
		return resolveExpEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getResolveExp_IsDeferred() {
		return (EAttribute)resolveExpEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getResolveExp_IsInverse() {
		return (EAttribute)resolveExpEClass.getEStructuralFeatures().get(1);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EAttribute getResolveExp_One() {
		return (EAttribute)resolveExpEClass.getEStructuralFeatures().get(2);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getResolveExp_Condition() {
		return (EReference)resolveExpEClass.getEStructuralFeatures().get(3);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getResolveExp_Target() {
		return (EReference)resolveExpEClass.getEStructuralFeatures().get(4);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EClass getResolveInExp() {
		return resolveInExpEClass;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EReference getResolveInExp_InMapping() {
		return (EReference)resolveInExpEClass.getEStructuralFeatures().get(0);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getDirectionKind() {
		return directionKindEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public EEnum getImportKind() {
		return importKindEEnum;
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	@Override
	public QvtOperationalFactory getQvtOperationalFactory() {
		return (QvtOperationalFactory)getEFactoryInstance();
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isCreated = false;

	/**
	 * Creates the meta-model objects for the package.  This method is
	 * guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void createPackageContents() {
		if (isCreated) return;
		isCreated = true;

		// Create classes and their features
		operationBodyEClass = createEClass(OPERATION_BODY);
		createEReference(operationBodyEClass, OPERATION_BODY__CONTENT);
		createEReference(operationBodyEClass, OPERATION_BODY__VARIABLE);
		createEReference(operationBodyEClass, OPERATION_BODY__OPERATION);

		mappingBodyEClass = createEClass(MAPPING_BODY);
		createEReference(mappingBodyEClass, MAPPING_BODY__INIT_SECTION);
		createEReference(mappingBodyEClass, MAPPING_BODY__END_SECTION);

		constructorBodyEClass = createEClass(CONSTRUCTOR_BODY);

		varParameterEClass = createEClass(VAR_PARAMETER);
		createEAttribute(varParameterEClass, VAR_PARAMETER__KIND);
		createEReference(varParameterEClass, VAR_PARAMETER__REPRESENTED_PARAMETER);
		createEReference(varParameterEClass, VAR_PARAMETER__OWNED_INIT);
		createEReference(varParameterEClass, VAR_PARAMETER__CTX_OWNER);
		createEReference(varParameterEClass, VAR_PARAMETER__RES_OWNER);

		mappingParameterEClass = createEClass(MAPPING_PARAMETER);
		createEReference(mappingParameterEClass, MAPPING_PARAMETER__EXTENT);

		modelParameterEClass = createEClass(MODEL_PARAMETER);

		imperativeOperationEClass = createEClass(IMPERATIVE_OPERATION);
		createEAttribute(imperativeOperationEClass, IMPERATIVE_OPERATION__IS_BLACKBOX);
		createEReference(imperativeOperationEClass, IMPERATIVE_OPERATION__BODY);
		createEReference(imperativeOperationEClass, IMPERATIVE_OPERATION__CONTEXT);
		createEReference(imperativeOperationEClass, IMPERATIVE_OPERATION__RESULT);
		createEReference(imperativeOperationEClass, IMPERATIVE_OPERATION__OVERRIDDEN);

		helperEClass = createEClass(HELPER);
		createEAttribute(helperEClass, HELPER__IS_QUERY);

		mappingOperationEClass = createEClass(MAPPING_OPERATION);
		createEReference(mappingOperationEClass, MAPPING_OPERATION__WHEN);
		createEReference(mappingOperationEClass, MAPPING_OPERATION__WHERE);
		createEReference(mappingOperationEClass, MAPPING_OPERATION__DISJUNCT);
		createEReference(mappingOperationEClass, MAPPING_OPERATION__INHERITED);
		createEReference(mappingOperationEClass, MAPPING_OPERATION__MERGED);

		constructorEClass = createEClass(CONSTRUCTOR);

		entryOperationEClass = createEClass(ENTRY_OPERATION);

		moduleEClass = createEClass(MODULE);
		createEAttribute(moduleEClass, MODULE__IS_BLACKBOX);
		createEReference(moduleEClass, MODULE__MODULE_IMPORT);
		createEReference(moduleEClass, MODULE__OWNED_VARIABLE);
		createEReference(moduleEClass, MODULE__OWNED_TAG);
		createEReference(moduleEClass, MODULE__ENTRY);
		createEReference(moduleEClass, MODULE__CONFIG_PROPERTY);
		createEReference(moduleEClass, MODULE__USED_MODEL_TYPE);

		operationalTransformationEClass = createEClass(OPERATIONAL_TRANSFORMATION);
		createEReference(operationalTransformationEClass, OPERATIONAL_TRANSFORMATION__MODEL_PARAMETER);
		createEReference(operationalTransformationEClass, OPERATIONAL_TRANSFORMATION__INTERMEDIATE_CLASS);
		createEReference(operationalTransformationEClass, OPERATIONAL_TRANSFORMATION__INTERMEDIATE_PROPERTY);

		libraryEClass = createEClass(LIBRARY);

		modelTypeEClass = createEClass(MODEL_TYPE);
		createEAttribute(modelTypeEClass, MODEL_TYPE__CONFORMANCE_KIND);
		createEReference(modelTypeEClass, MODEL_TYPE__ADDITIONAL_CONDITION);
		createEReference(modelTypeEClass, MODEL_TYPE__METAMODEL);

		moduleImportEClass = createEClass(MODULE_IMPORT);
		createEAttribute(moduleImportEClass, MODULE_IMPORT__KIND);
		createEReference(moduleImportEClass, MODULE_IMPORT__IMPORTED_MODULE);
		createEReference(moduleImportEClass, MODULE_IMPORT__MODULE);
		createEReference(moduleImportEClass, MODULE_IMPORT__BINDING);

		contextualPropertyEClass = createEClass(CONTEXTUAL_PROPERTY);
		createEReference(contextualPropertyEClass, CONTEXTUAL_PROPERTY__INIT_EXPRESSION);
		createEReference(contextualPropertyEClass, CONTEXTUAL_PROPERTY__CONTEXT);
		createEReference(contextualPropertyEClass, CONTEXTUAL_PROPERTY__OVERRIDDEN);

		imperativeCallExpEClass = createEClass(IMPERATIVE_CALL_EXP);
		createEAttribute(imperativeCallExpEClass, IMPERATIVE_CALL_EXP__IS_VIRTUAL);

		mappingCallExpEClass = createEClass(MAPPING_CALL_EXP);
		createEAttribute(mappingCallExpEClass, MAPPING_CALL_EXP__IS_STRICT);

		objectExpEClass = createEClass(OBJECT_EXP);
		createEReference(objectExpEClass, OBJECT_EXP__BODY);
		createEReference(objectExpEClass, OBJECT_EXP__REFERRED_OBJECT);

		resolveExpEClass = createEClass(RESOLVE_EXP);
		createEAttribute(resolveExpEClass, RESOLVE_EXP__IS_DEFERRED);
		createEAttribute(resolveExpEClass, RESOLVE_EXP__IS_INVERSE);
		createEAttribute(resolveExpEClass, RESOLVE_EXP__ONE);
		createEReference(resolveExpEClass, RESOLVE_EXP__CONDITION);
		createEReference(resolveExpEClass, RESOLVE_EXP__TARGET);

		resolveInExpEClass = createEClass(RESOLVE_IN_EXP);
		createEReference(resolveInExpEClass, RESOLVE_IN_EXP__IN_MAPPING);

		// Create enums
		directionKindEEnum = createEEnum(DIRECTION_KIND);
		importKindEEnum = createEEnum(IMPORT_KIND);
	}

	/**
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	private boolean isInitialized = false;

	/**
	 * Complete the initialization of the package and its meta-model.  This
	 * method is guarded to have no affect on any invocation but its first.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	public void initializePackageContents() {
		if (isInitialized) return;
		isInitialized = true;

		// Initialize package
		setName(eNAME);
		setNsPrefix(eNS_PREFIX);
		setNsURI(eNS_URI);

		// Obtain other dependent packages
		OclPackage theOclPackage = (OclPackage)EPackage.Registry.INSTANCE.getEPackage(OclPackage.eNS_URI);
		ImperativeOclPackage theImperativeOclPackage = (ImperativeOclPackage)EPackage.Registry.INSTANCE.getEPackage(ImperativeOclPackage.eNS_URI);

		// Create type parameters

		// Set bounds for type parameters

		// Add supertypes to classes
		operationBodyEClass.getESuperTypes().add(ecorePackage.getEModelElement());
		mappingBodyEClass.getESuperTypes().add(this.getOperationBody());
		constructorBodyEClass.getESuperTypes().add(this.getOperationBody());
		varParameterEClass.getESuperTypes().add(ecorePackage.getEParameter());
		mappingParameterEClass.getESuperTypes().add(this.getVarParameter());
		modelParameterEClass.getESuperTypes().add(this.getVarParameter());
		imperativeOperationEClass.getESuperTypes().add(ecorePackage.getEOperation());
		helperEClass.getESuperTypes().add(this.getImperativeOperation());
		mappingOperationEClass.getESuperTypes().add(this.getImperativeOperation());
		constructorEClass.getESuperTypes().add(this.getImperativeOperation());
		entryOperationEClass.getESuperTypes().add(this.getImperativeOperation());
		moduleEClass.getESuperTypes().add(ecorePackage.getEPackage());
		operationalTransformationEClass.getESuperTypes().add(this.getModule());
		libraryEClass.getESuperTypes().add(this.getModule());
		modelTypeEClass.getESuperTypes().add(ecorePackage.getEClass());
		moduleImportEClass.getESuperTypes().add(ecorePackage.getEModelElement());
		contextualPropertyEClass.getESuperTypes().add(ecorePackage.getEStructuralFeature());
		imperativeCallExpEClass.getESuperTypes().add(theOclPackage.getOperationCallExp());
		imperativeCallExpEClass.getESuperTypes().add(theImperativeOclPackage.getImperativeExpression());
		mappingCallExpEClass.getESuperTypes().add(this.getImperativeCallExp());
		objectExpEClass.getESuperTypes().add(theImperativeOclPackage.getInstantiationExp());
		resolveExpEClass.getESuperTypes().add(theOclPackage.getCallExp());
		resolveInExpEClass.getESuperTypes().add(this.getResolveExp());

		// Initialize classes, features, and operations; add parameters
		initEClass(operationBodyEClass, OperationBody.class, "OperationBody", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOperationBody_Content(), theOclPackage.getOclExpression(), null, "content", null, 0, -1, OperationBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOperationBody_Variable(), theOclPackage.getVariable(), null, "variable", null, 0, -1, OperationBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOperationBody_Operation(), this.getImperativeOperation(), this.getImperativeOperation_Body(), "operation", null, 0, 1, OperationBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mappingBodyEClass, MappingBody.class, "MappingBody", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMappingBody_InitSection(), theOclPackage.getOclExpression(), null, "initSection", null, 0, -1, MappingBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappingBody_EndSection(), theOclPackage.getOclExpression(), null, "endSection", null, 0, -1, MappingBody.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constructorBodyEClass, ConstructorBody.class, "ConstructorBody", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(varParameterEClass, VarParameter.class, "VarParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getVarParameter_Kind(), this.getDirectionKind(), "kind", null, 0, 1, VarParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVarParameter_RepresentedParameter(), theOclPackage.getVariable(), null, "representedParameter", null, 0, 1, VarParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVarParameter_OwnedInit(), theOclPackage.getOclExpression(), null, "ownedInit", null, 0, 1, VarParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVarParameter_CtxOwner(), this.getImperativeOperation(), this.getImperativeOperation_Context(), "ctxOwner", null, 0, 1, VarParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getVarParameter_ResOwner(), this.getImperativeOperation(), this.getImperativeOperation_Result(), "resOwner", null, 0, 1, VarParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mappingParameterEClass, MappingParameter.class, "MappingParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMappingParameter_Extent(), this.getModelParameter(), null, "extent", null, 0, 1, MappingParameter.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(modelParameterEClass, ModelParameter.class, "ModelParameter", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(imperativeOperationEClass, ImperativeOperation.class, "ImperativeOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getImperativeOperation_IsBlackbox(), ecorePackage.getEBoolean(), "isBlackbox", null, 0, 1, ImperativeOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getImperativeOperation_Body(), this.getOperationBody(), this.getOperationBody_Operation(), "body", null, 0, 1, ImperativeOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getImperativeOperation_Context(), this.getVarParameter(), this.getVarParameter_CtxOwner(), "context", null, 0, 1, ImperativeOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getImperativeOperation_Result(), this.getVarParameter(), this.getVarParameter_ResOwner(), "result", null, 0, -1, ImperativeOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getImperativeOperation_Overridden(), this.getImperativeOperation(), null, "overridden", null, 0, 1, ImperativeOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(helperEClass, Helper.class, "Helper", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getHelper_IsQuery(), ecorePackage.getEBoolean(), "isQuery", null, 0, 1, Helper.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mappingOperationEClass, MappingOperation.class, "MappingOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getMappingOperation_When(), theOclPackage.getOclExpression(), null, "when", null, 0, -1, MappingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappingOperation_Where(), theOclPackage.getOclExpression(), null, "where", null, 0, 1, MappingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappingOperation_Disjunct(), this.getMappingOperation(), null, "disjunct", null, 0, -1, MappingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappingOperation_Inherited(), this.getMappingOperation(), null, "inherited", null, 0, -1, MappingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getMappingOperation_Merged(), this.getMappingOperation(), null, "merged", null, 0, -1, MappingOperation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(constructorEClass, Constructor.class, "Constructor", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(entryOperationEClass, EntryOperation.class, "EntryOperation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(moduleEClass, org.eclipse.fennec.m2m.model.qvtoperational.Module.class, "Module", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModule_IsBlackbox(), ecorePackage.getEBoolean(), "isBlackbox", null, 0, 1, org.eclipse.fennec.m2m.model.qvtoperational.Module.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModule_ModuleImport(), this.getModuleImport(), this.getModuleImport_Module(), "moduleImport", null, 0, -1, org.eclipse.fennec.m2m.model.qvtoperational.Module.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModule_OwnedVariable(), theOclPackage.getVariable(), null, "ownedVariable", null, 0, -1, org.eclipse.fennec.m2m.model.qvtoperational.Module.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModule_OwnedTag(), ecorePackage.getEAnnotation(), null, "ownedTag", null, 0, -1, org.eclipse.fennec.m2m.model.qvtoperational.Module.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModule_Entry(), this.getEntryOperation(), null, "entry", null, 0, 1, org.eclipse.fennec.m2m.model.qvtoperational.Module.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModule_ConfigProperty(), ecorePackage.getEStructuralFeature(), null, "configProperty", null, 0, -1, org.eclipse.fennec.m2m.model.qvtoperational.Module.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModule_UsedModelType(), this.getModelType(), null, "usedModelType", null, 0, -1, org.eclipse.fennec.m2m.model.qvtoperational.Module.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(operationalTransformationEClass, OperationalTransformation.class, "OperationalTransformation", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getOperationalTransformation_ModelParameter(), this.getModelParameter(), null, "modelParameter", null, 0, -1, OperationalTransformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOperationalTransformation_IntermediateClass(), ecorePackage.getEClass(), null, "intermediateClass", null, 0, -1, OperationalTransformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getOperationalTransformation_IntermediateProperty(), ecorePackage.getEStructuralFeature(), null, "intermediateProperty", null, 0, -1, OperationalTransformation.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(libraryEClass, Library.class, "Library", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);

		initEClass(modelTypeEClass, ModelType.class, "ModelType", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModelType_ConformanceKind(), ecorePackage.getEString(), "conformanceKind", null, 0, 1, ModelType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelType_AdditionalCondition(), theOclPackage.getOclExpression(), null, "additionalCondition", null, 0, -1, ModelType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModelType_Metamodel(), ecorePackage.getEPackage(), null, "metamodel", null, 1, -1, ModelType.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(moduleImportEClass, ModuleImport.class, "ModuleImport", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getModuleImport_Kind(), this.getImportKind(), "kind", null, 0, 1, ModuleImport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModuleImport_ImportedModule(), this.getModule(), null, "importedModule", null, 1, 1, ModuleImport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModuleImport_Module(), this.getModule(), this.getModule_ModuleImport(), "module", null, 0, 1, ModuleImport.class, IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getModuleImport_Binding(), this.getModelType(), null, "binding", null, 0, -1, ModuleImport.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(contextualPropertyEClass, ContextualProperty.class, "ContextualProperty", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getContextualProperty_InitExpression(), theOclPackage.getOclExpression(), null, "initExpression", null, 0, 1, ContextualProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContextualProperty_Context(), ecorePackage.getEClass(), null, "context", null, 1, 1, ContextualProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getContextualProperty_Overridden(), ecorePackage.getEStructuralFeature(), null, "overridden", null, 0, 1, ContextualProperty.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(imperativeCallExpEClass, ImperativeCallExp.class, "ImperativeCallExp", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getImperativeCallExp_IsVirtual(), ecorePackage.getEBoolean(), "isVirtual", "true", 0, 1, ImperativeCallExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(mappingCallExpEClass, MappingCallExp.class, "MappingCallExp", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getMappingCallExp_IsStrict(), ecorePackage.getEBoolean(), "isStrict", null, 0, 1, MappingCallExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(objectExpEClass, ObjectExp.class, "ObjectExp", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getObjectExp_Body(), this.getConstructorBody(), null, "body", null, 0, 1, ObjectExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getObjectExp_ReferredObject(), theOclPackage.getVariable(), null, "referredObject", null, 1, 1, ObjectExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resolveExpEClass, ResolveExp.class, "ResolveExp", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEAttribute(getResolveExp_IsDeferred(), ecorePackage.getEBoolean(), "isDeferred", null, 0, 1, ResolveExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResolveExp_IsInverse(), ecorePackage.getEBoolean(), "isInverse", null, 0, 1, ResolveExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEAttribute(getResolveExp_One(), ecorePackage.getEBoolean(), "one", null, 0, 1, ResolveExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_UNSETTABLE, !IS_ID, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getResolveExp_Condition(), theOclPackage.getOclExpression(), null, "condition", null, 0, 1, ResolveExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);
		initEReference(getResolveExp_Target(), theOclPackage.getVariable(), null, "target", null, 0, 1, ResolveExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, IS_COMPOSITE, !IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		initEClass(resolveInExpEClass, ResolveInExp.class, "ResolveInExp", !IS_ABSTRACT, !IS_INTERFACE, IS_GENERATED_INSTANCE_CLASS);
		initEReference(getResolveInExp_InMapping(), this.getMappingOperation(), null, "inMapping", null, 0, 1, ResolveInExp.class, !IS_TRANSIENT, !IS_VOLATILE, IS_CHANGEABLE, !IS_COMPOSITE, IS_RESOLVE_PROXIES, !IS_UNSETTABLE, IS_UNIQUE, !IS_DERIVED, IS_ORDERED);

		// Initialize enums and add enum literals
		initEEnum(directionKindEEnum, DirectionKind.class, "DirectionKind");
		addEEnumLiteral(directionKindEEnum, DirectionKind.IN);
		addEEnumLiteral(directionKindEEnum, DirectionKind.INOUT);
		addEEnumLiteral(directionKindEEnum, DirectionKind.OUT);

		initEEnum(importKindEEnum, ImportKind.class, "ImportKind");
		addEEnumLiteral(importKindEEnum, ImportKind.EXTENSION);
		addEEnumLiteral(importKindEEnum, ImportKind.ACCESS);

		// Create resource
		createResource(eNS_URI);

		// Create annotations
		// Version
		createVersionAnnotations();
	}

	/**
	 * Initializes the annotations for <b>Version</b>.
	 * <!-- begin-user-doc -->
	 * <!-- end-user-doc -->
	 * @generated
	 */
	protected void createVersionAnnotations() {
		String source = "Version";
		addAnnotation
		  (this,
		   source,
		   new String[] {
			   "value", "1.0"
		   });
	}

} //QvtOperationalPackageImpl
