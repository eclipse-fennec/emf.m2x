# M2T Example: Library → Catalog Files

A worked, plain-Java MOFM2T example over real files (#208) — deliberately more than the minimal
case: a module import, for/if blocks, both file open modes, and a protected area whose content
survives regeneration. Complements `m2t.generator.example`, which shows the bnd `-generate`
path and carries no Java at all.

| | |
|---|---|
| `model/library.ecore`, `model/library.xmi` | metamodel and instance model |
| `templates/catalog.mtl` | the main module — imports `format`, writes `catalog.md` (overwrite) with a protected area, appends to `runs.log` |
| `templates/format.mtl` | the imported module — a query and a template |

Two ways to run it, from this directory (generation goes to `gen/`):

- **`AdHocExample`** — parse and generate; the format module arrives as source through a
  resolver. Run it twice and edit the text inside `[protected ('curator-notes')]` in between —
  your edit survives, the generated part is regenerated, and `runs.log` grows by one line.
- **`CompiledUnitExample`** — compile both modules, store, prepare, generate: after compile no
  resolver is asked and no parser runs.

`M2tExampleSmokeTest` runs both in CI — including the edit-and-regenerate round trip for the
protected area. The unit mechanism is described in
[`docs/compiled-units-guide.md`](../docs/compiled-units-guide.md).
