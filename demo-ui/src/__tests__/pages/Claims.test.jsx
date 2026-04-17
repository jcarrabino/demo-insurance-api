import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Claims from '../../pages/Claims'
import { AuthProvider } from '../../context/AuthContext'
import { mockApi, setDefaultApiMocks } from '../../__test-mocks__/apiMocks'

const queryClient = new QueryClient({
  defaultOptions: { queries: { retry: false } },
})

const mockUser = { id: 1, email: 'test@test.com', firstName: 'Test', lastName: 'User', admin: false }

const renderClaims = () => {
  localStorage.setItem('token', 'test-token')
  localStorage.setItem('user', JSON.stringify(mockUser))

  return render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Claims />
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

describe('Claims', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    localStorage.clear()
    queryClient.clear()
    setDefaultApiMocks()
  })

  it('should render claims page', async () => {
    renderClaims()

    expect(screen.getByText('Claims')).toBeInTheDocument()
    
    await waitFor(() => {
      expect(mockApi.getClaims).toHaveBeenCalled()
    })
  })

  it('should display claims list', async () => {
    const mockClaims = [
      { id: 1, claimNumber: 'CLM-001', policy: { policyNumber: 'POL-001' }, claimAmount: 5000, status: 'PENDING', claimDate: '2024-01-15' },
      { id: 2, claimNumber: 'CLM-002', policy: { policyNumber: 'POL-002' }, claimAmount: 3000, status: 'APPROVED', claimDate: '2024-01-20' },
    ]
    mockApi.getClaims.mockResolvedValue({ data: mockClaims })

    renderClaims()

    await waitFor(() => {
      expect(screen.getByText('CLM-001')).toBeInTheDocument()
      expect(screen.getByText('CLM-002')).toBeInTheDocument()
    })
  })

  it('should delete claim', async () => {
    const user = userEvent.setup()
    const mockClaims = [
      { id: 1, claimNumber: 'CLM-001', policy: { policyNumber: 'POL-001' }, claimAmount: 5000, status: 'PENDING', claimDate: '2024-01-15' },
    ]
    mockApi.getClaims.mockResolvedValue({ data: mockClaims })
    mockApi.deleteClaim.mockResolvedValue({ data: 'deleted' })

    renderClaims()

    await waitFor(() => {
      expect(screen.getByText('CLM-001')).toBeInTheDocument()
    })

    const deleteButton = screen.getByText('Delete')
    await user.click(deleteButton)

    await waitFor(() => {
      expect(mockApi.deleteClaim).toHaveBeenCalledWith(1)
    })
  })
})
