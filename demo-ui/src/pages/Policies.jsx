import { useEffect, useState } from 'react'
import { getPolicies, createPolicy, updatePolicy, deletePolicy, getAccounts, getLines } from '../api/client'
import { useAuth } from '../context/AuthContext'
import Spinner from '../components/Spinner'

const empty = {
  lineId: '', premium: '', startDate: '', endDate: ''
}

const emptyEdit = {
  id: null, accountId: null, lineId: '', premium: '', startDate: '', endDate: ''
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
  const [editingPolicy, setEditingPolicy] = useState(null)
  const [editForm, setEditForm] = useState(emptyEdit)
  const [isLoading, setIsLoading] = useState(true)

  const load = async () => {
    setIsLoading(true)
    try {
      const policiesData = await getPolicies()
      setPolicies(Array.isArray(policiesData.data) ? policiesData.data : [])
      
      // Load lines for policy creation
      const linesData = await getLines()
      setLines(Array.isArray(linesData.data) ? linesData.data : [])
      
      // If admin, load all accounts for selection
      if (user?.admin) {
        const accountsData = await getAccounts()
        setAccounts(Array.isArray(accountsData.data) ? accountsData.data : [])
      }
    } catch (err) { 
      console.error('Failed to load data:', err)
      setError('Failed to load data')
      // Ensure arrays are initialized even on error
      setPolicies([])
      setLines([])
      setAccounts([])
    } finally {
      setIsLoading(false)
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
      // Include accountId in the form data for validation
      const formData = { ...form, accountId: parseInt(accountId) }
      await createPolicy(accountId, formData)
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

  const startEdit = (policy) => {
    setEditingPolicy(policy.id)
    setEditForm({
      id: policy.id,
      accountId: policy.accountId, // Include accountId for PUT request
      lineId: policy.lineId || '',
      premium: policy.premium || '',
      startDate: policy.startDate || '',
      endDate: policy.endDate || ''
    })
    setError('')
    setSuccess('')
  }

  const cancelEdit = () => {
    setEditingPolicy(null)
    setEditForm(emptyEdit)
  }

  const setEdit = (f) => (e) => setEditForm(p => ({ ...p, [f]: e.target.value }))

  const handleUpdate = async (e) => {
    e.preventDefault(); setError(''); setSuccess('')
    try {
      const { id, ...updateData } = editForm
      // Only send fields that are provided
      const partialData = {}
      if (updateData.lineId) partialData.lineId = updateData.lineId
      if (updateData.premium) partialData.premium = updateData.premium
      if (updateData.startDate) partialData.startDate = updateData.startDate
      if (updateData.endDate) partialData.endDate = updateData.endDate
      
      await updatePolicy(id, partialData)
      setSuccess('Policy updated')
      setEditingPolicy(null)
      setEditForm(emptyEdit)
      load()
      setTimeout(() => setSuccess(''), 3000)
    } catch (err) {
      const data = err.response?.data
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.message || data?.Massege || 'Failed to update')
    }
  }

  // Check if user can edit a policy
  const canEdit = (policy) => {
    if (user?.admin) return true
    return policy.accountId === user?.id
  }

  // Check if user can delete a policy (admin only)
  const canDelete = () => {
    return user?.admin
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
      
      {isLoading ? (
        <Spinner message="Loading policies..." />
      ) : (
        <div className="grid">
          {policies.map(p => (
          <div className="card" key={p.id}>
            {editingPolicy === p.id ? (
              <form onSubmit={handleUpdate}>
                <h3>Edit Policy</h3>
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Policy Number (Read-only)</label>
                <input 
                  value={`POL-${String(p.id).padStart(5, '0')}`}
                  disabled
                  style={{ background: '#f5f5f5', cursor: 'not-allowed' }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Account (Read-only)</label>
                <input 
                  value={p.account ? `${p.account.firstName} ${p.account.lastName} (${p.account.email})` : `Account ID: ${p.accountId}`}
                  disabled
                  style={{ background: '#f5f5f5', cursor: 'not-allowed' }}
                />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Insurance Line *</label>
                <select value={editForm.lineId} onChange={setEdit('lineId')} required>
                  <option value="">Select Line</option>
                  {lines.map(l => (
                    <option key={l.id} value={l.id}>
                      {l.name} (${l.minCoverage.toLocaleString()} - ${l.maxCoverage.toLocaleString()})
                    </option>
                  ))}
                </select>
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Premium *</label>
                <input type="number" step="0.01" value={editForm.premium} onChange={setEdit('premium')} required />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>Start Date *</label>
                <input type="date" value={editForm.startDate} onChange={setEdit('startDate')} required />
                <label style={{ fontSize: '0.85rem', color: '#555', fontWeight: 'bold' }}>End Date *</label>
                <input type="date" value={editForm.endDate} onChange={setEdit('endDate')} required />
                <div className="actions">
                  <button className="btn sm" type="submit">Save</button>
                  <button className="btn sm" type="button" onClick={cancelEdit}>Cancel</button>
                </div>
              </form>
            ) : (
              <>
                <h3>POL-{String(p.id).padStart(5, '0')}</h3>
                <p><strong>Line ID:</strong> {p.lineId || 'N/A'}</p>
                {user?.admin && p.account && (
                  <p style={{ fontSize: '0.85rem', color: '#666', marginBottom: '0.5rem' }}>
                    <strong>Account:</strong> {p.account.firstName} {p.account.lastName}
                  </p>
                )}
                <p><strong>Premium:</strong> ${p.premium?.toLocaleString()}</p>
                <p><strong>Period:</strong> {p.startDate} → {p.endDate}</p>
                <div className="actions">
                  {canEdit(p) && (
                    <button className="btn sm" onClick={() => startEdit(p)}>Edit</button>
                  )}
                  {canDelete() && (
                    <button className="btn danger sm" onClick={() => handleDelete(p.id)}>Delete</button>
                  )}
                </div>
              </>
            )}
          </div>
        ))}
        {policies.length === 0 && <p style={{ color: '#888' }}>No policies found.</p>}
      </div>
      )}
    </div>
  )
}
