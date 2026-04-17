import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { login } from '../api/client'
import { useAuth } from '../context/AuthContext'

export default function Login() {
  const [email, setEmail] = useState('')
  const [password, setPassword] = useState('')
  const [error, setError] = useState('')
  const { saveAuth } = useAuth()
  const navigate = useNavigate()

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError('')
    try {
      const res = await login(email, password)
      // Try different header case variations
      const jwt = res.headers['authorization'] || res.headers['Authorization']
      
      if (!jwt) {
        console.error('No JWT token received in response headers:', res.headers)
        setError('Authentication failed - no token received')
        return
      }
      
      saveAuth(jwt, res.data)
      navigate('/policies')
    } catch (err) {
      console.error('Login error:', err)
      setError(err.response?.data?.Massege || err.response?.data?.message || 'Invalid credentials')
    }
  }

  return (
    <div className="page" style={{ maxWidth: 420 }}>
      <div className="card">
        <h2>Sign In</h2>
        <form onSubmit={handleSubmit}>
          <input type="email" placeholder="Email" value={email} onChange={e => setEmail(e.target.value)} required />
          <input type="password" placeholder="Password" value={password} onChange={e => setPassword(e.target.value)} required />
          {error && <p className="error">{error}</p>}
          <button className="btn" type="submit">Login</button>
        </form>
        <p style={{ marginTop: '1rem', fontSize: '0.875rem' }}>
          No account? <Link to="/register">Register</Link>
        </p>
      </div>
    </div>
  )
}
