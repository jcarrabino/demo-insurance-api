export default function Spinner({ size = 'md', message = 'Loading...' }) {
  const sizeClasses = {
    sm: 'w-4 h-4 border-2',
    md: 'w-8 h-8 border-3',
    lg: 'w-12 h-12 border-4'
  }

  return (
    <div className="spinner-container">
      <div className={`spinner ${sizeClasses[size]}`}></div>
      {message && <p className="spinner-message">{message}</p>}
    </div>
  )
}
