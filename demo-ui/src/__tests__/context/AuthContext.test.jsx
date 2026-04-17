import { renderHook, act } from '@testing-library/react'
import { AuthProvider, useAuth } from '../../context/AuthContext'

describe('AuthContext', () => {
  beforeEach(() => {
    localStorage.clear()
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

    const mockToken = 'test-token'
    const mockUser = { id: 1, email: 'test@test.com', admin: true }

    act(() => {
      result.current.saveAuth(mockToken, mockUser)
    })

    expect(result.current.token).toBe(mockToken)
    expect(result.current.user).toEqual(mockUser)
    expect(result.current.isLoggedIn).toBe(true)
    expect(localStorage.setItem).toHaveBeenCalledWith('token', mockToken)
    expect(localStorage.setItem).toHaveBeenCalledWith('user', JSON.stringify(mockUser))
  })

  it('should logout and clear auth data', () => {
    const { result } = renderHook(() => useAuth(), {
      wrapper: AuthProvider,
    })

    act(() => {
      result.current.saveAuth('token', { id: 1, email: 'test@test.com' })
    })

    act(() => {
      result.current.logout()
    })

    expect(result.current.token).toBeNull()
    expect(result.current.user).toBeNull()
    expect(result.current.isLoggedIn).toBe(false)
    expect(localStorage.removeItem).toHaveBeenCalledWith('token')
    expect(localStorage.removeItem).toHaveBeenCalledWith('user')
  })

  it('should restore auth from localStorage', () => {
    const mockToken = 'stored-token'
    const mockUser = { id: 2, email: 'stored@test.com' }

    localStorage.getItem.mockImplementation((key) => {
      if (key === 'token') return mockToken
      if (key === 'user') return JSON.stringify(mockUser)
      return null
    })

    const { result } = renderHook(() => useAuth(), {
      wrapper: AuthProvider,
    })

    expect(result.current.token).toBe(mockToken)
    expect(result.current.user).toEqual(mockUser)
    expect(result.current.isLoggedIn).toBe(true)
  })
})
