import { renderHook, act } from '@testing-library/react'
import { AuthProvider, useAuth } from '../../context/AuthContext'

describe('AuthContext', () => {
  beforeEach(() => {
    sessionStorage.clear()
    jest.clearAllMocks()
  })

  it('should provide initial auth state', () => {
    const { result } = renderHook(() => useAuth(), {
      wrapper: AuthProvider,
    })

    expect(result.current.token).toBeNull()
    expect(result.current.user).toBeNull()
    expect(result.current.isLoggedIn).toBe(false)
  })

  it('should save auth data', () => {
    const { result } = renderHook(() => useAuth(), {
      wrapper: AuthProvider,
    })

    const mockUser = { id: 1, email: 'test@test.com', admin: true }

    act(() => {
      result.current.saveAuth(mockUser)
    })

    expect(result.current.token).toBe(true)
    expect(result.current.user).toEqual(mockUser)
    expect(result.current.isLoggedIn).toBe(true)
  })

  it('should logout and clear auth data', () => {
    const { result } = renderHook(() => useAuth(), {
      wrapper: AuthProvider,
    })

    act(() => {
      result.current.saveAuth({ id: 1, email: 'test@test.com' })
    })

    act(() => {
      result.current.logout()
    })

    expect(result.current.token).toBeNull()
    expect(result.current.user).toBeNull()
    expect(result.current.isLoggedIn).toBe(false)
  })

  it('should restore auth from sessionStorage', () => {
    const mockUser = { id: 2, email: 'stored@test.com' }

    sessionStorage.getItem = jest.fn((key) => {
      if (key === 'demo_app_user') return JSON.stringify(mockUser)
      return null
    })

    const { result } = renderHook(() => useAuth(), {
      wrapper: AuthProvider,
    })

    expect(result.current.user).toEqual(mockUser)
    expect(result.current.isLoggedIn).toBe(true)
  })
})
