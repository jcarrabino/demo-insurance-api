import { useEffect, useState } from 'react'
import { getClaims, createClaim, deleteClaim, getPolicies } from '../api/client'

const STATUSES = ['SUBMITTED', 'IN_PROGRESS', 'APPROVED', 'DENIED']
const empty = { claimNumber: '', description: '', claimDate: '', claimStatus: 'SUBMITTED', policyId: '' }

export default function Claims() {
  const [claims, setClaims] = useState([])
  const [policies, setPolicies] = useState([])
  const [form, setForm] = useState(empty)
  const [error, setError] = useState('')
  const [success, setSuccess] = useState('')
  const [showForm, setShowForm] = useState(false)

  const load = async () => {
    try {
      const [c, p] = await Promise.all([getClaims(), getPolicies()])
      setClaims(c.data); setPolicies(p.data)
    } catch { setError('Failed to load data') }
  }

  useEffect(() => { load() }, [])

  const set = (f) => (e) => setForm(p => ({ ...p, [f]: e.target.value }))

  const handleCreate = async (e) => {
    e.preventDefault(); setError(''); setSuccess('')
    const { policyId, ...claimData } = form
    try {
      await createClaim(policyId, claimData)
      setSuccess('Claim submitted'); setForm(empty); setShowForm(false); load()
    } catch (err) {
      const data = err.response?.data
      setError(typeof data === 'object' ? Object.values(data).join(', ') : data?.Massege || 'Failed')
    }
  }

  const handleDelete = async (id) => {
    if (!confirm('Delete this claim?')) return
    try { await deleteClaim(id); setClaims(c => c.filter(x => x.id !== id)) }
    catch { setError('Failed to delete') }
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
            <select value={form.policyId} onChange={set('policyId')} required>
              <option value="">Select Policy</option>
              {policies.map(p => <option key={p.id} value={p.id}>{p.policyNumber} — {p.policyType}</option>)}
            </select>
            <input placeholder="Claim Number" value={form.claimNumber} onChange={set('claimNumber')} required />
            <input placeholder="Description" value={form.description} onChange={set('description')} required />
            <label style={{ fontSize: '0.85rem', color: '#555' }}>Claim Date</label>
            <input type="date" value={form.claimDate} onChange={set('claimDate')} required />
            <select value={form.claimStatus} onChange={set('claimStatus')}>
              {STATUSES.map(s => <option key={s} value={s}>{s}</option>)}
            </select>
            {error && <p className="error">{error}</p>}
            {success && <p className="success">{success}</p>}
            <button className="btn" type="submit">Submit Claim</button>
          </form>
        </div>
      )}

      {error && !showForm && <p className="error">{error}</p>}
      <div className="grid">
        {claims.map(c => (
          <div className="card" key={c.id}>
            <div style={{ display: 'flex', justifyContent: 'space-between', alignItems: 'flex-start' }}>
              <h3>{c.claimNumber}</h3>
              <span className={`badge ${c.claimStatus}`}>{c.claimStatus}</span>
            </div>
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
