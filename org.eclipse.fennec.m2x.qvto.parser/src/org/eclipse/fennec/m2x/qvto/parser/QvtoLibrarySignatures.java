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
package org.eclipse.fennec.m2x.qvto.parser;

import java.util.List;

import org.eclipse.fennec.m2x.ocl.parser.typing.OclLibrarySignatures;

/**
 * The QVT-O standard library (QVT v1.3 §8.3) as signatures on top of OCL's, for typing QVT-O
 * expressions (#378): what the evaluator and the QVT-O operation provider implement.
 *
 * <p>QVT-O's {@code List(T)} is a {@code Sequence} and its {@code Dict(K, V)} a map type (D26), so
 * their operations are added to those receivers. The model, element, transformation and status
 * operations apply to model parameters and model objects, which the table addresses as
 * {@code OclAny}. A result the signature cannot know — an object's container — is
 * {@code Unknown}, never {@code OclAny}.
 */
final class QvtoLibrarySignatures {

	/** OCL's library and QVT-O's. */
	static final OclLibrarySignatures QVTO = OclLibrarySignatures.OCL.extend(QvtoLibrarySignatures::define);

	private QvtoLibrarySignatures() {
	}

	private static void define(OclLibrarySignatures.Builder b) {
		// §8.3.2 OclAny / Element
		b.op("OclAny", "repr", "String")
				.op("OclAny", "metaClassName", "String")
				.op("OclAny", "container", "Unknown")
				.op("OclAny", "subobjects", "Set(Unknown)")
				.op("OclAny", "allSubobjects", "Set(Unknown)")
				.op("OclAny", "subobjectsOfType", "Set(TypeArg0)", "Type")
				.op("OclAny", "subobjectsOfKind", "Set(TypeArg0)", "Type")
				.op("OclAny", "allSubobjectsOfType", "Set(TypeArg0)", "Type")
				.op("OclAny", "allSubobjectsOfKind", "Set(TypeArg0)", "Type")
				.op("OclAny", "clone", "Self")
				.op("OclAny", "deepclone", "Self")
				.op("OclAny", "unlink", "Unknown", "OclAny");

		// §8.3.5 Model
		b.op("OclAny", "objects", "Set(Unknown)")
				.op("OclAny", "objectsOfType", "Set(TypeArg0)", "Type")
				.op("OclAny", "objectsOfKind", "Set(TypeArg0)", "Type")
				.op("OclAny", "rootObjects", "Set(Unknown)")
				.op("OclAny", "addElement", "Unknown", "OclAny")
				.op("OclAny", "addObject", "Unknown", "OclAny")
				.op("OclAny", "removeElement", "Unknown", "OclAny")
				.op("OclAny", "copy", "Unknown")
				.op("OclAny", "createEmptyModel", "Unknown")
				.op("OclAny", "asTransformation", "Unknown");

		// §8.3.6 Transformation, §8.3.7 Status
		b.op("OclAny", "transform", "Unknown")
				.op("OclAny", "parallelTransform", "Unknown")
				.op("OclAny", "wait", "Unknown", "OclAny")
				.op("OclAny", "succeeded", "Boolean")
				.op("OclAny", "failed", "Boolean")
				.op("OclAny", "raisedException", "Unknown");

		// §8.3.9 String
		b.op("String", "format", "String", "OclAny")
				.op("String", "%", "String", "OclAny")
				.op("String", "length", "Integer")
				.op("String", "substringBefore", "String", "String")
				.op("String", "substringAfter", "String", "String")
				.op("String", "firstToUpper", "String")
				.op("String", "lastToUpper", "String")
				.op("String", "normalizeSpace", "String")
				.op("String", "replace", "String", "String", "String")
				.op("String", "match", "Boolean", "String")
				.op("String", "find", "Integer", "String")
				.op("String", "rfind", "Integer", "String")
				.op("String", "isQuoted", "Boolean", "String")
				.op("String", "quotify", "String", "String")
				.op("String", "unquotify", "String", "String")
				.op("String", "matchBoolean", "Boolean")
				.op("String", "matchInteger", "Boolean")
				.op("String", "matchFloat", "Boolean")
				.op("String", "matchReal", "Boolean")
				.op("String", "matchIdentifier", "Boolean")
				.op("String", "asBoolean", "Boolean")
				.op("String", "asInteger", "Integer")
				.op("String", "asFloat", "Real")
				.op("String", "asReal", "Real")
				.op("String", "startStrCounter", "Unknown", "String")
				.op("String", "getStrCounter", "Integer", "String")
				.op("String", "incrStrCounter", "Integer", "String")
				.op("String", "restartAllStrCounter", "Unknown")
				.op("String", "addSuffixNumber", "String");

		// §8.3.8 Integer
		b.op("Integer", "range", "Sequence(Integer)", "Integer")
				.op("Integer", "range", "Sequence(Integer)", "Integer", "Integer");

		// §8.3.11 List — a Sequence (D26); insertAt takes the element first
		b.op("Sequence", "add", "Unknown", "OclAny")
				.op("Sequence", "insertAt", "Unknown", "OclAny", "Integer")
				.op("Sequence", "remove", "Unknown", "OclAny")
				.op("Sequence", "removeAt", "T", "Integer")
				.op("Sequence", "removeFirst", "T")
				.op("Sequence", "removeLast", "T")
				.op("Sequence", "removeAll", "Unknown", "Collection(OclAny)")
				.op("Collection", "asList", "Sequence(T)")
				.op("Collection", "joinfields", "String")
				.op("Collection", "joinfields", "String", "String")
				.op("Collection", "joinfields", "String", "String", "String")
				.op("Collection", "joinfields", "String", "String", "String", "String");

		// §8.3.10 Dict — a map type (D26)
		b.op(List.of("Map"), "size", "Integer")
				.op("Map", "isEmpty", "Boolean")
				.op("Map", "notEmpty", "Boolean")
				.op("Map", "includes", "Boolean", "OclAny")
				.op("Map", "excludes", "Boolean", "OclAny")
				.op("Map", "includesValue", "Boolean", "OclAny")
				.op("Map", "excludesValue", "Boolean", "OclAny")
				.op("Map", "hasKey", "Boolean", "OclAny")
				.op("Map", "get", "T", "OclAny")
				.op("Map", "at", "T", "OclAny")
				.op("Map", "defaultget", "Common(T, Arg1)", "OclAny", "OclAny")
				.op("Map", "put", "Unknown", "OclAny", "OclAny")
				.op("Map", "keys", "Unknown")
				.op("Map", "values", "Sequence(T)")
				.op("Map", "including", "Unknown", "OclAny", "OclAny")
				.op("Map", "excluding", "Self", "OclAny")
				.op("Map", "union", "Unknown", "OclAny")
				.op("Map", "clear", "Unknown")
				.op("Map", "remove", "Unknown", "OclAny");
	}
}
