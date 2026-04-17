// Manual test to verify Bearer token is sent in requests
// Run this in browser console after logging in

export const testBearerToken = async () => {
  console.log('=== Testing Bearer Token in API Requests ===')
  
  // Check if token exists
  const token = localStorage.getItem('token')
  console.log('Token in localStorage:', token ? 'EXISTS' : 'MISSING')
  
  if (!token) {
    console.error('❌ No token found. Please login first.')
    return
  }
  
  // Test with a simple API call
  try {
    const response = await fetch('/api/policies/', {
      headers: {
        'Authorization': `Bearer ${token}`
      }
    })
    
    console.log('Response status:', response.status)
    console.log('Response headers:', Object.fromEntries(response.headers.entries()))
    
    if (response.ok) {
      console.log('✅ Bearer token is working correctly')
    } else {
      console.error('❌ Request failed with status:', response.status)
    }
  } catch (error) {
    console.error('❌ Request error:', error)
  }
}

// Instructions:
// 1. Login to the application
// 2. Open browser console
// 3. Run: testBearerToken()
