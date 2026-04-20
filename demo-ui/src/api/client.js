import axios from 'axios'
import { secureStorage } from '../utils/secureStorage'

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || 'http://localhost:8080'
const API_VERSION = import.meta.env.VITE_API_VERSION || 'v1'

// Helper function to build versioned API path
const apiPath = (endpoint) => `/api/${API_VERSION}${endpoint}`

const api = axios.create({ baseURL: API_BASE_URL })

// Attach JWT Bearer token to every request if present
api.interceptors.request.use(
  (config) => {
    const token = secureStorage.getToken()
    
    // Only add Bearer token if:
    // 1. Token exists in sessionStorage
    // 2. Authorization header is not already set (e.g., for Basic auth in login)
    if (token && !config.headers.Authorization) {
      config.headers.Authorization = `Bearer ${token}`
    }
    
    return config
  },
  (error) => {
    return Promise.reject(error)
  }
)

// Response interceptor to handle 401 errors (expired/invalid token) and unwrap ApiResponse
api.interceptors.response.use(
  (response) => {
    // Unwrap ApiResponse wrapper - extract the data field
    // Backend now wraps all responses in ApiResponse<T>
    if (response.data && typeof response.data === 'object' && 'data' in response.data) {
      // This is an ApiResponse wrapper, extract the actual data
      return {
        ...response,
        data: response.data.data,
        // Keep metadata for debugging if needed
        _meta: {
          message: response.data.message,
          success: response.data.success,
          timestamp: response.data.timestamp,
          requestId: response.data.requestId
        }
      }
    }
    return response
  },
  (error) => {
    if (error.response?.status === 401) {
      // Don't redirect if already on login page
      if (window.location.pathname !== '/login') {
        // Token is expired or invalid - clear auth data and redirect to login
        console.warn('Unauthorized request - token expired or invalid, redirecting to login')
        secureStorage.clear()
        
        // Dispatch custom event for AuthContext to handle
        window.dispatchEvent(new Event('auth:logout'))
        
        // Redirect to login page
        window.location.href = '/login'
      }
    }
    return Promise.reject(error)
  }
)

export default api

// Auth
export const register = (data) => api.post(apiPath('/auth/register'), data)
export const login = (email, password) =>
  api.post(apiPath('/auth/login'), { email, password })

// Accounts
export const getAccounts = (page = 0, size = 20) => api.get(apiPath(`/accounts?page=${page}&size=${size}`))
export const getAccount = (id) => api.get(apiPath(`/accounts/${id}`))
export const updateAccount = (id, data) => api.patch(apiPath(`/accounts/${id}`), data)
export const deleteAccount = (id) => api.delete(apiPath(`/accounts/${id}`))

// Policies
export const getPolicies = () => api.get(apiPath('/policies'))
export const getPolicy = (id) => api.get(apiPath(`/policies/${id}`))
export const createPolicy = (accountId, data) => api.post(apiPath(`/policies/${accountId}`), data)
export const updatePolicy = (id, data) => api.patch(apiPath(`/policies/${id}`), data)
export const deletePolicy = (id) => api.delete(apiPath(`/policies/${id}`))

// Claims
export const getClaims = (page = 0, size = 20) => api.get(apiPath(`/claims?page=${page}&size=${size}`))
export const getClaim = (id) => api.get(apiPath(`/claims/${id}`))
export const createClaim = (policyId, data) => api.post(apiPath(`/claims/${policyId}`), data)
export const updateClaim = (id, data) => api.patch(apiPath(`/claims/${id}`), data)
export const deleteClaim = (id) => api.delete(apiPath(`/claims/${id}`))

// Lines
export const getLines = () => api.get(apiPath('/lines'))
export const getLine = (id) => api.get(apiPath(`/lines/${id}`))
export const createLine = (data) => api.post(apiPath('/lines'), data)
export const updateLine = (id, data) => api.patch(apiPath(`/lines/${id}`), data)
export const deleteLine = (id) => api.delete(apiPath(`/lines/${id}`))

// Coverage
export const calculateCoverage = (accountId, lineId) => api.get(apiPath(`/coverage/calculate/${accountId}/${lineId}`))