import { render, screen, waitFor } from '@testing-library/react'
import userEvent from '@testing-library/user-event'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import CoverageCalculator from '../../pages/CoverageCalculator'
import { AuthProvider } from '../../context/AuthContext'
import * as api from '../../api/client'

jest.mock('../../api/client')

const queryClient = new QueryClient({
  defaultOptions: { queries: { retry: false } },
})

const mockUser = { id: 1, email: 'test@test.com', firstName: 'Test', lastName: 'User', admin: false }
const mockAdminUser = { ...mockUser, admin: true }

const mockLines = [
  { id: 1, name: 'Auto', minCoverage: 10000, maxCoverage: 500000 },
  { id: 2, name: 'Home', minCoverage: 50000, maxCoverage: 1000000 },
]

const mockAccounts = [
  { id: 1, firstName: 'John', lastName: 'Doe', email: 'john@test.com' },
  { id: 2, firstName: 'Jane', lastName: 'Smith', email: 'jane@test.com' },
]

const renderCalculator = (user = mockUser) => {
  localStorage.setItem('token', 'test-token')
  localStorage.setItem('user', JSON.stringify(user))

  return render(
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <CoverageCalculator />
      </AuthProvider>
    </QueryClientProvider>
  )
}

describe('CoverageCalculator', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    localStorage.clear()
    api.getLines.mockResolvedValue({ data: mockLines })
    api.getAccounts.mockResolvedValue({ data: mockAccounts })
  })

  it('should render calculator form', async () => {
    renderCalculator()

    expect(screen.getByText('Coverage Calculator')).toBeInTheDocument()
    expect(screen.getByText(/Calculate insurance coverage/i)).toBeInTheDocument()

    await waitFor(() => {
      expect(screen.getByText('Insurance Line')).toBeInTheDocument()
    })
  })

  it('should show account selector for admin users', async () => {
    renderCalculator(mockAdminUser)

    await waitFor(() => {
      expect(screen.getByText('Select Account')).toBeInTheDocument()
    })
  })

  it('should not show account selector for regular users', async () => {
    renderCalculator(mockUser)

    await waitFor(() => {
      expect(screen.queryByText('Select Account')).not.toBeInTheDocument()
      expect(screen.getByText(/Calculating for:/i)).toBeInTheDocument()
    })
  })

  it('should calculate coverage successfully', async () => {
    const user = userEvent.setup()
    const mockResult = {
      accountName: 'Test User',
      zipCode: '10001',
      lineName: 'Auto',
      minCoverage: 10000,
      maxCoverage: 500000,
      calculatedCoverage: 250000,
      riskLevel: 'MEDIUM',
      premiumAdjustment: 1.2,
    }

    api.calculateCoverage.mockResolvedValue({ data: mockResult })

    renderCalculator(mockUser)

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /calculate coverage/i })).toBeInTheDocument()
    })

    const lineSelect = screen.getByRole('combobox')
    await user.selectOptions(lineSelect, '1')
    await user.click(screen.getByRole('button', { name: /calculate coverage/i }))

    await waitFor(() => {
      expect(screen.getByText('Coverage Calculation Result')).toBeInTheDocument()
      expect(screen.getByText('$250,000')).toBeInTheDocument()
      expect(screen.getByText('MEDIUM')).toBeInTheDocument()
    })
  })

  it('should display error when calculation fails', async () => {
    const user = userEvent.setup()
    api.calculateCoverage.mockRejectedValue({
      response: { data: { message: 'Calculation failed' } },
    })

    renderCalculator(mockUser)

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /calculate coverage/i })).toBeInTheDocument()
    })

    const lineSelect = screen.getByRole('combobox')
    await user.selectOptions(lineSelect, '1')
    await user.click(screen.getByRole('button', { name: /calculate coverage/i }))

    await waitFor(() => {
      expect(screen.getByText(/Calculation failed/i)).toBeInTheDocument()
    })
  })

  it('should show error when fields are not selected', async () => {
    const user = userEvent.setup()
    renderCalculator(mockUser)

    await waitFor(() => {
      expect(screen.getByRole('button', { name: /calculate coverage/i })).toBeInTheDocument()
    })

    await user.click(screen.getByRole('button', { name: /calculate coverage/i }))

    await waitFor(() => {
      expect(screen.getByText(/Please select all required fields/i)).toBeInTheDocument()
    })
  })
})
