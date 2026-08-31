---
layout: home

hero:
  name: Fennec M2X
  text: Model transformation & constraints for EMF
  tagline: Lightweight, spec-compliant OCL, QVT-O, QVT-R and MOFM2T engines — standalone Java libraries, no Eclipse platform required.
  image:
    src: /fennec-logo.png
    alt: Eclipse Fennec logo
  actions:
    - theme: brand
      text: User Guides
      link: /guides/ocl
    - theme: alt
      text: Eclipse Update Site
      link: https://eclipse-fennec.github.io/emf.m2x/ocl/snapshot/p2/
    - theme: alt
      text: View on GitHub
      link: https://github.com/eclipse-fennec/emf.m2x

features:
  - icon: ✓
    title: OCL
    details: Object Constraint Language v2.5 (backward compatible with v2.4) — validation, queries and derived features over Ecore models.
    link: /guides/ocl
    linkText: OCL Engine Guide
  - icon: 🔁
    title: QVT-O
    details: QVT Operational v1.3 — imperative model-to-model transformations with mappings, traces and resolve.
    link: /guides/qvto
    linkText: QVT-O Engine Guide
  - icon: 🔗
    title: QVT-R
    details: QVT Relations v1.3 — declarative, bidirectional model-to-model transformations with relations and trace classes.
    link: /guides/qvtd
    linkText: QVT-R Engine Guide
  - icon: 📝
    title: M2T
    details: MOFM2T v1.0 model-to-text generation, compatible with Acceleo 3.7 templates — including protected areas.
    link: /guides/m2t
    linkText: M2T Engine Guide
---

## About Fennec M2X

Fennec M2X (`org.eclipse.fennec.m2x`) provides clean-room, specification-compliant
implementations of the OMG modeling-transformation stack — **OCL**, **QVT
Operational**, **QVT Relations** and **MOFM2T** — that run as plain Java 21
libraries and as OSGi services. Every engine works without the Eclipse platform.

### Compile once, execute often

QVT-O, QVT-R and MOFM2T units can be compiled into a storable document — the AST
together with its manifest, its dependencies and the fingerprints it was built
against — and later loaded, prepared and executed without a parser. How that
works, and what the dependency modes `embed`, `pin` and `rebind` mean, is
described once for all languages in the
[Compiled Units guide](/guides/compiled-units).

### Use it in the Eclipse IDE

The OCL engine ships as an Eclipse feature that adds OCL delegate support to the
generic EMF editor. Install it from the
[Eclipse update site](https://eclipse-fennec.github.io/emf.m2x/ocl/snapshot/p2/),
or follow the [installation guide](/guides/ide-installation).
