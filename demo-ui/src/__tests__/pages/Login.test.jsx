import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Login from '../../pages/Login'
import { AuthProvider } from '../../context/AuthContext'
import * as api from '../../api/client'

jest.mock('../../api/client')

const queryClient = new QueryClient({
  defaultOptions: { queries: { retry: false } },
})

const renderLogin = () => {
  return render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Login />
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

describe('Login', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  it('should render login form', () => {
    renderLogin()

    expect(screen.getByText('Sign In')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Email')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument()
    expect(screen.getByRole('button', { name: /login/i })).toBeInTheDocument()
  })

  it('should handle successful login', async () => {
    const user = userEvent.setup()
    const mockResponse = {
      headers: { authorization: 'Bearer test-token' },
      data: { id: 1, email: 'test@test.com', admin: false },
    }

    api.login.mockResolvedValue(mockResponse)

    renderLogin()

    await user.type(screen.getByPlaceholderText('Email'), 'test@test.com')
    await user.type(screen.getByPlaceholderText('Password'), 'Password1!')
    await user.click(screen.getByRole('button', { name: /login/i }))

    await waitFor(() => {
      expect(api.login).toHaveBeenCalledWith('test@test.com', 'Password1!')
    })
  })

  it('should display error on failed login', async () => {
    const user = userEvent.setup()
    api.login.mockRejectedValue({
      response: { data: { Massege: 'Invalid credentials' } },
    })

    renderLogin()

    await user.type(screen.getByPlaceholderText('Email'), 'wrong@test.com')
    await user.type(screen.getByPlaceholderText('Password'), 'wrong')
    await user.click(screen.getByRole('button', { name: /login/i }))

    await waitFor(() => {
      expect(screen.getByText('Invalid credentials')).toBeInTheDocument()
    })
  })

  it('should have link to register page', () => {
    renderLogin()

    const registerLink = screen.getByText('Register')
    expect(registerLink).toBeInTheDocument()
    expect(registerLink.closest('a')).toHaveAttribute('href', '/register')
  })
})
