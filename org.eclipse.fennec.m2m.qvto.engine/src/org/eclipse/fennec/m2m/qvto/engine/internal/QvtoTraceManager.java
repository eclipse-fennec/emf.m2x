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
package org.eclipse.fennec.m2m.qvto.engine.internal;

import java.util.ArrayList;
import java.util.List;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EObject;

/**
 * Lightweight in-memory trace store for basic resolve support.
 *
 * <p>Records mapping invocations (source → result) and provides forward
 * and inverse resolution. Uses linear scan — deliberately simple.
 * Phase C will replace this with the full EMF trace model.
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
class QvtoTraceManager {

	private final List<QvtoTraceRecord> records = new ArrayList<>();

	/**
	 * Records a mapping invocation.
	 *
	 * @param mappingName the mapping name
	 * @param source the source object
	 * @param result the result object
	 */
	void addRecord(String mappingName, Object source, Object result) {
		records.add(new QvtoTraceRecord(mappingName, source, result));
	}

	/**
	 * Forward resolve: finds all result EObjects created from the given source.
	 *
	 * @param source the source object
	 * @param targetType the required target EClass (or {@code null} for any)
	 * @return matching result objects
	 */
	List<EObject> resolve(Object source, EClass targetType) {
		List<EObject> results = new ArrayList<>();
		for (QvtoTraceRecord record : records) {
			if (record.source() == source && record.result() instanceof EObject eo) {
				if (targetType == null || targetType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}

	/**
	 * Inverse resolve: finds all source EObjects that produced the given result.
	 *
	 * @param result the result object
	 * @param sourceType the required source EClass (or {@code null} for any)
	 * @return matching source objects
	 */
	List<EObject> invResolve(Object result, EClass sourceType) {
		List<EObject> results = new ArrayList<>();
		for (QvtoTraceRecord record : records) {
			if (record.result() == result && record.source() instanceof EObject eo) {
				if (sourceType == null || sourceType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}

	/**
	 * Forward resolve constrained to a specific mapping.
	 *
	 * @param mappingName the mapping name to match
	 * @param source the source object
	 * @param targetType the required target EClass (or {@code null} for any)
	 * @return matching result objects
	 */
	List<EObject> resolveIn(String mappingName, Object source, EClass targetType) {
		List<EObject> results = new ArrayList<>();
		for (QvtoTraceRecord record : records) {
			if (record.mappingName().equals(mappingName)
					&& record.source() == source
					&& record.result() instanceof EObject eo) {
				if (targetType == null || targetType.isInstance(eo)) {
					results.add(eo);
				}
			}
		}
		return results;
	}
}
