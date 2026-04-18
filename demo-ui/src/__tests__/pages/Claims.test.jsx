import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Claims from '../../pages/Claims'
import { AuthProvider } from '../../context/AuthContext'
import * as api from '../../api/client'

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
    api.getClaims.mockResolvedValue({ data: [] })
    api.getPolicies.mockResolvedValue({ data: [] })
  })

  it('should render claims page', async () => {
    renderClaims()

    expect(screen.getByText('Claims')).toBeInTheDocument()
    
    await waitFor(() => {
      expect(api.getClaims).toHaveBeenCalled()
    })
  })

  it('should display claims list', async () => {
    const mockClaims = [
      { id: 1, policyId: 1, policy: { id: 1, lineId: 1 }, description: 'Car accident', claimStatus: 'SUBMITTED', claimDate: '2024-01-15' },
      { id: 2, policyId: 2, policy: { id: 2, lineId: 2 }, description: 'Home damage', claimStatus: 'APPROVED', claimDate: '2024-01-20' },
    ]
    api.getClaims.mockResolvedValue({ data: mockClaims })

    renderClaims()

    await waitFor(() => {
      expect(screen.getByText('CLM-00001')).toBeInTheDocument()
      expect(screen.getByText('CLM-00002')).toBeInTheDocument()
    })
  })
})
