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
    patch: jest.fn(),
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

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/v1/auth/register', mockData)
    })

    it('should call login endpoint', async () => {
      const email = 'test@test.com'
      const password = 'Password1!'
      mockAxiosInstance.post.mockResolvedValue({ 
        data: { account: { id: 1, email } },
        headers: { authorization: 'Bearer token' }
      })

      await client.login(email, password)

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/v1/auth/login', { email, password })
    })
  })

  describe('Account endpoints', () => {
    it('should get all accounts with pagination', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: { content: [] } })

      await client.getAccounts(0, 20)

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/v1/accounts?page=0&size=20')
    })

    it('should get account by id', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: {} })

      await client.getAccount(1)

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/v1/accounts/1')
    })

    it('should update account', async () => {
      const mockData = { firstName: 'John' }
      mockAxiosInstance.put.mockResolvedValue({ data: mockData })

      await client.updateAccount(1, mockData)

      expect(mockAxiosInstance.put).toHaveBeenCalledWith('/api/v1/accounts/1', mockData)
    })

    it('should delete account', async () => {
      mockAxiosInstance.delete.mockResolvedValue({ data: 'deleted' })

      await client.deleteAccount(1)

      expect(mockAxiosInstance.delete).toHaveBeenCalledWith('/api/v1/accounts/1')
    })
  })

  describe('Policy endpoints', () => {
    it('should get all policies', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: [] })

      await client.getPolicies()

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/v1/policies')
    })

    it('should create policy', async () => {
      const mockData = { policyNumber: 'POL-001' }
      mockAxiosInstance.post.mockResolvedValue({ data: mockData })

      await client.createPolicy(1, mockData)

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/v1/policies/1', mockData)
    })

    it('should update policy', async () => {
      const mockData = { premium: 500 }
      mockAxiosInstance.put.mockResolvedValue({ data: mockData })

      await client.updatePolicy(1, mockData)

      expect(mockAxiosInstance.put).toHaveBeenCalledWith('/api/v1/policies/1', mockData)
    })

    it('should delete policy', async () => {
      mockAxiosInstance.delete.mockResolvedValue({ data: 'deleted' })

      await client.deletePolicy(1)

      expect(mockAxiosInstance.delete).toHaveBeenCalledWith('/api/v1/policies/1')
    })
  })

  describe('Claim endpoints', () => {
    it('should get all claims with pagination', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: { content: [] } })

      await client.getClaims(0, 20)

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/v1/claims?page=0&size=20')
    })

    it('should create claim', async () => {
      const mockData = { claimNumber: 'CLM-001' }
      mockAxiosInstance.post.mockResolvedValue({ data: mockData })

      await client.createClaim(1, mockData)

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/v1/claims/1', mockData)
    })

    it('should update claim', async () => {
      const mockData = { description: 'Updated' }
      mockAxiosInstance.put.mockResolvedValue({ data: mockData })

      await client.updateClaim(1, mockData)

      expect(mockAxiosInstance.put).toHaveBeenCalledWith('/api/v1/claims/1', mockData)
    })

    it('should partially update claim', async () => {
      const mockData = { claimStatus: 'APPROVED' }
      mockAxiosInstance.patch.mockResolvedValue({ data: mockData })

      await client.partialUpdateClaim(1, mockData)

      expect(mockAxiosInstance.patch).toHaveBeenCalledWith('/api/v1/claims/1', mockData)
    })

    it('should delete claim', async () => {
      mockAxiosInstance.delete.mockResolvedValue({ data: 'deleted' })

      await client.deleteClaim(1)

      expect(mockAxiosInstance.delete).toHaveBeenCalledWith('/api/v1/claims/1')
    })
  })

  describe('Line endpoints', () => {
    it('should get all lines', async () => {
      mockAxiosInstance.get.mockResolvedValue({ data: [] })

      await client.getLines()

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/v1/lines')
    })

    it('should create line', async () => {
      const mockData = { name: 'Auto' }
      mockAxiosInstance.post.mockResolvedValue({ data: mockData })

      await client.createLine(mockData)

      expect(mockAxiosInstance.post).toHaveBeenCalledWith('/api/v1/lines', mockData)
    })

    it('should update line', async () => {
      const mockData = { name: 'Auto Insurance' }
      mockAxiosInstance.put.mockResolvedValue({ data: mockData })

      await client.updateLine(1, mockData)

      expect(mockAxiosInstance.put).toHaveBeenCalledWith('/api/v1/lines/1', mockData)
    })

    it('should delete line', async () => {
      mockAxiosInstance.delete.mockResolvedValue({ data: 'deleted' })

      await client.deleteLine(1)

      expect(mockAxiosInstance.delete).toHaveBeenCalledWith('/api/v1/lines/1')
    })
  })

  describe('Coverage endpoints', () => {
    it('should calculate coverage', async () => {
      const mockResult = { calculatedCoverage: 250000 }
      mockAxiosInstance.get.mockResolvedValue({ data: mockResult })

      await client.calculateCoverage(1, 2)

      expect(mockAxiosInstance.get).toHaveBeenCalledWith('/api/v1/coverage/calculate/1/2')
    })
  })
})
