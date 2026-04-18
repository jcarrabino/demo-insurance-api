import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Policies from '../../pages/Policies'
import { AuthProvider } from '../../context/AuthContext'
import * as api from '../../api/client'

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
    api.getPolicies.mockResolvedValue({ data: [] })
    api.getLines.mockResolvedValue({ data: [] })
  })

  it('should render policies page', async () => {
    renderPolicies()

    expect(screen.getByText('Policies')).toBeInTheDocument()
    
    await waitFor(() => {
      expect(api.getPolicies).toHaveBeenCalled()
    })
  })

  it('should display policies list', async () => {
    const mockPolicies = [
      { id: 1, lineId: 1, accountId: 1, premium: 1000, startDate: '2024-01-01', endDate: '2025-01-01' },
      { id: 2, lineId: 2, accountId: 1, premium: 2000, startDate: '2024-01-01', endDate: '2025-01-01' },
    ]
    api.getPolicies.mockResolvedValue({ data: mockPolicies })

    renderPolicies()

    await waitFor(() => {
      expect(screen.getByText('POL-00001')).toBeInTheDocument()
      expect(screen.getByText('POL-00002')).toBeInTheDocument()
    })
  })
})
