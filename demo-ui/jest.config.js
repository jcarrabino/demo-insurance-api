export default {
  testEnvironment: 'jsdom',
  setupFilesAfterEnv: ['<rootDir>/src/__test-mocks__/setupTests.js'],
  moduleNameMapper: {
    '\\.(css|less|scss|sass)$': 'identity-obj-proxy',
  },
  transform: {
    '^.+\\.(js|jsx)$': ['babel-jest', { configFile: './babel.config.cjs' }],
  },
  testMatch: [
    '<rootDir>/src/__tests__/**/*.test.{js,jsx}',
  ],
  collectCoverageFrom: [
    'src/**/*.{js,jsx}',
    '!src/main.jsx',
    '!src/__tests__/**',
    '!src/__test-mocks__/**',
  ],
  coverageThreshold: {
    global: {
      branches: 45,
      functions: 45,
      lines: 60,
      statements: 50,
    },
  },
  // Performance optimizations
  maxWorkers: '50%',
  testTimeout: 10000,
  clearMocks: true,
  resetMocks: true,
  restoreMocks: true,
  // Cache configuration
  cacheDirectory: '<rootDir>/node_modules/.cache/jest',
}
