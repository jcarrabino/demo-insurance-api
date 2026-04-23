import { createContext, useContext, useState, useEffect } from 'react'
import { secureStorage } from '../utils/secureStorage'
import * as apiClient from '../api/client'

/* eslint-disable react-refresh/only-export-components */

const AuthContext = createContext(null)

export function AuthProvider({ children }) {
  const [token, setToken] = useState(() => secureStorage.getToken())
  const [user, setUser] = useState(() => secureStorage.getUser())

  const saveAuth = (userData) => {
    // Token is now stored in HttpOnly cookie, only store user data
    if (!userData) {
      console.warn('Invalid user data provided to saveAuth')
      return
    }
    secureStorage.setUser(userData)
    setToken(true) // Mark as authenticated
    setUser(userData)
  }

  const logout = async () => {
    try {
      // Call logout endpoint to clear the HttpOnly cookie
      await apiClient.logout()
    } catch (error) {
      console.error('Logout API call failed:', error)
    } finally {
      // Clear local state regardless of API success
      secureStorage.clear()
      setToken(null)
      setUser(null)
    }
  }

  // Listen for auth:logout events from axios interceptor
  useEffect(() => {
    const handleLogout = () => {
      secureStorage.clear()
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
