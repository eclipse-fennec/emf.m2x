# QVT-O Example: Library → Catalog

A worked, plain-Java QVT-O example over real files (#206) — deliberately more than the minimal
case: an imported helper library, a guarded mapping, ordering, and two metamodels.

| | |
|---|---|
| `model/library.ecore`, `model/catalog.ecore` | the two metamodels |
| `model/library.xmi` | the instance model (one book has no lendable copy) |
| `transforms/Library2Catalog.qvto` | the transformation — imports the helper library |
| `transforms/CatalogFormat.qvto` | the helper library, a unit of its own |

Two ways to run it, from this directory:

- **`AdHocExample`** — parse and execute, no units anywhere: the helper library arrives as
  *source* through a resolver at parse time, the models are extents.
- **`CompiledUnitExample`** — compile once, store, prepare, execute: the library is compiled
  and stored first, the transformation pins its fingerprint, and after prepare no resolver is
  asked and no parser runs.

`QvtoExampleSmokeTest` runs both in CI, so the example cannot rot. The whole mechanism —
modes, store, prepare, fingerprints — is described in
[`docs/compiled-units-guide.md`](../docs/compiled-units-guide.md).
