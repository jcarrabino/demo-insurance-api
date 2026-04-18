import axios from 'axios'

const api = axios.create({ baseURL: '' })

// Attach JWT Bearer token to every request if present
api.interceptors.request.use(
  (config) => {
    const token = localStorage.getItem('token')
    
    // Only add Bearer token if:
    // 1. Token exists in localStorage
    // 2. Authorization header is not already set (e.g., for Basic auth in login)
    if (token && !config.headers.Authorization) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    console.log('Request:', config.method.toUpperCase(), config.url, 'Headers:', config.headers)
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor to handle 401 errors (expired/invalid token)
api.interceptors.response.use(
  (response) => response,
  (error) => {
    if (error.response?.status === 401) {
      // Don't redirect if we're already on the login page or if this is a login request
      const isLoginRequest = error.config?.url === '/login'
      const isOnLoginPage = window.location.pathname === '/login' || window.location.pathname === '/'
      
      if (!isLoginRequest && !isOnLoginPage) {
        // Token is expired or invalid - clear auth data and redirect to home
        console.warn('Unauthorized request - token expired or invalid, redirecting to home')
        localStorage.removeItem('token')
        localStorage.removeItem('user')
        
        // Dispatch custom event for AuthContext to handle
        window.dispatchEvent(new Event('auth:logout'))
        
        // Redirect to home page
        window.location.href = '/'
      }
    }
    return Promise.reject(error)
  }
)

export default api

// Auth
export const register = (data) => api.post('/register', data)
export const login = (email, password) =>
  api.post('/login', { email, password })

// Accounts
export const getAccounts = () => api.get('/api/accounts/')
export const getAccount = (id) => api.get(`/api/accounts/${id}`)
export const updateAccount = (id, data) => api.post(`/api/accounts/update/${id}`, data)
export const deleteAccount = (id) => api.delete(`/api/accounts/${id}`)

// Policies
export const getPolicies = () => api.get('/api/policies/')
export const getPolicy = (id) => api.get(`/api/policies/${id}`)
export const createPolicy = (accountId, data) => api.post(`/api/policies/${accountId}`, data)
export const updatePolicy = (id, data) => api.post(`/api/policies/update/${id}`, data)
export const deletePolicy = (id) => api.delete(`/api/policies/${id}`)

// Claims
export const getClaims = () => api.get('/api/claims/')
export const getClaim = (id) => api.get(`/api/claims/${id}`)
export const createClaim = (policyId, data) => api.post(`/api/claims/${policyId}`, data)
export const updateClaim = (id, data) => api.post(`/api/claims/update/${id}`, data)
export const deleteClaim = (id) => api.delete(`/api/claims/${id}`)

// Lines
export const getLines = () => api.get('/api/lines/')
export const getLine = (id) => api.get(`/api/lines/${id}`)
export const createLine = (data) => api.post('/api/lines/', data)
export const updateLine = (id, data) => api.post(`/api/lines/update/${id}`, data)
export const deleteLine = (id) => api.delete(`/api/lines/${id}`)

// Coverage
export const calculateCoverage = (accountId, lineId) => api.get(`/api/coverage/calculate/${accountId}/${lineId}`)