import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { BrowserRouter } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import Lines from '../../pages/Lines'
import { AuthProvider } from '../../context/AuthContext'
import * as api from '../../api/client'

jest.mock('../../api/client')

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

  it('should create new line', async () => {
    const user = userEvent.setup()
    const newLine = { id: 3, name: 'Life', minCoverage: 100000, maxCoverage: 5000000 }
    api.createLine.mockResolvedValue({ data: newLine })

    renderLines()

    await waitFor(() => {
      expect(screen.getByText('Add New Line')).toBeInTheDocument()
    })

    await user.type(screen.getByPlaceholderText('Line Name'), 'Life')
    await user.type(screen.getByPlaceholderText('Min Coverage'), '100000')
    await user.type(screen.getByPlaceholderText('Max Coverage'), '5000000')
    await user.click(screen.getByText('Add Line'))

    await waitFor(() => {
      expect(api.createLine).toHaveBeenCalledWith({
        name: 'Life',
        minCoverage: 100000,
        maxCoverage: 5000000,
      })
    })
  })

  it('should delete line', async () => {
    const user = userEvent.setup()
    const mockLines = [
      { id: 1, name: 'Auto', minCoverage: 10000, maxCoverage: 500000 },
    ]
    api.getLines.mockResolvedValue({ data: mockLines })
    api.deleteLine.mockResolvedValue({ data: 'deleted' })

    renderLines()

    await waitFor(() => {
      expect(screen.getByText('Auto')).toBeInTheDocument()
    })

    const deleteButton = screen.getByText('Delete')
    await user.click(deleteButton)

    await waitFor(() => {
      expect(api.deleteLine).toHaveBeenCalledWith(1)
    })
  })
})
