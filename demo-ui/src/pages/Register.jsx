import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { register } from '../api/client'

export default function Register() {
  const navigate = useNavigate()
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [form, setForm] = useState({
    firstName: '', lastName: '', email: '', password: '',
    phoneNumber: '', about: '', dateOfBirth: ''
  })

  const set = (field) => (e) => setForm(f => ({ ...f, [field]: e.target.value }))

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(''); setSuccess('')
    try {
      await register(form)
      setSuccess('Account created! You can now log in.')
      setTimeout(() => navigate('/login'), 1500)
    } catch (err) {
      const data = err.response?.data
      if (typeof data === 'object' && !data.Massege) {
        setError(Object.values(data).join(', '))
      } else {
        setError(data?.Massege || 'Registration failed')
      }
    }
  }

  return (
    <div className="page" style={{ maxWidth: 480 }}>
      <div className="card">
        <h2>Create Account</h2>
        <form onSubmit={handleSubmit}>
          <input placeholder="First Name" value={form.firstName} onChange={set('firstName')} required />
          <input placeholder="Last Name" value={form.lastName} onChange={set('lastName')} required />
          <input type="email" placeholder="Email" value={form.email} onChange={set('email')} required />
          <input type="password" placeholder="Password (min 8 chars, upper, lower, number, special)" value={form.password} onChange={set('password')} required />
          <input placeholder="Phone Number (10 digits)" value={form.phoneNumber} onChange={set('phoneNumber')} required />
          <input placeholder="About" value={form.about} onChange={set('about')} required />
          <label style={{ fontSize: '0.85rem', color: '#555' }}>Date of Birth</label>
          <input type="date" value={form.dateOfBirth} onChange={set('dateOfBirth')} required />
          {error && <p className="error">{error}</p>}
          {success && <p className="success">{success}</p>}
          <button className="btn" type="submit">Register</button>
        </form>
        <p style={{ marginTop: '1rem', fontSize: '0.875rem' }}>
          Already have an account? <Link to="/login">Login</Link>
        </p>
      </div>
    </div>
  )
}
