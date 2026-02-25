# Fennec M2M Development Guideline

> This document covers core conventions and coding standards (sections 1-9).
> See also: [Architecture](architecture.md) (sections 10-13), [Implementation Plan](implementation-plan.md) (sections 14-15), [Design Decisions](design-decisions.md) (sections 16-20), [OCL Architecture](ocl-architecture.md)

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

*Continues in:*
- [OCL Architecture](ocl-architecture.md) - consolidated OCL implementation reference (Phase 1)
- [Architecture](architecture.md) - OSGi-optional design, extension points, EMF delegates, LSP (sections 10-13)
- [Implementation Plan](implementation-plan.md) - phases, testing strategy, performance benchmarks (sections 14-15)
- [Design Decisions](design-decisions.md) - decision table, forbidden list, reference projects, review process, decision records (sections 16-20)

---

## 10. Test Coverage Policy

For OCL-specific coverage targets, thread-safety testing, and performance regression policy, see
[OCL Architecture §12](ocl-architecture.md#12-testing).

---

## 11. Security Considerations

When evaluating OCL expressions from untrusted sources, configure appropriate resource limits
via `OclEvaluationOptions`. See [OCL Architecture §10](ocl-architecture.md#10-security-hardening)
for the full threat model, attack vector catalogue, and embedder guidance.
