import axios from 'axios'
import api, * as client from '../../api/client'

jest.mock('axios')

describe('API Client', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    localStorage.clear()
  })

  describe('Interceptors', () => {
    it('should attach JWT token to requests', () => {
      const mockToken = 'test-token'
      localStorage.setItem('token', mockToken)

      const config = { headers: {} }
      const interceptor = axios.create.mock.results[0].value.interceptors.request.use.mock.calls[0][0]
      const result = interceptor(config)

      expect(result.headers.Authorization).toBe(`Bearer ${mockToken}`)
    })

    it('should not attach token if not present', () => {
      const config = { headers: {} }
      const interceptor = axios.create.mock.results[0].value.interceptors.request.use.mock.calls[0][0]
      const result = interceptor(config)

      expect(result.headers.Authorization).toBeUndefined()
    })
  })

  describe('Auth endpoints', () => {
    it('should call register endpoint', async () => {
      const mockData = { email: 'test@test.com', password: 'Password1!' }
      axios.post = jest.fn().mockResolvedValue({ data: mockData })

      await client.register(mockData)

      expect(axios.post).toHaveBeenCalledWith('/register', mockData)
    })

    it('should call login endpoint with Basic auth', async () => {
      const email = 'test@test.com'
      const password = 'Password1!'
      axios.get = jest.fn().mockResolvedValue({ data: {}, headers: {} })

      await client.login(email, password)

      expect(axios.get).toHaveBeenCalledWith('/login', {
        headers: { Authorization: expect.stringContaining('Basic ') },
      })
    })
  })

  describe('Account endpoints', () => {
    it('should get all accounts', async () => {
      axios.get = jest.fn().mockResolvedValue({ data: [] })

      await client.getAccounts()

      expect(axios.get).toHaveBeenCalledWith('/api/accounts/')
    })

    it('should get account by id', async () => {
      axios.get = jest.fn().mockResolvedValue({ data: {} })

      await client.getAccount(1)

      expect(axios.get).toHaveBeenCalledWith('/api/accounts/1')
    })

    it('should update account', async () => {
      const mockData = { firstName: 'John' }
      axios.put = jest.fn().mockResolvedValue({ data: mockData })

      await client.updateAccount(1, mockData)

      expect(axios.put).toHaveBeenCalledWith('/api/accounts/1', mockData)
    })

    it('should delete account', async () => {
      axios.delete = jest.fn().mockResolvedValue({ data: 'deleted' })

      await client.deleteAccount(1)

      expect(axios.delete).toHaveBeenCalledWith('/api/accounts/1')
    })
  })

  describe('Policy endpoints', () => {
    it('should get all policies', async () => {
      axios.get = jest.fn().mockResolvedValue({ data: [] })

      await client.getPolicies()

      expect(axios.get).toHaveBeenCalledWith('/api/policies/')
    })

    it('should create policy', async () => {
      const mockData = { policyNumber: 'POL-001' }
      axios.post = jest.fn().mockResolvedValue({ data: mockData })

      await client.createPolicy(1, mockData)

      expect(axios.post).toHaveBeenCalledWith('/api/policies/1', mockData)
    })

    it('should delete policy', async () => {
      axios.delete = jest.fn().mockResolvedValue({ data: 'deleted' })

      await client.deletePolicy(1)

      expect(axios.delete).toHaveBeenCalledWith('/api/policies/1')
    })
  })

  describe('Claim endpoints', () => {
    it('should get all claims', async () => {
      axios.get = jest.fn().mockResolvedValue({ data: [] })

      await client.getClaims()

      expect(axios.get).toHaveBeenCalledWith('/api/claims/')
    })

    it('should create claim', async () => {
      const mockData = { claimNumber: 'CLM-001' }
      axios.post = jest.fn().mockResolvedValue({ data: mockData })

      await client.createClaim(1, mockData)

      expect(axios.post).toHaveBeenCalledWith('/api/claims/1', mockData)
    })

    it('should delete claim', async () => {
      axios.delete = jest.fn().mockResolvedValue({ data: 'deleted' })

      await client.deleteClaim(1)

      expect(axios.delete).toHaveBeenCalledWith('/api/claims/1')
    })
  })

  describe('Line endpoints', () => {
    it('should get all lines', async () => {
      axios.get = jest.fn().mockResolvedValue({ data: [] })

      await client.getLines()

      expect(axios.get).toHaveBeenCalledWith('/api/lines/')
    })

    it('should create line', async () => {
      const mockData = { name: 'Auto' }
      axios.post = jest.fn().mockResolvedValue({ data: mockData })

      await client.createLine(mockData)

      expect(axios.post).toHaveBeenCalledWith('/api/lines/', mockData)
    })

    it('should delete line', async () => {
      axios.delete = jest.fn().mockResolvedValue({ data: 'deleted' })

      await client.deleteLine(1)

      expect(axios.delete).toHaveBeenCalledWith('/api/lines/1')
    })
  })

  describe('Coverage endpoints', () => {
    it('should calculate coverage', async () => {
      const mockResult = { calculatedCoverage: 250000 }
      axios.get = jest.fn().mockResolvedValue({ data: mockResult })

      await client.calculateCoverage(1, 2)

      expect(axios.get).toHaveBeenCalledWith('/api/coverage/calculate/1/2')
    })
  })
})
