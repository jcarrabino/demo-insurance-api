// Mock axios before importing client
jest.mock('axios', () => {
  const mockAxiosInstance = {
    interceptors: {
      request: { use: jest.fn() },
      response: { use: jest.fn() },
    },
    get: jest.fn(),
    post: jest.fn(),
    put: jest.fn(),
    delete: jest.fn(),
  }
  
  return {
    create: jest.fn(() => mockAxiosInstance),
    ...mockAxiosInstance,
  }
})

import axios from 'axios'
import * as client from '../../api/client'

const mockAxiosInstance = axios.create()

describe('API Client', () => {
  beforeEach(() => {
    jest.clearAllMocks()
    localStorage.clear()
  })

  describe('Auth endpoints', () => {
    it('should call register endpoint', async () => {
      const mockData = { email: 'test@test.com', password: 'Password1!' }
      mockAxiosInstance.post.mockResolvedValue({ data: mockData })

      await client.register(mockData)

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/register', mockData)
    })

    it('should call login endpoint with Basic auth', async () => {
      const email = 'test@test.com'
      const password = 'Password1!'
      mockAxiosInstance.get.mockResolvedValue({ data: {}, headers: {} })

      await client.login(email, password)

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/login', {
        headers: { Authorization: expect.stringContaining('Basic ') },
      })
    })
  })

  describe('Account endpoints', () => {
    it('should get all accounts', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: [] })

      await client.getAccounts()

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/accounts/')
    })

    it('should get account by id', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: {} })

      await client.getAccount(1)

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/accounts/1')
    })

    it('should update account', async () => {
      const mockData = { firstName: 'John' }
      mockAxiosInstance.put.mockResolvedValue({ data: mockData })

      await client.updateAccount(1, mockData)

      expect(mockAxiosInstance.put).toHaveBeenCalledWith('/api/accounts/1', mockData)
    })

    it('should delete account', async () => {
      mockAxiosInstance.delete.mockResolvedValue({ data: 'deleted' })

      await client.deleteAccount(1)

      expect(mockAxiosInstance.delete).toHaveBeenCalledWith('/api/accounts/1')
    })
  })

  describe('Policy endpoints', () => {
    it('should get all policies', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: [] })

      await client.getPolicies()

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/policies/')
    })

    it('should create policy', async () => {
      const mockData = { policyNumber: 'POL-001' }
      mockAxiosInstance.post.mockResolvedValue({ data: mockData })

      await client.createPolicy(1, mockData)

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/policies/1', mockData)
    })

    it('should delete policy', async () => {
      mockAxiosInstance.delete.mockResolvedValue({ data: 'deleted' })

      await client.deletePolicy(1)

      expect(mockAxiosInstance.delete).toHaveBeenCalledWith('/api/policies/1')
    })
  })

  describe('Claim endpoints', () => {
    it('should get all claims', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: [] })

      await client.getClaims()

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/claims/')
    })

    it('should create claim', async () => {
      const mockData = { claimNumber: 'CLM-001' }
      mockAxiosInstance.post.mockResolvedValue({ data: mockData })

      await client.createClaim(1, mockData)

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/claims/1', mockData)
    })

    it('should delete claim', async () => {
      mockAxiosInstance.delete.mockResolvedValue({ data: 'deleted' })

      await client.deleteClaim(1)

      expect(mockAxiosInstance.delete).toHaveBeenCalledWith('/api/claims/1')
    })
  })

  describe('Line endpoints', () => {
    it('should get all lines', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: [] })

      await client.getLines()

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/lines/')
    })

    it('should create line', async () => {
      const mockData = { name: 'Auto' }
      mockAxiosInstance.post.mockResolvedValue({ data: mockData })

      await client.createLine(mockData)

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/lines/', mockData)
    })

    it('should delete line', async () => {
      mockAxiosInstance.delete.mockResolvedValue({ data: 'deleted' })

      await client.deleteLine(1)

      expect(mockAxiosInstance.delete).toHaveBeenCalledWith('/api/lines/1')
    })
  })

  describe('Coverage endpoints', () => {
    it('should calculate coverage', async () => {
      const mockResult = { calculatedCoverage: 250000 }
      mockAxiosInstance.get.mockResolvedValue({ data: mockResult })

      await client.calculateCoverage(1, 2)

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/coverage/calculate/1/2')
    })
  })
})
