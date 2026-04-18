// Secure storage utility using sessionStorage instead of localStorage
// to mitigate XSS attacks (tokens cleared when browser closes)

const STORAGE_KEY_PREFIX = 'demo_app_'

export const secureStorage = {
  setToken: (token) => {
    sessionStorage.setItem(`${STORAGE_KEY_PREFIX}token`, token)
  },

  getToken: () => {
    return sessionStorage.getItem(`${STORAGE_KEY_PREFIX}token`)
  },

  setUser: (user) => {
    sessionStorage.setItem(`${STORAGE_KEY_PREFIX}user`, JSON.stringify(user))
  },

  getUser: () => {
    const user = sessionStorage.getItem(`${STORAGE_KEY_PREFIX}user`)
    return user ? JSON.parse(user) : null
  },

  clear: () => {
    sessionStorage.removeItem(`${STORAGE_KEY_PREFIX}token`)
    sessionStorage.removeItem(`${STORAGE_KEY_PREFIX}user`)
  }
}
