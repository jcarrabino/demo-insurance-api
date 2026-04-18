import { useState, useCallback } from 'react'

/**
 * Hook to track which fields have been edited in a form
 * Returns only the fields that have been changed from their original values
 */
export function useEditedFields(initialData) {
  const [editedFields, setEditedFields] = useState(new Set())

  const trackEdit = useCallback((fieldName) => {
    setEditedFields(prev => new Set(prev).add(fieldName))
  }, [])

  const getEditedData = useCallback((currentData) => {
    const result = {}
    editedFields.forEach(field => {
      if (field === 'address' && typeof currentData[field] === 'object') {
        // Handle nested address object
        result[field] = currentData[field]
      } else {
        result[field] = currentData[field]
      }
    })
    return result
  }, [editedFields])

  const reset = useCallback(() => {
    setEditedFields(new Set())
  }, [])

  return { trackEdit, getEditedData, reset, editedFields }
}
