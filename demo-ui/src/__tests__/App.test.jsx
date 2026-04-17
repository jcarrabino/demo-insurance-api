import { render, screen } from '@testing-library/react'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import App from '../App'

const queryClient = new QueryClient({
  defaultOptions: { queries: { retry: false } },
})

const renderApp = () => {
  return render(
    <QueryClientProvider client={queryClient}>
      <App />
    </QueryClientProvider>
  )
}

describe('App', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  it('should render app with navigation', () => {
    renderApp()

    expect(screen.getByText('Insurance Management')).toBeInTheDocument()
  })

  it('should show login/register links when not logged in', () => {
    renderApp()

    expect(screen.getByText('Login')).toBeInTheDocument()
    expect(screen.getByText('Register')).toBeInTheDocument()
  })

  it('should show navigation links when logged in', () => {
    localStorage.setItem('token', 'test-token')
    localStorage.setItem('user', JSON.stringify({ id: 1, email: 'test@test.com', admin: false }))

    renderApp()

    expect(screen.getByText('Policies')).toBeInTheDocument()
    expect(screen.getByText('Claims')).toBeInTheDocument()
  })

  it('should show admin links for admin users', () => {
    localStorage.setItem('token', 'test-token')
    localStorage.setItem('user', JSON.stringify({ id: 1, email: 'admin@test.com', admin: true }))

    renderApp()

    expect(screen.getByText('Accounts')).toBeInTheDocument()
    expect(screen.getByText('Lines')).toBeInTheDocument()
    expect(screen.getByText('Coverage Calculator')).toBeInTheDocument()
  })
})
