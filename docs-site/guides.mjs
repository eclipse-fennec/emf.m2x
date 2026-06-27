// The published, user-facing guides (allowlist). Shared by the sync script and
// the VitePress config so the set and its order are defined exactly once.
//   file  — source markdown in ../docs
//   slug  — route name under /guides/
//   title — sidebar / nav label
export const GUIDES = [
  { file: 'ocl-user-guide.md', slug: 'ocl', title: 'OCL Engine' },
  { file: 'qvto-user-guide.md', slug: 'qvto', title: 'QVT-O Engine' },
  { file: 'qvtd-user-guide.md', slug: 'qvtd', title: 'QVT-R Engine' },
  { file: 'm2t-user-guide.md', slug: 'm2t', title: 'M2T Engine' },
  { file: 'ocl-ide-installation.md', slug: 'ide-installation', title: 'Eclipse IDE Installation' },
];
