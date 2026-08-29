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
package org.eclipse.fennec.m2x.m2t.generator;

import java.io.File;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Formatter;
import java.util.List;
import java.util.Locale;
import java.util.Optional;
import java.util.Properties;
import java.util.Set;
import java.util.TreeSet;

import org.eclipse.emf.common.util.Diagnostic;
import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.m2t.engine.FileSystemGenerationStrategy;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngines;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
import org.eclipse.fennec.m2x.model.m2t.Template;

import aQute.bnd.service.externalplugin.ExternalPlugin;
import aQute.bnd.service.generate.BuildContext;
import aQute.bnd.service.generate.Generator;
import aQute.lib.fileset.FileSet;
import aQute.lib.strings.Strings;

/**
 * A bnd {@code Generator} that runs MOFM2T v1.0 templates as part of a build — whether
 * what comes out is code, documentation, or any other text.
 *
 * <p>Everything is configured with attributes of the {@code -generate} instruction; the
 * fileset in front of it is what bnd watches for changes, so metamodels, models and
 * templates all belong in there:
 *
 * <pre>
 * -generate: \
 *     model/&#42;.ecore,model/&#42;.xmi,templates/&#42;.mtl; \
 *         output         = gen-resources/; \
 *         generate       = fennecM2T; \
 *         ecore          = "model/addressbook.ecore,model/contact.ecore"; \
 *         model          = "model/addressbook.xmi"; \
 *         template       = "templates/vcard.mtl"
 * </pre>
 *
 * <table>
 * <caption>Attributes</caption>
 * <tr><th>Attribute</th><th>Meaning</th></tr>
 * <tr><td>{@code template}</td><td>the module holding the {@code main} template; required</td></tr>
 * <tr><td>{@code modules}</td><td>further modules to link, for {@code extends} and {@code import}</td></tr>
 * <tr><td>{@code ecore}</td><td>the metamodels, registered under their nsURI</td></tr>
 * <tr><td>{@code model}</td><td>the instance models handed to {@code main}</td></tr>
 * <tr><td>{@code output}</td><td>where generated files go; defaults to {@value #DEFAULT_OUTPUT}</td></tr>
 * <tr><td>{@code charset}</td><td>encoding of templates and generated files; defaults to UTF-8</td></tr>
 * <tr><td>{@code whitespace}</td><td>{@code NONE}, {@code SPEC} or {@code ACCELEO}; defaults to {@code ACCELEO}</td></tr>
 * <tr><td>{@code protectedAreas}</td><td>whether {@code [protected]} blocks are merged; defaults to {@code true}</td></tr>
 * </table>
 *
 * <p>Every file attribute takes a comma separated list, and each entry is either a path
 * or a bnd fileset such as {@code templates/&#42;&#42;/&#42;.mtl}. Paths are ordered as
 * written, matches of a fileset alphabetically, because the models are bound to the
 * parameters of {@code main} in that order.
 *
 * <p><b>How the models reach the template.</b> If {@code main} declares a single
 * parameter, it is executed once per model root — the usual case, one generation per
 * input model. Otherwise the roots are bound to its parameters positionally, which is
 * how a template that needs a second input model gets it.
 *
 * <p><b>Protected areas.</b> bnd empties the output directory before each generation
 * unless the instruction says {@code clear=false}. A generation whose templates use
 * {@code [protected]} blocks has to set it, or the content that was to be preserved is
 * deleted before it can be read back.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@ExternalPlugin(name = "fennecM2T", objectClass = Generator.class)
public class M2tGenerator implements Generator<M2tGeneratorOptions> {

	/** Output directory when neither the attribute nor the option says otherwise. */
	public static final String DEFAULT_OUTPUT = "src-gen";

	static final String ATTR_TEMPLATE = "template";
	static final String ATTR_MODULES = "modules";
	static final String ATTR_ECORE = "ecore";
	static final String ATTR_MODEL = "model";
	static final String ATTR_OUTPUT = "output";
	static final String ATTR_CHARSET = "charset";
	static final String ATTR_WHITESPACE = "whitespace";
	static final String ATTR_PROTECTED_AREAS = "protectedAreas";

	@Override
	public Optional<String> generate(BuildContext context, M2tGeneratorOptions options) throws Exception {
		try (Formatter errors = new Formatter()) {

			File output = outputDirectory(context, options, errors);
			List<File> ecores = files(context, ATTR_ECORE, errors);
			List<File> models = files(context, ATTR_MODEL, errors);
			List<File> templates = files(context, ATTR_TEMPLATE, errors);
			List<File> modules = files(context, ATTR_MODULES, errors);
			Charset charset = charset(context, errors);
			WhitespaceMode whitespace = whitespaceMode(context, errors);
			boolean protectedAreas = booleanAttribute(context, ATTR_PROTECTED_AREAS, true, errors);

			if (attribute(context, ATTR_TEMPLATE) == null) {
				errors.format("no 'template' attribute — nothing to generate from%n");
			} else if (templates.size() > 1) {
				errors.format(
						"'template' names %d modules; the main template lives in one of them, "
								+ "put the others in 'modules': %s%n",
						templates.size(), templates);
			}

			String configErrors = errors.toString();
			if (!configErrors.isEmpty()) {
				return Optional.of(configErrors.trim());
			}
			if (templates.isEmpty()) {
				return Optional.of("no template resolved from the 'template' attribute");
			}

			return run(context, output, ecores, models, templates.get(0), modules, charset, whitespace,
					protectedAreas);
		}
	}

	private Optional<String> run(BuildContext context, File output, List<File> ecores, List<File> models,
			File template, List<File> modules, Charset charset, WhitespaceMode whitespace,
			boolean protectedAreas) {

		try (Formatter errors = new Formatter()) {

			M2tModelLoader loader = new M2tModelLoader();
			for (File ecore : ecores) {
				for (EPackage ePackage : loader.loadMetamodel(ecore)) {
					context.trace("fennecM2T: registered %s from %s", ePackage.getNsURI(), ecore);
				}
			}

			List<EObject> roots = new ArrayList<>();
			for (File model : models) {
				roots.addAll(loader.loadModel(model));
			}
			loader.getWarnings().forEach(w -> context.warning("fennecM2T: %s", w));

			M2tConfiguration configuration = M2tConfiguration.builder()
					.resourceSet(loader.getResourceSet())
					.generationStrategy(new FileSystemGenerationStrategy(output.toPath()))
					.defaultCharset(charset)
					.whitespaceMode(whitespace)
					.protectedAreaEnabled(protectedAreas)
					.build();
			M2tEngine engine = M2tEngines.create(configuration);

			Module main = engine.parse(URI.createFileURI(template.getAbsolutePath()));
			List<Module> linked = new ArrayList<>();
			linked.add(main);
			for (File module : modules) {
				linked.add(engine.parse(URI.createFileURI(module.getAbsolutePath())));
			}
			if (linked.size() > 1) {
				engine.link(linked.toArray(Module[]::new))
						.forEach(w -> context.warning("fennecM2T: %s", w));
			}

			Template mainTemplate = mainTemplate(main);
			if (mainTemplate == null) {
				return Optional.of("module '" + main.getName() + "' in " + template
						+ " has no [template public main(...)] entry point");
			}

			int parameters = mainTemplate.getParameter().size();
			if (roots.isEmpty() && parameters > 0) {
				return Optional.of("template 'main' takes " + parameters + " parameter(s) but no 'model' "
						+ "was configured");
			}
			if (parameters > 1 && roots.size() != parameters) {
				context.warning("fennecM2T: template 'main' takes %d parameters but %d model root(s) "
						+ "were loaded", parameters, roots.size());
			}

			for (List<EObject> arguments : bind(roots, parameters)) {
				M2tResult result = engine.execute(main, new M2tContext(arguments, output.toPath()));
				report(context, errors, result);
			}

			String failures = errors.toString();
			return failures.isEmpty() ? Optional.empty() : Optional.of(failures.trim());
		} catch (Exception e) {
			return Optional.of("fennecM2T failed: " + causes(e));
		}
	}

	/**
	 * Splits the model roots into the argument lists of the executions to run: one per
	 * root for a single-parameter {@code main}, one execution with all of them otherwise.
	 */
	private static List<List<EObject>> bind(List<EObject> roots, int parameters) {
		if (parameters != 1) {
			return List.of(roots);
		}
		List<List<EObject>> arguments = new ArrayList<>(roots.size());
		for (EObject root : roots) {
			arguments.add(List.of(root));
		}
		return arguments;
	}

	private static void report(BuildContext context, Formatter errors, M2tResult result) {
		for (Diagnostic diagnostic : result.diagnostics()) {
			if (diagnostic.getSeverity() >= Diagnostic.ERROR) {
				errors.format("%s%n", diagnostic.getMessage());
			} else if (diagnostic.getSeverity() == Diagnostic.WARNING) {
				context.warning("fennecM2T: %s", diagnostic.getMessage());
			} else {
				context.trace("fennecM2T: %s", diagnostic.getMessage());
			}
		}
	}

	/**
	 * Renders a throwable and its causes as one line, so that the reason a generation
	 * failed survives into the single string bnd reports.
	 */
	private static String causes(Throwable t) {
		StringBuilder sb = new StringBuilder();
		for (Throwable cause = t; cause != null; cause = cause.getCause()) {
			if (sb.length() > 0) {
				sb.append(" -> ");
			}
			sb.append(cause.getClass().getSimpleName());
			if (cause.getMessage() != null) {
				sb.append(": ").append(cause.getMessage());
			}
			if (cause.getCause() == cause) {
				break;
			}
		}
		return sb.toString();
	}

	private static Template mainTemplate(Module module) {
		Template named = null;
		for (ModuleElement element : module.getOwnedModuleElement()) {
			if (element instanceof Template candidate) {
				if (candidate.isMain()) {
					return candidate;
				}
				if (named == null && "main".equals(candidate.getName())) {
					named = candidate;
				}
			}
		}
		return named;
	}

	private File outputDirectory(BuildContext context, M2tGeneratorOptions options, Formatter errors) {
		String attribute = attribute(context, ATTR_OUTPUT);
		File output = attribute != null ? context.getFile(attribute)
				: options.output().orElseGet(() -> context.getFile(DEFAULT_OUTPUT));
		// A directory that cannot be created is worth saying so — every file of the generation
		// would fail on it afterwards, one diagnostic each, none of them naming the cause
		if (!output.isDirectory() && !output.mkdirs()) {
			errors.format("output directory '%s' could not be created%n", output);
		}
		return output;
	}

	private List<File> files(BuildContext context, String name, Formatter errors) {
		String spec = attribute(context, name);
		if (spec == null) {
			return List.of();
		}
		List<File> files = new ArrayList<>();
		for (String entry : Strings.split(spec)) {
			if (entry.indexOf('*') >= 0 || entry.indexOf('?') >= 0) {
				Set<File> matches = new TreeSet<>(new FileSet(context.getBase(), entry).getFiles());
				if (matches.isEmpty()) {
					errors.format("'%s': fileset %s matches no file%n", name, entry);
				}
				files.addAll(matches);
			} else {
				File file = context.getFile(entry);
				if (file.isFile()) {
					files.add(file);
				} else {
					errors.format("'%s': no such file %s%n", name, file);
				}
			}
		}
		return files;
	}

	private Charset charset(BuildContext context, Formatter errors) {
		String value = attribute(context, ATTR_CHARSET);
		if (value == null) {
			return StandardCharsets.UTF_8;
		}
		try {
			return Charset.forName(value);
		} catch (RuntimeException e) {
			errors.format("'%s': %s is not a known charset%n", ATTR_CHARSET, value);
			return StandardCharsets.UTF_8;
		}
	}

	private WhitespaceMode whitespaceMode(BuildContext context, Formatter errors) {
		String value = attribute(context, ATTR_WHITESPACE);
		if (value == null) {
			return WhitespaceMode.ACCELEO;
		}
		try {
			return WhitespaceMode.valueOf(value.toUpperCase(Locale.ROOT));
		} catch (IllegalArgumentException e) {
			errors.format("'%s': %s is not one of %s%n", ATTR_WHITESPACE, value,
					List.of(WhitespaceMode.values()));
			return WhitespaceMode.ACCELEO;
		}
	}

	private boolean booleanAttribute(BuildContext context, String name, boolean deflt, Formatter errors) {
		String value = attribute(context, name);
		if (value == null) {
			return deflt;
		}
		if ("true".equalsIgnoreCase(value)) {
			return true;
		}
		if ("false".equalsIgnoreCase(value)) {
			return false;
		}
		errors.format("'%s': %s is neither true nor false%n", name, value);
		return deflt;
	}

	/**
	 * Reads an attribute of the {@code -generate} instruction.
	 *
	 * <p>bnd hands the attributes over as local properties of the {@link BuildContext},
	 * whose parent is the project. Reading them with {@code get} alone would therefore
	 * answer with a project or workspace property of the same name — {@code model} and
	 * {@code template} are names a build file may well use for something else. So the
	 * local properties decide whether the attribute is there at all, and only then is
	 * the value read, through bnd's macro processor.
	 */
	private String attribute(BuildContext context, String name) {
		Properties local = context.getProperties();
		if (!local.containsKey(name)) {
			return null;
		}
		String value = context.get(name);
		if (value == null) {
			return null;
		}
		value = value.trim();
		return value.isEmpty() ? null : value;
	}
}
