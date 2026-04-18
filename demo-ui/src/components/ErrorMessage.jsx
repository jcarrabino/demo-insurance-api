import PropTypes from 'prop-types'

export default function ErrorMessage({ error, onRetry }) {
  if (!error) return null

  const message = error?.response?.data?.message || error?.message || 'An error occurred'

  return (
    <div style={{ 
      padding: '1rem', 
      background: '#fee', 
      border: '1px solid #fcc', 
      borderRadius: '4px',
      margin: '1rem 0'
    }}>
      <h3 style={{ margin: '0 0 0.5rem 0', color: '#c00' }}>Error</h3>
      <p style={{ margin: '0 0 0.5rem 0' }}>{message}</p>
      {onRetry && (
        <button onClick={onRetry} style={{ padding: '0.5rem 1rem' }}>
          Try Again
        </button>
      )}
    </div>
  )
}

ErrorMessage.propTypes = {
  error: PropTypes.shape({
    message: PropTypes.string,
    response: PropTypes.shape({
      data: PropTypes.shape({
        message: PropTypes.string
      })
    })
  }),
  onRetry: PropTypes.func
}
