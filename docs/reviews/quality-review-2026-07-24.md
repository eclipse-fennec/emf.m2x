# Quality review — emf.m2x — 2026-07-24

Mode: quick · Scope: whole repo · Rule sets: SOLID/OSGi + Eclipse Foundation (see skill references)

## Summary

| Severity | api-hygiene | release-readiness | solid-dip / osgi-ds | docs / conventions | Total |
|----------|------------|-------------------|---------------------|--------------------|-------|
| blocker  | 1          | —                 | —                   | —                  | 1     |
| major    | 1          | 2                 | 2                   | —                  | 5     |
| minor    | 1          | —                 | 1                   | 3                  | 5     |
| info     | —          | 1                 | 2                   | —                  | 3     |
| **Total**| 3          | 3                 | 5                   | 3                  | 14    |

Overall the codebase is in very good shape for a young repository: every one of the 860 handwritten Java files carries a correct EPL-2.0/SPDX header, license enforcement runs in CI via the central reusable verify workflow, all exported packages carry `@Export`/`@Version` annotations, API javadoc is unusually thorough (contract, nullability, examples), and the OCL engine demonstrates a textbook DS design (prototype-scoped engine, whiteboard caches/operation providers, ConfigAdmin-driven configuration, paired `installDelegates()`/`uninstallDelegates()`). The findings cluster in two areas: (1) release-readiness plumbing that emf.osgi already solved (root documents, Dash/DEPENDENCIES) has not been copied over yet, and (2) the OSGi service story is uneven — the OCL engine is a first-class service while the QVT-O/QVT-R/M2T engines are plain-Java-only, and three bundles export `internal` packages nothing consumes.

## Findings

### F1 · blocker · api-hygiene · ocl.engine / qvtd.engine / qvto.engine
- **Where:** org.eclipse.fennec.m2x.ocl.engine/src/org/eclipse/fennec/m2x/ocl/engine/internal/package-info.java:16 (also qvtd.engine/.../engine/internal/package-info.java, qvto.engine/.../engine/internal/package-info.java)
- **What:** The `*.engine.internal` packages of the OCL, QVT-R, and QVT-O engine bundles are exported (`@org.osgi.annotation.bundle.Export` + `@Version`), although no other bundle in the workspace imports them — evaluators, linkers, trace managers, and the DS delegate factories all become versioned public API.
- **Why it matters:** Everything public in an exported package is API under Eclipse evolution rules. Once released, refactoring `OclEvaluator`, `QvtoLinker`, etc. requires semantic-version discipline on packages that were never meant to be consumed. The m2t.engine bundle already does this correctly (`Private-Package: org.eclipse.fennec.m2x.m2t.engine.internal` in its manifest) — the other three predate that pattern.
- **Suggested fix:** Remove the `@Export` and `@Version` annotations from the three `internal` package-infos (keep the `@Requirement` on the OCL one); bnd will make the packages private automatically, as it already does for m2t.engine.

### F2 · major · release-readiness · repo root
- **Where:** repo root (only `LICENSE` and `README.md` exist)
- **What:** Four of the six required Eclipse Foundation root documents are missing: `NOTICE.md`, `CONTRIBUTING.md`, `CODE_OF_CONDUCT.md`, `SECURITY.md`.
- **Why it matters:** These are release-blocking for an Eclipse project: ECA/DCO expectations, security-report routing, and IP notices are undefined for contributors, and the Eclipse release review checklist requires them.
- **Suggested fix:** Copy the four files from `eclipse-fennec/emf.osgi` and adapt (repo URL `eclipse-fennec/emf.m2x`, GitHub security-advisories URL for this repo, supported-versions table).

### F3 · major · release-readiness · repo root
- **Where:** repo root / `.github/workflows/` (no `DEPENDENCIES`, no `dash-licenses.yml`, no `tools/` directory)
- **What:** The Dash license-tool setup is completely absent: no `tools/dash-licenses.sh`, no Dash workflow, and no generated `DEPENDENCIES` file is committed.
- **Why it matters:** Third-party IP cleanliness (ANTLR runtime, ICU4J, EMF, etc.) is unverified; a release review cannot pass without a reviewed `DEPENDENCIES` file, and `restricted` entries would surface only at the worst moment.
- **Suggested fix:** Copy `tools/dash-licenses.sh`/`.bat` and `.github/workflows/dash-licenses.yml` from emf.osgi, generate `DEPENDENCIES` from the bnd workspace (`bnd repo deps`), review and commit it. Use the dotted PMI id (`technology.fennec`-style) for `--review`, not the GitHub org.

### F4 · major · osgi-ds · m2t.engine / qvto.engine / qvtd.engine
- **Where:** org.eclipse.fennec.m2x.m2t.engine/src/org/eclipse/fennec/m2x/m2t/engine/M2tEngineImpl.java:117 (representative; also QvtoEngineImpl, QvtdEngineImpl)
- **What:** Only the OCL engine is published as a DS service (`OclEngineComponent`). The M2T, QVT-O, and QVT-R engines have no component at all — OSGi consumers must import the exported impl package and call `new M2tEngineImpl(config)` — and `M2tEngineImpl` internally hard-wires `new OclEngineImpl(...)` instead of accepting an `OclEngine`, bypassing the configured expression cache and operation-provider whiteboard of the OSGi runtime. qvto.engine and qvtd.engine even declare `org.osgi.service.component.annotations` on their buildpath without containing a single component.
- **Why it matters:** The README promises "optional OSGi support via Declarative Services" for all engines, and the api bundles define clean service interfaces (`M2tEngine`, `QvtoEngine`, `QvtdEngine`) — but in an OSGi runtime none of them is discoverable, substitutable, or ConfigAdmin-configurable the way `OclEngine` is. Consumers `new`-ing impls across bundle boundaries is the dependency-inversion violation the ocl.engine design explicitly avoids.
- **Suggested fix:** Add prototype-scoped components mirroring `OclEngineComponent` (e.g. `M2tEngineComponent` that `@Reference`s `OclEngineImpl`/`OclExpressionParser`), following the existing OCL pattern. Until then, drop the unused component-annotations buildpath entries.

### F5 · major · solid-dip · qvto.engine / qvtd.engine
- **Where:** org.eclipse.fennec.m2x.qvto.engine/src/org/eclipse/fennec/m2x/qvto/engine/QvtoEngineImpl.java:122 (also :152, QvtdEngineImpl.java:100, internal/QvtoOperationProvider.java:636, internal/QvtoAliasRegistry.java:109)
- **What:** Metamodel resolution during parse and link is hardwired to the global `EPackage.Registry.INSTANCE`; `QvtoConfiguration`/`QvtdConfiguration` offer no package-registry injection point (only a blackbox registry).
- **Why it matters:** In an OSGi/emf.osgi runtime, EPackages typically live in scoped registries/ResourceSets, not the global singleton — transformations referencing such models will fail to resolve, and tests cannot run against isolated registries. The workspace rules treat global-registry coupling in new code as a finding (the mirroring idiom is a legacy exception, not a template).
- **Suggested fix:** Add an optional `EPackage.Registry` to `QvtoConfiguration`/`QvtdConfiguration` (builder method, defaulting to `EPackage.Registry.INSTANCE` for standalone use) and thread it through `parse(...)` and `QvtoLinker`.

### F6 · major · api-hygiene · ocl.engine
- **Where:** org.eclipse.fennec.m2x.ocl.engine/src/org/eclipse/fennec/m2x/ocl/engine/OclEngineComponent.java:70 (also DefaultOclExpressionCacheComponent.java:46, NoOpOclOperationProvider.java)
- **What:** Three DS lifecycle classes live in the exported `org.eclipse.fennec.m2x.ocl.engine` package. The package is deliberately public (the `new OclEngineImpl(config)` plain-Java entry point is documented API), but the components ride along as exported, subclassable API.
- **Why it matters:** DS components are wiring, not API — clients can compile against `OclEngineComponent`, subclass it, or instantiate it outside DS, and every future change to component constructors becomes an API-evolution question on a released package.
- **Suggested fix:** Move `OclEngineComponent`, `DefaultOclExpressionCacheComponent`, and `NoOpOclOperationProvider` into the (per F1, no-longer-exported) `ocl.engine.internal` package; DS registration does not require the package to be exported. `OclParserSupport` (ocl.parser) is the same hybrid, but its plain-Java constructor is the documented parser entry point — if it stays public, document that the DS annotations are not API.

### F7 · minor · docs / conventions · repo root
- **Where:** README.md:1 (title), :49 (`cd workspace/`), :43 (module table), :100–106
- **What:** The README is stale and misses required content: title says "Fennec M2M" for repo `emf.m2x`; build instructions start with `cd workspace/` (no such directory); the module table lists `m2t.engine` as "Planned" and `m2t.parser` as "In Progress" although both are implemented; branch model (snapshot → main) and Maven coordinates (`org.eclipse.fennec.m2x`) are not stated.
- **Why it matters:** The Eclipse handbook expects the README to state the branch model and consumption coordinates; stale status tables mislead adopters.
- **Suggested fix:** Refresh the module table, fix the build section, and add "Branching & Releases" and "Maven coordinates" sections modeled on emf.osgi's README.
- Incidental (out of quick-scope severity): README.md:106 declares "Data In Motion Consulting GmbH - All rights reserved", which contradicts the EPL-2.0 licensing statement two lines above — see F8.

### F8 · minor · docs / conventions · repo root / cnf
- **Where:** cnf/cache/7.2.1/expanded/urn%resource%org.eclipse.fennec.bnd.library-0.0.9/library/workspace.bnd:7 (upstream default), visible in every built manifest (e.g. ocl.engine jar: `Bundle-Copyright: Data In Motion Consulting GmbH - All rights reserved`)
- **What:** All shipped manifests carry `Bundle-Copyright: … All rights reserved` (inherited from the shared `org.eclipse.fennec.bnd.library` 0.0.9) alongside `Bundle-License: Eclipse Public License 2.0` and `Bundle-Vendor: Eclipse Foundation`.
- **Why it matters:** "All rights reserved" by a single company contradicts EPL-2.0 terms and Eclipse Foundation branding on released artifacts.
- **Suggested fix:** Override `Bundle-Copyright` in `cnf/build.bnd` (e.g. "Copyright (c) Contributors to the Eclipse Foundation") and file an issue against the upstream fennec bnd library; fix README.md:106 in the same change.

### F9 · minor · osgi-ds · m2t.engine
- **Where:** org.eclipse.fennec.m2x.m2t.engine/src/org/eclipse/fennec/m2x/m2t/engine/M2tEngineImpl.java:77–90
- **What:** `parseResultCache`, `linkedModules`, `normalizedModules`, and `globalIndentationMap` are identity-keyed maps that only ever grow — nothing evicts entries, and parsed `Module`s are held strongly for the engine's lifetime.
- **Why it matters:** The class is documented as thread-safe library code; a long-lived instance (and any future DS component per F4 would be exactly that) accumulates every module ever parsed. This is the known "leaking registry" bug class in this workspace.
- **Suggested fix:** Key the caches weakly (identity-weak map) or add an explicit `release(Module)`/`clear()` API and document the retention contract on `M2tEngine`.

### F10 · minor · conventions · ocl.example.model
- **Where:** org.eclipse.fennec.m2x.ocl.example.model/bnd.bnd:21 (`Bundle-Version: 1.0.0.SNAPSHOT`)
- **What:** The example-model bundle pins `Bundle-Version: 1.0.0.SNAPSHOT`, overriding the workspace-wide `${base-version}.SNAPSHOT` (0.1.1) from cnf/build.bnd.
- **Why it matters:** A release would publish this artifact as 1.0.0 while every sibling is 0.1.x — confusing Maven coordinates and a baselining anomaly later.
- **Suggested fix:** Delete the `Bundle-Version` line so the workspace default applies (or exclude the example bundle from release publication if that is the intent).

### F11 · minor · conventions · ocl.benchmark / qvto.benchmark
- **Where:** org.eclipse.fennec.m2x.qvto.benchmark/README.md:8–13 and bnd.bnd `-testpath` (`lib/*.jar;version=file`); same pattern in ocl.benchmark
- **What:** Benchmark test dependencies must be hand-provisioned as symlinks in `lib/`, pointing at a machine-specific location (`/opt/git/m2m/tools/`), before the bundles build.
- **Why it matters:** The build is not reproducible from a clean checkout; every new contributor (and any CI leg that compiles the benchmarks) needs undocumented local state outside the repository.
- **Suggested fix:** Pull the Eclipse OCL/QVT-O comparison jars from a repository instead — most are on Maven Central (`org.eclipse.platform`, `org.eclipse.emf` group ids) and could be regular `-testpath` entries via `cnf/central.mvn`.

### F12 · info · release-readiness · cnf
- **Where:** cnf/build.bnd:13 (`#fennec-baselining: true`)
- **What:** Baselining is present but commented out. No release exists on Maven Central yet, so this is not yet a violation.
- **Why it matters:** From the first release onward, the API-evolution rules (§5 of the guidelines) are unenforced without it.
- **Suggested fix:** Enable `fennec-baselining: true` in the same PR that performs the first release, and publish the release OBR (orphan `release-obr` branch) from the same release run, per the fennec release guide.

### F13 · info · solid-dip · ocl.ide
- **Where:** org.eclipse.fennec.m2x.ocl.ide/src/org/eclipse/fennec/m2x/ocl/ide/SharedOclEngine.java:38
- **What:** Process-wide static singleton engine shared by the extension-registry delegates.
- **Why it matters (context):** Normally a DIP finding, but this bundle is the one deliberately platform-coupled bundle (design decision D41): the Eclipse extension registry instantiates delegates via no-arg constructors, so DS injection is unavailable. The class is package-private, double-checked-locking correct, and well documented. No action needed; recorded so future reviews don't re-litigate it.
- **Suggested fix:** None.

### F14 · info · solid-dip · ocl.engine
- **Where:** org.eclipse.fennec.m2x.ocl.engine/src/org/eclipse/fennec/m2x/ocl/engine/internal/OclSettingDelegateFactory.java:67 (same in the other four delegate factories)
- **What:** The delegate factories `@Reference` the concrete `OclEngineImpl` (which `OclEngineComponent` additionally registers as a service type) rather than the `OclEngine` API, because they need impl-only methods (`parse`, `getDelegateOptions`).
- **Why it matters:** Same-bundle wiring, so no cross-bundle coupling — but publishing the impl class as a service type widens the exported surface and is worth revisiting if those methods could be promoted to an internal role interface.
- **Suggested fix:** Optional: introduce an internal `OclDelegateSupport` role interface implemented by `OclEngineImpl` and register/reference that instead.

## Systemic issues

- **Uneven OSGi service layer across sibling engines (F4):** ocl.engine is the reference design (component + whiteboard + ConfigAdmin); m2t/qvto/qvtd engines are plain-Java-only. One pattern, three bundles to align.
- **Exported `internal` packages (F1):** three of four engine bundles; m2t.engine shows the correct pattern.
- **Release-readiness gaps (F2, F3, F12):** all resolvable by copying emf.osgi's root documents, Dash setup, and baselining switch.

## Skipped / not reviewed

- **EMF-generated model bundles** (m2t.model, ocl.model, ocl.example.model, qvt.model, qvtd.model, qvto.model): structural review suppressed per fennec idioms; header/export checks ran (exports of `impl`/`util` packages are the EMF convention — not flagged). Generated `configuration` companion components skimmed only.
- **ANTLR-generated parser code** (`src-gen/` in all four parser bundles, including the four modified files in the working tree): generated, excluded. Handwritten parser support classes were read.
- **Test bundles** (`*.tests`, ocl.ide.tests): excluded from SOLID checks; header check passed globally.
- **Skimmed only:** ocl.ide.p2, bom, library.project, library.workspace (resource-only bundles — bnd.bnd read, no Java), benchmark sources (bnd.bnd + README only), OclEvaluator/OclStdlib internals (structure sanity-checked at ~1.4k lines each; interpreter-style type dispatch is appropriate there, not an OCP violation), docs-site.
- **Full reads:** all 31 bnd.bnd files, all 53 package-info files, ocl.engine components and one delegate factory, M2tEngineImpl, QvtoEngineImpl (parse/execute), QvtdEngineImpl (listing + parse), ocl.ide (SharedOclEngine, _plugin.xml), ocl.api/qvto.api/qvtd.api/m2t.api listings with spot reads, repo root documents, all workflows, cnf/build.bnd, .licenserc.yaml.
