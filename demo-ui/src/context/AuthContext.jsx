import { createContext, useContext, useState, useEffect } from 'react'
import { secureStorage } from '../utils/secureStorage'

/* eslint-disable react-refresh/only-export-components */

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => secureStorage.getToken())
  const [user, setUser] = useState(() => secureStorage.getUser())

  const saveAuth = (jwt, userData) => {
    secureStorage.setToken(jwt)
    secureStorage.setUser(userData)
    setToken(jwt)
    setUser(userData)
  }

  const logout = () => {
    secureStorage.clear()
    setToken(null)
    setUser(null)
  }

  // Listen for auth:logout events from axios interceptor
  useEffect(() => {
    const handleLogout = () => {
      setToken(null)
      setUser(null)
    }
    
    window.addEventListener('auth:logout', handleLogout)
    return () => window.removeEventListener('auth:logout', handleLogout)
  }, [])

  return (
    <AuthContext.Provider value={{ token, user, saveAuth, logout, isLoggedIn: !!token }}>
      {children}
    </AuthContext.Provider>
  )
}

export function useAuth() {
  return useContext(AuthContext)
}
