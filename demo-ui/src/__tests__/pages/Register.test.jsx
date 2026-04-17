import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Register from '../../pages/Register'
import { AuthProvider } from '../../context/AuthContext'
import { mockApi } from '../../__test-mocks__/apiMocks'

const queryClient = new QueryClient({
  defaultOptions: { queries: { retry: false } },
})

const renderRegister = () => {
  return render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Register />
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

describe('Register', () => {
  beforeEach(() => {
    jest.clearAllMocks()
  })

  it('should render register form', () => {
    renderRegister()

    expect(screen.getByText('Create Account')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('First Name')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Last Name')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Email')).toBeInTheDocument()
    expect(screen.getByPlaceholderText('Password')).toBeInTheDocument()
  })

  it('should handle successful registration', async () => {
    const user = userEvent.setup()
    const mockResponse = { data: { id: 1, email: 'test@test.com' } }
    mockApi.register.mockResolvedValue(mockResponse)

    renderRegister()

    await user.type(screen.getByPlaceholderText('First Name'), 'John')
    await user.type(screen.getByPlaceholderText('Last Name'), 'Doe')
    await user.type(screen.getByPlaceholderText('Email'), 'test@test.com')
    await user.type(screen.getByPlaceholderText('Password'), 'Password1!')
    await user.click(screen.getByRole('button', { name: /register/i }))

    await waitFor(() => {
      expect(mockApi.register).toHaveBeenCalled()
    })
  })

  it('should display error on failed registration', async () => {
    const user = userEvent.setup()
    mockApi.register.mockRejectedValue({
      response: { data: { message: 'Email already exists' } },
    })

    renderRegister()

    await user.type(screen.getByPlaceholderText('First Name'), 'John')
    await user.type(screen.getByPlaceholderText('Last Name'), 'Doe')
    await user.type(screen.getByPlaceholderText('Email'), 'existing@test.com')
    await user.type(screen.getByPlaceholderText('Password'), 'Password1!')
    await user.click(screen.getByRole('button', { name: /register/i }))

    await waitFor(() => {
      expect(screen.getByText('Email already exists')).toBeInTheDocument()
    })
  })

  it('should have link to login page', () => {
    renderRegister()

    const loginLink = screen.getByText('Login')
    expect(loginLink).toBeInTheDocument()
    expect(loginLink.closest('a')).toHaveAttribute('href', '/login')
  })
})
