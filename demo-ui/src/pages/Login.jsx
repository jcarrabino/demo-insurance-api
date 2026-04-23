import { useState } from 'react'
import { useNavigate, Link } from 'react-router-dom'
import { useForm } from 'react-hook-form'
import { login } from '../api/client'
import { useAuth } from '../context/AuthContext'
import ErrorMessage from '../components/ErrorMessage'

export default function Login() {
  const [error, setError] = useState('')
  const [isLoading, setIsLoading] = useState(false)
  const { saveAuth } = useAuth()
  const navigate = useNavigate()
  
  console.log('Login component mounted')
  
  const { register, handleSubmit, formState: { errors } } = useForm()

  const onSubmit = async (data) => {
    setError('')
    setIsLoading(true)
    try {
      const res = await login(data.email, data.password)
      
      // Token is now in HttpOnly cookie, only extract user data from response
      // res.data now contains the LoginResponse object directly
      const loginResponse = res.data
      saveAuth(loginResponse.account || loginResponse)
      navigate('/policies')
    } catch (err) {
      console.error('Login error:', err)
      const errorData = err.response?.data
      // Handle both wrapped and unwrapped error responses
      const errorMessage = errorData?.message || errorData?.data?.message || 'Invalid credentials'
      setError(errorMessage)
    } finally {
      setIsLoading(false)
    }
  }

  return (
    <div className="page" style={{ maxWidth: 420 }}>
      <div className="card">
        <h2>Sign In</h2>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div>
            <input 
              type="email" 
              placeholder="Email" 
              aria-label="Email address"
              {...register('email', { 
                required: 'Email is required',
                pattern: {
                  value: /^[A-Z0-9._%+-]+@[A-Z0-9.-]+\.[A-Z]{2,}$/i,
                  message: 'Invalid email address'
                }
              })} 
            />
            {errors.email && <p className="error" role="alert">{errors.email.message}</p>}
          </div>
          
          <div>
            <input 
              type="password" 
              placeholder="Password" 
              aria-label="Password"
              {...register('password', { 
                required: 'Password is required',
                minLength: {
                  value: 6,
                  message: 'Password must be at least 6 characters'
                }
              })} 
            />
            {errors.password && <p className="error" role="alert">{errors.password.message}</p>}
          </div>
          
          <ErrorMessage error={error ? { message: error } : null} />
          
          <button className="btn" type="submit" disabled={isLoading}>
            {isLoading ? 'Logging in...' : 'Login'}
          </button>
        </form>
        <p style={{ marginTop: '1rem', fontSize: '0.875rem' }}>
          No account? <Link to="/register">Register</Link>
        </p>
      </div>
    </div>
  )
}
