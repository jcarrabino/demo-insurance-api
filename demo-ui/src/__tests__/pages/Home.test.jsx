import { render, screen } from '@testing-library/react'
import { BrowserRouter } from 'react-router-dom'
import Home from '../../pages/Home'
import { AuthProvider } from '../../context/AuthContext'

const renderHome = (isLoggedIn = false) => {
  if (isLoggedIn) {
    localStorage.setItem('token', 'test-token')
    localStorage.setItem('user', JSON.stringify({ id: 1, email: 'test@test.com' }))
  }

  return render(
    <BrowserRouter>
      <AuthProvider>
        <Home />
      </AuthProvider>
    </BrowserRouter>
  )
}

describe('Home', () => {
  beforeEach(() => {
    localStorage.clear()
  })

  it('should render hero section', () => {
    renderHome()

    expect(screen.getByText('Protect What Matters Most')).toBeInTheDocument()
    expect(screen.getByText(/Comprehensive insurance solutions/i)).toBeInTheDocument()
  })

  it('should show login/register buttons when not logged in', () => {
    renderHome(false)

    expect(screen.getByText('Get Started')).toBeInTheDocument()
    expect(screen.getByText('Sign In')).toBeInTheDocument()
  })

  it('should show policy/claims buttons when logged in', () => {
    renderHome(true)

    expect(screen.getByText('View Policies')).toBeInTheDocument()
    expect(screen.getByText('File a Claim')).toBeInTheDocument()
  })

  it('should display all insurance line features', () => {
    renderHome()

    expect(screen.getByText('Auto Insurance')).toBeInTheDocument()
    expect(screen.getByText('Home Insurance')).toBeInTheDocument()
    expect(screen.getByText('Health Insurance')).toBeInTheDocument()
    expect(screen.getByText('Life Insurance')).toBeInTheDocument()
  })

  it('should display CTA section', () => {
    renderHome()

    expect(screen.getByText('Ready to Get Protected?')).toBeInTheDocument()
    expect(screen.getByText(/Join thousands of satisfied customers/i)).toBeInTheDocument()
  })
})
