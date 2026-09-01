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
package org.eclipse.fennec.m2x.unit.store;

import java.util.Objects;

import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.compiled.CompiledFactory;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.model.compiled.SourceUnit;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitFingerprintService;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;

/**
 * What every store does to a unit on the way in, whatever its medium: refuse what is not a
 * document, copy it, stamp the copy, and derive the key from the manifest (#211, #213).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class UnitDocuments {

	private UnitDocuments() {}

	/**
	 * A document ready to be stored: the key it goes under, and the document that goes there —
	 * a sealed copy for a compiled unit, a fresh {@code SourceUnit} for a source. The store's
	 * own, never the caller's.
	 *
	 * @param key the key, carrying the fingerprint
	 * @param document what to store under it
	 */
	public record Sealed(UnitKey key, EObject document) {}

	/**
	 * Seals a compiled unit document for storing.
	 *
	 * <p>The copy is what is stored, so the copy is what is stamped: a document that reaches a
	 * store unsealed used to be written without a fingerprint and rejected by its own load as
	 * "carries no unit fingerprint" (#183). The caller's document stays as it was — the copy
	 * neither resolves a proxy nor rebinds a reference, whatever state the document is in.
	 *
	 * @param document the compiled unit document
	 * @param fingerprints where an unstamped document's fingerprint comes from
	 * @return the key and the copy to store
	 * @throws UnitStoreException if the document carries no manifest, no language or no name
	 */
	public static Sealed seal(CompiledUnit document, UnitFingerprintService fingerprints)
			throws UnitStoreException {
		Objects.requireNonNull(document, "document must not be null");
		Objects.requireNonNull(fingerprints, "fingerprints must not be null");
		if (document.getManifest() == null) {
			throw new UnitStoreException("the document carries no manifest; a store takes the compiled"
					+ " document — compile() first");
		}
		String language = document.getManifest().getLanguage();
		String qualifiedName = document.getManifest().getQualifiedName();
		if (language == null || language.isBlank()) {
			throw new UnitStoreException("the unit '" + qualifiedName + "' declares no language");
		}
		if (qualifiedName == null || qualifiedName.isBlank()) {
			throw new UnitStoreException("the document declares no qualified name");
		}
		EcoreUtil.Copier copier = new EcoreUtil.Copier(false, true);
		CompiledUnit stored = (CompiledUnit) copier.copy(document);
		copier.copyReferences();
		String fingerprint = stored.getManifest().getUnitFingerprint();
		if (fingerprint == null || fingerprint.isBlank()) {
			fingerprint = fingerprints.fingerprint(stored);
			stored.getManifest().setUnitFingerprint(fingerprint);
		}
		return new Sealed(UnitKey.pinned(language, qualifiedName, UnitKind.COMPILED, fingerprint), stored);
	}

	/**
	 * The document form of a source, and the key it goes under.
	 *
	 * @param language the language tag — a source does not name its own
	 * @param source the source unit
	 * @param fingerprints where the source fingerprint comes from
	 * @return the key and the document to store
	 */
	public static Sealed sourceForm(String language, Unit.Source source, UnitFingerprintService fingerprints) {
		Objects.requireNonNull(language, "language must not be null");
		Objects.requireNonNull(source, "source must not be null");
		Objects.requireNonNull(fingerprints, "fingerprints must not be null");
		String fingerprint = fingerprints.fingerprint(source);
		SourceUnit document = CompiledFactory.eINSTANCE.createSourceUnit();
		document.setLanguage(language);
		document.setQualifiedName(source.qualifiedName());
		document.setUri(source.uri().toString());
		document.setSource(source.source());
		document.setFingerprint(fingerprint);
		return new Sealed(UnitKey.pinned(language, source.qualifiedName(), UnitKind.SOURCE, fingerprint), document);
	}
}
