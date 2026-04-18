import PropTypes from 'prop-types'

export default function Spinner({ size = 'md', message = 'Loading...' }) {
  const sizeClasses = {
    sm: 'w-4 h-4 border-2',
    md: 'w-8 h-8 border-3',
    lg: 'w-12 h-12 border-4'
  }

  return (
    <div className="spinner-container" role="status" aria-live="polite">
      <div className={`spinner ${sizeClasses[size]}`} aria-label="Loading"></div>
      {message && <p className="spinner-message">{message}</p>}
    </div>
  )
}

Spinner.propTypes = {
  size: PropTypes.oneOf(['sm', 'md', 'lg']),
  message: PropTypes.string
}
