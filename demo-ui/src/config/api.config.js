/**
 * API Configuration
 * Centralized configuration for API endpoints and versioning
 */

export const API_CONFIG = {
  baseURL: import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080',
  version: import.meta.env.VITE_API_VERSION || 'v1',
  
  // Build full API path with version
  getPath: (endpoint) => `/api/${API_CONFIG.version}${endpoint}`,
  
  // API endpoints
  endpoints: {
    // Auth
    auth: {
      login: '/auth/login',
      register: '/auth/register',
    },
    
    // Accounts
    accounts: {
      list: '/accounts',
      get: (id) => `/accounts/${id}`,
      create: '/accounts',
      update: (id) => `/accounts/${id}`,
      delete: (id) => `/accounts/${id}`,
    },
    
    // Policies
    policies: {
      list: '/policies',
      get: (id) => `/policies/${id}`,
      create: (accountId) => `/policies/${accountId}`,
      update: (id) => `/policies/${id}`,
      delete: (id) => `/policies/${id}`,
    },
    
    // Claims
    claims: {
      list: '/claims',
      get: (id) => `/claims/${id}`,
      create: (policyId) => `/claims/${policyId}`,
      update: (id) => `/claims/${id}`,
      delete: (id) => `/claims/${id}`,
    },
    
    // Lines
    lines: {
      list: '/lines',
      get: (id) => `/lines/${id}`,
      create: '/lines',
      update: (id) => `/lines/${id}`,
      delete: (id) => `/lines/${id}`,
    },
    
    // Coverage
    coverage: {
      calculate: (accountId, lineId) => `/coverage/calculate/${accountId}/${lineId}`,
    },
  },
}

// Log configuration in development
if (import.meta.env.DEV) {
  console.log('API Configuration:', {
    baseURL: API_CONFIG.baseURL,
    version: API_CONFIG.version,
  })
}
