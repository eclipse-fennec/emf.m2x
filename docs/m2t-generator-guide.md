# M2T bnd Generator Guide

`fennecM2T` is a bnd *external plugin* that runs MOFM2T v1.0 templates as a build step. A
bnd project declares a `-generate` instruction, and the templates turn models into files —
code, documentation, configuration, whatever the template writes — without a line of Java
in the generating project.

The engine behind it is the one described in the [M2T Engine User Guide](m2t-user-guide.md);
this guide is about driving it from a build.

## Table of Contents

1. [Dependencies](#1-dependencies)
2. [Quick Start](#2-quick-start)
3. [The Instruction](#3-the-instruction)
4. [Metamodels and Namespace URIs](#4-metamodels-and-namespace-uris)
5. [How the Models Reach `main`](#5-how-the-models-reach-main)
6. [Several Generations in One Project](#6-several-generations-in-one-project)
7. [Protected Areas and `clear`](#7-protected-areas-and-clear)
8. [Diagnostics](#8-diagnostics)
9. [The Example Project](#9-the-example-project)

---

## 1. Dependencies

**Requirements:** bnd 7.2 or later, Java 21.

The generator bundle is **self-contained**. bnd loads an external plugin with a plain class
loader over one JAR and does not read `Bundle-ClassPath`, so everything the generator needs
is inside it: EMF, the ANTLR runtime and the M2T and OCL engines. Nothing has to be
installed next to it, and no Eclipse Platform is involved.

### 1.1 With the fennecM2X library

If the workspace already uses the M2X bnd library, the generator is part of it and there is
nothing to add:

```
# cnf/build.bnd
-library: fennecM2X
```

The library contributes the repository that carries the generator, so `generate=fennecM2T`
resolves. The library itself comes from
`org.eclipse.fennec.m2x:org.eclipse.fennec.m2x.library.workspace`.

### 1.2 Without the library

bnd finds an external plugin through the capability index of the workspace repositories, so
it is enough for the bundle to be in one of them. Add **one** artifact to the Maven index:

```
# cnf/central.mvn
org.eclipse.fennec.m2x:org.eclipse.fennec.m2x.m2t.generator:<version>
```

with a repository plugin that reads that index — the standard bnd setup:

```
# cnf/build.bnd
-plugin.Central: \
    aQute.bnd.repository.maven.provider.MavenBndRepository; \
        releaseUrl=https://repo.maven.apache.org/maven2/; \
        snapshotUrl="https://central.sonatype.com/repository/maven-snapshots/"; \
        index=${.}/central.mvn; \
        readOnly=true; \
        name="Central"
```

### 1.3 What is *not* needed

| Not needed | Why |
|------------|-----|
| `org.eclipse.emf.*`, `org.antlr.antlr4-runtime` | inside the generator JAR |
| `…m2t.api`, `…m2t.model`, `…m2t.parser`, `…m2t.engine`, the OCL bundles | inside the generator JAR |
| anything on `-buildpath` | a project that only generates compiles nothing against M2T |

`-buildpath` only comes into play if the **generated code** references an API — then it is a
dependency of that code, not of the generator.

### 1.4 Generating inside the workspace that builds the generator

A `-generate` instruction creates no build order, so a project generating with a plugin from
its own workspace has to say so:

```
-dependson: org.eclipse.fennec.m2x.m2t.generator
```

Outside that workspace this is unnecessary — the plugin comes from a repository.

## 2. Quick Start

A project with two metamodels, one model and one template:

```
model/addressbook.ecore
model/contact.ecore
model/addressbook.xmi
templates/vcard.mtl
```

```
# bnd.bnd
-resourceonly: true

-generate: \
    "model/*.ecore,model/*.xmi,templates/vcard.mtl"; \
        output   = gen-resources/; \
        generate = fennecM2T; \
        ecore    = "model/addressbook.ecore,model/contact.ecore"; \
        model    = "model/addressbook.xmi"; \
        template = "templates/vcard.mtl"

-includeresource: vcards=gen-resources/vcards
```

`./gradlew build` — or a build in the IDE — runs the templates and puts what they wrote into
`gen-resources/`.

## 3. The Instruction

The key in front of the attributes is the fileset bnd watches for changes. Metamodels,
models and templates all belong in it, or editing one of them does not trigger a
regeneration. **It has to be quoted**, because an unquoted comma starts a second instruction
entry.

| Attribute | Meaning |
|-----------|---------|
| `generate` | `fennecM2T` — the plugin to invoke; required by bnd |
| `output` | where generated files go; defaults to `src-gen`. Required by bnd anyway |
| `template` | the module that holds the `main` template; required |
| `modules` | further modules to link, for `extends` and `import` |
| `ecore` | the metamodels; every `EPackage` in them is registered under its nsURI |
| `model` | the instance models handed to `main` |
| `charset` | encoding of templates and generated files; defaults to UTF-8 |
| `whitespace` | `NONE`, `SPEC` or `ACCELEO`; defaults to `ACCELEO` |
| `protectedAreas` | whether `[protected]` blocks are merged; defaults to `true` |

Every file attribute takes a comma separated list, and each entry is either a path or a bnd
fileset such as `templates/**/*.mtl`. Paths keep the order they are written in, matches of a
fileset are sorted — the order matters, because the models are bound to the parameters of
`main` in it.

Attribute values are read from the instruction, not from the project: an attribute that is
absent stays absent, even if the project or workspace defines a property of the same name.

## 4. Metamodels and Namespace URIs

An `.ecore` file in a project is a *location*. Everything else addresses a metamodel by its
*namespace URI*: the module declaration names it, an instance model names it, and the type
names in a template are resolved against an `EPackage.Registry`.

```mtl
[module vcard(_'http://www.eclipse.org/fennec/m2x/example/addressbook/1.0',
              _'http://www.eclipse.org/fennec/m2x/example/contact/1.0')/]
[template public main(book : AddressBook)]
```

So the generator loads every file named by `ecore` from its file URI and registers each
`EPackage` in it — root packages **and subpackages** — under its nsURI, before parsing
anything. In addition the nsURI of each root package is mapped to its file, so a cross
reference between two metamodels resolves whether it is written as a relative path
(`contact.ecore#//Address`) or as a namespace URI.

That is what lets an instance model use `xsi:type="contact:PostalAddress"`, a template name
a type from a second metamodel, or an enum literal from a subpackage — with nothing
installed anywhere.

Everything is loaded into one `ResourceSet` whose package registry has **no** delegate to
`EPackage.Registry.INSTANCE`. The generator runs inside the build tool, where the global
registry is neither ours nor predictable: in the IDE an Eclipse-hosted EMF sits on the
parent class loader with a different `EPackage` class. Only what `ecore` names is visible,
plus Ecore and XMLType themselves.

## 5. How the Models Reach `main`

If `main` declares a **single parameter**, it is executed once per model root — the usual
case, one generation per input model, and the way to generate from several models in one
step is to list them all under `model`.

Otherwise the roots are bound to the parameters **positionally**, in the order the models
were written. That is how a template that needs a second input model gets it:

```mtl
[template public main(book : AddressBook, config : Settings)]
```

```
model = "model/addressbook.xmi,model/settings.xmi"
```

## 6. Several Generations in One Project

`-generate` is a merged instruction, so suffixing the name adds another generation — code
into one directory, documentation into another:

```
-generate.doc: \
    "model/*.ecore,templates/addressbook-doc.mtl"; \
        output   = gen-doc/; \
        generate = fennecM2T; \
        ecore    = "model/*.ecore"; \
        model    = "model/addressbook.xmi"; \
        template = "templates/addressbook-doc.mtl"
```

Give each its own `output`: bnd empties the output directory of a generation before it runs,
so two generations sharing one directory would delete each other's work.

## 7. Protected Areas and `clear`

That same emptying is what `[protected]` blocks cannot survive — the hand-written content
would be deleted before the generator can read it back. A generation with protected areas
has to turn it off:

```
-generate: \
    "model/**,templates/**"; \
        output         = src-gen/; \
        clear          = false; \
        generate       = fennecM2T; \
        …
```

`protectedAreas=false` does the opposite: `[protected]` blocks emit their body without
markers and nothing is merged.

## 8. Diagnostics

Errors from configuration, parsing and execution are reported as bnd errors on the
`-generate` instruction, all of them at once rather than one per run:

```
bnd.bnd:0: error: fennecM2T : 'ecore': no such file …/model/missing.ecore
'template': no such file …/templates/vcards.mtl
```

Runtime warnings — an unresolved `extends`, a navigation that found nothing, a namespace URI
declared by two files — become bnd warnings and do not fail the build. Which nsURI was
registered from which file is reported as bnd trace output.

## 9. The Example Project

`org.eclipse.fennec.m2x.m2t.generator.example` in this repository is a working setup and the
integration test for the generator: two metamodels, an instance model that uses `xsi:type`
from the second of them and an enum from a subpackage, generated once into vCard files and
once into a Markdown document. The example is deliberately **not** part of the fennecM2X
library — it is a reference to read, not a dependency to take.
