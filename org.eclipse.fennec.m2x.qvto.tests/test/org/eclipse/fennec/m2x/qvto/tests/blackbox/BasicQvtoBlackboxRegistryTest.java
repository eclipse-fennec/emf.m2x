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
package org.eclipse.fennec.m2x.qvto.tests.blackbox;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.eclipse.fennec.m2x.model.qvtoperational.BlackboxOperationDescriptor;
import org.eclipse.fennec.m2x.qvto.api.BasicQvtoBlackboxRegistry;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxInvocationContext;
import org.eclipse.fennec.m2x.qvto.api.QvtoBlackboxLibrary;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Unit tests for {@link BasicQvtoBlackboxRegistry}.
 */
class BasicQvtoBlackboxRegistryTest {

	private BasicQvtoBlackboxRegistry registry;

	@BeforeEach
	void setUp() {
		registry = new BasicQvtoBlackboxRegistry();
	}

	@Test
	void emptyRegistry_getLibrary_returnsEmpty() {
		assertTrue(registry.getLibrary("any.name").isEmpty());
	}

	@Test
	void emptyRegistry_getLibraries_returnsEmptyList() {
		assertTrue(registry.getLibraries().isEmpty());
	}

	@Test
	void register_andRetrieveByQualifiedName() {
		QvtoBlackboxLibrary lib = stubLibrary("MyLib", "com.example.MyLib");
		registry.register(lib);

		assertTrue(registry.getLibrary("com.example.MyLib").isPresent());
		assertEquals(lib, registry.getLibrary("com.example.MyLib").get());
	}

	@Test
	void register_notFoundByDifferentName() {
		registry.register(stubLibrary("MyLib", "com.example.MyLib"));

		assertTrue(registry.getLibrary("com.other.Lib").isEmpty());
	}

	@Test
	void register_getLibraries_containsLibrary() {
		QvtoBlackboxLibrary lib = stubLibrary("MyLib", "com.example.MyLib");
		registry.register(lib);

		List<QvtoBlackboxLibrary> all = registry.getLibraries();
		assertEquals(1, all.size());
		assertEquals(lib, all.get(0));
	}

	@Test
	void getLibraries_returnsImmutableCopy() {
		registry.register(stubLibrary("A", "a"));

		List<QvtoBlackboxLibrary> all = registry.getLibraries();
		assertThrows(UnsupportedOperationException.class, () -> all.add(stubLibrary("B", "b")));
	}

	@Test
	void unregister_removesLibrary() {
		QvtoBlackboxLibrary lib = stubLibrary("MyLib", "com.example.MyLib");
		registry.register(lib);
		registry.unregister(lib);

		assertTrue(registry.getLibrary("com.example.MyLib").isEmpty());
		assertTrue(registry.getLibraries().isEmpty());
	}

	@Test
	void unregister_nonExistent_noError() {
		QvtoBlackboxLibrary lib = stubLibrary("MyLib", "com.example.MyLib");
		// Should not throw
		registry.unregister(lib);
	}

	@Test
	void register_duplicateQualifiedName_overwrites() {
		QvtoBlackboxLibrary lib1 = stubLibrary("MyLib", "com.example.MyLib");
		QvtoBlackboxLibrary lib2 = stubLibrary("MyLib-v2", "com.example.MyLib");
		registry.register(lib1);
		registry.register(lib2);

		assertEquals(lib2, registry.getLibrary("com.example.MyLib").get());
		assertEquals(1, registry.getLibraries().size());
	}

	@Test
	void register_multipleLibraries() {
		registry.register(stubLibrary("A", "lib.A"));
		registry.register(stubLibrary("B", "lib.B"));
		registry.register(stubLibrary("C", "lib.C"));

		assertEquals(3, registry.getLibraries().size());
		assertTrue(registry.getLibrary("lib.A").isPresent());
		assertTrue(registry.getLibrary("lib.B").isPresent());
		assertTrue(registry.getLibrary("lib.C").isPresent());
	}

	@Test
	void register_null_throwsNPE() {
		assertThrows(NullPointerException.class, () -> registry.register(null));
	}

	@Test
	void unregister_null_throwsNPE() {
		assertThrows(NullPointerException.class, () -> registry.unregister(null));
	}

	// ==================== Helper ====================

	private static QvtoBlackboxLibrary stubLibrary(String moduleName, String qualifiedName) {
		return new QvtoBlackboxLibrary() {
			@Override
			public String getModuleName() {
				return moduleName;
			}

			@Override
			public String getUnitQualifiedName() {
				return qualifiedName;
			}

			@Override
			public List<String> getUsedPackageURIs() {
				return List.of();
			}

			@Override
			public List<BlackboxOperationDescriptor> getOperationDescriptors() {
				return List.of();
			}

			@Override
			public Object invoke(String operationName, QvtoBlackboxInvocationContext context,
					Object[] args) {
				return null;
			}

			@Override
			public String toString() {
				return "StubLibrary[" + moduleName + "/" + qualifiedName + "]";
			}
		};
	}
}
