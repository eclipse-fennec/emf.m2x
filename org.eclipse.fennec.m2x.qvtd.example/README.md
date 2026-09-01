# QVT-R Example: Library ↔ Inventory

A worked, plain-Java QVT-R example over real files (#207) — deliberately more than the minimal
case: keys, a query, a where-chained relation and a when-guard.

| | |
|---|---|
| `model/library.ecore`, `model/inventory.ecore` | the two metamodels |
| `model/library.xmi` | the instance model (one book has no lendable copy) |
| `transforms/Library2Inventory.qvtr` | the transformation — `key Record {label}`, queries `label` and `lendable`, `LibraryToInventory` where-chains `BookToRecord`, whose when-guard drops the manuscript |

Two ways to run it, from this directory:

- **`AdHocExample`** — parse and enforce, no units anywhere.
- **`CompiledUnitExample`** — compile once, store, prepare, enforce: after compile no parser
  runs, and the preparer's contributions decide which metamodel instances the prepared
  transformation binds to.

`QvtdExampleSmokeTest` runs both in CI, so the example cannot rot. The whole mechanism is
described in [`docs/compiled-units-guide.md`](../docs/compiled-units-guide.md).

Building this example surfaced and fixed two store bugs: #230 (same-document forward references
to relations never resolved — reflective fragments now) and #231 (a file-loaded metamodel was
referenced by its absolute file path — by nsURI now).
