// Secure storage utility using sessionStorage instead of localStorage
// to mitigate XSS attacks (tokens cleared when browser closes)

const STORAGE_KEY_PREFIX = 'demo_app_'

export const secureStorage = {
  setToken: (token) => {
    sessionStorage.setItem(`${STORAGE_KEY_PREFIX}token`, token)
  },

  getToken: () => {
    const token = sessionStorage.getItem(`${STORAGE_KEY_PREFIX}token`)
    // Handle null or undefined string
    if (!token || token === 'undefined' || token === 'null') {
      return null
    }
    return token
  },

  setUser: (user) => {
    sessionStorage.setItem(`${STORAGE_KEY_PREFIX}user`, JSON.stringify(user))
  },

  getUser: () => {
    const user = sessionStorage.getItem(`${STORAGE_KEY_PREFIX}user`)
    // Handle null, undefined string, or empty string
    if (!user || user === 'undefined' || user === 'null') {
      return null
    }
    try {
      return JSON.parse(user)
    } catch (e) {
      console.error('Failed to parse user from storage:', e)
      return null
    }
  },

  clear: () => {
    sessionStorage.removeItem(`${STORAGE_KEY_PREFIX}token`)
    sessionStorage.removeItem(`${STORAGE_KEY_PREFIX}user`)
  }
}
