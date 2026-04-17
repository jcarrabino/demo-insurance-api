import { useEffect, useState } from 'react'
import { getClaims, createClaim, deleteClaim, getPolicies, getAccounts } from '../api/client'
import { useAuth } from '../context/AuthContext'

const STATUSES = ['SUBMITTED', 'IN_PROGRESS', 'APPROVED', 'DENIED']
const empty = { claimNumber: '', description: '', claimDate: '', claimStatus: 'SUBMITTED', policyId: '' }

export default function Claims() {
  const { user } = useAuth()
  const [claims, setClaims] = useState([])
  const [policies, setPolicies] = useState([])
  const [accounts, setAccounts] = useState([])
  const [form, setForm] = useState(empty)
  const [selectedAccountId, setSelectedAccountId] = useState('')
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [showForm, setShowForm] = useState(false)

  const load = async () => {
    try {
      const [c, p] = await Promise.all([getClaims(), getPolicies()])
      setClaims(c.data)
      setPolicies(p.data)
      
      // If admin, load all accounts for filtering
      if (user?.admin) {
        const accountsData = await getAccounts()
        setAccounts(accountsData.data)
      }
    } catch { 
      setError('Failed to load data') 
    }
  }

  useEffect(() => { load() }, [user])

  const set = (f) => (e) => setForm(p => ({ ...p, [f]: e.target.value }))

  // Filter policies based on selected account (for admins)
  const filteredPolicies = user?.admin && selectedAccountId
    ? policies.filter(p => p.account?.id === parseInt(selectedAccountId))
    : policies

  const handleCreate = async (e) => {
    e.preventDefault(); setError(''); setSuccess('')
    const { policyId, ...claimData } = form
    try {
      await createClaim(policyId, claimData)
      setSuccess('Claim submitted'); setForm(empty); setSelectedAccountId(''); setShowForm(false); load()
    } catch (err) {
      const data = err.response?.data
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.message || data?.Massege || 'Failed')
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this claim?')) return
    try { 
      await deleteClaim(id)
      setClaims(c => c.filter(x => x.id !== id))
      setSuccess('Claim deleted')
      setTimeout(() => setSuccess(''), 3000)
    } catch { 
      setError('Failed to delete') 
    }
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2 style={{ margin: 0 }}>Claims</h2>
        <button className="btn" onClick={() => setShowForm(s => !s)}>{showForm ? 'Cancel' : '+ New Claim'}</button>
      </div>

      {showForm && (
        <div className="card" style={{ marginBottom: '1.5rem' }}>
          <h3>New Claim</h3>
          <form onSubmit={handleCreate}>
            {user?.admin && (
              <>
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Account (Optional Filter)</label>
                <select value={selectedAccountId} onChange={(e) => setSelectedAccountId(e.target.value)}>
                  <option value="">All Accounts</option>
                  {accounts.map(a => (
                    <option key={a.id} value={a.id}>
                      {a.firstName} {a.lastName} ({a.email})
                    </option>
                  ))}
                </select>
              </>
            )}
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Policy</label>
            <select value={form.policyId} onChange={set('policyId')} required>
              <option value="">Select Policy</option>
              {filteredPolicies.map(p => (
                <option key={p.id} value={p.id}>
                  {p.line?.name || 'Policy'} - ${p.premium}
                  {user?.admin && p.account && ` (${p.account.firstName} ${p.account.lastName})`}
                </option>
              ))}
            </select>
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Claim Number</label>
            <input placeholder="Claim Number" value={form.claimNumber} onChange={set('claimNumber')} required />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Description</label>
            <textarea 
              placeholder="Describe the claim" 
              value={form.description} 
              onChange={set('description')} 
              rows="3"
              required 
            />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Claim Date</label>
            <input type="date" value={form.claimDate} onChange={set('claimDate')} required />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Status</label>
            <select value={form.claimStatus} onChange={set('claimStatus')}>
              {STATUSES.map(s => <option key={s} value={s}>{s.replace('_', ' ')}</option>)}
            </select>
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
            <button className="btn" type="submit">Submit Claim</button>
          </form>
        </div>
      )}

      {error && !showForm && <p className="error">{error}</p>}
      {success && !showForm && <p className="success">{success}</p>}
      
      <div className="grid">
        {claims.map(c => (
          <div className="card" key={c.id}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <h3>{c.claimNumber}</h3>
              <span className={`badge ${c.claimStatus}`}>{c.claimStatus?.replace('_', ' ')}</span>
            </div>
            {user?.admin && c.policy?.account && (
              <p style={{ fontSize: '0.85rem', color: '#666', marginBottom: '0.5rem' }}>
                <strong>Account:</strong> {c.policy.account.firstName} {c.policy.account.lastName}
              </p>
            )}
            {c.policy && (
              <p style={{ fontSize: '0.85rem', color: '#666' }}>
                <strong>Policy:</strong> {c.policy.line?.name || 'Policy'}
              </p>
            )}
            <p>{c.description}</p>
            <p style={{ fontSize: '0.8rem', color: '#888' }}>{c.claimDate}</p>
            <div className="actions">
              <button className="btn danger sm" onClick={() => handleDelete(c.id)}>Delete</button>
            </div>
          </div>
        ))}
        {claims.length === 0 && <p style={{ color: '#888' }}>No claims found.</p>}
      </div>
    </div>
  )
}
