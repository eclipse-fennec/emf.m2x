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
package org.eclipse.fennec.m2x.unit.tests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import java.util.Map;

import org.eclipse.fennec.m2x.unit.osgi.RankedServices;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.osgi.framework.Constants;

/**
 * The bound services of a dynamic multiple reference, by name and ranking (#269).
 *
 * <p>The component runtime may call bind and unbind methods in any order, so the answer must
 * depend on the services' rankings and ids alone — the order the service registry uses.
 */
@DisplayName("RankedServices")
class RankedServicesTest {

	/** A service that answers to a name; identity matters, so equal names are distinct objects. */
	private record Service(String name, String label) {
	}

	private final RankedServices<Service> services = new RankedServices<>(Service::name);

	private static Map<String, Object> properties(int ranking, long id) {
		return Map.of(Constants.SERVICE_RANKING, ranking, Constants.SERVICE_ID, id);
	}

	@Test
	@DisplayName("the highest ranking answers, whatever order the services were bound in")
	void highestRankingWins() {
		Service low = new Service("lib", "low");
		Service high = new Service("lib", "high");
		services.add(high, properties(10, 2));
		services.add(low, properties(0, 1));

		assertSame(high, services.get("lib").orElseThrow());
		assertEquals(List.of(high), services.all(), "one service per name");
	}

	@Test
	@DisplayName("among equal rankings the one registered first answers")
	void equalRankingFirstRegisteredWins() {
		Service first = new Service("lib", "first");
		Service second = new Service("lib", "second");
		services.add(second, properties(0, 8));
		services.add(first, properties(0, 7));

		assertSame(first, services.get("lib").orElseThrow());
	}

	@Test
	@DisplayName("removing the winner brings the next one back")
	void removingWinnerFallsBack() {
		Service low = new Service("lib", "low");
		Service high = new Service("lib", "high");
		services.add(low, properties(0, 1));
		services.add(high, properties(5, 2));

		services.remove(high);

		assertSame(low, services.get("lib").orElseThrow());
	}

	@Test
	@DisplayName("removing removes that instance, not every service with its name")
	void removeByIdentity() {
		Service bound = new Service("lib", "same");
		Service equalButOther = new Service("lib", "same");
		services.add(bound, properties(0, 1));

		services.remove(equalButOther);
		assertSame(bound, services.get("lib").orElseThrow(),
				"records are equal, but only identity counts here");

		services.add(equalButOther, properties(0, 2));
		services.remove(bound);
		assertSame(equalButOther, services.get("lib").orElseThrow());
	}

	@Test
	@DisplayName("a ranking that is not an Integer counts as 0")
	void nonIntegerRankingIsZero() {
		Service odd = new Service("lib", "odd");
		Service plain = new Service("lib", "plain");
		services.add(odd, Map.of(Constants.SERVICE_RANKING, "100", Constants.SERVICE_ID, 1L));
		services.add(plain, properties(1, 2));

		assertSame(plain, services.get("lib").orElseThrow());
	}

	@Test
	@DisplayName("all() returns each name once, highest ranking first")
	void allOnePerName() {
		Service a = new Service("a", "a");
		Service b = new Service("b", "b");
		Service bLow = new Service("b", "b-low");
		services.add(a, properties(0, 1));
		services.add(bLow, properties(0, 2));
		services.add(b, properties(3, 3));

		assertEquals(List.of(b, a), services.all());
		assertTrue(services.get("c").isEmpty());
	}
}
