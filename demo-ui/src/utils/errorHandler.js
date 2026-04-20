/**
 * Utility function to extract error messages from API responses
 * Handles both wrapped (ApiResponse) and unwrapped error responses
 */
export const getErrorMessage = (error) => {
  if (!error) return 'An unknown error occurred'

  // Handle axios error response
  if (error.response?.data) {
    const data = error.response.data

    // Handle ApiResponse wrapper with error message
    if (data.message) {
      return data.message
    }

    // Handle validation errors (object with field names as keys)
    if (typeof data === 'object' && !Array.isArray(data)) {
      const messages = Object.entries(data)
        .filter(([key]) => key !== 'timestamp' && key !== 'status' && key !== 'error')
        .map(([key, value]) => {
          // Handle nested error objects
          if (typeof value === 'object') {
            return Object.values(value).join(', ')
          }
          return value
        })
        .filter(msg => msg && msg.length > 0)

      if (messages.length > 0) {
        return messages.join('\n')
      }
    }

    // Handle typo in backend (Massege instead of Message)
    if (data.Massege) {
      return data.Massege
    }
  }

  // Handle error message string
  if (error.message) {
    return error.message
  }

  return 'An unknown error occurred'
}

/**
 * Format error message for display (convert newlines to readable format)
 */
export const formatErrorForDisplay = (errorMessage) => {
  if (!errorMessage) return ''
  
  // Split by newline and create readable list
  const lines = errorMessage.split('\n').filter(line => line.trim())
  
  if (lines.length === 1) {
    return lines[0]
  }
  
  // Return as bullet list for multiple errors
  return lines.map(line => `• ${line}`).join('\n')
}

/**
 * Extract field-specific errors from validation response
 * Useful for displaying errors next to form fields
 */
export const getFieldErrors = (error) => {
  const fieldErrors = {}

  if (!error?.response?.data) {
    return fieldErrors
  }

  const data = error.response.data

  // Handle validation errors object
  if (typeof data === 'object' && !Array.isArray(data)) {
    Object.entries(data).forEach(([key, value]) => {
      if (key !== 'timestamp' && key !== 'status' && key !== 'error' && key !== 'message') {
        fieldErrors[key] = typeof value === 'object' ? Object.values(value).join(', ') : value
      }
    })
  }

  return fieldErrors
}
