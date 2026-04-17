import React from 'react'
import { Routes, Route, Navigate, NavLink, useNavigate } from 'react-router-dom'
import { AuthProvider, useAuth } from './context/AuthContext'
import Login from './pages/Login'
import Register from './pages/Register'
import Accounts from './pages/Accounts'
import Policies from './pages/Policies'
import Claims from './pages/Claims'

function Nav() {
  const { isLoggedIn, user, logout } = useAuth()
  const navigate = useNavigate()

  const handleLogout = () => { logout(); navigate('/login') }

  return (
    <nav>
      <h1>Insurance Management</h1>
      <div className="nav-links">
        {isLoggedIn ? (
          <>
            <NavLink to="/accounts">Accounts</NavLink>
            <NavLink to="/policies">Policies</NavLink>
            <NavLink to="/claims">Claims</NavLink>
            <span style={{ color: '#aaa', fontSize: '0.85rem' }}>{user?.email}</span>
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

export default function App() {
  return (
    <AuthProvider>
      <Nav />
      <Routes>
        <Route path="/login" element={<Login />} />
        <Route path="/register" element={<Register />} />
        <Route path="/accounts" element={<PrivateRoute><Accounts /></PrivateRoute>} />
        <Route path="/policies" element={<PrivateRoute><Policies /></PrivateRoute>} />
        <Route path="/claims" element={<PrivateRoute><Claims /></PrivateRoute>} />
        <Route path="*" element={<Navigate to="/login" replace />} />
      </Routes>
    </AuthProvider>
  )
}
