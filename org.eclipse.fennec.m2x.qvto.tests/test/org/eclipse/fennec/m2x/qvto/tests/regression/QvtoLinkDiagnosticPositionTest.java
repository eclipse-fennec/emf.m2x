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
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.eclipse.emf.common.util.URI;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.emf.ecore.resource.Resource;
import org.eclipse.fennec.m2x.model.compiled.DependencyMode;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoConfiguration;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.eclipse.fennec.m2x.qvto.engine.QvtoEngines;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoLinkPolicy;
import org.eclipse.fennec.m2x.qvto.engine.internal.QvtoLinker;
import org.eclipse.fennec.m2x.qvto.parser.QvtoParserSupport;
import org.eclipse.fennec.m2x.unit.api.UnitCompileOptions;
import org.eclipse.fennec.m2x.unit.api.UnitResolutionException;
import org.junit.jupiter.api.Test;

/**
 * A link failure is a compile error like a syntax error, and arrives the same way: as a
 * {@link Resource.Diagnostic} at the declaration that asked for it, in
 * {@link QvtoParseException#getErrors()} (#264).
 *
 * <p>Every import is tried before the compile fails, so a unit missing several libraries lists
 * them all. What fails inside a dependency is reported at the import of that dependency — the line
 * the importing unit can change.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoLinkDiagnosticPositionTest {

	private static final String GATE_USER = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			import gate.Lib;
			transformation GateUser(inout m : ECORE) {
			    main() {
			        m.objectsOfType(EPackage)->forEach(p) {
			            p.name := shout(p.name);
			        };
			    }
			}
			""";

	private static final String TWO_MISSING = """
			modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
			import first.Missing;
			import HelperLib;
			import second.Missing;
			transformation Main(inout m : ECORE) {
			    main() {
			        log(greet());
			    }
			}
			""";

	private static final String HELPER_LIB = """
			library HelperLib {
			    helper greet() : String {
			        return 'hello';
			    }
			}
			""";

	private static QvtoEngine engineWith(Map<String, String> libraries) {
		QvtoUnitResolver resolver = name -> Optional.ofNullable(libraries.get(name))
				.map(source -> new QvtoUnit.SourceUnit(name, URI.createURI("inline:" + name), source));
		return QvtoEngines.create(QvtoConfiguration.builder(OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(resolver).unitResolverEnabled(true)
				.build());
	}

	// ==== compile ====

	@Test
	void unresolvableImport_isOneDiagnostic_atTheImport_inEveryMode() {
		QvtoEngine engine = engineWith(Map.of());
		for (DependencyMode mode : DependencyMode.values()) {
			QvtoParseException failure = assertThrows(QvtoParseException.class,
					() -> engine.compile(GATE_USER, "GateUser", UnitCompileOptions.of(mode)), mode.getName());
			assertEquals("Cannot resolve import: gate.Lib", failure.getMessage(), mode.getName());
			assertEquals(1, failure.getErrors().size(), mode.getName());
			assertAt(failure.getErrors().get(0), GATE_USER, "import gate.Lib");
			assertEquals("Cannot resolve import: gate.Lib", failure.getErrors().get(0).getMessage());
		}
	}

	@Test
	void everyUnresolvableImport_isReported_notOnlyTheFirst() {
		QvtoEngine engine = engineWith(Map.of("HelperLib", HELPER_LIB));
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> engine.compile(TWO_MISSING, "Main"));
		List<Resource.Diagnostic> errors = failure.getErrors();
		assertEquals(2, errors.size(), String.valueOf(errors));
		assertAt(errors.get(0), TWO_MISSING, "import first.Missing");
		assertEquals("Cannot resolve import: first.Missing", errors.get(0).getMessage());
		assertAt(errors.get(1), TWO_MISSING, "import second.Missing");
		assertEquals("Cannot resolve import: second.Missing", errors.get(1).getMessage());
		assertTrue(failure.getMessage().contains("first.Missing")
				&& failure.getMessage().contains("second.Missing"), failure.getMessage());
	}

	@Test
	void unresolvableExtends_isPlacedAtTheModuleReference() {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE) extends MissingBase {
				    main() {
				    }
				}
				""";
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> engineWith(Map.of()).compile(source, "Main"));
		assertEquals(1, failure.getErrors().size(), String.valueOf(failure.getErrors()));
		assertAt(failure.getErrors().get(0), source, "MissingBase");
	}

	@Test
	void unresolvableAccess_isPlacedAtTheModuleReference() {
		String source = """
				modeltype ECORE uses ecore('http://www.eclipse.org/emf/2002/Ecore');
				transformation Main(inout m : ECORE) access MissingLib {
				    main() {
				    }
				}
				""";
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> engineWith(Map.of()).compile(source, "Main"));
		assertEquals(1, failure.getErrors().size(), String.valueOf(failure.getErrors()));
		assertAt(failure.getErrors().get(0), source, "MissingLib");
	}

	@Test
	void failureInsideADependency_isReportedAtTheImportOfThatDependency() {
		String mid = "import deep.Missing;\nlibrary Mid { helper m() : String { return 'm'; } }\n";
		String main = "\nimport Mid;\ntransformation Main() { main() { log(m()); } }\n";
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> engineWith(Map.of("Mid", mid)).compile(main, "Main"));
		assertEquals(1, failure.getErrors().size(), String.valueOf(failure.getErrors()));
		Resource.Diagnostic error = failure.getErrors().get(0);
		// line 2 of Main, not line 1 of Mid, where the missing import is written
		assertAt(error, main, "import Mid");
		assertTrue(error.getMessage().contains("In import 'Mid'")
				&& error.getMessage().contains("Cannot resolve import: deep.Missing"), error.getMessage());
	}

	@Test
	void circularImport_isPlacedAtTheImportThatStartsTheCycle() {
		QvtoEngine cyclic = engineWith(Map.of(
				"A", "import B;\nlibrary A { helper a() : String { return b(); } }\n",
				"B", "import A;\nlibrary B { helper b() : String { return a(); } }\n"));
		String main = "import A;\ntransformation Main() { main() { log(a()); } }\n";
		QvtoParseException failure = assertThrows(QvtoParseException.class, () -> cyclic.compile(main, "Main"));
		assertEquals(1, failure.getErrors().size(), String.valueOf(failure.getErrors()));
		assertAt(failure.getErrors().get(0), main, "import A");
		assertTrue(failure.getMessage().contains("Circular import"), failure.getMessage());
	}

	@Test
	void failingResolver_isPlacedAtTheImport_andKeepsItsCause() {
		QvtoUnitResolver broken = name -> {
			throw new UnitResolutionException("store unavailable");
		};
		QvtoEngine engine = QvtoEngines.create(QvtoConfiguration.builder(
				OclConfiguration.builder(new OclParserSupport()).build())
				.addUnitResolver(broken).unitResolverEnabled(true).build());
		QvtoParseException failure = assertThrows(QvtoParseException.class,
				() -> engine.compile(GATE_USER, "GateUser"));
		assertEquals(1, failure.getErrors().size(), String.valueOf(failure.getErrors()));
		assertAt(failure.getErrors().get(0), GATE_USER, "import gate.Lib");
		assertTrue(failure.getMessage().contains("store unavailable"), failure.getMessage());
		assertTrue(hasCause(failure, UnitResolutionException.class), "the resolver's failure stays the cause");
	}

	// ==== execute-time linker ====

	@Test
	void linker_reportsEveryUnresolvableImport_atItsDeclaration() throws Exception {
		QvtoParserSupport parserSupport = new QvtoParserSupport();
		OperationalTransformation transformation = parserSupport.parse(TWO_MISSING, "Main",
				EPackage.Registry.INSTANCE);
		QvtoUnitResolver resolver = name -> "HelperLib".equals(name)
				? Optional.of(new QvtoUnit.SourceUnit(name, URI.createURI("inline:" + name), HELPER_LIB))
				: Optional.empty();
		QvtoLinker linker = new QvtoLinker(new QvtoLinkPolicy(parserSupport, List.of(resolver),
				EPackage.Registry.INSTANCE, new BasicQvtoBlackboxRegistry(), Set.of(), Set.of(), 10));

		QvtoParseException failure = assertThrows(QvtoParseException.class, () -> linker.link(transformation));
		List<Resource.Diagnostic> errors = failure.getErrors();
		assertEquals(2, errors.size(), String.valueOf(errors));
		assertAt(errors.get(0), TWO_MISSING, "import first.Missing");
		assertAt(errors.get(1), TWO_MISSING, "import second.Missing");
	}

	// ==== helpers ====

	/** The diagnostic stands where {@code needle} first occurs in {@code source}. */
	private static void assertAt(Resource.Diagnostic diagnostic, String source, String needle) {
		int offset = source.indexOf(needle);
		assertTrue(offset >= 0, needle);
		String before = source.substring(0, offset);
		int line = (int) before.chars().filter(c -> c == '\n').count() + 1;
		int column = offset - (before.lastIndexOf('\n') + 1);
		assertEquals(line + ":" + column, diagnostic.getLine() + ":" + diagnostic.getColumn(),
				"position of '" + needle + "' for " + diagnostic.getMessage());
	}

	private static boolean hasCause(Throwable failure, Class<? extends Throwable> type) {
		for (Throwable cause = failure.getCause(); cause != null; cause = cause.getCause()) {
			if (type.isInstance(cause)) {
				return true;
			}
		}
		return false;
	}
}
