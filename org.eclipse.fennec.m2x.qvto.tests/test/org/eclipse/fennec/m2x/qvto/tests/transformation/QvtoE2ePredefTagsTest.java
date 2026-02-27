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
package org.eclipse.fennec.m2x.qvto.tests.transformation;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * End-to-end tests for predefined tags (§8.3.19).
 *
 * <p>QVT-O v1.3 §8.3.19 defines 5 predefined tags:
 * <ul>
 *   <li>{@code alias} — tested in {@link QvtoE2eTagTypedefTest}</li>
 *   <li>{@code proxy} — marks a module as placeholder</li>
 *   <li>{@code topclasses} — restricts root object types in an extent</li>
 *   <li>{@code rememberChanges} — intentionally ignored (re-execution scenario)</li>
 *   <li>{@code manuallyChanged} — intentionally ignored (tool annotation)</li>
 * </ul>
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoE2ePredefTagsTest extends AbstractQvtoEngineTest {

	// ==== proxy tag ====

	// §8.3.19: tag "proxy" parses without error, stored in ownedTag
	@Test
	void tag_proxyParsed() throws Exception {
		QvtoExecutionResult result = execute("""
				transformation test() {
				    tag "proxy" test = true;
				    main() {
				        log('proxy parsed');
				    }
				}
				""");
		assertSuccess(result);
		assertLogged(result, "proxy parsed");
	}

	// ==== topclasses tag ====

	// §8.3.19: topclasses allows matching root types
	@Test
	void tag_topclasses_allowed() throws Exception {
		QvtoModelExtent src = new BasicQvtoModelExtent();
		src.add(createSourceElement("s1", 1));
		QvtoModelExtent tgt = new BasicQvtoModelExtent();

		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    tag "topclasses" t = 'TargetElement';
				    main() {
				        s.rootObjects()[SourceElement]->forEach(e) {
				            object TargetElement {
				                name := e.name;
				            };
				        };
				        log('count:' + t.rootObjects()->size().toString());
				    }
				}
				""", src, tgt);
		assertSuccess(result);
		assertLogged(result, "count:1");
		assertEquals(1, tgt.getContents().size());
	}

	// §8.3.19: topclasses restricts non-matching root types
	@Test
	void tag_topclasses_restrictsRoot() throws Exception {
		QvtoModelExtent src = new BasicQvtoModelExtent();
		src.add(createSourceElement("s1", 1));
		QvtoModelExtent tgt = new BasicQvtoModelExtent();

		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    tag "topclasses" t = 'TargetContainer';
				    main() {
				        s.rootObjects()[SourceElement]->forEach(e) {
				            object TargetElement {
				                name := e.name;
				            };
				        };
				    }
				}
				""", src, tgt);
		assertSuccess(result);
		// TargetElement is not in topclasses list → not added as root
		assertEquals(0, tgt.getContents().size());
	}

	// §8.3.19: topclasses with multiple allowed types
	@Test
	void tag_topclasses_multipleTypes() throws Exception {
		QvtoModelExtent src = new BasicQvtoModelExtent();
		src.add(createSourceElement("s1", 1));
		QvtoModelExtent tgt = new BasicQvtoModelExtent();

		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				modeltype TGT uses 'http://test/target/1.0';
				transformation test(in s : SRC, out t : TGT) {
				    tag "topclasses" t = 'TargetContainer, TargetElement';
				    main() {
				        s.rootObjects()[SourceElement]->forEach(e) {
				            object TargetElement {
				                name := e.name;
				            };
				        };
				    }
				}
				""", src, tgt);
		assertSuccess(result);
		// TargetElement IS in the topclasses list → added as root
		assertEquals(1, tgt.getContents().size());
	}

	// ==== rememberChanges & manuallyChanged — intentionally ignored ====

	// §8.3.19: rememberChanges tag parses without error
	@Test
	void tag_rememberChanges_ignored() throws Exception {
		assertDoesNotThrow(() -> parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(in s : SRC) {
				    tag "rememberChanges" s = true;
				    main() {
				        log('ok');
				    }
				}
				"""));
	}

	// §8.3.19: manuallyChanged tag parses without error
	@Test
	void tag_manuallyChanged_ignored() throws Exception {
		assertDoesNotThrow(() -> parse("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(in s : SRC) {
				    tag "manuallyChanged" s = true;
				    main() {
				        log('ok');
				    }
				}
				"""));
	}

	// ---- Helpers ----

	private static void assertSuccess(QvtoExecutionResult result) {
		assertNotNull(result);
		assertTrue(result.isSuccess(), () -> "Diagnostics: " + result.diagnostics());
	}

	private static void assertLogged(QvtoExecutionResult result, String expected) {
		boolean found = result.diagnostics().stream()
				.anyMatch(d -> d.getMessage().contains(expected));
		assertTrue(found, "Expected log containing '" + expected
				+ "' but diagnostics were: " + result.diagnostics());
	}
}
