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
package org.eclipse.fennec.m2x.ocl.metadata;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicLong;

import org.eclipse.emf.ecore.EClass;
import org.eclipse.emf.ecore.EClassifier;
import org.eclipse.emf.ecore.EObject;
import org.eclipse.emf.ecore.EPackage;
import org.eclipse.fennec.emf.osgi.constants.EMFNamespaces;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryEntry;
import org.eclipse.fennec.emf.osgi.eobject.registry.EObjectRegistryWriter;
import org.eclipse.fennec.emf.osgi.fingerprint.FingerprintService;
import org.eclipse.fennec.m2x.model.ocl.OclExpression;
import org.eclipse.fennec.m2x.ocl.api.OclCacheKey;
import org.eclipse.fennec.m2x.ocl.api.OclExpressionCache;
import org.eclipse.fennec.m2x.ocl.api.OclParseResult;
import org.eclipse.fennec.m2x.ocl.api.OclParseResult.Parsed;

/**
 * Keeps compiled expressions in a named {@link EObjectRegistry}, anchored to the model
 * version they were compiled against.
 *
 * <p>An LRU inside the engine knows how many entries it may keep, but not what a model
 * version is: an expression compiled against a model long retired survives because it was
 * used recently, and one belonging to a live model is dropped because it was not. A registry
 * entry carries {@code emf.nsURI} and {@code emf.fingerprint}, which is what ties it to the
 * version it belongs to — and what lets the {@code RegistryMetadataBridge} put it onto the
 * tree of every live version, including one that registers later.
 *
 * <p><b>The registry is the read face here, not the aspect.</b> The metadata bridge files at
 * most one aspect per anchor class and type id, so a domain with many entries per class —
 * which every OCL context type is, since each expression is an entry of its own — keeps the
 * registry as its query face. That is the boundary the registry guide draws, and this cache
 * stays on the right side of it.
 *
 * <p><b>Two fingerprints, two jobs.</b> The key carries the <em>derived</em> fingerprint —
 * {@code fingerprint(ePackage, "ocl")}, the answer to "may this compiled artifact be reused?"
 * — so two versions of one nsURI never answer for each other, and a different OCL engine
 * version would not adopt this one's output. The {@code emf.fingerprint} property carries the
 * <em>plain</em> model fingerprint, because that is the join key the rest of the runtime uses:
 * the metadata service computes it for every version it registers, and the bridge compares it
 * against a tree's version before placing anything there. Mixing the two would leave every
 * entry naming a version that no tree has. Where a context type has no package to fingerprint
 * — a classifier of Ecore itself, or a type built at runtime — there is no version to anchor
 * to and the delegate takes it.
 *
 * <p><b>It also answers for its own entries.</b> {@link OclVersionedExpressions} makes the
 * context type of an entry askable, which is what an anchor resolver needs and what nothing
 * else can supply: the context type is not in the compiled expression, and asking a registry
 * of models fails exactly when it matters, while a version's tree is being built and is not
 * published yet.
 *
 * <p><b>Only executable ASTs go into the registry.</b> The registry is shared and read by
 * others, and what they find there must run as it is. Parse failures and parses with declared
 * variables (whose AST has free variables) stay in memory: the delegate keeps them under the
 * derived fingerprint, so a changed model is parsed again, and bounds failures with a limit of
 * their own, so a flood of broken expressions cannot displace anything. {@link #release} does
 * not reach them; they are bounded and age out of the delegate on their own.
 *
 * <p><b>Model mutation after registration is out of contract.</b> A fingerprint describes the
 * package as it was; changing it afterwards leaves entries that no longer describe it, and
 * nothing here tries to notice.
 *
 * @since 1.0
 */
public class RegistryExpressionCache implements OclExpressionCache, OclVersionedExpressions {

	/** Entry property naming the model an entry belongs to. */
	public static final String PROP_NS_URI = EMFNamespaces.EMF_MODEL_NSURI;

	/** Entry property naming the model <em>version</em> an entry belongs to. */
	public static final String PROP_FINGERPRINT = EMFNamespaces.EMF_MODEL_FINGERPRINT;

	/** Entry property naming the context type an entry's expression was compiled against. */
	public static final String PROP_CONTEXT_TYPE = "ocl.contextType";

	/** What this cache derives fingerprints for, so other artifacts of a model differ. */
	private static final String PURPOSE = "ocl";

	/** Technical origin of the entries, which is what {@code sync} is scoped by. */
	private static final String SOURCE = "ocl-compiled";

	private final EObjectRegistryWriter writer;
	private final EObjectRegistry registry;
	private final FingerprintService fingerprintService;
	private final OclExpressionCache delegate;
	private final AtomicLong hits = new AtomicLong();

	/**
	 * The context type per entry key — what this cache knows and no lookup could tell it in
	 * time, see {@link #anchorOf(String)}.
	 */
	private final Map<String, EClass> anchors = new ConcurrentHashMap<>();

	/**
	 * @param writer             the registry to file compiled expressions in, must not be
	 *                           {@code null}
	 * @param fingerprintService computes the model version an entry belongs to, must not be
	 *                           {@code null}
	 * @param delegate           takes what has no model version to anchor to, must not be
	 *                           {@code null}
	 */
	public RegistryExpressionCache(EObjectRegistryWriter writer,
			FingerprintService fingerprintService, OclExpressionCache delegate) {
		this.writer = Objects.requireNonNull(writer, "writer must not be null");
		this.registry = writer.getRegistry();
		this.fingerprintService =
				Objects.requireNonNull(fingerprintService, "fingerprintService must not be null");
		this.delegate = Objects.requireNonNull(delegate, "delegate must not be null");
	}

	@Override
	public OclExpression get(String expression, EClassifier contextType) {
		return lookup(OclCacheKey.of(expression, contextType))
				.filter(Parsed.class::isInstance)
				.map(result -> ((Parsed) result).expression())
				.orElse(null);
	}

	@Override
	public void put(String expression, EClassifier contextType, OclExpression parsed) {
		Objects.requireNonNull(parsed, "parsed must not be null");
		store(OclCacheKey.of(expression, contextType), new Parsed(parsed));
	}

	@Override
	public void invalidate(String expression, EClassifier contextType) {
		invalidate(OclCacheKey.of(expression, contextType));
	}

	@Override
	public Optional<OclParseResult> lookup(OclCacheKey key) {
		String derived = derivedFingerprintOf(key.contextType());
		if (derived == null) {
			return delegate.lookup(key);
		}
		if (!key.hasVariables()) {
			Optional<EObject> found = registry.get(key(derived, key.expression(), key.contextType()));
			if (found.isPresent() && found.get() instanceof OclExpression parsed) {
				hits.incrementAndGet();
				return Optional.of(new Parsed(parsed));
			}
		}
		// the delegate counts the miss, or the hit on a failure or a parse with variables
		return delegate.lookup(inMemory(derived, key));
	}

	@Override
	public void store(OclCacheKey key, OclParseResult result) {
		Objects.requireNonNull(result, "result must not be null");
		String derived = derivedFingerprintOf(key.contextType());
		if (derived == null) {
			delegate.store(key, result);
			return;
		}
		if (key.hasVariables() || !(result instanceof Parsed parsed)) {
			delegate.store(inMemory(derived, key), result);
			return;
		}
		delegate.invalidate(inMemory(derived, key));
		EClassifier contextType = key.contextType();
		String registryKey = key(derived, key.expression(), contextType);
		if (contextType instanceof EClass eClass) {
			// before the write, because filing the entry is what notifies a listener, and a
			// bridge asking for the anchor must not be told "unknown" for what it just saw
			anchors.put(registryKey, eClass);
		}
		writer.put(SOURCE, registryKey, parsed.expression(), propertiesOf(contextType));
	}

	@Override
	public void invalidate(OclCacheKey key) {
		String derived = derivedFingerprintOf(key.contextType());
		if (derived == null) {
			delegate.invalidate(key);
			return;
		}
		if (!key.hasVariables()) {
			String registryKey = key(derived, key.expression(), key.contextType());
			anchors.remove(registryKey);
			writer.remove(SOURCE, registryKey);
		}
		delegate.invalidate(inMemory(derived, key));
	}

	@Override
	public void invalidateAll() {
		// Only what this cache wrote: a registry is shared, and other sources' entries are
		// none of its business — which is what scoping by source is for.
		anchors.clear();
		writer.sync(SOURCE, List.of());
		delegate.invalidateAll();
	}

	@Override
	public Optional<EClass> anchorOf(String registryKey) {
		return registryKey == null ? Optional.empty() : Optional.ofNullable(anchors.get(registryKey));
	}

	@Override
	public int release(EPackage ePackage) {
		String modelFingerprint = ePackage == null ? null : fingerprintService.fingerprint(ePackage);
		if (modelFingerprint == null) {
			return 0;
		}
		List<String> keys = registry.entries().stream()
				.filter(entry -> SOURCE.equals(entry.source()))
				.filter(entry -> modelFingerprint.equals(entry.properties().get(PROP_FINGERPRINT)))
				.map(EObjectRegistryEntry::key)
				.toList();
		for (String key : keys) {
			anchors.remove(key);
			writer.remove(SOURCE, key);
		}
		return keys.size();
	}

	@Override
	public long size() {
		return registry.entries().stream().filter(e -> SOURCE.equals(e.source())).count()
				+ delegate.size();
	}

	@Override
	public long hitCount() {
		return hits.get() + delegate.hitCount();
	}

	@Override
	public long missCount() {
		return delegate.missCount();
	}

	/**
	 * The properties an entry carries: which model, which version of it, and which class
	 * inside it. The version is the <em>plain</em> model fingerprint, deliberately — it is the
	 * join key everything else in the runtime is keyed by, and the metadata bridge compares it
	 * against the version of a tree before placing anything on it.
	 */
	private Map<String, Object> propertiesOf(EClassifier contextType) {
		Map<String, Object> properties = new LinkedHashMap<>();
		properties.put(PROP_NS_URI, contextType.getEPackage().getNsURI());
		properties.put(PROP_FINGERPRINT, modelFingerprintOf(contextType));
		if (contextType.getName() != null) {
			properties.put(PROP_CONTEXT_TYPE, contextType.getName());
		}
		return properties;
	}

	/**
	 * The <em>derived</em> fingerprint the entries are keyed by: "can this compiled artifact
	 * be reused?", which is a different question from "which model version is this?" and must
	 * not share its answer. {@code null} when the context type has no package, and therefore
	 * no version to anchor to.
	 */
	private String derivedFingerprintOf(EClassifier contextType) {
		EPackage ePackage = contextType != null ? contextType.getEPackage() : null;
		return ePackage == null ? null : fingerprintService.fingerprint(ePackage, PURPOSE);
	}

	/**
	 * The <em>plain</em> model fingerprint — the identity of the model version itself, which
	 * is what the metadata service computes for its trees and what an entry names as the
	 * version it belongs to.
	 */
	private String modelFingerprintOf(EClassifier contextType) {
		EPackage ePackage = contextType != null ? contextType.getEPackage() : null;
		return ePackage == null ? null : fingerprintService.fingerprint(ePackage);
	}

	/**
	 * Registry keys are unique per registry and the convention is the domain's own. Ours is
	 * version, then context type, then expression — so that two versions of one model never
	 * answer for each other, and two classes of one model do not either.
	 */
	/**
	 * The key under which the delegate keeps what does not go into the registry: the derived
	 * fingerprint in front of the text, as the registry key has it.
	 */
	private static OclCacheKey inMemory(String derived, OclCacheKey key) {
		return key.withExpression(derived + "#" + key.expression());
	}

	private static String key(String fingerprint, String expression, EClassifier contextType) {
		return fingerprint + "#" + contextType.getName() + "#" + expression;
	}
}
