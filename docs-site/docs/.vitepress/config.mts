import { defineConfig } from 'vitepress'
import { GUIDES } from '../../guides.mjs'

// Per-project docs are served under a versioned sub-path, matching the org
// convention (https://eclipse-fennec.github.io/<repo>/<version>/). The snapshot
// branch publishes to /emf.m2x/snapshot/; tagged releases / `latest` get added
// once the first release lands.
const version = process.env.DOCS_BRANCH || 'snapshot'
const base = `/emf.m2x/${version}/`

// Version selector. Only `snapshot` is deployed today; keep as data so adding
// `latest` and tagged versions later is a one-liner.
const versions = [{ text: 'snapshot', link: '/emf.m2x/snapshot/' }]

const guideItems = GUIDES.map((g) => ({ text: g.title, link: `/guides/${g.slug}` }))

export default defineConfig({
  title: 'Fennec M2X',
  description:
    'Lightweight, spec-compliant OCL, QVT-O, QVT-R and MOFM2T engines for EMF — decoupled from the Eclipse platform.',
  lang: 'en-US',
  base,
  cleanUrls: true,
  lastUpdated: true,
  ignoreDeadLinks: true,

  markdown: {
    // Shiki has no grammars for the OMG transformation languages. QVT-O/QVT-R
    // are OCL-derived and read reasonably under the Java grammar; Acceleo MTL
    // templates are markup-like, so leave them unhighlighted rather than mangled.
    languageAlias: { qvto: 'java', qvtr: 'java', mtl: 'html' },
  },

  head: [
    ['link', { rel: 'icon', type: 'image/png', href: `${base}fennec-logo.png` }],
    ['meta', { name: 'theme-color', content: '#c0631c' }],
    ['meta', { property: 'og:type', content: 'website' }],
    ['meta', { property: 'og:title', content: 'Fennec M2X' }],
    [
      'meta',
      {
        property: 'og:description',
        content:
          'Lightweight OCL, QVT-O, QVT-R and MOFM2T engines for EMF — no Eclipse platform required.',
      },
    ],
  ],

  themeConfig: {
    logo: '/fennec-logo.png',
    siteTitle: 'Fennec M2X',

    nav: [
      { text: 'Home', link: '/' },
      { text: 'Guides', items: guideItems },
      {
        text: 'Eclipse Update Site',
        link: `/emf.m2x/ocl/${version}/p2/`,
      },
      { text: `version: ${version}`, items: versions },
    ],

    sidebar: {
      '/guides/': [{ text: 'User Guides', items: guideItems }],
    },

    socialLinks: [{ icon: 'github', link: 'https://github.com/eclipse-fennec/emf.m2x' }],

    search: { provider: 'local' },

    editLink: {
      pattern: 'https://github.com/eclipse-fennec/emf.m2x/edit/main/workspace/docs/:path',
      text: 'Edit this page on GitHub',
    },

    footer: {
      message:
        'Released under the EPL-2.0 License. Eclipse Fennec is part of the Eclipse Foundation.',
      copyright: 'Copyright © Eclipse Foundation and contributors',
    },
  },
})
