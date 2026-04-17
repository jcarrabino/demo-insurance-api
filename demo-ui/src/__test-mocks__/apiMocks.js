// Mock all API functions
export const mockApi = {
  login: jest.fn(),
  register: jest.fn(),
  getAccounts: jest.fn(),
  getAccount: jest.fn(),
  updateAccount: jest.fn(),
  deleteAccount: jest.fn(),
  getPolicies: jest.fn(),
  getPolicy: jest.fn(),
  createPolicy: jest.fn(),
  updatePolicy: jest.fn(),
  deletePolicy: jest.fn(),
  getClaims: jest.fn(),
  getClaim: jest.fn(),
  createClaim: jest.fn(),
  updateClaim: jest.fn(),
  deleteClaim: jest.fn(),
  getLines: jest.fn(),
  getLine: jest.fn(),
  createLine: jest.fn(),
  updateLine: jest.fn(),
  deleteLine: jest.fn(),
  calculateCoverage: jest.fn(),
}

// Reset all mocks
export const resetApiMocks = () => {
  Object.values(mockApi).forEach(mock => mock.mockReset())
}

// Set default resolved values
export const setDefaultApiMocks = () => {
  mockApi.getAccounts.mockResolvedValue({ data: [] })
  mockApi.getPolicies.mockResolvedValue({ data: [] })
  mockApi.getClaims.mockResolvedValue({ data: [] })
  mockApi.getLines.mockResolvedValue({ data: [] })
}
