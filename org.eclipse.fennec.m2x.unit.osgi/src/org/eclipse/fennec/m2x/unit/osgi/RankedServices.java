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
package org.eclipse.fennec.m2x.unit.osgi;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.function.Function;

import org.osgi.framework.Constants;

/**
 * The services a dynamic multiple reference has bound, by the name each one answers to (#269).
 *
 * <p>A component that binds services through bind and unbind methods keeps them here. Several
 * services may answer to the same name; the one with the highest {@code service.ranking} wins,
 * and among equal rankings the one registered first — the order the service registry itself
 * uses, and the one {@link OsgiServiceUnitResolver} applies to units (#141). Unbinding the
 * winner brings the next one back, so the answer never depends on the order in which the
 * component runtime happened to call the bind methods.
 *
 * <p>Thread-safe: bind and unbind arrive on the component runtime's threads while a running
 * transformation reads. A reader sees a consistent snapshot; a service bound or unbound during
 * a run is seen by the next lookup.
 *
 * @param <T> the service type
 * @author Data In Motion Consulting
 * @since 1.0
 */
public final class RankedServices<T> {

	private record Entry<T>(T service, String name, int ranking, long id) {
	}

	/** Highest ranking first; among equals, the lower service id — the one registered first. */
	private static final Comparator<Entry<?>> ORDER = Comparator
			.comparingInt((Entry<?> entry) -> entry.ranking()).reversed()
			.thenComparingLong(Entry::id);

	private final Function<T, String> nameOf;
	private final CopyOnWriteArrayList<Entry<T>> entries = new CopyOnWriteArrayList<>();

	/**
	 * @param nameOf the name a service answers to, read once when it is bound; must not be
	 *               {@code null}
	 */
	public RankedServices(Function<T, String> nameOf) {
		this.nameOf = Objects.requireNonNull(nameOf, "nameOf must not be null");
	}

	/**
	 * Adds a bound service.
	 *
	 * @param service    the service, must not be {@code null}
	 * @param properties its service properties, as the component runtime passes them to a bind
	 *                   method; must not be {@code null}
	 */
	public void add(T service, Map<String, ?> properties) {
		Objects.requireNonNull(service, "service must not be null");
		Objects.requireNonNull(properties, "properties must not be null");
		String name = Objects.requireNonNull(nameOf.apply(service), "a service must have a name");
		// The specification makes a ranking that is not an Integer count as 0
		int ranking = properties.get(Constants.SERVICE_RANKING) instanceof Integer i ? i : 0;
		long id = properties.get(Constants.SERVICE_ID) instanceof Long l ? l : Long.MAX_VALUE;
		entries.add(new Entry<>(service, name, ranking, id));
	}

	/**
	 * Removes an unbound service — that instance, not every service with its name.
	 *
	 * @param service the service, must not be {@code null}
	 */
	public void remove(T service) {
		Objects.requireNonNull(service, "service must not be null");
		entries.removeIf(entry -> entry.service() == service);
	}

	/**
	 * The service that answers to a name.
	 *
	 * @param name the name
	 * @return the highest-ranked service with that name, or empty if none is bound
	 */
	public Optional<T> get(String name) {
		return entries.stream()
				.filter(entry -> entry.name().equals(name))
				.sorted(ORDER)
				.map(Entry::service)
				.findFirst();
	}

	/**
	 * All names, each with the service that answers to it.
	 *
	 * @return the highest-ranked service per name, in ranking order; unmodifiable
	 */
	public List<T> all() {
		Map<String, T> winners = new LinkedHashMap<>();
		List<Entry<T>> ordered = new ArrayList<>(entries);
		ordered.sort(ORDER);
		for (Entry<T> entry : ordered) {
			winners.putIfAbsent(entry.name(), entry.service());
		}
		return List.copyOf(winners.values());
	}
}
