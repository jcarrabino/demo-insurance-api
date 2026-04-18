import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Accounts from '../../pages/Accounts'
import { AuthProvider } from '../../context/AuthContext'
import * as api from '../../api/client'

const queryClient = new QueryClient({
  defaultOptions: { queries: { retry: false } },
})

const mockAdminUser = { id: 1, email: 'admin@test.com', firstName: 'Admin', lastName: 'User', admin: true }

const renderAccounts = () => {
  localStorage.setItem('token', 'test-token')
  localStorage.setItem('user', JSON.stringify(mockAdminUser))

  return render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Accounts />
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

describe('Accounts', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    localStorage.clear()
    queryClient.clear()
    api.getAccounts.mockResolvedValue({ data: [] })
  })

  it('should render accounts page', async () => {
    renderAccounts()

    expect(screen.getByText('Accounts')).toBeInTheDocument()
    
    await waitFor(() => {
      expect(api.getAccounts).toHaveBeenCalled()
    })
  })

  it('should display accounts list', async () => {
    const mockAccounts = [
      { id: 1, firstName: 'John', lastName: 'Doe', email: 'john@test.com', admin: false, address: { zipCode: '10001' } },
      { id: 2, firstName: 'Jane', lastName: 'Smith', email: 'jane@test.com', admin: false, address: { zipCode: '20002' } },
    ]
    api.getAccounts.mockResolvedValue({ data: mockAccounts })

    renderAccounts()

    await waitFor(() => {
      expect(screen.getByText('John Doe')).toBeInTheDocument()
      expect(screen.getByText('Jane Smith')).toBeInTheDocument()
    })
  })
})
