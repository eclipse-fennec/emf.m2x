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
package org.eclipse.fennec.m2x.qvto.tests.regression;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EReference;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

/**
 * Target state for #137: a parsed unit is self-contained and can be stored.
 *
 * <p>Measured on {@code snapshot} before these tests were written — the numbers
 * are the reason they are disabled rather than the reason they are wrong:
 *
 * <table>
 *   <caption>cross-references pointing outside the transformation tree</caption>
 *   <tr><th>parsed source</th><th>targets outside the tree</th><th>{@code save()}</th></tr>
 *   <tr><td>minimal, no import, no modeltype</td><td>1 — {@code Variable.type → PrimitiveType(Integer)}</td>
 *       <td>fails on {@code PrimitiveTypeImpl (name: Integer)}</td></tr>
 *   <tr><td>plus modeltype, mapping and {@code self}</td>
 *       <td>15 — {@code VariableExp.referredVariable}, {@code TypeExp.referredType},
 *           {@code ModelParameter.eType}, …</td>
 *       <td>fails on {@code ModelTypeImpl (name: SRC)}</td></tr>
 * </table>
 *
 * <p>Both transformations resolve, link and execute; the objects with no
 * container are <em>parser-created satellites</em> (type instances, environment
 * variables), not unresolved imports — see #144. An unresolvable import is a
 * different matter and already fails with {@code Cannot resolve import}.
 *
 * <p>Note what these tests deliberately do <em>not</em> demand: references into
 * the metamodels stay external. A referenced {@code EPackage} lives in its own
 * resource and must keep living there, otherwise a shared unit could not lend
 * its types — the reason the containment approach of PR #128 was rejected. Only
 * objects the parser itself created need a home.
 *
 * <p><b>SPEC-FIRST:</b> the target state follows from D1 (the AST metamodels
 * exist for XMI serialization) and the acceptance criteria of #135. Failures are
 * implementation gaps, NOT test errors.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Disabled("#137 — parser satellites have no container yet, so a parsed unit is not storable")
class QvtoAstSelfContainmentTest extends AbstractQvtoEngineTest {

	private static final String MINIMAL = """
			transformation test() {
			    property threshold : Integer = 10;
			    main() {
			        log('x');
			    }
			}
			""";

	private static final String WITH_MAPPING = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			transformation test(in s : SRC, out t : TGT) {
			    property threshold : Integer = 10;
			    mapping SourceElement::toTarget() : TargetElement
			        when { self.value > threshold; } {
			        name := self.name;
			    }
			    main() {
			        s.objectsOfType(SourceElement)->map toTarget();
			    }
			}
			""";

	// #135: a parsed unit holds no reference to an object that lives neither in
	// its own tree nor in a resource
	@Test
	void parse_minimalUnit_hasNoUncontainedTargets() throws Exception {
		OperationalTransformation t = parse(MINIMAL);
		assertEquals(List.of(), uncontainedTargets(t));
	}

	// #135: the same for a unit with modeltypes, a mapping and a guard
	@Test
	void parse_unitWithMapping_hasNoUncontainedTargets() throws Exception {
		OperationalTransformation t = parse(WITH_MAPPING);
		assertEquals(List.of(), uncontainedTargets(t));
	}

	// #135: a parsed unit can be written as XMI
	@Test
	void parse_minimalUnit_savesAsXmi() throws Exception {
		assertSaves(parse(MINIMAL));
	}

	// #135: the same for a unit with modeltypes, a mapping and a guard
	@Test
	void parse_unitWithMapping_savesAsXmi() throws Exception {
		assertSaves(parse(WITH_MAPPING));
	}

	// ---- Helpers ----

	/**
	 * Returns the cross-reference targets that live neither in the transformation's
	 * own containment tree nor in a resource. References into a loaded metamodel are
	 * not among them and must not be: a unit lends its types, it does not own them.
	 */
	private static List<String> uncontainedTargets(OperationalTransformation t) {
		List<String> found = new ArrayList<>();
		for (Iterator<EObject> it = t.eAllContents(); it.hasNext();) {
			EObject owner = it.next();
			for (EReference ref : owner.eClass().getEAllReferences()) {
				if (ref.isContainment() || ref.isDerived() || !owner.eIsSet(ref)) {
					continue;
				}
				Object value = owner.eGet(ref);
				List<?> targets = value instanceof List<?> list ? list : List.of(value);
				for (Object target : targets) {
					if (target instanceof EObject e && e.eResource() == null
							&& EcoreUtil.getRootContainer(e) != t) {
						String entry = owner.eClass().getName() + "." + ref.getName()
								+ " -> " + e.eClass().getName();
						if (!found.contains(entry)) {
							found.add(entry);
						}
					}
				}
			}
		}
		return found;
	}

	private static void assertSaves(OperationalTransformation t) {
		ResourceSet rs = new ResourceSetImpl();
		rs.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("*", new XMIResourceFactoryImpl());
		Resource resource = rs.createResource(URI.createURI("unit.xmi"));
		resource.getContents().add(t);
		try {
			resource.save(new ByteArrayOutputStream(), Map.of());
		} catch (Exception failure) {
			assertTrue(false, "saving the parsed unit failed: " + failure.getMessage());
		}
	}
}
