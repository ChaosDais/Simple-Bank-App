// Prefixes API paths with VITE_API_URL in production; falls back to relative paths (Vite dev proxy) otherwise.
export const API_BASE_URL = import.meta.env.VITE_API_URL ?? ''

export function apiUrl(path) {
  return `${API_BASE_URL}${path}`
}
