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
package org.eclipse.fennec.m2x.m2t.engine.internal;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.function.Predicate;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.VisibilityKind;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.ocl.parser.typing.OclTypeRules;

/**
 * The template a generation starts from, or why there is none (MOFM2T §7, #369).
 *
 * <p>"A transformation (template) can be started by directly invoking a public template with the
 * correct parameters": the template is public, of the module or a module it extends, and the
 * input elements fit its parameters in number and type. Without a name the template marked
 * {@code @main} is taken, else the one named {@code main} — the default of
 * {@code execute(module, context)}.
 *
 * @param template the template to start from, or {@code null}
 * @param problem why there is none, or {@code null}
 * @author Data In Motion Consulting
 * @since 1.0
 */
record M2tEntryPoint(Template template, String problem) {

	/**
	 * @param module the module to run
	 * @param templateName the template to start from, or {@code null} for the main one
	 * @param inputs the input elements, one per parameter
	 * @return the entry point, never {@code null}
	 */
	static M2tEntryPoint find(Module module, String templateName, List<EObject> inputs) {
		List<Template> candidates;
		String wanted;
		if (templateName != null) {
			candidates = templates(module, t -> templateName.equals(t.getName()));
			wanted = "public template '" + templateName + "'";
		} else {
			candidates = templates(module, Template::isMain);
			if (candidates.isEmpty()) {
				candidates = templates(module, t -> "main".equals(t.getName()));
			}
			wanted = "[template public main(...)] entry point";
		}
		List<Template> visible = candidates.stream().filter(t -> t.getVisibility() == VisibilityKind.PUBLIC).toList();
		if (visible.isEmpty()) {
			return new M2tEntryPoint(null, "Module '" + module.getName() + "' has no " + wanted
					+ (candidates.isEmpty() ? "" : ": '" + candidates.get(0).getName() + "' is not public") + " (MOFM2T §7)");
		}
		for (Template candidate : visible) {
			if (mismatch(candidate, inputs) == null) {
				return new M2tEntryPoint(candidate, null);
			}
		}
		return new M2tEntryPoint(null, mismatch(visible.get(0), inputs) + " (MOFM2T §7)");
	}

	/** The templates of the module and the modules it extends, transitively, that match. */
	private static List<Template> templates(Module module, Predicate<Template> filter) {
		List<Template> found = new ArrayList<>();
		Set<Module> visited = new LinkedHashSet<>();
		List<Module> queue = new ArrayList<>(List.of(module));
		while (!queue.isEmpty()) {
			Module current = queue.remove(0);
			if (!visited.add(current)) {
				continue;
			}
			for (ModuleElement element : current.getOwnedModuleElement()) {
				if (element instanceof Template template && filter.test(template)) {
					found.add(template);
				}
			}
			queue.addAll(current.getExtends());
		}
		return found;
	}

	/** Why the inputs do not fit the template's parameters, or {@code null} when they do. */
	private static String mismatch(Template template, List<EObject> inputs) {
		List<Variable> parameters = template.getParameter();
		if (parameters.size() != inputs.size()) {
			return "Template '" + template.getName() + "' takes " + parameters.size() + " input(s), the generation passes "
					+ inputs.size();
		}
		for (int i = 0; i < inputs.size(); i++) {
			EObject input = inputs.get(i);
			if (input != null && !OclTypeRules.conformsTo(input.eClass(), parameters.get(i).getType())) {
				return "Input " + (i + 1) + " of template '" + template.getName() + "' is a " + input.eClass().getName()
						+ ", which is no " + OclTypeRules.nameOf(parameters.get(i).getType());
			}
		}
		return null;
	}
}
