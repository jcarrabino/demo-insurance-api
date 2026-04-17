import { Routes, Route, Navigate, NavLink, useNavigate } from 'react-router-dom'
import { QueryClient, QueryClientProvider } from '@tanstack/react-query'
import { AuthProvider, useAuth } from './context/AuthContext'
import Home from './pages/Home'
import Login from './pages/Login'
import Register from './pages/Register'
import Accounts from './pages/Accounts'
import Policies from './pages/Policies'
import Claims from './pages/Claims'
import Lines from './pages/Lines'
import CoverageCalculator from './pages/CoverageCalculator'

const queryClient = new QueryClient({
  defaultOptions: {
    queries: {
      refetchOnWindowFocus: false,
      retry: 1,
      staleTime: 5 * 60 * 1000,
    },
  },
})

function Nav() {
  const { isLoggedIn, user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => { logout(); navigate('/login') }

  return (
    <nav>
      <h1 onClick={() => navigate('/')} style={{ cursor: 'pointer' }}>Insurance Management</h1>
      <div className="nav-links">
        {isLoggedIn ? (
          <>
            {user?.admin && <NavLink to="/accounts">Accounts</NavLink>}
            <NavLink to="/policies">Policies</NavLink>
            <NavLink to="/claims">Claims</NavLink>
            {user?.admin && <NavLink to="/lines">Lines</NavLink>}
            <NavLink to="/coverage">Coverage</NavLink>
            <span style={{ color: '#aaa', fontSize: '0.85rem' }}>
              {user?.email} {user?.admin && <span style={{ color: '#4f46e5' }}>(Admin)</span>}
            </span>
            <button onClick={handleLogout}>Logout</button>
          </>
        ) : (
          <>
            <NavLink to="/login">Login</NavLink>
            <NavLink to="/register">Register</NavLink>
          </>
        )}
      </div>
    </nav>
  )
}

function PrivateRoute({ children }) {
  const { isLoggedIn } = useAuth()
  return isLoggedIn ? children : <Navigate to="/login" replace />
}

function AdminRoute({ children }) {
  const { isLoggedIn, user } = useAuth()
  if (!isLoggedIn) return <Navigate to="/login" replace />
  if (!user?.admin) return <Navigate to="/" replace />
  return children
}

export default function App() {
  return (
    <QueryClientProvider client={queryClient}>
      <AuthProvider>
        <Nav />
        <Routes>
          <Route path="/" element={<Home />} />
          <Route path="/login" element={<Login />} />
          <Route path="/register" element={<Register />} />
          <Route path="/accounts" element={<AdminRoute><Accounts /></AdminRoute>} />
          <Route path="/policies" element={<PrivateRoute><Policies /></PrivateRoute>} />
          <Route path="/claims" element={<PrivateRoute><Claims /></PrivateRoute>} />
          <Route path="/lines" element={<AdminRoute><Lines /></AdminRoute>} />
          <Route path="/coverage" element={<PrivateRoute><CoverageCalculator /></PrivateRoute>} />
          <Route path="*" element={<Navigate to="/" replace />} />
        </Routes>
      </AuthProvider>
    </QueryClientProvider>
  )
}
