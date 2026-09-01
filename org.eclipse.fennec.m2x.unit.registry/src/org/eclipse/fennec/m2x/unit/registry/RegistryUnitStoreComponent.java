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
package org.eclipse.fennec.m2x.unit.registry;

import java.util.List;
import java.util.Optional;

import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryWriter;
import org.eclipse.fennec.m2x.model.compiled.CompiledUnit;
import org.eclipse.fennec.m2x.unit.fingerprint.DefaultUnitFingerprintService;
import org.eclipse.fennec.m2x.unit.api.Unit;
import org.eclipse.fennec.m2x.unit.api.UnitKey;
import org.eclipse.fennec.m2x.unit.api.UnitKind;
import org.eclipse.fennec.m2x.unit.api.UnitStore;
import org.eclipse.fennec.m2x.unit.api.UnitStoreException;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicyOption;

/**
 * Publishes a {@link RegistryUnitStore} as a {@link UnitStore} service over a named
 * {@link EObjectRegistry} (#213).
 *
 * <p>Which registry it reads and which write face it uses is a matter of the reference targets:
 *
 * <pre>
 * "M2xRegistryUnitStore": {
 *     "registry.target": "(emf.eobject.registry.name=units)",
 *     "writer.target": "(emf.eobject.registry.name=units)",
 *     "source": "m2x-unit-store"
 * }
 * </pre>
 *
 * <p>Without a write face the store is read-only — the registry is the atlas's delivery, and
 * writing is the content source's business.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(name = "M2xRegistryUnitStore", service = UnitStore.class,
		configurationPolicy = ConfigurationPolicy.REQUIRE)
public class RegistryUnitStoreComponent implements UnitStore {

	/** The component's configuration. */
	public @interface Config {
		/**
		 * The source name written entries belong to.
		 *
		 * @return the source name
		 */
		String source() default RegistryUnitStore.DEFAULT_SOURCE;
	}

	private final RegistryUnitStore store;

	/**
	 * DS constructor.
	 *
	 * @param registry the registry to read from
	 * @param writer the write face, or {@code null} for a read-only store
	 * @param config the configuration
	 */
	@Activate
	public RegistryUnitStoreComponent(@Reference(name = "registry") EObjectRegistry registry,
			@Reference(name = "writer", cardinality = ReferenceCardinality.OPTIONAL,
					policyOption = ReferencePolicyOption.GREEDY) EObjectRegistryWriter writer,
			Config config) {
		this.store = new RegistryUnitStore(registry, writer, config.source(),
				DefaultUnitFingerprintService.INSTANCE);
	}

	@Override
	public UnitKey put(CompiledUnit document) throws UnitStoreException {
		return store.put(document);
	}

	@Override
	public UnitKey put(String language, Unit.Source source) throws UnitStoreException {
		return store.put(language, source);
	}

	@Override
	public Optional<Unit> get(UnitKey key) throws UnitStoreException {
		return store.get(key);
	}

	@Override
	public boolean contains(UnitKey key) throws UnitStoreException {
		return store.contains(key);
	}

	@Override
	public List<UnitKey> versions(String language, String qualifiedName, UnitKind kind) throws UnitStoreException {
		return store.versions(language, qualifiedName, kind);
	}

	@Override
	public boolean remove(UnitKey key) throws UnitStoreException {
		return store.remove(key);
	}
}
