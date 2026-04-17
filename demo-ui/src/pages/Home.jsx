import { Link } from 'react-router-dom'
import { useAuth } from '../context/AuthContext'

export default function Home() {
  const { isLoggedIn } = useAuth()

  return (
    <div className="home-page">
      <section className="hero">
        <div className="hero-content">
          <h1>Protect What Matters Most</h1>
          <p>Comprehensive insurance solutions tailored to your needs. From auto to home, health to life — we&apos;ve got you covered.</p>
          <div className="hero-actions">
            {isLoggedIn ? (
              <>
                <Link to="/policies" className="btn primary">View Policies</Link>
                <Link to="/claims" className="btn secondary">File a Claim</Link>
              </>
            ) : (
              <>
                <Link to="/register" className="btn primary">Get Started</Link>
                <Link to="/login" className="btn secondary">Sign In</Link>
              </>
            )}
          </div>
        </div>
      </section>

      <section className="features">
        <div className="feature-grid">
          <div className="feature-card">
            <div className="feature-icon">🚗</div>
            <h3>Auto Insurance</h3>
            <p>Comprehensive coverage for your vehicle with competitive rates and 24/7 roadside assistance.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🏠</div>
            <h3>Home Insurance</h3>
            <p>Protect your home and belongings from unexpected events with customizable coverage options.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">❤️</div>
            <h3>Health Insurance</h3>
            <p>Access quality healthcare with plans designed to fit your budget and medical needs.</p>
          </div>
          <div className="feature-card">
            <div className="feature-icon">🛡️</div>
            <h3>Life Insurance</h3>
            <p>Secure your family&apos;s financial future with flexible life insurance policies.</p>
          </div>
        </div>
      </section>

      <section className="cta">
        <h2>Ready to Get Protected?</h2>
        <p>Join thousands of satisfied customers who trust us with their insurance needs.</p>
        {!isLoggedIn && <Link to="/register" className="btn primary">Create Account</Link>}
      </section>
    </div>
  )
}
