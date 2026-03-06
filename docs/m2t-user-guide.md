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
13. [M2tGenerationStrategy](#13-m2tgenerationstrategy)
14. [Whitespace Handling](#14-whitespace-handling)

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
import org.eclipse.fennec.m2x.m2t.engine.M2tEngineImpl;
import org.eclipse.fennec.m2x.model.m2t.Module;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

// 1. Create engine
OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
M2tEngineImpl engine = new M2tEngineImpl(config);

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

### 3.1 Minimal

```java
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngineImpl;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;

OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
M2tEngineImpl engine = new M2tEngineImpl(config);
```

### 3.2 With Builder Options

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

M2tEngineImpl engine = new M2tEngineImpl(config);
```

### 3.3 Configuration Options

| Method | Default | Description |
|--------|---------|-------------|
| `defaultCharset(charset)` | UTF-8 | Charset for file output encoding |
| `whitespaceMode(mode)` | `ACCELEO` | Whitespace normalization mode (see [§14](#14-whitespace-handling)) |
| `generationStrategy(strategy)` | `null` (in-memory) | SPI for file output (see [§13](#13-m2tgenerationstrategy)) |
| `maxDiagnostics(int)` | 10,000 | Maximum diagnostics before truncation |
| `maxTemplateDepth(int)` | 1,000 | Maximum template invocation depth (recursion limit) |
| `maxForIterations(int)` | 1,000,000 | Maximum for-block iterations |
| `maxCrossProductSize(int)` | 1,000,000 | Maximum cross-product size for set-argument invocations |
| `maxOutputSize(long)` | 10,000,000 | Maximum total output size in characters (~10 MB), `0` for unlimited |
| `protectedAreaEnabled(boolean)` | `true` | Enable/disable protected area markers and merging (see [§10.5](#105-disabling-protected-areas)) |

### 3.4 OSGi (Declarative Services)

The M2T engine currently does not register a DS component. Use standalone instantiation in OSGi by creating the engine in your component's `@Activate` method:

```java
import org.eclipse.fennec.m2x.m2t.api.M2tConfiguration;
import org.eclipse.fennec.m2x.m2t.engine.M2tEngineImpl;
import org.eclipse.fennec.m2x.ocl.api.OclEngine;
import org.eclipse.fennec.m2x.ocl.api.OclConfiguration;
import org.eclipse.fennec.m2x.ocl.parser.OclParserSupport;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;

@Component
public class MyGenerator {

    private M2tEngineImpl engine;

    @Activate
    void activate() {
        OclConfiguration oclConfig = OclConfiguration.builder(new OclParserSupport()).build();
        M2tConfiguration config = M2tConfiguration.builder(oclConfig).build();
        engine = new M2tEngineImpl(config);
    }
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
[c.name/]                           [comment single property /]
[c.eAttributes->size()/]            [comment collection operation /]
[c.name.toUpper()/]                 [comment string operation /]
['hello'.concat(' world')/]         [comment string literal /]
```

### 4.4 For Block

Iterate over collections with optional separator, before, after, and guard:

```mtl
[comment simple iteration /]
[for (a : EAttribute | c.eAttributes)]
  [a.eType.name/] [a.name/];
[/for]

[comment with separator /]
[for (a : EAttribute | c.eAttributes) separator (', ')]
[a.name/]
[/for]

[comment with before/after /]
[for (c : EClassifier | p.eClassifiers) before ('[') separator (', ') after (']')]
[c.name/]
[/for]

[comment with guard — only matching elements /]
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
[comment this is a comment /]
```

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
[comment base.mtl /]
[module base(ecore)/]
[template public greet(e : EClass)]Hello[/template]
```

```mtl
[comment child.mtl /]
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
[comment base.mtl /]
[module base(ecore)/]
[template public render(e : EClass)]Base[/template]
[template public main(e : EClass)]
[file ('out.txt', false)]
[render(e)/]
[/file]
[/template]
```

```mtl
[comment child.mtl /]
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
[comment A /]
[template public render(e : EClass)]A[/template]

[comment B extends A /]
[template public render(e : EClass) overrides render][super/]+B[/template]

[comment C extends B /]
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
[comment base /]
[template public render(e : EClass)]Original[/template]

[comment child — only overrides for abstract classes /]
[template public render(e : EClass) overrides render ? (e.abstract)]Abstract: [e.name/][/template]
```

- Abstract class input: `Abstract: AbstractClass`
- Concrete class input: `Original` (fallback to base)

### 9.3 Parameter Type Narrowing

An override can narrow the parameter type. The override applies only when the actual argument matches the narrower type:

```mtl
[comment base — accepts EClassifier /]
[template public render(e : EClassifier)]Base[/template]

[comment child — narrowed to EDataType /]
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
[comment OCL String operations /]
['hello'.concat(' world')/]           [comment → hello world /]
['hello'.size()/]                     [comment → 5 /]
['hello'.substring(2, 4)/]            [comment → ell /]

[comment OCL Collection operations /]
[p.eClassifiers->size()/]             [comment → 3 /]
[p.eClassifiers->first().name/]       [comment → ClasseA /]
[p.eClassifiers->exists(c | c.name = 'ClasseA')/]  [comment → true /]
[p.eClassifiers->select(c | c.name.startsWith('C'))->collect(c | c.name)/]
```

---

## 12. Error Handling

### 12.1 Parse Errors

`M2tParseException` is a checked exception with line/column information:

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

## 13. M2tGenerationStrategy

`M2tGenerationStrategy` is the SPI for controlling where generated text goes. When no strategy is configured, the engine collects all output in memory (`M2tResult.generatedFiles()`).

### 13.1 Interface

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

### 13.2 File-System Strategy

A simple implementation that writes to the filesystem:

```java
import java.io.FileWriter;
import java.io.Writer;
import java.nio.charset.Charset;
import java.nio.file.Files;
import java.nio.file.Path;
import org.eclipse.fennec.m2x.m2t.api.M2tGenerationStrategy;
import org.eclipse.fennec.m2x.model.m2t.OpenModeKind;

public class FileSystemStrategy implements M2tGenerationStrategy {

    private final Path outputDir;

    public FileSystemStrategy(Path outputDir) {
        this.outputDir = outputDir;
    }

    @Override
    public Writer createWriter(String filePath, OpenModeKind mode, Charset charset) {
        Path target = outputDir.resolve(filePath);
        target.getParent().toFile().mkdirs();
        boolean append = mode == OpenModeKind.APPEND;
        return new FileWriter(target.toFile(), charset, append);
    }

    @Override
    public String readExistingContent(String filePath, Charset charset) {
        Path target = outputDir.resolve(filePath);
        if (Files.exists(target)) {
            return Files.readString(target, charset);
        }
        return null;
    }
}
```

### 13.3 In-Memory Mode (Default)

When no strategy is configured, the engine uses an internal `StringWriter` for each file block. All generated content is available in the result:

```java
M2tResult result = engine.execute(module, context);

Map<String, String> files = result.generatedFiles();
for (Map.Entry<String, String> entry : files.entrySet()) {
    System.out.printf("File: %s (%d chars)%n", entry.getKey(), entry.getValue().length());
}
```

### 13.4 Registration

Pass the strategy via `M2tConfiguration`:

```java
M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .generationStrategy(new FileSystemStrategy(Path.of("/tmp/output")))
    .build();
```

---

## 14. Whitespace Handling

MOFM2T §8.4 defines precise whitespace normalization rules. The engine supports three modes via `WhitespaceMode`:

### 14.1 Modes

| Mode | Description |
|------|-------------|
| `NONE` | No normalization — raw template output as written |
| `SPEC` | MOFM2T §8.4 strict mode with all spec rules including BOL `^` indicator |
| `ACCELEO` | Acceleo 3.7 compatible mode (default). Like SPEC but without BOL `^` support |

### 14.2 §8.4 Rules (SPEC and ACCELEO modes)

The whitespace normalizer applies these rules:

1. **Body-trimming** — The first and last newlines of template and multi-line block bodies are stripped
2. **Standalone block stripping** — When a block tag (`[for]`, `[if]`, `[/for]`, `[/if]`, etc.) is alone on a line, the entire line (including trailing newline) is removed from output
3. **Default separator injection** — Standalone `[for]` blocks without an explicit `separator()` get a `"\n"` separator
4. **BOL indicator** (**SPEC mode only**) — The `^` character at the beginning of a line resets indentation to column 0. In ACCELEO mode, `^` is passed through as literal text (Acceleo 3.7 does not implement this feature)
5. **Indent propagation** — When a template invocation is indented, that indent is propagated to all subsequent lines of the invoked template's output

### 14.2.1 SPEC vs ACCELEO Differences

| Feature | SPEC | ACCELEO |
|---------|------|---------|
| Body-trimming | Yes | Yes |
| Standalone block stripping | Yes | Yes |
| Default separator injection | Yes | Yes |
| BOL indicator `^` | Yes — strips leading whitespace | No — literal passthrough |
| Indent propagation | Yes | Yes |

### 14.3 Configuration

```java
import org.eclipse.fennec.m2x.m2t.api.WhitespaceMode;

M2tConfiguration config = M2tConfiguration.builder(oclConfig)
    .whitespaceMode(WhitespaceMode.SPEC)
    .build();
```

### 14.4 Example: Standalone Block Stripping

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
