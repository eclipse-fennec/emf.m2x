// Sync the curated, user-facing guides from ../docs into ./guides for VitePress.
//
// Single source of truth stays workspace/docs/. Publication is an explicit
// ALLOWLIST — internal dev docs (architecture, test plans, security analyses,
// spec-gap) are deliberately NOT published. Cross-links inside the guides that
// point at a NON-published doc are rewritten to the GitHub blob URL so they keep
// working instead of 404-ing on the site.
//
// ANCHORS. The guides are written to read on GitHub as well, so their in-page
// tables of contents use GitHub's heading slugs (`#1-overview`). VitePress mints
// different ids for the same headings — `_1-overview`, because a CSS id must not
// start with a digit, and `3.1 Foo` becomes `_3-1-foo` where GitHub writes
// `31-foo`. Those links therefore have to be translated on the way in, per
// target document; the right-hand outline works untouched because VitePress
// generates it from its own ids. An anchor that matches no heading of the target
// guide fails the sync instead of shipping a dead link.
import { readFileSync, writeFileSync, mkdirSync, rmSync } from 'node:fs';
import { dirname, join } from 'node:path';
import { fileURLToPath } from 'node:url';
import { GUIDES } from './guides.mjs';

const here = dirname(fileURLToPath(import.meta.url));
const srcDir = join(here, '..', 'docs'); // workspace/docs — source of truth
const outDir = join(here, 'docs', 'guides'); // VitePress content root

// Git ref used for the GitHub blob fallback links (internal docs are browsed on
// GitHub, not published). Deliberately NOT DOCS_BRANCH: that is the version
// segment of the URL (`snapshot`, `latest`) and `latest` is no branch. The
// repository root IS the workspace, so the docs live under `docs/`, not
// `workspace/docs/`.
const ref = process.env.DOCS_REF || 'snapshot';
const blobBase = `https://github.com/eclipse-fennec/emf.m2x/blob/${ref}/docs`;

const published = new Map(GUIDES.map((g) => [g.file, g.slug]));

// --- heading ids -----------------------------------------------------------

// @mdit-vue/shared's slugify, which is what VitePress stamps on its headings.
const rControl = /[\u0000-\u001f]/g;
const rSpecial = /[\s~`!@#$%^&*()\-_+=[\]{}|\\;:"'“”‘’<>,.?/]+/g;
const rCombining = /[\u0300-\u036f]/g;

function vitepressSlug(text) {
  return text
    .normalize('NFKD')
    .replace(rCombining, '')
    .replace(rControl, '')
    .replace(rSpecial, '-')
    .replace(/-{2,}/g, '-')
    .replace(/^-+|-+$/g, '')
    .replace(/^(\d)/, '_$1')
    .toLowerCase();
}

// What a heading reads as once the inline markup is rendered — that is what both
// slug flavours are computed from.
function headingText(raw) {
  return raw
    .replace(/`([^`]*)`/g, '$1')
    .replace(/\*\*([^*]*)\*\*/g, '$1')
    .replace(/\[([^\]]*)\]\([^)]*\)/g, '$1')
    .trim();
}

// Both flavours agree once everything but letters and digits is dropped, so this
// is the key an anchor is looked up by — no second slugifier to keep in step
// with GitHub's.
function anchorKey(anchor) {
  return anchor.toLowerCase().replace(/[^a-z0-9]/g, '');
}

// anchorKey -> VitePress id, for one document.
function anchorMap(md) {
  const body = md.replace(/^```[\s\S]*?^```/gm, '');
  const map = new Map();
  for (const match of body.matchAll(/^#{1,6}[ \t]+(.+?)[ \t]*$/gm)) {
    const text = headingText(match[1]);
    const id = vitepressSlug(text);
    const key = anchorKey(id);
    if (!map.has(key)) {
      map.set(key, id);
    } else if (map.get(key) !== id) {
      throw new Error(`two headings collide on anchor key '${key}': '${map.get(key)}' and '${id}'`);
    }
  }
  return map;
}

// --- link rewriting --------------------------------------------------------

const sources = new Map(GUIDES.map((g) => [g.file, readFileSync(join(srcDir, g.file), 'utf8')]));
const anchors = new Map([...sources].map(([file, md]) => [file, anchorMap(md)]));

const problems = [];

function resolveAnchor(inFile, targetFile, anchor) {
  const map = anchors.get(targetFile);
  const id = map.get(anchorKey(anchor));
  if (id) {
    return `#${id}`;
  }
  problems.push(
    `${inFile}: anchor '#${anchor}' matches no heading of ${targetFile}` +
      (targetFile === inFile ? '' : ' (the guide it links to)')
  );
  return `#${anchor}`;
}

// Rewrite `](target.md#anchor)`: published -> sibling route, others -> GitHub
// blob. Anchors are translated only for published targets; a blob link keeps the
// GitHub-style anchor it was written with.
function rewriteDocLinks(file, md) {
  return md.replace(/\]\((\.?\/?)([a-z0-9-]+)\.md(#[^)\s]*)?\)/gi, (_m, _prefix, name, hash = '') => {
    const target = `${name}.md`;
    if (published.has(target)) {
      const anchor = hash ? resolveAnchor(file, target, hash.slice(1)) : '';
      return `](./${published.get(target)}${anchor})`;
    }
    return `](${blobBase}/${target}${hash})`;
  });
}

// Rewrite `](#anchor)` — the in-page table of contents.
function rewriteOwnAnchors(file, md) {
  return md.replace(/\]\(#([^)\s]+)\)/g, (_m, anchor) => `](${resolveAnchor(file, file, anchor)})`);
}

rmSync(outDir, { recursive: true, force: true });
mkdirSync(outDir, { recursive: true });

for (const g of GUIDES) {
  const md = rewriteOwnAnchors(g.file, rewriteDocLinks(g.file, sources.get(g.file)));
  writeFileSync(join(outDir, `${g.slug}.md`), md, 'utf8');
  console.log(`synced ${g.file} -> guides/${g.slug}.md`);
}

if (problems.length > 0) {
  console.error(`\n${problems.length} unresolvable anchor(s):`);
  problems.forEach((p) => console.error(`  ${p}`));
  throw new Error('dead anchors in the published guides — fix the link or the heading');
}

console.log(`Done. ${GUIDES.length} guides (ref=${ref}).`);
