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
package org.eclipse.fennec.m2x.qvto.tests.syntax;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.fennec.m2x.model.ocl.Variable;
import org.eclipse.fennec.m2x.model.ocl.VariableExp;
import org.eclipse.fennec.m2x.model.qvtoperational.ObjectExp;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * A reference to a local variable is a reference to its declaration, not to a free variable of
 * the same name that only the evaluator's name lookup connects to it (#384).
 */
class QvtoLocalVariableLinkTest extends AbstractQvtoEngineTest {

	/** Asserts every reference to {@code name} refers to its one contained declaration. */
	private static void assertLinked(String body, String name) throws Exception {
		OperationalTransformation transformation = parse("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				%s
				}
				""".formatted(body));
		List<Variable> declarations = new ArrayList<>();
		List<Variable> references = new ArrayList<>();
		for (var it = transformation.eAllContents(); it.hasNext();) {
			EObject o = it.next();
			if (o instanceof Variable v && name.equals(v.getName())) {
				declarations.add(v);
			} else if (o instanceof VariableExp ve && ve.getReferredVariable() != null
					&& name.equals(ve.getReferredVariable().getName())) {
				references.add(ve.getReferredVariable());
			} else if (o instanceof ObjectExp oe && name.equals(oe.getReferredObject().getName())) {
				references.add(oe.getReferredObject());
			}
		}
		assertEquals(1, declarations.size(), "declarations of " + name);
		assertFalse(references.isEmpty(), "no reference to " + name);
		for (Variable reference : references) {
			assertSame(declarations.get(0), reference, "a reference to '" + name + "' is a free variable");
		}
	}

	@Test
	void theComputeShorthandVariable_inTheLoopBody() throws Exception {
		// §8.2.2.6: forEach (i; acc : T = init) ≡ compute (acc : T = init) forEach (i)
		assertLinked("""
				main() {
				    Sequence{1, 2}->forEach(i; acc : Integer = 0) { acc := acc + i; };
				}
				""", "acc");
	}

	@Test
	void theSwitchVariable_inTheCases() throws Exception {
		assertLinked("""
				main() {
				    var label := switch (x := 5) { case (x > 10) 'big'; case (x > 0) 'small'; else 'zero'; };
				}
				""", "x");
	}

	@Test
	void theArrowSwitchIterator_inTheCases() throws Exception {
		assertLinked("""
				main() {
				    var labels := Sequence{1, 20}->switch(x) { case (x > 10) 'big'; else 'small'; };
				}
				""", "x");
	}

	@Test
	void theResolveTarget_inTheCondition() throws Exception {
		assertLinked("""
				mapping SourceElement::toTarget() : TargetElement { name := self.name; }
				main() {
				    s.objectsOfType(SourceElement)->map toTarget();
				    var first := s.objectsOfType(SourceElement)->any(true);
				    var r := first.resolve(te : TargetElement | te.name = 'a');
				}
				""", "te");
	}

	@Test
	void anObjectExpression_onAnExistingVariable_refersToItsDeclaration() throws Exception {
		// §8.2.2.24: "if the variable already exists … the object it references is updated"
		assertLinked("""
				main() {
				    var e : TargetElement := object TargetElement { name := 'a'; };
				    object e : TargetElement { name := 'b'; };
				    log(e.name);
				}
				""", "e");
	}

	@Test
	void anObjectExpression_onAnExistingVariable_updatesItsObject() throws Exception {
		var result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				main() {
				    var e : TargetElement := object TargetElement { name := 'a'; };
				    var same := object e : TargetElement { name := 'b'; };
				    log('same=' + (same = e).repr() + ' name=' + e.name + ' count=' + t.objects()->size().repr());
				}
				}
				""", emptyExtent(), emptyExtent());
		assertNotNull(result);
		assertEquals(List.of("log: same=true name=b count=1"), result.diagnostics().stream()
				.map(d -> d.getMessage()).filter(m -> m.startsWith("log:")).toList(), result.diagnostics().toString());
	}
}
