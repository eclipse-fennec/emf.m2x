# M2T Engine User Guide

Fennec M2T is a lightweight, spec-compliant MOFM2T v1.0 (Model to Text) engine that works as a standalone Java library — no Eclipse platform required. Compatible with Acceleo 3.7 templates.

## Table of Contents

1. [Overview](#1-overview)
2. [Quick Start](#2-quick-start)
3. [Engine Setup](#3-engine-setup)
4. [Template Syntax](#4-template-syntax)
5. [Template Invocation](#5-template-invocation)
6. [Multiple Inputs](#6-multiple-inputs)
7. [Multiple Output Files](#7-multiple-output-files)
8. [Module Composition](#8-module-composition)
9. [Guards and Post-Processing](#9-guards-and-post-processing)
10. [Protected Areas](#10-protected-areas)
11. [Standard Library](#11-standard-library)
12. [Error Handling](#12-error-handling)
13. [Calling Operations from a Template](#13-calling-operations-from-a-template)
14. [Engine Lifetime and Module Retention](#14-engine-lifetime-and-module-retention)
15. [M2tGenerationStrategy](#15-m2tgenerationstrategy)
16. [Whitespace Handling](#16-whitespace-handling)
17. [Generating from a bnd Build](#17-generating-from-a-bnd-build)

---

## 1. Overview

The Fennec M2T Engine provides:

- **MOFM2T v1.0** template parsing and execution
- **Acceleo 3.7 compatible** — existing `.mtl` templates work out of the box
- **Standalone operation** — works as a plain Java library without OSGi
- **OSGi-optional** — designed for future Declarative Services support
- **ANTLR4-based parser** — fast, reliable parsing with precise error locations
- **Built on Fennec OCL** — full OCL v2.5 expression support within templates
- **Protected area merging** — hash-based smart merge (D31) for user-code preservation
- **Configurable whitespace** — MOFM2T §8.4 strict mode and Acceleo-compatible mode

### Bundles

| Bundle | Description |
|--------|-------------|
| `org.eclipse.fennec.m2x.m2t.api` | Public API interfaces |
| `org.eclipse.fennec.m2x.m2t.parser` | ANTLR4 parser |
| `org.eclipse.fennec.m2x.m2t.engine` | Evaluator implementation |
| `org.eclipse.fennec.m2x.m2t.model` | MOFM2T EMF metamodel |
| `org.eclipse.fennec.m2x.ocl.*` | Required OCL bundles (transitive) |

---

## 2. Quick Start

Minimal example — parse an MOFM2T template and generate a Java file from an EClass:

```java
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.M2tContext;
import org.eclipse.fennec.m2x.m2t.api.M2tResult;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngine;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

// 1. Create engine
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
M2tEngine engine = M2tEngines.create(config);

// 2. Parse template
Module module = engine.parse(
    "[module m(Ecore)/]\n" +
    "[template public main(c : EClass)]\n" +
    "[file (c.name.concat('.java'), false)]\n" +
    "public class [c.name/] {}\n" +
    "[/file]\n" +
    "[/template]\n",
    "MyTemplate");

// 3. Execute with an EClass as input
M2tResult result = engine.execute(module, M2tContext.of(myEClass));

// 4. Read generated output
if (result.isSuccess()) {
    String javaCode = result.generatedFiles().get("Employee.java");
    // → "public class Employee {}"
}
```

Five lines of setup, one template, one generated file.

---

## 3. Engine Setup

In a bnd workspace, one instruction puts the M2T API on the buildpath — the OCL API comes
with it, because part of M2T's API surface is OCL:

```
-library: enableM2T
```

Running templates from a build needs none of this: see the
[M2T bnd Generator Guide](m2t-generator-guide.md).

### 3.1 Three ways to give M2T its OCL engine

M2T evaluates template expressions with an OCL engine. Which one, is the caller's choice — and the first door needs no OCL knowledge at all:

```java
// 1. don't care: M2T settings alone, the factory supplies a default OCL engine
M2tConfiguration.builder().whitespaceMode(WhitespaceMode.ACCELEO).build();

// 2. bring the engine you already have — the injected service under OSGi,
//    OclEngines.create(...) in plain Java
M2tConfiguration.builder(oclEngine).build();

// 3. configure the OCL side yourself
M2tConfiguration.builder(oclConfiguration).build();
```

A supplied engine is used as it is: its cache, its operation providers, its evaluation settings. The MOFM2T §8.3 string operations travel with each evaluation, so an engine that knows nothing about M2T needs no preparation.

`M2tEngine.getOclEngine()` returns the engine that actually runs — useful to warm it up, inspect its cache, or install EMF delegates on it.

Under OSGi the engine is a service and door 2 is what the component uses — see [§3.6](#36-osgi-declarative-services).

### 3.2 Minimal

```java
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngine;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
M2tEngine engine = M2tEngines.create(config);
```

### 3.3 With Builder Options

```java
import java.nio.charset.StandardCharsets;
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.engine.OclLruExpressionCache;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport())
    .expressionCache(OclLruExpressionCache.ofSize(2048))
    .build();

M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .defaultCharset(StandardCharsets.UTF_8)
    .whitespaceMode(WhitespaceMode.ACCELEO)
    .generationStrategy(myStrategy)
    .build();

M2tEngine engine = M2tEngines.create(config);
```

### 3.4 Configuration Options

| Method | Default | Description |
|--------|---------|-------------|
| `resourceSet(resourceSet)` | — | Resource set whose package registry resolves metamodel type names (see [§3.5](#35-which-metamodels-the-engine-sees)) |
| `packageRegistry(registry)` | `EPackage.Registry.INSTANCE` | The registry itself; wins over `resourceSet` when both are set |
| `defaultCharset(charset)` | UTF-8 | Charset for file output encoding |
| `whitespaceMode(mode)` | `ACCELEO` | Whitespace normalization mode (see [§16](#16-whitespace-handling)) |
| `generationStrategy(strategy)` | `null` (in-memory) | SPI for file output (see [§15](#15-m2tgenerationstrategy)) |
| `maxDiagnostics(int)` | 10,000 | Maximum diagnostics before truncation |
| `maxTemplateDepth(int)` | 1,000 | Maximum template invocation depth (recursion limit) |
| `maxForIterations(int)` | 1,000,000 | Maximum for-block iterations |
| `maxCrossProductSize(int)` | 1,000,000 | Maximum cross-product size for set-argument invocations |
| `maxOutputSize(long)` | 10,000,000 | Maximum total output size in characters (~10 MB), `0` for unlimited |
| `protectedAreaEnabled(boolean)` | `true` | Enable/disable protected area markers and merging (see [§10.5](#105-disabling-protected-areas)) |

### 3.5 Which Metamodels the Engine Sees

Every metamodel type name in a template is resolved when the template is parsed — the `Book` in `[template public main(b : Book)]`, in `[b.oclIsKindOf(Book)/]`, in a `[for (b : Book | …)]` and in an `overrides` declaration. The engine looks them up in its `EPackage.Registry`.

```java
// the ResourceSet you already have — under OSGi this is what emf.osgi injects
M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .resourceSet(resourceSet)
    .build();

// or the registry itself, if that is what you hold
M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .packageRegistry(registry)
    .build();
```

If both are set, the explicitly configured registry wins — the more specific setting beats the more general one. Only the resource set's package registry is used; nothing is loaded through it.

The default is `EPackage.Registry.INSTANCE`, which is the right answer in plain Java. Supply your own when you hold the packages yourself — under OSGi, or wherever two versions of one nsURI can coexist. Nothing inside the engine reaches for the global registry on its own (D42), so what you pass in is what the parser sees. Model version identity stays yours; see the `emf.osgi` fingerprint guide.

**This matters more than it looks.** A declared type that resolves nowhere used to degrade to `EObject` without a word, and two things broke at once: `oclIsKindOf` answered for the wrong type, and MOFM2T §8.1.3 override dispatch selected no override at all, because `EObject` is not a supertype of a dynamic `EClass`.

A type name that resolves nowhere is now an error — `parse` fails with `M2tParseException`, and `getErrors()` lists every unresolved name in the template:

```mtl
[template public main(x : NoSuchType)]     → M2tParseException: Unknown type (NoSuchType)
```

So a missing or wrong registry announces itself at parse time instead of producing quietly wrong output.


Templates are loaded with `engine.parse(URI)`, taking an EMF URI, and read through the resource set's `URIConverter`. `UriHelper` in the shared `org.eclipse.fennec.m2x` bundle converts what a caller usually holds:

```java
import org.eclipse.fennec.m2x.utils.UriHelper;

Module module = engine.parse(UriHelper.fromPath(Path.of("/srv/templates/report.mtl")));
```

Prefer it over `URI.createURI(path.toString())` — a path is not a URI, and on Windows that would produce a URI whose scheme is the drive letter.

> `UriHelper` ships in the bundle **`org.eclipse.fennec.m2x`** (package `org.eclipse.fennec.m2x.utils`) — the shared commons of this workspace, separate from the engine bundles:
>
> ```xml
> <dependency>
>     <groupId>org.eclipse.fennec.m2x</groupId>
>     <artifactId>org.eclipse.fennec.m2x</artifactId>
>     <version>0.1.1-SNAPSHOT</version>
> </dependency>
> ```


### 3.6 OSGi (Declarative Services)

`DefaultM2tEngine` publishes `M2tEngine` as a **PROTOTYPE**-scoped service. Every `@Reference` injection gets its own engine with its own caches, so one consumer's warm-up and another's memory footprint stay separate.

Its configuration policy is **optional**: there is a working engine without configuring anything, and configuring one later does not register a second service that consumers would have to disambiguate.

```java
import org.eclipse.fennec.m2x.m2t.api.M2tEngine;
import org.eclipse.fennec.m2x.m2t.api.annotation.require.RequireM2T;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Reference;

@RequireM2T                       // resolver: a bundle providing the M2T engine must be present
@Component
public class MyGenerator {

    @Reference
    private M2tEngine engine;     // fresh instance, own caches

    public void generate(EObject model) throws M2tParseException {
        Module module = engine.parse(templateSource, "myModule");
        engine.link(module);
        engine.execute(module, M2tContext.of(model));
    }
}
```

**Components and scopes:**

| Component | Scope | What's shared? |
|---|---|---|
| `DefaultM2tEngine` | PROTOTYPE | Nothing — each consumer gets its own engine, module cache and linker state |
| `DefaultOclEngine` | PROTOTYPE | Nothing — bound `prototype_required`, so each M2T engine evaluates on its own OCL engine |
| `DefaultOclExpressionCacheComponent` | SINGLETON | The parsed-expression cache, shared by all OCL engines |

The engine takes its OCL engine as a **mandatory** service reference. That is door 2 from [§3.1](#31-three-ways-to-give-m2t-its-ocl-engine): the OCL engine a consumer configured — with its cache and its operation providers — is the one that evaluates the template expressions, rather than a second one built beside it.

### 3.6.1 Configuration via ConfigAdmin

All properties use the `m2t.` prefix. OCL limits are **not** repeated here; they are configured on `DefaultOclEngine` with `ocl.*`, so the two cannot drift apart.

```json
{
    ":configurator:resource-version": 1,
    "DefaultM2tEngine": {
        "m2t.whitespaceMode": "SPEC",
        "m2t.defaultCharset": "UTF-8",
        "m2t.maxOutputSize": 5000000
    },
    "DefaultOclEngine": {
        "ocl.maxDepth": 500
    }
}
```

**Available properties:**

| Property | Type | Default | Description |
|---|---|---|---|
| `m2t.defaultCharset` | String | `UTF-8` | Charset for generated files that do not name one |
| `m2t.whitespaceMode` | String | `ACCELEO` | `NONE`, `SPEC` or `ACCELEO` — see [§16](#16-whitespace-handling) |
| `m2t.maxDiagnostics` | int | 10,000 | Diagnostics collected before generation gives up (T-2) |
| `m2t.maxTemplateDepth` | int | 1,000 | Nesting depth of template invocations (T-1 runaway recursion) |
| `m2t.maxForIterations` | int | 1,000,000 | Iterations of a single `for` block (T-3) |
| `m2t.maxCrossProductSize` | int | 1,000,000 | Size of a `for` over several collections (T-3) |
| `m2t.maxOutputSize` | long | 10,000,000 | Characters one generation may produce (T-4 output flooding) |
| `m2t.protectedAreaEnabled` | boolean | `true` | Whether protected area markers in existing files are honoured (T-6) |
| `m2t.unitResolverEnabled` | boolean | `false` | Whether modules may be reached beyond the ones handed to `link` |
| `m2t.discoverUnitResolvers` | boolean | `false` | Whether resolvers registered as services are looked up by the name a template extends or imports |
| `m2t.allowedUnitModules` | String[] | *(empty)* | Module names that may be resolved; **empty puts no restriction on the names** |
| `m2t.maxUnitResolvers` | int | 5 | How many resolvers one generation may consult |

The same settings are available to plain Java through `M2tConfiguration.Builder` — see [§3.4](#34-configuration-options).

### 3.6.2 Modules the engine fetches

`extends` and `[import/]` resolve against the modules handed to `link` — and, if you let them, against modules fetched by name:

```java
M2tEngines.create(M2tConfiguration.builder()
    .addUnitResolver(name -> Optional.of(new M2tUnit.SourceUnit(name, uri, source)))
    .unitResolverEnabled(true)
    .allowedUnitModules(Set.of("base"))
    .build());
```

Under OSGi a bundle offers a module by publishing a resolver under the name it answers for, and the engine looks it up when it links:

```java
@Component(service = M2tUnitResolver.class, property = "m2t.unit.name=base")
public class BaseTemplates implements M2tUnitResolver { … }
```

**Off by default.** A resolver produces template source from a name a template chose, which is a way out of the set of modules the caller decided to generate from. `m2t.unitResolverEnabled` is the gate; `m2t.allowedUnitModules` narrows what may be reached once it is open, and an **empty** list narrows nothing.

A name that stays unresolved is a warning from `link`, as it always was — a linker asked to link a partial set is not a failure.

**Running on a specific OCL engine:** the reference is unfiltered by default, so the engine binds whichever `OclEngine` service is there. To pin it to one you configured yourself, set a target filter:

```json
{
    "DefaultM2tEngine": { "oclEngine.target": "(component.name=MyTunedOclEngine)" }
}
```

---

## 4. Template Syntax

MOFM2T templates use `[` and `]` as delimiters. Everything outside `[...]` is literal text; everything inside is a directive or OCL expression.

### 4.1 Module Declaration

Every template file starts with a module declaration:

```mtl
[module myModule(Ecore)/]
```

The parameter in parentheses is the metamodel used by the templates. For Ecore models, use `Ecore`.

### 4.2 Template

Templates are the main code-generation constructs. A template marked `main` (or named `main`) is the entry point:

```mtl
[module m(Ecore)/]
[template public main(c : EClass)]
[file (c.name.concat('.java'), false)]
class [c.name/]
{
  // Constructor
  [c.name/]()
  {
  }
}
[/file]
[/template]
```

Input: `EClass(name="Employee")` produces:

```
class Employee
{
  // Constructor
  Employee()
  {
  }
}
```

### 4.3 OCL Expressions

Inline OCL expressions are enclosed in `[` and `/]`:

```mtl
[c.name/]                           [comment]single property[/comment]
[c.eAttributes->size()/]            [comment]collection operation[/comment]
[c.name.toUpper()/]                 [comment]string operation[/comment]
['hello'.concat(' world')/]         [comment]string literal[/comment]
```

### 4.4 For Block

Iterate over collections with optional separator, before, after, and guard:

```mtl
[comment]simple iteration[/comment]
[for (a : EAttribute | c.eAttributes)]
  [a.eType.name/] [a.name/];
[/for]

[comment]with separator[/comment]
[for (a : EAttribute | c.eAttributes) separator (', ')]
[a.name/]
[/for]

[comment]with before/after[/comment]
[for (c : EClassifier | p.eClassifiers) before ('[') separator (', ') after (']')]
[c.name/]
[/for]

[comment]with guard — only matching elements[/comment]
[for (cl : EClassifier | p.eClassifiers) ? (cl.name.startsWith('C'))]
[cl.name/]
[/for]
```

The `i` variable is available inside for blocks as a 1-based iteration counter:

```mtl
[for (cl : EClassifier | p.eClassifiers)]
[i/]. [cl.name/]
[/for]
```

Output: `1. ClasseA`, `2. ClasseB`, `3. AbstractClass`

### 4.5 If / ElseIf / Else

Conditional output:

```mtl
[if (cl.name = 'ClasseA')]
A
[elseif (cl.name = 'ClasseB')]
B
[else]
OTHER
[/if]
```

### 4.6 Let Block

Bind a variable for use within the block:

```mtl
[let n : String = p.name]
package: [n/]
[/let]
```

The `let` block also supports an `else` branch that executes when the binding evaluates to null:

```mtl
[let prefix : String = p.nsPrefix]
prefix: [prefix/]
[else]
no prefix
[/let]
```

### 4.7 File Block

Direct output to a named file. The second parameter controls append mode (`true` = append, `false` = overwrite):

```mtl
[file (c.name.concat('.java'), false)]
public class [c.name/] {}
[/file]
```

Append mode for log files:

```mtl
[file ('generation.log', true)]
Generated: [c.name/]
[/file]
```

### 4.8 Query

Queries are reusable OCL expressions that return a value:

```mtl
[query public opCount(c : EClass) : Integer = c.eOperations->size()/]
```

Use in templates:

```mtl
[c.opCount()/]
```

### 4.9 Macro

Macros are like templates that return a string value rather than writing to the output stream:

```mtl
[macro wrap(c : EClass)]<[c.name/]>[/macro]
```

Use in templates:

```mtl
Result: [wrap(c)/]
```

Output: `Result: <Employee>`

### 4.10 Comment

```mtl
[comment]
  this is a comment, and it is discarded
[/comment]
```

Only the paired form exists. MOFM2T v1.0 specifies comments as a macro — `[comment()] … [/comment]` — and the grammar follows that; the single-expression form `[comment]…[/comment]` is an Acceleo 3.x extension the parser does not know.

A comment may not precede the `[module …/]` header.

---

## 5. Template Invocation

### 5.1 Calling Helper Templates

A template can call other templates defined in the same module:

```mtl
[module m(Ecore)/]
[template public genAttribute(a : EAttribute)]
private [a.eType.name/] [a.name/];
[/template]
[template public main(c : EClass)]
[file (c.name.concat('.java'), false)]
public class [c.name/] {
[for (a : EAttribute | c.eAttributes)]
  [genAttribute(a)/]
[/for]
}
[/file]
[/template]
```

Output for `EClass(Employee, attrs=[name:EString, salary:EDouble])`:

```
public class Employee {
  private EString name;
  private EDouble salary;
}
```

### 5.2 Multi-Layer Template Chains

Templates can call templates that call further templates:

```mtl
[module m(Ecore)/]
[template public attrGen(a : EAttribute)]
[a.eType.name/] [a.name/]
[/template]
[template public classGen(c : EClass)]
class [c.name/]([for (a : EAttribute | c.eAttributes) separator (', ')][attrGen(a)/][/for])
[/template]
[template public main(p : EPackage)]
[file ('out', false)]
[for (c : EClassifier | p.eClassifiers)]
[classGen(c.oclAsType(EClass))/]
[/for]
[/file]
[/template]
```

Output: `class Employee(EString name, EDouble salary)`

### 5.3 Template Invocation with Before/After/Separator

When invoking a template on a collection, use `before`, `after`, and `separator`:

```mtl
[fmt(p.eClassifiers) before ('[') separator (', ') after (']')/]
```

With `[template public fmt(c : EClassifier)][c.name/][/template]` this produces:

```
[ClasseA, ClasseB, AbstractClass]
```

### 5.4 Calling Queries from Templates

Queries encapsulate reusable OCL logic:

```mtl
[module m(Ecore)/]
[query public fieldCount(c : EClass) : Integer = c.eAttributes->size()/]
[template public genSummary(c : EClass)]
[c.name/] ([c.fieldCount()/] fields)
[/template]
[template public main(c : EClass)]
[file ('out', false)]
Summary: [genSummary(c)/]
[/file]
[/template]
```

Output: `Summary: Employee (2 fields)`

---

## 6. Multiple Inputs

### 6.1 Multi-Parameter Templates

A template can accept multiple parameters. Pass multiple input elements via `M2tContext.of(List, Path)`:

```mtl
[module m(Ecore)/]
[template public main(p : EPackage, c : EClass)]
[file ('out', false)]
package [p.name/];
class [c.name/] {}
[/file]
[/template]
```

```java
Module module = engine.parse(mtl, "test");
M2tResult result = engine.execute(module,
    M2tContext.of(List.of(myPackage, myClass), null));
// → "package company;\nclass Employee {}"
```

### 6.2 Navigation to Referenced Elements

A single input element can navigate to related model elements via OCL:

```mtl
[template public main(c : EClass)]
[file (c.name.concat('.java'), false)]
package [c.ePackage.name/];
class [c.name/] {
[for (a : EAttribute | c.eAttributes)]
  [a.eType.name/] [a.name/];
[/for]
}
[/file]
[/template]
```

The template receives one `EClass` but navigates to its `ePackage` and iterates over its `eAttributes`.

### 6.3 Helper Templates with Multiple Parameters

Helper templates can accept additional parameters:

```mtl
[template public genField(a : EAttribute, prefix : EString)]
[prefix/]_[a.name/] : [a.eType.name/]
[/template]
[template public main(c : EClass)]
[file ('out', false)]
[for (a : EAttribute | c.eAttributes) separator ('\n')]
[genField(a, c.name)/]
[/for]
[/file]
[/template]
```

Output: `Employee_name : EString` / `Employee_salary : EDouble`

---

## 7. Multiple Output Files

### 7.1 One File Per Element

Iterate over model elements and generate a separate file for each:

```mtl
[module m(Ecore)/]
[template public genClass(c : EClass)]
[file (c.name.concat('.java'), false)]
public class [c.name/] {
[for (a : EAttribute | c.eAttributes)]
  private [a.eType.name/] [a.name/];
[/for]
}
[/file]
[/template]
[template public main(p : EPackage)]
[for (c : EClassifier | p.eClassifiers)]
[genClass(c.oclAsType(EClass))/]
[/for]
[/template]
```

Generates `Employee.java` and `Manager.java` with their respective attributes.

### 7.2 Different File Types

Generate different kinds of files from the same model:

```mtl
[module m(Ecore)/]
[template public main(p : EPackage)]
[file ('MANIFEST.MF', false)]
Bundle-Name: [p.name/]
Bundle-SymbolicName: [p.nsURI/]
[/file]
[for (c : EClassifier | p.eClassifiers)]
[file (c.name.concat('.java'), false)]
package [p.name/];
public class [c.name/] {}
[/file]
[/for]
[/template]
```

Generates `MANIFEST.MF` plus one `.java` file per classifier.

### 7.3 Append-Mode Log File

Use append mode (`true`) to accumulate output across iterations:

```mtl
[module m(Ecore)/]
[template public main(p : EPackage)]
[for (c : EClassifier | p.eClassifiers)]
[file (c.name.concat('.java'), false)]
class [c.name/] {}
[/file]
[file ('generation.log', true)]
Generated: [c.name/]
[/file]
[/for]
[/template]
```

The `generation.log` file contains one line per generated class.

---

## 8. Module Composition

### 8.1 Extends

A module can extend another module to inherit its templates, queries, and macros:

```mtl
[comment]base.mtl[/comment]
[module base(ecore)/]
[template public greet(e : EClass)]Hello[/template]
```

```mtl
[comment]child.mtl[/comment]
[module child(ecore) extends base/]
[template public main(e : EClass)]
[file ('out.txt', false)]
[greet(e)/]
[/file]
[/template]
```

```java
Module baseModule = engine.parse(baseSource, "base");
Module childModule = engine.parse(childSource, "child");
engine.link(baseModule, childModule);

M2tResult result = engine.execute(childModule, M2tContext.of(input));
// → "Hello"
```

**Key:** Call `engine.link(modules...)` after parsing all modules and before executing. This resolves cross-module references.

### 8.2 Imports

Import makes another module's public templates and queries available:

```mtl
[module main(ecore)/]
[import util/]
[template public main(e : EClass)]
[file ('out.txt', false)]
[render(e)/]
[/file]
[/template]
```

```java
Module utilModule = engine.parse(utilSource, "util");
Module mainModule = engine.parse(mainSource, "main");
engine.link(utilModule, mainModule);
```

### 8.3 Overrides and Super

A child module can override a template from its parent, optionally calling `[super/]` to include the original output:

```mtl
[comment]base.mtl[/comment]
[module base(ecore)/]
[template public render(e : EClass)]Base[/template]
[template public main(e : EClass)]
[file ('out.txt', false)]
[render(e)/]
[/file]
[/template]
```

```mtl
[comment]child.mtl[/comment]
[module child(ecore) extends base/]
[template public render(e : EClass) overrides render]Before-[super/]-After[/template]
```

```java
Module baseMod = engine.parse(baseSource, "base");
Module childMod = engine.parse(childSource, "child");
engine.link(baseMod, childMod);

M2tResult result = engine.execute(baseMod, M2tContext.of(input));
// → "Before-Base-After"
```

Override chains work transitively. With modules A -> B -> C, each overriding `render`:

```mtl
[comment]A[/comment]
[template public render(e : EClass)]A[/template]

[comment]B extends A[/comment]
[template public render(e : EClass) overrides render][super/]+B[/template]

[comment]C extends B[/comment]
[template public render(e : EClass) overrides render][super/]+C[/template]
```

Result: `A+B+C`

### 8.4 Visibility

| Visibility | Same module | Via extends | Via import |
|------------|:-----------:|:-----------:|:----------:|
| `public` | Yes | Yes | Yes |
| `protected` | Yes | Yes | No |
| `private` | Yes | No | No |

### 8.5 Namesake Resolution

When multiple modules define a template with the same name, resolution priority is:

1. **Local** (defined in the current module)
2. **Extended** (inherited from the parent module)
3. **Imported** (from imported modules)

### 8.6 Linking API

```java
// Parse all modules
Module base = engine.parse(baseSource, "base");
Module child = engine.parse(childSource, "child");
Module util = engine.parse(utilSource, "util");

// Link — returns warnings for unresolved references
List<String> warnings = engine.link(base, child, util);

// Execute the entry-point module
M2tResult result = engine.execute(child, M2tContext.of(input));
```

For single-module scenarios, linking happens automatically during `execute()`.

---

## 9. Guards and Post-Processing

### 9.1 Template Guards

A guard is an OCL boolean expression that controls whether a template produces output:

```mtl
[template public concreteOnly(c : EClassifier)
    ? (c.oclIsKindOf(EClass) and not c.oclAsType(EClass).abstract)]
[c.name/]
[/template]
```

When the guard evaluates to `false`, the template produces no output.

### 9.2 Override Guards

Override templates can have guards. When the guard is `false`, the engine falls back to the overridden template:

```mtl
[comment]base[/comment]
[template public render(e : EClass)]Original[/template]

[comment]child — only overrides for abstract classes[/comment]
[template public render(e : EClass) overrides render ? (e.abstract)]Abstract: [e.name/][/template]
```

- Abstract class input: `Abstract: AbstractClass`
- Concrete class input: `Original` (fallback to base)

### 9.3 Parameter Type Narrowing

An override can narrow the parameter type. The override applies only when the actual argument matches the narrower type:

```mtl
[comment]base — accepts EClassifier[/comment]
[template public render(e : EClassifier)]Base[/template]

[comment]child — narrowed to EDataType[/comment]
[template public render(e : EDataType) overrides render]Narrowed[/template]
```

- `EDataType` argument: `Narrowed`
- `EClass` argument: `Base` (fallback — EClass is not an EDataType)

### 9.4 Post-Processing (Acceleo Extension)

The `post()` clause applies a String operation to the entire template output:

```mtl
[template public trimmed(p : EPackage) post(trim())]
   [p.name/]
[/template]
```

```mtl
[template public uppered(p : EPackage) post(toUpper())]
[p.name/]
[/template]
```

- `trimmed(p)` with `p.name = "target"` produces `target` (whitespace trimmed)
- `uppered(p)` produces `TARGET`

---

## 10. Protected Areas

Protected areas mark regions in generated files that are preserved across regenerations. User modifications inside protected areas survive re-generation.

### 10.1 Syntax

```mtl
[protected ('unique_id')]
// default content — replaced by user code after first generation
[/protected]
```

The generated output contains markers with a content hash (D31):

```
// [protected 'unique_id' a1b2c3d4]
// default content
// [/protected]
```

### 10.2 Dynamic IDs

Use OCL expressions for unique IDs per model element:

```mtl
[for (c : EClassifier | p.eClassifiers)]
[protected (c.name)]
code for [c.name/]
[/protected]
[/for]
```

Produces separate protected areas for `ClasseA`, `ClasseB`, etc.

### 10.3 Hash-Based Smart Merge (D31)

The protected area merger uses SHA-256 hashes to detect changes:

1. **First generation:** Default content is written with its hash in the marker
2. **Regeneration (no user changes):** The existing content hash matches the marker hash — content is regenerated fresh
3. **Regeneration (user edited):** The existing content hash differs from the marker — user content is preserved

This approach is more robust than marker-only matching because it detects when the default content was changed by the user.

### 10.4 M2tGenerationStrategy for Protected Areas

Protected area merging requires reading existing file content. Implement `readExistingContent()` in your `M2tGenerationStrategy`:

```java
@Override
public String readExistingContent(String filePath, Charset charset) {
    Path path = outputDir.resolve(filePath);
    if (Files.exists(path)) {
        return Files.readString(path, charset);
    }
    return null;  // no existing content — first generation
}
```

When no `M2tGenerationStrategy` is configured (in-memory mode), protected areas use default content on every execution.

### 10.5 Disabling Protected Areas

Protected areas can be disabled entirely via `protectedAreaEnabled(false)`. When disabled, `[protected]` blocks emit their body content without markers, and no merge with existing content is performed:

```java
M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .protectedAreaEnabled(false)
    .build();
```

```mtl
[template public main(c : EClass)]
[file ('out.java', false, 'UTF-8')]
public class [c.name/] {
    [protected ('fields')]
    // custom fields
    [/protected]
}
[/file]
[/template]
```

With `protectedAreaEnabled(false)`, the output contains no markers:

```java
public class MyClass {
    // custom fields
}
```

With `protectedAreaEnabled(true)` (default), the output includes markers for merge:

```java
public class MyClass {
    // [protected 'fields' a1b2c3d4]
    // custom fields
    // [/protected]
}
```

Use `protectedAreaEnabled(false)` when processing untrusted templates or when protected area merging is not needed.

---

## 11. Standard Library

MOFM2T defines 13 string operations (§8.3) in addition to the full OCL standard library. These are automatically available in all templates:

| Operation | Signature | Description | Example |
|-----------|-----------|-------------|---------|
| `substitute` | `substitute(s, r) : String` | Replace all occurrences of `s` with `r` | `'test'.substitute('es', 'se')` = `tset` |
| `index` | `index(s) : Integer` | 1-based position of first occurrence | `'test'.index('es')` = `2` |
| `first` | `first(n) : String` | First `n` characters | `'hello'.first(3)` = `hel` |
| `last` | `last(n) : String` | Last `n` characters | `'hello'.last(3)` = `llo` |
| `strstr` | `strstr(s) : Boolean` | Contains check | `'test'.strstr('es')` = `true` |
| `strcmp` | `strcmp(s) : Integer` | Lexicographic comparison (0 = equal) | `'test'.strcmp('test')` = `0` |
| `isAlpha` | `isAlpha() : Boolean` | All characters are letters | `'abc'.isAlpha()` = `true` |
| `isAlphanum` | `isAlphanum() : Boolean` | All characters are letters or digits | `'abc1'.isAlphanum()` = `true` |
| `toUpper` | `toUpper() : String` | Convert to uppercase | `'Hello'.toUpper()` = `HELLO` |
| `toLower` | `toLower() : String` | Convert to lowercase | `'Hello'.toLower()` = `hello` |
| `toUpperFirst` | `toUpperFirst() : String` | Capitalize first character | `'test'.toUpperFirst()` = `Test` |
| `toLowerFirst` | `toLowerFirst() : String` | Lowercase first character | `'TEST'.toLowerFirst()` = `tEST` |
| `trim` | `trim() : String` | Remove leading/trailing whitespace | `' hi '.trim()` = `hi` |

All OCL String and Collection operations are also available:

```mtl
[comment]OCL String operations[/comment]
['hello'.concat(' world')/]           [comment]→ hello world[/comment]
['hello'.size()/]                     [comment]→ 5[/comment]
['hello'.substring(2, 4)/]            [comment]→ ell[/comment]

[comment]OCL Collection operations[/comment]
[p.eClassifiers->size()/]             [comment]→ 3[/comment]
[p.eClassifiers->first().name/]       [comment]→ ClasseA[/comment]
[p.eClassifiers->exists(c | c.name = 'ClasseA')/]  [comment]→ true[/comment]
[p.eClassifiers->select(c | c.name.startsWith('C'))->collect(c | c.name)/]
```

---

## 12. Error Handling

### 12.1 Parse Errors

`M2tParseException` is a checked exception carrying one diagnostic per problem:

```java
try {
    engine.parse(source, "MyTemplate");
} catch (M2tParseException e) {
    System.err.println(e.getMessage());
    for (Resource.Diagnostic error : e.getErrors()) {
        System.err.printf("  Line %d, Col %d: %s%n",
            error.getLine(), error.getColumn(), error.getMessage());
    }
}
```

**Positions are real for both kinds now** (#110). A syntax error carries what ANTLR reported; an unresolved name, unknown type or unknown metamodel carries the place it stands. `0:0` still means "position unknown", which is what a diagnostic reported without any parse context gets.

### 12.2 Execution Diagnostics

`M2tResult` contains diagnostics collected during template execution:

```java
M2tResult result = engine.execute(module, context);

if (!result.isSuccess()) {
    for (Diagnostic d : result.diagnostics()) {
        System.err.printf("[%s] %s%n",
            d.getSeverity() == Diagnostic.ERROR ? "ERROR" : "WARN",
            d.getMessage());
    }
}
```

### 12.3 Severity Levels

| Level | Value | Meaning |
|-------|:-----:|---------|
| `Diagnostic.OK` | 0 | Success |
| `Diagnostic.WARNING` | 1 | Warning (generation completed) |
| `Diagnostic.ERROR` | 2 | Error (generation may have partial output) |

### 12.4 Linking Warnings

The `link()` method returns warnings for unresolved cross-module references:

```java
List<String> warnings = engine.link(baseMod, childMod);
if (!warnings.isEmpty()) {
    warnings.forEach(w -> System.err.println("Link warning: " + w));
}
```

Common causes: private templates accessed from another module, typos in template names, missing module in `link()` call.

---

## 13. Calling Operations from a Template

`[book.displayLabel()/]` resolves in this order: an `EOperation` of the model first, then the OCL standard library, then registered custom operations.

The first of those has a catch worth knowing before you design around it. A **generated** EMF model implements its operations in the generated `Impl`, so the call lands in your Java method. A **dynamic** model — an `.ecore` loaded at runtime — has no such code, and the call only works if the operation carries an OCL body annotation and the package declares the delegate; see [OCL User Guide §9.4](ocl-user-guide.md#94-operations-of-dynamic-models). Without that, generation reports:

```
Operation 'displayLabel' has no implementation. Either the model is generated and
implements it, or the operation carries an OCL body annotation and the package
declares the delegate (see installDelegates())
```

For generator concerns — output paths, cross-references, diagrams — an `EOperation` is the wrong home anyway. Those belong to the generator, not to the metamodel, and are better registered as OCL custom operations (see [OCL User Guide §11](ocl-user-guide.md#11-custom-operations)): the receiver is the real `EObject`, the implementation is an ordinary Java lambda, and the model stays free of documentation concerns.

### 13.1 Java from a template

A generator usually needs things a template language cannot express: an output path derived from the model, a link relative to the document that carries it, a diagram written to disk. There is no M2T-specific extension point for that — a Java method becomes callable by being registered as an OCL operation, and M2T picks it up:

```java
OclConfiguration ocl = OclConfiguration.builder(new OclParserSupport())
        .customOperationsEnabled(true)        // required — see below
        .addOperationProvider(new DocOperations(outputDirectory))
        .build();

M2tEngine engine = M2tEngines.create(M2tConfiguration.builder(ocl)
        .packageRegistry(registry)
        .generationStrategy(new FileSystemGenerationStrategy(outputDirectory))
        .build());
```

```mtl
[file (b.docPath(), false)]
# [b.title/]

- Slug: [b.title.kebab()/]
- Author: [b.author.mdLinkFrom(b)/]
[/file]
```

**`customOperationsEnabled(true)` on the OCL configuration is not optional.** M2T sets the flag on the *evaluation options* it uses, but a provider registered on the configuration is active only when the flag is set in **both** places (D29 — custom operations are off by default, deliberately). Leave it out and the calls resolve to nothing: `[file (b.docPath(), false)]` writes a file literally named `OclInvalid`. The run fails and says why — `Unknown operation: docPath` — because M2T forwards what the OCL engine reports; before #114 it discarded those diagnostics and reported success over a broken document. See [OCL User Guide §11](ocl-user-guide.md#11-custom-operations) for how to write a provider, and register as many as you need.

The whole configuration above runs as `M2tDocGenerationExampleTest` in `org.eclipse.fennec.m2x.m2t.tests` — the model, the three operations, the template, and the files it writes. It is the executable version of this section, so the two cannot drift apart.

**What the receiver is** decides where an operation can be called, and that is the `ownerType`:

| `ownerType` | callable on |
|---|---|
| `ClassifierType` around an `EClass` | instances of that class and its subclasses — `[b.docPath()/]` |
| `PrimitiveType("String")` | attribute values — `[b.title.kebab()/]` |
| `PrimitiveType("Integer")` / `("Real")` / `("Boolean")` | the matching Java type; an Integer widens to Real |
| `AnyType` or `PrimitiveType("OclAny")` | anything — the catch-all |
| `ClassifierType` around `EcorePackage.Literals.EOBJECT` | every model element |

One method that makes sense for two receiver types is registered twice, once per type. The implementation is an ordinary lambda, so anything Java can reach is reachable — a static utility, a file written to disk, an SVG rendered by hand.

**Four things that will bite:**

| | |
|---|---|
| **A literal `[` cannot be written** | The lexer opens code mode on `[` with no escape, so neither `[text](url)` nor `![alt](url)` can be typed in template text. Let a Java operation return the finished link or image. A lone bracket: `['['/]`. |
| **Types must resolve** | Without `packageRegistry(…)`, a type name in the template degrades and `oclIsKindOf` stops discriminating — see [§3.5](#35-which-metamodels-the-engine-sees). |
| **An unset reference is your operation's problem** | M2T evaluates leniently, so a call on `null` reaches your lambda with a `null` receiver instead of failing — guard there, as `mdLinkFrom` does. Navigating *from* an unset reference (`b.author.name`) answers `null`, which renders as nothing and is reported as a warning. Neither writes `OclInvalid` into the document. |
| **An exception becomes a diagnostic** | The evaluator catches `RuntimeException`, reports it and yields `null`. An empty document usually means an operation threw — check `result.isSuccess()` and `result.diagnostics()`. |

**What a generation diagnostic looks like.** A problem found while generating names the place it happened, so a log is enough to find it:

```
librarydoc:7:11 [template main → books/anonymous-work.md] Null source for property 'name' — evaluated as null (lenient)
```

The unit comes first, then line and column of the expression inside it, then the template that was running and the document it was writing. The unit matters as soon as a module `extends` or `imports` another: several units contribute to one generation, and a bare line reads as the main file's. The template named is the one that was executing when the problem occurred, not the one the generation started from — so a problem inside an imported template names that template. The position also travels as `SourcePosition` in the diagnostic's data, for a consumer that would rather not read the text. Two cases have no position and say so by leaving it out: an expression the engine took from its cache — it belongs to a parse this run did not do — and an OCL expression that came from an `EAnnotation`, which has no template at all.

Operations of the same name are told apart by how many arguments the call passes, and then by receiver type. Among equally applicable candidates the first registered one answers, so registration order is what decides — not which provider it came from.

### 13.2 `allInstances()` and what it sees

`allInstances()` works in a template. What it answers with is the model the generation runs on:

| Situation | The extent |
|---|---|
| `self` is a model element in a resource | that resource — every root in it and everything they contain |
| `self` is a model element outside any resource | the containment tree of its root container |
| `self` is not a model element (a template called with a `String`, for instance) | the input elements the generation was started with |
| no input elements and no model context | none — `allInstances()` reports a missing extent instead of answering emptily |

The first two rows are what Acceleo does, because both sit on Eclipse OCL's extent map. The third goes beyond it deliberately: Eclipse OCL hands out an empty extent there, so `allInstances()` would quietly answer `0` inside a template with a string parameter — the kind of wrong answer nobody notices. Subtypes count in every row, so `Book.allInstances()` includes the instances of `Novel`.

```
[template public toc(s : Shelf)]
[Book.allInstances()->size()/] books in this catalogue
[/template]
```

Two things worth knowing: a model in *another* resource of the same `ResourceSet` is not in the extent — a generator generates from a model, not from everything that happens to be loaded — and the answer per class is collected once per model root and kept for the run, so asking inside a `[for]` loop costs one pass, not one per iteration. `maxCollectionSize` still caps the result.

---

## 14. Engine Lifetime and Module Retention

The engine remembers what it parsed: a module's parse result, whether it has been linked and normalized, and the indentation of its template invocations. That is what makes repeated executions cheap.

Those caches are keyed weakly, so a module is let go once you drop it. Garbage collection is a safety net, not a schedule — an engine that lives as long as the application and parses modules in a loop should say when it is done with one:

```java
Module module = engine.parse(source, "report");
engine.link(module);
engine.execute(module, context);

engine.release(module);      // this module is done
// engine.clearCaches();     // or: forget everything
```

After `release`, the engine no longer knows the module. Linking it again reports it as unknown until it is parsed again — `link` returns a warning per module it has no parse result for, instead of quietly reporting success.

---

## 15. M2tGenerationStrategy

`M2tGenerationStrategy` is the SPI for controlling where generated text goes. When no strategy is configured, the engine collects all output in memory (`M2tResult.generatedFiles()`).

### 15.1 Interface

```java
public interface M2tGenerationStrategy {

    // Create a writer for a file block
    Writer createWriter(String filePath, OpenModeKind mode, Charset charset);

    // Close a writer when the file block ends (default: writer.close())
    default void closeWriter(String filePath, Writer writer) { ... }

    // Read existing content for protected area merging (default: null)
    default String readExistingContent(String filePath, Charset charset) { ... }
}
```

### 15.2 Writing to the File System

`FileSystemGenerationStrategy` ships with the engine — there is no need to write one:

```java
import org.eclipse.fennec.m2x.m2t.engine.FileSystemGenerationStrategy;

M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .generationStrategy(new FileSystemGenerationStrategy(Path.of("/tmp/output")))
    .build();
```

Every `[file (...)]` block of the run is written below that directory after generation:

- missing parent directories are created;
- `[file (path, false)]` truncates an existing file, `[file (path, true)]` appends to it;
- existing content is read back for protected area merging (§10), except for append blocks — an appended file is extended, not regenerated, so merging its own content back in would duplicate it;
- a path that would escape the output directory is rejected with a `SecurityException`, including `..` segments, absolute paths and symlinks pointing outside (T-4);
- if one file cannot be written, the failure becomes an error diagnostic and the remaining files are still written. Check `result.isSuccess()`.

Files are handed to the strategy **after** protected area merging, not streamed during evaluation: the merger needs the complete generated *and* the complete existing content before it can decide anything.

### 15.3 In-Memory Mode (Default)

When no strategy is configured, the engine uses an internal `StringWriter` for each file block. All generated content is available in the result:

```java
M2tResult result = engine.execute(module, context);

Map<String, String> files = result.generatedFiles();
for (Map.Entry<String, String> entry : files.entrySet()) {
    System.out.printf("File: %s (%d chars)%n", entry.getKey(), entry.getValue().length());
}
```

### 15.4 Registration

Pass the strategy via `M2tConfiguration`:

```java
M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .generationStrategy(new FileSystemStrategy(Path.of("/tmp/output")))
    .build();
```

---

## 16. Whitespace Handling

MOFM2T §8.4 defines precise whitespace normalization rules. The engine supports three modes via `WhitespaceMode`:

### 16.1 Modes

| Mode | Description |
|------|-------------|
| `NONE` | No normalization — raw template output as written |
| `SPEC` | MOFM2T §8.4 strict mode with all spec rules including BOL `^` indicator |
| `ACCELEO` | Acceleo 3.7 compatible mode (default). Like SPEC but without BOL `^` support |

### 16.2 §8.4 Rules (SPEC and ACCELEO modes)

The whitespace normalizer applies these rules:

1. **Body-trimming** — The first and last newlines of template and multi-line block bodies are stripped
2. **Standalone block stripping** — When a block tag (`[for]`, `[if]`, `[/for]`, `[/if]`, etc.) is alone on a line, the whitespace in front of the tag is removed from the output. What happens to the newline that ends the tag line follows the shape of the block — see 16.2.2
3. **Default separator injection** — Standalone `[for]` blocks without an explicit `separator()` get a `"\n"` separator
4. **BOL indicator** (**SPEC mode only**) — The `^` character at the beginning of a line resets indentation to column 0. In ACCELEO mode, `^` is passed through as literal text (Acceleo 3.7 does not implement this feature)
5. **Indent propagation** — When a template invocation is indented, that indent is propagated to all subsequent lines of the invoked template's output

### 16.2.2 The Newline After a Standalone Block

§8.4 defines the body of the two block shapes differently, and that decides what becomes of
the newline ending the tail line:

| Shape | Body per §8.4 | Newline after the tail |
|-------|---------------|------------------------|
| multi-line (`[for]` … `[/for]` on their own lines) | starts on the line after the head, ends **excluding** the newline in front of the tail | **kept** — the construct already spent a newline in front of its tail |
| single-line (`[if (…)]text[/if]` on one line) | starts after the head's closing bracket, ends before the tail's opening bracket | **removed** — no newline was spent, so the tag line takes its own |

So a multi-line block leaves the line after it on its own line, while a single-line block on
a line by itself contributes only the text it produced:

```mtl
A
[for (a : EAttribute | c.eAttributes)]
  [a.name/];
[/for]
Z
```

produces `A\n  name;\n  salary;\nZ` — the `[for]` and `[/for]` tag lines are gone, every
line the body wrote keeps its newline. Every branch of an `[if]` behaves the same way,
whether the `if`, an `elseif` or the `else` is taken.

### 16.2.1 SPEC vs ACCELEO Differences

| Feature | SPEC | ACCELEO |
|---------|------|---------|
| Body-trimming | Yes | Yes |
| Standalone block stripping | Yes | Yes |
| Default separator injection | Yes | Yes |
| BOL indicator `^` | Yes — strips leading whitespace | No — literal passthrough |
| Indent propagation | Yes | Yes |

### 16.3 Configuration

```java
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;

M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .whitespaceMode(WhitespaceMode.SPEC)
    .build();
```

### 16.4 Example: Standalone Block Stripping

Given this template:

```mtl
[template public main(c : EClass)]
[file ('out', false)]
class [c.name/] {
[for (a : EAttribute | c.eAttributes)]
  [a.eType.name/] [a.name/];
[/for]
}
[/file]
[/template]
```

The `[for ...]` and `[/for]` lines are standalone (only whitespace + block tag). They are stripped entirely, producing clean output without extra blank lines:

```
class Employee {
  EString name;
  EDouble salary;
}
```

---

## 17. Generating from a bnd Build

The engine is also available as a bnd *external plugin*, `fennecM2T`, so a bnd project can
run templates as a build step instead of from Java — configured entirely through a
`-generate` instruction, with no Java in the generating project.

That has its own guide: **[M2T bnd Generator Guide](m2t-generator-guide.md)** — dependencies,
the instruction and its attributes, how metamodels are registered under their namespace URI,
and how the models are bound to `main`.
