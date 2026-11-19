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
package org.eclipse.fennec.m2x.qvto.engine.internal;

import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CopyOnWriteArrayList;

import org.eclipse.emf.ecore.util.EcoreUtil;
import org.eclipse.fennec.m2x.model.qvtoperational.OperationalTransformation;
import org.eclipse.fennec.m2x.qvtd.api.QvtdExecutionContext;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationProvider;
import org.eclipse.fennec.m2x.qvtd.api.RelationImplementationResult;
import org.eclipse.fennec.m2x.qvto.api.QvtoEngine;
import org.eclipse.fennec.m2x.qvto.api.QvtoParseException;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnit;
import org.eclipse.fennec.m2x.qvto.api.QvtoUnitResolver;
import org.osgi.framework.Constants;
import org.osgi.service.component.ComponentServiceObjects;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.ConfigurationPolicy;
import org.osgi.service.component.annotations.Deactivate;
import org.osgi.service.component.annotations.Reference;
import org.osgi.service.component.annotations.ReferenceCardinality;
import org.osgi.service.component.annotations.ReferencePolicy;
import org.osgi.service.component.annotations.ReferenceScope;

/**
 * The QVT-O side of a QVT-R/QVT-O hybrid transformation under OSGi (QVT v1.3 §7.11.3.6, #286):
 * serves the {@code implementedby} clauses of a relational transformation with the mappings of
 * the QVT-O units marked as relation implementations.
 *
 * <p>A bundle marks a unit it publishes:
 *
 * <pre>
 * &#64;Component(property = {
 *     QvtoServiceUnitResolver.UNIT_NAME + "=my.company.Uml2RdbmsImpl",
 *     QvtoRelationImplementationComponent.IMPLEMENTATION + "=true" })
 * public class Uml2RdbmsImplUnit implements QvtoUnitResolver { … }
 * </pre>
 *
 * <p>Which relational transformation a unit implements is said by the unit itself, with
 * {@code refines} (§8.2.1.1): {@code transformation Uml2RdbmsImpl(…) refines uml2rdbms;}. A unit
 * serves only that one; a unit without {@code refines} serves any. The service property only
 * says which units a provider loads — narrowed with {@code units.target}, so that a factory
 * configuration can make one provider per group of units.
 *
 * <p>The units are loaded into an engine instance of this component's own, when they are first
 * needed after they changed; a unit that cannot be resolved or parsed is not served, and the
 * QVT-R run reports the operation as one nobody serves. The QVT-R engine uses this provider only
 * when {@code qvtd.implementationProvidersEnabled} is on.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
@Component(name = "QvtoRelationImplementations", service = RelationImplementationProvider.class,
		configurationPolicy = ConfigurationPolicy.OPTIONAL)
public class QvtoRelationImplementationComponent implements RelationImplementationProvider {

	/** Service property marking a QVT-O unit as a relation implementation. */
	public static final String IMPLEMENTATION = "qvto.relation.implementation";

	/** A bound unit with what orders it: its ranking, then its registration. */
	private record RankedUnit(QvtoUnitResolver resolver, String name, int ranking, long id) {
	}

	/** Highest ranking first — the unit asked first for an operation — then the one registered first. */
	private static final Comparator<RankedUnit> ORDER = Comparator
			.comparingInt(RankedUnit::ranking).reversed()
			.thenComparingLong(RankedUnit::id);

	private final ComponentServiceObjects<QvtoEngine> engines;
	private final CopyOnWriteArrayList<RankedUnit> units = new CopyOnWriteArrayList<>();
	private final Object lock = new Object();
	private volatile boolean stale = true;
	private QvtoEngine engine;

	/**
	 * @param engines where this provider gets an engine instance of its own
	 */
	@Activate
	public QvtoRelationImplementationComponent(@Reference(name = "engine", service = QvtoEngine.class,
			scope = ReferenceScope.PROTOTYPE_REQUIRED) ComponentServiceObjects<QvtoEngine> engines) {
		this.engines = engines;
	}

	/**
	 * Binds a unit marked as relation implementation.
	 *
	 * @param resolver   the resolver publishing the unit
	 * @param properties its service properties: the unit's name and its ranking
	 */
	@Reference(name = "units", service = QvtoUnitResolver.class, cardinality = ReferenceCardinality.MULTIPLE,
			policy = ReferencePolicy.DYNAMIC, target = "(" + IMPLEMENTATION + "=true)", unbind = "removeUnit")
	void addUnit(QvtoUnitResolver resolver, Map<String, Object> properties) {
		if (!(properties.get(QvtoServiceUnitResolver.UNIT_NAME) instanceof String name)) {
			return; // a resolver without a unit name has no unit to ask for
		}
		int ranking = properties.get(Constants.SERVICE_RANKING) instanceof Integer i ? i : 0;
		long id = properties.get(Constants.SERVICE_ID) instanceof Long l ? l : Long.MAX_VALUE;
		units.add(new RankedUnit(resolver, name, ranking, id));
		stale = true;
	}

	/**
	 * Unbinds a unit.
	 *
	 * @param resolver the resolver publishing the unit
	 */
	void removeUnit(QvtoUnitResolver resolver) {
		if (units.removeIf(unit -> unit.resolver() == resolver)) {
			stale = true;
		}
	}

	@Deactivate
	void deactivate() {
		synchronized (lock) {
			if (engine != null) {
				engines.ungetService(engine);
				engine = null;
			}
		}
	}

	@Override
	public boolean canProvide(String operationName) {
		return current().canProvide(operationName);
	}

	@Override
	public boolean canProvide(String transformationName, String operationName) {
		return current().canProvide(transformationName, operationName);
	}

	@Override
	public RelationImplementationResult invoke(String operationName, List<Object> arguments,
			QvtdExecutionContext context) {
		return current().invoke(operationName, arguments, context);
	}

	@Override
	public RelationImplementationResult invoke(String transformationName, String operationName,
			List<Object> arguments, QvtdExecutionContext context) {
		return current().invoke(transformationName, operationName, arguments, context);
	}

	/**
	 * The engine with the bound units loaded — a fresh one after the units changed, as an engine
	 * keeps what it loaded. A call already running finishes on the engine it started with.
	 */
	private QvtoEngine current() {
		synchronized (lock) {
			if (stale || engine == null) {
				stale = false;
				QvtoEngine fresh = engines.getService();
				units.stream().sorted(ORDER).forEach(unit -> load(fresh, unit));
				if (engine != null) {
					engines.ungetService(engine);
				}
				engine = fresh;
			}
			return engine;
		}
	}

	private static void load(QvtoEngine engine, RankedUnit unit) {
		try {
			OperationalTransformation transformation = unit.resolver().resolveUnit(unit.name())
					.map(resolved -> transformation(engine, resolved)).orElse(null);
			if (transformation != null) {
				engine.loadTransformation(transformation);
			}
		} catch (RuntimeException notLoadable) {
			// Not served: the QVT-R run reports the operation as one nobody serves
		}
	}

	/** A unit as a transformation of the engine's own — a compiled one is lent, so it is copied. */
	private static OperationalTransformation transformation(QvtoEngine engine, QvtoUnit unit) {
		try {
			return switch (unit) {
				case QvtoUnit.SourceUnit source -> engine.parse(source.source(), source.qualifiedName());
				case QvtoUnit.ResourceUnit reference -> engine.parse(reference.uri());
				case QvtoUnit.CompiledUnit compiled -> EcoreUtil.copy(compiled.transformation());
			};
		} catch (QvtoParseException e) {
			throw new IllegalStateException(e);
		}
	}
}
