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
package org.eclipse.fennec.m2x.qvtd.parser;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.ocl.IterateExp;
import org.eclipse.fennec.m2x.model.ocl.IteratorExp;
import org.eclipse.fennec.m2x.model.ocl.LetExp;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtbase.Domain;
import org.eclipse.fennec.m2x.model.qvtbase.Pattern;
import org.eclipse.fennec.m2x.model.qvtbase.Predicate;
import org.eclipse.fennec.m2x.model.qvtrelation.DomainPattern;
import org.eclipse.fennec.m2x.model.qvtrelation.Relation;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationCallExp;
import org.eclipse.fennec.m2x.model.qvtrelation.RelationDomain;
import org.eclipse.fennec.m2x.model.qvttemplate.CollectionTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.ObjectTemplateExp;
import org.eclipse.fennec.m2x.model.qvttemplate.PropertyTemplateItem;
import org.eclipse.fennec.m2x.model.qvttemplate.TemplateExp;

/**
 * Static validator for QVT-R §7.5 "Restrictions on Expressions".
 *
 * <p>Validates that all variable references in a relation can be organized into
 * a valid sequential binding order, ensuring executability. This is a post-parse
 * validation pass run after the AST has been built by {@link QvtrUnitBuilder}.
 *
 * <p>The validator checks two constraints from the specification:
 * <ol>
 *   <li><b>Source side (§7.5 Rule 1):</b> Expressions in the when-clause, source domains,
 *       and where-clause must be organizable into a sequential order where each variable
 *       is bound before it is used.</li>
 *   <li><b>Target side (§7.5 Rule 2):</b> Expressions in the target domain must only
 *       reference variables that are already bound by the source side.</li>
 * </ol>
 *
 * <p>Since the target domain is determined at execution time (not parse time), the
 * validator checks that for <em>each</em> domain as a potential target, all its
 * non-binding variable references are satisfiable by the remaining domains,
 * the when-clause, and explicit variable declarations.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 * @see <a href="https://www.omg.org/spec/QVT/1.3">QVT v1.3, §7.5</a>
 */
class QvtrBindingValidator {

	/** Variables that are implicitly available and never flagged as unresolved. */
	private static final Set<String> IMPLICIT_VARS = Set.of("self", "_");

	/**
	 * Validates §7.5 binding restrictions for a single relation.
	 *
	 * @param relation the relation to validate
	 * @return list of diagnostics (empty if the relation is valid)
	 */
	List<Resource.Diagnostic> validate(Relation relation) {
		List<Resource.Diagnostic> diagnostics = new ArrayList<>();
		String relName = relation.getName();

		// Phase 1: Collect binding sites per scope
		Set<String> varDeclBindings = collectVarDeclBindings(relation);
		Set<String> whenBindings = collectWhenBindings(relation);
		Map<RelationDomain, Set<String>> domainBindings = collectAllDomainBindings(relation);

		Set<String> allBindings = new HashSet<>(varDeclBindings);
		allBindings.addAll(whenBindings);
		domainBindings.values().forEach(allBindings::addAll);

		// Phase 2: Check when-clause — all non-binding variable references must
		// be resolvable from variable declarations or when-clause bindings themselves
		if (relation.getWhen() != null) {
			Set<String> whenAvailable = new HashSet<>(varDeclBindings);
			whenAvailable.addAll(whenBindings);
			checkPattern(relation.getWhen(), whenAvailable, "when-clause", relName,
					diagnostics);
		}

		// Phase 3: Check each domain as potential target (§7.5 Rule 2)
		// For each domain D: all variable references that are not bound BY D
		// must be satisfiable from external sources (when + varDecls + other domains)
		for (var entry : domainBindings.entrySet()) {
			RelationDomain domain = entry.getKey();
			Set<String> ownBindings = entry.getValue();

			Set<String> externalBindings = new HashSet<>(varDeclBindings);
			externalBindings.addAll(whenBindings);
			for (var other : domainBindings.entrySet()) {
				if (other.getKey() != domain) {
					externalBindings.addAll(other.getValue());
				}
			}

			// All references in the domain must be bound by own + external
			Set<String> available = new HashSet<>(ownBindings);
			available.addAll(externalBindings);
			checkDomain(domain, available, relName, diagnostics);

			// §7.5 Rule 2: references that are NOT own-bindings must come from external
			Set<String> usages = collectDomainUsages(domain);
			for (String usage : usages) {
				if (IMPLICIT_VARS.contains(usage)) {
					continue;
				}
				if (!ownBindings.contains(usage) && !externalBindings.contains(usage)) {
					diagnostics.add(new QvtdParseDiagnostic(
							"§7.5: Variable '" + usage + "' in domain '"
									+ domain.getName() + "' of relation '" + relName
									+ "' is never bound (no valid binding order exists)",
							0, 0));
				}
			}
		}

		// Phase 4: Check where-clause — all refs must be bound by when + all domains + varDecls
		if (relation.getWhere() != null) {
			checkPattern(relation.getWhere(), allBindings, "where-clause", relName,
					diagnostics);
		}

		return diagnostics;
	}

	// ==================== Binding Collection ====================

	/**
	 * Collects variables bound by explicit relation-level variable declarations.
	 * Includes variables with or without init expressions, since template matching
	 * can also bind declared variables.
	 */
	private Set<String> collectVarDeclBindings(Relation relation) {
		Set<String> bindings = new HashSet<>();
		for (Variable v : relation.getVariable()) {
			bindings.add(v.getName());
		}
		return bindings;
	}

	/**
	 * Collects variables bound by the when-clause.
	 * {@link RelationCallExp} arguments in the when-clause are bound from trace records.
	 */
	private Set<String> collectWhenBindings(Relation relation) {
		Set<String> bindings = new HashSet<>();
		if (relation.getWhen() == null) {
			return bindings;
		}
		for (Predicate pred : relation.getWhen().getPredicate()) {
			OclExpression expr = pred.getConditionExpression();
			if (expr instanceof RelationCallExp relCall) {
				// §7.2.1: RelationCallExp arguments in when are bound from trace
				for (OclExpression arg : relCall.getArgument()) {
					if (arg instanceof VariableExp ve && ve.getReferredVariable() != null) {
						bindings.add(ve.getReferredVariable().getName());
					}
				}
			}
		}
		return bindings;
	}

	/**
	 * Collects variables bound by each domain's template patterns.
	 */
	private Map<RelationDomain, Set<String>> collectAllDomainBindings(Relation relation) {
		Map<RelationDomain, Set<String>> result = new LinkedHashMap<>();
		for (Domain d : relation.getDomain()) {
			if (d instanceof RelationDomain rd) {
				Set<String> bindings = new HashSet<>();
				// Root variables (primitive domains + template bindsTo)
				for (Variable v : rd.getRootVariable()) {
					bindings.add(v.getName());
				}
				// Template bindings
				for (DomainPattern dp : rd.getPattern()) {
					if (dp.getTemplateExpression() != null) {
						collectTemplateBindings(dp.getTemplateExpression(), bindings);
					}
				}
				result.put(rd, bindings);
			}
		}
		return result;
	}

	/**
	 * Recursively collects variables bound by a template expression.
	 *
	 * <p>Binding sites in templates:
	 * <ul>
	 *   <li>{@link ObjectTemplateExp#getBindsTo()} — root variable</li>
	 *   <li>{@link PropertyTemplateItem} with value being a simple {@link VariableExp}
	 *       — potential free variable binding (§7.5 Rule 1.1)</li>
	 *   <li>{@link CollectionTemplateExp#getBindsTo()} — collection variable</li>
	 *   <li>{@link CollectionTemplateExp#getRest()} — rest variable</li>
	 *   <li>Collection member {@link VariableExp} — member variable binding</li>
	 * </ul>
	 */
	private void collectTemplateBindings(TemplateExp template, Set<String> bindings) {
		if (template instanceof ObjectTemplateExp ote) {
			if (ote.getBindsTo() != null) {
				bindings.add(ote.getBindsTo().getName());
			}
			for (PropertyTemplateItem item : ote.getPart()) {
				OclExpression value = item.getValue();
				if (value instanceof VariableExp ve && ve.getReferredVariable() != null) {
					// §7.5 Rule 1.1: obj.prop = var — potential binding for free variable
					bindings.add(ve.getReferredVariable().getName());
				} else if (value instanceof TemplateExp nested) {
					// Nested template — recurse
					collectTemplateBindings(nested, bindings);
				}
				// Complex expressions are not binding sites
			}
		} else if (template instanceof CollectionTemplateExp cte) {
			if (cte.getBindsTo() != null) {
				bindings.add(cte.getBindsTo().getName());
			}
			if (cte.getRest() != null) {
				bindings.add(cte.getRest().getName());
			}
			// Collection members
			for (OclExpression member : cte.getMember()) {
				if (member instanceof VariableExp ve && ve.getReferredVariable() != null) {
					bindings.add(ve.getReferredVariable().getName());
				} else if (member instanceof TemplateExp nested) {
					collectTemplateBindings(nested, bindings);
				}
			}
		}
	}

	// ==================== Usage Collection ====================

	/**
	 * Collects all variable references (usages) in a domain's template expressions
	 * that are NOT potential binding sites.
	 */
	private Set<String> collectDomainUsages(RelationDomain domain) {
		Set<String> usages = new HashSet<>();
		for (DomainPattern dp : domain.getPattern()) {
			if (dp.getTemplateExpression() != null) {
				collectTemplateUsages(dp.getTemplateExpression(), usages);
			}
		}
		// Default value assignments
		domain.getDefaultAssignment().forEach(
				da -> collectFreeVarRefs(da.getValueExp(), new HashSet<>(), usages));
		return usages;
	}

	/**
	 * Recursively collects variable usages from template expressions.
	 * Only collects references from complex expressions (not simple VariableExp
	 * values which are potential binding sites).
	 */
	private void collectTemplateUsages(TemplateExp template, Set<String> usages) {
		if (template instanceof ObjectTemplateExp ote) {
			for (PropertyTemplateItem item : ote.getPart()) {
				OclExpression value = item.getValue();
				if (value instanceof VariableExp) {
					// Simple variable — this is a potential binding site, not a usage
				} else if (value instanceof TemplateExp nested) {
					collectTemplateUsages(nested, usages);
				} else if (value != null) {
					// Complex expression — all variable refs are usages
					collectFreeVarRefs(value, new HashSet<>(), usages);
				}
			}
			// Template where-guard
			if (ote.getWhere() != null) {
				collectFreeVarRefs(ote.getWhere(), new HashSet<>(), usages);
			}
		} else if (template instanceof CollectionTemplateExp cte) {
			for (OclExpression member : cte.getMember()) {
				if (member instanceof VariableExp) {
					// Simple variable member — potential binding
				} else if (member instanceof TemplateExp nested) {
					collectTemplateUsages(nested, usages);
				} else if (member != null) {
					collectFreeVarRefs(member, new HashSet<>(), usages);
				}
			}
			if (cte.getWhere() != null) {
				collectFreeVarRefs(cte.getWhere(), new HashSet<>(), usages);
			}
		}
	}

	// ==================== Validation Checks ====================

	/**
	 * Checks a when/where pattern for unresolved variable references.
	 */
	private void checkPattern(Pattern pattern, Set<String> available, String clauseName,
			String relName, List<Resource.Diagnostic> diagnostics) {
		for (Predicate pred : pattern.getPredicate()) {
			OclExpression expr = pred.getConditionExpression();
			if (expr instanceof RelationCallExp relCall) {
				// In when-clause: arguments are bindings (handled elsewhere)
				// In where-clause: arguments must be bound — check them
				if ("where-clause".equals(clauseName)) {
					for (OclExpression arg : relCall.getArgument()) {
						checkExpressionRefs(arg, available, clauseName, relName, diagnostics);
					}
				}
				// Non-argument parts of the RelationCallExp are usages
			} else if (expr != null) {
				checkExpressionRefs(expr, available, clauseName, relName, diagnostics);
			}
		}
	}

	/**
	 * Checks a domain's template expressions for unresolved variable references.
	 */
	private void checkDomain(RelationDomain domain, Set<String> available,
			String relName, List<Resource.Diagnostic> diagnostics) {
		for (DomainPattern dp : domain.getPattern()) {
			if (dp.getTemplateExpression() != null) {
				checkTemplateRefs(dp.getTemplateExpression(), available,
						domain.getName(), relName, diagnostics);
			}
		}
	}

	/**
	 * Recursively checks template expressions for unresolved variable references.
	 */
	private void checkTemplateRefs(TemplateExp template, Set<String> available,
			String domainName, String relName, List<Resource.Diagnostic> diagnostics) {
		if (template instanceof ObjectTemplateExp ote) {
			for (PropertyTemplateItem item : ote.getPart()) {
				OclExpression value = item.getValue();
				if (value instanceof VariableExp) {
					// Potential binding — skip
				} else if (value instanceof TemplateExp nested) {
					checkTemplateRefs(nested, available, domainName, relName, diagnostics);
				} else if (value != null) {
					checkExpressionRefs(value, available,
							"domain '" + domainName + "'", relName, diagnostics);
				}
			}
			if (ote.getWhere() != null) {
				checkExpressionRefs(ote.getWhere(), available,
						"domain '" + domainName + "' where-guard", relName, diagnostics);
			}
		} else if (template instanceof CollectionTemplateExp cte) {
			for (OclExpression member : cte.getMember()) {
				if (member instanceof VariableExp) {
					// Potential binding — skip
				} else if (member instanceof TemplateExp nested) {
					checkTemplateRefs(nested, available, domainName, relName, diagnostics);
				} else if (member != null) {
					checkExpressionRefs(member, available,
							"domain '" + domainName + "'", relName, diagnostics);
				}
			}
		}
	}

	/**
	 * Checks an OCL expression for unresolved variable references.
	 */
	private void checkExpressionRefs(OclExpression expr, Set<String> available,
			String context, String relName, List<Resource.Diagnostic> diagnostics) {
		Set<String> freeRefs = new HashSet<>();
		collectFreeVarRefs(expr, new HashSet<>(), freeRefs);
		for (String ref : freeRefs) {
			if (IMPLICIT_VARS.contains(ref)) {
				continue;
			}
			if (!available.contains(ref)) {
				diagnostics.add(new QvtdParseDiagnostic(
						"§7.5: Variable '" + ref + "' in " + context
								+ " of relation '" + relName + "' is not bound",
						0, 0));
			}
		}
	}

	// ==================== Expression Tree Walker ====================

	/**
	 * Recursively collects free variable references from an OCL expression,
	 * respecting local scopes introduced by {@link LetExp}, {@link IteratorExp},
	 * and {@link IterateExp}.
	 *
	 * @param expr the expression to analyze
	 * @param localVars variables in scope from enclosing let/iterator/iterate
	 * @param freeRefs accumulator for free variable names
	 */
	private void collectFreeVarRefs(OclExpression expr, Set<String> localVars,
			Set<String> freeRefs) {
		if (expr == null) {
			return;
		}

		if (expr instanceof VariableExp ve) {
			if (ve.getReferredVariable() != null) {
				String name = ve.getReferredVariable().getName();
				if (!localVars.contains(name)) {
					freeRefs.add(name);
				}
			}
			return;
		}

		if (expr instanceof LetExp let) {
			// Init expression is evaluated before the variable is in scope
			if (let.getOwnedVariable() != null && let.getOwnedVariable().getOwnedInit() != null) {
				collectFreeVarRefs(let.getOwnedVariable().getOwnedInit(), localVars, freeRefs);
			}
			// Body has the let variable in scope
			if (let.getOwnedIn() != null && let.getOwnedVariable() != null) {
				Set<String> innerLocals = new HashSet<>(localVars);
				innerLocals.add(let.getOwnedVariable().getName());
				collectFreeVarRefs(let.getOwnedIn(), innerLocals, freeRefs);
			}
			return;
		}

		if (expr instanceof IteratorExp iter) {
			// Source is evaluated without iterator variables
			collectFreeVarRefs(iter.getOwnedSource(), localVars, freeRefs);
			// Body has iterator variables in scope
			Set<String> innerLocals = new HashSet<>(localVars);
			for (Variable v : iter.getOwnedIterators()) {
				innerLocals.add(v.getName());
			}
			collectFreeVarRefs(iter.getOwnedBody(), innerLocals, freeRefs);
			return;
		}

		if (expr instanceof IterateExp iterate) {
			// Source is evaluated without iterator/accumulator variables
			collectFreeVarRefs(iterate.getOwnedSource(), localVars, freeRefs);
			// Accumulator init is evaluated without iterator variable
			if (iterate.getOwnedResult() != null
					&& iterate.getOwnedResult().getOwnedInit() != null) {
				collectFreeVarRefs(iterate.getOwnedResult().getOwnedInit(), localVars, freeRefs);
			}
			// Body has iterator + accumulator in scope
			Set<String> innerLocals = new HashSet<>(localVars);
			for (Variable v : iterate.getOwnedIterators()) {
				innerLocals.add(v.getName());
			}
			if (iterate.getOwnedResult() != null) {
				innerLocals.add(iterate.getOwnedResult().getName());
			}
			collectFreeVarRefs(iterate.getOwnedBody(), innerLocals, freeRefs);
			return;
		}

		if (expr instanceof RelationCallExp relCall) {
			// Arguments are variable references
			for (OclExpression arg : relCall.getArgument()) {
				collectFreeVarRefs(arg, localVars, freeRefs);
			}
			return;
		}

		// Default: walk all contained EObjects looking for OclExpression children
		for (EObject child : expr.eContents()) {
			if (child instanceof OclExpression childExpr) {
				collectFreeVarRefs(childExpr, localVars, freeRefs);
			}
		}
	}
}
