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

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.eclipse.fennec.m2x.qvto.api.BasicQvtoModelExtent;
import org.eclipse.fennec.m2x.qvto.api.QvtoExecutionResult;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Test;

/**
 * An assignment to an object of an in model (§8.1.3.2) names where it happened, what it wrote and
 * which model refused it.
 */
class QvtoReadOnlyMutationMessageTest extends AbstractQvtoEngineTest {

	@Test
	void theMessage_namesPositionObjectFeatureAndModel() throws Exception {
		QvtoExecutionResult result = executeWithExtents("""
				modeltype SRC uses 'http://test/source/1.0';
				transformation test(in s : SRC) {
				    helper SourceElement::rename() {
				        self.name := 'changed';
				    }
				    main() { s.rootObjects()->first().oclAsType(SourceElement).rename(); }
				}
				""", new BasicQvtoModelExtent(createSourceElement("a", 1)));
		assertFalse(result.isSuccess());
		String message = result.diagnostics().stream().map(d -> d.getMessage())
				.filter(m -> m.contains("InvalidModelMutation")).findFirst().orElseThrow();
		assertTrue(message.contains("Cannot assign 'name' of SourceElement 'a': it belongs to the in model 's'"), message);
		assertTrue(message.contains("test:4:"), message);
	}
}
