# Fennec OCL — Generic EMF Editor Support: Installation Guide

This guide explains how to install the **Fennec OCL** Eclipse integration and how
to make the generic (reflective) EMF editor evaluate OCL annotations — invariant
constraints, derived‑feature derivations and operation bodies — with the Fennec
OCL engine.

The integration is shipped as an **Eclipse p2 repository packaged into a single
JAR** (`org.eclipse.fennec.m2x.ocl.p2`). That JAR is published to Maven Central
(releases) and Central Snapshots and can be added to Eclipse directly as an
*archive* repository — no separate update site needs to be hosted.

---

## 1. Prerequisites

- **Eclipse** with EMF installed (any *Modeling* package, or a base Eclipse with
  the *EMF SDK* / *EMF — Eclipse Modeling Framework SDK* feature). Verified
  against **Eclipse 2026‑06**.
- **Java 21** runtime for Eclipse.
- That's it. EMF (`org.eclipse.emf.ecore`/`common`/`xmi`), the OSGi framework and
  Declarative Services (`org.apache.felix.scr`) are all part of such an Eclipse
  and are used as‑is — they are **not** bundled by this feature. The feature ships
  only the Fennec OCL bundles, the Fennec `emf.osgi` API and the ANTLR runtime.

---

## 2. Install from the update site (recommended)

The p2 repository is published as a live **GitHub Pages update site** — a stable
URL you simply add in Eclipse, with no download:

```
https://eclipse-fennec.github.io/emf.m2x/ocl/snapshot/p2/
```

In Eclipse: **Help → Install New Software… → Add… → Location:** paste the URL →
tick **Fennec OCL** → install → restart.

The path carries the branch/version: `…/ocl/<branch>/p2/`. Today the development
branch `snapshot` is published; once the first release is cut, a stable
`…/ocl/latest/p2/` alias (tracking `main`) is added — prefer that for production.

> This is the easiest option and supports normal *Check for Updates*. The Maven
> archive below (§3) is an alternative for offline/pinned installs.

---

## 3. Get the p2 archive (alternative)

The same repository is also a normal Maven artifact whose JAR *is* a p2 repository.

| | |
|---|---|
| **groupId** | `org.eclipse.fennec.m2x` |
| **artifactId** | `org.eclipse.fennec.m2x.ocl.p2` |
| **packaging** | `jar` (a self‑contained p2 repository) |

**Snapshot (Sonatype Central Snapshots)** — current development builds:

```
https://central.sonatype.com/repository/maven-snapshots/org/eclipse/fennec/m2x/org.eclipse.fennec.m2x.ocl.p2/0.1.1-SNAPSHOT/
```
Snapshot JARs are timestamped; the directory's `maven-metadata.xml` names the
latest one, e.g.:
```
.../0.1.1-SNAPSHOT/org.eclipse.fennec.m2x.ocl.p2-0.1.1-<timestamp>-<n>.jar
```
You can either download that JAR (§3, Method A) **or** point Eclipse straight at
it with the `jar:…!/` URL (§3, Method B) — that works for snapshots too, e.g.:
```
jar:https://central.sonatype.com/repository/maven-snapshots/org/eclipse/fennec/m2x/org.eclipse.fennec.m2x.ocl.p2/0.1.1-SNAPSHOT/org.eclipse.fennec.m2x.ocl.p2-0.1.1-<timestamp>-<n>.jar!/
```
Note the URL pins one timestamped build, so it doesn't auto‑update — use the
GitHub Pages update site (§2) if you want *Check for Updates*.

**Release (Maven Central)** — once a release is published:

```
https://repo1.maven.org/maven2/org/eclipse/fennec/m2x/org.eclipse.fennec.m2x.ocl.p2/<version>/org.eclipse.fennec.m2x.ocl.p2-<version>.jar
```
Releases have stable file names, so the direct `jar:…!/` URL (§3, Method B) works
without a download. Substitute `<version>` (e.g. `0.1.1`).

---

### Install the archive in Eclipse

#### Method A — local archive (works for releases *and* snapshots)

1. Download the `org.eclipse.fennec.m2x.ocl.p2-<version>.jar`.
2. In Eclipse: **Help → Install New Software… → Add… → Archive…**
3. Select the downloaded JAR. (The JAR is itself a p2 repository, so Eclipse reads
   `content.jar`/`artifacts.jar` straight from inside it.)
4. Tick the **Fennec OCL** category, **Next**, accept the EPL‑2.0 license, **Finish**.
5. Restart Eclipse when prompted.

#### Method B — direct `jar:` URL (releases *and* snapshots, no download)

In **Help → Install New Software… → Add…**, set *Location* to the JAR wrapped as a
p2 archive URL (note the leading `jar:` and trailing `!/`). Eclipse reads the p2
metadata straight from inside the remote JAR — no download needed.

Release (stable file name):
```
jar:https://repo1.maven.org/maven2/org/eclipse/fennec/m2x/org.eclipse.fennec.m2x.ocl.p2/<version>/org.eclipse.fennec.m2x.ocl.p2-<version>.jar!/
```
Snapshot (timestamped file name from `maven-metadata.xml`):
```
jar:https://central.sonatype.com/repository/maven-snapshots/org/eclipse/fennec/m2x/org.eclipse.fennec.m2x.ocl.p2/0.1.1-SNAPSHOT/org.eclipse.fennec.m2x.ocl.p2-0.1.1-<timestamp>-<n>.jar!/
```

Then proceed as in Method A from step 4.

> **Updating to a newer version:** Fennec OCL releases use a new version number
> each time, so a normal *Install/Update* picks them up cleanly. Snapshots all map
> to the same p2 version (`x.y.z.SNAPSHOT`), so when testing snapshots **uninstall
> the previously installed "Fennec OCL" first** (see §7) before re‑installing.

---

## 4. Enable OCL on your Ecore model

The generic editor only evaluates OCL where the model asks for it. Add the
delegate wiring to your `.ecore`:

**a) On the `EPackage`** — declare which delegate URI handles each delegate kind
(EAnnotation source `http://www.eclipse.org/emf/2002/Ecore`):

| detail key | value |
|---|---|
| `settingDelegates` | `http://www.eclipse.org/fennec/m2x/ocl/1.0` |
| `invocationDelegates` | `http://www.eclipse.org/fennec/m2x/ocl/1.0` |
| `validationDelegates` | `http://www.eclipse.org/fennec/m2x/ocl/1.0` |

**b) On the model elements** — put the OCL text in an EAnnotation whose **source is
the Fennec URI** `http://www.eclipse.org/fennec/m2x/ocl/1.0`:

| Where | detail key | example value |
|---|---|---|
| derived `EStructuralFeature` (set `derived`/`volatile`/`transient`) | `derivation` | `self.name.size()` |
| `EOperation` | `body` | `'Hello, '.concat(self.name)` |
| `EClass` (+ list the names in an `…/Ecore` annotation under key `constraints`) | `<constraintName>` | `self.age > 0` |

> **Legacy Eclipse OCL models work too.** The same delegates are also registered
> under the Eclipse OCL Pivot URI `http://www.eclipse.org/emf/2002/Ecore/OCL/Pivot`,
> so models already annotated for Eclipse OCL are evaluated by the Fennec engine
> without changes.

A ready‑to‑use example lives at `company.ecore` (and `company-eclipse-ocl.ecore`
for the Pivot URI variant) in the repository root.

---

## 5. Use it in the generic editor

1. Open your `.ecore`, right‑click an `EClass` → **Create Dynamic Instance…**, save
   the `.xmi`.
2. Open the `.xmi` in the **Sample Reflective Ecore Model Editor** (the default
   generic editor).
3. Set the stored attributes (e.g. `name`, `age`). In the **Properties** view the
   **derived** features (e.g. `nameLength`) now show their **computed** values.
4. **Validate**: right‑click the root object → **Validate**. Invariant violations
   are reported as diagnostics (e.g. set `age = -5` to trip `agePositive`).

Notes:
- Operation bodies are evaluated when the operation is invoked programmatically
  (`eInvoke`); the reflective tree editor has no UI to call operations directly.
- If a derivation evaluates to *invalid* (e.g. `self.name.size()` while `name` is
  `null`), the feature is shown **empty** — same behaviour as Eclipse OCL.

---

## 6. Verify the integration is active

In the **OSGi console** (or *Host OSGi Console*):

```
headers org.eclipse.fennec.m2x.ocl.ide
```

The `Bundle-SymbolicName` must read `org.eclipse.fennec.m2x.ocl.ide;singleton:=true`.
The bundle normally sits in state **STARTING** (it uses lazy activation) and only
turns **ACTIVE** the first time EMF asks it for a delegate (first derived‑feature
read or validation) — that is expected, not an error.

---

## 7. Troubleshooting

**"Cannot complete the install … requires 'org.eclipse.emf.ecore…' could not be
found".** Your target Eclipse has no EMF. Install an EMF/Modeling package or the
*EMF SDK* feature first, then retry.

**Nothing is evaluated in the editor (derived features blank, Validate does
nothing).**
- Confirm the bundle is present and *singleton* (see §6). The Eclipse extension
  registry only reads a bundle's `plugin.xml` if it is `singleton:=true`.
- Confirm your model carries the package‑level `settingDelegates` /
  `validationDelegates` / `invocationDelegates` annotations **and** the per‑element
  OCL annotations under the Fennec (or Pivot) URI (see §4).
- Check **Window → Show View → Error Log** for an exception thrown while a delegate
  is first used.

**A rebuilt snapshot doesn't take effect after re‑install.** Snapshot bundles all
share the p2 version `x.y.z.SNAPSHOT`, so p2 keeps the copy already in the shared
bundle pool. Fully **uninstall** the old "Fennec OCL" (Help → About → Installation
Details → *Installed Software* → Uninstall), restart with `-clean`, then install
the new archive. Released versions (distinct version numbers) do not have this
issue.

---

## 8. Uninstall

**Help → About Eclipse → Installation Details → Installed Software →** select
**Fennec OCL → Uninstall…**, then restart.

---

## Appendix — building the p2 archive yourself

From the bnd workspace (`workspace/`):

```bash
./gradlew :org.eclipse.fennec.m2x.ocl.ide:jar
./gradlew :org.eclipse.fennec.m2x.ocl.ide.p2:export --rerun-tasks
```

The repository JAR is written to
`org.eclipse.fennec.m2x.ocl.ide.p2/generated/org.eclipse.fennec.m2x.ocl.p2.jar`
and can be added to Eclipse via **Add… → Archive…** exactly like the published one.
