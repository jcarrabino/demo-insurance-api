import { useEffect, useState } from 'react'
import { getPolicies, createPolicy, deletePolicy, getAccounts, getLines } from '../api/client'
import { useAuth } from '../context/AuthContext'

const empty = {
  lineId: '', premium: '', startDate: '', endDate: ''
}

export default function Policies() {
  const { user } = useAuth()
  const [policies, setPolicies] = useState([])
  const [accounts, setAccounts] = useState([])
  const [lines, setLines] = useState([])
  const [form, setForm] = useState(empty)
  const [selectedAccountId, setSelectedAccountId] = useState('')
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [showForm, setShowForm] = useState(false)

  const load = async () => {
    try {
      const policiesData = await getPolicies()
      setPolicies(policiesData.data)
      
      // Load lines for policy creation
      const linesData = await getLines()
      setLines(linesData.data)
      
      // If admin, load all accounts for selection
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

  const handleCreate = async (e) => {
    e.preventDefault(); setError(''); setSuccess('')
    
    // Determine which account to create policy for
    const accountId = user?.admin && selectedAccountId ? selectedAccountId : user?.id
    
    if (!accountId) {
      setError('Please select an account')
      return
    }
    
    try {
      await createPolicy(accountId, form)
      setSuccess('Policy created'); setForm(empty); setSelectedAccountId(''); setShowForm(false); load()
    } catch (err) {
      const data = err.response?.data
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.message || data?.Massege || 'Failed')
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this policy?')) return
    try { 
      await deletePolicy(id)
      setPolicies(p => p.filter(x => x.id !== id))
      setSuccess('Policy deleted')
      setTimeout(() => setSuccess(''), 3000)
    } catch { 
      setError('Failed to delete') 
    }
  }

  return (
    <div className="page">
      <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'center', marginBottom: '1.5rem' }}>
        <h2 style={{ margin: 0 }}>Policies</h2>
        <button className="btn" onClick={() => setShowForm(s => !s)}>{showForm ? 'Cancel' : '+ New Policy'}</button>
      </div>

      {showForm && (
        <div className="card" style={{ marginBottom: '1.5rem' }}>
          <h3>New Policy</h3>
          <form onSubmit={handleCreate}>
            {user?.admin && (
              <>
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Account</label>
                <select value={selectedAccountId} onChange={(e) => setSelectedAccountId(e.target.value)} required>
                  <option value="">Select Account</option>
                  {accounts.map(a => (
                    <option key={a.id} value={a.id}>
                      {a.firstName} {a.lastName} ({a.email})
                    </option>
                  ))}
                </select>
              </>
            )}
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Insurance Line</label>
            <select value={form.lineId} onChange={set('lineId')} required>
              <option value="">Select Line</option>
              {lines.map(l => (
                <option key={l.id} value={l.id}>
                  {l.name} (${l.minCoverage.toLocaleString()} - ${l.maxCoverage.toLocaleString()})
                </option>
              ))}
            </select>
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Premium</label>
            <input type="number" step="0.01" placeholder="Premium Amount" value={form.premium} onChange={set('premium')} required />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Start Date</label>
            <input type="date" value={form.startDate} onChange={set('startDate')} required />
            <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>End Date</label>
            <input type="date" value={form.endDate} onChange={set('endDate')} required />
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
            <button className="btn" type="submit">Create Policy</button>
          </form>
        </div>
      )}

      {error && !showForm && <p className="error">{error}</p>}
      {success && !showForm && <p className="success">{success}</p>}
      
      <div className="grid">
        {policies.map(p => (
          <div className="card" key={p.id}>
            <h3>{p.line?.name || 'Policy'}</h3>
            {user?.admin && p.account && (
              <p style={{ fontSize: '0.85rem', color: '#666', marginBottom: '0.5rem' }}>
                <strong>Account:</strong> {p.account.firstName} {p.account.lastName}
              </p>
            )}
            <p><strong>Premium:</strong> ${p.premium?.toLocaleString()}</p>
            <p><strong>Period:</strong> {p.startDate} → {p.endDate}</p>
            <div className="actions">
              <button className="btn danger sm" onClick={() => handleDelete(p.id)}>Delete</button>
            </div>
          </div>
        ))}
        {policies.length === 0 && <p style={{ color: '#888' }}>No policies found.</p>}
      </div>
    </div>
  )
}
