# Fennec M2M Development Guideline

> This document covers core conventions, coding standards, testing strategy, architecture,
> and roadmap (sections 1-14).
> See also: [OCL Architecture](ocl-architecture.md), [QVT-O Architecture](qvto-architecture.md),
> [Design Decisions](design-decisions.md) (sections 16-20), [QVT-O Test Plan](qvto-test-plan.md)

## 1. Project Overview

Lightweight, specification-compliant implementations of **OCL v2.5** (backward compatible with v2.4), **QVT-Operational v1.3**, **QVT-Declarative v1.3** (Relations + Core), and **MOFM2T v1.0** (with OCL + practical extensions), fully decoupled from the Eclipse platform ecosystem.

- **GroupId:** `org.eclipse.fennec.m2x`
- **GitHub:** `eclipse-fennec / emf.m2m`
- **Workspace:** `/opt/git/m2m/workspace/` (bnd workspace)
- **Java version:** 21
- **Build:** bnd 7.2.1+ with Gradle wrapper
- **License:** EPL-2.0

### 1.1 Specifications

| Language | Spec Version | Document |
|----------|-------------|----------|
| OCL | v2.4 (formal) | `docs/specifications/OCL/OCL-formal-14-02-03.pdf` |
| OCL | v2.5 (Eclipse impl) | `docs/specifications/OCL/ocl.pdf` (Eclipse OCL 6.19.0 Documentation, covers 2.5 standard library + extensions) |
| QVT-O (Operational) | v1.3 | `docs/specifications/QVT/QVT-formal-16-06-03.pdf` |
| QVT-R (Relations) | v1.3 | `docs/specifications/QVT/QVT-formal-16-06-03.pdf` (same spec) |
| QVT-C (Core) | v1.3 | `docs/specifications/QVT/QVT-formal-16-06-03.pdf` (same spec) |
| MOFM2T | v1.0 | `docs/specifications/MOFM2T/MOFM2T-formal-08-01-16.pdf` |

---

## 2. Workspace Conventions

### 2.1 bnd Workspace

The workspace uses the `biz.aQute.bnd.workspace` Gradle plugin (see `settings.gradle`). Global config is in `cnf/build.bnd`.

**Active libraries** (from `cnf/build.bnd`):
```
-library: fennec, fennecTest, fennecJacoco, fennecEMF
```

- `fennec` - from `/opt/git/fennec.bnd.libraries` (GitHub config, baseline, release)
- `fennecTest` - from `/opt/git/fennec.bnd.libraries` (JUnit 5, Mockito, AssertJ test support)
- `fennecJacoco` - from `/opt/git/fennec.bnd.libraries` (code coverage)
- `fennecEMF` - from `emf.osgi` repository in `/opt/git/m2m/repositories/emf.osgi/` (EMF OSGi code generation)

**Repositories** (`cnf/build.bnd`):
- `Central` - Maven Central
- `Temp` / `Local` / `Templates` - local indexed repos
- `EMFCompare` - P2 repository
- `libraries` - additional Maven repos (via `cnf/ext/libraries.bnd`)

### 2.2 Bundle Layout

Every bundle follows this structure:

```
org.eclipse.fennec.m2x.<name>/
├── bnd.bnd                    # Bundle manifest & build config
├── .project                   # Eclipse project (javabuilder + bndbuilder)
├── .classpath                 # Eclipse classpath (JavaSE-21, bnd container, src folders)
├── .settings/
│   └── org.eclipse.jdt.core.prefs
├── src/                       # Hand-written Java source
│   └── org/eclipse/fennec/m2x/<name>/
├── src-gen/                   # EMF-generated source (if model project)
│   └── org/eclipse/fennec/m2x/<name>/
├── test/                      # Test sources (test projects)
│   └── org/eclipse/fennec/m2x/<name>/
└── model/                     # Ecore + genmodel files (if model project)
```

### 2.3 `.project` Template

```xml
<?xml version="1.0" encoding="UTF-8"?>
<projectDescription>
	<name>org.eclipse.fennec.m2x.PROJECTNAME</name>
	<comment></comment>
	<projects>
	</projects>
	<buildSpec>
		<buildCommand>
			<name>org.eclipse.jdt.core.javabuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
		<buildCommand>
			<name>bndtools.core.bndbuilder</name>
			<arguments>
			</arguments>
		</buildCommand>
	</buildSpec>
	<natures>
		<nature>org.eclipse.jdt.core.javanature</nature>
		<nature>bndtools.core.bndnature</nature>
	</natures>
</projectDescription>
```

### 2.4 `.classpath` Template

**Code project (no src-gen):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-21">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="aQute.bnd.classpath.container"/>
	<classpathentry kind="src" output="bin" path="src"/>
	<classpathentry kind="output" path="bin"/>
</classpath>
```

**Model project (with src-gen):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-21">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="aQute.bnd.classpath.container"/>
	<classpathentry kind="src" output="bin" path="src"/>
	<classpathentry kind="src" path="src-gen"/>
	<classpathentry kind="output" path="bin"/>
</classpath>
```

**Test project (with test folder):**
```xml
<?xml version="1.0" encoding="UTF-8"?>
<classpath>
	<classpathentry kind="con" path="org.eclipse.jdt.launching.JRE_CONTAINER/org.eclipse.jdt.internal.debug.ui.launcher.StandardVMType/JavaSE-21">
		<attributes>
			<attribute name="module" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="con" path="aQute.bnd.classpath.container"/>
	<classpathentry kind="src" output="bin" path="src"/>
	<classpathentry kind="src" output="bin_test" path="test">
		<attributes>
			<attribute name="test" value="true"/>
		</attributes>
	</classpathentry>
	<classpathentry kind="output" path="bin"/>
</classpath>
```

### 2.5 `bnd.bnd` Patterns

**Pure code bundle:**
```bnd
-library: enableEMF

-buildpath: \
	org.osgi.service.condition;version=latest
```

**Model bundle (with EMF code generation):**
```bnd
-library: enableEMF

src=${^src},src-gen

-generate:\
	model/MODELNAME.genmodel;\
		generate=fennecEMF;\
		genmodel=model/MODELNAME.genmodel;\
		output=src-gen

-includeresource.model: model=model

-buildpath: \
	org.osgi.service.condition;version=latest
```

**Test bundle:**
```bnd
-library: enableEMF

-buildpath: \
	org.osgi.service.condition;version=latest,\
	org.eclipse.fennec.m2x.DEPENDENCY;version=latest
```

---

## 3. License Header

### 3.1 Java Source Files

All hand-written Java source files must start with this license header:

```java
/********************************************************************
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
 ********************************************************************/
```

### 3.2 Ecore GenModel Annotation (copyrightText)

The same copyright text is embedded in ecore files via GenModel annotation, so EMF-generated code also carries the license:

```xml
<eAnnotations source="http://www.eclipse.org/emf/2002/GenModel">
    <details key="copyrightText" value="******************************************************************&#xA;Copyright (c) 2026 Contributors to the Eclipse Foundation.&#xA;&#xA;This program and the accompanying materials are made&#xA;available under the terms of the Eclipse Public License 2.0&#xA;which is available at https://www.eclipse.org/legal/epl-2.0/&#xA;&#xA;SPDX-License-Identifier: EPL-2.0&#xA;&#xA;Contributors:&#xA;  Data In Motion Consulting - initial implementation&#xA;******************************************************************"/>
</eAnnotations>
```

---

## 4. EMF Model Conventions

### 4.1 When to Use an EMF Model vs Plain Java

| Use Case | Approach |
|----------|----------|
| Metamodel that needs XMI serialization | EMF Ecore model with generated code |
| API types exposed to consumers | EMF Ecore model (de-coupled in own project if needed) |
| Internal AST for parser/evaluator | Plain Java (sealed interfaces + records) |
| Configuration/trace models | EMF Ecore model if persistence needed |

### 4.2 Ecore Files

- **GenModel annotations in the ecore file itself** - do not create genmodel files by hand; let EMF generate the `.genmodel` from the `.ecore` with its embedded annotations
- Required GenModel annotations on the root `EPackage`:
  ```
  complianceLevel = 17.0
  oSGiCompatible = true
  basePackage = org.eclipse.fennec.m2x.<component>
  resource = XMI
  copyrightText = (see section 3.2)
  ```
- Use `@GenModel(documentation="...")` annotations on EClasses and EStructuralFeatures for Javadoc in generated code
- All ecore files go in a `model/` folder within the project

### 4.3 Code Generation

- **Pure model project** (model-only, no hand-written companion code): generate into `src/`. No `src-gen/` folder needed; `.classpath` has only `src/` as source folder. The `bnd.bnd` uses `output=src` in the `-generate:` instruction.
- **Mixed project** (model + hand-written code): generate into `src-gen/`. The `src-gen/` folder is listed in `bnd.bnd` via `src=${^src},src-gen` and the `.classpath` must include `src-gen` as a source folder.
- Generation is triggered by the `-generate:` instruction in `bnd.bnd` using `fennecEMF`
- Generated folders (`src-gen/` or `src/` in pure model projects) are generated artifacts - do not hand-edit files inside them

### 4.4 Model vs Code Separation

If the EMF model is **de-coupled** from model-specific logic (services, implementations), it may go into its **own project**:
- Example: `/opt/git/emf.codec/org.eclipse.fennec.model.metadata/` has `src/` (hand-written services) + `src-gen/` (generated model) + `src-gen-api/` (generated API model) all in one project

If the model is tightly coupled to a single consumer, it can live in the same project with `src/` and `src-gen/` side by side.

---

## 5. Project Structure

### 5.1 Module Overview

```
workspace/
├── cnf/                                    # bnd workspace config (exists)
├── org.eclipse.fennec.m2x.model/           # Test model (exists)
├── org.eclipse.fennec.m2x.code/            # Placeholder (exists)
│
│   ── OCL ──
├── org.eclipse.fennec.m2x.ocl.model/       # OCL EMF metamodel (Ecore-based AST)
├── org.eclipse.fennec.m2x.ocl.api/        # OCL public API interfaces
├── org.eclipse.fennec.m2x.ocl.parser/     # OCL parser (ANTLR4)
├── org.eclipse.fennec.m2x.ocl.engine/     # OCL evaluator/interpreter
├── org.eclipse.fennec.m2x.ocl.tests/      # OCL tests
│
│   ── QVT-O (Operational) ──
├── org.eclipse.fennec.m2x.qvto.model/     # QVT-O EMF metamodel (imperative constructs, trace model)
├── org.eclipse.fennec.m2x.qvto.api/      # QVT-O public API interfaces
├── org.eclipse.fennec.m2x.qvto.parser/   # QVT-O parser
├── org.eclipse.fennec.m2x.qvto.engine/   # QVT-O transformation engine (imperative interpreter)
├── org.eclipse.fennec.m2x.qvto.tests/    # QVT-O tests
│
│   ── QVT-D (Declarative: Relations + Core) ──
├── org.eclipse.fennec.m2x.qvtd.model/    # QVT-R/QVT-C EMF metamodel (relations, domains, keys, patterns)
├── org.eclipse.fennec.m2x.qvtd.api/     # QVT-D public API interfaces
├── org.eclipse.fennec.m2x.qvtd.parser/  # QVT-R parser (QVT-C as compilation target)
├── org.eclipse.fennec.m2x.qvtd.engine/  # QVT-D transformation engine (relation matching, enforcement)
├── org.eclipse.fennec.m2x.qvtd.tests/   # QVT-D tests
│
│   ── M2T ──
├── org.eclipse.fennec.m2x.m2t.model/      # MOFM2T EMF template model
├── org.eclipse.fennec.m2x.m2t.api/       # MOFM2T public API interfaces
├── org.eclipse.fennec.m2x.m2t.parser/    # Template parser
├── org.eclipse.fennec.m2x.m2t.engine/    # Template evaluation engine
└── org.eclipse.fennec.m2x.m2t.tests/     # M2T tests
```

**Note:** The groupId is `org.eclipse.fennec.m2x`, so all bundle names follow the pattern `org.eclipse.fennec.m2x.<component>.<layer>`.

### 5.2 Module Dependency Rules

```
                ┌──────────┐
                │  .model   │  ← EMF metamodel (Ecore), generated code in src-gen
                └────┬─────┘
                     │
                ┌────┴────┐
                │  .api    │  ← Public API interfaces (depends on .model)
                └────┬────┘
                     │
           ┌─────────┼─────────┐
           ▼                   ▼
    ┌──────────┐        ┌──────────┐
    │ .parser  │        │ .engine  │
    └──────────┘        └──────────┘
           │                   │
           └─────────┬─────────┘
                     ▼
              ┌──────────┐
              │  .tests  │
              └──────────┘
```

**Strict rules:**
- `.model` depends only on EMF Ecore (provided by `enableEMF` library)
- `.api` depends on `.model` only
- `.parser` depends on `.api` and `.model`
- `.engine` depends on `.api` and `.model` (NOT on `.parser`)
- `.tests` may depend on all above

**Cross-component dependencies (permitted):**
- `qvto.model` depends on `ocl.model` (QVT-O extends OCL expression types with imperative constructs)
- `qvto.engine` depends on `ocl.engine` (delegates OCL expression evaluation)
- `qvtd.model` depends on `ocl.model` (QVT-R/C uses OCL expressions in when/where clauses and patterns)
- `qvtd.engine` depends on `ocl.engine` (delegates OCL expression evaluation)
- `m2t.model` depends on `ocl.model` (templates embed OCL expressions)
- `m2t.engine` depends on `ocl.engine` (delegates OCL expression evaluation)

---

## 6. Java Coding Conventions

### 6.1 General

- **Java 21** source and target
- **Use imports** - never use fully-qualified class names in code
- **No star imports** - import each class explicitly
- **Javadoc** on public API classes with `@author` and `@since` tags
- **Package-private by default** - only `public` what is part of the API

### 6.2 Class Header Template

```java
/********************************************************************
 * Copyright (c) 2026 Contributors to the Eclipse Foundation.
 * ...
 ********************************************************************/
package org.eclipse.fennec.m2x.ocl.api;

/**
 * Brief description of the class.
 *
 * @author mark
 * @since 18.02.2026
 */
public class MyClass {
}
```

### 6.3 Hand-Written AST Types (where EMF model is not used)

For internal/parser-only types that don't need XMI serialization, use Java 21 sealed interfaces + records:

```java
public sealed interface OclExpression
    permits LiteralExp, VariableExp, CallExp, IfExp, LetExp, TypeExp {
}

public record IntegerLiteralExp(long value) implements LiteralExp {}
```

### 6.4 Visitor Pattern

Where applicable, provide visitor interfaces for AST traversal:

```java
public interface OclExpressionVisitor<T> {
    T visit(IntegerLiteralExp exp);
    T visit(StringLiteralExp exp);
    // ... one method per concrete expression type
}
```

---

## 7. Parser Approach (ANTLR4)

### 7.1 Why ANTLR4

- **Proven in our ecosystem** - Eclipse Acceleo already uses ANTLR4 (`Query.g4`) for expression parsing in an OSGi/EMF context
- **Best-in-class error recovery** - automatic single-token insertion/deletion, sync-and-consume recovery, custom error listeners
- **Largest community** - extensive documentation, tooling (ANTLRWorks, IntelliJ/VS Code plugins), thousands of grammar examples
- **Mature and maintained** - actively developed by Terence Parr, regular releases
- **Grammar composition** via `import` - OCL base grammar can be extended for QVT-O and MOFM2T
- **Visitor and listener patterns** auto-generated from grammar
- **Runtime dependency:** `antlr4-runtime` (~318KB, zero transitive deps, already OSGi-compatible)
- **License:** 3-Clause BSD, compatible with EPL-2.0

### 7.2 Runtime and Tool Dependencies

The ANTLR4 **runtime** is an OSGi bundle and used at compile time. The ANTLR4 **tool** (code generator) and its transitive dependencies are needed only at build time. Add to `cnf/central.mvn`:

```
org.antlr:antlr4-runtime:4.13.2
org.antlr:antlr4:4.13.2
org.antlr:antlr-runtime:3.5.3
org.abego.treelayout:org.abego.treelayout.core:1.0.3
com.ibm.icu:icu4j:72.1
```

**Note:** Non-OSGi jars (like the ANTLR4 tool) can be resolved in bnd macros using Maven GAV syntax: `${repo;groupId:artifactId;version}` (colon-separated). This works for any jar in the bnd repository, regardless of whether it has an OSGi Bundle-SymbolicName.

### 7.3 OCL Parser Architecture

For OCL-specific parser details (bnd `-generate:` configuration, grammar organization,
two-phase parsing, operator precedence, error handling), see
[OCL Architecture §5](ocl-architecture.md#5-parser-architecture-oclparser).

---

## 8. Engine / Evaluator Design

For OCL engine architecture (evaluator, standard library, iterators, configurable evaluation,
thread safety, EMF integration), see
[OCL Architecture §6](ocl-architecture.md#6-engine-architecture-oclengine).

---

## 9. Public API Design

### 9.1 OCL Engine API

For the full OCL API design (OclEngine, OclConfiguration, OclContext, OclResult,
OclEvaluationOptions, extension interfaces, expression cache), see
[OCL Architecture §4](ocl-architecture.md#4-api-design-oclapi).

### 9.2 QVT-O Engine API

```java
public interface QvtoEngine {
    QvtoTransformation compile(String source);
    TransformationResult execute(QvtoTransformation transformation,
                                  Map<String, Resource> modelExtents);
}
```

### 9.3 QVT-D Engine API

```java
public interface QvtdEngine {
    QvtdTransformation compile(String source);
    TransformationResult execute(QvtdTransformation transformation,
                                  Map<String, Resource> modelExtents);
    boolean checkOnly(QvtdTransformation transformation,
                       Map<String, Resource> modelExtents);
}
```

### 9.4 M2T Engine API

```java
public interface M2tEngine {
    M2tModule parse(String templateSource);
    GenerationResult generate(M2tModule module, EObject modelRoot, Path outputDirectory);
    GenerationResult generate(M2tModule module, EObject modelRoot, Path outputDirectory,
                               M2tGenerationOptions options);
}
```

The M2T engine uses the OCL engine internally with `OclEvaluationOptions.lenient()` by default (null→empty string, errors collected but generation continues). This can be overridden via `M2tGenerationOptions`.

---

*See also:*
- [OCL Architecture](ocl-architecture.md) - consolidated OCL implementation reference (Phase 1)
- [QVT-O Architecture](qvto-architecture.md) - consolidated QVT-O implementation reference (Phase 2)
- [Design Decisions](design-decisions.md) - decision table, forbidden list, reference projects, review process, decision records (sections 16-20)
- [QVT-O Test Plan](qvto-test-plan.md) - QVT-O spec-conformance test plan & progress tracking

---

## 10. Testing Strategy

> **SPEC-FIRST WORKFLOW** — bei JEDEM Task befolgen:
> 1. ZUERST die OMG-Spec lesen (OCL v2.4/v2.5, QVT-O v1.3, QVT-D v1.3, MOFM2T v1.0)
> 2. Tests gegen die Spec schreiben — das erwartete Ergebnis kommt aus der Spec
> 3. Tests laufen lassen — Failures sind Implementierungslücken, NICHT Testfehler
> 4. Implementierung fixen — bis die Tests grün sind
> 5. NIEMALS Tests an die Implementierung anpassen, um sie grün zu machen
> 6. Bei Unklarheit: Rückfrage an den User

### 10.1 Plain JUnit 5 First

**All tests that can run without OSGi must be plain JUnit 5 tests.** This includes:

- Parser tests (grammar correctness, error reporting)
- Evaluator tests (expression evaluation, standard library)
- EMF delegate tests (validation/setting/invocation delegates)
- Spec compliance tests
- Integration tests (parse -> evaluate pipeline)

Only create a **separate OSGi test project** (`*.itest`) when the test **requires an OSGi container**:
- DS component wiring tests
- Whiteboard extension discovery tests
- emf.osgi EPackageConfigurator integration tests

### 10.2 Test Project Structure

```
workspace/
├── org.eclipse.fennec.m2x.ocl.tests/       # Plain JUnit 5 tests (parser, engine, delegates)
├── org.eclipse.fennec.m2x.ocl.itest/       # OSGi integration tests (DS, whiteboard) - only if needed
├── org.eclipse.fennec.m2x.qvto.tests/      # Plain JUnit 5 tests (QVT-O)
├── org.eclipse.fennec.m2x.qvto.itest/      # OSGi integration tests - only if needed
├── org.eclipse.fennec.m2x.qvtd.tests/      # Plain JUnit 5 tests (QVT-R/QVT-C)
├── org.eclipse.fennec.m2x.qvtd.itest/      # OSGi integration tests - only if needed
├── org.eclipse.fennec.m2x.m2t.tests/       # Plain JUnit 5 tests
└── org.eclipse.fennec.m2x.m2t.itest/       # OSGi integration tests - only if needed
```

### 10.3 Test Categories

| Category | Type | Runner | Tag |
|----------|------|--------|-----|
| **Unit** | Individual classes/methods | JUnit 5 | - |
| **Integration** | Parse -> evaluate pipeline | JUnit 5 | - |
| **Spec compliance** | Examples from OCL/QVT/MOFM2T specs | JUnit 5 | `@Tag("spec")` |
| **Edge cases** | Error handling, boundary conditions | JUnit 5 | - |
| **Eclipse spec tests** | Migrated from Eclipse OCL/QVTo/Acceleo test suites | JUnit 5 (adapted) | `@Tag("spec")` |
| **Performance** | Benchmarks comparing Fennec vs Eclipse implementations | JUnit 5 + JMH | `@Tag("perf")` |
| **OSGi integration** | DS wiring, whiteboard, emf.osgi | OSGi test (separate `*.itest` project) | - |

### 10.4 Migrating Eclipse Spec Tests

Existing spec-related tests from Eclipse OCL, QVTo, and Acceleo repositories should be:

1. Identified and extracted from the Eclipse test projects
2. Migrated to **JUnit 5** (from JUnit 3/4)
3. Adapted to use our API instead of Eclipse's
4. Placed in the corresponding `*.tests` project
5. Tagged with `@Tag("spec")` for filtering

### 10.5 Test Naming

```java
@Test
void integerLiteral_parsesCorrectly() { ... }

@Test
@Tag("spec")
void specExample_7_3_3_invariant() { ... }  // Reference to spec section
```

### 10.6 Performance Testing

Both engines under identical conditions (standalone, no OSGi), shared test models,
System.nanoTime() loops with warmup (JMH for rigorous benchmarks later).

For OCL-specific benchmark results, see [OCL Architecture §11](ocl-architecture.md#11-performance).

#### Test Project Dependencies

Performance test projects need the Eclipse implementations on their buildpath:

```bnd
# In org.eclipse.fennec.m2x.ocl.tests/bnd.bnd
-buildpath: \
    org.eclipse.fennec.m2x.ocl.engine;version=latest,\
    org.eclipse.fennec.m2x.ocl.parser;version=latest,\
    org.eclipse.ocl.pivot;version=latest,\
    org.eclipse.ocl.xtext.essentialocl;version=latest,\
    ...
```

The Eclipse OCL/QVTo/Acceleo jars must be available in the bnd repository (indexed from the `repositories/` folder or Maven Central).

---

## 11. OSGi-Optional Architecture

### 11.1 Core Principle: Works Without OSGi, Enhanced With OSGi

All engines and services must be **usable as plain Java libraries** without any OSGi container. OSGi is an optional enhancement that provides service discovery, whiteboard patterns, and lifecycle management.

**Pattern: Pure Java core + OSGi adapter layer**

```
┌─────────────────────────────────────────────────────────────┐
│  .engine (pure Java, no OSGi imports)                       │
│                                                             │
│  OclEngineImpl implements OclEngine                         │
│    - constructor injection: new OclEngineImpl(config)        │
│    - works standalone as plain Java library                  │
└──────────────────────────┬──────────────────────────────────┘
                           │
┌──────────────────────────┴──────────────────────────────────┐
│  .engine (OSGi DS adapter, same bundle)                     │
│                                                             │
│  @Component(service = OclEngine.class)                      │
│  OclEngineComponent extends OclEngineImpl                   │
│    - @Reference injection for extensions                    │
│    - DS whiteboard for dynamic extension discovery          │
└─────────────────────────────────────────────────────────────┘
```

### 11.2 API vs Implementation Packages

- `.api` and `.model` bundles **export** their packages (public API)
- `.parser` and `.engine` bundles export only their top-level service package
- Internal implementation packages use `*.internal` naming

For OCL-specific standalone/OSGi usage examples, see
[OCL Architecture §9](ocl-architecture.md#9-osgi-integration).

---

## 12. Extension Points

The Eclipse implementations use `plugin.xml` extension points for registration. We replace these with **programmatic registration** (plain Java) and **OSGi DS whiteboard** (OSGi).

### 12.1 EMF Infrastructure Registrations

These are mandatory baseline registrations that Eclipse handles via `plugin.xml` → `org.eclipse.emf.ecore.generated_package` etc. We must handle them explicitly.

| Registration | Eclipse mechanism | Our approach (plain Java) | Our approach (OSGi) |
|---|---|---|---|
| **EPackage registration** | `org.eclipse.emf.ecore.generated_package` | `EPackage.Registry.INSTANCE.put(uri, pkg)` | fennec emf.osgi `EPackageConfigurator` (automatic) |
| **Resource.Factory for file extensions** | `org.eclipse.emf.ecore.extension_parser` | `Resource.Factory.Registry.INSTANCE.getExtensionToFactoryMap().put(ext, factory)` | DS component |
| **Resource.Factory for protocols** | `org.eclipse.emf.ecore.protocol_parser` | `Resource.Factory.Registry.INSTANCE.getProtocolToFactoryMap().put(proto, factory)` | DS component |
| **URI mapping** (e.g. stdlib location) | `org.eclipse.emf.ecore.uri_mapping` | `URIConverter.URI_MAP.put(logicalUri, physicalUri)` | DS component |

Each of our `.model` bundles must register its EPackages. Each `.parser` bundle must register resource factories for its file extensions.

### 12.2 Language-Specific Extension Points

#### OCL Extensions

| Extension | Eclipse extension point | Without OSGi | With OSGi |
|---|---|---|---|
| **OCL Custom Operations** | `org.eclipse.ocl.pivot.standard_library` | `OclConfiguration.builder(parser).addOperationProvider(...).customOperationsEnabled(true)` (D29: disabled by default) | Single `@Reference(name="operationProvider")` on `OclEngineComponent` — default: `NoOpOclOperationProvider`; custom: register `@Component(service = OclOperationProvider.class, property = "provider.name=myProvider")` and set `operationProvider.target=(provider.name=myProvider)` via ConfigAdmin |
| **OCL Standard Library** | `org.eclipse.ocl.pivot.standard_library` | Built-in, loaded at startup | DS whiteboard: `@Component(service = OclStandardLibraryContribution.class)` |
| **Complete OCL Documents** | `org.eclipse.ocl.pivot.complete_ocl_registry` | `OclEngine.registerCompleteOclDocument(contribution)` / `unregisterCompleteOclDocument(contribution)` | DS whiteboard: `@Component(service = CompleteOclContribution.class)` |
| **Model Extents / Resource Providers** | N/A (programmatic in Eclipse too) | Pass `Resource` / `ResourceSet` directly | DS `@Reference` |

#### QVT-O Extensions

| Extension | Eclipse extension point | Without OSGi | With OSGi |
|---|---|---|---|
| **Blackbox Libraries** | `org.eclipse.m2m.qvt.oml.javaBlackboxUnits` / `blackboxProvider` | `QvtoConfiguration.builder(ocl).blackboxRegistry(...).blackboxEnabled(true)` (D29: disabled by default) | DS whiteboard: `@Component(service = QvtoBlackboxLibrary.class)` |
| **Unit Resolver** (finds .qvto files) | `org.eclipse.m2m.qvt.oml.unitResolverFactory` | `QvtoConfiguration.builder(ocl).unitResolvers(...).unitResolverEnabled(true)` (D29: disabled by default) | DS whiteboard: `@Component(service = QvtoUnitResolver.class)` |
| **Deployed Transformations** | `org.eclipse.m2m.qvt.oml.runtime.qvtTransformation` | Load by URI/path directly | DS whiteboard: `@Component(service = QvtoTransformationContribution.class)` |

#### QVT-D Extensions

| Extension | Eclipse extension point | Without OSGi | With OSGi |
|---|---|---|---|
| **QVT-D Key Definitions** | N/A (in metamodel) | `QvtdEngine.registerKeys(...)` | DS whiteboard: `@Component(service = QvtdKeyProvider.class)` |
| **QVT Runtime Library** | `org.eclipse.ocl.pivot.standard_library` | Built-in, loaded at startup | DS whiteboard |

#### M2T Extensions

| Extension | Eclipse extension point | Without OSGi | With OSGi |
|---|---|---|---|
| **Custom Java Services** | `org.eclipse.acceleo.query.ide.servicesConfigurator` | `M2tEngine.registerService(...)` | DS whiteboard: `@Component(service = M2tService.class)` |
| **Module/Name Resolver** | `org.eclipse.acceleo.query.ide.resolverfactory` | `M2tEngine.registerResolver(...)` (classpath-based) | DS whiteboard: `@Component(service = M2tModuleResolver.class)` |
| **ResourceSet Configurator** | `org.eclipse.acceleo.query.ide.resourceSetConfigurator` | Pass configured `ResourceSet` directly | DS whiteboard: `@Component(service = M2tResourceSetConfigurator.class)` |

### 12.3 EMF Delegate Integration

EMF delegate registries allow OCL expressions in Ecore models via `EAnnotation` — for
validation constraints, derived features, and operation bodies. See
[OCL Architecture §8](ocl-architecture.md#8-emf-delegate-integration) for full details.

### 12.4 Whiteboard Pattern (OSGi)

Extensions register themselves as OSGi services. The engine discovers them dynamically:

```java
// Extension provider (user code)
@Component(service = QvtoBlackboxLibrary.class)
public class MyBlackbox implements QvtoBlackboxLibrary {
    @Override
    public String getName() { return "mylib"; }

    @Override
    public Map<String, Method> getOperations() { ... }
}

// Engine (consumer) - discovers all blackbox libraries
@Component(service = QvtoEngine.class)
public class QvtoEngineComponent extends QvtoEngineImpl {

    @Reference(cardinality = ReferenceCardinality.MULTIPLE,
               policy = ReferencePolicy.DYNAMIC)
    volatile List<QvtoBlackboxLibrary> blackboxLibraries;
}
```

### 12.5 Extension Interfaces (in `.api` bundles)

For OCL extension interfaces (`OclOperationProvider`, `CompleteOclContribution`,
`OclStandardLibraryContribution`), see [OCL Architecture §4.6](ocl-architecture.md#46-extension-interfaces).

For QVT-O extension interfaces (`QvtoBlackboxLibrary`, `QvtoUnitResolver`), see
[QVT-O Architecture §4](qvto-architecture.md#4-api-design-qvtoapi).

```java
// In org.eclipse.fennec.m2x.m2t.api (planned)
public interface M2tService {
    String getName();
    Object invoke(String operationName, Object... args);
}

public interface M2tModuleResolver {
    Optional<M2tModule> resolveModule(String qualifiedName);
}
```

---

## 13. Language Server Protocol (LSP) — Future Phase

### 13.1 Overview

As a **later phase** (after core engines are stable), provide LSP implementations for all languages. This enables IDE support in VS Code, Eclipse, IntelliJ, and any LSP-capable editor.

### 13.2 Planned Modules

```
workspace/
├── org.eclipse.fennec.m2x.ocl.lsp/     # OCL Language Server
├── org.eclipse.fennec.m2x.qvto.lsp/    # QVT-O Language Server
├── org.eclipse.fennec.m2x.qvtd.lsp/   # QVT-R Language Server
└── org.eclipse.fennec.m2x.m2t.lsp/     # MOFM2T Language Server
```

### 13.3 LSP Features Per Language

| Feature | OCL | QVT-O | QVT-R | MOFM2T |
|---------|-----|-------|-------|--------|
| Syntax highlighting | Yes | Yes | Yes | Yes |
| Error diagnostics | Yes (ANTLR4 errors) | Yes | Yes | Yes |
| Completion | Types, properties, operations, iterators | Mappings, helpers, model types | Relations, domains, patterns | Templates, OCL expressions |
| Hover | Type information, documentation | Mapping signatures | Relation signatures | Template parameters |
| Go to definition | Variable/type definitions | Mapping/helper definitions | Relation/key definitions | Template definitions |
| Find references | Variable/type usage | Mapping calls | Relation invocations | Template calls |
| Formatting | Yes | Yes | Yes | Yes |
| Validation | OCL well-formedness rules | QVT-O type checking | QVT-R domain/pattern rules | Template parameter types |

### 13.4 Implementation Approach

- Use [Eclipse LSP4J](https://github.com/eclipse-lsp4j/lsp4j) as the LSP protocol library (Java, lightweight)
- Reuse ANTLR4 parsers for incremental parsing and error recovery
- Reuse EMF metamodels for type information and completion
- Each LSP server is a standalone Java process (no Eclipse platform required)
- Can also run embedded in OSGi for Eclipse IDE integration

### 13.5 Dependencies on Core

LSP modules depend on the parser and engine modules being stable. They are **not part of the initial implementation phases**.

---

## 14. Roadmap

### Phase 1: OCL (Foundation) — ✅ Complete

**Status:** ✅ Complete — 4202 tests, 0 failures, spec-verified against OCL v2.4/v2.5.

**Deferred OCL Gaps:**
- **S-10** (validate type-checker) — Deferred to Phase 5 (LSP). No-op stub, brings value only with real-time diagnostics.
- **S-03/S-05** (OclMessage / `^^` / UnspecifiedValueExp) — Deferred indefinitely. UML behavioral modeling, Eclipse also skips.
- **S-06/S-07/S-08** (oclIsInState, AssociationClass, Qualified associations) — UML-only, no Ecore equivalent.
- **G-01** (escaped identifiers `_'keyword'`), **G-03** (real exponent `1e10`), **G-10** (self-variable alias `context p : Person`) — Medium-priority syntax gaps, workarounds exist.
- 8 low-priority syntax gaps (G-02, G-04–G-07, G-09, G-11, G-12) — see [OCL Spec Compliance](ocl-spec-compliance.md).

See [OCL Architecture](ocl-architecture.md) and [OCL Spec Compliance](ocl-spec-compliance.md) for full details.

### Phase 2: QVT-Operational — ✅ Complete

**Status:** ✅ Complete — 1050 tests (0 failures, 0 @Disabled, 1 @Tag("perf")). Phase 0–10 + GAP-1.

**Deferred QVT-O Gaps:**
- **GAP-4** (Persisted Trace Data) — Spec says "left unspecified", Eclipse doesn't implement it either. Deferred until concrete use case arises.
- **GAP-5** (`asTransformation()` dynamic compilation) — Works with pre-registered transformations; runtime compiler deferred indefinitely (conscious limitation).
- **P10-07** (UML-specific operations: `_localId`, `_globalId`, `markedAs`, `markValue`, `stereotypedBy`, `stereotypedStrictlyBy`) — Consciously skipped, like Eclipse. Fennec targets Ecore/EMF, not UML.

See [QVT-O Architecture](qvto-architecture.md) for full details, [QVT-O Test Plan](qvto-test-plan.md) for spec-conformance tracking, and [QVT-O Spec Gap Analysis](qvto-spec-gap-analysis.md) for detailed gap documentation.

### Phase 3: Acceleo/MOFM2T (with OCL expressions + cherry-picked extensions) — In Progress

**Status:** P3-0 (Metamodel + Infrastructure) ✅ Complete. Next: P3-1 (Parser).

**Implementation Plan:** See [`m2t-implementation-plan.md`](m2t-implementation-plan.md) for detailed sub-phases.

| Sub-Phase | Module | Description | Status |
|-----------|--------|-------------|--------|
| P3-0 | `m2t.model`, `m2t.api` | EMF metamodel (2 enums, 17 EClasses), public API interfaces, bundle infrastructure | ✅ Done |
| P3-1 | `m2t.parser` | ANTLR4 grammar (`M2t.g4`, imports `Ocl.g4`, text_explicit + code_explicit modes) | TODO |
| P3-2 | `m2t.engine` | Core engine (template eval, for/if/let, whitespace handling, query invocation) | TODO |
| P3-3 | `m2t.engine` | FileBlock + writer stack (file output, stdout, charset) | TODO |
| P3-4 | `m2t.engine` | Module composition (import/extends), protected areas, trace block | TODO |
| P3-5 | `m2t.engine` | Macros + MOFM2T standard library (15 string operations) | TODO |
| P3-6 | `m2t.engine` | Post-expression, encoding, end-to-end tests, regression check | TODO |

### Phase 4: QVT-Declarative (Relations + Core)

| Step | Module | Description |
|------|--------|-------------|
| 4.1 | `qvtd.model` | QVT-R metamodel (Relation, RelationDomain, DomainPattern, Key, Template) + QVT-C metamodel (CoreModel, Area, Mapping, Guard, Assignment) |
| 4.2 | `qvtd.api` | `QvtdEngine` interface, `QvtdKeyProvider` extension interface, public API |
| 4.3 | `qvtd.parser` | ANTLR4 grammar (`QvtRelations.g4`, imports `Ocl.g4`). QVT-Core is primarily a compilation target, no separate parser unless needed. |
| 4.4 | `qvtd.engine` | Declarative transformation engine: relation matching, pattern matching, domain enforcement, `checkonly`/`enforce` semantics, optional QVTr→QVTc compilation |
| 4.5 | `qvtd.tests` | Relation tests, domain tests, pattern matching tests |

**Deferred QVT-O Gaps (require QVT-R metamodel):**
- **GAP-6** (`refined`, `relation`, `refinedRelation`) — `OperationalTransformation.refined: Transformation[0..1]`, `MappingOperation.refinedRelation` require QVT-R metamodel types (§8.2.1.1, §8.2.1.11)
- **GAP-13 Semantik** (`refines` pattern inheritance) — Parser support ✅ (6 tests), but semantic execution of `refines` requires relation refinement logic from QVT-R

### Phase 5: Language Servers (Future)

| Step | Module | Description |
|------|--------|-------------|
| 5.1 | `ocl.lsp` | OCL Language Server (LSP4J) |
| 5.2 | `qvto.lsp` | QVT-O Language Server |
| 5.3 | `qvtd.lsp` | QVT-R Language Server |
| 5.4 | `m2t.lsp` | MOFM2T Language Server |
| 5.5 | OCL GAP-2 | `@pre` postcondition support (pre-state snapshot mechanism, MessageExp, oclIsNew) |
| 5.6 | OCL GAP-3 | `validate()` implementation (static type-checking visitor over AST) |

---

## 15. Security & Performance

When evaluating OCL expressions from untrusted sources, configure appropriate resource limits
via `OclEvaluationOptions`. See [OCL Architecture §10](ocl-architecture.md#10-security-hardening)
for the full threat model, attack vector catalogue, and embedder guidance.

For OCL performance details (thread-safety, caching, benchmarks), see
[OCL Architecture §7](ocl-architecture.md#7-caching-architecture) and
[OCL Architecture §11](ocl-architecture.md#11-performance).
