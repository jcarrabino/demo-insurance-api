import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Accounts from '../../pages/Accounts'
import { AuthProvider } from '../../context/AuthContext'
import * as api from '../../api/client'

jest.mock('../../api/client')

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

  it('should enable edit mode', async () => {
    const user = userEvent.setup()
    const mockAccounts = [
      { id: 1, firstName: 'John', lastName: 'Doe', email: 'john@test.com', admin: false, address: { zipCode: '10001' } },
    ]
    api.getAccounts.mockResolvedValue({ data: mockAccounts })

    renderAccounts()

    await waitFor(() => {
      expect(screen.getByText('John Doe')).toBeInTheDocument()
    })

    const editButton = screen.getByText('Edit')
    await user.click(editButton)

    await waitFor(() => {
      expect(screen.getByText('Save')).toBeInTheDocument()
      expect(screen.getByText('Cancel')).toBeInTheDocument()
    })
  })

  it('should update account', async () => {
    const user = userEvent.setup()
    const mockAccounts = [
      { id: 1, firstName: 'John', lastName: 'Doe', email: 'john@test.com', admin: false, address: { zipCode: '10001' } },
    ]
    api.getAccounts.mockResolvedValue({ data: mockAccounts })
    api.updateAccount.mockResolvedValue({ data: { ...mockAccounts[0], firstName: 'Johnny' } })

    renderAccounts()

    await waitFor(() => {
      expect(screen.getByText('John Doe')).toBeInTheDocument()
    })

    const editButton = screen.getByText('Edit')
    await user.click(editButton)

    const firstNameInput = screen.getByDisplayValue('John')
    await user.clear(firstNameInput)
    await user.type(firstNameInput, 'Johnny')

    const saveButton = screen.getByText('Save')
    await user.click(saveButton)

    await waitFor(() => {
      expect(api.updateAccount).toHaveBeenCalled()
    })
  })

  it('should delete account', async () => {
    const user = userEvent.setup()
    const mockAccounts = [
      { id: 2, firstName: 'John', lastName: 'Doe', email: 'john@test.com', admin: false, address: { zipCode: '10001' } },
    ]
    api.getAccounts.mockResolvedValue({ data: mockAccounts })
    api.deleteAccount.mockResolvedValue({ data: 'deleted' })

    renderAccounts()

    await waitFor(() => {
      expect(screen.getByText('John Doe')).toBeInTheDocument()
    })

    const deleteButton = screen.getByText('Delete')
    await user.click(deleteButton)

    await waitFor(() => {
      expect(api.deleteAccount).toHaveBeenCalledWith(2)
    })
  })
})
