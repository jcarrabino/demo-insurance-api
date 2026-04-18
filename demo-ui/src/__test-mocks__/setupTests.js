import '@testing-library/jest-dom'

// Mock the API client BEFORE any imports
jest.mock('../api/client', () => ({
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
}))

// Mock window.confirm
global.confirm = jest.fn(() => true)

// Mock localStorage
const localStorageMock = {
  getItem: jest.fn(),
  setItem: jest.fn(),
  removeItem: jest.fn(),
  clear: jest.fn(),
}
global.localStorage = localStorageMock
