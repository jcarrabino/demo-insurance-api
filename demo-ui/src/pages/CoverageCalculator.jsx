import { useState } from 'react'
import { useQuery } from '@tanstack/react-query'
import { getAccounts, getLines, calculateCoverage } from '../api/client'
import { useAuth } from '../context/AuthContext'

export default function CoverageCalculator() {
  const { user } = useAuth()
  const [selectedAccount, setSelectedAccount] = useState('')
  const [selectedLine, setSelectedLine] = useState('')
  const [result, setResult] = useState(null)
  const [error, setError] = useState('')

  const { data: accounts } = useQuery({
    queryKey: ['accounts'],
    queryFn: async () => {
      if (user?.admin) {
        return (await getAccounts()).data
      }
      return []
    },
    enabled: !!user?.admin,
  })

  const { data: lines } = useQuery({
    queryKey: ['lines'],
    queryFn: async () => (await getLines()).data,
  })

  const handleCalculate = async () => {
    setError('')
    setResult(null)

    const accountId = user?.admin ? selectedAccount : user?.id

    if (!accountId || !selectedLine) {
      setError('Please select all required fields')
      return
    }

    try {
      const res = await calculateCoverage(accountId, selectedLine)
      setResult(res.data)
    } catch (err) {
      setError(err.response?.data?.message || 'Failed to calculate coverage')
    }
  }

  const getRiskColor = (level) => {
    switch (level) {
      case 'HIGH': return '#dc2626'
      case 'MEDIUM': return '#f59e0b'
      case 'LOW': return '#10b981'
      case 'VERY_LOW': return '#3b82f6'
      default: return '#6b7280'
    }
  }

  return (
    <div className="page">
      <h2>Coverage Calculator</h2>
      <p style={{ color: '#666', marginBottom: '2rem' }}>
        Calculate insurance coverage based on location and policy type
      </p>

      <div className="card" style={{ maxWidth: 600 }}>
        <h3>Calculate Coverage</h3>
        <div style={{ display: 'flex', flexDirection: 'column', gap: '1rem' }}>
          {user?.admin && (
            <div>
              <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.9rem', fontWeight: 500 }}>
                Select Account
              </label>
              <select value={selectedAccount} onChange={(e) => setSelectedAccount(e.target.value)}>
                <option value="">Choose an account...</option>
                {accounts?.map(acc => (
                  <option key={acc.id} value={acc.id}>
                    {acc.firstName} {acc.lastName} - {acc.email}
                  </option>
                ))}
              </select>
            </div>
          )}

          {!user?.admin && (
            <div style={{ padding: '0.75rem', background: '#f0f9ff', borderRadius: '6px', fontSize: '0.9rem' }}>
              Calculating for: <strong>{user?.firstName} {user?.lastName}</strong>
            </div>
          )}

          <div>
            <label style={{ display: 'block', marginBottom: '0.5rem', fontSize: '0.9rem', fontWeight: 500 }}>
              Insurance Line
            </label>
            <select value={selectedLine} onChange={(e) => setSelectedLine(e.target.value)}>
              <option value="">Choose a line...</option>
              {lines?.map(line => (
                <option key={line.id} value={line.id}>
                  {line.name} - ${line.minCoverage?.toLocaleString()} to ${line.maxCoverage?.toLocaleString()}
                </option>
              ))}
            </select>
          </div>

          {error && <p className="error">{error}</p>}

          <button className="btn" onClick={handleCalculate}>
            Calculate Coverage
          </button>
        </div>
      </div>

      {result && (
        <div className="card" style={{ maxWidth: 600, marginTop: '1.5rem', background: '#f9fafb' }}>
          <h3 style={{ marginBottom: '1rem' }}>Coverage Calculation Result</h3>
          
          <div style={{ display: 'grid', gap: '1rem' }}>
            <div>
              <div style={{ fontSize: '0.85rem', color: '#666', marginBottom: '0.25rem' }}>Account</div>
              <div style={{ fontWeight: 600 }}>{result.accountName}</div>
              <div style={{ fontSize: '0.85rem', color: '#666' }}>Zip Code: {result.zipCode}</div>
            </div>

            <div>
              <div style={{ fontSize: '0.85rem', color: '#666', marginBottom: '0.25rem' }}>Insurance Line</div>
              <div style={{ fontWeight: 600 }}>{result.lineName}</div>
              <div style={{ fontSize: '0.85rem', color: '#666' }}>
                Range: ${result.minCoverage?.toLocaleString()} - ${result.maxCoverage?.toLocaleString()}
              </div>
            </div>

            <div style={{ padding: '1rem', background: 'white', borderRadius: '6px', border: '2px solid #4f46e5' }}>
              <div style={{ fontSize: '0.85rem', color: '#666', marginBottom: '0.25rem' }}>Calculated Coverage</div>
              <div style={{ fontSize: '1.75rem', fontWeight: 700, color: '#4f46e5' }}>
                ${result.calculatedCoverage?.toLocaleString()}
              </div>
            </div>

            <div style={{ display: 'grid', gridTemplateColumns: '1fr 1fr', gap: '1rem' }}>
              <div>
                <div style={{ fontSize: '0.85rem', color: '#666', marginBottom: '0.25rem' }}>Risk Level</div>
                <div style={{ 
                  fontWeight: 600, 
                  color: getRiskColor(result.riskLevel),
                  fontSize: '1.1rem'
                }}>
                  {result.riskLevel}
                </div>
              </div>
              <div>
                <div style={{ fontSize: '0.85rem', color: '#666', marginBottom: '0.25rem' }}>Premium Adjustment</div>
                <div style={{ fontWeight: 600, fontSize: '1.1rem' }}>
                  {(result.premiumAdjustment * 100).toFixed(0)}%
                </div>
              </div>
            </div>
          </div>
        </div>
      )}
    </div>
  )
}
