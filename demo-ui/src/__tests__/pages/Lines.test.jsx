import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Lines from '../../pages/Lines'
import { AuthProvider } from '../../context/AuthContext'
import * as api from '../../api/client'

const queryClient = new QueryClient({
  defaultOptions: { queries: { retry: false } },
})

const mockAdminUser = { id: 1, email: 'admin@test.com', firstName: 'Admin', lastName: 'User', admin: true }

const renderLines = () => {
  localStorage.setItem('token', 'test-token')
  localStorage.setItem('user', JSON.stringify(mockAdminUser))

  return render(
    <QueryClientProvider client={queryClient}>
      <BrowserRouter>
        <AuthProvider>
          <Lines />
        </AuthProvider>
      </BrowserRouter>
    </QueryClientProvider>
  )
}

describe('Lines', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    localStorage.clear()
    queryClient.clear()
    api.getLines.mockResolvedValue({ data: [] })
  })

  it('should render lines page', async () => {
    renderLines()

    expect(screen.getByText('Insurance Lines')).toBeInTheDocument()
    
    await waitFor(() => {
      expect(api.getLines).toHaveBeenCalled()
    })
  })

  it('should display lines list', async () => {
    const mockLines = [
      { id: 1, name: 'Auto', minCoverage: 10000, maxCoverage: 500000 },
      { id: 2, name: 'Home', minCoverage: 50000, maxCoverage: 1000000 },
    ]
    api.getLines.mockResolvedValue({ data: mockLines })

    renderLines()

    await waitFor(() => {
      expect(screen.getByText('Auto')).toBeInTheDocument()
      expect(screen.getByText('Home')).toBeInTheDocument()
    })
  })
})
