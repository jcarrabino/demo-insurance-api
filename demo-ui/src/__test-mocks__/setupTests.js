import '@testing-library/jest-dom'

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
