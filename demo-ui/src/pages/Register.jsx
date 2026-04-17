import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { register } from '../api/client'

export default function Register() {
  const navigate = useNavigate()
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [form, setForm] = useState({
    firstName: '',
    middleName: '',
    lastName: '',
    email: '',
    password: '',
    phoneNumber: '',
    dateOfBirth: '',
    address: {
      street: '',
      city: '',
      state: '',
      zipCode: '',
      country: ''
    }
  })

  const set = (field) => (e) => setForm(f => ({ ...f, [field]: e.target.value }))
  const setAddress = (field) => (e) => setForm(f => ({ ...f, address: { ...f.address, [field]: e.target.value } }))

  const handleSubmit = async (e) => {
    e.preventDefault()
    setError(''); setSuccess('')
    
    // Validate phone number format
    if (!/^[0-9]{10}$/.test(form.phoneNumber)) {
      setError('Phone number must be exactly 10 digits')
      return
    }
    
    try {
      await register(form)
      setSuccess('Account created! You can now log in.')
      setTimeout(() => navigate('/login'), 1500)
    } catch (err) {
      const data = err.response?.data
      if (typeof data === 'object' && !data.message && !data.Massege) {
        setError(Object.values(data).join(', '))
      } else {
        setError(data?.message || data?.Massege || 'Registration failed')
      }
    }
  }

  return (
    <div className="page" style={{ maxWidth: 520 }}>
      <div className="card">
        <h2>Create Account</h2>
        <form onSubmit={handleSubmit}>
          <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Personal Information</label>
          <input 
            placeholder="First Name" 
            value={form.firstName} 
            onChange={set('firstName')} 
            minLength="2"
            maxLength="50"
            required 
          />
          <input 
            placeholder="Middle Name (Optional)" 
            value={form.middleName} 
            onChange={set('middleName')}
            maxLength="50"
          />
          <input 
            placeholder="Last Name" 
            value={form.lastName} 
            onChange={set('lastName')} 
            minLength="2"
            maxLength="50"
            required 
          />
          <input 
            type="email" 
            placeholder="Email" 
            value={form.email} 
            onChange={set('email')} 
            required 
          />
          <input 
            type="password" 
            placeholder="Password (min 8 chars, upper, lower, number, special)" 
            value={form.password} 
            onChange={set('password')}
            minLength="8"
            maxLength="20"
            pattern="^(?=.*[a-z])(?=.*[A-Z])(?=.*\d)(?=.*[@$!%*?&])[A-Za-z\d@$!%*?&]+$"
            title="Password must contain at least one uppercase letter, one lowercase letter, one number and one special character"
            required 
          />
          <input 
            type="tel"
            placeholder="Phone Number (10 digits)" 
            value={form.phoneNumber} 
            onChange={set('phoneNumber')}
            pattern="[0-9]{10}"
            title="Phone number must be exactly 10 digits"
            maxLength="10"
            required 
          />
          <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Date of Birth</label>
          <input 
            type="date" 
            value={form.dateOfBirth} 
            onChange={set('dateOfBirth')}
            max={new Date().toISOString().split('T')[0]}
            required 
          />
          
          <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold', marginTop: '1rem' }}>Address</label>
          <input 
            placeholder="Street" 
            value={form.address.street} 
            onChange={setAddress('street')}
          />
          <input 
            placeholder="City" 
            value={form.address.city} 
            onChange={setAddress('city')}
          />
          <input 
            placeholder="State" 
            value={form.address.state} 
            onChange={setAddress('state')}
          />
          <input 
            placeholder="Zip Code" 
            value={form.address.zipCode} 
            onChange={setAddress('zipCode')}
          />
          <input 
            placeholder="Country" 
            value={form.address.country} 
            onChange={setAddress('country')}
          />
          
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
