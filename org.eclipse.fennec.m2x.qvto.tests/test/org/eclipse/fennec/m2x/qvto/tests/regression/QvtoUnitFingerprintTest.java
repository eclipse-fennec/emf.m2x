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
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.util.Map;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EcorePackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.emf.ecore.resource.ResourceSet;
import org.eclipse.emf.ecore.resource.impl.ResourceSetImpl;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.emf.ecore.xmi.impl.XMIResourceFactoryImpl;
import org.eclipse.fennec.m2x.model.compiled.CompiledPackage;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.ocl.OclPackage;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.model.qvtoperational.QvtOperationalPackage;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.tests.AbstractQvtoEngineTest;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.junit.jupiter.api.Test;

/**
 * The unit fingerprint of a QVT-O transformation (#138, acceptance criteria).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoUnitFingerprintTest extends AbstractQvtoEngineTest {

	private static final String SOURCE = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			transformation test(in s : SRC, out t : TGT) {
			    property threshold : Integer = 10;
			    intermediate class Holder { count : Integer = 0; }
			    mapping SourceElement::toTarget() : TargetElement
			        when { self.value > threshold; } {
			        name := self.name;
			    }
			    main() {
			        s.objectsOfType(SourceElement)->map toTarget();
			    }
			}
			""";

	// Whitespace, line breaks and comments rearranged; the program is the same
	private static final String REFORMATTED = """
			-- the same transformation, formatted differently
			modeltype SRC uses 'http://test/source/1.0';   modeltype TGT uses 'http://test/target/1.0';

			transformation test(in s : SRC, out t : TGT)
			{
			    /* a module property */
			    property threshold : Integer = 10;
			    intermediate class Holder {
			        count : Integer = 0;
			    }
			    mapping SourceElement::toTarget() : TargetElement when { self.value > threshold; }
			    {
			        name := self.name;   -- copy the name
			    }
			    main() { s.objectsOfType(SourceElement)->map toTarget(); }
			}
			""";

	// The same intermediate classes, different mapping logic — the case fp1 cannot tell apart
	private static final String OTHER_LOGIC = """
			modeltype SRC uses 'http://test/source/1.0';
			modeltype TGT uses 'http://test/target/1.0';
			transformation test(in s : SRC, out t : TGT) {
			    property threshold : Integer = 10;
			    intermediate class Holder { count : Integer = 0; }
			    mapping SourceElement::toTarget() : TargetElement
			        when { self.value < threshold; } {
			        name := self.name + '!';
			    }
			    main() {
			        s.objectsOfType(SourceElement)->map toTarget();
			    }
			}
			""";

	@Test
	void reformattedSource_sameFingerprint() throws Exception {
		assertEquals(fingerprint(SOURCE), fingerprint(REFORMATTED));
	}

	@Test
	void differentLogic_sameIntermediateClasses_differentFingerprint() throws Exception {
		assertNotEquals(fingerprint(SOURCE), fingerprint(OTHER_LOGIC));
	}

	@Test
	void compiledTwice_sameFingerprint_differentIds() throws Exception {
		CompiledUnit first = engine.compile(SOURCE, "test");
		CompiledUnit second = engine.compile(SOURCE, "test");
		assertNotEquals(first.getId(), second.getId());
		assertEquals(first.getManifest().getUnitFingerprint(), second.getManifest().getUnitFingerprint());
	}

	// The manifest's value is the one the service computes, in the current scheme
	@Test
	void manifest_carriesTheServiceValue() throws Exception {
		CompiledUnit compiled = engine.compile(SOURCE, "test");
		assertEquals(DefaultUnitFingerprintService.INSTANCE.fingerprint(compiled),
				compiled.getManifest().getUnitFingerprint());
		assertTrue(compiled.getManifest().getUnitFingerprint().startsWith("m2x1:"));
	}

	// Satellites do not influence the value: the parsed graph — satellites uncontained — and the
	// compiled unit — satellites in the container — agree, and so does another engine's parse
	@Test
	void parsedAndCompiled_andAnotherEngine_agree() throws Exception {
		OperationalTransformation parsed = parse(SOURCE);
		String ofParsed = DefaultUnitFingerprintService.INSTANCE.fingerprint(
				new QvtoUnit.CompiledUnit("test", parsed));
		CompiledUnit compiled = engine.compile(SOURCE, "test");
		assertEquals(ofParsed, compiled.getManifest().getUnitFingerprint());

		QvtoEngine another = QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build()).build());
		assertEquals(ofParsed, another.compile(SOURCE, "test").getManifest().getUnitFingerprint());
	}

	// Independent of serialization: a saved and reloaded unit has the same value
	@Test
	void reloaded_sameFingerprint() throws Exception {
		CompiledUnit compiled = engine.compile(SOURCE, "test");
		CompiledUnit reloaded = roundTrip(compiled);
		assertEquals(compiled.getManifest().getUnitFingerprint(),
				DefaultUnitFingerprintService.INSTANCE.fingerprint(reloaded));
	}

	// ---- Helpers ----

	private static String fingerprint(String source) throws Exception {
		return engine.compile(source, "test").getManifest().getUnitFingerprint();
	}

	private static CompiledUnit roundTrip(CompiledUnit compiled) throws Exception {
		Resource resource = freshResourceSet().createResource(URI.createURI("unit.compiled"));
		resource.getContents().add(compiled);
		ByteArrayOutputStream bytes = new ByteArrayOutputStream();
		resource.save(bytes, Map.of());
		ResourceSet in = freshResourceSet();
		Resource loaded = in.createResource(URI.createURI("unit.compiled"));
		loaded.load(new ByteArrayInputStream(bytes.toByteArray()), Map.of());
		EcoreUtil.resolveAll(in);
		return (CompiledUnit) loaded.getContents().get(0);
	}

	private static ResourceSet freshResourceSet() {
		ResourceSet resourceSet = new ResourceSetImpl();
		resourceSet.getResourceFactoryRegistry().getExtensionToFactoryMap()
				.put("*", new XMIResourceFactoryImpl());
		resourceSet.getPackageRegistry().put(CompiledPackage.eNS_URI, CompiledPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(QvtOperationalPackage.eNS_URI, QvtOperationalPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(OclPackage.eNS_URI, OclPackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(EcorePackage.eNS_URI, EcorePackage.eINSTANCE);
		resourceSet.getPackageRegistry().put(sourcePackage.getNsURI(), sourcePackage);
		resourceSet.getPackageRegistry().put(targetPackage.getNsURI(), targetPackage);
		return resourceSet;
	}
}
