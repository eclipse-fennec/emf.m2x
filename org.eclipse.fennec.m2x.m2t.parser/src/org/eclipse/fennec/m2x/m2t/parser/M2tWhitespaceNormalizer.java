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

import java.util.Collections;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;

import org.eclipse.emf.common.util.EList;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.model.m2t.Block;
import org.eclipse.fennec.m2x.model.m2t.ForBlock;
import org.eclipse.fennec.m2x.model.m2t.IfBlock;
import org.eclipse.fennec.m2x.model.m2t.LetBlock;
import org.eclipse.fennec.m2x.model.m2t.Macro;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.model.m2t.ModuleElement;
import org.eclipse.fennec.m2x.model.m2t.ProtectedAreaBlock;
import org.eclipse.fennec.m2x.model.m2t.Template;
import org.eclipse.fennec.m2x.model.m2t.TemplateExpression;
import org.eclipse.fennec.m2x.model.m2t.TemplateInvocation;
import org.eclipse.fennec.m2x.model.m2t.TextExpression;
import org.eclipse.fennec.m2x.model.ocl.OclFactory;
import org.eclipse.fennec.m2x.model.ocl.StringLiteralExp;

/**
 * MOFM2T §8.4 whitespace normalizer.
 *
 * <p>Applies the following transformations to a parsed Module AST:
 * <ol>
 *   <li><b>Body-trimming</b>: Strip leading newline after head and trailing
 *       newline+whitespace before tail for templates and multi-line blocks.</li>
 *   <li><b>Standalone-block detection</b>: Remove the leading whitespace of blocks that
 *       occupy a line by themselves. The newline ending the tail line stays — it is a
 *       whitespace body element of the enclosing body (#122).</li>
 *   <li><b>Default separator</b>: Set {@code "\n"} as default separator for standalone
 *       multi-line for-blocks and standalone template invocations without an explicit
 *       separator (#273, #276).</li>
 *   <li><b>BOL indicator</b>: Process {@code ^} markers in text expressions
 *       to strip leading whitespace on that line.</li>
 *   <li><b>Indent extraction</b>: Detect standalone template invocations and
 *       extract their indentation for eval-time indent-propagation onto every line of
 *       the invoked template's output (#277).</li>
 * </ol>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public class M2tWhitespaceNormalizer {

	private final Map<TemplateInvocation, String> indentationMap = new IdentityHashMap<>();

	/**
	 * Single-line stand-alone blocks: the newline ending their line goes only when the block
	 * produces no text, which the evaluator decides (#374).
	 */
	private final Set<Block> conditionalNewlines = Collections.newSetFromMap(new IdentityHashMap<>());

	/**
	 * Which blocks are multi-line, i.e. have their head and their tail on different lines.
	 * §8.4 gives the two shapes different body rules, and that difference decides what
	 * happens to the newline ending the tail line — see {@link #processStandaloneBlock}.
	 * Filled while trimming, read in the pass after it.
	 */
	private final Map<Block, Boolean> multiLineBlocks = new IdentityHashMap<>();
	private final WhitespaceMode mode;

	/**
	 * Creates a normalizer with the given whitespace mode.
	 *
	 * @param mode the whitespace mode ({@link WhitespaceMode#SPEC} or {@link WhitespaceMode#ACCELEO})
	 */
	public M2tWhitespaceNormalizer(WhitespaceMode mode) {
		this.mode = Objects.requireNonNull(mode, "mode must not be null");
	}

	/**
	 * Normalizes the given module AST in-place.
	 *
	 * @param module the module to normalize
	 * @return the indentation map for standalone template invocations
	 */
	public Map<TemplateInvocation, String> normalize(Module module) {
		Objects.requireNonNull(module, "module must not be null");
		if (mode == WhitespaceMode.NONE) {
			return Map.of();
		}
		for (ModuleElement element : module.getOwnedModuleElement()) {
			// §8.4 applies to "the body of a template (or a block expression)"; a macro is a
			// block too (§8.1.19, #371)
			if (element instanceof Template template) {
				processBody(template.getBody(), trimBody(template));
			} else if (element instanceof Macro macro) {
				processBody(macro.getBody(), trimBody(macro));
			}
		}
		return Map.copyOf(indentationMap);
	}

	// --- Body Trimming (§8.4 Rule 1+2) ---

	/**
	 * Trims the body of a block: removes leading newline after head
	 * and trailing newline+whitespace before tail.
	 *
	 * @return {@code true} if a head-line newline was trimmed, which is what makes the
	 *         block multi-line in the sense of §8.4
	 */
	private boolean trimBody(Block block) {
		EList<TemplateExpression> body = block.getBody();
		if (body.isEmpty()) {
			return false;
		}
		boolean multiLine = false;

		// Trim leading: first body element — remove text up to and including first newline
		if (body.get(0) instanceof TextExpression first) {
			String value = first.getValue();
			int nlIdx = value.indexOf('\n');
			if (nlIdx >= 0) {
				// Only trim if text before newline is whitespace-only (part of head line)
				String beforeNl = value.substring(0, nlIdx);
				if (beforeNl.isBlank()) {
					multiLine = true;
					String trimmed = value.substring(nlIdx + 1);
					if (trimmed.isEmpty()) {
						body.remove(0);
					} else {
						first.setValue(trimmed);
					}
				}
			}
		}

		if (body.isEmpty()) {
			return multiLine;
		}

		// Trim trailing: last body element
		int lastIdx = body.size() - 1;
		if (body.get(lastIdx) instanceof TextExpression last) {
			String value = last.getValue();
			int lastNlIdx = value.lastIndexOf('\n');
			if (lastNlIdx >= 0) {
				// Only trim if text after last newline is whitespace-only (part of tail line)
				String afterNl = value.substring(lastNlIdx + 1);
				if (afterNl.isBlank()) {
					String trimmed = value.substring(0, lastNlIdx);
					if (trimmed.isEmpty()) {
						body.remove(lastIdx);
					} else {
						last.setValue(trimmed);
					}
				}
			}
		}
		return multiLine;
	}

	// --- Recursive body processing ---

	/**
	 * Normalizes one body.
	 *
	 * @param body the body elements, already trimmed by {@link #trimBody(Block)}
	 * @param multiLine whether the owning block is multi-line — then the first element
	 *        starts on its own line and the last element ends before the tail line; in a
	 *        single-line body both share the line with the head and the tail, which makes a
	 *        block there an {@code <embedded-block-expression>} (§8.4, #272)
	 */
	private void processBody(EList<TemplateExpression> body, boolean multiLine) {
		// First pass: process nested blocks (trim their bodies + recurse)
		for (TemplateExpression expr : body) {
			if (expr instanceof Block nested) {
				boolean nestedMultiLine = trimBody(nested);
				multiLineBlocks.put(nested, nestedMultiLine);
				processBody(nested.getBody(), nestedMultiLine);
				processAlternatives(nested);
			}
		}

		// Second pass: standalone block detection + standalone invocation detection
		// Iterate by index since we modify TextExpression values
		for (int i = 0; i < body.size(); i++) {
			TemplateExpression expr = body.get(i);
			if (expr instanceof Block block) {
				processStandaloneBlock(body, i, block, multiLine);
			} else if (expr instanceof TemplateInvocation invocation && !invocation.isSuper()) {
				processStandaloneInvocation(body, i, invocation, multiLine);
			}
		}

		// Third pass: BOL indicator processing (SPEC mode only — Acceleo 3.7 does not support ^)
		if (mode == WhitespaceMode.SPEC) {
			for (TemplateExpression expr : body) {
				if (expr instanceof TextExpression text) {
					processBolIndicator(text);
				}
			}
		}
	}

	/**
	 * Normalizes the branches a conditional block carries beside its primary body: the
	 * {@code elseif} chain and the {@code else} of an {@link IfBlock}, the {@code elselet}
	 * chain and the {@code else} of a {@link LetBlock}.
	 *
	 * <p>§8.4 defines the body of a multi-line block as starting on the line after the
	 * head and ending before the newline in front of the tail. An {@code [elseif]} head is
	 * a block head like any other, so its own line's newline is not part of the body it
	 * introduces — without this, a taken {@code elseif} branch emits a blank line that the
	 * same body would not emit as the {@code if} branch (#123). Recursing here is also
	 * what gives a standalone block *inside* a branch its standalone handling.
	 */
	private void processAlternatives(Block block) {
		if (block instanceof IfBlock ifBlock) {
			for (IfBlock elseIf : ifBlock.getElseIf()) {
				processBody(elseIf.getBody(), trimBody(elseIf));
				processAlternatives(elseIf);
			}
			processElse(ifBlock.getElse());
		} else if (block instanceof LetBlock letBlock) {
			for (LetBlock elseLet : letBlock.getElseLet()) {
				processBody(elseLet.getBody(), trimBody(elseLet));
				processAlternatives(elseLet);
			}
			processElse(letBlock.getElse());
		}
	}

	private void processElse(Block elseBlock) {
		if (elseBlock == null) {
			return;
		}
		processBody(elseBlock.getBody(), trimBody(elseBlock));
		processAlternatives(elseBlock);
	}

	// --- Standalone Block Detection (§8.4 Rule 3) ---

	private void processStandaloneBlock(EList<TemplateExpression> body, int index, Block block,
			boolean multiLine) {
		// Check if block is standalone: only whitespace before on its line
		// and only whitespace/newline after on its line
		String leadingWs = getLeadingWhitespace(body, index, multiLine);
		if (leadingWs == null) {
			return; // not standalone
		}
		if (!hasTrailingNewline(body, index, multiLine)) {
			return; // not standalone
		}

		// Strip the whitespace in front of the head — §8.4's one rule for a standalone
		// block. A protected area keeps it: its markers are written in its place, and they are
		// indented like the tag, the end marker as well (#367).
		if (!(block instanceof ProtectedAreaBlock)) {
			stripLeadingWhitespace(body, index);
		}

		// What happens to the newline that ends the tail line follows the two body rules
		// §8.4 gives the two block shapes (#122). A multi-line block has already spent a
		// newline there: its body "ends on the last character (excluding the new line) of
		// the line previous to the block tail", so the newline in front of the tail is
		// gone. Removing the one after the tail as well would delete a second newline,
		// and the line after the block would be glued to the block's last line. A
		// single-line block consumed no newline — its body "starts after the closing
		// bracket of the block head" — so its line ends where the tag line ends and the
		// newline goes with it.
		//
		// That newline is a whitespace element of the enclosing body, which §8.4 outputs "as
		// is" — so it stays when the block produced text: `[if (true)]X[/if]` on its own line
		// is `X` and a line break, not `X` glued to the next line. The line of a block that
		// produced nothing (`[if (false)]`) goes, as Acceleo 3.7's testingIf drops it. Which
		// of the two is known only when the block has run, so the evaluator decides (#374).
		if (!Boolean.TRUE.equals(multiLineBlocks.get(block))) {
			conditionalNewlines.add(block);
		}

		// Inject the default "\n" separator for a standalone for-block without an explicit
		// separator — but only for the multi-line shape: §8.4 says "in the case of a
		// multi-line 'for block', when a separator character is not specified, use 'new
		// line' as the default separator". A single-line [for …]…[/for] alone on its line
		// concatenates its iterations, as Acceleo 3.7's testForI reference ("123") shows
		// (#273). Both SPEC and ACCELEO modes do this.
		if (block instanceof ForBlock forBlock && forBlock.getEach() == null
				&& Boolean.TRUE.equals(multiLineBlocks.get(block))) {
			StringLiteralExp newlineSep = OclFactory.eINSTANCE.createStringLiteralExp();
			newlineSep.setStringSymbol("\n");
			forBlock.setEach(newlineSep);
		}
	}

	/**
	 * Returns the leading whitespace before the element at {@code index}
	 * if the element is standalone on its line, or {@code null} if not standalone.
	 *
	 * @param multiLine whether the owning block is multi-line, see
	 *        {@link #processBody(EList, boolean)}
	 */
	private String getLeadingWhitespace(EList<TemplateExpression> body, int index, boolean multiLine) {
		if (index == 0) {
			// First element: in a multi-line body it starts its own line; in a single-line
			// body it follows the head on the head's line, which makes it embedded (§8.4).
			return multiLine ? "" : null;
		}
		if (!(body.get(index - 1) instanceof TextExpression prev)) {
			return null; // previous element is not text → not standalone
		}
		String value = prev.getValue();
		int lastNlIdx = value.lastIndexOf('\n');
		String afterNl = (lastNlIdx >= 0) ? value.substring(lastNlIdx + 1) : value;
		return afterNl.isBlank() ? afterNl : null;
	}

	private boolean hasTrailingNewline(EList<TemplateExpression> body, int index, boolean multiLine) {
		if (index == body.size() - 1) {
			// Last element: in a multi-line body its line ends before the tail line; in a
			// single-line body the tail follows on the same line (§8.4).
			return multiLine;
		}
		if (!(body.get(index + 1) instanceof TextExpression next)) {
			return false;
		}
		String value = next.getValue();
		int nlIdx = value.indexOf('\n');
		if (nlIdx < 0) {
			// No newline — standalone only if the rest of the line is whitespace, it is the
			// last text, and the tail does not share that line
			return multiLine && value.isBlank() && index + 1 == body.size() - 1;
		}
		String beforeNl = value.substring(0, nlIdx);
		return beforeNl.isBlank();
	}

	private void stripLeadingWhitespace(EList<TemplateExpression> body, int index) {
		if (index == 0) {
			return;
		}
		if (body.get(index - 1) instanceof TextExpression prev) {
			String value = prev.getValue();
			int lastNlIdx = value.lastIndexOf('\n');
			if (lastNlIdx >= 0) {
				prev.setValue(value.substring(0, lastNlIdx + 1));
			} else {
				// Entire text was whitespace
				if (value.isBlank()) {
					prev.setValue("");
				}
			}
		}
	}

	/**
	 * The single-line stand-alone blocks of the modules normalized so far: the newline after
	 * such a block is written only when the block produces text (#374).
	 *
	 * @return the blocks, compared by identity
	 */
	public Set<Block> getConditionalNewlines() {
		return Collections.unmodifiableSet(conditionalNewlines);
	}

	// --- BOL Indicator (§8.4 Rule 5) ---

	private void processBolIndicator(TextExpression text) {
		String value = text.getValue();
		if (!value.contains("^")) {
			return;
		}
		StringBuilder result = new StringBuilder();
		String[] lines = value.split("(\r\n|\r|\n)", -1);
		for (int i = 0; i < lines.length; i++) {
			if (i > 0) {
				result.append('\n');
			}
			String line = lines[i];
			int caretIdx = line.indexOf('^');
			if (caretIdx >= 0 && line.substring(0, caretIdx).isBlank()) {
				// BOL indicator: strip leading whitespace and the caret itself
				result.append(line.substring(caretIdx + 1));
			} else {
				result.append(line);
			}
		}
		text.setValue(result.toString());
	}

	// --- Standalone Template Invocation + Indent Extraction (§8.4 Rule 4) ---

	private void processStandaloneInvocation(EList<TemplateExpression> body, int index,
			TemplateInvocation invocation, boolean multiLine) {
		String indent = getLeadingWhitespace(body, index, multiLine);
		if (indent == null) {
			return; // not standalone
		}
		if (!hasTrailingNewline(body, index, multiLine)) {
			return; // not standalone
		}

		// §8.4, <stand-alone-template-invocation>: "In the case of an iterative template
		// invocation (e.g., when the argument is a collection), if a separator character is
		// not specified, use 'new line' as the default separator between the outputs
		// produced by successive iterations" — the twin of the multi-line for block's rule.
		// The evaluator reads it only when an argument is a collection (#276).
		if (invocation.getEach() == null) {
			StringLiteralExp newlineSep = OclFactory.eINSTANCE.createStringLiteralExp();
			newlineSep.setStringSymbol("\n");
			invocation.setEach(newlineSep);
		}

		if (indent.isEmpty()) {
			return; // nothing to propagate
		}
		// Extract indentation and strip leading whitespace only — the evaluator puts the
		// indentation in front of every line of the output, the first included (#277).
		// Trailing newline is kept — it separates invocation output from following text.
		indentationMap.put(invocation, indent);
		stripLeadingWhitespace(body, index);
	}
}
