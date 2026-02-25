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

import java.util.concurrent.CompletableFuture;

import org.eclipse.fennec.m2m.model.qvtoperational.QvtOperationalFactory;
import org.eclipse.fennec.m2m.model.qvtoperational.Status;

/**
 * Factory and utility methods for QVT-O {@link Status} objects (§8.3.6).
 *
 * @author Data In Motion Consulting
 * @since 1.0
 */
final class QvtoStatusHelper {

	private QvtoStatusHelper() {
		// utility class
	}

	/**
	 * Creates a success status.
	 */
	static Status success() {
		Status s = QvtOperationalFactory.eINSTANCE.createStatus();
		s.setSucceeded(true);
		s.setFailed(false);
		return s;
	}

	/**
	 * Creates a failure status with the given exception.
	 */
	static Status failed(Exception e) {
		Status s = QvtOperationalFactory.eINSTANCE.createStatus();
		s.setSucceeded(false);
		s.setFailed(true);
		s.setRaisedException(e);
		return s;
	}

	/**
	 * Creates a pending status backed by a {@link CompletableFuture}.
	 * Call {@link #await(Status)} to block until the real status is available.
	 */
	static PendingStatus pending(CompletableFuture<Status> future) {
		return new PendingStatus(future);
	}

	/**
	 * Blocks until the given status is resolved (if it is a {@link PendingStatus}).
	 * Returns the resolved status, or the input status if already resolved.
	 */
	static Status await(Status status) {
		if (status instanceof PendingStatus ps) {
			return ps.join();
		}
		return status;
	}

	/**
	 * A Status subtype that wraps a CompletableFuture for async execution.
	 * Calling {@link #join()} blocks until the real status is available.
	 */
	static final class PendingStatus extends org.eclipse.fennec.m2m.model.qvtoperational.impl.StatusImpl {

		private final CompletableFuture<Status> future;

		PendingStatus(CompletableFuture<Status> future) {
			this.future = future;
		}

		Status join() {
			return future.join();
		}

		@Override
		public boolean isSucceeded() {
			return join().isSucceeded();
		}

		@Override
		public boolean isFailed() {
			return join().isFailed();
		}

		@Override
		public Exception getRaisedException() {
			return join().getRaisedException();
		}
	}
}
