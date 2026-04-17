import axios from 'axios'

const api = axios.create({ baseURL: '' })

// Attach JWT to every request if present
api.interceptors.request.use(config => {
  const token = localStorage.getItem('token')
  if (token) config.headers.Authorization = `Bearer ${token}`
  return config
})

export default api

// Auth
export const register = (data) => api.post('/register', data)
export const login = (email, password) =>
  api.get('/login', {
    headers: { Authorization: 'Basic ' + btoa(`${email}:${password}`) }
  })

// Accounts
export const getAccounts = () => api.get('/api/accounts/')
export const getAccount = (id) => api.get(`/api/accounts/${id}`)
export const updateAccount = (id, data) => api.put(`/api/accounts/${id}`, data)
export const deleteAccount = (id) => api.delete(`/api/accounts/${id}`)

// Policies
export const getPolicies = () => api.get('/api/policies/')
export const getPolicy = (id) => api.get(`/api/policies/${id}`)
export const createPolicy = (accountId, data) => api.post(`/api/policies/${accountId}`, data)
export const updatePolicy = (id, data) => api.put(`/api/policies/${id}`, data)
export const deletePolicy = (id) => api.delete(`/api/policies/${id}`)

// Claims
export const getClaims = () => api.get('/api/claims/')
export const getClaim = (id) => api.get(`/api/claims/${id}`)
export const createClaim = (policyId, data) => api.post(`/api/claims/${policyId}`, data)
export const updateClaim = (id, data) => api.put(`/api/claims/${id}`, data)
export const deleteClaim = (id) => api.delete(`/api/claims/${id}`)
