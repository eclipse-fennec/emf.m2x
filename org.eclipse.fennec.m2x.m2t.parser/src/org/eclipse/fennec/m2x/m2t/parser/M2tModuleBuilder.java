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
package org.eclipse.fennec.m2x.m2t.parser;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.m2t.Block;
import org.eclipse.fennec.m2x.model.m2t.FileBlock;
import org.eclipse.fennec.m2x.model.m2t.ForBlock;
import org.eclipse.fennec.m2x.model.m2t.IfBlock;
import org.eclipse.fennec.m2x.model.m2t.InitSection;
import org.eclipse.fennec.m2x.model.m2t.LetBlock;
import org.eclipse.fennec.m2x.model.m2t.M2tFactory;
import org.eclipse.fennec.m2x.model.m2t.Macro;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;
import org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock;
import org.eclipse.fennec.m2x.model.m2t.Query;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateExpression;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.model.m2t.TextExpression;
import org.eclipse.fennec.m2x.model.m2t.TraceBlock;
import org.eclipse.fennec.m2x.model.m2t.VisibilityKind;
import org.eclipse.fennec.m2x.model.ocl.BooleanLiteralExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.OclType;
import org.eclipse.fennec.m2x.model.ocl.Variable;

/**
 * Visitor that transforms an ANTLR4 M2T parse tree (CST) into an EMF M2T AST.
 *
 * <p>Builds a {@link Module} with its templates, queries, macros, and body elements.
 * OCL expressions embedded in templates are built via {@link M2tExpressionBuilder}.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class M2tModuleBuilder extends M2tParserBaseVisitor<Object> {

	private static final M2tFactory FACTORY = M2tFactory.eINSTANCE;

	private final String unitName;
	private final M2tExpressionBuilder exprBuilder;
	private final EPackage.Registry packageRegistry;
	private Module module;

	// Pending references — collected during parse, resolved during linking
	private final List<String> pendingExtends = new ArrayList<>();
	private final List<String> pendingImports = new ArrayList<>();
	private final Map<Template, List<String>> pendingOverrides = new LinkedHashMap<>();
	private final Map<TemplateInvocation, String> pendingInvocations = new LinkedHashMap<>();

	/**
	 * Creates a new module builder.
	 *
	 * @param unitName the logical unit name for the module
	 * @param contextType the default context type for OCL expressions (may be EObject)
	 * @param packageRegistry optional package registry for type resolution
	 */
	M2tModuleBuilder(String unitName, EClassifier contextType, EPackage.Registry packageRegistry) {
		this.unitName = unitName;
		this.exprBuilder = new M2tExpressionBuilder(contextType, packageRegistry);
		this.packageRegistry = packageRegistry;
	}

	/**
	 * Returns the diagnostics collected while building — unresolved type names (#66).
	 */
	List<Resource.Diagnostic> getDiagnostics() {
		return exprBuilder.support.getDiagnostics();
	}

	// ==================== Module ====================

	@Override
	public Module visitModule(M2tParser.ModuleContext ctx) {
		module = FACTORY.createModule();

		// Module declaration
		if (ctx.moduleDecl() != null) {
			visitModuleDecl(ctx.moduleDecl());
		} else {
			module.setName(unitName);
		}

		// Import declarations
		for (M2tParser.ImportDeclContext importCtx : ctx.importDecl()) {
			List<String> segments = M2tExpressionBuilder.pathNameSegments(importCtx.pathName());
			pendingImports.add(String.join("::", segments));
		}

		// Module elements (templates, queries, macros)
		for (M2tParser.ModuleElementContext elemCtx : ctx.moduleElement()) {
			visit(elemCtx);
		}

		return module;
	}

	@Override
	public Object visitModuleDecl(M2tParser.ModuleDeclContext ctx) {
		List<String> nameSegments = M2tExpressionBuilder.pathNameSegments(ctx.pathName());
		module.setName(String.join("::", nameSegments));

		// Metamodel declarations — stored as EPackage references
		// (At parse time we just record the names; resolution happens later)
		if (ctx.metamodelList() != null) {
			for (M2tParser.PathNameContext metamodelPath : ctx.metamodelList().pathName()) {
				List<String> segments = M2tExpressionBuilder.pathNameSegments(metamodelPath);
				String metamodelUri = String.join("::", segments);
				// Resolve from the registry this build was given (D42)
				EPackage pkg = packageRegistry == null ? null
						: packageRegistry.getEPackage(metamodelUri);
				if (pkg != null) {
					module.getInput().add(pkg);
				}
			}
		}

		// Extends declarations — collect names for linking
		if (ctx.extendsDecl() != null) {
			for (M2tParser.PathNameContext pathCtx : ctx.extendsDecl().pathName()) {
				List<String> segments = M2tExpressionBuilder.pathNameSegments(pathCtx);
				pendingExtends.add(String.join("::", segments));
			}
		}

		return null;
	}

	// ==================== Module Elements ====================

	@Override
	public Object visitModuleElement(M2tParser.ModuleElementContext ctx) {
		if (ctx.templateDef() != null) {
			visit(ctx.templateDef());
		} else if (ctx.queryDef() != null) {
			visit(ctx.queryDef());
		} else if (ctx.macroDef() != null) {
			visit(ctx.macroDef());
		}
		// commentBlock is discarded
		return null;
	}

	// ==================== Template ====================

	@Override
	public Object visitTemplateDef(M2tParser.TemplateDefContext ctx) {
		Template template = FACTORY.createTemplate();
		M2tParser.SignatureContext sig = ctx.signature();

		// Name
		List<String> nameSegments = M2tExpressionBuilder.pathNameSegments(sig.pathName());
		template.setName(String.join("::", nameSegments));

		// Visibility
		if (sig.visibility() != null) {
			if (sig.visibility().KW_PRIVATE() != null) {
				template.setVisibility(VisibilityKind.PRIVATE);
			} else if (sig.visibility().KW_PROTECTED() != null) {
				template.setVisibility(VisibilityKind.PROTECTED);
			} else {
				template.setVisibility(VisibilityKind.PUBLIC);
			}
		}

		// Parameters
		if (sig.argList() != null) {
			for (M2tParser.ArgDeclContext argCtx : sig.argList().argDecl()) {
				Variable param = buildParameter(argCtx);
				template.getParameter().add(param);

				// First parameter determines if this is a "main" template
				// (main = public template with first param typed to a metamodel input type)
			}
		}

		// Overrides declarations — collect names for linking
		if (sig.overridesDecl() != null) {
			List<String> overrideList = new ArrayList<>();
			for (M2tParser.PathNameContext pathCtx : sig.overridesDecl().pathName()) {
				List<String> segments = M2tExpressionBuilder.pathNameSegments(pathCtx);
				overrideList.add(String.join("::", segments));
			}
			pendingOverrides.put(template, overrideList);
		}

		// Guard
		if (sig.guard() != null) {
			template.setGuard(exprBuilder.buildExpression(sig.guard().expression()));
		}

		// Post
		if (sig.postDecl() != null) {
			template.setPost(exprBuilder.buildExpression(sig.postDecl().expression()));
		}

		// Init section
		if (sig.initSection() != null) {
			template.setInit(buildInitSection(sig.initSection()));
		}

		// Body
		if (ctx.body() != null) {
			for (TemplateExpression bodyExpr : buildBody(ctx.body())) {
				template.getBody().add(bodyExpr);
			}
		}

		module.getOwnedModuleElement().add(template);
		return template;
	}

	// ==================== Query ====================

	@Override
	public Object visitQueryDef(M2tParser.QueryDefContext ctx) {
		Query query = FACTORY.createQuery();

		List<String> nameSegments = M2tExpressionBuilder.pathNameSegments(ctx.pathName());
		query.setName(String.join("::", nameSegments));

		// Visibility
		if (ctx.visibility() != null) {
			if (ctx.visibility().KW_PRIVATE() != null) {
				query.setVisibility(VisibilityKind.PRIVATE);
			} else if (ctx.visibility().KW_PROTECTED() != null) {
				query.setVisibility(VisibilityKind.PROTECTED);
			} else {
				query.setVisibility(VisibilityKind.PUBLIC);
			}
		}

		// Parameters
		if (ctx.argList() != null) {
			for (M2tParser.ArgDeclContext argCtx : ctx.argList().argDecl()) {
				query.getParameter().add(buildParameter(argCtx));
			}
		}

		// Return type
		query.setReturnType(exprBuilder.resolveTypeExpression(ctx.typeExpression()));

		// Expression body
		query.setExpression(exprBuilder.buildExpression(ctx.expression()));

		module.getOwnedModuleElement().add(query);
		return query;
	}

	// ==================== Macro ====================

	@Override
	public Object visitMacroDef(M2tParser.MacroDefContext ctx) {
		Macro macro = FACTORY.createMacro();

		List<String> nameSegments = M2tExpressionBuilder.pathNameSegments(ctx.pathName());
		macro.setName(String.join("::", nameSegments));

		// Parameters
		if (ctx.argList() != null) {
			for (M2tParser.ArgDeclContext argCtx : ctx.argList().argDecl()) {
				macro.getParameter().add(buildParameter(argCtx));
			}
		}

		// Body
		if (ctx.body() != null) {
			for (TemplateExpression bodyExpr : buildBody(ctx.body())) {
				macro.getBody().add(bodyExpr);
			}
		}

		module.getOwnedModuleElement().add(macro);
		return macro;
	}

	// ==================== Body Elements ====================

	private List<TemplateExpression> buildBody(M2tParser.BodyContext ctx) {
		List<TemplateExpression> result = new ArrayList<>();
		if (ctx.bodyElement() == null) {
			return result;
		}
		for (M2tParser.BodyElementContext elemCtx : ctx.bodyElement()) {
			TemplateExpression expr = buildBodyElement(elemCtx);
			if (expr != null) {
				result.add(expr);
			}
		}
		return result;
	}

	private TemplateExpression buildBodyElement(M2tParser.BodyElementContext ctx) {
		if (ctx.textBlock() != null) {
			return buildTextBlock(ctx.textBlock());
		}
		if (ctx.inlineExpression() != null) {
			return buildInlineExpression(ctx.inlineExpression());
		}
		if (ctx.forBlock() != null) {
			return buildForBlock(ctx.forBlock());
		}
		if (ctx.ifBlock() != null) {
			return buildIfBlock(ctx.ifBlock());
		}
		if (ctx.letBlock() != null) {
			return buildLetBlock(ctx.letBlock());
		}
		if (ctx.fileBlock() != null) {
			return buildFileBlock(ctx.fileBlock());
		}
		if (ctx.traceBlock() != null) {
			return buildTraceBlock(ctx.traceBlock());
		}
		if (ctx.protectedBlock() != null) {
			return buildProtectedBlock(ctx.protectedBlock());
		}
		if (ctx.templateInvocation() != null) {
			return buildTemplateInvocation(ctx.templateInvocation());
		}
		// commentBlock — discarded
		return null;
	}

	// ==================== Text Block ====================

	private TextExpression buildTextBlock(M2tParser.TextBlockContext ctx) {
		TextExpression text = FACTORY.createTextExpression();
		StringBuilder sb = new StringBuilder();
		for (var token : ctx.TEXT()) {
			sb.append(token.getText());
		}
		text.setValue(sb.toString());
		return text;
	}

	// ==================== Inline Expression ====================

	private TemplateExpression buildInlineExpression(M2tParser.InlineExpressionContext ctx) {
		// An inline expression [expr/] evaluates an OCL expression and converts result to text.
		// OclExpression is the supertype of TemplateExpression, so a plain OCL expression
		// cannot be added directly to EList<TemplateExpression> (EMF enforces EType).
		// We wrap it in a LetBlock: [let __inline = <expr>] outputs the variable value.
		OclExpression expr = exprBuilder.buildExpression(ctx.expression());

		// If the expression is already a TemplateExpression, return directly
		if (expr instanceof TemplateExpression te) {
			return te;
		}

		// Wrap in a LetBlock: the evaluator outputs let variable values
		LetBlock wrapper = FACTORY.createLetBlock();
		Variable var = OclFactory.eINSTANCE.createVariable();
		var.setName("__inline__" + System.identityHashCode(ctx));
		var.setOwnedInit(expr);
		wrapper.setLetVariable(var);
		// Store the original expression as a marker so the evaluator can output its value
		// The LetBlock has no body elements — the evaluator should output the let variable
		return wrapper;
	}

	// ==================== For Block ====================

	private ForBlock buildForBlock(M2tParser.ForBlockContext ctx) {
		ForBlock forBlock = FACTORY.createForBlock();

		// Loop variable
		Variable loopVar = buildParameter(ctx.argDecl());
		forBlock.setLoopVariable(loopVar);

		// Iterator set (collection expression)
		forBlock.setIterSet(exprBuilder.buildExpression(ctx.expression()));

		// Before/separator/after
		if (ctx.beforeClause() != null) {
			forBlock.setBefore(exprBuilder.buildExpression(ctx.beforeClause().expression()));
		}
		if (ctx.separatorClause() != null) {
			forBlock.setEach(exprBuilder.buildExpression(ctx.separatorClause().expression()));
		}
		if (ctx.afterClause() != null) {
			forBlock.setAfter(exprBuilder.buildExpression(ctx.afterClause().expression()));
		}

		// Guard
		if (ctx.guard() != null) {
			forBlock.setGuard(exprBuilder.buildExpression(ctx.guard().expression()));
		}

		// Init section
		if (ctx.initSection() != null) {
			forBlock.setInit(buildInitSection(ctx.initSection()));
		}

		// Body
		for (TemplateExpression bodyExpr : buildBody(ctx.body())) {
			forBlock.getBody().add(bodyExpr);
		}

		return forBlock;
	}

	// ==================== If Block ====================

	private IfBlock buildIfBlock(M2tParser.IfBlockContext ctx) {
		IfBlock ifBlock = FACTORY.createIfBlock();

		// Condition
		ifBlock.setIfExpr(exprBuilder.buildExpression(ctx.expression()));

		// Body
		for (TemplateExpression bodyExpr : buildBody(ctx.body())) {
			ifBlock.getBody().add(bodyExpr);
		}

		// ElseIf chain
		if (ctx.elseIfBlock() != null) {
			for (M2tParser.ElseIfBlockContext elseIfCtx : ctx.elseIfBlock()) {
				IfBlock elseIfBlock = FACTORY.createIfBlock();
				elseIfBlock.setIfExpr(exprBuilder.buildExpression(elseIfCtx.expression()));
				for (TemplateExpression bodyExpr : buildBody(elseIfCtx.body())) {
					elseIfBlock.getBody().add(bodyExpr);
				}
				ifBlock.getElseIf().add(elseIfBlock);
			}
		}

		// Else block
		if (ctx.elseBlock() != null) {
			Block elseBlock = FACTORY.createIfBlock();
			for (TemplateExpression bodyExpr : buildBody(ctx.elseBlock().body())) {
				elseBlock.getBody().add(bodyExpr);
			}
			ifBlock.setElse(elseBlock);
		}

		return ifBlock;
	}

	// ==================== Let Block ====================

	private LetBlock buildLetBlock(M2tParser.LetBlockContext ctx) {
		LetBlock letBlock = FACTORY.createLetBlock();

		// Variable declaration
		M2tParser.VariableDeclarationContext varDecl = ctx.variableDeclaration();
		Variable letVar = buildVariableDeclaration(varDecl);
		letBlock.setLetVariable(letVar);

		// Body
		for (TemplateExpression bodyExpr : buildBody(ctx.body())) {
			letBlock.getBody().add(bodyExpr);
		}

		// ElseLet chain
		if (ctx.elseLetBlock() != null) {
			for (M2tParser.ElseLetBlockContext elseLetCtx : ctx.elseLetBlock()) {
				LetBlock elseLetBlock = FACTORY.createLetBlock();
				Variable elseLetVar = buildVariableDeclaration(elseLetCtx.variableDeclaration());
				elseLetBlock.setLetVariable(elseLetVar);
				for (TemplateExpression bodyExpr : buildBody(elseLetCtx.body())) {
					elseLetBlock.getBody().add(bodyExpr);
				}
				letBlock.getElseLet().add(elseLetBlock);
			}
		}

		// Else block
		if (ctx.elseBlock() != null) {
			Block elseBlock = FACTORY.createIfBlock();
			for (TemplateExpression bodyExpr : buildBody(ctx.elseBlock().body())) {
				elseBlock.getBody().add(bodyExpr);
			}
			letBlock.setElse(elseBlock);
		}

		return letBlock;
	}

	// ==================== File Block ====================

	private FileBlock buildFileBlock(M2tParser.FileBlockContext ctx) {
		FileBlock fileBlock = FACTORY.createFileBlock();

		List<M2tParser.ExpressionContext> exprs = ctx.expression();

		// File URL (first expression)
		fileBlock.setFileUrl(exprBuilder.buildExpression(exprs.get(0)));

		// Append mode (second expression — Boolean, MOFM2T §7.3 / §8.1.17)
		if (exprs.size() > 1) {
			OclExpression modeExpr = exprBuilder.buildExpression(exprs.get(1));
			if (modeExpr instanceof BooleanLiteralExp boolLit && boolLit.isBooleanSymbol()) {
				fileBlock.setOpenMode(OpenModeKind.APPEND);
			}
		}

		// Unique ID (third expression — MOFM2T §8.1.17)
		if (exprs.size() > 2) {
			fileBlock.setUniqId(exprBuilder.buildExpression(exprs.get(2)));
		}

		// Body
		for (TemplateExpression bodyExpr : buildBody(ctx.body())) {
			fileBlock.getBody().add(bodyExpr);
		}

		return fileBlock;
	}

	// ==================== Trace Block ====================

	private TraceBlock buildTraceBlock(M2tParser.TraceBlockContext ctx) {
		TraceBlock traceBlock = FACTORY.createTraceBlock();
		traceBlock.setModelElement(exprBuilder.buildExpression(ctx.expression()));

		for (TemplateExpression bodyExpr : buildBody(ctx.body())) {
			traceBlock.getBody().add(bodyExpr);
		}

		return traceBlock;
	}

	// ==================== Protected Block ====================

	private ProtectedAreaBlock buildProtectedBlock(M2tParser.ProtectedBlockContext ctx) {
		ProtectedAreaBlock protectedBlock = FACTORY.createProtectedAreaBlock();
		protectedBlock.setMarker(exprBuilder.buildExpression(ctx.expression()));

		for (TemplateExpression bodyExpr : buildBody(ctx.body())) {
			protectedBlock.getBody().add(bodyExpr);
		}

		return protectedBlock;
	}

	// ==================== Template Invocation ====================

	private TemplateInvocation buildTemplateInvocation(M2tParser.TemplateInvocationContext ctx) {
		TemplateInvocation invocation = FACTORY.createTemplateInvocation();

		// Check for [super/]
		if (ctx.KW_SUPER() != null) {
			invocation.setSuper(true);
			return invocation;
		}

		// Template name — collect for linking
		List<String> nameSegments = M2tExpressionBuilder.pathNameSegments(ctx.pathName());
		String invocationName = String.join("::", nameSegments);
		pendingInvocations.put(invocation, invocationName);

		// Arguments
		if (ctx.argumentList() != null) {
			for (M2tParser.ExpressionContext argCtx : ctx.argumentList().expression()) {
				invocation.getArgument().add(exprBuilder.buildExpression(argCtx));
			}
		}

		// Before/separator/after
		if (ctx.beforeClause() != null) {
			invocation.setBefore(exprBuilder.buildExpression(ctx.beforeClause().expression()));
		}
		if (ctx.separatorClause() != null) {
			invocation.setEach(exprBuilder.buildExpression(ctx.separatorClause().expression()));
		}
		if (ctx.afterClause() != null) {
			invocation.setAfter(exprBuilder.buildExpression(ctx.afterClause().expression()));
		}

		return invocation;
	}

	// ==================== Helper Methods ====================

	private Variable buildParameter(M2tParser.ArgDeclContext ctx) {
		String name = M2tExpressionBuilder.identifierText(ctx.identifier());
		OclType type = exprBuilder.resolveTypeExpression(ctx.typeExpression());
		return exprBuilder.support.createVariable(name, type, null);
	}

	private Variable buildVariableDeclaration(M2tParser.VariableDeclarationContext ctx) {
		String name = M2tExpressionBuilder.identifierText(ctx.identifier());
		OclType type = exprBuilder.resolveTypeExpression(ctx.typeExpression());
		OclExpression init = ctx.expression() != null
				? exprBuilder.buildExpression(ctx.expression()) : null;
		return exprBuilder.support.createVariable(name, type, init);
	}

	private InitSection buildInitSection(M2tParser.InitSectionContext ctx) {
		InitSection initSection = FACTORY.createInitSection();
		for (M2tParser.VariableDeclarationContext varDecl : ctx.variableDeclaration()) {
			initSection.getVariable().add(buildVariableDeclaration(varDecl));
		}
		return initSection;
	}

	/**
	 * Returns the parse result containing the module and all pending name references.
	 * Must be called after {@link #visitModule(M2tParser.ModuleContext)}.
	 */
	M2tParseResult getParseResult() {
		return new M2tParseResult(module, pendingExtends, pendingImports,
				pendingOverrides, pendingInvocations);
	}
}
