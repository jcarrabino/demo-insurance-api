import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Policies from '../../pages/Policies'
import { AuthProvider } from '../../context/AuthContext'
import { mockApi, setDefaultApiMocks } from '../../__test-mocks__/apiMocks'

const queryClient = new QueryClient({
  defaultOptions: { queries: { retry: false } },
})

const mockUser = { id: 1, email: 'test@test.com', firstName: 'Test', lastName: 'User', admin: false }

const renderPolicies = () => {
  localStorage.setItem('token', 'test-token')
  localStorage.setItem('user', JSON.stringify(mockUser))

  return render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Policies />
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

describe('Policies', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    localStorage.clear()
    queryClient.clear()
    setDefaultApiMocks()
  })

  it('should render policies page', async () => {
    renderPolicies()

    expect(screen.getByText('Policies')).toBeInTheDocument()
    
    await waitFor(() => {
      expect(mockApi.getPolicies).toHaveBeenCalled()
    })
  })

  it('should display policies list', async () => {
    const mockPolicies = [
      { id: 1, policyNumber: 'POL-001', line: { name: 'Auto' }, premium: 1000, startDate: '2024-01-01', endDate: '2025-01-01' },
      { id: 2, policyNumber: 'POL-002', line: { name: 'Home' }, premium: 2000, startDate: '2024-01-01', endDate: '2025-01-01' },
    ]
    mockApi.getPolicies.mockResolvedValue({ data: mockPolicies })

    renderPolicies()

    await waitFor(() => {
      expect(screen.getByText('POL-001')).toBeInTheDocument()
      expect(screen.getByText('POL-002')).toBeInTheDocument()
    })
  })

  it('should delete policy', async () => {
    const user = userEvent.setup()
    const mockPolicies = [
      { id: 1, policyNumber: 'POL-001', line: { name: 'Auto' }, premium: 1000, startDate: '2024-01-01', endDate: '2025-01-01' },
    ]
    mockApi.getPolicies.mockResolvedValue({ data: mockPolicies })
    mockApi.deletePolicy.mockResolvedValue({ data: 'deleted' })

    renderPolicies()

    await waitFor(() => {
      expect(screen.getByText('POL-001')).toBeInTheDocument()
    })

    const deleteButton = screen.getByText('Delete')
    await user.click(deleteButton)

    await waitFor(() => {
      expect(mockApi.deletePolicy).toHaveBeenCalledWith(1)
    })
  })
})
